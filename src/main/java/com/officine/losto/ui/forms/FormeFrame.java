package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.logging.log4j.util.Strings;
import com.officine.losto.backend.entity.Forme;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.FormeTableModel;
import com.officine.losto.uti.shared.Shared;

import lombok.Getter;



@org.springframework.stereotype.Component
@Getter
public class FormeFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JButton btnAjouter;
	private javax.swing.JButton btnSupprimer;

	private JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel jPanel4;

	private JScrollPane tablejScrollPane;
	private JTextField tb_code;
	private JTextField tb_libelle;
	private JTextField tb_search;

	
	
	private JPanel TablePanel;
	private JTable table; 
	private JTextArea tb_ErrorMessage;
	private JLabel jLabel2_1;
	private JLabel jLabel2_3;
	private JTableHeader tableHeader; 
	
	//public FormeFrame() {}

	public FormeFrame(FormeTableModel tableModel) {
		
		setRootPaneCheckingEnabled(false);
		setResizable(false);
		
		initComponents();
		//Shared.setFrameUp(MainMenuFrame.ctrl.formeFrame, this);
		
		this.getTable().setModel(tableModel);
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
		tableHeader.setBorder(null);
		tableHeader.setMixingCutoutShape(getShape());
		this.getTable().setRowHeight(20);
		
		
	}

	@SuppressWarnings("serial")
	private void initComponents() {
		getContentPane().setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		getContentPane().setForeground(Color.GRAY);
		setUndecorated(false);
		setTitle(ConstMessagesEN.DialogTitles.FORME_MODAL);
		//setResizable(true);
		//setSize(ConstMessagesEN.Params.DEFAULT_WIDTH, ConstMessagesEN.Params.DEFAULT_HEIGHT);
		//setBounds(100, 100, 20, 20);
		
		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBorder(null);
		BtnjPanel.setBackground(Color.WHITE);
		BtnjPanel.setPreferredSize(new Dimension(720, 40));
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(null);
		FormJPanel.setPreferredSize(new Dimension(270, 300));
		TablePanel = new javax.swing.JPanel();
		TablePanel.setBorder(null);
		TablePanel.setBackground(Color.WHITE);
		//groupeTablePanel.setBorder(null);
		

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();

		tb_code = new javax.swing.JTextField();
		tb_code.setBackground(SystemColor.window);

		tb_libelle = new javax.swing.JTextField();
		tb_libelle.setBackground(SystemColor.window);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBorder(null);
		jPanel4.setBackground(Color.WHITE);
		jPanel4.setBounds(0, 254, 449, 46);
		jLabel9 = new javax.swing.JLabel();
		jLabel9.setBounds(10, 14, 88, 14);
		tb_search = new javax.swing.JTextField();
		tb_search.setBackground(Color.WHITE);
		tb_search.setBounds(87, 11, 356, 20);
		tablejScrollPane = new javax.swing.JScrollPane();
		tablejScrollPane.setBorder(null);
		tablejScrollPane.setBounds(0, 0, 449, 254);
		table = new javax.swing.JTable() {

			/*public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();

			}*/

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					//c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
					//c.setBackground(row % 2 == 0 ? Color.decode("#f1f2dc") : Color.white); //new Color(220, 240, 255)
					c.setBackground(row % 2 == 0 ? new Color(220, 240, 255) : Color.white);
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.BLUE));

				return c;
			}

		};
		table.setBorder(null);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setFillsViewportHeight(true);
		table.setBorder(null);
		table.setForeground(Color.BLACK);
		table.setGridColor(SystemColor.red);
		table.setIntercellSpacing(new Dimension(0, 0));
		
		
	
		

		
		
		
		//table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		BtnjPanel.setPreferredSize(new Dimension(720, 40));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(488, Short.MAX_VALUE))
		);
		gl_BtnjPanel.setVerticalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAjouter)
						.addComponent(btnSupprimer))
					.addContainerGap())
		);
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);

		jLabel1.setText("Code forme");

		jLabel2.setText("Désignation forme");

		tb_code.setEditable(false);
		tb_code.setEnabled(false);
		
		tb_ErrorMessage = new JTextArea();
		tb_ErrorMessage.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		tb_ErrorMessage.setBackground(Color.WHITE);
		
		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");
		
		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		javax.swing.GroupLayout gl_FormJPanel = new javax.swing.GroupLayout(FormJPanel);
		gl_FormJPanel.setHorizontalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(tb_ErrorMessage, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(jLabel2)
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(jLabel1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(jLabel2_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(jLabel2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tb_libelle, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
								.addComponent(tb_code, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_FormJPanel.setVerticalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(jLabel2_3)
						.addComponent(tb_code, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel2)
						.addComponent(tb_libelle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_1))
					.addGap(67)
					.addComponent(tb_ErrorMessage, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(48, Short.MAX_VALUE))
		);
		FormJPanel.setLayout(gl_FormJPanel);

		getContentPane().add(FormJPanel, java.awt.BorderLayout.LINE_START);
		TablePanel.setLayout(null);

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(ConstMessagesEN.Labels.RECHERCHER);

		TablePanel.add(jPanel4);
		jPanel4.setLayout(null);
		jPanel4.add(jLabel9);
		jPanel4.add(tb_search);

		
		
		tablejScrollPane.setViewportView(table);
		TablePanel.add(tablejScrollPane);
		getContentPane().add(TablePanel, java.awt.BorderLayout.CENTER);

		pack();
	}

	public Forme getMenuFromForm() {
		Forme forme = new Forme(); 
		forme.setFormeName(tb_libelle.getText().trim());
	
		if (! tb_code.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			forme.setId(Long.parseLong(tb_code.getText().trim()));	
		  
		return forme; 
	}
	
	public void clearForm() {
		tb_libelle.setText(Strings.EMPTY);
		tb_code.setText(Strings.EMPTY);
		tb_ErrorMessage.setText(Strings.EMPTY);
		
	}
	
	



}
