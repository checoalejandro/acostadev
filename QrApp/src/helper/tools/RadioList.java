package helper.tools;

import java.util.ArrayList;

import android.widget.RadioButton;

public class RadioList extends ArrayList<RadioButton>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RadioList(){
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
