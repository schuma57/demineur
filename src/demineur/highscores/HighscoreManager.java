package demineur.highscores;

/**
 * Created by schuma on 18/03/14.
 */

import demineur.Partie;
import java.util.*;
import java.io.*;

public class HighscoreManager {
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
        int max = 10;

        ArrayList<Partie> part;
        part = getParties();

        int i = 0;
        int x = part.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ".\t" + part.get(i).getNomJoueur() + "\t\t" + part.get(i).getSecondes() + " sec\n";
            i++;
        }
        return highscoreString;
    }
}
