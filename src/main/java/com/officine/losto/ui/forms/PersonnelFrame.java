package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.PersonnelTableModel;

import lombok.Getter;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Getter
public class PersonnelFrame extends JDialog {

	private javax.swing.JComboBox<String> cb_groupe;
	private javax.swing.JButton btnAjouter;
	private javax.swing.JButton btnSupprimer;
	private javax.swing.JButton btnQuitter;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private JLabel jLabel2_1;
	private JLabel jLabel2_2;
	private JLabel jLabel2_3;
	private JLabel jLabel2_4;
	private JLabel jLabel2_5;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel TablePanel;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JScrollPane tablejScrollPane;
	private javax.swing.JTable table;
	private javax.swing.JTextField tb_login;
	private javax.swing.JTextField tb_matricule;
	private javax.swing.JTextField tb_nom;
	private javax.swing.JPasswordField tb_mot_passe;
	private javax.swing.JPasswordField tb_mot_passe_confirm;
	private javax.swing.JTextField tb_prenom;
	private javax.swing.JTextField tb_search;
	private JLabel jLabel2_6;
	private JLabel lbl_id;
	private JCheckBox chk_password;

	private JTableHeader tableHeader; 
	
	/*
	 * private int[] dbTable; private int nbLigne; private final String[] entete = {
	 * "Matricule", "Noms et prénoms", "Groupe d'utilisateur", "Login" }; private
	 * Object[][] data; private DefaultTableModel dtPersonnel;
	 */

	public PersonnelFrame(PersonnelTableModel tableModel) {
		initComponents();
		this.getTable().setModel(tableModel);
		
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(ConstMessagesEN.Labels.HEADER_COLOR_ALl);
	}

