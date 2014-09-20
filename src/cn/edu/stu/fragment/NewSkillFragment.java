package cn.edu.stu.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import cn.edu.stu.newskillget.LoginActivity;
import cn.edu.stu.newskillget.MainActivity;
import cn.edu.stu.newskillget.NewSkillDetail;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.utils.HttpClients;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.JsonTools;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import cn.edu.stu.view.XListView;
import cn.edu.stu.view.XListView.IXListViewListener;

/**
 * @author ifanr
 * 
 */
public class NewSkillFragment extends Fragment implements IXListViewListener {
	private static final String TAG = "NewSkillFragment";
	private View view;
	private XListView mListView;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private String userdata = "";
	private int shid;
	private int uid;
	private String titledetail = "";
	private String date = "";
	private static int i = 1;
	// private List<Map<String, Object>> listData = new ArrayList<Map<String,
	// Object>>();
	private int[] ids = { R.id.tv_share_list_item_tilte,
			R.id.tv_share_list_item_shid, R.id.tv_share_list_item_uid,
			R.id.tv_share_list_item_content, R.id.tv_share_list_item_username,
			R.id.tv_share_list_item_time, R.id.tv_share_list_item_support,
			R.id.tv_share_list_item_comment };
	private String[] strs = { "title", "shid", "uid", "description", "username",
			"date", "agree_cnt", "comment_cnt" };

	public NewSkillFragment(String userdata) {
		super();
		this.userdata = userdata;
	}

	/**
	 * 网络通信部分 listmap 是操作，0表示不是新技能
	 */
	Map<String, Object> map = JsonService.getNewSkills(0);
	private String jsonString = " ";
	private List<Map<String, Object>> listData;
	private List<Map<String, Object>> lsBuff;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_newskill, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TaskManager taskManager = TaskManager.getInstance();
		Task task = new Task("newskills", map, 0, newSkillHandler);
		taskManager.addTask(task);

		mListView = (XListView) view.findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView shidTV = (TextView) arg1
						.findViewById(R.id.tv_share_list_item_shid);
				TextView uidTV = (TextView) arg1
						.findViewById(R.id.tv_share_list_item_uid);
				TextView titlefordetail = (TextView) arg1
						.findViewById(R.id.tv_share_list_item_tilte);
				TextView timetv = (TextView) arg1
						.findViewById(R.id.tv_share_list_item_time);
				uid = Integer.parseInt(uidTV.getText().toString());
				shid = Integer.parseInt(shidTV.getText().toString());
				titledetail = titlefordetail.getText().toString();
				date = timetv.getText().toString();
				Intent intent = new Intent(getActivity(), NewSkillDetail.class);
				intent.putExtra("user", userdata);
				intent.putExtra("shid", shid);
				intent.putExtra("uid", uid);
				intent.putExtra("title", titledetail);
				intent.putExtra("date", date);
				startActivity(intent);
			}
		});
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
				start = ++refreshCnt;

				/**
				 * @author GT 清空list表，从服务重新获取更新数据
				 */
				listData.clear();// 清空list表
				map = JsonService.getNewSkills(0);// need to change
				i--;
				String jsonString = HttpClients.callClient("newskills", map, 0);// 从服务器重新获取信息
				lsBuff = JsonTools.getListMap("newskills", jsonString);

				for (Map<String, Object> map : lsBuff) {
					listData.add(map);
				}

				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.newskill_list_item, strs, ids);
				onLoad();
			}
		}, 2000);
	}

	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				/**
				 * @author GT lsBuff 从服务加载更多数据，添加到界面list框架上
				 */
				map = JsonService.getNewSkills(i);
				i++;
				String jsonString = HttpClients.callClient("newskills", map, 0);// 从服务器重新获取信息
				lsBuff = JsonTools.getListMap("newskills", jsonString);
				for (Map<String, Object> map : lsBuff) {
					listData.add(map);
				}

				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.newskill_list_item, strs, ids);
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	private Handler newSkillHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			if (jsonString.length() > 3) {
				Log.i(TAG, jsonString);
				listData = JsonTools.getListMap("newskills", jsonString);
				lsBuff = new ArrayList<Map<String, Object>>();
				SimpleAdapter mAdapter = new SimpleAdapter(getActivity()
						.getApplicationContext(), listData,
						R.layout.newskill_list_item, strs, ids);
				mListView.setAdapter(mAdapter);
			} else {
				Log.i(TAG, "获取失败");
			}
		}
	};
}
