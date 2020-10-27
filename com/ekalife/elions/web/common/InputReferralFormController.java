/**@author  : Ferry Harlim
 * @created : Dec 22, 2006 
 */
package com.ekalife.elions.web.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import com.ekalife.elions.model.Bfa;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class InputReferralFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Command command=(Command)cmd;
		List lsReferrer,lsReferrerBii;
		List lsLead;
		List lsBfa=new ArrayList();
		List lsReferrerLeader;
		String lsReferrerLeader1, lsReferrerLeader2, lsReferrerLeader3;
		List<Map> lsreferrer_id;
		Nasabah nasabah=command.getNasabah();
		if(nasabah == null){
			lsLead=elionsManager.selectLstJnLead(null);
			Map map=new HashMap();
			//dibuat Ferry : untuk menampilkan cabang dengan kode_region =2 (wilayah jakarta)
			//map.put("lsCabBii",elionsManager.selectLstCabBii("02"));
			
			//dibuat Deddy : untuk menampilkan semua cabang (Request dari Kuseno)
			map.put("lsCabBii", elionsManager.selectLstCabBiiAll());
			
			map.put("lsLead", lsLead);
			lsReferrerBii=elionsManager.selectLstReferrerBii(null);
			lsReferrer=elionsManager.selectMstBfaLevel(4);
			lsBfa=elionsManager.selectMstBfaKode(nasabah.getKode());
			map.put("lsBfa", lsBfa);
			map.put("lsReferrer", lsReferrer);
			map.put("lsReferrerBii", lsReferrerBii);
			return map;
		}
		if(nasabah.getLjl_jenis()==null)
			nasabah.setLjl_jenis(0);
		
		
//		if(nasabah.getPlatinum()==1)
//			lsLead=elionsManager.selectLstJnLead(null);
//		else if(nasabah.getPlatinum()==3)
//			lsLead=elionsManager.selectLstJnLead("4,5");
//		else
			lsLead=elionsManager.selectLstJnLead(null);
		Map map=new HashMap();
		//map.put("lsCabBii",elionsManager.selectLstCabBii("02"));
		map.put("lsCabBii", elionsManager.selectLstCabBiiAll());
		map.put("lsLead", lsLead);
		if(nasabah.getLjl_jenis()>=5){
			Integer levelId=elionsManager.selectLstJnLeadLevelId(nasabah.getLjl_jenis());
			if(levelId!=null && levelId !=0)
				lsReferrerBii=uwManager.selectLstReferrerBiiWithId(null);
			else
				lsReferrerBii=uwManager.selectLstReferrerBiiWithId(null);
		}else
			lsReferrerBii=uwManager.selectLstReferrerBiiWithId(null);
		lsReferrer=elionsManager.selectMstBfaLevel(4);
