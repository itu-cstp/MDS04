package macgyvers.mds04;

import java.util.ArrayList;

// Enum class made to make it easier to keep track of dependencies.

enum TaskType{
	unknown, handin, review(new String[] {"handin"}), reject(new String[]{"handin", "review"}), approve(new String[]{"handin", "review"}), qualify(new String[]{"handin", "review", "approve"});
	
	public ArrayList<String> conditions;
	
	private TaskType(String[] conditions){
		for(int i=0; i<conditions.length; i++) 
			this.conditions.add(conditions[i]);
	}
	// overload
	private TaskType(){}
	
	public static TaskType getType(String name){
		switch(name){
		case "handin": return TaskType.handin;
		case "review": return TaskType.review;
		case "reject": return TaskType.reject;
		case "approve": return TaskType.approve;
		case "qualify": return TaskType.qualify;
		}
		return TaskType.unknown;
		
	}
}