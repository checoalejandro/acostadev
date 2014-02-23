package main.java.com.iteso.factory.stores;

import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.PozoleRojoCachete;
import main.java.com.iteso.factory.pozoles.PozoleRojoOreja;
import main.java.com.iteso.factory.pozoles.PozoleRojoPierna;
import main.java.com.iteso.factory.pozoles.PozoleRojoPollo;
import main.java.com.iteso.factory.pozoles.PozoleRojoTrompa;
import main.java.com.iteso.factory.pozoles.Pozolito;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pozolillo extends PozoleStore{
    @Override
    protected Pozole createPozole(String meat) {
        if (meat.equals("granos"))
            return new Pozolito();
        else return null;


    }
}
