package demineur.vue;

import demineur.vue.swing.CaseDemineur;

public interface IObservable {
	public int getTailleSaisie();
	public void afficheErreur(String s);
	public void afficheModele();
	public void afficheFin(String s);
	public CaseDemineur[][] getCases();
	public void setCases(CaseDemineur[][] cases);
}
