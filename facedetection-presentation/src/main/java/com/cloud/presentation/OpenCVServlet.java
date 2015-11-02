package com.cloud.presentation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.dto.ProcessResult;
import com.cloud.service.opencv.OpenCVRunnerService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class OpenCVServlet
 */
@WebServlet("/OpenCVServlet")
public class OpenCVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpenCVServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String imagen = request.getParameter("image");
		List<ProcessResult> resultado = OpenCVRunnerService.procesar(imagen);
		
		//Traduccion a JSON
		ObjectMapper om = new ObjectMapper();		
		response.getOutputStream().println(om.writeValueAsString(resultado));
		
	}

}
