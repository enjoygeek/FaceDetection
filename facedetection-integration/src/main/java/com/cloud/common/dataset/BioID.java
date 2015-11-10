package com.cloud.common.dataset;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.cloud.dto.BioImage;
import com.cloud.dto.Image;

public class BioID {

	
	
	public static List<Image> loadImages(String path){
		 List<Image> images;
		 images = Arrays.asList(new Image[] {
						new BioImage("http://www.faceplusplus.com/wp-content/themes/faceplusplus/assets/img/demo/1.jpg"),
						new BioImage("http://www.weddingcostarica.com/images/pack1.jpg")
					});
		
		File dir = new File(path);		
		List<File> directoryListing = Arrays.asList(dir.listFiles());
				
		directoryListing .stream()
			.filter(f -> f.getName().endsWith(".eye"))
			.forEach(f -> {
				images.add(new BioImage(path+f.getName()));
				//Supongo que se llaman igual (.eye y .pgm)
				
				
			});
		
		
		
		return images;
		
	}
}
