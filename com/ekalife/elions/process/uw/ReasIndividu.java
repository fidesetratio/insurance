/**
 * @author  : Ferry Harlim
 * @created : Jul 26, 2007 9:20:22 AM
 */
package com.ekalife.elions.process.uw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.validation.BindException;

import com.ekalife.elions.model.D_DS_Sar;
import com.ekalife.elions.model.Medical;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.ReasTemp;
import com.ekalife.elions.model.ReasTempNew;
import com.ekalife.elions.model.TabelMedis;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReasIndividu extends ParentDao{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	//Yusuf (5 Jul 2011) Tambahan Keterangan
	//MSTE_REAS
	//0 = Non-Reas; 1= Treaty; 2 = Fac; 3 = Proses EB

	//MSTE_BACKUP
	//0 = belum ada backup, 1 = sudah ada backup, 2 = treaty, 3 = non-reas; 4 = Proses EB	
	
	DecimalFormat f3=new DecimalFormat("000");

	/**Fungsi:	Melakukan proses Reas produk individu dimana posisi polis berada di Underwriting, dimana pada proses ini akan dilakukan :
	 * 			Insert pada tabel : EKA.M_SAR_TEMP, EKA.M_REAS_TEMP,EKA.MST_POSITION_SPAJ, EKA.MST_SIMULTANEOUS
	 * 			Update pada tabel : EKA.MST_INSURED
	 * @param	Reas dataReas, BindException err
//	 * @return 	Integer (null= Error, 0=Non Reas, 1=Treaty, 2=Facultative)
	 * @throws 	Exception
	 * @author 	Ferry Harlim
	 */
	public Map prosesReasUnderwriting(Reas dataReas,BindException err)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();	
		Map map=new HashMap();  
		Integer liReas=null;
		Integer liReasRider=null;
		Integer liBackup=null;
		boolean abAdd=true;
		Map mapReas=getReasIndividu(dataReas,abAdd,err);
		if(mapReas==null)
			return null;
		
		liReas=(Integer)mapReas.get("liReas");
		liReasRider=(Integer)mapReas.get("liReasRider");
		if(liReasRider==1 && liReas==0)
			liReas=1;
		
		// UW LImit Untuk fakultatif
		//power save tidak ada fakultatif kecuali umur tt >=69
		Map mData=uwDao.selectDataUsulan(dataReas.getSpaj());
		Integer lsbsId=(Integer)mData.get("LSBS_ID");
		String prodSave=props.getProperty("product.plan_power_save_reas");
		String bisnisId=f3.format(lsbsId);
		Integer pos=prodSave.indexOf(bisnisId);
		if(liReas==2){//jika facultative
			if(pos>0 && dataReas.getUmurTt()<69){ //bila powersave dan umur < 69
				liReas=1;
				uwDao.updateMReasTempMsteReas(dataReas.getSpaj(),liReas, null);
			}
		}	
		if(liReas==null){
			err.reject("","Please ContacT ITwebandmobile@sinarmasmsiglife.co.id, Gagal Proses Reas");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return null;
		}else if(liReas==0){	//Non Reas
			liBackup=3;
			uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(NON REAS)", dataReas.getSpaj(), 0);
		}else if(liReas==1){	//Treaty
			liBackup=2;
			uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(TREATY)", dataReas.getSpaj(), 0);
		}else if(liReas==2){	//facultative
			liBackup=0;
			uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(FACULTATIVE)", dataReas.getSpaj(), 0);
		}
		//
		Integer countSim=uwDao.selectCountMstSimultaneous(dataReas.getSpaj());
		if(countSim==0){
			Map mPemegang=uwDao.selectPemegang(dataReas.getSpaj());
			Integer lsreId=(Integer)mPemegang.get("LSRE_ID");
			
			String idSimultanPp,idSimultanTt = null;
			//cek ada id simultan blom jika ada ambil
			if(lsreId==1){
				idSimultanPp=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMspoPolicyHolder());
				if(idSimultanPp==null){
					idSimultanPp=uwDao.createSimultanId();
				}
				idSimultanTt=idSimultanPp;
			}else{
				idSimultanPp=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMspoPolicyHolder());
				idSimultanTt=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMsteInsured());
				if(idSimultanPp==null)
					idSimultanPp=uwDao.createSimultanId();
				if(idSimultanTt==null)
					idSimultanTt=uwDao.createSimultanId();
			}
			uwDao.wfInsSimultanNew(dataReas.getSpaj(), dataReas.getMsteInsured(), dataReas.getMspoPolicyHolder(),
					dataReas.getInsuredNo(),idSimultanPp,idSimultanTt);
		}
		
		//bila bulannya begdate < bulannya sysdate, liReas=2 di buat fakultatif sesuai permintaan atik (june 9 2008)
		Integer hasil= uwDao.selectIntMonth(dataReas.getSpaj());
//		if (hasil==0){
//			liReas=2;
//			liBackup=0;
//			uwDao.updateMReasTempMsteReas(dataReas.getSpaj(), liReas, liBackup);
//		}else 
//			liReas=liReas;
		
		uwDao.updateMstInsuredReasnBackup(dataReas.getSpaj(),dataReas.getInsuredNo(),liReas,liBackup,null,null);
		uwDao.wf_ins_cash_tpd(dataReas.getSpaj(),dataReas.getInsuredNo());
		//proses simultan medis
		Integer medis=null;
		//medis=prosesSimultanMedis(dataReas.getSpaj(),dataReas.getInsuredNo());
		
		//email automatis ke akturia tentang hasil reas
		//Yusuf - Per Jul 2011, Req Maya : hasil reas sudah tidak perlu diemail lagi
		//emailReas(dataReas);
		
		map.put("liReas",liReas);
		map.put("medis", medis);
		return map;
	}
	
	public Map prosesReasUnderwritingPas(Reas dataReas,BindException err)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();	
		Map map=new HashMap();  
		Integer liReas=null;
		Integer liReasRider=null;
		Integer liBackup=null;
		boolean abAdd=true;
		Map mapReas=getReasIndividu(dataReas,abAdd,err);
		if(mapReas==null)
			return null;
		
		liReas=(Integer)mapReas.get("liReas");
		liReasRider=(Integer)mapReas.get("liReasRider");
		if(liReasRider==1 && liReas==0)
			liReas=1;
		
		// UW LImit Untuk fakultatif
		//power save tidak ada fakultatif kecuali umur tt >=69
		Map mData=uwDao.selectDataUsulan(dataReas.getSpaj());
		Integer lsbsId=(Integer)mData.get("LSBS_ID");
		String prodSave=props.getProperty("product.plan_power_save_reas");
		String bisnisId=f3.format(lsbsId);
		Integer pos=prodSave.indexOf(bisnisId);
		if(liReas==2){//jika facultative
			if(pos>0 && dataReas.getUmurTt()<69){ //bila powersave dan umur < 69
				liReas=1;
				uwDao.updateMReasTempMsteReas(dataReas.getSpaj(),liReas, null);
			}
		}	
		if(liReas==null){
			err.reject("","Please ContacT ITwebandmobile@sinarmasmsiglife.co.id Gagal Proses Reas");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return null;
		}else if(liReas==0){	//Non Reas
			liBackup=3;
			Boolean ok = false;
			do{
				try{
					uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(NON REAS)", dataReas.getSpaj(), 0);
					ok=true;
				}catch(Exception e){};
			}while (!ok);
		}else if(liReas==1){	//Treaty
			liBackup=2;
			Boolean ok = false;
			do{
				try{
					uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(TREATY)", dataReas.getSpaj(), 0);
					ok=true;
				}catch(Exception e){};
			}while (!ok);
		}else if(liReas==2){	//facultative
			liBackup=0;
			Boolean ok = false;
			do{
				try{
					uwDao.insertMstPositionSpaj(dataReas.getCurrentUser().getLus_id(), "PROSES REAS(FACULTATIVE)", dataReas.getSpaj(), 0);
					ok=true;
				}catch(Exception e){};
			}while (!ok);
		}
		//
		Integer countSim=uwDao.selectCountMstSimultaneous(dataReas.getSpaj());
		if(countSim==0){
			Map mPemegang=uwDao.selectPemegang(dataReas.getSpaj());
			Integer lsreId=(Integer)mPemegang.get("LSRE_ID");
			
			String idSimultanPp,idSimultanTt = null;
			//cek ada id simultan blom jika ada ambil
			if(lsreId==1){
				idSimultanPp=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMspoPolicyHolder());
				if(idSimultanPp==null){
					idSimultanPp=uwDao.createSimultanId();
				}
				idSimultanTt=idSimultanPp;
			}else{
				idSimultanPp=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMspoPolicyHolder());
				idSimultanTt=uwDao.selectMstSimultaneousIdSimultan(dataReas.getMsteInsured());
				if(idSimultanPp==null)
					idSimultanPp=uwDao.createSimultanId();
				if(idSimultanTt==null)
					idSimultanTt=uwDao.createSimultanId();
			}
			uwDao.wfInsSimultanNew(dataReas.getSpaj(), dataReas.getMsteInsured(), dataReas.getMspoPolicyHolder(),
					dataReas.getInsuredNo(),idSimultanPp,idSimultanTt);
		}
		
		//bila bulannya begdate < bulannya sysdate, liReas=2 di buat fakultatif sesuai permintaan atik (june 9 2008)
		Integer hasil= uwDao.selectIntMonth(dataReas.getSpaj());
