package main.java.com.iteso.factory.stores;

import main.java.com.iteso.decorator.toppings.Broth;
import main.java.com.iteso.decorator.toppings.Col;
import main.java.com.iteso.decorator.toppings.Oregano;
import main.java.com.iteso.decorator.toppings.Pollo;
import main.java.com.iteso.decorator.toppings.Rabanos;
import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.PozoleGeneric;
import main.java.com.iteso.factory.pozoles.PozoleRojoCachete;
import main.java.com.iteso.factory.pozoles.PozoleRojoOreja;
import main.java.com.iteso.factory.pozoles.PozoleRojoPierna;
import main.java.com.iteso.factory.pozoles.PozoleRojoPollo;
import main.java.com.iteso.factory.pozoles.PozoleRojoTrompa;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PozoleRojoStore extends PozoleStore{
	Pozole pozole;
    @Override
    protected Pozole createPozole(String meat) {
        if (meat.equals("pollo")){
        	pozole = new Pollo(pozole);
        	pozole = new Broth(pozole);
        	pozole = new Oregano(pozole);
        	pozole = new Col(pozole);
        	pozole = new Rabanos(pozole);
        	return pozole;
        }
        else if (meat.equals("cachete") )
            return new PozoleRojoCachete();
        else if (meat.equals("oreja"))
            return new PozoleRojoOreja();
        else if (meat.equals("pierna") )
            return new PozoleRojoPierna();
        else if (meat.equals("trompa"))
            return new PozoleRojoTrompa();
        else return null;


    }
}
