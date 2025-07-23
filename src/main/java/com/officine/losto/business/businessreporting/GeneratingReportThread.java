package com.officine.losto.business.businessreporting;
import java.util.Map;
import javax.swing.JDialog;
import org.springframework.stereotype.Component;
import com.officine.losto.shared.annimation.splash.JDWaiting;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class GeneratingReportThread extends Thread {
	private JDWaiting myJDWaiting;
	private String jrxmlfile;
	private String reportFilePath;
	private Map<String, Object> params;
	private JRBeanCollectionDataSource datasource;
	private JDialog parentFrame;
	private ReportManager reportManager ; 

	public void run() {
		myJDWaiting = new JDWaiting();
		reportManager.ViewReport(reportFilePath, 
				jrxmlfile, 
				params, 
				datasource, 
				myJDWaiting, 
				parentFrame);
		myJDWaiting.setVisible(false);
		myJDWaiting.dispose();
	}
}
