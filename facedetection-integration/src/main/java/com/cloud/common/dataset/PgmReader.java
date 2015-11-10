package com.cloud.common.dataset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cloud.dto.Image;

public class PgmReader {

	public static void setImageData(Image bI, String extension) {
		File fImage = new File(bI.getUri() + extension);
		BufferedImage image = null;
		try {
			image = ImageIO.read(fImage);
			if (image != null) {
				bI.setWidth(image.getWidth());
				bI.setHeight(image.getHeight());
				bI.setFileSize(fImage.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
