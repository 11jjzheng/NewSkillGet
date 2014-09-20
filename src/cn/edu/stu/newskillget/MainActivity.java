package cn.edu.stu.newskillget;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.fragment.LeftFragment;
import cn.edu.stu.fragment.MainFragment;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.view.SlidingMenu;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {
	private SlidingMenu mSlidingMenu;// 侧边栏的view
	private LeftFragment leftFragment; // 左侧边栏的碎片化view
	private MainFragment centerFragment;// 中间内容碎片化的view
	private FragmentTransaction ft; // 碎片化管理的事务
	private long exitTime = 0;
	private static final String TAG_STRING = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去标题栏
     	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        //个人信息传入
		Intent intent = this.getIntent();
		String userdata = intent.getStringExtra("user");
		if(userdata != null) {
			userdata = userdata.replace('[', ' ').replace(']', ' ');
			Log.i(TAG_STRING, userdata);
		}
        
        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		ft = this.getSupportFragmentManager().beginTransaction();
		
		JSONObject jsonObject;
		String username = ""; 
		try {
			jsonObject = new JSONObject(userdata);
			username = jsonObject.getString("username");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leftFragment = new LeftFragment(userdata);
		ft.replace(R.id.left_frame, leftFragment);

		Bundle bundle = new Bundle();
		bundle.putInt("item", 1);  //初始化页面
		bundle.putString("user", userdata);
		centerFragment = MainFragment.newInstance(bundle);
		
		ft.replace(R.id.center_frame, centerFragment);
		ft.commit();
		
		
    }

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
