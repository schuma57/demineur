package demineur;

import java.io.Serializable;

/**
 * Created by schuma on 17/03/14.
 */
public class Partie implements Serializable{
    private String nomJoueur;
    private int taille;
    private int minutes;
    private int secondes;

    public Partie(int ta, String s){
        setTaille(ta);
        nomJoueur = s;
        minutes =0;
        secondes =0;
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

    public void setSecondes(int secondes) {
        this.secondes = secondes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getNomJoueur(){
        return nomJoueur;
    }

    private void setNomJoueur(String nom){

        if (nom.equals("")) {
            throw new IllegalArgumentException("Veuillez saisir un Nom");
        } else {
            if (nom.length() < 3 || nom.length() > 10) {
                throw new IllegalArgumentException("Longueur Nom : entre 3 et 10");
            } else {
                nomJoueur = nom;
            }
        }
    }

}