//		nasabah.setCabang_detail(elionsManager.selectCabangBii(nasabah.getKode()));
//		nasabah.setBfa_detail(elionsManager.selectNamaBfa(nasabah.getMsag_id(),nasabah.getKode()));
		
		//if(command.getNasabah().getPlatinum()== null || command.getNasabah().getPlatinum()<6 ){
			lsBfa=elionsManager.selectMstBfaKode(nasabah.getKode());
		//}
		
		//ini validasi untuk mendapatkan tingkatannya yakni leader1, leader2, dan leader3
			if(nasabah.getReferrer_id()!=null || nasabah.getReferrer_fa()!=null){
				if(nasabah.getReferrer_id()!=null){
					lsreferrer_id = uwManager.selectLstReffererLeader(nasabah.getReferrer_id());
					if(lsreferrer_id.size()!=0){
						String referrer1 = (String) lsreferrer_id.get(0).get("LEADER1");
						String referrer2 = (String) lsreferrer_id.get(0).get("LEADER2");
						String referrer3 = (String) lsreferrer_id.get(0).get("LEADER3");
						lsReferrerLeader1 = uwManager.selectRefLeader(referrer1);
						lsReferrerLeader2 = uwManager.selectRefLeader(referrer2);
						lsReferrerLeader3 = uwManager.selectRefLeader(referrer3);
						nasabah.setLeader_ref1(lsReferrerLeader1);
						nasabah.setLeader_ref2(lsReferrerLeader2);
						nasabah.setLeader_ref3(lsReferrerLeader3);
						map.put("lsReferrerLeader1", uwManager.selectLstReferrerBiiWithId(null));
					}
				}else {
					lsreferrer_id = uwManager.selectLstReffererLeader(nasabah.getReferrer_fa());
					if(lsreferrer_id.size()!=0){
						String referrer1 = (String) lsreferrer_id.get(0).get("LEADER1");
						String referrer2 = (String) lsreferrer_id.get(0).get("LEADER2");
						String referrer3 = (String) lsreferrer_id.get(0).get("LEADER3");
						lsReferrerLeader1 = uwManager.selectRefLeader(referrer1);
						lsReferrerLeader2 = uwManager.selectRefLeader(referrer2);
						lsReferrerLeader3 = uwManager.selectRefLeader(referrer3);
						map.put("lsReferrerLeader1", uwManager.selectLstReferrerBiiWithId(null));
				}
				
				
				}
				map.put("lsreferrer_id", lsreferrer_id.size());
			}else map.put("lsreferrer_id", 0);
			
		map.put("lsBfa", lsBfa);
		map.put("lsReferrer", lsReferrer);
		map.put("lsReferrerBii", lsReferrerBii);

		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		Integer p=ServletRequestUtils.getIntParameter(request, "p",0);
		Nasabah nasabah=new Nasabah();
		nasabah.setPlatinum(p);
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		
		if(flag.equals("0")){//tampil atau edit 
			String kdNasabah=null,noReferral=null;
			if(! nomor.equals("")){
				int pos=nomor.indexOf('~');
				noReferral=nomor.substring(0,pos);
				kdNasabah=nomor.substring(pos+1,nomor.length());
			}
			
			nasabah=elionsManager.selectMstNasabah(kdNasabah);
			if(nasabah == null){
				nasabah = new Nasabah();
				if(nasabah.getMns_ok_saran()==null){
					nasabah.setMns_ok_saran(0);
				}
				command.setNasabah(nasabah);
				return command;
			}
			nasabah.setPlatinum(0);
			nasabah.setCabang_detail(elionsManager.selectCabangBii(nasabah.getKode()));
			nasabah.setBfa_detail(elionsManager.selectNamaBfa(nasabah.getMsag_id(),nasabah.getKode()));
			nasabah.setFlag(1); //untuk menampilkan data Informasi Pemberi lead 
			List list=elionsManager.selectMstBfa(nasabah.getMsag_id(),nasabah.getKode());
			if(list.isEmpty()==false){
				Bfa bfa1=(Bfa)list.get(0);
				nasabah.setNama_bfa(bfa1.getNama_bfa());
			}	
			String kdRef=props.getProperty("kode.referral");
			String ljlJenis=f2.format(nasabah.getLjl_jenis());
			if( nasabah.getLjl_jenis().compareTo(2)==0){
				nasabah.setNamaLead("Nama Agen");

			}else if(kdRef.indexOf(ljlJenis)>=0){
				nasabah.setNamaLead("Nama Referrer");
			}else{ 
				nasabah.setNamaLead("Nama");
			}
			if(nasabah.getMns_ok_saran()==null){
				nasabah.setMns_ok_saran(0);
			}
			command.setNasabah(nasabah);
			
		}else if(flag.equals("1")){//add rererral
			nasabah=new Nasabah();
			nasabah.setMns_no_ref(ServletRequestUtils.getStringParameter(request, "noRef"));
			if(elionsManager.selectCountMstNasabahMnsNoRef(nasabah.getMns_no_ref())>0){
				command.setError(1);
				nasabah.setMns_kd_nasabah(nomor);
			}
			nasabah.setMns_ok_saran(0);
			nasabah.setMns_tgl_app(elionsManager.selectSysdate());
			nasabah.setS_mns_tgl_app(FormatDate.toString(nasabah.getMns_tgl_app()));
			nasabah.setPlatinum(p);
			command.setNasabah(nasabah);
			
		}
		
		return command;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		
		String proses=ServletRequestUtils.getStringParameter(request, "proses","0");
		Nasabah nasabah=command.getNasabah();
		Nasabah nTemp=elionsManager.selectMstNasabah(nasabah.getMns_kd_nasabah());
		if(nTemp==null)
			command.setI(1);
		else 
			command.setI(0);
		
		//jn_nasabah 6-10 BFA tidak ada.

