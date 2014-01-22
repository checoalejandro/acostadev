package helper.tools;

import java.util.ArrayList;

import android.widget.CheckBox;

public class CheckBoxList extends ArrayList<CheckBox>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckBoxList(){
		super();
	}
	
	public boolean isAnswered(){
		int i = 0;
		for(i = 0; i < this.size(); i++){
			if(this.get(i).isChecked()){
				return true;
			}
		}
		return false;
	}
}
