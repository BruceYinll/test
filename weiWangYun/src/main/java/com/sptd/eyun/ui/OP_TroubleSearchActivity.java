package com.sptd.eyun.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Question;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class OP_TroubleSearchActivity extends BaseInteractActivity implements OnItemClickListener {

	private List<Question> questions;
	private PullToRefreshListView search_listView;
	private Button cancleBtn;
	private Button sureBtn;
	private EditText search_editText;
	int curPage = 0;
	int before = curPage;
	public static final int PAGESIZE = 10;
	private OPListAdapter op_adpter;

	public OP_TroubleSearchActivity() {
		super(R.layout.activity_op_troublesearch);
	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub

		if (res.getInfCode() == InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA) {
			search_listView.onRefreshComplete();
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

		} else if (res.getInfCode() == InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA) {
			search_listView.onRefreshComplete();
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
		}

	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stubo

	}

	@Override
	public void onFail(BaseModel res) {
		// TODO Auto-generated method stub
		super.onFail(res);
		search_listView.onRefreshComplete();
	}

	@Override
	public void onCancel(BaseModel res) {
		// TODO Auto-generated method stub
		super.onCancel(res);
		search_listView.onRefreshComplete();
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		search_listView = (PullToRefreshListView) findViewById(R.id.search_listView);
		cancleBtn = (Button) findViewById(R.id.cancleBtn);
		sureBtn = (Button) findViewById(R.id.sureBtn);
		search_editText = (EditText) findViewById(R.id.search_editText);
		questions = new ArrayList<Question>();
		search_listView.setMode(Mode.BOTH);
		search_listView.setOnPullEventListener(new OnPullEventListener<ListView>() {

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

		search_listView.setOnItemClickListener(this);
		search_listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				curPage = 0;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA, search_editText.getText().toString());

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				curPage++;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA, search_editText.getText().toString());

			}
		});

	}

	private void findQuestionList(int todo, String key) {
		Type t = new TypeToken<BaseModel<List<Question>>>() {
		}.getType();
		BaseAsyncTask<List<Question>> asyncTask = new BaseAsyncTask<List<Question>>(this, t, todo, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", String.valueOf(curPage));
		params.put("pageSize", String.valueOf(PAGESIZE));
		params.put("token", String.valueOf(getUserData().getToken()));
		params.put("status", "1");
		params.put("jsonFilter", "{'search_LIKE_name':'" + key + "'}");
		asyncTask.execute(params);
	}

	@Override
	protected void refreshView() {

		// TODO Auto-generated method stub
		op_adpter = new OPListAdapter(this, questions);
		search_listView.setAdapter(op_adpter);
		search_editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					cancleBtn.setVisibility(View.GONE);
					sureBtn.setVisibility(View.VISIBLE);
				} else {
					cancleBtn.setVisibility(View.VISIBLE);
					sureBtn.setVisibility(View.GONE);
				}

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

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, OPDetailsActivity.class);
		Question question = op_adpter.getItem(arg2 - 1);
		Bundle bundle = new Bundle();
		bundle.putSerializable("question", question);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	public void cancleClicl(View view) {
		finish();
	}

	public void sureClick(View view) {
		findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA, search_editText.getText().toString());
	}

	class OPListAdapter extends BaseAdapter {
		Context context;
		private List<Question> questions;

		public OPListAdapter(Context context, List<Question> questions) {
			this.context = context;
			this.questions = questions;
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

			holder.type_textView.setText(question.getStation().getName() + ">" + question.getDeviceType().getName()
					+ ">" + question.getDevice().getName());
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
