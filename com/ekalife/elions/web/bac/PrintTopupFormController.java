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

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.InputTopup;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk Input Topup di tempat untuk Bank Sinarmas dan Sinarmas Sekuritas
 * Khusus produk stable link
 * 
 * @author Yusuf
 * @since Jan 15, 2009 (1:40:06 PM)
 */
public class PrintTopupFormController extends ParentFormController {
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		cmd.setTrans(new InputTopup());
		cmd.setReg_spaj(ServletRequestUtils.getStringParameter(request, "spaj",""));
		
		return cmd;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map ref = new HashMap();
		Command cmd=(Command) command;
		
		List<DropDown> daftarRo = new ArrayList<DropDown>();
		String disabledEndorsemen = "";
		daftarRo.add(new DropDown("1", "Rollover All"));
		daftarRo.add(new DropDown("2", "Rollover Premi"));
		daftarRo.add(new DropDown("3", "Autobreak"));
		ref.put("daftarRo", daftarRo);
		String spaj = cmd.getReg_spaj();
		String lsbs_id = uwManager.selectBusinessId(cmd.getReg_spaj());
		
		if(Integer.parseInt(lsbs_id) ==164){
			Integer checkSpajInMstSLink = this.uwManager.selectCheckSpajInMstSLink( spaj );
			if( checkSpajInMstSLink != null && checkSpajInMstSLink > 0 )
			{
				Integer checkSpajInMstSLinkBasedFlagBulanan = this.uwManager.selectCheckSpajInMstSLinkBasedFlagBulanan( spaj );
				if( checkSpajInMstSLinkBasedFlagBulanan != null && checkSpajInMstSLinkBasedFlagBulanan > 0 )
				{
					Integer selectCheckSpajInLstUlangan = this.uwManager.selectCheckSpajInLstUlangan( spaj );
					if( selectCheckSpajInLstUlangan != null && selectCheckSpajInLstUlangan > 0 )
						{ disabledEndorsemen = "disabled"; }
					else
						{ disabledEndorsemen = ""; }
				}
				else
				{
					disabledEndorsemen = "disabled";
				}
			}
			else
			{
				disabledEndorsemen = "disabled";
			}
		}else{
//			Integer checkSpajInMstPsave = this.uwManager.selectCheckSpajInMstPsave( spaj );
//			if( checkSpajInMstPsave != null && checkSpajInMstPsave > 0 )
//			{
				//powersave new tidak ada manfaat bulanan jadi comment aja bagian ini
//				Integer checkSpajInMstPsaveBasedFlagBulanan = this.uwManager.selectCheckSpajInMstPsaveBasedFlagBulanan( spaj );
//				if( checkSpajInMstPsaveBasedFlagBulanan != null && checkSpajInMstPsaveBasedFlagBulanan > 0 )
//				{
//					Integer selectCheckSpajInLstUlangan = this.uwManager.selectCheckSpajInLstUlangan( spaj );
//					if( selectCheckSpajInLstUlangan != null && selectCheckSpajInLstUlangan > 0 )
//						{ disabledEndorsemen = "disabled"; }
//					else
//						{ disabledEndorsemen = ""; }
//				}
//				else
//				{
//					disabledEndorsemen = "disabled";
//				}
//			}
//			else
//			{
				disabledEndorsemen = "disabled";
//			}
		}
		
		List<DropDown> daftarMti = new ArrayList<DropDown>();
		daftarMti.add(new DropDown("1", "1"));
		daftarMti.add(new DropDown("3", "3"));
		daftarMti.add(new DropDown("6", "6"));
		daftarMti.add(new DropDown("12", "12"));
		daftarMti.add(new DropDown("24", "24"));
		daftarMti.add(new DropDown("36", "36"));
		ref.put("disabledEndorsemen", disabledEndorsemen);
		ref.put("daftarMti", daftarMti);
		List detail = uwManager.selectDetailBisnis(cmd.getReg_spaj());
		if(!detail.isEmpty()){
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				ref.put("nama_produk", (String) det.get("LSDBS_NAME"));
			}
		}
		
	
