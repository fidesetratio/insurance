package com.ekalife.elions.web.akseptasi_ssh;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Tertanggung2;
import com.ekalife.elions.model.Utama;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PremiSshFormController extends ParentFormController {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Utama utama;
	private Pemegang2 pemegang;
	private Tertanggung2 tertanggung;
	private DataUsulan2 dataUsulan;
	private Double premiNew,rateNew;
	List lsProduk;
	List lsProdukNew;

	//	int i=0;
	int proses=0;//1=insert 2=update;
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map=new HashMap();
		String filBisnisId="900";
		map.put("bisnisIdRider",elionsManager.selectLstDetBisnisRider(filBisnisId));
		//map.put("bisnisIdAll",elionsManager.selectLstDetBisnisAll());
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
		
		NumberFormat f2=new DecimalFormat("00");
		NumberFormat f3=new DecimalFormat("000");
		Double ldec_premi;
		int row = 0;
		utama.setTombol(new Integer(0));
		
		//
		//if(i>=0){
			if(request.getParameter("flag")==null)
			lsProduk=elionsManager.selectMstProductInsured(spaj,insured,active);
			
			tertanggung=elionsManager.selectTertanggung(spaj,new Integer(4));
			pemegang=elionsManager.selectPemegang(spaj,new Integer(4));
			dataUsulan=elionsManager.selectDataUsulan(spaj,new Integer(1));
			ls_plan = "produk_asuransi.n_prod_"+ f3.format(dataUsulan.getLsbs_id());
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
				//coba cek lagi logika updatenya kayaknya salah deh
				Product produkUpdate=(Product) lsProduk.get(row);
				produkUpdate.setLsbs_id(dataUsulan.getLsbs_id());
				produkUpdate.setLsdbs_number(dataUsulan.getLsdbs_number());
				produkUpdate.setMspr_tsi(dataUsulan.getMspr_tsi());
				produkUpdate.setMspr_premium(ldec_premi);
				produkUpdate.setProd_id(dataUsulan.getLsbs_id()+"~X"+dataUsulan.getLsdbs_number());
				
				lsProduk.set(row,produkUpdate);
				
				//

				String produk=ekalife.planPremi;   //" 39, 40, 145, 146";
				int pos=produk.indexOf(f3.format(dataUsulan.getLsbs_id()));
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
		hitTotal();
		return utama;
		 
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		String flag,item;
		Product produkAdd=new Product();
		Product produkBef,produkEdit ;
		
		String[] lsbsId;
		//String produk,lsbsId,lsdbsNumber,baris;
		String lsdbsNumber[];
		String extra[],rate[];
		String rb=request.getParameter("rb");
		int row=0;
		
		utama.setStandard(Integer.valueOf(rb));
		flag=request.getParameter("flag");//1=add 2=add 3=itemchanged
		item=request.getParameter("item");//1=produk 2=extra 3=rate 
		String baris=request.getParameter("brs");//baris yang diedit;
		
		lsProdukNew=utama.getLsProduk();
		extra=request.getParameterValues("extra");
		rate=request.getParameterValues("rate");
		lsbsId=request.getParameterValues("lsbs_id");
		lsdbsNumber=request.getParameterValues("lsdbs_number");
		
		String proses =request.getParameter("proses");
		utama.setProses(proses); //1=insert 2=update
		
		if(flag.equals("1")  ){//add  
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
			

			utama.setTombol(new Integer(1)); ///1=add 2=edit 3=save
			hitTotal();
			err.reject("","add");
			
		}else if(flag.equals("2")){//itemchanged
			row=Integer.parseInt(baris);
			//JOptionPane.showMessageDialog(null,lsbsId);
			produkEdit=(Product) lsProdukNew.get(row);
			produkEdit.setSelectIndex(Integer.valueOf(lsbsId[row]));
			produkEdit.setLsbs_id(Integer.valueOf(lsbsId[row]));
			produkEdit.setLsdbs_number(Integer.valueOf(lsdbsNumber[row]));
			produkEdit.setMspr_extra(Double.valueOf(extra[row]));
			produkEdit.setMspr_rate(Double.valueOf(rate[row]));
			double disc=0;
			if(logger.isDebugEnabled())logger.debug(item);
			if(item.equals("1")){//produk
					//
				if(lsbsId[row].equals("900"))
				if(produkEdit.getMspr_extra()==null || produkEdit.getMspr_extra().doubleValue()==0){
					produkEdit.setMspr_extra(new Double(25));
				}	
				//
				wf_set_premi(produkEdit.getMspr_extra());
				produkEdit.setMspr_rate(rateNew);
				produkEdit.setMspr_premium(premiNew);
				if(produkEdit.getMspr_discount()!=null)
					disc=produkEdit.getMspr_discount().doubleValue();
				produkEdit.setTotal(new Double(premiNew.doubleValue()+disc));
				lsProdukNew.set(row,produkEdit);
			}else if(item.equals("2")){//extra
				wf_set_premi(produkEdit.getMspr_extra());
				//produkEdit=(Product) lsProdukNew.get(row);
				produkEdit.setMspr_rate(rateNew);
				produkEdit.setMspr_premium(premiNew);
				if(produkEdit.getMspr_discount()!=null)
					disc=produkEdit.getMspr_discount().doubleValue();
				produkEdit.setTotal(new Double(premiNew.doubleValue()+disc));
				lsProdukNew.set(row,produkEdit);
			}else if(item.equals("3")){//rate
				double dRate=produkEdit.getMspr_rate().doubleValue();
				double hasil;
				hasil=dRate*dataUsulan.getMspr_tsi().doubleValue()/1000;
				produkEdit.setMspr_premium(new Double(hasil));
				if(produkEdit.getMspr_discount()!=null)
					disc=produkEdit.getMspr_discount().doubleValue();
				produkEdit.setTotal(new Double(produkEdit.getMspr_premium().doubleValue()+disc));
				lsProdukNew.set(row,produkEdit);
				
			}
			
			if(logger.isDebugEnabled())logger.debug(produkEdit);
			hitTotal();
			err.reject("","itemchanged");
			utama.setTombol(new Integer(2));

		}else if(flag.equals("3")){//save
			utama.setTombol(new Integer(3));
		}
		if(logger.isDebugEnabled())logger.debug(lsProdukNew);
		utama.setLsProduk(lsProdukNew);
		
	}
	
	private void hitTotal(){
		double totPremi=0,totTot=0;
		double premi=0,tot=0;
		for(int i=0;i<utama.getLsProduk().size();i++){
			Product a=(Product)utama.getLsProduk().get(i);
			if(a.getMspr_premium()==null)
				a.setMspr_premium(new Double(0));
			premi=a.getMspr_premium().doubleValue();
			totPremi+=premi;
			if(a.getTotal()==null)
				a.setTotal(new Double(0));
			
			tot=a.getTotal().doubleValue();
			totTot+=tot;
			
		}
		utama.setTotalPremi(new Double(totPremi));
		utama.setTotalTot(new Double(totTot));
		

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
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		String flag=request.getParameter("flag");
		String lsbsId[]=request.getParameterValues("lsbs_id");
		String lsdbsNumber[]=request.getParameterValues("lsdbs_number");
		String rate[]=request.getParameterValues("rate");
		String extra[]=request.getParameterValues("extra");
		
		if(flag.equals("3")){//save
			if(utama.getProses().equals("1")){//insert
				Product productIns =(Product) utama.getLsProduk().get(utama.getLsProduk().size()-1);
				for(int i=0;i<lsbsId.length;i++){
					productIns.setLsbs_id(Integer.valueOf(lsbsId[i]));
					productIns.setLsdbs_number(Integer.valueOf(lsdbsNumber[i]));
					productIns.setMspr_rate(Double.valueOf(rate[i]));
					if(logger.isDebugEnabled())logger.debug(extra[i]);
					
					if(extra[i]!=null )
						if(!extra[i].equals(""))
						productIns.setMspr_extra(Double.valueOf(extra[i]));
				}
				
				if(logger.isDebugEnabled())logger.debug(productIns.getLsbs_id());
				if(logger.isDebugEnabled())logger.debug(productIns.getLsdbs_number());
				elionsManager.insertMstProductInsured(productIns);
			}else if(utama.getProses().equals("2")){//update
				List lsUpdate=utama.getLsProduk();
				for(int i=1;i<lsUpdate.size();i++){
					Product productUp=new Product();
					productUp=(Product) lsUpdate.get(i);
					productUp.setMspr_rate(Double.valueOf(rate[i]));
					productUp.setMspr_extra(Double.valueOf(extra[i]));
					lsUpdate.set(i,productUp);
					
				}
				for(int i=1;i<lsUpdate.size();i++){
					Product productUp=new Product();
					productUp=(Product) lsUpdate.get(i);
					if(logger.isDebugEnabled())logger.debug(productUp.getMspr_rate());
					if(logger.isDebugEnabled())logger.debug(productUp.getMspr_premium());
				}
				
				elionsManager.updateMstProductInsured(lsUpdate);
				
			}
//			utama.setFlagAdd(new Integer(0));
//			utama.setFlagEdit(new Integer(0));
//			utama.setFlagSave(new Integer(1));
//			utama.setFlagCancel(new Integer(0));
		}
		hitTotal();
		return new ModelAndView("akseptasi_ssh/w_premi_calc_ssh", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}	
	
}