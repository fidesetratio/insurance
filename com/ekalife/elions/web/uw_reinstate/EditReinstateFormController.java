package com.ekalife.elions.web.uw_reinstate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Edit;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EditReinstateFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		map.put("lsPilih",lsPilih());
		map.put("lsKurs",lsKurs());
		map.put("lsKondisi",lsKondisi());
		return map;
	}
	
	private List lsPilih(){
		Map param=new HashMap();
		List lsPilih=new ArrayList();
		param.put("ID","0");
		param.put("VALUE","Ditolak");
		lsPilih.add(param);
		param=new HashMap();
		param.put("ID","1");
		param.put("VALUE","Diterima");
		lsPilih.add(param);
		return lsPilih;
	}
	
	private List lsKurs(){
		List lsKurs=new ArrayList();
		Map param=new HashMap();
		param.put("ID","01");
		param.put("VALUE","Rp.");
		lsKurs.add(param);
		param=new HashMap();
		param.put("ID","02");
		param.put("VALUE","US$");
		lsKurs.add(param);
		return lsKurs;
		
	}
	
	private List lsKondisi(){
		List lsKondisi=new ArrayList();
		Map param=new HashMap();
		param.put("ID","0");
		param.put("VALUE","Kondisi Semula");
		lsKondisi.add(param);
		param=new HashMap();
		param.put("ID","1");
		param.put("VALUE","Kondisi Khusus");
		lsKondisi.add(param);
		return lsKondisi;
		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		Command command=new Command();
		Edit edit=new Edit();
		//setValidateOnBinding(true);
		String nomor,spaj,reinsNo;
		nomor=request.getParameter("nomor");
		spaj=nomor.substring(0,nomor.indexOf("-"));
		reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());

		edit=elionsManager.selectSuratKonfirmasi(spaj,reinsNo);
		Integer li_print=elionsManager.selectMstUwReinstateMsurPrint(spaj,reinsNo);
		int info=0;
		User currentUser = (User) request.getSession().getAttribute("currentUser");        
		if(li_print!=null){
			if(li_print.intValue()==1){
				info=1;
			}
		}else
			info=2;
		
		command.setSpaj(spaj);
		command.setReins(reinsNo);
		command.setEdit(edit);
		command.setFlagAdd(info);
		return command;
	}
	
	
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		if(request.getParameter("proses").equals("1")){//edit
			err.reject("","Berhasil Diubah, Silahkan Tekan Tombol Save Untuk melakukan perubahan");
		}
			
	}	
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Edit edit=new Edit();
		edit=command.getEdit();
		String flag=request.getParameter("selectAksep");
		Integer aksep=Integer.valueOf(flag);
		String tgl_paid=request.getParameter("tgl_paid");
		String kondisi=request.getParameter("selectKondisi");
		String terimaNote=request.getParameter("terimaNote");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
//		if(edit.getMsur_total_unbayar().isNaN())
//			err.reject("","Isi Total Premi Tertunggak Dengan Benar");
//			
//		if(edit.getMsur_total_bunga_unbayar().isNaN())
//			err.reject("","Isi Total Bunga Tertunggak Dengan Benar");
		
		Date dtTglPaid,dtTglAksep;
		dtTglPaid=new Date(defaultDateFormat.parse(tgl_paid).getTime());
		
		//logger.info(dtTglPaid);
		
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_total_unbayar());
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_total_bunga_unbayar());
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_tanggal_acc());
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_tgl_batas_paid());
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_kondisi_polis());
		if(logger.isDebugEnabled())logger.debug(edit.getMsur_kondisi_note());
		
		if(flag.equals("0")){//ditolak
			String tolakNote=request.getParameter("tolakNote");
			edit.setMsur_accept(new Integer(0));
			edit.setMsur_accept_note(tolakNote);
			edit.setMsur_total_unbayar(edit.getMsur_total_unbayar());
			edit.setMsur_total_bunga_unbayar(edit.getMsur_total_bunga_unbayar());
			edit.setMsur_total(new Double(edit.getMsur_total_unbayar().doubleValue()+edit.getMsur_total_bunga_unbayar().doubleValue()));
			edit.setMsur_tgl_batas_paid(dtTglPaid);
			elionsManager.prosesEditSurat(edit.getReg_spaj(),edit.getMsrt_reinstate_no(),aksep,null,null,edit.getMsur_total_unbayar(),
					null,edit.getMsur_total_bunga_unbayar(),dtTglPaid,tolakNote, currentUser.getLus_id());
		}else if(flag.equals("1")){//diterima
			String tglAksep=request.getParameter("tglAksep");
			dtTglAksep=new Date(defaultDateFormat.parse(tglAksep).getTime());
			edit.setMsur_accept(new Integer(1));
			edit.setMsur_tanggal_acc(dtTglAksep);
			if(kondisi.equals("0")){//kondisi semula
				edit.setMsur_kondisi_polis(new Integer(0));
				
			}else if(kondisi.equals("1")){//kondisi khusus
				edit.setMsur_kondisi_polis(new Integer(1));
				edit.setMsur_kondisi_note(terimaNote);
			}
			edit.setMsur_total_unbayar(edit.getMsur_total_unbayar());
			edit.setMsur_total_bunga_unbayar(edit.getMsur_total_bunga_unbayar());
			edit.setMsur_total(new Double(edit.getMsur_total_unbayar().doubleValue()+edit.getMsur_total_bunga_unbayar().doubleValue()));
			edit.setMsur_tgl_batas_paid(dtTglPaid);
			elionsManager.prosesEditSurat(edit.getReg_spaj(),edit.getMsrt_reinstate_no(),aksep,Integer.valueOf(kondisi),edit.getMsur_kondisi_note(),edit.getMsur_total_unbayar(),
					dtTglAksep,edit.getMsur_total_bunga_unbayar(),dtTglPaid,null, currentUser.getLus_id());

			
		}
		return new ModelAndView("uw_reinstate/edit_report", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	
	
	
}
