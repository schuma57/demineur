package demineur.vue;

import demineur.vue.swing.CaseDemineur;

public interface IObservable {
	public int getTailleSaisie();
    public String getNomSaisie();
	public void afficheErreur(String s);
	public void afficheModele();
	public void afficheFin(String s);
}
