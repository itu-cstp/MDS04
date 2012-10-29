package macgyvers.mds04;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.bind.JAXBException;

import macgyvers.mds04.xml.Cal;
import macgyvers.mds04.xml.CalSerializer;
import macgyvers.mds04.xml.Task;
import macgyvers.mds04.xml.TaskList;
import macgyvers.mds04.xml.TaskSerializer;

public class TaskHandler {
	
	private static TaskHandler instance = new TaskHandler();
	private Queue<Task> notExecuted = new LinkedList<Task>();
	private HashMap<String, Task> executed = new HashMap<String, Task>();
	
	private TaskHandler(){
		
	}
	
	public void submitTask(Task task){
		notExecuted.offer(task);
	}
	
	public static TaskHandler getInstance(){
		return instance;
	}
	
	
	
	public Task popNotExecuted() {
		return notExecuted.remove();
	}

	public boolean isExecuted(String id) {
		return executed.containsKey(id);
	}
	
	public void putExecuted(Task task){
		executed.put(task.id, task);
	}
	
	public boolean hasJobs(){
		return !notExecuted.isEmpty();
	}

	public static void main(String[] args) {
		TaskHandler handler = TaskHandler.getInstance();
		CalSerializer ser = new CalSerializer();
		try {
			Cal t = ser.deserialize();
			for(Task task : t.tasks){
				System.out.println("Task: "+task);
			}
		} catch (FileNotFoundException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class TaskExecuter extends Thread {
		TaskHandler handler;
		public TaskExecuter(){
			handler = TaskHandler.getInstance();
		}
		public void start(){
			while(true){
				if(handler.hasJobs()){
					Task task = handler.popNotExecuted();
					if(!task.conditions.isEmpty()){
						//tjek conditions for hvert osv...
					}
				}
					
			}
			
		}
	}
}