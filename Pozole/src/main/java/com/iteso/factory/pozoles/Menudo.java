package main.java.com.iteso.factory.pozoles;

import main.java.com.iteso.factory.Pozole;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 9/27/13
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class Menudo extends Pozole {
    public Menudo(){
        name = "Menudo";
        broth = "Caldo Rojo";
        toppings.add("Oregano");
        toppings.add("Col");
        toppings.add("Rabanos");

    }

}
