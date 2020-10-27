package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;

/**
 * 
 * Controller Khusus untuk JSON
 * 
 * @author Deddy
 * @since Dec 7, 2011 (11:20:32 AM)
 *
 */
public class JSONMultiController extends ParentMultiController{

	private UwManager uwManager;
	private BacManager bacManager;
    
    public void setUwManager(UwManager uwManager) {
        this.uwManager = uwManager;
    }
    
    public void setBacManager(BacManager bacManager) {
        this.bacManager = bacManager;
    }

    /**
	 * Method khusus untuk return data dalam bentuk JSON (untuk ajax)
	 * 
	 * @author Deddy
	 * @since Dec 7, 2011
	 */
	public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tipe = ServletRequestUtils.getStringParameter(request, "type", "");
		
		Object result = null;
		
		if(tipe.equals("getSubProduk")){
			String tampung = request.getParameter("paramA");
			if(!request.getParameter("paramA").equals("")){ //berdasarkan aging
				result = ajaxManager.select_detilprodukutama(Integer.parseInt(ServletRequestUtils.getStringParameter(request,"paramA", "")));
			}
			
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.close();
		
		return null;
	}
	
	/**
	 * Select lus_id, lus_full_name dari eka.lst_user berdasarkan lus_full_name dalam bentuk json
	 * 
	 * @author Canpri
	 * @since Jan 09, 2014
	 */
	public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Object result = null;
		
		String term = ServletRequestUtils.getRequiredStringParameter(request, "term");
		result = elionsManager.selectDropDown("EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME || ' [' || LUS_FULL_NAME || ']'", "", "LUS_FULL_NAME", "upper(LUS_FULL_NAME) like '%"+term.toUpperCase()+"%' AND LUS_ACTIVE = 1");
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.close();
		
