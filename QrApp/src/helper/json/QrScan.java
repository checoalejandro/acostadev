package helper.json;

public class QrScan {

	int bizid;
	public String type;
	int qrid;
	String url;
	int value;
	int bestlistid;
	int idg;
	int subbizid;
	int groupid;
	int actid;
	
	public int getActivityId(){
		return actid;
	}
	
	public int getGroupId(){
		return this.groupid;
	}
	
	public int getBizId(){
		return this.bizid;
	}
	
	public int getSubBizId(){
		return this.subbizid;
	}
	
	public int getType(){
		if(this.type.equals("activity")){
			return 0;
		}else{
			if(this.type.equals("qrgame")){
				return 1;
			}else{
				return 2;
			}
		}
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public int getQrId(){
		return this.qrid;
	}
	
	public int getBestListId(){
		return this.bestlistid;
	}
	
	public String toString(){
		if(this.type.equals("qrgame")){
			return "ID: " + this.qrid + "URL: " + this.url + " Type: " + this.type;
		}
		if(this.type.equals("activity")){
			return "ID: " + this.bizid + " URL: " + this.url +" Type: " + this.type;
		}else{
			return "ID: " + this.bizid + "Type: " + this.type;
		}
	}
	
	public void setBizId(int b){
		this.bizid = b;
	}
	
	public int getIdGroup(){
		return this.idg;
	}
	
	public void setQrId(int id){
		this.qrid = id;
	}
}
