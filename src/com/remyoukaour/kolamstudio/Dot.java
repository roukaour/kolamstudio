package com.remyoukaour.kolamstudio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Dot {
	private int x, y;
	private ArrayList<Dot> neighbors = new ArrayList<Dot>();
	private int alternation = 1;
	
	public Dot(int x, int y) {
		setLocation(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getAlternation() {
		return alternation;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		resort();
		for (Dot neighbor : neighbors) {
			neighbor.resort();
		}
	}
	
	public void setAlternation(int alternation) {
		if (alternation > 0 && alternation <= countNeighbors() / 2) {
			this.alternation = alternation;
		}
	}
	
	public ArrayList<Dot> getNeighbors() {
		return neighbors;
	}
	
	public int countNeighbors() {
		return neighbors.size();
	}
	
	public boolean isConnected(Dot dot) {
		return neighbors.contains(dot);
	}
	
	public boolean connect(Dot dot) {
		if (isConnected(dot)) {
			return false;
		}
		neighbors.add(dot);
		dot.neighbors.add(this);
		resort();
		dot.resort();
		return true;
	}
	
	public boolean disconnect(Dot dot) {
		return disconnectOneWay(dot) && dot.disconnectOneWay(this);
	}
	
	private boolean disconnectOneWay(Dot dot) {
		if (!neighbors.remove(dot)) {
			return false;
		}
		if (alternation > countNeighbors() / 2) {
			alternation = 1;
		}
		return true;
	}
	
	public void disconnectAll() {
		ArrayList<Dot> neighbors2 = new ArrayList<Dot>(neighbors);
		for (Dot neighbor : neighbors2) {
			disconnect(neighbor);
		}
	}
	
	public double angle(Dot dot) {
		// Find the positive angle with the +x axis
		double a = Math.atan2(y - dot.y, dot.x - x);
		if (a < 0) {
			a += 2 * Math.PI;
		}
		return a;
	}
	
	public int distance2(Dot dot) {
		return distance2(dot.x, dot.y);
	}
	
	public int distance2(int x, int y) {
		int dx = this.x - x;
		int dy = this.y - y;
		return dx * dx + dy * dy;
	}
	
	public void resort() {
		// Sort neighbors counterclockwise from the +x axis
		Collections.sort(neighbors, new Comparator<Dot>() {
			public int compare(Dot a, Dot b) {
				double aa = angle(a);
				double ab = angle(b);
				return aa > ab ? 1 : aa < ab ? -1 : 0;
			}	
		});
	}
}
