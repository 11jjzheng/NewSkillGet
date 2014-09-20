package cn.edu.stu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.XListView;
import cn.edu.stu.view.XListView.IXListViewListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MessageFragment extends Fragment implements IXListViewListener{
	private View view;
	private XListView mListView;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	private int[] ids = { R.id.tv_message_who,
			R.id.iv_message_whose_pic, R.id.tv_message_num,
			R.id.tv_message_title };
	private String[] strs = { "who", "pic", "num", "title"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_message, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView = (XListView) view.findViewById(R.id.xlv_message);
		mListView.setPullLoadEnable(true);
		SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
				.getApplicationContext(), getData(), R.layout.message_list_item,
				strs, ids);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent intent=new Intent(getActivity(),NewSkillDetail.class);
//				startActivity(intent);
			}
		});
	}

	private List<Map<String, Object>> getData() {
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("who","ifanr");
			map.put("pic",R.drawable.user);
			map.put("num","100");
			map.put("title","优雅地使用Google的方法");
			listData.add(map);
		}
		return listData;
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("");
	}

	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				onLoad();
			}
		}, 2000);
	}

	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				onLoad();
			}
		}, 2000);
	}
}
