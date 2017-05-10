package com.sptd.eyun.ui.myinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.OtherFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.PicObj;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.PictureUtils;




public class MyinfoActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_back;
	
	private LinearLayout ll_head,ll_userName,ll_remark,ll_mobile,ll_guanlianzhan,ll_modifypassword;
	
	private ImageView iv_head;
	private TextView tv_userName;
	private TextView tv_mobile;
	
	private UserObj userObj;
	private String token;
	
	
	private String pictureName;
	private Bitmap bitmap;
	private File tempFile;
	
	
	private String userName;
	public  MyinfoActivity() {
		super(R.layout.activity_myinfo,false);
	}

	@Override
	public void onSuccess(BaseModel res) {
	  switch (res.getInfCode()) {
	   case InterfaceFinals.INF_PHOTOUPLOAD:
		 PicObj obj = (PicObj) res.getObject();
		 if (obj.getPicturePath()!=null) {
			if (obj.getPicturePath().contains("http")) {
				displayImage(iv_head,obj.getPicturePath(), 76);
			}else {
				displayImage(iv_head, InterfaceFinals.URL_HEAD+obj.getPicturePath(), 76);
			}
		 }
		 UserObj userobj = getUserData();
		 userobj.setHead(obj.getPicturePath());
		 setUserData(userobj);
//		 updateMember(obj.getPicturePath(),"");
		 break;
	  case InterfaceFinals.INF_UPDATESELF:
		  UserObj userObj2=(UserObj) res.getObject();
		  userObj2.setToken(token);
		  setUserData(userObj2);
		  refreshView2();
	 }
	}
	@Override
	public void onFail(BaseModel res) {
		showToast(res.getMessage());
		String failInf=res.getMessage();
		if("未登陆".equals(failInf)){
			startActivity(LoginActivity.class);
			finish();
		}
	}

	@Override
	protected void getData() {
		userObj=getUserData();
		token=userObj.getToken();
		
	}

	@Override
	protected void findView() {
		
				
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		ll_head=(LinearLayout) findViewById(R.id.ll_head);
		ll_head.setOnClickListener(this);
		ll_userName=(LinearLayout) findViewById(R.id.ll_userName);
		ll_userName.setOnClickListener(this);
		ll_remark=(LinearLayout) findViewById(R.id.ll_remark);
		ll_remark.setOnClickListener(this);
		ll_mobile=(LinearLayout) findViewById(R.id.ll_mobile);
		ll_mobile.setOnClickListener(this);
		ll_guanlianzhan=(LinearLayout) findViewById(R.id.ll_guanlianzhan);
		ll_guanlianzhan.setOnClickListener(this);
		ll_modifypassword=(LinearLayout) findViewById(R.id.ll_modifypassword);
		ll_modifypassword.setOnClickListener(this);
		
		iv_head=(ImageView) findViewById(R.id.iv_head);
		tv_userName=(TextView) findViewById(R.id.tv_userName);
		tv_mobile=(TextView) findViewById(R.id.tv_mobile);
		findMyinfo();
	}

	@Override
	protected void refreshView() {
		/*if(userObj.getHead()!=null){
			Log.v("head", InterfaceFinals.URL_HEAD+userObj.getHead());
	   		displayImage(iv_head,InterfaceFinals.URL_HEAD+userObj.getHead() , 76);
	   	}else {
			iv_head.setImageResource(R.drawable.touxiang);
		}
		
		tv_userName.setText(userObj.getUserName());
		tv_mobile.setText(userObj.getMobile());*/
		
	}
	private void refreshView2() {
		userObj=getUserData();
		if(userObj.getHead()!=null && !"".equals(userObj.getHead())){
			Log.v("head", InterfaceFinals.URL_HEAD+userObj.getHead());
			displayImage(iv_head,InterfaceFinals.URL_HEAD+userObj.getHead() , 76);
		}else {
			iv_head.setImageResource(R.drawable.touxiang);
		}
		
		tv_userName.setText(userObj.getUserName());
		tv_mobile.setText(userObj.getMobile());
		
	}

	@Override
	public void onClick(View v) {
        
		switch (v.getId()) {
		case R.id.iv_back:
			 finish();
			break;

		case R.id.ll_head:
			PictureUtils.doPickPhotoAction(MyinfoActivity.this,false,false);
			break;
			
		case R.id.ll_userName:
			startActivityForResult(ModifyUserNameActivity.class,tv_userName.getText().toString(), 2);

			break;
		case R.id.ll_remark:
			startActivityForResult(ModifyRemarkActivity.class,getUserData().getRemark(),3);
			break;
		case R.id.ll_mobile:
			break;
		case R.id.ll_guanlianzhan:
			startActivity(ModifyMyZhanActivity.class);
			break;
		case R.id.ll_modifypassword:
			startActivity(ModifyPasswordActivity.class);
			break;
		}
		
	}
	
	/**
	 * 接照片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PictureUtils.CAMERA_WITH_DATA:// 拍照返回
				if (PictureUtils.hasSdcard()) {
					PictureUtils.crop(this,Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PictureUtils.picTime+"_pic.png")));
				} else {
					showToast("未找到存储卡，无法存储照片！");
				}
				break;
			case PictureUtils.PHOTO_PICKED_WITH_DATA:// 选择相册返回    
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					PictureUtils.crop(this,uri);
				}
				break;
			case PictureUtils.PHOTO_REQUEST_CUT:// 裁剪图片返回  
				try {
					Bitmap bitmap = data.getParcelableExtra("data");
					//userPic.setImageBitmap(bitmap);
					if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
						pictureName = String.valueOf(System.currentTimeMillis())+"temp_pic.png";
						tempFile = new File(OtherFinals.DIR_IMG,pictureName);
						if(tempFile.exists()){
							tempFile.delete();
						}
						try {
							FileOutputStream out = new FileOutputStream(tempFile);
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
							out.flush();
							out.close();
							if(getUserData()!=null){
								photoUpload(tempFile.getAbsolutePath());
/*								FileInputStream fis = new FileInputStream(tempFile);
								BitmapFactory.decodeStream(fis);
								iv_head .setImageBitmap(bitmap); //设置Bitmap
								out.close();*/
							}
							//							iv_photo.setImageBitmap(BitmapUtil.getRoundedCornerBitmap(bitmap,getResources().getDimension(R.dimen.dim30)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		UserObj obj = getUserData();
		if (resultCode==2) {
			userName = (String)data.getStringExtra("username");
			tv_userName.setText(userName);	
			obj.setUserName(userName);
			setUserData(obj);
		}
		if (resultCode==3) {
			String sign = (String)data.getStringExtra("sign");
			obj.setRemark(sign);
			setUserData(obj);
		}
	}
	
	/**
	 * 文件上传，返回文件存储的相对路径
	 * @param absolutePath
	 */
	private void photoUpload(String absolutePath) {
		Type t = new TypeToken<BaseModel<PicObj>>() {
		}.getType();
		BaseAsyncTask<PicObj> task = new BaseAsyncTask<PicObj>(this, t,
				InterfaceFinals.INF_PHOTOUPLOAD,true);
		HashMap<String, String> params = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();
	
		data.put("photoFile", absolutePath);
		params.put("token", getUserData().getToken());
		task.execute(params,data);

	}
	/**
	 * 
	 *获取个人信息
	 */	
	private void findMyinfo() {
		Type t = new TypeToken<BaseModel<UserObj>>() {
		}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this,t,
				InterfaceFinals.INF_UPDATESELF,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", getUserData().getToken());				
			

		task.execute(params);
	}
}
