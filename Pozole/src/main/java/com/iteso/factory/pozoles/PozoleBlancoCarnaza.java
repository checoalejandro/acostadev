package main.java.com.iteso.factory.pozoles;

import main.java.com.iteso.factory.Pozole;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PozoleBlancoCarnaza extends Pozole{
    public PozoleBlancoCarnaza(){
        name = "Pozole Blanco con Carnaza";
        broth = "Caldo Blanco";
        toppings.add("Oregano");
        toppings.add("Col");
        toppings.add("Rabanos");
        toppings.add("Carnaza");

    }
}