	private void initComponents() {
		setUndecorated(false);

		setTitle("Gestion des Utilisateurs");

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBackground(Color.WHITE);
		BtnjPanel.setBorder(BorderFactory.createTitledBorder("Saisir Nouvel Utilisateur"));

		btnAjouter = new javax.swing.JButton();
		btnSupprimer = new javax.swing.JButton();
		btnQuitter = new javax.swing.JButton();
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(BorderFactory.createTitledBorder("Liste des Utilisateurs"));
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setBackground(Color.WHITE);
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		tb_matricule = new javax.swing.JTextField();
		tb_matricule.setBackground(SystemColor.window);
		tb_nom = new javax.swing.JTextField();
		tb_nom.setBackground(SystemColor.window);
		tb_prenom = new javax.swing.JTextField();
		tb_prenom.setBackground(SystemColor.window);
		cb_groupe = new javax.swing.JComboBox<String>();
		cb_groupe.setBackground(SystemColor.menu);
		tb_login = new javax.swing.JTextField();
		tb_login.setBackground(SystemColor.window);
		tb_mot_passe = new javax.swing.JPasswordField();
		tb_mot_passe.setBackground(SystemColor.window);
		tb_mot_passe_confirm = new javax.swing.JPasswordField();
		tb_mot_passe_confirm.setBackground(SystemColor.window);
		TablePanel = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();

		tablejScrollPane = new javax.swing.JScrollPane();

		table = new javax.swing.JTable() {

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};
		table.setForeground(SystemColor.inactiveCaptionText);
		table.setFont(new Font("Dialog", Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);

		BtnjPanel.setBorder(null);
		BtnjPanel.setPreferredSize(new java.awt.Dimension(729, 50));

		btnAjouter.setText("Ajouter");
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText("Supprimer");
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText("Quitter");
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
					.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_BtnjPanel.setVerticalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAjouter)
						.addComponent(btnQuitter)
						.addComponent(btnSupprimer))
					.addContainerGap())
		);
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);

		FormJPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		FormJPanel.setPreferredSize(new java.awt.Dimension(350, 311));

		jLabel1.setText("Matricule");

		jLabel2.setText("Nom");

		jLabel3.setText("Prénom");

		jLabel5.setText("Groupe");

		jLabel6.setText("Login");

		jLabel7.setText("Mot de passe");

		jLabel8.setText("Confirmer mot de passe");

		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setText("*");

		jLabel2_2 = new JLabel();
		jLabel2_2.setText("*");
		jLabel2_2.setForeground(Color.RED);

		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);

		jLabel2_4 = new JLabel();
		jLabel2_4.setText("*");
		jLabel2_4.setForeground(Color.RED);

		jLabel2_5 = new JLabel();
		jLabel2_5.setText("*");
		jLabel2_5.setForeground(Color.RED);

		jLabel2_6 = new JLabel();
		jLabel2_6.setText("*");
		jLabel2_6.setForeground(Color.RED);

		lbl_id = new JLabel();
		lbl_id.setBackground(Color.LIGHT_GRAY);
		
		chk_password = new JCheckBox("Afficher mot de passe");
		chk_password.setBackground(SystemColor.window);
		chk_password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chk_password.isSelected() ) {
					chk_password.setText("masquer mot de passe"); 
				  tb_mot_passe.setEchoChar((char)0); 
					return ; 
				}			
				chk_password.setText("afficher mot de passe"); 
				tb_mot_passe.setEchoChar('•'); 
			}
		});
		chk_password.setFont(new Font("Lucida Grande", Font.PLAIN, 11));

		javax.swing.GroupLayout gl_FormJPanel = new javax.swing.GroupLayout(FormJPanel);
		gl_FormJPanel.setHorizontalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(jLabel5)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel2_2, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)
					.addGap(280))
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_6, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(jLabel8)
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(jLabel7)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jLabel2_4, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(jLabel6)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jLabel2_3, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING, false)
									.addGroup(gl_FormJPanel.createSequentialGroup()
										.addComponent(jLabel2)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel2_1, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
									.addComponent(jLabel3)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_5, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_FormJPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(chk_password, GroupLayout.PREFERRED_SIZE, 142, Short.MAX_VALUE)
							.addGap(87))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(tb_matricule, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
								.addComponent(tb_nom, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
								.addComponent(tb_prenom, 149, 149, 149)
								.addComponent(tb_mot_passe, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addComponent(tb_mot_passe_confirm, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
								.addGroup(gl_FormJPanel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(cb_groupe, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(tb_login, Alignment.LEADING, 149, 149, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lbl_id, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
		);
		gl_FormJPanel.setVerticalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(jLabel2_6)
						.addComponent(tb_matricule, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl_id, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel2)
						.addComponent(tb_nom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_1))
					.addGap(18)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel3)
						.addComponent(tb_prenom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(jLabel5)
							.addComponent(jLabel2_2))
						.addComponent(cb_groupe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(jLabel6)
							.addComponent(jLabel2_3))
						.addComponent(tb_login, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(jLabel7)
							.addComponent(jLabel2_4))
						.addComponent(tb_mot_passe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chk_password)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel8)
						.addComponent(tb_mot_passe_confirm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_5))
					.addContainerGap(134, Short.MAX_VALUE))
		);
		gl_FormJPanel.linkSize(SwingConstants.VERTICAL, new Component[] {tb_matricule, tb_nom, tb_prenom, tb_login});
		gl_FormJPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {tb_nom, tb_prenom, tb_login});
		FormJPanel.setLayout(gl_FormJPanel);

		getContentPane().add(FormJPanel, java.awt.BorderLayout.LINE_START);

		TablePanel.setLayout(new java.awt.BorderLayout());

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText("Rechercher(*)");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel9)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(tb_search, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9).addComponent(tb_search, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		TablePanel.add(jPanel4, java.awt.BorderLayout.PAGE_END);
		tablejScrollPane.setViewportView(table);
		TablePanel.add(tablejScrollPane, java.awt.BorderLayout.CENTER);
		getContentPane().add(TablePanel, java.awt.BorderLayout.CENTER);
		pack();
	}

	public AppUser getPersonnelFromForm() {

		AppUser appUser = new AppUser();
		appUser.setMatricule(tb_matricule.getText());
		appUser.setNom(tb_nom.getText());
		appUser.setPrenom(tb_prenom.getText());

		//retrieve_group_in_controller
		appUser.setUsername(tb_login.getText());
		
		if (!lbl_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			appUser.setId(Long.parseLong(lbl_id.getText().trim()));
		return appUser;
	}

	public void clearForm() {
		lbl_id.setText(Strings.EMPTY);
		tb_matricule.setText(Strings.EMPTY);
		tb_nom.setText(Strings.EMPTY);
		tb_prenom.setText(Strings.EMPTY);
		tb_login.setText(Strings.EMPTY);
		tb_mot_passe.setText(Strings.EMPTY);
		tb_mot_passe_confirm.setText(Strings.EMPTY);
		
	}

	public void loadSelectedRow(PersonnelTableModel tableModel) {

		if (tableModel.getRowCount() == 0)
			return;
		int i = table.getSelectedRow();

		lbl_id.setText(table.getValueAt(i, 0).toString());
		tb_matricule.setText(table.getValueAt(i, 1).toString());
		tb_nom.setText(table.getValueAt(i, 2).toString());
		tb_prenom.setText(table.getValueAt(i, 3).toString());
		cb_groupe.setSelectedItem(table.getValueAt(i, 4).toString());
		tb_login.setText(table.getValueAt(i, 5).toString());
		tb_mot_passe.setText(table.getValueAt(i, 6).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}
}
