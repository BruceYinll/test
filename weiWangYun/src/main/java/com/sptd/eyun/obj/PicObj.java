package com.sptd.eyun.obj;

import java.io.Serializable;

public class PicObj implements Serializable{
	private String pictureWidth;
	private String picturePath;
	private String pictureHeight;
	public String getPictureWidth() {
		return pictureWidth;
	}
	public void setPictureWidth(String pictureWidth) {
		this.pictureWidth = pictureWidth;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getPictureHeight() {
		return pictureHeight;
	}
	public void setPictureHeight(String pictureHeight) {
		this.pictureHeight = pictureHeight;
	}
}
