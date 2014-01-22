package helper.json;

import java.util.List;

public class ParkingsRequest {

	int offset;
	List<Parking> parkings;
	
	public int getOffset(){
		return offset;
	}
	
	public List<Parking> getData(){
		
		return parkings;
	}
}