//		if (hasil==0){
//			liReas=2;
//			liBackup=0;
//			uwDao.updateMReasTempMsteReas(dataReas.getSpaj(), liReas, liBackup);
//		}else 
//			liReas=liReas;
		
		uwDao.updateMstInsuredReasnBackup(dataReas.getSpaj(),dataReas.getInsuredNo(),liReas,liBackup,null,null);
		uwDao.wf_ins_cash_tpd(dataReas.getSpaj(),dataReas.getInsuredNo());
		//proses simultan medis
		Integer medis=null;
		//medis=prosesSimultanMedis(dataReas.getSpaj(),dataReas.getInsuredNo());
		//email automatis ke akturia tentang hasil reas
		//emailReas(dataReas);
		map.put("liReas",liReas);
		map.put("medis", medis);
		return map;
	}

	/*proses simultan medis untuk menentukan tipe medis, dan jenis medis apa saja yang harus di
	 * ambil.. sesuai dengan tabel medis uw.
	 * 
	 */
	private Integer prosesSimultanMedis(String spaj,Integer insured) {
		//proses simultan medis.. june 2007
		//cek 2 tahun saja yang simultannya. lalu bandingkan ke tabel medis
		Policy policy=uwDao.selectDw1Underwriting(spaj,2,1);
		List lsSar=uwDao.selectMSarTemp(spaj);
		Date begDateCom=null,begDateNext;
		Double totalSar=new Double(0);
		for(int i=0;i<lsSar.size();i++){
			Map mSar=(HashMap)lsSar.get(i);
			String regSpaj=(String)mSar.get("REG_SPAJ");
			String regSpajRef=(String)mSar.get("REG_SPAJ_REF");
			Integer lsbsId=(Integer)mSar.get("BISNIS_ID");
			Integer lsdbsNumber=(Integer)mSar.get("BISNIS_NO");
			Double sar=(Double)mSar.get("SAR");
			Map product=uwDao.selectMstProductInsuredDateNKurs(regSpajRef, 1, lsbsId,lsdbsNumber);
			if(i==0){
				begDateCom=(Date)product.get("MSPR_BEG_DATE");
				begDateNext=(Date)product.get("MSPR_BEG_DATE");
			}else{
				begDateNext=(Date)product.get("MSPR_BEG_DATE");
			}
			if(FormatDate.dateDifferenceInYears(begDateCom, begDateNext)<=2){
				totalSar+=sar;
			}
		}	
		//cari range age 
		Integer age=uwDao.selectMstInsuredMsteAge(spaj, insured);
		Integer rangeAge=uwDao.selectLstMedicalRangeAge(age);
		//cari range sar
		Integer rangeSar=uwDao.selectLstMedicalRangeSar(uwDao.selectMstProductInsuredKurs(spaj, insured), totalSar);
		Integer medis=0;
		List lsMedis=uwDao.selectDaftarTabelMedis(rangeAge, rangeSar, 1, policy.getLku_id());
		for(int j=0;j<lsMedis.size();j++){
			TabelMedis tabelMedis=(TabelMedis)lsMedis.get(j);
			if(tabelMedis.getJns_medis()!=1){
				Medical medical=new Medical();
				medical.setReg_spaj(spaj);
				medical.setLsmc_id(tabelMedis.getLsmc_id());
				medical.setMste_insured_no(1);
				medical.setMpa_number(j+1);
				medical.setMsdm_status(1);
				uwDao.insertMstDetMedical(medical);
				medis=1;
			}
		}
		uwDao.updateMstInsuredMedical(spaj, medis);
		
		return medis;
	}
	
	/**Fungsi:	Untuk mendapatkan hasil proses Reas produk individu.
	 * @param 	Reas dataReas
	 * @return	Integer (null= Error, 0=Non Reas, 1=Treaty, 2=Facultative)
	 * @author  Ferry Harlim
	 */
	public Map getReasIndividu(Reas dataReas,boolean abAdd,BindException err)throws Exception{
		int aiCnt=0;
		String spaj=dataReas.getSpaj();
		Integer liReas=0;
		boolean reasRider=false;
		Integer liReasRider=0;
		Integer liBisnisId[]=new Integer[5];
		Integer liBisnisNo[]=new Integer[5];
		Integer liReasClient[]=new Integer[5];
		Double ldecRateSar[]=new Double[5];
		Double ldecLimit[]=new Double[5];
		Double[] ldecLimitTemp=new Double[5];
		Double sarTemp1[]=new Double[10]; 
		Double ldecTotalReas[]=new Double[5];
		Integer liThKe,liMedis=null,liInsuredNo,liStsPolis=null,liCbayar,liLbayar,liLTanggung,liInsMonth,liFlat;
		Integer liTypeReas=null;
		String lsRegSpaj,lsKursId=null;
		Double ldecUp=null,ldecBunga,ldecReasComp;
		Double ldecQsReas[]=new Double[5];
		int liCnt[]=new int[10], liAge[]=new int[10];
		Double[][][] ldecReasLf=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasPa=new Double[3][6][4]; // dec{2} pada pb
		Double[][][] ldecReasPk=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasSs=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasCp=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasTPD=new Double[3][6][3]; //Tambahan Dian
		Double ldecExtMort[]=new Double[10]; //dec{3}
		Double ldecKurs,ldecTsi=null,ldecSar,ldecReasSar,ldecOwnSar=null;
		//Double ldecReasHcp[]=new Double [5]; //simultan,tsi,sar,retensi,reas

		Date begDate;
		Integer liTypeBisnis;
		Integer paramTypeBisnis=1;
		int posDoc[] = new int[3];
		
		Map mPosisi=uwDao.selectF_check_posisi(spaj);
		Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
		Integer lspdId=lspdIdTemp;
		
		Integer lstbId=1;//individu
		Integer umurPp,umurTt;
		String lcaId;
		String lsOldSpaj,lsClient,lkuId,noPolis;
		Integer liSimultanNo;
		Integer liPHolder = null,liStsAksep;
		int llRow[] = new int[10];
		List ldsProd;
		//int flagRider=0;
		posDoc[1]=2;
		posDoc[2]=10;
		String msteInsured,mspoPolicyHolder,idSimultan;
		Integer insuredNo;
		
		//data usulan asuransi
		Map mDataUsulan=uwDao.selectDataUsulan(spaj);
		begDate=(Date)mDataUsulan.get("MSTE_BEG_DATE");
		Integer lsbsId=(Integer)mDataUsulan.get("LSBS_ID");
		Integer lsdbsNumber=(Integer)mDataUsulan.get("LSDBS_NUMBER");
		//tertanggung
		Map mTertanggung=uwDao.selectTertanggung(spaj);
		insuredNo=(Integer)mTertanggung.get("MSTE_INSURED_NO");
		msteInsured=(String)mTertanggung.get("MCL_ID");
		umurTt=(Integer)mTertanggung.get("MSTE_AGE");
		//pemegang
		Policy policy=uwDao.selectDw1Underwriting(spaj,lspdId,lstbId);
		mspoPolicyHolder=policy.getMspo_policy_holder();
		//insPeriod=policy.getMspo_ins_period();
		//payPeriod=policy.getMspo_pay_period();
		lkuId=policy.getLku_id();
		umurPp=policy.getMspo_age();
		//lcaId=policy.getLca_id();
		
		//Deklarasi variabel Array
		for(liCnt[1]=1;liCnt[1]<=2;liCnt[1]++){	// 1=inssured ; 2=policy-holder
			for(liCnt[2]=1;liCnt[2]<=5;liCnt[2]++) {// 1=total-sar-simultan ; 2=tsi ; 3=resiko-awal ; 4=retensi ; 5=sum-reas
				ldecReasLf [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //term
				ldecReasLf [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //life
				ldecReasPa [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //ssp
				ldecReasPa [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //pa include (risk-a)
				ldecReasPa [liCnt[1]] [liCnt[2]] [3]=new Double(0);  //pa rider
				ldecReasPk [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //pk include
				ldecReasPk [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //pk rider
				ldecReasSs [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //ssh,ss,ss+
				ldecReasCp [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //cash plan
			}
		}
		
		/*
		Yusuf : contoh cara bacanya
		ldecReasLf [1] [1] [1] = INSURED, TOTAL SAR SIMULTAN, TERM
		ldecReasLf [1] [2] [1] = INSURED, TSI, TERM
		 */
		
		liCnt[4]=0;
		ldecExtMort[1]=new Double(0);
		ldecKurs=uwDao.selectGetKursReins("02",begDate);
		//
//		Integer jum=uwDao.selectCountMReasTemp(spaj);
		// delete table m_reas_temp dan sar temp
		//if(jum>0){
			uwDao.deleteMReasTemp(spaj);
			uwDao.deleteMSarTemp(spaj);
			uwDao.deleteMReasTempNew(spaj);
		//}
		//
		int llReas;
		String bisnisId=f3.format(lsbsId);
		String bisnisNumber=f3.format(lsdbsNumber);
		if(props.getProperty("product.ekasiswa").indexOf(bisnisId)>0)
			llReas=2;
		else
			llReas=1;
		
		double retensi_pa = (double) 0;		
		
		for(liCnt[1]=1;liCnt[1]<=llReas;liCnt[1]++){// 1=inssured ; 2=policy-holder 	
			for(liCnt[2]=1;liCnt[2]<=2;liCnt[2]++) {	// 1=simultan ; 2=now				
 				if(liCnt[2]==1){
					lsOldSpaj=null;
					liSimultanNo=null;
					if (liCnt[1] == 1)
						lsClient = msteInsured;
					else	
						lsClient = mspoPolicyHolder;
					//
					lsOldSpaj=uwDao.selectMstCancelRegSpaj(spaj);
					//TODO
					if(lsOldSpaj!=null)
						liSimultanNo=uwDao.selectMinMstSimultaneous(lsOldSpaj,lsClient);
					if(liSimultanNo==null)
						liSimultanNo=0;
					//
					//simultan tertanggung kecuali produk ekasiswa emas
					//liCnt[1]-1 untuk flag pemegang coz 0=tertanggung 1=pemegang
					/**
					 * TODO
					 * kudu benerin reas kalo insert cuma 1 yaitu tertanggung jadi pemegangnya ga di insert
					 */
					
					if (lsbsId.equals(172)){
						idSimultan=uwDao.selectIdSimultan(spaj, 1);
					}else {
						if (lsOldSpaj!=null){
							idSimultan=uwDao.selectIdSimultan(lsOldSpaj, liCnt[1]-1);
						}else
							idSimultan=uwDao.selectIdSimultan(spaj, liCnt[1]-1);
					}		
					ldsProd=uwDao.selectDdsSarNew(idSimultan,spaj,liSimultanNo);
					llRow[1]=ldsProd.size();
				}else{
					ldsProd=uwDao.selectDdsSarnNew(spaj,insuredNo);					
//					idSimultan=uwDao.selectIdSimultan(spaj, liCnt[1]-1);
//					ldsProd=uwDao.selectDdsSarNew(idSimultan, spaj, 0);
					llRow[1]=ldsProd.size();
				}
				//
 				
				for (liCnt[3] = 1;liCnt[3]<=llRow[1];liCnt[3]++){ // per-detil product
					D_DS_Sar dataProd=(D_DS_Sar)ldsProd.get(liCnt[3]-1);
					liBisnisId[1]=dataProd.getLsbs_id();
					liBisnisNo[1]=dataProd.getLsdbs_number();
					liReasClient[1]=dataProd.getLsdbs_reas_client();
					noPolis=dataProd.getMspo_policy_no();
					
					if(dataProd.getRetensi_ssp() != null) retensi_pa += dataProd.getRetensi_ssp();
					
					if(liBisnisId[1]<600){//pokok;
						liThKe= new Integer(uwDao.selectTahunKe(defaultDateFormat.format(dataProd.getMste_beg_date()),defaultDateFormat.format(begDate)).intValue());
						liMedis=dataProd.getMste_medical();
						lsRegSpaj=dataProd.getReg_spaj();
						liInsuredNo=dataProd.getMste_insured_no();
						liStsPolis=dataProd.getLssp_id();
						liCbayar=dataProd.getLscb_id();
						liLbayar=dataProd.getMspo_pay_period();
						liLTanggung=dataProd.getMspo_ins_period();
						liInsMonth=dataProd.getMspo_ins_bulan();
						liFlat=dataProd.getMspo_flat();
						ldecBunga=dataProd.getMspr_bunga_kpr();
						liTypeBisnis=dataProd.getLstb_id();
						liTypeReas=dataProd.getLstr_id();
						ldecQsReas[1]=dataProd.getLstr_quota_reas();
						lsKursId=dataProd.getLku_id();
						ldecUp=dataProd.getMspr_tsi();
						liBisnisId[2]=liBisnisId[1];// assign main_bisnis_id
						liBisnisNo[2]=liBisnisNo[1];// assign main_bisnis_no
						//
						if(liCnt[2] == 1){
							liPHolder= dataProd.getMssm_pemegang();
							liStsAksep = dataProd.getLssa_id();
							if (liStsPolis == 10 && liStsAksep != 5){
								if(liStsAksep!= 8 && liStsAksep !=3 ){
									err.reject("","Pending Reins Proccess until Simultan Policy Accepted, Polis Simultan (SPAJ= "+dataProd.getReg_spaj()+") Belum Ada Backup Facultative-Nya");
									return null;
								}
							}
						}else{
							ldecQsReas[2]   = ldecQsReas[1];
							liReasClient[2] = liReasClient[1];
							liAge[1] = umurTt;
							liAge[2] = umurTt;
							if (liReasClient[2] == 2 || liReasClient[2] == 3)
								liAge[2] = umurPp;
							if (liCnt[1] == 1 )
								liPHolder = 0 ;
							else 
								liPHolder = 1;
						}
						//
						ldecTsi=uwDao.selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],liBisnisId[1],
								liBisnisNo[1],lsKursId,ldecUp,err);
						if(ldecTsi==null)
							return null;
						if(ldecTsi == 0)
							continue;
						//
						if(liReasClient[1]== 1 && liPHolder ==0  || liReasClient[1]== 2 && 
								liPHolder ==1 || liReasClient[1] > 2){
							if(liTypeBisnis==1){//individu
								ldecRateSar[1] = uwDao.selectGetSar(1, liBisnisId[1], liBisnisNo[1], lsKursId, liCbayar, liLbayar, liLTanggung, liThKe, umurTt);
								ldecRateSar[2] = uwDao.selectGetSar(2, liBisnisId[1], liBisnisNo[1], lsKursId, liCbayar, liLbayar, liLTanggung, liThKe, umurTt);
							}else if(liTypeBisnis==2){//MRI
								if(liInsMonth==null) 
									liInsMonth =0;
								//
								ldecRateSar[1] = uwDao.selectGetMriSar(1, liBisnisId[1], liFlat, ldecBunga, 
															new Integer( (liLTanggung * 12) + liInsMonth), liThKe,err);
								ldecRateSar[2] = uwDao.selectGetMriSar(2, liBisnisId[1], liFlat, ldecBunga, 
															new Integer( (liLTanggung * 12) + liInsMonth), liThKe,err);
							}
							//
							if (ldecRateSar[1]==null || ldecRateSar[2]==null )
								return null;
							//
							ldecSar      = new Double(ldecTsi * ldecRateSar[1] / 1000);
							if (liCnt[2] == 1)
								if (liBisnisId[1]==56||liBisnisId[1]==55||liBisnisId[1]==64||liBisnisId[1]==64){
									ldecSar      = new Double(ldecTsi * ldecRateSar[1] / 1000);
								}else 
//								ldecSar = new Double(ldecSar * ldecRateSar[2] / 1000);
									ldecSar= dataProd.getMspr_tsi();	
							if (liBisnisId[1] == 66 || liBisnisId[1] == 79 || 
									liBisnisId[1] == 80)
								ldecSar = new Double(ldecSar * 1.5); //procare,SEHAT,PRIVACY
							//
							ldecReasSar = new Double(ldecSar * ldecQsReas[1] / 100);
							ldecOwnSar  = new Double(ldecSar - ldecReasSar);
							//
							if( dataProd.getLsgr_id()==3){// simas sehat harian/HEALTH
								if (liCnt[2]== 1)
									ldecReasSs[liCnt[1]][1][1] =new Double( ldecReasSs[liCnt[1]][1][1] +  
																	uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasSs[liCnt[1]][2][1] =new Double( ldecReasSs[liCnt[1]][2][1] +ldecTsi );
									if(bisnisId=="132"|| bisnisId.equals("132")||bisnisId=="131"|| bisnisId.equals("131")||bisnisId=="183" || bisnisId.equals("183")||bisnisId=="189"|| bisnisId.equals("189")||bisnisId=="178"|| bisnisId.equals("178") || bisnisId=="193"|| bisnisId.equals("193") || bisnisId.equals("195") || bisnisId=="204"|| bisnisId.equals("204") || bisnisId=="208"|| bisnisId.equals("208")){
										ldecReasSs[liCnt[1]][3][1] =new Double( ldecReasSs[liCnt[1]][3][1]+ ldecSar);
									}else{
									ldecReasSs[liCnt[1]][3][1] =new Double( ldecReasSs[liCnt[1]][3][1]+ ldecSar / 100 );//bc
									}
								}	
							}else if(dataProd.getLsgr_id()==2){	 // simas super protection /PA
								if (liCnt[2]== 1)
									ldecReasPa[liCnt[1]][1][1] =new Double( ldecReasPa[liCnt[1]][1][1] + 
																	uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasPa[liCnt[1]][2][1] =new Double( ldecReasPa[liCnt[1]][2][1] +ldecTsi );
									ldecReasPa[liCnt[1]][3][1] =new Double( ldecReasPa[liCnt[1]][3][1]+ldecSar );
								}	
							}else{//others LIFE
								//simultan
								if (liCnt[2]== 1)
									//simultan
									ldecReasLf[liCnt[1]][1][2] =new Double( ldecReasLf[liCnt[1]][1][2] + 
																	uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								//now
								if (liCnt[2] == 2){
									//tsi
									ldecReasLf[liCnt[1]][2][2] =new Double( ldecReasLf[liCnt[1]][2][2] +ldecTsi );
									//sar
									ldecReasLf[liCnt[1]][3][2] =new Double( ldecReasLf[liCnt[1]][3][2]+ ldecSar );
								
								}	
							}
							//
							if (abAdd)
								if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){ // khusus untu EKA SISWA EMAS.. yang tertanggungnya aja yang direasin
									break;	
								}else
								aiCnt=uwDao.insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
										liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
						
					}
					//rider	
					}else if(dataProd.getLsgr_id()==4){
						if (liReasClient[1] == 1 && liPHolder == 0  || liReasClient[1] == 2 && 
								liPHolder == 1 || liReasClient[1] > 2){
							ldecUp=dataProd.getMspr_tsi();
							ldecTsi =uwDao.selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],
									liBisnisId[1],liBisnisNo[1],lsKursId,ldecUp,err);
					
							if(ldecTsi==null)
								return null;
							ldecSar=ldecTsi;
							//untuk produk 601-808 tidak ada sar 223012008 (tabel atik)
//							ldecSar=0.0;
							if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu eka siswa.. yang tertanggungnya aja yang direasin
								break;	
							}else
								aiCnt=uwDao.insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
										liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
							//
							if(liBisnisId[1]==800){// PA Rider
								if (liCnt[2]== 1)
									ldecReasPa[liCnt[1]][1][3] =new Double( ldecReasPa[liCnt[1]][1][3] + 
																			uwDao.selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasPa[liCnt[1]][2][3] =new Double( ldecReasPa[liCnt[1]][2][3] + ldecTsi );
									ldecReasPa[liCnt[1]][3][3] =new Double( ldecReasPa[liCnt[1]][3][3]+ldecSar );
								}
							}else if(liBisnisId[1]==801){// PK Rider
								if (liCnt[2]== 1)
									ldecReasPk[liCnt[1]][1][2] =new Double( ldecReasPk[liCnt[1]][1][2] + 
																			uwDao.selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasPk[liCnt[1]][2][2] =new Double( ldecReasPk[liCnt[1]][2][2] +ldecTsi );
									ldecReasPk[liCnt[1]][3][2] =new Double( ldecReasPk[liCnt[1]][3][2]+ldecSar );
								}	
							}else if(liBisnisId[1]==803){// Term Rider 
								//simultan
								if (liCnt[2]== 1)
									//simultan
									ldecReasLf[liCnt[1]][1][1] =new Double( ldecReasLf[liCnt[1]][1][1] + 
																			uwDao.selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								//now
								if (liCnt[2] == 2){
									//tsi
									ldecReasLf[liCnt[1]][2][1] =new Double( ldecReasLf[liCnt[1]][2][1]+ldecTsi );
									//sar
									ldecReasLf[liCnt[1]][3][1] =new Double( ldecReasLf[liCnt[1]][3][1]+ldecSar );
								}	
							}else if(liBisnisId[1]==804){// WPD
								//simultan
								if (liCnt[2]== 1)
									//simultan
									ldecReasTPD[liCnt[1]][1][1] = new Double( ldecReasLf[liCnt[1]][1][1] + 
											uwDao.selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								//now
								if (liCnt[2] == 2){
									//tsi
									ldecReasTPD[liCnt[1]][2][1] =new Double( ldecReasLf[liCnt[1]][2][1]+ldecTsi );
									//sar
									ldecReasTPD[liCnt[1]][3][1] =new Double( ldecReasLf[liCnt[1]][3][1]+ldecSar );
								}	
							}else if(liBisnisId[1]==820){// WPD
								Double sar=dataProd.getMspr_tsi();
								ldecUp=dataProd.getMspr_tsi();
								ldecTsi =uwDao.selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],
										liBisnisId[1],liBisnisNo[1],lsKursId,ldecUp,err);
//								aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, sar, liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
								
							}else if(liBisnisId[1]==802){
//								Double sar=dataProd.getMspr_tsi();
//								aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, sar, liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
								
								Double reas=0.;
								Double retensiPa=new Double(0);
								List lsRider=uwDao.selectMstProductInsuredRiderTambahanSar(spaj, 1, 1);
								for(int i=0;i<lsRider.size();i++){
									Double retensiAwal=0.0;	
//									retensiAwal=0.0;
									liReasRider=0; 	
									Product prod=(Product)lsRider.get(i);
									retensiPa=uwDao.selectGetRetensi(prod.getLsbs_id(), prod.getLsdbs_number(), 1, 1, liMedis, prod.getLku_id(), begDate, liAge[1], spaj,err);
									//cek di sar_temp dimana groupnya pa lalu bandingkan dengan retensi
//									reas=prod.getSar()-retensiLife; //yang lama dari ferry
									retensiAwal=retensiPa;
									Double sarTemp=prod.getSar();
//									retensiPa=retensiPa-prod.getSar();
//									reas=retensiPa;
									if(sarTemp<=retensiAwal) //dian :prod rider sar<retensi reas=nol
//										retensiPa=0.0;
										reas=0.0;
									else{
										reas= sarTemp-retensiPa;//prod rider sar>retensi
//										reas=retensiPa; //rider itu di reas
									}
									if(reas<=0)//rider tersebut di reas kan.
										reas=0.0;
									else{
										reasRider=true;
										liReasRider=1; 	
									}
									/*
									//kalo perlu aja di buka
									Integer contReasTemp= uwDao.selectCountReasTempNew(spaj);
									if (contReasTemp>0){
										
									}else
									*/
									insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
											liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);	
								}
								
							}else{//tanya atik  untuk WPD dan HCR bagaimana? (802,804,805)
								//produk yang sudah tidak terjual.
								//dian: WPD udah di jual kembali 
								err.reject("","Rider ini belum di modifikasi dalam sistem, silahkan hubungi ITwebandmobile@sinarmasmsiglife.co.id ");
								return null;
							}
						}
					}else if (dataProd.getLsgr_id()==3&&dataProd.getLsbs_id()==821){ 
						Double sar = dataProd.getMspr_tsi();
						aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, sar, liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
					//rider link & rider hidup bahagia (814,815) - Yusuf (26/02/2008)						
					}else if(dataProd.getLsgr_id()==5){
						// --->cek product mainnya termasuk group yang mana
						if(dataProd.getLsbs_id()==813||dataProd.getLsbs_id()==822){
//							Double sar=dataProd.getMspr_tsi()*0.5;
							Double sar=dataProd.getMspr_tsi();
							if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu EKA SISWA EMAS.. yang tertanggungnya aja yang direasin
								break;	
							}else
							aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, sar, liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
						}else if(dataProd.getLsbs_id()>=814 && dataProd.getLsbs_id()<=817){//sistem reas surplus (life) -- UP dari tabel
							Integer age;
							Double rate;
							Double sar;
							age=uwDao.selectMstInsuredMsteAge(dataProd.getReg_spaj_ref(), 1);
							
//							if(dataProd.getLsbs_id()==817){
//								rate=4.312;
//							}else
							
							//Yusuf (8 Aug 2011)
							//Selain produk2 tertentu, maka rate rider diambil dengan lsdbs_number = 1 (di hardcode karena sama semua)
							int lsdbs_number = 1;
							if(dataProd.getLsbs_id().intValue() == 815 && dataProd.getLsdbs_number().intValue() == 6 /*|| dataProd.getLsbs_id().intValue() == 815*/ /*&& dataProd.getLsdbs_number().intValue() == 5*/){
								lsdbs_number = 6;
							}
							
							if(lsbsId==120 && "019,020,021,022,023,024".indexOf(bisnisNumber)>-1){
								String kode_rider=f3.format(dataProd.getLsbs_id().intValue());
								if("815,817".indexOf(kode_rider)>-1){
									lsdbs_number=dataProd.getLsdbs_number().intValue();
									age=1;
								}
							}
							
							rate=uwDao.selectLstRateRider(dataProd.getLsbs_id(), lsdbs_number, age, dataProd.getLku_id());
							
							//TODO: KHUSUS TESTING 
							//if(rate == null) rate = 1000.;
							
							Product prodUtama=uwDao.selectMstProductInsuredUtamaFromSpaj(dataProd.getReg_spaj());
							
							//perhitungan utama (Yusuf, 20 Jun 2011) - nolnya kebanyakan 3
							sar = rate * prodUtama.getMspr_premium() / 1000;
							
							if (dataProd.getLsbs_id()==816||dataProd.getLsbs_id()==817){
								if (dataProd.getLscb_id()==6){//dian--cara bayar bulanan
									sar=sar/0.1;
								}
								if (dataProd.getLscb_id()==1){//dian--triwulanan
									sar=sar/0.27;
								}
								if (dataProd.getLscb_id()==2){//dian--semesteran
									sar=sar/0.525;
								}
							}else if (dataProd.getLsbs_id()==814 || dataProd.getLsbs_id()==815){
								if (dataProd.getLscb_id()==6){//dian--cara bayar bulanan
									sar=sar*12;
								}
								if (dataProd.getLscb_id()==1){//dian--triwulanan
									sar=sar*4;
								}
								if (dataProd.getLscb_id()==2){//dian--semesteran
									sar=sar*2;
								}
							}
							if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu eka siswa.. yang tertanggungnya aja yang direasin
								break;	
							}else{
								aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, sar, liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
							}
						}else{
							if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu EKA SISWA EMAS.. yang tertanggungnya aja yang direasin
								break;
//							}else if(liBisnisId[1].intValue() == 810){ //Yusuf (5 Jul 2011) req hanifah, untuk PA tidak ada SAR nya
//								continue;
							}else
							aiCnt=uwDao.insertMSarTemp(spaj, noPolis, liBisnisId[1], liBisnisNo[1], lsKursId, dataProd.getMspr_tsi(), liStsPolis, liMedis, aiCnt, dataProd.getReg_spaj_ref());
						}
					}else if(dataProd.getLsgr_id()==6 ){//rider include
						if(liReasClient[1] == 1 && liPHolder == 0  || 
								liReasClient[1]== 2 && liPHolder == 1 || liReasClient[1] > 2 ){
							ldecTsi =uwDao.selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],
										liBisnisId[1],liBisnisNo[1],lsKursId,ldecUp,err);
							if(ldecTsi==null)
								return null;
							//
							//procare
							//if(liBisnisId[1] == 601 && liBisnisId[2] == 54)
							//	ldecSar = new Double(100 * ldecTsi);
							//else 
							//	ldecSar = ldecTsi;
							//tidak ada sar 23012008 (tabel dr atik) 
							//
							ldecSar=0.0;
							ldecReasSar = new Double(ldecSar * ldecQsReas[1]/100);	
							ldecOwnSar  = new Double(ldecSar - ldecReasSar);
							if(liBisnisId[1]==600){// PA Include Resiko A
								if (liCnt[2]== 1)
									ldecReasPa[liCnt[1]][1][2] =new Double( ldecReasPa[liCnt[1]][1][2] + 
																			uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasPa[liCnt[1]][2][2] =new Double( ldecReasPa[liCnt[1]][2][2] +ldecTsi );
									ldecReasPa[liCnt[1]][3][2] =new Double( ldecReasPa[liCnt[1]][3][2]+ ldecSar );
								}	
							}else if(liBisnisId[1]==601){// PK Include
								if (liCnt[2]== 1)
									ldecReasPk[liCnt[1]][1][1] =new Double( ldecReasPk[liCnt[1]][1][1] + 
																			uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasPk[liCnt[1]][2][1] =new Double( ldecReasPk[liCnt[1]][2][1] + ldecTsi );
									ldecReasPk[liCnt[1]][3][1] =new Double( ldecReasPk[liCnt[1]][3][1]+ ldecSar );
								}	
							}else if(liBisnisId[1]>=806 && liBisnisId[1]<=808){/// CASH PLAN, tpd
								if (liCnt[2]== 1)
									ldecReasCp[liCnt[1]][1][1] =new Double( ldecReasCp[liCnt[1]][1][1] + 
																			uwDao.selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2){
									ldecReasCp[liCnt[1]][2][1] =new Double( ldecReasCp[liCnt[1]][2][1] +ldecTsi );
									ldecReasCp[liCnt[1]][3][1] =new Double( ldecReasCp[liCnt[1]][3][1]+ ldecSar );
								}	
							}else{
								err.reject("","Please Contact ITwebandmobile@sinarmasmsiglife.co.id. Aplikasi belum dimodifikasi utk rider include (Bisnis Id="+liBisnisId[1]+" )");
								return null;
							}
							if (abAdd)
								if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu eka siswa.. yang tertanggungnya aja yang direasin
									break;	
								}else
								aiCnt=uwDao.insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
										liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
						}
					}else if(liCnt[2]==2 && liBisnisId[1]==900 ){//Extra Mortalita
						ldecExtMort[1]=dataProd.getMspr_extra();
						if(ldecExtMort[1]==null)
							ldecExtMort[1]=new Double(0);
						ldecExtMort[2]=uwDao.selectLstEmMaxLsemMaxLsemValue(paramTypeBisnis,liTypeReas,begDate);
						if(ldecExtMort[2]==null){
							err.reject("","EM Max not found Bisnis="+paramTypeBisnis+" reins="+liTypeReas+" begdate="+defaultDateFormat.format(begDate));
							return null;
						}
						if (ldecExtMort[1]> ldecExtMort[2]){
							ldecQsReas[2] = new Double(Math.max( 50, ldecQsReas[1] ) );
							//SET FACULTATIVE
							liReas = 2;
						}
					}
				}	
			}
			/* Hitung Retensi & Sum-Reas */
			// reas_life
			
			if(ldecReasLf[liCnt[1]][3][1] + ldecReasLf[liCnt[1]][3][2] > 0){
				lsOldSpaj=uwDao.selectMstCancelRegSpaj(spaj);
				if (lsbsId.equals(172)){
					idSimultan=uwDao.selectIdSimultan(spaj, 1);
				}else {
					if(lsOldSpaj!=null){
						idSimultan=uwDao.selectIdSimultan(lsOldSpaj, liCnt[1]-1);
					}else
						idSimultan=uwDao.selectIdSimultan(spaj, liCnt[1]-1);
				}
				List spajList=uwDao.selectSpajSimultan(idSimultan);
				llRow[1]=spajList.size();
				for(int i=0;i<llRow[1];i++){
					Map spajMap= (Map) spajList.get(i);
					String spajTemp=(String) spajMap.get("REG_SPAJ");
					if(lsOldSpaj!=null){//khusus untuk polis endors
						if (lkuId=="02"||lkuId.equals("02")){
							ldecLimit[1] =new Double(775000);
							ldecLimit[2] =new Double(75000);
						}else{
							ldecLimit[1] =new Double("7000000000");
							ldecLimit[2] =new Double("7000000000");
						}
						
					}else{
					ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err);//Retensi limit					
					ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); //UW limit
					}
				}
