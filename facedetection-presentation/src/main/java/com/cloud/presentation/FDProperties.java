package com.cloud.presentation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Singleton para obtener parametros de configuración de archivo workflow.properties
 * @author lgi_unicen
 *
 */
public class FDProperties {
		

	//Singleton instance
	private static FDProperties instance = null;
	//Properties
	private static Properties prop;
	//Private Constants 
	private static String PROPFILENAME = "facedetection.properties";
		
	//Public Constants
	//DATASET
	public static final String DATASET = "DATASET";
	//CLASSIFIERS
	public static final String FACE_CASCADE_URL = "FACE_CASCADE_URL";
	public static final String EYE_CASCADE_URL = "EYE_CASCADE_URL";
		

	/**
	 * Constructor por defecto, protegido por clase Singleton
	 */
	protected FDProperties(){}
	
	/**
	 * Devuelve la instancia del singleton
	 * @return Singleton WorfklowProperties
	 */
	public static FDProperties getInstance(){
		if(instance== null){
			instance = new FDProperties();
			try {
				init();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return instance;
	}
	
	/**
	 * Lee el archivo de configuraciones
	 * @throws IOException
	 */
	private static void init() throws IOException{
		prop = new Properties();		
 
		InputStream inputStream = instance.getClass().getClassLoader().getResourceAsStream(PROPFILENAME);
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + PROPFILENAME + "' not found in the classpath");
		}
 
	}
	
	/**
	 * Lee el parametro de configuración identificado por value
	 * @param value Nombre del parametro de configuración
	 * @return valor de configuración
	 */
	public String getPropValue(String value) {
		return prop.getProperty(value);
	}
	
}

