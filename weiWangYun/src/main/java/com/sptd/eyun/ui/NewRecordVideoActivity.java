package com.sptd.eyun.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.f;
import com.sptd.eyun.R;
import com.sptd.util.PermissionsChecker;
import com.umeng.analytics.MobclickAgent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import sz.itguy.utils.FileUtil;
import sz.itguy.wxlikevideo.camera.CameraHelper;
import sz.itguy.wxlikevideo.recorder.WXLikeVideoRecorder;
import sz.itguy.wxlikevideo.recorder.WXLikeVideoRecorder.RecordListener;
import sz.itguy.wxlikevideo.views.CameraPreviewView;

/**
 * 新视频录制页面
 *
 * @author Martin
 */
public class NewRecordVideoActivity extends Activity implements View.OnTouchListener, RecordListener {

	private static final String TAG = "NewRecordVideoActivity";

	// 输出宽度
	private static final int OUTPUT_WIDTH = 640;
	// 输出高度
	private static final int OUTPUT_HEIGHT = 480;
	// 宽高比
	private static final float RATIO = 1f * OUTPUT_WIDTH / OUTPUT_HEIGHT;

	private Camera mCamera;

	private WXLikeVideoRecorder mRecorder;

	private static final int CANCEL_RECORD_OFFSET = -100;
	private float mDownX, mDownY;
	private boolean isCancelRecord = false;
	private ProgressBar mProgressBar;
	private int mRecordMaxTime = 30;
	private int mTimeCount;// 时间计数
	private Timer mTimer;// 计时器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
			} else {
				mProgressBar.setProgress(mTimeCount);// 设置进度条
				if (mTimeCount >= mRecordMaxTime) {// 达到指定时间，停止拍摄
					stopRecord();
					mTimer.cancel();
				}
			}
		};
	};
	private static final int REQUEST_CODE = 0; // 请求码

	// 所需的全部权限
	static final String[] PERMISSIONS = new String[] { Manifest.permission.RECORD_AUDIO };

	private PermissionsChecker mPermissionsChecker; // 权限检测器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int cameraId = CameraHelper.getDefaultCameraID();
		// Create an instance of Camera
		mCamera = CameraHelper.getCameraInstance(cameraId);
		if (null == mCamera) {
			Toast.makeText(this, "打开相机失败！", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		mPermissionsChecker = new PermissionsChecker(this);
		// 初始化录像机
		mRecorder = new WXLikeVideoRecorder(this, FileUtil.MEDIA_FILE_DIR);
		mRecorder.setOutputSize(OUTPUT_WIDTH, OUTPUT_HEIGHT);
		mRecorder.setFrameSize(OUTPUT_WIDTH, OUTPUT_HEIGHT);
		mRecorder.setRecordListener(this);
		setContentView(R.layout.activity_new_recorder);
		CameraPreviewView preview = (CameraPreviewView) findViewById(R.id.camera_preview);
		preview.setCamera(mCamera, cameraId);
		mRecorder.setCameraPreviewView(preview);
		findViewById(R.id.button_start).setOnTouchListener(this);
		mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mRecorder != null) {
			boolean recording = mRecorder.isRecording();
			if (recording) {
				// 页面不可见就要停止录制
				mRecorder.stopRecording();
			}
			// 录制时退出，直接舍弃视频
			if (recording) {
				FileUtil.deleteFile(mRecorder.getFilePath());
			}
		}
		releaseCamera(); // release the camera immediately on pause event
		finish();
		MobclickAgent.onPause(this);
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			// 释放前先停止预览
			mCamera.stopPreview();
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/**
	 * 开始录制
	 */
	private void startRecord() {
		if (mRecorder.isRecording()) {
			Toast.makeText(this, "正在录制中…", Toast.LENGTH_SHORT).show();
			return;
		}

		// initialize video camera
		if (prepareVideoRecorder()) {
			// 录制视频
			mRecorder.startRecording();
		}
	}

	/**
	 * 准备视频录制器
	 * 
	 * @return
	 */
	private boolean prepareVideoRecorder() {
		if (!FileUtil.isSDCardMounted()) {
			Toast.makeText(this, "SD卡不可用！", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/**
	 * 停止录制
	 */
	private void stopRecord() {
		mRecorder.stopRecording();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isCancelRecord = false;
			mRecorder.setUp(false);
			mDownX = event.getX();
			mDownY = event.getY();
			startRecord();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mRecorder.isRecording())
				return false;

			float y = event.getY();
			if (y - mDownY < CANCEL_RECORD_OFFSET) {
				if (!isCancelRecord) {
					// cancel record
					isCancelRecord = true;
					Toast.makeText(this, "cancel record", Toast.LENGTH_SHORT).show();
				}
			} else {
				isCancelRecord = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("OP", mRecorder.isRecording() + "----");
			mRecorder.setUp(true);
			if (mRecorder.isRecording()) {
				stopRecord();
				mTimer.cancel();
			}
			break;
		}

		return true;
	}

	@Override
	public void error() {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(this, "录制异常!", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onAudioStart() {
		// TODO Auto-generated method stub

		mTimeCount = 0;// 时间计数器重新赋值
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTimeCount++;
				handler.sendMessage(handler.obtainMessage());
			}
		}, 0, 1000);

	}

	@Override
	public void onAudioStop() {
		// TODO Auto-generated method stub
		mTimer.cancel();
		String videoPath = mRecorder.getFilePath();
		if (isCancelRecord) {
			FileUtil.deleteFile(videoPath);
		} else {
			// 告诉宿主页面录制视频的路径
			// startActivity(new Intent(this,
			// PlayVideoActiviy.class).putExtra(PlayVideoActiviy.KEY_FILE_PATH,
			// videoPath));
			Intent intent = new Intent();
			intent.putExtra(NewOpActivity.KEY_FILE_PATH, videoPath);
			setResult(NewOpActivity.VIDEO_RESULT_CODE, intent);
			this.finish();
		}

	}

	/**
	 * 开始录制失败回调任务
	 *
	 * @author Martin
	 */
	// public static class StartRecordFailCallbackRunnable implements Runnable {
	//
	// private WeakReference<NewRecordVideoActivity>
	// mNewRecordVideoActivityWeakReference;
	//
	// public StartRecordFailCallbackRunnable(NewRecordVideoActivity activity) {
	// mNewRecordVideoActivityWeakReference = new
	// WeakReference<NewRecordVideoActivity>(activity);
	// }
	//
	// @Override
	// public void run() {
	// NewRecordVideoActivity activity;
	// if (null == (activity = mNewRecordVideoActivityWeakReference.get()))
	// return;
	//
	// String filePath = activity.mRecorder.getFilePath();
	// if (!TextUtils.isEmpty(filePath)) {
	// FileUtil.deleteFile(filePath);
	// Toast.makeText(activity, "Start record failed.",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// }

	/**
	 * 停止录制回调任务
	 *
	 * @author Martin
	 */
	// public static class StopRecordCallbackRunnable implements Runnable {
	//
	// private WeakReference<NewRecordVideoActivity>
	// mNewRecordVideoActivityWeakReference;
	//
	// public StopRecordCallbackRunnable(NewRecordVideoActivity activity) {
	// mNewRecordVideoActivityWeakReference = new
	// WeakReference<NewRecordVideoActivity>(activity);
	// }
	//
	// @Override
	// public void run() {
	// NewRecordVideoActivity activity;
	// if (null == (activity = mNewRecordVideoActivityWeakReference.get()))
	// return;
	//
	// String filePath = activity.mRecorder.getFilePath();
	// if (!TextUtils.isEmpty(filePath)) {
	// if (activity.isCancelRecord) {
	// FileUtil.deleteFile(filePath);
	// } else {
	// Toast.makeText(activity, "Video file path: " + filePath,
	// Toast.LENGTH_LONG).show();
	// }
	// }
	// }
	// }

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// TODO
		/*
		 * MobclickAgent.onResume(this); JPushInterface.onResume(this);
		 */
	}
}
