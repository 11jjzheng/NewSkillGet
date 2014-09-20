package cn.edu.stu.utils;

import java.util.Map;

import android.os.Handler;
import android.os.Message;

/*任务单元，传入文件名字区分任务是否重复
 * 更改run方法可以执行不同任务
 */
public class Task implements Runnable {
	private String action_flags = "";
	private int num;
	private Map<String, Object> map;
	private String resultString;
	private Handler UIHandler;

	public Task(String action_flags, Map<String, Object> map, int num, Handler UIHandler) {
		this.action_flags = action_flags;
		this.num = num;
		this.map = map;
		this.UIHandler = UIHandler;
	}

	@Override
	public void run() {
		// String name=Thread.currentThread().getName();
		 
	    HttpClients login = new HttpClients();
	    resultString = login.callClient(action_flags, map, num);
	    Message msg = UIHandler.obtainMessage(0, resultString);
	    UIHandler.sendMessage(msg);
	}

	public String getResultString() {
		return resultString;
	}

	public String getFileId() {
		return action_flags;
	}

}
