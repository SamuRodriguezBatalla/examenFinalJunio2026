package com.politecnicomalaga.clinica.dataservice;

import com.politecnicomalaga.clinica.model.Paciente;

import java.util.List;

public interface DataService {
    public boolean addProducto(Paciente p);
    public List<Paciente> listAll();
    // y pesha más...
}
