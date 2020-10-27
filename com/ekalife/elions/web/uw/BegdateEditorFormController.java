package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Begdate;
import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Production;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class BegdateEditorFormController extends ParentFormController{
	
	//Fungsi ini untuk mengubah bentuk angka baik type data double, integer, dan date. Misal: hasil keluarnya 12.234AF menjadi 12.234.567
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map = new HashMap();
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Begdate command = new Begdate();
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Begdate command = (Begdate) cmd;
		PolicyInfoVO dataSpaj = new PolicyInfoVO();
		String s_spaj = command.getReg_spaj();
		Integer i_tanggal;
		Integer i_bulan;
		Integer i_tahun;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "reg_spaj", "", "No SPAJ harus diisi!");
		
		if(!s_spaj.equals("")){
			if(elionsManager.selectGetSpaj(s_spaj) == null){
				err.rejectValue("reg_spaj","","No SPAJ tidak terdaftar!");
			}else if(bacManager.selectNewBussiness(s_spaj) > 1){
				err.rejectValue("reg_spaj","","Beg Date tidak dapat diedit karena posisi Polis sudah tidak di New Bussiness!");
			}else{
				dataSpaj = elionsManager.selectPolicyInfoBySpaj(s_spaj);
            	String s_ulink = props.getProperty("product.unitLink");
            	String s_psave = props.getProperty("product.powerSave");
            	String s_lsbsid = dataSpaj.getLsbsId().toString();
            	
				//Cek apakah produk tersebut konvensional atau bukan
            	if(s_ulink.indexOf(s_lsbsid)>-1){
            		//err.rejectValue("pesan","","Untuk sementara tidak bisa melakukan Edit BegDate terhadap produk Unit Link!");
            	}else if(s_psave.indexOf(s_lsbsid)>-1){
            		err.rejectValue("pesan","","Edit BegDate terhadap produk Power Save tidak bisa dilakukan melalui Program ini!");
            	}
				
    			if(request.getParameter("btnCari") != null && !err.hasErrors()){
    				command.setPp_bdate(dataSpaj.getNamaPp());
    				command.setTt_bdate(dataSpaj.getNamaTt());
       				command.setProd_bdate(dataSpaj.getNamaProduk());
       				command.setOld_bdate(df.format(dataSpaj.getBegDate()));
       				command.setNew_bdate(df.format(dataSpaj.getBegDate()));
       				
    			}else if(request.getParameter("btnSave") != null){
    				//Validasi field yg tidak boleh kosong
    				ValidationUtils.rejectIfEmpty(err, "new_bdate", "", "Tgl BegDate Baru harus diisi!");
    				ValidationUtils.rejectIfEmpty(err, "ket_bdate", "", "Kolom Keterangan harus diisi!");
    				ValidationUtils.rejectIfEmpty(err, "no_helpdesk", "", "Kolom No. Helpdesk harus diisi!");
    				
    				if(command.getNew_bdate().equals(command.getOld_bdate())){
    					err.rejectValue("new_bdate","","Tgl BegDate Baru tidak boleh sama dengan Tgl BegDate Lama!");
    				}
    				
    				if(!err.hasErrors()){
    					try{
    						
//    						long selisihtgl = df.parse(command.getNew_bdate()).getTime() - df.parse(command.getOld_bdate()).getTime();
    						long l_selisihhari = ( df.parse(command.getNew_bdate()).getTime() - df.parse(command.getOld_bdate()).getTime() )/(1000*60*60*24);
    						Date d_newbegdate = df.parse(command.getNew_bdate());
    						
    						
    						//*** 1. Proses update tabel MST_POLICY
    						command.setPolicy(bacManager.selectMstPolicyAll(command.getReg_spaj()));
    						Integer i_carabayar = elionsManager.selectLstPayModeLscbTtlMonth(command.getPolicy().getLscb_id());
    						if(command.getPolicy().getMspo_beg_date() != null) command.getPolicy().setMspo_beg_date( d_newbegdate );
    						if(command.getPolicy().getMspo_end_date() != null) command.getPolicy().setMspo_end_date( converter(d_newbegdate, -1, i_carabayar, 0) );
    						if(command.getPolicy().getMspo_next_bill() != null) command.getPolicy().setMspo_next_bill( converter(d_newbegdate, 0, i_carabayar, 0) );
    						//*** End proses update tabel MST_POLICY
    						
    						//*** 2. Proses update tabel MST_INSURED
    						command.setInsured(bacManager.selectMstInsuredAll(command.getReg_spaj()));
    						if(command.getInsured().getMste_beg_date() != null) command.getInsured().setMste_beg_date( d_newbegdate );
    						if(command.getInsured().getMste_end_date() != null) command.getInsured().setMste_end_date( converter(d_newbegdate, -1, 0, command.getPolicy().getMspo_ins_period()) );
    						//*** End proses update tabel MST_INSURED
    						
    						//*** 3. Proses update tabel MST_PRODUCT_INSURED
    						command.setLsprodins(bacManager.selectMstProdInsAll(command.getReg_spaj()));
    						if(command.getLsprodins().size()>0){
    							for(int i=0;i<command.getLsprodins().size();i++){
    								if(command.getLsprodins().get(i).getMspr_beg_date() != null) command.getLsprodins().get(i).setMspr_beg_date( d_newbegdate );
    								if(command.getLsprodins().get(i).getMspr_end_date() != null) command.getLsprodins().get(i).setMspr_end_date( converter(d_newbegdate, -1, 0, command.getLsprodins().get(i).getMspr_ins_period()) );
    								if(command.getLsprodins().get(i).getMspr_end_pay() != null) command.getLsprodins().get(i).setMspr_end_pay( converter(d_newbegdate, 0, -1, command.getLsprodins().get(i).getMspr_ins_period()) );
    							}
    						}
    						//*** End proses update tabel MST_PRODUCT_INSURED
    						
    						//*** 4. Proses update tabel MST_PRODUCTION
    						command.setLsprod(bacManager.selectMstProductionAll(command.getReg_spaj()));
    						if(command.getLsprod().size()>0){
    							for(int i=0;i<command.getLsprod().size();i++){
    								if(command.getLsprod().get(i).getMspro_beg_date() != null) command.getLsprod().get(i).setMspro_beg_date( d_newbegdate );
    								if(command.getLsprod().get(i).getMspro_end_date() != null) command.getLsprod().get(i).setMspro_end_date( converter(d_newbegdate, -1, i_carabayar, 0) );
    							}
    						}
    						//*** End proses update tabel MST_PRODUCTION
    						
    						//*** 5. Proses update tabel MST_BILLING
    						command.setLsbilling(bacManager.selectMstBillingAll(command.getReg_spaj()));
    						if(command.getLsbilling().size()>0){
    							for(int i=0;i<command.getLsbilling().size();i++){
    								if(command.getLsbilling().get(i).getMsbi_beg_date() != null) command.getLsbilling().get(i).setMsbi_beg_date( d_newbegdate );
    								if(command.getLsbilling().get(i).getMsbi_end_date() != null) command.getLsbilling().get(i).setMsbi_end_date( converter(d_newbegdate, -1, i_carabayar, 0) );
    								if(command.getLsbilling().get(i).getMsbi_due_date() != null) command.getLsbilling().get(i).setMsbi_due_date( converter(d_newbegdate, 7, 0, 0) );
    							}
    						}
    						//*** End proses update tabel MST_BILLING
    						
    						//*** 6. Proses update tabel MST_PAS_SMS
    						HashMap mstpassms = bacManager.selectMstPasSMSAll(command.getReg_spaj());
							if(mstpassms != null){
	    						if(mstpassms.get("MSP_PAS_BEG_DATE") != null) command.setMsp_pas_beg_date( d_newbegdate );
								if(mstpassms.get("MSP_PAS_END_DATE") != null) command.setMsp_pas_end_date( command.getInsured().getMste_end_date() );
							}
    						//*** End proses update tabel MST_PAS_SMS
							
							//*** 7. Proses update tabel MST_ULINK
							command.setLsulink(bacManager.selectMstUlinkAll(command.getReg_spaj()));
    						if(command.getLsulink().size()>0){
    							for(int i=0;i<command.getLsulink().size();i++){
    								if(command.getLsulink().get(i).getMu_tgl_surat() != null) command.getLsulink().get(i).setMu_tgl_surat( converter(d_newbegdate, 0, 6, 0) );
    							}
    						}
							//*** End proses update tabel MST_ULINK
							
							//*** 8. Proses update tabel MST_TRANS_ULINK
//							command.setLstransulink(bacManager.selectMstTransUlinkAll(command.getReg_spaj()));
							//*** End proses update tabel MST_TRANS_ULINK
							
							//*** 9. Proses update tabel MST_DET_ULINK
							command.setLsdetulink(bacManager.selectMstDetUlinkAll(command.getReg_spaj()));
    						if(command.getLsdetulink().size()>0){
    							for(int i=0;i<command.getLsdetulink().size();i++){
    								if(command.getLsdetulink().get(i).getMdu_last_trans() != null) command.getLsdetulink().get(i).setMdu_last_trans( d_newbegdate );
    							}
    						}
							//*** End proses update tabel MST_DET_ULINK
							
							//*** 10. Proses update tabel MST_BIAYA_ULINK
							command.setLsbiayaulink(bacManager.selectMstBiayaUlinkAll(command.getReg_spaj()));
    						if(command.getLsbiayaulink().size()>0){
    							for(int i=0;i<command.getLsbiayaulink().size();i++){
    								if(command.getLsbiayaulink().get(i).getMbu_end_pay() != null) command.getLsbiayaulink().get(i).setMbu_end_pay( converter(d_newbegdate, 0, -1, command.getPolicy().getMspo_ins_period()) );
    							}
    						}
							//*** End proses update tabel MST_BIAYA_ULINK
							
							//*** 11. Proses update tabel MST_ULINK_BILL
							command.setLsulinkbill(bacManager.selectMstUlinkBillAll(command.getReg_spaj()));
							if(command.getLsulinkbill().size()>0){
    							for(int i=0;i<command.getLsulinkbill().size();i++){
    								if(command.getLsulinkbill().get(i).getNext_bill() != null) command.getLsulinkbill().get(i).setNext_bill( converter(d_newbegdate, 0, i_carabayar, 0) );
        						}
							}
							//*** End proses update tabel MST_ULINK_BILL
	    				}
	    				catch (Exception e) {
	    					err.rejectValue("pesan","","Terjadi kesalahan pada proses perubahan BegDate!");
	    					logger.error("ERROR :", e);
	    				}
    				}
    			}
			}
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Begdate command = (Begdate) cmd;
		
		if(request.getParameter("btnSave") != null){
			command = (Begdate) bacManager.editBegDateBySystem(command, currentUser.getLus_id());
			err.rejectValue("pesan", "", command.getPesan());
		}

		return new ModelAndView("uw/begdate_editor", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}
	
//	public Date converter(Date tgl, long selisih_tgl) throws ParseException{
//		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		
//		long tmp;
//		String tmp2;
//	    Date tgl_baru;
//	    
//	    tmp = df.parse(df.format(tgl)).getTime() + selisih_tgl;
//	    tmp2 = df.format(new Date(tmp));
//	    tgl_baru = df.parse(tmp2);
//	    
//	    return tgl_baru;
//	}

	public Date converter(Date d_begdate, Integer i_tambahhari, Integer i_tambahbulan, Integer i_tambahtahun){
		Calendar cal = Calendar.getInstance();
		Date d_tglhasil = null;
		
		
		Integer tanggal1 = (d_begdate.getDate());
		Integer bulan1 = (d_begdate.getMonth())+1;
		Integer tahun1 = (d_begdate.getYear())+1900;
		
		cal.set(tahun1, bulan1-1, tanggal1);
		cal.add(cal.DAY_OF_MONTH, i_tambahhari);
		cal.add(cal.MONTH, i_tambahbulan);
		cal.add(cal.YEAR, i_tambahtahun);
		
		Integer tanggal2 = (cal.getTime().getDate());
		Integer bulan2 = (cal.getTime().getMonth())+1;
		Integer tahun2 = (cal.getTime().getYear())+1900;
		
		try{
			d_tglhasil = defaultDateFormat.parse( FormatString.rpad("0",tanggal2.toString(),2)+"/"+FormatString.rpad("0",bulan2.toString(),2)+"/"+tahun2.toString() );
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		return d_tglhasil;
	}
}
