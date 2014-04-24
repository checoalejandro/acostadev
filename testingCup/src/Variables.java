import java.util.HashMap;


public class Variables {

	public static HashMap<String, Double> simbols = new HashMap <String,Double>();
	
	
	public static void initializeVariable(String idName){
		
		if(!simbols.containsKey(idName))
			simbols.put(idName,  0.0);
	}
	
	//public static void setVariable(String idName, Double val){
		//simbols.put(idName, val);
	//}

	public static void setValue(String idName, Double val){
		simbols.put(idName, val);//
		
	}
	
	
	public static double getValue(String idName){
		if(!simbols.containsKey(idName))
			setValue(idName, 0.0);//setVariable(idName, 0.0);
		
		return simbols.get(idName);
	}
	
	
	
	
}