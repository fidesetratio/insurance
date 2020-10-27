package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Akseptasi;
import com.ekalife.elions.model.BiayaUlink;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.ParameterClass;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.User;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
	
/**@author Ferry Harlim
 *<p> Class merupakan Simple Form Controller, dimana berfungsi untuk mengontrol 
 * proses Akseptasi atau Fund Allocation, Setelah melakukan salah satu dari proses ini,
 * maka akan secara langsung akan menggenerate NoPolis dari suatu SPAJ.Proses Fund Allocation hanya dapat
 * dilakukan oleh produk unit link yaitu untuk mengalokasikan dana, sesuai yang tertera pada polis
 * tersebut. </p>
 * 
 * 
 */
public class AkseptasiFormController extends ParentFormController {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	DecimalFormat fmt = new DecimalFormat ("000");
	SimpleDateFormat sdfMm=new SimpleDateFormat("MM");
	SimpleDateFormat sdfYy=new SimpleDateFormat("yyyy");
	
	/**Fungsi : Sebagai referensi dari suatu controller dimana akan mereferensikan data-
	 * 			data yang akan ditampilkan pada halaman jsp. Seperti : List dari suatu status,
	 * 			list cabang, dll.
	 * @param  HttpServletRequest request, Object cmd, Errors err
	 * @return Map
	 * */
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map=new HashMap();
		Akseptasi aksep=(Akseptasi)cmd;
		List lsStatus=elionsManager.selectLstStatusAccept(null);
		List lsStatusAksep=elionsManager.selectLstStatusAcceptAksepNFund();
		List lsStatusAksepSub = uwManager.selectSubStatusAccept(aksep.getLssaId());
		String spaj=request.getParameter("spaj");
		String team_name=uwManager.selectBancassTeam(spaj);
		if(team_name==null)team_name= "";			
		if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")){			
			map.put("team",1);
		}else{
			map.put("team", 0);
		}
		map.put("lsStatus",lsStatus);
		map.put("lsStatusAksep",lsStatusAksep);
		map.put("lsStatusAksepSub", lsStatusAksepSub);
		return map;
	}
	/**Fungsi : Untuk Mengurangi resiko dari input data yang akan di koreksi pada halaman ini.
	 * 			Diamana terdapat pengkoresiaan dalam bentuk doubleEditor, integerEditor,dan dateEditor
	 * @param	HttpServletRequest request, ServletRequestDataBinder binder
	 * */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	/**Fungsi : Untuk membuka data polis dan melakukan pengecekan awal dari suatu polis jika ingin diproses,
	 * 			seperti pengecekan posisi polis, dimana polis pada proses ini harus berada di Underwritting(2)
	 * @param 	HttpServletRequest request
	 * @return	com.ekalife.elions.uw.model.Akseptasi
	 **/
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Akseptasi akseptasi=new Akseptasi();
		akseptasi.setLbUlink(false);
		akseptasi.setLspdId(new Integer(2));
		akseptasi.setLsspId(new Integer(10));
		akseptasi.setLssaId(new Integer(5));
		akseptasi.setLstbId(new Integer(1));
