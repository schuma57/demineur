package demineur.highscores;

/**
 * Created by schuma on 18/03/14.
 */

import java.util.Comparator;
import demineur.Partie;

public class ScoreComparator implements Comparator<Partie> {
    public int compare(Partie partie1, Partie partie2) {

        int sp1 = partie1.getSecondes();
        int sp2 = partie2.getSecondes();

        if (sp1 < sp2){
            return -1;
        }else if (sp1 > sp2){
            return +1;
        }else{
            return 0;
        }
    }
}
