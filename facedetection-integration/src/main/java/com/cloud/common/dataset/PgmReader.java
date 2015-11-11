package com.cloud.common.dataset;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.cloud.dto.Image;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PgmReader {
	
	// We'll use a constant maximum greyscale value
	private static final int MAXVAL = 255;

	public static void setImageData(Image bI, String extension)	{
		String path = bI.getUri()+extension;
		System.out.println(path);
		Mat imread = Imgcodecs.imread(path,1);
		System.out.println(imread.cols()+","+imread.rows());

	}

	public static void main(String[] args) {
		Image img = new Image();
		img.setUri("D:\\datasets\\BioID\\BioID_0000");		
		try {
			PgmReader.setImageData(img, ".pgm");
			System.out.println(new ObjectMapper()
					.writerWithDefaultPrettyPrinter().writeValueAsString(img));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
