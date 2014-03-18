package demineur;

import java.io.Serializable;

/**
 * Created by schuma on 17/03/14.
 */
public class Partie implements Serializable{
    private String nomJoueur;
    private int taille;
    private int secondes;

    public Partie(String s, int ta){
        setTaille(ta);
        setNomJoueur(s);
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

    public int getSecondes() {
        return secondes;
    }

    public void setSecondes(int secondes) {
        this.secondes = secondes;
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

    public String toString(){
        return("Joueur : " +nomJoueur +" , " +secondes + " , taille : " +taille);
    }

}