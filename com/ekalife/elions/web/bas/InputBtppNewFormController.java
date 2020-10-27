package com.ekalife.elions.web.bas;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.btpp.Btpp;
import com.ekalife.elions.model.btpp.CommandBtpp;
import com.ekalife.elions.model.tts.PolicyTts;
import com.ekalife.utils.parent.ParentFormController;

public class InputBtppNewFormController extends ParentFormController {

	SimpleDateFormat dd=new SimpleDateFormat("dd");
	SimpleDateFormat mm=new SimpleDateFormat("MM");
	SimpleDateFormat yyyy=new SimpleDateFormat("yyyy");
	DecimalFormat f3=new DecimalFormat("000");
	NumberFormat decRound2= new DecimalFormat("#.00;(#,##0.00)"); //
	NumberFormat decRound3= new DecimalFormat("#.000;(#,##0.000)"); //
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		List lsKurs=elionsManager.selectAllLstKurs();//nebeng bac krn bisa dipake umum
		List<String> value=new ArrayList<String>();//(tunai,cek,giro,credit card,tahapan);
		value.add("1");
		value.add("2");
		//value.add("3");
		value.add("6");
		value.add("11");
		
		List lsCaraBayar=elionsManager.selectInLstPaymentType(value);//nebeng punya tts krn umum
		map.put("lsKurs",lsKurs);
		map.put("lsCaraBayar",lsCaraBayar);
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		CommandBtpp commandBtpp=new CommandBtpp();
		commandBtpp.setDesc(request.getParameter("desc"));
		User currentUser = (User) session.getAttribute("currentUser");
		List lsPembayaran=new ArrayList();
		int info=0;
		
		String proses=request.getParameter("proses");
		
		commandBtpp.setS_tgl_setor("00/00/0000");
		commandBtpp.setSprde_byr_awal("00/00/0000");
		commandBtpp.setSprde_byr_akhr("00/00/0000");
		
		Btpp btpp = new Btpp();
		btpp.setTot_byr(0.0);
		commandBtpp.setBtpp(btpp);