//		if(elionsManager.selectMstNasabah(nasabah.getMns_kd_nasabah())==null){
//			err.reject("","Lakukan Proses Save terlebih Dahulu");
//		}
		
		if(proses.equals("kode")){
			String kodePla=nasabah.getKode().substring(0,6);
			
			if(command.getNasabah().getPlatinum() == null) command.getNasabah().setPlatinum(0);
			
			if(command.getNasabah().getPlatinum()==1 && (!(kodePla.equals("020701")) ) ){
				err.reject("","Cabang yang anda pilih salah, ganti dengan cabang platinum, karena Anda memilih referral platinum");
					
			}else{
				List lsBfa=elionsManager.selectMstBfaKode(nasabah.getKode());
				command.setLsBfa(lsBfa);
				if(lsBfa.isEmpty()==false){
					Bfa bfa=(Bfa)lsBfa.get(0);
					nasabah.setMsag_id(bfa.getMsag_id());
					nasabah.setKd_cabang(bfa.getKd_cabang());
					nasabah.setKd_area(bfa.getKd_area());
					nasabah.setKd_region(bfa.getKd_region());
					nasabah.setKd_koord(bfa.getKd_koord());
					nasabah.setNama_bfa(bfa.getNama_bfa());
					nasabah.setKd_leader(bfa.getLeader());
					nasabah.setKd_asm(bfa.getLeader());
					//
					Bfa bfaParam=new Bfa();
					bfaParam.setLevel_id(2);
					bfaParam.setStatus_aktif(1);
					bfaParam.setKd_region(bfa.getKd_region());
					bfaParam.setKd_koord(bfa.getKd_koord());
					nasabah.setKd_sm(elionsManager.selectMstBfaMsagId(bfaParam));
					//
					List list2=elionsManager.selectMstBfa(bfa.getLeader(),bfa.getKode());
					if(list2.isEmpty()==false){
						Bfa bfa2=(Bfa)list2.get(0);
						if(bfa2!=null){
							nasabah.setKd_leader(bfa2.getLeader());
						}
					}
					nasabah.setBfa_detail(elionsManager.selectNamaBfa(bfa.getMsag_id(),nasabah.getKode()));	
				}else{
					nasabah.setMsag_id(null);
					nasabah.setBfa_detail(elionsManager.selectNamaBfa(null,nasabah.getKode()));
				}
			}
			nasabah.setCabang_detail(elionsManager.selectCabangBii(nasabah.getKode()));
			err.reject("","Telah dilakukan Perubahan Cabang");
		}else if(proses.equals("lead")){
			String kdRef=props.getProperty("kode.referral");
			String ljlJenis=f2.format(nasabah.getLjl_jenis());
			if( nasabah.getLjl_jenis().compareTo(2)==0){
				nasabah.setNamaLead("Nama Agen");
				nasabah.setFlag(1);
			}else if(kdRef.indexOf(ljlJenis)>=0){
				nasabah.setNamaLead("Nama Referrer");
				nasabah.setFlag(1);
			}else {
				nasabah.setFlag(1);
				nasabah.setNamaLead("Nama");
				
			}
			err.reject("","Telah dilakukan Perubahan Lead");
		}else if(proses.equals("agen")){
			Map map=elionsManager.selectBfaNCabang(nasabah.getReferrer_fa());
			if(map!=null){
				nasabah.setMns_reff_cab((String)map.get("NAMA_CABANG"));
				nasabah.setMns_nama_lead((String)map.get("NAMA_BFA"));
			}	
			err.reject("","Telah dilakukan Perubahan agen");
		}else if(proses.equals("referrer")){
			Map map=elionsManager.selectLstReffBiiNCabang(nasabah.getReferrer_id());
			if(map!=null){
				nasabah.setMns_reff_cab((String)map.get("NAMA_CABANG"));
				nasabah.setMns_nama_lead((String)map.get("NAMA_REF"));
			}
			err.reject("","Telah dilakukan Perubahan referrer");
			List<Map> lsreferrer_id = uwManager.selectLstReffererLeader(nasabah.getReferrer_id());
			if(lsreferrer_id.size()!=0){
				nasabah.setLeader_ref1((String) lsreferrer_id.get(0).get("LEADER1"));
				nasabah.setLeader_ref2((String) lsreferrer_id.get(0).get("LEADER2"));
				nasabah.setLeader_ref3((String) lsreferrer_id.get(0).get("LEADER3"));
			}
			
			
		}else if(proses.equals("bfa")){
				List lsBfa=elionsManager.selectMstBfa(nasabah.getMsag_id(), nasabah.getKode());
				Bfa bfa=(Bfa)lsBfa.get(0);
				nasabah.setMsag_id(bfa.getMsag_id());
				nasabah.setKd_cabang(bfa.getKd_cabang());
				nasabah.setKd_area(bfa.getKd_area());
				nasabah.setKd_region(bfa.getKd_region());
				nasabah.setKd_koord(bfa.getKd_koord());
				nasabah.setNama_bfa(bfa.getNama_bfa());
				nasabah.setKd_leader(bfa.getLeader());
				nasabah.setKd_asm(bfa.getLeader());
				//
				Bfa bfaParam=new Bfa();
				bfaParam.setLevel_id(2);
				bfaParam.setStatus_aktif(1);
				bfaParam.setKd_region(bfa.getKd_region());
				bfaParam.setKd_koord(bfa.getKd_koord());
				nasabah.setKd_sm(elionsManager.selectMstBfaMsagId(bfaParam));
				//
				List list2=elionsManager.selectMstBfa(bfa.getLeader(),bfa.getKode());
				if(list2.isEmpty()==false){
					Bfa bfa2=(Bfa)list2.get(0);
					if(bfa2!=null){
						nasabah.setKd_leader(bfa2.getLeader());
					}
				}
				nasabah.setBfa_detail(elionsManager.selectNamaBfa(bfa.getMsag_id(),nasabah.getKode()));	
				err.reject("","Telah dilakukan Perubahan BFA");
		}
		if(nasabah.getMns_ok_saran()==null){
			nasabah.setMns_ok_saran(0);
		}
		command.setNasabah(nasabah);
		//harus ada data BFA nya 
