package com.ekalife.elions.dao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.DetBilling;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Tertanggung2;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class AkseptasiDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.akseptasi_ssh.";
	}	

	public List selectMstPolicy(Integer lspdId,Integer lstbId,Integer lsspId1,Integer lsspId2,
			String kata,String kategori) throws DataAccessException{
		Map param=new HashMap();
		param.put("lspd_id",lspdId);
		param.put("lstb_id",lstbId);
		param.put("lssp_id",lsspId1); //cuma logika untuk filterisasi doang
		param.put("lssp_id1",lsspId1);
		param.put("lssp_id2",lsspId2);
		param.put("kata",kata);
		param.put("kategori",kategori);
		return query("mst_policy",param);
	}
	/**Fungsi :	Untuk Menampilkan Produk-produk apa saja yang dipilih pada spaj tersebut
	 * @param	String spaj,Integer insured,Integer active
	 * @return 	List
	 * */
	public List selectMstProductInsured(String spaj,Integer insured,Integer active) throws DataAccessException{
		Map param =new HashMap();
		param.put("spaj",spaj);
		param.put("insured",insured);
		param.put("active",active);
		return query("mst_product_insured",param);
	}
	
	public Double selectmst_product_insuredUP(String spaj) throws DataAccessException{
		return(Double) querySingle("mst_product_insuredUP",spaj);
	}
	
	
	public List selectMstProductInsured(String spaj) throws DataAccessException{
		return query("mst_product_insured2",spaj);
	}
	
	public String selectMstProductInsured3(String spaj) throws DataAccessException{
		return(String) querySingle("mst_product_insured3",spaj);
	}
	
	public Product selectMstProductInsured(String spaj,Integer lsbsId) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("lsbs_id", lsbsId);
		return (Product) querySingle("selectMstProductInsuredPerProduct",param);
	}
	
	public Product selectMstProductInsuredDetail(String spaj,Integer lsbsId,Integer lsdbsNumber) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("lsbs_id", lsbsId);
		param.put("lsdbs_number", lsdbsNumber);
		return (Product) querySingle("selectMstProductInsuredPerProduct",param);
	}
	
	public List selectLstDetBisnisAll() throws DataAccessException{
		return query("lst_det_bisnis",null);
	}

	public List selectLstDetBisnisRider(String filBisnisId) throws DataAccessException{
		Map map=new HashMap();
		map.put("filBisnisId",filBisnisId);
		return query("lst_det_bisnis",map);
	}
	
	public Double selectLstPremiEm(Integer lsbsId,String kursId,Integer caraBayar,Integer lamaBayar,
			Integer lamaTanggung,Integer umur,Double extra) throws DataAccessException{
		Map map=new HashMap();
		map.put("lsbs_id",lsbsId);
		map.put("lku_id",kursId);
		map.put("lscb_id",caraBayar);
		map.put("lama_bayar",lamaBayar);
		map.put("lama_tanggung",lamaTanggung);
		map.put("umur",umur);
		map.put("extra",extra);
		return (Double) querySingle("lst_premiEm.lspreRate",map);
	}

	public Tertanggung2 selectTertanggung(String spaj,Integer lsle_id) throws DataAccessException{
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("lsle_id",lsle_id);
		return (Tertanggung2) querySingle("selectTertanggung",map);
	}
	
	public Pemegang2 selectPemegang(String spaj,Integer lsle_id) throws DataAccessException{//lsle_id=4
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("lsle_id",lsle_id);
		return (Pemegang2) querySingle("selectPemegang",map);
	}
	
	public DataUsulan2 selectDataUsulan(String spaj, Integer lstb_id) throws DataAccessException{//lstb_id=1
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("lstb_id",lstb_id);
		return (DataUsulan2) querySingle("selectDataUsulan",map);
	}
	
	public Integer selectMstInsuredMsteStandard(String spaj,Integer insuredNo) throws DataAccessException{
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("insuredNo",insuredNo);
		return (Integer) querySingle("selectMstInsuredMsteStandard",map);
	}

	public void insertMstProductInsert(Product productInsert) throws DataAccessException{
		insert("insertMstProductInsured",productInsert);
	}
	
	public void updateMstProductInsert(Product productUpdate) throws DataAccessException{
		update("updateMstProductInsured",productUpdate);
	}

	public List selectMstPositionSpaj(String spaj) throws DataAccessException{
		return query("selectMstPositionSpaj",spaj);
	}

	public Integer selectLstPositionSpaj(Integer lssa_id) throws DataAccessException{
		return (Integer) querySingle("selectLstStatusAccept",lssa_id);
	}

	public Map selectMstCntPolis(String lcaId,Integer lsbsId) throws DataAccessException{
		Map param=new HashMap();
		param.put("as_cab",lcaId);
		param.put("as_bisnis",lsbsId);
		return (HashMap)getSqlMapClientTemplate().queryForObject("elions.uw.select.mst_cnt_polis",param);
	}
	
	/*
	public Date selectSysdateTime() throws DataAccessException{
		return(Date) querySingle("selecetDateTime",null);
	}
	*/
	
	/*
	public Date selectSysdate() throws DataAccessException{
		return (Date) querySingle("selecetDate",null);
	}
	*/
	
	public Map mst_billing(String spaj ,  String data)
	{
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("qry",data );
		return (HashMap)getSqlMapClientTemplate().queryForObject("elions.akseptasi_ssh.mst_billing",param);

	}
	
	public String cabang_production(String spaj,Integer prod_ke)
	{
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("prod_ke", prod_ke);
		return (String)querySingle("cabang_production",param);
	}
	
	public Map mst_billing_ke(String spaj , Integer ke)
	{
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("ke",ke);
		return (HashMap)querySingle("mst_billing_ke",param);
	}
	
	public void insertMstCntPolis(String lcaId, Integer lsbsId, Long ldNo) throws DataAccessException{
		Map insParam=new HashMap();
		insParam.put("as_cab",lcaId);
		insParam.put("as_bisnis",lsbsId);
		insParam.put("ld_no",ldNo);
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_cnt_polis",insParam);
	}

	public void updateMstCntPolis(String lcaId,Integer lsbsId,Long ldNo) throws DataAccessException{
		 Map upParam=new HashMap();
		 upParam.put("ld_no",ldNo);
		 upParam.put("as_cab",lcaId);
		 upParam.put("as_bisnis",lsbsId);
	 }

	public void updateMstPolicy1(String mspoPolicyNo,String regSpaj,Integer lspdId,Integer lstbId) throws DataAccessException{
		Map upParam=new HashMap();
		upParam.put("mspo_policy_no",mspoPolicyNo);
		upParam.put("reg_spaj",regSpaj);
		upParam.put("lspd_id",lspdId);
		upParam.put("lstb_id",lstbId);
		update("updateMstPolicy1",upParam);
	}

	public void updateMstPolicy2(String regSpaj,Integer lspdIdNew,Integer lspdIdOld,Integer lstbId) throws DataAccessException{
		Map upParam=new HashMap();
		upParam.put("reg_spaj",regSpaj);
		upParam.put("lstb_id",lstbId);
		upParam.put("lspd_id_new",lspdIdNew);
		upParam.put("lspd_id_old",lspdIdOld);
		
		update("updateMstPolicy2",upParam);
	}
	
	public String selectMstCancelRegSpaj(String spaj) throws DataAccessException{
		return (String)querySingle("mst_cancel",spaj);
	}

	public Map selectMstBillingMax(String spaj) throws DataAccessException{
		return (HashMap)querySingle("mst_billing_max",spaj);
	}
	
	public void updateMstCounter(Integer number,String lcaId,Integer value) throws DataAccessException{
		Map param=new HashMap();
		param.put("msco_number",number);
		param.put("lca_id",lcaId);
		param.put("msco_value",value);
		update("updateMstCounter",param);
	}

	public Date selectFAddMonths(String tgl,Integer bulan) throws DataAccessException{
		Map param=new HashMap();
		param.put("s_ldt_closing",tgl);
		param.put("ai_month",bulan);
		return (Date)getSqlMapClientTemplate().queryForObject("elions.uw.select.f_add_months",param);
	}

	public Map selectLstDailyCurrency(Date tgl,String lkuId) throws DataAccessException{
		Map param=new HashMap();
		param.put("tgl",tgl);
		param.put("lku_id",lkuId);
		return (HashMap)querySingle("selectLstDailyCurrency",param);
	}

	public void insertMstEndors(String noEnd,String spaj,Integer internal,
			Double cost,Integer print,Integer lspdId) throws DataAccessException{
		Map insParam=new HashMap();
		insParam.put("msen_endors_no",noEnd);
		insParam.put("reg_spaj",spaj);
		insParam.put("msen_internal",internal);
		insParam.put("msen_endors_cost",cost);
		insParam.put("msen_print",print);
		insParam.put("lspd_id",lspdId);
		
		insert("insertMstEndors",insParam);
	}

	public void insertMstDetEndors(String noEnd,Integer number,Integer lsjeId,
			Integer insured, String sPlanLama,String sTsiLama,String sMassaLama,
			String sPremiLama, String sPlanBaru,String sTsiBaru,
			String sMassaBaru, String sPremiBaru) throws DataAccessException{
		
		Map insParam=new HashMap();
		insParam.put("no_end",noEnd);
		insParam.put("number",number);
		insParam.put("lsje_id",lsjeId);
		insParam.put("insured",insured);
		insParam.put("plan_lama",sPlanLama);
		insParam.put("tsi_lama",sTsiLama);
		insParam.put("massa_lama",sMassaLama);
		insParam.put("premi_lama",sPremiLama);
		insParam.put("plan_baru",sPlanBaru);
		insParam.put("tsi_baru",sTsiBaru);
		insParam.put("massa_baru",sMassaBaru);
		insParam.put("premi_baru",sPremiBaru);
		
		insert("insertMstDetEndors",insParam);
	}
	
	public Map selectMaxMstPorductInsured(String spaj) throws DataAccessException{
		return (HashMap) querySingle("mst_product_insuredTsi",spaj);
	}
	
	public String selectLstBisnisLsdbsName(String spaj) throws DataAccessException{
		return (String)querySingle("lst_det_bisnisLsdbsName",spaj);
	}
	
	public void updateMstPolicyBilling(String spaj,Date nextBill,Integer prosesBill) throws DataAccessException{
		Map upParam=new HashMap();
		upParam.put("next_bill",nextBill);
		upParam.put("proses_bill",prosesBill);
		upParam.put("spaj",spaj);
		update("updateMstPolicy_billing",upParam);
	}
	
	public void updateMstInsuredDate(String spaj,Date begDate,Date endDate) throws DataAccessException{
		Map upParam=new HashMap();
		upParam.put("beg_baru",begDate);
		upParam.put("end_baru",endDate);
		upParam.put("spaj",spaj);
		update("updateMstInsured_date",upParam);
	}
	
	public void updateMstProductInsuredDate(String spaj,Date begDate,Date endDate) throws DataAccessException{
		Map upParam=new HashMap();
		upParam.put("beg_baru",begDate);
		upParam.put("end_baru",endDate);
		upParam.put("spaj",spaj);
		update("updateMstProductInsured_date",upParam);
	}
	
	public void insertMstBilling(Billing billing) throws DataAccessException{
		insert("insertMstBilling",billing);
	}
	
	public void insertMstDetBilling(DetBilling detBilling) throws DataAccessException{
		insert("insertMstDetBilling",detBilling);
	}
	
	public List selectMstBilling(String spaj,Integer tahunKe,Integer premiKe) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tahun_ke",tahunKe);
		param.put("premi_ke",premiKe);
		return query("selectMstBilling",param);
	}
	
	public List selectMstDetBilling(String spaj,Integer tahunKe,Integer premiKe) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tahun_ke",tahunKe);
		param.put("premi_ke",premiKe);
		return query("selectMstDetBilling",param);
	}
	
	private static double chooseBetween(boolean a,double param1,double param2) throws DataAccessException{
		if(a){
			return param1;
		}else
			return param2;
	}

	//0=ok 1=no polis error   2=no endor error
	public int prosesTransfer(String spaj,List lsProdukInsured,Product produkInsured,Integer tahunKe,Integer premiKe,
							   String lcaId,Integer lsspId,String lwkId,String lsrgId,String lkuId,Integer lusId,String mspoPolicyNo,
							   Integer lspdId,Integer lstbId)throws Exception{
		tahunKe=new Integer(tahunKe.intValue()+1);
		premiKe=new Integer(premiKe.intValue()+1);
		double premi=0;
		String sPremiBaru,sPremiLama = null,sTsiBaru=null,sTsiLama=null,sPlanLama=null;
		String massa_lama, massa_baru;
		String kurs=lkuId;
		Double ldecKurs = null,ldecTemp,ldecStamp;
		String spajLama,noEnd;
		Double tsiLama,premiLama;
		Integer lsbsId;
		Date begDateNew,endDateNew;
		DetBilling insDetBilling=new DetBilling();
		Billing insBilling=new Billing();
		List lsBilling,lsDetBilling;
		String noBill=null;
		//
		DataUsulan2 dataUsulan=this.selectDataUsulan(spaj,new Integer(1)); //lstb =1=individu
		lsbsId=(Integer)dataUsulan.getLsbs_id();
		begDateNew=(Date)dataUsulan.getMspr_beg_date();
		endDateNew=(Date)dataUsulan.getMspr_end_date();
		String sPlanBaru=(String)dataUsulan.getLsdbs_name();
		//
		
		NumberFormat nf=new DecimalFormat("#,###.00;(#,###.00)");
		
		if(mspoPolicyNo==null){
			mspoPolicyNo=f_get_nopolis(lcaId,lsbsId);
			if(mspoPolicyNo.length()<= 0 || mspoPolicyNo==null) { 
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				return 1;
			}
			//update no polis
			updateMstPolicy1(mspoPolicyNo,spaj,lspdId,lstbId);
		}
	//ins det billing ssh
	noEnd= f_get_no_end(lcaId);
	if(noEnd==null) {
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return 2;
	}
	
	//lds3.SetItem(1,"mspr_premium",tab_1.tabpage_3.dw_3.GetItemDecimal(1,"premi"))
	Product upProduct=new Product();
	upProduct.setReg_spaj(spaj);
	upProduct.setLsbs_id(dataUsulan.getLsbs_id());
	upProduct.setMspr_premium(dataUsulan.getMspr_premium());
	updateMstProductInsert(upProduct);
	//
	insBilling.setReg_spaj(spaj);
	insBilling.setLca_id(lcaId);
	insBilling.setLwk_id(lwkId);
	insBilling.setLsrg_id(lsrgId);
	insBilling.setLku_id(lkuId);
	
	if(lsspId.intValue()==10) {
		
		tahunKe=new Integer(tahunKe.intValue()+1);
		premiKe=new Integer(premiKe.intValue()+1);
		//
		for(int i=0;i<lsProdukInsured.size();i++) {
			
			insDetBilling.setReg_spaj(spaj);
			insDetBilling.setMsbi_tahun_ke(tahunKe);
			insDetBilling.setMsbi_premi_ke(premiKe);
			insDetBilling.setMsdb_det_ke(new Integer(i+1));
			insDetBilling.setLsbs_id(produkInsured.getLsbs_id());
			insDetBilling.setLsdbs_number(produkInsured.getLsdbs_number());
			insDetBilling.setMsdb_premium(produkInsured.getMspr_premium());
			insDetBilling.setMsdb_discount(produkInsured.getMspr_discount());
			insertMstDetBilling(insDetBilling);
			premi= (int) (premi+ (produkInsured.getMspr_premium().doubleValue() - 
						  produkInsured.getMspr_discount().doubleValue()));
			
		}
		//
		

		insBilling.setMsbi_tahun_ke(tahunKe);
		insBilling.setMsbi_premi_ke(premiKe);
		
		if(lkuId.equals("01"))
			lkuId="Rp. ";
		else
			lkuId="US$ ";
		sPremiBaru=lkuId+" "+nf.format(premi);
		massa_lama=defaultDateFormatStripes.format( selectFAddMonths(defaultDateFormatStripes.format(begDateNew),new Integer(-12) )) +
				   " s/d "+ defaultDateFormatStripes.format(FormatDate.add(begDateNew,Calendar.DATE,-1)) ;
		massa_baru=defaultDateFormatStripes.format(begDateNew)+" s/d "+defaultDateFormatStripes.format(endDateNew);
		insBilling.setMsbi_beg_date(begDateNew);
		insBilling.setMsbi_end_date(endDateNew);
		insBilling.setMsbi_due_date(selectFAddMonths(defaultDateFormatStripes.format(begDateNew),new Integer(1)));
		insBilling.setMsbi_aktif_date(begDateNew);
	}else{
		lsDetBilling=selectMstDetBilling(spaj,tahunKe,premiKe);
		DetBilling detBilling=(DetBilling)lsDetBilling.get(0);
		//
		for(int i=0;i<lsProdukInsured.size();i++) {
			Product pInsured=(Product)lsProdukInsured.get(i);
			insDetBilling.setReg_spaj(spaj);
			insDetBilling.setMsbi_tahun_ke(new Integer(detBilling.getMsbi_tahun_ke().intValue()+1));
			insDetBilling.setMsbi_premi_ke(new Integer(detBilling.getMsbi_premi_ke().intValue()+1));
			insDetBilling.setMsdb_det_ke(new Integer(i+1));
			insDetBilling.setLsbs_id(pInsured.getLsbs_id());
			insDetBilling.setLsdbs_number(pInsured.getLsdbs_number());
			insDetBilling.setMsdb_premium(pInsured.getMspr_premium());
			insDetBilling.setMsdb_discount(pInsured.getMspr_discount());
			premi=premi+(pInsured.getMspr_premium().doubleValue() - pInsured.getMspr_discount().doubleValue());
		}
		//
		//Ins bill_ssh

		
		lsBilling=selectMstBilling(spaj,tahunKe,premiKe);
		Billing billing=(Billing) lsBilling.get(0);
		//lds.SetItemStatus(1, 0 ,Primary!, NewModified!)
		insBilling.setMsbi_tahun_ke(new Integer(billing.getMsbi_tahun_ke().intValue()+1));
		insBilling.setMsbi_premi_ke(new Integer(billing.getMsbi_premi_ke().intValue()+1));
		insBilling.setMsbi_bill_no(noBill);
		sPremiBaru=billing.getLku_symbol()+" "+nf.format(premi);
		sPremiLama=sPremiBaru;
		sTsiBaru=billing.getLku_symbol()+" "+nf.format(dataUsulan.getMspr_tsi().doubleValue());
		sTsiLama=sTsiBaru;
		sPlanLama=sPlanBaru;
		//
		Date begDateAwal=billing.getMsbi_beg_date();
		String sBegAwal=defaultDateFormatStripes.format(begDateAwal);
		Date begDateBaru=selectFAddMonths(sBegAwal,new Integer(12));
		String sBegBaru=defaultDateFormatStripes.format(begDateBaru);
		//
		Date endDateAwal=billing.getMsbi_end_date();
		String sEndAwal=defaultDateFormatStripes.format(endDateAwal);
		//
		Date endDateBaru=FormatDate.add(selectFAddMonths(sBegBaru,new Integer(12)),Calendar.DATE,-1);
		String sEndBaru=defaultDateFormatStripes.format(endDateBaru);
		//
		massa_lama=sBegAwal+" s/d "+sEndAwal;
		massa_baru=sBegBaru+" s/d "+sEndBaru;
		insBilling.setMsbi_beg_date(begDateBaru);
		insBilling.setMsbi_end_date(endDateBaru);
		insBilling.setMsbi_due_date(selectFAddMonths(defaultDateFormatStripes.format(billing.getMsbi_due_date()),new Integer(12)));
		insBilling.setMsbi_aktif_date(begDateBaru);

	}


		//
		insBilling.setMsbi_policy_cost(new Double(0));
		insBilling.setMsbi_hcr_policy_cost(new Double(0));
		insBilling.setMsbi_ttl_card_cost(new Double(0));
		//
		if(! kurs.equals("01"))
			ldecKurs=new Double( f_get_kurs_jb(commonDao.selectSysdateTrunc(),"J").doubleValue() ) ;
		//
		ldecTemp=new Double( chooseBetween(kurs.equals("01"),premi*ldecKurs.doubleValue(),premi) );
		ldecStamp=new Double( chooseBetween( ldecTemp.doubleValue() < 250000,0,
			chooseBetween( ldecTemp.doubleValue() < 1000000,3000,6000 )) );
		//
		if(! kurs.equals("01"))
			ldecStamp=new Double (ldecStamp.doubleValue()/ldecKurs.doubleValue());
		//
		insBilling.setMsbi_stamp(ldecStamp);
		insBilling.setMsbi_input_date(new Date());
		insBilling.setMsbi_remain(new Double(premi));
		insBilling.setMsbi_paid(new Integer(0));
		insBilling.setMsbi_active(new Integer(1));
		insBilling.setMsbi_print(new Integer(0));
		insBilling.setMsbi_add_bill(new Integer(0));
		insBilling.setMsbi_flag_sisa(new Integer(0));
		insBilling.setLus_id(lusId);
		insBilling.setLspd_id(new Integer(12));
		insertMstBilling(insBilling);
		//
		spajLama=selectMstCancelRegSpaj(spaj);
	
		if(spajLama==null || spajLama.equals("") ) {
			insertMstEndors(noEnd,spaj,new Integer(1),new Double(0),new Integer(0),new Integer(32));
			updateMstPolicy2(spaj,new Integer(32),lspdId,lstbId);
		}else{
			Map a=selectMaxMstPorductInsured(spajLama);
			tsiLama=(Double)a.get("MAX_TSI");
			premiLama=(Double)a.get("SUM_PREMIUM");
			//
			sPlanLama=selectLstBisnisLsdbsName(spajLama);
			sPremiLama=lkuId+" "+nf.format(premiLama.doubleValue());
			sTsiLama=lkuId+" "+nf.format(tsiLama.doubleValue());
			sTsiBaru=lkuId+" "+nf.format(dataUsulan.getMspr_tsi().doubleValue());
			
			insertMstEndors(noEnd,spaj,new Integer(1),new Double(0),new Integer(0),new Integer(32));
			updateMstPolicy2(spaj,new Integer(6),lspdId,lstbId);
		}
		//
		insertMstDetEndors(noEnd,new Integer(1), new Integer(17), new Integer(1),
				sPlanLama, sTsiLama,massa_lama,sPremiLama,sPlanBaru,sTsiBaru,massa_baru,sPremiBaru);
		
		Date dNext=FormatDate.add(endDateNew,Calendar.DATE,1);
		updateMstPolicyBilling(spaj,dNext,new Integer(0));
		updateMstInsuredDate(spaj,begDateNew,endDateNew);
		updateMstProductInsuredDate(spaj,begDateNew,endDateNew);
			
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/*
	public Date selectSysdate(Integer flag){
		if(flag.intValue()==1)
			return selectSysdate();
		else
			return new Date();
		
	}
	*/
	
	private String f_get_no_end(String lca_id)throws Exception{
		
		Integer no;
		long thnNow, ctl=0, thn = 0;
		String noEnd = null;
		//Date tgl= selectSysdate(new Integer(1));
		Date tgl= commonDao.selectSysdateTrunc();
		thnNow=tgl.getYear()+1900;
		no=new Integer(this.uwDao.f_get_counter(new Integer(5),lca_id).intValue());
		
		if(no==null)
			return null;
			
		else	
			if(no.intValue()==0){
				thn=thnNow;
				ctl=1;
			}
			else if(String.valueOf(no).length()==9){
				thn=Long.parseLong(String.valueOf(no).substring(0,4));
				ctl=Long.parseLong(String.valueOf(no).substring(String.valueOf(no).length()-4,
						String.valueOf(no).length()));
				ctl++;
				if(thn!=thnNow){
					thn=thnNow;
					ctl=1;
				}
			}else{
				return null;
			}
			//
			no=new Integer( (int) ((thn*100000)+ctl) );

			updateMstCounter(new Integer(5),lca_id,no);
			noEnd=lca_id+String.valueOf(no).substring(0,4)+"E"+
						 String.valueOf(no).substring(String.valueOf(no).length()-5,
								 					  String.valueOf(no).length());
		return noEnd;
	}

	private Integer f_get_kurs_jb(Date tgl,String jb){
		Integer ldecKurs=new Integer(1);
		Map a=(HashMap) selectLstDailyCurrency(tgl,"02");
		
		if(jb.equals("J")){
			ldecKurs=(Integer)a.get("LKH_KURS_JUAL");
		}else if(jb.equals("B")){
			ldecKurs=(Integer)a.get("LKH_KURS_BELI");
		}else
			ldecKurs=(Integer)a.get("LKH_CURRENCY");
		
		return ldecKurs;
	}

	/**Fungsi : Untuk Menggenerate Counter no POlis
	 * @param	String lcaId,Integer lsbsId
	 * @return 	String
	 * */
	public String f_get_nopolis(String lcaId,Integer lsbsId){

		Long ld_no = null, ld_max = null;
		long ll_th_now, ll_ctl = 0, ll_th = 0; 
		String  ls_nopol=null ;
		boolean baru;//,up_mst_cnt_polis=false,ins_mst_cnt_polis=false; 
		String s_ld_no="",s_as_cab="",s_th_now;
		String bisnisId;
		int i_ld_no=0,i_as_cab=0;
		baru      = false;
		Date tglNow;
		DecimalFormat fBisnis= new DecimalFormat ("000");
		bisnisId=fBisnis.format(lsbsId.intValue());
		//tglNow=selectSysdate(new Integer(1));
		tglNow=commonDao.selectSysdateTrunc();
		ll_th_now = tglNow.getYear()+1900;
//		if(logger.isDebugEnabled())logger.debug("ll_th_now ="+ll_th_now);
		//
		Map cnt=selectMstCntPolis(lcaId,lsbsId);
		
		if(cnt==null){
			baru=true;
			ld_no=new Long(0);
			ld_max=new Long(0);
		}else{
			if(cnt.get("MSCNP_VALUE")==null)
				ld_no=null;
			else
				ld_no=Long.valueOf(cnt.get("MSCNP_VALUE").toString());
			//
			if(cnt.get("MSCNP_MAX")==null)
				ld_max=null;
			else
				ld_max=Long.valueOf(cnt.get("MSCNP_MAX").toString());
			
			if(ld_max.longValue() <= 0){ 
				ls_nopol=null;
				return ls_nopol;
			}else{ 
				if(ld_no==null || ld_max==null){
					ls_nopol=null;
					return ls_nopol;
				}
			}
			

		}
		//
		if(ld_no.longValue() >= ld_max.longValue())
			ld_no = new Long(0);
		//
		if(ld_no.longValue() == 0){
			if(lcaId.equals("62")){
				ll_th = Long.parseLong(Long.toString(ll_th_now).substring(2, 4)) ;
			}else{
				ll_th = ll_th_now;
			}
			ll_ctl = 1;
		}else{
			s_ld_no=String.valueOf(ld_no);
			if (s_ld_no.length()== 9 ){
				i_ld_no=s_ld_no.length();
				if(lcaId.equals("62")){
					s_th_now = Long.toString(ll_th_now);
					ll_th=Long.parseLong(s_ld_no.substring(0,2));
					ll_ctl = Long.parseLong(s_ld_no.substring(i_ld_no-7,i_ld_no));
					
					ll_ctl++;
					if(!s_ld_no.substring(0,2).equals(s_th_now.substring(2,4))){
						ll_th = Long.parseLong(s_th_now.substring(2,4));
						ll_ctl = 1;
					}
				}else{
					ll_th=Long.parseLong(s_ld_no.substring(0,4));
					ll_ctl = Long.parseLong(s_ld_no.substring(i_ld_no-5,i_ld_no));
					
					ll_ctl++;
					if(ll_th != ll_th_now){
						ll_th = ll_th_now;
						ll_ctl = 1;
					}
				}
				
				
			}
		}
		
		if(lcaId.equals("62")){
			ld_no = new Long((ll_th * 10000000) + ll_ctl);
		}else{
			ld_no = new Long((ll_th * 100000) + ll_ctl);
		}
		
		//
		s_as_cab=lcaId.trim();
		i_as_cab=s_as_cab.length();
		lcaId=s_as_cab.substring(i_as_cab-2,i_as_cab);
		
		if(baru){//insert
			insertMstCntPolis(lcaId,lsbsId,ld_no);
			
		}else{//update
			updateMstCntPolis(lcaId,lsbsId,ld_no);
		}
		//
		NumberFormat fmt = new DecimalFormat ("#");
		s_ld_no=fmt.format(ld_no);
		
		ls_nopol = lcaId+ bisnisId + ld_no;
		
		return ls_nopol;
	}

	public Double f_get_sar(int ai_type, int ai_bisnis_id,int ai_bisnis_no,String as_kurs,int ai_cbayar,int ai_lbayar,//ok
			int ai_ltanggung,int ai_year,int ai_age) throws Exception{
		Integer li_ltanggung_max=null;
		Double ldec_value =null; //dec{3} 
		if(ai_bisnis_id < 300){
			ldec_value=null;
			li_ltanggung_max=null;
			
			/*switch(ai_bisnis_id){
			case 63: case 65: case 66: case 67: case 68: case 69: case 73: case 74: case 75: case 76: case 77: case 79: case 80: case 81: case 82: case 85: //Belum ada table SAR nya
				ldec_value = new Double(1000);//060401BC
			break;
			default:*/
				if((ai_bisnis_id == 70) || (ai_bisnis_id == 71) || (ai_bisnis_id == 72) || (ai_bisnis_id == 172))
					ai_bisnis_id = 31;
				else if(ai_bisnis_id == 78) 
					ai_bisnis_id = 52; 
				else if(ai_bisnis_id == 82)
					ai_bisnis_id = 69;
				
				if((ai_cbayar == 1) || (ai_cbayar == 2) || (ai_cbayar == 6))
						ai_cbayar = 3;
				if((ai_cbayar == 4) || (ai_cbayar == 5))
					ai_cbayar = 3;
				//
				li_ltanggung_max=(Integer) this.uwDao.selectLstReinsDesc1(as_kurs,ai_bisnis_id,ai_bisnis_no);
				if (li_ltanggung_max!=null) 
					if (li_ltanggung_max.intValue() > 0)
						ai_ltanggung = li_ltanggung_max.intValue() - ai_age;
				//
				if (ai_type == 1)
					ai_year = 1;
				//
				if (ai_bisnis_id  == 62)
					ai_bisnis_id  =  52; //040401BC
				if ((ai_bisnis_id == 56) || (ai_bisnis_id == 64))
					ai_ltanggung  =  8; //ASAL LSBS=56 050401BC
				if (ai_bisnis_id  == 64)
					ai_bisnis_id  =  56;
				if (ai_bisnis_id  == 21)
					ai_ltanggung  =  99;
				if (ai_bisnis_id  != 65)
					ai_age = 1; 		
				if (ai_cbayar == 0)
					ai_lbayar = 1;
				
				//
				
				ldec_value=(Double) this.uwDao.selectLstTableAwal(ai_type,ai_bisnis_id,as_kurs,ai_cbayar,ai_lbayar,ai_ltanggung,ai_year,ai_age);
			//}//end switch	
				
			}//end if
			else if(ai_bisnis_id < 600){
				ldec_value=null;
			}else
				ldec_value = new Double(1000);
		if(ldec_value==null){ 
			ldec_value = new Double(1000);
					
			return ldec_value;
		}
		return ldec_value;
	}

}
