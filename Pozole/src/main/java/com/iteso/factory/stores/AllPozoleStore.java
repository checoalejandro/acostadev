package main.java.com.iteso.factory.stores;

import main.java.com.iteso.factory.Pozole;
import main.java.com.iteso.factory.PozoleStore;
import main.java.com.iteso.factory.pozoles.Menudo;
import main.java.com.iteso.factory.pozoles.PozoleBlancoBuche;
import main.java.com.iteso.factory.pozoles.PozoleBlancoCarnaza;
import main.java.com.iteso.factory.pozoles.PozoleRojoCachete;
import main.java.com.iteso.factory.pozoles.PozoleRojoOreja;
import main.java.com.iteso.factory.pozoles.PozoleRojoPierna;
import main.java.com.iteso.factory.pozoles.PozoleRojoPollo;
import main.java.com.iteso.factory.pozoles.PozoleRojoTrompa;
import main.java.com.iteso.factory.pozoles.PozoleVerdeCachete;
import main.java.com.iteso.factory.pozoles.PozoleVerdeOreja;
import main.java.com.iteso.factory.pozoles.PozoleVerdePierna;
import main.java.com.iteso.factory.pozoles.PozoleVerdePollo;
import main.java.com.iteso.factory.pozoles.PozoleVerdeTrompa;
import main.java.com.iteso.factory.pozoles.Pozolito;

public class AllPozoleStore extends PozoleStore{

	@Override
	protected Pozole createPozole(String meat) {
		// TODO Auto-generated method stub
		if (meat.equals("blanco con carnaza"))
            return new PozoleBlancoCarnaza();
        else if (meat.equals("blanco con buche") )
            return new PozoleBlancoBuche();
        else if( meat.equals("menudo"))
        	return new Menudo();
        else if (meat.equals("rojo con pollo"))
            return new PozoleRojoPollo();
        else if (meat.equals("rojo con cachete") )
            return new PozoleRojoCachete();
        else if (meat.equals("rojo con oreja"))
            return new PozoleRojoOreja();
        else if (meat.equals("rojo con pierna") )
            return new PozoleRojoPierna();
        else if (meat.equals("rojo con trompa"))
            return new PozoleRojoTrompa();
        else if (meat.equals("verde con pollo"))
            return new PozoleVerdePollo();
        else if (meat.equals("verde con cachete") )
            return new PozoleVerdeCachete();
        else if (meat.equals("verde con oreja"))
            return new PozoleVerdeOreja();
        else if (meat.equals("verde con pierna") )
            return new PozoleVerdePierna();
        else if (meat.equals("verde con trompa"))
            return new PozoleVerdeTrompa();
        else if (meat.equals("pozolito"))
            return new Pozolito();
        else return null;
	}

}