//				ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2],1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); //Retensi limit
//				ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2],1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); //UW limit
				
				if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
				//
				ldecReasLf[liCnt[1]][4][1] = new Double( Math.max( 0, ldecLimit[1] - ldecReasLf[liCnt[1]][1][1] - 
																      ldecReasLf[liCnt[1]][1][2] ) );
				ldecReasLf[liCnt[1]][5][1] = new Double(Math.max( ldecReasLf[liCnt[1]][3][1] * ldecQsReas[2] / 100, 
																   ldecReasLf[liCnt[1]][3][1] - ldecReasLf[liCnt[1]][4][1] )) ;
				ldecReasLf[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasLf[liCnt[1]][4][1] - ( ldecReasLf[liCnt[1]][3][1] -
																	 ldecReasLf[liCnt[1]][5][1] ) ));
				if (bisnisId.equals("169") || bisnisId.equals("212")){
					if (ldecReasLf[liCnt[1]][4][2]>ldecReasLf[liCnt[1]][3][2]){
						ldecReasLf[liCnt[1]][5][2]= new Double(0.0);
					}else
						ldecReasLf[liCnt[1]][5][2]= new Double(Math.max( 0,ldecReasLf[liCnt[1]][3][2]-ldecReasLf[liCnt[1]][4][2]));
				}else
				ldecReasLf[liCnt[1]][5][2] = new Double( Math.max( ldecReasLf[liCnt[1]][3][2] * ldecQsReas[2] / 100, 
																  ldecReasLf[liCnt[1]][3][2] - ldecReasLf[liCnt[1]][4][2] ));
				
				//
				if (ldecReasLf[liCnt[1]][3][1] == 0)
					ldecReasLf[liCnt[1]][4][1] = new Double(0);
				if (ldecReasLf[liCnt[1]][3][2] == 0) 
					ldecReasLf[liCnt[1]][4][2] = new Double(0);

				//Yusuf (20 Jul 2011) - Bila produk ulink, ada excess of loss sampai dengan 7 M.
				//Dengan arti, walaupun melebihi retensi 750 jt, kalau sar nya belum sampai 7M masih tetap treaty (tidak facultative dan tidak perlu dibackup)
				Double limit = ldecLimit[2];
				if(uwDao.selectIsUlink(spaj) > 0) {
					if(limit < new Double("7000000000")){
						limit = new Double("7000000000");
					}
				}
				
				if ( (ldecReasLf[liCnt[1]][1][1] + ldecReasLf[liCnt[1]][1][2] + ldecReasLf[liCnt[1]][3][1] +
					ldecReasLf[liCnt[1]][3][2]) > limit.doubleValue()){
					//SET FACULTATIVE
					liReas = 2;
				}
			}
			// reas_pa
			if (ldecReasPa[liCnt[1]][3][1] + ldecReasPa[liCnt[1]][3][2] + 
					ldecReasPa[liCnt[1]][3][3] > 0){
				lsOldSpaj=uwDao.selectMstCancelRegSpaj(spaj);
				if (lsbsId.equals(172)){
					idSimultan=uwDao.selectIdSimultan(spaj, 1);
				}else {
					if(lsOldSpaj!=null){
						idSimultan=uwDao.selectIdSimultan(lsOldSpaj, liCnt[1]-1);
					}else
						idSimultan=uwDao.selectIdSimultan(spaj, liCnt[1]-1);
				}
				List spajList=uwDao.selectSpajSimultan(idSimultan);
				llRow[1]=spajList.size();
				for(int i=0;i<llRow[1];i++){
					Map spajMap= (Map) spajList.get(i);
					String spajTemp=(String) spajMap.get("REG_SPAJ");
					Integer lstb_Id=((BigDecimal) spajMap.get("LSTB_ID")).intValue();
					//limit > 0 dan resiko awal ssp = 0
					if ( (ldecQsReas[1] > 0) && (ldecReasPa[liCnt[1]][3][1] == 0) ){
						if(lsOldSpaj!=null||lstb_Id==2){//khusus untuk polis endors
							if (lkuId=="02"){
								ldecLimit[1] =new Double(75000);
								ldecLimit[2] =new Double(75000);
							}else{
								ldecLimit[1] =new Double(750000000);
								ldecLimit[2] =new Double(750000000);
							}
							
						}else
						ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 3,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
						ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 3,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
					}else{
						if(lsOldSpaj!=null){//khusus untuk polis endors
							if (lkuId=="02"){
								ldecLimit[1] =new Double(75000);
								ldecLimit[2] =new Double(75000);
							}else{
								ldecLimit[1] =new Double(750000000);
								ldecLimit[2] =new Double(750000000);
							}
							
						}else
						ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 	
						ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
					
					}
				}
//				String prodSave=props.getProperty("product.plan_power_save");
//				prodSave.contains(bisnisId);
//				if (prodSave.contains(bisnisId)&& aiCnt>1){
//					for (int d=0;d<spajList.size()-1;d++){
//						Map spajMap= (Map) spajList.get(d);
//						String spajTemp=(String) spajMap.get("REG_SPAJ");
//						ldecLimitTemp[1] = uwDao.selectGetRetensi(liBisnisId[2],1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
//						ldecLimitTemp[2] = uwDao.selectGetRetensi(liBisnisId[2],1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
//					}
//					ldecLimitTemp[3]=new Double(Math.max(0,ldecLimit[2]-ldecLimitTemp[2]));
//				}
				if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
			
				//khsusus power save itu < retensi non reas ato qouta reas=0
//				String prodSave=props.getProperty("product.plan_powero_save");
				String tempId=f3.format(liBisnisId[1]);
//				if(prodSave.indexOf(tempId)>0 && ldecTsi >ldecLimit[1]) {
//					ldecQsReas[2]=new Double(50);
//				}
				
				// [x][][] = inssured ; 2=policy-holder
				// [][x][] = total-sar-simultan ; 2=tsi ; 3=resiko-awal ; 4=retensi ; 5=sum-reas
				
				//retensi = limit - total_sar_simultan
				if((products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString()))){ // retensi untuk product syariah... retensi rider nya sama retensi dengan retensi main product
					ldecReasPa[liCnt[1]][4][1]= new Double(500000000);
				}else
				ldecReasPa[liCnt[1]][4][1] = new Double(Math.max(0, ldecLimit[1] - ldecReasPa[liCnt[1]][1][1] - ldecReasPa[liCnt[1]][1][2] - ldecReasPa[liCnt[1]][1][3] ));
				//sum_reas = sar * qs / 100
				//sum_reas = resiko_awal - retensi
				ldecReasPa[liCnt[1]][5][1] = new Double(Math.max( 0, 
																  ldecReasPa[liCnt[1]][3][1] - ldecReasPa[liCnt[1]][4][1] ));
				
				//

