package com.politecnicomalaga.clinica.dataservice;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.politecnicomalaga.clinica.controller.Controlador;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BBDDAccess {
    private final Controlador c;
    public BBDDAccess(Controlador c) {
        this.c = c;
    }
    // Nuevo metodo generico, para no tener que modificar la clase en un futuro

    public void peticionGet(String urlDestino){
        OkHttpClient clienteHTTP = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlDestino)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Call llamada = clienteHTTP.newCall(request);

        llamada.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                final String respuestaError = "Fallo de conexión: "+e.getMessage();
                Handler manejador = new Handler(Looper.getMainLooper());
                manejador.post(() -> c.setData(respuestaError,true));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String respuesta = response.body().string();
                Handler manejador = new Handler(Looper.getMainLooper());
                manejador.post(() -> c.setData(respuesta,false));
            }
        });
    }
}