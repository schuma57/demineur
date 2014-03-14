package demineur.vue.swing;

import javax.swing.JButton;

public class CaseDemineur extends JButton {
	private int l;
	private int c;

	public CaseDemineur(int i, int j) {
		super();
		setL(i);
		setC(j);
	}

	public CaseDemineur() {
		super();
	}

	public CaseDemineur(String s) {
		super(s);
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
}
