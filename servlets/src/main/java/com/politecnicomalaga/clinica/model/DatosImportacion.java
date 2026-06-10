package com.politecnicomalaga.clinica.model;

import java.util.ArrayList;
import java.util.List;

public class DatosImportacion {
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medicamento> medicamentos = new ArrayList<>();
    private List<Tratamiento> tratamientos = new ArrayList<>();
    private List<MedicamentoTratamiento> medicamentoTratamientoList = new ArrayList<>();

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<Tratamiento> getTratamientos() {
        return tratamientos;
    }

    public void setTratamientos(List<Tratamiento> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public List<MedicamentoTratamiento> getMedicamentoTratamientoList() {
        return medicamentoTratamientoList;
    }

    public void setMedicamentoTratamientoList(List<MedicamentoTratamiento> medicamentoTratamientoList) {
        this.medicamentoTratamientoList = medicamentoTratamientoList;
    }
}