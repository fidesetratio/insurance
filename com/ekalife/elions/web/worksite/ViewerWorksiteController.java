package com.ekalife.elions.web.worksite;


import java.io.File;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;

import com.ekalife.elions.model.CekValidPrintPolis;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.ekalife;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ViewerWorksiteController  extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("worksite/worksite");
	}
	
	public ModelAndView daftar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		map.put("listperusahaan", listperusahaan);
		return new ModelAndView("worksite/viewer_worksite", map);
	}
	
	   public ModelAndView list_summary_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception
	    {
	        logger.info( "ViewerWorksiteController.list_summary_komisi" );
	        Map<String, Object> map = new HashMap<String, Object>();

	        String monthFrom = request.getParameter("monthFrom");
	        String yearFrom = request.getParameter("yearFrom");
	        String bank = request.getParameter("bank");
	        String nama = request.getParameter("nama");
	        String showButton = request.getParameter("showButton");
	        String transfer = request.getParameter("transfer");
	        
	        String periode_from = yearFrom + monthFrom;

	        Map<String, String> m;
	        List<Map<String, String>> transferList = new ArrayList<Map<String, String>>();
	        m = new HashMap<String, String>();
	        m.put("VALUE", "Sudah Transfer");
	        m.put("ID", "1");
	        transferList.add(m);
	        m = new HashMap<String, String>();
	        m.put("VALUE", "Belum Transfer");
	        m.put("ID", "2");
	        transferList.add(m);
	        
	        map.put("transferList", transferList );
	        map.put("monthList", DateUtil.selectMonth() );
	        map.put("yearList", DateUtil.selectYearFromFewYearsAgoTillNow( 10, false ) );
	        map.put("monthFrom", monthFrom );
	        map.put("yearFrom", yearFrom );
	        map.put("bank", bank );
	        m.put( "nama", nama );
	        map.put("transfer", transfer );
	        Map<String, Object> params = new HashMap<String, Object>();
	        if( bank != null && "".equals( bank.trim() ) ) { bank = null; }
	        params.put( "periode", periode_from );
	        params.put( "bank", bank );
	        params.put( "nama", nama );
	        Map<String, Object> closingDetail = elionsManager.selectUnderTablePeriode();
	        Date dayClosing = FormatDate.toDateWithSlash(closingDetail.get("CLOSING_DATE").toString());
	        Date selectNowDate = elionsManager.selectNowDate();
	        dayClosing.setMonth(dayClosing.getMonth()+1);
	        DateFormat df = new SimpleDateFormat("dd");
	        DateFormat mf = new SimpleDateFormat("MM");
	        DateFormat yf = new SimpleDateFormat("yyyy");
	        String nowDay = df.format(selectNowDate);
	        String closingMonth = mf.format(dayClosing);
	        String closingYear = yf.format(dayClosing);
	        String nowYear = yf.format(selectNowDate);
	        String month = monthFrom;
	        String year = yearFrom;
	        if( monthFrom == null ) { month = "01";}
	        if( yearFrom == null) {year = nowYear;}
	        Date filterDate = FormatDate.toDateWithSlash(nowDay+"/"+month+"/"+year);
	        Integer flag = null;
	        if( LazyConverter.toInt(year) >= LazyConverter.toInt(closingYear) ){
	        	if( LazyConverter.toInt(year) == LazyConverter.toInt(closingYear) ){
	        		 if( filterDate.after( dayClosing ))
		    	        {
	        			 if( monthFrom != null ){
	        			  	String alert = "Closing utk periode ini blm diproses!";
		    	        	map.put( "alert", alert );
	        			 }
		    	        }else{
		    	        	 if( showButton != null && "true".equals(showButton)){
			    		        	flag = 1;
			    		        	List<Map<String, Object>> listSummaryKomisi = uwManager.selectListSummaryKomisi( periode_from, bank, transfer ,nama);
			    		        	map.put( "listSummaryKomisi", listSummaryKomisi );
			    		        }
		    	        }
	        	}else{
	        		if( monthFrom != null ){
	        		String alert = "Closing utk periode ini blm diproses!";
    	        	map.put( "alert", alert );
	        		}
	        	}
	    	       
	        }
	        else{
	        	if( showButton != null && "true".equals(showButton)){
		        	flag = 1;
		        	List<Map<String, Object>> listSummaryKomisi = uwManager.selectListSummaryKomisi( periode_from, bank, transfer,nama );
		        	map.put( "listSummaryKomisi", listSummaryKomisi );
		        }
	        }


//	        for( Map<String, Object> contentMap : listSummaryKomisi )
//	        {
//	            String fileName = ( String ) contentMap.get( "FILE_NAME" );
//	            String[] fileNameArr = new String[]{};
//	            if( fileName != null ) fileNameArr = fileName.replace( " ", ""  ).split( ";" );
//	            contentMap.put( "FILE_NAME_ARRAY", fileNameArr );
//	        }

	       map.put("flag", flag);
	        return ( new ModelAndView("worksite/list_summary_komisi", map) );
	    }
	
	public ModelAndView invoice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Integer inttgl1 = null;
		Integer inttgl2 = null;
		String no_counter=null;
		String no_invoice="";
		String regspaj=null;
		Calendar tgl_sekarang = Calendar.getInstance(); 
		String kode= request.getParameter("namaperusahaan");
		
		if (kode==null)
		{
			kode="";
		}
			
		if (!kode.equalsIgnoreCase(""))
		{
			List data= this.elionsManager.select_invoice(kode);
			if (data.size()!=0)
			{		
				/*Map data1= (HashMap) this.elionsManager.select_max_invoice(kode);
				if (data1!=null)
				{
					no_invoice = (String)data1.get("MAXIMUM");
					if (no_invoice==null)
					{
						no_invoice="";
					}
				}else{
					no_invoice = "";
				}*/
				
				if (no_invoice.equalsIgnoreCase(""))
				{
					Map param1 = new HashMap();
					param1.put("kode","42");
					param1.put("no","67");
					Long intIDCounter = this.elionsManager.select_counter_invoice(param1);
					
					if (intIDCounter.longValue() == 0)
					{
						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000));
					}else{
						if ((intIDCounter).toString().length()==7)
						{
							inttgl1=new Integer(Integer.parseInt((intIDCounter).toString().substring(0,4)));
							inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
							
							if (inttgl1.compareTo(inttgl2)!=0)
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000")));
								Map param=new HashMap();
								param.put("intIDCounter", Long.toString(intIDCounter.longValue()));
								param.put("kodecbg", "42");
								param.put("no","67");
								this.elionsManager.update_mst_counter(param);
								//update mst counter kalau ganti tahun 
							}
						}
					}	
					//--------------------------------------------
					//Increase current no No by 1 and
					//update the value to MST_COUNTER table
					Map param=new HashMap();
					param.put("intIDCounter", new Long(intIDCounter.longValue()+1));
					param.put("kodecbg", "42");
					param.put("no","67");
					this.elionsManager.update_mst_counter(param);
					//update tambah 1 counter di mst_counter
					//logger.info("update mst counter naik");
					
					Long intno = new Long(intIDCounter.longValue() + 1);
					String Strtahun = (intno).toString().substring(0,4);
					String Strno = (intno).toString().substring(4,7);
					no_counter = Strno.concat(Strtahun);
					no_invoice=no_counter.concat("WS");
				}
				//logger.info(data.size());
				for (int i=0; i < data.size();i++)
				{
					regspaj=(String)(((Map) data.get(i)).get("REG_SPAJ"));
					Map param =new HashMap();
					param.put("regspaj",regspaj);
					param.put("noinvoice",no_invoice);
					this.elionsManager.update_counter_mst_insured(param);
					//update no invoice pada mst_insured
					//logger.info(regspaj);
				}
				response.sendRedirect("../report/uw.pdf?window=invoice&show=pdf&customer="+kode+"&no_invoice="+no_invoice);
				
				//response.sendRedirect("/ReportServer/?rs=WORKSITE/invoice&new=true&1kode="+kode+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2+"&1no_invoice="+no_invoice);	
			}else{
				//response.sendRedirect("../report/uw.htm?window=invoice&show=pdf&customer="+kode+"&no_invoice="+no_invoice);
				response.sendRedirect("../report/uw.pdf?window=tidak_ada_data&show=pdf");

				//response.sendRedirect("/ReportServer/?rs=WORKSITE/invoice&new=true&1kode="+kode+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2+"&1no_invoice="+no_invoice);	
			}
		}
		
		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		map.put("listperusahaan", listperusahaan);
		return new ModelAndView("worksite/invoice_worksite", map);
	}
	
	public ModelAndView kwitansi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj= request.getParameter("spaj");
		Integer ke = new Integer(1);
		String ls_bill_no=null;
		String ls_cab="";
		
		Map data= (HashMap) this.elionsManager.select_billing(spaj,ke);
		if (data!=null)
		{		
			ls_bill_no = (String)data.get("MSBI_BILL_NO");
			ls_cab = (String)data.get("LCA_ID");
		
	
			if (ls_bill_no==null)
			{
				ls_bill_no="";
			}
			
			ls_cab = FormatString.rpad("0",ls_cab,2);
			
			if (ls_bill_no.equalsIgnoreCase(""))
			{
				Map param =new HashMap();
				param.put("kode",ls_cab);
				param.put("no","10");
				Long ld_no=(Long) this.elionsManager.select_counter_invoice(param);
				ld_no = new Long(ld_no.longValue()+1);
				
				Map param1 = new HashMap();
				param1.put("intIDCounter",ld_no);
				param1.put("no","10");
				param1.put("kodecbg",ls_cab);
				this.elionsManager.update_mst_counter(param1);
				ls_bill_no = ls_cab + FormatString.rpad("0",Long.toString(ld_no.longValue()),9);
				Map param2=new HashMap();
				param2.put("nobill",ls_bill_no);
				param2.put("regspaj",spaj);
				this.elionsManager.update_nobilling(param2);
				
				//response.sendRedirect("../report/uw.htm?window=kwitansi&show=html&kode="+spaj+"&no_bill="+ls_bill_no);

			}	
			response.sendRedirect("../report/uw.pdf?window=kwitansi&show=pdf&kode="+spaj+"&no_bill="+ls_bill_no);

		}else{
			response.sendRedirect("../report/uw.pdf?window=tidak_ada_data&show=pdf");
		}

		//response.sendRedirect("/ReportServer/?rs=WORKSITE/kwitansi&new=true&1kode="+spaj+"&1no_bill="+ls_bill_no);	

		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		map.put("listperusahaan", listperusahaan);
		return new ModelAndView("worksite/kwitansi_worksite", map);
	}

	public ModelAndView tgl_invoice_bayar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String kode= request.getParameter("namaperusahaan");
		String jenis = request.getParameter("jenis"); 
		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		Date dateNow = this.elionsManager.selectSysdate();
		SimpleDateFormat dayDF = new SimpleDateFormat("dd");
		SimpleDateFormat monthDF = new SimpleDateFormat("MM");
		SimpleDateFormat yearDF = new SimpleDateFormat("yyyy");
		String day = dayDF.format(dateNow);
		String month = monthDF.format(dateNow);
		String year = yearDF.format(dateNow);
		List<DropDown> jenisList = new ArrayList<DropDown>();
		jenisList.add(new DropDown("1", "BLM BAYAR > 2 MGG"));
		jenisList.add(new DropDown("2", "BLM INPUT TGL BAYAR"));
		jenisList.add(new DropDown("3", "SUDAH BAYAR"));
		
		map.put("jenisList", jenisList);
		map.put("jenis", jenis);
		map.put("listperusahaan", listperusahaan);
		map.put("namaperusahaan", kode);
		map.put("day", day);
		map.put("month", month);
		map.put("year", year);
		if(request.getParameter("showButton") != null && request.getParameter("showButton").equals("true")){
			List companyWsList = this.uwManager.selectCompanyWsList(kode,jenis);
			map.put("companyWsList", companyWsList);
		}
		
		return new ModelAndView("worksite/tgl_invoice_bayar", map);
	}	
	
	public ModelAndView kwitansi_pt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl2");
		String tgl1 = tanggal1;
		String tgl2 = tanggal2;
		if (tanggal1!=null)
		{
			tanggal1=tanggal1.replaceAll("/","");
			tanggal2=tanggal2.replaceAll("/","");
			String yy1,mm1,dd1,yy2,mm2,dd2;
			yy1=tanggal1.substring(4);
			mm1=tanggal1.substring(2,4);
			dd1=tanggal1.substring(0,2);
			yy2=tanggal2.substring(4);
			mm2=tanggal2.substring(2,4);
			dd2=tanggal2.substring(0,2);
			tanggal1=yy1+mm1+dd1;
			tanggal2=yy2+mm2+dd2;
		}else{
			tanggal1 = "";
			tgl1 = "";
			tanggal2 ="";
			tgl2="";
		}
		
		Map map = new HashMap();
		String kode= request.getParameter("namaperusahaan");

		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		map.put("listperusahaan", listperusahaan);
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("worksite/kwitansi_pt_worksite", map);
	}	

    public ModelAndView billing_info(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        logger.info( "ViewerWorksiteController.billing_info" );
        Map<String, Object> map = new HashMap<String, Object>();

        String monthFrom = request.getParameter("monthFrom");
        String yearFrom = request.getParameter("yearFrom");
        String monthTo = request.getParameter("monthTo");
        String yearTo = request.getParameter("yearTo");
        String namaperusahaan = request.getParameter("namaperusahaan");

        String periode_from = yearFrom + monthFrom;
        String periode_to = yearTo + monthTo;

        logger.info( "monthFrom = " + monthFrom );
        logger.info( "yearFrom = " + yearFrom );
        logger.info( "monthTo = " + monthTo );
        logger.info( "yearTo = " + yearTo );
        logger.info( "namaperusahaan = " + namaperusahaan );
        logger.info( "periode_from = " + periode_from );
        logger.info( "periode_to = " + periode_to );

        map.put("companyList", elionsManager.select_nama_perusahaan());
        map.put("monthList", DateUtil.selectMonth() );
        map.put("yearList", DateUtil.selectYearFromFewYearsAgoTillNow( 10, false ) );
        map.put("namaperusahaan", namaperusahaan );
        map.put("monthFrom", monthFrom );
        map.put("yearFrom", yearFrom );
        map.put("monthTo", monthTo );
        map.put("yearTo", yearTo );

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "MCL_ID", namaperusahaan );
        params.put( "JENIS", 1 );
        params.put( "REG_SPAJ", "00000000000" );
        params.put( "TAHUN_KE", 0 );
        params.put( "PREMI_KE", 0 );
        params.put( "PERIODE_FROM", periode_from );
        params.put( "PERIODE_TO", periode_to );

        List<Map<String, Object>> historyList = uwManager.selectHistoryUT( params );

        for( Map<String, Object> contentMap : historyList )
        {
            String fileName = ( String ) contentMap.get( "FILE_NAME" );
            String[] fileNameArr = new String[]{};
            if( fileName != null ) fileNameArr = fileName.replace( " ", ""  ).split( ";" );
            contentMap.put( "FILE_NAME_ARRAY", fileNameArr );
        }

        map.put( "historyList", historyList );

        return ( new ModelAndView("worksite/billing_info", map) );
    }

    public ModelAndView dokumen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tanggal1 = request.getParameter("tgl1");
        String tanggal2 = request.getParameter("tgl2");
        String tgl1 = tanggal1;
        String tgl2 = tanggal2;
        if (tanggal1!=null)
        {
            tanggal1=tanggal1.replaceAll("/","");
            tanggal2=tanggal2.replaceAll("/","");
            String yy1,mm1,dd1,yy2,mm2,dd2;
            yy1=tanggal1.substring(4);
            mm1=tanggal1.substring(2,4);
            dd1=tanggal1.substring(0,2);
            yy2=tanggal2.substring(4);
            mm2=tanggal2.substring(2,4);
            dd2=tanggal2.substring(0,2);
            tanggal1=yy1+mm1+dd1;
            tanggal2=yy2+mm2+dd2;
        }else{
            tanggal1 = "";
            tgl1 = "";
            tanggal2 ="";
            tgl2="";
        }

        Map map = new HashMap();
        String kode= request.getParameter("namaperusahaan");

        List listperusahaan = this.elionsManager.select_nama_perusahaan();
        map.put("listperusahaan", listperusahaan);
        map.put("tgl1",tgl1);
        map.put("tgl2", tgl2);
        return new ModelAndView("worksite/kwitansi_pt_worksite", map);
    }

	//MANTA (18-03-2013) -- Pengiriman Invoice New Business Ke Perusahaan Payroll
	public ModelAndView invoice_payroll_new(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Upload uploader = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(uploader);
		binder.bind(request);
		
		String customer = ServletRequestUtils.getRequiredStringParameter(request, "customer");
		String tgl1 = ServletRequestUtils.getRequiredStringParameter(request, "tgl1");
		String tgl2 = ServletRequestUtils.getRequiredStringParameter(request, "tgl2");
		String emailto = ServletRequestUtils.getStringParameter(request, "emailto", "");
		String emailcc = ServletRequestUtils.getStringParameter(request, "emailcc", ""); 
		String emailsubject = ServletRequestUtils.getStringParameter(request, "emailsubject", ""); 
		String emailmessage = ServletRequestUtils.getStringParameter(request, "emailmessage", "");
		String emailfrom = "";
		String emailattach = "";
		String file1 = ServletRequestUtils.getStringParameter(request, "file1", "");
		String namafile1 = ServletRequestUtils.getStringParameter(request, "namafile1", "");
		
		List tempdetcompany = this.uwManager.select_nama_perusahaan_by_mcl_id(customer);
		Map detcompany = (HashMap) tempdetcompany.get(0);
		String namacompany = detcompany.get("MCL_FIRST").toString();
		
		List cp = this.elionsManager.selectContactPerson(customer);
		ContactPerson companycontact = (ContactPerson) cp.get(0);
		String emailcompany = companycontact.getEmail();
		
		String period = FormatDate.toIndonesian(tgl1.substring(0,6));
		String outputFilename = "invoice_peserta_report_" + period.trim() + ".pdf";
		
		//Proses Pengiriman Email
		if(request.getParameter("kirim") != null) {
			
			try {
				String dir = props.getProperty("pdf.dir.report") + "\\invoice_payroll_new\\";
				String uploadDir = props.getProperty("pdf.dir.report")+"\\invoice_payroll_new\\attachment\\";
				List<File> attachments = new ArrayList<File>();
				
				List<Map> data = new ArrayList<Map>();
				data = uwManager.selectInvoicePayroll(customer,tgl1,tgl2);
		        
//				String awal = tgl1.substring(6)+tgl1.substring(4,6)+tgl1.substring(0,4);
//				String akhir = tgl2.substring(6)+tgl2.substring(4,6)+tgl2.substring(0,4);
				
		        Map<String, Object> params = new HashMap<String, Object>();
		        params.put("tgl1", tgl1);
		        params.put("tgl2", tgl2);
		        params.put("customer", customer);
		        
				JasperUtils.exportReportToPdf(
						props.getProperty("report.worksite.list_peserta")+".jasper", 
						dir, 
						outputFilename, 
						params, 
						data, 
						PdfWriter.AllowPrinting, null, null);
				
				File sourceFile = new File(dir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				//Atachment tambahan
				if(!uploader.getFile1().isEmpty()){
					File sourceFile1 = new File(uploadDir+uploader.getFile1().getOriginalFilename());
					FileCopyUtils.copy(uploader.getFile1().getBytes(), sourceFile1);
					
					if(sourceFile1.exists()){
						attachments.add(sourceFile1);
					}
				}
				
				if(currentUser.getEmail() == null){
					emailfrom = props.getProperty("admin.ajsjava");
				}else{
					emailfrom = currentUser.getEmail().toString();
				}
				String sendemailto = emailto.replaceAll(" ", "");
				String[] to = sendemailto.split(";");
				
				String[] cc = null;
				if(!emailcc.equals("")){
					String sendemailcc = emailcc.replaceAll(" ", "");
					cc = sendemailcc.split(";");
				}
				
				email.send(false, emailfrom, to, cc, null, 
						emailsubject, emailmessage, attachments);
				
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Email berhasil dikirm!');window.close();</script>");
    			sos.close();
    			
    			return null;
    			
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} catch (MessagingException e) {
				logger.error("ERROR :", e);
			}
			
		}else {
			
			cmd.put("emailto", emailcompany);
			cmd.put("emailcc", emailcc);
			cmd.put("emailsubject", "Invoice " + namacompany + " Periode"+ period +".");
			cmd.put("emailmessage", 
					"Dear All,\n\n"+
					"Berikut ini saya kirimkan Invoice " + namacompany + " Periode" + period + "\n\n"+
					"Jika sudah dilakukan pembayaran premi, mohon kirimkan Bukti Setor melalui:\n" +
					"1. Email\t: novie@sinarmasmsiglife.co.id ; tities@sinarmasmsiglife.co.id ; devy@sinarmasmsiglife.co.id\n" +
					"2. Fax\t: 021-6257779 (UP: Novie/Titis/Devy)\n\n" +
					"Regards,\n"+
					currentUser.getLus_full_name() + " (" + currentUser.getDept() + ")");
			cmd.put("emailattach", outputFilename);
			
		}
		return new ModelAndView("worksite/invoice_newbusiness", cmd);
	}
}