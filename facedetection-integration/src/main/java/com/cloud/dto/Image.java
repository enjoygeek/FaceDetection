package com.cloud.dto;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Image {
	
	private String uri;
	private int width;
	private int height;
	private long fileSize;	
	private String extensionImage;
	
	
	public Image(){};

	public Image(String uri, String extension,int width, int height, long fileSize) {
		super();		
		this.setUri(uri);
		this.setWidth(width);
		this.setHeight(height);
		this.setFileSize(fileSize);
		this.setExtensionImage(extension);
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public double getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	
	/*
	public Mat rotate(double angle)
	{
		Mat src = Imgcodecs.imread(getUri()+getExtensionImage(), 1);
		 // assuming source image's with and height are a pair value:
        int centerX = Math.round(src.width()/2);
        int centerY = Math.round(src.height()/2);

        Point center = new Point(centerY,centerX);        
        double scale = 1.0;

        double ratio =  src.height() / (double) src.width();

        int rotatedHeight = (int) Math.round(src.height());       
        int rotatedWidth  = (int) Math.round(src.height() * ratio);

        Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, scale);

        Size rotatedSize = new Size(rotatedWidth, rotatedHeight);
        Mat mIntermediateMat = new Mat(rotatedSize, src.type());

        Imgproc.warpAffine(src, mIntermediateMat, mapMatrix, mIntermediateMat.size(), Imgproc.INTER_LINEAR);

        Mat ROI = src.submat(0, mIntermediateMat.rows(), 0, mIntermediateMat.cols());

        mIntermediateMat.copyTo(ROI);
        return ROI;
	}
	
	
	public BufferedImage getImage() {
		File img = new File(this.getUri());
		BufferedImage in;
		BufferedImage newImage = null;
		try {
			in = ImageIO.read(img);
			newImage = new BufferedImage(in.getWidth(), in.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = newImage.createGraphics();
			g.drawImage(in, 0, 0, null);
			g.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newImage;
	}*/

	public String getExtensionImage() {
		return extensionImage;
	}

	public void setExtensionImage(String extensionImage) {
		this.extensionImage = extensionImage;
	}
}
