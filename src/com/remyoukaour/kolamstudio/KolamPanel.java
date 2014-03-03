package com.remyoukaour.kolamstudio;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JPanel;

public class KolamPanel extends JPanel {
	private static final long serialVersionUID = -2150188449129039129L;
	
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 360;
	
	private static final int DOT_DIAMETER = 7;
	private static final int DOT_RADIUS = DOT_DIAMETER / 2 + 1;
	private static final int DOT_RANGE = (DOT_RADIUS + 1) * (DOT_RADIUS + 1) * 2;
	
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final Color BACKGROUND_COLOR_ALT = Color.BLACK;
	private static final Color DOT_BORDER = new Color(0, 0, 160);
	private static final Color DOT_INNER = new Color(64, 64, 255);
	private static final Color DOT_SELECTED_BORDER = new Color(128, 128, 0);
	private static final Color DOT_SELECTED_INNER = Color.YELLOW;
	private static final Color CONNECTION_COLOR = Color.GREEN;
	private static final Color CURVE_COLOR = Color.RED;
	
	private HashSet<Dot> dots = new HashSet<Dot>();
	private Dot selected = null;
	
	private boolean showDots = true;
	private boolean showConnections = true;
	private boolean showCurves = true;
	
	public KolamPanel() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public KolamPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		invertBackground(false);
	}
	
	public void showDots(boolean show) {
		showDots = show;
		repaint();
	}
	
	public void showConnections(boolean show) {
		showConnections = show;
		repaint();
	}
	
	public void showCurves(boolean show) {
		showCurves = show;
		repaint();
	}
	
	public void invertBackground(boolean invert) {
		setBackground(invert ? BACKGROUND_COLOR_ALT : BACKGROUND_COLOR);
		repaint();
	}
	
	public Dot getSelected() {
		return selected;
	}
	
	public void resetSize() {
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
	
	public void clear() {
		dots.clear();
		selected = null;
		repaint();
	}
	
	public void clearUnused() {
		HashSet<Dot> dotsCopy = new HashSet<Dot>(dots);
		for (Dot dot : dotsCopy) {
			if (dot.countNeighbors() == 0) {
				removeDot(dot);
			}
		}
		repaint();
	}
	
	public Dot getDot(int x, int y) {
		for (Dot dot : dots) {
			int d2 = dot.distance2(x, y);
			if (d2 < DOT_RANGE) {
				return dot;
			}
		}
		return null;
	}
	
	public Dot addDot(int x, int y) {
		Dot dot = new Dot(x, y);
		dots.add(dot);
		repaint();
		return dot;
	}
	
	public boolean removeDot(Dot removed) {
		if (dots.contains(removed)) {
			removed.disconnectAll();
			dots.remove(removed);
			repaint();
			return true;
		}
		return false;
	}
	
	public boolean selectDot(Dot selected) {
		if (dots.contains(selected)) {
			this.selected = selected;
			repaint();
			return true;
		}
		return false;
	}
	
	public void deselect() {
		selected = null;
		repaint();
	}
	
	public void moveSelected(int x, int y) {
		if (selected == null) {
			return;
		}
		selected.setLocation(x, y);
		repaint();
	}
	
	public Dot connectSelected(int x, int y) {
		if (selected == null) {
			return null;
		}
		Dot chosen = getDot(x, y);
		if (chosen != null && chosen != selected) {
			selected.connect(chosen);
			repaint();
		}
		return chosen;
	}
	
	public Dot disconnectSelected(int x, int y) {
		if (selected == null) {
			return null;
		}
		Dot chosen = getDot(x, y);
		if (chosen != null && selected.isConnected(chosen)) {
			selected.disconnect(chosen);
			repaint();
		}
		return chosen;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (showConnections) {
			paintConnections(g);
		}
		if (showDots) {
			paintDots(g);
		}
		if (showCurves) {
			paintCurves(g);
		}
	}
	
	private void paintDots(Graphics g) {
		for (Dot dot : dots) {
			int x = dot.getX();
			int y = dot.getY();
			g.setColor(dot == selected ? DOT_SELECTED_INNER : DOT_INNER);
			g.fillArc(x - DOT_RADIUS, y - DOT_RADIUS, DOT_DIAMETER,
					DOT_DIAMETER, 0, 360);
			g.setColor(dot == selected ? DOT_SELECTED_BORDER : DOT_BORDER);
			g.drawArc(x - DOT_RADIUS, y - DOT_RADIUS, DOT_DIAMETER,
					DOT_DIAMETER, 0, 360);
		}
	}
	
	private void paintConnections(Graphics g) {
		g.setColor(CONNECTION_COLOR);
		for (Dot dot : dots) {
			for (Dot neighbor : dot.getNeighbors()) {
				g.drawLine(dot.getX(), dot.getY(), neighbor.getX(), neighbor.getY());
			}
		}
	}
	
	private void paintCurves(Graphics g) {
		for (Dot dot : dots) {
			int x = dot.getX();
			int y = dot.getY();
			int alt = dot.getAlternation();
			ArrayList<Dot> neighbors = dot.getNeighbors();
			int count = dot.countNeighbors();
			// TODO: make the curves curved
			if (count == 1) {
				Dot neighbor = neighbors.get(0);
				int dx = neighbor.getX() - x;
				int dy = neighbor.getY() - y;
				g.setColor(CURVE_COLOR);
				g.drawLine(x + dx / 2, y + dy / 2, x + dy / 4, y - dx / 4);
				g.drawLine(x + dx / 2, y + dy / 2, x - dy / 4, y + dx / 4);
				g.drawLine(x - dx / 3, y - dy / 3, x + dy / 4, y - dx / 4);
				g.drawLine(x - dx / 3, y - dy / 3, x - dy / 4, y + dx / 4);
				continue;
			}
			for (int i = 0; i < count; i++) {
				Dot n1 = neighbors.get(i);
				Dot n2 = neighbors.get((i + alt) % count);
				double n1a = dot.angle(n1);
				double n2a = dot.angle(n2);
				double a = n2a - n1a;
				if (a < 0) {
					a += 2 * Math.PI;
				}
				double pa = (n1a + n2a) / 2;
				if (a > Math.PI || a == Math.PI && n1a > n2a) {
					pa += Math.PI;
				}
				if ((n1a < Math.PI && n2a > Math.PI && a > Math.PI) ||
						(n2a < Math.PI && n1a > Math.PI && a < Math.PI)) {
					pa += Math.PI;
				}
				int n1d2 = dot.distance2(n1);
				int n2d2 = dot.distance2(n2);
				double f = Math.cos(a < Math.PI / 2 ? a / 2 :
						a < Math.PI ? (a + Math.PI) / 6 :
						a < 3 * Math.PI / 2 ? Math.PI / 2 - a / 6 :
						Math.PI - a / 2);
				double pd = Math.sqrt((n1d2 + n2d2) / 2) / 2 * f;
				int dx = (int)(Math.cos(pa) * pd);
				int dy = (int)(-Math.sin(pa) * pd);
				g.setColor(CURVE_COLOR);
				g.drawLine(x + dx, y + dy, (x + n1.getX()) / 2,
						(y + n1.getY()) / 2);
				g.drawLine(x + dx, y + dy, (x + n2.getX()) / 2,
						(y + n2.getY()) / 2);
			}
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("KOLAM ");
		sb.append(getWidth() + " ");
		sb.append(getHeight() + " ");
		sb.append(dots.size() + " ");
		HashMap<Dot, Integer> indices = new HashMap<Dot, Integer>();
		int i = 0;
		for (Dot dot : dots) {
			indices.put(dot, i++);
		}
		for (Dot dot : dots) {
			sb.append(indices.get(dot) + " ");
			sb.append(dot.getX() + " ");
			sb.append(dot.getY() + " ");
			sb.append(dot.countNeighbors() + " ");
			for (Dot neighbor : dot.getNeighbors()) {
				sb.append(indices.get(neighbor) + " ");
			}
		}
		return sb.toString();
	}
	
	public void loadData(String[] data) throws IOException {
		if (!data[0].equals("KOLAM")) {
			throw new IOException();
		}
		int width = Integer.parseInt(data[1]);
		int height = Integer.parseInt(data[2]);
		setPreferredSize(new Dimension(width, height));
		HashSet<Dot> loaded = new HashSet<Dot>();
		try {
			int count = Integer.parseInt(data[3]);
			HashMap<Integer, Dot> lookup = new HashMap<Integer, Dot>();
			for (int i = 0; i < count; i++) {
				Dot dot = new Dot(0, 0);
				loaded.add(dot);
				lookup.put(i, dot);
			}
			int i = 4;
			for (int parsed = 0; parsed < count; parsed++) {
				int index = Integer.parseInt(data[i++]);
				Dot dot = lookup.get(index);
				int x = Integer.parseInt(data[i++]);
				int y = Integer.parseInt(data[i++]);
				dot.setLocation(x, y);
				int neighbors = Integer.parseInt(data[i++]);
				for (int j = 0; j < neighbors; j++) {
					int n = Integer.parseInt(data[i++]);
					Dot neighbor = lookup.get(n);
					dot.connect(neighbor);
				}
			}
		}
		catch (Exception ex) {
			throw new IOException();
		}
		dots.clear();
		dots = loaded;
		selected = null;
		repaint();
		revalidate();
	}
	
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		paintComponent(g);
		g.dispose();
		return image;
	}
}
