package demineur.vue;

import demineur.vue.swing.CaseDemineur;
import javax.swing.*;


public interface IObservable {
	public int getTailleSaisie();
    public String getNomSaisie();
	public void afficheErreur(String s);
	public void afficheModele();
	public void afficheFin(String s);
    public void chrono();
    public Timer getTimer1();
}