		if(proses.equals("1") || proses.equals("4")){//input atau batal
			String nomor=request.getParameter("nomor");
			commandBtpp.getBtpp().setLst_kd_cab(currentUser.getLca_id());
			commandBtpp.getBtpp().setLst_nm_admin(currentUser.getLus_full_name());
			commandBtpp.getBtpp().setLst_nm_cab(elionsManager.selectLstCabangNamaCabang(currentUser.getLca_id()));
			commandBtpp.setMst_no(nomor);
			if(proses.equals("4")){//untuk reff nomor btpp sebelummhya
					String alasanBatal=request.getParameter("alasan"); //alasan pembatalan
					nomor=request.getParameter("nomor");
					commandBtpp.setMstNoBatal(nomor);
					commandBtpp.setAlasanBatal(alasanBatal);
					Date today=elionsManager.selectSysdate();
					List lsBtpp=elionsManager.selectAllMstBtpp(nomor,"1",null,currentUser.getLca_id());
					
					Integer counter= elionsManager.selectCountHist(nomor);
					btpp=(Btpp)lsBtpp.get(0);
                    btpp.setMst_flag_batal(1);
                    btpp.setMst_no_reff_btl(commandBtpp.getMstNoBatal());
					elionsManager.updateMstBtppFlagbatal(nomor);
					elionsManager.insertLstHistoryPrint(nomor, null, commandBtpp.getBtpp().getLst_kd_cab(), "Membatalkan BTPP, Belum melakukan penginputan ulang", null, btpp.getMst_flag_batal());
					info=cek_userRight(btpp,currentUser,1);
					if(btpp.getMst_flag_batal()==1)
						info=3;
					else if(info==2)//klo sudah print bisa batal
						info=0;
					commandBtpp.setMst_no(nomor);
					btpp.setMst_no("");
					btpp.setKd_agen("");
					btpp.setNm_penutup("");				
					btpp.setReg_spaj("");
					btpp.setMst_nm_pemegang("");
					btpp.setSprde_byr_awal("00/00/0000");
					btpp.setSprde_byr_akhr("00/00/0000");
					btpp.setBiaya_polis(00.00);
					btpp.setExtra_premi(00.00);
					btpp.setPremi(00.00);
					btpp.setTot_byr(00.00);
					btpp.setUp(00.00);
					commandBtpp.setBtpp(btpp);
					
					
					if (counter >=0){
						elionsManager.updateMstBtppFlagbatal1(nomor);
//						elionsManager.insertLstHistoryPrint(nomor+"B","",commandBtpp.getBtpp().getLst_kd_cab(), "Penginputan ulang setelah batal");
						nomor=request.getParameter("nomor");
						commandBtpp.getBtpp().setLst_kd_cab(currentUser.getLca_id());
						commandBtpp.getBtpp().setLst_nm_admin(currentUser.getLus_full_name());
						commandBtpp.getBtpp().setLst_nm_cab(elionsManager.selectLstCabangNamaCabang(currentUser.getLca_id()));
						commandBtpp.setMst_no(nomor);
					}
					
			
				}	
				
			Date today=elionsManager.selectSysdate();
			commandBtpp.setMst_flag_batal(btpp.getMst_flag_batal());
			commandBtpp.setTglRk(today);
			commandBtpp.setPrde_byr_awal(today);
			commandBtpp.setPrde_byr_akhr(today);
			commandBtpp.setS_tgl_rk(defaultDateFormat.format(today));
			commandBtpp.setSprde_byr_awal(defaultDateFormat.format(today));
			commandBtpp.setSprde_byr_akhr(defaultDateFormat.format(today));

		}else if(proses.equals("2") || proses.equals("3")){//input atau batal
			String nomor=request.getParameter("nomor");
			nomor=nomor.substring(0,nomor.indexOf("~"));
			
			List lsBtpp=elionsManager.selectAllMstBtpp(nomor,"1",null,currentUser.getLca_id());	
			commandBtpp.setMst_no(nomor);
			//btpp=(Btpp)lsBtpp.get(0);
			Date ldTglSetor=elionsManager.selectMstBtppTglSetor(commandBtpp.getMst_no());
			SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
			btpp=new Btpp();
			if(lsBtpp.isEmpty()==false)
				btpp=(Btpp)lsBtpp.get(0);
			if(proses.equals("2")){
				info=cek_userRight(btpp,currentUser,0);
				btpp=(Btpp)lsBtpp.get(0);
				btpp.setMst_flag_batal(0);
				btpp.setMsag_id(btpp.getKd_agen());
				btpp.setMcl_first(btpp.getNm_penutup());
				
				String tgl_rk= elionsManager.selectTglRk(nomor);
				String prd_awal= elionsManager.selectperiodeAwal(nomor);
				String prd_akhr= elionsManager.selectperiodeAkhr(nomor);
				
				btpp.setS_tgl_rk(tgl_rk);
				btpp.setSprde_byr_akhr(prd_akhr);
				btpp.setSprde_byr_awal(prd_awal);
				
			}else if (proses.equals("3")){
				info=cek_userRight(btpp,currentUser,1);
				btpp.setMst_flag_batal(1);
				btpp.setMcl_id("");
				btpp.setMcl_first("");
				
				
			}
			if(btpp.getMst_flag_batal()==1)
				info=3;
			else if(info==2)//klo sudah print bisa batal
				info=0; 
			btpp.setNama(btpp.getMst_nm_pemegang());

			commandBtpp.setBtpp(btpp);
		}
		commandBtpp.setInfo(new Integer(info));
		commandBtpp.setProses(Integer.valueOf(proses));
		
