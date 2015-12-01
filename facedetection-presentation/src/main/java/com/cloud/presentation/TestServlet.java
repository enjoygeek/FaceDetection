package com.cloud.presentation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.dto.ProcessResult;
import com.cloud.remote.FacePlusPlus;
import com.cloud.service.TestService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

	private TestService testService;
	private FDProperties properties = FDProperties.getInstance();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
		String dataset = properties.getPropValue(FDProperties.DATASET);
		
		testService = new TestService(dataset);
		//Agrego los servicios disponibles
//		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
//		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
//		ts.addService(new OpenCVProcessor(faceCascade, eyeCascade));
		testService.addService(new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES));
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<ProcessResult> testOutput = testService.test();
		ObjectMapper om = new ObjectMapper();
		response.getWriter().println(om.writeValueAsString(testOutput));
		//String[] salidas = testOutput.stream().map(out -> out.getServiceOutput()).toArray(String[]::new);
		//response.getWriter().println(Arrays.toString(salidas));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().println("<html><body><p>Use " + "GET instead!</p></body></html>");
	}

}
