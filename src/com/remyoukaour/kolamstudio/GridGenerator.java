package com.remyoukaour.kolamstudio;

import java.awt.*;
import javax.swing.*;

public class GridGenerator {
	public static final String SQUARE_PATTERN = "Square";
	public static final String TRIANGLE_PATTERN = "Triangular";
	
	private String dialogTitle = "Generate grid";
	
	private final JPanel container = new JPanel();
	private final JLabel patternLabel = new JLabel("Pattern:", SwingConstants.RIGHT);
	private final JComboBox<Object> pattern = new JComboBox<Object>(new Object[] {
			SQUARE_PATTERN, TRIANGLE_PATTERN
		});
	private final JLabel spacingLabel = new JLabel("Spacing:", SwingConstants.RIGHT);
	private final SpinnerNumberModel spacingModel = new SpinnerNumberModel(1, 1, null, 1);
	private final JSpinner spacing = new JSpinner(spacingModel);
	
	public GridGenerator() {
		// TODO: polish layout
		container.setLayout(new BorderLayout(5, 5));
		JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
		labels.add(patternLabel);
		labels.add(spacingLabel);
		container.add(labels, BorderLayout.WEST);
		JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
		controls.add(pattern);
		controls.add(spacing);
		container.add(controls, BorderLayout.CENTER);
		pattern.addAncestorListener(new RequestFocusListener());
	}
	
	public Object getSelectedPattern() {
		return pattern.getSelectedItem();
	}
	
	public int getSelectedSpacing() {
		return spacingModel.getNumber().intValue();
	}
	
	public String getDialogTitle() {
		return dialogTitle;
	}
	
	public void setSelectedSpacing(int spacing) {
		spacingModel.setValue(spacing);
	}
	
	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}
	
	public void setFormat(String format) {
		spacing.setEditor(new JSpinner.NumberEditor(spacing, format));
	}
	
	public int showGridDialog(Component parent) {
		return JOptionPane.showOptionDialog(parent, container, dialogTitle,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, null, null);
	}
}
