package main.java;

import main.java.com.iteso.decorator.toppings.Broth;
import main.java.com.iteso.decorator.toppings.Col;
import main.java.com.iteso.decorator.toppings.Oregano;
import main.java.com.iteso.decorator.toppings.Pollo;
import main.java.com.iteso.decorator.toppings.Rabanos;
import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.PozoleBlancoBuche;
import main.java.com.iteso.factory.pozoles.PozoleGeneric;
import main.java.com.iteso.factory.stores.MenudoStore;
import main.java.com.iteso.factory.stores.PozoleBlancoStore;
import main.java.com.iteso.factory.stores.PozoleRojoStore;
import main.java.com.iteso.factory.stores.PozoleVerdeStore;
import main.java.com.iteso.factory.stores.Pozolillo;



/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 9/2/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class PozoleTestDrive {
    public static void main (String[] args){
        PozoleStore verdeStore = new PozoleVerdeStore();
        PozoleStore rojoStore = new PozoleRojoStore();
        PozoleStore menuderia = new MenudoStore();
        PozoleStore pozolillo = new Pozolillo();
        PozoleStore blancoStore = new PozoleBlancoStore();

        Pozole pozole = verdeStore.orderPozole("pollo");

        System.out.println("First order is: "+ pozole.getName());
        System.out.println();
        System.out.println();
    	
        pozole = rojoStore.orderPozoleDecorator("pollo");
        System.out.println(pozole.description);
        
        System.out.println("Second order is: "+ pozole.getName());
        System.out.println();
        System.out.println();

        pozole = menuderia.orderPozole("pollo");

        System.out.println("Third order is: "+ pozole.getName());
        
        pozole = pozolillo.orderPozole("granos");
        System.out.println("Forth order is: "+ pozole.getName());
        System.out.println();
        System.out.println();
        
        pozole = blancoStore.orderPozole("carnaza");
        System.out.println("Fifth order is: "+ pozole.getName());
        System.out.println();
        System.out.println();
        
        pozole = blancoStore.orderPozole("buche");
        System.out.println("Sixth order is: "+ pozole.getName());
        System.out.println();
        System.out.println();
        
    }
}
