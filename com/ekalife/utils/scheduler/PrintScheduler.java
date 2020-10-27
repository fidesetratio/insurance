package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.PrintPolisPrintingController;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.Print;
import com.ekalife.utils.parent.ParentScheduler;

//Scheduler untuk automatic Print unit link

public class PrintScheduler extends ParentScheduler {
 
	protected PrintPolisPrintingController ppc;
	private Properties props;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	

	public PrintPolisPrintingController getPpc() {
		return ppc;
	}

	public void setPpc(PrintPolisPrintingController ppc) {
		this.ppc = ppc;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public UwManager getUwManager() {
		return uwManager;
	}

	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}

	public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")&& 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			
			String spaj = "";
			String desc = "OK";
			String err = "";
			String msh_name = "SCHEDULER PRINT POLIS UNIT LINK";
			if(bacManager.selectAlreadyExistScheduler(msh_name)<=0){
				try{
	
					List<Map> dataPrint = new ArrayList<Map>(); //ambil semua spaj yang akan diprint
					dataPrint = bacManager.selectSpajPrint();
					for(int i=0; i<dataPrint.size();i++){
						Map spajPrint = dataPrint.get(i);
						spaj = spajPrint.get("REG_SPAJ").toString();
						
						List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
						boolean oke = true;
						for(Date d : asdf){
							if(d == null) {
								oke = false;
								break;
							}
						}
						if(asdf.size()==0){
							oke = false;
						}
						
						if(oke){//print
							
							bacManager.prosesSchedulerPrint(spaj);
							Integer mspoProvider = uwManager.selectGetMspoProvider(spaj);
							ppc.generateReportScheduler(spaj, mspoProvider);
							
							HashMap<String, Object> printer = (HashMap<String, Object>) this.bacManager.selectPropertiesPrinter();
							String ipAddress = (String) printer.get("IP_ADDRESS");
							String printerName = (String) printer.get("PRINTER_NAME");

							String cabang = elionsManager.selectCabangFromSpaj(spaj);
							String allowPrint = this.bacManager.getAllowPrint(printerName);
							String pdfFile = ppc.pdfFile(cabang, spaj);
							
							Print.directPrint(pdfFile, printerName, allowPrint);
							bacManager.insertPrintHistory(spaj, "POLIS ALL(SCHEDULER)", Print.getCountPrint(pdfFile), "1");
						}	
					}
				}catch(Exception e){
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"natanael@sinarmasmsiglife.co.id"}, null, null,  
							"Error Auto Print Unit Link", 
							e+"", null, spaj);
				}
				
				try {
					bacManager.insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							msh_name, new Date(), new Date(), desc, err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}
			}
		}
	}
	
}