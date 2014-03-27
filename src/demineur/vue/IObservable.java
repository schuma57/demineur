package demineur.vue;

import demineur.vue.swing.CaseDemineur;
import javax.swing.*;


public interface IObservable {
	public int getTailleSaisie();
    public String getNomSaisie();
	public void afficheErreur(String s);
	public void afficheModele();
	public void afficheFin(String s);
    public void afficheScores(String s);
    public void afficheScoreBdd(String s);
    public void chrono();
    public Timer getTemps();
    public int getMinute();
    public int getSeconde();
}