//				logger.info(1);
//				TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
				
				if (aiCnt>1){
					//retensi = limit - total_sar_simultan
					//YUSUF
//					if (ldecLimit[1]>(ldecReasPa[liCnt[1]][1][1])){
//						ldecLimit[1]= (ldecLimit[1]-(ldecReasPa[liCnt[1]][1][1]))+ (ldecReasPa[liCnt[1]][1][1]);
//						ldecReasPa[liCnt[1]][4][1]= new Double(Math.max(0, ldecLimit[1]-ldecReasPa[liCnt[1]][3][2]));
//					}else
						ldecReasPa[liCnt[1]][4][1]= new Double(Math.max(0, ldecLimit[1]-(ldecReasPa[liCnt[1]][1][1])));
						//ldecReasPa[liCnt[1]][4][1] = new Double(Math.max(0, ldecLimit[1] - ldecReasPa[liCnt[1]][1][1] - ldecReasPa[liCnt[1]][1][2] - ldecReasPa[liCnt[1]][1][3] ));
					
					//
//					logger.info(2);
//					TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
					//apabila retensi > resiko awal
					if (ldecReasPa[liCnt[1]][4][1]>ldecReasPa[liCnt[1]][3][1]){
						//reas = 0 dan retensi = limit - total_sar_simultan
						ldecReasPa[liCnt[1]][5][1] =new Double(0);
						ldecReasPa[liCnt[1]][4][1] = new Double(Math.max( 0, ldecLimit[1] - ldecReasPa[liCnt[1]][1][1] - 
								 ldecReasPa[liCnt[1]][1][2] - ldecReasPa[liCnt[1]][1][3] ));
//						ldecReasPa[liCnt[1]][4][1]= new Double(Math.max(0, ldecLimit[1]-(300000000)));
						//
//						logger.info(3);
//						TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
					}else{
						//apabila total_sar_simultan/2 > limit
						if ((ldecReasPa[liCnt[1]][1][1]*0.5)>ldecLimit[1]){
							//retensi = 0 dan reas = resiko awal - retensi
							ldecReasPa[liCnt[1]][4][1]=new Double(0);
							ldecReasPa[liCnt[1]][5][1] = new Double(ldecReasPa[liCnt[1]][3][1]-ldecReasPa[liCnt[1]][4][1]);
							//
//							logger.info(4);
//							TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
						}else{
							//reas = resiko awal - retensi
							ldecReasPa[liCnt[1]][5][1] = new Double(ldecReasPa[liCnt[1]][3][1]-ldecReasPa[liCnt[1]][4][1]);
							//
//							logger.info(5);
//							TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
						}
					}
				}
				ldecReasPa[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][1] - ( ldecReasPa[liCnt[1]][3][1] - 
																	 ldecReasPa[liCnt[1]][5][1] ) ));
				ldecReasPa[liCnt[1]][5][2] = new Double(Math.max( ldecReasPa[liCnt[1]][3][2] * ldecQsReas[2] / 100, 
																  ldecReasPa[liCnt[1]][3][2] - ldecReasPa[liCnt[1]][4][2] ));
				ldecReasPa[liCnt[1]][4][3] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][2] - 
																   ( ldecReasPa[liCnt[1]][3][2] - ldecReasPa[liCnt[1]][5][2] ) ));
				ldecReasPa[liCnt[1]][5][3] = new Double(Math.max( ldecReasPa[liCnt[1]][3][3] * ldecQsReas[2] / 100, 
																  ldecReasPa[liCnt[1]][3][3] - ldecReasPa[liCnt[1]][4][3] ));
				//testing 
