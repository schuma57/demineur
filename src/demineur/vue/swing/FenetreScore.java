package demineur.vue.swing;

import javax.swing.*;

/**
 * Created by schuma on 28/03/14.
 */
public class FenetreScore extends JFrame{
    private JTextArea jTexte;

    public FenetreScore(String s){
        super(s);
        JPanel pan = new JPanel();
        jTexte = new JTextArea(30,34);
        jTexte.setEditable(false);
        JScrollPane scroll = new JScrollPane (jTexte);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pan.add(scroll);
        this.add(pan);
        this.pack();
        this.setSize(400, 500);
        this.setVisible(true);
    }

    public JTextArea getJTexte() {
        return jTexte;
    }
}
