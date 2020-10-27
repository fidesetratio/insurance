package com.ekalife.elions.web.accounting;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.DBank;
import com.ekalife.elions.model.TBank;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

public class InputPreFormController extends ParentFormController{
	
	//Fungsi ini untuk mengubah bentuk angka baik type data double, integer maupun date(misal: hasil keluarnya 12.234AF menjadi 12.234.567)
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	

	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Command command=(Command)cmd;
		List lstKas = new ArrayList();
		List lstFlow = new ArrayList();
		List lstKasRowFirst = new ArrayList();
		DBank dbank = command.getDbank();
		
		//List list = dbank.getListDBank();
			lstKasRowFirst = elionsManager.selectLstKasRowFirst();
			lstKas = elionsManager.selectLstKas();
			lstFlow = elionsManager.selectKodeCashFlow();
		//String pesan = request.getAttribute("pesan");
		Map map=new HashMap();
		map.put("lstKasRowFirst", lstKasRowFirst);
		map.put("lstKas", lstKas);
		map.put("lstFlow", lstFlow);
		//map.put("pesan", pesan);
		return map;
		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		Command command = new Command();
		DBank dbank = new DBank();
		dbank.setTbank(new TBank());
		dbank.setUser(new User());
		Date sysdate = elionsManager.selectSysdateSimple();
		HttpSession session = request.getSession();
		String pesan = null;
		User currentUser = (User) session.getAttribute("currentUser");
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		Integer p=ServletRequestUtils.getIntParameter(request, "p",0);
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String nomorcari=ServletRequestUtils.getStringParameter(request,"nomorcari","");
		String nomortampung=ServletRequestUtils.getStringParameter(request,"nomortampung","");
		session.removeAttribute("sysDate");
		nomortampung=nomor;
		if(!nomorcari.equals("")){
			nomor=nomorcari;
		}
		
