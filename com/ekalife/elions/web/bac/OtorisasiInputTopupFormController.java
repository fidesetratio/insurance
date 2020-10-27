package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.InputTopup;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk Input Topup di tempat untuk Bank Sinarmas dan Sinarmas Sekuritas
 * Khusus produk stable link
 * 
 * @author Yusuf
 * @since Jan 15, 2009 (1:40:06 PM)
 */
public class OtorisasiInputTopupFormController extends ParentFormController {
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String reg_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj", "");
		if(request.getParameter("pesan") != null) {
			//tarik datanya untuk refresh setelah melakukan save
			
			if(!reg_spaj.equals("")) {
				cmd.setReg_spaj(reg_spaj);
				List selectMslTuKeMstPositionSpajList = elionsManager.selectMslTuKeMstPositionSpajList(cmd.getReg_spaj());
				String mslTuKeList = null;
				String[] tempTuKe =  null;
				for( int i = 0 ; i < selectMslTuKeMstPositionSpajList.size() ; i ++ )
				{
					Map temp = (Map) selectMslTuKeMstPositionSpajList.get(i);
					Object mslTuKe = temp.get("MSL_TU_KE");
					if( mslTuKe != null && !"".equals( mslTuKe ) )
					{
						if( mslTuKeList == null )
						{
							mslTuKeList = mslTuKe.toString(); 
						}
						else
						{
							mslTuKeList = mslTuKeList + "," + mslTuKe.toString();
						}
					}
				}
				if( mslTuKeList != null && !"".equals( mslTuKeList ) )
				{
					tempTuKe = mslTuKeList.split(",");
				}
				cmd.setTrans(elionsManager.selectEntryTransStableLink(reg_spaj));
				cmd.setDaftarTopup(elionsManager.selectOtorisasiTopupStableLink(reg_spaj, 45, tempTuKe, currentUser.getLus_id(), null)); 
			}
		}else {
			cmd.setTrans(new InputTopup());
			cmd.setReg_spaj(reg_spaj);
		}
		
		cmd.setValid1(elionsManager.selectUserName(currentUser.getValid_bank_1()));
		cmd.setValid2(elionsManager.selectUserName(currentUser.getValid_bank_2()));
		
		
		return cmd;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map ref = new HashMap();
		Command cmd=(Command) command;
		
		List<DropDown> daftarRo = new ArrayList<DropDown>();
		daftarRo.add(new DropDown("1", "Rollover All"));
		daftarRo.add(new DropDown("2", "Rollover Premi"));
		daftarRo.add(new DropDown("3", "Autobreak"));
		ref.put("daftarRo", daftarRo);
		
		List<DropDown> daftarMti = new ArrayList<DropDown>();
		daftarMti.add(new DropDown("1", "1"));
		daftarMti.add(new DropDown("3", "3"));
		daftarMti.add(new DropDown("6", "6"));
		daftarMti.add(new DropDown("12", "12"));
		daftarMti.add(new DropDown("24", "24"));
		daftarMti.add(new DropDown("36", "36"));
		ref.put("daftarMti", daftarMti);
		
