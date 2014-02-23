package main.java.com.iteso.decorators;

import main.java.com.iteso.factory.Pozole;

public abstract class PozoleDecorator extends Pozole {
	public String description = "Any Pozole ";
    public String getDescription(){
        return description;
    }
	public abstract void addTopping(Pozole pozole);
}
