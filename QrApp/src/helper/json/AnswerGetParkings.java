package helper.json;

import java.util.List;

public class AnswerGetParkings {
	int status;
	String msg;
//	List<Parking> data;
	ParkingsRequest data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public ParkingsRequest getParkings(){
		return data;
	}

}
