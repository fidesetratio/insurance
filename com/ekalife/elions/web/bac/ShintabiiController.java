package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author HEMILDA Controller Shinta bii
 */
public class ShintabiiController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );

	protected Map referenceData(HttpServletRequest request, Object cmd,
			Errors err) throws Exception {
		String spaj = ServletRequestUtils
				.getStringParameter(request, "spaj", ServletRequestUtils
						.getStringParameter(request, "editSPAJ", ""));
		String status = ServletRequestUtils.getStringParameter(request,
				"status", ServletRequestUtils.getStringParameter(request,
						"sts", ""));
		String flag = ServletRequestUtils.getStringParameter(request, "flag",
				ServletRequestUtils.getStringParameter(request, "flag", ""));
		Boolean show_ajspusat = ServletRequestUtils.getBooleanParameter(request, "show_ajspusat", true);
		ReffBii datautama = (ReffBii) cmd;
		Map map = new HashMap();
		datautama.setReg_spaj(spaj);
		datautama.setStatus(status);
		datautama.setHit_err(new Integer(0));
		Integer kode_flag = new Integer(0);
		/**
		 * bank Sinarmas
		 */
		if (flag.equalsIgnoreCase("shinta") ||flag.equalsIgnoreCase("shintacekbc") ) {
			kode_flag = new Integer(2);
		}else if (flag.equalsIgnoreCase("syariah")||flag.equalsIgnoreCase("shintacekbcsyh")) {//syariah
			kode_flag = new Integer(16);
		}else if (flag.equalsIgnoreCase("victoria")) {//Bank Victoria
			kode_flag = new Integer(20);
		}else if (flag.equalsIgnoreCase("simas")) {
			/**
			 * simas
			 */
			kode_flag = new Integer(3);
		}else if (flag.equalsIgnoreCase("biiothers")) {
			/**
			 * biiothers
			 */
			kode_flag = new Integer(12);
		}else if (flag.equalsIgnoreCase("harda")) {
			/**
			 * Bank Harda
			 */
			kode_flag = new Integer(25);
		}else if (flag.equalsIgnoreCase("bri")) {
			/**
			 * Bank BRI
			 */
			kode_flag = new Integer(42);
		}else if (flag.equalsIgnoreCase("btnsyh")) { //helpdesk [133346] produk baru 142-13 Smart Investment Protection
			/**
			 * Bank BTN Syariah
			 */
			kode_flag = new Integer(43);
		}else if (flag.equalsIgnoreCase("bjb")) {
			/**
			 * Bank BJB
			 */
			kode_flag = new Integer(44);
		}else if (flag.equalsIgnoreCase("bjatim")) {
			/**
			 * Bank JATIM
			 */
			kode_flag = new Integer(51);
		}else if (flag.equalsIgnoreCase("bjatimsyh")) {
			/**
			 * Bank JATIM SYARIAH
			 */
			kode_flag = new Integer(58);
		}else if (flag.equalsIgnoreCase("bukopin")) {
			/**
			 * Bank BUKOPIN
			 */
			kode_flag = new Integer(50);
		}else if (flag.equalsIgnoreCase("btnsyariah")) {
			/**
			 * Bank BTN SYARIAH
			 */
			kode_flag = new Integer(56);
		}
		else if (flag.equalsIgnoreCase("bukopinsyariah")) {
			/**
			 * Bank BUKOPIN SYARIAH
			 */
			kode_flag = new Integer(60);
		} else if (flag.equalsIgnoreCase("btn")) { //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
			/**
			 * Bank BTN
			 */
			kode_flag = new Integer(63);
		}
		
		// agen penutup
		if (datautama.getLrb_id() != null) {
			Map data = (HashMap) this.elionsManager.select_referrer_shinta(
					datautama.getLrb_id(), kode_flag);
			if (data != null) {
				datautama.setNama_reff((String) data.get("NAMA_REFF"));
				datautama.setNo_rek((String) data.get("NO_REK"));
				datautama.setCab_rek((String) data.get("CAB_REK"));
				datautama.setAtas_nama((String) data.get("ATAS_NAMA"));
				datautama.setFlag_aktif((String) data.get("FLAG_AKTIF"));
				datautama.setNpk((String) data.get("NPK"));
				datautama.setLcb_no((String) data.get("LCB_NO"));
				//datautama.setLcb_no((String)head_no.get("LCB_NO"));
				datautama.setLcb_penutup((String) data.get("LCB_NO"));
				datautama.setLrbj_id1((String) data.get("LRBJ_ID1"));
				if (((String) data.get("FLAG_AKTIF")).equals("1")) {
					datautama.setAktif("AKTIF");
				} else {
					datautama.setAktif("TIDAK AKTIF");
				}

				// referral diset sama dengan agen penutup, bila dikosongin
				if (datautama.getReff_id() == null
						|| "".equals(datautama.getReff_id())) {
					datautama.setReff_id(datautama.getLrb_id());
					datautama.setNama_reff2((String) data.get("NAMA_REFF"));
					datautama.setNo_rek2((String) data.get("NO_REK"));
					datautama.setCab_rek2((String) data.get("CAB_REK"));
					datautama.setAtas_nama2((String) data.get("ATAS_NAMA"));
					datautama.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
					datautama.setNpk2((String) data.get("NPK"));
					datautama.setLcb_no2((String) data.get("LCB_NO"));
					datautama.setLrbj_id1_2((String) data.get("LRBJ_ID1"));
					//datautama.setLcb_no_kk((String)head_no.get("LCB_NO"));
					datautama.setLcb_penutup2((String) data.get("LCB_NO"));
					if (((String) data.get("FLAG_AKTIF")).equals("1")) {
						datautama.setAktif2("AKTIF");
					} else {
						datautama.setAktif2("TIDAK AKTIF");
					}
				}

			}
		}
		// referral
		//untuk set jadi bisa  refferal syariah - ridhaal
//		kode_flag = 16;
		if (datautama.getReff_id() != null) {
			Map data = (HashMap) this.elionsManager.select_referrer_shinta(
					datautama.getReff_id(), kode_flag);
			if (data != null) {
				datautama.setNama_reff2((String) data.get("NAMA_REFF"));
				datautama.setNo_rek2((String) data.get("NO_REK"));
				datautama.setCab_rek2((String) data.get("CAB_REK"));
				datautama.setAtas_nama2((String) data.get("ATAS_NAMA"));
				datautama.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
				datautama.setNpk2((String) data.get("NPK"));
				datautama.setLcb_no2((String) data.get("LCB_NO"));
				datautama.setLcb_penutup2((String) data.get("LCB_NO"));
				datautama.setLrbj_id1_2((String) data.get("LRBJ_ID1"));
				List<Map> cabangKK = new ArrayList<Map>();
				cabangKK = uwManager.selectCabangKK(datautama.getLcb_no2());
				//datautama.setLcb_no2_kk(cabangKK);
				if(cabangKK.size()>0){
					Map asd = new HashMap();
					asd.put("LCB_NO", datautama.getLcb_no2());
					asd.put("NAMA_CABANG", "CABANG " + datautama.getCab_rek2());
					cabangKK.add(0,asd);
				}
				if (((String) data.get("FLAG_AKTIF")).equals("1")) {
					datautama.setAktif2("AKTIF");
				} else {
					datautama.setAktif2("TIDAK AKTIF");
				}
				map.put("lcb_no_kk", cabangKK);
			}
		}
		datautama.setFlag(flag);
        map.put("show_ajspusat",show_ajspusat);
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		ReffBii datautama = new ReffBii();
		String spaj = ServletRequestUtils
				.getStringParameter(request, "spaj", ServletRequestUtils
						.getStringParameter(request, "editSPAJ", ""));
		String status = ServletRequestUtils.getStringParameter(request,
				"status", ServletRequestUtils.getStringParameter(request,
						"sts", ""));
		String flag = ServletRequestUtils.getStringParameter(request, "flag",
				ServletRequestUtils.getStringParameter(request, "flag", ""));
		
		String pabsm = ServletRequestUtils.getStringParameter(request, "pabsm", ServletRequestUtils.getStringParameter(request, "pabsm"));

		datautama.setReg_spaj(spaj);
		datautama.setStatus(status);
		datautama.setFlag(flag);
		//tambahan dari andy untuk pa bsm
		datautama.setPabsm(pabsm);
		//==========================
		datautama = (ReffBii) this.elionsManager.selectmst_reff_bii(spaj);
		if (datautama == null) {
			datautama = new ReffBii();
		}

		return datautama;
	}

	protected void onBind(HttpServletRequest request, Object cmd,
			BindException errors) throws Exception {
		ReffBii datautama = (ReffBii) cmd;
		datautama.setHit_err(new Integer(0));
		
		if (datautama.getStatus().equalsIgnoreCase("insert")) {
			if (datautama.getLrb_id() == null) {
				datautama.setLrb_id("");
			}
			if (datautama.getLcb_no() == null) {
				datautama.setLcb_no("");
			}
			if (datautama.getLrb_id().equalsIgnoreCase("")) {
				datautama.setStatus("awal");
				datautama.setHit_err(new Integer(1));
				errors.rejectValue("lrb_id", "",
						"Silahkan cari kode FA/Agent Penutup terlebih dahulu.");
			}
			if (datautama.getReff_id().equalsIgnoreCase("")) {
				datautama.setStatus("awal");
				datautama.setHit_err(new Integer(1));
				errors.rejectValue("lrb_id", "",
						"Silahkan cari kode Referral terlebih dahulu.");
			}
			
			//Khusus BC , ada Pengecekan KHUSUS ( Semua SPAJ BC harus sesuai cabang yang dihandle nya)//
			if(datautama.getLrbj_id1() != null){
				Map mDataUsulan=uwManager.selectDataUsulan(datautama.getReg_spaj());
				Integer lsbsId = 0;
				Integer lsdbsNumber = 0;
				if(mDataUsulan!=null){
					lsbsId = (Integer)mDataUsulan.get("LSBS_ID");
					lsdbsNumber = (Integer)mDataUsulan.get("LSDBS_NUMBER");
				}

				Integer jnbankdetbisnis = bacManager.selectJnBankDetBisnis(lsbsId, lsdbsNumber);
			// (lsbsId==120 || lsbsId==202){
				if(datautama.getLrbj_id1().equalsIgnoreCase("366")){
					Integer lewat = bacManager.selectCountAreaBC(datautama.getLrb_id() , datautama.getLcb_no2());
					if (lewat==0){
						if((lsbsId==216 && lsdbsNumber==1) || 
								(lsbsId==215 && lsdbsNumber==1) || 
								jnbankdetbisnis==44 || jnbankdetbisnis==50 || jnbankdetbisnis==51 || jnbankdetbisnis==56 || jnbankdetbisnis==58 || jnbankdetbisnis==60 || jnbankdetbisnis==63
								|| (lsbsId==212 && lsdbsNumber==9)
								|| (lsbsId==118 && lsdbsNumber==3)
								|| (lsbsId==118 && lsdbsNumber==4)
								|| (lsbsId==116 && lsdbsNumber==3)
								|| (lsbsId==116 && lsdbsNumber==4)
								
								
								
								
								
								){
							//hanya sementara karena prime link syariah dan magana link syariah belum dapat ijin untuk dijual,
							//sementara menggunakan refferalnya BC-BC
						}else{
							errors.rejectValue("lrb_id", "","Untuk tutupan SPAJ "+ datautama.getReg_spaj() +" atas nama / Agent Tersebut " +datautama.getAtas_nama()+
								" tidak terdaftar di cabang [ "+datautama.getCab_rek2()+ " ]. Silakan dikonfirmasikan ke leaderbancassurancebsim@sinarmasmsiglife.co.id mengenai perihal ini" );
						}
					}	
				}
			//	}
			}
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		ReffBii datautama = (ReffBii) cmd;
		String spaj = datautama.getReg_spaj();
		String cabangKK = ServletRequestUtils.getStringParameter(request, "lcb_no_kk","");
		Integer status_polis = null;
		if("y".equals(datautama.getPabsm())){//pabsm ada kemungkinan null, jdnya dibalik
			status_polis = 10;//nilai ngasal buat masuk validasi (status_polis.intValue() != 5)
		}else{
			status_polis = this.elionsManager.selectPositionSpaj(datautama.getReg_spaj());
		}
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		/**
		 * @author HEMILDA kalau sudah aksep tidak bisa diubah lagi
		 */
		if (status_polis.intValue() != 5) {
			// logger.info(datautama.getStatus());
			if (datautama.getStatus().equalsIgnoreCase("insert")) {
				
				//mod to uwmanager by andy
				datautama = uwManager.ShintabiiSubmit(currentUser, datautama, cabangKK);
				
				/* pindah ke uwmanager
				//Yusuf (7 Aug 09) 
				//Request Joni BSM : MST_REFF_BII diisi LCB_NO dari USER nya, bukan LCB_NO dari REFERALnya
				//Sehingga hak untuk mengubah data tetap ada di usernya, yg berubah adalah report2nya
				String lcb_no = datautama.getLcb_no();
				//Deddy (16 Dec 2009)
				//Req by BSM(via Ko Yusuf)
				//Proses insert ke 
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				if(!currentUser.getCab_bank().trim().equals("") && !currentUser.getCab_bank().trim().equals("SSS")){
					lcb_no = currentUser.getCab_bank();
				}
				if(!cabangKK.equals("") && !cabangKK.equals(datautama.getLcb_no2())){
					String lcbNyaLrb = cabangKK;
					this.elionsManager.deletemstreff_bii(datautama.getReg_spaj());
					this.uwManager.insertmst_reff_bii2(datautama.getReg_spaj(),
							"4", lcb_no, datautama.getLrb_id(), datautama.getReff_id(), lcbNyaLrb,datautama.getLcb_no_kk(),currentUser.getLus_id());
				datautama.setStatussubmit("1");}
				else{
					Map referrer = elionsManager.select_referrer(datautama.getLrb_id());
					String lcbNyaLrb = (String) referrer.get("LCB_NO");
					this.elionsManager.deletemstreff_bii(datautama.getReg_spaj());
					this.uwManager.insertmst_reff_bii2(datautama.getReg_spaj(),
							"4", lcb_no, datautama.getLrb_id(), datautama.getReff_id(), lcbNyaLrb,datautama.getLcb_no(), currentUser.getLus_id());
					// logger.info("insert reff bii");
					datautama.setStatussubmit("1");
				}
				*/
			}
			datautama.setStatus("awal");
		} else {
			datautama = new ReffBii();
			datautama.setStatussubmit("5");
			datautama.setReg_spaj(spaj);
			err.rejectValue("reg_spaj", "",
					"Spaj ini sudah diaksep, tidak bisa diedit lagi.");
		}
	
		return new ModelAndView("bac/reff_bii_shinta", "cmd", datautama)
				.addAllObjects(this.referenceData(request, cmd, err));
}


	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("common/duplicate");
	}

}