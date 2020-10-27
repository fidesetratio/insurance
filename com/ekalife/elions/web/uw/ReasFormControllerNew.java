package com.ekalife.elions.web.uw;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class ReasFormControllerNew extends ParentFormController {

	DecimalFormat fmt = new DecimalFormat ("000");
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Reas reas=new Reas();
		reas.setInfo(new Integer(0));
		reas.setLspdId(new Integer(2));
		reas.setLstbId(new Integer(1));
		String las_reas[]=new String[3];
		las_reas[0]="Non-Reas";
		las_reas[1]="Treaty";
		las_reas[2]="Facultative";
		reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));        

		reas.setSpaj(request.getParameter("spaj"));
		Map mPosisi=elionsManager.selectF_check_posisi(reas.getSpaj());
		Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
		//produk asm
		Map map=uwManager.selectDataUsulan(reas.getSpaj());
		Integer lsbsId=(Integer)map.get("LSBS_ID");

		//validasi Posisi dokumen
		if(lspdIdTemp.intValue()!=2){
			reas.setInfo(new Integer(1));
			reas.setLsPosDoc(lspdPosTemp);
			//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
		}else if(lsbsId.intValue()==161){//produk asm
			reas.setInfo(5);
		}
		//
		//tertanggung
		Map mTertanggung=elionsManager.selectTertanggung(reas.getSpaj());
		reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
		reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
		reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
		//
		Map mStatus=elionsManager.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
		reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
		reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
		if (reas.getLiAksep()==null) 
			reas.setLiAksep( new Integer(1));
		
		
		//dw1 //pemegang
		Policy policy=elionsManager.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
		if(policy!=null){
			reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
			reas.setNoPolis(policy.getMspo_policy_no());
			reas.setInsPeriod(policy.getMspo_ins_period());
			reas.setPayPeriod(policy.getMspo_pay_period());
			reas.setLkuId(policy.getLku_id());
			reas.setUmurPp(policy.getMspo_age());
			reas.setLcaId(policy.getLca_id());
			reas.setLcaId(policy.getLca_id());
			reas.setMste_kyc_date(policy.getMste_kyc_date());
			if(reas.getMste_kyc_date()==null){
				reas.setInfo(new Integer(6));
			}		
			if ( (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("XX")) || (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("WW")) ){
				reas.setInfo(new Integer(2));
				//MessageBox('Info', 'Proses Simultan Belum Dilakukan !!!')
			}else if (reas.getLiReas()!=null){
				Integer liBackup=(Integer)elionsManager.selectMstInsuredMsteBackup(reas.getSpaj(),reas.getInsuredNo());
				if(liBackup==null)
					liBackup=new Integer(100);
				if(liBackup.intValue()!=0 || (liBackup.intValue()==0 && reas.getLiReas().intValue()==2) ){
					reas.setInfo(new Integer(3));
					reas.setLsPosDoc(las_reas[reas.getLiReas().intValue()]);
	//				If MessageBox( 'Information', 'Proses Reas sudah pernah dilakukan~r~nType Reas = ' + las_reas[li_reas+1] &
	//				+  '~r~nView hasil proses sebelumnya ?', Exclamation!, OKCancel!, 1 ) = 1 Then OpenWithParm(w_simultan,lstr_polis.no_spaj)
				}
				
			}
			//cek standard
			if(policy.getMste_standard().intValue()==1){
			Integer liCount=elionsManager.selectCountMstProductInsuredCekStandard(reas.getSpaj());
				if(liCount.intValue()==0){
					//li_count = Messagebox('Info', 'Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?', Question!, Yesno!, 2)
					reas.setInfo(new Integer(4));
					//info=4;
				}
			}
		}
		return reas;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		//list=sqlMap.queryForList("elions.uw.select.product_utama",txtnospaj);
		Reas reas=(Reas)cmd;
		List ldsBisnis=elionsManager.selectProductUtama(reas.getSpaj());
		if(ldsBisnis.size()<=0){
			Product product=(Product)ldsBisnis.get(0);
			err.reject("","PLAN BARU..!!! Bisnis Id= "+product.getLsbs_id()+" Bisnis no= "+product.getLsdbs_number()+" Kurs="+product.getLku_id()+" Kontak Aktuaria");
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Reas reas=(Reas)cmd;
		String success="";
		Integer liReas=null;
		Integer medis=0;
		Map proses=elionsManager.prosesReasIndividuNew(reas, err);
		String hasil = "";
		Integer flag=null;

		
		if(proses!=null){
			liReas= (Integer) proses.get("liReas");
			medis= (Integer) proses.get("medis");
			if(medis==null)
				medis=0;

			flag=new Integer(100);
			success="true";
			if(liReas==0)//backup =3
				hasil="Non Reas";
			else if(liReas==1)//bakcup=2
				hasil="Treaty";
			else//bakcup  =0
				hasil="Facultative";
		}
		reas.setInfo(flag);
		return new ModelAndView("uw/reas_new", err.getModel()).addObject("submitSuccess", success).addObject("proses",hasil).addObject("medis",medis);
	}
}
