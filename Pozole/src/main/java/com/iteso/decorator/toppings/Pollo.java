package main.java.com.iteso.decorator.toppings;

import main.java.com.iteso.decorators.PozoleDecorator;
import main.java.com.iteso.factory.Pozole;

public class Pollo extends PozoleDecorator {
	
	Pozole pozole;
	
	public Pollo(Pozole pozole){
		this.pozole = pozole;
	}

	public String getDescription(){
		return this.pozole.description += " Pollo";
	}

	@Override
	public void addTopping(Pozole pozole) {
		// TODO Auto-generated method stub
		
	}

}
