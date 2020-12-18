package com.acgist.utils;

import java.io.File;

import com.acgist.face.ImageFace;

public class LocalPath {

	public static final String localPath(String name) {
		return ImageFace.class.getResource(name).getPath().substring(1).replace("/", File.separator);
	}
	
}
