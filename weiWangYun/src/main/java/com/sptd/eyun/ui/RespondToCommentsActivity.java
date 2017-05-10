package com.sptd.eyun.ui;

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
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.PictureVo;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.Reply;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.widget.NestGridView;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
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
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RespondToCommentsActivity extends BaseInteractActivity implements OnItemClickListener, OnClickListener {

	public RespondToCommentsActivity() {
		super(R.layout.activity_respond_to_comment);
		// TODO Auto-generated constructor stub
	}

	private GridViewAdpter gridViewAdpter;
	private NestGridView take_photo_gridView;
	private ImageView add_video_btn;
	private RelativeLayout play_framelayout;
	private ScalableVideoView videoView;
	private ImageView thumbnailImageView;
	private ImageView playImageView;
	public static final int REQUEST_CODE = 1001;
	private String videoPath;
	private ProgressDialog submitDialog;
	private EditText trouble_describe_editText;
	private TextView count_textView;
	private TextView title_textView;

	private ArrayList<String> selectorPicPaths = new ArrayList<String>();
	private ArrayList<PictureVo> resultPics = new ArrayList<PictureVo>();
	private String resultVideoPath = null;
	private ArrayList<String> tempSelects = new ArrayList<String>();
	private Reply reply = null;
	private Question question = null;

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
				add_video_btn.setVisibility(View.VISIBLE);
			}
			thumbnailImageView.setImageBitmap(Utils.getVideoThumbnail(videoPath));
		}
	}

	public void delete_video_btn(View view) {
		videoPath = "";
		if (videoView.isPlaying()) {
			videoView.stop();
		}
		play_framelayout.setVisibility(View.GONE);
		add_video_btn.setVisibility(View.VISIBLE);
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
		case R.id.respond_add_video_btn:
			Intent intent = new Intent(this, NewRecordVideoActivity.class);
			startActivityForResult(intent, VIDEO_REQUEST_CODE);
			break;
		case R.id.respond_video_view:
			videoView.stop();
			playImageView.setVisibility(View.VISIBLE);
			thumbnailImageView.setVisibility(View.VISIBLE);
			break;
		case R.id.respond_playImageView:
			try {
				videoView.setDataSource(videoPath);
				videoView.setLooping(true);
				videoView.prepare();
				videoView.start();
				playImageView.setVisibility(View.GONE);
				thumbnailImageView.setVisibility(View.GONE);
			} catch (IOException e) {
				Toast.makeText(this, "播放视频异常", Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub
		if (res.getInfCode() == InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY) {
			if (res.getCode() == 1) {
				PictureVo pictureVo = (PictureVo) res.getObject();
				if (resultPics != null) {
					resultPics.add(pictureVo);
				}
				if (videoPath != null && videoPath.length() > 0) {
					if (resultPics.size() == selectorPicPaths.size() && resultVideoPath != null) {
						subimtData(getUserData());
					}
				} else {
					if (resultPics.size() == selectorPicPaths.size()) {
						subimtData(getUserData());
					}
				}
			} else {
				showToast(res.getMessage());
				resultPics.clear();
				submitDialog.dismiss();
				// showToast("提交数据失败！");
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY) {
			if (res.getCode() == 1) {
				resultVideoPath = (String) res.getObject();
				if (selectorPicPaths != null && selectorPicPaths.size() > 0) {
					if (resultPics.size() == selectorPicPaths.size() && resultVideoPath != null) {
						subimtData(getUserData());
					}
				} else {
					if (resultVideoPath != null) {
						subimtData(getUserData());
					}
				}

			} else {
				showToast(res.getMessage());
				videoPath = null;
				// showToast("提交数据失败！");
				submitDialog.dismiss();
			}
		} else if (res.getInfCode() == InterfaceFinals.INF_SAVEA_REPLY) {
			submitDialog.dismiss();
			if (res.getCode() == 1) {
				// res.getObject();
				showToast("发布成功！");
				Reply resultReply = (Reply) res.getObject();
				if (resultReply != null) {
					// if (reply != null) {
					// resultReply.setUser(getUserData());
					// resultReply.setReply(reply);
					// }
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					Bundle bundle = new Bundle();
					bundle.putSerializable(OPDetailsActivity.REPLY_KEY, resultReply);
					bundle.putInt(OPDetailsActivity.TODO, OPDetailsActivity.TO_ADD);
					intent.putExtras(bundle);
					intent.setAction(OPDetailsActivity.UPDATEREPLYFLAG);
					sendBroadcast(intent);
				}
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
		// TODO Auto-generated method stub
		String failInf = res.getMessage();
		if ("未登陆".equals(failInf)) {
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			question = (Question) bundle.getSerializable("question");
			reply = (Reply) bundle.getSerializable("reply");
		}

	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		take_photo_gridView = (NestGridView) findViewById(R.id.respond_take_photo_gridView);
		add_video_btn = (ImageView) findViewById(R.id.respond_add_video_btn);
		play_framelayout = (RelativeLayout) findViewById(R.id.respond_play_framelayout);
		videoView = (ScalableVideoView) findViewById(R.id.respond_video_view);
		thumbnailImageView = (ImageView) findViewById(R.id.respond_thumbnailImageView);
		trouble_describe_editText = (EditText) findViewById(R.id.respond_trouble_describe_editText);
		count_textView = (TextView) findViewById(R.id.respond_count_textView);
		title_textView = (TextView) findViewById(R.id.respond_title_textView);
		playImageView = (ImageView) findViewById(R.id.respond_playImageView);
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
		// take_photo_gridView.setSelector(android.R.color.transparent);
		gridViewAdpter = new GridViewAdpter(selectorPicPaths, this);
		take_photo_gridView.setAdapter(gridViewAdpter);
		take_photo_gridView.setOnItemClickListener(this);

	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		if (reply != null) {
			title_textView.setText("回复  " + reply.getUser().getUserName());
		}
		InputFilter filters[] = new InputFilter[] { new EmojiFilter(), new InputFilter.LengthFilter(200) };
		trouble_describe_editText.setFilters(filters);
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
		int size = (int) getResources().getDimension(R.dimen.dim10);
		// int left = (int) getResources().getDimension(R.dimen.dim30);
		int withSize = (getWindowWith() - size * 8) / 3;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(withSize, withSize);
		params.setMargins(size, size, size, size);
		add_video_btn.setLayoutParams(params);
		float fwith = withSize * 4f / 3f;
		LinearLayout.LayoutParams reParams = new LinearLayout.LayoutParams((int) fwith, withSize);
		reParams.setMargins(size, size, size, size);
		play_framelayout.setLayoutParams(reParams);
	}

	private int getWindowWith() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	class StationAdpter extends ArrayAdapter<Station> {
		Context context;
		List<Station> stations;

		public StationAdpter(Context context, int resource, List<Station> objects) {
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
				textView.setText(stations.get(position - 1).getName());
			}

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stations == null ? 1 : stations.size() + 1;
		}

		@Override
		public Station getItem(int position) {
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
				textView.setText(stations.get(position - 1).getName());
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

	public void title_submit_btn(View view) {
		if (inspectTerm()) {
			uploadPics();
		}
	}

	/**
	 * 检查需上传项是否合格
	 */
	private boolean inspectTerm() {
		boolean isQualified = true;
		String describe = trouble_describe_editText.getText().toString();
		if (describe.length() == 0) {
			showToast("还是说点什么吧！");
			isQualified = false;
		} else if (describe.length() > 200) {
			showToast("内容不超过200个字符！");
			isQualified = false;
		} else if (getUserData() == null) {
			showToast("请登录后再提交！");
			isQualified = false;
		}
		return isQualified;
	}

	private void uploadPics() {
		submitDialog.show();
		HashMap<String, String> map = new HashMap<String, String>();
		UserObj user = getUserData();
		map.put("token", user.getToken());
		if (selectorPicPaths != null && selectorPicPaths.size() > 0) {
			Type t = new TypeToken<BaseModel<PictureVo>>() {
			}.getType();
			for (String str : selectorPicPaths) {
				BaseAsyncTask<BaseModel<PictureVo>> task = new BaseAsyncTask<BaseModel<PictureVo>>(this, t,
						InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY, false);
				HashMap<String, String> filemap = new HashMap<String, String>();
				filemap.put("photoFile", str);
				task.execute(map, filemap);
			}
		}
		if (!StringUtils.isNull(videoPath)) {
			Type ty = new TypeToken<BaseModel<String>>() {
			}.getType();
			BaseAsyncTask<BaseModel<String>> videTask = new BaseAsyncTask<BaseModel<String>>(this, ty,
					InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY, false);
			HashMap<String, String> filemap = new HashMap<String, String>();
			filemap.put("videoFile", videoPath);
			videTask.execute(map, filemap);
		}

		if (StringUtils.isNull(videoPath) && (selectorPicPaths == null || selectorPicPaths.size() == 0)) {

			subimtData(getUserData());
		}

	}

	private void subimtData(UserObj user) {
		Type t = new TypeToken<BaseModel<Reply>>() {
		}.getType();
		BaseAsyncTask<BaseModel<PictureVo>> task = new BaseAsyncTask<BaseModel<PictureVo>>(this, t,
				InterfaceFinals.INF_SAVEA_REPLY, false);

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("content", trouble_describe_editText.getText().toString());
		map.put("questionId", question.getTid() + "");
		if (reply != null) {

			map.put("replyId", reply.getTid() + "");
		}
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
	}

	public void backEvent(View view) {
		finish();
	}
}
