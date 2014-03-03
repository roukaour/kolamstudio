package com.remyoukaour.kolamstudio;

import java.awt.*;
import javax.swing.*;

public class Resizer {	
	private String dialogTitle = "Resize";
	
	private final JPanel container = new JPanel(new BorderLayout(5, 5));
	private final JLabel widthLabel = new JLabel("Width:", SwingConstants.RIGHT);
	private final SpinnerNumberModel widthModel = new SpinnerNumberModel(0, 0, null, 1);
	private final JSpinner width = new JSpinner(widthModel);
	private final JLabel heightLabel = new JLabel("Height:", SwingConstants.RIGHT);
	private final SpinnerNumberModel heightModel = new SpinnerNumberModel(0, 0, null, 1);
	private final JSpinner height = new JSpinner(heightModel);
	
	public Resizer() {
		// TODO: polish layout
		// http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/spinner.html
		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		labels.add(widthLabel);
		labels.add(heightLabel);
		container.add(labels, BorderLayout.WEST);
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
		controls.add(width);
		controls.add(height);
		container.add(controls, BorderLayout.CENTER);
		width.addAncestorListener(new RequestFocusListener());
	}
	
	public int getSelectedWidth() {
		return widthModel.getNumber().intValue();
	}
	
	public int getSelectedHeight() {
		return heightModel.getNumber().intValue();
	}
	
	public Dimension getSelectedDimension() {
		return new Dimension(getSelectedWidth(), getSelectedHeight());
	}
	
	public String getDialogTitle() {
		return dialogTitle;
	}
	
	public void setSelectedWidth(int width) {
		widthModel.setValue(width);
	}
	
	public void setSelectedHeight(int height) {
		heightModel.setValue(height);
	}
	
	public void setSelectedDimension(Dimension dimension) {
		widthModel.setValue(dimension.getWidth());
		heightModel.setValue(dimension.getHeight());
	}
	
	public void allowZeroSize(boolean zero) {
		widthModel.setMinimum(zero ? 0 : 1);
		heightModel.setMinimum(zero ? 0 : 1);
	}
	
	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}
	
	public void setFormat(String format) {
		width.setEditor(new JSpinner.NumberEditor(width, format));
		height.setEditor(new JSpinner.NumberEditor(height, format));
	}
	
	public int showResizeDialog(Component parent) {
		return JOptionPane.showOptionDialog(parent, container, dialogTitle,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, null, null);
	}
}
