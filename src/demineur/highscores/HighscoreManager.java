package demineur.highscores;

/**
 * Created by schuma on 18/03/14.
 */

import demineur.Partie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    private final int TAILLE_MIN = 5;
    private final int TAILLE_MAX = 15;
    private final int NB_SCORE = 10;
    private ArrayList<Partie> parties;
    private ArrayList<Partie> partGlobal;
    private static final File fich = new File("score.txt");

    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        parties = new ArrayList<Partie>();
        partGlobal = new ArrayList<Partie>();
    }

    public ArrayList<Partie> getParties() {
        loadScoreFile();
        sort(parties);
        return parties;
    }

    public ArrayList<Partie> getPartGlobal() {
        lireBDD();
        sort(partGlobal);
        return partGlobal;
    }

    private void sort(ArrayList<Partie> arr) {
        ScoreComparator comparator = new ScoreComparator();
        Collections.sort(arr, comparator);
    }

    public void addScore(Partie p) {
        loadScoreFile();
        parties.add(p);
        updateScoreFile();
        ajoutBDD(p);
    }

    private void loadScoreFile() {
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

    private void updateScoreFile() {
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

    private ArrayList<Partie>[] trierParties( ArrayList<Partie> arr ){
        ArrayList<Partie> par[] = new ArrayList[TAILLE_MAX - TAILLE_MIN +1 ];

        for(int y = 0 ; y <= (TAILLE_MAX - TAILLE_MIN); y++){
            par[y] = new ArrayList<Partie>();
        }

        for(int y = 0 ; y < arr.size() ; y++)
        {
            //on repartit les scores suivant leur taille.
            switch (arr.get(y).getTaille()){
                case 5:
                    par[0].add(arr.get(y)); break;
                case 6:
                    par[1].add(arr.get(y)); break;
                case 7:
                    par[2].add(arr.get(y)); break;
                case 8:
                    par[3].add(arr.get(y)); break;
                case 9:
                    par[4].add(arr.get(y)); break;
                case 10:
                    par[5].add(arr.get(y)); break;
                case 11:
                    par[6].add(arr.get(y)); break;
                case 12:
                    par[7].add(arr.get(y)); break;
                case 13:
                    par[8].add(arr.get(y)); break;
                case 14:
                    par[9].add(arr.get(y)); break;
                case 15:
                    par[10].add(arr.get(y)); break;
            }
        }
        return par; //on retour le tableau d'arraylist trié par taille de demineur
    }


    public String getHighscoreString() {
        String highscoreString = "";
        int max = NB_SCORE;

        ArrayList<Partie> par[] = trierParties(getParties());

        //on affiche les scores par taille croissante
        for(int y = 0 ; y <= (TAILLE_MAX - TAILLE_MIN) ; y++){

            int i = 0;
            int x = par[y].size();
            if (x > max) {
                x = max;
            }

            if(x > 0)
                highscoreString += " Rang\tNom\tTaille\tTemps \n";
            while (i < x) {
                highscoreString += (i + 1) +".\t" + par[y].get(i).toString();
                i++;
            }
            if(x>0)
                highscoreString +="\n\n";
        }

        return highscoreString;
    }

    public String getHighscoreStringGlobal() {
        String highscoreString = "";
        int max = NB_SCORE;

        ArrayList<Partie> par[] = trierParties(getPartGlobal());

        //on affiche les scores par taille croissante
        for(int y = 0 ; y <= (TAILLE_MAX - TAILLE_MIN) ; y++){

            int i = 0;
            int x = par[y].size();
            if (x > max) {
                x = max;
            }

            if(x > 0)
                highscoreString += " Rang\tNom\tTaille\tTemps \n";
            while (i < x) {
                highscoreString += (i + 1) +".\t" + par[y].get(i).toString();
                i++;
            }
            if(x>0)
                highscoreString +="\n\n";
        }

        return highscoreString;
    }

    private void ajoutBDD(Partie p){
        try {
            Connection co = ConnexionBDD.getInstance();
            PreparedStatement stat = co.prepareStatement
                    ("INSERT INTO score (id_score, pseudo, temps, taille) VALUES(NULL, ? , ?, ?)");
            stat.setString(1, p.getNomJoueur());
            stat.setInt(2, p.getSecondes());
            stat.setInt(3, p.getTaille());
            stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void lireBDD(){
        String s = "";
        try{
            Connection conn = ConnexionBDD.getInstance();
            ResultSet res = null;
            java.sql.Statement requete = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            res = requete.executeQuery("SELECT * FROM score");

            int i = 1;
            while(res.next()){
                partGlobal.add(new Partie(res.getString("pseudo"), res.getInt("taille") ,res.getInt("temps") ) );
                i++;
            }
        }
        catch (SQLException sqle) {
            System.out.println("Probleme dans SELECT " + sqle.getMessage());
        }
    }
}
