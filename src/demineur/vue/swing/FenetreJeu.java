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
    private JSpinner jsTaille;
    private JTextField jtfNom;
    private JLabel lTemps = new JLabel();

	private CaseDemineur[][] cases;
	private JPanel panelDemineur;
	private final ImageIcon drapeau = new ImageIcon("images/drapeau.png");
	private final ImageIcon bombe = new ImageIcon("images/bombe.png");
    private final ImageIcon drap_faux = new ImageIcon("images/drapeau_faux.png");

	public FenetreJeu(CtrlDemineur ctl) {
		super("Jeu du Demineur");
		this.monCt = ctl;

        JLabel lNom = new JLabel("Pseudo : ");
        this.jtfNom = new JTextField(9);
		JLabel lTaille = new JLabel("Choisir taille : ");

        SpinnerNumberModel model = new SpinnerNumberModel(5,5,15,1);
        this.jsTaille = new JSpinner(model);
        ((JSpinner.DefaultEditor) jsTaille.getEditor()).getTextField().setEditable(false);

		JButton bOk = new JButton("OK");
        this.getRootPane().setDefaultButton(bOk);
        lTemps = new JLabel();


		JPanel pSaisie = new JPanel(new FlowLayout());
        pSaisie.add(lNom);
        pSaisie.add(jtfNom);
		pSaisie.add(lTaille);
        pSaisie.add(jsTaille);
		pSaisie.add(bOk);

        // on ajoute la gestion des Evenements
		bOk.addActionListener(monCt);

        // on construit la fenetre
		this.add(pSaisie, BorderLayout.NORTH);
		panelDemineur = new JPanel();
		this.add(panelDemineur, BorderLayout.CENTER);

        JButton bScore = new JButton("Scores Locaux");
        bScore.setActionCommand("SL");
        bScore.addActionListener(monCt);
        JButton bScoreBdd = new JButton("Scores Globaux");
        bScoreBdd.setActionCommand("SG");
        bScoreBdd.addActionListener(monCt);
        JPanel pMontre = new JPanel();
        pMontre.add(bScore);
        pMontre.add(bScoreBdd);
        pMontre.add(lTemps);
        this.add(pMontre, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.pack();
		this.setSize(600, 600);
        this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


    public void afficheModele() {
        panelDemineur.removeAll();
        panelDemineur.add(this.createPanelDemineur());
        panelDemineur.validate();
        this.validate();
        this.repaint();
    }

	private JPanel createPanelDemineur() {
		int taille = monCt.getModele().getPartieC().getTaille();
		this.cases = new CaseDemineur[taille][taille];
		JPanel pTable = new JPanel(new GridLayout(taille, taille));

		for (int i = 0; i < taille ; i++) {
			for (int j = 0; j < taille; j++) {
				cases[i][j] = new CaseDemineur(i, j);
                cases[i][j].setBackground(Color.lightGray);

				if (monCt.getModele().estDrapeau(i, j)) // si c'est un drapeau
					cases[i][j].setIcon(drapeau); // on affiche un drapeau

				if (monCt.getModele().estDecouvert(i, j) ) {
					cases[i][j].setText(""+ monCt.getModele().getTableauVisible()[i][j]);
					cases[i][j].setBackground(Color.WHITE);
					if (cases[i][j].getText().equals("0"))
						cases[i][j].setText("");
                    else if(cases[i][j].getText().equals("1"))
                        cases[i][j].setForeground(Color.BLACK );
                    else if(cases[i][j].getText().equals("2"))
                        cases[i][j].setForeground(Color.BLUE );
                    else
                        cases[i][j].setForeground(Color.RED);
                }

				if (monCt.isPerdu()) {
                    if(monCt.getModele().getTableauMines()[i][j])
					    cases[i][j].setIcon(bombe);
                    if(monCt.getModele().estDrapeau(i,j) && !monCt.getModele().getTableauMines()[i][j])
                        cases[i][j].setIcon(drap_faux);
				}
				cases[i][j].setPreferredSize(new Dimension(45, 45));
				cases[i][j].addMouseListener(monCt);

                if (monCt.getModele().estDecouvert(i, j) )
                    cases[i][j].removeMouseListener(monCt);
				pTable.add(cases[i][j]);
			}
		}

		if (monCt.isPerdu() || monCt.getModele().gagne()) {
			for (int i = 0; i < taille ; i++) {
				for (int j = 0; j < taille ; j++) {
					cases[i][j].removeMouseListener(monCt);
				}
			}
		}
		return pTable;
	}


	public int getTailleSaisie() {
        return (Integer) jsTaille.getValue();
	}

    public String getNomSaisie(){
        return jtfNom.getText();
    }

	public void afficheErreur(String s) {
        JOptionPane popup = new JOptionPane();
        popup.showMessageDialog(null, s, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public int afficheFin(String s) {
        int a= JOptionPane.showConfirmDialog(null, s+"\n\nContinuer ?", "Fin de partie",JOptionPane.YES_NO_OPTION);
        return a;
	}

    public void afficheTemps(int minute, int seconde){
        //Afficher le chrono dans un JLabel
        lTemps.setText(" " +minute+":"+seconde +" sec.");
    }

    public void afficheScores(String s){
        FenetreScore fen = new FenetreScore("Tableau des scores locaux");
        fen.getJTexte().setText(s);
    }

    public void afficheScoreBdd(String s){
        FenetreScore fen = new FenetreScore("Tableau des scores globaux");
        fen.getJTexte().setText(s);
    }

    public void choixContinuer(int i) {
        if(i != 0)
           System.exit(0);
    }
}
