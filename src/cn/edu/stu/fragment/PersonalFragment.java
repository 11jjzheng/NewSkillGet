package cn.edu.stu.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.stu.layout.MyBegSkill;
import cn.edu.stu.layout.MyLearnLayout;
import cn.edu.stu.layout.MySkillLayout;
import cn.edu.stu.newskillget.R;
import cn.edu.stu.utils.Task;
import cn.edu.stu.utils.TaskManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author ifanr
 * 
 */
public class PersonalFragment extends Fragment implements OnClickListener {
	private static final String tag = "PersonalFragment";
	private View view;
	private TextView tvMyLearn, tvMySkill, tvMyBegSkill;
	private LinearLayout llPersonalContent;
	private FragmentActivity mActivity;
	private MyLearnLayout myLearn;
	private MySkillLayout mySkill;
	private MyBegSkill myBegSkill;
	private String userdata = " ";
	private TaskManager taskManager;
	private Map<String,Object> map, map2;
	private JSONArray jsonArrayLearn, jsonArrayNew, jsonArrayGet;
	private String uname = " ";
	private int s_cnt = 0, a_cnt = 0, j_cnt = 0, uid = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_personal, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity=getActivity();
		
		tvMyLearn = (TextView) view.findViewById(R.id.tv_my_learn);
		tvMySkill = (TextView) view.findViewById(R.id.tv_my_skill);
		tvMyBegSkill = (TextView) view.findViewById(R.id.tv_my_beg_skill);
		llPersonalContent=(LinearLayout)view.findViewById(R.id.ll_personal_content);
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userdata);
			uid = jsonObject.getInt("uid");
			uname = jsonObject.getString("username");
			s_cnt = jsonObject.getInt("share_cnt");
			a_cnt = jsonObject.getInt("ask_cnt");
			j_cnt = jsonObject.getInt("join_cnt");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map = new HashMap<String, Object>();
		map.put("nickname", uname);
		map.put("skillnum", Integer.toString(s_cnt));
		map.put("begskillnum", Integer.toString(a_cnt));
		map.put("supportnum", Integer.toString(j_cnt));
		
		llPersonalContent.removeAllViews();
		myLearn=new MyLearnLayout(mActivity);
		myLearn.setPersonalInfo(map);
		llPersonalContent.addView(myLearn);
		
		map2 = new HashMap<String, Object>();
		map2.put("uid", uid);
		taskManager = TaskManager.getInstance();
		Task task = new Task("personal", map2, 0, mylearnHandler);
		taskManager.addTask(task);
		
		tvMyLearn.setOnClickListener(this);
		tvMySkill.setOnClickListener(this);
		tvMyBegSkill.setOnClickListener(this);
	}

	public PersonalFragment(String userdata) {
		super();
		this.userdata = userdata;
	}
	
	public PersonalFragment() {
		super();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_my_learn:
			llPersonalContent.removeAllViews();
			
			map2 = new HashMap<String, Object>();
			map2.put("uid", uid);
			taskManager = TaskManager.getInstance();
			Task task = new Task("personal", map2, 0, mylearnHandler);
			taskManager.addTask(task);
			
			myLearn=new MyLearnLayout(mActivity);	
			myLearn.setPersonalInfo(map);
			myLearn.setMyLearn(uid, jsonArrayLearn);
			llPersonalContent.addView(myLearn);
			break;
		case R.id.tv_my_skill:
			llPersonalContent.removeAllViews();
			mySkill=new MySkillLayout(mActivity);
			mySkill.setListData(jsonArrayNew);
			mySkill.setUserData(userdata);
			llPersonalContent.addView(mySkill);
			break;
		case R.id.tv_my_beg_skill:
			llPersonalContent.removeAllViews();
			myBegSkill=new MyBegSkill(mActivity);
			myBegSkill.setListData(jsonArrayGet);
			myBegSkill.setUserData(userdata);
			llPersonalContent.addView(myBegSkill);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getActivity().getContentResolver();
		/**
		 * 如果不拍照 或者不选择图片返回 不执行任何操作
		 */
		
		if (data != null) {
			/**
			 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
			 * 所以为了区别到底选择了那个方式获取图片要进行判断
			 * ，这里的requestCode跟startActivityForResult里面第二个参数对应  
			 *   1== 相册
			 *  2 ==相机
			 */
			if (requestCode == 1) {

				try {
					// 获得图片的uri
					Uri originalUri = data.getData();
					// 将图片内容解析成字节数组
					myLearn.mContent = readStream(resolver.openInputStream(Uri
							.parse(originalUri.toString())));
					// 将字节数组转换为ImageView可调用的Bitmap对象
					myLearn.myBitmap = getPicFromBytes(myLearn.mContent, null);
					// //把得到的图片绑定在控件上显示
					myLearn.ciPic.setImageBitmap(myLearn.myBitmap);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			} else if (requestCode == 0) {

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				FileOutputStream b = null;
				File file = new File("/sdcard/newskillget/");
				file.mkdirs();// 创建文件夹，名称为newskillget

				// 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。
				String str = null;
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
				date = new Date();
				str = format.format(date);
				String fileName = "/sdcard/myImage/" + str + ".jpg";
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (data != null) {
						Bitmap cameraBitmap = (Bitmap) data.getExtras().get(
								"data");
						System.out.println("fdf================="
								+ data.getDataString());
						myLearn.ciPic.setImageBitmap(cameraBitmap);

						System.out.println("成功======" + cameraBitmap.getWidth()
								+ cameraBitmap.getHeight());
					}

				}
			}
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}
	
	private Handler mylearnHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonString = msg.obj.toString();
			Log.i(tag, "personal" + jsonString);
			if(jsonString.length() > 3) {
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					jsonArrayLearn = jsonObject.getJSONArray("learnskills");
					jsonArrayGet = jsonObject.getJSONArray("getskills");
					jsonArrayNew = jsonObject.getJSONArray("newskills");
					myLearn.setMyLearn(uid, jsonArrayLearn);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				
			}
		}
	};
}
