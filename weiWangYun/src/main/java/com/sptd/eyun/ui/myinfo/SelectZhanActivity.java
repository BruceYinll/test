package com.sptd.eyun.ui.myinfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.XuanzezhanAdapter;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Station;
import com.sptd.net.BaseAsyncTask;
/**
 * 搜索添加站
 * @author ldy
 *
 */
public class SelectZhanActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_left;
	//搜索
	private Button button_search;
	//key
	private EditText edittext_search;
	
	private ListView zhan_listView;
	private XuanzezhanAdapter xuanzezhanAdapter;
	
	//所有站
    private List<Station> allStations=new ArrayList<Station>();
    //关键词站
    private List<Station> keyStations=new ArrayList<Station>();
    
	public SelectZhanActivity() {
		super(R.layout.myzhan_addzhan_xuanzezhan,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_FINDSTATIONLIST:
			  showToast(res.getMessage());
			
			break;

		default:
			break;
		}
		
	}

	@Override
	protected void getData() {
//		initListView();
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		allStations=(List<Station>) bundle.getSerializable("allzhan");
		for(Station station:allStations){
			keyStations.add(station);
		}
	}

	@Override
	protected void findView() {
		
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(this);
		button_search=(Button) findViewById(R.id.button_search);
		button_search.setOnClickListener(this);
		edittext_search=(EditText) findViewById(R.id.edittext_search);
		zhan_listView=(ListView) findViewById(R.id.zhan_listView);
		xuanzezhanAdapter=new XuanzezhanAdapter(SelectZhanActivity.this,
				allStations);
		zhan_listView.setAdapter(xuanzezhanAdapter);
		
	
	}

	@Override
	protected void refreshView() {
		xuanzezhanAdapter=new XuanzezhanAdapter(SelectZhanActivity.this,
				keyStations);
		zhan_listView.setAdapter(xuanzezhanAdapter);
		
		zhan_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=getIntent();				
				intent.putExtra("zhan", keyStations.get(arg2).getTid());
				setResult(RESULT_OK, intent);
				SelectZhanActivity.this.finish();
				
			}
		});
		
	}
	
	
	/**
	 * 获得所有站
	 */
	private void findAllStations() {
		Type t = new TypeToken<BaseModel<List<Station>>>() {
		}.getType();
		BaseAsyncTask<List<Station>> task = new BaseAsyncTask<List<Station>>(this,t,
				InterfaceFinals.INF_FINDSTATIONLIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", "0");
		params.put("pageSize", "100");
		params.put("status", "1");
		task.execute(params);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_search:
//			 keyStations=allStations;
			 keyStations.clear();
			 search();
			 refreshView();
			break;

		case R.id.iv_left:
			  SelectZhanActivity.this.finish();
			break;
		}
		
	}
	private void search(){
		String keyString=edittext_search.getText().toString();
		for(Station station:allStations){
			if(station.getName().contains(keyString))
				keyStations.add(station);
		}
	}
	
	

}