		if(flag.equals("0")){//tampil atau edit
			dbank.setListDBank(elionsManager.selectMstDBank(nomor));
			if(dbank.getListDBank().size() == 0){
				request.setAttribute("pesan","Nomor yang dicari tidak ada");
				dbank.setListDBank(elionsManager.selectMstDBank(nomortampung));
				dbank.setFlag(1);
				dbank.setLus_login_name(dbank.getListDBank().get(0).getLus_login_name());
				dbank.setTgl_input(dbank.getListDBank().get(0).getTgl_input());
				dbank.setNo_pre(nomortampung);
				int nol =0;
				Double debet = new Double(nol);
				Double kredit = new Double(nol);
				//Double kredit = '0';
				for(int i=0;i<dbank.getListDBank().size();i++){
					if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
						if(i==0){
							debet = dbank.getListDBank().get(0).getJumlah();
						}else
							debet += dbank.getListDBank().get(i).getJumlah();
						dbank.setDebet(debet);
						dbank.setKredit(kredit);
					}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
						if(i==0){
							kredit = dbank.getListDBank().get(0).getJumlah();
						}else
							kredit += dbank.getListDBank().get(i).getJumlah();
						dbank.setKredit(kredit);
						dbank.setDebet(debet);
					}
				}
				Double balance = debet - kredit;
				dbank.setBalance(balance);
				command.setFlagAdd(0);
				command.setDbank(dbank);
			}else{
				dbank.setFlag(1);
				dbank.setLus_login_name(dbank.getListDBank().get(0).getLus_login_name());
				dbank.setTgl_input(dbank.getListDBank().get(0).getTgl_input());
				dbank.setNo_pre(nomor);
				int nol =0;
				Double debet = new Double(nol);
				Double kredit = new Double(nol);
				//Double kredit = '0';
				for(int i=0;i<dbank.getListDBank().size();i++){
					if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
						if(i==0){
							debet = dbank.getListDBank().get(0).getJumlah();
						}else
							debet += dbank.getListDBank().get(i).getJumlah();
						dbank.setDebet(debet);
						dbank.setKredit(kredit);
					}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
						if(i==0){
							kredit = dbank.getListDBank().get(0).getJumlah();
						}else
							kredit += dbank.getListDBank().get(i).getJumlah();
						dbank.setKredit(kredit);
						dbank.setDebet(debet);
					}
				}
				Double balance = debet - kredit;
				dbank.setBalance(balance);
				command.setFlagAdd(0);
				command.setDbank(dbank);
				String in = ServletRequestUtils.getStringParameter(request, "in", "");
				if(in == null){
					in="";
				}
				request.setAttribute("in","in");
			}
		}else if(flag.equals("1")){//add row baru dari no_pre yang sudah ada
			dbank.setListDBank(elionsManager.selectMstDBank(nomor));
			dbank.setLus_login_name(currentUser.getName());
			dbank.setTgl_input(sysdate);
			dbank.setNo_pre(nomor);
			int nol =0;
			Double debet = new Double(nol);
			Double kredit = new Double(nol);
			for(int i=0;i<dbank.getListDBank().size();i++){
				if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
					if(i==0){
						debet = dbank.getListDBank().get(0).getJumlah();
					}else
						debet += dbank.getListDBank().get(i).getJumlah();
					dbank.setDebet(debet);
					dbank.setKredit(kredit);
				}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
					if(i==0){
						kredit = dbank.getListDBank().get(0).getJumlah();
					}else
						kredit += dbank.getListDBank().get(i).getJumlah();
					dbank.setKredit(kredit);
					dbank.setDebet(debet);
				}
			}
			Double balance = debet - kredit;
			dbank.setBalance(balance);
			command.setDbank(dbank);
		}else if(flag.equals("2")){//insert no_pre yang baru
			dbank.setTgl_input(sysdate);
			dbank.setNo_pre("NEW");
			dbank.setLus_login_name(currentUser.getName());
			dbank.setLus_id(currentUser.getLus_id());
			dbank.setListDBank(new ArrayList());//set default 2 row
			dbank.getListDBank().add(new DBank());
			dbank.getListDBank().add(new DBank());
			dbank.getListDBank().get(0).setKeterangan("bank");
			command.setFlagAdd(2);
			command.setDbank(dbank);
		}else if(flag.equals("3")){//proses refresh insert baru & add row baru
			dbank.setListDBank(elionsManager.selectMstDBank(nomor));
			dbank.setLus_login_name(currentUser.getName());
			dbank.setTgl_input(sysdate);
			dbank.setNo_pre(nomor);
			int nol =0;
			Double debet = new Double(nol);
			Double kredit = new Double(nol);
			for(int i=0;i<dbank.getListDBank().size();i++){
				if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
					if(i==0){
						debet = dbank.getListDBank().get(0).getJumlah();
					}else
						debet += dbank.getListDBank().get(i).getJumlah();
					dbank.setDebet(debet);
					dbank.setKredit(kredit);
				}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
					if(i==0){
						kredit = dbank.getListDBank().get(0).getJumlah();
					}else
						kredit += dbank.getListDBank().get(i).getJumlah();
					dbank.setKredit(kredit);
					dbank.setDebet(debet);
				}
			}
			Double balance = debet - kredit;
			dbank.setBalance(balance);
			command.setDbank(dbank);
		}else if(flag.equals("4")){//deleterow
			String row = ServletRequestUtils.getStringParameter(request, "row", "");
			int no_jurnal= Integer.parseInt(row);
			dbank.setListDBank(elionsManager.selectMstDBank(nomor));
			//bagian ini untuk mengecek apakah row yang diinput ada di listDBank
			for(int k=0;k<dbank.getListDBank().size();k++){
				if(no_jurnal==dbank.getListDBank().get(k).getNo_jurnal()){
					//bagian ini untuk menghapus row
					elionsManager.deleteMstDBank(nomor,no_jurnal);
				}
				if(no_jurnal>dbank.getListDBank().size()){
					request.setAttribute("pesan","Row yang ingin dihapus tidak ada di data");
				}
			}
			
			dbank.setListDBank(elionsManager.selectMstDBank(nomor));
			for(int j=0;j<dbank.getListDBank().size();j++){
				//int no_jurnalafter= dbank.getListDBank().get(j).setNo_jurnal(j+1);
				//bagian ini untuk mengupdate no_jurnal agar terurut
				int no_jurnalafter=j+1;
				elionsManager.updateMstDBankRow(nomor,no_jurnalafter,dbank.getListDBank().get(j).getNo_jurnal());
				dbank.getListDBank().get(j).setNo_jurnal(j+1);
			}
			dbank.setFlag(1);
			dbank.setLus_login_name(dbank.getListDBank().get(0).getLus_login_name());
			dbank.setTgl_input(dbank.getListDBank().get(0).getTgl_input());
			dbank.setNo_pre(nomor);
			int nol =0;
			Double debet = new Double(nol);
			Double kredit = new Double(nol);
			//Double kredit = '0';
			for(int i=0;i<dbank.getListDBank().size();i++){
				if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
					if(i==0){
						debet = dbank.getListDBank().get(0).getJumlah();
					}else
						debet += dbank.getListDBank().get(i).getJumlah();
					dbank.setDebet(debet);
					dbank.setKredit(kredit);
				}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
					if(i==0){
						kredit = dbank.getListDBank().get(0).getJumlah();
					}else
						kredit += dbank.getListDBank().get(i).getJumlah();
					dbank.setKredit(kredit);
					dbank.setDebet(debet);
				}
			}
			Double balance = debet - kredit;
			dbank.setBalance(balance);
			command.setFlagAdd(0);
			command.setDbank(dbank);
		}
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		List<DBank> lstDBank = command.getDbank().getListDBank();
		DBank dbank = command.getDbank();
		List<DBank> lstDBankDefault = elionsManager.selectMstDBank(dbank.getListDBank().get(0).getNo_pre());
		int nol =0;
		Double debet = new Double(nol);
		Double kredit = new Double(nol);
		for(int i=0;i<dbank.getListDBank().size();i++){
			if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
				if(i==0){
					debet = dbank.getListDBank().get(0).getJumlah();
				}else
					debet += dbank.getListDBank().get(i).getJumlah();
				dbank.setDebet(debet);
				dbank.setKredit(kredit);
			}else if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
				if(i==0){
					kredit = dbank.getListDBank().get(0).getJumlah();
				}else
					kredit += dbank.getListDBank().get(i).getJumlah();
				dbank.setKredit(kredit);
				dbank.setDebet(debet);
			}
		}
		Double balance = debet - kredit;
		dbank.setBalance(balance);
		
		command.setDaftardbank(lstDBank);
		command.setDaftardbankDefault(lstDBankDefault);
		//cek data keterangan null atau tidak
		for(int i=0;i<lstDBank.size();i++){
			String keterangan = lstDBank.get(i).getKeterangan();
			keterangan = keterangan.trim();
			String kas = lstDBank.get(i).getKas();
			kas = kas.trim();
			if(lstDBank.get(i).getKeterangan().equals(""))
				err.reject("","Data Keterangan baris ke"+(i+1)+" tidak boleh kosong");
			if(lstDBank.get(i).getKas()==null)
				err.reject("","Jenis Kas baris ke"+(i+1)+" tidak boleh kosong");
		}
		
		for(int i=0;i<lstDBank.size();i++){
			String kas = lstDBank.get(i).getKas();
			kas = kas.trim();
			if(lstDBank.get(i).getKas().equals(""))
				err.reject("","Jenis Kas baris ke"+(i+1)+" tidak boleh kosong");
		}
		//untuk tipe data double
		for(int i=0;i<lstDBank.size();i++){
			if(lstDBank.get(i).getJumlah()==null)
				err.reject("","Jumlah uang baris ke"+(i+1)+" belum terisi");
		}
		//untuk tipe data int
		for(int i=1;i<lstDBank.size();i++){
			if(lstDBank.get(i).getKode_cash_flow()==null)
				err.reject("","Flow baris ke"+(i+1)+" tidak boleh kosong");
		}
		
		if(balance !=0){
			err.reject("","Jumlah harus Balance");
		}
		
		if(request.getParameter("addRow") != null) {
			int no_baris = lstDBank.size()+1;
			lstDBank.add(new DBank());
			command.setFlagAdd(1);
			err.reject("", "Tambah Row");
		}
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!=null){
			elionsManager.prosesInputPre(command.getDbank(),command.getFlagAdd(),command.getDaftardbankDefault());
		}
		return new ModelAndView("accounting/input_pre",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
}