package com.sptd.eyun.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Picture;
import com.sptd.eyun.obj.PictureVo;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.Reply;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.ui.myinfo.MyyunweiActivity;
import com.sptd.eyun.widget.DeleteDialog;
import com.sptd.eyun.widget.NestGridView;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.net.HttpDownLoad;
import com.sptd.net.HttpDownLoad.LoadListener;
import com.sptd.net.ModifyBaseAsyncTask;
import com.sptd.util.EmojiFilter;
import com.sptd.util.GlideLoader;
import com.sptd.util.StringUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yqritc.scalablevideoview.ScalableVideoView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditQuestionOrReplyActivity extends BaseInteractActivity implements OnItemClickListener, OnClickListener {
	public static final int REQUEST_CODE = 1000;
	public static final String key = "editType";
	public static final String QUESTION = "question";
	public static final String REPLY = "reply";
	private Question question = null;
	private Reply reply = null;

	public static enum EDIT_TYPE {
		EDIT_QUESSTION, EDIT_REPLY
	}

	private EDIT_TYPE edit_type;

	private Button titleDeleteBtn;
	private LinearLayout questionLayout;
	private EditText describe_editText;
	private TextView titleTextView;

	/**
	 * Quesstion
	 */

	private TextView station_device_name_textView;
	private TextView question_titleTextView;
	private Spinner spin_province;
	private StateAdpter stateAdpter;
	private final String states[] = new String[] { "未解决", "已解决" };

	/**
	 * 通用
	 */

	TextView count_textView;
	private GridViewAdpter gridViewAdpter;
	private NestGridView take_photo_gridView;
	private List<Picture> remoteImages = new ArrayList<Picture>();
	private ArrayList<LocalImage> localImages = new ArrayList<LocalImage>();
	private ArrayList<String> locaTemp = new ArrayList<String>();

	private RelativeLayout play_framelayout;
	private ScalableVideoView videoView;
	private ImageView thumbnailImageView;
	private ImageView playImageView;
	private ProgressBar progress_view;
	private ImageView add_video_btn;

	private String remoteVideoUrl;
	private String localVideoUrl;
	public static final int VIDEO_REQUEST_CODE = 10;
	public static final String KEY_FILE_PATH = "video_path";
	public static final int VIDEO_RESULT_CODE = 11;

	private int video_up_type;
	private int image_up_type;
	private ProgressDialog submitDialog;

	class LocalImage {
		String locapath;
		long contentId;
	}

	public EditQuestionOrReplyActivity() {
		super(R.layout.activity_editquestionorreply);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub
		if (res.getInfCode() == video_up_type) {
			if (res.getCode() == 1) {
				remoteVideoUrl = (String) res.getObject();
				if (localImages != null && localImages.size() > 0) {
					if (needPostImgs.size() == localImages.size() && remoteVideoUrl != null) {
						if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO
								&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE) {
							subimtData(getUserData());
						} else if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY
								&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY) {
							subimtDataReply(getUserData());
						}
					}
				} else {
					if (remoteVideoUrl != null) {
						if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO
								&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE) {
							subimtData(getUserData());
						} else if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY
								&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY) {
							subimtDataReply(getUserData());
						}
					}
				}

			} else {
				showToast(res.getMessage());
				remoteVideoUrl = "";
				// showToast("提交数据失败！");
				submitDialog.dismiss();
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_UPDATA_QUESTION) {
			submitDialog.dismiss();
			if (res.getCode() == 1) {
				// res.getObject();
				showToast("修改成功！");

				Question resultQuestion = (Question) res.getObject();
				if (resultQuestion != null) {
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					Bundle bundle = new Bundle();
					bundle.putSerializable(MyyunweiActivity.QUESTION_KEY, resultQuestion);
					bundle.putInt(MyyunweiActivity.TODO, MyyunweiActivity.TO_UPDATA);
					intent.putExtras(bundle);
					intent.setAction(MyyunweiActivity.UPDATEQUESTIONFLAG);
					sendBroadcast(intent);
				}
				this.finish();
			} else {
				showToast(res.getMessage());
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_UPDATA_REPLY) {
			submitDialog.dismiss();
			if (res.getCode() == 1) {
				// res.getObject();
				showToast("修改成功！");
				Reply resultReply = (Reply) res.getObject();
				if (resultReply != null) {
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					Bundle bundle = new Bundle();
					bundle.putSerializable(OPDetailsActivity.REPLY_KEY, resultReply);
					bundle.putInt(OPDetailsActivity.TODO, OPDetailsActivity.TO_UPDATA);
					intent.putExtras(bundle);
					intent.setAction(OPDetailsActivity.UPDATEREPLYFLAG);
					sendBroadcast(intent);
				}
				this.finish();
			} else {
				showToast(res.getMessage());
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_MODIFY_QUESTION_STATE) {
			if (res.getCode() == 1) {
				// res.getObject();
				showToast("删除成功！");
				Question resultQuestion = (Question) res.getObject();
				if (resultQuestion != null) {
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					Bundle bundle = new Bundle();
					bundle.putSerializable(MyyunweiActivity.QUESTION_KEY, resultQuestion);
					bundle.putInt(MyyunweiActivity.TODO, MyyunweiActivity.TO_DETELE);
					intent.putExtras(bundle);
					intent.setAction(MyyunweiActivity.UPDATEQUESTIONFLAG);
					sendBroadcast(intent);
				}
				this.finish();
			} else {
				showToast(res.getMessage());
			}

		}
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			edit_type = (EDIT_TYPE) bundle.getSerializable(key);
		}
		if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
			question = (Question) bundle.getSerializable(QUESTION);
			List<Picture> temps = question.getPictureList();
			/**
			 * 可删除
			 */
			StringBuffer sBuffer=new StringBuffer();
			for(Picture picture:temps){
				sBuffer.append(picture.getIconUrl()+"____");
			}
			Log.v("sBuffer", sBuffer.toString());
			
			/**
			 * 可删除
			 */
			if (temps != null) {
				remoteImages.addAll(temps);
			}

			remoteVideoUrl = question.getVideo();
		} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
			reply = (Reply) bundle.getSerializable(REPLY);
			List<Picture> temp = reply.getPictureList();
			remoteImages.addAll(temp);
			remoteVideoUrl = reply.getVideo();
		}
	}

	private void refreshQuestion() {
		titleDeleteBtn.setVisibility(View.VISIBLE);
		questionLayout.setVisibility(View.VISIBLE);
		describe_editText.setHint("请详细描述你的设备故障信息");
		titleTextView.setText("运维编辑");
		if (question != null) {
			// station_device_name_textView.setText(question.getStation().getName()
			// + ">"
			// + question.getDeviceType().getName() + ">" +
			// question.getDevice().getName());
			if (question.getStation() == null && question.getDeviceType() == null && question.getDevice() == null) {
				station_device_name_textView.setText("");
				station_device_name_textView.setVisibility(View.GONE);
			} else {
				station_device_name_textView.setVisibility(View.VISIBLE);
				String buffer = null;
				if (question.getStation() != null) {
					buffer = question.getStation().getName();
				}
				if (question.getDeviceType() != null) {
					if (StringUtils.isNull(buffer)) {
						buffer = question.getDeviceType().getName();
					} else {
						buffer = buffer + ">" + question.getDeviceType().getName();
					}
				}
				if (question.getDevice() != null) {
					if (StringUtils.isNull(buffer)) {
						buffer = question.getDevice().getName();
					} else {
						buffer = buffer + ">" + question.getDevice().getName();
					}
				}
				station_device_name_textView.setText(buffer);
			}

			question_titleTextView.setText(question.getName());
			stateAdpter = new StateAdpter(this, R.layout.item_spinner, states);
			spin_province.setAdapter(stateAdpter);
			spin_province.setSelection(question.getState());
			describe_editText.setText(question.getDescription());
			describe_editText.setSelection(question.getDescription().length());
		}
		video_up_type = InterfaceFinals.INF_UP_LOAP_OP_VIDEO;
		image_up_type = InterfaceFinals.INF_UP_LOAP_OP_PICTURE;
	}

	private void refreshReply() {
		titleDeleteBtn.setVisibility(View.GONE);
		questionLayout.setVisibility(View.GONE);
		describe_editText.setHint("说点什么吧");
		titleTextView.setText("回帖");
		if (reply != null) {
			describe_editText.setText(reply.getContent());
			describe_editText.setSelection(reply.getContent().length());
		}
		video_up_type = InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY;
		image_up_type = InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY;
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		titleDeleteBtn = (Button) findViewById(R.id.titleDeleteBtn);
		questionLayout = (LinearLayout) findViewById(R.id.questionLayout);
		describe_editText = (EditText) findViewById(R.id.describe_editText);
		titleTextView = (TextView) findViewById(R.id.title_textView);
		station_device_name_textView = (TextView) findViewById(R.id.station_device_name_textView);
		question_titleTextView = (TextView) findViewById(R.id.question_titleTextView);
		spin_province = (Spinner) findViewById(R.id.spin_province);
		count_textView = (TextView) findViewById(R.id.count_textView);
		take_photo_gridView = (NestGridView) findViewById(R.id.take_photo_gridView);

		play_framelayout = (RelativeLayout) findViewById(R.id.play_framelayout);
		videoView = (ScalableVideoView) findViewById(R.id.video_view);
		thumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
		playImageView = (ImageView) findViewById(R.id.playImageView);
		progress_view = (ProgressBar) findViewById(R.id.progress_view);
		add_video_btn = (ImageView) findViewById(R.id.add_video_btn);

	}

	// public void backEvent(View view) {
	// finish();
	// }

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
				EditQuestionOrReplyActivity.this.finish();
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		InputFilter filters[] = new InputFilter[] { new EmojiFilter(), new InputFilter.LengthFilter(200) };
		describe_editText.setFilters(filters);
		describe_editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				count_textView.setText(describe_editText.getText().toString().length() + "/" + 200);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});
		if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
			refreshQuestion();
		} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
			refreshReply();
		} else {
			finish();
		}
		gridViewAdpter = new GridViewAdpter(remoteImages, localImages, this);
		take_photo_gridView.setAdapter(gridViewAdpter);
		take_photo_gridView.setOnItemClickListener(this);
		add_video_btn.setOnClickListener(this);
		playImageView.setOnClickListener(this);
		videoView.setOnClickListener(this);
		try {
			videoView.setDataSource("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		submitDialog = new ProgressDialog(this);
		submitDialog.setMessage("正在提交，请稍候...");
		submitDialog.setCanceledOnTouchOutside(false);
		if (StringUtils.isNull(remoteVideoUrl)) {
			play_framelayout.setVisibility(View.GONE);
			add_video_btn.setVisibility(View.VISIBLE);
		} else {
			add_video_btn.setVisibility(View.GONE);
			play_framelayout.setVisibility(View.VISIBLE);
			Glide.with(this).load(InterfaceFinals.URL_HEAD + remoteVideoUrl + ".jpg").centerCrop()
					.into(thumbnailImageView);

		}
		int size = (int) getResources().getDimension(R.dimen.dim10);
		int left = (int) getResources().getDimension(R.dimen.dim30);
		int withSize = (getWindowWith() - size * 8) / 3;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(withSize, withSize);
		params.setMargins(left, left, left, 0);
		add_video_btn.setLayoutParams(params);
		float fwith = withSize * 4f / 3f;
		LinearLayout.LayoutParams reParams = new LinearLayout.LayoutParams((int) fwith, withSize);
		reParams.setMargins(left, size, size, 0);
		play_framelayout.setLayoutParams(reParams);

	}

	private int getWindowWith() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public void title_delete_btn(View view) {

		DeleteDialog.Builder builder = new DeleteDialog.Builder(this);
		builder.setMessage("确定要执行删除操作吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				Type t = new TypeToken<BaseModel<Question>>() {
				}.getType();
				BaseAsyncTask<Question> asyncTask = new BaseAsyncTask<Question>(EditQuestionOrReplyActivity.this, t,
						InterfaceFinals.INF_MODIFY_QUESTION_STATE, true);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("modelId", question.getTid() + "");
				params.put("token", EditQuestionOrReplyActivity.this.getUserData().getToken());
				params.put("status", 3 + "");
				asyncTask.execute(params);
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

			localImages.clear();
			for (String path : pathList) {
				Log.i("ImagePathList", path);
				LocalImage image = new LocalImage();
				image.locapath = path;
				localImages.add(image);
			}
			gridViewAdpter.notifyDataSetChanged();
		} else if (requestCode == VIDEO_REQUEST_CODE && resultCode == VIDEO_RESULT_CODE && data != null) {
			localVideoUrl = data.getStringExtra(KEY_FILE_PATH);
			if (TextUtils.isEmpty(localVideoUrl)) {
				play_framelayout.setVisibility(View.GONE);
				add_video_btn.setVisibility(View.VISIBLE);
				Toast.makeText(this, "视频路径错误", Toast.LENGTH_SHORT).show();
				return;
			}
			// 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
			play_framelayout.setVisibility(View.VISIBLE);
			add_video_btn.setVisibility(View.GONE);
			playImageView.setVisibility(View.VISIBLE);
			thumbnailImageView.setVisibility(View.VISIBLE);
			try {
				videoView.setDataSource(localVideoUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Glide.with(this).load(localVideoUrl).centerCrop().into(thumbnailImageView);

		}
	}

	class StateAdpter extends ArrayAdapter<String> {
		Context context;
		String[] state;

		public StateAdpter(Context context, int resource, String[] state) {
			super(context, resource, state);
			// TODO Auto-generated constructor stub
			this.state = state;
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

			textView.setText(state[position]);

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return state == null ? 0 : state.length;
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return state[position];
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
			textView.setText(state[position]);
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (remoteImages.size() + localImages.size() == 0) {
			if (arg2 == 0)
				openSelectorPic(9);
		} else if (remoteImages.size() + localImages.size() < 9) {
			if (arg2 == remoteImages.size() + localImages.size()) {
				openSelectorPic(9 - remoteImages.size() < 0 ? 0 : 9 - remoteImages.size());
			} else {
				imageBrower(arg2, getRLURL());
			}
		} else {
			imageBrower(arg2, getRLURL());
		}

	}

	private ArrayList<String> getRLURL() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (Picture picture : remoteImages) {
			arrayList.add(InterfaceFinals.URL_HEAD + picture.getUrl());
		}
		for (LocalImage localImage : localImages) {
			arrayList.add("file://" + localImage.locapath);
		}
		return arrayList;

	}

	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerRLActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerRLActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	private void openSelectorPic(int size) {
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
						.mutiSelectMaxSize(size)
						// 已选择的图片路径
						.pathList(locaTemp)
						// 拍照后存放的图片路径（默认 /temp/picture）
						.filePath("/ImageSelector/Pictures")
						// 开启拍照功能 （默认开启）
						.showCamera().requestCode(REQUEST_CODE).build();
		ImageSelector.open(this, imageConfig); // 开启图片选择器
	}

	class GridViewAdpter extends BaseAdapter {
		private List<Picture> remoteImages;
		private List<LocalImage> localImages;
		private Context context;
		private DeleteListener deleteListener;

		public GridViewAdpter(List<Picture> remoteImages, List<LocalImage> localImages, Context context) {
			this.remoteImages = remoteImages;
			this.localImages = localImages;
			this.context = context;
			deleteListener = new DeleteListener(remoteImages, localImages);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			if (remoteImages.size() + localImages.size() == 0) {
				return 1;
			} else if (remoteImages.size() + localImages.size() < 9) {
				return remoteImages.size() + localImages.size() + 1;
			} else {
				return remoteImages.size() + localImages.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position < remoteImages.size()) {
				return remoteImages.get(position);
			} else {

				return localImages.get(position - remoteImages.size());
			}
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

			if (remoteImages.size() + localImages.size() == 0) {
				holder.imageView.setImageResource(R.drawable.add_pic_btn);
				Glide.with(context).load(R.drawable.add_pic_btn).centerCrop().into(holder.imageView);
				holder.delete_photo_btn.setVisibility(View.GONE);
			} else if (remoteImages.size() + localImages.size() < 9) {
				if (remoteImages.size() + localImages.size() - position == 0) {
					Glide.with(context).load(R.drawable.add_pic_btn).centerCrop().into(holder.imageView);
					holder.delete_photo_btn.setVisibility(View.GONE);
				} else {
					holder.delete_photo_btn.setVisibility(View.VISIBLE);
					if (position < remoteImages.size()) {
						Glide.with(context).load(InterfaceFinals.URL_HEAD + remoteImages.get(position).getIconUrl())
								.placeholder(R.mipmap.imageselector_photo).centerCrop().into(holder.imageView);
					} else {
						Glide.with(context).load(localImages.get(position - remoteImages.size()).locapath)
								.placeholder(R.mipmap.imageselector_photo).centerCrop().into(holder.imageView);
					}

				}
			} else {
				holder.delete_photo_btn.setVisibility(View.VISIBLE);
				if (position < remoteImages.size()) {
					Glide.with(context).load(InterfaceFinals.URL_HEAD + remoteImages.get(position).getIconUrl())
							.placeholder(R.mipmap.imageselector_photo).centerCrop().into(holder.imageView);
				} else {
					Glide.with(context).load(localImages.get(position - remoteImages.size()).locapath)
							.placeholder(R.mipmap.imageselector_photo).centerCrop().into(holder.imageView);
				}
			}

			holder.delete_photo_btn.setOnClickListener(deleteListener);

			return convertView;
		}

		class ViewHolder {
			public ImageView imageView;
			public Button delete_photo_btn;
		}

		class DeleteListener implements OnClickListener {
			List<Picture> remoteImages;
			List<LocalImage> localImages;

			public DeleteListener(List<Picture> remoteImages, List<LocalImage> localImages) {
				this.remoteImages = remoteImages;
				this.localImages = localImages;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub.
				int position = (Integer) v.getTag();
				if (position < remoteImages.size()) {
					remoteImages.remove(position);
				} else {
					localImages.remove(position - remoteImages.size());
					locaTemp.remove(position - remoteImages.size());
				}
				GridViewAdpter.this.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_video_btn:
			Intent intent = new Intent(this, NewRecordVideoActivity.class);
			startActivityForResult(intent, VIDEO_REQUEST_CODE);
			break;
		case R.id.video_view:
			String videoUrl = "";
			if (!StringUtils.isNull(localVideoUrl)) {
				videoUrl = localVideoUrl;
			} else if (!StringUtils.isNull(remoteVideoUrl)) {
				if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
					videoUrl = question.getVideoFile().getAbsolutePath();
				} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
					videoUrl = reply.getVideoFile().getAbsolutePath();
				}

			} else {
				showToast("发生未知错误");
				return;
			}
			Intent videoIntent = new Intent(this, PlayVideoActiviy.class);
			videoIntent.putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoUrl);
			startActivity(videoIntent);
			videoView.stop();
			playImageView.setVisibility(View.VISIBLE);
			thumbnailImageView.setVisibility(View.VISIBLE);
			break;
		case R.id.playImageView:
			if (!StringUtils.isNull(remoteVideoUrl)) {
				new HttpDownLoad(this, new LoadListener() {

					@Override
					public void onstart() {
						// TODO Auto-generated method stub
						playImageView.setVisibility(View.GONE);
						progress_view.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSucess(File file) {
						// TODO Auto-generated method stub
						progress_view.setVisibility(View.GONE);
						if (file != null) {
							try {
								if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
									question.setVideoFile(file);
								} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
									reply.setVideoFile(file);
								}
								videoView.setDataSource(file.getAbsolutePath());
								videoView.setLooping(true);
								videoView.prepare();
								videoView.start();
								videoView.setTag(0);
								thumbnailImageView.setVisibility(View.GONE);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								playImageView.setVisibility(View.VISIBLE);
								progress_view.setVisibility(View.GONE);
								thumbnailImageView.setVisibility(View.VISIBLE);
								videoView.setTag(1);
							}
						} else {
							playImageView.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void onLoad(long count, long curCount) {
						// TODO Auto-generated method stub

					}
				}).execute(InterfaceFinals.URL_HEAD + remoteVideoUrl);
			} else {
				if (!StringUtils.isNull(localVideoUrl)) {
					try {
						playImageView.setVisibility(View.GONE);
						videoView.setDataSource(localVideoUrl);
						videoView.setLooping(true);
						videoView.prepare();
						videoView.start();
						videoView.setTag(0);
						thumbnailImageView.setVisibility(View.GONE);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						playImageView.setVisibility(View.VISIBLE);
						progress_view.setVisibility(View.GONE);
						thumbnailImageView.setVisibility(View.VISIBLE);
					}
				} else {
					playImageView.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	public void delete_video_btn(View view) {
		remoteVideoUrl = "";
		localVideoUrl = "";
		if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
			question.setVideoFile(null);
		} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
			reply.setVideoFile(null);
		}
		if (videoView.isPlaying()) {
			videoView.stop();
		}
		play_framelayout.setVisibility(View.GONE);
		add_video_btn.setVisibility(View.VISIBLE);
	}

	public void submitOnclick(View view) {

		if (inspectTerm()) {
			uploadPics(image_up_type, video_up_type);
		}
	}

	private ArrayList<LocalImage> needPostImgs = new ArrayList<LocalImage>();

	@Override
	public void onSuccess(BaseModel res, Object obj) {
		// TODO Auto-generated method stub
		super.onSuccess(res, obj);
		if (res.getCode() == 1) {
			PictureVo pictureVo = (PictureVo) res.getObject();
			LocalImage locaImg = (LocalImage) obj;
			if (pictureVo != null) {
				locaImg.contentId = pictureVo.getTid();
				needPostImgs.add(locaImg);
			}
			if (localVideoUrl != null && localVideoUrl.length() > 0) {
				if (needPostImgs.size() == localImages.size() && remoteVideoUrl != null) {
					if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO
							&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE) {
						subimtData(getUserData());
					} else if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY
							&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY) {
						subimtDataReply(getUserData());
					}
				}
			} else {
				if (needPostImgs.size() == localImages.size()) {
					if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO
							&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE) {
						subimtData(getUserData());
					} else if (video_up_type == InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY
							&& image_up_type == InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY) {
						subimtDataReply(getUserData());
					}
				}
			}
		} else {
			showToast(res.getMessage());
			needPostImgs.clear();
			submitDialog.dismiss();
			// showToast("提交数据失败！");
		}
	}

	private void subimtData(UserObj user) {
		Type t = new TypeToken<BaseModel<Question>>() {
		}.getType();

		BaseAsyncTask<BaseModel<Question>> task = new BaseAsyncTask<BaseModel<Question>>(this, t,
				InterfaceFinals.INF_UPDATA_QUESTION, false);

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("name", question.getName());
		map.put("tid", question.getTid() + "");
		map.put("description", describe_editText.getText().toString());

		map.put("stationId", question.getStation().getTid() + "");
		map.put("deviceTypeId", question.getDeviceType().getTid() + "");
		map.put("deviceId", question.getDevice().getTid() + "");
		map.put("state", spin_province.getSelectedItemPosition() + "");
		String photos = "";
		for (Picture vo : remoteImages) {
			photos = photos + vo.getTid() + ",";
		}
		for (LocalImage vo : needPostImgs) {
			photos = photos + vo.contentId + ",";
		}
		if (StringUtils.isNull(photos)) {
			map.put("photos", "");
		} else {
			map.put("photos", photos.substring(0, photos.length()));
		}
		if (StringUtils.isNull(remoteVideoUrl)) {
			map.put("video", "");
		} else {
			map.put("video", remoteVideoUrl);
		}
		map.put("token", user.getToken());
		task.execute(map);
	}

	@Override
	public void onFail(BaseModel res) {
		// TODO Auto-generated method stub
		super.onFail(res);
		submitDialog.dismiss();
		showToast(res.getMessage());
		String failInf = res.getMessage();
		if ("未登陆".equals(failInf)) {
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
	}

	private void subimtDataReply(UserObj user) {
		Type t = new TypeToken<BaseModel<Reply>>() {
		}.getType();
		BaseAsyncTask<BaseModel<Reply>> task = new BaseAsyncTask<BaseModel<Reply>>(this, t,
				InterfaceFinals.INF_UPDATA_REPLY, false);

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("content", describe_editText.getText().toString());
		if (reply != null) {
			map.put("questionId", reply.getQuestionId() + "");

			map.put("replyId", reply.getReplyId() + "");
		}
		map.put("tid", reply.getTid() + "");
		String photos = "";
		for (Picture vo : remoteImages) {
			photos = photos + vo.getTid() + ",";
		}
		for (LocalImage vo : needPostImgs) {
			photos = photos + vo.contentId + ",";
		}
		if (StringUtils.isNull(photos)) {

			map.put("photos", "");
		} else {

			map.put("photos", photos.substring(0, photos.length()));
		}
		if (StringUtils.isNull(remoteVideoUrl)) {

			map.put("video", "");
		} else {
			map.put("video", remoteVideoUrl);
		}
		map.put("token", user.getToken());
		task.execute(map);
	}

	private void uploadPics(int up_imgType, int up_videoType) {
		submitDialog.show();
		Type t = new TypeToken<BaseModel<PictureVo>>() {
		}.getType();

		HashMap<String, String> map = new HashMap<String, String>();
		UserObj user = getUserData();
		map.put("token", user.getToken());
		if (localImages != null && localImages.size() > 0) {
			for (LocalImage local : localImages) {
				ModifyBaseAsyncTask<BaseModel<PictureVo>> task = new ModifyBaseAsyncTask<BaseModel<PictureVo>>(this, t,
						up_imgType, false, local);
				HashMap<String, String> filemap = new HashMap<String, String>();
				filemap.put("photoFile", local.locapath);
//				filemap.put("folderName", "ceshi");
				task.execute(map, filemap);
			}
		}
		if (!StringUtils.isNull(localVideoUrl)) {
			Type ty = new TypeToken<BaseModel<String>>() {
			}.getType();
			BaseAsyncTask<BaseModel<String>> videTask = new BaseAsyncTask<BaseModel<String>>(this, ty, up_videoType,
					false);
			HashMap<String, String> filemap = new HashMap<String, String>();
			filemap.put("videoFile", localVideoUrl);
			videTask.execute(map, filemap);
		}
		if (StringUtils.isNull(localVideoUrl) && localImages.size() == 0) {
			if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
				subimtData(getUserData());
			} else if (edit_type == EDIT_TYPE.EDIT_REPLY) {
				subimtDataReply(getUserData());
			} else {
				submitDialog.dismiss();
			}

		}

	}

	/**
	 * 检查需上传项是否合格
	 */
	private boolean inspectTerm() {
		boolean isQualified = true;
		String describe = describe_editText.getText().toString();
		if (describe.length() == 0) {
			showToast("请输入描述！");
			isQualified = false;
		} else if (describe.length() > 200) {
			showToast("描述字符不超过200个字符！");
			isQualified = false;
		} else if (getUserData() == null) {
			showToast("请登录后再提交！");
			isQualified = false;
		}
		// if (edit_type == EDIT_TYPE.EDIT_QUESSTION) {
		// if ((remoteImages.size() + localImages.size() == 0)
		// && (StringUtils.isNull(localVideoUrl) &&
		// StringUtils.isNull(remoteVideoUrl))) {
		// showToast("请拍摄视频或者图片！");
		// isQualified = false;
		// }
		// }
		return isQualified;
	}
}
