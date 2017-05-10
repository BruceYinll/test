package com.sptd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CompressImage {

	public static InputStream getInputStream(String filePath) throws Exception {
		return compressImage(BitmapFactory.decodeFile(filePath), 6 * 1024);

	}

	public static InputStream compressImage(Bitmap image, int size) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩比options=50，把压缩后的数据存放到baos中
				options -= 5;// 每次都减少10
			}
			return new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*** 获取文件大小 ***/
	public static long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = new FileInputStream(f);
			s = fis.available();
		} else {
			s = 0;
			System.out.println("文件不存在");
		}
		return s;
	}
}
