package com.sptd.eyun.ui.myinfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.Reply;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.EditQuestionOrReplyActivity;
import com.sptd.eyun.ui.NewOpActivity;
import com.sptd.eyun.ui.OPDetailsActivity;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.Utils;

public class MyyunweiActivity extends BaseInteractActivity implements OnClickListener, OnItemClickListener {

	public static final String TAG = "MyyunweiActivity";

	private PullToRefreshListView fragment_yunwei_yunweilist;// 运维列表
	private OPListAdapter op_adpter;
	private UserObj userObj;// 用户
	private ImageView iv_back;
	private ImageView new_op_btn;
	private List<Question> questions;
	private UpdataReplyBroadCast upBroadCast = new UpdataReplyBroadCast();

	public MyyunweiActivity() {
		super(R.layout.activity_myyunwei, false);
	}

	public static final String UPDATEQUESTIONFLAG = "com.sptd.eyun.ui.update_question_flag";
	public static final String QUESTION_KEY = "question_key";
	public static final String TODO = "todo";
	public static final int TO_UPDATA = 121;
	public static final int TO_DETELE = 122;
	public static final int TO_ADD = 123;

	class UpdataReplyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Question question = (Question) bundle.getSerializable(QUESTION_KEY);
				int todo = bundle.getInt(TODO);
				if (todo == TO_UPDATA) {
					if (question != null) {
						int count = op_adpter.getCount();
						for (int i = 0; i < count; i++) {
							Question temp = op_adpter.getItem(i);
							if (temp.getTid().longValue() == question.getTid().longValue()) {
								op_adpter.replaceItem(temp, question, i);
							}
						}
					}
				} else if (todo == TO_DETELE) {
					if (question != null) {
						int count = op_adpter.getCount();
						for (int i = 0; i < count; i++) {
							Question temp = op_adpter.getItem(i);
							if (temp.getTid().longValue() == question.getTid().longValue()) {
								op_adpter.removeItem(i);
								break;
							}
						}
					}
				} else if (todo == TO_ADD) {
					if (question != null) {
						op_adpter.addItem(question);
					}
				}

			}

		}

	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA_OF_MINE:
			fragment_yunwei_yunweilist.onRefreshComplete();
			if (res.getCode() == 1) {
				List<Question> tempQues = (List<Question>) res.getObject();
				questions.clear();
				questions.addAll(tempQues);
				op_adpter.notifyDataSetInvalidated();
				before = curPage;
			} else {
				curPage = before;
				showToast(res.getMessage());
			}
			break;

		case InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA_OF_MINE:
			fragment_yunwei_yunweilist.onRefreshComplete();
			if (res.getCode() == 1) {
				List<Question> tempQues = (List<Question>) res.getObject();
				if (tempQues.size() == 0) {
					curPage = before;
					return;
				}
				questions.addAll(tempQues);
				op_adpter.notifyDataSetInvalidated();
				before = curPage;
			} else {
				curPage = before;
				showToast(res.getMessage());
			}
			break;
		}

	}

	@Override
	public void onCancel(BaseModel res) {
		// TODO Auto-generated method stub
		super.onCancel(res);
		fragment_yunwei_yunweilist.onRefreshComplete();
	}

	@Override
	public void onFail(BaseModel res) {
		// TODO Auto-generated method stub
		super.onFail(res);
		fragment_yunwei_yunweilist.onRefreshComplete();
		// TODO Auto-generated method stub
		String failInf = res.getMessage();
		if ("未登陆".equals(failInf)) {
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
	}

	@Override
	protected void getData() {
		userObj = getUserData();
		questions = new ArrayList<Question>();
		op_adpter = new OPListAdapter(this, questions);
		IntentFilter filter = new IntentFilter(UPDATEQUESTIONFLAG);
		registerReceiver(upBroadCast, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(upBroadCast);
	}

	@Override
	protected void findView() {
		fragment_yunwei_yunweilist = (PullToRefreshListView) findViewById(R.id.fragment_yunwei_yunweilist);
		fragment_yunwei_yunweilist.setMode(Mode.BOTH);
		fragment_yunwei_yunweilist.setOnPullEventListener(new OnPullEventListener<ListView>() {

			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
				// TODO Auto-generated method stub
				if (direction == Mode.PULL_FROM_END) {
				} else if (direction == Mode.PULL_FROM_START) {
					refreshView.getLoadingLayoutProxy(true, false)
							.setRefreshingLabel("最后刷新时间" + Utils.formatData(System.currentTimeMillis()));
				}

			}
		});
		fragment_yunwei_yunweilist.setAdapter(op_adpter);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		new_op_btn = (ImageView) findViewById(R.id.new_op_btn);
		new_op_btn.setOnClickListener(this);
		fragment_yunwei_yunweilist.setOnItemClickListener(this);

		fragment_yunwei_yunweilist.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				curPage = 0;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA_OF_MINE);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				curPage++;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA_OF_MINE);

			}
		});

		findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA_OF_MINE);

	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获得所有的运维设备
	 */

	int curPage = 0;
	int before = curPage;
	public static final int PAGESIZE = 10;

	private void findQuestionList(int todo) {
		Type t = new TypeToken<BaseModel<List<Question>>>() {
		}.getType();
		BaseAsyncTask<List<Question>> asyncTask = new BaseAsyncTask<List<Question>>(this, t, todo, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", String.valueOf(curPage));
		params.put("pageSize", String.valueOf(PAGESIZE));
		params.put("token", getUserData().getToken());
		params.put("status", "1");
		String jsonFilter = "{}";
		params.put("jsonFilter", jsonFilter);
		asyncTask.execute(params);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, EditQuestionOrReplyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(EditQuestionOrReplyActivity.QUESTION, op_adpter.getItem(position - 1));
		bundle.putSerializable(EditQuestionOrReplyActivity.key, EditQuestionOrReplyActivity.EDIT_TYPE.EDIT_QUESSTION);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.new_op_btn:
			Intent intent = new Intent(this, NewOpActivity.class);
			startActivity(intent);
			break;
		}

	}

	class OPListAdapter extends BaseAdapter {
		Context context;
		private List<Question> questions;

		public OPListAdapter(Context context, List<Question> questions) {
			this.context = context;
			this.questions = questions;
		}

		public void removeItem(int position) {
			questions.remove(position);
			this.notifyDataSetChanged();
		}

		public void addItem(Question question) {
			questions.add(0, question);
			this.notifyDataSetChanged();
		}

		public void replaceItem(final Question question, final Question tempQuestion, int position) {
			questions.remove(question);
			questions.add(position, tempQuestion);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return questions.size();
		}

		@Override
		public Question getItem(int position) {
			// TODO Auto-generated method stub
			return questions.get(position);
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
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_op_list, null);
				holder.type_textView = (TextView) convertView.findViewById(R.id.type_textView);
				holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
				holder.content_textView = (TextView) convertView.findViewById(R.id.content_textView);
				holder.unenable_textView = (TextView) convertView.findViewById(R.id.unenable_textView);
				holder.enable_textView = (TextView) convertView.findViewById(R.id.enable_textView);
				holder.creatTimeTextView = (TextView) convertView.findViewById(R.id.creatTimeTextView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();

			Question question = getItem(position);

			if (question.getStation() == null && question.getDeviceType() == null && question.getDevice() == null) {
				holder.type_textView.setText("");
				holder.type_textView.setVisibility(View.GONE);
			} else {
				holder.type_textView.setVisibility(View.VISIBLE);
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
				holder.type_textView.setText(buffer);
			}
			holder.titleTextView.setText(question.getName());
			holder.content_textView.setText(question.getDescription());
			if (question.getState() == 1) {
				holder.unenable_textView.setVisibility(View.VISIBLE);
				holder.enable_textView.setVisibility(View.GONE);
			} else if (question.getState() == 0) {
				holder.unenable_textView.setVisibility(View.GONE);
				holder.enable_textView.setVisibility(View.VISIBLE);
			} else {
				holder.unenable_textView.setVisibility(View.GONE);
				holder.enable_textView.setVisibility(View.GONE);
			}
			holder.creatTimeTextView.setText(question.getCreateTime());
			return convertView;
		}

		class ViewHolder {
			public TextView type_textView;
			public TextView titleTextView;
			public TextView content_textView;
			public TextView unenable_textView;
			public TextView enable_textView;
			public TextView creatTimeTextView;
		}
	}

}