//		if(nasabah.getMsag_id()==null || nasabah.getMsag_id().equals(""))
//			err.reject("","Tidak Bisa Input Referral, Data BFA Tidak Ada");
		if(nasabah.getMns_nama()==null || nasabah.getMns_nama().equals(""))
			err.reject("","Nama Referral Tidak Boleh Kosong");
		//Informasi Pemberi Lead harus lengkap
		if(nasabah.getLjl_jenis().compareTo(0)==0)
			err.reject("","Silahkan Pilih Sumber LEAD");
		if(nasabah.getNamaLead()==null)
			err.reject("","Silahkan Lengkapi Informasi Pemberi Lead");
		else if(nasabah.getNamaLead().equals("Nama Agen")){
			if(nasabah.getReferrer_fa()==null || nasabah.getReferrer_fa().equals(""))
				err.reject("","Nama Agen tidak boleh kosong");
		}else if(nasabah.getNamaLead().equals("Nama Referrer")){
			if(nasabah.getReferrer_id()==null || nasabah.getReferrer_id().equals(""))
				err.reject("","Nama Referrer Tidak Boleh Kosong");
		}else if(nasabah.getNamaLead().equals("Nama")){
			if(nasabah.getMns_nama_lead()==null){
				err.reject("","Nama Lead Tidak Boleh Kosong");
			}
		} 
		if(nasabah.getJn_lead()==null || nasabah.getJn_lead().equals(""))
			err.reject("", "Pilih salah satu Jenis Lead");
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		command.getNasabah().setLus_id(currentUser.getLus_id());
		if(request.getParameter("btnsave")!=null){
			if(command.getNasabah().getMns_no_ref().equals("NEW")){
				Integer autoNoRef = elionsManager.selectCountReferralNumber();
				String formatNoRef = autoNoRef.toString()+"L";
				formatNoRef = FormatString.rpad("0", formatNoRef, 6);
				command.getNasabah().setMns_no_ref(formatNoRef);
			}
			elionsManager.prosesInputReferral(command.getNasabah());
		}else{
			command.setFlagAdd(1);
			elionsManager.prosesTrans(command.getNasabah());
		}
		
		return new ModelAndView("common/input_referral",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
}
