package com.politecnicomalaga.clinica.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.politecnicomalaga.clinica.Reaccionable;
import com.politecnicomalaga.clinica.dataservice.BBDDAccess;
import com.politecnicomalaga.clinica.model.*;

import java.lang.reflect.Type;
import java.util.*;


public class Controlador {
    // instance variables
    private Reaccionable miPantalla;
    private static Controlador singleton;

    public static final int MODO_MEDICAMENTO = 1;
    public static final int MODO_PACIENTES = 2;
    public static final int MODO_PACIENTES_BUSQUEDA = 3;
    private int modoActual;

    private List<Medicamento> listaMedicamentos;
    private List<Paciente> listaPacientes;
    private List<Paciente> listaPacienteBusqueda;

    private Controlador(Reaccionable miPantalla) {
        this.miPantalla = miPantalla;
        this.listaMedicamentos = new ArrayList<>();
        this.listaPacientes = new ArrayList<>();
        this.listaPacienteBusqueda = new ArrayList<>();
        this.modoActual = MODO_MEDICAMENTO;
    }
    public static Controlador getSingleton(Reaccionable miPantalla) {
        // put your code here
        if (singleton == null){
            singleton = new Controlador(miPantalla);
        } else if (miPantalla != null) {
            singleton.setPantalla(miPantalla);
        }
        return singleton;
    }

    public void pedirMedicamentos(){
        modoActual = MODO_MEDICAMENTO;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/listarMedicamentos");
    }

    public void pedirPacientes(){
        modoActual = MODO_PACIENTES;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/listarPacientes");
    }

    public void pedirPacienteXDNI(String dni){
        modoActual = MODO_PACIENTES_BUSQUEDA;
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.peticionGet("http://10.0.2.2:8888/buscarPaciente?dni="+dni);
    }


    public List<String> getDatosPantalla() {

        List<String> resultado = new ArrayList<>();
        if (modoActual == MODO_MEDICAMENTO){
            for (Medicamento m: listaMedicamentos){
                resultado.add(m.getCodigo()+" - "+m.getNombre()+" - "+m.getPosologia());
            }
        } else if (modoActual == MODO_PACIENTES){
            for (Paciente c: listaPacientes){
                resultado.add(c.getDni()+" - "+c.getNombre()+" - "+c.getApellidos()+" - "+c.getEmail()+" - "+c.getTelefono()+" - "+c.getDireccion());
            }
        } else if (modoActual == MODO_PACIENTES_BUSQUEDA) {
            for (Paciente c : listaPacienteBusqueda) {
                resultado.add(c.getDni()+" - "+c.getNombre()+" - "+c.getApellidos()+" - "+c.getEmail()+" - "+c.getTelefono()+" - "+c.getDireccion());
            }
        }
        return resultado;
    }

    //Este método es llamado por OKhttp cuando se produce la respuesta a la
    // petición de datos a nuestro backend
    public void setData(String jsonData, boolean error) {
        if (error){
            miPantalla.reaccionar("Error de conexión con Tomcat.");
            return;
        }
        try {
            Gson gson = new Gson();
            Type tipoLista;
            if (modoActual == MODO_MEDICAMENTO) {
                tipoLista = new TypeToken<List<Medicamento>>(){}.getType();
                listaMedicamentos = gson.fromJson(jsonData, tipoLista);
            } else if (modoActual == MODO_PACIENTES) {
                tipoLista = new TypeToken<List<Paciente>>(){}.getType();
                listaPacientes = gson.fromJson(jsonData, tipoLista);
            } else if (modoActual == MODO_PACIENTES_BUSQUEDA) {
                if (jsonData.contains("\"error\"")){
                    miPantalla.reaccionar("No se han encontrado resultados...");
                    return;
                } else {
                    tipoLista = new TypeToken<List<Paciente>>(){}.getType();
                    listaPacienteBusqueda = gson.fromJson(jsonData, tipoLista);
                }
            }
            // Decimos al MainActivity que ya puede pintar la pantalla
            miPantalla.reaccionar("");
        } catch (JsonSyntaxException e) {
            miPantalla.reaccionar("Error al parsear el JSON.");
        }
    }

    public void setPantalla(Reaccionable pantalla){
        this.miPantalla = pantalla;
    }

}