package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.springframework.stereotype.Component;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;

import lombok.Getter;

@Component
@Getter
public class Loginframe extends JDialog {

	private static final long serialVersionUID = 8137863997275758249L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JPasswordField txtPassword;

	private JButton btnOk;
	private JButton btnClose;



	public Loginframe() {
		setUndecorated(true);
		setResizable(false);
		initComponents();
		setFrameUp();

	}

	private void setFrameUp() {
		setTitle(ConstMessagesEN.DialogTitles.LOGING);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//LookAndFeelUtils.setWindowsLookAndFeel();
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setBounds(p.x, p.y, 328, 211);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true); 
		setModal(true);
	}



	private void initComponents() {

		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panellm = new JPanel();
		panellm.setBounds(10, 11, 308, 136);
		panellm.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panellm.setForeground(new Color(0, 128, 0));
		panellm.setBackground(new Color(255, 255, 255));

		JPanel panelbtn = new JPanel();
		panelbtn.setBounds(10, 158, 308, 42);
		panelbtn.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelbtn.setForeground(Color.LIGHT_GRAY);
		panelbtn.setBackground(new Color(255, 255, 255));

		btnOk = new JButton(ConstMessagesEN.Labels.OUVRIR_BTN);
		btnOk.setForeground(Color.DARK_GRAY);
		//System.out.println("URL =" + this.url);
		btnOk.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH +  "images/Dial.png")));
		btnOk.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnOk.setBounds(10, 11, 121, 22);
		btnOk.setMnemonic(KeyEvent.VK_O);

		btnClose = new JButton(ConstMessagesEN.Labels.QUITTER_BTN);
		btnClose.setForeground(Color.DARK_GRAY);
		btnClose.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH +"images/Erase.png")));
		btnClose.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnClose.setBounds(167, 11, 115, 22);
		btnClose.setMnemonic(KeyEvent.VK_C);

		JLabel lblNewLabel = new JLabel("Identifiant");
		lblNewLabel.setForeground(Color.GRAY);
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		lblNewLabel.setBounds(91, 42, 76, 22);
		JLabel lblNewLabel_1 = new JLabel(ConstMessagesEN.Labels.MOT_DE_PASSE);
		lblNewLabel_1.setForeground(Color.GRAY);
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		lblNewLabel_1.setBounds(91, 81, 76, 22);

		txtLogin = new JTextField();
		txtLogin.setBackground(SystemColor.menu);
		txtLogin.putClientProperty("JComponent.sizeVariant", "medium");

		txtLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		txtLogin.setBounds(167, 42, 115, 22);
		txtLogin.setColumns(10);
		panellm.setLayout(null);
		panellm.add(lblNewLabel);
		panellm.add(lblNewLabel_1);
		panellm.add(txtLogin);

		txtPassword = new JPasswordField();
		txtPassword.setBackground(SystemColor.menu);

		txtPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		txtPassword.setBounds(167, 81, 115, 22);
		panellm.add(txtPassword);
		contentPane.setLayout(null);
		contentPane.add(panelbtn);
		panelbtn.setLayout(null);
		panelbtn.add(btnOk);
		panelbtn.add(btnClose);
		contentPane.add(panellm);
		JLabel lblLosto = new JLabel("");
		lblLosto.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH+ "images/officinelogo.png")));
		lblLosto.setForeground(new Color(0, 0, 255));
		lblLosto.setFont(new Font("Trebuchet MS", Font.PLAIN, 9));
		lblLosto.setBounds(6, 6, 76, 99);
		panellm.add(lblLosto);
		
		JLabel lblPworedByLosto = new JLabel("powered by LOSTO");
		lblPworedByLosto.setForeground(new Color(60, 179, 113));
		lblPworedByLosto.setFont(new Font("Trebuchet MS", Font.BOLD, 9));
		lblPworedByLosto.setBounds(142, 6, 150, 11);
		panellm.add(lblPworedByLosto);

	}
}