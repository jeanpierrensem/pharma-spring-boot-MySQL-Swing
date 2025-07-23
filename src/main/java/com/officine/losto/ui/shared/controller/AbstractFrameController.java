package com.officine.losto.ui.shared.controller;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;

import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.ui.forms.RayonFrame;
import com.officine.losto.ui.forms.TypeFrame;

public abstract class AbstractFrameController {

	public abstract  void  prepareAndOpenFrame(JDialog parent);
	
	
	protected void registerAction(JButton button, ActionListener listener) {
		button.addActionListener(listener);
	}

	protected void registerAction(JMenuItem mnItem, ActionListener listener) {
		mnItem.addActionListener(listener);
	}

	
	protected void registerAction(JComboBox<String> CB,  ItemListener listener) {
		CB.addItemListener(listener); 
	}


	


	

	
	

	
	
	
	
	
}