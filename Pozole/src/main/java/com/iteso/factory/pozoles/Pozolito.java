package main.java.com.iteso.factory.pozoles;

import main.java.com.iteso.factory.Pozole;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 9/27/13
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class Pozolito extends Pozole {
    public Pozolito(){
        name = "Pozolito";
        broth = "Caldo de pollo";
        toppings.add("Oregano");
        toppings.add("Col");
        toppings.add("Rabanos");
        toppings.add("Granos");
    }

}
