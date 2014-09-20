package cn.edu.stu.newskillget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.edu.stu.utils.HttpClients;
import cn.edu.stu.utils.JsonService;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;

public class RegisterActivity extends Activity {

	private Button btnReLogin;
	private EditText etUserName, etPassword, etPassword2;
	private RadioGroup rgSex;
	private Button btnRegister;
	private String strSex = "男"; //
	private static final String TAG_STRING = "RegisterActivity";

	// map
	// private JSONObject jsonObject = JsonService.getRegister();
	private Map<String, Object> map = JsonService.getRegister();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);

		initView();
	}

	private void initView() {
		btnReLogin = (Button) findViewById(R.id.btn_relogin);
		btnReLogin.setOnClickListener(new onReLoginClick());

		etUserName = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		etPassword2 = (EditText) findViewById(R.id.et_password2);

		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		rgSex.setOnCheckedChangeListener(new onRadioBtnListener());

		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(new onRegisterListener());
	}

	class onReLoginClick implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	class onRadioBtnListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			switch (arg0.getCheckedRadioButtonId()) {
			case R.id.rb_male:
				strSex = "男";
				break;
			case R.id.rb_female:
				strSex = "女";
				break;
			default:
				break;
			}
		}
	}

	class onRegisterListener implements OnClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			String username = etUserName.getText().toString();
			String password = etPassword.getText().toString();
			String password2 = etPassword2.getText().toString();
			if (!password.equals(password2)) {
				Toast.makeText(RegisterActivity.this, "两次密码不一样", 2000);
			} else {
				// String tempPW = encryptPassword(password);
				sendRegisterInfo(username, password, strSex);
			}
		}
	}

	/*
	 * 
	 */
	private void sendRegisterInfo(String username, String password,
			String strSex) {
		// TODO
		// try {
		// jsonObject.put("username", username);
		// jsonObject.put("password", password);
		// jsonObject.put("ssex", strSex);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

		map.put("username", username);
		map.put("password", password);
		map.put("sex", strSex);
		TaskManager taskManager = TaskManager.getInstance();
		Task registerTask = new Task("register", map, 0, registerHandler);
		taskManager.addTask(registerTask);
	}

	/*
	 *
	 */
	private String encryptPassword(String password) {
		// TODO
		return null;
	}

	private Handler registerHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Log.i(TAG_STRING, msg.obj.toString());
			JSONObject jsonObject;
			String resultString = "FAIL";
			try {
				jsonObject = new JSONObject(msg.obj.toString());
				resultString = jsonObject.getString("stat");
				Log.i(TAG_STRING, "result= " + resultString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			if (resultString.equals("SUC")) {
				Toast.makeText(RegisterActivity.this, "注册成功", 2000).show();
				startActivity(new Intent(RegisterActivity.this,
						LoginActivity.class));
			} else if (resultString.equals("FAIL")) {

				Toast.makeText(RegisterActivity.this, "注册失败", 2000).show();
			} else if (resultString.equals("User_E")) {
				Toast.makeText(RegisterActivity.this, "用户已存在", 2000).show();
			}
		}
	};
}