//		akseptasi.setBlock(new String());
		akseptasi.setSpaj(request.getParameter("spaj"));
		List lsStatusAksep=elionsManager.selectMstPositionSpajAkseptasi(akseptasi.getSpaj());
		String proses=request.getParameter("proses");
		//
		akseptasi.setCurrentUser( (User) request.getSession().getAttribute("currentUser"));        
		
		Position position=new Position();
		position.setReg_spaj(akseptasi.getSpaj());
		position.setMsps_date(elionsManager.selectSysdate(new Integer(2)));
		position.setLus_id(akseptasi.getCurrentUser().getLus_id());
		position.setLus_login_name(akseptasi.getCurrentUser().getName());
		//cek lagi untuk pengesetan info pake karena karena untuk sementara kondisi ini gak terpakai seutuhnya
		
		if(akseptasi.getInfo1()==10)
			position.setLssa_id(new Integer(8));
		else
			position.setLssa_id(new Integer(5));
		lsStatusAksep.add(position);
		
		Map mPosisi=elionsManager.selectF_check_posisi(akseptasi.getSpaj());
		Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
		List <Product> extrapremi =bacManager.selectMstProductInsuredExtra(akseptasi.getSpaj());
			if (!extrapremi.isEmpty()){
				akseptasi.setSubstandart("[SUBSTANDART]");
			}
		//produk asm
		Map map=uwManager.selectDataUsulan(akseptasi.getSpaj());
		Integer lsbsId=(Integer)map.get("LSBS_ID");
		//cekin untuk produk yang masih pending
		Map mapTt=elionsManager.selectTertanggung(akseptasi.getSpaj());
		Integer lssaId=(Integer)mapTt.get("LSSA_ID");
		Integer flag_investasi=(Integer) mapTt.get("MSTE_FLAG_INVESTASI");
		akseptasi.setFlag_investasi(flag_investasi);
		Map mAksep=(HashMap)elionsManager.selectLstStatusAccept(lssaId).get(0);
		String statusAksep=(String) mAksep.get("STATUS_ACCEPT");
		Integer countSts=elionsManager.selectCountMstPositionSpaj(akseptasi.getSpaj(),"3,4,9");
		
		if(lspdIdTemp.intValue()!=2){
			akseptasi.setInfo1(1);
			akseptasi.setLsposDoc(lspdPosTemp);
			//polis ini ada di lspdPosTemp.
		}else if(lsbsId.intValue()==161){//produk asm
			akseptasi.setInfo1(2);
		}else if(countSts>0){ //akseptasi khusus (further requirement, extra premi, postponed spaj)
			akseptasi.setInfo1(3);
			akseptasi.setPesan("Status Polis masih "+statusAksep+"\nSilahkan gunakan Akseptasi Khusus untuk Mengaksep Polis ini.");
			//akseptasi.set
		}	
		
		List<Datarider> listRider = (List<Datarider>) elionsManager.selectDataUsulan_rider(akseptasi.getSpaj());
		Integer total_ekasehat = uwManager.selectCountEkaSehatAdmedikaNew(akseptasi.getSpaj(),2);
		
		for(int i=0;i<total_ekasehat;i++){
			List<Map> listPeserta = uwManager.selectDataPeserta(akseptasi.getSpaj());
			if(listPeserta.isEmpty())continue;
			
			for(int j=0; j<listPeserta.size();j++){
				Map mapPeserta = listPeserta.get(j);
				Integer umur;
				if(mapPeserta.get("UMUR")==null){ //untuk mengatasi UMUR null di eka.mst_peserta
					f_hit_umur umr= new f_hit_umur();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String birthDate = df.format(mapPeserta.get("TGL_LAHIR"));
					String sysDate = df.format(elionsManager.selectSysdate());
					umur = umr.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)),Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2)));
					String noreg = (mapPeserta.get("NO_REG").toString());
					bacManager.updateUmurMstPeserta(akseptasi.getSpaj(), umur, noreg);
				}else{
					umur = Integer.parseInt(mapPeserta.get("UMUR").toString());
				}
				if( umur>=50 && umur<=55){
					akseptasi.setInfo1(4);
				}
			}
		}
		
		String rdm = ServletRequestUtils.getStringParameter(request, "spaj");
		String rdblock = uwManager.selectRedeemBlock(rdm);
		if (rdblock != null){		
			akseptasi.setBlock("SPAJ ini sedang diajukan redeem, tidak dapat proses lebih lanjut!");
		}
		
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(akseptasi.getSpaj());
		List<Datarider> xx = dataUsulan.getDaftaRider();
		HashMap tgl_kr_lhr = bacManager.selectMstTransHistoryNewBussines(akseptasi.getSpaj());
		
		if (dataUsulan.getDaftaRider().size() != 0) {
			for (Datarider datarider : xx) {
				if (datarider.getLsbs_id() == 836) {
					if (tgl_kr_lhr.get("TGL_PERKIRAAN_LAHIR") == null) 
						akseptasi.setBlock("Tanggal perkiraan lahir bayi belum diisi, mohon isi kolom SMiLe Baby");
				} 
			}
		}
		
		akseptasi.setListSize(lsStatusAksep.size());
		akseptasi.setLsStatusAksep(lsStatusAksep);
		akseptasi.setSize(new Integer(lsStatusAksep.size()));
		return akseptasi;
	}
	/**
	 * Fungsi : Untuk melakukan pengecekan dari kriteria-kriteria/syarat dari suatu polis,
	 * 			Apakah polis tersebut telah memenuhi seluruh kriteria untuk di Aksep
	 * 			dan dilanjutkan ke proses selanjutnya.
	 * 			Jika ada Suatu kriteria tambahan maka dapat di sisipan suatu kondisi pada method ini.
	 * 
	 * @param HttpServletRequest request, Object cmd, BindException err
	 * */
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Akseptasi akseptasi=(Akseptasi)cmd;
		akseptasi.setProses(request.getParameter("proses"));
		String sAksep=request.getParameter("statAksep");
		akseptasi.setInfo1(0);
		//data usulan asuransi
		Map mDataUsulan=uwManager.selectDataUsulan(akseptasi.getSpaj());
		akseptasi.setPModeId((Integer)mDataUsulan.get("LSCB_ID"));
		akseptasi.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
		akseptasi.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
		akseptasi.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
		akseptasi.setLsdbsNumber((Integer)mDataUsulan.get("LSDBS_NUMBER"));
		akseptasi.setBisnisId(fmt.format(akseptasi.getLsbsId().intValue()));
		akseptasi.setPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
		akseptasi.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
		
		
		int pos=0;
		String kode;
		//cek power save
		if(products.powerSave(akseptasi.getBisnisId())){
			if(!products.stableSavePremiBulanan(akseptasi.getBisnisId()) && !products.stableSave(akseptasi.getLsbsId(), akseptasi.getLsdbsNumber())){
				Long llCount=elionsManager.selectCountMstPowerSave(akseptasi.getSpaj());
				ParameterClass parameterClass=elionsManager.selectMstPowersaveDpDate(akseptasi.getSpaj());
				if(Integer.parseInt(akseptasi.getBisnisId())==188){
					llCount = uwManager.selectCountMstPowerSaveBaru(akseptasi.getSpaj());
					parameterClass=uwManager.selectMstPowersaveBaruDpDate(akseptasi.getSpaj());
				}
				Date ldtDepo=parameterClass.getMps_deposit_date();
				Double ldecPrmDepo=parameterClass.getMps_prm_deposit();
				
				if(llCount.intValue()==0 ){
					err.reject("","Investasi Power Save Belum Ada..!!");
				}else if(ldtDepo.compareTo(ldtDepo)!=0 || ldecPrmDepo.compareTo(akseptasi.getPremium())!=0 ){
					err.reject("","Investasi Power Save Tidak Sama..!!");
				}
			}
		}
		//data tertanggung
		Map mTertanggung=elionsManager.selectTertanggung(akseptasi.getSpaj());
		akseptasi.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
		akseptasi.setMsteInsured((String)mTertanggung.get("MCL_ID"));
		akseptasi.setMsagId((String)mTertanggung.get("MSAG_ID"));
		akseptasi.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
		//dw1 //pemegang
		Policy policy=elionsManager.selectDw1Underwriting(akseptasi.getSpaj(),akseptasi.getLspdId(),akseptasi.getLstbId());
		akseptasi.setMspoPolicyHolder(policy.getMspo_policy_holder());
		akseptasi.setNoPolis(policy.getMspo_policy_no());
		akseptasi.setInsPeriod(policy.getMspo_ins_period());
		akseptasi.setPayPeriod(policy.getMspo_pay_period());
		akseptasi.setLkuId(policy.getLku_id());
		akseptasi.setUmurPp(policy.getMspo_age());
		akseptasi.setLcaId(policy.getLca_id());
		
		//
		if(! wf_check_premi(akseptasi.getSpaj(),akseptasi.getInsuredNo())){
			err.reject("","Tidak Bisa transfer ada kesalahan premi.~nHubungi E.D.P !!!");
		}
		//
		if(products.unitLink(akseptasi.getBisnisId()))
			akseptasi.setLbUlink(true);
		//cek jika ada rider tambahan pada Lions sehingga harus tekan tombol investasi (untuk sementara)
		if(akseptasi.isLbUlink()){
			Integer riderBiayaId,riderBiayaNum;
			Integer riderProdId,riderProdNum;
			
			List lsRider=elionsManager.selectMstProductInsuredRiderTambahan(akseptasi.getSpaj(),akseptasi.getInsuredNo(),new Integer(1));
			//jika rider tidak kosong(ada)
			boolean flag=false;
			//khusus platinum link (134) dan amanah link (166) biaya rider tidak ada yusuf (03/01/2008)
			//untuk produk Rencana Cerdas juga dikenakan di awal, bkn potong unit
			if(! lsRider.isEmpty() && !akseptasi.getBisnisId().equals("134") && !akseptasi.getBisnisId().equals("164") && !akseptasi.getBisnisId().equals("166") && !akseptasi.getBisnisId().equals("186") && !akseptasi.getBisnisId().equals("190") && !akseptasi.getBisnisId().equals("191") ){
				for(int j=0;j<lsRider.size();j++){
					Product product=(Product)lsRider.get(j);
					riderProdId=product.getLsbs_id();
					riderProdNum=product.getLsdbs_number();
					flag=false;
					if(logger.isDebugEnabled())logger.debug("prod id="+riderProdId);
					if(logger.isDebugEnabled())logger.debug("prod number ="+riderProdNum);
					//cek rider dengan biaya apakah sama atau tidak
					List lsBiayaUlink=elionsManager.selectMstBiayaUlink(akseptasi.getSpaj(),new Integer(1));
					for(int i=0;i<lsBiayaUlink.size();i++){
						BiayaUlink biayaUlink=(BiayaUlink)lsBiayaUlink.get(i);
						riderBiayaId=biayaUlink.getLsbs_id();
						riderBiayaNum=biayaUlink.getLsdbs_number();
						
						if(riderBiayaId==null)
							riderBiayaId=new Integer(1000);
						if(riderBiayaNum==null)
							riderBiayaNum=new Integer(1000);
						
						if( (riderBiayaId.intValue()== riderProdId.intValue()) && (riderBiayaNum.intValue()==riderProdNum.intValue()) )
							flag=true;
						
					}
					if(flag==false)
						err.reject("","Product Unit Link Rider Tambahan Tidak Terdapat Pada BIaya Investasi,Silahkan Tekan Tombol Investasi");
					
					//(Deddy) - REQ ASRI: Untuk eka sehat dan produk Unit link yg memiliki eka sehat rider, apabila usia peserta >=50tahun, tidak bisa diaksep
					//(Deddy) - REQ ASRI - 26 Oktober 2010: Tambahkan lagi,apabila belum cek medis maka tidak aksep.
//					if(sAksep.equals("5")){
//						if((products.unitLink(akseptasi.getBisnisId()) || Integer.parseInt(akseptasi.getBisnisId())==183 || Integer.parseInt(akseptasi.getBisnisId())==189 || Integer.parseInt(akseptasi.getBisnisId())==193) && akseptasi.getUmurTt()>=50 && uwManager.cekMstWorksheet(akseptasi.getSpaj(),new Integer(1)) == 0  ){
//							if(Integer.parseInt(akseptasi.getBisnisId())==183 || Integer.parseInt(akseptasi.getBisnisId())==189 || Integer.parseInt(akseptasi.getBisnisId())==193){
//								err.reject("","Tidak dapat di - AKSEP, karena usia peserta di atas 50 tahun");
//							}else{
//								for(int i=0;i<lsRider.size();i++){
//									Product banding1 = (Product)lsRider.get(i);
//									if((banding1.getLsbs_id()==820 || banding1.getLsbs_id()==823 || banding1.getLsbs_id()==825) && banding1.getLsdbs_number()<16){
//										err.reject("","Tidak dapat di - AKSEP, karena usia peserta di atas 50 tahun");
//									}
//								}
//							}
//						}
//					}DICOMMENT
					
				}	
			}
			
		}
	
		//
		Map mStatus=elionsManager.selectWfGetStatus(akseptasi.getSpaj(),akseptasi.getInsuredNo());
		akseptasi.setLiAksep((Integer)mStatus.get("LSSA_ID"));
		akseptasi.setLiReas((Integer)mStatus.get("MSTE_REAS"));
		Integer liActive=(Integer)mStatus.get("MSTE_ACTIVE");
		Integer flagInvestasi=(Integer)mStatus.get("MSTE_FLAG_INVESTASI");
		Date mste_tgl_aksep=(Date)mStatus.get("MSTE_TGL_AKSEP");
		if(akseptasi.getLiReas()==null)
			akseptasi.setLiReas(new Integer(100));
		
		if (akseptasi.getLiAksep()==null) 
			akseptasi.setLiAksep(new Integer(1));
		Integer liAksepAsli= akseptasi.getLiAksep();
		Integer liBackup=null;
		liBackup=(Integer)elionsManager.selectMstInsuredMsteBackup(akseptasi.getSpaj(),akseptasi.getInsuredNo());
		if(liBackup==null)
			liBackup=new Integer(100);
		//flag tambahan jika reas error bagi ulink
