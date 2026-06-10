package com.politecnicomalaga.clinica.view;

import com.politecnicomalaga.clinica.controller.Controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet ("/importar")
public class ImportarDatosServlet  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            BufferedReader br = request.getReader();
            String linea;
            StringBuilder sb = new StringBuilder();

            while ((linea = br.readLine()) != null){
                sb.append(linea);
            }

            String json = sb.toString();

            Controlador controlador = new Controlador();
            String resultado = controlador.importData(json);

            out.println(resultado);
        } catch (Exception e){
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
