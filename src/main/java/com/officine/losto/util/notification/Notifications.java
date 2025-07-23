package com.officine.losto.util.notification;



import javax.swing.JOptionPane;

import com.officine.losto.params.constant.ConstMessagesEN;

public class Notifications {

    public static void showFormValidationAlert(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                ConstMessagesEN.Messages.INFORMATION_TITLE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showDeleteRowErrorMessage() {
        JOptionPane.showMessageDialog(
                null,
                ConstMessagesEN.Messages.DELETE_ROW_ERROR,
                ConstMessagesEN.Messages.ALERT_TILE,
                JOptionPane.ERROR_MESSAGE
        );
    }
}
