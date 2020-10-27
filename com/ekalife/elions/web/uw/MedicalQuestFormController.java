package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class MedicalQuestFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor); 
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd =new Command();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cmd.setCurrentUser(currentUser);
		cmd.setReg_spaj(ServletRequestUtils.getStringParameter(request, "spaj",""));
		cmd.setLsMedQuests(new ArrayList<MedQuest>());
		for(int a=0;a<2;a++) {
			cmd.getLsMedQuests().add(new MedQuest());
			cmd.getLsMedQuests().get(a).setReg_spaj(cmd.getReg_spaj());
			cmd.getLsMedQuests().get(a).setMste_insured_no(a);
			cmd.getLsMedQuests().get(a).setMsadm_berat_berubah(0);
			cmd.getLsMedQuests().get(a).setMsadm_sehat(1);
			cmd.getLsMedQuests().get(a).setMsadm_penyakit(0);
			cmd.getLsMedQuests().get(a).setMsadm_family_sick(0);
			cmd.getLsMedQuests().get(a).setMsadm_operasi(0);
			cmd.getLsMedQuests().get(a).setMsadm_medis(0);
			cmd.getLsMedQuests().get(a).setMsadm_medis_alt(0);
//			cmd.getLsMedQuests().get(a).setHealth_checklist(uwManager.selectListHealth());
//			cmd.getLsMedQuests().get(a).setHealth_checklist(healthChecklist);
		}
		cmd.setShowTab(ServletRequestUtils.getIntParameter(request, "tab",1));
		
		Integer lsre_id = uwManager.selectPolicyRelation(cmd.getReg_spaj());
		cmd.setLsreIdPp(lsre_id);
		
		List<MedQuest> mq = uwManager.selectMedQuest(cmd.getReg_spaj(),null);
		for(int a=0;a<mq.size();a++) {
			cmd.getLsMedQuests().set(mq.get(a).getMste_insured_no(), mq.get(a));
			if(lsre_id==1){
				cmd.getLsMedQuests().set(1, mq.get(0));
//				cmd.getLsMedQuests().get(1).setMste_insured_no(1);
			}
//			cmd.getLsMedQuests().get(a).setHealth_checklist(uwManager.selectListHealth());
			
//			uwManager.selectHealthChecklist(cmd.getReg_spaj(), a);
		}
		
		
		return cmd;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException err) throws Exception {
		Command cmd=(Command) command;
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		cmd.setSubmitMode(submitMode);
		List<MedQuest> mq = cmd.getLsMedQuests(); 
		Double up = uwManager.selectmst_product_insuredUP(cmd.getReg_spaj());
		String lku_id = elionsManager.select_kurs(cmd.getReg_spaj());
		cmd.setMsl_premi(up/new Double(1000));
		//extra premi hamil (904)
		Product produkAdd = new Product();
		List lsProdukNew = new ArrayList();
		produkAdd.setReg_spaj(cmd.getReg_spaj());
		produkAdd.setMste_insured_no(new Integer(1));
		produkAdd.setLku_id(lku_id);
		produkAdd.setMspr_tsi(up);
		produkAdd.setLsbs_id(new Integer(904));
		produkAdd.setLsdbs_number(new Integer(1));
		produkAdd.setMspr_tsi_pa_a(new Double(0));
		produkAdd.setMspr_tsi_pa_b(new Double(0));
		produkAdd.setMspr_tsi_pa_c(new Double(0));
		produkAdd.setMspr_tsi_pa_d(new Double(0));
		produkAdd.setMspr_tsi_pa_m(new Double(0));
		produkAdd.setMspr_persen(new Integer(0));
		produkAdd.setMspr_premium(cmd.getMsl_premi());
		produkAdd.setMspr_discount(new Double(0));
		produkAdd.setMspr_active(new Integer(1));
		produkAdd.setMspr_extra(new Double(0));
		produkAdd.setMspr_rate(new Double(0));
		produkAdd.setMspr_tt(new Integer(0));
		produkAdd.setTotal(new Double(0));
		produkAdd.setTambah(new Integer(1));
		cmd.setProduct(produkAdd);
		Integer lsre_id = uwManager.selectPolicyRelation(cmd.getReg_spaj());
		cmd.setLsreIdPp(lsre_id);
		Pemegang pemegang = elionsManager.selectpp(cmd.getReg_spaj());
		Tertanggung tertanggung = elionsManager.selectttg(cmd.getReg_spaj());
		String lsbs_id = uwManager.selectLsbsId(cmd.getReg_spaj());
		
		if(cmd.getLsreIdPp()==1){
			cmd.getLsMedQuests().set(1, mq.get(0));
		}
		
		if(tertanggung.getLspd_id()!=1){
			err.rejectValue("lsMedQuests","","Polis Sudah ditransfer. Tidak dapat dilakukan pengeditan kembali.");
		}
		
		for(int a=0;a<mq.size();a++) {
			if(Common.isEmpty(mq.get(a).getMsadm_tinggi()) || Common.isEmpty(mq.get(a).getMsadm_berat())){
				String pelaku = "";
				if(cmd.getLsreIdPp()==1){
					pelaku = "Pemegang Polis/Tertanggung";
					if(a==0){
						err.rejectValue("lsMedQuests","","Silakan Masukkan Terlebih Dahulu Data Mengenai Tinggi Badan dan Berat Badan" +" "+ pelaku);
					}
				}else{
					if(a==0 && cmd.getShowTab()==1){
						err.rejectValue("lsMedQuests","","Silakan Masukkan Terlebih Dahulu Data Mengenai Tinggi Badan dan Berat Badan Pemegang Polis");
					}else if( a==1 && cmd.getShowTab()==2){
						err.rejectValue("lsMedQuests","","Silakan Masukkan Terlebih Dahulu Data Mengenai Tinggi Badan dan Berat Badan Tertanggung");
					}
					
				}
				
				
			}
			if(a==0){
				if(pemegang.getMspe_sex()==1){
					if(!Common.isEmpty(mq.get(a).getMsadm_pregnant()) || !Common.isEmpty(mq.get(a).getMsadm_abortion()) || !Common.isEmpty(mq.get(a).getMsadm_usg()) ){
						err.rejectValue("lsMedQuests","","Bagian Pemegang : Untuk Jenis Kelamin Laki-laki, tidak perlu mengisi Data Khusus Wanita!");
						mq.get(a).setMsadm_pregnant(null);	
						mq.get(a).setMsadm_abortion(null);
						mq.get(a).setMsadm_usg(null);
						mq.get(a).setMsadm_usg_mcu(null);
						mq.get(a).setMsadm_usg_mcu_std(null);
						mq.get(a).setMsadm_pregnant_time(null);
					}
				}else{
					if(Common.isEmpty(mq.get(a).getMsadm_pregnant()) || Common.isEmpty(mq.get(a).getMsadm_abortion()) || Common.isEmpty(mq.get(a).getMsadm_usg()) ){
						err.rejectValue("lsMedQuests","","Bagian Pemegang : Untuk Jenis Kelamin Perempuan, harap mengisi Data Khusus Wanita!");
					}
				}
			}else if(a==1){
				if(tertanggung.getMspe_sex()==1){
					if(!Common.isEmpty(mq.get(a).getMsadm_pregnant()) || !Common.isEmpty(mq.get(a).getMsadm_abortion()) || !Common.isEmpty(mq.get(a).getMsadm_usg()) ){
						err.rejectValue("lsMedQuests","","Bagian Tertanggung : Untuk Jenis Kelamin Laki-laki, tidak perlu mengisi Data Khusus Wanita!");
						mq.get(a).setMsadm_pregnant(null);	
						mq.get(a).setMsadm_abortion(null);
						mq.get(a).setMsadm_usg(null);
						mq.get(a).setMsadm_usg_mcu(null);
						mq.get(a).setMsadm_usg_mcu_std(null);
						mq.get(a).setMsadm_pregnant_time(null);
					}
				}else{
					if(Common.isEmpty(mq.get(a).getMsadm_pregnant()) || Common.isEmpty(mq.get(a).getMsadm_abortion()) || Common.isEmpty(mq.get(a).getMsadm_usg()) ){
						err.rejectValue("lsMedQuests","","Bagian Tertanggung : Untuk Jenis Kelamin Perempuan, harap mengisi Data Khusus Wanita!");
					}
				}
			}
			
			if(!err.hasErrors()){
				int b;
				if(cmd.getSubmitMode().equals("simpanPp")){
					b=0;
				}else{
					b=1;
				}
				BigDecimal height =  new BigDecimal(mq.get(b).getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
				BigDecimal bmi = new BigDecimal(mq.get(b).getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
				mq.get(b).setMsadm_bmi(bmi.doubleValue());
				Map Em = uwManager.selectEmBmi(bmi.doubleValue(),tertanggung.getMste_age());
				mq.get(b).setMsadm_em(Em);
				mq.get(b).setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
				//pernah pengecekan medis maupun alternatif, hamil, abortion;
				//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
				//tidak sehat && sakit tidak standar
				//terkena penyakit && bukan penyakit standar
				//Jika wanita, pernah MCU, dan hasilnya tidak baik
				//Jika wanita, 
				
				Integer mspe_sex = 0;
				if(mq.get(b).getMsadm_pregnant()==null) mspe_sex = 1;
				if(mspe_sex==0){
					if( mq.get(b).getMsadm_medis()==1 || mq.get(b).getMsadm_medis_alt()==1 || 
							mq.get(b).getMsadm_pregnant()==1  || (mq.get(b).getMsadm_usg()==1 && mq.get(b).getMsadm_usg_mcu()==1 && mq.get(b).getMsadm_usg_mcu_std()==0 ) ||
							(mq.get(b).getMsadm_usg()==1 && mq.get(b).getMsadm_usg_mcu()==0) ||
							mq.get(b).getMsadm_abortion()==1 || mq.get(b).getMsadm_em_life()>50 || (mq.get(b).getMsadm_sehat()==0 && mq.get(b).getMsadm_sehat_std()==0) ||
							(mq.get(b).getMsadm_penyakit()==1 && mq.get(b).getMsadm_penyakit_std()==0)
							
						){
							mq.get(b).setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
						}else{
							mq.get(b).setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
						}
				}else{
					if( mq.get(b).getMsadm_medis()==1 || mq.get(b).getMsadm_medis_alt()==1 || 
						mq.get(b).getMsadm_em_life()>50 || (mq.get(b).getMsadm_sehat()==0 && mq.get(b).getMsadm_sehat_std()==0) ||
						(mq.get(b).getMsadm_penyakit()==1 && mq.get(b).getMsadm_penyakit_std()==0)
					){
						mq.get(b).setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						mq.get(b).setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
				}
				if(lsbs_id.equals("178")){
					mq.get(b).setMsadm_clear_case(1);
				}else if(lsbs_id.equals("208")){
					mq.get(b).setMsadm_clear_case(1);
				}
			}
			
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		Command cmd = (Command) command;
		Map map = new HashMap();
		Integer mspe_sex = 0;
//		map = uwManager.prosesSavingMedQuest(cmd); 
		if(cmd.getLsreIdPp()==1){
				cmd.getLsMedQuests().set(1, cmd.getLsMedQuests().get(0));
				cmd.getLsMedQuests().get(0).setMste_insured_no(0);
				cmd.getLsMedQuests().get(0).setReg_spaj(cmd.getReg_spaj());
				cmd.getLsMedQuests().get(1).setReg_spaj(cmd.getReg_spaj());
				map.put("pesan", uwManager.insertMedQuest(cmd.getLsMedQuests().get(0)));
				cmd.getLsMedQuests().get(1).setMste_insured_no(1);
				map.put("pesan", uwManager.insertMedQuest(cmd.getLsMedQuests().get(1)));
				
				if(cmd.getLsMedQuests().get(0).getMsadm_pregnant()==null) mspe_sex = 1;
				if(mspe_sex==0){
					if(cmd.getLsMedQuests().get(0).getMsadm_pregnant()==1){
						if(cmd.getLsMedQuests().get(0).getMsadm_pregnant_time()<=7){
							Product product_sebelumnya = uwManager.selectMstProductInsuredDetail(cmd.getReg_spaj(), cmd.getProduct().getLsbs_id(), cmd.getProduct().getLsdbs_number());
							if(product_sebelumnya!=null){
								//jika hamil <=7 bulan, dikenakan extra premi hamil
								elionsManager.insertMstProductInsured(cmd.getProduct());
								map.put("pesan", "Pernyataan Medis telah diperbaharui.Dikenakan Extra Premi Kehamilan");
							}
						}else{
							//jika hamil di atas 7 bulan, postponed
							String prosesPostponed = uwManager.prosesPostPonedMallAss(cmd.getReg_spaj(), cmd.getCurrentUser());
							map.put("pesan", "Polis ditunda 40 hari/ Postponed dikarenakan sedang hamil >7 bulan");
						}
					}
				}
		}else{
			if(cmd.getSubmitMode().equals("simpanPp")) {
				map.put("tab", 1);
				cmd.getLsMedQuests().get(0).setReg_spaj(cmd.getReg_spaj());
				map.put("pesan", uwManager.insertMedQuest(cmd.getLsMedQuests().get(0)));
			} 
			else if(cmd.getSubmitMode().equals("simpanTtg")) {
				map.put("tab", 2);
				cmd.getLsMedQuests().get(1).setReg_spaj(cmd.getReg_spaj());
				map.put("pesan", uwManager.insertMedQuest(cmd.getLsMedQuests().get(1)));
				if(cmd.getLsMedQuests().get(1).getMsadm_pregnant()==null) mspe_sex = 1;
				if(mspe_sex==0){
					if(cmd.getLsMedQuests().get(1).getMsadm_pregnant()==1){
						if(cmd.getLsMedQuests().get(1).getMsadm_pregnant_time()<=7){
							Product product_sebelumnya = uwManager.selectMstProductInsuredDetail(cmd.getReg_spaj(), cmd.getProduct().getLsbs_id(), cmd.getProduct().getLsdbs_number());
							if(product_sebelumnya==null){
								//jika hamil <=7 bulan, dikenakan extra premi hamil
								
								elionsManager.insertMstProductInsured(cmd.getProduct());
								map.put("pesan", "Pernyataan Medis telah diperbaharui.Dikenakan Extra Premi Kehamilan");
							}
						}else{
							//jika hamil di atas 7 bulan, postponed
							String prosesPostponed = uwManager.prosesPostPonedMallAss(cmd.getReg_spaj(), cmd.getCurrentUser());
							map.put("pesan", "Polis ditunda 40 hari/ Postponed dikarenakan sedang hamil >7 bulan");
						}
					}
				}
			}
		}
		
		map.put("spaj", cmd.getReg_spaj());
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/medical_quest.htm")).addAllObjects(map);
	}
	
}
