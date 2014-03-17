package demineur;

import java.util.GregorianCalendar;
import java.util.Random;

public class Demineur {
	private int taille;
	private boolean tableauMines[][];
	private char tableauVisible[][]; //nombres de mines adjacentes
	private final static char DRAPEAU='d';
	private final static char CACHE=' ';
    private Partie parties;
	

	public Demineur() {
		this(10, "Joueur");
	}

	public Demineur(int taille, String nom) {
		this.setTaille(taille);
		this.initialiseTableaux();
        this.parties.setNomJoueur(nom);
	}

	private void initialiseTableaux() {
		
		this.tableauMines = new boolean[this.taille][this.taille];
		this.tableauVisible = new char[this.taille][this.taille];
		
		// Environ 1 case sur 6 est une mine
		Random hasard = new Random(new GregorianCalendar().getTimeInMillis());

		for (int l = 0; l < this.taille; l++) {
			for (int c = 0; c < this.taille; c++) {
				int val = hasard.nextInt(6);
				if (val == 0) {
					this.tableauMines[l][c] = true;
				}
				else {
					this.tableauMines[l][c] = false;
				}
				this.tableauVisible[l][c] = Demineur.CACHE;
			}
		}
	}

	private void setTaille(int taille) {

		if (taille < 5 || taille > 15) {
			throw new IllegalArgumentException("Taille autorisee : entre 5 et 15");
		} else {
			this.taille = taille;
		}
	}

	public int getTaille() {
		return taille;
	}

    public Partie getParties(){
        return parties;
    }

	public boolean[][] getTableauMines() {
		return tableauMines;
	}

	public char[][] getTableauVisible() {
		return tableauVisible;
	}

	/**
	 * Switch Drapeau/Case non jouee
	 * @param l ligne jouee
	 * @param c colonne jouee
	 */
	public void setDrapeau(int l, int c) {
		
		if (this.tableauVisible[l][c]==Demineur.CACHE) {
			this.tableauVisible[l][c]=Demineur.DRAPEAU;
		}
		else if (this.tableauVisible[l][c]==Demineur.DRAPEAU) {
			this.tableauVisible[l][c]=Demineur.CACHE;
		}
		else {
			// l'interface doit faire en sorte que cela n'arrive jamais !
			throw new RuntimeException("Pose d'un drapeau sur une case non autorisee !!!");
		}
	}

	/**
	 * @param y ligne jouee
	 * @param x colonne jouee
	 * @return faux si boum !
	 */
	public boolean decouvreTable(int y, int x) {

		// boum s'il y a une bombe
		if (this.tableauMines[y][x]) {
			return false;
		}
		
		int nb = this.nombreMinesAdjacentes(y, x);
		this.tableauVisible[y][x] = String.valueOf(nb).charAt(0);
		
		// Si le nombre de mines autour est 0, on deploie le tableau jusqu'a arriver
		// en bordure de bombes
		if (nb == 0) {
			for (int l = y-1; l <= y + 1; l++) {
				for (int c = x-1; c <= x + 1; c++) {
					if (inDemineur(l, c) && this.tableauVisible[l][c] == Demineur.CACHE) {
						this.decouvreTable(l, c);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Calcul du nombre de mines adjacentes a la case
	 * @param y ligne jouee
	 * @param x colonne jouee
	 * @return le nombre de mines
	 */
	private int nombreMinesAdjacentes(int y, int x) {

		int nb = 0;
		for (int l = y - 1; l <= y + 1; l++) {
			for (int c = x - 1; c <= x + 1; c++) {
				if (this.inDemineur(l, c) && this.tableauMines[l][c]) {
					nb++;
				}
			}
		}

		return nb;
	}

	/**
	 * Permet de savoir si on est sorti de la table
	 * @param l ligne jouee
	 * @param c colonne jouee
	 * @return vrai si on est encore dans la table
	 */
	private boolean inDemineur(int l, int c) {

		return (l >= 0 && c >= 0 && l < this.taille && c < this.taille);
	}
	
	/**
	 * Test de la table pour voir si tout est decouvert
	 * 
	 * @return vrai si le joueur a gagne
	 */
	public boolean gagne() {
		
		boolean gagne=true;
		int i=0;
		while (i < this.taille && gagne) {
			int j=0;
			while (j < this.taille && gagne) {
				gagne=this.tableauVisible[i][j]!=Demineur.CACHE && 
						(this.tableauVisible[i][j]!=Demineur.DRAPEAU || this.tableauMines[i][j]);
				j++;
			}
			i++;
		}
		
		return gagne;
	}

	/**
	 * La case a-t-elle deja ete jouee ?
	 * @param l ligne jouee
	 * @param c colonne jouee
	 * @return vrai si la case n'est plus jouable
	 */
	public boolean estDecouvert(int l, int c) {
		
		return this.tableauVisible[l][c]!=Demineur.CACHE &&
				this.tableauVisible[l][c]!=Demineur.DRAPEAU;
	}
	
	/**
	 * La case contient-elle un drapeau ?
	 * @param l ligne jouee
	 * @param c colonne jouee
	 * @return vrai s'il y a un drapeau sur la case
	 */
	public boolean estDrapeau(int l, int c) {
		
		return this.tableauVisible[l][c]==Demineur.DRAPEAU;
	}
		
	public String toString() {

		String s = "demineur taille " + this.taille + "\n\n";
		s += tableBombesToString();

		return s;
	}
	
	private String tableBombesToString() {

		String s = "";
		for (int l = 0; l < this.taille; l++) {
			for (int c = 0; c < this.taille; c++) {
				if (this.tableauMines[l][c]) {
					s += "b";
				} else {
					s += " ";
				}
				s += "\n";
			}
		}
		return s;
	}
}