//		if(sAksep.equals("8"))
//			if(akseptasi.getLiReas().intValue()==100 || liBackup.intValue()==100 || liBackup.intValue()==0)
//				err.reject("","Blom Proses Reas");
		if(sAksep.equals("5")){		
			if( (akseptasi.getMspoPolicyHolder().substring(0,2).equals("XX") || akseptasi.getMspoPolicyHolder().substring(0,2).equals("WW")) && (!akseptasi.isLbUlink())){
				err.reject("","Proses Simultan Belum dilakukan !!!");
			}else if( (akseptasi.getLiReas().intValue()==100|| liBackup.intValue()==100 ||liBackup.intValue()==0) && (!akseptasi.isLbUlink())){
				err.reject("","Belum Proses Reas");
			}else if(akseptasi.getLiReas().intValue()==2 && liBackup.intValue()==0){
				err.reject("","Tidak dapat di - AKSEP, karena tidak Ada Backup Reasuransi. Hubungi Aktuaria");
			}
			
		}	
		
		//ariani 31012008 postponed bisa accepted
		if(akseptasi.getLiAksep()==1 ||akseptasi.getLiAksep()==5|| akseptasi.getLiAksep()==8 || 
		   akseptasi.getLiAksep()==3 || akseptasi.getLiAksep()==4 || akseptasi.getLiAksep()==9 || 
		   akseptasi.getLiAksep()==10 || akseptasi.getLiAksep()==17){
			if(sAksep.equals("5")){//aksep
				if(mste_tgl_aksep!=null){
				//if(liActive==1){
					err.reject("","Polis ini sudah di Aksep");
					if(akseptasi.getNoPolis()==null){
						err.reject("","Polis ini sudah di Aksep Tetapi No Polis Null Silahkan Hubungi ITwebandmobile@sinarmasmsiglife.co.id");
					}
				}	
			}else if(sAksep.equals("8")){
				if(elionsManager.selectCountMstTransUlink(akseptasi.getSpaj(),null).intValue()!=0)
						err.reject("","Polis ini sudah di FUND ALOCATION.Tidak Dapat di Fund Ulang");
					
			}else if(sAksep.equals("10")){
				if(akseptasi.getLiAksep()==10)
					err.reject("","Polis Inis Sudah DiAksep (AkseptasI Khusus). Tidak Bisa Di Aksep Lagi.");
			}
					
					
			
		}else{
			List lsStatus=elionsManager.selectLstStatusAccept(akseptasi.getLiAksep());
			Map map=(HashMap)lsStatus.get(0);
			String status=(String)map.get("STATUS_ACCEPT");
			err.reject("","Polis Tidak dapat di aksep status polis "+status);
		}	
		//
		if(akseptasi.isLbUlink()){
			if(products.stableLink(akseptasi.getLsbsId().toString())) {
				List<Map> slink = elionsManager.selectValidasiStableLink(akseptasi.getSpaj());
				if(akseptasi.getLsdbsNumber()==11){
					slink = uwManager.selectValidasiNewStableLink(akseptasi.getSpaj());
				}
				if(slink.isEmpty()) {
					err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
				}else {
					for(Map m : slink) {
						int validasi = ((BigDecimal) m.get("V")).intValue();
						if(validasi == 0) {
							err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
							break;
						}
					}
				}
				
				if(uwManager.selectCountMstSlinkBasedPosition(akseptasi.getSpaj(), null).intValue()>0)
					if(Integer.parseInt(sAksep)==5 || Integer.parseInt(sAksep)==10){
						err.reject("","Ini Polis New Simas Stabil Link. Harus FUND ALLOCATION dulu");
				}
			}else {
				Double ldecPersen;
				ldecPersen=elionsManager.selectSumPersenMstDetUlink(akseptasi.getSpaj());
				if(ldecPersen==null || ldecPersen.intValue()!=100){
					err.reject("","Produk Unit-Linked !!! Alokasi Investasi Belum Lengkap");
				}
				//
				if(akseptasi.getLsbsId().intValue()==87 || akseptasi.getLsbsId().intValue()==101){
					Double ldecPremi;
					ldecPremi=elionsManager.selectSumJlhPremiMstUlink(akseptasi.getSpaj());
					if(ldecPremi==null ||ldecPremi.doubleValue()<50000000){
						err.reject("","xcellink Platinum !!! Total premi kurang dari Rp 50jt.");
					}
				}
				//cek proses fund dgn proses fund baru akep (default)
				//if(flagInvestasi.intValue()==1)
				if(elionsManager.selectCountMstTransUlink(akseptasi.getSpaj(),null).intValue()==0)
					if(Integer.parseInt(sAksep)==5 || Integer.parseInt(sAksep)==10){
							err.reject("","Ini Polis Excellink. Harus FUND ALLOCATION dulu");
					}
			}
		}
		//
		pos=0;
		//'096, 099, 135, 136'
		kode=props.getProperty("product.plan_minvest");
		pos=kode.indexOf(akseptasi.getBisnisId());
		logger.info(akseptasi.getBisnisId()); //27 oktober
