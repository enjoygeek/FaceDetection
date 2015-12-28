package com.cloud.common;

import org.opencv.core.Core;

public class NativeLibraries {
	
	public static void loadLibraries(){
		try {
			final String[] libraries = ClassScope.getLoadedLibraries(ClassLoader.getSystemClassLoader());
			if(libraries != null){
				for(String s : libraries){
					if (s.contains(Core.NATIVE_LIBRARY_NAME)){
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		}catch(Exception e){
			//NADA
		}
	}
		
}
