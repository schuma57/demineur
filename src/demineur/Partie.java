package demineur;

import java.io.Serializable;

/**
 * Created by schuma on 17/03/14.
 */
public class Partie implements Serializable{
    private String nomJoueur;
    //private int taille;
    private int minutes;
    private int secondes;

    public void setSecondes(int secondes) {
        this.secondes = secondes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getNomJoueur(){
        return nomJoueur;
    }

    public void setNomJoueur(String nom){

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
