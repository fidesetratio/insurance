/**
 * Class Khusus set Data Upload Format SIO(Cerdas & Dana Sejahtera)
 * Untuk Set Data Peserta Yg diset Hanya tertanggung utama(baru untuk Rider Kesehatan baru Smile Medical Saja) Karena Cerdas hanya bisa mengambil Satu rider Smile Medical untuk ttg utama
 * Untuk Premi Produk Utama hanya mengambil data dari file upload tidak melakukan perhitungan 
 * Untuk Produk utama link(Cerdas) premi rider diset 0 sedangkan untuk premi rider non link(Dana Sejahtera)system melakukan perhitungan
 * 
 **/



package com.ekalife.elions.process.upload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Biayainvestasi;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.SuratUnitLink;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;

import produk_asuransi.n_prod;
	
/**
 * @author lufi
 *
 */
public class UploadSetDataTemp  implements Serializable {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private static final long serialVersionUID = -7558103290098002329L;
//	private BacManager bacManager;	
//	public void setBacManager(BacManager bacManager) {
//		this.bacManager = bacManager;
//	}
	private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
//	private SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMyyyy");
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//	private BacDao bacDao = new BacDao();
//	private UwDao uwDao = new UwDao();
//	private SuratUnitLink suratUnitLink = new SuratUnitLink();
	private Calendar tgl_sekarang = Calendar.getInstance(); 
	private Integer fmYearSpaj=new Integer(tgl_sekarang.get(Calendar.YEAR));
	private Integer tanggal=new Integer(0);
	private Integer bulan=new Integer(0);
	private Integer tahun=new Integer(0);
	private Integer tanggalEd=new Integer(0);
	private Integer bulanEd=new Integer(0);
	private Integer tahunEd=new Integer(0);
	private Integer tanggal1=new Integer(0);
	private Integer bulan1=new Integer(0);
	private Integer tahun1=new Integer(0);

	
	ValidateUploadTM validatorTM=new ValidateUploadTM();
	
	

	public HashMap prosesSetDataPemegangSIO(List spaj, BacDao bacDao, UwDao uwDao, User user) {
		HashMap<String, Serializable> mapPemegang = new HashMap<String, Serializable>();
		Pemegang pemegang =  new Pemegang();		
		String err = "";
//		BacDao bac_da
		try{
			String mcl_idPp= getMclId(1,fmYearSpaj,bacDao);
			Date bdate = df.parse(spaj.get(187).toString());			
			pemegang.setMcl_id(mcl_idPp);
			pemegang.setMcl_jenis(0);
			pemegang.setCampaign_id(Integer.parseInt(spaj.get(0).toString().replace(".0", "")));//campaign id
			pemegang.setMspo_flag_spaj(Integer.parseInt(spaj.get(1).toString().replace(".0", "")));//flag spaj SIO=4		
			pemegang.setMspo_nasabah_dcif(spaj.get(2).toString());//no cif nasabah
			//no va belum jelas
			//pemegang.setMspo_no_blanko(spajExcelList.get(2).toString());		
			pemegang.setMcl_first(spaj.get(5).toString());//nama lengkap pemegang
			pemegang.setMspe_mother(spaj.get(6).toString());//nama ibu kandung pemegang
			pemegang.setLsne_id(Integer.parseInt(spaj.get(7).toString().replace(".0", "")));//id negara pemegang
			pemegang.setMcl_green_card(spaj.get(8).toString().replace(".0", ""));//green card pemegang
			pemegang.setLside_id(Integer.parseInt(spaj.get(9).toString().replace(".0", "")));//jenis kartu identitas
			//pemegang.setMcl_gelar(spajExcelList.get(6).toString());
			pemegang.setMspe_no_identity(spaj.get(10).toString().replace(".0", ""));//no kartu identitas
			String expired_date_idPP=spaj.get(11).toString();
			if(expired_date_idPP!=null && !expired_date_idPP.equals("") )pemegang.setMspe_no_identity_expired(df.parse(expired_date_idPP));//tanggal expired kartu identitas			
			pemegang.setMspe_sex(Integer.parseInt(spaj.get(12).toString().substring(0, 1)));//jenis kelamin pemegang
			pemegang.setMspe_sts_mrt(spaj.get(13).toString().replace(".0", ""));//status pernikahan pemegang
			pemegang.setMspe_date_birth(df.parse(spaj.get(14).toString()));//tanggal lahir pemegang
			pemegang.setMspe_place_birth(spaj.get(15).toString());//kota lahir pemegang	
			pemegang.setLsag_id(Integer.parseInt(spaj.get(17).toString().replace(".0", "")));//agama pemegang
			pemegang.setMcl_company_name(spaj.get(18).toString());//perusahaan tempat bekerja
			pemegang.setLus_id(Integer.parseInt(user.getLus_id()));
			String namaKerjaan = bacDao.selectKeteranganKerja(spaj.get(19).toString().replace(".0", ""));
			pemegang.setMkl_kerja(namaKerjaan);//id pekerjaan		
			pemegang.setMkl_kerja_ket(spaj.get(20).toString());//uraian tugas
			pemegang.setAlamat_kantor(spaj.get(21).toString());//alamat kantor pemegang
			pemegang.setKota_kantor(spaj.get(22).toString());//kota kantor pemegang
			pemegang.setKd_pos_kantor(spaj.get(23).toString().replace(".0", ""));//kode pos kantor
			pemegang.setAlamat_rumah(spaj.get(24).toString());//alamat rumah
			pemegang.setKota_rumah(spaj.get(25).toString());//kota rumah
			pemegang.setKd_pos_rumah(spaj.get(26).toString().replace(".0", ""));//kode pos rumah
			pemegang.setAlamat_tpt_tinggal(spaj.get(27).toString());//alamat tempat tinggal pemegang
			pemegang.setKota_tpt_tinggal(spaj.get(28).toString());//kota tempat tinggal
			pemegang.setKd_pos_tpt_tinggal(spaj.get(29).toString().replace(".0", ""));//kode pos tempat tinggal
			pemegang.setArea_code_rumah(spaj.get(30).toString().replace(".0", ""));//kode area telepon rumah pemegang
			pemegang.setTelpon_rumah(spaj.get(31).toString().replace(".0", ""));//no tlp rumah pemegang
			pemegang.setArea_code_kantor(spaj.get(32).toString().replace(".0", ""));//area kode kantor pemegang
			pemegang.setTelpon_kantor(spaj.get(33).toString().replace(".0",""));//no telepon kantor
			pemegang.setNo_hp(spaj.get(34).toString().replace(".0", ""));//no hp pemegang
			pemegang.setEmail(spaj.get(35).toString());//email pemegang
			pemegang.setMcl_npwp(spaj.get(36).toString().replace(".0", ""));//no npwp pemegang polis
			pemegang.setLsre_id(Integer.parseInt(spaj.get(37).toString().replace(".0", "")));//hubungan pp dengan ttg
			pemegang.setMkl_penghasilan(spaj.get(38).toString());//total pendapatan
			pemegang.setMspo_korespondensi("0");
			pemegang.setMcl_jenis(0);
			pemegang.setMspo_jenis_terbit(0);
			if(pemegang.getMspo_flag_spaj()!=5) pemegang.setMspo_flag_spaj(4);
			if(pemegang.getCampaign_id()==18){
				pemegang.setMste_tgl_recur(null);
			}else{
				pemegang.setMste_tgl_recur(df.parse(spaj.get(187).toString()));
			}
			//pemegang.setReff_tm_id(spaj.get(196).toString().replace(".0", ""));
			String concat_pendanaan="";
			String concat_tujuan="";
			//set sumber dana,tujuan belum tau model untuk sio
			String sumberDanaPP = spaj.get(39).toString();
			String tujuanPP = spaj.get(40).toString();
			if(!sumberDanaPP.equals("")){
				String[] dataSumberDana=sumberDanaPP.split(";");
				pemegang.setPendapatanBulan(dataSumberDana);
				for(int i=0;i<dataSumberDana.length;i++){					
					concat_pendanaan=concat_pendanaan.concat(dataSumberDana[i]+";");
				}
				pemegang.setMkl_pendanaan(concat_pendanaan.substring(0, concat_pendanaan.length()-1));
				
			}
			if(!tujuanPP.equals("")){
				String[] dataTujuan=tujuanPP.split(";");
				pemegang.setTujuanInvestasi(dataTujuan);
				for(int i=0;i<dataTujuan.length;i++){					
					concat_tujuan=concat_tujuan.concat(dataTujuan[i]+";");
			    }
				pemegang.setMkl_tujuan(concat_tujuan.substring(0, concat_tujuan.length()-1));
			}
			
			
			pemegang.setLsre_id_premi(Integer.parseInt(spaj.get(76).toString().replace(".0", "")));
			pemegang.setMcl_blacklist(0);
			pemegang.setLscb_id(Integer.parseInt(spaj.get(137).toString().replace(".0", "")));
			pemegang.setSpv(spaj.get(191).toString().replace(".0", ""));
			pemegang.setApplication_id(spaj.get(192).toString().replace(".0", ""));
			Integer umurPP = gethitungUmur(bdate,pemegang.getMspe_date_birth());
			pemegang.setMste_age(umurPP);
			pemegang.setTgl_verification_date(df.parse(spaj.get(193).toString()));
			pemegang.setPemegang_polis("2");
			Integer flag_pa = Integer.parseInt(spaj.get(195).toString().replace(".0", ""));
			pemegang.setCf_job_code(spaj.get(196).toString().replace(".0", "").trim());
			pemegang.setCf_customer_id(spaj.get(197).toString().replace(".0", "").trim());
			pemegang.setCf_campaign_code(spaj.get(198).toString().replace(".0", "").trim());
			if(flag_pa!=null){
				if(flag_pa==1 || flag_pa==2){
					pemegang.setFlag_free_pa(flag_pa);
				}
			}
			pemegang.setFlag_cashback(Integer.parseInt(spaj.get(200).toString().replace(".0", "")));
			int flagKpr = 0;
			if(spaj.size() > 202){
				if(spaj.get(202) != null)
					flagKpr = (spaj.get(202) == "" ? 0 : Integer.parseInt(spaj.get(202).toString()));
			}
			pemegang.setFlag_kpr(flagKpr); //tambah kolom baru. helpdesk[132502] //chandra
			mapPemegang.put("ERR", err);
			mapPemegang.put("PEMEGANG", pemegang);
		}catch (Exception e) {
			logger.error("ERROR :", e);
			err = err+"Ada Kesalahan pada saat set data pemegang";
			mapPemegang.put("ERR", err);
			mapPemegang.put("PEMEGANG", pemegang);
			return mapPemegang;
		}	
		return mapPemegang;
	}

	/**
	 * @author lufi
	 * u/ Set Data Tertanggung SIO
	 * @param uwDao 
	 * @param bacDao 
	 * @param spajExcelList,index
	 * @return
	 * @since May 2015
	 */
	public HashMap prosesSetDataTertanggungSIO(List spaj, int index, Pemegang pemegang, BacDao bacDao, UwDao uwDao) {
		HashMap<String, Serializable> mapTertanggung = new HashMap<String, Serializable>();
		Tertanggung tertanggung = new Tertanggung();
		String err="";
		try{
			Date bdate = df.parse(spaj.get(187).toString());
			Integer hubungan = pemegang.getLsre_id();
			
			if(hubungan == 1){
				tertanggung.setMcl_first(pemegang.getMcl_first());//nama lengkap tertanggung
				tertanggung.setMspe_mother(pemegang.getMspe_mother());//nama ibu kandung tertanggung
				tertanggung.setLsne_id(pemegang.getLsne_id());//id negara tertanggung
				tertanggung.setMcl_green_card(pemegang.getMcl_green_card());//green card tertanggung
				tertanggung.setLside_id(pemegang.getLside_id());//jenis kartu identitas				
				tertanggung.setMspe_no_identity(pemegang.getMspe_no_identity());//no kartu identitas
				tertanggung.setMspe_no_identity_expired(pemegang.getMspe_no_identity_expired());//tanggal expired kartu identitas
				tertanggung.setMspe_sex(pemegang.getMspe_sex());//jenis kelamin tertanggung
				tertanggung.setMspe_sts_mrt(pemegang.getMspe_sts_mrt());//status pernikahan tertanggung
				tertanggung.setMspe_date_birth(pemegang.getMspe_date_birth());//tanggal lahir tertanggung
				tertanggung.setMspe_place_birth(pemegang.getMspe_place_birth());//kota lahir tertanggung	
				tertanggung.setLsag_id(pemegang.getLsag_id());//agama tertanggung
				tertanggung.setMcl_company_name(pemegang.getMcl_company_name());//perusahaan tempat bekerja
				tertanggung.setMkl_kerja(pemegang.getMkl_kerja());//id pekerjaan
				tertanggung.setMkl_kerja_ket(pemegang.getMkl_kerja_ket());//uraian tugas
				tertanggung.setAlamat_kantor(pemegang.getAlamat_kantor());//alamat kantor tertanggung
				tertanggung.setKota_kantor(pemegang.getKota_kantor());//kota kantor tertanggung
				tertanggung.setKd_pos_kantor(pemegang.getKd_pos_kantor());//kode pos kantor
				tertanggung.setAlamat_rumah(pemegang.getAlamat_rumah());//alamat rumah
				tertanggung.setKota_rumah(pemegang.getKota_rumah());//kota rumah
				tertanggung.setKd_pos_rumah(pemegang.getKd_pos_rumah());//kode pos rumah
				tertanggung.setAlamat_tpt_tinggal(pemegang.getAlamat_tpt_tinggal());//alamat tempat tinggal tertanggung
				tertanggung.setKota_tpt_tinggal(pemegang.getKota_tpt_tinggal());//kota tempat tinggal
				tertanggung.setKd_pos_tpt_tinggal(pemegang.getKd_pos_tpt_tinggal());//kode pos tempat tinggal
				tertanggung.setArea_code_rumah(pemegang.getArea_code_rumah());//kode area telepon rumah tertanggung
				tertanggung.setTelpon_rumah(pemegang.getTelpon_rumah());//no tlp rumah tertanggung
				tertanggung.setArea_code_kantor(pemegang.getArea_code_kantor());//area kode kantor tertanggung
				tertanggung.setTelpon_kantor(pemegang.getTelpon_kantor());//no telepon kantor
				tertanggung.setNo_hp(pemegang.getNo_hp());//no hp tertanggung
				tertanggung.setEmail(pemegang.getEmail());//email tertanggung
				tertanggung.setMcl_npwp(pemegang.getMcl_npwp());//no npwp tertanggung polis
				tertanggung.setLsre_id(pemegang.getLsre_id());//hubungan ttg dengan pp
				tertanggung.setMkl_penghasilan(pemegang.getMkl_penghasilan());//total pendapatan
				tertanggung.setMcl_blacklist(pemegang.getMcl_blacklist());
				tertanggung.setLscb_id(pemegang.getLscb_id());
				tertanggung.setMcl_id(pemegang.getMcl_id());
				tertanggung.setMste_age(pemegang.getMste_age());
				tertanggung.setTujuanInvestasi(pemegang.getTujuanInvestasi());
				tertanggung.setPendapatanBulan(pemegang.getPendapatanBulan());	
				tertanggung.setMste_no_vacc(spaj.get(199).toString());//No VA
			}else{
				Integer hubunganTT=1;
				if(hubungan==2){
					hubunganTT=4;
				}else if(hubungan==4){
					hubunganTT=2;
				}else if(hubungan==5){
					hubunganTT=5;
				}else if(hubungan==17){
					hubunganTT=8;
				}else if(hubungan==10){
					hubunganTT=13;
				}
				String mcl_idTtg = getMclId(2,fmYearSpaj,bacDao);
				tertanggung.setMcl_id(mcl_idTtg);
				tertanggung.setMcl_first(spaj.get(41).toString());//nama lengkap tertanggung
				tertanggung.setMspe_mother(spaj.get(42).toString());//nama ibu kandung tertanggung
				tertanggung.setLsne_id(Integer.parseInt(spaj.get(43).toString().replace(".0", "")));//id negara tertanggung
				tertanggung.setMcl_green_card(spaj.get(44).toString().replace(".0", ""));//green card tertanggung
				tertanggung.setLside_id(Integer.parseInt(spaj.get(45).toString().replace(".0", "")));//jenis kartu identitas
				//tertanggung.setMcl_gelar(spajExcelList.get(6).toString());
				tertanggung.setMspe_no_identity(spaj.get(46).toString().replace(".0", ""));//no kartu identitas
				String expired_date_idPTT=spaj.get(47).toString();
				if(expired_date_idPTT!=null && !expired_date_idPTT.equals(""))tertanggung.setMspe_no_identity_expired(df.parse(expired_date_idPTT));//tanggal expired kartu identitas		
				//tertanggung.setMspe_no_identity_expired(df.parse(spaj.get(47).toString()));//tanggal expired kartu identitas
				tertanggung.setMspe_sex(Integer.parseInt(spaj.get(48).toString().substring(0, 1)));//jenis kelamin tertanggung
				tertanggung.setMspe_sts_mrt(spaj.get(49).toString().replace(".0", ""));//status pernikahan tertanggung
				tertanggung.setMspe_date_birth(df.parse(spaj.get(50).toString()));//tanggal lahir tertanggung
				tertanggung.setMspe_place_birth(spaj.get(51).toString());//kota lahir tertanggung	
				tertanggung.setLsag_id(Integer.parseInt(spaj.get(53).toString().replace(".0", "")));//agama tertanggung
				tertanggung.setMcl_company_name(spaj.get(54).toString());//perusahaan tempat bekerja				
				String namaKerjaan = bacDao.selectKeteranganKerja(spaj.get(55).toString().replace(".0", ""));				
				tertanggung.setMkl_kerja(namaKerjaan);//id pekerjaan				
				tertanggung.setMkl_kerja_ket(spaj.get(56).toString());//uraian tugas
				tertanggung.setAlamat_kantor(spaj.get(57).toString());//alamat kantor tertanggung
				tertanggung.setKota_kantor(spaj.get(58).toString());//kota kantor tertanggung
				tertanggung.setKd_pos_kantor(spaj.get(59).toString().replace(".0", ""));//kode pos kantor
				tertanggung.setAlamat_rumah(spaj.get(60).toString());//alamat rumah
				tertanggung.setKota_rumah(spaj.get(61).toString());//kota rumah
				tertanggung.setKd_pos_rumah(spaj.get(62).toString().replace(".0", ""));//kode pos rumah
				tertanggung.setAlamat_tpt_tinggal(spaj.get(63).toString());//alamat tempat tinggal tertanggung
				tertanggung.setKota_tpt_tinggal(spaj.get(64).toString());//kota tempat tinggal
				tertanggung.setKd_pos_tpt_tinggal(spaj.get(65).toString().replace(".0", ""));//kode pos tempat tinggal
				tertanggung.setArea_code_rumah(spaj.get(66).toString().replace(".0", ""));//kode area telepon rumah tertanggung
				tertanggung.setTelpon_rumah(spaj.get(67).toString().replace(".0", ""));//no tlp rumah tertanggung
				tertanggung.setArea_code_kantor(spaj.get(68).toString().replace(".0", ""));//area kode kantor tertanggung
				tertanggung.setTelpon_kantor(spaj.get(69).toString().replace(".0",""));//no telepon kantor
				tertanggung.setNo_hp(spaj.get(70).toString().replace(".0", ""));//no hp tertanggung
				tertanggung.setEmail(spaj.get(71).toString());//email tertanggung
				tertanggung.setMcl_npwp(spaj.get(72).toString().replace(".0", ""));//no npwp tertanggung polis
				tertanggung.setLsre_id(hubunganTT);//hubungan ttg dengan pp
				tertanggung.setMkl_penghasilan(spaj.get(73).toString());//total pendapatan
				tertanggung.setMste_no_vacc(spaj.get(199).toString());//No VA
				
				String concat_pendanaan="";
				String concat_tujuan="";
				//set sumber dana,tujuan belum tau model untuk sio
				String sumberDanaTT = spaj.get(74).toString();
				String tujuanTT = spaj.get(75).toString();
				if(!sumberDanaTT.equals("")){
					String[] dataSumberDana=sumberDanaTT.split(";");
					tertanggung.setPendapatanBulan(dataSumberDana);
					for(int i=0;i<dataSumberDana.length;i++){					
						concat_pendanaan=concat_pendanaan.concat(dataSumberDana[i]+";");
						
					}
					tertanggung.setMkl_pendanaan(concat_pendanaan.substring(0, concat_pendanaan.length()-1));
				}
				if(!tujuanTT.equals("")){
					String[] dataTujuan=tujuanTT.split(";");
					tertanggung.setTujuanInvestasi(dataTujuan);
					for(int i=0;i<dataTujuan.length;i++){					
						concat_tujuan=concat_tujuan.concat(dataTujuan[i]+";");
						
					}
					tertanggung.setMkl_tujuan(concat_tujuan.substring(0, concat_tujuan.length()-1));
				}
				
				Integer umurTtg = gethitungUmur(bdate,tertanggung.getMspe_date_birth());
				tertanggung.setMste_age(umurTtg);
				tertanggung.setMcl_blacklist(0);
				tertanggung.setLscb_id(Integer.parseInt(spaj.get(137).toString().replace(".0", "")));	
			}
			
			mapTertanggung.put("ERR", err);
			mapTertanggung.put("TERTANGGUNG", tertanggung);
		}catch (Exception e) {
			err = err+" "+", Ada Kesalahan pada saat set data tertanggung ";
			mapTertanggung.put("ERR", err);
			mapTertanggung.put("TERTANGGUNG", tertanggung);
			return mapTertanggung;
		}

		return mapTertanggung;
	}
	
