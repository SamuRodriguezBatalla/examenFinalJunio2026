package com.politecnicomalaga.clinica.dataservice;

import com.politecnicomalaga.clinica.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BBDDAccess {

    //Aquí los métodos necesarios para CRUD de datos en la BBDD

    // public String listAllMedicamentos(); //-> Accede a la BBDD y obtiene todos los medicamentos disponibles
    public List<Map<String, Object>> listarTodosMedicamentos() throws SQLException, ClassNotFoundException {
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();

        String sql = "SELECT codigo, nombre, posologia FROM medicamentos";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Map<String, Object> medi = new HashMap<>();
            medi.put("codigo", rs.getString("codigo"));
            medi.put("nombre", rs.getString("nombre"));
            medi.put("posologia", rs.getString("posologia"));
            lista.add(medi);
        }
        rs.close();
        stmt.close();
        conn.close();
        return lista;
    }

    // public String findMedicamentoXCodigo(String code); //-> Accede a la BBDD y busca el medicamento que coindice con codigo=code
    public Map<String, Object> buscarMedicamentoXCodigo(String codigo) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        Map<String, Object> medi = null;
        String sql = "SELECT codigo, nombre, posologia from medicamentos WHERE codigo = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,codigo);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            medi = new HashMap<>();
            medi.put("codigo", rs.getString("codigo"));
            medi.put("nombre", rs.getString("nombre"));
            medi.put("posologia", rs.getString("posologia"));
        }

        rs.close(); pstmt.close(); conn.close();
        return medi;
    }

    // public String findPacienteXDNI(String dni); //-> Accede a la BBDD y busca el cliente con el DNI=dni
    public List<Map<String, Object>> buscarPacienteXDNI(String dni) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();

        String sql = "SELECT dni, nombre, apellidos, email, telefono, direccion from pacientes where dni LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,"%"+ dni+"%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> paciente = new HashMap<>();
            paciente.put("dni", rs.getString("dni"));
            paciente.put("nombre", rs.getString("nombre"));
            paciente.put("apellidos", rs.getString("apellidos"));
            paciente.put("email", rs.getString("email"));
            paciente.put("telefono", rs.getString("telefono"));
            paciente.put("direccion", rs.getString("direccion"));
            lista.add(paciente);
        }
        rs.close(); pstmt.close(); conn.close();
        return lista;
    }

    // public String listMedicamentosXTratamiento(String dni, String tratamiendo_id); //-> Accede a la BBDD y busca todos los medicamenntos asociados en el id_tratamiento=tratamiento y para el paciente DNI=dni
    public List<Map<String,Object>> listarMedicamentosXTratamiento(String codigo, String tratamiento_id) throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String,Object>> lista = new ArrayList<>();
        String sql = "SELECT md.codigo, md.nombre, mt.id_registro " +
                "FROM medicamentos md " +
                "JOIN medicamentos_tratamiento mt ON md.codigo = mt.codigo_medicamento " +
                "JOIN tratamientos tr ON mt.id_tratamiento = tr.id_tratamiento " +
                "WHERE md.codigo = ? AND tr.id_tratamiento = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, codigo);
        pstmt.setInt(2,Integer.parseInt(tratamiento_id));
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()){
            Map<String,Object> item = new HashMap<>();
            item.put("codigo", rs.getInt("codigo"));
            item.put("nombre", rs.getString("nombre"));
            item.put("id_registro", rs.getInt("id_registro"));
            lista.add(item);
        }

        rs.close(); pstmt.close(); conn.close();
        return lista;
    }

    // public String importData(String jsonDataFromCSV); //-> parsea el json con los datos del CSV leído en el programa de importación y realiza los INSERT adecuados para actualizar la BBDD con la info almacenada en ese JSON
    public void procesarImportacion(DatosImportacion datos) throws Exception{
        Connection conn = ConexionBD.getConnection();
        conn.setAutoCommit(false);

        try {
            // 1 Importar Pacientes
            if (datos.getPacientes() != null){
                String sql = "insert ignore into pacientes (dni, nombre, apellidos, email, telefono, direccion) values (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (Paciente p: datos.getPacientes()){
                    pstmt.setString(1, p.getDni());
                    pstmt.setString(2, p.getNombre());
                    pstmt.setString(3, p.getApellidos());
                    pstmt.setString(4, p.getEmail());
                    pstmt.setString(5, p.getTelefono());
                    pstmt.setString(6, p.getDireccion());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 2 Importar Medicamentos
            if (datos.getMedicamentos() != null){
                String sql = "INSERT IGNORE INTO medicamentos (codigo, nombre, posologia) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                for (Medicamento m : datos.getMedicamentos()) {
                    pstmt.setString(1, m.getCodigo());
                    pstmt.setString(2, m.getNombre());
                    pstmt.setString(3, m.getPosologia());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 3. Importar pedidos
            // (Debe ir después de Clientes porque la BBDD exige que el DNI ya exista)
            if (datos.getTratamientos() != null) {
                String sql = "INSERT IGNORE INTO tratamientos (id_tratamiento, dni_paciente, fecha_inicio, dias_tratamiento, diagnostico) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (Tratamiento t : datos.getTratamientos()) {
                    pstmt.setInt(1, t.getId_tratamiento());
                    pstmt.setString(2, t.getDni_paciente());
                    pstmt.setString(3, t.getFecha_inicio());
                    pstmt.setInt(4,t.getDias_tratamiento());
                    pstmt.setString(5, t.getDiagnostico());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // 4 Importar medicamentosXTratamiento

            if (datos.getMedicamentoTratamientoList() != null) {
                // No insertamos id_linea ni subtotal porque son campos autogenerados en la BBDD
                String sql = "INSERT IGNORE INTO medicamentos_tratamiento (id_tratamiento, codigo_medicamento) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                for (MedicamentoTratamiento mt : datos.getMedicamentoTratamientoList()) {
                    pstmt.setInt(1, mt.getId_tratamiento());
                    pstmt.setString(2, mt.getCodigo_medicamento());
                    pstmt.executeUpdate();
                }
                pstmt.close();
            }

            // Si llegamos hasta aqui sin que ningun PreparedStatement explote, confirmamos todos los cambios
            conn.commit();

        } catch (Exception e) {
            // Si salta un error en cualquier tabla, damos marcha atra para dejar la BBDD intacta
            conn.rollback();
            throw new Exception("Error procesando la importación: " + e.getMessage());
        } finally {
            // Siempre devolvemos la conexion a su estado original antes de cerrarla
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<Map<String, Object>> listarTodosPacientes() throws SQLException, ClassNotFoundException{
        Connection conn = ConexionBD.getConnection();
        List<Map<String, Object>> lista = new ArrayList<>();

        String sql = "SELECT dni, nombre, apellidos, email, telefono, direccion FROM pacientes";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()){
            Map<String, Object> paciente = new HashMap<>();
            paciente.put("dni", rs.getString("dni"));
            paciente.put("nombre",rs.getString("nombre"));
            paciente.put("apellidos",rs.getString("apellidos"));
            paciente.put("email", rs.getString("email"));
            paciente.put("telefono",rs.getString("telefono"));
            paciente.put("direccion",rs.getString("direccion"));
            lista.add(paciente);
        }
        rs.close();
        stmt.close();
        conn.close();
        return lista;
    }
}