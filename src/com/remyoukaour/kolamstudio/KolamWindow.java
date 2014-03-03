package com.remyoukaour.kolamstudio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class KolamWindow extends JFrame implements ActionListener, ItemListener, MouseListener {
	private static final long serialVersionUID = 6188512225530851037L;
	
	private static enum Mode {
		ADD, REMOVE, MOVE, MOVING, CONNECT, CONNECTING,
		DISCONNECT, DISCONNECTING, ALTERNATION
	}
	
	private static final String UNTITLED = "Untitled";
	
	private final String name;
	private Mode mode;
	
	private final JMenuBar menuBar = new JMenuBar();
	// TODO: make status bar useful
	private final StatusBar statusBar = new StatusBar(" ");
	private final KolamPanel kolam = new KolamPanel();
	private final JScrollPane kolamScroll = new JScrollPane(kolam);
	
	private final JMenu fileMenu = new JMenu("File");
	private final JMenuItem newKolam = new JMenuItem("New kolam", KeyEvent.VK_N);
	private final JMenuItem openKolam = new JMenuItem("Open kolam...", KeyEvent.VK_O);
	private final JMenuItem saveKolam = new JMenuItem("Save kolam...", KeyEvent.VK_S);
	private final JMenuItem exportImage = new JMenuItem("Export image...", KeyEvent.VK_E);
	private final JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
	
	private final JMenu editMenu = new JMenu("Edit");
	private final JMenuItem resize = new JMenuItem("Resize", KeyEvent.VK_R);
	private final JMenuItem clear = new JMenuItem("Clear", KeyEvent.VK_C);
	private final JMenuItem clearUnused = new JMenuItem("Clear unused dots", KeyEvent.VK_U);
	private final JMenuItem grid = new JMenuItem("Generate grid...", KeyEvent.VK_G);
	
	private final JMenu viewMenu = new JMenu("View");
	private final JCheckBoxMenuItem dots = new JCheckBoxMenuItem("Dots");
	private final JCheckBoxMenuItem connections = new JCheckBoxMenuItem("Connections");
	private final JCheckBoxMenuItem curves = new JCheckBoxMenuItem("Curves");
	private final JCheckBoxMenuItem invertBackground = new JCheckBoxMenuItem("Invert background");
	
	private final JMenu helpMenu = new JMenu("Help");
	private final JMenuItem help = new JMenuItem("Help", KeyEvent.VK_H);
	private final JMenuItem about = new JMenuItem("About", KeyEvent.VK_A);
	
	private final SideBar sideBar = new SideBar(SideBar.LEFT);
	private final ButtonGroup sideButtons = new ButtonGroup();
	// TODO: select, move, rotate, copy, cut, paste, and delete dot groups
	private final JToggleButton add = new JToggleButton();
	private final JToggleButton remove = new JToggleButton();
	private final JToggleButton move = new JToggleButton();
	private final JToggleButton connect = new JToggleButton();
	private final JToggleButton disconnect = new JToggleButton();
	private final JToggleButton alternate = new JToggleButton();
	
	private final JFileChooser fileChooser = new JFileChooser();
	private final FileExtensionFilter kolamFilter =
		new FileExtensionFilter("Kolam files", "kolam");
	private final FileExtensionFilter imageFilter =
		new FileExtensionFilter("Image files", "png");
	
	private final Resizer resizer = new Resizer();
	private final GridGenerator gridGenerator = new GridGenerator();
	
	public KolamWindow(String title) {
		super(UNTITLED + " - " + title);
		this.name = title;
		// components
		setupMenuBar();
		setupSideBar();
		setupLayout();
		// configuration
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		resizer.setDialogTitle("Resize kolam");
		gridGenerator.setSelectedSpacing(48);
		gridGenerator.setDialogTitle("Generate grid");
	}
	
	private void setupMenuBar() {
		// mnemonics
		fileMenu.setMnemonic(KeyEvent.VK_F);
		editMenu.setMnemonic(KeyEvent.VK_E);
		viewMenu.setMnemonic(KeyEvent.VK_V);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		dots.setMnemonic(KeyEvent.VK_D);
		connections.setMnemonic(KeyEvent.VK_C);
		curves.setMnemonic(KeyEvent.VK_V);
		// accelerators
		newKolam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		openKolam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveKolam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		exportImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		resize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		clearUnused.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		grid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		dots.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		connections.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		curves.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		invertBackground.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		// structure
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);
		fileMenu.add(newKolam);
		fileMenu.add(openKolam);
		fileMenu.add(saveKolam);
		fileMenu.add(exportImage);
		fileMenu.add(exit);
		editMenu.add(resize);
		editMenu.add(clear);
		editMenu.add(clearUnused);
		editMenu.addSeparator();
		editMenu.add(grid);
		viewMenu.add(dots);
		viewMenu.add(connections);
		viewMenu.add(curves);
		viewMenu.addSeparator();
		viewMenu.add(invertBackground);
		helpMenu.add(help);
		helpMenu.add(about);
		// listeners
		newKolam.addActionListener(this);
		openKolam.addActionListener(this);
		saveKolam.addActionListener(this);
		exportImage.addActionListener(this);
		exit.addActionListener(this);
		resize.addActionListener(this);
		clear.addActionListener(this);
		clearUnused.addActionListener(this);
		grid.addActionListener(this);
		dots.addItemListener(this);
		connections.addItemListener(this);
		curves.addItemListener(this);
		invertBackground.addItemListener(this);
		help.addActionListener(this);
		about.addActionListener(this);
		// configuration
		dots.setSelected(true);
		connections.setSelected(true);
		curves.setSelected(true);
	}
	
	private void setupSideBar() {
		// structure
		sideBar.add(add);
		sideBar.add(remove);
		sideBar.add(move);
		sideBar.add(connect);
		sideBar.add(disconnect);
		sideBar.add(alternate);
		// listeners
		add.addActionListener(this);
		remove.addActionListener(this);
		move.addActionListener(this);
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		alternate.addActionListener(this);
		// configuration
		sideButtons.add(add);
		sideButtons.add(remove);
		sideButtons.add(move);
		sideButtons.add(connect);
		sideButtons.add(disconnect);
		sideButtons.add(alternate);
		add.setFocusPainted(false);
		remove.setFocusPainted(false);
		move.setFocusPainted(false);
		connect.setFocusPainted(false);
		disconnect.setFocusPainted(false);
		alternate.setFocusPainted(false);
		Insets margin = new Insets(1, 1, 1, 1);
		add.setMargin(margin);
		remove.setMargin(margin);
		move.setMargin(margin);
		connect.setMargin(margin);
		disconnect.setMargin(margin);
		alternate.setMargin(margin);
		add.setIcon(getIcon("/res/add.png"));
		remove.setIcon(getIcon("/res/remove.png"));
		move.setIcon(getIcon("/res/move.png"));
		connect.setIcon(getIcon("/res/connect.png"));
		disconnect.setIcon(getIcon("/res/disconnect.png"));
		alternate.setIcon(getIcon("/res/alternate.png"));
		add.setToolTipText("Add");
		remove.setToolTipText("Remove");
		move.setToolTipText("Move");
		connect.setToolTipText("Connect");
		disconnect.setToolTipText("Disconnect");
		alternate.setToolTipText("Alternation");
		add.setSelected(true);
		mode = Mode.ADD;
	}
	
	private void setupLayout() {
		// structure
		setLayout(new BorderLayout());
		add(sideBar, BorderLayout.WEST);
		add(statusBar, BorderLayout.SOUTH);
		add(kolamScroll, BorderLayout.CENTER);
		// listeners
		kolam.addMouseListener(this);
		// configuration
		kolamScroll.getViewport().setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		kolamScroll.getViewport().setBackground(Color.GRAY);
		kolamScroll.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == newKolam)
			action_new();
		else if (source == openKolam)
			action_open();
		else if (source == saveKolam)
			action_save();
		else if (source == exportImage)
			action_export();
		else if (source == exit)
			action_exit();
		else if (source == resize)
			action_resize();
		else if (source == clear)
			action_clear();
		else if (source == clearUnused)
			action_clearUnused();
		else if (source == grid)
			action_grid();
		else if (source == help)
			action_help();
		else if (source == about)
			action_about();
		else if (source == add)
			action_add();
		else if (source == remove)
			action_remove();
		else if (source == move)
			action_move();
		else if (source == connect)
			action_connect();
		else if (source == disconnect)
			action_disconnect();
		else if (source == alternate)
			action_alternate();
	}
	
	public void itemStateChanged(ItemEvent event) {
		Object item = event.getItem();
		if (item == dots)
			toggle_dots();
		else if (item == connections)
			toggle_connections();
		else if (item == curves)
			toggle_curves();
		else if (item == invertBackground) {
			toggle_invertBackground();
		}
	}
	
	public void mouseClicked(MouseEvent event) {
		Object source = event.getSource();
		if (source != kolam || event.getButton() != MouseEvent.BUTTON1)
			return;
		int x = event.getX();
		int y = event.getY();
		Dot chosen;
		switch (mode) {
		case ADD:
			kolam.addDot(x, y);
			break;
		case REMOVE:
			chosen = kolam.getDot(x, y);
			if (chosen != null) {
				kolam.removeDot(chosen);
			}
			break;
		case MOVE:
			chosen = kolam.getDot(x, y);
			if (chosen != null) {
				kolam.selectDot(chosen);
				mode = Mode.MOVING;
			}
			break;
		case MOVING:
			kolam.moveSelected(x, y);
			kolam.deselect();
			mode = Mode.MOVE;
			break;
		case CONNECT:
			chosen = kolam.getDot(x, y);
			if (chosen != null) {
				kolam.selectDot(chosen);
				mode = Mode.CONNECTING;
			}
			break;
		case CONNECTING:
			kolam.connectSelected(x, y);
			kolam.deselect();
			mode = Mode.CONNECT;
			break;
		case DISCONNECT:
			chosen = kolam.getDot(x, y);
			if (chosen != null && chosen.countNeighbors() > 0) {
				kolam.selectDot(chosen);
				mode = Mode.DISCONNECTING;
			}
			break;
		case DISCONNECTING:
			kolam.disconnectSelected(x, y);
			kolam.deselect();
			mode = Mode.DISCONNECT;
			break;
		case ALTERNATION:
			chosen = kolam.getDot(x, y);
			int count = chosen.countNeighbors();
			if (chosen != null && count > 0) {
				int alternation = chosen.getAlternation() + 1;
				if (alternation > count / 2) {
					alternation = 1;
				}
				chosen.setAlternation(alternation);
				kolam.repaint();
			}
			break;
		}
	}

	public void mouseEntered(MouseEvent event) {}

	public void mouseExited(MouseEvent event) {}

	public void mousePressed(MouseEvent event) {}

	public void mouseReleased(MouseEvent event) {}
	
	@Override
	public void setVisible(boolean visible) {
		sideBar.setPreferredSize(new Dimension(sideBar.getWidth(), 0));
		statusBar.setPreferredSize(new Dimension(0, statusBar.getHeight()));
		super.setVisible(visible);
	}
	
	private ImageIcon getIcon(String path) {
	    URL url = getClass().getResource(path);
	    if (url != null)
	        return new ImageIcon(url);
	    return null;
	}
	
	private void action_new() {
		kolam.clear();
		kolam.resetSize();
		kolam.revalidate();
		setTitle(UNTITLED + " - " + name);
	}
	
	private void action_open() {
		fileChooser.setDialogTitle("Open kolam");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileFilter(kolamFilter);
		if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			kolam.clear();
			File file = fileChooser.getSelectedFile();
			String filename = file.getAbsolutePath();
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String[] data = reader.readLine().split(" ");
			reader.close();
			kolam.loadData(data);
			setTitle(file.getName() + " - " + name);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Could not open file!",
					"Error", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void action_save() {
		fileChooser.setDialogTitle("Save kolam");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setFileFilter(kolamFilter);
		if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			File file = fileChooser.getSelectedFile();
			String filename = file.getAbsolutePath();
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(String.valueOf(kolam));
			writer.flush();
			writer.close();
			setTitle(file.getName() + " - " + name);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Could not save file!",
					"Error", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void action_export() {
		fileChooser.setDialogTitle("Export image");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setFileFilter(imageFilter);
		if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			File file = fileChooser.getSelectedFile();
			ImageIO.write(kolam.getImage(), "png", file);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Could not save file!",
					"Error", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void action_exit() {
		kolam.clear();
		setVisible(false);
		dispose();
	}
	
	private void action_resize() {
		resizer.setSelectedWidth(kolam.getWidth());
		resizer.setSelectedHeight(kolam.getHeight());
		if (resizer.showResizeDialog(this) != JOptionPane.OK_OPTION) {
			return;
		}
		kolam.setPreferredSize(resizer.getSelectedDimension());
		kolam.revalidate();
		// TODO: resizing resets scroll position but not scroll bars
	}
	
	private void action_clear() {
		kolam.clear();
	}
	
	private void action_clearUnused() {
		kolam.clearUnused();
	}
	
	private void action_grid() {
		if (gridGenerator.showGridDialog(this) != JOptionPane.OK_OPTION) {
			return;
		}
		Object pattern = gridGenerator.getSelectedPattern();
		int d = gridGenerator.getSelectedSpacing();
		// Square grid
		if (pattern == GridGenerator.SQUARE_PATTERN) {
			kolam.clear();
			int width = kolam.getWidth();
			int height = kolam.getHeight();
			for (int y = d / 2; y <= height - d / 2; y += d) {
				for (int x = d / 2; x <= width - d / 2; x += d) {
					kolam.addDot(x, y);
				}
			}
		}
		// Triangular grid
		else if (pattern == GridGenerator.TRIANGLE_PATTERN) {
			kolam.clear();
			int width = kolam.getWidth();
			int height = kolam.getHeight();
			int dx = d;
			int dy = (int)(dx * Math.sqrt(3) / 2);
			boolean shift = false;
			for (int y = dx / 2; y <= height - dx / 2; y += dy) {
				for (int x = shift ? dx : dx / 2; x <= width - dx / 2; x += dx) {
					kolam.addDot(x, y);
				}
				shift = !shift;
			}
		}
	}
	
	private void toggle_dots() {
		kolam.showDots(dots.isSelected());
	}
	
	private void toggle_connections() {
		kolam.showConnections(connections.isSelected());
	}
	
	private void toggle_curves() {
		kolam.showCurves(curves.isSelected());
	}
	
	private void toggle_invertBackground() {
		kolam.invertBackground(invertBackground.isSelected());
	}
	
	private void action_help() {
		String message = "<html><body>" +
				"Help is not yet available.<br>" +
				"We apologize for the inconvenience." +
				"</body></html>";
		JOptionPane.showMessageDialog(this, message, "Help",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private void action_about() {
		String message = "<html><body>" +
				"<b>" + name + "</b><br><br>" +
				"Copyright &copy; 2012 Remy Oukaour." +
				"</body></html>";
		JOptionPane.showMessageDialog(this, message, "About",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private void action_add() {
		mode = Mode.ADD;
		kolam.deselect();
	}
	
	private void action_remove() {
		mode = Mode.REMOVE;
		kolam.deselect();
	}
	
	private void action_move() {
		mode = Mode.MOVE;
		kolam.deselect();
	}
	
	private void action_connect() {
		mode = Mode.CONNECT;
		kolam.deselect();
	}
	
	private void action_disconnect() {
		mode = Mode.DISCONNECT;
		kolam.deselect();
	}
	
	private void action_alternate() {
		mode = Mode.ALTERNATION;
		kolam.deselect();
	}
}
