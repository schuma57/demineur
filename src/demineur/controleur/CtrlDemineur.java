package demineur.controleur;

import java.awt.event.*;
import java.io.IOException;

import demineur.Demineur;
import demineur.Partie;
import demineur.highscores.HighscoreManager;
import demineur.vue.IObservable;
import demineur.vue.swing.CaseDemineur;

public class CtrlDemineur implements ActionListener, MouseListener {
	private Demineur modele;
	private IObservable vue;
	private boolean perdu;
    private HighscoreManager hs = new HighscoreManager();


	@Override
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();

		if (s.equals("OK")) {
            try{
			    this.modele = new Demineur(vue.getTailleSaisie(), vue.getNomSaisie());
            }catch (IllegalArgumentException e){
                vue.afficheErreur(e.getMessage());
            }
            perdu = false;
            vue.chrono();
			vue.afficheModele();
		}

        if (s.equals("Scores")){
           vue.afficheScores(hs.getHighscoreString());
        }
	}

	public Demineur getModele() {
		return modele;
	}

	public void setVue(IObservable v) {
		this.vue = v;
	}

	public boolean isPerdu() {
		return perdu;
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		Object o = ev.getSource();
		CaseDemineur temp = (CaseDemineur) o;

		if ((ev.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			try{
				modele.setDrapeau(temp.getL(), temp.getC());
			}
			catch(RuntimeException e){
				vue.afficheErreur(e.getMessage());
			}
			vue.afficheModele();
		}

		if ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			modele.decouvreTable(temp.getL(), temp.getC());
            perdu = !modele.decouvreTable(temp.getL(), temp.getC());

            if (modele.gagne()){
                vue.getTemps().stop();
                modele.getPartieC().setSecondes(vue.getSeconde() + vue.getMinute()*60);

                vue.afficheFin("Bravo, " +modele.getPartieC().getNomJoueur() +" " +
                        "\nGagne en : " +vue.getMinute() +" min " +vue.getSeconde() +" sec");

                hs.addScore(modele.getPartieC().getNomJoueur(), modele.getPartieC().getTaille());
            }

            vue.afficheModele();

			if (perdu) {
                vue.getTemps().stop();
				vue.afficheFin("Boum ! " +modele.getPartieC().getNomJoueur() +" Perd !!");
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
	}

	@Override
	public void mouseExited(MouseEvent ev) {
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
	}
}
