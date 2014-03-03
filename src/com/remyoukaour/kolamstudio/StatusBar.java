package com.remyoukaour.kolamstudio;

import java.awt.*;

import javax.swing.*;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = 3594146783477354882L;
	
	private final JLabel status = new JLabel();

	public StatusBar() {
		this("");
	}
	
	public StatusBar(String text) {
		this(text, 0);
	}
	
	public StatusBar(int height) {
		this("", height);
	}

	public StatusBar(String text, int height) {
		setLayout(new BorderLayout());
		add(status, BorderLayout.CENTER);
		if (height > 0) {
			setPreferredSize(new Dimension(0, height));
		}
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		status.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		setText(text);
	}

	public String getText() {
		return status.getText();
	}

	public void setText(String text) {
		status.setText(text);
	}
}
