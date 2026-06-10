package com.politecnicomalaga.clinica.controller;



import com.google.gson.Gson;
import com.politecnicomalaga.clinica.dataservice.BBDDAccess;
import com.politecnicomalaga.clinica.model.DatosImportacion;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Controlador implements DataAccess{

    private BBDDAccess miBBDD;

    public Controlador() {
        miBBDD = new BBDDAccess();
    }

    //Implementar lógica definida en el interfaz DataAccess para que los Servlets soliciten lo que quieran

    // public String listAllMedicamentos(); //-> Accede a la BBDD y obtiene todos los medicamentos disponibles
    @Override
    public String listAllMedicamentos(){
        try{
            List<Map<String, Object>> lista = miBBDD.listarTodosMedicamentos();
            return new Gson().toJson(lista);
        } catch (Exception e){
            return "{\"error\": \"List Medicamentos: " + e.getMessage() + "\"}";
        }
    }
    // public String findMedicamentoXCodigo(String code); //-> Accede a la BBDD y busca el medicamento que coindice con codigo=code
    @Override
    public String findMedicamentoXCodigo(String code){
        try {
            Map<String, Object> med = miBBDD.buscarMedicamentoXCodigo(code);
            if (med == null) return "{\"error\": \"Medicamento no encontrado\"}";
            return new Gson().toJson(med);
        } catch (Exception e) {
            return "{\"error\": \"Find Medicamento: " + e.getMessage() + "\"}";
        }
    }
    // public String findPacienteXDNI(String dni); //-> Accede a la BBDD y busca el cliente con el DNI=dni
    @Override
    public String findPacienteXDNI(String dni){
        try {
            List<Map<String, Object>> lista = miBBDD.buscarPacienteXDNI(dni);
            if (lista.isEmpty()) return "{\"error\": \"Paciente no encontrado\"}";
            return new Gson().toJson(lista);
        } catch (Exception e) {
            return "{\"error\": \"Find Paciente: " + e.getMessage() + "\"}";
        }
    }
    // public String listMedicamentosXTratamiento(String dni, String tratamiendo_id); //-> Accede a la BBDD y busca todos los medicamenntos asociados en el id_tratamiento=tratamiento y para el paciente DNI=dni
    @Override
    public String listMedicamentosXTratamiento(String dni, String tratamiento){
        try {
            List<Map<String, Object>> lista = miBBDD.listarMedicamentosXTratamiento(dni, tratamiento);
            return new Gson().toJson(lista);
        } catch (Exception e){
            return "{\"error\": \"Find Tratamiento: " + e.getMessage() + "\"}";
        }
    }


    // public String importData(String jsonDataFromCSV); //-> parsea el json con los datos del CSV leído en el programa de importación y realiza los INSERT adecuados para actualizar la BBDD con la info almacenada en ese JSON
    @Override
    public String importData(String jsonDataFromCSV){
        try {
            DatosImportacion datos = new Gson().fromJson(jsonDataFromCSV, DatosImportacion.class);
            (new BBDDAccess()).procesarImportacion(datos);

            return "{\"resultado\": \"ok\"}";
        } catch (Exception e) {
            System.err.println("ERROR EN LA IMPORTACIÓN: "+e.getMessage());
            e.printStackTrace();
            return "{\"resultado\": \"error\", \"detalle\": \"" + e.getMessage() + "\"}";
        }
    }

    @Override
    public String listAllPacientes(){
        try {
            List<Map<String, Object>> lista = miBBDD.listarTodosPacientes();
            return new Gson().toJson(lista);
        } catch (Exception e) {
            return "{\\\"error\\\": \\\"List Pacientes: \" + e.getMessage() + \"\\\"}";
        }
    }


}