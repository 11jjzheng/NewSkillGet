package cn.edu.stu.newskillget;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import cn.edu.stu.utils.TaskManagerThread;

public class LoginActivity extends Activity {
	private static final String TAG_STRING = "LoginActivity";
	private EditText etUserName;
	private EditText etPassword;
	private Button btnLogin;
	private TextView tvRegister;
	private LinearLayout llGotoMain;
	private CheckBox cbRemember;
	private SharedPreferences sp;
	private String username = "";
	private String password = "";

	// map 存放要发送给服务器的数据
	private Map<String, Object> map = JsonService.getLogin();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		init();

	}

	private void init() {
		sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		
		etUserName = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		tvRegister = (TextView) findViewById(R.id.tv_register);
		cbRemember = (CheckBox) findViewById(R.id.cb_remember);
		llGotoMain = (LinearLayout) findViewById(R.id.ll_goto_main);

		btnLogin.setOnClickListener(new OnLoginListener());
		tvRegister.setOnClickListener(new OnRegisterListener());
		llGotoMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				// finish();
			}
		});
		
		//线程池初始化
		TaskManager.getInstance();
		TaskManagerThread taskManagerThread = new TaskManagerThread();
		new Thread(taskManagerThread).start();
		
		if(sp.getBoolean("IsCheak", false)) {
			cbRemember.setChecked(true);
			etUserName.setText(sp.getString("username", ""));
			etPassword.setText(sp.getString("password", ""));
		}
	}

	class OnLoginListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			username = etUserName.getText().toString();
			password = etPassword.getText().toString();
			verifyLogin(username, password);
		}
	}

	class OnRegisterListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
		}
	}

	/*
	 * 验证登录信息 把要传输给服务器的用户名和密码存放在map里面
	 */
	private void verifyLogin(String username, String password) {
		// TODO

		map.put("username", username);
		map.put("password", password);

		// 第三个参数为0表示没意义
		TaskManager taskManager = TaskManager.getInstance();
		Task loginTask = new Task("login", map, 0, verifyLoginHandler);
		taskManager.addTask(loginTask);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private Handler verifyLoginHandler = new Handler() {
		public void handleMessage (Message msg) {
			String resultString = msg.obj.toString();
			if(resultString.length() > 3) {
				if(cbRemember.isChecked()) {
					Editor editor = sp.edit();
					editor.putBoolean("IsCheak", true);
					editor.putString("username", username);
					editor.putString("password", password);
					editor.commit();
				}
				Toast.makeText(LoginActivity.this, "登陆成功！", 2000).show();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				intent.putExtra("user", resultString);
				startActivity(intent);
			} else {
				Toast.makeText(LoginActivity.this, "登陆失败！", 2000).show();
			}
		}
	};
}
