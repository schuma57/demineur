package demineur;

import java.util.GregorianCalendar;
import java.util.Random;

public class Demineur {
	/**
	 * Taille du d�mineur (carr�)
	 */
	private int taille;

	/**
	 * Le tableau des mines
	 */
	private boolean tableauMines[][];

	/**
	 * Le tableau d'affichage des nombres de mines adjacentes
	 */
	private char tableauVisible[][];

	/**
	 * Constante indiquanr que le joueur a plac� un drapeau "attention bombe"
	 */
	private final static char DRAPEAU='d';
	
	/**
	 * Constante indiquant que la case n'a pas encore �t� cliqu�e
	 */
	private final static char CACHE=' ';
	
	/**
	 * Constructeur par d�faut : d�mineur 10x10
	 */
	public Demineur() {

		this(10);
	}

	/**
	 * Constructeur avec choix de la taille
	 * @param taille la taille du d�mineur
	 */
	public Demineur(int taille) {

		this.setTaille(taille);
		this.initialiseTableaux();
	}

	/**
	 * Initialisation du tableau de mines
	 * et du tableau visible
	 */
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
			throw new IllegalArgumentException("Taille autoris�e : entre 5 et 15");
		} else {
			this.taille = taille;
		}
	}

	public int getTaille() {
		return this.taille;
	}

	public boolean[][] getTableauMines() {
		return this.tableauMines;
	}

	public char[][] getTableauVisible() {
		return this.tableauVisible;
	}

	/**
	 * Switch Drapeau/Case non jou�e 
	 * @param l ligne jou�e
	 * @param c colonne jou�e
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
			throw new RuntimeException("Pose d'un drapeau sur une case non autoris�e !!!");
		}
	}

	/**
	 * Apr�s une s�lection de case, affichage du nombre de bombes adjacentes puis,
	 * si ce nombre est 0, d�couverte de toutes les cases adjacentes qui ont 0 bombes voisines
	 *  
	 * @param y ligne jou�e
	 * @param x colonne jou�e
	 * @return faux si boum !
	 */
	public boolean decouvreTable(int y, int x) {

		// boum s'il y a une bombe
		if (this.tableauMines[y][x]) {
			return false;
		}
		
		int nb = this.nombreMinesAdjacentes(y, x);
		this.tableauVisible[y][x] = String.valueOf(nb).charAt(0);
		
		// Si le nombre de mines autour est 0, on d�ploie le tableau jusqu'� arriver
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
	 * Calcul du nombre de mines adjacentes � la case
	 * @param y ligne jou�e
	 * @param x colonne jou�e
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
	 * @param l ligne jou�e
	 * @param c colonne jou�e
	 * @return vrai si on est encore dans la table
	 */
	private boolean inDemineur(int l, int c) {

		return (l >= 0 && c >= 0 && l < this.taille && c < this.taille);
	}
	
	/**
	 * Test de la table pour voir si tout est d�couvert
	 * 
	 * @return vrai si le joueur a gagn�
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
	 * La case a-t-elle d�j� �t� jou�e ?
	 * @param l ligne jou�e
	 * @param c colonne jou�e
	 * @return vrai si la case n'est plus jouable
	 */
	public boolean estDecouvert(int l, int c) {
		
		return this.tableauVisible[l][c]!=Demineur.CACHE &&
				this.tableauVisible[l][c]!=Demineur.DRAPEAU;
	}
	
	/**
	 * La case contient-elle un drapeau ?
	 * @param l ligne jou�e
	 * @param c colonne jou�e
	 * @return vrai s'il y a un drapeau sur la case
	 */
	public boolean estDrapeau(int l, int c) {
		
		return this.tableauVisible[l][c]==Demineur.DRAPEAU;
	}
		
	public String toString() {

		String s = "d�mineur taille " + this.taille + "\n\n";
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
