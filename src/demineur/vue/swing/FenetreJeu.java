package demineur.vue.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

import demineur.controleur.CtrlDemineur;
import demineur.vue.IObservable;

public class FenetreJeu extends JFrame implements IObservable {
	private CtrlDemineur monCt;
	private JTextField jtfTaille;
	private CaseDemineur[][] cases;
	private JPanel panelDemineur;
	private final ImageIcon drapeau = new ImageIcon("images/drapeau.png");
	private final ImageIcon bombe = new ImageIcon("images/bombe.png");

	public FenetreJeu(CtrlDemineur ctl) {
		super("Demineur");
		this.monCt = ctl;

		JLabel lTaille = new JLabel("Choisir taille : ");
		this.jtfTaille = new JTextField(6);
		JButton bOk = new JButton("OK");
		bOk.setActionCommand("valid");

		JPanel pSaisie = new JPanel(new FlowLayout());
		pSaisie.add(lTaille);
		pSaisie.add(jtfTaille);
		pSaisie.add(bOk);

		bOk.addActionListener(monCt);
        jtfTaille.addKeyListener(monCt);

		JPanel pMessage = new JPanel(new FlowLayout());

		this.add(pSaisie, BorderLayout.NORTH);
		panelDemineur = new JPanel();
		this.add(panelDemineur, BorderLayout.CENTER);
		this.add(pMessage, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

		this.pack();
		this.setSize(500, 600);
		this.setVisible(true);
	}

	private JPanel createPanelDemineur() {
		int taille = monCt.getModele().getTaille();
		this.cases = new CaseDemineur[taille][taille];
		JPanel pTable = new JPanel(new GridLayout(taille, taille));

		for (int i = 0; i < this.getTailleSaisie(); i++) {
			for (int j = 0; j < this.getTailleSaisie(); j++) {
				cases[i][j] = new CaseDemineur(i, j);

				if (monCt.getModele().estDrapeau(i, j)) // si c'est un drapeau
					cases[i][j].setIcon(drapeau); // on affiche un drapeau

				if (monCt.getModele().estDecouvert(i, j) ) {
					cases[i][j].setText(""+ monCt.getModele().getTableauVisible()[i][j]);
					cases[i][j].setBackground(Color.white);
					if (cases[i][j].getText().equals("0"))
						cases[i][j].setText("");
				}

				if (monCt.isPerdu() && monCt.getModele().getTableauMines()[i][j]) {
					cases[i][j].setIcon(bombe);
				}
				cases[i][j].setPreferredSize(new Dimension(45, 45));
				cases[i][j].addMouseListener(monCt);
				pTable.add(cases[i][j]);
			}
		}
		if (monCt.isPerdu()) {
			for (int i = 0; i < this.getTailleSaisie(); i++) {
				for (int j = 0; j < this.getTailleSaisie(); j++) {
					cases[i][j].removeMouseListener(monCt);
				}
			}
		}
		return pTable;
	}

	public int getTailleSaisie() {
		return Integer.parseInt(jtfTaille.getText());
	}

	public void afficheErreur(String s) {
        JOptionPane popup = new JOptionPane();
        popup.showMessageDialog(null, s , "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public void afficheModele() {
		panelDemineur.removeAll();
		panelDemineur.add(this.createPanelDemineur());
		panelDemineur.validate();
		this.validate();
	}

	public void afficheFin(String s) {
        JOptionPane popup = new JOptionPane();
        popup.showMessageDialog(null, s, "Fin partie", JOptionPane.WARNING_MESSAGE);
	}

}
