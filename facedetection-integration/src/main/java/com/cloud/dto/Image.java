package com.cloud.dto;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.cloud.common.NativeLibraries;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image {
	
	public static String BASE_PATH_TO_DOWNLOAD = "";
	private String uri;
	private int width;
	private int height;
	@JsonIgnore
	private File file;
	private Mat image;

	public Image() {
	};

	
	public Image(String uri,int width,int height) throws Exception {
		super();
		String fileName = this.downloadImage(uri); //Si es una imagen que está en la web, la descarga y retorna el path local
		this.setUri(fileName);		
		image = Imgcodecs.imread(fileName, 1);
		if (width == -1){
			this.setWidth(image.width());
			this.setHeight(image.height());
		}			
		else{
			this.setWidth(width);
			this.setHeight(height);
		}						
		file = new File(fileName);
	}
	
	public Image(String uri) throws Exception {
		super();
		String fileName = this.downloadImage(uri); //Si es una imagen que está en la web, la descarga y retorna el path local
		this.setUri(fileName);		
		image = Imgcodecs.imread(fileName, 1);
		if (width == -1){
			this.setWidth(image.width());
			this.setHeight(image.height());
		}			
		else{
			this.setWidth(width);
			this.setHeight(height);
		}						
		file = new File(fileName);
	}

	private String downloadImage(String imageUrl) throws Exception {		
		String protocolo = imageUrl; 
		if (protocolo.toLowerCase().startsWith("http:") || protocolo.toLowerCase().startsWith("https:")  || protocolo.toLowerCase().startsWith("www.")){
			if(Image.BASE_PATH_TO_DOWNLOAD == null || Image.BASE_PATH_TO_DOWNLOAD.isEmpty())
				throw new Exception("Missing local upload path");
			try {				
				URL url = new URL(imageUrl);
				InputStream in = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
	
				Random random = new Random();
				String imageFormat = imageUrl.substring(imageUrl.lastIndexOf(".")+1);
				String filename = BASE_PATH_TO_DOWNLOAD + "\\" + random.nextLong() + "." + imageFormat;
				FileOutputStream fos = new FileOutputStream(filename);
				fos.write(response);
				fos.close();
				return filename;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
		else
		{			
			return imageUrl;
		}
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
		
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getFileSize() {
		return file.length();
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Mat rotate(double angle) {
		Mat src = Imgcodecs.imread(getUri() + getExtensionImage(), 1);
		// assuming source image's with and height are a pair value:
		int centerX = Math.round(src.width() / 2);
		int centerY = Math.round(src.height() / 2);

		Point center = new Point(centerY, centerX);
		double scale = 1.0;

		double ratio = src.height() / (double) src.width();

		int rotatedHeight = (int) Math.round(src.height());
		int rotatedWidth = (int) Math.round(src.height() * ratio);

		Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, scale);

		Size rotatedSize = new Size(rotatedWidth, rotatedHeight);
		Mat mIntermediateMat = new Mat(rotatedSize, src.type());

		Imgproc.warpAffine(src, mIntermediateMat, mapMatrix, mIntermediateMat.size(), Imgproc.INTER_LINEAR);

		Mat ROI = src.submat(0, mIntermediateMat.rows(), 0, mIntermediateMat.cols());

		mIntermediateMat.copyTo(ROI);
		return ROI;
	}

	@JsonIgnore
	public Mat getMatImage(){
		return image;
	}
	
	@JsonIgnore
	public BufferedImage getImage() {
		NativeLibraries.loadLibraries();
		File img = new File(this.getUri());

		BufferedImage in;
		BufferedImage newImage = null;
		try {
			in = ImageIO.read(img);
			if (in != null) {
				newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

				Graphics2D g = newImage.createGraphics();
				g.drawImage(in, 0, 0, null);
				g.dispose();
			} else {
				System.out.println("No se pudo leer la imagen: " + img);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newImage;
	}

	public String getExtensionImage() {
		return FilenameUtils.getExtension(uri);
		//return uri.substring(uri.lastIndexOf('.'), uri.length());
	}
	
	
}
