/**
 * 
 */
package com.ekalife.elions.web.finance;

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

import com.ekalife.elions.model.Reksadana;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentController;
import com.ekalife.utils.scheduler.unused.NabReksadanaScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author Yusuf
 * @since Dec 2, 2005
 */
public class UploadNabController extends ParentController{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		map.put("sysdate", elionsManager.selectSysdate());
		map.put("tanggalNAB", ServletRequestUtils.getStringParameter(request, "tanggalNAB", defaultDateFormat.format(elionsManager.selectSysdate(-1))));
		map.put("tanggal", ServletRequestUtils.getStringParameter(request, "tanggal", defaultDateFormat.format(elionsManager.selectSysdate())));
		map.put("tanggalAkhir", ServletRequestUtils.getStringParameter(request, "tanggalAkhir", defaultDateFormat.format(elionsManager.selectSysdate())));
		String modus = ServletRequestUtils.getStringParameter(request, "modus", "");
		HttpSession session = request.getSession();
		
		//tes
		String pt = ServletRequestUtils.getStringParameter(request, "textarea1", "");
		String cp = ServletRequestUtils.getStringParameter(request, "textarea2", "");
		String sh = ServletRequestUtils.getStringParameter(request, "textarea3", "");
		String ie = ServletRequestUtils.getStringParameter(request, "textarea4", "");
		
		//String URL_NAB_PENDAPATAN_TETAP = "http://www.infovesta.com/isd/infovesta/umum/reksa.jsp?tipe=pt";
		//String URL_NAB_PENDAPATAN_TETAP = "http://www.infovesta.com/isd/free/reksa2.jsp?tipe=pt";
		//String URL_NAB_CAMPURAN = "http://www.infovesta.com/isd/infovesta/umum/reksa.jsp?tipe=cp";
		//String URL_NAB_SAHAM = "http://www.infovesta.com/isd/infovesta/umum/reksa.jsp?tipe=sh";	
		String URL_NAB_TGL = "http://www.infovesta.com/isd/index.jsp";	
		
