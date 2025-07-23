package com.officine.losto.business.businessreporting;

import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import com.officine.losto.shared.annimation.splash.JDWaiting;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class ReportManager {
	private JDWaiting myJDWaiting;
	private String jrxmlfile;
	private String reportFilePath;
	private Map<String, Object> params;
	private JRBeanCollectionDataSource datasource;
	private JDialog parentFrame;

	

	public  boolean ViewReport(String reportFilePath, String jrxmlfile, Map<String, Object> params,
			JRBeanCollectionDataSource datasource, JDialog myJDWaiting, JDialog parentFrame) {
		try {

			myJDWaiting.setLocationRelativeTo(parentFrame);
			parentFrame.setAlwaysOnTop(false);
			parentFrame.setModal(false);
			parentFrame.setEnabled(false);
			myJDWaiting.setAlwaysOnTop(true);
			myJDWaiting.setVisible(true);
			JasperDesign jDesign = JRXmlLoader.load(reportFilePath + jrxmlfile);
			JasperReport jReport = JasperCompileManager.compileReport(jDesign);
			JasperPrint Jprint = JasperFillManager.fillReport(jReport, params, datasource);
			JasperViewer Reportviewer = new JasperViewer(Jprint, false);
			Reportviewer.setLocationRelativeTo(null);
			Reportviewer.setVisible(true);
			Reportviewer.setAlwaysOnTop(true);
			myJDWaiting.setAlwaysOnTop(false);
			myJDWaiting.setVisible(false);
			myJDWaiting.disable();
			myJDWaiting.dispose();

		} catch (JRException e) {
			JOptionPane.showMessageDialog(null, 
					"Not able to preview the report", "erreur de reporting",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();

		}
		return false;
	}

}
