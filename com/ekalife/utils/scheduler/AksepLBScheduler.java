package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.ListPengirimanPolis;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AksepLBScheduler extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );
	public void main() throws Exception{
		
		Date bdate 	= new Date();
		String desc	= "OK";
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List<ListPengirimanPolis> hasil = this.uwManager.selectListPengirimanPolis();
		logger.info(hasil);
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSCLUS1"))
				) {
			if(hasil.size()!=0){
				//Bagian ini untuk menampilkan nilai SPAJ(yg harus diproses oleh Life Benefit) dalam emailnya
				String   spajLoop="\nNO POLIS           NAMA PRODUK                                                    TANGGAL AKSEPTASI\n";
				spajLoop=spajLoop+"===================================================================================================\n";
				for (int i = 0; i < hasil.size(); i++) {
					ListPengirimanPolis hasilMap=hasil.get(i);
					spajLoop=spajLoop+ hasilMap.getMspo_policy_no_format()+" "+" "+FormatString.rpadRataKiri(" ", hasilMap.getLsdbs_name(), 60) +" "+"         "+ df.format(hasilMap.getMste_tgl_aksep())+"\n";
				}
				//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Irman@sinarmasmsiglife.co.id", "Anna@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id", "Eti@sinarmasmsiglife.co.id", "ari@sinarmasmsiglife.co.id", "MieYoen@sinarmasmsiglife.co.id"}, null, "AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER UNTUK AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
				//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Sulardi@sinarmasmsiglife.co.id", "dede@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id"}, null, "AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER UNTUK AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
				//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Deddy@sinarmasmsiglife.co.id"}, null, null, "AKSEPTASI PENGIRIMAN POLIS DARI UNDERWRITING", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER UNTUK AKSEPTASI PENGIRIMAN POLIS KE LIFE BENEFIT.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
			}else{
			}
		}
		
		try {
			uwManager.insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					"SCHEDULER AKSEPTASI LB", bdate, new Date(), desc,"");
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
		}

	}
	
}









