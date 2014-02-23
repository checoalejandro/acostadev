package main.java.com.iteso.decorator.toppings;

import main.java.com.iteso.decorators.PozoleDecorator;
import main.java.com.iteso.factory.Pozole;

public class Broth extends PozoleDecorator {
	
	Pozole pozole;
	
	public Broth(Pozole pozole){
		this.pozole = pozole;
	}

	@Override
	public String getDescription(){
		return this.pozole.description += " Broth";
	}

	@Override
	public void addTopping(Pozole pozole) {
		// TODO Auto-generated method stub
		
	}

}
