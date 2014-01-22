package helper.json;

public class ParentCateg {
	int catid;
	CategList sub;
	String cat_name;
	
	public int getCatId(){
		return this.catid;
	}
	
	public String getName(){
		return this.cat_name;
	}
	
	public CategList getSubcategories(){
		return this.sub;
	}
}