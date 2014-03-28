package demineur.controleur;

import java.awt.event.*;
import java.io.IOException;

import demineur.Demineur;
import demineur.Partie;
import demineur.highscores.HighscoreManager;
import demineur.vue.IObservable;
import demineur.vue.swing.CaseDemineur;

import javax.swing.*;

public class CtrlDemineur implements ActionListener, MouseListener {
	private Demineur modele;
	private IObservable vue;
	private boolean perdu;
    private HighscoreManager hs = new HighscoreManager();
    private int minute,seconde;
    private ActionListener tache_timer;
    private Timer temps;

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
            chrono();
			vue.afficheModele();
		}

        if (s.equals("SL")){
            vue.afficheScores(hs.getHighscoreString());
        }
        if (s.equals("SG")){
            vue.afficheScoreBdd(hs.getHighscoreStringGlobal() );
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

    public void chrono(){
        minute = 0;
        seconde = 0;
        final long tempsDebut = System.currentTimeMillis();

        if(temps != null)
            temps.stop();
        tache_timer= new ActionListener()  {
            public void actionPerformed(ActionEvent e1)  {
                long tempsActuel = System.currentTimeMillis();
                seconde = (int) ((tempsActuel - tempsDebut) /1000 % 60 );
                minute = (int) ((tempsActuel - tempsDebut) /60000 );

                vue.afficheTemps(minute, seconde);
            }
        };
        temps = new Timer(1000,tache_timer);
        temps.start();
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

            if (modele.gagne()){
                temps.stop();
                modele.getPartieC().setSecondes(seconde + minute*60);

                vue.afficheFin("Bravo, " +modele.getPartieC().getNomJoueur() +" " +
                        "\nGagne en : " +minute +" min " +seconde +" sec");

                hs.addScore(modele.getPartieC() );
            }
		}

		if ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			modele.decouvreTable(temp.getL(), temp.getC());
            perdu = !modele.decouvreTable(temp.getL(), temp.getC());

            vue.afficheModele();

            if (modele.gagne()){
                temps.stop();
                modele.getPartieC().setSecondes(seconde + minute*60);

                vue.afficheFin("Bravo, " +modele.getPartieC().getNomJoueur() +" " +
                        "\nGagne en : " +minute +" min " +seconde +" sec");

                hs.addScore(modele.getPartieC() );
            }

			if (perdu) {
                temps.stop();
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
