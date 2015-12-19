package com.cloud.presentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;
import com.cloud.remote.FacePlusPlus;
import com.cloud.remote.FaceRect;
import com.cloud.remote.SkyBiometry;
import com.cloud.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

	private static String urlResultados;
	private TestService testService;
	private FDProperties properties = FDProperties.getInstance();

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
		String dataset = properties.getPropValue(FDProperties.REMOTE_DATASET);
		String imageService = properties.getPropValue(FDProperties.REMOTE_IMAGE);

		testService = new TestService(dataset, imageService);
		// Agrego los servicios disponibles

		// OpenCVProcessor ocv_p = initOpenCV_Parameters(properties);
		// testService.addService(ocv_p);

		// FaceRect fRect = initFaceRect_Parameters(properties);
		// testService.addService(fRect);

		SkyBiometry sBiometry = initSkyBiometry_Parameters(properties);
		testService.addService(sBiometry);

		// FacePlusPlus facePlusPlus = initFacePlusPlus_Parameters(properties);
		// testService.addService(facePlusPlus);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<ProcessResult> testOutput = testService.test();
		// Imprime JSON
		try {
			ObjectMapper om = new ObjectMapper();
			escribirArchivo(om.writerWithDefaultPrettyPrinter().writeValueAsString(testOutput));
			response.getWriter().println(om.writeValueAsString(testOutput));
			// String[] salidas = testOutput.stream().map(out ->
			// out.getServiceOutput()).toArray(String[]::new);
			// response.getWriter().println(Arrays.toString(salidas));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

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

	private static FacePlusPlus initFacePlusPlus_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.FACEPLUSPLUS_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String attributes = FacePlusPlus.DEFAULT_ATTRIBUTES;

		FacePlusPlus facePlusPlus = new FacePlusPlus(serviceUrl, xMashapeKey, attributes);
		return facePlusPlus;
	}

	private static SkyBiometry initSkyBiometry_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.SKYBIOMETRY_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String api_key = properties.getPropValue(FDProperties.SKYBIOMETRY_API_KEY);
		String api_secret = properties.getPropValue(FDProperties.SKYBIOMETRY_API_SECRET);
		String attributes = SkyBiometry.DEFAULT_ATTRIBUTES;
		String detector = SkyBiometry.DEFAULT_DETECTOR;
		SkyBiometry skyBiometry = new SkyBiometry(serviceUrl, xMashapeKey, api_key, api_secret, attributes, detector);
		return skyBiometry;
	}

	private static FaceRect initFaceRect_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.FACERECT_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String attributes = FaceRect.DEFAULT_ATTRIBUTES;
		FaceRect fRect = new FaceRect(serviceUrl, xMashapeKey, attributes);
		return fRect;
	}

	private static OpenCVProcessor initOpenCV_Parameters(FDProperties properties) {
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		urlResultados = properties.getPropValue(FDProperties.RESULTADOS);
		if (!new File(faceCascade).exists()) {
			throw new ExceptionInInitializerError("Face-Classifier not found");
		}
		if (!new File(eyeCascade).exists()) {
			throw new ExceptionInInitializerError("Eye-Classifier not found");
		}

		OpenCVProcessor ocv_p = new OpenCVProcessor(faceCascade, eyeCascade);
		ocv_p.setfEscalarFace(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_FACE)));
		ocv_p.setfEscalarEye(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_EYE)));
		ocv_p.setMinNeighborsFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_FACE)));
		ocv_p.setMinNeighborsEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_EYE)));
		ocv_p.setMinPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_FACE)));
		ocv_p.setMaxPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_FACE)));
		ocv_p.setMinPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_EYE)));
		ocv_p.setMaxPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_EYE)));
		ocv_p.setBlurEffect(Integer.parseInt(properties.getPropValue(FDProperties.BLUR_EFFECT)));
		ocv_p.setShowFaceDetection((properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("true")
				|| (properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("TRUE"));
		return ocv_p;
	}

	private static void escribirArchivo(String texto) {
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		System.out.println(urlResultados + File.separator + timeLog + ".txt");
		File logFile = new File(urlResultados + File.separator + timeLog + ".txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(texto);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

}
