package com.officine.losto.uti.ui.LookAndFeelUtils;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.officine.losto.params.constant.ConstMessagesEN;



public class LookAndFeelUtils {

	  public static void setWindowsLookAndFeel() {
	        try {
	            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	        	/*Metal
	        	Nimbus
	        	CDE/Motif
	        	Mac OS X
	        	Metal
	        	Nimbus
	        	CDE/Motif
	        	Mac OS X*/

	            /*for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

	                if ("Mac OS X".equals(info.getName())) {
	                    //UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	                

	        }*/
	        	
	        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        }catch (Exception e) {
	            JOptionPane.showMessageDialog(
	                    null,
	                    ConstMessagesEN.Messages.WINDOWS_STYLE_LOADING_ERROR_MESSAGE + e,
	                    ConstMessagesEN.Messages.ALERT_TILE,
	                    JOptionPane.ERROR_MESSAGE
	            );
	        }
	    }

}
