package main.java.com.iteso.factory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Pozole {
    public String name;
    public String broth;
    public ArrayList toppings = new ArrayList();
    public String description;


    public void serve() {
        System.out.println("Serving..." );
    }

    public void prepare() {
        System.out.println("Preparing " + name);
        System.out.println("Adding corn kernels..." );
        System.out.println("Adding broth..." );
        System.out.println("Adding toppings:" );
        for (int i = 0; i < toppings.size(); i++){
            System.out.println("    " + toppings.get(i));
        }

    }
    public String getName(){
        return name;
    }
    
    
    public String getDescription(){
    	return description;
    }
}
