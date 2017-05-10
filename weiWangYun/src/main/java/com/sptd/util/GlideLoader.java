package com.sptd.util;

import com.bumptech.glide.Glide;
import com.yancy.imageselector.R;

import android.content.Context;
import android.widget.ImageView;

/**
 * GlideLoader Created by Yancy on 2015/12/6.
 */
public class GlideLoader implements com.yancy.imageselector.ImageLoader {

	@Override
	public void displayImage(Context context, String path, ImageView imageView) {
		Glide.with(context).load(path).placeholder(R.mipmap.imageselector_photo).centerCrop().into(imageView);
	}

}
/*
 * ┏┓ ┏┓ ┏┛┻━━━┛┻┓ ┃ ┃ ┃ ━ ┃ ┃ ┳┛ ┗┳ ┃ ┃ ┃ ┃ ┻ ┃ ┃ ┃ ┗━┓ ┏━┛ ┃ ┃ ┃ ┃ ┃ ┗━━━┓ ┃
 * ┣┓ ┃ ┏┛ ┗┓┓┏━┳┓┏┛ ┃┫┫ ┃┫┫ ┗┻┛ ┗┻┛ 神兽保佑 代码无BUG!
 */