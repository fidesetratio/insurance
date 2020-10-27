package com.ekalife.elions.web.akseptasi_ssh;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Tertanggung2;
import com.ekalife.elions.model.Utama;
import com.ekalife.elions.service.ElionsManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PremiSshFormControllerold extends SimpleFormController {
	
	private Utama utama;
	private Pemegang2 pemegang;
	private Tertanggung2 tertanggung;
	private DataUsulan2 dataUsulan;
	private ElionsManager elionsManager;
	private Double premiNew,rateNew;
	List lsProduk;
	List lsProdukNew;

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	//	int i=0;
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map=new HashMap();
		String filBisnisId="900";
		map.put("bisnisIdRider",elionsManager.selectLstDetBisnisRider(filBisnisId));
		map.put("bisnisIdAll",elionsManager.selectLstDetBisnisAll());
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		utama=new Utama();
		//Map map=new HashMap();
		String spaj=request.getParameter("spaj"); //get parsing parameter dari tombol premi sebuat list atao map
		Integer insured=new Integer(1);  //yang isinya banyak objek bukan spaj and insure saja.
		Integer active=new Integer(1);
		//String awal=request.getParameter("awal");
		String ls_plan;
		
		NumberFormat nf=new DecimalFormat("00");
		Double ldec_premi;
		int row = 0;
		
		
		//
		//if(i>=0){
			JOptionPane.showMessageDialog(null,"awal");
			lsProduk=elionsManager.selectMstProductInsured(spaj,insured,active);
			tertanggung=elionsManager.selectTertanggung(spaj,new Integer(4));
			pemegang=elionsManager.selectPemegang(spaj,new Integer(4));
			dataUsulan=elionsManager.selectDataUsulan(spaj,new Integer(1));
			ls_plan = "produk_asuransi.n_prod_"+ nf.format(dataUsulan.getLsbs_id());
			/*try{
				Class aClass = Class.forName(ls_plan);
				n_prod ln_prod = (n_prod)aClass.newInstance();
				
				ln_prod.of_set_usia_pp(pemegang.getMspo_age().intValue());
				ln_prod.of_set_usia_tt(tertanggung.getMste_age().intValue());	
				tertanggung.setMste_age(new Integer(ln_prod.of_get_age()));
				//
				ln_prod.of_set_bisnis_id(dataUsulan.getLsbs_id().intValue());
				ln_prod.of_set_bisnis_no(dataUsulan.getLsdbs_number().intValue());
				ln_prod.of_set_sex(tertanggung.getMspe_sex().intValue());
				ln_prod.of_set_begdate( dataUsulan.getMspr_beg_date().getYear(),dataUsulan.getMspr_beg_date().getMonth(),dataUsulan.getMspr_beg_date().getDate());
				ln_prod.of_set_kurs( dataUsulan.getLku_id());
				ln_prod.of_set_up( dataUsulan.getMspr_tsi().doubleValue());
				ln_prod.of_set_pmode(dataUsulan.getLscb_id().intValue());
				ln_prod.of_set_medis(dataUsulan.getMste_medical().intValue());
				ln_prod.of_set_class( dataUsulan.getMspr_class().intValue());
				ln_prod.of_get_rate();
				ldec_premi = new Double(ln_prod.of_get_premi());
				for(int i=0;i<lsProduk.size();i++){
					Product produk=(Product) lsProduk.get(i);
					if(produk.getLsbs_id().intValue()<300)
						row=i;
				}
				
				Product produkUpdate=(Product) lsProduk.get(row);
				produkUpdate.setLsbs_id(dataUsulan.getLsbs_id());
				produkUpdate.setLsdbs_number(dataUsulan.getLsdbs_number());
				produkUpdate.setMspr_tsi(dataUsulan.getMspr_tsi());
				produkUpdate.setMspr_premium(ldec_premi);
				produkUpdate.setProd_id(dataUsulan.getLsbs_id()+"~X"+dataUsulan.getLsdbs_number());
				
				lsProduk.set(row,produkUpdate);
				
				//

				String produk=ekalife.planPremi;   //" 39, 40, 145, 146";
				int pos=produk.indexOf(dataUsulan.getLsbs_id().intValue());
				if(pos>0)
					dataUsulan.setMspo_ins_period(new Integer(79));
			}
			catch (ClassNotFoundException e)
			{
				logger.error("ERROR :", e);
				//throw new NoClassDefFoundError (e.getMessage());
				 
			} catch (InstantiationException e) {
				logger.error("ERROR :", e);
			} catch (IllegalAccessException e) {
				logger.info("Ilegal exception error+>"+e.toString()); 
				logger.error("ERROR :", e);
			}*/
	//	}
	//	i=1;
		utama.setStandard(elionsManager.selectMstInsuredMsteStandard(spaj,tertanggung.getMste_insured_no()));
		utama.setLsProduk(lsProduk);
		return utama;
		 
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		String flag,item;
		Product produkAdd=new Product();
		Product produkBef,produkEdit = null;
		
		String produk,lsbsId,lsdbsNumber,baris;
		String extra[],rate[];
		String rb=request.getParameter("rb");
		int row;
		
		utama.setStandard(Integer.valueOf(rb));
		flag=request.getParameter("flag");//1=add 2=add 3=itemchanged
		item=request.getParameter("item");//1=produk 2=extra 3=rate 
		baris=request.getParameter("brs");//baris yang diedit;
		
		lsProdukNew=utama.getLsProduk();
		extra=request.getParameterValues("extra");
		rate=request.getParameterValues("rate");
		
		if(flag!=null)	
			if(flag.equals("1")  || flag.equals("2")){//add  
				produkBef=(Product) lsProdukNew.get(lsProdukNew.size()-1);
				
				produkAdd.setReg_spaj(produkBef.getReg_spaj());
				produkAdd.setMste_insured_no(produkBef.getMste_insured_no());
				produkAdd.setLku_id(produkBef.getLku_id());
				produkAdd.setMspr_tsi(produkBef.getMspr_tsi());
				produkAdd.setMspr_tsi_pa_a(produkBef.getMspr_tsi_pa_a());
				produkAdd.setMspr_tsi_pa_b(produkBef.getMspr_tsi_pa_b());
				produkAdd.setMspr_tsi_pa_c(produkBef.getMspr_tsi_pa_c());
				produkAdd.setMspr_tsi_pa_d(produkBef.getMspr_tsi_pa_d());
				produkAdd.setMspr_tsi_pa_m(produkBef.getMspr_tsi_pa_m());
				produkAdd.setMspr_persen(new Integer(0));
				produkAdd.setMspr_premium(new Double(0));
				produkAdd.setMspr_discount(new Double(0));
				produkAdd.setMspr_active(new Integer(1));
				produkAdd.setMspr_extra(new Double(0));
				produkAdd.setMspr_rate(new Double(0));
				produkAdd.setMspr_tt(new Integer(0));
				produkAdd.setTotal(new Double(0));
				produkAdd.setTambah(new Integer(1));
				lsProdukNew.add(produkAdd);
				if(flag.equals("2")){
					row=Integer.parseInt(baris);
					produk=request.getParameter("produk");
					if(logger.isDebugEnabled())logger.debug(produk);
					int bts=produk.indexOf("~");
					lsbsId=produk.substring(0,bts);
					lsdbsNumber=produk.substring(bts+2,produk.length());
					//JOptionPane.showMessageDialog(null,lsbsId);
					produkEdit=(Product) lsProdukNew.get(row);
					produkEdit.setSelectIndex(Integer.valueOf(lsbsId));
					produkEdit.setMspr_extra(Double.valueOf(extra[row]));
					produkEdit.setMspr_rate(Double.valueOf(rate[row]));
					if(item.equals("1")){//produk
							//
						if(lsbsId.equals("900"))
						if(extra[row]==null || Double.parseDouble(extra[row])==0){
							JOptionPane.showMessageDialog(null,"extra");
							produkEdit.setMspr_extra(new Double(25));
						}	
						//
						wf_set_premi(Double.valueOf(extra[row]));
						produkEdit.setMspr_rate(rateNew);
						produkEdit.setMspr_premium(premiNew);
						lsProdukNew.set(row,produkEdit);
					}else if(item.equals("2")){//extra
						wf_set_premi(Double.valueOf(extra[row]));
						//produkEdit=(Product) lsProdukNew.get(row);
						JOptionPane.showConfirmDialog(null,produkEdit.getLsbs_id());
						produkEdit.setMspr_rate(rateNew);
						produkEdit.setMspr_premium(premiNew);
						lsProdukNew.set(row,produkEdit);
					}else if(item.equals("3")){//rate
						double dRate=Double.parseDouble(rate[row]);
						double hasil;
						hasil=dRate*dataUsulan.getMspr_tsi().doubleValue()/1000;
						
						//produkEdit=(Product) lsProdukNew.get(row);
						produkEdit.setMspr_premium(new Double(hasil));
						lsProdukNew.set(row,produkEdit);
					}
					if(logger.isDebugEnabled())logger.debug(produkEdit);
				}
				
				
				
		}/*else if(flag.equals("2")){
			row=Integer.parseInt(baris);
			JOptionPane.showMessageDialog(null,item);
			if(item.equals("1")){//produk
				produk=request.getParameter("produk");
				if(logger.isDebugEnabled())logger.debug(produk);
				int bts=produk.indexOf("~");
				lsbsId=produk.substring(0,bts);
				lsdbsNumber=produk.substring(bts+2,produk.length());
				//
				produkEdit=(Product) lsProdukNew.get(row);
				if(lsbsId.equals("900"))
				if(extra[row]==null || Double.parseDouble(extra[row])==0){
					JOptionPane.showMessageDialog(null,"extra");
					produkEdit.setMspr_extra(new Double(25));
				}	
				//
				wf_set_premi(Double.valueOf(extra[row]));
				produkEdit.setMspr_rate(rateNew);
				produkEdit.setMspr_premium(premiNew);
				produkEdit.setSelectIndex(Integer.valueOf(lsbsId));
				lsProdukNew.set(row,produkEdit);
			}else if(item.equals("2")){//extra
				wf_set_premi(Double.valueOf(extra[row]));
				produkEdit=(Product) lsProdukNew.get(row);
				JOptionPane.showConfirmDialog(null,produkEdit.getLsbs_id());
				produkEdit.setMspr_rate(rateNew);
				produkEdit.setMspr_premium(premiNew);
				lsProdukNew.set(row,produkEdit);
			}else if(item.equals("3")){//rate
				double dRate=Double.parseDouble(rate[row]);
				double hasil;
				hasil=dRate*dataUsulan.getMspr_tsi().doubleValue()/1000;
				
				produkEdit=(Product) lsProdukNew.get(row);
				produkEdit.setMspr_premium(new Double(hasil));
				lsProdukNew.set(row,produkEdit);
			if(logger.isDebugEnabled())logger.debug(produkEdit);
				}	
		}*/
		if(logger.isDebugEnabled())logger.debug(lsProdukNew);
		utama.setLsProduk(lsProdukNew);
		err.reject("","");
	}
	
	public void wf_set_premi(Double ldec_em){
		Double ldec_rate;
		ldec_rate=(Double)elionsManager.selectLstPremiEm(dataUsulan.getLsbs_id(),dataUsulan.getLku_id(),dataUsulan.getLscb_id(),
				dataUsulan.getMspo_pay_period(),dataUsulan.getMspo_ins_period(),tertanggung.getMste_age(),ldec_em);
		if(ldec_rate==null)
			ldec_rate=new Double(0);
		rateNew=ldec_rate;
		if(logger.isDebugEnabled())logger.debug(ldec_rate);
		if(logger.isDebugEnabled())logger.debug(dataUsulan.getMspr_tsi());
		premiNew=new Double(ldec_rate.doubleValue()*dataUsulan.getMspr_tsi().doubleValue()/1000);

	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(Double.class, nf, true)); 
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, nf, true)); 
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(df,true));
	}
	
}
