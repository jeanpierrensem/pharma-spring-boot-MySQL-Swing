package com.officine.losto.uti.shared;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

public class ConfirmDialog {
	
	private final Object[] options = { "Oui", "Non" };
	private final Object[] optionsInfo = { "Ok"};
	
	
  /* public int showConfirmDialog(JInternalFrame jif, String message) {
	   int choice = JOptionPane.showOptionDialog(
	            jif, // Parent component (null means center on screen)
	            message, // Message to display
	            "", // Dialog title
	            JOptionPane.YES_NO_OPTION, // Option type (Yes, No, Cancel)
	            JOptionPane.ERROR_MESSAGE, // Message type (question icon)
	            null, // Custom icon (null means no custom icon)
	            options, // Custom options array
	            options[1] // Initial selection (default is "Cancel")
	        );
	   return choice; 
   }*/
   
   public int showConfirm(JDialog jif, String message) {
	 
	   int choice = JOptionPane.showOptionDialog(
	            jif, // Parent component (null means center on screen)
	            message, // Message to display
	            "", // Dialog title
	            JOptionPane.YES_NO_OPTION , // Option type (Yes, No, Cancel)	         
	            JOptionPane.ERROR_MESSAGE, // Message type (question icon)
	            null, // Custom icon (null means no custom icon)
	            options, // Custom options array
	            options[1] // Initial selection (default is "Cancel")
	        );
	   return choice; 
   }
   
   public int  showInfo(JDialog jif, String message) {
		 
	   int choice = JOptionPane.showOptionDialog(
	            jif, // Parent component (null means center on screen)
	            message, // Message to display
	            "Information", // Dialog title
	            JOptionPane.OK_OPTION, // Option type (Yes, No, Cancel)
	            JOptionPane.INFORMATION_MESSAGE, // Message type (question icon)
	            null, // Custom icon (null means no custom icon)
	            optionsInfo, // Custom options array
	            optionsInfo[0] // Initial selection (default is "Cancel")
	        );
	   return choice; 
	   
   }
}
