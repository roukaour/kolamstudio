package com.remyoukaour.kolamstudio;

import javax.swing.*;
import javax.swing.event.*;

public class RequestFocusListener implements AncestorListener {
	public RequestFocusListener() {}
	
	public void ancestorAdded(AncestorEvent event) {
		JComponent component = event.getComponent();
		component.requestFocusInWindow();
	}

	public void ancestorMoved(AncestorEvent event) {}

	public void ancestorRemoved(AncestorEvent event) {}
}
