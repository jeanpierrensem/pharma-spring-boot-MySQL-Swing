package com.officine.losto.shared.annimation.splash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.officine.losto.params.constant.ConstMessagesEN;

public class JDWaiting extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JLabel lblGnerationDuRapport;


	public static void main(String[] args) {
		try {
			
			JDWaiting dialog = new JDWaiting();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setLocationRelativeTo(null);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public JDWaiting() {
		getContentPane().setBackground(Color.BLUE);
		setBounds(100, 100, 344, 254);
		setDefaultLookAndFeelDecorated(true);
		this.setUndecorated(true);
		
		this.setAlwaysOnTop(true);
		
		System.out.println("ici "+this.isUndecorated());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.BLUE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel labe = new JLabel("");
			labe.setBounds(0, 26, 344, 228);
			labe.setBackground(Color.GREEN);
			labe.setIcon(new ImageIcon(JDWaiting.class.getResource(ConstMessagesEN.Params.BASE_PATH+"/images/chargement.gif")));
			labe.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(labe);
		}
		{
			lblGnerationDuRapport = new JLabel("Patientez SVP");
			lblGnerationDuRapport.setBackground(Color.BLUE);
			lblGnerationDuRapport.setBounds(0, 11, 344, 17);
			lblGnerationDuRapport.setVerticalAlignment(SwingConstants.TOP);
			lblGnerationDuRapport.setForeground(Color.YELLOW);
			lblGnerationDuRapport.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblGnerationDuRapport.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblGnerationDuRapport);
		}
	}
}