		return null;
	}
	
	/**
	 * Get List Cara Bayar using n_prod for E-Policy
	 * 
	 * @author Daru
	 * @since Sep 5, 2016
	 */
	public ModelAndView getListCaraBayar(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    String product = ServletRequestUtils.getStringParameter(request, "product", "");
	    
	    if(!"".equals(product.trim())) {
	        String namaProduk = "";
	        Integer pos = product.indexOf("X");
	        String lsbsId = product.substring(0, pos - 1);
	        String lsdbsNumber = product.substring(pos + 1, product.length());
	        
	        if(lsbsId.trim().length() == 1) {
	            namaProduk = "produk_asuransi.n_prod_0" + lsbsId;
	        } else {
	            namaProduk = "produk_asuransi.n_prod_" + lsbsId;
	        }
	        
	        try {
	            if(!lsbsId.trim().equals("0")) {
	                Class aClass = Class.forName(namaProduk);
	                n_prod produk = (n_prod) aClass.newInstance();
	                produk.setSqlMap(ajaxManager.getBacDao().getSqlMapClient());
	                
	                produk.of_set_bisnis_no(Integer.parseInt(lsdbsNumber));
	                Integer jmlCaraBayar = produk.indeks_li_pmode;
	                
	                Integer[] caraBayar = new Integer[jmlCaraBayar];
	                String[] namaCaraBayar = new String[jmlCaraBayar];
	                
	                for(int i = 1; i < jmlCaraBayar; i++) {
	                    caraBayar[i] = produk.li_pmode[i];
	                    namaCaraBayar[i] = ajaxManager.select_detilcrbayar(caraBayar[i]);
	                }
	                
	                map.put("caraBayar", caraBayar);
	                map.put("namaCaraBayar", namaCaraBayar);
	            }
	        } catch (Exception e) {
                logger.error("ERROR :", e);
            }
	    } else {
	        map.put("result", "error");
	        map.put("message", "Parameter required");
	    }
	    
	    response.setContentType("application/json");
	    PrintWriter writer = response.getWriter();
	    Gson gson = new Gson();
	    writer.print(gson.toJson(map));
	    writer.close();
	    
	    return null;
	}
	
	/**
	 * Get Premi using n_prod for E-Policy
	 * 
	 * @author Daru
	 * @since Sep 6, 2016
	 */
	public ModelAndView getPremi(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HashMap<String, Object> map = new HashMap<String, Object>();
        String product = ServletRequestUtils.getStringParameter(request, "product", "");
        String sBegDate = ServletRequestUtils.getStringParameter(request, "beg_date", "");
        String sBirthPp = ServletRequestUtils.getStringParameter(request, "birth_pp", "");
        String sBirthTt = ServletRequestUtils.getStringParameter(request, "birth_tt", "");
        Integer sex = ServletRequestUtils.getIntParameter(request, "sex", 0);
        String lkuId = ServletRequestUtils.getStringParameter(request, "lku_id", "01");
        Double up = ServletRequestUtils.getDoubleParameter(request, "up", 0.0);
        Integer lscbId = ServletRequestUtils.getIntParameter(request, "lscb_id", 0);
        Integer medical = ServletRequestUtils.getIntParameter(request, "medical", 0);
        
        if(!"".equals(product.trim()) && !"".equals(sBegDate.trim()) && !"".equals(sBirthPp.trim()) 
                && !"".equals(sBirthTt.trim())) {
            String namaProduk = "";
            Integer pos = product.indexOf("X");
            String lsbsId = product.substring(0, pos - 1);
            String lsdbsNumber = product.substring(pos + 1, product.length());
            
            if(lsbsId.trim().length() == 1) {
                namaProduk = "produk_asuransi.n_prod_0" + lsbsId;
            } else {
                namaProduk = "produk_asuransi.n_prod_" + lsbsId;
            }
            
            try {
                if(!lsbsId.trim().equals("0")) {
                    Class aClass = Class.forName(namaProduk);
                    n_prod produk = (n_prod) aClass.newInstance();
                    produk.setSqlMap(ajaxManager.getBacDao().getSqlMapClient());
                    
                    produk.of_set_bisnis_id(Integer.parseInt(lsbsId));
                    produk.of_set_bisnis_no(Integer.parseInt(lsdbsNumber));
                    
                    Calendar begDate = Calendar.getInstance();
                    begDate.setTime(defaultDateFormat.parse(sBegDate));
                    Calendar birthPp = Calendar.getInstance();
                    birthPp.setTime(defaultDateFormat.parse(sBirthPp));
                    Calendar birthTt = Calendar.getInstance();
                    birthTt.setTime(defaultDateFormat.parse(sBirthTt));
                    
                    f_hit_umur hitUmur = new f_hit_umur();
                    produk.of_set_usia_pp(hitUmur.umur(birthPp.get(Calendar.YEAR), birthPp.get(Calendar.MONTH), birthPp.get(Calendar.DATE), begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE)));
                    produk.of_set_usia_tt(hitUmur.umur(birthTt.get(Calendar.YEAR), birthTt.get(Calendar.MONTH), birthTt.get(Calendar.DATE), begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE)));
                    
                    produk.of_set_sex(sex);
                    produk.of_set_begdate(begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE));
                    produk.of_set_kurs(lkuId);
                    produk.of_set_up(new Double(up));
                    produk.of_set_pmode(lscbId);
                    produk.of_set_medis(medical);
                    produk.of_get_rate();
                    
                    Double premi = produk.of_get_premi();
                    
                    map.put("premi", premi);
                }
            } catch (Exception e) {
                logger.error("ERROR :", e);
            }
        } else {
            map.put("result", "error");
            map.put("message", "Invalid parameter");
        }
        
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        writer.print(gson.toJson(map));
        writer.close();
        
        return null;
	}
	
	/**
	 * Get Min UP using n_prod for E-Policy
	 * @author Daru
	 * @since Sep 16, 2016
	 */
	public ModelAndView getMinUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String product = ServletRequestUtils.getStringParameter(request, "product", "");
        String sBegDate = ServletRequestUtils.getStringParameter(request, "beg_date", "");
        String sBirthPp = ServletRequestUtils.getStringParameter(request, "birth_pp", "");
        String sBirthTt = ServletRequestUtils.getStringParameter(request, "birth_tt", "");
        Integer sex = ServletRequestUtils.getIntParameter(request, "sex", 0);
        String lkuId = ServletRequestUtils.getStringParameter(request, "lku_id", "01");
        Double premium = ServletRequestUtils.getDoubleParameter(request, "premium", 0.0);
        Integer lscbId = ServletRequestUtils.getIntParameter(request, "lscb_id", 0);
        Integer medical = ServletRequestUtils.getIntParameter(request, "medical", 0);
        
        if(!"".equals(product.trim()) && !"".equals(sBegDate.trim()) && !"".equals(sBirthPp.trim()) 
                && !"".equals(sBirthTt.trim())) {
            String namaProduk = "";
            Integer pos = product.indexOf("X");
            String lsbsId = product.substring(0, pos - 1);
            String lsdbsNumber = product.substring(pos + 1, product.length());
            
            if(lsbsId.trim().length() == 1) {
                namaProduk = "produk_asuransi.n_prod_0" + lsbsId;
            } else {
                namaProduk = "produk_asuransi.n_prod_" + lsbsId;
            }
            
            try {
                if(!lsbsId.trim().equals("0")) {
                    Class aClass = Class.forName(namaProduk);
                    n_prod produk = (n_prod) aClass.newInstance();
                    produk.setSqlMap(ajaxManager.getBacDao().getSqlMapClient());
                    
                    produk.of_set_bisnis_id(Integer.parseInt(lsbsId));
                    produk.of_set_bisnis_no(Integer.parseInt(lsdbsNumber));
                    
                    Calendar begDate = Calendar.getInstance();
                    begDate.setTime(defaultDateFormat.parse(sBegDate));
                    Calendar birthPp = Calendar.getInstance();
                    birthPp.setTime(defaultDateFormat.parse(sBirthPp));
                    Calendar birthTt = Calendar.getInstance();
                    birthTt.setTime(defaultDateFormat.parse(sBirthTt));
                    
                    f_hit_umur hitUmur = new f_hit_umur();
                    produk.of_set_usia_pp(hitUmur.umur(birthPp.get(Calendar.YEAR), birthPp.get(Calendar.MONTH), birthPp.get(Calendar.DATE), begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE)));
                    produk.of_set_usia_tt(hitUmur.umur(birthTt.get(Calendar.YEAR), birthTt.get(Calendar.MONTH), birthTt.get(Calendar.DATE), begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE)));
                    
                    produk.of_set_sex(sex);
                    produk.of_set_begdate(begDate.get(Calendar.YEAR), begDate.get(Calendar.MONTH), begDate.get(Calendar.DATE));
                    produk.of_set_kurs(lkuId);
                    produk.of_set_pmode(lscbId);
                    produk.of_set_medis(medical);
                    produk.of_set_premi(premium);
                    
                    map.put("minUp", produk.idec_up);
                }
            } catch (Exception e) {
                logger.error("ERROR :", e);
            }
        } else {
            map.put("result", "error");
            map.put("message", "Invalid parameter");
        }
        
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        writer.print(gson.toJson(map));
        writer.close();
        
        return null;
    }
	
	public ModelAndView submitPaBsm(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    String msp_id = ServletRequestUtils.getStringParameter(request, "msp_id", null);
	    
	    if("POST".equals(request.getMethod())) {
	        if(msp_id != null) {
	            ArrayList<Pas> listPas = (ArrayList<Pas>) uwManager.selectAllPasList(msp_id, "1", null, null, null, "pabsm", null, "pabsm", null, null, null);
	            
	            if(!Common.isEmpty(listPas)) {
	                Pas p = listPas.get(0);
	                Cmdeditbac edit = new Cmdeditbac();
	                p.setLus_id_uw_pas(0);
	                p.setLus_login_name("SYSTEM");
	                //langsung set ke posisi payment
	                p.setLspd_id(4);
	                BindException errors = new BindException(new HashMap(), "Pas");
	                User currentUser = new User();
	                currentUser.setLus_id("0");
	                currentUser.setLus_full_name("SYSTEM");
	                currentUser.setJn_bank(2);
	                edit = uwManager.prosesPas(request, "update", p, null, "input", currentUser, errors);
	                
	                
	                if(edit.getPemegang().getMspo_policy_no() == null) {
	                    map.put("result", "error");
	                    map.put("message", "Proses gagal.");
	                } else {
	                	
	                	//proses create PDF
	                	String pdfName = p.getNo_kartu();
	                	String no_polis_induk = null; 
	                	String nama_plan = null;
	                	List<Map>  kartu = bacManager.selectDetailKartuPasBSMIB(pdfName);
	                	if(!kartu.isEmpty()){
							Map m = (HashMap) kartu.get(0);
							no_polis_induk = m.get("NO_POLIS_INDUK").toString();
							nama_plan = m.get("NAMA_PLAN").toString();
	                	}	    				
  				
	    				
						try {
							ITextPdf.generateSertifikatPaBsmV2(p.getNo_kartu(), no_polis_induk, "73", "14", nama_plan, p.getMsp_up(), p.getMsp_premi(), elionsManager);
							
							String emailFrom = p.getMsp_pas_email();
							String[] emailBcc = new String[] {"ridhaal@sinarmasmsiglife.co.id","daru@sinarmasmsiglife.co.id"};
							String emailSubject = "Polis Personal Accident Risiko ABD";
							String emailMessage = "Nasabah Terhormat,<br><br>"+
	                                    "Selamat, Anda telah terdaftar sebagai pemegang polis asuransi Personal Accident Risiko ABD dari Sinarmas MSIG Life.<br>"+
	                                    "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
	                                    "Terima kasih.<br><br>"+
	                                    "Salam Hangat,<br>"+
	                                    "Sinarmas MSIG Life";
							
		                	String setUrlForPdf = props.getProperty("pdf.dir.export")+"\\bsm\\73\\";
		                	String fileName = setUrlForPdf+pdfName+".pdf";
		    				fileName = setUrlForPdf + no_polis_induk + "-" + "073" + "-" + pdfName + ".pdf";
		    				File file = new File(fileName);  
							List<File> attachments = new ArrayList<File>();
	                        attachments.add( file );
							
							EmailPool.send(bacManager.selectSeqEmailId(),"SMiLe E-Lions", 0, 0, 0, 0, 
	                                null, 0, 0, bacManager.selectSysdate(), null, true, "info@sinarmasmsiglife.co.id",                                             
	                                new String[]{emailFrom}, 
	                                null,
	                                emailBcc,
	                                emailSubject, 
	                                emailMessage,
	                                attachments,11);
							
							//test kirim email buat data UAT
//							email.send(true, "info@sinarmasmsiglife.co.id", new String[]{emailFrom}, null, emailBcc, 
//									emailSubject, 
//									emailMessage, 
//									attachments);
							
							
						} catch(Exception e) {
							FileInputStream in = null;
							ServletOutputStream ouputStream = null;
							ouputStream = response.getOutputStream();
							if(ouputStream != null) {
								ouputStream.flush();
							    ouputStream.close();							
							}							
							
							logger.error("ERROR :", e);
						}						
						
	                    map.put("result", "success");
	                    map.put("message", "Proses sukses.");
	                    map.put("spaj", edit.getPemegang().getReg_spaj());
	                    map.put("nopolis", edit.getPemegang().getMspo_policy_no());
	                }
	            } else {
	                map.put("result", "error");
	                map.put("message", "Data tidak ditemukan.");
	            }
	        } else {
	            map.put("result", "error");
	            map.put("message", "Invalid parameter.");
	        }
	    } else {
	        map.put("result", "error");
            map.put("message", "Bad request.");
	    }
	    
	    response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();
        writer.print(gson.toJson(map));
        writer.close();
        
        return null;
	}
	
}