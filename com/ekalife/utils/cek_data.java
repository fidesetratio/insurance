/*
 * Created on Aug 25, 2005
 */
package com.ekalife.utils;

/**
 * @author FerryH
 *
 */
public class cek_data {
	public String cek_marital_status(String marital)
	{
		int status;
		status=Integer.parseInt(marital);
		if(status==1)
			marital="Belum Menikah";
		else if(status==2)
			marital="Menikah";
		else if(status==3)
			marital="Janda";
		else
			marital="Duda";
		return marital;
		
	}
	
	/**
	 * Fungsi getPoint untuk mengambil jumlah point yang di dapat dari program Tambang Emas
	 * 
	 * @author Canpri Setiawan
	 * @since 15 Jun 2012
	 * @param premi new bisnis, lsbs_id, lsdbs_number, top up single, top up berkala
	 * untuk point dari premi pokok param top_up_single = null
	 * untuk point dari premi top up single param top_up_berkala = null
	 * @return nilai point yang di dapat
	 */
	public Integer getPoint(Double premi, Integer lsbs_id, Integer lsdbs_number, Double top_up_single, Double top_up_berkala){
		Integer point = 0;
		
		//EKALINK FAMILY
		//ekalink family regular atau ekalink family syariah regular
		if((lsbs_id==159 && lsdbs_number==2) || (lsbs_id==160 && lsdbs_number==2)){
			if(top_up_berkala != null){
				if(top_up_berkala==null)top_up_berkala = (double) 0;
				//point = ((int) (top_up_single/70000)) + ((int) (top_up_berkala/70000));
				point = (int) (top_up_berkala/70000);
			}else if(top_up_single != null){
				if(top_up_single==null)top_up_single = (double) 0;
				point = (int)(top_up_single/70000);
			}else{
				point = (int) (premi/4000);
			}
		}
		//ekalink family single/top up atauekalink family syariah single/top up
		if((lsbs_id==159 && lsdbs_number==1) || (lsbs_id==160 && lsdbs_number==1)){
			if(top_up_berkala != null){
				if(top_up_berkala==null)top_up_berkala = (double) 0;
				//point = ((int) (top_up_single/70000)) + ((int) (top_up_berkala/70000));
				point = (int) (top_up_berkala/70000);
			}else if(top_up_single != null){
				if(top_up_single==null)top_up_single = (double) 0;
				point = (int) (top_up_single/70000);
			}else{
				point = (int) (premi/70000);
			}
		}
		
		//EKALINK 80 PLUS
		//ekalink 80 plus regular atau ekalink 80 plus syariah regular
		//Smile Link 100 and Smile Link 100 syariah
		if((lsbs_id==116 && lsdbs_number==4) || (lsbs_id==118 && lsdbs_number==4) || (lsbs_id==153 && lsdbs_number==4) || (lsbs_id==153 && lsdbs_number==6) || (lsbs_id==217 ) || (lsbs_id==218)){
			if(top_up_berkala != null){
				if(top_up_berkala==null)top_up_berkala = (double) 0;
				//point = ((int) (top_up_single/70000)) + ((int) (top_up_berkala/70000));
				point = (int) (top_up_berkala/70000);
			}else if(top_up_single != null){
				if(top_up_single==null)top_up_single = (double) 0;
				point = (int) (top_up_single/70000);
			}else{
				point = (int) (premi/5000);
			}
		}
		//ekalink 80 plus single/top up atau ekalink 80 plus syariah single/top up
		if((lsbs_id==116 && lsdbs_number==3) || (lsbs_id==118 && lsdbs_number==3) || (lsbs_id==153 && lsdbs_number==3)){
			if(top_up_berkala != null){
				if(top_up_berkala==null)top_up_berkala = (double) 0;
				//point = ((int) (top_up_single/70000)) + ((int) (top_up_berkala/70000));
				point = (int) (top_up_berkala/70000);
			}else if(top_up_single != null){
				if(top_up_single==null)top_up_single = (double) 0;
				point = (int) (top_up_single/70000);
			}else{
				point = (int) (premi/70000);
			}
		}
		
		//EKALINK 80
		//ekalink 80 atau ekalink 80 syariah
		if((lsbs_id==115 && lsdbs_number==2) || (lsbs_id==117 && lsdbs_number==2) || (lsbs_id==152 && lsdbs_number==2)){
			if(top_up_berkala != null){
				if(top_up_berkala==null)top_up_berkala = (double) 0;
				//point = ((int) (top_up_single/140000)) + ((int) (top_up_berkala/140000));
				point = (int) (top_up_berkala/140000);
			}else if(top_up_single != null){
				if(top_up_single==null)top_up_single = (double) 0;
				point = (int) (top_up_single/140000);
			}else{
				point = (int) (premi/70000);
			}
		}
		
		//EKA PROTEKSI
		//eka proteksi new
		if((lsbs_id==181 && lsdbs_number==2) || (lsbs_id==181 && lsdbs_number==3))point = (int) (premi/5000);
		//eka proteksi new single
		if((lsbs_id==181 && lsdbs_number==1))point = (int) (premi/20000);
		
		//SUPER SEJAHTERA
		//super sejahtera
		if((lsbs_id==185 && lsdbs_number!=5) || (lsbs_id==185 && lsdbs_number!=10))point = (int) (premi/7000);
		//super sejahtera single
		if((lsbs_id==185 && lsdbs_number==5) || (lsbs_id==185 && lsdbs_number==10))point = (int) (premi/20000);
		
		//MULTI INVEST
		//multi invest atau multi invest syariah
		if(lsbs_id==74 || lsbs_id==76 || lsbs_id==96 || lsbs_id==99 || lsbs_id==135 || lsbs_id==136 || lsbs_id==182)point = (int) (premi/10000);
		
		//EKA SARJANA MANDIRI
		//eka sarjana mandiri new
		if((lsbs_id==173 && lsdbs_number==2) || (lsbs_id==173 && lsdbs_number==3))point = (int) (premi/10000);
		//eka sarjana mandiri new single
		if(lsbs_id==173 && lsdbs_number==1)point = (int) (premi/20000);
		
		//HCP Provider Stand Alone 
		if(lsbs_id==195 || lsbs_id==204)point = (int) (premi/10000);
		//PAS
		if(lsbs_id==187)point = (int) (premi/20000);
		
		return point;
	}
	
}