		return commandBtpp;
	}
	
	private int cek_userRight(Btpp btpp, User currentUser,Integer n) {
		int info=0;
		if( (btpp.getLst_kd_cab().equals(currentUser.getLca_id())) &&
				(Integer.parseInt(currentUser.getLus_id())==btpp.getLus_id()))
				info=0;
		else
			info=1;
		
		if(n==1){//untuk edit tanggal setor, batal
			if(btpp.getMst_flag_batal()==1)
				info=3;
		}else{//cetak,edit btpp
			if (btpp.getFlag_print().intValue()==1)//jika sudah print gak bisa edit
				info=2;
			else if(btpp.getMst_flag_batal()==1)
				info=3;
		}	
		return info;
	}

	private void jejak(Btpp btpp, User currentUser){
		
	}
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandBtpp commandBtpp= (CommandBtpp)cmd;
		
		Btpp btpp =commandBtpp.getBtpp();
		
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String nomor=request.getParameter("nomor");
		Integer suc=null;
		String flag=btpp.getFlag();
		String nopolis=request.getParameter("nopolis");
		if(commandBtpp.getProses().intValue()==1||commandBtpp.getProses().intValue()==2){
		btpp.setMcl_id(btpp.getMcl_id());
		btpp.setMcl_first(btpp.getMcl_first());
		String tglRk=request.getParameter("tanggalSetor");
		if(! tglRk.equals("00/00/0000")){
			Calendar calRk=new GregorianCalendar(Integer.parseInt(tglRk.substring(6)),
									Integer.parseInt(tglRk.substring(3,5))-1,
									Integer.parseInt(tglRk.substring(0,2)));;
			commandBtpp.setTglRk(calRk.getTime());
			commandBtpp.setS_tgl_rk(defaultDateFormat.format(calRk.getTime()));
			
		}else
			err.reject("","Silahkan Masukan Tanggal Bayar yang benar");
		
		String sprde_byr_awal= request.getParameter("periodeAwal");
		if (! sprde_byr_awal.equals("00/00/0000")){
			Calendar calRk=new GregorianCalendar(Integer.parseInt(sprde_byr_awal.substring(6)),
					Integer.parseInt(sprde_byr_awal.substring(3,5))-1,
					Integer.parseInt(sprde_byr_awal.substring(0,2)));;
					commandBtpp.setPrde_byr_awal(calRk.getTime());
					commandBtpp.setSprde_byr_awal(defaultDateFormat.format(calRk.getTime()));
		}else
			err.reject("","Silahkan Masukan Periode bayar awal");
		
		
		String sprde_byr_akhr= request.getParameter("periodeAkhir");
		if (! sprde_byr_akhr.equals("00/00/0000")){
			Calendar calRk=new GregorianCalendar(Integer.parseInt(sprde_byr_akhr.substring(6)),
					Integer.parseInt(sprde_byr_akhr.substring(3,5))-1,
					Integer.parseInt(sprde_byr_akhr.substring(0,2)));;
					commandBtpp.setPrde_byr_akhr(calRk.getTime());
					commandBtpp.setSprde_byr_akhr(defaultDateFormat.format(calRk.getTime()));
		}else
			err.reject("","Silahkan Masukan Periode bayar akhir");
		
		btpp.setTot_byr(btpp.getPremi()+ btpp.getExtra_premi()+ btpp.getBiaya_polis());
		
		}
		if (commandBtpp.getProses().intValue()==4){
			btpp.setMcl_id("");
			btpp.setMcl_first("");
			
		}
	}
	

	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandBtpp commandBtpp=(CommandBtpp)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String nomor=null;
		Integer suc=null;
		List lsPolisNew=commandBtpp.getLsPolis();
//		Map map=selectPolis(lsPolisNew, request,lsPolisNew.size());
//		lsPolisNew=(List)map.get("lsPolis");
		
		
//		List lsPolisNew=commandBtpp.getLsPolis();
//		Map map=selectPolis(lsPolisNew, request,lsPolisNew.size());
//		lsPolisNew=(List)map.get("lsPolis");
		
//		commandBtpp.setLsPolis(lsPolisNew);
		if(commandBtpp.getProses().intValue()==1 || commandBtpp.getProses().intValue()==4){//input atau batal
			suc=new Integer(1);
			if(commandBtpp.getProses().intValue()==1)
				commandBtpp.setDesc("Input BTPP");
			else
				commandBtpp.setDesc("Input BTPP Dengan Alasan Batal: " +commandBtpp.getAlasanBatal());	
				nomor=elionsManager.prosesInputMstBtpp(commandBtpp,currentUser.getLus_id());
				Integer flg_batal = elionsManager.selectflagBatal(nomor);
				
				return new ModelAndView("bas/input_btppNew",err.getModel()).addObject("submitSuccess", suc).addObject("nomor",nomor).addAllObjects(this.referenceData(request,cmd,err));
					
		}else if(commandBtpp.getProses().intValue()==2){//edit
			suc=new Integer(2);
			nomor=elionsManager.prosesInputMstBtpp(commandBtpp,currentUser.getLus_id());
		}
		return new ModelAndView("bas/input_btppNew",err.getModel()).addObject("submitSuccess", suc).addObject("nomor",nomor).addAllObjects(this.referenceData(request,cmd,err));
			
	}
	
	
	
	public Map selectPolis(List lsPolisNew,HttpServletRequest request,int count){
		Map<String, Object> map=new HashMap<String, Object>();
		String[] pil ;
		String pil2;
		if(lsPolisNew!=null)
			pil=new String[lsPolisNew.size()];
		else
			pil=new String[0];
		
		int byk=0;
		
		pil=request.getParameterValues("pil");
		pil2=request.getParameter("pil");
		if(pil!=null && count==lsPolisNew.size())
			for(int i=0;i<lsPolisNew.size();i++ ){
				PolicyTts polTts=(PolicyTts)lsPolisNew.get(i);
				if(pil[i].equals("1")){
					polTts.setPil("1");
					byk++;
				}else
					polTts.setPil("0");
				lsPolisNew.set(i, polTts);
		}else{
			if(pil2!=null)
			if(pil2.equals("1")){
				PolicyTts polTts=(PolicyTts)lsPolisNew.get(0);
				polTts.setPil("1");
				lsPolisNew.set(0,polTts);
				byk=1;
			}
		}
		map.put("lsPolis", lsPolisNew);
		map.put("byk", new Integer(byk));
		
		return map;

	}
	
}
