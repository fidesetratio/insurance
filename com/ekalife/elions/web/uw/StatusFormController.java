package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class StatusFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	/*
	protected Connection connection = null;
	
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/
	
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Command cmd = (Command) command;
		Map map = new HashMap();
		Integer lssa_id = cmd.getLiAksep();
		List<Map> temp = elionsManager.selectLstStatusAccept(null);
		List<Map> temp2 = uwManager.selectSubStatusAccept(lssa_id);
		List<Map> lsStatusAccept = new ArrayList<Map>();
		List<Map> lsSubStatusAccept = new ArrayList<Map>();
		int pos = ServletRequestUtils.getIntParameter(request, "pos", 2);
		
		if(pos == 1) {
			for(Map m : temp) {
				int lssa = ((BigDecimal) m.get("LSSA_ID")).intValue();
				logger.info(lssa);
				if(lssa == 2 || lssa == 3 || lssa == 9) {
					logger.info("OK");
					lsStatusAccept.add(m);
				}
			}
			for(Map m : temp2) {
				lsSubStatusAccept.add(m);
			}
		} else {
			
			ArrayList<HashMap> temp3 = Common.serializableList(elionsManager.selectLstStatusAccept(null));	
			ArrayList <HashMap> lsStatus = new ArrayList<HashMap>();
			
			//pada list UW Viewer Status tidak di munculkan untuk proses BAS dan Collection
			for(HashMap m : temp3) {
				int lssa = ((BigDecimal) m.get("LSSA_ID")).intValue();
				logger.info(lssa);
				if(lssa != 15 && lssa != 16 && lssa != 18 && lssa != 19) {
					logger.info("OK");
					lsStatus.add(m);
				}
			}			
			
			lsStatusAccept.addAll(lsStatus);
			lsSubStatusAccept.addAll(temp);
		}
		
		List lsUser = elionsManager.selectLstUser();
		map.put("lsStatusAccept", lsStatusAccept);
		map.put("lsSubStatusAccept", lsSubStatusAccept);
		map.put("lsUser", lsUser);
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command = new Command();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id = currentUser.getLus_id();
		String spaj = request.getParameter("spaj");
		command.setSpaj(spaj);
		command.setFlag_ut(0);
		command.setLspd_id(ServletRequestUtils.getIntParameter(request, "pos", 2));
		Integer lssa_id = ServletRequestUtils.getIntParameter(request, "lssa_id", 0);
		Date tgl = (Date) elionsManager.selectSysdateSimple();

		List lsPosition = uwManager.selectMstPositionSpajWithSubId(spaj);
		Map mAdd = new HashMap();
		mAdd.put("LUS_ID", lus_id);
		mAdd.put("LSPD_ID", new Integer(2));
		mAdd.put("LSSA_ID", new Integer(3)); //FURTHER
		mAdd.put("LSSP_ID", new Integer(10));
		mAdd.put("MSPS_DATE", df.format(tgl));
		mAdd.put("SUB_ID", new Integer(1));
		mAdd.put("SIZE", new Integer(lsPosition.size()));
		lsPosition.add(mAdd);
		//tambahan untuk attachment
		Map MPp = (HashMap) elionsManager.selectPemegang(spaj);
		command.setLcaIdPp((String) MPp.get("LCA_ID"));
		if(currentUser.getEmail() == null || currentUser.getEmail().trim().equals(""))
			command.setError(new Integer(2));
		command.setLsPosition(lsPosition);
		command.setLiAksep(new Integer(3));
		return command;
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command = (Command) cmd;
		NumberFormat nf = new DecimalFormat("000");
		String spaj = request.getParameter("spaj");
		String fileAddress = ServletRequestUtils.getStringParameter(request, "fileAddress", null);
		String lspdPosition;
		String sBisnisId;
		Integer lspdId;
		Integer li_insured=1;
		Integer liAksep;
		Integer li_reas, li_aksep_asli = null;
		Integer bisnisId = null;
		int pos = 0;
		boolean lbUlink = false;
		
		Map mPosisi = (HashMap) elionsManager.selectF_check_posisi(spaj);
		lspdId = (Integer) mPosisi.get("LSPD_ID");
		lspdPosition = (String) mPosisi.get("LSPD_POSITION");

//		YUSUF - 15 OKT 08 - DISABLED KARENA UW MAU INI BISA DIEDIT DI VIEWER
//		if (lspdId.intValue() != 1 && lspdId.intValue() != 2 ) err.reject("", "Posisi Polis ini Ada di " + lspdPosition);
		
		Map mDataUsulan = (HashMap) uwManager.selectDataUsulan2(spaj);
		if(mDataUsulan.isEmpty()) err.reject("", "Tolong hubungi EDP Data Usulan Tidak Lengkap");
		if(mDataUsulan.get("LSBS_ID") == null) err.reject("", "Tolong hubungi EDP Bisnis Id tidak ada ");
		else{
			bisnisId = Integer.valueOf(mDataUsulan.get("LSBS_ID").toString());
			command.setMsprTsi((Double)mDataUsulan.get("MSPR_TSI"));
			command.setMsprPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
			command.setLscbPayMode((String)mDataUsulan.get("LSCB_PAY_MODE"));
		}	
		if(logger.isDebugEnabled())logger.debug(bisnisId);
		sBisnisId = nf.format(bisnisId.intValue());
		if(logger.isDebugEnabled())logger.debug(sBisnisId);

		String desc[] = new String[request.getParameterValues("textarea").length];
		desc = request.getParameterValues("textarea");
		String deskripsi = desc[desc.length - 1];
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map mTt = (HashMap) elionsManager.selectTertanggung(spaj);
		Map MPp = (HashMap) elionsManager.selectPemegang(spaj);
		command.setNamaTertanggung((String) mTt.get("MCL_FIRST"));
		command.setNamaPemegang((String) MPp.get("MCL_FIRST"));
		command.setLcaIdPp((String) MPp.get("LCA_ID"));
		
//		Attachment file
//		if(fileAddress!=null){
//			command.setAttachment(new File(fileAddress));
//		}
		
//		Yusuf - 28/11/08 - Bila sudah ACCEPTED (LSSA_ID = 5), MAKA TIDAK BISA UPDATE STATUS LAGI
//		Map mTmp = (HashMap) elionsManager.selectWf_get_status(spaj, li_insured.intValue());
//		if (mTmp.get("LSSA_ID") != null) {
//			int lssa_id = Integer.valueOf(mTmp.get("LSSA_ID").toString());
//			if(lssa_id == 5) {
//				err.reject("", "SPAJ INI STATUSNYA SUDAH 'ACCEPTED'. TIDAK BISA DILAKUKAN PERUBAHAN APAPUN!");
//				return;
//			}
//		}
		
		if(products.unitLink(sBisnisId)) lbUlink = true;
		if(request.getParameter("proses") != null)
			if(request.getParameter("proses").equals("10")) {
				String ket = request.getParameter("ket");
				String brs = request.getParameter("brs");
				Map a = (HashMap) command.getLsPosition().get(
						Integer.parseInt(brs));
				String mspsDate = (String) a.get("MSPS_DATE");
				String status = (String) a.get("STATUS_ACCEPT");
				Integer subid = ((BigDecimal) a.get("SUB_ID")).intValue();
				
				elionsManager.updateMstPositionSpajDesc(spaj, new Integer(3),mspsDate, ket.toUpperCase());
				command.setLsPosition(elionsManager.selectMstPositionSpaj(spaj));
				err.reject("", "Keterangan Telah di ubah");
				command.setError(new Integer(1));
				String lus_id = currentUser.getLus_id();
				int lspd = 2, lssp = 10;
				int liAktif = 0;
				String flagEmail = request.getParameter("flagEmail");
				String emailAdmin = elionsManager.selectEmailAdmin(spaj);
				Map agen = elionsManager.selectEmailAgen(spaj);
				String namaAgen = (String) agen.get("MCL_FIRST");
				String emailAgen = (String) agen.get("MSPE_EMAIL");
				Integer msagActive = (Integer) agen.get("MSAG_ACTIVE");
				Integer msagSertifikat = (Integer) agen.get("MSAG_SERTIFIKAT");
				Date tglSertifikat = (Date) agen.get("MSAG_BERLAKU");
				String msagId = (String) agen.get("MSAG_ID");
				Date msagBegDate = (Date) agen.get("MSAG_BEG_DATE");
				Map mGetStatus = (HashMap) elionsManager.selectWf_get_status(spaj, li_insured.intValue());
				Integer lssaId=Integer.valueOf(mGetStatus.get("LSSA_ID").toString());
				command.setSubLiAksep(subid);
				
				if(status == null){
					status = uwManager.selectStatusAcceptFromSpaj(spaj);
				}
				
				command.setFlagAdd(bacManager.kirimEmail(spaj,lssaId, emailAdmin, emailAgen,
						status, mspsDate, currentUser, ket, request.getContextPath(), msagId, msagSertifikat,
						tglSertifikat, msagBegDate, msagActive, command.getLcaIdPp(), command.getNamaPemegang(),
						command.getNamaTertanggung(), command, err, null));

			} else {
				if (deskripsi == null || deskripsi.equalsIgnoreCase(""))
					err.reject("", "Silahkan isi alasan proses ");

				li_insured = Integer.valueOf(mTt.get("MSTE_INSURED_NO").toString());
				Map mGetStatus = (HashMap) elionsManager.selectWf_get_status(spaj, li_insured.intValue());
				if (mGetStatus.get("LSSA_ID") != null)
					li_aksep_asli = Integer.valueOf(mGetStatus.get("LSSA_ID").toString());
				if (mGetStatus.get("MSTE_REAS") != null)
					li_reas = Integer.valueOf(mGetStatus.get("MSTE_REAS").toString());
				if (li_aksep_asli == null)
					li_aksep_asli = new Integer(1);
				
				String sLiAksep = request.getParameter("status");
				if (sLiAksep != null) {
					sLiAksep = sLiAksep.substring(sLiAksep.indexOf("~")+1,sLiAksep.indexOf("+"));
				} else {
					String temp = request.getParameter("temp");
					sLiAksep = temp.substring(temp.indexOf("~") + 1, temp.length());
				}
				if (sLiAksep.equals(""))
					err.reject("", "Silahkan Masukan Pilihan Anda");
				else {
					command.setLiAksep(Integer.valueOf(sLiAksep));
					if(command.getLiAksep()==3 || command.getLiAksep()==5){
						String sub = request.getParameter("substatus");
						Integer sub_id = Integer.valueOf(sub);
						command.setSubLiAksep(sub_id);
					}
					if (command.getLiAksep() == null)
						err.reject("", "Tidak ada pilihan status");

					if (li_aksep_asli.intValue() == 8 && command.getLiAksep().intValue() == 8)
						err.reject("", "Sudah Fund Alocation.");

					if ((command.getLiAksep().intValue() == 5 && !lbUlink) || (command.getLiAksep().intValue() == 8 && lbUlink)) {
						if(li_aksep_asli != 3 && li_aksep_asli != 10) {
							err.reject("","Silahkan lakukan Fund Alocation / Akseptasi Di Tombol Aksep. Menu ini hanya bisa untuk merubah status FURTHER REQUIREMENT/AKSEPTASI KHUSUS menjadi ACCEPTED");
						}
					}
										
					//(Deddy - 19 Jul 2012) Syncronisasi program SNOWS, dimana titipan premi diinput untuk semua case baik Bancass maupun non bancass.Jadi titipan premi harus diinput sebelum mengaksep polis.
					Account_recur account_recur = elionsManager.select_account_recur(spaj);
					
					List lds_dp=elionsManager.selectMstDepositPremium(spaj,null);
					List<Payment> lds_payment = elionsManager.selectPaymentCount(spaj, 1, 1);
					int isInputanBank = elionsManager.selectIsInputanBank(spaj);
					if(lds_dp.size()==0 && lds_payment.size()==0 && (command.getLiAksep().intValue()==5 || command.getLiAksep().intValue()==10) && isInputanBank!=2 && isInputanBank!=3 && !command.getLcaIdPp().equals("58") ){
						if(account_recur.getFlag_autodebet_nb()!=null){
							if(account_recur.getFlag_autodebet_nb()!=1){
								err.reject("","Jumlah Titipan Premi/Payment Belum Di Input.Silakan diinput terlebih dahulu sebelum melakukan Akseptasi/Akseptasi Khusus");
							}
						}else{
							err.reject("","Jumlah Titipan Premi/Payment Belum Di Input.Silakan diinput terlebih dahulu sebelum melakukan Akseptasi/Akseptasi Khusus");
						}
					}
				}
			}
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command = (Command) cmd;
		command = elionsManager.prosesStatusFormController(command,request,err);
		command.setLsPosition(uwManager.selectMstPositionSpajWithSubId(command.getSpaj()));
		
		return new ModelAndView("uw/status", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request, cmd, err));
	}



}