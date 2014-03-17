package demineur.vue.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.Timer;

import demineur.controleur.CtrlDemineur;
import demineur.vue.IObservable;

public class FenetreJeu extends JFrame implements IObservable {
	private CtrlDemineur monCt;
	private JTextField jtfTaille;
    private JTextField jtfNom;
    private int minute,seconde;
    private JLabel lTemps = new JLabel(""+minute+":"+seconde);
    private ActionListener tache_timer;
    private Timer timer1;

	private CaseDemineur[][] cases;
	private JPanel panelDemineur;
	private final ImageIcon drapeau = new ImageIcon("images/drapeau.png");
	private final ImageIcon bombe = new ImageIcon("images/bombe.png");

	public FenetreJeu(CtrlDemineur ctl) {
		super("Jeu du Demineur");
		this.monCt = ctl;

        JLabel lNom = new JLabel("Nom : ");
        this.jtfNom = new JTextField(9);
		JLabel lTaille = new JLabel("Choisir taille : ");
		this.jtfTaille = new JTextField(5);
		JButton bOk = new JButton("OK");
        lTemps = new JLabel();
		bOk.setActionCommand("valid");


		JPanel pSaisie = new JPanel(new FlowLayout());
        pSaisie.add(lNom);
        pSaisie.add(jtfNom);
		pSaisie.add(lTaille);
		pSaisie.add(jtfTaille);
		pSaisie.add(bOk);

        // on ajoute la gestion des Evenements
		bOk.addActionListener(monCt);
        jtfTaille.addKeyListener(monCt);

        // on construit la fenetre
		this.add(pSaisie, BorderLayout.NORTH);
		panelDemineur = new JPanel();
		this.add(panelDemineur, BorderLayout.CENTER);

        JPanel pMontre = new JPanel();
        pMontre.add(lTemps);
        this.add(pMontre, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.pack();
		this.setSize(500, 500);
        this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


    public void afficheModele() {
        panelDemineur.removeAll();
        panelDemineur.add(this.createPanelDemineur());
        panelDemineur.validate();
        this.validate();
    }

	private JPanel createPanelDemineur() {
		int taille = monCt.getModele().getTaille();
		this.cases = new CaseDemineur[taille][taille];
		JPanel pTable = new JPanel(new GridLayout(taille, taille));

		for (int i = 0; i < taille ; i++) {
			for (int j = 0; j < taille; j++) {
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

		if (monCt.isPerdu() || monCt.getModele().gagne()) {
			for (int i = 0; i < taille ; i++) {
				for (int j = 0; j < taille ; j++) {
					cases[i][j].removeMouseListener(monCt);
				}
			}
		}
		return pTable;
	}

    public void chrono(){
        minute = 0;
        seconde = 0;
        tache_timer= new ActionListener()  {
            public void actionPerformed(ActionEvent e1)  {
                seconde++;
                if(seconde==60)  {
                    seconde=0;
                    minute++;
                }
                //Afficher le chrono dans un JLabel
                lTemps.setText(minute+":"+seconde);
            }
        };
        //Action et temps execution de la tache
        timer1=new Timer(1000,tache_timer);
        //Demarrer le chrono
        timer1.start();
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconde() {
        return seconde;
    }

    public Timer getTimer1(){
        System.out.println(timer1.toString());
        return timer1;
    }

	public int getTailleSaisie() {
		return Integer.parseInt(jtfTaille.getText());
	}

    public String getNomSaisie(){
        return jtfNom.getText();
    }

	public void afficheErreur(String s) {
        JOptionPane popup = new JOptionPane();
        popup.showMessageDialog(null, s, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public void afficheFin(String s) {
        JOptionPane popup = new JOptionPane();
        popup.showMessageDialog(null, s, "Fin partie", JOptionPane.WARNING_MESSAGE);
	}

}
