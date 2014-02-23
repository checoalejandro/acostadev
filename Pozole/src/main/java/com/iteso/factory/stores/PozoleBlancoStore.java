package main.java.com.iteso.factory.stores;

import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.PozoleBlancoBuche;
import main.java.com.iteso.factory.pozoles.PozoleBlancoCarnaza;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 8/26/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class PozoleBlancoStore extends PozoleStore{
    @Override
    protected Pozole createPozole(String meat) {
        if (meat.equals("carnaza"))
            return new PozoleBlancoCarnaza();
        else if (meat.equals("buche") )
            return new PozoleBlancoBuche();
        else return null;


    }
}