/*		if(akseptasi.getLsbsId().intValue()!=182){
			if(pos>=0){
				List lsDp=elionsManager.selectMstDepositPremium(akseptasi.getSpaj(),new Integer(1));//order by msdp_date_book desc
				Date ldtRk;
//				logger.info(ldtRk);
				Calendar calAwal=new GregorianCalendar(2005,10-1,01);
				Calendar calAkhir=new GregorianCalendar(2007,1-1,31);
			
				DepositPremium depositPremium=(DepositPremium)lsDp.get(0);
				ldtRk=depositPremium.getMsdp_date_book();
				if(akseptasi.getLsbsId().intValue()==96 || akseptasi.getLsbsId().intValue()==99){
					if(FormatDate.isDateBetween(ldtRk,calAwal.getTime(),calAkhir.getTime())){
						err.reject("","Tgl RK SPAJ ini : "+defaultDateFormatStripes.format(ldtRk)+"(dd/mm/yyyy) Khusus Periode 1 Oktober 2005 sampai dengan 31 Januari 2007 Harus Pakai Multi-Invest III (new) 135/136 ");
						akseptasi.setTglAwal(defaultDateFormatStripes.format(ldtRk));
					}	
				}else if( akseptasi.getLsbsId().intValue()==135 || akseptasi.getLsbsId().intValue()==136){
					if(! FormatDate.isDateBetween(ldtRk,calAwal.getTime(),calAkhir.getTime())){
						err.reject("","Tgl RK SPAJ ini :"+defaultDateFormatStripes.format(ldtRk) +"(dd/mm/yyyy) Di Luar Periode 1 Oktober 2005 sampai dengan 31 Januari 2007 !!! Harus Pakai Multi-Invest III 96/99 !!!");
						akseptasi.setTglAwal(defaultDateFormatStripes.format(ldtRk));
					}
				}
			}
		}*/
		//
		pos=0;
		kode=props.getProperty("product.plan_asystem");
		pos=kode.indexOf(akseptasi.getBisnisId());
		if(pos>=0){
			//untuk ekalink bisa as dan regional
			if("37,46,52".indexOf(akseptasi.getLcaId()) == -1){
//			if( (! akseptasi.getLcaId().equalsIgnoreCase("37") && ! akseptasi.getLcaId().equalsIgnoreCase("46")) ){
				String kodeTemp=props.getProperty("product.plan_asystemandregional");
				int posTemp=0;
				posTemp=kodeTemp.indexOf(akseptasi.getBisnisId());
				if(posTemp<0) {
					//err.reject("","Kode Plan Yang Anda di-Pilih Hanya untuk Agency System");
				}
			}
		}
		//
		if("37,46,52".indexOf(akseptasi.getLcaId()) > -1){
//		if(akseptasi.getLcaId().equalsIgnoreCase("37") || akseptasi.getLcaId().equalsIgnoreCase("46")){
			if( (akseptasi.getLsbsId().intValue()!=158 && akseptasi.getLsdbsNumber().intValue()!=3) ){//power save bulanan AS (158~3)
				//pada power save bulanan tidak semuanya plan nya AS.
				String kodeTemp=props.getProperty("product.plan_asystemandregional");
				int posTemp=0;
				posTemp=kodeTemp.indexOf(akseptasi.getBisnisId());
				if(posTemp<0)//ekalink family merupakan produk regional dan agency
					if(pos<=0){
						//err.reject("","Kode Agent Agency System.Silahkan Pilih Hanya Kode Plan untuk Agency System");
					}
			}
			
		}else if(akseptasi.getLcaId().equalsIgnoreCase("09")){
			if(akseptasi.getLsbsId().intValue()==123){
				err.reject("","Power Save yg di-pilih, untuk individu.");
			}
		}else{
			if(akseptasi.getLsbsId().intValue()==94){
				err.reject("","Power Save yg di-pilih, untuk Bancass.");
			}
		}
		//closed by Himmia 24052006
		//IF MessageBox('U/W', 'Anda Yakin Aksep Polis ini ?', Question!, Yesno!, 2) <> 1 THEN RETURN
		//if(medical.intValue()==0 && umurTt.intValue()>=40){
		//wf_sample_uw(err);
		//}
			
		akseptasi.setLiAksep(Integer.valueOf(sAksep));
		
		if(akseptasi.getLiAksep()==5){
			String sub = request.getParameter("substatus");
			Integer sub_id = Integer.valueOf(sub);
			akseptasi.setSubliAksep(sub_id);
		}
		
		if(akseptasi.getProses().equals("1")){//proses akseptasi
			if(liAksepAsli.intValue()==8 && akseptasi.getLiAksep().intValue()==8){
				err.reject("","Sudah Fund Alocation");
			}
		}
		if(akseptasi.getLiAksep().intValue()==0){
			err.reject("","Gagal Akseptasi");
		}
		//
		if( akseptasi.isLbUlink()==false && sAksep.equals("8"))
			err.reject("","Bukan Product Unitlink tidak dapat di fund");
		
		//(Deddy - 19 Jul 2012) Syncronisasi program SNOWS, dimana titipan premi diinput untuk semua case baik Bancass maupun non bancass.Jadi titipan premi harus diinput sebelum mengaksep polis.
		Account_recur account_recur = elionsManager.select_account_recur(akseptasi.getSpaj());
		List lds_dp=elionsManager.selectMstDepositPremium(akseptasi.getSpaj(),null);
		int isInputanBank = elionsManager.selectIsInputanBank(akseptasi.getSpaj());
		//Anta - Penambahan kondisi untuk PowerSave Syariah
		if(lds_dp.size()==0 && (Integer.parseInt(sAksep)==5 || Integer.parseInt(sAksep)==10) &&
			isInputanBank!=2 && isInputanBank!=3 && "58, 40".indexOf(akseptasi.getLcaId())<0  &&
			(akseptasi.getLsbsId().intValue()!=175 && akseptasi.getLsdbsNumber().intValue()!=2)){
				if(account_recur.getFlag_autodebet_nb()!=null){
					if(account_recur.getFlag_autodebet_nb()!=1){
					//	err.reject("","Jumlah Titipan Premi/Payment Belum Di Input.Silakan diinput terlebih dahulu sebelum melakukan Akseptasi/Akseptasi Khusus");
					}
			}else{
					//err.reject("","Jumlah Titipan Premi/Payment Belum Di Input.Silakan diinput terlebih dahulu sebelum melakukan Akseptasi/Akseptasi Khusus");
			}
		}
		
		// Check apakah dana investasi mencukupi req Hanifah (#67541) - Daru 23 Mar 2015
		if(akseptasi.isLbUlink() && "8".equals(sAksep)) {
			if(!elionsManager.cekDanaInvestasiMencukupi(akseptasi.getSpaj()))
				err.reject("", "Dana investasi tidak cukup untuk melakukan proses Fund");
		}

	}
	/**
	 * Fungsi : berfungsi untuk memperbolehkan suatu polis untuk di proses karena telah
	 * 			melewati kriteria atau persyaratan dari polis tersebut sebelum di transfer.
	 * @param 	HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err
	 * @return 	Object untuk menampilkan info dalam bentuk object parameter dan dalam bentuk BindException
	 * 			dari proses transfer ini dan 
	 * hasil=51 ==> No Polis Kosong , Silahkan Hubungi EDP
	 * hasil=52 ==> Nomor Polis Kembar, Coba tranfer ulang...
	 * hasil=81 ==> Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap 
	 * hasil=82 ==> Produk Unit-Linked !!!~nBiaya Alokasi Investasi Belum Lengkap !!!
	 * */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		String lsNopol;
		int hasil = 0;
		boolean proSubmit=true;
		Akseptasi akseptasi=(Akseptasi)cmd;
		String desc=request.getParameter("txtmsps_desc");
		if(akseptasi.getProses().equals("1")){
			hasil=elionsManager.prosesAkseptasi(akseptasi,0,0,desc,err);
			if(hasil>0){
				if(hasil==51){
					err.reject("","No Polis Kosong , Silahkan Hubungi ITwebandmobile@sinarmasmsiglife.co.id");
					proSubmit=false;
				}else if(hasil==52){
					err.reject("","Nomor Polis Kembar, Coba tranfer ulang...");
					proSubmit=false;
				}else if(hasil==81){
					err.reject("","Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap ");
					proSubmit=false;
				}else if(hasil==82){
					err.reject("","Produk Unit-Linked !!!~nBiaya Alokasi Investasi Belum Lengkap !!!");
					proSubmit=false;
				}/*else if(hasil==83){
					err.reject("","Power SaveBelum Lengkap !!!Silahkan Tekan Tombol Investasi");
					proSubmit=false;
				}*/
			}else{
				List <Map> dataInbox = uwManager.selectMstInbox(akseptasi.getSpaj(),"3");
				if(dataInbox.size()>0){
					Policy policy = bacManager.selectMstPolicyAll(akseptasi.getSpaj());
					Pemegang pmg=elionsManager.selectpp(akseptasi.getSpaj());
					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							new String[]{"Kamarudinsyah@sinarmasmsiglife.co.id", "Ari@sinarmasmsiglife.co.id"}, 
							new String[]{"Ety@sinarmasmsiglife.co.id", "Anna@sinarmasmsiglife.co.id", "bas@sinarmasmsiglife.co.id"},
							null, 
							"AKSEPTASI POLIS BARU",
							"Telah dilakukan akseptasi polis baru untuk Blanko No "+policy.getMspo_no_blanko()+", Polis "+policy.getMspo_policy_no_format()+" an "+pmg.getMcl_first(),null, null);
				}
			}
		}else
			err.reject("",".");
		Policy policy=elionsManager.selectDw1Underwriting(akseptasi.getSpaj(),akseptasi.getLspdId(),akseptasi.getLstbId());
		return new ModelAndView("uw/akseptasi", err.getModel()).addObject("submitSuccess", ""+proSubmit).addObject("hasil",""+hasil).addObject("nopolis",policy.getMspo_policy_no()).addAllObjects(this.referenceData(request,cmd,err));
	}
	/**Fungsi : Untuk Melakukan proses Sample Underwritting
	 * @param BindException err,com.ekalife.elions.web.uw.model.Akseptasi
	 * @return boolean 
	 **/
	private boolean wf_sample_uw(BindException err,Akseptasi akseptasi)throws Exception {
		boolean lb_sample_uw = true, lb_batal = false, lb_okmedis = false;
		int pos=0;
		List ldsSample;
		String kode=props.getProperty("product.sample_uw");
		pos=kode.indexOf(akseptasi.getBisnisId());
		//proses>=100 adalah proses sample Underwering 
		if(pos <0){
			Date ldtTempDate;
			int thn,bln,row;
			Integer liBatal;
			Date ldtClosing,ldtLastClosing;
			liBatal=elionsManager.selectMstSampleUwStatusBatal(akseptasi.getSpaj(),akseptasi.getMsteInsured());
			if(liBatal==null)
				liBatal=new Integer(0);
			
			if(liBatal.intValue()==1){
				lb_okmedis=true;
				//hanya update status medis
				akseptasi.setProses("100");
				akseptasi.setMedical(new Integer(1));
				elionsManager.prosesAkseptasi(akseptasi,0,0,null,err);
				lb_batal=true;
			}else{
				ldtClosing=elionsManager.selectMstDefaultMsdefdate(new Integer(2));
				ldtLastClosing=elionsManager.selectFAddMonths(defaultDateFormatStripes.format(ldtClosing),-1);
				if(ldtLastClosing==null){
					err.reject("","Get End Is Not Working Properly ");
				}
				bln=Integer.parseInt(sdfMm.format(ldtLastClosing));
				thn=Integer.parseInt(sdfYy.format(ldtLastClosing));
				//
				List lsTempDate=elionsManager.selectLstSampleUwTglSample();
				if(!lsTempDate.isEmpty()){
					Integer count;
					count=elionsManager.selectCountLstSampleUw();
					if(count.intValue()<5){
						count=elionsManager.selectCountMstSampleUw();
						if(count.intValue()<=0){
							ldsSample=elionsManager.selectDSampleUwRegion(thn,bln,akseptasi.getLcaId());
							if(ldsSample.size()<=0){
			   					err.reject("","Polis ini terkena sample Underwriting ");
								lb_okmedis=true;									
			    				akseptasi.setProses("101");
			    				akseptasi.setMedical(new Integer(1));
			   					elionsManager.prosesAkseptasi(akseptasi,thn,bln,null,err);
							}
						}
					}
				}
			}
		}else
			lb_sample_uw=false;
		//
		if(lb_okmedis){
			if(lb_sample_uw){
				if(lb_batal){
				  	err.reject("","Nasabah ini pernah terkena sample Underwriting dan dibatalkan oleh tertanggung ! Status telah diubah menjadi medis");
				}else{
					err.reject("","Polis ini terpilih menjadi sample UW !Status telah diubah menjadi medis");
				}
			}
		}

		return lb_okmedis;
	}
	/**Fungsi : untuk mengecek apakah Premi dari suatu polis yang di pilih itu tidak kosong
	 * 			
	 * @param String spaj,Integer insuredNo
	 * @return boolean 
	 * 
	 * 
	 **/
	public boolean wf_check_premi(String spaj,Integer insuredNo){
		boolean lb_ret = true;
		Integer id;
		Double value;
		
		List list=elionsManager.selectd_mst_prod_ins(spaj,insuredNo);
		for(int i=0;i<list.size();i++){
			Product product=(Product)list.get(i);
			id=product.getLsbs_id();
			value=product.getMspr_premium();
			//
			if(value.doubleValue()<=0){
				if(id.intValue()>=810 || id.intValue()<=818){
					lb_ret=true;
				}else if(id.intValue()==904 || id.intValue()==905){
					lb_ret=true;
				}else{
					return false;
					//MessageBox('Info', 'Tidak Bisa transfer ada kesalahan premi.~nHubungi E.D.P !!!')
				}
			}
		}
		return lb_ret;
	}
}
