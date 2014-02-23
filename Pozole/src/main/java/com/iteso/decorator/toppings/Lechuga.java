package main.java.com.iteso.decorator.toppings;

import main.java.com.iteso.decorators.PozoleDecorator;
import main.java.com.iteso.factory.Pozole;

public class Lechuga extends PozoleDecorator {
	
	Pozole pozole;
	
	public Lechuga(Pozole pozole){
		this.pozole = pozole;
	}

	public String getDescription(){
		return this.pozole.description += " Lechuga";
	}

	@Override
	public void addTopping(Pozole pozole) {
		// TODO Auto-generated method stub
		
	}

}
