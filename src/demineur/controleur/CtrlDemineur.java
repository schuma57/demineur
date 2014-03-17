package demineur.controleur;

import java.awt.event.*;

import demineur.Demineur;
import demineur.vue.IObservable;
import demineur.vue.swing.CaseDemineur;

public class CtrlDemineur implements ActionListener, MouseListener, KeyListener {
	private Demineur modele;
	private IObservable vue;
	private boolean perdu;

	@Override
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();

		if (s.equals("valid")) {
            try{
			    this.modele = new Demineur(vue.getTailleSaisie(), vue.getNomSaisie());
            }catch (IllegalArgumentException e){
                vue.afficheErreur(e.getMessage());
            }
            perdu = false;
            vue.chrono();
			vue.afficheModele();
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
            vue.afficheModele();

			if (perdu) {
                vue.getTimer1().stop();
				vue.afficheFin("Boum ! " +modele.getParties().getNomJoueur() +" Perd !!");
			}

			if (modele.gagne()){
                vue.getTimer1().stop();
                modele.getParties().setMinutes(vue.getMinute() );
                modele.getParties().setSecondes(vue.getSeconde() );
				vue.afficheFin("Bravo, " +modele.getParties().getNomJoueur() +" " +
                        "\nGagne en : " +vue.getMinute() +" min " +vue.getSeconde() +" sec");
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

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                this.modele = new Demineur(vue.getTailleSaisie(), vue.getNomSaisie());
            }catch (IllegalArgumentException e){
                vue.afficheErreur(e.getMessage());
            }
            vue.chrono();
            vue.afficheModele();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }
}
