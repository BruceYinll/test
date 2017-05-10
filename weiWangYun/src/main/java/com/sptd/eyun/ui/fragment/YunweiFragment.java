package com.sptd.eyun.ui.fragment;

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
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Question;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.eyun.ui.NewOpActivity;
import com.sptd.eyun.ui.OPDetailsActivity;
import com.sptd.eyun.ui.OP_TroubleSearchActivity;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.ui.myinfo.MyyunweiActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.Utils;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 运维
 * 
 * @author ldy
 */
public class YunweiFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
	public static final String TAG = "YunweiFragment";

	private Spinner stationSpinner;
	private Spinner deciveSpinner;
	private PullToRefreshListView fragment_yunwei_yunweilist;// 运维列表
	private OPListAdapter op_adpter;
	private UserObj userObj;// 用户
	private Button search_trouble_btn;
	private ImageButton new_op_btn;
	private StationAdpter stationAdpter;
	private DeviceTypeAdpter deviceTypeAdpter;
	private List<UserStation> stations;
	private List<DeviceType> deviceTypeList;
	private List<Question> questions;
	private UpdataReplyBroadCast upBroadCast;

	public YunweiFragment() {
		super(R.layout.fragment_yunwei, NoTitle);
	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { View
	 * view=inflater.inflate(R.layout.fragment_ceshi, container, false);
	 * TextView textView=(TextView) view.findViewById(R.id.textView1);
	 * textView.setText("yunweifragment"); return view; }
	 */
	@Override
	protected void getData() {
		userObj = getUserData();
		stations = new ArrayList<UserStation>();
		deviceTypeList = new ArrayList<DeviceType>();
		questions = new ArrayList<Question>();
		stationAdpter = new StationAdpter(activity, R.layout.item_spinner, stations);
		deviceTypeAdpter = new DeviceTypeAdpter(activity, R.layout.item_spinner, deviceTypeList);
		op_adpter = new OPListAdapter(activity, questions);
		fragment_yunwei_yunweilist.setAdapter(op_adpter);
		findStationList();
		// findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		upBroadCast = new UpdataReplyBroadCast();
		IntentFilter filter = new IntentFilter(MyyunweiActivity.UPDATEQUESTIONFLAG);
		getActivity().registerReceiver(upBroadCast, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(upBroadCast);
	}

	@Override
	protected void findView() {
		stationSpinner = (Spinner) findViewById(R.id.spin_province);
		deciveSpinner = (Spinner) findViewById(R.id.spin_city);
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

		search_trouble_btn = (Button) findViewById(R.id.search_trouble_btn);
		search_trouble_btn.setOnClickListener(this);
		new_op_btn = (ImageButton) findViewById(R.id.new_op_btn);
		new_op_btn.setOnClickListener(this);
		fragment_yunwei_yunweilist.setOnItemClickListener(this);

		fragment_yunwei_yunweilist.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				curPage = 0;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				curPage++;
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA);

			}
		});
	}

	/**
	 * 同步站
	 */

	public boolean synStation() {
		HomeActivity homeActivity = (HomeActivity) getActivity();
		if (homeActivity == null) {
			return false;
		}
		UserStation curtempStation = homeActivity.getCurrentUserStation();
		if (curtempStation == null) {
			stationSpinner.setSelection(0);
			return false;
		}

		int count = stationAdpter.getCount();
		if (count > 1) {
			for (int i = 1; i < count; i++) {
				UserStation station = stationAdpter.getItem(i);
				if (station.getStationId().longValue() == curtempStation.getStationId().longValue()) {
					beforeStation = curStation;
					curStation = curtempStation;
					stationSpinner.setSelection(i);
					return true;
				}
			}

		} else {
			stationSpinner.setSelection(0);
			return false;
		}
		return false;
	}

	/**
	 * 同步站
	 */

	public void synToHomeStation(int select) {
		HomeActivity homeActivity = (HomeActivity) getActivity();
		if (homeActivity == null) {
			return;
		}

		UserStation station = stationAdpter.getItem(select);
		if (station != null) {
			// curStation.setStation(station);
			// curStation.setStationId(station.getTid());
			homeActivity.setCurrentUserStation(station);
		}
	}

	public void synDeicveType(int select) {
		HomeActivity homeActivity = (HomeActivity) getActivity();
		if (homeActivity == null) {
			return;
		}
		DeviceType deviceType = deviceTypeAdpter.getItem(select);
		if (deviceType != null) {
			homeActivity.setCurrentDeviceType(deviceType);
		}
	}

	public boolean synDeicveType() {
		HomeActivity homeActivity = (HomeActivity) getActivity();
		if (homeActivity == null) {
			return false;
		}
		DeviceType curDeviceType = homeActivity.getCurrentDeviceType();
		if (curDeviceType == null) {
			deciveSpinner.setSelection(0);
			return false;
		}

		int count = deviceTypeAdpter.getCount();
		if (count > 1) {
			for (int i = 1; i < count; i++) {
				DeviceType deviceType = deviceTypeAdpter.getItem(i);
				if (deviceType.getTid() == curDeviceType.getTid()) {
					beforedeviceType = curdeviceType;
					curdeviceType = curDeviceType;
					deciveSpinner.setSelection(i);
					return true;
				}
			}

		} else {
			stationSpinner.setSelection(0);
			return false;
		}
		return false;
	}

	/**
	 * Station Spinner adpter
	 */

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
			// spinner中显示的颜色
			// textView.setTextColor(Color.parseColor("#FF0000"));
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

	private UserStation curStation;
	private UserStation beforeStation;
	private DeviceType curdeviceType;
	private DeviceType beforedeviceType;

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		stationSpinner.setAdapter(stationAdpter);
		deciveSpinner.setAdapter(deviceTypeAdpter);
		stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				synToHomeStation(arg2);
				if (arg2 == 0) {
					beforeStation = curStation;
					curStation = null;
					deviceTypeAdpter.clear();
					for (UserStation station : stations) {
						if (station.getStation().getDeviceTypeList() != null) {

							deviceTypeList.addAll(station.getStation().getDeviceTypeList());
						}
					}
					deviceTypeAdpter.notifyDataSetChanged();

				} else {
					beforeStation = curStation;
					curStation = stationAdpter.getItem(arg2);
					if (curStation != null) {
						deviceTypeAdpter.clear();
						if (curStation.getStation().getDeviceTypeList() != null) {
							deviceTypeAdpter.addAll(curStation.getStation().getDeviceTypeList());
						}
						deviceTypeAdpter.notifyDataSetChanged();
					}
				}
				deciveSpinner.setSelection(0, true);
				if (beforeStation != curStation) {
					curPage = 0;

					/**
					 * shuxin
					 */

					findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA);
				}
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
				synDeicveType(arg2);
				if (arg2 == 0) {
					beforedeviceType = curdeviceType;
					curdeviceType = null;
				} else {
					beforedeviceType = curdeviceType;
					curdeviceType = deviceTypeAdpter.getItem(arg2);
				}
				if (beforedeviceType != curdeviceType) {
					curPage = 0;

					/**
					 * shuxin
					 */
					findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

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
					deviceTypeAdpter.clear();
					for (UserStation station : stations) {
						if (station.getStation().getDeviceTypeList() != null) {
							deviceTypeList.addAll(station.getStation().getDeviceTypeList());
						}
					}
					deviceTypeAdpter.notifyDataSetChanged();
				}

			} else {
				showToast(res.getMessage());
			}
			if (!synStation() && !synDeicveType()) {
				findQuestionList(InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA);
			}

		} else if (res.getInfCode() == InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA) {
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

		} else if (res.getInfCode() == InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA) {
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
		String failInf = res.getMessage();
		if("未登陆".equals(failInf)){
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
	}

	/**
	 * 获得所有的运维设备
	 */

	int curPage = 0;
	int before = curPage;
	public static final int PAGESIZE = 10;

	private void findQuestionList(int todo) {
		if (getActivity() == null) {
			return;
		}
		Type t = new TypeToken<BaseModel<List<Question>>>() {
		}.getType();
		BaseAsyncTask<List<Question>> asyncTask = new BaseAsyncTask<List<Question>>(this, t, todo, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", String.valueOf(curPage));
		params.put("pageSize", String.valueOf(PAGESIZE));
		params.put("token", getUserData().getToken());
		params.put("status", "1");
		String jsonFilter = "";
		if (beforedeviceType != curdeviceType && beforeStation != curStation && curdeviceType != null
				&& curStation != null) {
			jsonFilter = "{'search_EQ_deviceTypeId':'" + curdeviceType.getTid() + "','search_EQ_stationId':'"
					+ curStation.getStation().getTid() + "'}";
		} else if (beforedeviceType != curdeviceType && curdeviceType != null) {
			jsonFilter = "{'search_EQ_deviceTypeId':'" + curdeviceType.getTid() + "'}";
		} else if (beforeStation != curStation && curStation != null) {
			jsonFilter = "{'search_EQ_stationId':'" + curStation.getStation().getTid() + "'}";
		}
		if (jsonFilter.length() > 0) {
			params.put("jsonFilter", jsonFilter);
		}
		asyncTask.execute(params);
	}

	/**
	 * 获取选项list
	 */

	class UpdataReplyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Question question = (Question) bundle.getSerializable(MyyunweiActivity.QUESTION_KEY);
				int todo = bundle.getInt(MyyunweiActivity.TODO);
				if (todo == MyyunweiActivity.TO_ADD) {
					if (question != null) {
						op_adpter.addItem(question);
					}
				}

			}

		}

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.search_trouble_btn) {
			Intent intent = new Intent(this.getActivity(), OP_TroubleSearchActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.new_op_btn) {
			Intent intent = new Intent(this.getActivity(), NewOpActivity.class);
			startActivity(intent);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		// TODO Auto-generated method stub
		Intent intent = new Intent(this.getActivity(), OPDetailsActivity.class);
		Question question = op_adpter.getItem(arg2 - 1);
		Bundle bundle = new Bundle();
		bundle.putSerializable("question", question);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	class OPListAdapter extends BaseAdapter {
		Context context;
		private List<Question> questions;

		public OPListAdapter(Context context, List<Question> questions) {
			this.context = context;
			this.questions = questions;
		}

		public void addItem(Question question) {
			questions.add(0, question);
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("YunweiFragment");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("YunweiFragment"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

}
