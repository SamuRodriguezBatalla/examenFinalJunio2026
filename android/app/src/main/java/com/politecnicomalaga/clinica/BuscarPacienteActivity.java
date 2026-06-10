package com.politecnicomalaga.clinica;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.politecnicomalaga.clinica.controller.Controlador;

import java.util.List;

public class BuscarPacienteActivity extends AppCompatActivity implements Reaccionable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_paciente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickBuscar(View v) {
        Controlador.getSingleton(this).setPantalla(this);
        EditText et = findViewById(R.id.editTextDni);
        String dni = et.getText().toString().trim();
        if (!dni.isEmpty()) {
            Controlador.getSingleton(this).pedirPacienteXDNI(dni);
        }
    }
    public void onClickVolver(View v){
        finish();
    }

    @Override
    public void reaccionar(String error) {
        ListView lista = findViewById(R.id.listaResultados);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        if (error.isEmpty()) {
            List<String> datos = Controlador.getSingleton(this).getDatosPantalla();
            adapter.addAll(datos);
        } else {
            adapter.add(error);
        }
        runOnUiThread(() -> lista.setAdapter(adapter));
    }
}
