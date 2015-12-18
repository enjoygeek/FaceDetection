package com.cloud.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.cloud.common.dataset.BioID;

public class BioImage extends Image {

	public BioImage(String uri) throws Exception {
		super(uri, -1, -1, true);
		loadEyeData();
	}

	private void loadEyeData() {
		try {
			String fileName = FilenameUtils.removeExtension(this.getUri());
			String line2 = Files.readAllLines(Paths.get(fileName + BioID.extensionData)).get(1);
			String[] split = line2.split("\t"); // #LX LY RX RY
			FaceDetection detection = new FaceDetection();
			detection.setLeftEye(new Coordinate2D(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
			detection.setRightEye(new Coordinate2D(Integer.valueOf(split[2]), Integer.valueOf(split[3])));
			super.addRefDetections(detection);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
