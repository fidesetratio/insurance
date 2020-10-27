package com.ekalife.elions.web.bas;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.btpp.Btpp;
import com.ekalife.elions.model.btpp.CommandBtpp;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

public class btppMultiController extends ParentMultiController {
	
	public ModelAndView Btpp_main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		List lsBtpp=elionsManager.selectAllMstBtpp("","1",null,currentUser.getLca_id());
		map.put("lsBtpp",lsBtpp);
		return new ModelAndView("bas/Btpp_main",map);
	}
	
	//TODO: Blom beres niy
	public ModelAndView cari_nomorBtpp(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Map<String, Object> map=new HashMap<String, Object>();
	User currentUser=(User)request.getSession().getAttribute("currentUser");
	String value=request.getParameter("value");
	String tipe=request.getParameter("tipe");
	String filter=request.getParameter("filter");
	
	if(tipe==null)
		tipe="1";
	if(filter==null)
		filter="LIKE%";
	
	List lsBtpp=elionsManager.selectAllMstBtpp(value,tipe,filter,currentUser.getLca_id());
	map.put("lsBtpp",lsBtpp);
	map.put("filter",filter);
	map.put("tipe", tipe);
	map.put("value", value);
	return new ModelAndView("bas/cari_nomorBtpp",map);
	}
	
	public ModelAndView show_btpp(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		CommandBtpp  commandBtpp = new CommandBtpp();
		String nomor=request.getParameter("nomor");
		List lsBtpp=elionsManager.selectMstBtpp(nomor,"1",null,currentUser.getLca_id());
		
		Date today=elionsManager.selectSysdate();
		commandBtpp.setTglRk(today);
		commandBtpp.setS_tgl_rk(FormatDate.toIndonesian(today));
		if(lsBtpp.isEmpty()== false){
			Btpp btpp = (Btpp)lsBtpp.get(0);
			btpp.setMst_no_btpp(btpp.getMst_no());
			String mst_noNew= elionsManager.selectMstNoNew(btpp.getMst_no_btpp());
			if (btpp.getMst_flag_batal()==5){
				btpp.setMst_no_reff_btl(nomor);
				btpp.setMst_no(mst_noNew);
			}
			if (btpp.getMst_flag_batal()==1){
				btpp.setMst_no_reff_btl(nomor);
			}
			map.put("lsBtpp", lsBtpp);
			map.put("btpp", btpp);
			map.put("commandBtpp", commandBtpp);
			return new ModelAndView("bas/show_btpp",map);
		}
		if(lsBtpp.isEmpty())
			return new ModelAndView("bas/show_btpp",map);
		
		return new ModelAndView("bas/show_btpp",map);
	}

	public ModelAndView lap_btpp(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		BindException err;
		
		
		Map<String, Object> map=new HashMap<String, Object>();
		Date today=elionsManager.selectSysdate();
		CommandBtpp  commandBtpp = new CommandBtpp();
		err = new BindException(bindAndValidate(request, commandBtpp, true));
//		commandBtpp.setSprde_byr_awal("00/00/0000");
//		commandBtpp.setSprde_byr_akhr("00/00/0000");
//		String sprde_byr_awal= request.getParameter("periodeAwal");
		
//		String periodeAwal=request.getParameter("periodeAwal");
		String periodeAwal="03/09/2007";
//		String periodeAkhir=request.getParameter("periodeAkhir");
		String periodeAkhir ="15/09/2007";
//		if(! periodeAwal.equals("00/00/0000")){
//			Calendar calRk=new GregorianCalendar(Integer.parseInt(periodeAwal.substring(6)),
//									Integer.parseInt(periodeAwal.substring(3,5))-1,
//									Integer.parseInt(periodeAwal.substring(0,2)));;
//			commandBtpp.setTglRk(calRk.getTime());
//			commandBtpp.setS_tgl_rk(defaultDateFormat.format(calRk.getTime()));
//			
//		}
		if(request.getParameter("btnOk") != null) {
			map.put("periodeAwal", periodeAwal);
			map.put("periodeAkhr", periodeAkhir);
//			elionsManager.selectPeriode(map);
			return new ModelAndView("bas/lap_btpp",map);
			
		} 
		return new ModelAndView("bas/lap_btpp",map);
		}
}