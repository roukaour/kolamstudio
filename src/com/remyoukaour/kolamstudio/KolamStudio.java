package com.remyoukaour.kolamstudio;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class KolamStudio {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException ex) {}
		catch (InstantiationException ex) {}
		catch (IllegalAccessException ex) {}
		catch (UnsupportedLookAndFeelException ex) {}
		KolamWindow gui = new KolamWindow("Kolam Studio");
		gui.pack();
		gui.setVisible(true);
	}
}