	public HashMap prosesSetDataPemPremiSIO(List spaj, int index, Pemegang pemegang, BacDao bacDao){
		HashMap<String, Serializable> mapPembayarPremi = new HashMap<String, Serializable>();
		PembayarPremi pembayarPremi = new PembayarPremi();
		Integer flagPemPremi = pemegang.getLsre_id_premi();
		String err = "";
		String mcl_idPemPremi = getMclId(3, fmYearSpaj, bacDao);
		try{
			if(flagPemPremi == 40){
				pembayarPremi.setMcl_id(pemegang.getMcl_id());
				pembayarPremi.setAda_pihak_ketiga("0");
				pembayarPremi.setTotal_rutin("<RP. 5 JUTA");
				pembayarPremi.setTotal_non_rutin("<RP. 5 JUTA");
				
				
			}else if(flagPemPremi == 41){
				pembayarPremi.setMcl_id(mcl_idPemPremi);
				pembayarPremi.setAda_pihak_ketiga("1");
				pembayarPremi.setNama_pihak_ketiga(spaj.get(116).toString());
				pembayarPremi.setAlamat_lengkap(spaj.get(117).toString());
				pembayarPremi.setKewarganegaraan(spaj.get(118).toString());
				pembayarPremi.setKota_perusahaan(spaj.get(124).toString());
				pembayarPremi.setTgl_pendirian(spaj.get(123).toString());
				pembayarPremi.setBidang_usaha_badan_hukum(spaj.get(120).toString());
				pembayarPremi.setNo_npwp(spaj.get(128).toString().replace(".0", ""));
				
			}else{
				pembayarPremi.setMcl_id(mcl_idPemPremi);
				pembayarPremi.setAda_pihak_ketiga("1");
				pembayarPremi.setNama_pihak_ketiga(spaj.get(116).toString());
				pembayarPremi.setAlamat_lengkap(spaj.get(117).toString());
				pembayarPremi.setKewarganegaraan(spaj.get(118).toString());
				pembayarPremi.setJabatan(spaj.get(119).toString());
				pembayarPremi.setNo_npwp(spaj.get(128).toString().replace(".0", ""));
				pembayarPremi.setTotal_rutin(spaj.get(131).toString().replace(".0", ""));
				String sumberDanaPayer = spaj.get(129).toString();
				String tujuanPayer = spaj.get(130).toString();
				pembayarPremi.setTotal_rutin(spaj.get(131).toString());
				pembayarPremi.setTotal_non_rutin(spaj.get(131).toString());
				if(!sumberDanaPayer.equals("")){
					String[] dataSumberDana=sumberDanaPayer.split(";");
					pembayarPremi.setPendapatanBulan(dataSumberDana);
					pembayarPremi.setSumber_dana(sumberDanaPayer);
				}
				if(!tujuanPayer.equals("")){
					String[] dataTujuan=tujuanPayer.split(";");
					pembayarPremi.setTujuanInvestasi(dataTujuan);
					pembayarPremi.setTujuan_dana(tujuanPayer);
				}
			}
			mapPembayarPremi.put("ERR", err);
			mapPembayarPremi.put("PAYER", pembayarPremi);
		}catch (Exception e) {
			err = err+" "+", Ada Kesalahan pada saat set data pembayar premi";
			mapPembayarPremi.put("ERR", err);
			mapPembayarPremi.put("PAYER", pembayarPremi);
			return mapPembayarPremi;
		}		
		
		return mapPembayarPremi;
	}	

	/**
	 * @author lufi
	 * u/ Set Data Agen Untuk Format SMile Proritas Dari kolom Excel yg diupload
	 * @param uwDao 
	 * @param bacDao 
	 * @param bacDao2 
	 * @param spajExcelList,excelListagen,index 
	 * @return excelListagen
	 * @since Oct 2014
	 */
	public HashMap prosesSetDataAgenSIO(List spaj,int index, BacDao bacDao, UwDao uwDao) {
		HashMap<String, Serializable> mapAgen = new HashMap<String, Serializable>();
		String err="";
		Agen agn = new Agen();
		try{
			agn.setMsag_id(spaj.get(180).toString().replace(".0", ""));
			if(agn.getMsag_id() == null)agn.setMsag_id("");							     
			if(!"".equals(agn.getMsag_id())){				
				agn = bacDao.select_detilagen3(agn.getMsag_id());	
				mapAgen.put("ERR", err);
				mapAgen.put("AGN", agn);
			}	
		}catch (Exception e) {
			err = err+" "+", Ada Kesalahan pada saat set data agen";
			mapAgen.put("ERR", err);
			mapAgen.put("AGN", agn);
			return mapAgen;
		}		

		return mapAgen;
	}
	
	/**
	 * @author lufi
	 * u/ Set Data Rekening Klien SIO
	 * @param uwDao 
	 * @param bacDao 
	 * @param spajExcelList,index
	 * @return mapRekCLient
	 * @since May 2015
	 */
	public HashMap prosesSetDataRekClientSIO(List spaj, int index, BacDao bacDao, UwDao uwDao) {
		HashMap<String, Serializable> mapRekClient = new HashMap<String, Serializable>();
		String err="";
		Rekening_client rekCLient = new Rekening_client();
		try{
			rekCLient.setMrc_no_ac(spaj.get(88).toString().replace(".0", ""));
			rekCLient.setLsbp_id(spaj.get(87).toString().replace(".0", ""));
			rekCLient.setMrc_nama(spaj.get(91).toString());
			rekCLient.setMrc_kota(spaj.get(90).toString());
			rekCLient.setMrc_cabang(spaj.get(89).toString().replace(".0", ""));
			rekCLient.setMrc_kurs(spaj.get(4).toString());
			mapRekClient.put("ERR", err);
			mapRekClient.put("REKCLIENT", rekCLient);
		}catch (Exception e) {
			err = err+" "+", Ada Kesalahan pada saat set data rekening pemegang";
			mapRekClient.put("ERR", err);
			mapRekClient.put("REKCLIENT", rekCLient);
			return mapRekClient;
		}
		return mapRekClient;
	}


	/**
	 * @author lufi
	 * u/ Set Data Penerima Manfaat SIO
	 * @param uwDao 
	 * @param bacDao 
	 * @param spajExcelList,index
	 * @return mapBenef
	 * @since May 2015
	 */

	public HashMap prosesSetDataBeneficiarySIO(List spaj,  int index, BacDao bacDao, UwDao uwDao) {
		HashMap<String, Serializable> mapBenef = new HashMap<String, Serializable>();
		String err="";
		ArrayList<Benefeciary> benef = new ArrayList<Benefeciary>();
		Integer jumlahBenef=0;
		if(!spaj.get(92).toString().equals(""))jumlahBenef+=1;
		if(!spaj.get(98).toString().equals(""))jumlahBenef+=1;
		if(!spaj.get(104).toString().equals(""))jumlahBenef+=1;
		if(!spaj.get(110).toString().equals(""))jumlahBenef+=1;
		String bod1=spaj.get(93).toString();
		String bod2=spaj.get(99).toString();
		String bod3=spaj.get(105).toString();
		String bod4=spaj.get(111).toString();
		if(jumlahBenef>0){
			f_replace konteks = new f_replace();			
			for(int j=1;j<=jumlahBenef;j++){
				try{
					String msaw_first = null ,lsre_id = null, msaw_sex = null,msaw_persen = null,lsne_id=null;
					Date bod = new Date() ;
					Benefeciary bf = new Benefeciary();
					Integer nobf = 0;				
					switch (j) {
					case 1:
						msaw_first=spaj.get(92).toString();
						if(!StringUtil.isEmpty(bod1))bod = df.parse(spaj.get(93).toString());
						msaw_sex = spaj.get(94).toString().replace(".0", "");
						lsne_id = spaj.get(95).toString().replace(".0", "");
						lsre_id = spaj.get(97).toString().replace(".0", "");
						msaw_persen = spaj.get(96).toString();
						nobf=1;
						break;

					case 2:
						msaw_first=spaj.get(98).toString();
						if(!StringUtil.isEmpty(bod2))bod = df.parse(spaj.get(99).toString());
						msaw_sex = spaj.get(100).toString().replace(".0", "");
						lsne_id = spaj.get(101).toString().replace(".0", "");
						lsre_id = spaj.get(103).toString().replace(".0", "");
						msaw_persen = spaj.get(102).toString().replace(".0", "");
						nobf = 2;
						break;
					case 3:
						msaw_first=spaj.get(104).toString();
						if(!StringUtil.isEmpty(bod3))bod = df.parse(spaj.get(105).toString());
						msaw_sex=spaj.get(106).toString().replace(".0", "");
						lsne_id = spaj.get(107).toString().replace(".0", "");
						lsre_id=spaj.get(109).toString().replace(".0", "");
						msaw_persen=spaj.get(108).toString().replace(".0", "");
						nobf=3;
						break;

					case 4:
						msaw_first=spaj.get(110).toString();
						if(!StringUtil.isEmpty(bod4))bod = df.parse(spaj.get(111).toString());
						msaw_sex=spaj.get(112).toString().replace(".0", "");
						lsne_id = spaj.get(113).toString().replace(".0", "");
						lsre_id=spaj.get(115).toString().replace(".0", "");
						msaw_persen=spaj.get(114).toString().replace(".0", "");
						nobf=4;
						break;
					}

					if (msaw_first==null){
						msaw_first="";
					}

					if (StringUtils.isEmpty(lsre_id)){
						lsre_id="1";
					}

					if (msaw_persen.trim().length()==0){
						msaw_persen="0";
					}else{
						msaw_persen=konteks.f_replace_persen(msaw_persen);
						boolean cekk1 = f_validasi.f_validasi_numerik1(msaw_persen);
						if (cekk1 == false){
							msaw_persen="0";
						}
					}
					bf.setMsaw_first(msaw_first);
					bf.setMsaw_birth(bod);
					bf.setLsne_id(Integer.parseInt(lsne_id));
					bf.setLsre_id(Integer.parseInt(lsre_id));
					bf.setMsaw_persen(Double.parseDouble(msaw_persen));
					bf.setMsaw_sex(Integer.parseInt(msaw_sex));
					bf.setMsaw_number(nobf);
					bf.setMste_insured_no(1);
					benef.add(bf);
					mapBenef.put("ERR", err);
					mapBenef.put("BENEF", benef);
				}catch (Exception e) {
					err = err+" "+", Ada Kesalahan pada saat set data penerima manfaat";
					mapBenef.put("ERR", err);
					mapBenef.put("BENEF", benef);
					return mapBenef;
				}
			}
		}	  
		return mapBenef;
	}

	/**
	 * @author lufi
	 * u/ Set Data Account Recur SIO
	 * @param uwDao 
	 * @param bacDao 
	 * @param spajExcelList,index
	 * @return mapRecur
	 * @since May 2015
	 */
	public HashMap prosesSetDataAccountRecurSIO(List spaj, int index, BacDao bacDao, UwDao uwDao) {
		HashMap<String, Serializable> mapRecur = new HashMap<String, Serializable>();
		String err="";
		Account_recur rekAutodebet = new Account_recur();
		Integer autodebet = new Integer(0);
		try{
			autodebet=Integer.parseInt(spaj.get(138).toString().replace(".0", ""));
			rekAutodebet.setMar_holder(spaj.get(140).toString());	    	
			rekAutodebet.setMar_jenis(autodebet);
			rekAutodebet.setMar_active(1);
			rekAutodebet.setMar_acc_no(spaj.get(139).toString().replace(".0", "")); 		    	    	 
			rekAutodebet.setLbn_id(spaj.get(142).toString().replace(".0", ""));
			Integer lsbs_id_utama=Integer.parseInt(spaj.get(132).toString().substring(0, 3));
			if(autodebet==1 ){
				String lsbp_id=spaj.get(142).toString().replace(".0", "");
				String lbn_id=bacDao.selectLbn_id(lsbp_id);
				rekAutodebet.setLbn_id(lbn_id);
				if(lsbs_id_utama!=221){
					String dateTime = spaj.get(141).toString().replace(".0","");
					if(!dateTime.trim().equals("")){
					rekAutodebet.setMar_expired(df.parse(dateTime));
					}
			    	err=err+validatorTM.checkDataAutodebet(rekAutodebet);
				}
			}else if(autodebet ==2){
				//rekAutodebet.setTgl_debet(df.parse(spaj.get(141).toString()));
				
				String accNo = rekAutodebet.getMar_acc_no();
				if(accNo.trim().equals("")){
					err=err+" Mohon Pastikan No Rekening ada";
				}
				
			}
			
			mapRecur.put("ERR", err);
			mapRecur.put("RECUR", rekAutodebet);
		}catch (Exception e) {
			err = err+" "+", Ada Kesalahan pada saat set data autodebet";
			mapRecur.put("ERR", err);
			mapRecur.put("RECUR", rekAutodebet);
			return mapRecur;
		}
		
		return mapRecur;
	}

