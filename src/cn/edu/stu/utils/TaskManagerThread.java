package cn.edu.stu.utils;

import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
  
/*
 * �����̳߳ص��̣߳�������ѯ��������ѯ������У�������������ȡִ�У�����˯�߹̶�ʱ��
 */
public class TaskManagerThread implements Runnable {  
  
    private TaskManager taskManager;  
  
    // ����һ�������ù̶��߳������̳߳�  
    private ExecutorService pool;  
    // �̳߳ش�С  
    private final int POOL_SIZE = 3;  
    // ��ѯʱ��  
    private final int SLEEP_TIME = 1000;  
    // �Ƿ�ֹͣ  
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
            } else {  //�����ǰδ��downloadTask�����������  
                try {  
                    // ��ѯ�������ʧ�ܵ�,���¼����������  
                    // ��ѯ,  
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
