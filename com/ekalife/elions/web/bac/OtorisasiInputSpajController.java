package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.PrintWriter;
import java.text.DateFormat;
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

import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_auto_debet;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.parent.ParentMultiController;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * @author Yusuf
 * @since Jan 11, 2006
 */
public class OtorisasiInputSpajController extends ParentMultiController{

	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        //hanya IT dan UW
        if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
            double angka = (double) currentUser.getScreenWidth() / (double) currentUser.getScreenHeight();
            if(angka > 1.4) {
            	map.put("wideScreen", true);
            }
        }

        //dept yg boleh buka payment?
		if(this.props.getProperty("access.viewer.payment").indexOf(currentUser.getLde_id())==-1) {
			map.put("paymentDisabled", "disabled");
		}
		
		//user yg boleh buka tombol CS Call dan CS Summary?
		//if(this.props.getProperty("access.cs.summary").indexOf(currentUser.getLus_id())>-1) {
		//	map.put("summaryEnabled", "enabled");
		//}
		//user yg boleh buka tombol CS Summary saja?
		//if(this.props.getProperty("access.cs.call").indexOf(currentUser.getLus_id())>-1) {
		if("01,11,12,27,04".indexOf(currentUser.getLde_id())==-1) {
			map.put("viewDokumenPolis", "disabled");
		}
		if("12".indexOf(currentUser.getLde_id())>-1) {
			map.put("callEnabled", "enabled");
			map.put("summaryEnabled", "enabled");
			int jumlahCustomerYangHarusDiFollowUp = this.uwManager.selectCsfCallReminder();
			if(jumlahCustomerYangHarusDiFollowUp > 0) map.put("showCsfReminder", "true");
		}
		
		return new ModelAndView("bac/otorisasiInputSpaj", "cmd", map);
		
//		if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3){
//			return new ModelAndView("uw/viewer/viewer_sekuritas", "cmd", map);
//		}else if(currentUser.getLde_id().equals("19") || currentUser.getLde_id().equals("20")) {
//			return new ModelAndView("uw/viewer/viewer_cabang", "cmd", map);
//		}else {
//			return new ModelAndView("uw/viewer/viewer", "cmd", map);
//		}
	}



}