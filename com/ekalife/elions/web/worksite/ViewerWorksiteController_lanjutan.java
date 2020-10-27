package com.ekalife.elions.web.worksite;


import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentMultiController;

public class ViewerWorksiteController_lanjutan  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("worksite/worksite_lanjutan");
	}
	
	public ModelAndView daftar_lanjutan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String kode= request.getParameter("value");
		List listperusahaan = this.bacManager.select_nama_perusahaan_by_filter(kode);
		map.put("listperusahaan", listperusahaan);
		return new ModelAndView("worksite/viewer_worksite_lanjutan", map);
	}
	
	public ModelAndView invoice_lanjutan(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
				Map data1= (HashMap) this.elionsManager.select_max_invoice(kode);
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
				response.sendRedirect("../report/uw.htm?window=invoice_lanjutan&show=pdf&customer="+kode+"&no_invoice="+no_invoice);
			}else{
				response.sendRedirect("../report/uw.htm?window=tidak_ada_data&show=pdf");
			}
		}
		
		List listperusahaan = this.elionsManager.select_nama_perusahaan();
		map.put("listperusahaan", listperusahaan);
		return new ModelAndView("worksite/invoice_worksite_lanjutan", map);
	}
	
}