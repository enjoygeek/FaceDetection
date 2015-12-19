package com.cloud.dto;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.cloud.common.dataset.BioID;

public class BioImage extends Image {

	public BioImage(String uri) throws Exception {
		super(uri, -1, -1, true);
		loadEyeData();
	}

	private void loadEyeData() {
		Scanner scanner = null;
		try {
			String fileName = FilenameUtils.removeExtension(this.getRemoteUri());			
			String out, line2 = "";
			scanner = new Scanner(
					new URL(fileName+".eye")
							.openStream(),
					"UTF-8");
			out = scanner.useDelimiter("\\A").next();
			String[] lines = out.split(System.getProperty("line.separator"));
			line2 = lines[1];
			// String line2 = Files.readAllLines(Paths.get(fileName +
			// BioID.extensionData)).get(1);			
			String[] split = line2.split("\t"); // #LX LY RX RY
			FaceDetection detection = new FaceDetection();
			detection.setLeftEye(new Coordinate2D(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
			detection.setRightEye(new Coordinate2D(Integer.valueOf(split[2]), Integer.valueOf(split[3])));
			super.addRefDetections(detection);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(scanner);
		}
	}
}