//		ref.put("printBabiRider",uwManager.selectPrintBabiRider(cmd.getReg_spaj()));
	
		ref.put("no_polis", elionsManager.selectPolicyNumberFromSpaj(spaj));
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
			
			//validasi harus masukkan nomor yang benar (cek ke db)
			if(!errors.hasErrors()) {
				String tmp = cmd.getReg_spaj().trim().replace(".", "");
				if(tmp.length() == 14) cmd.setReg_spaj(elionsManager.selectSpajFromPolis(tmp));
				else if(tmp.length() == 11) cmd.setReg_spaj(tmp);
				else errors.rejectValue("reg_spaj", "", "Harap masukkan No. Polis / Register SPAJ yang benar");
			}
			
			if(!errors.hasErrors() && ((jn_bank!=4 && jn_bank!=3 && jn_bank!= 2) && jn_bank!=-1)) {
				errors.reject("", "Menu ini hanya dapat digunakan oleh user Bank Sinarmas/Sekuritas.");
			}
			
			//validasi khusus bank sinarmas, harus punya akses terhadap suatu polis
			if(!errors.hasErrors() && jn_bank==2) {
				if(elionsManager.selectIsUserYangInputBank(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("SSS")) {
					if(currentUser.getJn_bank().intValue() == 2){
						errors.reject("","Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
							elionsManager.selectCabangBiiPolis(cmd.getReg_spaj()));
					}
				}
			}
			
			if(!errors.hasErrors() && jn_bank==3) {
				if(elionsManager.selectIsUserYangInputSekuritas(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) <=0 && !currentUser.getCab_bank().trim().equals("M35")) {
					if(currentUser.getJn_bank().intValue() == 3){
							errors.reject("","Anda tidak mempunyai akses transfer/Print terhadap Polis ini.");
					}
				}else if(elionsManager.selectIsUserYangInputSekuritas(cmd.getReg_spaj(), Integer.valueOf(currentUser.getLus_id())) >=1 && currentUser.getCab_bank().trim().equals("M35")) {
					if(currentUser.getJn_bank().intValue() == 3){
						if(uwManager.selectCountOtorisasiSpaj(cmd.getReg_spaj())<=0){
							errors.reject("","Polis Ini belum diotorisasi SPV yang bersangkutan.");
						}
						if(currentUser.getFlag_approve().intValue()!=0){
							errors.reject("","Polis hanya bisa di print oleh admin pusat.");
						}
					}
				}
			}
			
			//validasi harus produk stable link
			if(!errors.hasErrors()) {
				String lsbs_id = uwManager.selectBusinessId(cmd.getReg_spaj());
				if(!products.stableLink(lsbs_id) && Integer.parseInt(lsbs_id) !=188) {
					errors.rejectValue("reg_spaj", "", "Polis ini bukan Polis STABLE LINK/ POWERSAVE NEW. Harap pastikan nomor yang Anda masukkan benar.");
				}
			}
			
			/* 
			 * Yusuf (13 Aug 09) - Disabled, request dari BSM / Iwen / Pak CK, ini agar bisa input topup barengan dengan input new business			
			//validasi lssp_id
			if(!errors.hasErrors()) {
				PowersaveCair pc = elionsManager.selectRolloverData(cmd.getReg_spaj());
				if(pc.lssp_id.intValue() != 1 && pc.lssp_id.intValue() != 6) {
					errors.rejectValue("reg_spaj", "", "Status Polis saat ini bukan INFORCE/DEATH CLAIM. Anda tidak bisa melanjutkan. Harap konfirmasi dengan dept LIFE BENEFIT AJS.");
				}
			}
			//validasi sudah masuk produksi pertama/belum
			if(!errors.hasErrors()) {
				List produksi = uwManager.selectProduksiKe(cmd.getReg_spaj(), 1);
				if(produksi.isEmpty()) {
					errors.rejectValue("reg_spaj", "", "Polis ini belum diakseptasi oleh UNDERWRITING kami. Harap konfirmasi dengan dept UNDERWRITING AJS.");
				}
			}
			*/
			
			//validasi tidak boleh ada di pinjaman (alias udah cair)
			if(!errors.hasErrors()) {
				int hitung = elionsManager.selectValidasiPinjaman(cmd.getReg_spaj());
				if(hitung > 0) {
					errors.rejectValue("reg_spaj", "", "Polis yang Anda masukkan sudah melakukan pencairan. Harap dikonfirmasi dengan dept LIFE BENEFIT AJS.");
				}
			}
			
			//** bila tembus semua validasi, baru tarik datanya **//
			if(!errors.hasErrors()) {
				cmd.setTrans(elionsManager.selectEntryTransStableLink(cmd.getReg_spaj()));
				cmd.setDaftarTopup(elionsManager.selectEntryTopupStableLink(cmd.getReg_spaj(), 73));
				if(!cmd.getDaftarTopup().isEmpty()){
					if(Common.isEmpty(cmd.getDaftarTopup().get(0).getMsl_tgl_nab()) ){
						errors.reject("", "PRINT TOP UP tidak dapat dilakukan karena Sedang Proses NAB untuk Top Up ini.");
					}else{
						errors.reject("", "Silahkan tekan tombol PRINT TOP UP untuk mencetak.");
					}
				}else{
					errors.reject("", "Maaf saat ini tidak ada data top up.");
				}
			}
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Command cmd = (Command) command;
		cmd.getTrans().getMsl_tu_ke();
		String reportUrl = request.getContextPath()+"/reports/alokasidanaSlinkTopup.pdf?spaj="+cmd.getReg_spaj()+"&tu_ke="+cmd.getTrans().getMsl_tu_ke();
		response.sendRedirect(reportUrl);
		return null;
	}
	
}