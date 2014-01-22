package helper.json;

public class BestList{
	int bestlistid;
	String name;
	String description;
	BestListBiznesList biznes;
	
	public int getBestListId(){
		return this.bestlistid;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public BestListBiznesList getBiznes(){
		return this.biznes;
	}
}
