package com.ekalife.elions.web.bac;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.TtgHcpvalidator;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;
/**
 * @author HEMILDA
 * Controller titipan premi
 */

public class TtgHcpMallController extends ParentFormController {

	protected void validatePage(Object cmd, Errors err, int page) {
		TtgHcpvalidator validator = (TtgHcpvalidator) this.getValidator();
		Cmdeditbac halaman= (Cmdeditbac)cmd;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "showSPAJ", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		String lsbs_id = uwManager.selectLsbsId(spaj);
		Integer lsdbs_number = Integer.parseInt(uwManager.selectLsdbsNumber(spaj));
		String sts = ServletRequestUtils.getStringParameter(request,"sts","");
		Map map = new HashMap();
		map.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
		if(lsbs_id.equals("183") || lsbs_id.equals("189") || lsbs_id.equals("193") ){
			if(lsbs_id.equals("193")){
				map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_IL.xml","BISNIS_ID",request));
			}else{
				map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT.xml","BISNIS_ID",request));
			}
				
		}else if(products.unitLink(lsbs_id)|| products.multiInvest(lsbs_id)){
			Integer countrider = uwManager.selectCountEkaSehatAndHCP(spaj);
			if(countrider>1){
				Date LimaBelasJuniDuaRibuSepuluh = defaultDateFormat.parse("15/6/2010");
				if(LimaBelasJuniDuaRibuSepuluh.before(uwManager.selectBegDateProductInsured(spaj))){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP_EKASEHAT.xml","BISNIS_ID",request));
				}else{
					List lsbs_id_rider = uwManager.selectLsbsIdRiderHCPOrEkaSehat(spaj);
					if(lsbs_id_rider.equals("819")){
						map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP.xml","BISNIS_ID",request));
					}else{
						map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_PROV.xml","BISNIS_ID",request));
					}
				}
			}else if(countrider==1){
				List lsbs_id_rider = uwManager.selectLsbsIdRiderHCPOrEkaSehat(spaj);
				if(lsbs_id_rider.equals("819")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP.xml","BISNIS_ID",request));
				}else if(lsbs_id_rider.equals("820") || lsbs_id_rider.equals("825")){
					if(lsbs_id_rider.equals("825")){
						map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_IL.xml","BISNIS_ID",request));
					}else{
						map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT.xml","BISNIS_ID",request));
					}
					
				}else if(lsbs_id_rider.equals("823")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_PROV.xml","BISNIS_ID",request));
				}
			}
		}else if(lsbs_id.equals("178")){
			map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_SMART_MEDICARE.xml","BISNIS_ID",request));
		}else if(products.powerSave(lsbs_id)){
			Integer countrider = uwManager.selectCountEkaSehatAndHCP(spaj);
			if(countrider>1){
				map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP_EKASEHAT.xml","BISNIS_ID",request));
			}else if(countrider==1){
				List lsbs_id_rider = uwManager.selectLsbsIdRiderHCPOrEkaSehat(spaj);
				if(lsbs_id_rider.equals("819")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP.xml","BISNIS_ID",request));
				}else if(lsbs_id_rider.equals("820")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT.xml","BISNIS_ID",request));
				}else if(lsbs_id_rider.equals("823")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_PROV.xml","BISNIS_ID",request));
				}else if(lsbs_id_rider.equals("825")){
					map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_IL.xml","BISNIS_ID",request));
				}
			}
		}else if(!products.unitLink(lsbs_id)){
			List lsbs_id_rider = uwManager.selectLsbsIdRiderHCPOrEkaSehat(spaj);
			if(lsbs_id_rider.equals("823")){
				map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_PROV.xml","BISNIS_ID",request));
			}else if(lsbs_id_rider.equals("825")){
				map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_EKA_SEHAT_IL.xml","BISNIS_ID",request));
			}else if(lsbs_id_rider.equals("826")){
				
			}
		}
		else
			map.put("select_rider",DroplistManager.getInstance().get("PLANRIDER_HCP.xml","BISNIS_ID",request));
		
		map.put("select_pengecualian",uwManager.selectLstPengecualian());
		map.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request)); 
		map.put("sts", sts);
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String lde_id = currentUser.getLde_id();
				Cmdeditbac detiledit = new Cmdeditbac();
				String spaj = request.getParameter("showSPAJ");
				if(spaj!=null)
				{
					/**
					 * @author HEMILDA
					 * edit
					 */
					detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
					detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
					detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
					detiledit.getDatausulan().setLde_id(lde_id);
					detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
					
					InvestasiUtama investasi  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
					if (investasi == null)
					{
						InvestasiUtama inv = new InvestasiUtama();
						inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
					}else{
						detiledit.setInvestasiutama(investasi);
						detiledit.getInvestasiutama().setDaftarinvestasi(this.elionsManager.selectdetilinvestasi(spaj));
						detiledit.getInvestasiutama().setDaftarbiaya(this.elionsManager.selectdetilinvbiaya(spaj));
						DetilTopUp detiltopup = (DetilTopUp)this.elionsManager.select_detil_topup(spaj);
						if (detiltopup == null)
						{
							detiltopup = new DetilTopUp();
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}else{
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}
					}
						
					detiledit.setRekening_client(this.elionsManager.select_rek_client(spaj));
					detiledit.setAccount_recur(this.elionsManager.select_account_recur(spaj));
					
					Powersave data_pwr = (Powersave)this.elionsManager.select_powersaver(spaj);
					if (data_pwr==null)
					{
						detiledit.setPowersave(new Powersave());
					}else{
						detiledit.setPowersave(data_pwr);
					}

					detiledit.setAgen(this.elionsManager.select_detilagen(spaj));

					String kode_agen = detiledit.getAgen().getMsag_id();
					String nama_agent="";
					if (kode_agen.equalsIgnoreCase("000000"))
					{
						nama_agent = (String)this.elionsManager.select_agent_temp(spaj);
					}
					detiledit.getAgen().setMcl_first(nama_agent);
					detiledit.setEmployee(this.elionsManager.select_detilemployee(spaj));
					
					detiledit.setHistory(new History());
					detiledit.getDatausulan().setDaftahcp(this.elionsManager.select_hcp(spaj));
					List<Hcp> tampung = detiledit.getDatausulan().getDaftahcp();
					if(tampung.get(0).getLsbs_id()==183 || tampung.get(0).getLsbs_id()==189 || tampung.get(0).getLsbs_id()==193){
						String plan_rider = tampung.get(0).getPlan_rider();
						String plan_2 = plan_rider.substring(3, plan_rider.length());
						String ekasehat = "820";
						if(tampung.get(0).getLsbs_id()==193){
							ekasehat = "825";
						}
						if(tampung.get(0).getLsdbs_number()<16){
							tampung.get(0).setPlan_rider(ekasehat+plan_2);
							tampung.get(0).setPremi(tampung.get(0).getMspr_premium());
						}else{
							tampung.get(0).setPlan_rider(ekasehat+"~X"+(tampung.get(0).getLsdbs_number().intValue()+75));
							tampung.get(0).setPremi(tampung.get(0).getMspr_premium());
						}
					}else if(tampung.get(0).getLsbs_id()==178){
						String plan_rider = tampung.get(0).getPlan_rider();
						String plan_2 = plan_rider.substring(3, plan_rider.length());
						tampung.get(0).setPlan_rider("821"+plan_2);
						tampung.get(0).setPremi(tampung.get(0).getMspr_premium());
					}
						
					
				}else{
					/**
					 * @author HEMILDA
					 * input
					 */
					detiledit.setPemegang(new Pemegang());
					detiledit.setAddressbilling(new AddressBilling());
					detiledit.setDatausulan(new Datausulan());
					detiledit.setTertanggung(new Tertanggung());
					InvestasiUtama inv = new InvestasiUtama();
					inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
					inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
					detiledit.setInvestasiutama(inv);
					inv.setDaftartopup(new DetilTopUp());
					detiledit.setRekening_client(new Rekening_client());
					detiledit.setAccount_recur(new Account_recur());
					detiledit.setPowersave(new Powersave());
					detiledit.setAgen(new Agen());
					detiledit.setHistory(new History());
					detiledit.setEmployee(new Employee());
					
				}
			return detiledit;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		f_validasi data = new f_validasi();
		
		Cmdeditbac a = (Cmdeditbac) cmd;
		Integer jumlah_peserta = ServletRequestUtils.getIntParameter(request, "jml_peserta");
		if (jumlah_peserta == null)
		{
			jumlah_peserta = new Integer(1);
			a.getDatausulan().setJml_peserta(jumlah_peserta);
		}
		List b= new ArrayList();
		List c = new ArrayList();
		List d = new ArrayList();
		Integer no_urut;                       
		Integer lsre_id;                       
		String nama;    
		String tgllhr;
		String blnhr;
		String thnhr;
		Integer kelamin;                       
		Integer umur;                          
		Double premi;                          
		String reg_spaj;
		String tanggal_lahir;

		String kode_rider;
		Integer kd_sm;
		String number_rider;
		Integer nm_sm = new Integer(0);
		String unit ;
		Integer unit_1 = new Integer(0);
		Integer unt = new Integer(0);
		String klas;
		Integer klas_1 = new Integer(0);
		Integer kls = new Integer(0);
		String tsi;
		Double tsi_1 = new Double(0);
		Double ts = new Double(0);
		Integer in = new Integer(0);
		Integer insperiod_1 = new Integer(0);
		String ins;
		String beg_date;
		Date beg_date_1 = null;
		Date begin_date = null;
		String end_date;
		Date end_date_1 = null;
		Date ending_date = null;		
		String end_pay;
		Date end_pay_1 = null;
		Date ending_pay = null;		
		String rate;
		Double rate_1 = new Double(0);
		Double rt = new Double(0);
		String insured;
		Integer insured_1 = new Integer(0);
		Integer isr = new Integer(0);
		String bisnis;
		Integer lspc_no;
		Double total_premi = 0.0;
		Double premi_berkala = 0.0;
		Double premi_tunggal = 0.0;
		Double total_biaya = 0.0;
		Integer lsbs_id;
		Integer[] ljbid;
		Integer[] ljbidCompare;
		ljbid = new Integer[jumlah_peserta.intValue()];
		if(products.unitLink(a.getDatausulan().getLsbs_id().toString())){
			ljbidCompare = new Integer[a.getInvestasiutama().getDaftarbiaya().size()];
		}
		int letak=0;
		if (jumlah_peserta > 0)
		{
			//lsbs_id = request.getParameter("ttg.plan_rider"+k);
			//Hcp data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(819), new Integer(1),new Integer(20));
			Hcp data_1 = null;
			
			if(a.getInvestasiutama().getDaftartopup().getPremi_berkala()==null){
				premi_berkala = 0.0;
			}else premi_berkala = a.getInvestasiutama().getDaftartopup().getPremi_berkala();
			
			if(a.getInvestasiutama().getDaftartopup().getPremi_tunggal()==null){
				premi_tunggal = 0.0;
			}else premi_tunggal = a.getInvestasiutama().getDaftartopup().getPremi_tunggal();
			
			if(products.unitLink(a.getDatausulan().getLsbs_id().toString())){
				total_premi = a.getInvestasiutama().getJml_premium() + premi_tunggal + premi_berkala;
			}
			for (int k = 1 ;k <= jumlah_peserta; k++)
			{
				if(request.getParameter("ttg.lspc_no1"+k) == null) lspc_no = 0;
				else lspc_no = request.getParameter("ttg.lspc_no1"+k)==""?0:Integer.parseInt(request.getParameter("ttg.lspc_no1"+k));
				
				//rider
				bisnis = request.getParameter("ttg.plan_rider1"+k);
				
				if (bisnis==null)
				{
					bisnis="0~X0";
				}
				if (bisnis.equalsIgnoreCase(""))
				{
					bisnis="0~X0";
				}
				letak=0;
				letak=bisnis.indexOf("X");
				kode_rider = bisnis.substring(0,letak-1);
				kd_sm = new Integer(Integer.parseInt(kode_rider));
				number_rider = bisnis.substring(letak+1,bisnis.length());
				nm_sm = new Integer(Integer.parseInt(number_rider));
				if(products.unitLink(a.getDatausulan().getLsbs_id().toString())){
					ljbid[k-1]=uwManager.selectLjbIdFromBiayaUlink(a.getPemegang().getReg_spaj(), kode_rider, number_rider);
				}
				if(a.getDatausulan().getLsbs_id().intValue()==183 || a.getDatausulan().getLsbs_id().intValue()==189 || a.getDatausulan().getLsbs_id().intValue()==193 || a.getDatausulan().getLsbs_id().intValue()==820 || a.getDatausulan().getLsbs_id().intValue()==823 || a.getDatausulan().getLsbs_id().intValue()==825){
					data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), a.getDatausulan().getLsbs_id().intValue(), new Integer(1),new Integer(30));
				}else if(a.getDatausulan().getLsbs_id().intValue()==178 || a.getDatausulan().getLsbs_id().intValue()==821){
					data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), a.getDatausulan().getLsbs_id().intValue(), new Integer(1),new Integer(16));
				}
				else if(products.unitLink(a.getDatausulan().getLsbs_id().toString())){
					if(kode_rider.equals("820")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(820), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("819")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(819), new Integer(141),new Integer(160));
					}else if(kode_rider.equals("823")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(823), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("825")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(825), new Integer(1),new Integer(15));
					}
					
				}else if(products.powerSave(a.getDatausulan().getLsbs_id().toString())){
					if(kode_rider.equals("820")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(820), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("819")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(819), new Integer(141),new Integer(160));
					}else if(kode_rider.equals("823")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(823), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("825")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(825), new Integer(1),new Integer(15));
					}
				}else if(products.multiInvest(a.getDatausulan().getLsbs_id().toString())){
					if(kode_rider.equals("820")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(820), new Integer(91),new Integer(105));
					}else if(kode_rider.equals("819")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(819), new Integer(381),new Integer(390));
					}else if(kode_rider.equals("823")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(823), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("825")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(825), new Integer(1),new Integer(15));
					}
				}else if(!products.unitLink(a.getDatausulan().getLsbs_id().toString()) ){
					if(kode_rider.equals("823")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(823), new Integer(1),new Integer(15));
					}else if(kode_rider.equals("819")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(819), new Integer(381),new Integer(390));
					}else if(kode_rider.equals("825")){
						data_1 = (Hcp) this.elionsManager.select_hcp_perkode(a.getPemegang().getReg_spaj(), new Integer(825), new Integer(1),new Integer(15));
					}
				}
					
				//peserta
				no_urut = ServletRequestUtils.getIntParameter(request, "ttg.no_urut"+k);
				lsre_id = ServletRequestUtils.getIntParameter(request, "ttg.lsre_id"+k);
				nama = request.getParameter("ttg.nama"+k);
				if (nama==null)
				{
					nama="";
				}
				tgllhr = request.getParameter("tgllhr"+k);
				if (tgllhr == null)
				{
					tgllhr ="";
				}
				blnhr = request.getParameter("blnhr"+k);
				if (blnhr == null)
				{
					blnhr = "";
				}
				thnhr = request.getParameter("thnhr"+k);
				if (thnhr == null)
				{
					thnhr = "";
				}
				
				tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
				if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0))
				{
					tanggal_lahir=null;
				}else{
					boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
					boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
					boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
					if ((cekk1==false) ||(cekk2==false) || (cekk3==false)  )
					{
						tanggal_lahir=null;
					}
				}
					
				Date tanggallahir = null;
				if (tanggal_lahir != null)
				{
					tanggallahir = defaultDateFormat.parse(tanggal_lahir);
				}
				
				if(tanggallahir==null){
					errors.reject("","Mohon mengisi kolom tanggal lahir yang kosong");
				}
				
				kelamin = ServletRequestUtils.getIntParameter(request, "ttg.kelamin"+k);
				if (kelamin == null)
				{
					kelamin= new Integer(1);
				}
				umur = ServletRequestUtils.getIntParameter(request, "ttg.umur"+k); 
				if (umur==null)
				{
					umur = new Integer(0);
				}
				premi = ServletRequestUtils.getDoubleParameter(request,"ttg.premi"+k);
				if (premi == null)
				{
					premi = new Double(0);
				}

				if ( k == 1)
				{	
					kd_sm = a.getDatausulan().getLsbs_id();
					nm_sm = a.getDatausulan().getLsdbs_number();
					//samain dengan rider utama (HCP family basic)
					unit_1 = data_1.getMspr_unit();
					if (unit_1 == null)
					{
						unit_1 = new Integer(0);
					}
					klas_1 = data_1.getMspr_class();
					if (klas_1 == null)
					{
						klas_1 = new Integer(0);
					}
					tsi_1 = data_1.getMspr_tsi();
					if (tsi_1 == null)
					{
						tsi_1 = new Double(0);
					}
					insperiod_1 = data_1.getMspr_ins_period();
					if (insperiod_1 == null)
					{
						insperiod_1 = new Integer(0);
					}
					beg_date_1 = data_1.getMspr_beg_date();
					
					end_date_1 = data_1.getMspr_end_date();
					end_pay_1 = data_1.getMspr_end_pay();
					rate_1 =data_1.getMspr_rate();
					if (rate_1  == null)
					{
						rate_1 = new Double(0);
					}
					insured_1 = data_1.getMspr_tt();
					if (insured_1 == null)
					{
						insured_1 = new Integer(0);
					}
				}
				
				unt = data_1.getMspr_unit();
				if (unt == null)
				{
					unt = unit_1;
				}
				
				kls = data_1.getMspr_class();
				if (kls == null)
				{
					kls = klas_1;
				}
				
				ts = data_1.getMspr_tsi();
				if (ts == null)
				{
					ts = tsi_1;
				}
				in  = data_1.getMspr_ins_period();
				if (in  == null)
				{
					in  = insperiod_1;
				}
				
				begin_date = data_1.getMspr_beg_date();
				if (begin_date == null)
				{
					begin_date=beg_date_1;
				}
			
				ending_date = data_1.getMspr_end_date();
				if (ending_date == null)
				{
					ending_date = end_date_1;
				}
				
				ending_pay = data_1.getMspr_end_pay();
				if (ending_pay == null)
				{
					ending_pay = end_pay_1;
				}
				
				rt  =data_1.getMspr_rate();
				if (rt   == null)
				{
					rt = rate_1;
				}
				
				isr= data_1.getMspr_tt();
				if (isr == null)
				{
					isr = insured_1;
				}
				
				if(k>1){
					f_hit_umur umr= new f_hit_umur();
					Integer tahun1,tahun2, bulan1, bulan2, tanggal1, tanggal2;
					tanggal1= a.getDatausulan().getMste_beg_date().getDate();
					bulan1 = (a.getDatausulan().getMste_beg_date().getMonth())+1;
					tahun1 = (a.getDatausulan().getMste_beg_date().getYear())+1900;
					tanggal2=tanggallahir.getDate();
					bulan2=(tanggallahir.getMonth())+1;
					tahun2=(tanggallahir.getYear())+1900;
					umur = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
				}
	
				reg_spaj = a.getPemegang().getReg_spaj();

				Hcp sm = new Hcp();
				sm.setKelamin(kelamin);
				sm.setLsre_id(lsre_id);
				sm.setNama(nama);
				sm.setNo_urut(no_urut);
				sm.setPremi(premi);
				sm.setReg_spaj(reg_spaj);
				sm.setTgl_lahir(tanggallahir);
				sm.setUmur(umur);
				sm.setLsbs_id(kd_sm);
				sm.setLsdbs_number(nm_sm);
				sm.setPlan_rider(bisnis);
				sm.setMspr_unit(unt);
				sm.setMspr_class(kls);
				sm.setMspr_ins_period(in);
				sm.setMspr_tt(isr);
				sm.setMspr_rate(rt);
				sm.setMspr_beg_date(begin_date);
				sm.setMspr_end_date(ending_date);
				sm.setMspr_end_pay(ending_pay);
				sm.setMspr_tsi(ts);
				sm.setPlan_rider(bisnis);
				sm.setLspc_no(lspc_no);
				b.add(sm);
				
				Datarider dr = new Datarider();
				dr.setLsbs_id(kd_sm);
				dr.setLsdbs_number(nm_sm);
				dr.setPlan_rider(bisnis);
				dr.setMspr_unit(unt);
				dr.setMspr_class(kls);
				dr.setMspr_ins_period(in);
				dr.setMspr_tt(isr);
				dr.setMspr_rate(rt);
				dr.setMspr_beg_date(begin_date);
				dr.setMspr_end_date(ending_date);
				dr.setMspr_end_pay(ending_pay);
				dr.setMspr_tsi(ts);
				if(k==1){
					dr.setMspr_premium(premi);
				}else {
					dr.setMspr_premium(uwManager.selectRateRider(data_1.getLku_id(), umur, 0, kd_sm, nm_sm));
				}
				
				dr.setPlan_rider(bisnis);
				dr.setLku_id(data_1.getLku_id());
				c.add(dr);
				
				Simas dt = new Simas();
				dt.setKelamin(kelamin);
				dt.setLsre_id(lsre_id);
				dt.setNama(nama);
				dt.setNo_urut(no_urut);
				if(k==1){
					dt.setPremi(premi);
				}else {
					dt.setPremi(uwManager.selectRateRider(data_1.getLku_id(), umur, 0, kd_sm, nm_sm));
				}
				
				dt.setReg_spaj(reg_spaj);
				dt.setTgl_lahir(tanggallahir);
				dt.setUmur(umur);
				if(a.getDatausulan().getLsbs_id().intValue()==183 || a.getDatausulan().getLsbs_id().intValue()==189 || a.getDatausulan().getLsbs_id().intValue()==193 || a.getDatausulan().getLsbs_id().intValue()==820 || a.getDatausulan().getLsbs_id().intValue()==823 || a.getDatausulan().getLsbs_id().intValue()==825  ){
					dt.setLsbs_id(kd_sm);
					dt.setLsdbs_number(nm_sm);
				}else if(a.getDatausulan().getLsbs_id().intValue()==178 || a.getDatausulan().getLsbs_id().intValue()==821){
					dt.setLsbs_id(kd_sm);
					dt.setLsdbs_number(nm_sm);
				}
				d.add(dt);
				
				//validasi untuk smart medicare, hanya relation suami/istri yg bisa menambahkan tertanggung tambahan.
				if(a.getDatausulan().getLsbs_id().intValue()==178){
					if(k>1){
						if(lsre_id.intValue()!=5){
							errors.reject("", "Plan ke "+k+" Untuk produk SMART MEDICARE, hanya hubungan suami/istri yang bisa tertanggung tambahan");
						}
					}
				}
				
