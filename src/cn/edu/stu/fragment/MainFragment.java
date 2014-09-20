package cn.edu.stu.fragment;

import cn.edu.stu.newskillget.BegSkillPublish;
import cn.edu.stu.newskillget.LoginActivity;
import cn.edu.stu.newskillget.MainActivity;
import cn.edu.stu.newskillget.NewSkillPublish;
import cn.edu.stu.newskillget.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements OnClickListener{
	private ImageView lvLeft;
	private TextView tvTitle;
	private ImageView ivPublish;
	private static Bundle bundle;
	private int itemFlag; // 接收左侧的信息
	private FragmentManager fm;
	private FragmentTransaction ft;
	private static int VISIBLE=0;
	private static int INVISIBLE=4;
	private static int GONE=8;
	private String userdata = "";
	
	public static MainFragment newInstance(Bundle b) {
		MainFragment f2 = new MainFragment();
		MainFragment.bundle = b;
		return f2;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.center_content, null);
		lvLeft = (ImageView) mView.findViewById(R.id.iv_left);
		
		tvTitle=(TextView)mView.findViewById(R.id.tv_title);
		ivPublish=(ImageView)mView.findViewById(R.id.iv_publish);
		ivPublish.setOnClickListener(this);
		container = (LinearLayout) mView.findViewById(R.id.ll_content);
		itemFlag = bundle.getInt("item");
		userdata = bundle.getString("user");
		return mView;
	}

	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 根据接收的itemFlag判断加载对应的Fragment
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		switch (itemFlag) {
		case 0:
			tvTitle.setText("个人中心");
			ivPublish.setVisibility(INVISIBLE);
			ft.replace(R.id.ll_content, new PersonalFragment(userdata));
			break;
		case 1:
			tvTitle.setText("新技能");
			ivPublish.setVisibility(VISIBLE);
			ft.replace(R.id.ll_content, new NewSkillFragment(userdata));
			break;
		case 2:
			tvTitle.setText("求技能");
			ivPublish.setVisibility(VISIBLE);
			ft.replace(R.id.ll_content, new BegSkillFragment(userdata));
			break;
		case 3:
			tvTitle.setText("消息");
			ivPublish.setVisibility(INVISIBLE);
			ft.replace(R.id.ll_content, new MessageFragment());
			break;
		case 4:
			Toast.makeText(getActivity(), "成功退出账号！", 1000).show();
			Intent intent=new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		ft.commit();

		lvLeft.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				((MainActivity) getActivity()).showLeft();
			}
		});
	}
	
	
	@Override
	public void onClick(View arg0) {
		if(itemFlag==1){
			Intent intent=new Intent(getActivity(),NewSkillPublish.class);
			intent.putExtra("user", userdata);
			startActivity(intent);
		}else if(itemFlag==2){
			Intent intent=new Intent(getActivity(),BegSkillPublish.class);
			intent.putExtra("user", userdata);
			startActivity(intent);
		}else{
			ivPublish.setClickable(false);
		}
	}
}
