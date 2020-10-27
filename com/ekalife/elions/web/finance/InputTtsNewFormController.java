package com.ekalife.elions.web.finance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.tts.CaraBayar;
import com.ekalife.elions.model.tts.CommandTts;
import com.ekalife.elions.model.tts.PolicyTts;
import com.ekalife.elions.model.tts.Tahapan;
import com.ekalife.elions.model.tts.Tts;
import com.ekalife.utils.FCheck;
import com.ekalife.utils.FHit;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.parent.ParentFormController;

public class InputTtsNewFormController extends ParentFormController {

	SimpleDateFormat dd=new SimpleDateFormat("dd");
	SimpleDateFormat mm=new SimpleDateFormat("MM");
	SimpleDateFormat yyyy=new SimpleDateFormat("yyyy");
	DecimalFormat f3=new DecimalFormat("000");
	NumberFormat decRound2= new DecimalFormat("#.00;(#,##0.00)"); //
	NumberFormat decRound3= new DecimalFormat("#.000;(#,##0.000)"); //
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		List lsKurs=elionsManager.selectAllLstKurs();
		List<String> value=new ArrayList<String>();//(tunai,cek,giro,credit card,tahapan);
		value.add("1");
		value.add("2");
		//value.add("3");
		value.add("6");
		value.add("11");
		
		List lsCaraBayar=elionsManager.selectInLstPaymentType(value);
		map.put("lsKurs",lsKurs);
		map.put("lsCaraBayar",lsCaraBayar);
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandTts commandTts=new CommandTts();
		commandTts.setDesc(request.getParameter("desc"));
		commandTts.setS_tgl_setor("00/00/0000");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		//pembayaran
		List lsPembayaran=new ArrayList();
		int info=0;
		
		String proses=request.getParameter("proses");

