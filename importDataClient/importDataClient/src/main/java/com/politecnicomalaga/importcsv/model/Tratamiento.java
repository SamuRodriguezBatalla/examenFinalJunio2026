package com.politecnicomalaga.importcsv.model;

public class Tratamiento {
    private int id_tratamiento;
    private String dni_paciente;
    private String fecha_inicio;
    private int dias_tratamiento;
    private String diagnostico;

    public int getId_tratamiento() {
        return id_tratamiento;
    }

    public void setId_tratamiento(int id_tratamiento) {
        this.id_tratamiento = id_tratamiento;
    }

    public String getDni_paciente() {
        return dni_paciente;
    }

    public void setDni_paciente(String dni_paciente) {
        this.dni_paciente = dni_paciente;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public int getDias_tratamiento() {
        return dias_tratamiento;
    }

    public void setDias_tratamiento(int dias_tratamiento) {
        this.dias_tratamiento = dias_tratamiento;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
