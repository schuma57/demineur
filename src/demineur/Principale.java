package demineur;

import demineur.controleur.CtrlDemineur;
import demineur.vue.IObservable;
import demineur.vue.swing.FenetreJeu;

public class Principale{

	public static void main(String[] args) {

		CtrlDemineur controleur = new CtrlDemineur();
		IObservable vue = new FenetreJeu(controleur);
		//IObservable vue = new AwtFenetreJeu(controleur);
		
		controleur.setVue(vue);
    }
}
