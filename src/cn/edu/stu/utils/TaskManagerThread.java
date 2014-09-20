package cn.edu.stu.utils;

import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
  
/*
 * 创建线程池的线程，采用轮询方法，轮询任务队列，若有任务则提取执行，无则睡眠固定时间
 */
public class TaskManagerThread implements Runnable {  
  
    private TaskManager taskManager;  
  
    // 创建一个可重用固定线程数的线程池  
    private ExecutorService pool;  
    // 线程池大小  
    private final int POOL_SIZE = 3;  
    // 轮询时间  
    private final int SLEEP_TIME = 1000;  
    // 是否停止  
    private boolean isStop = false;  
  
    public TaskManagerThread() {  
        taskManager = TaskManager.getInstance();  
        pool = Executors.newFixedThreadPool(POOL_SIZE);  
  
    }  
  
    @Override  
    public void run() {  
        // TODO Auto-generated method stub  
        while (!isStop) {  
            Task task = taskManager.getTask();  
            if (task != null) {  
                pool.execute(task);  
            } else {  //如果当前未有downloadTask在任务队列中  
                try {  
                    // 查询任务完成失败的,重新加载任务队列  
                    // 轮询,  
                    Thread.sleep(SLEEP_TIME);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
  
        }  
        if (isStop) {  
            pool.shutdown();  
        }  
  
    }  
  
    /** 
     * @param isStop 
     *            the isStop to set 
     */  
    public void setStop(boolean isStop) {  
        this.isStop = isStop;  
    }  
  
}  
