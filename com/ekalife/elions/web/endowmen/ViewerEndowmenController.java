package com.ekalife.elions.web.endowmen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ViewerEndowmenController  extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("endowmen/endowmen");
	}
	
	public ModelAndView daftar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		//String[] nama_perusahaan = new String[]{"KSO.PERKASA ABADI","MUTIARA MATAHARI MAKMUR SEJAHTERA"};
		List value=new ArrayList();
		value.add("KSO.PERKASA ABADI");
		value.add("MUTIARA MATAHARI MAKMUR SEJAHTERA");
		value.add("TIMURJAYA TELADAN");
		value.add("FAMILY INTI SEJATI");
		List listperusahaan = this.elionsManager.select_namacompany_list_endow(value);
		map.put("listperusahaan", listperusahaan );
		return new ModelAndView("endowmen/viewer_endowmen", map);
	}
	
	public ModelAndView invoice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Integer inttgl1 = null;
		Integer inttgl2 = null;
		String no_counter=null;
		String no_invoice="";
		String regspaj=null;
		Calendar tgl_sekarang = Calendar.getInstance(); 
		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
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
			String kode = "";
			/*String nama_perusahaan1="KSO.PERKASA ABADI";
			Map data2 = this.elionsManager.select_company_endow(nama_perusahaan1);
			if (data2 != null)
			{
				kode = (String)data2.get("COMPANY_ID");
			}*/
			kode = request.getParameter("namaperusahaan");
			
			String kode_area="";
				
			List data= this.elionsManager.select_invoice_endow(kode,tanggal1,tanggal2);
			if (data.size()!=0)
			{		
				for (int i = 0; i < data.size();i++)
				{
					Map a = (HashMap) data.get(i);
					kode_area=(String) a.get("LCA_ID");
				}
	
				Map data1= (HashMap) this.elionsManager.select_max_invoice_endow(kode,tanggal1,tanggal2);
				if (data1!=null)
				{
					no_invoice = (String)data1.get("MAXIMUM");
					if (no_invoice==null)
					{
						no_invoice="";
					}
				}else{
					no_invoice = "";
				}
				
				if (no_invoice.equalsIgnoreCase(""))
				{
					Map param1 = new HashMap();
					param1.put("kode",kode_area);
					param1.put("no","68");
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
								param.put("kodecbg", kode_area);
								param.put("no","68");
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
					param.put("kodecbg", kode_area);
					param.put("no","68");
					this.elionsManager.update_mst_counter(param);
					//update tambah 1 counter di mst_counter
					//logger.info("update mst counter naik");
					
					Long intno = new Long(intIDCounter.longValue() + 1);
					String Strtahun = (intno).toString().substring(0,4);
					String Strno = (intno).toString().substring(4,7);
					no_counter = Strno.concat(Strtahun);
					no_invoice=no_counter.concat("PA");
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
				response.sendRedirect("/ReportServer/?rs=ENDOWMENT/invoice&new=true&1kode="+kode+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2+"&1no_invoice="+no_invoice);	
			}else{
				response.sendRedirect("/ReportServer/?rs=ENDOWMENT/invoice&new=true&1kode="+kode+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2+"&1no_invoice="+no_invoice);	
			}
		}
		List value=new ArrayList();
		value.add("KSO.PERKASA ABADI");
		value.add("MUTIARA MATAHARI MAKMUR SEJAHTERA");
		value.add("TIMURJAYA TELADAN");
		value.add("FAMILY INTI SEJATI");
		List listperusahaan = this.elionsManager.select_namacompany_list_endow(value);
		map.put("listperusahaan", listperusahaan );
		return new ModelAndView("endowmen/invoice_endowmen", map);
	}
	
	
	public ModelAndView kwitansi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = "";
		Integer ke = new Integer(1);
		String ls_bill_no="";
		String ls_cab="";
		
		/*String nama_perusahaan1="KSO.PERKASA ABADI";
		Map data2 = this.elionsManager.select_company_endow(nama_perusahaan1);
		
		String kode_perusahaan = (String)data2.get("COMPANY_ID");*/
		String kode_perusahaan = request.getParameter("namaperusahaan");
		String bisnis = "157";

		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
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
			List data3= this.elionsManager.select_billing_endow(ke,kode_perusahaan,bisnis,tanggal1,tanggal2);
			if (data3.size()!=0)
			{		
				for (int i = 0; i < data3.size();i++)
				{
					Map a = (HashMap) data3.get(i);
					ls_cab = (String)a.get("LCA_ID");
				}
			
				Map data4 = (HashMap) this.elionsManager.select_max_billing_endow(ke,kode_perusahaan,bisnis,tanggal1,tanggal2);
				if (data4 != null)
				{
					ls_bill_no = (String)data4.get("MAXIMUM");
					if (ls_bill_no==null)
					{
						ls_bill_no="";
					}
				}else{
					ls_bill_no = "";
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
					
					for (int i = 0; i < data3.size();i++)
					{
						Map a = (HashMap) data3.get(i);
						spaj = (String)a.get("REG_SPAJ");
						
						Map param2=new HashMap();
						param2.put("nobill",ls_bill_no);
						param2.put("regspaj",spaj);
						this.elionsManager.update_nobilling(param2);	
					}
					
				}	
				
				//logger.info(ls_bill_no);
				response.sendRedirect("/ReportServer/?rs=ENDOWMENT/kwitansi&new=true&1kode="+kode_perusahaan+"&1no_bill="+ls_bill_no+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2);	
			}else{
				response.sendRedirect("/ReportServer/?rs=ENDOWMENT/kwitansi&new=true&1kode="+kode_perusahaan+"&1no_bill="+ls_bill_no+"&1tgl1="+tanggal1+"&1tgl2="+tanggal2);	
				
			}
		}
		List value=new ArrayList();
		value.add("KSO.PERKASA ABADI");
		value.add("MUTIARA MATAHARI MAKMUR SEJAHTERA");
		value.add("TIMURJAYA TELADAN");
		value.add("FAMILY INTI SEJATI");
		List listperusahaan = this.elionsManager.select_namacompany_list_endow(value);
		map.put("listperusahaan", listperusahaan );
		return new ModelAndView("endowmen/kwitansi_endowmen", map);
	}
	
	public ModelAndView kwitansi_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		String spaj= request.getParameter("spaj");
		Integer ke = new Integer(1);
		String ls_bill_no=null;
		String ls_cab="";
		
		Map data= (HashMap) this.elionsManager.select_billing_endow_spaj(spaj,ke);
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
			response.sendRedirect("../report/uw.pdf?window=kwitansi_spaj&show=pdf&kode="+spaj+"&no_bill="+ls_bill_no);

		}else{
			response.sendRedirect("../report/uw.pdf?window=tidak_ada_data&show=pdf");
		}
		List value=new ArrayList();
		value.add("KSO.PERKASA ABADI");
		value.add("MUTIARA MATAHARI MAKMUR SEJAHTERA");
		value.add("TIMURJAYA TELADAN");
		value.add("FAMILY INTI SEJATI");
		List listperusahaan = this.elionsManager.select_namacompany_list_endow(value);
		map.put("listperusahaan", listperusahaan );

		return new ModelAndView("endowmen/kwitansi_endowmen", map);
	}
	
	public ModelAndView invoice_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		String spaj= request.getParameter("spaj");
		Integer ke = new Integer(1);
		String ls_bill_no=null;
		String ls_cab="";
		String kode_area ="";
		String no_invoice="";
		Integer inttgl1 = null;
		Integer inttgl2 = null;
		String no_counter=null;
		String regspaj=null;
		Calendar tgl_sekarang = Calendar.getInstance(); 
		
		List data= this.elionsManager.select_invoice_endow_perspaj(spaj);
		if (data.size()!=0)
		{		
			for (int i = 0; i < data.size();i++)
			{
				Map a = (HashMap) data.get(i);
				kode_area=(String) a.get("LCA_ID");
			}

			Map data1= (HashMap) this.elionsManager.select_max_invoice_endow_perspaj(spaj);
			if (data1!=null)
			{
				no_invoice = (String)data1.get("MAXIMUM");
				if (no_invoice==null)
				{
					no_invoice="";
				}
			}else{
				no_invoice = "";
			}
			
			if (no_invoice.equalsIgnoreCase(""))
			{
				Map param1 = new HashMap();
				param1.put("kode",kode_area);
				param1.put("no","68");
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
							param.put("kodecbg", kode_area);
							param.put("no","68");
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
				param.put("kodecbg", kode_area);
				param.put("no","68");
				this.elionsManager.update_mst_counter(param);
				//update tambah 1 counter di mst_counter
				//logger.info("update mst counter naik");
				
				Long intno = new Long(intIDCounter.longValue() + 1);
				String Strtahun = (intno).toString().substring(0,4);
				String Strno = (intno).toString().substring(4,7);
				no_counter = Strno.concat(Strtahun);
				no_invoice=no_counter.concat("PA");
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
				
				//response.sendRedirect("../report/uw.htm?window=invoice&show=html&kode="+spaj+"&no_bill="+ls_bill_no);	
			response.sendRedirect("../report/uw.pdf?window=invoice_spaj&show=pdf&kode="+spaj+"&no_bill="+no_invoice);

		}else{
			response.sendRedirect("../report/uw.pdf?window=tidak_ada_data&show=pdf");
		}
		List value=new ArrayList();
		value.add("KSO.PERKASA ABADI");
		value.add("MUTIARA MATAHARI MAKMUR SEJAHTERA");
		value.add("TIMURJAYA TELADAN");
		value.add("FAMILY INTI SEJATI");
		List listperusahaan = this.elionsManager.select_namacompany_list_endow(value);
		map.put("listperusahaan", listperusahaan );

		return new ModelAndView("endowmen/invoice_endowmen", map);
	}	
	
}
