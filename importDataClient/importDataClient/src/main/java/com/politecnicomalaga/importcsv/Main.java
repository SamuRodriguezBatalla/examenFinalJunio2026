package com.politecnicomalaga.importcsv;

import com.politecnicomalaga.importcsv.model.*;
import com.google.gson.Gson;
import okhttp3.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatosImportacion datos = new DatosImportacion();

        System.out.println("Leyendo el archivo data.csv...");
        try{
            File archivo = new File("data.csv");
            Scanner lector = new Scanner(archivo);

            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();

                if (linea.isEmpty()) continue;
                String[] partes = linea.split(";");

                if (partes[0].equals("Paciente")){
                    Paciente p = new Paciente();
                    p.setDni(partes[1]);
                    p.setNombre(partes[2]);
                    p.setApellidos(partes[3]);
                    p.setEmail(partes[4]);
                    p.setTelefono(partes[5]);
                    p.setDireccion(partes[6]);
                    datos.getPacientes().add(p);
                } else if (partes[0].equals("Tratamiento")) {
                    Tratamiento t = new Tratamiento();
                    t.setId_tratamiento(Integer.parseInt(partes[1]));
                    t.setDni_paciente(partes[2]);
                    t.setFecha_inicio(partes[3]);
                    t.setDias_tratamiento(Integer.parseInt(partes[4]));
                    t.setDiagnostico(partes[5]);
                    datos.getTratamientos().add(t);
                } else if (partes[0].equals("Medicamento")){
                    Medicamento m = new Medicamento();
                    m.setCodigo(partes[1]);
                    m.setNombre(partes[2]);
                    m.setPosologia(partes[3]);
                    datos.getMedicamentos().add(m);
                } else if (partes[0].equals("MedicamentoXTratamiento")){
                    MedicamentoTratamiento mt = new MedicamentoTratamiento();
                    mt.setId_tratamiento(Integer.parseInt(partes[1]));
                    mt.setCodigo_medicamento(partes[2]);
                    datos.getMedicamentoTratamientoList().add(mt);
                }
            }
            lector.close();
            System.out.println("Lectura completada. Enviando al servidor...");
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encuentra el archivo: "+e.getMessage());
            return;
        }
        // Convertir a JSON con GSON
        Gson gson = new Gson();
        String json = gson.toJson(datos);

        // Enviar al servidor
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("http://localhost:8888/importar")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("RESPUESTA DEL SERVIDOR: "+response.body().string());
        } catch (IOException e) {
            System.out.println("ERROR DE CONEXIÓN: "+e.getMessage());
        }

    }
}