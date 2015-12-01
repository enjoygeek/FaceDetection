package com.cloud.presentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.common.NativeLibraries;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;
import com.cloud.service.TestService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class OpenCVServlet
 */
@WebServlet("/OpenCVServlet")
public class OpenCVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private FDProperties properties = FDProperties.getInstance();
       
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
		
		ObjectMapper om = new ObjectMapper();	
		
		if(imagen== null || imagen.isEmpty()){
			response.getOutputStream().println(om.writeValueAsString("Falta parametro image"));
			return;
		}
		
		Image.BASE_PATH_TO_DOWNLOAD = properties.getPropValue(FDProperties.DOWNLOAD_PATH);
		NativeLibraries.loadLibraries();			
		Image image = null;
		try {
			image = new Image(imagen,-1, -1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.getOutputStream().println(e.getMessage());
			return;
		}
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		ProcessResult resultado = new OpenCVProcessor(faceCascade,eyeCascade).run(image);
		
		//Traduccion a JSON
			
		response.getOutputStream().println(om.writeValueAsString(resultado));
		
	}
	

	

}
