package demineur.highscores;

/**
 * Created by schuma on 18/03/14.
 */

import demineur.Partie;
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

    public String getHighscoreString() {
        String highscoreString = "";
        int max = NB_SCORE;

        // pas tres propre a refaire !!!!
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

        } // fin truc à refaire

        return highscoreString;
    }
}
