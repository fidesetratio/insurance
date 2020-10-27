package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import com.ekalife.elions.model.Children;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Jiffy;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.RelasiNasabah;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

public class InputFactFormController extends ParentFormController {
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
		List<DropDown> lstKarir, lstPartner, lstRelasiChild;
		List lstBii;
		Map map=new HashMap();
		lstKarir = elionsManager.selectLstKarir();
		lstPartner = elionsManager.selectLstPartner();
		lstBii = elionsManager.selectListBii();
		lstRelasiChild = elionsManager.selectLstRelasi();
		map.put("lstKarir", lstKarir);
		map.put("lstPartner",lstPartner);
		map.put("lstBii", lstBii);
		map.put("lstRelasiChild", lstRelasiChild);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		command.setShowTab(1); //tab yang ditampilkan adalah tab 1
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		String tipe=ServletRequestUtils.getStringParameter(request, "tipe","");
		Nasabah nasabah=new Nasabah();
		Jiffy jiffy = new Jiffy();
		RelasiNasabah relasiNasabah = new RelasiNasabah();
		Children children = new Children();
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		
		
		if(flag.equals("1")){//untuk deleteRow
			String row = ServletRequestUtils.getStringParameter(request, "row", "");
			int no_jurnal= Integer.parseInt(row);
			if(tipe.equals("0")){//bagian deleteRowReferensi
				relasiNasabah.setListRelasiNasabah(elionsManager.selectMstRelasiNasabah(kdNasabah));
//				bagian ini untuk mengecek apakah row yang diinput ada di listRelasiNasabah
				for(int k=0;k<relasiNasabah.getListRelasiNasabah().size();k++){
					if(no_jurnal==relasiNasabah.getListRelasiNasabah().get(k).getMrn_no_relasi()){
						//bagian ini untuk menghapus row
						elionsManager.deleteMstRelasiNasabahByNo(kdNasabah,no_jurnal);
					}
					if(no_jurnal>relasiNasabah.getListRelasiNasabah().size()){
						request.setAttribute("pesan","Row yang ingin dihapus tidak ada di data");
					}
				}
				//bagian ini untuk merapikan nomor dari mrn_no_relasi agar terurut.
				relasiNasabah.setListRelasiNasabah(elionsManager.selectMstRelasiNasabah(kdNasabah));
				for(int k=0;k<relasiNasabah.getListRelasiNasabah().size();k++){
					int no_jurnalafter=k+1;
					elionsManager.updateMstRelasiNasabahRow(kdNasabah,no_jurnalafter,relasiNasabah.getListRelasiNasabah().get(k).getMrn_no_relasi());
					relasiNasabah.getListRelasiNasabah().get(k).setMrn_no_relasi(k+1);
				}
				
			}else if(tipe.equals("1")){//bagian deleteRowChild
				children.setListChildren(elionsManager.selectMstChildren(kdNasabah));
//				bagian ini untuk mengecek apakah row yang diinput ada di listChildren
				for(int k=0;k<children.getListChildren().size();k++){
					if(no_jurnal==children.getListChildren().get(k).getMch_id()){
						//bagian ini untuk menghapus row
						elionsManager.deleteMstChildrenByNo(kdNasabah,no_jurnal);
					}
					if(no_jurnal>children.getListChildren().size()){
						request.setAttribute("pesan","Row yang ingin dihapus tidak ada di data");
					}
				}
				//bagian ini untuk merapikan nomor dari mch_id agar terurut.
				children.setListChildren(elionsManager.selectMstChildren(kdNasabah));
				for(int k=0;k<children.getListChildren().size();k++){
					int no_jurnalafter=k+1;
					elionsManager.updateMstChildrenRow(kdNasabah,no_jurnalafter,children.getListChildren().get(k).getMch_id());
					children.getListChildren().get(k).setMch_id(k+1);
				}
			}
			
			
		}
		
		relasiNasabah.setListRelasiNasabah(elionsManager.selectMstRelasiNasabah(kdNasabah));
		if(relasiNasabah.getListRelasiNasabah()!=null){
			for(int i=0;i<relasiNasabah.getListRelasiNasabah().size();i++){
				if(relasiNasabah.getListRelasiNasabah().get(i).getMrn_ref_to_bii()==null){
					relasiNasabah.getListRelasiNasabah().get(i).setMrn_ref_to_bii(0);
				}
			}
		}
		children.setListChildren(elionsManager.selectMstChildren(kdNasabah));
		
		nasabah=elionsManager.selectMstNasabah(kdNasabah);
		jiffy = elionsManager.selectMstjiffy(kdNasabah);
		command.setNasabah(nasabah);
		command.setJiffy(jiffy);
		command.setRelasiNasabah(relasiNasabah);
		command.setChildren(children);
		
		if(flag.equals("2")){//bagian ini untuk view dari aktivitas saja
			command.setFlagAdd(2);
		}
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Jiffy jiffy = command.getJiffy();
		List relasi = command.getRelasiNasabah().getListRelasiNasabah();
		List  children = command.getChildren().getListChildren();
		
		command.setJiffy(jiffy);
		command.getRelasiNasabah().setListRelasiNasabah(relasi);
		command.getChildren().setListChildren(children);
		
		//Menambah row di referensi
		if(request.getParameter("addRowRef") != null) {
			int no_baris = relasi.size()+1;
			relasi.add(new RelasiNasabah());
			err.reject("", "Tambah Row Referensi");
		}
		
		//menambah row di Rencana Pendidikan
		if(request.getParameter("AddRowChild") != null) {
			int no_baris = children.size()+1;
			children.add(new Children());
			command.setShowTab(3);
			err.reject("", "Tambah Row Rencana Pendidikan");
		}
		
		for(int i=0;i<children.size();i++){
			Date tgl_lahir = command.getChildren().getListChildren().get(i).getMch_birth_date();
			if (tgl_lahir==null){
				err.reject("","Tanggal Lahir tabel Rencana Pendidikan Row ["+(i+1)+"] Harap Diisi");
//			}else if(tgl_lahir!=null || !tgl_lahir.equals("dd/MM/yyyy")){
//				DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
//				String tgl = df2.format(tgl_lahir);
//				if(!tgl_lahir.equals("dd/mm/yyyy")){
//					err.reject("","Format Tanggal salah");
//				}
//				
			}
				
			
		}
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!=null){
			elionsManager.prosesInputJiffy(command);
		}else {//bagian ini untuk transfer
			command.setFlagAdd(1);
			elionsManager.prosesTransFact(command);
		}
		
		return new ModelAndView("common/input_fact",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}