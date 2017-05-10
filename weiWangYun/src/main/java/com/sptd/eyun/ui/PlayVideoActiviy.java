package com.sptd.eyun.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.sptd.eyun.R;
import com.sptd.eyun.widget.SaveVideoPopupWindow;
import com.umeng.analytics.MobclickAgent;
import com.yqritc.scalablevideoview.ScalableVideoView;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;
import sz.itguy.utils.FileUtil;

/**
 * 播放视频页面
 *
 * @author Martin
 */
public class PlayVideoActiviy extends Activity implements OnClickListener, OnLongClickListener {

	public static final String TAG = "PlayVideoActiviy";

	public static final String KEY_FILE_PATH = "file_path";

	private String filePath;

	private ScalableVideoView mScalableVideoView;

	private SaveVideoPopupWindow saveVideoPopupWindow;
	private SaveListener saveListener;
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				try {
					mScalableVideoView.setDataSource(filePath);
					mScalableVideoView.setLooping(true);
					mScalableVideoView.prepare();
					mScalableVideoView.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		filePath = getIntent().getStringExtra(KEY_FILE_PATH);
		Log.d(TAG, filePath);
		if (TextUtils.isEmpty(filePath)) {
			Toast.makeText(this, "视频路径错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		saveListener = new SaveListener();
		saveVideoPopupWindow = new SaveVideoPopupWindow(this, saveListener);

		setContentView(R.layout.activity_play_video);
		mScalableVideoView = (ScalableVideoView) findViewById(R.id.video_view);
		try {
			// 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
			mScalableVideoView.setDataSource("");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Message msg = hand.obtainMessage();
		msg.what = 1;
		hand.sendMessageDelayed(msg, 500);

		mScalableVideoView.setOnClickListener(this);
		mScalableVideoView.setOnLongClickListener(this);
	}

	/**
	 * 获取视频缩略图（这里获取第一帧）
	 * 
	 * @param filePath
	 * @return
	 */
	public Bitmap getVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mScalableVideoView.isPlaying()) {
			mScalableVideoView.stop();
		}
		this.finish();

	}

	class SaveListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.btn_video_pick_photo) {
				saveVideoPopupWindow.dismiss();
				if (prepareCopy()) {

					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
							+ "com.sptd.eyun" + File.separator + "video" + File.separator + System.currentTimeMillis()
							+ ".Mp4";

					try {
						copyFile(filePath, new File(path));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(PlayVideoActiviy.this, "保存失败", Toast.LENGTH_LONG).show();
					}
				}
			}

		}

	}

	private boolean prepareCopy() {
		if (!FileUtil.isSDCardMounted()) {
			Toast.makeText(this, "SD卡不可用！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void copyFile(String oldPath, File newFile) throws IOException {
		try {
			File folder = newFile.getParentFile();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newFile);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}

			ContentValues values = new ContentValues(6);
			values.put(MediaStore.Video.Media.DATA, newFile.getAbsolutePath());
			values.put(MediaStore.Video.Media.DISPLAY_NAME, newFile.getName());
			values.put(MediaStore.Video.Media.DATE_ADDED, newFile.getParent());
			values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
			values.put(MediaStore.Video.Media.DATE_MODIFIED, System.currentTimeMillis());
			values.put(MediaStore.Video.Media.TITLE, "");
			Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
			if (uri != null) {
				Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
			}

		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		if (saveVideoPopupWindow != null) {
			saveVideoPopupWindow.showAtLocation(mScalableVideoView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
		return false;
	}
	
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		//TODO
		/*
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	    */
	}
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		//TODO
		/*
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	    */
	}
}
