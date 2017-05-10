package com.sptd.util;

import java.io.File;

import com.sptd.eyun.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;


public class PictureUtils extends Activity{
	private static final String TAG = "PictureUtils";
	public final static int INLAYFLAG = 20152211;
	/** 
	 * 用来标识请求照相功能的requestCode 
	 */  
	public static final int CAMERA_WITH_DATA = 1;  
	/** 
	 * 用来标识请求相册的requestCode 
	 */  
	public static final int PHOTO_PICKED_WITH_DATA = 2;  
	/** 
	 * 用来标示请求图片裁剪 requestCode 
	 */  
	public static final int PHOTO_REQUEST_CUT = 3; 
	/* 头像名称 */
	public static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	public static Uri photoUri;
	/**获取到的图片路径*/
	private String picPath;	
	private Intent lastIntent ;
	private static Activity context;
	public static String picTime;
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	//照片处理入口,开始启动照片选择框
	public static void doPickPhotoAction(final Activity context,final boolean isShow,final boolean isPic){
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(true);
		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);  
		window.setContentView(R.layout.takephotodialog);
		View view = window.getDecorView();

		//调用照相机
		view.findViewById(R.id.perfectinfo_dialog_takefhoto).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss(); 
				String status = Environment.getExternalStorageState();  
				if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡  
					Toast.makeText(context, "没有找到SD卡或者正在使用请关闭usb连接模式", 0).show(); 
					return;  
				}  
				try {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					/***
					 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
					 * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
					 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
					 */
					//ContentValues values = new ContentValues();
					//photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
					//					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
					//sintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//					if(isShow){
						picTime = System.currentTimeMillis()+"";
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),picTime +"_pic.png")));
//					}

					context.startActivityForResult(intent, CAMERA_WITH_DATA);
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}
		});
		//调用系统图库
		view.findViewById(R.id.perfectinfo_dialog_selectfromphotograph).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
					String status = Environment.getExternalStorageState();  
					if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡  
						Toast.makeText(context, "没有找到SD卡或者正在使用请关闭usb连接模式", 0).show(); 
						return;  
					}
					try {
						Intent intent = new Intent(Intent.ACTION_PICK);  
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
						context.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
					} catch (Exception e) {
						e.printStackTrace();
					}  
			}
		});
		//取消
		view.findViewById(R.id.perfectinfo_dialog_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			doPhoto(requestCode,data,PictureUtils.this);
		}
	}

	//获取选择图片路径
	public static String getSelectPhotoPath(Activity activity, Intent data) {
		String path = "";  
		Uri imageuri = data.getData();
		if (null != imageuri && imageuri.getScheme().compareTo("file") == 0) {
			path = imageuri.toString().replace("file://", "");
		}else{
			if (imageuri != null) {
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = activity.managedQuery(imageuri, proj, null, null, null);  
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
				if (cursor.moveToFirst()) {  
					path = cursor.getString(column_index);  
				}  
			}
		}
		return path; 
	}


	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode,Intent data,Activity context){
		if(requestCode == PHOTO_PICKED_WITH_DATA ) {
			if(data == null){
				Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null ){
				Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = context.managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null ){
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") 
				||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  )){
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			context.setResult(Activity.RESULT_OK, lastIntent);
			context.finish();
		}else{
			Toast.makeText(context, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	public static void crop(Activity context,Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		context.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