//				validasi berdasarkan umur tertanggung utama untuk produk Eka Sehat
				if(k>1){
						if(kd_sm==820 || kd_sm==823 || kd_sm==825){
							Integer kode_plan = data_1.getLsdbs_number();
							if(kode_plan>15 && data_1.getLsbs_id()==183){
								kode_plan-=15;
							}
							if((nm_sm>15 && nm_sm<31) || nm_sm>105 && nm_sm<121){
								if(sm.getUmur()>55){
									errors.reject("", "Umur Spouse maximum 55 tahun!");
								}else if(sm.getUmur()<17){
									errors.reject("", "Umur Spouse minimum 17 tahun!");
								}
								if(kd_sm==820){
									if((kode_plan.intValue()+105 !=nm_sm)){
										errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Spouse Jenis A)");
									}
								}else{
									if((kode_plan.intValue()+15 !=nm_sm) ){
										errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Spouse Jenis A)");
									}
								}
							}else if((nm_sm>30 && nm_sm <91) || (nm_sm>90 && nm_sm <196) ){
								if(sm.getUmur()<2 && sm.getUmur()>24){
									errors.reject("", "Umur Child harus antara 2 s/d 19 tahun atau 24 tahun jika masih terdaftar secara resmi sebagai pelajar!");
								}
								if(kd_sm==820){
									if((nm_sm>120 && nm_sm <136) ){
										if((kode_plan.intValue()+120 != nm_sm) ){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child1 Jenis A)");
										}
									}
									if((nm_sm>135 && nm_sm<151) ){
										if((kode_plan.intValue()+135 != nm_sm) ){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child2 Jenis A)");
										}
									}
									if((nm_sm>150 && nm_sm<166)){
										if((kode_plan.intValue()+150 != nm_sm) ){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child3 Jenis A)");
										}
									}
									if((nm_sm>165 && nm_sm<181) ){
										if((kode_plan.intValue()+165 != nm_sm) ){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child4 Jenis A)");
										}
									}
								}else{
									if((nm_sm>30 && nm_sm <46)  ){
										if((kode_plan.intValue()+30 != nm_sm)){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child1 Jenis A)");
										}
									}
									if((nm_sm>45 && nm_sm<61) ){
										if((kode_plan.intValue()+45 != nm_sm)){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child2 Jenis A)");
										}
									}
									if((nm_sm>60 && nm_sm<76)){
										if((kode_plan.intValue()+60 != nm_sm)){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child3 Jenis A)");
										}
									}
									if((nm_sm>75 && nm_sm<91) ){
										if((kode_plan.intValue()+75 != nm_sm)){
											errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya(Misal : Basic Jenis A harus mengambil Child4 Jenis A)");
										}
									}
								}
							}
						}else if(kd_sm==821){
							Integer kode_plan = data_1.getLsdbs_number();
							if(nm_sm>=1 && nm_sm<=8){
								if(sm.getUmur()>60){
									errors.reject("", "Untuk yg MPP 5tahun, Umur tidak boleh lebih dari 60");
								}
								if(kode_plan.intValue() !=nm_sm){
									errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya");
								}
							}else if(nm_sm>8 && nm_sm<=16 ){
								if(sm.getUmur()>55){
									errors.reject("", "Untuk yg MPP 10tahun, Umur tidak boleh lebih dari 55");
								}
								if(kode_plan.intValue() != nm_sm ){
									errors.reject("","Jenis Plan Yang diambil tidak sama dengan Plan Utamanya");
								}
							}
						}
					}
			}
			
			a.getDatausulan().setDaftahcp(b);
			a.getDatausulan().setDaftapeserta(d);
			
			
			List datarider = a.getDatausulan().getDaftaRider();
			for (int i = 0 ; i < datarider.size() ; i++)
			{
				Datarider dr1 = (Datarider)datarider.get(i);
				if ((dr1.getLsbs_id().intValue() != 819) && "820,823".indexOf(dr1.getLsbs_id().intValue()) ==-1)
				{
					Datarider dr = new Datarider();
					dr = dr1;
					c.add(dr);
				}
			}
			a.getDatausulan().setDaftaRider(c);
			
			
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac a = (Cmdeditbac) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String status_submit="";
//		a=this.elionsManager.savinghcp(cmd, err, status_submit, currentUser);
		status_submit = a.getDatausulan().getStatus_submit();
		if (status_submit == null)
		{
			status_submit="";
		}
		
		//return new ModelAndView("bac/ttghcp");
		return new ModelAndView("bac/ttghcp_mall",err.getModel()).addAllObjects(this.referenceData(request));

	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}

}