package cn.edu.stu.utils;

/*�������������������������
 * ��������Ϊ�Ƚ��ȳ�����
 * set�����ų��ظ�����
 */
import java.util.HashSet;  
import java.util.LinkedList;  
import java.util.Set;  
  
public class TaskManager {  
    // UI�������  
    private LinkedList<Task> tasks;  
    // �������ظ�  
    private Set<String> taskIdSet;  
  
    private static TaskManager taskMananger;  
  
    private TaskManager() {  
  
        tasks = new LinkedList<Task>();  
        taskIdSet = new HashSet<String>();  
          
    }  
  
    /*synchronized�ؼ��ֱ�ʾֻ����һ���߳���һ��ʱ���ڵ��ô˺���
     * �˺��������ó���ֻά��һ�����������
     */
    public static synchronized TaskManager getInstance() {  
        if (taskMananger == null) {  
            taskMananger = new TaskManager();  
        }  
        return taskMananger;  
    }  
  
    //������β����������
    public void addTask(Task task) {  
        synchronized (tasks) {  
            if (!isTaskRepeat(task.getFileId())) {  
                // ������������  
                tasks.addLast(task);   
            }  
            else {
            	tasks.addLast(task); //ȥ�ظ�����Ļ�ɾȥ�˾�
            }
        }  
  
    }  
    
    //ȥ�����ã�����fileId�жϣ�fileId�Զ���
    public boolean isTaskRepeat(String fileId) {  
        synchronized (taskIdSet) {  
            if (taskIdSet.contains(fileId)) {  
                return true;  
            } else {  
            	System.out.println("��������������"+ fileId);
                taskIdSet.add(fileId);  
                return false;  
            }  
        }  
    }  
      
    //������ͷ��ȡ������
    public Task getTask() {  
        synchronized (tasks) {  
            if (tasks.size() > 0) {  
            	System.out.println("������ȡ������");
                Task task = tasks.removeFirst();  
                return task;  
            }  
        }  
        return null;  
    }  
}  
