package com.officine.losto.test;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Vector;


public class FilteredComboBoxDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FilteredComboBoxDemo().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("ComboBox filtrant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        Vector<String> data = new Vector<>(List.of("Paracétamol", "Ibuprofène", "Amoxicilline", "Doliprane", "Dafalgan", "Efferalgan", "Spasfon"));

       
        
        JComboBox<String> comboBox = new JComboBox<>(new Vector<>(data));
        comboBox.setEditable(true);

        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = editor.getText();
                comboBox.hidePopup();

                List<String> filtered = data.stream()
                		.filter(item -> item.toLowerCase().contains(input.toLowerCase()))
                        .collect(Collectors.toList());

                comboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(filtered)));
                editor.setText(input);
                comboBox.setSelectedItem(input);
                comboBox.showPopup();
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(comboBox);
        frame.setVisible(true);
    }
}
