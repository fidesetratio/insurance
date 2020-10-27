package com.ekalife.elions.web.filing;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Filing;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.parent.ParentMultiController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FilingFormController extends ParentFormController{
	protected final Log logger = LogFactory.getLog( getClass() );
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
		List lstBox;
		Map map=new HashMap();
		lstBox = uwManager.selectNoBoxFromMBox(11);
		map.put("lstBox", lstBox);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String spajOrnopolis=ServletRequestUtils.getStringParameter(request,"reg_spaj","");
		if(!spajOrnopolis.equals("")){
			Filing filing = uwManager.selectSpajOrNoPolis(spajOrnopolis);
			command.setFiling(filing);
		}
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Date sysdate = elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		
		String spaj = ServletRequestUtils.getStringParameter(request,"reg_spaj","");
		
		if(request.getParameter("cari") != null || request.getParameter("submitMode") != "") {
			if(spaj.equals("") || spaj==null){
				err.reject("", "Silakan Masukkan No SPAJ/ No Polis terlebih dahulu");
			}else {
				List<Filing> countSPAJInFiling = uwManager.selectFilterSPAJInFilling(spaj);
				if(countSPAJInFiling.size()>0){
					err.reject("","No SPAJ : "+countSPAJInFiling.get(0).getReg_spaj()+" /Polis : "+countSPAJInFiling.get(0).getMspo_policy_no()+" sudah pernah di Filing di Box : "+countSPAJInFiling.get(0).getKd_box()+" urutan : "+countSPAJInFiling.get(0).getKd_bundle()+ " pada tanggal : "+FormatDate.toIndonesian(countSPAJInFiling.get(0).getTgl_created()));
					command.setFiling(null);
				}else {
					if(uwManager.selectCountSpajAfterAcceptInFilling(spaj)==0){
						err.reject("","No SPAJ / No Polis harus melewati tahapan Akseptasi terlebih dahulu");
					}else {
						Filing filing = new Filing();
						filing = uwManager.selectSpajOrNoPolis(spaj);
						filing.setNama_bundle("NB_" + filing.getMspo_policy_no());
						filing.setTgl_created(sysdate);
						filing.setTgl_aktif(sysdate);
						filing.setSts_dokumen("NEW ENTRY");
						filing.setPos_dokumen("ADMIN");
						filing.setRequest_by(currentUser.getLus_id());
						//String flag_tgl_destroyed = ServletRequestUtils.getStringParameter(request, "tgl", defaultDateFormat.format(FormatDate.add(filing.getMste_end_date(), Calendar.YEAR, +5)));
						filing.setFlag_tgl_destroyed(defaultDateFormat.format(FormatDate.add(filing.getMste_end_date(), Calendar.YEAR, +5)));
						command.setFiling(filing);
						
						err.reject("", "Tampilkan Detail Informasi");
					}
				}
				
			}
		}else {
			//String tgl_destroyed = ServletRequestUtils.getStringParameter(request,"tgl","");
			if(!command.getFiling().getFlag_tgl_destroyed().equals("")){
				String flag_tgl_destroyed = ServletRequestUtils.getStringParameter(request, "tgl","");
				command.getFiling().setTgl_destroyed_plan(formatDate.parse(flag_tgl_destroyed));
				command.getFiling().setTgl_destroyed(formatDate.parse(flag_tgl_destroyed));
				command.getFiling().setFlag_tgl_destroyed(flag_tgl_destroyed);
			}
			logger.info(command.getFiling().getFlag_tgl_destroyed());
			if(command.getFiling().getKd_box().equals("") || command.getFiling().getKd_box()==null){
				err.reject("","Silakan pilih box dahulu.Apabila tidak ada, silakan create box terlebih dahulu");
			}
			
			if(command.getFiling().getFlag_tgl_destroyed().equals("")){
				err.reject("","Tanggal Pemusnahan harus diisi");
			}
				
		}
		
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!=null){
			uwManager.prosesFilingUW(command.getFiling());
			String pesanSuccess= "Penginputan Filing untuk SPAJ "+command.getFiling().getReg_spaj()+" berhasil dilakukan. Kode file : "+command.getFiling().getKd_file();
			command.getFiling().setSuccess(pesanSuccess);
		}
		
		return new ModelAndView("filing/uw",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}