		//
		if(proses.equals("1") || proses.equals("4")){//input atau batal
			CaraBayar caraBayar=new CaraBayar();
			caraBayar.setNo_urut(new Integer(1));
			caraBayar.setLsjb_id(new Integer(1));
			lsPembayaran.add(caraBayar);
			commandTts.setKd_cabang(currentUser.getLca_id());
			commandTts.setNama_admin(currentUser.getLus_full_name());
			commandTts.setNama_cabang(elionsManager.selectLstCabangNamaCabang(currentUser.getLca_id()));
			commandTts.setTahapan(new Tahapan());
			if(proses.equals("4")){//untuk reff nomor tts sebelummhya
				String alasanBatal=request.getParameter("alasan"); //alasan pembatalan
				String nomor=request.getParameter("nomor");
				nomor=nomor.substring(0,nomor.indexOf("~"));
				commandTts.setMstNoBatal(nomor);
				commandTts.setAlasanBatal(alasanBatal);
				List lsTts=elionsManager.selectAllMstTts(nomor,"1",null,currentUser.getLca_id());
				Tts tts=(Tts)lsTts.get(0);
				info=cek_userRight(tts,currentUser,1);
				if(tts.getMst_flag_batal()==1)
					info=3;
				else if(info==2)//klo sudah print bisa batal
					info=0;
				commandTts.setMst_no(nomor);
			}
			Date today=elionsManager.selectSysdate();
			commandTts.setTglRk(today);
			commandTts.setS_tgl_rk(defaultDateFormat.format(today));
		}else if(proses.equals("2") || proses.equals("3")){//edit dan edit tanggal setor
			String nomor=request.getParameter("nomor");
			nomor=nomor.substring(0,nomor.indexOf("~"));
			commandTts.setMst_no(nomor);
			List lsTts=elionsManager.selectAllMstTts(nomor,"1",null,currentUser.getLca_id());
			
			//cek untuk tahapan
			List lsPolisTahapan=elionsManager.selectMstPolicyTtsNopolis(nomor);
			if(lsPolisTahapan.size()==1){//klo input yang ada tahapan nya pasti polisnya satu
				String nopolis;
				nopolis=(String)lsPolisTahapan.get(0);
				commandTts.setTahapan(elionsManager.selectMstTahapan(nopolis.trim(),20));//lspdId 20=Konfirmasi Tahapan
			}
			Tts tts=new Tts();
			if(lsTts.isEmpty()==false)
				tts=(Tts)lsTts.get(0);
			if(proses.equals("2"))
				info=cek_userRight(tts,currentUser,0);
			else if(proses.equals("3")){//edit tanggal setor
				info=cek_userRight(tts,currentUser,1);
				//
				if(tts.getMst_tgl_setor()!=null){
					commandTts.setS_tgl_setor(defaultDateFormat.format(tts.getMst_tgl_setor()));
					info=4;	
				}
				commandTts.setEditTglSetor(1);
				
				
			}
			//
			List lsPolis=elionsManager.selectMstPolicyTts(nomor,null);
			lsPembayaran=elionsManager.selectMstCaraByr(nomor);
			//
			double totBayarRp=0,totBayarDlr=0,totTahapan=0,totHari=0;
				for(int i=0;i<lsPolis.size();i++){
					PolicyTts polTts=(PolicyTts)lsPolis.get(i);
					totBayarRp+=polTts.getMst_jumlah_byr_rp().doubleValue();
					totBayarDlr+=polTts.getMst_jumlah_byr_dlr().doubleValue();
					totHari+=polTts.getMst_jum_hari().doubleValue();
					totTahapan+=polTts.getMstah_jumlah();
				}
			//
			commandTts.setGtBayarRp(new Double(totBayarRp));
			commandTts.setGtBayarDlr(new Double(totBayarDlr));
			commandTts.setGtTahapan(new Double(totTahapan));
			commandTts.setGtJlhHari(new Double(totHari));
			commandTts.setKd_cabang(tts.getLst_kd_cab());
			commandTts.setNama_admin(tts.getLus_full_name());
			commandTts.setNama_cabang(tts.getLca_nama());
			commandTts.setNama(tts.getMst_nm_pemegang());
			commandTts.setLsPolis(lsPolis);
			commandTts.setKeterangan(tts.getMst_ket());
			commandTts.setTglRk(tts.getMst_tgl_rk());
			commandTts.setNoTelp(tts.getMst_no_telp());
			if(tts.getMst_tgl_rk()!=null){
				commandTts.setS_tgl_rk(defaultDateFormat.format(tts.getMst_tgl_rk()));
			}	
			//
			
			
		}
		commandTts.setInfo(new Integer(info));
		commandTts.setProses(Integer.valueOf(proses));
		commandTts.setLsPembayaran(lsPembayaran);
		commandTts.setSize(new Integer(lsPembayaran.size()));
		return commandTts;
	}
	
	private int cek_userRight(Tts tts, User currentUser,Integer n) {
		int info=0;
		if( (tts.getLst_kd_cab().equals(currentUser.getLca_id())) &&
				(Integer.parseInt(currentUser.getLus_id())==tts.getLus_id()))
				info=0;
		else
			info=1;
		
		if(n==1){//untuk edit tanggal setor, batal
			if(tts.getMst_flag_batal()==1)
				info=3;
		}else{//cetak,edit tts
			if (tts.getFlag_print().intValue()==1)//jika sudah print gak bisa edit
				info=2;
			else if(tts.getMst_flag_batal()==1)
				info=3;
		}	
		return info;
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandTts commandTts=(CommandTts)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String flag=commandTts.getFlag();
		String nopolis=request.getParameter("nopolis");
		int bilSize=0;
		int limit=500;
		Integer lsbsId = null;
		String lkuId;
		List lsPolis=new ArrayList(); //maximal 7 (kuseno)
		List lsPolisOld=new ArrayList();
		JasperScriptlet jasper=new JasperScriptlet();
		lsPolisOld=commandTts.getLsPolis();
		
		if(commandTts.getProses().intValue()==1||commandTts.getProses().intValue()==2||commandTts.getProses().intValue()==4){//klo edit biasa harus cek tanggal rk atau tanggal bayar
			String tglRk=request.getParameter("tanggalrk");
			if(! tglRk.equals("00/00/0000")){
				Calendar calRk=new GregorianCalendar(Integer.parseInt(tglRk.substring(6)),
										Integer.parseInt(tglRk.substring(3,5))-1,
										Integer.parseInt(tglRk.substring(0,2)));;
				commandTts.setTglRk(calRk.getTime());
				commandTts.setS_tgl_rk(defaultDateFormat.format(calRk.getTime()));
			}else
				err.reject("","Silahkan Masukan Tanggal Bayar yang benar");
		}else if(commandTts.getProses().intValue()==3){//klo edit tanggal setor
			String tglSetor=request.getParameter("tanggalSetor");
			Date ldTglSetor=elionsManager.selectMstTtsTglSetor(commandTts.getMst_no());
			if(ldTglSetor!=null){
				err.reject("","Tanggal Setor Telah Di input Tidak Boleh Di Edit Ulang");
			}else{
				if(! tglSetor.equals("00/00/0000")){
					Calendar calSetor=new GregorianCalendar(Integer.parseInt(tglSetor.substring(6)),
											Integer.parseInt(tglSetor.substring(3,5))-1,
											Integer.parseInt(tglSetor.substring(0,2)));;
					commandTts.setTglSetor(calSetor.getTime());
					commandTts.setS_tgl_setor(defaultDateFormat.format(calSetor.getTime()));
				}else{
					err.reject("","Silahkan Masukan Tanggal setor yang benar");
				}
			}	
		}
		
		int count = 0;
		if(lsPolisOld!=null)
			count=lsPolisOld.size();
		//
		Map MapKurs=elionsManager.selectTodayKurs("02", elionsManager.selectSysdate());
		Double kursJual=null;
		String infoKurs="";
		if(MapKurs!=null){		
			kursJual= (Double)MapKurs.get("LKH_KURS_JUAL");  //kurs jual\
			infoKurs="\nKurs Dolar saat ini "+jasper.format2Digit(new BigDecimal(kursJual));
		}else
			err.reject("","Kurs Harian Belum  di input, silahkan hubungi Dept ITwebandmobile@sinarmasmsiglife.co.id");
		
		//
		if(flag.equals("1")){//add polis
			
			commandTts.setTahapan(elionsManager.selectMstTahapan(nopolis.trim(),20));//lspdId 20=Konfirmasi Tahapan
			List lsbillingDetail=elionsManager.selectAllBillingNotPaidNew(nopolis.trim());
			String mcl_first = null;
			Double stamp = new Double(0);
			String lkuIdTahapan="";
			if(lsbillingDetail.isEmpty()==true){
				List lsPolTemp=commandTts.getLsPolis();
				if(lsPolTemp!=null){
					Map map=selectPolis(lsPolTemp, request,lsPolTemp.size());
					lsPolTemp=(List)map.get("lsPolis");
					Integer byk=(Integer)map.get("byk");
					commandTts.setLsPolis(lsPolTemp);
				}	
				err.reject("tts.billing");
			}else{
				lsPolis=commandTts.getLsPolis();
				if(lsPolis==null)
					lsPolis=new ArrayList();
				for(int i=0;i<lsbillingDetail.size();i++){
					PolicyTts policyTts=(PolicyTts)lsbillingDetail.get(i);
					mcl_first=policyTts.getMcl_first();
					stamp=policyTts.getMsbi_stamp();
					lsPolis.add(policyTts);
				}	
				//
				Date ldtBegDate,ldtNextBill,ldtBayar,ldtDueDate;
				Date ldTemp,ldtEndBill;
				double ldecDisc;
				Integer liPmode,liPayPeriod,liThKe,liPremiKe = null;
				String reg_spaj;
				Double ldecPremi,ldecHamil,ldecPct = new Double(0);
				double ldecDiscount;
				
				Map mapPolicy=elionsManager.selectMstPolicy(nopolis.trim());
				if(mapPolicy==null){
					mapPolicy=new HashMap();
				}
				reg_spaj=(String)mapPolicy.get("REG_SPAJ");
				lkuId=(String)mapPolicy.get("LKU_ID");
				ldtBegDate=(Date)mapPolicy.get("MSTE_BEG_DATE");
				ldtNextBill=(Date)mapPolicy.get("MSPO_NEXT_BILL");
				liPmode=(Integer)mapPolicy.get("LSCB_ID");
				liPayPeriod=(Integer)mapPolicy.get("MSPO_PAY_PERIOD");
				//
				List lsProdIns=elionsManager.selectMstProductInsured(reg_spaj);
				//
				Product prodIns=(Product)lsProdIns.get(0);
				lsbsId=prodIns.getLsbs_id();
				if("079, 080, 091, 092".indexOf(f3.format(lsbsId.intValue()))>0){
						//ldec_bunga = 0.05 // fharian
						ldecPct= new Double(18);
				}else{
					ldecPct=elionsManager.selectLstBungaLsbunBungaTts(new Integer(4),lkuId);//4==bunga tunggakan
					if(ldecPct==null)
						ldecPct=new Double(0);
				}
					
				ldtBayar=elionsManager.selectSysdate();
				if(ldtBayar.compareTo(ldtNextBill)>=0){
					Integer liMonth=elionsManager.selectLstPayModeLscbTtlMonth(liPmode);
					Integer liBisnisId;
					Integer liBisnisNo;
					do{
						if(Integer.parseInt(dd.format(ldtNextBill))!=Integer.parseInt(dd.format(ldtBegDate))){
							ldtNextBill = FCheck.getEndBill(ldtNextBill, ldtBegDate, liPayPeriod.intValue()* 12);
						}
						//
						//cek sini
						Date ldTempTemp=FormatDate.add(ldtNextBill,Calendar.MONTH,liMonth.intValue());
						ldTemp=FormatDate.add(ldTempTemp,Calendar.DATE,-1);
						ldtEndBill= ldTemp;
						ldtEndBill=FCheck.getEndAktif(ldtEndBill,ldtBegDate);
						//
						ldecPremi = new Double(0);
						ldecDiscount=0;
						ldecHamil=new Double(0);
						Double ldecdiscPlan=new Double(0);
						
						liThKe =FHit.getTahunKe(ldtNextBill,ldtBegDate,liPayPeriod);// f_hit_tahun_ke(ldt_next_bill, ldt_beg_date, li_pperiod)
						liPremiKe= FHit.getPremiKe(ldtEndBill, ldtBegDate, liMonth);
						//
						String polLkuId = null;
						for(int i=0;i<lsProdIns.size();i++){
							Product product=(Product)lsProdIns.get(i);
							liBisnisId=product.getLsbs_id();
							liBisnisNo=product.getLsdbs_number();
							if(liThKe.intValue() > 1 && liBisnisId.intValue()== 904 && liBisnisNo.intValue()== 1){
								ldecHamil= product.getMspr_premium();
								continue;
							}
							//Discount rider = discount premi basic
							if(liBisnisId.intValue()< 300){
								ldecdiscPlan=elionsManager.selectGetDiscountPlan(liBisnisId,liBisnisNo,liThKe);
								if(ldecdiscPlan==null)
									ldecdiscPlan=new Double(0);
							}
							//Discount rider hanya untuk WPD & PC (kata si Ayti) 19/12/2000
							ldecDisc= 0;
							if( liBisnisId.intValue()== 804 || liBisnisId.intValue() == 802 || liBisnisId.intValue()< 300 )
								ldecDisc= product.getMspr_premium().doubleValue()*ldecdiscPlan.doubleValue()/100;
							//
							ldecPremi =new Double(ldecPremi.doubleValue()+product.getMspr_premium().doubleValue());
							String temp=decRound2.format(ldecPremi.doubleValue());
							ldecPremi=Double.valueOf(temp);
							ldecDiscount+= ldecDisc;
							polLkuId=product.getLku_id();
							
						}
						ldecPremi=new Double(ldecPremi.doubleValue()-ldecHamil.doubleValue());
						//
						PolicyTts policyTts=new PolicyTts();
						policyTts.setMcl_first(mcl_first);
						policyTts.setTahun_ke(liThKe);
						policyTts.setPremi_ke(liPremiKe);
						policyTts.setBeg_date(ldtNextBill);
						policyTts.setEnd_date(ldtEndBill);
						//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
						Calendar calTemp=new GregorianCalendar(2006,06-1,1);
						if(ldtBegDate.compareTo(calTemp.getTime()) >0){
							ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
						}else
							ldtDueDate = elionsManager.selectAddMonths(defaultDateFormat.format(ldtBegDate),new Integer(1));
						if(ldtDueDate==null)
							err.reject("","Get End-Date Is Not Working Properly ");
						
						policyTts.setDue_date(ldtDueDate);
						policyTts.setMst_premium(ldecPremi);
						policyTts.setMst_discount(new Double(ldecDiscount));
						policyTts.setMst_no_polis(nopolis);
						policyTts.setMstah_jumlah(new Double(0));
						policyTts.setMsbi_stamp(stamp);
						policyTts.setLku_id(polLkuId);
						//due date
						lsPolis.add(policyTts);
						//
						ldtNextBill=FCheck.getEndBill(ldtEndBill,ldtBegDate,liPayPeriod.intValue()*12);
						
						
					}while((ldtNextBill.compareTo(ldtBayar)<0) || ldtNextBill==null );
				}
				//
				Date ldTgl;
				double liHari;
				int pos=0;
				double liPersen,ldecBunga = 0,ldecBungaTemp=0;
				double ldecPremiMin;
				String sBisnisId="074, 076, 096, 099, 135, 136"; //product multi invest
				String kode=f3.format(lsbsId.intValue());
				pos=kode.indexOf(sBisnisId);
				List lsPolisNew=new ArrayList();
				
				for(int i=count;i<lsPolis.size();i++){
					PolicyTts policyTts=(PolicyTts)lsPolis.get(i);
					//
					//ldTgl=policyTts.getDue_date();
					ldTgl=policyTts.getBeg_date();
					ldecPremi= new Double(policyTts.getMst_premium().doubleValue()-policyTts.getMst_discount().doubleValue());
					//jk MultiInvest,yg ditampilkan premi minimum
					if(pos>0){ 
						liThKe= policyTts.getTahun_ke();
						if(liThKe.intValue()==1)
							liPersen= 1;				 
						else if(liThKe.intValue()== 2) 
							liPersen= 0.2;
						else if(liThKe.intValue()== 3)
							liPersen = 0.1;
						else if(liThKe.intValue()== 4)
							liPersen= 0.05;					
						else
							liPersen= 0;								
		   			ldecPremiMin= ldecPremi.doubleValue()* liPersen;
		   			if(liThKe.intValue()> 1 )
		   				ldecPremiMin=1;			
		   				ldecPremi= new Double(ldecPremiMin);
					}
					//
					liHari= FormatDate.dateDifference(ldTgl,ldtBayar,false)+1;
					if(liHari< 0 )
						liHari= 0; 
					//
					Calendar calFlag1=new GregorianCalendar(2003,7,13);//bulan telah di minus 1 (13/08/2003)
					Calendar calFlag2=new GregorianCalendar(2003,9,31);//bulan telah di minus 1 (31/10/2003)
					Calendar calFlag3=new GregorianCalendar(2005,7,31);//bulan telah di minus 1 (31/08/2005)
					if(ldtBayar.compareTo(calFlag1.getTime())<=0)
						ldecBunga= (liHari/365) * ldecPremi.doubleValue()* ldecPct.doubleValue()/ 100;
					else if ( (ldtBayar.compareTo(calFlag1.getTime())>=0) && 
							  (ldtBayar.compareTo(calFlag2.getTime())<=0))
						ldecBunga= liHari* ldecPct.doubleValue()/ 100 * ldecPremi.doubleValue();
					else if( (ldtBayar.compareTo(calFlag2.getTime())>0) && 
							 (ldtBayar.compareTo(calFlag3.getTime())<=0) ) {
						if(lkuId.equals("02")){
							ldecPct= new Double(0.085);
							if(liHari <= 60 ){
								ldecBunga= liHari* ldecPct.doubleValue()/ 100 * ldecPremi.doubleValue();
								//em_2.Text = string(ldec_pct)
							}else{
								ldecBunga= ( 60 * ldecPct.doubleValue()/ 100 * ldecPremi.doubleValue()) + 
											((liHari- 60) * 0.05 / 100 * ldecPremi.doubleValue());//IM No. 083/IM-DIR/X/03
								//em_2.Text = '0.05'
							}
						}else{
							ldecPct = new Double( 0.1);
							ldecBunga= liHari* ldecPct.doubleValue()/ 100 * ldecPremi.doubleValue();
							//em_2.Text = string(ldec_pct)
						}		
					}else{
						ldecBunga = (liHari/360) * (ldecPct.doubleValue()/100) * ldecPremi.doubleValue();
						ldecBunga=Double.parseDouble(decRound3.format(ldecBunga));
					}
					ldecBungaTemp=Double.parseDouble(decRound2.format(ldecBunga));
					policyTts.setMst_kurs(lkuId);
					//
					double total=(ldecPremi+ldecBungaTemp+policyTts.getMsbi_stamp())-policyTts.getMst_discount();
					if(policyTts.getMst_kurs().equals("02"))
						total=total*kursJual;
					
					total=Double.parseDouble(decRound2.format(total));
					if(lkuId.equals("01")){
						policyTts.setLku_symbol("Rp.");
						policyTts.setMst_jumlah_byr_rp(new Double(0));
						policyTts.setMst_jumlah_byr_dlr(new Double(0));
					}else if(lkuId.equals("02")){
						policyTts.setLku_symbol("US$.");
						policyTts.setMst_jumlah_byr_dlr(new Double(0));
						policyTts.setMst_jumlah_byr_rp(new Double(0));
					}
				
					policyTts.setMst_jumlah(new Double(total));
					policyTts.setLsbun_bunga(new Double(ldecBungaTemp ));
					policyTts.setMst_jum_hari(new Integer((int)liHari));
					policyTts.setNo_urut(new Integer(i+1));
					policyTts.setMst_no(commandTts.getMst_no());
					lsPolisNew.add(policyTts);
					
				}
				//
				Map map=selectPolis(lsPolisNew, request,count);
				if(lsPolisOld!=null)
					lsPolisNew=lsPolisOld;
				
				Integer byk=(Integer)map.get("byk");
				if(byk.intValue()>7)
					err.reject("tts.maxPolis");
				//
				double totBayarRp=0,totBayarDlr=0,totHari=0;
				if(lsPolisNew!=null)
				for(int i=0;i<lsPolisNew.size();i++){
					PolicyTts polTts=(PolicyTts)lsPolisNew.get(i);
					totBayarRp+=polTts.getMst_jumlah_byr_rp().doubleValue();
					totBayarDlr+=polTts.getMst_jumlah_byr_dlr().doubleValue();

					totHari+=polTts.getMst_jum_hari().doubleValue();
				}
					
				commandTts.setGtBayarRp(new Double(totBayarRp));
				commandTts.setGtBayarDlr(new Double(totBayarDlr));
				commandTts.setGtJlhHari(new Double(totHari));
				commandTts.setLsPolis(lsPolisNew);
				commandTts.setSize2(new Integer(lsPolisNew.size()));
				
				err.reject("tts.edit");
			}
		}else if(flag.equals("2")){//cara pembayaran
			String bykBayar=request.getParameter("bykbayar");
			int len;
			if(bykBayar==null)
				len=1;
			else
				len=Integer.parseInt(bykBayar);
			//
			List lsPembayaran=new ArrayList();
			CaraBayar caraBayar;
			for (int i=0;i<len;i++){
				caraBayar=new CaraBayar();
				caraBayar.setNo_urut(new Integer(i+1));
				caraBayar.setLsjb_id(new Integer(1));
				lsPembayaran.add(caraBayar);
			}
			err.reject("tts.jumBayar");
			
			commandTts.setLsPembayaran(lsPembayaran);
			commandTts.setSize(new Integer(lsPembayaran.size()));
		}else if(flag.equals("3")){//simpan
			boolean thpn=false,cb=false;
			double totTahapan=0;
			
			List lsPembayaran=commandTts.getLsPembayaran();
			if(commandTts.getLsPolis()==null)
				err.reject("tts.polis");
			//
			if(commandTts.getNama()==null || commandTts.getNama().trim().equals("")){
				err.reject("tts.nama");
			}
			if(commandTts.getNoTelp()==null || commandTts.getNoTelp().trim().equals(""))
				err.reject("tts.noTelp");
			List lsPolisNew=commandTts.getLsPolis();
			Map map=selectPolis(lsPolisNew, request,lsPolisNew.size());
			lsPolisNew=(List)map.get("lsPolis");
			commandTts.setLsPolis(lsPolisNew);
			//
			Integer byk=(Integer)map.get("byk");
			if(byk.intValue()>7)
				err.reject("tts.maxPolis");
			if(byk.intValue()==0)
				err.reject("tts.minPolis");
			/**untuk perhitungan semua nya di conver jadi rupiah dahulu*/
//			cek tahapan dan cara bayar nya
			String infoBayar="";
			Double jumByrDlr=new Double(0);
			Double jumTahapan=new Double(0);
			Double totKurang=new Double(0);
			for(int i=0;i<commandTts.getLsPolis().size();i++){
				PolicyTts policyTts=(PolicyTts)commandTts.getLsPolis().get(i);
					
				if(policyTts.getPil().equals("1"))
					totTahapan+=policyTts.getMstah_jumlah();
				if(policyTts.getMstah_jumlah()>0)
					thpn=true;
				//
				if(policyTts.getMst_jumlah_byr_dlr()>0){
					jumByrDlr=policyTts.getMst_jumlah_byr_dlr()*kursJual;
				}
				if(policyTts.getLku_id_tahapan()!=null)
					if(policyTts.getLku_id_tahapan().equals("02"))
						jumTahapan=policyTts.getMstah_jumlah()*kursJual;
					else
						jumTahapan=totTahapan;
				if(policyTts.getPil().equals("1")){
					Double kurang=(policyTts.getMst_jumlah()- (policyTts.getMst_jumlah_byr_rp()+jumByrDlr+jumTahapan));
					totKurang=totKurang+kurang;
				}	
			}
			if(totKurang>0)
				infoBayar=infoBayar+"\nAda Kekurangan Pembayaran TTS sebesar Rp."+jasper.format2Digit(new BigDecimal(totKurang))+" Batas Toleransi=Rp. "+limit+"\n"+infoKurs;

			commandTts.setLimit(infoBayar);
			if(totKurang>limit)
				err.reject("",infoBayar);

			for(int i=0;i<commandTts.getLsPembayaran().size();i++){
				CaraBayar caraBayar=(CaraBayar)commandTts.getLsPembayaran().get(i);
				if(caraBayar.getLsjb_id()==11){
					cb=true;
					break;
				}
			}
			if( (thpn && cb)){
				if(totTahapan>commandTts.getTahapan().getMstah_jumlah()){
					err.reject("","Jumlah Tahapan < dari jumlah yang akan di bayar");
				}else if(totTahapan<commandTts.getTahapan().getMstah_jumlah())
					err.reject("","Premi > Jumlah Tahapan, Maka Jumlah Potongan Tahapan harus di input semua");
			}else{	
				if(thpn || cb)
					err.reject("","Untuk Pembayaran Tahapan Silahkan Pilih Cara Bayar Tahapan atau masukan jumlah potongan tahapan");
			}
			
			/*boolean thpn=false,cb=false;
			double totTahapan=0;
//			cek tahapan dan cara bayar nya
			String infoBayar="";
			Double jumByrDlr=new Double(0);
			Double jumTahapan=new Double(0);
			Double totKurang=new Double(0);
			for(int i=0;i<commandTts.getLsPolis().size();i++){
				PolicyTts policyTts=(PolicyTts)commandTts.getLsPolis().get(i);
				if(policyTts.getMst_kurs().equals("02"))
					policyTts.setMst_jumlah(policyTts.getMst_jumlah()*kursJual);
					
				if(policyTts.getPil().equals("1"))
					totTahapan+=policyTts.getMstah_jumlah();
				if(policyTts.getMstah_jumlah()>0)
					thpn=true;
				//
				if(policyTts.getMst_jumlah_byr_dlr()>0){
					jumByrDlr=policyTts.getMst_jumlah_byr_dlr()*kursJual;
				}
				if(policyTts.getLku_id_tahapan()!=null)
					if(policyTts.getLku_id_tahapan().equals("02"))
						jumTahapan=policyTts.getMstah_jumlah()*kursJual;
					else
						jumTahapan=totTahapan;
				if(policyTts.getPil().equals("1")){
					Double kurang=(policyTts.getMst_jumlah()- (policyTts.getMst_jumlah_byr_rp()+jumByrDlr+jumTahapan));
					logger.info(policyTts.getMst_jumlah());
					logger.info(policyTts.getMst_jumlah_byr_rp());
					logger.info(jumByrDlr);
					logger.info(jumTahapan);
					totKurang=totKurang+kurang;
				}	
			}
			if(totKurang>0)
				infoBayar=infoBayar+"\nAda Kekurangan Pembayaran TTS sebesar Rp."+jasper.format2Digit(new BigDecimal(totKurang))+" Batas Toleransi=Rp. "+limit+"\n"+infoKurs;
//			else
//				infoBayar=infoBayar+"\nAda Kelebihan Pembayaran TTS sebesar "+kurs+" "+jasper.format2Digit(new BigDecimal(totKurang))+" Batas Toleransi=Rp. "+limit+"\n"+infoKurs;
			commandTts.setLimit(infoBayar);
			if(totKurang>limit)
				err.reject("",infoBayar);

			
			for(int i=0;i<commandTts.getLsPembayaran().size();i++){
				CaraBayar caraBayar=(CaraBayar)commandTts.getLsPembayaran().get(i);
				if(caraBayar.getLsjb_id()==11){
					cb=true;
					break;
				}
			}
			if( (thpn && cb)){
				if(totTahapan>commandTts.getTahapan().getMstah_jumlah()){
					err.reject("","Jumlah Tahapan < dari jumlah yang akan di bayar");
				}else if(totTahapan<commandTts.getTahapan().getMstah_jumlah())
					err.reject("","Premi > Jumlah Tahapan, Maka Jumlah Potongan Tahapan harus di input semua");
			}else{	
				if(thpn || cb)
					err.reject("","Untuk Pembayaran Tahapan Silahkan Pilih Cara Bayar Tahapan atau masukan jumlah potongan tahapan");
			}*/
		}
		double totBayarRp=0,totBayarDlr=0,totHari=0,totTahapan=0;
		if(commandTts.getLsPolis()!=null)
		for(int i=0;i<commandTts.getLsPolis().size();i++){
			PolicyTts polTts=(PolicyTts)commandTts.getLsPolis().get(i);
			if(polTts.getPil()!=null)
			if(polTts.getPil().equals("1")){
				totBayarRp+=polTts.getMst_jumlah_byr_rp().doubleValue();
				totBayarDlr+=polTts.getMst_jumlah_byr_dlr().doubleValue();
				totTahapan+=polTts.getMstah_jumlah().doubleValue();
			}
			totHari+=polTts.getMst_jum_hari().doubleValue();
			commandTts.setGtBayarRp(new Double(totBayarRp));
			commandTts.setGtBayarDlr(new Double(totBayarDlr));
			commandTts.setGtJlhHari(new Double(totHari));
			commandTts.setGtTahapan(new Double(totTahapan));
		}
		//cek pembayaran
		for(int i=0;i<commandTts.getLsPembayaran().size();i++){
			CaraBayar caraBayar=(CaraBayar)commandTts.getLsPembayaran().get(i);
			caraBayar.setS_tgl_jth_tempo(request.getParameter("tgl_jth_tempo"+i));
			if(caraBayar.getLsjb_id()==6){
				if(caraBayar.getS_tgl_jth_tempo().equals("00/00/0000") || caraBayar.getS_tgl_jth_tempo().trim().equals(""))
					err.reject("","Silahkan Isi tanggal Jatuh Tempo Giro Dengan Benar");
				else{
					Calendar calJthTmp=new GregorianCalendar(Integer.parseInt(caraBayar.getS_tgl_jth_tempo().substring(6)),
							Integer.parseInt(caraBayar.getS_tgl_jth_tempo().substring(3,5))-1,
							Integer.parseInt(caraBayar.getS_tgl_jth_tempo().substring(0,2)));;
					caraBayar.setTgl_jth_tempo(calJthTmp.getTime());
				}
			}
				
		}
		if(commandTts.getGtJlhHari()!=null)
		if(commandTts.getGtJlhHari()>90)
			err.reject("","Polis Reinstate Tidak Bisa Dilanjutkan Keterlambatan > 90");
	
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandTts commandTts=(CommandTts)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String nomor=null;
		Integer suc=null;
		List lsPolisNew=commandTts.getLsPolis();
		Map map=selectPolis(lsPolisNew, request,lsPolisNew.size());
		lsPolisNew=(List)map.get("lsPolis");
		
		commandTts.setLsPolis(lsPolisNew);
		if(commandTts.getProses().intValue()==1 || commandTts.getProses().intValue()==4){//input atau batal
			suc=new Integer(1);
			if(commandTts.getProses().intValue()==1)
				commandTts.setDesc("Input TTS");
			else
				commandTts.setDesc("Input TTS Dengan Alasan Batal: " +commandTts.getAlasanBatal());
			
			nomor=elionsManager.inputTtsNew(commandTts,currentUser.getLus_id());

			
		}else if(commandTts.getProses().intValue()==2){//edit
			suc=new Integer(2);
			nomor=elionsManager.inputTtsNew(commandTts,currentUser.getLus_id());
		}else if(commandTts.getProses().intValue()==3){//edit tgl setor
			suc=new Integer(3);
			commandTts.setDesc("Edit Tgl Setor");
			nomor=commandTts.getMst_no();
			elionsManager.editTglSetor(commandTts,currentUser.getLus_id());
			//String[] to={props.getProperty("email.report_tts")};
			String[] to={props.getProperty("admin.yusuf")};
			String subject="Setor TTS Online ke Bank";
			String message="Informasi: Untuk no tts :"+commandTts.getMst_no()+" telah di setor ke Bank pada tanggal "+commandTts.getS_tgl_setor();
			//email.send(to, null,subject, message, currentUser);
		}
		
		err.reject("",commandTts.getLimit());
		return new ModelAndView("finance/input_ttsNew", err.getModel()).addObject("submitSuccess", suc).addObject("nomor",nomor).addAllObjects(this.referenceData(request,cmd,err));
			
	}
	
	public Map selectPolis(List lsPolisNew,HttpServletRequest request,int count){
		Map<String, Object> map=new HashMap<String, Object>();
		String[] pil ;
		String pil2;
		if(lsPolisNew!=null)
			pil=new String[lsPolisNew.size()];
		else
			pil=new String[0];
		
		int byk=0;
		
		pil=request.getParameterValues("pil");
		pil2=request.getParameter("pil");
		if(pil!=null && count==lsPolisNew.size())
			for(int i=0;i<lsPolisNew.size();i++ ){
				PolicyTts polTts=(PolicyTts)lsPolisNew.get(i);
				if(pil[i].equals("1")){
					polTts.setPil("1");
					byk++;
				}else
					polTts.setPil("0");
				lsPolisNew.set(i, polTts);
		}else{
			if(pil2!=null)
			if(pil2.equals("1")){
				PolicyTts polTts=(PolicyTts)lsPolisNew.get(0);
				polTts.setPil("1");
				lsPolisNew.set(0,polTts);
				byk=1;
			}
		}
		map.put("lsPolis", lsPolisNew);
		map.put("byk", new Integer(byk));
		
		return map;

	}
	
}
