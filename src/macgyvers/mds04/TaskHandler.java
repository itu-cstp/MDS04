package macgyvers.mds04;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
	private Cal cal;
	private CalSerializer ser;
	
	private TaskHandler(){
		ser = new CalSerializer();
		try {
			cal = ser.deserialize();
			for(Task task : cal.tasks){
				//add tasks to the correct list or map
				if(!task.status.equals("executed"))
					notExecuted.add(task);
				else executed.put(task.id, task);
			}
		} catch (FileNotFoundException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cal.tasks.size() +" Tasks read from XML file.");
	}
	
	public void submitTask(Task task){
		notExecuted.offer(task);
	}
	
	public static TaskHandler getInstance(){
		return instance;
	}
	

	public class TaskExecuter extends Thread {
		TaskHandler handler;
		public TaskExecuter(){
			handler = TaskHandler.getInstance();
		}
		public void start(){
			while(true){
				//if there are jobs in the queue
				if(!handler.notExecuted.isEmpty()){
					//pop the task from the queue
					Task task = handler.notExecuted.remove();
					//if it has conditions, check whether they're fullfilled.
					if(!task.conditions.isEmpty()){
						for(String condition : task.conditions){
							//this is where we make use of the separate idNumber. If the conditions are not in executed list.
							if(!executed.containsKey(condition+"-"+task.idNumber)){
								handler.submitTask(task); //submit task in the back of the queue and pop another :-)
								System.out.println("Delaying execution of: " + task.toString());
								continue;
							}
						}
						//if there are no conditions before execution
					} else {
						task.status = "executed";
						handler.executed.put(task.id, task);
						//save state
						Collection<Task> fullList = new ArrayList<Task>();
						fullList.addAll(notExecuted); fullList.addAll(executed.values());
						cal.tasks = fullList;
						try {
							ser.serialize(cal);
						} catch (JAXBException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
					
			}
			
		}
	}
}