		if(modus.equals("tarik")) {
			NabReksadanaScheduler nab = new NabReksadanaScheduler();
			nab.setElionsManager(elionsManager);
			List<Map> nabReksadanaList = uwManager.selectInvReksadanaName();
			
			//String tgl = nab.processDataTgl(nab.getResponseFromWeb(URL_NAB_TGL));
			String tgl =  ServletRequestUtils.getStringParameter(request, "tanggalNAB");
			//String tgl = "26/03/2012";
			request.setAttribute("tgl", tgl);
			
			DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
			Date ird_trans_date = df.parse(tgl);
			//List<Reksadana> daftarReksa = nab.processData(nab.getResponseFromWeb());
			//logger.info(nab.getResponseFromWeb(URL_NAB_PENDAPATAN_TETAP));
			List<Reksadana> daftarPendTtp = nab.processData(pt, nabReksadanaList, ird_trans_date);
			List<Reksadana> daftarCamp = nab.processData(cp, nabReksadanaList, ird_trans_date);
			List<Reksadana> daftarSaham = nab.processData(sh, nabReksadanaList, ird_trans_date);
			List<Reksadana> daftarIndeksETF = nab.processData(ie, nabReksadanaList, ird_trans_date);
			
			session.setAttribute("daftarPendTtp", daftarPendTtp);
			session.setAttribute("daftarCamp", daftarCamp);
			session.setAttribute("daftarSaham", daftarSaham);
			session.setAttribute("daftarIndeksETF", daftarIndeksETF);
			
			//map.put("daftarReksa", daftarReksa);
		}else if(modus.equals("simpan")) {
			// tanggal start dan tanggal akhirnya?
			try {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				
				NabReksadanaScheduler nab = new NabReksadanaScheduler();
				nab.setElionsManager(elionsManager);
				List<Map> nabReksadanaList = uwManager.selectInvReksadanaName();
				List<Reksadana> daftarPendTtp = (List<Reksadana>) session.getAttribute("daftarPendTtp");
				List<Reksadana> daftarCamp = (List<Reksadana>) session.getAttribute("daftarCamp");
				List<Reksadana> daftarSaham = (List<Reksadana>) session.getAttribute("daftarSaham");
				List<Reksadana> daftarIndeksETF = (List<Reksadana>) session.getAttribute("daftarIndeksETF");
				
				/*String tgl = nab.processDataTgl(nab.getResponseFromWeb(URL_NAB_TGL));*/
				//String tgl = "26/03/2012";
				//String tgl = (String) request.getAttribute("tgl");
				String tgl = ServletRequestUtils.getRequiredStringParameter(request, "tanggalNAB");
				request.setAttribute("tgl", tgl);
				
				DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
				
				//Date ird_trans_date = df.parse(tgl);

				List<Reksadana> daftarReksa = new ArrayList<Reksadana>();
				daftarReksa.addAll(daftarPendTtp);
				daftarReksa.addAll(daftarCamp);
				daftarReksa.addAll(daftarSaham);
				daftarReksa.addAll(daftarIndeksETF);
				
				//Looping berdasarkan tanggal
				String tgl2 = null;
				int sukses = 0;
				int gagal = 0;
				int gagal2 = 0;
				Date tglAwal = null;
				Date tglAkhir = null;
				//Date ird_trans_date = tglAwal;
				
				Boolean chbox = ServletRequestUtils.getBooleanParameter(request,  "checkbox");
				//logger.info("CHBOX : "+chbox);
				if(chbox!=null){

					tglAwal = df.parse(ServletRequestUtils.getRequiredStringParameter(request, "tanggal"));
					tglAkhir = df.parse(ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"));
					//request.setAttribute("tgl", tgl);
					tgl2 = tglAwal+" s/d "+tglAkhir;
					
				}else{
					
					tglAwal = df.parse(tgl);
					tglAkhir = tglAwal;
					//request.setAttribute("tgl", tgl);
					tgl2 = ""+tglAwal;
				}
				
				do{
					Date ird_trans_date = tglAwal;
					logger.info("Trans Date : "+ird_trans_date);
		        	for(int j = 0 ; j < daftarReksa.size() ; j++){
		        				
        				double last_unit = uwManager.selectUnitTerakhir(daftarReksa.get(j).getIre_reksa_no(), ird_trans_date);
        				daftarReksa.get(j).setIrd_unit(last_unit);
		        		
		        	}
		        	
					int result = elionsManager.insertDetailReksadana(currentUser, FormatDate.add(ird_trans_date, Calendar.DATE, 0), daftarReksa);
					
					if(result == 0){
						gagal += 1;
						//request.setAttribute("successMessage", "Data nab reksadana untuk tanggal "+tgl+", sudah pernah tersimpan");
					}else if(result == -1){
						gagal2 += 1;
					}else{
						sukses += 1;
						//request.setAttribute("successMessage", "simpan nab reksadana untuk tanggal "+tgl+", SUKSES!!");
					}
					tglAwal = FormatDate.add(tglAwal, Calendar.DATE, 1);

				}while(tglAwal.compareTo(tglAkhir) <= 0);//End Looping
				
				if(sukses == 0){
					if(gagal != 0){
						request.setAttribute("successMessage", "Data NAB reksadana untuk tanggal "+tgl2+", sudah pernah tersimpan");
					}else if(gagal2 != 0){
						request.setAttribute("successMessage", "Data NAB reksadana untuk tanggal "+df.format(FormatDate.add(tglAwal, Calendar.DATE, -1))+", belum pernah tersimpan." +
								" Harap upload NAB pada tanggal tersebut terlebih dahulu");
					}
				}else{
					request.setAttribute("successMessage", "Simpan NAB reksadana untuk tanggal "+tgl2+", SUKSES!!");
				}
			} catch (Exception e) {
				request.setAttribute("successMessage", "Simpan NAB reksadana gagal dilakukan");
			}
			
		}else {
			session.removeAttribute("daftarPendTtp");
			session.removeAttribute("daftarCamp");
			session.removeAttribute("daftarSaham");
			session.removeAttribute("daftarIndeksETF");
		}
		
		//return new ModelAndView("finance/nab", "cmd", map);
		return new ModelAndView("finance/nab", map);
	}

}