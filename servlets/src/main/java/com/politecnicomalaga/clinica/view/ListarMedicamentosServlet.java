package com.politecnicomalaga.clinica.view;


import com.politecnicomalaga.clinica.controller.Controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/listarMedicamentos")
public class ListarMedicamentosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Controlador controlador = new Controlador();
        String json = controlador.listAllMedicamentos();

        PrintWriter out = response.getWriter();
        out.println(json);

    }
}
