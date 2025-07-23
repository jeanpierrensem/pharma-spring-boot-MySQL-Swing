package com.officine.losto.test;

import javax.swing.JButton;
import javax.swing.*;


public class DialogFocusExample {
	
	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            JDialog dialog1 = new JDialog((JFrame) null, "Dialog 1");
	            dialog1.setSize(300, 200);
	            dialog1.setLocation(100, 100);
	            dialog1.setModal(false);
	            dialog1.setVisible(true);

	            JButton openDialog2 = new JButton("Ouvrir Dialog 2");
	            dialog1.add(openDialog2);

	            openDialog2.addActionListener(e -> {
	                dialog1.setEnabled(false); // désactive dialog1

	                JDialog dialog2 = new JDialog(dialog1, "Dialog 2", true);
	                dialog2.setSize(300, 150);
	                dialog2.setLocationRelativeTo(dialog1);

	                dialog2.addWindowListener(new java.awt.event.WindowAdapter() {
	                    public void windowClosed(java.awt.event.WindowEvent e) {
	                        dialog1.setEnabled(true); // réactive dialog1
	                        dialog1.toFront(); // remet au premier plan
	                    }
	                });
	                
	                dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	                dialog2.setVisible(true);
	            });
	        });
	    }


}
