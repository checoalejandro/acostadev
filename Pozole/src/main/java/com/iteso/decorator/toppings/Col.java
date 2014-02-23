package main.java.com.iteso.decorator.toppings;

import main.java.com.iteso.decorators.PozoleDecorator;
import main.java.com.iteso.factory.Pozole;

public class Col extends PozoleDecorator {
	
	Pozole pozole;
	
	public Col(Pozole pozole){
		this.pozole = pozole;
	}

	public String getDescription(){
		return this.pozole.description += " Col";
	}

	@Override
	public void addTopping(Pozole pozole) {
		// TODO Auto-generated method stub
		
	}
	

}
