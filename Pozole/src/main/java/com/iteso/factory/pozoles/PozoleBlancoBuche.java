package main.java.com.iteso.factory.pozoles;

import main.java.com.iteso.factory.Pozole;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PozoleBlancoBuche extends Pozole{
    public PozoleBlancoBuche(){
        name = "Pozole Blanco con Buche";
        broth = "Caldo Blanco";
        toppings.add("Oregano");
        toppings.add("Col");
        toppings.add("Rabanos");
        toppings.add("Buche");

    }
}
