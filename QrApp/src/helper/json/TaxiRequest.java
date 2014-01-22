package helper.json;

import java.util.List;

public class TaxiRequest {

	int offset;
	List<Taxi> taxidispatches;
	
	public int getOffset(){
		return offset;
	}
	
	public List<Taxi> getTaxis(){
		return taxidispatches;
	}
}