//				if (Integer.valueOf(tempId)==143 || Integer.valueOf(tempId)==144){
//					if (ldecReasPa[liCnt[1]][3][1]>=ldecLimit[1]){
//						ldecReasPa[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][1] - ( ldecReasPa[liCnt[1]][3][1] - 
//								 ldecReasPa[liCnt[1]][5][1] ) *0.5));
//						if (ldecReasPa[liCnt[1]][4][1]>ldecReasPa[liCnt[1]][4][2]){
//							ldecReasPa[liCnt[1]][4][1] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][1] - ( ldecReasPa[liCnt[1]][3][1] - 
//									 ldecReasPa[liCnt[1]][5][1] ) *0.5));
//							ldecReasPa[liCnt[1]][5][1]= new Double(ldecReasPa[liCnt[1]][4][1]-ldecReasPa[liCnt[1]][4][2]);
//						}else{
//							ldecReasPa[liCnt[1]][5][1]= new Double(0);
//						}
//						//
//						logger.info(6);
//						TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
//					}else {
//						ldecReasPa[liCnt[1]][4][2] = new Double(ldecLimit[1]-ldecReasPa[liCnt[1]][3][1]);
//						if (ldecReasPa[liCnt[1]][4][1]>ldecReasPa[liCnt[1]][4][2]){
//							ldecReasPa[liCnt[1]][5][1]= new Double(ldecReasPa[liCnt[1]][4][1]-(ldecLimit[1]-ldecReasPa[liCnt[1]][3][1]));
//							ldecReasPa[liCnt[1]][4][1]=new Double(ldecReasPa[liCnt[1]][4][2]);
//						}else{
//							ldecReasPa[liCnt[1]][5][1]= new Double(0);
//						}
//						//
//						logger.info(7);
//						TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
//					}
//				}
				//
//				logger.info(8);
//				TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
				
				if (ldecReasPa[liCnt[1]][3][1] == 0)
					ldecReasPa[liCnt[1]][4][1] = new Double(0);
				if (ldecReasPa[liCnt[1]][3][2] == 0)
					ldecReasPa[liCnt[1]][4][2] = new Double(0);
				if (ldecReasPa[liCnt[1]][3][3] == 0)
					ldecReasPa[liCnt[1]][4][3] = new Double(0);
				if ( (ldecReasPa[liCnt[1]][1][1] + ldecReasPa[liCnt[1]][1][2] 
						+ ldecReasPa[liCnt[1]][1][3] + ldecReasPa[liCnt[1]][3][1]
						+ ldecReasPa[liCnt[1]][3][2] + ldecReasPa[liCnt[1]][3][3])
						> ldecLimit[2]){
					String prodSave=props.getProperty("product.plan_power_save_reas");
					Integer pos=prodSave.indexOf(bisnisId);
					if(pos>0 && dataReas.getUmurTt()<69){
						liReas = 0;
					}
					
				}

				//
