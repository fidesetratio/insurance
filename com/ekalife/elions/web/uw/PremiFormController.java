package com.ekalife.elions.web.uw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Utama;
import com.ekalife.utils.parent.ParentFormController;

public class PremiFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	DecimalFormat f2 = new DecimalFormat ("00");
	DecimalFormat f3 = new DecimalFormat ("000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map = new HashMap();
		map.put("lsExtraPremi", elionsManager.selectLstDetBisnisRider("900"));
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String flag = request.getParameter("flag"); //1=add 2=add 3=itemchanged
		Utama command = new Utama();
		command.setSpaj(request.getParameter("spaj"));
		
		//canpri, supaya bisa di buka di uw helpdesk -- permintaan Tities
		Policy pol = bacManager.selectMstPolicyAll(command.getSpaj());
//		if(pol.getLspd_id()==209)command.setLspdId(pol.getLspd_id());
//		else command.setLspdId(new Integer(2));
		command.setLspdId(pol.getLspd_id());
		
		
		command.setLstbId(new Integer(1));
		List lsProduct = new ArrayList();
		//Tertanggung
		Map mTertanggung = elionsManager.selectTertanggung(command.getSpaj());
		command.setInsuredNo((Integer) mTertanggung.get("MSTE_INSURED_NO"));
		command.setUmurTt((Integer) mTertanggung.get("MSTE_AGE"));
		command.setSex_tt((Integer) mTertanggung.get("MSPE_SEX"));
		//Dw1 Pemegang
		Policy policy = elionsManager.selectDw1Underwriting(command.getSpaj(),command.getLspdId(),command.getLstbId());
		command.setInsPeriod(policy.getMspo_ins_period());
		command.setPayPeriod(policy.getMspo_pay_period());
		command.setLkuId(policy.getLku_id());
		command.setUmurPp(policy.getMspo_age());
		//Data Usulan Asuransi
		Map mDataUsulan = uwManager.selectDataUsulan(command.getSpaj());
		command.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
		command.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
		command.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
		command.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
		command.setBisnisId(f3.format(command.getLsbsId().intValue()));
		command.setLscbId((Integer)mDataUsulan.get("LSCB_ID"));
		
		if(command.getLsbsId()!=null || command.getLsbsId().intValue()>0){
			command.setPlan("produk_asuransi.n_prod_"+ f2.format(command.getLsbsId()));
			try{
				Class aClass = Class.forName(command.getPlan());
				n_prod ln_prod = (n_prod) aClass.newInstance();
				
				ln_prod.of_set_usia_pp(command.getUmurPp().intValue());
				ln_prod.of_set_usia_tt(command.getUmurTt().intValue());	
				command.setUmurTt(new Integer(ln_prod.of_get_age()));
				
			}catch (ClassNotFoundException e){
				logger.error("ERROR :", e);
			}catch (InstantiationException e){
				logger.error("ERROR :", e);
			}catch (IllegalAccessException e){
				logger.info("Ilegal exception error+>"+e.toString()); 
				logger.error("ERROR :", e);
			}
		}	
		
		lsProduct = elionsManager.selectMstProductInsured(command.getSpaj(),command.getInsuredNo(),new Integer(1));
		command.setJumAwal(lsProduct.size());
		command.setMsteStandard(this.elionsManager.selectMstInsuredMsteStandard(command.getSpaj(),command.getInsuredNo()));
		
		Double ldcAmtDisc, ldcPremi, ldcDisc;
		if(!lsProduct.isEmpty() && flag==null){	
			for(int i=0; i<lsProduct.size(); i++){
				Product productUpdate = (Product) lsProduct.get(i);
				ldcAmtDisc = productUpdate.getMspr_discount();
				ldcPremi = productUpdate.getMspr_premium();
				productUpdate.setLength(new Integer(lsProduct.size()));
				if(ldcAmtDisc == null)
					ldcAmtDisc = new Double(0);
				
				if(ldcAmtDisc.intValue() == 0){
					productUpdate.setDisc(new Double(0));
				}else{
					ldcDisc = new Double(ldcAmtDisc.doubleValue()/ldcPremi.doubleValue() * 100);
					productUpdate.setDisc(ldcDisc);
				}	
				
				lsProduct.set(i, productUpdate);
			}
		}
		
		command.setLsProduk(lsProduct);
		String produk = props.getProperty("product.planPremi");
		int pos = produk.indexOf(command.getBisnisId());
		if(pos >= 0) command.setInsPeriod(new Integer(79));
		command.setTombol(new Integer(0));
		hitTotal(command);
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		String flag = request.getParameter("flag"); //1=add 2=add 3=itemchanged 4=delete
		String item = request.getParameter("item"); //1=produk 2=extra 3=rate
		String baris = request.getParameter("brs"); //baris yang diedit;
		String lsbsId[], lsdbsNumber[], produk[], cheklist[];
		Utama command = (Utama) cmd;
		Product produkAdd = new Product();
		Product produkBef, produkEdit;
		Integer row = 0;

		cheklist = request.getParameterValues("check");
		lsbsId = request.getParameterValues("lsbs_id");
		lsdbsNumber = request.getParameterValues("lsdbs_number");
		produk = request.getParameterValues("produk");
		List lsProdukNew = command.getLsProduk();
		String produkTemp;
		String lsbsIdTemp = null, lsdbsNumberTemp = null;
		
		if(produk != null){
			if(lsbsId.length <= 1){
				produkTemp=request.getParameter("produk");
			}else{
				produkTemp=produk[row];
			}
			Integer pos = produkTemp.indexOf("~X");
			if(pos > -1){
				lsbsIdTemp = produkTemp.substring(0,pos);
				lsdbsNumberTemp = produkTemp.substring(pos+2);
			}
		}
		
		if(flag.equals("1")){//Add Extra
			produkBef = (Product) lsProdukNew.get(lsProdukNew.size()-1);
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
			command.setTombol(new Integer(1)); // 1=add 2=edit 3=save
			hitTotal(command);
			err.reject("","Proses Add telah berhasil dilakukan,silakan tekan tombol SAVE untuk menyimpan perubahan");
			
		}else if(flag.equals("2")){//Item Changed
			row = Integer.parseInt(baris);
			produkEdit = (Product) lsProdukNew.get(row);
			Double disc = 0.0;
			if(logger.isDebugEnabled()) logger.debug(item);
			
			if(item.equals("1")){//Extra Produk
				produkEdit.setSelectIndex(Integer.valueOf(lsbsIdTemp));
				produkEdit.setLsbs_id(Integer.valueOf(lsbsIdTemp));
				produkEdit.setLsdbs_number(Integer.valueOf(lsdbsNumberTemp));
				if(produkEdit.getLsbs_id().intValue() == 900){
					Double ldecEm=produkEdit.getMspr_extra();
					if(ldecEm == null || ldecEm.doubleValue() == 0)
						produkEdit.setMspr_extra(new Double(25));
					produkEdit = wf_set_premi(produkEdit,command);
				}else{
					produkEdit.setMspr_extra(new Double(0));
					produkEdit.setMspr_rate(new Double(0));
					produkEdit.setMspr_premium(new Double(0));
				}
				if(produkEdit.getMspr_discount()!=null)
					disc=produkEdit.getMspr_discount().doubleValue();

				produkEdit.setTotal(new Double(produkEdit.getMspr_premium().doubleValue()+disc));
				
				/*
			    900 EXTRA MORTALITA
			    901 EXTRA JOB
			    902 EXTRA PA - 800, 810
			    903 EXTRA PAYOR - 802, 815, 817
			    904 EXTRA HAMIL
			    905 EXTRA MORTALITA BAYI
			    906 EXTRA WAIVER - 804, 814, 816, 827
			    907 EXTRA CI - 801, 813
			    908 EXTRA TPD - 812
			    909 EXTRA SCHOLARSHIP - 835
			    910 EXTRA MEDICAL(ALL TYPE PRODUK MEDICAL) -819, 823,848
			    911 EXTRA DOMISILI
				912 EXTRA ESCI - 837
			    999 EXTRA RIDER - 818
				*/
				for(int y=0; y<lsProdukNew.size(); y++){
					Product p = (Product) lsProdukNew.get(y);
					if(produkEdit.getLsbs_id().intValue() == 902 && "800, 810".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 903 && "802, 815, 817, 828".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 906 && "804, 814, 816, 827".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 907 && "801, 813".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 908 && "812".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 909 && "835".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 910 && "819, 823, 848".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
						if("848".contains(p.getLsbs_id().toString())){
							Product p1 = (Product) findByName(lsProdukNew, "TERTANGGUNG I");
							if(p1!= null){
								produkEdit.setMspr_rate(p1.getMspr_rate());
								produkEdit.setMspr_tsi(p1.getMspr_tsi());
							};
						};
					}else if(produkEdit.getLsbs_id().intValue() == 912 && "837".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if(produkEdit.getLsbs_id().intValue() == 999 && "818".contains(p.getLsbs_id().toString())){
						produkEdit.setMspr_tsi(p.getMspr_tsi());
					}else if("900, 901, 911".contains(produkEdit.getLsbs_id().toString())) {
						Product p1 = (Product) lsProdukNew.get(0);
						produkEdit.setMspr_tsi(p1.getMspr_tsi());
					}
				}
				lsProdukNew.set(row,produkEdit);
				
			}else if(item.equals("2")){//Extra
				produkEdit.setLsbs_id(Integer.parseInt(lsbsId[Integer.parseInt(baris)]));
				produkEdit.setLsdbs_number(Integer.parseInt(lsdbsNumber[Integer.parseInt(baris)]));
				produkEdit.setSelectIndex(Integer.parseInt(baris));
				produkEdit = wf_set_premi(produkEdit,command);
				
				if(produkEdit.getMspr_discount() != null)
					disc=produkEdit.getMspr_discount().doubleValue();
				produkEdit.setTotal(new Double(produkEdit.getMspr_premium().doubleValue()+disc));
				
				lsProdukNew.set(row,produkEdit);
			}
			
			if(logger.isDebugEnabled()) logger.debug(produkEdit);
			hitTotal(command);
			err.reject("","Proses Perubahan Rate/Em/Premi telah dilakukan, silahkan tekan SAVE untuk menyimpan perubahan");
			command.setTombol(new Integer(2));

		}else if(flag.equals("3")){//Save
			command.setTombol(new Integer(3));
			command.setMsteStandard(1);
			
		}else if(flag.equals("4")){//Delete
			if(cheklist == null || cheklist.length <= 0){
				err.reject("","Pilih Extra Premi yang akan dihapus!");
			}else{
				HashMap transhist =bacManager.selectMstTransHistoryNewBussines(command.getSpaj());
				if(transhist.get("TGL_FUND_ALLOCATION") != null){
					err.reject("","Sudah diproses FUND ALLOCATION, Extra Premi tidak dapat dihapus!");
				}
				command.setTombol(new Integer(4));
				command.setMsteStandard(0);
			}
		}
		
		if(logger.isDebugEnabled()) logger.debug(lsProdukNew);
		command.setLsProduk(lsProdukNew);
	}
	
	public static Product findByName(List prod,String name){
		Product p = null;
		
		for(int y=0; y<prod.size(); y++){
			Product pt = (Product) prod.get(y);
			if(pt.getLsdbs_name().toLowerCase().contains(name.toLowerCase().trim())){
				return pt;
			}
			
		}
		
		return p;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Utama command = (Utama) cmd;
		List lsProduct = command.getLsProduk();
		Boolean cek = true;
		String succ = "true";
		String flag = request.getParameter("flag");
		String keterangan=request.getParameter("keterangan");
		String flag_jenis=request.getParameter("selectSatuan");
		String cheklist[];
		cheklist = request.getParameterValues("check");
		
		if(flag.equals("3")){//Save
			if(command.getJumAwal() != lsProduct.size()){
				Product productIns = (Product) command.getLsProduk().get(command.getLsProduk().size()-1);
				
				//Cek product yang sudah di ambil
				for(int i=0; i<lsProduct.size()-1; i++){
					Product product = (Product) command.getLsProduk().get(i);
					if(product.getLsbs_id().compareTo(productIns.getLsbs_id())==0){
						err.reject("","Product tersebut sudah di ambil, silahkan pilih produk yang lain");
						lsProduct.remove(lsProduct.size()-1);
						command.setLsProduk(lsProduct);
						cek = false;
						succ = "";
						break;
					}
				}
				if(cek){
					command = (Utama) bacManager.prosesExtraPremi(command,keterangan, Integer.parseInt(flag_jenis));
				}
			}else{
				elionsManager.updateMstProductInsured(command.getLsProduk());
			}

		}else if(flag.equals("4")){//Delete
			for(int i=0; i<cheklist.length; i++){
				Product delproduk = (Product) command.getLsProduk().get(Integer.parseInt(cheklist[i]));
				bacManager.deleteExtraPremi(command, delproduk);
				command.getLsProduk().remove(Integer.parseInt(cheklist[i]));
			}
		}
		
		elionsManager.updateMstInsuredMsteStandard(command.getSpaj(), command.getInsuredNo(), command.getMsteStandard());
		hitTotal(command);
		return new ModelAndView("uw/premi", err.getModel()).addObject("submitSuccess", succ).addAllObjects(this.referenceData(request,cmd,err));
	}
	
	private void hitTotal(Utama command){
		Double totPremi = 0.0;
		Double premi = 0.0;
		for(int i=0; i<command.getLsProduk().size(); i++){
			Product prod = (Product) command.getLsProduk().get(i);
			if(prod.getMspr_premium()==null) prod.setMspr_premium(new Double(0));
			premi = prod.getMspr_premium().doubleValue();
			totPremi += premi;
		}
		command.setTotalPremi(new Double(totPremi));
		command.setTotalTot(new Double(totPremi));
	}
	
	/*
    900 EXTRA MORTALITA
    901 EXTRA JOB
    902 EXTRA PA - 800, 810
    903 EXTRA PAYOR - 802, 815, 817
    904 EXTRA HAMIL                   
    905 EXTRA MORTALITA BAYI          
    906 EXTRA WAIVER - 804, 814, 816, 827
    907 EXTRA CI - 801, 813
    908 EXTRA TPD - 812
    909 EXTRA SCHOLARSHIP - 835
    910 EXTRA MEDICAL(ALL TYPE PRODUK MEDICAL) - 819, 823, 848
    911 EXTRA DOMISILI
    912 EXTRA ESCI - 837
    999 EXTRA RIDER - 818
	*/
	public Product wf_set_premi(Product productEdit, Utama command){
		Double premium = 0.0;
		Double extUP = 0.0;
		Integer liBisnis, liBisnis2;
		Map dataEm = new HashMap();
		Map dataEmNew = new HashMap();
		Integer lscb_kali = bacManager.selectPengaliCaraBayar(productEdit.getReg_spaj());
		Integer lsgb_id = uwManager.selectIsPolisUnitlink(command.getSpaj());
		dataEm = uwManager.selectExtraMortalita(productEdit.getReg_spaj());
		Date spajDateParam = uwManager.getUwDao().selectSpajDateRateMortalitaFromConfig();
		Date spajdate = bacManager.selectSpajDate(productEdit.getReg_spaj());
		//Extra Mortalita
		if(productEdit.getLsbs_id() == 900){
			if (lsgb_id==17 && spajdate.after(spajDateParam)){
				dataEmNew = bacManager.selectExtraMortalitaNew(productEdit.getReg_spaj());
				productEdit.setMspr_rate(Double.parseDouble(dataEmNew.get("LEM_YRT").toString()));
				premium = (productEdit.getMspr_rate()/ 1000) * (productEdit.getMspr_extra() / 100) * Double.parseDouble(dataEmNew.get("MSPR_TSI").toString()) * 0.1;
			}else{
				productEdit.setMspr_rate(Double.parseDouble(dataEm.get("LEM_YRT").toString()));
				premium = (productEdit.getMspr_rate()/ 1000) * (productEdit.getMspr_extra() / 100) * Double.parseDouble(dataEm.get("MSPR_TSI").toString()) * 0.1;	
			}

		
		//Extra Job & Hamil
		}else if("901,904,911".indexOf(productEdit.getLsbs_id().toString())>-1 && productEdit.getLsdbs_number() == 1){
			if(Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 115 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 116 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 119 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 120 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 121 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 127 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 128 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 129 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 134 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 140 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 141 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 152 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 153 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 159 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 160 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 162 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 164 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 166 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 174 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 190 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 217 ||
			   Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 218 || Integer.parseInt(dataEm.get("LSBS_ID").toString()) == 220 /*<= req mba hanifah*/ || lsgb_id == 17) {
				premium = (productEdit.getMspr_extra() / 1000) * Double.parseDouble(dataEm.get("MSPR_TSI").toString()) * 0.1;
			}else{
				Double caraByr = 1.0;
				if(Integer.parseInt(dataEm.get("LSCB_ID").toString())  == 1) caraByr = 0.27; // triwulan
				else if(Integer.parseInt(dataEm.get("LSCB_ID").toString()) == 2)  caraByr = 0.525; // semester
				else if(Integer.parseInt(dataEm.get("LSCB_ID").toString()) == 6)  caraByr = 0.1; // bulanan
				premium = (productEdit.getMspr_extra() / 1000) * Double.parseDouble(dataEm.get("MSPR_TSI").toString()) * caraByr;
			}
			
		//Extra Lain-lain
		}else if("902,903,906,907,908,909,910,912,999".indexOf(productEdit.getLsbs_id().toString())>-1){
			for(int i=0; i<command.getLsProduk().size(); i++){
				Product prod = (Product) command.getLsProduk().get(i);
				liBisnis = prod.getLsbs_id();
				if(liBisnis.intValue() == productEdit.getLsbs_id()){
					for(int j=0; j<command.getLsProduk().size(); j++){
						Product prod2 = (Product) command.getLsProduk().get(j);
						liBisnis2 = prod2.getLsbs_id();
						if(productEdit.getLsbs_id() == 902 && ("800,810".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							extUP = prod2.getMspr_tsi();
						}else if(productEdit.getLsbs_id() == 903 && ("802,815,817,828".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							// Premi pokok + top up tahunan
							extUP = (lscb_kali * prod2.getMspr_tsi());
						}else if(productEdit.getLsbs_id() == 906 && ("804,814,816,827".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							// Premi pokok + top up tahunan
							extUP = (lscb_kali * prod2.getMspr_tsi());
						}else if(productEdit.getLsbs_id() == 907 && ("801,813".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							extUP = prod2.getMspr_tsi();
						}else if(productEdit.getLsbs_id() == 908 && ("812".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							extUP = prod2.getMspr_tsi();
						}else if(productEdit.getLsbs_id() == 909 && ("835".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							Double faktorup = bacManager.selectRateUpScholarship(835, command.getUmurTt(), prod2.getLsdbs_number());
							extUP = (prod2.getMspr_tsi() * faktorup);
						}else if(productEdit.getLsbs_id() == 910 && ("819,823,848".indexOf(liBisnis2.toString())>-1)){
							
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							extUP = prod2.getMspr_tsi();
							
							if("848".contains(liBisnis2.toString())){
								Product p1 = (Product) findByName(command.getLsProduk(), "TERTANGGUNG I");
								if(p1!= null){
									productEdit.setMspr_rate(Double.parseDouble(p1.getMspr_rate().toString()));
									extUP = p1.getMspr_tsi();
								};
							};
							
							
						}else if(productEdit.getLsbs_id() == 912 && ("837".indexOf(liBisnis2.toString())>-1)){
							Double result = elionsManager.selectRateRider(command.getLkuId(), command.getUmurTt().intValue(), 0, 837, prod2.getLsdbs_number());
							productEdit.setMspr_rate(new Double(result));
							extUP = prod2.getMspr_tsi();
						}else if(productEdit.getLsbs_id() == 999 && ("818".indexOf(liBisnis2.toString())>-1)){
							productEdit.setMspr_rate(Double.parseDouble(prod2.getMspr_rate().toString()));
							extUP = prod2.getMspr_tsi();
						}
					}
				}
			}
			if("910".indexOf(productEdit.getLsbs_id().toString())>-1){
				for(int i=0; i<command.getLsProduk().size(); i++){
					Product prod = (Product) command.getLsProduk().get(i);
					liBisnis = prod.getLsbs_id();
					if(liBisnis.intValue() == productEdit.getLsbs_id()){
						for(int j=0; j<command.getLsProduk().size(); j++){
							Product prod2 = (Product) command.getLsProduk().get(j);
							liBisnis2 = prod2.getLsbs_id();
							
							if(productEdit.getLsbs_id() == 910 && ("819,823,848".indexOf(liBisnis2.toString())>-1)){
								if("819".indexOf(liBisnis2.toString())>-1){
									premium = (productEdit.getMspr_extra() / 100) * productEdit.getMspr_rate() * 0.1;
								}else if("848".indexOf(liBisnis2.toString())>-1){
									premium = (productEdit.getMspr_extra() / 100) * productEdit.getMspr_rate() * 0.1;
								}else{
									premium = (productEdit.getMspr_extra() / 100) * productEdit.getMspr_rate() * 0.12;
								}
							}
						}
					}
				}
			}else{
				premium = (productEdit.getMspr_rate() / 1000) * (productEdit.getMspr_extra() / 100) * extUP * 0.1;
			}
		}
		productEdit.setMspr_premium(premium);
		return productEdit;
	}
}
