package com.cloud.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.cloud.common.dataset.BioID;

public class BioImage extends Image {

	private Eye refLeftEye;
	private Eye refRightEye;

	public BioImage(String uri) {
		super();
		setUri(uri);//No incluye la extension
		loadEyeData();
	}

	private void loadEyeData() {
		try {
			String line2 = Files.readAllLines(Paths.get(this.getUri()+BioID.extensionData)).get(1);
			String[] split = line2.split("\t"); // #LX LY RX RY
			this.setRefLeftEye(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
			this.setRefRightEye(Integer.valueOf(split[2]), Integer.valueOf(split[3]));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public BioImage(String uri, int width, int height, long fileSize, int lx, int ly, int rx, int ry) {
		super(uri, width, height, fileSize);//Uri no incluye la extension
		setRefLeftEye(lx, ly);
		setRefRightEye(rx, ry);
	}

	public Eye getRefLeftEye() {
		return refLeftEye;
	}

	public void setRefLeftEye(int x, int y) {
		this.refLeftEye = new Eye(x, y);
	}

	public Eye getRefRightEye() {
		return refRightEye;
	}

	public void setRefRightEye(int x, int y) {
		this.refRightEye = new Eye(x, y);
		;
	}

}