//				logger.info(9);
//				TestUtils.printDetailReas(ldecReasLf, ldecReasPa, ldecReasPk, ldecReasSs, ldecReasCp);
				
			}
			// reas_pk
			if( ldecReasPk[liCnt[1]][3][1] + ldecReasPk[liCnt[1]][3][2] > 0){
				
					if (ldecQsReas[1] > 0){
						ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 4,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); 
						ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 4,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); 
					}else{
						ldecLimit[1] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 2,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); 
						ldecLimit[2] = uwDao.selectGetRetensi(liBisnisId[2], liBisnisNo[2], 2,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); 
					
				}	
					if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
				//
				ldecReasPk[liCnt[1]][4][1] = new Double(Math.max( 0, ldecLimit[1] - ldecReasPk[liCnt[1]][1][1] 
																	 - ldecReasPk[liCnt[1]][1][2] ));
				ldecReasPk[liCnt[1]][5][1] = new Double(Math.max( (ldecReasPk[liCnt[1]][3][1] * ldecQsReas[2] / 100), 
																   (ldecReasPk[liCnt[1]][3][1] - ldecReasPk[liCnt[1]][4][1]) ));
				ldecReasPk[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasPk[liCnt[1]][4][1] - 
																	( ldecReasPk[liCnt[1]][3][1] - ldecReasPk[liCnt[1]][5][1] ) ));
				ldecReasPk[liCnt[1]][5][2] = new Double(Math.max( (ldecReasPk[liCnt[1]][3][2] * ldecQsReas[2] / 100),//SUPER SEHAT
																   (ldecReasPk[liCnt[1]][3][2] - ldecReasPk[liCnt[1]][4][2]) ));
				//
				if (ldecReasPk[liCnt[1]][3][1] == 0)
					ldecReasPk[liCnt[1]][4][1] = new Double(0);
				if (ldecReasPk[liCnt[1]][3][2] == 0)
					ldecReasPk[liCnt[1]][4][2] = new Double(0);
				
				//Yusuf (20 Jul 2011) - Bila produk ulink, ada excess of loss sampai dengan 7 M.
				//Dengan arti, walaupun melebihi retensi 750 jt, kalau sar nya belum sampai 7M masih tetap treaty (tidak facultative dan tidak perlu dibackup)
				Double limit = ldecLimit[2];
				if(uwDao.selectIsUlink(spaj) > 0) {
					limit = new Double("7000000000");
				}
				
				if ( (ldecReasPk[liCnt[1]][1][1] + ldecReasPk[liCnt[1]][1][2]
						+ ldecReasPk[liCnt[1]][3][1] + ldecReasPk[liCnt[1]][3][2]) 
						> limit.doubleValue()){
						//SET FACULTATIVE
						liReas = 2;
				}
				
			}
			//806, 807 = cash plan, tpd
			if (ldecReasCp[liCnt[1]][2][1] > 0){
				ldecReasCp[liCnt[1]][5][1] = new Double( ldecReasCp[liCnt[1]][3][1] * ldecQsReas[2] / 100);
				ldecReasCp[liCnt[1]][4][1] = ldecReasCp[liCnt[1]][5][1];
			}
			// simas sehat harian
			if (ldecReasSs[liCnt[1]][3][1] > 0){
				ldecReasSs[liCnt[1]][5][1] = new Double(ldecReasSs[liCnt[1]][3][1] * ldecQsReas[2] / 100);
				ldecReasSs[liCnt[1]][4][1] = new Double(ldecReasSs[liCnt[1]][3][1] - ldecReasSs[liCnt[1]][5][1] );
			}
			ldecTotalReas[liCnt[1]] = new Double(ldecReasLf[liCnt[1]][5][1] + ldecReasLf[liCnt[1]][5][2] 
										+ ldecReasPa[liCnt[1]][5][1] + ldecReasPa[liCnt[1]][5][2]
										+ ldecReasPa[liCnt[1]][5][3] + ldecReasPk[liCnt[1]][5][1] 
										+ ldecReasPk[liCnt[1]][5][2] + ldecReasSs[liCnt[1]][5][1] 
										+ ldecReasCp[liCnt[1]][5][1]); 
			if(ldecTotalReas[liCnt[1]] > 0 )
				liReas = Math.max( 1, liReas);
		}

	/*
		
		
		//Dian B

		idSimultan=uwDao.selectIdSimultan(spaj, 0);
		List spajListPowerSave=uwDao.selectReasSimultanPowerSave(idSimultan);
		
		
		//tarik retensi powersave terakhir (tapi yang sebelumnya)
		for (int d=0;d<spajListPowerSave.size()-1;d++){
			Map spajMap= (Map) spajListPowerSave.get(d);
			String spajTemp=(String) spajMap.get("REG_SPAJ");
			Double sarTemp=((BigDecimal) spajMap.get("SAR_SSP")).doubleValue();
			Double retensiTemp=((BigDecimal) spajMap.get("RETENSI_SSP")).doubleValue();
			Double reastemp =((BigDecimal) spajMap.get("REAS_SSP")).doubleValue();
//			
			List prod = uwDao.selectBisnisId(spajTemp);
			for (int i=0;i<prod.size();i++){
				Map lsbsMap= (Map)prod.get(i);
				String lsbs = f3.format((Integer) lsbsMap.get("LSBS_ID"));
				if(prodSave.contains(lsbs)){
					ldecLimitTemp[1]=new Double(0);
					ldecLimitTemp[1] =  uwDao.selectGetRetensi(liBisnisId[2],1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err); 
					ldecLimitTemp[2] =  uwDao.selectGetRetensi(liBisnisId[2],1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spajTemp,err);
				}
			}
		}
		
		//kurangi retensi terbaru dgn retensi yg terakhir
		if (prodSave.contains(bisnisId)&& aiCnt>1){
//			ldecLimitTemp[3]=new Double(Math.max(0,ldecLimit[2]-ldecLimitTemp[1]));
			ldecLimitTemp[1] =  uwDao.selectGetRetensi(liBisnisId[2],1,1,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err); 
			ldecLimitTemp[2] =  uwDao.selectGetRetensi(liBisnisId[2],1,2,liMedis,lkuId,begDate,liAge[liCnt[1]],spaj,err);
			ldecReasPa[liCnt[1]-1][4][1] = ldecLimitTemp[1];
			ldecReasPa[liCnt[1]-1][5][1] = new Double(Math.max( 0,ldecReasPa[liCnt[1]-1][3][1]-ldecLimitTemp[1]));

		}
*/
		if(liReasClient[2] == 3){
			if(ldecTsi==null)
				return null;
			Double tempTsi1=uwDao.selectGetTsiReas(spaj,insuredNo,0,liBisnisId[2],liBisnisNo[2],
							liBisnisId[2],liBisnisNo[2],lsKursId,ldecUp,err);
			Double tempTsi2=uwDao.selectGetTsiReas(spaj,insuredNo,1,liBisnisId[2],liBisnisNo[2],
							liBisnisId[2],liBisnisNo[2],lsKursId,ldecUp,err);
			if(tempTsi1==null || tempTsi2==null)
				return null;
			ldecReasComp=new Double(tempTsi1/tempTsi2);
			ldecReasLf[1][5][2] = new Double(Math.max( ldecReasLf[1][5][2], ldecReasLf[2][5][2] * ldecReasComp ));
			ldecReasLf[2][5][2] = new Double(ldecReasLf[1][5][2] / ldecReasComp);
		}
		//untuk provestara jika error proses REAS, dilakukan perubahan dari for(liCnt[1]=1;liCnt[1]<=2; menjadi for(liCnt[1]=1;liCnt[1]<2; (Ridhaal - warning )
		if(abAdd){
			for(liCnt[1]=1;liCnt[1]<=2;liCnt[1]++){
				if( (liReasClient[2] == liCnt[1]) || (liReasClient[2] > 2 ) ){
					if ( (liReasClient[2] > 2 ) && (liCnt[1] == 2 ) &&
							(msteInsured.equals(mspoPolicyHolder))) 
						return null;
					ReasTemp insReas=new ReasTemp();
					//
					
					insReas.setReg_spaj(spaj);
					if (bisnisId.equals(172)){
						insReas.setPemegang(liCnt[1]);
					}else{
						insReas.setPemegang(liCnt[1]-1);
					}
					insReas.setMste_reas(liReas);
					insReas.setExtra_mortality(ldecExtMort[1]);
					insReas.setNil_kurs(ldecKurs.intValue());
					insReas.setLku_id(lkuId);
					//
					insReas.setSimultan_tr_rd(ldecReasLf[liCnt[1]][1][1]);
					insReas.setTsi_tr_rd(ldecReasLf[liCnt[1]][2][1]);
					insReas.setSar_tr_rd(ldecReasLf[liCnt[1]][3][1]);
					insReas.setRetensi_tr_rd(ldecReasLf[liCnt[1]][4][1]);
					insReas.setReas_tr_rd(ldecReasLf[liCnt[1]][5][1] );
					//
					insReas.setSimultan_life(ldecReasLf[liCnt[1]][1][2]);
					insReas.setTsi_life(ldecReasLf[liCnt[1]][2][2]);
					insReas.setSar_life(ldecReasLf[liCnt[1]][3][2]);
					insReas.setRetensi_life(ldecReasLf[liCnt[1]][4][2]);
					insReas.setReas_life(ldecReasLf[liCnt[1]][5][2]);
					//
					//untuk produk power save perhitungan retensi balik ke awal lagi 
					//jikalau simultan polis yang ambil retensi status acceptnya sudah CCV, maturity, dll
					
					//PowerSave Dian 
					//jikalau sudah ambil rider yang berjenis PA akan mengurangi retensi power save yang dengan di ambil
					if(products.powerSave(bisnisId)){
//						String simultaId=(String)uwDao.selectMstSimultaneousIdSimultan(dataReas.getMsteInsured());
						lsOldSpaj=uwDao.selectMstCancelRegSpaj(spaj);
						if (lsbsId.equals(172)){
							idSimultan=uwDao.selectIdSimultan(spaj, 1);
						}else {
							if(lsOldSpaj!=null){
								idSimultan=uwDao.selectIdSimultan(lsOldSpaj, liCnt[1]-1);
							}else
								idSimultan=uwDao.selectIdSimultan(spaj, liCnt[1]-1);
						}
						List listSarProdSave= uwDao.listOldProdSave(idSimultan);
						Integer rlistSarProdSave= listSarProdSave.size();
						
						for(int r=0; r<rlistSarProdSave-1;r++){
							Map listSarOldMap= (Map)listSarProdSave.get(r);
							Double sarSimultan=((BigDecimal)listSarOldMap.get("SAR")).doubleValue();
							ldecReasPa[liCnt[1]][4][1]= new Double(Math.max(0 , ldecLimit[1] - sarSimultan ));
							if (ldecReasPa[liCnt[1]][1][1]> ldecLimit[1]){
								ldecReasPa[liCnt[1]][5][1]= new Double(Math.max(0,ldecReasPa[liCnt[1]][3][1]));//reas
								ldecReasPa[liCnt[1]][4][1]=0.0; //retensi
								liReas=1;
								insReas.setMste_reas(liReas);
							}else {
								ldecReasPa[liCnt[1]][4][1]= new Double(Math.max(0,ldecLimit[1]-ldecReasPa[liCnt[1]][1][1]));
								ldecReasPa[liCnt[1]][5][1]=new Double(Math.max(0,ldecReasPa[liCnt[1]][3][1]-ldecReasPa[liCnt[1]][4][1]));
								liReas=1;
								insReas.setMste_reas(liReas);
							}
						}
						//lopping id yang simultan di table sar_temp
						//trus hitung retensi nya.
						//uwDao.selectMSarTempGroup(spaj, groupReas)
/*
						SELECT b.*
						  FROM eka.mst_policy a,
						       eka.m_sar_temp b,
						       eka.lst_bisnis c,
						       eka.lst_policy_status d
						 WHERE a.mspo_policy_no = b.no_polis
						   AND b.bisnis_id = c.lsbs_id
						   AND a.lssp_id = d.lssp_id
						   AND c.lsgr_id = 2
						   AND d.lms_id IN (1, 2, 6)
						   AND b.reg_spaj = '09200800720'
						UNION
						SELECT b.*
						  FROM eka.mst_policy a,
						       eka.m_sar_temp b,
						       eka.lst_bisnis c,
						       eka.lst_policy_status d
						 WHERE a.reg_spaj= b.reg_spaj 
						   AND b.bisnis_id = c.lsbs_id
						   AND a.lssp_id = d.lssp_id
						   AND c.lsgr_id = 2
						   AND d.lms_id IN (1, 2, 6)
						   AND b.no_polis IS NULL
						   AND b.reg_spaj = '09200800720'*/
					}
					
					insReas.setSimultan_ssp(ldecReasPa[liCnt[1]][1][1]);
					insReas.setTsi_ssp(ldecReasPa[liCnt[1]][2][1]);
					insReas.setSar_ssp(ldecReasPa[liCnt[1]][3][1]);	
					insReas.setRetensi_ssp(ldecReasPa[liCnt[1]][4][1]);
					insReas.setReas_ssp(ldecReasPa[liCnt[1]][5][1]);
					if (bisnisId.equals("045")|| bisnisId=="45"||bisnisId.equals("130")|| bisnisId=="130"){
						if (insReas.getRetensi_ssp()>insReas.getSar_ssp()){
							insReas.setReas_ssp(0.0);
							liReas=0;
							insReas.setMste_reas(liReas);
						}else{
							Double reas_sspTemp=new Double(Math.max( 0,insReas.getSar_ssp()-ldecLimit[1]));
//							Double reas_sspTemp=insReas.getSar_ssp() * 0.5;//kalo lebih dari retensi langsung quateshared.....
//							Double reas_sspTemp=(insReas.getSar_ssp()-insReas.getRetensi_ssp());
							liReas=1;
							insReas.setMste_reas(liReas);
							insReas.setReas_ssp(reas_sspTemp);
						}
					}
					insReas.setSimultan_pa_in(ldecReasPa[liCnt[1]][1][2]);
					insReas.setTsi_pa_in(ldecReasPa[liCnt[1]][2][2]);
					insReas.setSar_pa_in(ldecReasPa[liCnt[1]][3][2]);
					insReas.setRetensi_pa_in(ldecReasPa[liCnt[1]][4][2]);
					insReas.setReas_pa_in(ldecReasPa[liCnt[1]][5][2]);
					//
					insReas.setSimultan_pa_rd(ldecReasPa[liCnt[1]][1][3]);
					insReas.setTsi_pa_rd(ldecReasPa[liCnt[1]][2][3]);
					insReas.setSar_pa_rd(ldecReasPa[liCnt[1]][3][3]);
					insReas.setRetensi_pa_rd(ldecReasPa[liCnt[1]][4][3]);
					insReas.setReas_pa_rd(ldecReasPa[liCnt[1]][5][3]);
					//
					insReas.setSimultan_pk_in(ldecReasPk[liCnt[1]][1][1]);
					insReas.setTsi_pk_in(ldecReasPk[liCnt[1]][2][1]);
					insReas.setSar_pk_in(ldecReasPk[liCnt[1]][3][1]);
					insReas.setRetensi_pk_in(ldecReasPk[liCnt[1]][4][1]);
					insReas.setReas_pk_in(ldecReasPk[liCnt[1]][5][1]);
					//
					insReas.setSimultan_pk_rd(ldecReasPk[liCnt[1]][1][2]);
					insReas.setTsi_pk_rd(ldecReasPk[liCnt[1]][2][2]);
					insReas.setSar_pk_rd(ldecReasPk[liCnt[1]][3][2]);
					insReas.setRetensi_pk_rd(ldecReasPk[liCnt[1]][4][2]);
					insReas.setReas_pk_rd(ldecReasPk[liCnt[1]][5][2]);
					//
					insReas.setSimultan_ssh(ldecReasSs[liCnt[1]][1][1]);
					insReas.setTsi_ssh(ldecReasSs[liCnt[1]][2][1]);
					insReas.setSar_ssh(ldecReasSs[liCnt[1]][3][1]);
					insReas.setRetensi_ssh(ldecReasSs[liCnt[1]][4][1]);
					insReas.setReas_ssh(ldecReasSs[liCnt[1]][5][1]);
					insReas.setTipe(0);
					if (bisnisId.equals("172")&&(liCnt[1]-1!=0)){//khusus untu EKA SISWA EMAS.. yang tertanggungnya aja yang direasin
						break;	
					}else
					uwDao.insertMReasTemp(insReas);
					
					if (lsbsId.equals(214) || lsbsId.equals(225)){
						liCnt[1]=3; // di set 3 biar lebih besar 2 biar untuk provestara tidak terjadi pengulangan for yg menyebabkan error - Ridhaal
					}
				}
			}
			//untuk reas cth kasus.
			//mis ada 3 polis power save maka
			//polis 1 tsi=700 jt retensi=500 maka reas=350jt ekalife=350jt
			//polis 2 tsi 200 jt retensi=500 maka reas=100 jt ekalife 100 jt
			//polis 3 tsi 600 jt retensi=500-350+100(ekalife)
			//khsusus power save itu <500 jt non reas ato qouta reas=0
			
			//sar = 100% up
			//reas =quota share 50/50
			//reas = retensi = 50% up
			//ldecUp=dataProd.getMspr_tsi();
			//TODO untuk rider new setelah insert ke reas temp baru inset ke reas temp new berdasa
			//rkan spaj jadi nilai retensi bisa surplus.
			//ldecSar=ldecTsi;

			Double reas=0.0;
			boolean getRetenPa = false;
			//jika retensi pa belum ada coz produk utama tersebut bukan pa 
			if(ldecReasPa[1][4][1]==0 && ldecReasPa[1][5][2]==0)
				getRetenPa=true;
			
				
			Double retensiPa=new Double(Math.max( 0,ldecReasPa[1][4][1]-ldecReasPa[1][3][1]));
			Double retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
			Double retensiTPD=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
			
			Double retensiH=new Double(Math.max( 0,ldecReasSs[1][4][1]));
			Integer counterL=0;
			Integer counterT=0;
			Integer counterTL=0;
			Integer counterH=0;
			//HCP banyak peserta..//TODO untuk rider link lebih baik query ulang aja. trus di looping.
			//if(flagRider==0){
			//khusus link >= 810 and <= 819
			List lsRider=uwDao.selectMstProductInsuredRiderTambahanSar(spaj, 1, 1);
			//--> cari tau dulu main product termasuk group apa??
//			just for DIAN
			List groupReas=uwDao.selectGroupReas(bisnisId);
			
			Boolean is822udah=false;
			for(int i=0;i<lsRider.size();i++){
				if(is822udah)continue;// : biar ga masuk 2 kali rider swine flue by Bertho 26092009 NOTE klo mau disable tinggal comment bagian ini aja
				liReasRider=0; 	
				Product prod=(Product)lsRider.get(i);
				if(prod.getLsbs_id()==810){//PA
					Double retensiAwal=0.0;	
//					retensiAwal=0.0;
					if(getRetenPa){
						if((products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString()))){ // retensi untuk product syariah... retensi rider nya sama retensi dengan retensi main product
						retensiPa= ldecReasLf[1][4][2];
						}else
						retensiPa=uwDao.selectGetRetensi(prod.getLsbs_id(), prod.getLsdbs_number(), 1, 1, liMedis, prod.getLku_id(), begDate, liAge[1], spaj,err);
					}
					//cek di sar_temp dimana groupnya pa lalu bandingkan dengan retensi
//					reas=prod.getSar()-retensiLife; //yang lama dari ferry
					retensiAwal=retensiPa;
					Double sarTemp=prod.getSar();
//					retensiPa=retensiPa-prod.getSar();
//					reas=retensiPa;
					if(sarTemp<=retensiAwal) //dian :prod rider sar<retensi reas=nol
//						retensiPa=0.0;
						reas=0.0;
					else{
						reas= sarTemp-retensiPa;//prod rider sar>retensi
//						reas=retensiPa; //rider itu di reas
					}
					if(reas<=0)//rider tersebut di reas kan.
						reas=0.0;
					else{
						reasRider=true;
						liReasRider=1; 	
					}
					/*
					//kalo perlu aja di buka
					Integer contReasTemp= uwDao.selectCountReasTempNew(spaj);
					if (contReasTemp>0){
						
					}else
					*/
					insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
							liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);	
				}else if(prod.getLsbs_id()!=802 && prod.getLsbs_id()!= 811 && prod.getLsbs_id()!= 819&&prod.getLsbs_id()!= 820&&prod.getLsbs_id()!= 821 ) {//sistem reas surplus (life) 100%UP
					Double retensiAwal=0.0;
					
					if (prod.getLsbs_id()== 813 ||prod.getLsbs_id().equals(813)||prod.getLsbs_id()== 822 ||prod.getLsbs_id().equals(822)||prod.getLsbs_id()== 803 ||prod.getLsbs_id().equals(803)||prod.getLsbs_id()== 816 ||prod.getLsbs_id().equals(816)||prod.getLsbs_id()== 818 ||prod.getLsbs_id().equals(818)||prod.getLsbs_id()== 817 ||prod.getLsbs_id().equals(817)){					
						
						if(prod.getLsbs_id()==822)is822udah=true;// TANYA KE DIAN APAKAH  rider yang sama dengan polis yang berbeda bisa masuk ke eka.mst_reas_temp_new
						
						idSimultan=uwDao.selectIdSimultan(spaj, 0);
						Integer rowke;
						//SAROLD
						Integer row=lsRider.size();
						List listSarOld= uwDao.listOldSar(idSimultan);
						Integer rlistSarOld= listSarOld.size();
						
						for(int r=0; r<rlistSarOld;r++){
						Map listSarOldMap= (Map)listSarOld.get(r);
						rowke=((BigDecimal)listSarOldMap.get("ROWKE")).intValue();
						counterL=counterL+1;
//						if ((products.syariah(liBisnisId[2].toString()))){
//								retensiLife= ldecReasLf[1][4][2];
//								retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
//								retensiAwal=retensiLife;
//								break;
//								if (){
//									Double sarOld= uwDao.selectOldSar(idSimultan,counterL-rowke);	
//									retensiLife=new Double(Math.max( 0,retensiLife-sarOld));
//								}
//						}else {
							if(ldecLimit[1]==null){
								ldecLimit[1]=0.0;
							}
							if (reas<ldecLimit[1]){
								if ((counterL-rowke==0) &&products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){
									retensiLife= new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
									reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
									retensiAwal=retensiLife;
//									retensiLife=new Double(Math.max( 0,retensiLife-ldecReasLf[1][3][2]));
									break;
								}else if ((counterL-rowke==0)||retensiLife==ldecLimit[1]){
//									retensiLife=ldecLimit[1];
									retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
									retensiAwal=retensiLife;
									if (retensiAwal==0.0)
										reas=prod.getSar();
									break;
								}else{
									Double sarOld= uwDao.selectOldSar(idSimultan,counterL-rowke);	
									retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]-sarOld));
									reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
									retensiAwal=retensiLife;
									break;
								}
							}else{
								retensiLife=0.0;
//								reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
//								retensiAwal=retensiLife;
								break;}
						}