		List detail = uwManager.selectDetailBisnis(cmd.getReg_spaj());
		if(!detail.isEmpty()){
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				ref.put("nama_produk", (String) det.get("LSDBS_NAME"));
			}
		}
		
		return ref;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command cmd = (Command) command;
		
		//tampilkan data spaj
		if(request.getParameter("show") != null) {
			
			//reset values dulu
			cmd.setTrans(new InputTopup());
			cmd.setDaftarTopup(null);
			int jn_bank = currentUser.getJn_bank().intValue();
			//validasi harus masukkan nomor polis/spaj
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reg_spaj", "", "Harap masukkan No. Polis / Register SPAJ");
			
			int size = cmd.getReg_spaj().trim().replace(".", "").length();
			//validasi harus masukkan nomor yang benar (cek ke db)
			if(!errors.hasErrors() && size != 11 && size != 14) {
				errors.reject("","Harap masukkan No. Polis / Register SPAJ yang benar");
			}
			
			if(!errors.hasErrors()) {
				String tmp = cmd.getReg_spaj().trim().replace(".", "");
				if(tmp.length() == 14) cmd.setReg_spaj(elionsManager.selectSpajFromPolis(tmp));
				else if(tmp.length() == 11) cmd.setReg_spaj(tmp);
				else errors.rejectValue("reg_spaj", "", "Harap masukkan No. Polis / Register SPAJ yang benar");
				
				if(cmd.getReg_spaj() == null){
					errors.rejectValue("reg_spaj", "", "No. Polis / Register SPAJ tidak dikenali. Harap cek ulang nomor yang dimasukkan.");
				}
			}
			
			if(!errors.hasErrors() && (jn_bank!=2 && jn_bank!=-1)) {
				errors.reject("", "Menu ini hanya dapat digunakan oleh user Bank Sinarmas.");
			}
			
			//validasi khusus bank sinarmas, harus punya akses terhadap suatu polis
			if(!errors.hasErrors()) {
				if(elionsManager.selectIsUserYangInputBank(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("SSS")) {
					if(currentUser.getJn_bank().intValue() == 2){
						errors.reject("","Anda tidak mempunyai akses terhadap Polis ini");
					}
				}
			}
			
			//validasi tidak boleh ada di pinjaman (alias udah cair)
			if(!errors.hasErrors()) {
				int hitung = elionsManager.selectValidasiPinjaman(cmd.getReg_spaj());
				if(hitung > 0) {
					errors.rejectValue("reg_spaj", "", "Polis yang Anda masukkan sudah melakukan pencairan. Harap dikonfirmasi dengan dept LIFE BENEFIT melalui perwakilan BANCASSURANCE kami.");
				}
			}
			
			//** bila tembus semua validasi, baru tarik datanya **//
			if(!errors.hasErrors()) {
				List selectMslTuKeMstPositionSpajList = elionsManager.selectMslTuKeMstPositionSpajList(cmd.getReg_spaj());
				String mslTuKeList = null;
				String[] tempTuKe =  null;
				for( int i = 0 ; i < selectMslTuKeMstPositionSpajList.size() ; i ++ )
				{
					Map temp = (Map) selectMslTuKeMstPositionSpajList.get(i);
					Object mslTuKe = temp.get("MSL_TU_KE");
					if( mslTuKe != null && !"".equals( mslTuKe ) )
					{
						if( mslTuKeList == null )
						{
							mslTuKeList = mslTuKe.toString(); 
						}
						else
						{
							mslTuKeList = mslTuKeList + ","+ mslTuKe.toString();
						}
					}
				}
				if( mslTuKeList != null && !"".equals( mslTuKeList ) )
				{
					tempTuKe = mslTuKeList.split(",");
				}
				cmd.setTrans(elionsManager.selectEntryTransStableLink(cmd.getReg_spaj()));
				cmd.setDaftarTopup(elionsManager.selectOtorisasiTopupStableLink(cmd.getReg_spaj(), 45, tempTuKe, currentUser.getLus_id(), null)); //45 dulu, gitu ditrans, baru jadi 73
				List<InputTopup> daftarTopup = cmd.getDaftarTopup();
				if( cmd.getDaftarTopup() != null && cmd.getDaftarTopup().size() > 0 )
				{
					errors.reject("", "Silahkan lanjutkan pengotorisasian.");
				}
				else
				{
					errors.reject("", "Tidak ada Topup untuk diotorisasi.");
				}
			}
		}
		//simpan data
		else if(cmd.getTrans().getSimpan_lji_id() != null && cmd.getTrans().getSimpan_mode() != null && cmd.getTrans().getSimpan_msl_no() != null) {
			InputTopup trans = cmd.getTrans();
						
			for(int i=0; i<cmd.getDaftarTopup().size(); i++) {
				errors.setNestedPath("daftarTopup["+i+"]");
				InputTopup tmp = (InputTopup) cmd.getDaftarTopup().get(i);
				//cari dulu yg mana yg mau di insert/update/delete, itu yg divalidasi
				if(trans.reg_spaj.equals(tmp.reg_spaj) && trans.simpan_lji_id.equals(tmp.lji_id) && trans.simpan_msl_no.equals(tmp.msl_no)) {
					
					//validasi untuk semua jenis edit
					if(tmp.getMsl_posisi() != null){
						if(tmp.getMsl_posisi().intValue() != 45){
							errors.rejectValue("msl_posisi","", "Transaksi Topup Ke-" + tmp.getMsl_tu_ke() + " tidak dapat dirubah/dibatalkan karena sudah diproses. Harap konfirmasi dengan perwakilan BANCASSURANCE kami.");
						}
					}
				}
			}
			errors.setNestedPath("");
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command cmd = (Command) command;
		InputTopup trans = cmd.getTrans();
		//save
//		String pesan = elionsManager.saveTopupStableLink(cmd.getTrans(), cmd.getDaftarTopup(), currentUser);
		String pesan = "";
		if(trans.simpan_mode.equals("otorisasi")) {
			Integer mslNo = trans.simpan_msl_no;			
			String ljId = trans.simpan_lji_id;
			List<InputTopup> spajNotOtorisasiInfo = elionsManager.selectOtorisasiTopupStableLink(trans.getReg_spaj(), 45, null, currentUser.getLus_id(), mslNo);
			Integer mslTuKe = null;
			Integer flagHakOtorisasi = 1;
			String differ = "";
			if( spajNotOtorisasiInfo != null && spajNotOtorisasiInfo.size() > 0 )
			{
				mslTuKe =  spajNotOtorisasiInfo.get(0).getMsl_tu_ke();
				if( spajNotOtorisasiInfo.get(0).getValid_bank_1() != null && !"".equals( spajNotOtorisasiInfo.get(0).getValid_bank_1() ) )
				{
					if(currentUser.getLus_id().equals(spajNotOtorisasiInfo.get(0).getValid_bank_1().toString()))
					{
						if( spajNotOtorisasiInfo.get(0).getLku_id() != null && "01".equals(spajNotOtorisasiInfo.get(0).getLku_id()) )
						{
							if(spajNotOtorisasiInfo.get(0).getMsl_premi() != null && spajNotOtorisasiInfo.get(0).getMsl_premi()>2000000000.0)
							{
								flagHakOtorisasi = 0;
								differ = "rupiah";
							}
						}
						else if( spajNotOtorisasiInfo.get(0).getLku_id() != null && "02".equals(spajNotOtorisasiInfo.get(0).getLku_id()) )
						{
							if(spajNotOtorisasiInfo.get(0).getMsl_premi() != null && spajNotOtorisasiInfo.get(0).getMsl_premi()>200000.0)
							{
								flagHakOtorisasi = 0;
								differ = "dollar";
							}
						}
					}
				}
			}
			if( flagHakOtorisasi == 0 )
			{
				if("rupiah".equals(differ))
				{
					pesan = "Maaf, Anda tidak mempunyai hak Otorisasi untuk nominal di atas Rp.2000.000.000,00";
				}
				else if("dollar".equals(differ))
				{
					pesan = "Maaf, Anda tidak mempunyai hak Otorisasi untuk nominal di atas $200.000";
				}
			}
			else
			{
				pesan = elionsManager.otorisasiTopUpStabilLink(mslTuKe,ljId, currentUser.getLus_id(), trans.getReg_spaj());
			}
		}
		return new ModelAndView(new RedirectView("otorisasi_topup.htm")).addObject("pesan", pesan).addObject("reg_spaj", cmd.getReg_spaj());
	}
	
}