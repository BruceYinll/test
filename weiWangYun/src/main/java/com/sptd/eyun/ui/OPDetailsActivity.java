package com.sptd.eyun.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Picture;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.Reply;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.widget.DeleteDialog;
import com.sptd.eyun.widget.NestGridView;
import com.sptd.eyun.widget.NestListView;
import com.sptd.net.BaseAsyncTask;
import com.sptd.net.HttpDownLoad;
import com.sptd.net.HttpDownLoad.LoadListener;
import com.sptd.net.NetAsyncTask;
import com.sptd.net.NetAsyncTask.CallBack;
import com.sptd.util.StringUtils;
import com.sptd.util.Utils;
import com.yqritc.scalablevideoview.ScalableVideoView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class OPDetailsActivity extends BaseInteractActivity implements OnClickListener, OnItemClickListener {

	private NestGridView photo_gridView;
	private GridViewAdpter gridViewAdpter;
	private List<Picture> pictures;
	private TextView titleTextView;
	private NestListView photo_listView;
	private ListViewAdpter listViewAdpter;
	private List<Reply> replys;
	private Question question;
	private ImageView details_imageView;
	private TextView creatNameTextView;
	private TextView creatTimeTextView;
	private TextView problem_state_textView;
	private TextView title_question_textView;
	private TextView type_textView;
	private TextView contentTextView;

	private FrameLayout play_framelayout;
	private ScalableVideoView mainVideoView;
	private ImageView thumbnailImageView;
	private ImageView playImageView;
	private ProgressBar progress_view;

	PullToRefreshScrollView pullToRefreshScrollView;

	private UpdataReplyBroadCast broadcast = new UpdataReplyBroadCast();
	int curPage = 0;
	int before = curPage;
	public static final int PAGESIZE = 10;

	public OPDetailsActivity() {
		super(R.layout.activity_op_details);
		// TODO Auto-generated constructor stub

	}

	public static final String UPDATEREPLYFLAG = "com.sptd.eyun.ui.update_reply_flag";
	public static final String REPLY_KEY = "reply_key";
	public static final String TODO = "todo";
	public static final int TO_UPDATA = 123;
	public static final int TO_ADD = 124;

	class UpdataReplyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Reply reply = (Reply) bundle.getSerializable(REPLY_KEY);
				int todo = bundle.getInt(TODO);
				if (todo == TO_UPDATA) {
					if (reply != null) {
						int count = listViewAdpter.getCount();
						if (count > 0) {
							for (int i = 0; i < count; i++) {
								final Reply temp = listViewAdpter.getItem(i);
								if (temp.getTid().longValue() == reply.getTid().longValue()) {
									listViewAdpter.replaceItem(temp, reply, i);
									break;
								}
							}
						}
					}
				} else if (todo == TO_ADD) {
					if (reply != null) {
						listViewAdpter.addItem(reply);
					}
				}
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcast);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mainVideoView.isPlaying()) {
			mainVideoView.setTag(true);
			mainVideoView.stop();
		}
		if (listViewAdpter != null) {
			listViewAdpter.setPause(true);
			listViewAdpter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (listViewAdpter != null) {
			listViewAdpter.setPause(false);
			listViewAdpter.notifyDataSetChanged();
		}
		if (mainVideoView.getTag() != null) {
			boolean isPlay = (Boolean) mainVideoView.getTag();
			if (isPlay) {
				try {
					mainVideoView.setLooping(true);
					mainVideoView.prepare();
					mainVideoView.start();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		titleTextView = (TextView) findViewById(R.id.title_textView);
		titleTextView.setText("运维详情");
		photo_gridView = (NestGridView) findViewById(R.id.opdetail_photo_gridView);
		photo_listView = (NestListView) findViewById(R.id.opdetail_photo_listView);
		findViewById(R.id.opdetail_comment_btn).setOnClickListener(this);
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.opdetail_scrollview);
		details_imageView = (ImageView) findViewById(R.id.opdetail_details_imageView);
		creatNameTextView = (TextView) findViewById(R.id.opdetail_creatNameTextView);
		creatTimeTextView = (TextView) findViewById(R.id.opdetail_creatTimeTextView);
		problem_state_textView = (TextView) findViewById(R.id.opdetail_problem_state_textView);
		title_question_textView = (TextView) findViewById(R.id.opdetail_title_question_textView);
		type_textView = (TextView) findViewById(R.id.opdetail_type_textView);
		contentTextView = (TextView) findViewById(R.id.opdetail_contentTextView);

		progress_view = (ProgressBar) findViewById(R.id.opdetail_progress_view);
		play_framelayout = (FrameLayout) findViewById(R.id.opdetail_play_framelayout);
		mainVideoView = (ScalableVideoView) findViewById(R.id.opdetail_video_view);
		thumbnailImageView = (ImageView) findViewById(R.id.opdetail_thumbnailImageView);
		playImageView = (ImageView) findViewById(R.id.opdetail_playImageView);
		pullToRefreshScrollView.setMode(Mode.BOTH);
		pullToRefreshScrollView.setOnPullEventListener(new OnPullEventListener<ScrollView>() {

			@Override
			public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, State state, Mode direction) {
				// TODO Auto-generated method stub
				if (direction == Mode.PULL_FROM_END) {
				} else if (direction == Mode.PULL_FROM_START) {
					refreshView.getLoadingLayoutProxy(true, false)
							.setRefreshingLabel("最后刷新时间" + Utils.formatData(System.currentTimeMillis()));
				}

			}

		});
		pullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				curPage = 0;
				findReplyList(InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_UP);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				curPage++;
				findReplyList(InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_LOAD);

			}
		});

		mainVideoView.setOnClickListener(this);

	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub
		if (res.getInfCode() == InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_UP) {
			pullToRefreshScrollView.onRefreshComplete();
			if (res.getCode() == 1) {
				List<Reply> temp = (List<Reply>) res.getObject();
				replys.clear();
				replys.addAll(temp);
				listViewAdpter.notifyDataSetInvalidated();
				before = curPage;
			} else {
				curPage = before;
				showToast(res.getMessage());
			}

		} else if (res.getInfCode() == InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_LOAD) {
			pullToRefreshScrollView.onRefreshComplete();
			if (res.getCode() == 1) {
				List<Reply> temp = (List<Reply>) res.getObject();
				if (temp.size() == 0) {
					curPage = before;
					return;
				}
				replys.addAll(temp);
				listViewAdpter.notifyDataSetInvalidated();
				before = curPage;
			} else {
				curPage = before;
				showToast(res.getMessage());
			}
		}
	}

	@Override
	public void onFail(BaseModel res) {
		// TODO Auto-generated method stub
		super.onFail(res);
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
		pictures = new ArrayList<Picture>();
		if (bundle != null) {
			question = (Question) bundle.getSerializable("question");
			pictures.clear();
			List<Picture> temps = question.getPictureList();
			if (temps != null && temps.size() > 0) {
				pictures.addAll(temps);
			}
		}
		replys = new ArrayList<Reply>();
		findReplyList(InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_UP);
		IntentFilter filter = new IntentFilter(UPDATEREPLYFLAG);
		registerReceiver(broadcast, filter);
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		try {
			mainVideoView.setDataSource("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Glide.with(this).load(InterfaceFinals.URL_HEAD + question.getUser().getHead()).placeholder(R.drawable.touxiang)
				.centerCrop().into(details_imageView);
		creatNameTextView.setText(question.getUser().getUserName());
		creatTimeTextView.setText(question.getCreateTime());
		if (question.getState() == 1) {
			problem_state_textView.setVisibility(View.VISIBLE);
			problem_state_textView.setText("已解决");
			Drawable greenPoint = getResources().getDrawable(R.drawable.green_point);
			greenPoint.setBounds(0, 0, greenPoint.getMinimumWidth(), greenPoint.getMinimumHeight());
			problem_state_textView.setCompoundDrawables(greenPoint, null, null, null);

			problem_state_textView.setTextColor(getResources().getColor(R.color.green));
		} else if (question.getState() == 0) {
			problem_state_textView.setVisibility(View.VISIBLE);
			problem_state_textView.setText("未解决");
			Drawable readPoint = getResources().getDrawable(R.drawable.red_point);
			readPoint.setBounds(0, 0, readPoint.getMinimumWidth(), readPoint.getMinimumHeight());
			problem_state_textView.setCompoundDrawables(readPoint, null, null, null);

			problem_state_textView.setTextColor(getResources().getColor(R.color.red_red));
		} else {
			problem_state_textView.setVisibility(View.VISIBLE);
		}
		title_question_textView.setText(question.getName());

		if (question.getStation() == null && question.getDeviceType() == null && question.getDevice() == null) {
			type_textView.setText("");
			type_textView.setVisibility(View.GONE);
		} else {
			type_textView.setVisibility(View.VISIBLE);
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
			type_textView.setText(buffer);
		}
		// type_textView.setText(question.getStation().getName() + ">" +
		// question.getDeviceType().getName() + ">"
		// + question.getDevice().getName());
		contentTextView.setText(question.getDescription());
		if (pictures != null) {
			gridViewAdpter = new GridViewAdpter(pictures, this);
			photo_gridView.setAdapter(gridViewAdpter);
		}
		listViewAdpter = new ListViewAdpter(this, replys);
		photo_listView.setAdapter(listViewAdpter);
		String video = question.getVideo();
		if (StringUtils.isNull(video)) {
			play_framelayout.setVisibility(View.GONE);
		} else {
			play_framelayout.setVisibility(View.VISIBLE);
			playImageView.setOnClickListener(this);
			Glide.with(this).load(InterfaceFinals.URL_HEAD + video + ".jpg").centerCrop().into(thumbnailImageView);
		}
		photo_gridView.setOnItemClickListener(this);
	}

	class ListViewAdpter extends BaseAdapter {
		private Context context;
		private List<Reply> replys;
		boolean isPause = false;

		public boolean isPause() {
			return isPause;
		}

		public void setPause(boolean isPause) {
			this.isPause = isPause;
		}

		public void addItem(Reply reply) {
			replys.add(0, reply);
			this.notifyDataSetChanged();
		}

		public void replaceItem(final Reply reply, final Reply tempReply, int position) {
			replys.remove(reply);
			replys.add(position, tempReply);
			this.notifyDataSetChanged();
		}

		public ListViewAdpter(Context context, List<Reply> replys) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.replys = replys;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return replys == null ? 0 : replys.size();
		}

		@Override
		public Reply getItem(int position) {
			// TODO Auto-generated method stub
			return replys.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ItemHolder itemHolder = null;
			if (convertView == null) {
				itemHolder = new ItemHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.detail_list_item, null);
				itemHolder.gridView = (NestGridView) convertView.findViewById(R.id.item_photo_gridView);
				itemHolder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
				itemHolder.playImageView = (ImageView) convertView.findViewById(R.id.playImageView);
				itemHolder.user_imageView = (ImageView) convertView.findViewById(R.id.user_imageView);
				itemHolder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
				itemHolder.creatTimeTextView = (TextView) convertView.findViewById(R.id.creatTimeTextView);
				itemHolder.postionTextView = (TextView) convertView.findViewById(R.id.postionTextView);
				itemHolder.play_framelayout = (FrameLayout) convertView.findViewById(R.id.play_framelayout);
				itemHolder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.thumbnailImageView);
				itemHolder.progress_view = (ProgressBar) convertView.findViewById(R.id.progress_view);
				itemHolder.videoView = (ScalableVideoView) convertView.findViewById(R.id.video_view);
				itemHolder.respone_btn = (Button) convertView.findViewById(R.id.respone_btn);
				itemHolder.edit_btn = (Button) convertView.findViewById(R.id.edit_btn);
				itemHolder.delete_btn = (Button) convertView.findViewById(R.id.delete_btn);
				itemHolder.pictures = new ArrayList<Picture>();
				itemHolder.adpter = new GridViewAdpter(itemHolder.pictures, context);
				itemHolder.gridView.setAdapter(itemHolder.adpter);
				itemHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Reply reply = (Reply) arg0.getTag();
						// TODO Auto-generated method stub
						ArrayList<String> strs = new ArrayList<String>();
						for (Picture pic : reply.getPictureList()) {
							strs.add(InterfaceFinals.URL_HEAD + pic.getUrl());
						}
						imageBrower(arg2, strs);

					}

				});
				ReplyHandleListener handleListener = new ReplyHandleListener();
				itemHolder.delete_btn.setOnClickListener(handleListener);
				itemHolder.respone_btn.setOnClickListener(handleListener);
				itemHolder.edit_btn.setOnClickListener(handleListener);
				convertView.setTag(itemHolder);
			}
			itemHolder = (ItemHolder) convertView.getTag();
			itemHolder.playImageView.setOnClickListener(null);
			final Reply itemData = getItem(position);
			itemHolder.gridView.setTag(itemData);
			List<Picture> temp = itemData.getPictureList();
			itemHolder.pictures.clear();
			itemHolder.pictures.addAll(temp);
			itemHolder.adpter.notifyDataSetChanged();
			Glide.with(context).load(InterfaceFinals.URL_HEAD + itemData.getUser().getHead())
					.placeholder(R.drawable.touxiang).centerCrop().into(itemHolder.user_imageView);
			// Glide.with(context).load(InterfaceFinals.URL_HEAD +
			// itemData.getUser().getHead())
			// .placeholder(R.mipmap.imageselector_photo).centerCrop().into(itemHolder.user_imageView);

			Reply respon = itemData.getReply();
			// long reId = respon.getUser().getUserId();
			// long itenId = itemData.getUser().getUserId();
			if (respon != null) {
				// if (respon != null && reId != itenId) {
				itemHolder.userNameTextView.setText(Html.fromHtml(itemData.getUser().getUserName() + " "
						+ "<font color=\"#ffffff\">回复</font>" + " " + respon.getUser().getUserName()));
				// }
				// else {
				// itemHolder.userNameTextView.setText(itemData.getUser().getUserName());
				// }
			} else {
				itemHolder.userNameTextView.setText(itemData.getUser().getUserName());
			}
			itemHolder.creatTimeTextView.setText(itemData.getCreateTime());
			itemHolder.postionTextView.setText("第" + (getCount() - position) + "楼");
			itemHolder.contentTextView.setText(itemData.getContent());
			String video = itemData.getVideo();
			Object tag = itemHolder.videoView.getTag();
			if (tag == null) {
				try {
					itemHolder.videoView.setDataSource("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (StringUtils.isNull(video)) {
				itemHolder.play_framelayout.setVisibility(View.GONE);
			} else {
				Glide.with(context).load(InterfaceFinals.URL_HEAD + itemData.getVideo() + ".jpg").centerCrop()
						.into(itemHolder.thumbnailImageView);
				itemHolder.play_framelayout.setVisibility(View.VISIBLE);
				itemHolder.playImageView.setVisibility(View.VISIBLE);
				itemHolder.progress_view.setVisibility(View.GONE);
				itemHolder.thumbnailImageView.setVisibility(View.VISIBLE);
				TagView tagView = null;
				tagView = (TagView) itemHolder.playImageView.getTag();
				if (tagView == null) {
					tagView = new TagView();
				}
				tagView.postion = position;
				tagView.playImageView = itemHolder.playImageView;
				tagView.progress_view = itemHolder.progress_view;
				tagView.thumbnailImageView = itemHolder.thumbnailImageView;
				tagView.videoView = itemHolder.videoView;
				tagView.item = itemData;
				itemHolder.playImageView.setTag(tagView);
				itemHolder.playImageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final TagView tagView = (TagView) v.getTag();
						new HttpDownLoad(context, new LoadListener() {

							@Override
							public void onstart() {
								// TODO Auto-generated method stub
								tagView.playImageView.setVisibility(View.GONE);
								tagView.progress_view.setVisibility(View.VISIBLE);
							}

							@Override
							public void onSucess(File file) {
								// TODO Auto-generated method stub
								tagView.progress_view.setVisibility(View.GONE);
								if (file != null) {
									try {
										tagView.item.setVideoFile(file);
										tagView.videoView.setDataSource(file.getAbsolutePath());
										tagView.videoView.setLooping(true);
										tagView.videoView.prepare();
										tagView.videoView.start();
										tagView.videoView.setTag(1);
										tagView.thumbnailImageView.setVisibility(View.GONE);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										tagView.playImageView.setVisibility(View.VISIBLE);
										tagView.progress_view.setVisibility(View.GONE);
										tagView.thumbnailImageView.setVisibility(View.VISIBLE);
										tagView.videoView.setTag(0);
									}
								} else {
									tagView.playImageView.setVisibility(View.VISIBLE);
								}

							}

							@Override
							public void onLoad(long count, long curCount) {
								// TODO Auto-generated method stub

							}
						}).execute(InterfaceFinals.URL_HEAD + tagView.item.getVideo());

					}
				});

				itemHolder.videoView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						File file = itemData.getVideoFile();
						if (file != null) {
							int m = (Integer) v.getTag();
							if (m == 1) {
								Intent intent = new Intent(OPDetailsActivity.this, PlayVideoActiviy.class);
								intent.putExtra(PlayVideoActiviy.KEY_FILE_PATH, file.getAbsolutePath());
								startActivity(intent);
							}
						}

					}
				});

				if (isPause) {
					Object object = itemHolder.videoView.getTag();
					if (object != null) {
						int isPlay = (Integer) itemHolder.videoView.getTag();
						if (isPlay == 1) {
							itemHolder.videoView.setTag(2);
							itemHolder.videoView.stop();
						}
					}
				} else {
					if (itemHolder.videoView.getTag() != null) {
						int isPlay = (Integer) itemHolder.videoView.getTag();
						if (isPlay == 2) {
							try {
								itemHolder.videoView.setLooping(true);
								itemHolder.videoView.prepare();
								itemHolder.videoView.start();
								tagView.videoView.setTag(1);
								itemHolder.playImageView.setVisibility(View.GONE);
								itemHolder.progress_view.setVisibility(View.GONE);
								itemHolder.thumbnailImageView.setVisibility(View.GONE);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								itemHolder.videoView.setTag(0);
								itemHolder.playImageView.setVisibility(View.VISIBLE);
								itemHolder.progress_view.setVisibility(View.GONE);
								itemHolder.thumbnailImageView.setVisibility(View.VISIBLE);
								e.printStackTrace();
							}
						}
					}
				}
			}

			long id = OPDetailsActivity.this.getUserData().getUserId();
			long creat_id = itemData.getUser().getUserId();
			if (id == creat_id) {
				itemHolder.respone_btn.setVisibility(View.VISIBLE);
				itemHolder.edit_btn.setVisibility(View.VISIBLE);
				itemHolder.delete_btn.setVisibility(View.VISIBLE);
			} else {
				itemHolder.respone_btn.setVisibility(View.VISIBLE);
				itemHolder.edit_btn.setVisibility(View.GONE);
				itemHolder.delete_btn.setVisibility(View.GONE);
			}
			itemHolder.respone_btn.setTag(itemData);
			itemHolder.edit_btn.setTag(itemData);
			itemHolder.delete_btn.setTag(itemData);
			return convertView;
		}

		class ReplyHandleListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Reply reply = (Reply) v.getTag();
				switch (v.getId()) {
				case R.id.respone_btn:
					Intent intent = new Intent(OPDetailsActivity.this, RespondToCommentsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("question", question);
					bundle.putSerializable("reply", reply);
					intent.putExtras(bundle);
					startActivity(intent);

					break;
				case R.id.edit_btn:
					Intent intent2 = new Intent(OPDetailsActivity.this, EditQuestionOrReplyActivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putSerializable(EditQuestionOrReplyActivity.REPLY, reply);
					bundle1.putSerializable(EditQuestionOrReplyActivity.key,
							EditQuestionOrReplyActivity.EDIT_TYPE.EDIT_REPLY);
					intent2.putExtras(bundle1);
					startActivity(intent2);
					break;
				case R.id.delete_btn:
					deleteReply(reply, replys);
					break;
				}

			}
		}
	}

	private void deleteReply(final Reply reply, final List<Reply> replies) {

		DeleteDialog.Builder builder = new DeleteDialog.Builder(this);
		builder.setMessage("确定要执行删除操作吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项

				Type t = new TypeToken<BaseModel<Reply>>() {
				}.getType();
				NetAsyncTask<Reply, Reply> net = new NetAsyncTask<Reply, Reply>(OPDetailsActivity.this, t,
						InterfaceFinals.INF_MODIFY_REPLY_STATE, reply, new CallBack<Reply>() {

							@Override
							public void onCancel(BaseModel<Reply> result) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(BaseModel<Reply> result) {
								// TODO Auto-generated method stub
								replies.remove(reply);
								listViewAdpter.notifyDataSetChanged();
							}

							@Override
							public void onFail(BaseModel<Reply> result) {
								// TODO Auto-generated method stub
								showToast(result.getMessage());
								String failInf = result.getMessage();
								if ("未登陆".equals(failInf)) {
									EyunApplication.getInstance().removeAllActivity();
									startActivity(LoginActivity.class);
								}

							}
						});
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("modelId", reply.getTid() + "");
				params.put("token", OPDetailsActivity.this.getUserData().getToken());
				params.put("status", 3 + "");
				net.execute(params);
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	class TagView {
		public int postion;
		public ProgressBar progress_view;
		public ImageView playImageView;
		public ImageView thumbnailImageView;
		public ScalableVideoView videoView;
		public Reply item;
	}

	class ItemHolder {
		public ImageView user_imageView;
		public TextView userNameTextView;
		public TextView creatTimeTextView;
		public TextView postionTextView;
		public ImageView thumbnailImageView;
		public TextView contentTextView;
		public ImageView playImageView;
		public NestGridView gridView;
		public GridViewAdpter adpter;
		public List<Picture> pictures;
		public ProgressBar progress_view;
		public ScalableVideoView videoView;
		public FrameLayout play_framelayout;
		public Button respone_btn;
		public Button edit_btn;
		public Button delete_btn;
	}

	private void findReplyList(int todo) {
		Type t = new TypeToken<BaseModel<List<Reply>>>() {
		}.getType();
		BaseAsyncTask<List<Reply>> asyncTask = new BaseAsyncTask<List<Reply>>(this, t, todo, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", String.valueOf(curPage));
		params.put("pageSize", String.valueOf(PAGESIZE));
		params.put("jsonFilter", "{'search_EQ_questionId':'" + question.getTid() + "'}");
		asyncTask.execute(params);
	}

	class GridViewAdpter extends BaseAdapter {
		private List<Picture> images;
		private Context context;

		public GridViewAdpter(List<Picture> images, Context context) {
			this.images = images;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// return images==null?0:images.size();
			return images == null ? 0 : images.size();
		}

		@Override
		public Picture getItem(int position) {
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
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.op_detail_gridview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imageView);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			Picture itemPic = getItem(position);
			Glide.with(context).load(InterfaceFinals.URL_HEAD + itemPic.getIconUrl())
					.placeholder(R.mipmap.imageselector_photo).centerCrop().into(viewHolder.imageView);

			return convertView;
		}

		class ViewHolder {
			public ImageView imageView;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub3
		if (v.getId() == R.id.opdetail_comment_btn) {
			Intent intent = new Intent(this, RespondToCommentsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("question", question);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (v.getId() == R.id.opdetail_playImageView) {
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
							question.setVideoFile(file);
							mainVideoView.setDataSource(file.getAbsolutePath());
							mainVideoView.setLooping(true);
							mainVideoView.prepare();
							mainVideoView.start();
							thumbnailImageView.setVisibility(View.GONE);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							playImageView.setVisibility(View.VISIBLE);
							thumbnailImageView.setVisibility(View.VISIBLE);
						}
					} else {
						playImageView.setVisibility(View.VISIBLE);
						thumbnailImageView.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onLoad(long count, long curCount) {
					// TODO Auto-generated method stub

				}
			}).execute(InterfaceFinals.URL_HEAD + question.getVideo());
		} else if (v.getId() == R.id.opdetail_video_view) {
			if (question.getVideoFile() != null) {
				Intent intent = new Intent(this, PlayVideoActiviy.class);
				intent.putExtra(PlayVideoActiviy.KEY_FILE_PATH, question.getVideoFile().getAbsolutePath());
				startActivity(intent);
			}
		}

	}

	public void backEvent(View view) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ArrayList<String> strs = new ArrayList<String>();
		for (Picture pic : question.getPictureList()) {
			strs.add(InterfaceFinals.URL_HEAD + pic.getUrl());
		}
		imageBrower(arg2, strs);
	}

	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.EXTRA_LOCAL, false);
		startActivity(intent);
	}

}
