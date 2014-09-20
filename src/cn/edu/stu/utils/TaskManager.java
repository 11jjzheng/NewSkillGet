package cn.edu.stu.utils;

/*任务管理器，由其管理任务队列
 * 所用链表为先进先出链表
 * set用以排除重复任务
 */
import java.util.HashSet;  
import java.util.LinkedList;  
import java.util.Set;  
  
public class TaskManager {  
    // UI请求队列  
    private LinkedList<Task> tasks;  
    // 任务不能重复  
    private Set<String> taskIdSet;  
  
    private static TaskManager taskMananger;  
  
    private TaskManager() {  
  
        tasks = new LinkedList<Task>();  
        taskIdSet = new HashSet<String>();  
          
    }  
  
    /*synchronized关键字表示只能由一个线程在一段时间内调用此函数
     * 此函数用以让程序只维护一个任务管理器
     */
    public static synchronized TaskManager getInstance() {  
        if (taskMananger == null) {  
            taskMananger = new TaskManager();  
        }  
        return taskMananger;  
    }  
  
    //在链表尾部增加任务
    public void addTask(Task task) {  
        synchronized (tasks) {  
            if (!isTaskRepeat(task.getFileId())) {  
                // 增加下载任务  
                tasks.addLast(task);   
            }  
            else {
            	tasks.addLast(task); //去重复任务的话删去此句
            }
        }  
  
    }  
    
    //去重所用，根据fileId判断，fileId自定义
    public boolean isTaskRepeat(String fileId) {  
        synchronized (taskIdSet) {  
            if (taskIdSet.contains(fileId)) {  
                return true;  
            } else {  
            	System.out.println("管理器增加任务："+ fileId);
                taskIdSet.add(fileId);  
                return false;  
            }  
        }  
    }  
      
    //从链表头部取出任务
    public Task getTask() {  
        synchronized (tasks) {  
            if (tasks.size() > 0) {  
            	System.out.println("管理器取出任务");
                Task task = tasks.removeFirst();  
                return task;  
            }  
        }  
        return null;  
    }  
}  
