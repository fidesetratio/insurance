package com.ekalife.elions.web.uw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;

/**
 * @author Yusuf
 *
 */
public class editTglTrmKrmAdminController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
//		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
//		String lspdPosition=(String)mPosisi.get("LSPD_POSITION");
		Integer lspdId=1;
		String lspdPosition="INPUT SPAJ";
		String keterangan;
		//
		Date tglTerimaAdmin;
		Date tglKirimPolis;
		Date tglPrintPolis;
		Date tglSpajDoc;
		List lsError=new ArrayList();
		//
		if(show==0 && lspdId!=1){//tanggal terima spaj 
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
			
		}else if(show==2 && lspdId!=2){//tanggal spaj doc
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
		}else if(show==1){//tanggal kirim
			//cek posisi hanya 6 dan 9
			if(! (lspdId==6 || lspdId==9 ))
				lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
		}
		
		Map mTertanggung=elionsManager.selectTertanggung(spaj);
		tglTerimaAdmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		tglPrintPolis=(Date)mTertanggung.get("MSPO_DATE_PRINT");		
		tglKirimPolis=(Date)mTertanggung.get("MSTE_TGL_KIRIM_POLIS");
		Date tglInput = (Date) mTertanggung.get("MSPO_INPUT_DATE");
		tglSpajDoc=(Date)mTertanggung.get("MSTE_TGL_SPAJ_DOC");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		//
		Map mPemegang=elionsManager.selectPemegang(spaj);
		Integer lsspId=(Integer)mPemegang.get("LSSP_ID");
		
		if(flag.equals("")){//not save
			if(show==0){//edit tanggal terima spaj
				if(tglTerimaAdmin==null)
					params.put("tanggalTerima", sysdate);
				else
					params.put("tanggalTerima", FormatDate.toString(tglTerimaAdmin));
			}else if(show==1){//edit tanggal kirim spaj
				if(tglKirimPolis==null)
					params.put("tanggalKirim", sysdate);
				else
					params.put("tanggalKirim", FormatDate.toString(tglKirimPolis));
			}else if(show==2){//edit tanggal spaj doc
				if(tglSpajDoc==null)
					params.put("tglSpajDoc", sysdate);
				else
					params.put("tglSpajDoc", FormatDate.toString(tglSpajDoc));
			}
			keterangan=elionsManager.selectMstPositionSpajMspsDesc(spaj, show);
			
		}else{//save
			String sTglTerimaAdmin=ServletRequestUtils.getStringParameter(request, "tanggalTerimaAdmin","00/00/0000");
			String sTglKirimPolis=ServletRequestUtils.getStringParameter(request, "tanggalKirim","00/00/0000");
			String sTglSpajDoc=ServletRequestUtils.getStringParameter(request, "tglSpajDoc","00/00/0000");
			keterangan=ServletRequestUtils.getStringParameter(request, "keterangan",null);
			if(show==0){//edit tanggal terima spaj
				if(sTglTerimaAdmin.equals("00/00/0000")){
					lsError.add("Silahkan Masukan tanggal Terima Admin dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglTerimaAdmin + " " + hh + ":" + mm);
						
						if(tglInput!=null){
							if(tmp.before(tglInput)){
								lsError.add("Tanggal Terima Admin harus lebih besar dari tanggal input spaj!");
							}else{
								elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
								params.put("success", "Berhasil Update Tanggal Terima Admin");
							}
						}else{
							elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Terima Spaj");
						}
					}	
				}
					params.put("tanggalTerima", sTglTerimaAdmin);
			}else if (show==1){//edit tanggal kirim spaj
				if(sTglKirimPolis.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Kirim Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglKirimPolis+ " " + hh + ":" + mm);
						if(tglPrintPolis!=null){
							if(tmp.before(tglPrintPolis)){
								lsError.add("Tanggal Kirim Polis harus lebih besar dari tanggal print polis!");
							}else{
								elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
								Map policy =(Map)elionsManager.selectPemegang(spaj);
								String noPolis=(String)policy.get("MSPO_POLICY_NO");
								String nama=(String)policy.get("MCL_FIRST");
								String subject="Print Polis";
								String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
								if(emailCabang==null)
									emailCabang=request.getParameter("email");
								
								String lca_id = (String) mTertanggung.get("LCA_ID");
								
								if(emailCabang!=null){
									params.put("email",1);
									String to[]={emailCabang};
									String cc[]={currentUser.getEmail()};
									String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting";
									if(!lca_id.equals("09")&& !lca_id.equals("40")){ 
										email.send(false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null);
									}else if(lca_id.equals("40")){
										String to1[]={"rumanthi@sinarmasfile.co.id;astri@sinarmasmsiglife.co.id"};
										email.send(false, props.getProperty("admin.ajsjava"), to1, to, null, subject,message, null);
									}
								}else
									params.put("email",0);
	
								params.put("success", "Berhasil Update Tanggal Kirim Spaj");
							}
						}else{

							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,null);
							Map policy =(Map)elionsManager.selectPemegang(spaj);
							String noPolis=(String)policy.get("MSPO_POLICY_NO");
							String nama=(String)policy.get("MCL_FIRST");
							String subject="Print Polis";
							String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
							if(emailCabang==null)
								emailCabang=request.getParameter("email");
							
							String lca_id = (String) mTertanggung.get("LCA_ID");
							
							if(emailCabang!=null){
								params.put("email",1);
								String to[]={emailCabang};
								String cc[]={currentUser.getEmail()};
								String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting";
								if(!lca_id.equals("09")) email.send(false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null);
							}else
								params.put("email",0);

							params.put("success", "Berhasil Update Tanggal Kirim Spaj");
						
						}
						
					}
				}
					params.put("tanggalKirim", sTglKirimPolis);
			}else if(show==2){//edit tanggal spaj doc
				if(keterangan==null)
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				if(sTglSpajDoc.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglSpajDoc+ " " + hh + ":" + mm);
						if(tglTerimaAdmin!=null){
							if(tmp.before(tglTerimaAdmin)){
								lsError.add("Tanggal terima Admin Doc harus lebih besar dari tanggal Terima Spaj!");
							}else{
								elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,null);
								params.put("success", "Berhasil Update Tanggal Spaj Doc");
							}
						}else{
							elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Spaj Doc");
						}
					
					}
				}
					params.put("tglSpajDoc", sTglSpajDoc);
			}
		}
		params.put("keterangan", keterangan);
		params.put("show", show);
		params.put("lsError", lsError);
		params.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
		params.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
		params.put("hh", hh);
		params.put("mm", mm);
		return new ModelAndView("uw/editTglTrmKrmAdmin", params);
	}
}