//						if(retensiLife<0){
//							retensiLife=0.0;}
//						if(reas<0)//rider tersebut di reas kan.
//							reas=0.0;
//						else{
//							reasRider=true;
//							liReasRider=1; 	
//						}
//						}
					}
					if(prod.getLsbs_id()== 814 ||prod.getLsbs_id().equals(814)||prod.getLsbs_id()== 812 ||prod.getLsbs_id().equals(812)||prod.getLsbs_id()== 807 ||prod.getLsbs_id().equals(807)){//TPD Dipisahkan dari life
						idSimultan=uwDao.selectIdSimultan(spaj, 0);
						//SAROLD
							Integer row=lsRider.size();
							List listOldTpd=uwDao.listOldSarTPD(idSimultan);
							Integer rlistOldTpd=listOldTpd.size();
							for(int r=0; r<rlistOldTpd;r++){
								Map listOldTpdMap= (Map)listOldTpd.get(r);
								Integer rowke=((BigDecimal)listOldTpdMap.get("ROWKE")).intValue();
								counterT=counterL+1;
								if ((counterT-rowke==0) &&products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){
									retensiTPD= ldecReasLf[1][4][2];
									reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
									retensiAwal=retensiTPD;
//									retensiLife=new Double(Math.max( 0,retensiLife-ldecReasLf[1][3][2]));
									break;
								}else if((counterT-rowke!=0) &&products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){
									retensiTPD= ldecReasLf[1][4][2];
									reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
									retensiAwal=retensiTPD;
									break;
								}else if ((counterT-rowke==0)||retensiLife==ldecLimit[1]){
//									retensiTPD=new Double(Math.max( 0,ldecLimit[1]-ldecReasLf[1][3][2]));
									retensiTPD=ldecLimit[1];
									retensiAwal=retensiTPD;
									break;
								}else{
									Double sarOld= uwDao.selectOldSarTPD(idSimultan,counterT-rowke);	
									if (sarOld==null){
										retensiTPD=retensiTPD;
									}else{
									retensiTPD=new Double(Math.max( 0,retensiTPD-sarOld));
									}
									reas=new Double(Math.max( 0,prod.getSar()-retensiTPD));
									retensiAwal=retensiTPD;
									break;
								}
							}
						if(reas<0)//rider tersebut di reas kan.
							reas=0.0;
						else{
							reasRider=true;
							liReasRider=1; 	
						}
					}
					if(prod.getLsbs_id()== 815 ||prod.getLsbs_id().equals(815)){
						if (prod.getLsdbs_number()==1||prod.getLsdbs_number()==6){//Dian :ikut di dalam retensi life
							idSimultan=uwDao.selectIdSimultan(spaj, 0);
							//SAROLD
							Double sarOld=0.0;
//							Integer row=lsRider.size();
//							sarOld= sarOld= uwDao.selectOldSar(idSimultan, (row-(i-1)));		
//							Integer row=lsRider.size();
							List listSarOld= uwDao.listOldSar(idSimultan);
							Integer rlistSarOld= listSarOld.size();
							
							for(int r=0; r<rlistSarOld;r++){
							Map listSarOldMap= (Map)listSarOld.get(r);
							Integer rowke=((BigDecimal)listSarOldMap.get("ROWKE")).intValue();
							counterL=counterL+1;
							if ((products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString()))){
									retensiLife= ldecReasLf[1][4][2];
									retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
									retensiAwal=retensiLife;
									break;
							}else {
								if (reas<ldecLimit[1]){
									if ((counterL-rowke==0) &&products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){
										retensiLife= ldecReasLf[1][4][2];
										reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
//										retensiAwal=retensiLife;
//										retensiLife=new Double(Math.max( 0,retensiLife-ldecReasLf[1][3][2]));
										break;
									}else if ((counterL-rowke==0)||retensiLife==ldecLimit[1]){
//										retensiLife=ldecLimit[1];
										retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
										retensiAwal=retensiLife;
										break;
									}else{
										
										sarOld= uwDao.selectOldSar(idSimultan,counterL-rowke);	
										retensiLife=new Double(Math.max( 0,retensiLife-sarOld));
										reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
										retensiAwal=retensiLife;
										break;
									}
								}else{
									retensiLife=0.0;
//									reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
//									retensiAwal=retensiLife;
									break;
								}
							}}	
						}else{//Dian : masuk ke dalam retensi TPD
							idSimultan=uwDao.selectIdSimultan(spaj, 0);
							//SAROLD
								Integer row=lsRider.size();
								List listOldTpd=uwDao.listOldSarTPD(idSimultan);
								Integer rlistOldTpd=listOldTpd.size();
								for(int r=0; r<rlistOldTpd;r++){
									Map listOldTpdMap= (Map)listOldTpd.get(r);
									Integer rowke=((BigDecimal)listOldTpdMap.get("ROWKE")).intValue();
									counterT=counterL+1;
									if ((counterT-rowke==0) &&products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){
										retensiTPD= ldecReasLf[1][4][2];
										reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
										retensiAwal=retensiTPD;
//										retensiLife=new Double(Math.max( 0,retensiLife-ldecReasLf[1][3][2]));
										break;
									}else if ((counterT-rowke==0)||retensiLife==ldecLimit[1]){
//										retensiTPD=new Double(Math.max( 0,ldecLimit[1]-ldecReasLf[1][3][2]));
										retensiTPD=ldecLimit[1];
										retensiAwal=retensiTPD;
										break;
									}else{
										Double sarOld= uwDao.selectOldSarTPD(idSimultan,counterT-rowke);	
										if(sarOld!=null){
											retensiTPD=new Double(Math.max( 0,retensiTPD-sarOld));
										}
										reas=new Double(Math.max( 0,prod.getSar()-retensiTPD));
										retensiAwal=retensiTPD;
										break;
									}
								}
							if(reas<0)//rider tersebut di reas kan.
								reas=0.0;
							else{
								reasRider=true;
								liReasRider=1; 	
							}}
					}
					
					if(prod.getLsbs_id().intValue() != 804){ //Yusuf (22 jul 2011) - 804 dipisah insertnya dibawah (WPD)
						insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
								liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);
					}
					if(products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString())){ // retensi untuk product syariah... retensi rider nya sama retensi dengan retensi main product
						retensiLife= ldecReasLf[1][4][2];
						if (groupReas.equals("1")&&prod.getLsbs_id()== 803 ||prod.getLsbs_id().equals(803)||prod.getLsbs_id()== 816 ||prod.getLsbs_id().equals(816)||prod.getLsbs_id()== 813 ||prod.getLsbs_id().equals(813)||prod.getLsbs_id()== 815 ||prod.getLsbs_id().equals(815)||prod.getLsbs_id()== 818 ||prod.getLsbs_id().equals(818)||prod.getLsbs_id()== 817 ||prod.getLsbs_id().equals(817)||prod.getLsbs_id()== 822 ||prod.getLsbs_id().equals(822)){
							idSimultan=uwDao.selectIdSimultan(spaj, 0);
							Double sarOld= uwDao.selectOldSar(idSimultan, i);					
								if(sarOld!=null){
									if (i==1){ //ada kondisi tertentu 1 ga perlu dikurangi
										retensiLife=retensiLife-sarOld;
//										retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
										retensiLife=new Double(Math.max( 0,retensiLife));
									}else 
									retensiLife=new Double(Math.max( 0,retensiLife-sarOld));
						}else {
							if((products.syariah(liBisnisId[2].toString(), liBisnisNo[2].toString()))){ // retensi untuk product syariah... retensi rider nya sama retensi dengan retensi main product
								retensiPa= ldecReasLf[1][4][2];
							}else
							retensiLife=new Double(Math.max( 0,ldecReasLf[1][4][2]-ldecReasLf[1][3][2]));
						}
					reas=new Double(Math.max( 0,prod.getSar()-retensiLife));
//					retensiLife=new Double(Math.max( 0,retensiLife-prod.getSar()));
					retensiAwal=retensiLife;
					if(retensiLife<0)
						retensiLife=0.0;
					if(reas<0)//rider tersebut di reas kan.
						reas=0.0;
					else{
						reasRider=true;
						liReasRider=1; 	
					}
//					insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
//							liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);
					
					//TODO insert cuma satu doang
					List lsRider2=uwDao.selectMstProductInsuredRiderTambahan(spaj, 1, 1);
					for(int k=0;k<lsRider2.size();k++){
						Product prod2=(Product)lsRider2.get(k);
						if(prod.getReg_spaj().equals(prod2.getReg_spaj()) && 
								prod.getLsbs_id().compareTo(prod2.getLsbs_id())==0 &&
								prod.getLsdbs_number().compareTo(prod2.getLsdbs_number())==0){//jika sama
							/*
							//dibuka kalo perlu
							Integer contReasTemp= uwDao.selectCountReasTempNew(spaj);
							if (contReasTemp>0){
								
							}else
*/
//							insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
//									liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);
						}}
					}
					}
				}
			} ///END HERE BRAIS
			//sitem reas untuk HCP yang sistem reas quota share.
			List lsRiderHcp=uwDao.selectMstProductInsuredRiderTambahan(spaj, 1, 1);
			for(int i=0;i<lsRiderHcp.size();i++){
				Product prod=(Product)lsRiderHcp.get(i);
				if(prod.getLsbs_id()==811 || prod.getLsbs_id()==819){//HCP 811 dan 819 sar=100%UP
					liReasRider=1; 	
					liReas=0;
					reasRider=false;
					ldecUp=prod.getMspr_tsi();
					retensiTPD=ldecLimit[1];
					reas=ldecUp/2;
					/*
						//dibuka kalo perlu
					Integer contReasTemp= uwDao.selectCountReasTempNew(spaj);
					if (contReasTemp>0){
						
					}else
					*/
					
					insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
							liReas, ldecUp, ldecUp, reas, reas);
					
				}
			}	
			
			//Dian: WPD (Atik minta WPD dipisahkan 09112008)
			List lsRiderWPD=uwDao.selectMstProductInsuredRiderWPD(spaj, 1, 1);
			for(int i=0;i<lsRiderWPD.size();i++){
				Product prod=(Product)lsRiderWPD.get(i);
				if(prod.getLsbs_id()==804){//WPD
					reasRider=true;
					ldecUp=prod.getMspr_tsi();
					retensiTPD=ldecLimit[1];
					reas=new Double(Math.max( 0,prod.getSar()-retensiTPD));
					Double retensiAwal=retensiTPD;
					if(retensiAwal<0)
						retensiAwal=0.0;
					if(reas<0)//rider tersebut di reas kan.
						reas=0.0;
					else{
						reasRider=true;
						liReasRider=1; 	
					}
				
			insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
					liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);
				
				}
			}	
			List lsRiderHealth=uwDao.selectMstProductInsuredRiderHealth(spaj, 1, 1);
			for(int i=0;i<lsRiderHealth.size();i++){
				Product prod=(Product)lsRiderHealth.get(i);
				if(prod.getLsbs_id()==820||prod.getLsbs_id()==821||prod.getLsbs_id()==823){//Health
					reasRider=false;
					ldecUp=prod.getMspr_tsi();
					retensiH=ldecUp*0.5;
					reas=new Double(Math.max( 0,prod.getSar()-retensiH));
					Double retensiAwal=retensiH;
					if(retensiAwal<0)
						retensiAwal=0.0;
					if(reas<0)//rider tersebut di reas kan.
						reas=0.0;
					else{
						reasRider=true;
						liReasRider=1; 	
					}
				
			insertMreasTempNew(spaj, insuredNo, prod.getLsbs_id(), prod.getLsdbs_number(), prod.getLku_id(), 
					liReasRider, prod.getMspr_tsi(), prod.getSar(), retensiAwal, reas);
				
				}
			}
		}
		Map map=new HashMap();
		map.put("liReas", liReas);
		if(reasRider){
			uwDao.updateMReasTempMsteReas(spaj, 1, null);
			map.put("liReasRider", 1);
		}else
			map.put("liReasRider", 0);
		return map;
	}
	
	protected void insertMreasTempNew(String spaj,Integer insuredNo,Integer lsbsId, Integer lsdbsNum,String lkuId,Integer liReas,
									Double tsi, Double sar, Double retensi, Double reas){
		ReasTempNew insReas=new ReasTempNew();
		insReas.setReg_spaj(spaj);
		insReas.setLsbs_id(lsbsId);
		insReas.setLsdbs_number(lsdbsNum);
		insReas.setLku_id(lkuId);
		insReas.setMste_reas(liReas);
		insReas.setTsi(tsi);
		insReas.setSar(sar);
		insReas.setRetensi(retensi);
		insReas.setReas(reas);
		insReas.setMste_insured_no(insuredNo);
		
		logger.info("============ " + sar);
		
		uwDao.insertMReasTempNew(insReas);
	}
	
	/**automatis Emailing hasil proses reas di underwriting.
	 * to: ferry (IT)
	 * cc: atik (aktuaria) 
	 * @param dataReas
	 */
	private void emailReas(Reas dataReas)throws Exception{
		NumberFormat nf=new DecimalFormat("#,###.00;(#,###.00)");

    	//String to = props.getProperty("aktuaria.fitriyana");
		//String cc = props.getProperty("aktuaria.putu");
    	String to = props.getProperty("admin.yusuf");
		String cc = props.getProperty("admin.yusuf");

		List lsSar=uwDao.selectMSarTemp2(dataReas.getSpaj());
		List lsReas =uwDao.selectMReasTemp(dataReas.getSpaj());
		List lsReasNew =uwDao.selectMReasTempNew(dataReas.getSpaj());
		String regSpaj,noPolis,lsbdsName,lsdbsName,lkuSymbol,lsspStatus;
		Double sar,retensi,simultan;
		Integer medis;
		String message="";
		String msSar="<fieldset><legend>SAR TEMP</legend><table>"+
					"<th>REG SPAJ</th>"+
					"<th>NO POLIS</th>"+
					"<th>NAMA PRODUK</th>"+
					"<th>KURS</th>"+
					"<th>SAR</th>"+
					"<th>STATUS</th>"+
					"<th>MEDIS</th>"+
					"<th>GROUP</th>"+
					"</tr>";
		Map mReas=(HashMap)lsReas.get(0);
		Integer pemegang=(Integer)mReas.get("PEMEGANG");
		Integer nilKurs=(Integer)mReas.get("NIL_KURS");
		Integer msteReas=(Integer)mReas.get("MSTE_REAS");
		Double extra=(Double)mReas.get("EXTRA_MORTALITY");
		String lkuId=(String)mReas.get("LKU_SYMBOL");
		String sInsured,reasType=null,group;
		if(pemegang==0)
			sInsured="Insured";
		else
			sInsured="Holder";
		if(msteReas==0)
			reasType="Non Reas";
		else if(msteReas==1)
			reasType="Treaty";
		else if(msteReas==2)
			reasType="Facultative";

		for(int i=0;i<lsSar.size();i++){
			Map map=(HashMap)lsSar.get(i);
			regSpaj=(String)map.get("REG_SPAJ");
			noPolis=(String)map.get("NO_POLIS");
			lsdbsName=(String)map.get("LSDBS_NAME");
			lkuSymbol=(String)map.get("LKU_SYMBOL");
			sar=(Double)map.get("SAR");
			lsspStatus=(String)map.get("LSSP_STATUS");
			medis=(Integer)map.get("MEDIS");
			group=(String)map.get("LSGR_NAME");
			msSar+="<tr>" +
						"<td>"+regSpaj+"</td>"+
						"<td>"+noPolis+"</td>"+
						"<td>"+lsdbsName+"</td>"+
						"<td>"+lkuSymbol+"</td>"+
						"<td>"+nf.format(sar)+"</td>"+
						"<td>"+lsspStatus+"</td>"+
						"<td>"+medis+"</td>"+
						"<td>"+group+"</td>"+
					"</tr>";
		}
		msSar+="</table></fieldset>";
		//
		String msReas="<fieldset><legend>REAS TEMP</legend><table>"+
					 "<tr><th>Insured For</th><td>"+sInsured+"</td><th>US$ Conv</th><td>"+nilKurs+"</td></tr>"+
					 "<tr><th>Reins Type</th><td>"+reasType+"</td><th>Extra Mort.</th><td>"+extra+"</td></tr>"+
					 "<tr><th></th><th>Sum At Risk</th><th>Retensi</th><th>Reassured</th></tr>"+
					 "<tr><th>Term Rider</th><td>"+nf.format(mReas.get("SAR_TR_RD"))+"</td><td>"+nf.format(mReas.get("RETENSI_TR_RD"))+"</td><td>"+nf.format(mReas.get("REAS_TR_RD"))+"</td></tr>"+
					 "<tr><th>Main Product</th><td>"+nf.format(mReas.get("SAR_LIFE"))+"</td><td>"+nf.format(mReas.get("RETENSI_LIFE"))+"</td><td>"+nf.format(mReas.get("REAS_LIFE"))+"</td></tr>"+
					 "<tr><th>Simas SP</th><td>"+nf.format(mReas.get("SAR_SSP"))+"</td><td>"+nf.format(mReas.get("RETENSI_SSP"))+"</td><td>"+nf.format(mReas.get("REAS_SSP"))+"</td></tr>"+
					 "<tr><th>PA Include</th><td>"+nf.format(mReas.get("SAR_PA_IN"))+"</td><td>"+nf.format(mReas.get("RETENSI_PA_IN"))+"</td><td>"+nf.format(mReas.get("REAS_PA_IN"))+"</td></tr>"+
					 "<tr><th>PK Include</th><td>"+nf.format(mReas.get("SAR_PA_RD"))+"</td><td>"+nf.format(mReas.get("RETENSI_PA_RD"))+"</td><td>"+nf.format(mReas.get("REAS_PA_RD"))+"</td></tr>"+
					 "<tr><th>PK Rider</th><td>"+nf.format(mReas.get("SAR_PK_IN"))+"</td><td>"+nf.format(mReas.get("RETENSI_PK_IN"))+"</td><td>"+nf.format(mReas.get("REAS_PK_IN"))+"</td></tr>"+
					 "<tr><th>Super Sehat</th><td>"+nf.format(mReas.get("SAR_PK_RD"))+"</td><td>"+nf.format(mReas.get("RETENSI_PK_RD"))+"</td><td>"+nf.format(mReas.get("REAS_PK_RD"))+"</td></tr>"+
       			     "<tr><th>Cash Plan</th><td>"+nf.format(mReas.get("SAR_SSH"))+"</td><td>"+nf.format(mReas.get("RETENSI_SSH"))+"</td><td>"+nf.format(mReas.get("REAS_SSH"))+"</td></tr>"+
					 "<tr><th>TPD</th><td>"+nf.format(mReas.get("SAR_CASH"))+"</td><td>"+nf.format(mReas.get("RETENSI_CASH"))+"</td><td>"+nf.format(mReas.get("REAS_CASH"))+"</td></tr>";
		//

		
		String msReasNew="";
		for(int i=0;i<lsReasNew.size();i++){
			ReasTempNew rtNew=(ReasTempNew)lsReasNew.get(i);
			msReasNew+="<tr><th>"+rtNew.getLsdbs_name()+"</th><td>"+nf.format(rtNew.getSar())+"</td><td>"+nf.format(rtNew.getRetensi())+"</td><td>"+nf.format(rtNew.getReas())+"</td></tr>";
		}
		msReasNew+="</table></fieldset>";
		
		message=msSar+msReas+msReasNew;
		//New business yang bermasalah
		logger.info(message);
		email.send(true, props.getProperty("admin.ajsjava"), new String[] {to}, new String[] {cc}, null, "Reas (NEW) by "+dataReas.getCurrentUser().getLus_full_name(),message, null);
//		email.send(true, props.getProperty("admin.ajsjava"), new String[] {to}, new String[] {cc}, null, "Reas TESTING EMAIL "+dataReas.getCurrentUser().getLus_full_name(),message, null);
	}
	
}
