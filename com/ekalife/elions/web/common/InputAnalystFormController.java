package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Jiffy;
import com.ekalife.elions.model.Kebutuhan;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.Pendapatan;
import com.ekalife.elions.model.ProdBank;
import com.ekalife.utils.parent.ParentFormController;

public class InputAnalystFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Command command=(Command)cmd;
		List lstJnProduk, lstAktivitas;
		List<DropDown> lstReviewUang;
		lstJnProduk = elionsManager.selectListProdBank();
		lstReviewUang = elionsManager.selectLstReviewUang();
		lstAktivitas = elionsManager.selectListAktivitas();
		Map map=new HashMap();
		map.put("lstJnProduk", lstJnProduk);
		map.put("lstReviewUang", lstReviewUang);
		map.put("lstAktivitas", lstAktivitas);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		command.setShowTab(1); //tab yang ditampilkan adalah tab 1
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		String tipe=ServletRequestUtils.getStringParameter(request, "tipe","");
		Nasabah nasabah = new Nasabah();
		Kebutuhan kebutuhan = new Kebutuhan();
		Jiffy jiffy = new Jiffy();
		ProdBank prodBank = new ProdBank();
		Pendapatan pendapatan = new Pendapatan();
		Aktivitas aktivitas = new Aktivitas();
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		String mns_kd_nasabah = kdNasabah;
		if(flag.equals("1")){//untuk deleteRow
			String row = ServletRequestUtils.getStringParameter(request, "row", "");
			int no_row= Integer.parseInt(row);
			if(tipe.equals("0")){//deleteRow bagian Produk
				prodBank.setListProdBank(elionsManager.selectMstProdBankPlusLpbKet(mns_kd_nasabah));
//				bagian ini untuk mengecek apakah row yang diinput ada di listProdBank
				for(int k=0;k<prodBank.getListProdBank().size();k++){
					if(no_row == prodBank.getListProdBank().get(k).getMpb_no()){
						elionsManager.deleteMstProdBankByNo(mns_kd_nasabah, no_row);
					}
					if(no_row >prodBank.getListProdBank().size()){
						request.setAttribute("pesan","Row yang ingin dihapus tidak ada di data");
					}
				}
//				bagian ini untuk merapikan nomor dari mpb_no agar terurut.
				prodBank.setListProdBank(elionsManager.selectMstProdBankPlusLpbKet(mns_kd_nasabah));
				for(int k=0;k<prodBank.getListProdBank().size();k++){
					int no_rowafter=k+1;
					elionsManager.updateMstProdBankRow(mns_kd_nasabah, no_rowafter, prodBank.getListProdBank().get(k).getMpb_no());
					prodBank.getListProdBank().get(k).setMpb_no(k+1);
				}
			}else if(tipe.equals("1")){//deleteRow bagian Aktivitas
				aktivitas.setListAktivitas(elionsManager.selectMstAktivitasNext(mns_kd_nasabah));
				for(int k=0;k<aktivitas.getListAktivitas().size();k++){
					if(no_row == aktivitas.getListAktivitas().get(k).getPert_ke()){
						elionsManager.deleteMstAktivitasByPertKe(mns_kd_nasabah, no_row);
					}
				}
			}
			
		}
		nasabah=elionsManager.selectMstNasabah(kdNasabah);
		jiffy = elionsManager.selectMstjiffy(mns_kd_nasabah);
		kebutuhan.setListKebutuhan(elionsManager.selectMstKebutuhanPlusLjkKet(mns_kd_nasabah));
		prodBank.setListProdBank(elionsManager.selectMstProdBankPlusLpbKet(mns_kd_nasabah));
		pendapatan.setListPendapatan(elionsManager.selectMstPendapatanPlusLspKet(mns_kd_nasabah));
		aktivitas.setListAktivitas(elionsManager.selectMstAktivitasNext(mns_kd_nasabah));
			
		
//		if(prodBank.getListProdBank().size()==0){
//			prodBank.getListProdBank().add(new ProdBank());
//		}
//		if(aktivitas.getListAktivitas().size()==0){
//			aktivitas.getListAktivitas().add(new Aktivitas());
//		}
		command.setNasabah(nasabah);
		command.setKebutuhan(kebutuhan);
		command.setJiffy(jiffy);
		command.setProdBank(prodBank);
		command.setPendapatan(pendapatan);
		command.setAktivitas(aktivitas);
		
		if(flag.equals("2")){//bagian ini untuk view dari aktivitas saja
			command.setFlagAdd(2);
		}
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Aktivitas aktivitas = new Aktivitas();
		
		if(command.getAktivitas().getListAktivitas().size() !=0){
			for(int i=0;i<command.getAktivitas().getListAktivitas().size();i++){
				if(command.getAktivitas().getListAktivitas().get(i).getPert_ke() == null){
					err.reject("", "Kolom Pert ke harap diisi");
				}
			}
		}
		
		if(command.getProdBank().getListProdBank().size() != 0){
			for(int i=0;i<command.getProdBank().getListProdBank().size();i++){
				if(command.getProdBank().getListProdBank().get(i).getLpb_id() == null){
					err.reject("", "Jenis Produk Harap Diisi");
				}
			}
		}
		
		if(command.getAktivitas().getListAktivitas().size() != 0){
			for(int i=0;i<command.getAktivitas().getListAktivitas().size();i++){
				if(command.getAktivitas().getListAktivitas().get(i).getKd_aktivitas() == null){
					err.reject("", "Jenis Aktivitas Harap Diisi");
				}
			}
		}
		
//		Menambah row di aktivitas
		if(request.getParameter("AddAktivitas") != null) {
			int no_baris = elionsManager.selectMstAktivitas(command.getJiffy().getMns_kd_nasabah()).size()+1;
			aktivitas.setPert_ke(no_baris);
			command.getAktivitas().getListAktivitas().add(aktivitas);
			//command.setAktivitas(aktivitas);
			command.setShowTab(3);
			err.reject("", "Tambah Row Aktivitas");
		}
		
//		Menambah row di produk bank
		if(request.getParameter("AddProduk") != null) {
			int no_baris = command.getProdBank().getListProdBank().size()+1;
			command.getProdBank().getListProdBank().add(new ProdBank());
			command.setShowTab(2);
			err.reject("", "Tambah Row Produk");
		}
		
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!= null){
			elionsManager.prosesInputAnalyst(command);
		}else{ 
			command.setFlagAdd(1);
			elionsManager.prosesTransAnalyst(command);
		}
		
		return new ModelAndView("common/input_analyst",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}