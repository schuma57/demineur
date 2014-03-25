package demineur.highscores;

/**
 * Created by schuma on 18/03/14.
 */

import demineur.Partie;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    private final int TAILLE_MIN = 5;
    private final int TAILLE_MAX = 15;
    private final int NB_SCORE = 10;
    private ArrayList<Partie> parties;
    private static final File fich = new File("score.txt");

    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        parties = new ArrayList<Partie>();
    }

    public ArrayList<Partie> getParties() {
        loadScoreFile();
        sort();
        return parties;
    }

    private void sort() {
        ScoreComparator comparator = new ScoreComparator();
        Collections.sort(parties, comparator);
    }

    public void addScore(Partie p) {
        loadScoreFile();
        parties.add(p);
        updateScoreFile();
    }

    public void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(fich));
            parties = (ArrayList<Partie>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Laad] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Laad] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
        }
    }

    public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(fich));
            outputStream.writeObject(parties);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    private ArrayList<Partie>[] trierParties(){
        ArrayList<Partie> par[] = new ArrayList[TAILLE_MAX - TAILLE_MIN +1 ];

        for(int y = 0 ; y <= (TAILLE_MAX - TAILLE_MIN); y++){
            par[y] = new ArrayList<Partie>();
        }

        for(int y = 0 ; y < getParties().size() ; y++)
        {
            //on repartit les scores suivant leur taille.
            switch (getParties().get(y).getTaille()){
                case 5:
                    par[0].add(getParties().get(y)); break;
                case 6:
                    par[1].add(getParties().get(y)); break;
                case 7:
                    par[2].add(getParties().get(y)); break;
                case 8:
                    par[3].add(getParties().get(y)); break;
                case 9:
                    par[4].add(getParties().get(y)); break;
                case 10:
                    par[5].add(getParties().get(y)); break;
                case 11:
                    par[6].add(getParties().get(y)); break;
                case 12:
                    par[7].add(getParties().get(y)); break;
                case 13:
                    par[8].add(getParties().get(y)); break;
                case 14:
                    par[9].add(getParties().get(y)); break;
                case 15:
                    par[10].add(getParties().get(y)); break;
            }
        }

        return par;
    }


    public String getHighscoreString() {
        String highscoreString = "";
        int max = NB_SCORE;

        ArrayList<Partie> par[] = trierParties();

        highscoreString += "Rang\tNom\tTaille\tTemps\n";

        //on affiche les scores par taille croissante
        for(int y = 0 ; y <= (TAILLE_MAX - TAILLE_MIN) ; y++){

            int i = 0;
            int x = par[y].size();
            if (x > max) {
                x = max;
            }

            while (i < x) {
                highscoreString += (i + 1) +".\t" + par[y].get(i).toString();
                i++;
            }
            highscoreString +="\n";
        }

        return highscoreString;
    }

    //TODO, fonctionne
    public void ajoutBDD(Partie p){
        try {
            Connection co = ConnexionBDD.getInstance();
            java.sql.PreparedStatement stat = co.prepareStatement
                    ("INSERT INTO score (id,nom_joueur, taille, temps) VALUES(NULL, ? , ?, ?)");
            stat.setString(1, p.getNomJoueur());
            stat.setInt(2, p.getTaille());
            stat.setInt(3, p.getSecondes());
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO, fonctionne
    public void lireBDD(){
        String s = "";
        try{
            Connection conn = ConnexionBDD.getInstance();
            ResultSet res = null;
            java.sql.Statement requete = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            res = requete.executeQuery("SELECT * FROM score");

            int i = 1;
            while(res.next()){
                s += res.getString("nom_joueur") +"\t\t" +res.getString("taille") +"\t\t" +res.getString("temps") +"\n";
                i++;
            }
        }
        catch (SQLException sqle) {
            System.out.println("Probleme dans SELECT " + sqle.getMessage());
        }

        System.out.println(s);
    }
}
