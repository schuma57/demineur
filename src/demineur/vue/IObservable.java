package demineur.vue;

import demineur.vue.swing.CaseDemineur;
import javax.swing.*;


public interface IObservable {
	public int getTailleSaisie();
    public String getNomSaisie();
	public void afficheErreur(String s);
	public void afficheModele();
	public int afficheFin(String s);
    public void afficheScores(String s);
    public void afficheScoreBdd(String s);
    public void afficheTemps(int minute, int seconde);
    public void choixContinuer(int i);
}
