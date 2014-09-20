package cn.edu.stu.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.newskillget.MainActivity;
import cn.edu.stu.newskillget.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeftFragment extends Fragment implements OnClickListener{
	private static final String TAG = "LeftFragment";
	/**
	 * 分別定义左侧选项的常量
	 */
	private static final int PERSONAL=0;
	private static final int SHARE=1;
	private static final int BEGSKILL=2;
	private static final int MESSAGE=3;
	private static final int EXIT=4;
	
	private TextView username;
	private String userdata = " ";
	private View view;
	
	private int[] llIds={R.id.ll_personal,R.id.ll_share,R.id.ll_beg_skill,R.id.ll_message,R.id.ll_exit};
	private LinearLayout[] llMenu=new LinearLayout[5];
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.left_fragment, null);
		
		for(int i=0;i<llMenu.length;i++){
			llMenu[i]=(LinearLayout)view.findViewById(llIds[i]);
			llMenu[i].setOnClickListener(this);
		}
		return view;
	}

	public LeftFragment(String userdata) {
		super();
		this.userdata = userdata;
		Log.i(TAG, userdata);
	}
	
	public LeftFragment() {
		super();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		username = (TextView) view.findViewById(R.id.username);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userdata);
			String uname = jsonObject.getString("username");
			username.setText(uname);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		int flag=-1;
		switch (arg0.getId()) {
		case R.id.ll_personal:
			flag=LeftFragment.PERSONAL;
			break;
		case R.id.ll_share:
			flag=LeftFragment.SHARE;
			break;
		case R.id.ll_beg_skill:
			flag=LeftFragment.BEGSKILL;
			break;
		case R.id.ll_message:
			flag=LeftFragment.MESSAGE;
			break;
		case R.id.ll_exit:
			flag=LeftFragment.EXIT;
			break;
		default:
			break;
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("item", flag);
		bundle.putString("user", userdata);
		Log.v(TAG, ""+flag);
		
		MainFragment f2 = MainFragment.newInstance(bundle);  
	      
       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.center_frame, f2).commit();  
		
    	((MainActivity) getActivity()).showLeft();
	}
}
