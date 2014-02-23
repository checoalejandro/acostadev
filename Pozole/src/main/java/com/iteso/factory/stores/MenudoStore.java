package main.java.com.iteso.factory.stores;

import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.Menudo;

/**
 * Created with IntelliJ IDEA.
 * User: rvillalobos
 * Date: 9/27/13
 * Time: 7:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class MenudoStore extends PozoleStore {

    @Override
    protected Pozole createPozole(String meat) {
        Pozole pozole = new Menudo();
        return pozole;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
