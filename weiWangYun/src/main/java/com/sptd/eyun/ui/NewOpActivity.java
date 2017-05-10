package com.sptd.eyun.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.PictureVo;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.Reply;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.EditQuestionOrReplyActivity.EDIT_TYPE;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.ui.myinfo.MyyunweiActivity;
import com.sptd.eyun.widget.DeleteDialog;
import com.sptd.eyun.widget.NestGridView;
import com.sptd.eyun.widget.OPHorizontalScrollView;
import com.sptd.eyun.widget.OPHorizontalScrollView.OPScrollChangedListener;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.net.NetAsyncTask;
import com.sptd.net.NetAsyncTask.CallBack;
import com.sptd.util.EmojiFilter;
import com.sptd.util.GlideLoader;
import com.sptd.util.StringUtils;
import com.sptd.util.Utils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yqritc.scalablevideoview.ScalableVideoView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewOpActivity extends BaseInteractActivity
		implements OPScrollChangedListener, OnItemClickListener, OnClickListener {

	public static final String TAG = "NewOpActivity";

	public NewOpActivity() {
		super(R.layout.activity_new_op);
		// TODO Auto-generated constructor stub
	}

	private LinearLayout viewPartent;
	private OPHorizontalScrollView op_horizontal_view;
	private RadioGroup op_radioGroup;
	private ImageButton paging_left_btn;
	private ImageButton paging_right_btn;
	private GridViewAdpter gridViewAdpter;
	private NestGridView take_photo_gridView;
	// private SelectPicPopupWindow picSelectWindow;
	// private SelectPicPopClickListener picPopListener;
	private ImageView add_video_btn;
	private RelativeLayout play_framelayout;
	private ScalableVideoView videoView;
	private ImageView thumbnailImageView;
	private ImageView playImageView;
	public static final int REQUEST_CODE = 1000;
	private String videoPath;

	private Spinner stationSpinner;
	private Spinner deciveSpinner;
	List<UserStation> stations; // ListView
	List<DeviceType> deviceTypeList; // ListView
	private List<Device> deviceList;
	private StationAdpter stationAdpter;
	private DeviceTypeAdpter deviceTypeAdpter;
	private Device selectDevice = null;

	private EditText trouble_describe_editText;
	private EditText title_editText;
	private TextView count_textView;

	private TextView selectOpNameTextView;

	private ProgressDialog submitDialog;
	private ArrayList<String> selectorPicPaths = new ArrayList<String>();

	private ArrayList<PictureVo> resultPics = new ArrayList<PictureVo>();
	private String resultVideoPath = null;
	private ArrayList<String> tempSelects = new ArrayList<String>();

	private void openSelectorPic() {
		ImageConfig imageConfig = new ImageConfig.Builder(
				// GlideLoader 可用自己用的缓存库
				new GlideLoader())
						// 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
						.steepToolBarColor(getResources().getColor(R.color.title))
						// 标题的背景颜色 （默认黑色）
						.titleBgColor(getResources().getColor(R.color.title))
						// 提交按钮字体的颜色 （默认白色）
						.titleSubmitTextColor(getResources().getColor(R.color.white))
						// 标题颜色 （默认白色）
						.titleTextColor(getResources().getColor(R.color.white)).
						// 开启多选 （默认为多选） (单选 为 singleSelect)
						mutiSelectMaxSize(9)
						// 多选时的最大数量 （默认 9 张）
						.mutiSelectMaxSize(9)
						// 已选择的图片路径
						.pathList(tempSelects)
						// 拍照后存放的图片路径（默认 /temp/picture）
						.filePath("/ImageSelector/Pictures")
						// 开启拍照功能 （默认开启）
						.showCamera().requestCode(REQUEST_CODE).build();
		ImageSelector.open(this, imageConfig); // 开启图片选择器
	}

	/**
	 * 添加radioBtn
	 */
	private void addRadioView() {
		selectDevice = null;
		if (op_radioGroup == null) {
			return;
		}
		if (op_radioGroup.getCheckedRadioButtonId() > 0) {
			op_radioGroup.clearCheck();
		}
		op_radioGroup.removeAllViews();
		if (deviceList == null && deviceList.size() <= 0) {
			return;
		}
		for (int i = 0; i < deviceList.size(); i++) {
			RadioButton itemRadio = (RadioButton) LayoutInflater.from(this).inflate(R.layout.op_radio_item, null);
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
					(int) getResources().getDimension(R.dimen.dim180),
					(int) getResources().getDimension(R.dimen.dim180));
			int margin = (int) getResources().getDimension(R.dimen.dim10);
			int padding = (int) getResources().getDimension(R.dimen.dim13);
			params.setMargins(margin, margin, margin, margin);
			itemRadio.setPadding(padding, padding, padding, padding);
			// itemRadio.setMaxLines(1);
			itemRadio.setId(i + 1);
			itemRadio.setText(deviceList.get(i).getName());
			op_radioGroup.addView(itemRadio, i, params);
		}

		op_radioGroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				int width = op_radioGroup.getWidth();
				if (width > getWindowWith()) {
					if (op_horizontal_view.getScrollX() == 0) {
						paging_left_btn.setVisibility(View.GONE);
						paging_right_btn.setVisibility(View.VISIBLE);
					} else {
						paging_left_btn.setVisibility(View.VISIBLE);
						paging_right_btn.setVisibility(View.VISIBLE);
					}

				} else {
					paging_left_btn.setVisibility(View.GONE);
					paging_right_btn.setVisibility(View.GONE);
				}
				if (op_radioGroup.getChildCount() > 0) {

					op_radioGroup.check(1);
				}
				 op_radioGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	/**
	 * 获取RadioGroup 中的 chileView大小
	 * 
	 * @return
	 */
	private int getViewSize() {
		int count = op_radioGroup.getChildCount();
		if (count > 0) {
			View view = op_radioGroup.getChildAt(0);
			return view.getWidth() + ((int) getResources().getDimension(R.dimen.dim10)) * 2;
		}
		return 0;
	}

	/**
	 * 获取手机屏幕的大小
	 * 
	 * @return
	 */
	private int getWindowWith() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 翻页事件
	 * 
	 * @param view
	 */
	public void pageTurningEvent(View view) {
		int count = getWindowWith() / getViewSize();
		if (count == 0) {
			return;
		}
		if (view.getId() == R.id.paging_left_btn) {
			if (op_horizontal_view.getScrollX() == 0) {
				return;
			}
			if (op_horizontal_view.getScrollX() > getWindowWith()) {
				op_horizontal_view.smoothScrollTo(op_horizontal_view.getScrollX() - count * getViewSize(), 0);
			} else {
				op_horizontal_view.smoothScrollTo(0, 0);
			}
		} else {

			int scrollx = op_horizontal_view.getScrollX();
			if (op_radioGroup.getWidth() - op_horizontal_view.getScrollX() > getWindowWith()) {
				op_horizontal_view.smoothScrollTo(scrollx + count * getViewSize(), 0);
			} else {
				op_horizontal_view.smoothScrollTo(op_radioGroup.getWidth() - getWindowWith(), 0);
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

			for (String path : pathList) {
				Log.i("ImagePathList", path);
			}
			selectorPicPaths.clear();
			tempSelects.clear();
			tempSelects.addAll(pathList);
			selectorPicPaths.addAll(pathList);
			gridViewAdpter.notifyDataSetChanged();
		} else if (requestCode == VIDEO_REQUEST_CODE && resultCode == VIDEO_RESULT_CODE && data != null) {
			videoPath = data.getStringExtra(KEY_FILE_PATH);
			if (TextUtils.isEmpty(videoPath)) {
				play_framelayout.setVisibility(View.GONE);
				add_video_btn.setVisibility(View.VISIBLE);
				Toast.makeText(this, "视频路径错误", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				// 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
				play_framelayout.setVisibility(View.VISIBLE);
				add_video_btn.setVisibility(View.GONE);
				playImageView.setVisibility(View.VISIBLE);
				thumbnailImageView.setVisibility(View.VISIBLE);
				videoView.setDataSource(videoPath);
			} catch (IOException e) {
				e.printStackTrace();
				play_framelayout.setVisibility(View.GONE);
				playImageView.setVisibility(View.GONE);
				thumbnailImageView.setVisibility(View.GONE);
				add_video_btn.setVisibility(View.VISIBLE);
			}

			Glide.with(this).load(videoPath).centerCrop().into(thumbnailImageView);
		}
	}

	public void backEvent(View view) {
		warningDialog();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backEvent(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void warningDialog() {

		DeleteDialog.Builder builder = new DeleteDialog.Builder(this);
		builder.setMessage("确定要取消此次编辑吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				NewOpActivity.this.finish();
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	/**
	 * 滚动变化
	 */
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if (l == oldl) {
			return;
		}

		int opWidth = op_radioGroup.getWidth();
		if (opWidth - op_horizontal_view.getScrollX() == getWindowWith()) {
			paging_right_btn.setVisibility(View.INVISIBLE);
		} else {
			paging_right_btn.setVisibility(View.VISIBLE);
		}

		if (op_horizontal_view.getScrollX() == 0) {
			paging_left_btn.setVisibility(View.INVISIBLE);
		} else {
			paging_left_btn.setVisibility(View.VISIBLE);
		}

	}

	class GridViewAdpter extends BaseAdapter {
		private List<String> images;
		private Context context;
		private DeleteListener deleteListener;

		public GridViewAdpter(List<String> images, Context context) {
			this.images = images;
			this.context = context;
			deleteListener = new DeleteListener(images);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (images == null || images.size() == 0) {
				return 1;
			} else if (images.size() < 9) {
				return images.size() + 1;
			} else {
				return images.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return images.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.op_gridview_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.item_imageView);
				holder.delete_photo_btn = (Button) convertView.findViewById(R.id.delete_photo_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.delete_photo_btn.setOnClickListener(null);
			holder.delete_photo_btn.setTag(position);

			if (images == null || images.size() == 0) {
				holder.imageView.setImageResource(R.drawable.add_pic_btn);
				Glide.with(context).load(R.drawable.add_pic_btn).centerCrop().into(holder.imageView);
				holder.delete_photo_btn.setVisibility(View.GONE);
			} else if (images.size() < 9) {
				if (images.size() - position == 0) {
					Glide.with(context).load(R.drawable.add_pic_btn).centerCrop().into(holder.imageView);
					holder.delete_photo_btn.setVisibility(View.GONE);
				} else {
					holder.delete_photo_btn.setVisibility(View.VISIBLE);
					Glide.with(context).load(images.get(position)).placeholder(R.mipmap.imageselector_photo)
							.centerCrop().into(holder.imageView);
				}
			} else {
				holder.delete_photo_btn.setVisibility(View.VISIBLE);
				Glide.with(context).load(images.get(position)).placeholder(R.mipmap.imageselector_photo).centerCrop()
						.into(holder.imageView);
			}

			holder.delete_photo_btn.setOnClickListener(deleteListener);

			return convertView;
		}

		class ViewHolder {
			public ImageView imageView;
			public Button delete_photo_btn;
		}

		class DeleteListener implements OnClickListener {
			List<String> images;

			public DeleteListener(List<String> images) {
				this.images = images;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub.
				int position = (Integer) v.getTag();
				images.remove(position);
				tempSelects.clear();
				tempSelects.addAll(images);
				GridViewAdpter.this.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// if (arg2 == 0) {
		// Log.i(TAG, "-----------------" + arg2);
		// // picSelectWindow.showAtLocation(findViewById(R.id.partent),
		// // Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
		// // 0);
		// openSelectorPic();
		// }

		if (selectorPicPaths == null || selectorPicPaths.size() == 0) {
			if (arg2 == 0)
				openSelectorPic();
		} else if (selectorPicPaths.size() < 9) {
			if (arg2 == selectorPicPaths.size()) {
				openSelectorPic();
			} else {
				imageBrower(arg2, selectorPicPaths);
			}
		} else {
			imageBrower(arg2, selectorPicPaths);
		}

	}

	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_LOCAL, true);
		startActivity(intent);
	}

	public static final int VIDEO_REQUEST_CODE = 10;
	public static final String KEY_FILE_PATH = "video_path";
	public static final int VIDEO_RESULT_CODE = 11;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_video_btn:
			Intent intent = new Intent(this, NewRecordVideoActivity.class);
			startActivityForResult(intent, VIDEO_REQUEST_CODE);
			break;
		case R.id.video_view:
			videoView.stop();
			playImageView.setVisibility(View.VISIBLE);
			thumbnailImageView.setVisibility(View.VISIBLE);
			break;
		case R.id.playImageView:
			try {
				videoView.setDataSource(videoPath);
				videoView.setLooping(true);
				videoView.prepare();
				videoView.start();
				playImageView.setVisibility(View.GONE);
				thumbnailImageView.setVisibility(View.GONE);
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
				Toast.makeText(this, "播放视频异常", Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub
		if (res.getInfCode() == InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL) {
			int code = res.getCode();
			if (code == 1) {
				List<UserStation> temps = (List<UserStation>) res.getObject();
				if (temps != null && temps.size() > 0) {
					stations.clear();
					stations.addAll(temps);
					stationAdpter.notifyDataSetChanged();
					stationSpinner.setSelection(0, true);
					deviceTypeAdpter.clear();
					deviceList.clear();
					for (UserStation station : stations) {
						deviceTypeList.addAll(station.getStation().getDeviceTypeList());
						for (DeviceType deviceType : station.getStation().getDeviceTypeList()) {
							deviceList.addAll(deviceType.getDeviceList());
						}
					}
					deviceTypeAdpter.notifyDataSetChanged();
					addRadioView();
				}
				viewPartent.setVisibility(View.VISIBLE);

			} else {
				showToast(res.getMessage());
			}

		} else if (res.getInfCode() == InterfaceFinals.INF_UP_LOAP_OP_PICTURE) {
			if (res.getCode() == 1) {
				PictureVo pictureVo = (PictureVo) res.getObject();
				if (resultPics != null) {
					resultPics.add(pictureVo);
					Log.i(TAG, "--------------------------" + resultPics.size());
				}
				if (!StringUtils.isNull(videoPath)) {
					if (resultPics.size() == selectorPicPaths.size() && resultVideoPath != null) {
						subimtData(getUserData());
					}
				} else if (resultPics.size() == selectorPicPaths.size()) {
					subimtData(getUserData());
				}
			} else {
				showToast(res.getMessage());
				resultPics.clear();
				submitDialog.dismiss();
				// showToast("提交数据失败！");
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_UP_LOAP_OP_VIDEO) {
			if (res.getCode() == 1) {
				resultVideoPath = (String) res.getObject();
				Log.i(TAG, "--------------------------" + resultVideoPath);
				if (selectorPicPaths != null && selectorPicPaths.size() > 0) {
					if (resultPics.size() == selectorPicPaths.size() && resultVideoPath != null) {
						subimtData(getUserData());
					}
				} else if (resultVideoPath != null) {
					subimtData(getUserData());
				}

			} else {
				showToast(res.getMessage());
				resultVideoPath = null;
				// showToast("提交数据失败！");
				submitDialog.dismiss();
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_SAVEA_QUESSION) {
			submitDialog.dismiss();
			if (res.getCode() == 1) {
				// res.getObject();
				showToast("新建成功！");
				Question resultQuestion = (Question) res.getObject();
				// Station selectStation = null;
				// DeviceType selecDeviceType = null;
				// if (selectDevice != null) {
				// for (Station station : stations) {
				// if (selectDevice.getStationId().longValue() ==
				// station.getTid().longValue()) {
				// selectStation = station;
				// break;
				// }
				// }
				// for (DeviceType deviceType : deviceTypeList) {
				// if (selectDevice.getDeviceTypeId().longValue() ==
				// deviceType.getTid().longValue()) {
				// selecDeviceType = deviceType;
				// break;
				// }
				// }
				if (resultQuestion != null) {
					// if (resultQuestion != null && selectStation != null) {
					resultQuestion.setUser(getUserData());
					// resultQuestion.setStation(selectStation);
					// resultQuestion.setDevice(selectDevice);
					// resultQuestion.setDeviceType(selecDeviceType);
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					Bundle bundle = new Bundle();
					bundle.putSerializable(MyyunweiActivity.QUESTION_KEY, resultQuestion);
					bundle.putInt(OPDetailsActivity.TODO, MyyunweiActivity.TO_ADD);
					intent.putExtras(bundle);
					intent.setAction(MyyunweiActivity.UPDATEQUESTIONFLAG);
					sendBroadcast(intent);
				}
				// }
				// }

				this.finish();
			} else {
				showToast(res.getMessage());
			}
		}

	}

	@Override
	public void onCancel(BaseModel res) {
		// TODO Auto-generated method stub
		super.onCancel(res);
		submitDialog.dismiss();
	}

	@Override
	public void onFail(BaseModel res) {
		// TODO Auto-generated method stub
		super.onFail(res);
		submitDialog.dismiss();
		String failInf = res.getMessage();
		if ("未登陆".equals(failInf)) {
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		stations = new ArrayList<UserStation>();
		deviceTypeList = new ArrayList<DeviceType>();
		deviceList = new ArrayList<Device>();
		stationAdpter = new StationAdpter(this, R.layout.item_spinner, stations);
		deviceTypeAdpter = new DeviceTypeAdpter(this, R.layout.item_spinner, deviceTypeList);
		findStationList();

	}

	private void findStationList() {
		Type t = new TypeToken<BaseModel<List<UserStation>>>() {
		}.getType();
		BaseAsyncTask<List<UserStation>> asyncTask = new BaseAsyncTask<List<UserStation>>(this, t,
				InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", "0");
		params.put("pageSize", "1000");
		params.put("token", getUserData().getToken());
		params.put("status", "2");
		asyncTask.execute(params);
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		viewPartent = (LinearLayout) findViewById(R.id.viewPartent);
		op_horizontal_view = (OPHorizontalScrollView) findViewById(R.id.op_horizontal_view);
		op_radioGroup = (RadioGroup) findViewById(R.id.op_radioGroup);
		paging_left_btn = (ImageButton) findViewById(R.id.paging_left_btn);
		paging_right_btn = (ImageButton) findViewById(R.id.paging_right_btn);
		take_photo_gridView = (NestGridView) findViewById(R.id.take_photo_gridView);
		add_video_btn = (ImageView) findViewById(R.id.add_video_btn);
		play_framelayout = (RelativeLayout) findViewById(R.id.play_framelayout);
		videoView = (ScalableVideoView) findViewById(R.id.video_view);
		thumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
		selectOpNameTextView = (TextView) findViewById(R.id.selectOpNameTextView);
		playImageView = (ImageView) findViewById(R.id.playImageView);
		videoView.setOnClickListener(this);
		try {
			// 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
			videoView.setDataSource("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		thumbnailImageView.setOnClickListener(this);
		playImageView.setOnClickListener(this);
		add_video_btn.setOnClickListener(this);
		op_horizontal_view.setChangedListener(this);
		// take_photo_gridView.setSelector(android.R.color.transparent);
		gridViewAdpter = new GridViewAdpter(selectorPicPaths, this);
		take_photo_gridView.setAdapter(gridViewAdpter);
		take_photo_gridView.setOnItemClickListener(this);
		viewPartent.setVisibility(View.GONE);
		stationSpinner = (Spinner) findViewById(R.id.spin_province);
		deciveSpinner = (Spinner) findViewById(R.id.spin_city);
		title_editText = (EditText) findViewById(R.id.title_editText);
		trouble_describe_editText = (EditText) findViewById(R.id.trouble_describe_editText);
		count_textView = (TextView) findViewById(R.id.count_textView);

	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		stationSpinner.setAdapter(stationAdpter);
		deciveSpinner.setAdapter(deviceTypeAdpter);
		stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					deviceTypeAdpter.clear();
					deviceList.clear();
					for (UserStation station : stations) {
						deviceTypeList.addAll(station.getStation().getDeviceTypeList());
						for (DeviceType deviceType : station.getStation().getDeviceTypeList()) {
							deviceList.addAll(deviceType.getDeviceList());
						}
					}
					deviceTypeAdpter.notifyDataSetChanged();
				} else {
					UserStation tempStation = stationAdpter.getItem(arg2);
					if (tempStation != null) {
						deviceTypeAdpter.clear();
						deviceList.clear();
						deviceTypeAdpter.addAll(tempStation.getStation().getDeviceTypeList());
						for (DeviceType deviceType : tempStation.getStation().getDeviceTypeList()) {
							deviceList.addAll(deviceType.getDeviceList());
						}
						deviceTypeAdpter.notifyDataSetChanged();
					}
				}
				addRadioView();
				deciveSpinner.setSelection(0, true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		deciveSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					deviceList.clear();
					for (DeviceType deviceType : deviceTypeList) {
						deviceList.addAll(deviceType.getDeviceList());
					}
				} else {
					DeviceType tempDeviceType = deviceTypeAdpter.getItem(arg2);
					deviceList.clear();
					deviceList.addAll(tempDeviceType.getDeviceList());
				}
				addRadioView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		stationSpinner.setSelection(0, true);
		op_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId < 1) {
					return;
				}
				if (null != deviceList && deviceList.size() > 0&&checkedId<=deviceList.size()) {
					selectDevice = deviceList.get(checkedId - 1);
					if (selectDevice != null) {
						selectOpNameTextView.setText(selectDevice.getName());
					} else {
						selectOpNameTextView.setText("");
					}
				}
			}
		});

		InputFilter filters[] = new InputFilter[] { new EmojiFilter(), new InputFilter.LengthFilter(40) };
		InputFilter defilters[] = new InputFilter[] { new EmojiFilter(), new InputFilter.LengthFilter(200) };
		title_editText.setFilters(filters);
		trouble_describe_editText.setFilters(defilters);
		trouble_describe_editText.setFadingEdgeLength(200);

		trouble_describe_editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				count_textView.setText(trouble_describe_editText.getText().toString().length() + "/" + 200);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		submitDialog = new ProgressDialog(this);
		submitDialog.setMessage("正在提交，请稍候...");
		submitDialog.setCanceledOnTouchOutside(false);
		int left = (int) getResources().getDimension(R.dimen.dim10);
		int withSize = (getWindowWith() - left * 8) / 3;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(withSize, withSize);
		int size = (int) getResources().getDimension(R.dimen.dim20);
		params.setMargins(left, size, size, 0);
		add_video_btn.setLayoutParams(params);
		float fwith = withSize * 4f / 3f;
		LinearLayout.LayoutParams reParams = new LinearLayout.LayoutParams((int) fwith, withSize);
		reParams.setMargins(left, size, size, 0);
		play_framelayout.setLayoutParams(reParams);
	}

	class StationAdpter extends ArrayAdapter<UserStation> {
		Context context;
		List<UserStation> stations;

		public StationAdpter(Context context, int resource, List<UserStation> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			this.stations = objects;
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.drop_spinner_style, null);
			}

			textView = (TextView) convertView;

			if (position == 0) {
				textView.setText("全部");
			} else {
				textView.setText(stations.get(position - 1).getStation().getName());
			}

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stations == null ? 1 : stations.size() + 1;
		}

		@Override
		public UserStation getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return null;
			}
			return stations.get(position - 1);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
				textView = (TextView) convertView.findViewById(R.id.item_spinner);
				convertView.setTag(textView);
			}
			textView = (TextView) convertView.getTag();

			if (position == 0) {
				textView.setText("全部");
			} else {
				textView.setText(stations.get(position - 1).getStation().getName());
			}
			return convertView;
		}
	}

	class DeviceTypeAdpter extends ArrayAdapter<DeviceType> {
		Context context;
		List<DeviceType> deviceTypes;

		public DeviceTypeAdpter(Context context, int resource, List<DeviceType> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			this.deviceTypes = objects;
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.drop_spinner_style, null);
			}

			textView = (TextView) convertView;

			if (position == 0) {
				textView.setText("全部");
			} else {
				textView.setText(deviceTypes.get(position - 1).getName());
			}

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return deviceTypes == null ? 1 : deviceTypes.size() + 1;
		}

		@Override
		public DeviceType getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return null;
			}
			return deviceTypes.get(position - 1);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
				textView = (TextView) convertView.findViewById(R.id.item_spinner);
				convertView.setTag(textView);
			}
			textView = (TextView) convertView.getTag();

			if (position == 0) {
				textView.setText("全部");
			} else {
				textView.setText(deviceTypes.get(position - 1).getName());
			}
			return convertView;
		}
	}

	public void submitOnclick(View view) {
		if (inspectTerm()) {
			submitDialog.show();
			if (StringUtils.isNull(videoPath) && (selectorPicPaths == null || selectorPicPaths.size() == 0)) {
				subimtData(getUserData());
			} else {
				uploadPics();
				upLoadVideo();
			}
		}
	}

	/**
	 * 检查需上传项是否合格
	 */
	private boolean inspectTerm() {
		boolean isQualified = true;
		String title = title_editText.getText().toString();
		String describe = trouble_describe_editText.getText().toString();
		if (title.length() == 0) {
			showToast("请输入标题！");
			isQualified = false;
		} else if (title.length() > 40) {
			showToast("标题字符不超过40个字！");
			isQualified = false;
		} else if (describe.length() == 0) {
			showToast("请输入描述！");
			isQualified = false;
		} else if (describe.length() > 200) {
			showToast("描述字符不超过200个字符！");
			isQualified = false;
		} else if (selectDevice == null) {
			showToast("请选择故障设备！");
			isQualified = false;
		} else if (getUserData() == null) {
			showToast("请登录后再提交！");
			isQualified = false;
		}
		// else if ((selectorPicPaths == null || selectorPicPaths.size() == 0)
		// && (videoPath == null || videoPath.length() == 0)) {
		// showToast("请拍摄视频或者图片！");
		// isQualified = false;
		// }
		return isQualified;
	}

	private void uploadPics() {
		resultPics.clear();
		isExec = false;
		Type t = new TypeToken<BaseModel<PictureVo>>() {
		}.getType();

		HashMap<String, String> map = new HashMap<String, String>();
		UserObj user = getUserData();
		map.put("token", user.getToken());
		if (selectorPicPaths != null && selectorPicPaths.size() > 0) {
			for (String str : selectorPicPaths) {
				BaseAsyncTask<BaseModel<PictureVo>> task = new BaseAsyncTask<BaseModel<PictureVo>>(this, t,
						InterfaceFinals.INF_UP_LOAP_OP_PICTURE, false);
				HashMap<String, String> filemap = new HashMap<String, String>();
				filemap.put("photoFile", str);
				task.execute(map, filemap);
			}
		}

	}

	private void upLoadVideo() {

		if (!StringUtils.isNull(videoPath)) {
			Type ty = new TypeToken<BaseModel<String>>() {
			}.getType();
			HashMap<String, String> map = new HashMap<String, String>();
			UserObj user = getUserData();
			map.put("token", user.getToken());
			BaseAsyncTask<BaseModel<String>> videTask = new BaseAsyncTask<BaseModel<String>>(this, ty,
					InterfaceFinals.INF_UP_LOAP_OP_VIDEO, false);
			HashMap<String, String> filemap = new HashMap<String, String>();
			filemap.put("videoFile", videoPath);
			videTask.execute(map, filemap);
		}
	}

	boolean isExec = false;

	private synchronized void subimtData(UserObj user) {
		Log.i("OP", "执行-----------------------------------");
		if (isExec) {
			return;
		}
		Log.i("OP", "执行-------------------1----------------");
		Type t = new TypeToken<BaseModel<Question>>() {
		}.getType();

		BaseAsyncTask<BaseModel<PictureVo>> task = new BaseAsyncTask<BaseModel<PictureVo>>(this, t,
				InterfaceFinals.INF_SAVEA_QUESSION, false);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", title_editText.getText().toString());
		map.put("description", trouble_describe_editText.getText().toString());
		map.put("stationId", selectDevice.getStationId() + "");
		map.put("deviceTypeId", selectDevice.getDeviceTypeId() + "");
		map.put("deviceId", selectDevice.getTid() + "");
		String photos = "";
		for (PictureVo vo : resultPics) {
			photos = photos + vo.getTid() + ",";
		}
		if (StringUtils.isNull(photos)) {
			map.put("photos", "");
		} else {
			map.put("photos", photos.substring(0, photos.length()));
		}
		if (StringUtils.isNull(resultVideoPath)) {
			map.put("video", "");
		} else {
			map.put("video", resultVideoPath);
		}
		map.put("token", user.getToken());
		task.execute(map);
		isExec = true;
	}

	public void delete_video_btn(View view) {
		videoPath = "";
		if (videoView.isPlaying()) {
			videoView.stop();
		}
		play_framelayout.setVisibility(View.GONE);
		add_video_btn.setVisibility(View.VISIBLE);
	}
}
