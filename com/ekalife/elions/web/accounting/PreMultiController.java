package com.ekalife.elions.web.accounting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.dao.AccountingDao;
import com.ekalife.elions.model.BVoucher;
import com.ekalife.elions.model.DBank;
import com.ekalife.elions.model.TBank;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PreMultiController extends ParentMultiController {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView main_input_pre(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		List lsNoPre=elionsManager.selectAllMstTBank(1);
		TBank tbank=(TBank)lsNoPre.get(0);
		if(flag.equals("1")){
			String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
			map.put("lsNoPre", lsNoPre);
			map.put("nomor", nomor);
			map.put("no_pre", nomor);
			return new ModelAndView("accounting/main_input_pre", map);
		}else if(flag.equals("2")){
			String no_voucher = ServletRequestUtils.getStringParameter(request, "no_voucher", "");
			map.put("no_voucher", no_voucher);
			map.put("lsNoPre",lsNoPre);
			map.put("nomor",tbank.getNo_pre());
			map.put("pesan", "No_voucher :"+no_voucher);
			return new ModelAndView("accounting/main_input_pre", map);
		}else if(flag.equals("3")){//proses delete(dengan mengupdate position ke 5 dari nomor pre nya)
			String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
			elionsManager.updateMstTBankPosition(nomor);
			lsNoPre=elionsManager.selectAllMstTBank(1);
			tbank=(TBank)lsNoPre.get(0);
			map.put("lsNoPre", lsNoPre);
			map.put("nomor", tbank.getNo_pre());
			map.put("pesan", "Data berhasil didelete");
			return new ModelAndView("accounting/main_input_pre", map);
		}
		map.put("lsNoPre",lsNoPre);
		map.put("nomor",tbank.getNo_pre());
		//map.put("no_pre", tbank.getNo_pre());
		return new ModelAndView("accounting/main_input_pre", map);
	}
	
	public ModelAndView cari_pre(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsCariPre=new ArrayList();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		Integer tipe=ServletRequestUtils.getIntParameter(request, "tipe",1);
		if(flag.equals("1")){
			lsCariPre=elionsManager.selectMstTBankByCode(nomor);
		}
		map.put("lsCariPre", lsCariPre);
		map.put("nomor", nomor);
		map.put("tipe", tipe);
		return new ModelAndView("accounting/cari_pre", map);
	}
	
	public ModelAndView trans_pre(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lstBank = new ArrayList();
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		Date sysdate = elionsManager.selectSysdateSimple();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		lstBank = elionsManager.selectLstBank();
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
		map.put("sysdate", defaultDateFormat.format(sysdate));
		map.put("nomor", nomor);
		map.put("lstBank", lstBank);
		
		if(flag.equals("0")){//save gantung
			int titipan = ServletRequestUtils.getIntParameter(request, "titipan", 2);
			String nomorLama = ServletRequestUtils.getStringParameter(request, "nomorLama", "");
			elionsManager.updatePreGantung(nomor, titipan, nomorLama);
			map.put("pesan", "UPDATE DATA BERHASIL");
			//logger.info(nomorLama);
		}else if(flag.equals("1")){//proses ke Accounting
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String tanggal= ServletRequestUtils.getStringParameter(request, "tanggal", "");
			Date tgl_input= dateFormat.parse(tanggal);
			//tgl_input = defaultDateFormat.format(tgl_input);
			//sysdate = dateFormat.format("sysdate");
			String mtb_gl_no= ServletRequestUtils.getStringParameter(request, "mtb_gl_no", "");
			String rek_id= elionsManager.selectRekEkalife(mtb_gl_no);
			String lus_id_trans = currentUser.getLus_id();
			String no_voucher = elionsManager.sequenceNoVoucher(mtb_gl_no,rek_id,nomor);
			
			//bagian ini untuk mendapatkan tanggal produksi di finance
			Date ldt_close = elionsManager.selectMstDefault();
			Date ldt_prod_date = dateFormat.parse(defaultDateFormat.format(sysdate));
			if(ldt_prod_date.after(ldt_close)) {
				Date ldt_rk_terakhir = elionsManager.selectMstDefault2();
				if(ldt_prod_date.before(ldt_rk_terakhir) || ldt_prod_date.equals(ldt_rk_terakhir)){
					ldt_prod_date = ldt_close;
				}
			}
			
			//Cek apakah no_voucher dapat atau tidak.Kalau dapat cek lagi apakah no_voucher tersebut sudah pernah ada untuk sebuah no_pre
			if(no_voucher != null){
				dateFormat = new SimpleDateFormat("yyyy");
				String ls_thn = dateFormat.format(ldt_prod_date);
				String ls_kembar = elionsManager.selectMstTBankVoucher(no_voucher,ls_thn);
				if(ls_kembar !=null){
					map.put("pesan", "No. Voucher Kembar....Transfer Ulang !!" + ls_kembar);
					return new ModelAndView("accounting/trans_pre", map);
				}
			}else{
				map.put("pesan", "GAGAL TRANSFER");
				map.put("InfoFrame", "accounting/pre.htm?window=main_input_pre");
				return new ModelAndView("accounting/trans_pre", map);
			}
			//proses update ke tabel mstTbank
			elionsManager.updateMstTBank(nomor , 2, sysdate, tgl_input, no_voucher, mtb_gl_no, Integer.parseInt(lus_id_trans));
			
			//untuk proses insert ke tabel mst_bvoucher
			DBank dbank = new DBank();
			BVoucher bvoucher = new BVoucher();
			dbank.setListDBank(elionsManager.selectMstDBankTrans(nomor));
			for(int i=0;i<dbank.getListDBank().size();i++){
				//int jumlah =0;
				bvoucher.setNo_pre(nomor);
				bvoucher.setNo_jurnal(dbank.getListDBank().get(i).getNo_jurnal());
				bvoucher.setKeterangan(dbank.getListDBank().get(i).getKeterangan());
				if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
					//convert int ke double(langsung)
					//jumlah = dbank.getListDBank().get(i).getJumlah();
					Double jmlh= dbank.getListDBank().get(i).getJumlah();
					bvoucher.setKredit(jmlh);
					int nol =0;
					Double debet = new Double(nol);
					bvoucher.setDebet(debet);
				}else if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
					//jumlah = dbank.getListDBank().get(i).getJumlah();
					Double debet = dbank.getListDBank().get(i).getJumlah();
					bvoucher.setDebet(debet);
					int nol =0;
					Double kredit = new Double(nol);
					bvoucher.setKredit(kredit);
				}
				if(i==0){
					bvoucher.setProject_no(mtb_gl_no.substring(0, 3));
					bvoucher.setBudget_no(mtb_gl_no.substring(4, mtb_gl_no.length()));
				}else {
					bvoucher.setProject_no("");
					bvoucher.setBudget_no("");
				}
				elionsManager.insertMstBVoucher(bvoucher);
			}
			String in = request.getParameter("in");
			if(in == null){
				in="";
			}
			map.put("no_voucher", no_voucher);
			map.put("pesan", "No_voucher : "+ no_voucher);
			request.setAttribute("in","in");
			return new ModelAndView("accounting/trans_pre", map);
			
		}else if(flag.equals("2")){//Proses ke BankBook
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String tanggal= ServletRequestUtils.getStringParameter(request, "tanggal", "");
			Date tgl_input= dateFormat.parse(tanggal);
			String mtb_gl_no= ServletRequestUtils.getStringParameter(request, "mtb_gl_no", "");
			String rek_id= elionsManager.selectRekEkalife(mtb_gl_no);
			String lus_id_trans = currentUser.getLus_id();
			
			//update Mst_TBank
			elionsManager.updateMstTBankBook(nomor , 2, tgl_input, mtb_gl_no, Integer.parseInt(lus_id_trans));
			
			//untuk proses insert ke tabel mst_bvoucher
			DBank dbank = new DBank();
			BVoucher bvoucher = new BVoucher();
			dbank.setListDBank(elionsManager.selectMstDBankTrans(nomor));
			for(int i=0;i<dbank.getListDBank().size();i++){
				int jumlah =0;
				bvoucher.setNo_pre(nomor);
				bvoucher.setNo_jurnal(dbank.getListDBank().get(i).getNo_jurnal());
				bvoucher.setKeterangan(dbank.getListDBank().get(i).getKeterangan());
				if(dbank.getListDBank().get(i).getKas().equals("K") || dbank.getListDBank().get(i).getKas().equals("A") || dbank.getListDBank().get(i).getKas().equals("B")){
					//convert int ke double(langsung)
					//jumlah = dbank.getListDBank().get(i).getJumlah();
					Double jmlh= dbank.getListDBank().get(i).getJumlah();
					bvoucher.setKredit(jmlh);
					int nol =0;
					Double debet = new Double(nol);
					bvoucher.setDebet(debet);
				}else if(dbank.getListDBank().get(i).getKas().equals("M") || dbank.getListDBank().get(i).getKas().equals("C") || dbank.getListDBank().get(i).getKas().equals("D")){
					//jumlah = dbank.getListDBank().get(i).getJumlah();
					Double debet = dbank.getListDBank().get(i).getJumlah();
					bvoucher.setDebet(debet);
					int nol =0;
					Double kredit = new Double(nol);
					bvoucher.setKredit(kredit);
				}
				if(i==0){
					bvoucher.setProject_no(mtb_gl_no.substring(0, 3));
					bvoucher.setBudget_no(mtb_gl_no.substring(4, mtb_gl_no.length()));
				}else {
					bvoucher.setProject_no("");
					bvoucher.setBudget_no("");
				}
				elionsManager.insertMstBVoucher(bvoucher);
			}
			String in = request.getParameter("in");
			if(in == null){
				in="";
			}
			request.setAttribute("in","in");
			map.put("pesan", "Transfer BankBook Berhasil");
			return new ModelAndView("accounting/trans_pre", map);
			
		}
		
		return new ModelAndView("accounting/trans_pre", map);
	}
	
//	public ModelAndView proses_trans_pre(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
//		//int titipan_gantung = ServletRequestUtils.getIntParameter(request, "titipan_gantung");
//		String flag = ServletRequestUtils.getStringParameter(request, "flag");
//		
//		//String buttonFlag = ServletRequestUtils.getStringParameter(request, "pilih");
//		int pilih = ServletRequestUtils.getIntParameter(request, "titipan_gantung", 4);
//		//if(request.getParameter(""))
//		return new ModelAndView("accounting/proses_trans_pre");
//	}
	
//	public ModelAndView pilih_pre(HttpServletRequest request,HttpServletResponse response) throws Exception {
//		Map map=new HashMap();
//		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
//		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
//		Integer p= ServletRequestUtils.getIntParameter(request, "p");
//		
//		map.put("nomor", nomor);
//		map.put("flag", flag);
//		map.put("p",p);
//		
//		return new ModelAndView("accounting/pilih_pre");
//	}
	
}