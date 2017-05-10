package com.sptd.eyun.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sptd.eyun.R;
import com.sptd.eyun.widget.PhotoViewAttacher;
import com.sptd.eyun.widget.PhotoViewAttacher.OnPhotoTapListener;
import com.sptd.eyun.widget.SelectPicPopupWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private SelectPicPopupWindow picSelectWindow;
	private SelectPicPopClickListener picPopListener;

	boolean isSucessLoad = false;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		picPopListener = new SelectPicPopClickListener();
		picSelectWindow = new SelectPicPopupWindow(this.getActivity(), picPopListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (isSucessLoad) {
					if (!mImageUrl.contains("file://")) {
						picSelectWindow.showAtLocation(mImageView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					}
				}
				return false;
			}
		});
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
				isSucessLoad = false;
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				isSucessLoad = false;
				if(getActivity()==null){
					return;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				isSucessLoad = true;
				mAttacher.update();
			}
		});
	}

	class SelectPicPopClickListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.btn_take_photo) {
				Bitmap bit = ImageLoader.getInstance().loadImageSync(mImageUrl);
				File file = saveImageToGallery(getActivity(), bit);
				if (file != null) {
					if (file.exists()) {
						Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_LONG).show();
				}
				
				picSelectWindow.dismiss();

			} else if (v.getId() == R.id.btn_pick_photo) {
				Bitmap bit = ImageLoader.getInstance().loadImageSync(mImageUrl);
				File file = saveImage(bit);
				if (file != null) {
					if (file.exists()) {
						Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_LONG).show();
				}
				picSelectWindow.dismiss();
			}

		}

	}

	public File saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "com.sptd.eyun");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			file = null;
		} catch (IOException e) {
			e.printStackTrace();
			file = null;
		}

		if (file != null) {

			try {
				MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName,
						null);
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			context.sendBroadcast(
					new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
		}
		return file;
	}

	public File saveImage(Bitmap bmp) {
		File appDir = new File(Environment.getExternalStorageDirectory(), "com.sptd.eyun");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			file = null;
		} catch (IOException e) {
			e.printStackTrace();
			file = null;
		}
		return file;
	}
}
