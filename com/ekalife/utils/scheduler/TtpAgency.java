package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.ListSpajTtp;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentScheduler;

public class TtpAgency extends ParentScheduler{

	public void main() throws Exception{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List<ListSpajTtp> hasil = this.uwManager.selectTtpTransferAgency();
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			if(hasil.size()!=0){
				//Bagian ini untuk menampilkan nilai SPAJ
				String   spajLoop="\nNO POLIS           KODE AGEN                                                    NAMA AGEN\n";
				spajLoop=spajLoop+"===================================================================================================\n";
				for (int i = 0; i < hasil.size(); i++) {
					ListSpajTtp hasilMap=hasil.get(i);
					spajLoop=spajLoop+ hasilMap.getReg_spaj()+" "+" "+FormatString.rpadRataKiri(" ", hasilMap.getMsag_id(), 60) +" "+"         "+ df.format(hasilMap.getMcl_first())+"\n";

				}
				//email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"Irman@sinarmasmsiglife.co.id", "Anna@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id", "Eti@sinarmasmsiglife.co.id", "ari@sinarmasmsiglife.co.id", "MieYoen@sinarmasmsiglife.co.id"}, null, "AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING", "PESAN INI TERKIRIM HANYA SEBAGAI REMINDER UNTUK AKSEPTASI PENGIRIMAN SPH/SPT DARI UNDERWRITING.\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
				email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"ali@sinarmasmsiglife.co.id","ariseko@sinarmasmsiglife.co.id","edhy@sinarmasmsiglife.co.id","josephine@sinarmasmsiglife.co.id","wesni@sinarmasmsiglife.co.id","robby@sinarmasmsiglife.co.id","tri.handini@sinarmasmsiglife.co.id","wenny@sinarmasmsiglife.co.id"}, null, null, "DAFTAR POLIS YANG MASIH DI CHECKING TTP > 2 HARI", "BERIKUT ADALAH DAFTAR SPAJ YANG MASIH BERADA DI PROSES CHECKING TTP:\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
//				email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {"yusuf@sinarmasmsiglife.co.id"}, null, null, "DAFTAR POLIS YANG MASIH DI CHECKING TTP > 2 HARI", "BERIKUT ADALAH DAFTAR SPAJ YANG MASIH BEADA DI PROSES CHECKING TTP: .\n"+spajLoop+" \nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions." , null);
				try {
					uwManager.insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							"SCHEDULER TTP AGENCY", bdate, new Date(), desc,err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}

			}else{
			}
		}
	}
}