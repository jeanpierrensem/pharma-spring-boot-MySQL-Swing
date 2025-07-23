package com.officine.losto.uti.shared;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.MainMenuFrame;

public class Shared {
	public static ctrl ctrl = null;

	public static boolean DONNEES_BASE_CTRL = false;

	public static void displayFrame(JDialog jif) {
		try {

			jif.setAlwaysOnTop(true);
			jif.setModal(true);
			jif.setLocationRelativeTo(MainMenuFrame.ctrl.mainMenu);
			jif.setVisible(true);

		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

	public static void displayFrame(JDialog jif, JDialog parent) {
		try {

			jif.setAlwaysOnTop(true);
			if (parent != null)
				parent.setAlwaysOnTop(false);
			jif.setModal(true);
			jif.setLocationRelativeTo(parent);
			jif.setVisible(true); // bloque jusqu'à la fermeture

		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	public static JDialog setFrameUp(JDialog jif, JDialog current_jif) {
		try {
			if (jif == null)
				jif = current_jif;
			jif.setAlwaysOnTop(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jif;
	}

	public static int getRandomNumber(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	public static String generateRandom(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHMMSS");
		String numCommande = type.concat(sdf.format(new java.util.Date()))
				.concat("" + getRandomNumber(ConstMessagesEN.Params.MIN, ConstMessagesEN.Params.MAX));
		return numCommande;
	}

	public static String toLetter(double value) {
		RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(Locale.FRANCE, RuleBasedNumberFormat.SPELLOUT);
		if (value == 0)
			return "";
		return rbnf.format(value).toUpperCase() + " FRANCS CFA";
	}

	public static JComboBox<String> customizeDropdownCompoment(KeyEvent e, JComboBox<String> comboBox,
			List<String> data, JTextField textField) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP)
			return comboBox;

		String input = textField.getText();

		comboBox.hidePopup();
		List<String> filtered = data.stream().filter(item -> item.toLowerCase().contains(input.toLowerCase()))
				.collect(Collectors.toList());
		comboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(filtered)));
		textField.setText(input);
		comboBox.setSelectedItem(input);
		comboBox.showPopup();
		return comboBox;
	}

}