	/**
	 * @author lufi
	 * u/ Set Data Usulan Untuk SIO	 
	 * @param suratUnitLink 
	 * @param uwDao 
	 * @param bacDao 
	 * @param defaultDateFormat2 
	 * @param spajExcelList,excelListTtg,excelListPp,index
	 * @return mapDatausulan
	 * @since May 2015
	 */
	public HashMap prosesSetDataUsulanSIO(List spaj,Tertanggung excelListTtg, Pemegang excelListPp, int index, BacDao bacDao, UwDao uwDao, SuratUnitLink suratUnitLink, DateFormat defaultDateFormat){
		HashMap<String, Serializable> mapDataUsulan = new HashMap<String, Serializable>();
		String err="";
		Datausulan datausulan = new Datausulan();	
		Integer payperiod=new Integer(0);
		Integer insperiod=new Integer(0);
		Calendar cal = Calendar.getInstance();
		Double up=new Double(0.);
		Integer flagKesehatan = 0;
		Integer flagProvider = 0;
		ArrayList<PesertaPlus_mix> daftarPeserta = new ArrayList<PesertaPlus_mix>();
		PesertaPlus_mix peserta = new PesertaPlus_mix();
		Integer errorpesertatamb214=0; 
		Integer ridertu841=0; Integer ridertu843=0;
		Integer rider840t2=0;Integer rider841t2=0; Integer rider843t2=0;
		Integer rider840t3=0;Integer rider841t3=0; Integer rider843t3=0;
		Integer rider840t4=0;Integer rider841t4=0; Integer rider843t4=0;
		Integer rider840t5=0;Integer rider841t5=0; Integer rider843t5=0;
		
		Integer errorpesertatamb225=0; 
		Integer ridertu847RJ=0; Integer ridertu847RG=0;Integer ridertu847PK=0;
		Integer rider846RIt2=0;Integer rider847RJt2=0; Integer rider847RGt2=0; Integer rider847PKt2=0;
		Integer rider846RIt3=0;Integer rider847RJt3=0; Integer rider847RGt3=0; Integer rider847PKt3=0;
		Integer rider846RIt4=0;Integer rider847RJt4=0; Integer rider847RGt4=0; Integer rider847PKt4=0;
		Integer rider846RIt5=0;Integer rider847RJt5=0; Integer rider847RGt5=0; Integer rider847PKt5=0;
		try{
			Class aClass;				
			n_prod produk = new n_prod();			
			String nama_produk;
			Date bdate = df.parse(spaj.get(187).toString());
			Integer lsbs_id_utama=Integer.parseInt(spaj.get(132).toString().substring(0, 3));
			Integer lsdbs_number_utama=Integer.parseInt(spaj.get(132).toString().substring(4));
			datausulan.setLsbs_id(lsbs_id_utama); //set lsbs_id
			datausulan.setLsdbs_number(lsdbs_number_utama);//set lsdbs_number
			datausulan.setKurs_premi(spaj.get(4).toString());//set kurs
			datausulan.setLscb_id(Integer.parseInt(spaj.get(137).toString().replace(".0", "")));//set cara bayar	   						    
			datausulan.setMste_flag_cc(Integer.parseInt(spaj.get(138).toString().replace(".0", "")));//set flag_cc							    
			String kode_produk=Integer.toString(datausulan.getLsbs_id());
			nama_produk="produk_asuransi.n_prod_"+kode_produk;
			aClass = Class.forName(nama_produk);
			produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(uwDao.getSqlMapClient());
			produk.ii_pmode=datausulan.getLscb_id();
			produk.ii_bisnis_id=lsbs_id_utama;
			produk.ii_bisnis_no=lsdbs_number_utama;
			produk.is_kurs_id=datausulan.getKurs_premi();
			produk.of_set_kurs(produk.is_kurs_id);
			produk.ii_age=excelListTtg.getMste_age();
			produk.of_set_usia_tt(excelListTtg.getMste_age());
			datausulan.setLi_umur_pp(excelListPp.getMste_age());	//set umut pemegang
			produk.of_set_usia_pp(excelListPp.getMste_age());
			datausulan.setLi_umur_ttg(excelListTtg.getMste_age());//set umur ttg	
			
			if(excelListTtg.getMste_age()!=null){ //2 untuk tertanggung utama 
				Integer tahun2 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));
				Integer bulan2 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
				Integer tanggal2 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
				f_hit_umur umr = new f_hit_umur();
				Integer umurTtg=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
				int hari=umr.hari(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
				if("183,189,195,204,214,221,225".indexOf(lsbs_id_utama.toString())>-1 && hari>15 && umurTtg == 0 ){
					 umurTtg = 1;
					 produk.ii_age=umurTtg;
					 datausulan.setLi_umur_ttg(umurTtg);
					 excelListTtg.setMste_age(umurTtg);
					 produk.of_set_usia_tt(umurTtg);
				}else if("183,189,195,204,214,221,225".indexOf(lsbs_id_utama.toString())>-1 && hari<15 && umurTtg == 0){
					 err = err+" "+",\n Usia Tertanggung kurang dari 15 hari.";
				}
			
			}
			
			
			/**
			 * Patar Timotius
			 * 2018/07/08
			 */
	        boolean isDMTMSmartKid1 = lsbs_id_utama==208 && (lsdbs_number_utama>=29 && lsdbs_number_utama<=32);
			/**
			 * Patar Timotius
			 * 2018/08/08
			 */
	        boolean isDMTMSimasKidBSIM1 = lsbs_id_utama==208 && (lsdbs_number_utama>=33 && lsdbs_number_utama<=36);
	        /**
			 * Patar Timotius
			 * 2019/05/09
			 */
			boolean isSmileKidBJB = lsbs_id_utama == 208 && (lsdbs_number_utama >= 45 && lsdbs_number_utama <= 48) ;
			
			if(lsbs_id_utama==169){
				produk.of_get_conperiod(lsdbs_number_utama);
				insperiod=produk.ii_contract_period;
			}else if(lsbs_id_utama==212 && (lsdbs_number_utama==1 ||lsdbs_number_utama==4 || lsdbs_number_utama==5 || lsdbs_number_utama==7 || lsdbs_number_utama==8 || lsdbs_number_utama==12 || lsdbs_number_utama == 13)){
				insperiod = Integer.parseInt(spaj.get(201).toString().replace(".0", ""));
				produk.ii_contract_period = insperiod;
				
				if(lsbs_id_utama == 212 && lsdbs_number_utama == 13){
					int minimum= 0; int maximum = 60;
					int validator = produk.ii_age+insperiod;
					if(validator>minimum && validator<=60){
						produk.ii_contract_period = insperiod;
					}else{
						 err = err+" "+",\n Mohon cek Untuk hubungan Usia "+produk.ii_age+" dengan lama tanggungnya";
					}
				}
				
				
			
				
			}else if(isDMTMSmartKid1 || isSmileKidBJB){
				produk.ii_age = excelListTtg.getMste_age();
				produk.of_get_conperiod(lsdbs_number_utama);
				insperiod=produk.ii_contract_period;
			//	String btn_flag_cc = spaj.get(202).toString();
				Integer mste_flag_cc_tabungan_only = 2; // hardcode di karenakan team hanya bisa dapat 2
			/*	if(btn_flag_cc != null){
					btn_flag_cc = btn_flag_cc.toString().replace(".0", "");
					mste_flag_cc_tabungan_only = new Integer(btn_flag_cc);
				}
			*/	datausulan.setMste_flag_cc(mste_flag_cc_tabungan_only);//set flag_cc							    
				
				
			}else if(isDMTMSimasKidBSIM1){
				produk.ii_age = excelListTtg.getMste_age();
				produk.of_get_conperiod(lsdbs_number_utama);
				insperiod=produk.ii_contract_period;
				
			}
			//set begdate
			datausulan.setMspr_beg_date(bdate);	
			datausulan.setMspo_no_kerjasama(spaj.get(190).toString().replace(".0", ""));
			tahun = Integer.parseInt(sdfYear.format(datausulan.getMspr_beg_date()));
			bulan = Integer.parseInt(sdfMonth.format(datausulan.getMspr_beg_date()));
			tanggal = Integer.parseInt(sdfDay.format(datausulan.getMspr_beg_date()));			
			produk.of_set_begdate(tahun,bulan,tanggal);
			//end of set begdate
			
			//set end date
			tanggalEd = new Integer(produk.idt_end_date.getTime().getDate());
			bulanEd = new Integer(produk.idt_end_date.getTime().getMonth()+1);
			tahunEd = new Integer(produk.idt_end_date.getTime().getYear()+1900);			
			String tgl_end = Integer.toString(tanggalEd.intValue());
			String bln_end = Integer.toString(bulanEd.intValue());
			String thn_end = Integer.toString(tahunEd.intValue());			
			if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
			{
				datausulan.setMste_end_date(null);
			}else{
				String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;				
				datausulan.setMspr_end_date(defaultDateFormat.parse(tanggal_end_date));				
			}
			//end of set end date
			
			//set payperiod , insperiod
			cal.setTime(datausulan.getMspr_end_date());
			cal.add(Calendar.MONTH, -1);
			cal.add(Calendar.DATE, 1);
			Date end_pay=cal.getTime();						
			datausulan.setMspr_end_pay(end_pay);
			payperiod = produk.of_get_payperiod();
			if(lsbs_id_utama==183 || lsbs_id_utama==189 || lsbs_id_utama==195 || lsbs_id_utama==204 || lsbs_id_utama==214 || lsbs_id_utama==221 || lsbs_id_utama==225){
				insperiod=1;
				flagProvider++;
			}else if(lsbs_id_utama==169){
				produk.of_get_conperiod(lsdbs_number_utama);
				insperiod=produk.ii_contract_period;
			}else if(lsbs_id_utama==212 && (lsdbs_number_utama == 1 || lsdbs_number_utama == 4 || lsdbs_number_utama==5 || lsdbs_number_utama==7 || lsdbs_number_utama==8 || lsdbs_number_utama==12 || lsdbs_number_utama == 13)){
//				insperiod = Integer.parseInt(spaj.get(201).toString().replace(".0", ""));
				payperiod = insperiod;
				
				 if ((payperiod < 10 || insperiod < 10 || payperiod > 20 || insperiod > 20 ) && lsdbs_number_utama == 7){
					 err = err+" "+",\n Untuk produk SMART LIFE CARE PLUS (DMTM SIO), masa Asuransi yg diperbolehkan hanya 10-20 Tahun.";
				}else if ((payperiod < 10 || insperiod < 10 || payperiod > 20 || insperiod > 20 ) && lsdbs_number_utama == 8){
					 err = err+" "+",\n Untuk produk NISSAN TERM ROP, masa Asuransi yg diperbolehkan hanya 10-20 Tahun.";
				}else if ((payperiod < 8 || insperiod < 8 || payperiod > 20 || insperiod > 20 ) && lsdbs_number_utama == 1){
					 err = err+" "+",\n Untuk produk SMART LIFE CARE PLUS (BTN), masa Asuransi yg diperbolehkan hanya 8-20 Tahun.";
				}else if ((payperiod < 10 || insperiod < 10 || payperiod > 20 || insperiod > 20 ) && lsdbs_number_utama == 12){
						 err = err+" "+",\n Untuk produk SMILE TERM ROP (DMTM GIO), masa Asuransi yg diperbolehkan hanya 10-20 Tahun.";					 
				}			
				
			}else{
				insperiod=produk.ii_contract_period;
			}
			datausulan.setMspo_pay_period(payperiod);
			datausulan.setMspo_ins_period(insperiod);
			//end of set payperiod , insperiod
			
			//set premi, up
			Double premi_pokok = 0.;
			Double premi_berkala = 0.;
			Double premi_tunggal = 0.;
			Double total_premi = 0.;
			premi_pokok = Double.parseDouble(spaj.get(135).toString().replace(".0", ""));
			premi_berkala = Double.parseDouble(spaj.get(136).toString().replace(".0", ""));
			premi_tunggal = Double.parseDouble(spaj.get(188).toString().replace(".0", ""));
		    total_premi = premi_pokok+premi_berkala;
		    if(premi_berkala>0) datausulan.setLi_trans_berkala(5);
		    if(premi_tunggal>0) datausulan.setLi_trans_tunggal(2);
	        up = Double.parseDouble(spaj.get(175).toString().replace(".0", ""));
	        double pct_add = produk.idec_pct_list[datausulan.getLscb_id()];
	        
	        /**
	         * isDMTMSmartKid
	         * 03/08/2018
	         * Patar Timotius
	         */
	        
	        boolean isDMTMSmartKid = datausulan.getLsbs_id()==208 && (datausulan.getLsdbs_number()>=29 && datausulan.getLsdbs_number()<=32);
	        
	        /**
	         * isDMTMSimasKidBSIM
	         * 08/08/2018
	         * Patar Timotius
	         */
	        
	        boolean isDMTMSimasKidBSIM = datausulan.getLsbs_id()==208 && (datausulan.getLsdbs_number()>=33 && datausulan.getLsdbs_number()<=36);
	        
	
	        
	        boolean isDMTMSmileKidBJB = datausulan.getLsbs_id()==208 && (datausulan.getLsdbs_number()>=45 && datausulan.getLsdbs_number()<=48);
	        
	        
	    
	 			
	 			
	        
	        
	        if(datausulan.getLsbs_id()==183 
	        		|| (datausulan.getLsbs_id()==189 && datausulan.getLsdbs_number()>=33 && datausulan.getLsdbs_number()<=47 )
	        		|| (datausulan.getLsbs_id()==189 && (datausulan.getLsdbs_number()>=48 && datausulan.getLsdbs_number()<=62)) //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
	        		|| datausulan.getLsbs_id()==195 
	        		|| (datausulan.getLsbs_id()==204 && datausulan.getLsdbs_number()>=37 && datausulan.getLsdbs_number()<=48 )
	        		|| datausulan.getLsbs_id()==221){
	        	up = produk.of_get_min_up();
	        	produk.of_hit_premi();
	        	premi_pokok = produk.idec_premi;
	        	
	        }else if(datausulan.getLsbs_id()==169){
	        	produk.idec_up = up.doubleValue();
				produk.idec_add_pct = pct_add;
				produk.of_hit_premi();	
				//produk.of_set_premi(produk.idec_premi);
	        	up = produk.idec_up;
	        	premi_pokok = produk.idec_premi;
	        	
	        }else if(datausulan.getLsbs_id()==187){
	        	produk.idec_add_pct = pct_add;
	        	produk.of_hit_premi();

	        }else if(datausulan.getLsbs_id()==212 && (lsdbs_number_utama==1 || lsdbs_number_utama==4 || lsdbs_number_utama==5 || lsdbs_number_utama==7 || lsdbs_number_utama==12 || lsdbs_number_utama==13)){ 
	        	String hasil_min_premi = produk.of_alert_min_premi(premi_pokok);
	        		if(!hasil_min_premi.equals("")) err = err+" ,"+hasil_min_premi;	        	
		        produk.of_set_premi(premi_pokok);
		        up = produk.idec_up;
		        
//		        if(datausulan.getLsdbs_number() == 4){
//		        	if(datausulan.getKurs_premi().equals("02")) err = err+" , Produk ini tidak bisa dengan mata uang Dollar.";
//		        }
		        
	        }else if(datausulan.getLsbs_id()==214 || datausulan.getLsbs_id()==225){		        
	        	produk.of_hit_premi();
	        	premi_pokok = produk.idec_premi;
	        	up = produk.idec_up;
	        	
	        }else if( (datausulan.getLsbs_id()==197 && lsdbs_number_utama==2)
	        		 || (datausulan.getLsbs_id()==73 && lsdbs_number_utama==15)
	        		 || (datausulan.getLsbs_id()==212 && lsdbs_number_utama==8)
	        		 || (datausulan.getLsbs_id()==203 && lsdbs_number_utama==4) ){		        
	        	produk.idec_up = up.doubleValue();
				produk.idec_add_pct = pct_add;
	        	produk.of_set_premi(premi_pokok);
	        	premi_pokok = produk.idec_premi;
	        	up = produk.idec_up;
	        }else if(isDMTMSmartKid || isDMTMSmileKidBJB){ //patar timotius 03/08/2018
	        	produk.ii_age = excelListTtg.getMste_age();
	        	produk.idec_up = up.doubleValue();
				produk.idec_add_pct = pct_add;
	        	produk.of_hit_premi();
	        	premi_pokok = produk.idec_premi;
	        	up = produk.idec_up;
	        }else if(isDMTMSimasKidBSIM){
	        	produk.ii_age = excelListTtg.getMste_age();
	        	produk.idec_up = up.doubleValue();
				produk.idec_add_pct = pct_add;
	        	produk.of_hit_premi();
	        	premi_pokok = produk.idec_premi;
	        	up = produk.idec_up;
	        }
//	        if (datausulan.getLsbs_id()==163 ){
//	        	produk.idec_add_pct=pct_add;
//	        	produk.idec_up=up.doubleValue();
//	        	produk.of_hit_premi();
//	        	premi_pokok=produk.idec_premi;
//        	}
	        
	        //helpdesk [148055]
	        //helpdesk [150296] DMTM BSIM 163 21-25 tambah simple questionare SIO+
	        double excel_premi = 0;
	        Boolean isDanaSejatera = datausulan.getLsbs_id().toString().equalsIgnoreCase("163") && (datausulan.getLsdbs_number() >= 21 || datausulan.getLsdbs_number() <= 25);
	        if(isDanaSejatera){
	        	produk.idec_add_pct = pct_add;
	        	produk.idec_up = up;
	        	excel_premi = premi_pokok;
	        }
	        
	        //Set Rate
	        produk.of_hit_premi();
		    Double rate = 0.0;
		    rate = produk.idec_rate;
		    datausulan.setMspr_rate(rate.intValue());	
	        premi_pokok = (produk.idec_premi > 0 && isDanaSejatera ? produk.idec_premi : premi_pokok); //helpdesk [148055]
			datausulan.setMspr_premium(premi_pokok);
			datausulan.setMspr_tsi(up);
			datausulan.setTotal_premi_kombinasi(total_premi);			
			datausulan.setKode_flag(produk.kode_flag);
			//end of set premi,up
			
			if(isDanaSejatera && excel_premi != premi_pokok){ //helpdesk [148055]
				err = String.format(" Perhitungan premi tidak sesuai [%s][%s-%s]", premi_pokok.toString(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
			}
			
			if("183,189,195,204,214,221,225".indexOf(datausulan.getLsbs_id().toString())>-1){
				peserta.setNama(excelListTtg.getMcl_first());
		    	peserta.setLsbs_id(datausulan.getLsbs_id());
		    	peserta.setLsdbs_number(datausulan.getLsdbs_number());
		    	peserta.setNo_reg("1a");
		    	peserta.setFlag_jenis_peserta(0);
		    	peserta.setNo_urut(0);
		    	peserta.setUmur(excelListTtg.getMste_age());
		    	peserta.setMspr_premium(datausulan.getMspr_premium());
		    	peserta.setLsre_id(excelListTtg.getLsre_id());
		    	peserta.setNext_send(datausulan.getMspr_beg_date());
		    	peserta.setKelamin(excelListTtg.getMspe_sex());
		    	peserta.setPremi(datausulan.getMspr_premium());
		    	peserta.setTanggal_lahir(excelListTtg.getMspe_date_birth());
		    	daftarPeserta.add(peserta);
		    	datausulan.setDaftapeserta(daftarPeserta);
				datausulan.setJml_peserta(daftarPeserta.size());
			}
			
			//proses Rider
			List<Datarider> dtrd = new ArrayList<Datarider>();
			String kode_rider = spaj.get(133).toString();		
			Class aClass2;	
			n_prod produk2 = new n_prod();
			if(!kode_rider.equals("")){    
				String[] dataRider=kode_rider.split(";");
				List<String> listRider = Arrays.asList(dataRider);				 
				int tahun_rider,bulan_rider,tanggal_rider;String nama_produk_rider=null;
				Integer pesertaTT=0;Integer usiaDariTT=0;
				for(int j=0; j<listRider.size(); j++){
					Datarider dtr=new Datarider();
					String kode=listRider.get(j);
					Integer lsbs_id_rider=Integer.parseInt(kode.substring(0, 3));
					Integer lsdbs_number_rider=Integer.parseInt(kode.substring(4));							
					Integer unit = 0;
					Integer klas=0; 
					Double premi = 0.;
					Double upRider = 0.;

					Integer factor = 0;
					Double rate_rider = 0.;
			  
					nama_produk_rider="produk_asuransi.n_prod_"+lsbs_id_rider;
					aClass2 = Class.forName( nama_produk_rider);
					produk2 = (n_prod)aClass2.newInstance();	
					produk2.setSqlMap(uwDao.getSqlMapClient());
					produk2.of_set_bisnis_no(lsbs_id_rider);
					produk2.ii_bisnis_id=lsbs_id_rider;
					produk2.ii_bisnis_no=lsdbs_number_rider;
					produk2.ii_usia_pp=datausulan.getLi_umur_pp();
					produk2.ii_usia_tt=datausulan.getLi_umur_ttg();
					produk2.ii_age=produk2.ii_usia_tt;
					tahun_rider = Integer.parseInt(sdfYear.format(datausulan.getMspr_beg_date()));
					bulan_rider= Integer.parseInt(sdfMonth.format(datausulan.getMspr_beg_date()));
					tanggal_rider = Integer.parseInt(sdfDay.format(datausulan.getMspr_beg_date()));
					produk2.of_set_begdate(tahun_rider,bulan_rider,tanggal_rider);	
					dtr.setMspr_beg_date(datausulan.getMspr_beg_date());	
					dtr.setLsbs_id(lsbs_id_rider);
					dtr.setLsdbs_number(lsdbs_number_rider);
					unit = produk2.set_unit(unit);
					klas = produk2.set_klas(klas); 
					factor=0;					

					produk2.is_kurs_id=datausulan.getKurs_premi();										
					//ENDATE DAN PAYDATE RIDER								
					produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),datausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk2.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, datausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
					Integer tanggal_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getDate());
					Integer bulan_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getMonth()+1);
					Integer tahun_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getYear()+1900);																	

					String tgl_akhir_polis1=null;
					if(produk2.ii_bisnis_id==829){
						tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tahunEd.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulanEd.intValue()),2)+"/"+Integer.toString(tahunEd.intValue());
					}else{
						tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
					}
					if ( tgl_akhir_polis1.trim().length()!=0)
					{
						dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
					}else{
						dtr.setMspr_end_date(null);
					}
					Calendar cal2 = Calendar.getInstance();	
					cal2.setTime(dtr.getMspr_end_date());
					cal2.add(Calendar.MONTH, -1);
					cal2.add(Calendar.DATE, 1);
					Date end_pay_rider=cal2.getTime();

					if ( end_pay_rider!=null)
					{
						dtr.setMspr_end_pay(end_pay_rider);
					}else{
						dtr.setMspr_end_pay(null);
					}
					
					//ambil usia dari TT tambahan buat Rate
					 if("819,820,823,825,826,831,832,833,844".indexOf(lsbs_id_rider.toString())>-1){
						 usiaDariTT = prosesSetDataPesertaUsiaSIO(dtr,spaj,flagKesehatan,excelListTtg,pesertaTT);
						  if(usiaDariTT > 0){
							  produk2.ii_usia_tt=usiaDariTT;
							  pesertaTT++;
						  } else if (usiaDariTT == 0) { // [141424] NANA - add validasi untuk smile hospital care DMTM BJB
							  produk2.ii_usia_tt= 1;
							  pesertaTT++;
						  }
						  
						  // usia tertanggung untuk produk ini adalah 60 maksimal 
							  if("195".indexOf(lsbs_id_utama.toString())>-1){
								if(lsbs_id_utama>=73 && lsdbs_number_utama <=84){
									if(usiaDariTT > 60){
										err = err+ "Usia Maksimal Tertanggung Tambahan adalah 60 Tahun";
									}
								}
							}
					 }
					 
					//ambil usia dari TT tambahan buat Rate Provestara
					 if("840,841,842,843".indexOf(lsbs_id_rider.toString())>-1){
						 
						 int jntertanggungtamb = 0;
						 int jumpes = 0;
						 
						 if(lsdbs_number_rider >= 18 && lsdbs_number_rider <= 24 ){ //Tertanggung II
							 pesertaTT =0;
							 jntertanggungtamb = 1;
						 }else if(lsdbs_number_rider >= 25 && lsdbs_number_rider <= 31 ){ //Tertanggung III
							 pesertaTT =1;
							 jntertanggungtamb = 2;
						 }else if(lsdbs_number_rider >= 32 && lsdbs_number_rider <= 38 ){ //Tertanggung IV
							 pesertaTT =2;
							 jntertanggungtamb = 3;
						 }else if(lsdbs_number_rider >= 39 && lsdbs_number_rider <= 45 ){ //Tertanggung V
							 pesertaTT =3;
							 jntertanggungtamb = 4;
						 }
						 
						 String namaPeserta = spaj.get(181).toString();
						 if(!namaPeserta.equals("")){
						 			String[] dataNamaPeserta = namaPeserta.split(";");
						 			List<String> listdataPeserta = Arrays.asList(dataNamaPeserta);
						 			jumpes = listdataPeserta.size();
						 			
						 }
						 
						 if(lsdbs_number_rider >= 11 && lsdbs_number_rider <= 17 ){ //Tertanggung I
							 usiaDariTT = excelListTtg.getMste_age();
						 }else{
							 if(jntertanggungtamb <= jumpes){
								 usiaDariTT = prosesSetDataPesertaUsiaSIO(dtr,spaj,flagKesehatan,excelListTtg,pesertaTT);  
							 }else{
								 //untuk produk  214 pastikan data rider tertanggung sudah sesuai di inputkan pada tabel Family2Name - Family2Nationality
								 //untuk produk rider tambahan tertanggung utama (I) (RG/RB/PK) tidak perlu menginput di kolom Family2Name - Family2Nationality
								 ///contoh pengisian
								 ////	ProdukRider													Family2Name
								 ////	841-12;840-19;841-19;842-19;840-26;841-26					SINTA;SANTI
								 ////	840-19;840-26												SINTA;SANTI
								 ////	840-19														SINTA
								 ////	841-12								
								 errorpesertatamb214 = 1;
							 }
							 
						 }
						 
						 
						  if(usiaDariTT > 0){
							  produk2.ii_usia_tt=usiaDariTT;
							 
						  }
					 }
					 
						//ambil usia dari TT tambahan buat Rate SMiLe MEDICAL PLUS 225
					 if("846,847".indexOf(lsbs_id_rider.toString())>-1){
						 
						 int jntertanggungtamb = 0;
						 int jumpes = 0;
						 
							String nama_plan="";
							Map paramkodeproduk=new HashMap();
							paramkodeproduk.put("kode_bisnis",lsbs_id_rider);
							paramkodeproduk.put("no_bisnis",lsdbs_number_rider);
							Map namaproduk = (HashMap) bacDao.selectNamaPlan(paramkodeproduk);
							if (namaproduk!=null)
							{		
								nama_plan=((String)namaproduk.get("LSDBS_NAME")).toUpperCase();
							}
						
										 
						 if(nama_plan.contains("(TERTANGGUNG II)")){ //Tertanggung II
							 pesertaTT =0;
							 jntertanggungtamb = 1;
						 }else if(nama_plan.contains("(TERTANGGUNG III)")){ //Tertanggung III
							 pesertaTT =1;
							 jntertanggungtamb = 2;
						 }else if(nama_plan.contains("(TERTANGGUNG IV)") ){ //Tertanggung IV
							 pesertaTT =2;
							 jntertanggungtamb = 3;
						 }else if(nama_plan.contains("(TERTANGGUNG V)") ){ //Tertanggung V
							 pesertaTT =3;
							 jntertanggungtamb = 4;
						 }
						 
						 String namaPeserta = spaj.get(181).toString();
						 if(!namaPeserta.equals("")){
						 			String[] dataNamaPeserta = namaPeserta.split(";");
						 			List<String> listdataPeserta = Arrays.asList(dataNamaPeserta);
						 			jumpes = listdataPeserta.size();
						 			
						 }
						 
						 if(nama_plan.contains("(TERTANGGUNG I)")){ //Tertanggung I
							 usiaDariTT = excelListTtg.getMste_age();
						 }else{
							 if(jntertanggungtamb <= jumpes){
								 usiaDariTT = prosesSetDataPesertaUsiaSIO(dtr,spaj,flagKesehatan,excelListTtg,pesertaTT);  
							 }else{
								 //untuk produk  225 pastikan data rider tertanggung sudah sesuai di inputkan pada tabel Family2Name - Family2Nationality
								 //untuk produk rider tambahan tertanggung utama (I) (RG/RB/PK) tidak perlu menginput di kolom Family2Name - Family2Nationality
								 ///contoh pengisian
								 ////	ProdukRider													Family2Name
								 ////	847-3;847-33;846-9;847-9;847-39;846-15;847-15;847-45		SINTA;SANTI
								 ////	847-1;846-7;847-7											SINTA
								 ////	847-1;847-61													
								 ////	847-2							
								 errorpesertatamb225 = 1;
							 }
							 
						 }
						 
						 
						  if(usiaDariTT > 0){
							  produk2.ii_usia_tt=usiaDariTT;
							 
						  }
					 }/////SMiLe MEDICAL PLUS 225
						
					upRider=produk2.of_get_up(total_premi,  datausulan.getMspr_tsi(), unit,  produk.flag_jenis_plan, produk2.ii_bisnis_id, produk2.ii_bisnis_no, 0);
					produk2.of_get_rate1(klas, produk.flag_jenis_plan, produk2.ii_bisnis_no, produk2.ii_usia_tt, produk2.ii_usia_pp);
					rate_rider=produk2.rate_rider;
					premi=0.;//link premi rider diset 0			
					if(datausulan.getLsbs_id()==163 || datausulan.getLsbs_id()==169 || datausulan.getLsbs_id()==183 
							|| (datausulan.getLsbs_id()==189 && datausulan.getLsdbs_number()>=33 && datausulan.getLsdbs_number()<=47 )
							|| (datausulan.getLsbs_id()==189 && (datausulan.getLsdbs_number()>=48 && datausulan.getLsdbs_number()<=62)) //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
							|| datausulan.getLsbs_id()==195 
							|| (datausulan.getLsbs_id()==204 && datausulan.getLsdbs_number()>=37 && datausulan.getLsdbs_number()<=48 )
							|| datausulan.getLsbs_id()==214 || datausulan.getLsbs_id()==221 || datausulan.getLsbs_id()==225){						
						premi = produk2.hit_premi_rider(rate_rider, upRider, produk2.idec_pct_list[datausulan.getLscb_id().intValue()], 0);
					}
					
					/* Begin [141424]
					 * Nana 30/01/2019 
					 * Lama pertanggunngan relasi anak 25 th
					 */
					
				    String relationRider = spaj.get(182).toString();
				    String[] dataRelation = relationRider.split(";");
				    List<String> dataRelationLst = Arrays.asList(dataRelation);
				    
				    if (dataRelationLst != null && 
				    		"823".indexOf(lsbs_id_rider.toString()) > -1 &&
				    		"4,8,21,22,24,25,26,27,43,45".indexOf(dataRelationLst.get(j)) > -1 &&
							 j != 0) {

						int lamaTanggung = 25 - produk2.ii_usia_tt;
						dtr.setMspr_ins_period(lamaTanggung);

					} else {
						dtr.setMspr_ins_period(produk2.li_insured);

					}
					
					/* End [141424]
					 * Nana 30/01/2019  
					 * Lama Pertanggungan */
				
