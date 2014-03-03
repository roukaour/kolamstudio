package com.remyoukaour.kolamstudio;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class SideBar extends JPanel {
	private static final long serialVersionUID = 340661896985372853L;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	public SideBar(int side) {
		this(side, 0);
	}
	
	public SideBar(int side, int width) {
		if (side != LEFT && side != RIGHT) {
			throw new IllegalArgumentException("Incorrect side: " + side);
		}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		if (width > 0) {
			setPreferredSize(new Dimension(width, 0));
		}
		int left = side == LEFT ? 0 : 1;
		int right = side == RIGHT ? 0 : 1;
		Border border = BorderFactory.createMatteBorder(0, left, 0, right, Color.LIGHT_GRAY);
		Border margin = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		setBorder(new CompoundBorder(border, margin));
	}
}
