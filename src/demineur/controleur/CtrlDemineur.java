package demineur.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import demineur.Demineur;
import demineur.vue.IObservable;
import demineur.vue.swing.CaseDemineur;

public class CtrlDemineur implements ActionListener, MouseListener {
	private Demineur modele;
	private IObservable vue;
	private boolean perdu;

	@Override
	public void actionPerformed(ActionEvent ae) {
		String s = ae.getActionCommand();

		if (s.equals("valid")) {
            try{
			    this.modele = new Demineur(vue.getTailleSaisie());
            }catch (IllegalArgumentException e){
                vue.afficheErreur(e.getMessage());
            }
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

			if (!modele.decouvreTable(temp.getL(), temp.getC())) {
				this.perdu = true;
				vue.afficheFin("Vous avez Perdu !! :-(");
			}

			if (modele.gagne())
				vue.afficheFin("Vous avez Gagne !!");

			vue.afficheModele();
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