//					dtr.setMspr_ins_period(produk2.li_insured);
					dtr.setMspr_tsi_pa_a(produk2.up_pa);
					dtr.setMspr_tsi_pa_b(produk2.up_pb);
					dtr.setMspr_tsi_pa_c(produk2.up_pc);
					dtr.setMspr_tsi_pa_d(produk2.up_pd);
					dtr.setMspr_rate(rate_rider);
					dtr.setMspr_premium(premi);
					dtr.setMspr_tsi(upRider);
					dtr.setMspr_unit(unit);
					dtr.setMspr_class(klas);
					dtr.setLku_id(produk2.is_kurs_id);	
					dtrd.add(dtr);
					datausulan.setDaftaRider(dtrd);
					datausulan.setJmlrider(dtrd.size());
				}  
												
				//Khusus Campaign, setelah itu dihapus
				if (excelListPp.getFlag_free_pa()!=null){
					if (excelListPp.getFlag_free_pa()==2){
						Datarider dtr=new Datarider();
						Integer lsbs_id_rider=824;
						Integer lsdbs_number_rider=1;							
						Integer unit = 0;
						Integer klas=0; 
						Double premi = 0.;
						Double upRider = 0.;

						Integer factor = 0;
						Double rate_rider = 0.;

						dtr.setMspr_beg_date(datausulan.getMspr_beg_date());	
						dtr.setLsbs_id(lsbs_id_rider);
						dtr.setLsdbs_number(lsdbs_number_rider);

						//ENDATE DAN PAYDATE RIDER								
						//produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),datausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk2.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, datausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
						Integer tanggal_akhir_polis1=tanggalEd;
						Integer bulan_akhir_polis1=bulanEd;
						Integer tahun_akhir_polis1=new Integer(2017);																	

						String tgl_akhir_polis1=null;
						tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
						if ( tgl_akhir_polis1.trim().length()!=0)
						{
							dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
						}else{
							dtr.setMspr_end_date(null);
						}
						dtr.setMspr_end_pay(null);

						premi=0.;//link premi rider diset 0			
						dtr.setMspr_ins_period(1);
						dtr.setMspr_tsi_pa_a(1000000.0);
						dtr.setMspr_tsi_pa_b(1000000.0);
						dtr.setMspr_tsi_pa_c(0.0);
						dtr.setMspr_tsi_pa_d(1000000.0);
						dtr.setMspr_rate(null);
						dtr.setMspr_premium(premi);
						dtr.setMspr_tsi(1000000.0);
						dtr.setMspr_unit(0);
						dtr.setMspr_class(1);
						dtr.setLku_id("01");	
						dtrd.add(dtr);
						datausulan.setDaftaRider(dtrd);
						datausulan.setJmlrider(dtrd.size());
					}
				}
			}else{ 
				datausulan.setJmlrider(new Integer(0));
			}
			
			
			if(datausulan.getJmlrider()!=0){
				for(int psrt=0;psrt<datausulan.getDaftaRider().size();psrt++){
					Datarider dtr2 = new Datarider();
					dtr2 = (Datarider)datausulan.getDaftaRider().get(psrt);
					if(dtr2.getLsbs_id()==820  || dtr2.getLsbs_id()==823 || dtr2.getLsbs_id()==826 || dtr2.getLsbs_id()==844)flagKesehatan++;
					if(flagKesehatan>0 && (dtr2.getLsbs_id()==820 || dtr2.getLsbs_id()==823 || dtr2.getLsbs_id()==826 || dtr2.getLsbs_id()==844) ){					
						peserta = prosesSetDataPesertaSIO(dtr2,spaj,flagKesehatan,excelListTtg,psrt,psrt+1);
						if(peserta.getNama()!=null)daftarPeserta.add(peserta);
						flagProvider++;
					}
									
					
					if(dtr2.getLsbs_id()==840  || dtr2.getLsbs_id()==841 || dtr2.getLsbs_id()==842 || dtr2.getLsbs_id()==843 || dtr2.getLsbs_id()==846 || dtr2.getLsbs_id()==847)flagKesehatan++;
					if(flagKesehatan>0 && (dtr2.getLsbs_id()==840  || dtr2.getLsbs_id()==841 || dtr2.getLsbs_id()==842 || dtr2.getLsbs_id()==843 || dtr2.getLsbs_id()==846 || dtr2.getLsbs_id()==847) ){					
						int pesertasmp = 99 ;
						int jntertanggungtamb = 0;
						int jumpes = 0;
						PesertaPlus_mix pesertaridersmp = new PesertaPlus_mix();
						
						String nama_plan="";
						Map paramkodeproduk=new HashMap();
						paramkodeproduk.put("kode_bisnis",dtr2.getLsbs_id());
						paramkodeproduk.put("no_bisnis",dtr2.getLsdbs_number());
						Map namaproduk = (HashMap) bacDao.selectNamaPlan(paramkodeproduk);
						if (namaproduk!=null)
						{		
							nama_plan=((String)namaproduk.get("LSDBS_NAME")).toUpperCase();
						}
					
						if(nama_plan.contains("(TERTANGGUNG II)")){ //Tertanggung II
							 pesertasmp =0;
							 jntertanggungtamb = 1;
						 }else if(nama_plan.contains("(TERTANGGUNG III)")){ //Tertanggung III
							 pesertasmp =1;
							 jntertanggungtamb = 2;
						 }else if(nama_plan.contains("(TERTANGGUNG IV)")){ //Tertanggung IV
							 pesertasmp =2;
							 jntertanggungtamb = 3;
						 }else if(nama_plan.contains("(TERTANGGUNG V)")){ //Tertanggung V
							 pesertasmp =3;
							 jntertanggungtamb = 4;
						 }
						
						String namaPeserta = spaj.get(181).toString();
						 if(!namaPeserta.equals("")){
						 			String[] dataNamaPeserta = namaPeserta.split(";");
						 			List<String> listdataPeserta = Arrays.asList(dataNamaPeserta);
						 			jumpes = listdataPeserta.size();
						 			
						 }
						 
						 
						 if(nama_plan.contains("(TERTANGGUNG I)")){ //Tertanggung I
							//cek jenkel untuk peserta Rawat Bersalin (khusus wanita)
								if(nama_plan.contains(" RB ")){
									if(excelListTtg.getMspe_sex() == 1){
										err = err+" "+",\n Rawat Bersalin Tertanggung Utama Hanya bisa diambil oleh Wanita";
									}
									if(excelListTtg.getLsre_id() != 1 && excelListTtg.getLsre_id() != 5 ){
										err = err+" "+",\n Rawat Bersalin Hanya bisa diambil jika hubungan tertanggung utama dengan pemegang polis adalah Diri sendiri atau Suami/Istri";
									}
								 }	
								
								if(nama_plan.contains(" RJ ")){
									ridertu847RJ=1; 
								}
								
								if(nama_plan.contains(" RG ")){
									ridertu847RG=1; 
								}
								
								if(nama_plan.contains(" PK ")){
									ridertu847PK=1; 
								}
							 
							 pesertaridersmp.setNama(excelListTtg.getMcl_first());
							 pesertaridersmp.setNo_reg("1a");
							 pesertaridersmp.setFlag_jenis_peserta(0);
							 pesertaridersmp.setNo_urut(psrt+1);
							 pesertaridersmp.setLsre_id(excelListTtg.getLsre_id());
							 pesertaridersmp.setUmur(excelListTtg.getMste_age());
							 pesertaridersmp.setKelamin(excelListTtg.getMspe_sex());
							 pesertaridersmp.setTanggal_lahir(excelListTtg.getMspe_date_birth());
							 pesertaridersmp.setPremi(dtr2.getMspr_premium());
							 pesertaridersmp.setNext_send(dtr2.getMspr_beg_date());
							 pesertaridersmp.setLsbs_id(dtr2.getLsbs_id());
							 pesertaridersmp.setLsdbs_number(dtr2.getLsdbs_number());
								
						 }else{
							 							 
							 if(jntertanggungtamb <= jumpes){
								 pesertaridersmp = prosesSetDataPesertaSIO(dtr2,spaj,flagKesehatan,excelListTtg,pesertasmp, psrt+1);
								 
															 
								 if (pesertaridersmp.getLsbs_id() == 846){//SMiLe MEDICAL PLUS RI Tambahan
									 if(jntertanggungtamb == 1){
										 	rider846RIt2=1; 
										}
									 else if(jntertanggungtamb == 2){
										 	rider846RIt3=1; 
										}
									 else if(jntertanggungtamb == 3){
										 	rider846RIt4=1; 
										}
									 else if(jntertanggungtamb == 4){
										 	rider846RIt5=1; 
										}
								 } 
								 
								 if (pesertaridersmp.getLsbs_id() == 847 && nama_plan.contains(" RJ ") ){//SMiLe MEDICAL PLUS RJ tambahan
									 if(jntertanggungtamb == 1){
										 	rider847RJt2=1; 
										}
									 else if(jntertanggungtamb == 2){
										 	rider847RJt3=1; 
										}
									 else if(jntertanggungtamb == 3){
										 	rider847RJt4=1; 
										}
									 else if(jntertanggungtamb == 4){
										 	rider847RJt5=1; 
										}
								 } 
								 
								 if (pesertaridersmp.getLsbs_id() == 847 && nama_plan.contains(" RG ") ){//SMiLe MEDICAL PLUS RG tambahan
									 if(jntertanggungtamb == 1){
										 	rider847RGt2=1; 
										}
									 else if(jntertanggungtamb == 2){
										 	rider847RGt3=1; 
										}
									 else if(jntertanggungtamb == 3){
										 	rider847RGt4=1; 
										}
									 else if(jntertanggungtamb == 4){
										 	rider847RGt5=1; 
										}
								 }
								 
								 if (pesertaridersmp.getLsbs_id() == 847 && nama_plan.contains(" PK ") ){//SMiLe MEDICAL PLUS PK tambahan
									 if(jntertanggungtamb == 1){
										 	rider847PKt2=1; 
										}
									 else if(jntertanggungtamb == 2){
										 	rider847PKt3=1; 
										}
									 else if(jntertanggungtamb == 3){
										 	rider847PKt4=1; 
										}
									 else if(jntertanggungtamb == 4){
										 	rider847PKt5=1; 
										}
								 }
								 
								 if (pesertaridersmp.getLsbs_id() == 840){//RI + RJ Provestara
									 if(jntertanggungtamb == 1){
											rider840t2=1; 
										}
									 else if(jntertanggungtamb == 2){
											rider840t3=1; 
										}
									 else if(jntertanggungtamb == 3){
											rider840t4=1; 
										}
									 else if(jntertanggungtamb == 4){
											rider840t5=1; 
										}
									 
								 } 
								 
								 if (pesertaridersmp.getLsbs_id() == 841){//RG Provestara
									 if(jntertanggungtamb == 1){
											rider841t2=1; 
										}
									 else if(jntertanggungtamb == 2){
											rider841t3=1; 
										}
									 else if(jntertanggungtamb == 3){
											rider841t4=1; 
										}
									 else if(jntertanggungtamb == 4){
											rider841t5=1; 
										}
									 
								 } 
								 if (pesertaridersmp.getLsbs_id() == 843){//Pk Provestara
									 if(jntertanggungtamb == 1){
											rider843t2=1; 
										}
									 else if(jntertanggungtamb == 2){
											rider843t3=1; 
										}
									 else if(jntertanggungtamb == 3){
											rider843t4=1; 
										}
									 else if(jntertanggungtamb == 4){
											rider843t5=1; 
										}
								 }								 
								 
								 if (nama_plan.contains(" RB ")){
									 if(pesertaridersmp.getKelamin() == 1){
											err = err+" "+", Rawat Bersalin Tertanggung Tambahan Hanya bisa diambil oleh Wanita";
										}
									 if(pesertaridersmp.getLsre_id() != 5 ){
											err = err+" "+",\n Rawat Bersalin Hanya bisa diambil jika hubungan tertanggung tambahan dengan pemegang polis adalah Suami/Istri";
										}
								 }
							 }else{
								 //untuk produk  214 pastikan data rider tertanggung sudah sesuai di inputkan pada tabel Family2Name - Family2Nationality
								 //untuk produk rider tambahan tertanggung utama (I) (RG/RB/PK) tidak perlu menginput di kolom Family2Name - Family2Nationality
								 ///contoh pengisian
								 ////	ProdukRider													Family2Name
								 ////	841-12;840-19;841-19;842-19;840-26;841-26					SINTA;SANTI
								 ////	840-19;840-26												SINTA;SANTI
								 ////	840-19														SINTA
								 ////	841-12								
								 errorpesertatamb214 = 1;
								 errorpesertatamb225 = 1;
							 }
							 
							
						 }
						
						if(peserta.getNama()!=null)daftarPeserta.add(pesertaridersmp);
						flagProvider++;
					}
				}
				if(!daftarPeserta.isEmpty()){
					datausulan.setDaftapeserta(daftarPeserta);
					datausulan.setJml_peserta(daftarPeserta.size());
				}else{
					datausulan.setJml_peserta(0);
				}
			}	
			
			//khusus produk 214 / 225 Smile Medical Plus Bukopin
			if (errorpesertatamb214 != 0 || errorpesertatamb225 != 0 ){
				 err = err+" "+",\n Data Rider dengan data Tertanggung Tambahan tidak sesuai, mohon periksa kembali";
			}
			
			if(((rider847RGt2 == 1 || rider847PKt2 == 1) && (rider846RIt2 == 0 || rider847RJt2 == 0 )) 
				|| ((rider847RGt3 == 1 || rider847PKt3 == 1) && (rider846RIt3 == 0 || rider847RJt3 == 0 ))
				|| ((rider847RGt4 == 1 || rider847PKt4 == 1) && (rider846RIt4 == 0 || rider847RJt4 == 0 ))
				|| ((rider847RGt5 == 1 || rider847PKt5 == 1) && (rider846RIt5 == 0 || rider847RJt5 == 0 ))){
				 err = err+" "+",\n Data Rider RG/PK Tertanggung Tambahan harus mengambil Rider RI dan RJ, mohon periksa kembali";
			}
			
			if((ridertu847RG != rider847RGt2 && rider846RIt2 == 1 && rider847RJt2 == 1) 
				|| (ridertu847RG != rider847RGt3 && rider846RIt3 == 1 && rider847RJt3 == 1) 
				|| (ridertu847RG != rider847RGt4 && rider846RIt4 == 1 && rider847RJt4 == 1) 
				|| (ridertu847RG != rider847RGt5 && rider846RIt5 == 1 && rider847RJt5 == 1) ){
				 err = err+" "+",\n Data Rider RG Tertanggung Tambahan harus sesuai dengan Rider RG Tertanggung Utama, mohon periksa kembali";
			}
			
			if((ridertu847PK != rider847PKt2 && rider846RIt2 == 1 && rider847RJt2 == 1) 
					|| (ridertu847PK != rider847PKt3 && rider846RIt3 == 1 && rider847RJt3 == 1) 
					|| (ridertu847PK != rider847PKt4 && rider846RIt4 == 1 && rider847RJt4 == 1) 
					|| (ridertu847PK != rider847PKt5 && rider846RIt5 == 1 && rider847RJt5 == 1) ){
					 err = err+" "+",\n Data Rider PK Tertanggung Tambahan harus sesuai dengan Rider RG Tertanggung Utama, mohon periksa kembali";
				}
			
			if((ridertu841 != rider841t2 && rider840t2 == 1) || (ridertu841 != rider841t3  && rider840t3 == 1) 
					|| (ridertu841 != rider841t4  && rider840t4 == 1) || (ridertu841 != rider841t5  && rider840t5 == 1)){
				 err = err+" "+",\n Data Rider RG Tertanggung Tambahan harus sesuai dengan Rider RG Tertanggung Utama, mohon periksa kembali";
			}
			if((ridertu843 != rider843t2 && rider840t2 == 1) || (ridertu843 != rider843t3 && rider840t3 == 1) 
					|| (ridertu843 != rider843t4 && rider840t4 == 1) || (ridertu843 != rider843t5 && rider840t5 == 1)){
				 err = err+" "+",\n Data Rider PK Tertanggung Tambahan harus sesuai dengan Rider PK Tertanggung Utama, mohon periksa kembali";
			}
			
			//Khusus Campaign, setelah itu dihapus
			if (excelListPp.getFlag_free_pa()!=null){
				if (kode_rider.equals("") && excelListPp.getFlag_free_pa()==2){
					int tahun_rider,bulan_rider,tanggal_rider;String nama_produk_rider=null;
					Datarider dtr=new Datarider();
					Integer lsbs_id_rider=824;
					Integer lsdbs_number_rider=1;							
					Integer unit = 0;
					Integer klas=0; 
					Double premi = 0.;
					Double upRider = 0.;

					Integer factor = 0;
					Double rate_rider = 0.;

					nama_produk_rider="produk_asuransi.n_prod_"+lsbs_id_rider;
					aClass2 = Class.forName( nama_produk_rider);
					produk2 = (n_prod)aClass2.newInstance();	
					produk2.setSqlMap(uwDao.getSqlMapClient());
					dtr.setMspr_beg_date(datausulan.getMspr_beg_date());	
					dtr.setLsbs_id(lsbs_id_rider);
					dtr.setLsdbs_number(lsdbs_number_rider);

					//ENDATE DAN PAYDATE RIDER								
					//produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),datausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk2.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, datausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
					Integer tanggal_akhir_polis1=tanggalEd;
					Integer bulan_akhir_polis1=bulanEd;
					Integer tahun_akhir_polis1=new Integer(2017);																	

					String tgl_akhir_polis1=null;
					tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
					if ( tgl_akhir_polis1.trim().length()!=0)
					{
						dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
					}else{
						dtr.setMspr_end_date(null);
					}
					dtr.setMspr_end_pay(null);

					premi=0.;//link premi rider diset 0			
					dtr.setMspr_ins_period(1);
					dtr.setMspr_tsi_pa_a(1000000.0);
					dtr.setMspr_tsi_pa_b(1000000.0);
					dtr.setMspr_tsi_pa_c(0.0);
					dtr.setMspr_tsi_pa_d(1000000.0);
					dtr.setMspr_rate(null);
					dtr.setMspr_premium(premi);
					dtr.setMspr_tsi(1000000.0);
					dtr.setMspr_unit(0);
					dtr.setMspr_class(1);
					dtr.setLku_id("01");	
					dtrd.add(dtr);
					datausulan.setDaftaRider(dtrd);
					datausulan.setJmlrider(dtrd.size());
				}
			}
			datausulan.setJmlrider_include(new Integer(0));
			if(flagProvider>0)datausulan.setMspo_provider(2);
			mapDataUsulan.put("ERR", err);
			mapDataUsulan.put("DATAUSULAN", datausulan);
		}catch (Exception e) {
			logger.error("ERROR :", e);
			err = err+" "+", Ada Kesalahan pada saat set data usulan";
			mapDataUsulan.put("ERR", err);
			mapDataUsulan.put("DATAUSULAN", datausulan);
			return mapDataUsulan;
		}
		return mapDataUsulan;
	}

	public HashMap setDataInvestasi(List spaj, Datausulan datausulan, BacDao bacDao, SuratUnitLink suratUnitLink)  {
		InvestasiUtama investUtama = new InvestasiUtama();
		HashMap<String, Serializable> mapDataInvest = new HashMap<String, Serializable>();
		String err="";
		ArrayList<DetilInvestasi>di=new ArrayList<DetilInvestasi>();
		ArrayList<Biayainvestasi>bi=new ArrayList<Biayainvestasi>();
		try{
			Double premi_berkala=Double.parseDouble(spaj.get(136).toString().replace(".0", ""));
			Double premi_tunggal=Double.parseDouble(spaj.get(188).toString().replace(".0", ""));
			//Double total_premi=datausulan.getMspr_premium()+premi_berkala;
			DetilTopUp detilTopup=new DetilTopUp();
			investUtama.setDaftartopup(detilTopup);

			if(premi_berkala>0){
				investUtama.getDaftartopup().setPremi_berkala(premi_berkala);
				investUtama.getDaftartopup().setPil_berkala(1);
			}else{
				investUtama.getDaftartopup().setPil_berkala(0);
				investUtama.getDaftartopup().setPremi_berkala(new Double(0));
			}

			if(premi_tunggal>0){
				investUtama.getDaftartopup().setPremi_tunggal(premi_tunggal);
				investUtama.getDaftartopup().setPil_tunggal(1);
			}else{
				investUtama.getDaftartopup().setPremi_tunggal(new Double(0));
				investUtama.getDaftartopup().setPil_tunggal(0);
			}
			bi=prosesSetDataBiayaInvestasi(spaj,investUtama,datausulan,bacDao,suratUnitLink);
			investUtama.setDaftarbiaya(bi);
			investUtama.setJmlh_biaya(bi.size());
			di=prosesSetDataDetilInvestasi(spaj,investUtama,datausulan,bacDao,suratUnitLink);
			investUtama.setDaftarinvestasi(di);
			investUtama.setJmlh_invest(di.size());
			mapDataInvest.put("ERR", err);
			mapDataInvest.put("DATA", investUtama);
		}catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			err = err+" "+", Ada Kesalahan pada saat set data investasi";
			mapDataInvest.put("ERR", err);
			mapDataInvest.put("DATA", investUtama);
			return mapDataInvest;
		}
		return mapDataInvest;
	}

	private ArrayList<Biayainvestasi> prosesSetDataBiayaInvestasi(List spaj,InvestasiUtama excelListInvestasi, Datausulan datausulan,BacDao bacDao,SuratUnitLink suratUnitLink) {		
		Integer index_biaya=new Integer(0);
		Integer number_produk=datausulan.getLsdbs_number();
		Integer payperiod = datausulan.getMspo_pay_period();
		Integer insperiod =  datausulan.getMspo_ins_period();
		Integer umurttg = datausulan.getLi_umur_ttg();
		Integer cara_bayar = datausulan.getLscb_id();
		Integer[] mbu_jenis;
		String[] mbu_nama_jenis;
		Double[] mbu_jumlah;
		Double[] mbu_persen;
		Double ldec_awal=new Double(0);
		Double ld_bia_ass=new Double(0);
		Double ld_bia_akui=new Double(0);
		Integer li_insper=new Integer(0);
		//String hasil_biaya="";
		Double mbu_jumlah1=new Double(0);
		String mbu_jumlah1_1="";
		Double mbu_persen1=new Double(0);
		Double mbu_jumlah2=new Double(0);
		String mbu_jumlah2_2="";
		Double mbu_persen2=new Double(0);
		Double mbu_jumlah3=new Double(0);
		String mbu_jumlah3_3="";		
		Double mbu_persen3=new Double(0);
		Integer[] klas_1;
		Integer[] unit_1;
		String[] kode_rider;
		String[] number_rider;	
		Integer[] f_Up;
		//Double jumlah_minus = new Double(0);
		boolean flag_minus = false;
		Integer idx=new Integer(0);
		Integer kode_produk=datausulan.getLsbs_id();
		Double total_mbu_jumlah=new Double(0);
		Double ld_premi_invest=new Double(0);
		Integer ldec_pct=new Integer(1);
		ArrayList dtby = new ArrayList();
		Biayainvestasi by1= new Biayainvestasi();

		String nama_produk="";
		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}

		
		try {
			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(bacDao.getSqlMapClient());
			produk.is_kurs_id=datausulan.getKurs_premi();
			if(produk.ii_bisnis_no==0){
				produk.ii_bisnis_id_utama= datausulan.getLsbs_id();
				produk.ii_bisnis_no_utama= datausulan.getLsdbs_number();
			}
			Integer jmlh_rider = datausulan.getJmlrider();
			idx=new Integer(0);
			if (produk.flag_rider==0)
			{
				index_biaya=new Integer(4);	
			}else{
				if (produk.flag_rider==1)
				{
					index_biaya=new Integer(3 + jmlh_rider.intValue() + 4);
				}else{
					index_biaya=new Integer(6);
				}
			}
			
			mbu_jumlah = new Double[index_biaya.intValue()];
			mbu_persen = new Double[index_biaya.intValue()];
			mbu_jenis = new Integer[index_biaya.intValue()];
			mbu_nama_jenis = new String[index_biaya.intValue()];
			ld_bia_akui = suratUnitLink.getBiayaAkuisisi(datausulan.getLscb_id(), "0", kode_produk.toString(), datausulan.getLsdbs_number(), datausulan.getMspo_pay_period(), 1, produk);
			ld_bia_ass=new Double(produk.cek_rate(kode_produk.intValue(),datausulan.getLscb_id(),payperiod.intValue(),li_insper.intValue(),umurttg.intValue(),datausulan.getKurs_premi(),insperiod.intValue()));

			produk.biaya1(ld_bia_akui.doubleValue(),datausulan.getMspr_premium());
			mbu_jumlah1=new Double(produk.mbu_jumlah1);
			mbu_persen1=new Double(produk.mbu_persen1);
			BigDecimal bd1 = new BigDecimal(mbu_jumlah1.doubleValue());
			mbu_jumlah1_1=bd1.setScale(3,BigDecimal.ROUND_HALF_UP).toString();
			mbu_jenis[0]=new Integer(1);
			mbu_nama_jenis[0]="BIAYA AKUISISI";
			mbu_jumlah[0]=new Double(Double.parseDouble(mbu_jumlah1_1));
			mbu_persen[0]=mbu_persen1;
			idx=new Integer(1);
			by1.setLjb_biaya(mbu_nama_jenis[0]);
			by1.setLjb_id((mbu_jenis[0]));
			by1.setMbu_jumlah((mbu_jumlah[0]));
			by1.setMbu_persen((mbu_persen[0]));
			by1.setMu_ke(new Integer(1));			
			dtby.add(by1);
			total_mbu_jumlah = total_mbu_jumlah+mbu_jumlah[0];

			by1= new Biayainvestasi();
			produk.biaya2(ld_bia_ass.doubleValue(),datausulan.getMspr_premium().doubleValue(),ldec_pct.intValue(),datausulan.getMspr_tsi());
			mbu_jumlah2=new Double(produk.mbu_jumlah2);
			mbu_persen2=new Double(produk.mbu_persen2);
			BigDecimal bd2 = new BigDecimal(mbu_jumlah2.doubleValue());
			mbu_jumlah2_2=bd2.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
			mbu_jenis[1]=new Integer(2);
			mbu_nama_jenis[1]="BIAYA ASURANSI";
			mbu_jumlah[1]=new Double(Double.parseDouble(mbu_jumlah2_2));
			mbu_persen[1]=mbu_persen2;	
			idx=new Integer(2);
			by1.setLjb_biaya(mbu_nama_jenis[1]);
			by1.setLjb_id((mbu_jenis[1]));
			by1.setMbu_jumlah((mbu_jumlah[1]));
			by1.setMbu_persen((mbu_persen[1]));
			by1.setMu_ke(new Integer(1));
			by1.setMbu_end_pay(datausulan.getMspr_beg_date());
			if ((kode_produk==190 || kode_produk==191 || (kode_produk==200 && number_produk == 7) )){	//untuk SMile link 99 && ULTIMATE SYARIAH biaya ass dan admin dibebaskan pada tahun pertama					
				by1.setMbu_jumlah((0.0));
				mbu_jumlah[1]=0.0;
				by1.setMbu_persen((0.0));

			}
			dtby.add(by1);
			total_mbu_jumlah = total_mbu_jumlah+mbu_jumlah[1];
			ldec_awal=new Double(produk.cek_awal());
			by1= new Biayainvestasi();
			produk.biaya3(ldec_awal.doubleValue());
			mbu_jumlah3=new Double(produk.mbu_jumlah3);
			mbu_persen3=new Double(produk.mbu_persen3);
			BigDecimal bd3 = new BigDecimal(mbu_jumlah3.doubleValue());
			mbu_jumlah3_3=bd3.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
			mbu_jenis[2]=new Integer(3);
			mbu_nama_jenis[2]="BIAYA ADMINISTRASI";
			mbu_jumlah[2]=new Double(Double.parseDouble(mbu_jumlah3_3));
			mbu_persen[2]=mbu_persen3;	
			idx=new Integer(3);
			by1.setLjb_biaya(mbu_nama_jenis[2]);
			by1.setLjb_id((mbu_jenis[2]));
			by1.setMbu_jumlah((mbu_jumlah[2]));
			by1.setMbu_persen((mbu_persen[2]));
			by1.setMu_ke(new Integer(1));
			by1.setMbu_end_pay(datausulan.getMspr_beg_date());
			if ((kode_produk==190 || kode_produk==191 || (kode_produk==200 && number_produk == 7))){						
				by1.setMbu_jumlah((0.0));
				mbu_jumlah[2]=0.0;
				by1.setMbu_persen((0.0));

			}
			dtby.add(by1);
			total_mbu_jumlah = total_mbu_jumlah+mbu_jumlah[2];
			Integer flag_topup = 0;
			if(excelListInvestasi.getDaftartopup().getPil_berkala()>0){
				by1= new Biayainvestasi();
				Double biaya_topup = new Double(0);
				biaya_topup = new Double(excelListInvestasi.getDaftartopup().getPremi_berkala() * produk.li_pct_biaya / 100);
				String biaya_topup1=null;
				BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
				biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
				idx=new Integer(idx.intValue()+1);
				if (produk.flag_as == 2 || produk.ii_bisnis_id==191)
				{
					by1.setMbu_jumlah(new Double(new Double(0)));
					by1.setMbu_persen(new Double(0));		
				}else{
					by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
					by1.setMbu_persen(new Double(produk.li_pct_biaya));		
				}
				by1.setLjb_biaya("BIAYA TOP-UP");
				by1.setLjb_id(new Integer(4));
				by1.setMu_ke(new Integer(2));
				dtby.add(by1);
				flag_topup=1;
			}

			if(excelListInvestasi.getDaftartopup().getPil_tunggal()>0){
				by1= new Biayainvestasi();
				Double biaya_topup = new Double(0);
				biaya_topup = new Double(excelListInvestasi.getDaftartopup().getPremi_tunggal().doubleValue() * produk.li_pct_biaya / 100);
				String biaya_topup1=null;
				BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
				biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
				idx=new Integer(idx.intValue()+1);
				if (produk.flag_as == 2 || produk.ii_bisnis_id==191)
				{
					by1.setMbu_jumlah(new Double(new Double(0)));
					by1.setMbu_persen(new Double(0));		
				}else{
					by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
					by1.setMbu_persen(new Double(produk.li_pct_biaya));	
				}
				by1.setLjb_biaya("BIAYA TOP-UP");
				by1.setLjb_id(new Integer(4));
				if (flag_topup == 0)
				{
					by1.setMu_ke(new Integer(2));
				}else{
					by1.setMu_ke(new Integer(3));
				}
				dtby.add(by1);
			}

			//biaya rider
			List dtrd = datausulan.getDaftaRider();			
			Double premi_pokok = datausulan.getMspr_premium();
			Double total_premi = datausulan.getTotal_premi_kombinasi();
			if(datausulan.getJmlrider()>0){
				kode_rider = new String[dtrd.size()];
				number_rider = new String[dtrd.size()];
				klas_1 = new Integer[dtrd.size()];
				unit_1 = new Integer[dtrd.size()];
				f_Up = new Integer[dtrd.size()];
				for (int i=0 ;i<dtrd.size() ; i++)
				{
					by1=new Biayainvestasi();
					Datarider rd1=(Datarider)dtrd.get(i);
					if(i>0) idx=new Integer(idx.intValue()+1);
					kode_rider[i]=Integer.toString(rd1.getLsbs_id());
					number_rider[i]=Integer.toString(rd1.getLsdbs_number().intValue());

					klas_1[i]=rd1.getMspr_class();
					unit_1[i]=rd1.getMspr_unit();
					f_Up[i]=rd1.getPersenUp();

					String nama_produk1="";
					if (Integer.parseInt(kode_rider[i])<900 )
					{
						if (kode_rider[i].trim().length()==1)
						{
							nama_produk1="produk_asuransi.n_prod_0"+kode_rider[i];	
						}else{
							nama_produk1="produk_asuransi.n_prod_"+kode_rider[i];	
						}
						Class aClass1 = Class.forName( nama_produk1 );
						n_prod produk1 = (n_prod)aClass1.newInstance();
						produk1.setSqlMap(bacDao.getSqlMapClient());
						if(produk1.ii_bisnis_no_utama==0){
							produk1.ii_bisnis_id_utama= rd1.getLsbs_id();
							produk1.ii_bisnis_no_utama= rd1.getLsdbs_number();
						}

						produk1.count_rate(1,1,kode_produk.intValue(),Integer.parseInt(number_rider[i]),datausulan.getKurs_premi(),umurttg.intValue(),datausulan.getLi_umur_pp(),
								rd1.getMspr_tsi(),total_premi,datausulan.getLscb_id(),1,insperiod.intValue(),payperiod.intValue());
						mbu_jumlah[idx.intValue()]=new Double(produk1.mbu_jumlah);
						BigDecimal mbu1 = new BigDecimal(mbu_jumlah[idx.intValue()].doubleValue());
						mbu1=mbu1.setScale(2,BigDecimal.ROUND_HALF_UP);
						mbu_jumlah[idx.intValue()]=new Double(mbu1.doubleValue());
						mbu_persen[idx.intValue()]=new Double(produk1.mbu_persen);
						Map data = null;
						data = (HashMap) bacDao.selectjenisbiaya((String)kode_rider[i],(String)number_rider[i]);

						if (data!=null && Integer.parseInt(kode_rider[i]) != 838 && Integer.parseInt(kode_rider[i]) != 839)
						{
							mbu_jenis[idx.intValue()]=new Integer(Integer.parseInt(data.get("LJB_ID").toString()));
							mbu_nama_jenis[idx.intValue()]=data.get("LJB_BIAYA").toString();	
							by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
							by1.setMu_ke(new Integer(1));
							by1.setLjb_id((mbu_jenis[idx.intValue()]));
							by1.setMbu_jumlah(mbu_jumlah[idx.intValue()]);
							by1.setMbu_persen(mbu_persen[idx.intValue()]);
							
							dtby.add(by1);
							total_mbu_jumlah = total_mbu_jumlah+mbu_jumlah[idx.intValue()];
						}	
					}		
				}
			}
			
			ld_premi_invest = new Double( premi_pokok-total_mbu_jumlah);
			if (ld_premi_invest.doubleValue()< 0)
			{
				by1= new Biayainvestasi();
				idx=new Integer(idx.intValue()+1);
				flag_minus = true;
				
				mbu_jumlah[idx.intValue()]=ld_premi_invest;

				mbu_persen[idx.intValue()]=new Double(0);
				by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
				by1.setMbu_persen((mbu_persen[idx.intValue()]));
				
				mbu_jenis[idx.intValue()]=new Integer(99);
				mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
				by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
				by1.setLjb_id((mbu_jenis[idx.intValue()]));
				by1.setMu_ke(new Integer(1));
				dtby.add(by1);

				if ((excelListInvestasi.getDaftartopup().getPremi_berkala()>0) && (excelListInvestasi.getDaftartopup().getPremi_tunggal()>0))
				{
					Double kekurangan =new Double(0 - ld_premi_invest.doubleValue());
					Double biaya_berkala = new Double(0);
					if (excelListInvestasi.getDaftartopup().getPremi_berkala()>0)
					{
						by1= new Biayainvestasi();
						Double biaya_topup = new Double(0);
						Double biaya_topup_sementara = new Double(excelListInvestasi.getDaftartopup().getPremi_berkala() * produk.li_pct_biaya / 100);
						biaya_topup = new Double(excelListInvestasi.getDaftartopup().getPremi_berkala() - biaya_topup_sementara.doubleValue() - kekurangan.doubleValue());
						if (biaya_topup.doubleValue() < 0)
						{
							biaya_topup = new Double(excelListInvestasi.getDaftartopup().getPremi_berkala() - biaya_topup_sementara.doubleValue());
						}else{
							biaya_topup =kekurangan.doubleValue();
						}
						biaya_berkala = biaya_topup;
						String biaya_topup1=null;
						BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
						biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
						idx=new Integer(idx.intValue()+1);
						by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
						by1.setMbu_persen(new Double(0));						
						by1.setLjb_biaya("KEKURANGAN PREMI");
						by1.setLjb_id(new Integer(99));
						by1.setMu_ke(new Integer(2));
						dtby.add(by1);
					}
					if (excelListInvestasi.getDaftartopup().getPremi_tunggal()>0)
					{
						if ((biaya_berkala.doubleValue()- kekurangan.doubleValue())<0)
						{
							by1= new Biayainvestasi();
							Double biaya_topup = new Double(0);
							biaya_topup = new Double(( kekurangan.doubleValue() - biaya_berkala.doubleValue()));
							String biaya_topup1=null;
							BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
							biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							idx=new Integer(idx.intValue()+1);
							by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
							by1.setMbu_persen(new Double(0));						
							by1.setLjb_biaya("KEKURANGAN PREMI");
							by1.setLjb_id(new Integer(99));
							by1.setMu_ke(new Integer(3));
							dtby.add(by1);
						}
					}							
				}else{
					by1= new Biayainvestasi();
					idx=new Integer(idx.intValue()+1);
					mbu_jumlah[idx.intValue()]=new Double(0 - ld_premi_invest.doubleValue());
					mbu_persen[idx.intValue()]=new Double(0);
					mbu_jenis[idx.intValue()]=new Integer(99);
					mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
					by1.setMbu_jumlah(mbu_jumlah[idx.intValue()]);
					by1.setMbu_persen((mbu_persen[idx.intValue()]));						
					by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
					by1.setLjb_id((mbu_jenis[idx.intValue()]));
					by1.setMu_ke(new Integer(2));
					dtby.add(by1);
					excelListInvestasi.setDaftarbiaya(dtby);
					excelListInvestasi.setJmlh_biaya(idx);
				}
			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
		return dtby;
	}


	private ArrayList<DetilInvestasi> prosesSetDataDetilInvestasi(
			List spaj, InvestasiUtama excelListInvestasi, Datausulan datausulan,BacDao bacDao, SuratUnitLink suratUnitLink) {

		ArrayList<DetilInvestasi>di=new ArrayList<DetilInvestasi>();

		String kode_invest=spaj.get(143).toString();
		String percentage=spaj.get(144).toString();
		if(!kode_invest.equals("")){
			String[] dataInvest=kode_invest.split(";");
			List<String> listDetInvest = Arrays.asList(dataInvest);	
			String[] persenInvest=percentage.split(";");
			List<String> listspersenInvest = Arrays.asList(persenInvest);		
			for(int i=0; i<listDetInvest.size(); i++){
				String detInv=listDetInvest.get(i);
				String perInv=listspersenInvest.get(i);
				DetilInvestasi detInvest=new DetilInvestasi();
				detInvest.setLji_id1(detInv);
				detInvest.setMdu_persen1(Integer.parseInt(perInv));
				if(detInvest.getMdu_persen1()>0){
					detInvest.setMdu_jumlah1(
						datausulan.getMspr_premium() * detInvest.getMdu_persen1()/100); //premi pokok
					detInvest.setMdu_jumlah_top(
						excelListInvestasi.getDaftartopup().getPremi_tunggal() * detInvest.getMdu_persen1()/100 * (100-0)/100); //premi topup tunggal
					detInvest.setMdu_jumlah_top_tunggal(
						excelListInvestasi.getDaftartopup().getPremi_berkala() * detInvest.getMdu_persen1()/100 * (100-0)/100); //premi topup berkala
				}
				di.add(detInvest);
			}	
		}
		return di;
	}
	
	/**
	 * @author lufi
	 * u/ Set Data AddressBilling Untuk Format SMile Proritas Dari kolom Excel yg diupload
	 * @param spajExcelList,excelListAddrBilling,excelListagen,excelListPp,index 
	 * @return
	 * @since Oct 2014
	 */
	public HashMap prosesSetDataAlamatPenagihanLink(List spaj,InvestasiUtama excelListInvestasi, Datausulan datausulan,BacDao bacDao,Pemegang pemegang,int index) {
        HashMap mapDataAddressBilling = new HashMap();
        AddressBilling AddBill = new AddressBilling();
        String err="";        
		
        try{
        	  AddBill.setLsne_id(pemegang.getLsne_id());
              Integer flagAddBil = Integer.parseInt(spaj.get(189).toString().replace("0.",""));
              if(flagAddBil == 1){//rumah
              	   AddBill.setMsap_address(pemegang.getAlamat_rumah());
              	   AddBill.setKota(pemegang.getKota_rumah());
              	   AddBill.setKota_tagih(pemegang.getKota_rumah());
              	   AddBill.setMsap_area_code1(pemegang.getArea_code_rumah());
              	   AddBill.setMsap_phone1(pemegang.getTelpon_rumah());
              	   AddBill.setNo_hp(pemegang.getNo_hp());        	   
              	   AddBill.setMsap_contact(pemegang.getMcl_first());
              	   AddBill.setTagih("2");
              }else if(flagAddBil == 2){//kantor
              	   AddBill.setMsap_address(pemegang.getAlamat_kantor());
           	       AddBill.setKota(pemegang.getKota_kantor());
           	       AddBill.setKota_tagih(pemegang.getKota_kantor());
           	       AddBill.setMsap_area_code1(pemegang.getArea_code_kantor());
           	       AddBill.setMsap_phone1(pemegang.getTelpon_kantor());
           	       AddBill.setNo_hp(pemegang.getNo_hp());     	     
           	       AddBill.setMsap_contact(pemegang.getMcl_first());
           	       AddBill.setTagih("3");
              }
              AddBill.setE_mail(pemegang.getEmail());
              mapDataAddressBilling.put("ADDR", AddBill);
              mapDataAddressBilling.put("ERR", err);
        }catch (Exception e) {
        	logger.error("ERROR :", e);
        	err = err+" "+", Ada Kesalahan pada saat set data alamat penagihan";
        	mapDataAddressBilling.put("ERR", err);
        	mapDataAddressBilling.put("ADDR", AddBill);
			return mapDataAddressBilling;
		}

		return mapDataAddressBilling;
	}
	

	private String getMclId(Integer flag, Integer year, BacDao bacDao ) {
		
		String mcl="";
		try {
			
		Long intIDCounter = (Long)bacDao.select_counter(194, "01"); 		
		BigDecimal intIDCounter2=new BigDecimal(Long.parseLong(fmYearSpaj.toString().concat("000000"))+intIDCounter.longValue());
		if(flag==1)mcl ="PP"+intIDCounter2;
		if(flag==2)mcl ="TT"+intIDCounter2;
		if(flag==3)mcl ="PY"+intIDCounter2;		
		bacDao.update_counter(new Long(intIDCounter.longValue()+1).toString(), 194, "01");
		} catch (Exception e) {			
			logger.error("ERROR :", e);
		}
		return mcl;
	}

	private Integer gethitungUmur(Date bdate, Date tglLahir) {
		f_hit_umur umr = new f_hit_umur();
		Integer getumur = 0;
		tahun = Integer.parseInt(sdfYear.format(bdate));
		bulan = Integer.parseInt(sdfMonth.format(bdate));
		tanggal = Integer.parseInt(sdfDay.format(bdate));		
		tahun1 = Integer.parseInt(sdfYear.format(tglLahir));
		bulan1 = Integer.parseInt(sdfMonth.format(tglLahir));
		tanggal1 = Integer.parseInt(sdfDay.format(tglLahir));
		getumur = umr.umur(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);	
		
//		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);
//		
//		if(getumur==0 && hari>15){
//			getumur=1;
//		}
		
		return getumur;
	}
	
	
	/**
	 * @param dtr
	 * @param spaj
	 * @param flagKesehatan
	 * @param excelListTtg
	 * @return
	 * @throws ParseException
	 * Untuk Method digunakan hanya untuk Produk Cerdas DMTM yg diset Hanya tertanggung Utama/Tambahan 1 saja
	 */
	private PesertaPlus_mix prosesSetDataPesertaSIO(
			Datarider dtr, List spaj, Integer flagKesehatan, Tertanggung excelListTtg, Integer peserta, Integer nourut) throws ParseException {
		ArrayList<PesertaPlus> daftarPeserta = new ArrayList<PesertaPlus>();
		String namaPeserta = spaj.get(181).toString();
		String relationPeserta = spaj.get(182).toString();
		String pekerjaanPeserta = spaj.get(183).toString();
		String jekelPeserta = spaj.get(184).toString();
		String dobTglPeserta = spaj.get(185).toString();
		String negaraPeserta = spaj.get(186).toString();
		PesertaPlus_mix peserta2 = new PesertaPlus_mix();
		
		if(!namaPeserta.equals("")){
			String[] dataNamaPeserta = namaPeserta.split(";");
			String[] dataRelation = relationPeserta.split(";");
			String[] datapekerjaanPeserta = pekerjaanPeserta.split(";");
			String[] datajekelPeserta = jekelPeserta.split(";");
			String[] datadobTglPeserta = dobTglPeserta.split(";");
			String[] datanegaraPeserta = negaraPeserta.split(";");
			
			List<String> listdataPeserta = Arrays.asList(dataNamaPeserta);
			List<String> listdataRelation = Arrays.asList(dataRelation);
			List<String> listdatapekerjaanPeserta = Arrays.asList(datapekerjaanPeserta);
			List<String> listdatajekelPeserta = Arrays.asList(datajekelPeserta);
			List<String> listdatadobTglPeserta = Arrays.asList(datadobTglPeserta);
			List<String> listdatanegaraPeserta = Arrays.asList(datanegaraPeserta);
			Integer umurTt2 =0, umurTt3 = 0, umurTt4 =0,umurTt5=0,umurTt6 =0;
			Date ttTambahan1_bod= null;
			Date ttTambahan2_bod = null;
			Date ttTambahan3_bod= null;
			Date ttTambahan4_bod= null;
			Date ttTambahan5_bod= null;
			Date bdate =dtr.getMspr_beg_date();

			if (peserta==0){
				ttTambahan2_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
				umurTt2 = gethitungUmur(bdate, ttTambahan2_bod);
				peserta2.setNama(listdataPeserta.get(peserta).toString());
				peserta2.setNo_reg("1b");
				peserta2.setFlag_jenis_peserta(1);
				peserta2.setNo_urut(nourut);
				peserta2.setLsre_id(Integer.parseInt(listdataRelation.get(peserta).toString().replace(".0", "")));
				peserta2.setUmur(umurTt2);
				peserta2.setKelamin(Integer.parseInt(listdatajekelPeserta.get(peserta).toString().replace(".0", "")));
				peserta2.setTanggal_lahir(ttTambahan2_bod);
			}else if(peserta==1){
				ttTambahan3_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
				umurTt3 = gethitungUmur(bdate, ttTambahan3_bod);
				peserta2.setNama(listdataPeserta.get(peserta).toString());
				peserta2.setNo_reg("1c");
				peserta2.setFlag_jenis_peserta(2);
				peserta2.setNo_urut(nourut);
				peserta2.setLsre_id(Integer.parseInt(listdataRelation.get(peserta).toString().replace(".0", "")));
				peserta2.setUmur(umurTt3);
				peserta2.setKelamin(Integer.parseInt(listdatajekelPeserta.get(peserta).toString().replace(".0", "")));
				peserta2.setTanggal_lahir(ttTambahan3_bod);
			}else if(peserta==2){
				ttTambahan4_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
				umurTt4 = gethitungUmur(bdate, ttTambahan4_bod);
				peserta2.setNama(listdataPeserta.get(peserta).toString());
				peserta2.setNo_reg("1d");
				peserta2.setFlag_jenis_peserta(3);
				peserta2.setNo_urut(nourut);
				peserta2.setLsre_id(Integer.parseInt(listdataRelation.get(peserta).toString().replace(".0", "")));
				peserta2.setUmur(umurTt4);
				peserta2.setKelamin(Integer.parseInt(listdatajekelPeserta.get(peserta).toString().replace(".0", "")));
				peserta2.setTanggal_lahir(ttTambahan4_bod);
			} else if(peserta==3){
				ttTambahan5_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
				umurTt5 = gethitungUmur(bdate, ttTambahan5_bod);
				peserta2.setNama(listdataPeserta.get(peserta).toString());
				peserta2.setNo_reg("1e");
				peserta2.setFlag_jenis_peserta(4);
				peserta2.setNo_urut(nourut);
				peserta2.setLsre_id(Integer.parseInt(listdataRelation.get(peserta).toString().replace(".0", "")));
				peserta2.setUmur(umurTt5);
				peserta2.setKelamin(Integer.parseInt(listdatajekelPeserta.get(peserta).toString().replace(".0", "")));
				peserta2.setTanggal_lahir(ttTambahan5_bod);
			}
		}
			
		peserta2.setPremi(dtr.getMspr_premium());
		peserta2.setNext_send(dtr.getMspr_beg_date());
		peserta2.setLsbs_id(dtr.getLsbs_id());
		peserta2.setLsdbs_number(dtr.getLsdbs_number());
		return peserta2;
	}

	public HashMap setDataQuestionareSIO(List spaj, BacDao bacDao, UwDao uwDao, Datausulan excelListDatausulan, User user) {
		
		HashMap<String, Serializable> mapDataQuestionare = new HashMap<String, Serializable>();	
		
		String jawabanNoSatu = spaj.get(145).toString().replace(".0", ""); //q1
		String jawabanNoSatuDesc = spaj.get(146).toString().replace(".0", ""); //q1 desc
		String jawabanNoDua = spaj.get(147).toString().replace(".0", ""); //q2
		String jawabanNoDuaDesc = spaj.get(148).toString().replace(".0", ""); //q2 desc
		String jawabanNoTigaa = spaj.get(149).toString().replace(".0", ""); //q3a
		String jawabanNoTigab = spaj.get(150).toString().replace(".0", ""); //q3b
		String jawabanNoTigac = spaj.get(151).toString().replace(".0", ""); //q3c
		String jawabanNoTigad = spaj.get(152).toString().replace(".0", ""); //q3d
		String jawabanNoTigae = spaj.get(153).toString().replace(".0", ""); //q3e
		String jawabanNoTigaf = spaj.get(154).toString().replace(".0", ""); //q3fa
		String jawabanNoTigafdesc = spaj.get(155).toString().replace(".0", ""); //q3fb
		String jawabanNoTigag = spaj.get(156).toString().replace(".0", ""); //q3g
		String jawabanNoTigah = spaj.get(157).toString().replace(".0", ""); //q3h
		String jawabanNoTigai = spaj.get(158).toString().replace(".0", ""); //q3i
		String jawabanNoTigaj = spaj.get(159).toString().replace(".0", ""); //q3j
		String jawabanNoTigak = spaj.get(160).toString().replace(".0", ""); //q3k
		String jawabanNoTigal = spaj.get(161).toString().replace(".0", ""); //q3l
		String jawabanNoTigam = spaj.get(162).toString().replace(".0", ""); //q3m
		String jawabanNoTigan = spaj.get(163).toString().replace(".0", ""); //q3n
		String jawabanNoTigao = spaj.get(164).toString().replace(".0", ""); //q3o
		String jawabanNoTigap = spaj.get(165).toString().replace(".0", ""); //q3p
		String jawabanNoTigadesc = spaj.get(166).toString().replace(".0", ""); //q3q
		String jawabanNoEmpat = spaj.get(167).toString().replace(".0", ""); //q4a
		String jawabanNoEmpatDesc = spaj.get(168).toString().replace(".0", ""); //q4b
		String jawabanNoLima = spaj.get(169).toString().replace(".0", ""); //q5a
		String jawabanNoLimaDesc = spaj.get(170).toString().replace(".0", ""); //q5b
		String jawabanNoEnam = spaj.get(171).toString().replace(".0", ""); //q6a
		String jawabanNoEnamDesc = spaj.get(172).toString().replace(".0", ""); //q6b
		String jawabanNoTujuh = spaj.get(173).toString().replace(".0", ""); //q7a
		String jawabanNoTujuhDesc = spaj.get(174).toString().replace(".0", ""); //q7b
		
		ArrayList data = new ArrayList();
		String gabungan1 = jawabanNoSatuDesc+";"+jawabanNoSatu;
		String gabungan2 = jawabanNoDuaDesc+";"+jawabanNoDua;				
		String gabungan3f =	jawabanNoTigafdesc+";"+jawabanNoTigaf;
		String gabungan4  =	 jawabanNoEmpatDesc+";"+jawabanNoEmpat;
		String gabungan5  =	 jawabanNoLimaDesc+";"+jawabanNoLima;
		String gabungan6  =	 jawabanNoEnamDesc+";"+jawabanNoEnam;
		String gabungan7a  = jawabanNoTujuh;
		String gabungan7b  = jawabanNoTujuhDesc;

		if(!jawabanNoSatu.equals("")){

			String[] datajawabanSatu = gabungan1.split(";");
			String[] datajawabandua = gabungan2.split(";");
			String[] datajawabanTigaa = jawabanNoTigaa.split(";");
			String[] datajawabanTigab = jawabanNoTigab.split(";");
			String[] datajawabanTigac = jawabanNoTigac.split(";");
			String[] datajawabanTigad = jawabanNoTigad.split(";");
			String[] datajawabanTigae = jawabanNoTigae.split(";");
			String[] datajawabanTigaf = gabungan3f.split(";");
			String[] datajawabanTigag = jawabanNoTigag.split(";");
			String[] datajawabanTigah = jawabanNoTigah.split(";");
			String[] datajawabanTigai = jawabanNoTigai.split(";");
			String[] datajawabanTigaj = jawabanNoTigaj.split(";");
			String[] datajawabanTigak = jawabanNoTigak.split(";");
			String[] datajawabanTigal = jawabanNoTigal.split(";");
			String[] datajawabanTigam = jawabanNoTigam.split(";");
			String[] datajawabanTigan = jawabanNoTigan.split(";");
			String[] datajawabanTigao = jawabanNoTigao.split(";");
			String[] datajawabanTigap = jawabanNoTigap.split(";");
			String[] datajawabanEmpat = gabungan4.split(";");
			String[] datajawabanLima = gabungan5.split(";");
			String[] datajawabanEnam = gabungan6.split(";");
			String[] datajawabanTujuha = gabungan7a.split(";");
			String[] datajawabanTujuhb = gabungan7b.split(";");
			String[] datajawabanTigaDesc = jawabanNoTigadesc.split(";");
			List<String> listdatajawaban = Arrays.asList(datajawabanSatu);		
			ArrayList data_LQLSIO=new ArrayList();
			
			Integer lsbs = excelListDatausulan.getLsbs_id();
			Integer lsdbs = excelListDatausulan.getLsdbs_number();
			
			if(lsbs==221 || lsbs==225 || (lsbs==183 && ((lsdbs>=46 && lsdbs <=60) || (lsdbs>=106 && lsdbs <=120))) || (lsbs==189 && (lsdbs>=33 && lsdbs <=62)) || (lsbs==195 && (lsdbs>=61 && lsdbs <=72)) || (lsbs==204 && (lsdbs>=37 && lsdbs <=48))){
				data_LQLSIO =Common.serializableList(uwDao.selectDataLQLTambahan(13,excelListDatausulan.getMste_beg_date(),null,null,excelListDatausulan.getDaftapeserta().size()));
			}else{
				data_LQLSIO =Common.serializableList(uwDao.selectDataLQL(12,excelListDatausulan.getMste_beg_date(),null,null));
			}
			
			String [][] list_data_jawaban ={datajawabanSatu,datajawabandua,datajawabanTigaa,datajawabanTigab,datajawabanTigac,datajawabanTigad,datajawabanTigae,datajawabanTigaf,
											datajawabanTigag,datajawabanTigah,datajawabanTigai,datajawabanTigaj,datajawabanTigak,datajawabanTigal,datajawabanTigam,datajawabanTigan,
											datajawabanTigao,datajawabanTigap,datajawabanTigaDesc,datajawabanEmpat,datajawabanLima,datajawabanEnam,datajawabanTujuha,datajawabanTujuhb};
			
			  Integer group =0;
			  Integer tipe =0;
			  Integer id=0;
			  Integer x=0,z=0,ass=0;
				for(int i=0;i<data_LQLSIO.size();i++){
					
					MstQuestionAnswer queAnswer=new MstQuestionAnswer();
					Map pertanyaan= (Map)data_LQLSIO.get(i);
					BigDecimal question_id =(BigDecimal) pertanyaan.get("QUESTION_ID");
					BigDecimal option_type =(BigDecimal) pertanyaan.get("OPTION_TYPE");
					BigDecimal option_group=(BigDecimal) pertanyaan.get("OPTION_GROUP");
					BigDecimal option_order=(BigDecimal) pertanyaan.get("OPTION_ORDER");
					BigDecimal question_type_id=(BigDecimal) pertanyaan.get("QUESTION_TYPE_ID");
					Date valid_date = (Date)pertanyaan.get("QUESTION_VALID_DATE");
					String option2 = (String)pertanyaan.get("OPTION2");
					String answer ="0";
					if(question_id.intValue()!=id){
						z=0;
						ass=0;
						group=1;
					}
					
					if(question_id.intValue()==81){
						x=0;
					}else if(question_id.intValue()==82){
						x=1;
					}else if(question_id.intValue()==84){
						x=2;
						//group =1;
					}else if(question_id.intValue()==85){
						x=3;
						//group =1;
					}else if(question_id.intValue()==86){
						x=4;
						//group =1;
					}else if(question_id.intValue()==87){
						x=5;
						//group =1;
					}else if(question_id.intValue()==88){
						x=6;
						//group =1;
					}else if(question_id.intValue()==89){
						x=7;						
					}else if(question_id.intValue()==90){
						x=8;
						//group =1;					
					}else if(question_id.intValue()==91){
						x=9;
						//group =1;					
					}else if(question_id.intValue()==92){
						x=10;
						//group =1;					
					}else if(question_id.intValue()==93){
						x=11;
						//group =1;					
					}else if(question_id.intValue()==94){
						x=12;
						//group =1;					
					}else if(question_id.intValue()==95){
						x=13;
						//group =1;					
					}else if(question_id.intValue()==96){
						x=14;
						//group =1;					
					}else if(question_id.intValue()==97){
						x=15;
//						group =1;					
					}else if(question_id.intValue()==98){
						x=16;
//						group =1;					
					}else if(question_id.intValue()==99){
						x=17;
//						group =1;					
					}else if(question_id.intValue()==100){
						x=18;
//						group =1;					
					}else if(question_id.intValue()==101){
						x=19;									
					}else if(question_id.intValue()==102){
						x=20;									
					}else if(question_id.intValue()==103){
						x=21;									
					}
					
					if(question_id.intValue()!=100 && question_id.intValue()!=104 ){
						if(option_type.intValue() ==0){
							answer = list_data_jawaban[x][z];
							z=z+1;
						}else{
							if(option_type.intValue()!=0){
								if(group==option_group.intValue()){
									z=z+0;
									answer = list_data_jawaban[x][z];
									if(option_order.intValue()==1){
										if(answer.equals("1")){
											answer="1";
										}else{
											answer="0";
										}
									}else{
										if(answer.equals("1")){
											answer="0";
										}else{
											answer="1";
										}
									}
								}else{
									z=z+1;
									
									try{
									answer = list_data_jawaban[x][z];
									}catch (Exception e){
										logger.info(x+" - "+z);
									}
									if(option_order.intValue()==1){
										if(answer.equals("1")){
											answer="1";
										}else{
											answer="0";
										}
									}else{
										if(answer.equals("1")){
											answer="0";
										}else{
											answer="1";
										}
									}
								}
							}
						}
						if(option_group.intValue()==0){
							group = 1;
						}else{
							group = option_group.intValue();
						}
						tipe = option_order.intValue();
						id = question_id.intValue();
					
					}else{
						if(question_id.intValue()==100){
							answer=list_data_jawaban[x][0];
						}else if(question_id.intValue()==104){
							
							if(option_order.intValue()==1){
								x=22;
							}else{
								x=23;
							}
							
							if(group==option_group.intValue()){
								z=z+0;
								answer=list_data_jawaban[x][z];
							}else{
								z=z+1;
								answer=list_data_jawaban[x][z];
							}
							
							if(option_group.intValue()==0){
								group = 1;
							}else{
								group = option_group.intValue();
							}
							tipe = option_order.intValue();
							id = question_id.intValue();
						}
						
					}
					queAnswer.setAnswer(answer);
					queAnswer.setQuestion_valid_date(valid_date);
					queAnswer.setLus_id(Integer.parseInt(user.getLus_id()));
					queAnswer.setOption_group(option_group.intValue());
					queAnswer.setOption_type(option_type.intValue());
					queAnswer.setQuestion_id(question_id.intValue());
					queAnswer.setOption_order(option_order.intValue());
					queAnswer.setQuestion_type_id(question_type_id.intValue());
					//logger.info(queAnswer.getQuestion_type_id());
					data.add(queAnswer);				
				}		
			
		}
		mapDataQuestionare.put("DATA",data);
		return mapDataQuestionare;
	}

	public HashMap prosesSetDataReffBii(List spaj, BacDao bacDao, User user,UwDao uwDao, Agen excelListagen ) {
		 HashMap<String, Serializable> mapReffBii = new HashMap<String, Serializable>();
		 ReffBii reffbii=new ReffBii();
		 String kode_reff = spaj.get(194).toString().replace(".0", "");
		 Integer jnbankdetbisnis = bacDao.selectJnBankDetBisnis(Integer.parseInt(spaj.get(132).toString().substring(0, 3)), Integer.parseInt(spaj.get(132).toString().substring(4)));
		 ArrayList<Map> reffBank = Common.serializableList(bacDao.selectReffSinarmas(kode_reff,0));
		 BigDecimal lrbidReff;BigDecimal jenis; String lcb_no;
		 String err="";
		 
		 //jika lebih dari 1 list 
		 if(reffBank.size()>1 && jnbankdetbisnis==2){
			 reffBank = Common.serializableList(bacDao.selectReffSinarmas(kode_reff,2));
		 }
	     try{
	    	 if(!reffBank.isEmpty()){
	    		 Map mapReff= (Map)reffBank.get(0);					 
				 lrbidReff = (BigDecimal)mapReff.get("LRB_ID");
				 jenis = (BigDecimal)mapReff.get("JENIS");
				 lcb_no = (String)mapReff.get("LCB_NO");
				 if(lrbidReff!=null){
					 if(jenis.intValue() == 2){//bank sinarmas
						 reffbii.setLevel_id("4");
						 reffbii.setLrb_id("149236");
						 reffbii.setReff_id(lrbidReff.toString());
						 reffbii.setLcb_no("S415");
						 reffbii.setLcb_penutup("S415");
						 reffbii.setLcb_penutup2("S415");
						 reffbii.setLcb_no2(lcb_no);
						 
					 }else if(jenis.intValue() == 19 || jenis.intValue() == 50 || jenis.intValue() == 43 || jenis.intValue() == 51 || jenis.intValue() == 61 || jenis.intValue() == 62){//others reff dmtm dari pihak luar(data nasabah berasal dari pihak luar) / Reff Bank Bukopin
						 reffbii.setLevel_id("4");
						 reffbii.setLrb_id(lrbidReff.toString());
						 reffbii.setReff_id(lrbidReff.toString());
						 reffbii.setLcb_no(lcb_no);
						 reffbii.setLcb_penutup(lcb_no);
						 reffbii.setLcb_penutup2(lcb_no);
						 reffbii.setLcb_no2(lcb_no);
					 }
				 }
	    	 }else{
	    		 if(( excelListagen.getLca_id().equals("40") && excelListagen.getLwk_id().equals("01") && ("01,02,03,04,07,11,12".indexOf(excelListagen.getLsrg_id())>-1) )){
	    			 err=err+"Data Referral tidak terdaftar atau non aktif. Harap cek kembali";
	    		 }	    		 
	    	 }
			 mapReffBii.put("REFF", reffbii);
	     }catch (Exception e) {
	    	 err=err+"Ada Kesalahan Saat Set Data Refferal";
	     }
	     mapReffBii.put("ERR", err);
	     return mapReffBii;
	}
	
	/**
	 * @param dtr
	 * @param spaj
	 * @param flagKesehatan
	 * @param excelListTtg
	 * @return
	 * @throws ParseException
	 * Untuk Method digunakan Untuk Mencari Usia Tertanggung Tambahan
	 */
	private Integer prosesSetDataPesertaUsiaSIO(Datarider dtr, List spaj, Integer flagKesehatan, Tertanggung excelListTtg, Integer peserta) throws ParseException {
		ArrayList<PesertaPlus> daftarPeserta = new ArrayList<PesertaPlus>();
		String namaPeserta = spaj.get(181).toString();
		String relationPeserta = spaj.get(182).toString();
		String pekerjaanPeserta = spaj.get(183).toString();
		String jekelPeserta = spaj.get(184).toString();
		String dobTglPeserta = spaj.get(185).toString();
		String negaraPeserta = spaj.get(186).toString();
		PesertaPlus_mix peserta2 = new PesertaPlus_mix();
		Integer umurTt =0;
		if(!namaPeserta.equals("")){
			String[] dataNamaPeserta = namaPeserta.split(";");
			String[] dataRelation = relationPeserta.split(";");
			String[] datapekerjaanPeserta = pekerjaanPeserta.split(";");
			String[] datajekelPeserta = jekelPeserta.split(";");
			String[] datadobTglPeserta = dobTglPeserta.split(";");
			String[] datanegaraPeserta = negaraPeserta.split(";");
			
			List<String> listdataPeserta = Arrays.asList(dataNamaPeserta);
			List<String> listdataRelation = Arrays.asList(dataRelation);
			List<String> listdatapekerjaanPeserta = Arrays.asList(datapekerjaanPeserta);
			List<String> listdatajekelPeserta = Arrays.asList(datajekelPeserta);
			List<String> listdatadobTglPeserta = Arrays.asList(datadobTglPeserta);
			List<String> listdatanegaraPeserta = Arrays.asList(datanegaraPeserta);
			Date ttTambahan1_bod= null;
			Date ttTambahan2_bod = null;
			Date ttTambahan3_bod= null;
			Date ttTambahan4_bod= null;
			Date ttTambahan5_bod= null;
			Date bdate =dtr.getMspr_beg_date();
			int relasi1 = 0;
			int relasi2 = 0;
			int relasi3 = 0;
			int relasi4 = 0;
			int relasi5 = 0;

			if (peserta==0){
				ttTambahan2_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
//				umurTt = gethitungUmur(bdate, ttTambahan2_bod);
				relasi2 = Integer.parseInt(listdataRelation.get(peserta));
				umurTt = gethitungUmurRelasi(bdate, ttTambahan2_bod ,relasi2 );			
				

			}else if(peserta==1){
				ttTambahan3_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
//				umurTt = gethitungUmur(bdate, ttTambahan3_bod);
				relasi3 = Integer.parseInt(listdataRelation.get(peserta));
				umurTt = gethitungUmurRelasi(bdate, ttTambahan3_bod ,relasi3 );	
		
			}else if(peserta==2){
				ttTambahan4_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
//				umurTt = gethitungUmur(bdate, ttTambahan4_bod);
				relasi4 = Integer.parseInt(listdataRelation.get(peserta));
				umurTt = gethitungUmurRelasi(bdate, ttTambahan4_bod ,relasi4 );	
		
			} else if(peserta==3){
				ttTambahan5_bod = df.parse(listdatadobTglPeserta.get(peserta).toString());
//				umurTt = gethitungUmur(bdate, ttTambahan5_bod);
				relasi5 = Integer.parseInt(listdataRelation.get(peserta));
				umurTt = gethitungUmurRelasi(bdate, ttTambahan5_bod ,relasi5 );	
			}
		}
		return umurTt;
	}
	
	private Integer gethitungUmurRelasi(Date bdate, Date tglLahir, int relasi) {
		f_hit_umur umr = new f_hit_umur();
		Integer getumur = 0;
		tahun = Integer.parseInt(sdfYear.format(bdate));
		bulan = Integer.parseInt(sdfMonth.format(bdate));
		tanggal = Integer.parseInt(sdfDay.format(bdate));		
		tahun1 = Integer.parseInt(sdfYear.format(tglLahir));
		bulan1 = Integer.parseInt(sdfMonth.format(tglLahir));
		tanggal1 = Integer.parseInt(sdfDay.format(tglLahir));
		getumur = umr.umur(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);	
		
		int hari=umr.hari(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);
		
		if(getumur==0 && hari>15 && (relasi == 4 || relasi == 8 || relasi == 21 || relasi == 22)){
			getumur=1;
		}
		
		return getumur;
	}
	
	//helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9, untuk UP > 200jt SIO ada tambah questionare
	//helpdesk [150296] DMTM BSIM 163 21-25 tambah simple questionare SIO+
	public void setDataSimpleQuestionareSIO(List dataRow, Datausulan excelListDatausulan, com.ekalife.elions.model.MedQuest_tambah question) {
		int lsbsId = excelListDatausulan.getLsbs_id(), lsdbsNumber = excelListDatausulan.getLsdbs_number();
		
		if((lsbsId == 163 && (lsdbsNumber >= 21 && lsdbsNumber <= 25)) ||
		   (lsbsId == 173 && (lsdbsNumber >= 7 && lsdbsNumber <= 9))){ 
			question.setMsadm_berat(Integer.parseInt(dataRow.get(203).toString().replace(".0", ""))); //sq1a berat badan
			question.setMsadm_tinggi(Integer.parseInt(dataRow.get(204).toString().replace(".0", ""))); //sq1b tinggi badan
			question.setMsadm_penyakit(Integer.parseInt((dataRow.get(205) == null ? "0" : dataRow.get(205).toString().trim()))); //sq2 sedang menderita/diberitahu sakit
			if(question.getMsadm_penyakit() == 1)
				question.setMsadm_penyakit_desc(dataRow.get(206).toString().trim()); //sq2desc deskripsi sedang menderita/diberitahu sakit
			else
				question.setMsadm_penyakit_desc("");
			question.setMsadm_medis(Integer.parseInt((dataRow.get(207) == null ? "0" : dataRow.get(207).toString().trim()))); //sq3 rawat,usg,dll
			if(question.getMsadm_medis() == 1)
				question.setMsadm_medis_desc(dataRow.get(208).toString().trim()); //sq3desc deskripsi rawat,usg,dll
			else
				question.setMsadm_medis_desc("");
			question.setMsadm_pregnant(Integer.parseInt((dataRow.get(209) == null ? "0" : dataRow.get(209).toString().trim()))); //sq4 apakah hamil
			if(question.getMsadm_pregnant() == 1)
				question.setMsadm_pregnant_desc(dataRow.get(210).toString().replace(".0", "")); //sq4desc berapa bulan hamil
			else
				question.setMsadm_pregnant_desc("");

			question.setMste_insured_no(1);
			question.setMsadm_sehat(1);
			question.setMsadm_berat_berubah(0);
		}
	}
}
