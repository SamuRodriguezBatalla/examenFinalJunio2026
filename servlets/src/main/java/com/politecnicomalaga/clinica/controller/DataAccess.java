package com.politecnicomalaga.clinica.controller;

public interface DataAccess {
    public String listAllMedicamentos(); //-> Accede a la BBDD y obtiene todos los medicamentos disponibles
    public String findMedicamentoXCodigo(String code); //-> Accede a la BBDD y busca el medicamento que coindice con codigo=code
    public String findPacienteXDNI(String dni); //-> Accede a la BBDD y busca el cliente con el DNI=dni
    public String listMedicamentosXTratamiento(String dni, String tratamiendo_id); //-> Accede a la BBDD y busca todos los medicamenntos asociados en el id_tratamiento=tratamiento y para el paciente DNI=dni
    public String importData(String jsonDataFromCSV); //-> parsea el json con los datos del CSV leído en el programa de importación y realiza los INSERT adecuados para actualizar la BBDD con la info almacenada en ese JSON
    public String listAllPacientes();
}
