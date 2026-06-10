package com.politecnicomalaga.clinica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.politecnicomalaga.clinica.controller.Controlador;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Reaccionable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Controlador.getSingleton(this).pedirMedicamentos();
    }
    public void onClickPacientes(View v){
        Controlador.getSingleton(this).pedirPacientes();
    }

    public void onClickMedicamentos(View v){
        Controlador.getSingleton(this).pedirMedicamentos();
    }
    public void onClickBuscarCliente(View v){
        Intent intent = new Intent(this, BuscarPacienteActivity.class);
        startActivity(intent);
    }

    public void reaccionar(String error) {
        ListView miListaEnPantalla = findViewById(R.id.listaResultados);
        ArrayAdapter<String> miAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        if (error.isEmpty()){
            List<String> datos = Controlador.getSingleton(this).getDatosPantalla();
            miAdapter.addAll(datos);
        } else {
            miAdapter.add(error);
        }
        runOnUiThread(() -> miListaEnPantalla.setAdapter(miAdapter));
    }
}