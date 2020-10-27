package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KuesionerPelayananByQuestion implements Serializable{
	
	/**
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Oct 11, 2012 3:01:23 PM
	 * @Description :
	 * 
	 */
	private static final long serialVersionUID = 6517374642732834927L;
	private Double sikap1;
	private Double sikap2;
	private Double sikap3;
	private Double sikap4;
	private Double sikap5;
	
	private Double inisiatif1;
	private Double inisiatif2;
	private Double inisiatif3;
	private Double inisiatif4;
	private Double inisiatif5;
	
	private Double kejelasan_dlm_info1;
	private Double kejelasan_dlm_info2;
	private Double kejelasan_dlm_info3;
	private Double kejelasan_dlm_info4;
	private Double kejelasan_dlm_info5;
	
	private Double solusi1;
	private Double solusi2;
	private Double solusi3;
	private Double solusi4;
	private Double solusi5;
	
	private Double penguasaan_produk1;
	private Double penguasaan_produk2;
	private Double penguasaan_produk3;
	private Double penguasaan_produk4;
	private Double penguasaan_produk5;
	
	private Double ketepatan_waktu1;
	private Double ketepatan_waktu2;
	private Double ketepatan_waktu3;
	private Double ketepatan_waktu4;
	private Double ketepatan_waktu5;
	
	private Double manfaat_produk1;
	private Double manfaat_produk2;
	private Double manfaat_produk3;
	private Double manfaat_produk4;
	private Double manfaat_produk5;
	
	private Double pemahaman_resiko1;
	private Double pemahaman_resiko2;
	private Double pemahaman_resiko3;
	private Double pemahaman_resiko4;
	private Double pemahaman_resiko5;
	
	private Double perlindungan_asuransi1;
	private Double perlindungan_asuransi2;
	private Double perlindungan_asuransi3;
	private Double perlindungan_asuransi4;
	private Double perlindungan_asuransi5;
	
	private Double tarif_premi1;
	private Double tarif_premi2;
	private Double tarif_premi3;
	private Double tarif_premi4;
	private Double tarif_premi5;
	
	private Double kelayakan_beban_biaya1;
	private Double kelayakan_beban_biaya2;
	private Double kelayakan_beban_biaya3;
	private Double kelayakan_beban_biaya4;
	private Double kelayakan_beban_biaya5;
	
	private Double penggunaan_policy1;
	private Double penggunaan_policy2;
	private Double penggunaan_policy3;
	private Double penggunaan_policy4;
	private Double penggunaan_policy5;
	
	private Double ketepatan_waktu_mail_sms1;
	private Double ketepatan_waktu_mail_sms2;
	private Double ketepatan_waktu_mail_sms3;
	private Double ketepatan_waktu_mail_sms4;
	private Double ketepatan_waktu_mail_sms5;
	
	private Double kemudahan_call_center1;
	private Double kemudahan_call_center2;
	private Double kemudahan_call_center3;
	private Double kemudahan_call_center4;
	private Double kemudahan_call_center5;
	
	
	public Double getSikap1() {
		return sikap1;
	}
	public void setSikap1(Double sikap1) {
		this.sikap1 = sikap1;
	}
	public Double getSikap2() {
		return sikap2;
	}
	public void setSikap2(Double sikap2) {
		this.sikap2 = sikap2;
	}
	public Double getSikap3() {
		return sikap3;
	}
	public void setSikap3(Double sikap3) {
		this.sikap3 = sikap3;
	}
	public Double getSikap4() {
		return sikap4;
	}
	public void setSikap4(Double sikap4) {
		this.sikap4 = sikap4;
	}
	public Double getSikap5() {
		return sikap5;
	}
	public void setSikap5(Double sikap5) {
		this.sikap5 = sikap5;
	}
	public Double getInisiatif1() {
		return inisiatif1;
	}
	public void setInisiatif1(Double inisiatif1) {
		this.inisiatif1 = inisiatif1;
	}
	public Double getInisiatif2() {
		return inisiatif2;
	}
	public void setInisiatif2(Double inisiatif2) {
		this.inisiatif2 = inisiatif2;
	}
	public Double getInisiatif3() {
		return inisiatif3;
	}
	public void setInisiatif3(Double inisiatif3) {
		this.inisiatif3 = inisiatif3;
	}
	public Double getInisiatif4() {
		return inisiatif4;
	}
	public void setInisiatif4(Double inisiatif4) {
		this.inisiatif4 = inisiatif4;
	}
	public Double getInisiatif5() {
		return inisiatif5;
	}
	public void setInisiatif5(Double inisiatif5) {
		this.inisiatif5 = inisiatif5;
	}
	public Double getKejelasan_dlm_info1() {
		return kejelasan_dlm_info1;
	}
	public void setKejelasan_dlm_info1(Double kejelasanDlmInfo1) {
		kejelasan_dlm_info1 = kejelasanDlmInfo1;
	}
	public Double getKejelasan_dlm_info2() {
		return kejelasan_dlm_info2;
	}
	public void setKejelasan_dlm_info2(Double kejelasanDlmInfo2) {
		kejelasan_dlm_info2 = kejelasanDlmInfo2;
	}
	public Double getKejelasan_dlm_info3() {
		return kejelasan_dlm_info3;
	}
	public void setKejelasan_dlm_info3(Double kejelasanDlmInfo3) {
		kejelasan_dlm_info3 = kejelasanDlmInfo3;
	}
	public Double getKejelasan_dlm_info4() {
		return kejelasan_dlm_info4;
	}
	public void setKejelasan_dlm_info4(Double kejelasanDlmInfo4) {
		kejelasan_dlm_info4 = kejelasanDlmInfo4;
	}
	public Double getKejelasan_dlm_info5() {
		return kejelasan_dlm_info5;
	}
	public void setKejelasan_dlm_info5(Double kejelasanDlmInfo5) {
		kejelasan_dlm_info5 = kejelasanDlmInfo5;
	}
	public Double getSolusi1() {
		return solusi1;
	}
	public void setSolusi1(Double solusi1) {
		this.solusi1 = solusi1;
	}
	public Double getSolusi2() {
		return solusi2;
	}
	public void setSolusi2(Double solusi2) {
		this.solusi2 = solusi2;
	}
	public Double getSolusi3() {
		return solusi3;
	}
	public void setSolusi3(Double solusi3) {
		this.solusi3 = solusi3;
	}
	public Double getSolusi4() {
		return solusi4;
	}
	public void setSolusi4(Double solusi4) {
		this.solusi4 = solusi4;
	}
	public Double getSolusi5() {
		return solusi5;
	}
	public void setSolusi5(Double solusi5) {
		this.solusi5 = solusi5;
	}
	public Double getPenguasaan_produk1() {
		return penguasaan_produk1;
	}
	public void setPenguasaan_produk1(Double penguasaanProduk1) {
		penguasaan_produk1 = penguasaanProduk1;
	}
	public Double getPenguasaan_produk2() {
		return penguasaan_produk2;
	}
	public void setPenguasaan_produk2(Double penguasaanProduk2) {
		penguasaan_produk2 = penguasaanProduk2;
	}
	public Double getPenguasaan_produk3() {
		return penguasaan_produk3;
	}
	public void setPenguasaan_produk3(Double penguasaanProduk3) {
		penguasaan_produk3 = penguasaanProduk3;
	}
	public Double getPenguasaan_produk4() {
		return penguasaan_produk4;
	}
	public void setPenguasaan_produk4(Double penguasaanProduk4) {
		penguasaan_produk4 = penguasaanProduk4;
	}
	public Double getPenguasaan_produk5() {
		return penguasaan_produk5;
	}
	public void setPenguasaan_produk5(Double penguasaanProduk5) {
		penguasaan_produk5 = penguasaanProduk5;
	}
	public Double getKetepatan_waktu1() {
		return ketepatan_waktu1;
	}
	public void setKetepatan_waktu1(Double ketepatanWaktu1) {
		ketepatan_waktu1 = ketepatanWaktu1;
	}
	public Double getKetepatan_waktu2() {
		return ketepatan_waktu2;
	}
	public void setKetepatan_waktu2(Double ketepatanWaktu2) {
		ketepatan_waktu2 = ketepatanWaktu2;
	}
	public Double getKetepatan_waktu3() {
		return ketepatan_waktu3;
	}
	public void setKetepatan_waktu3(Double ketepatanWaktu3) {
		ketepatan_waktu3 = ketepatanWaktu3;
	}
	public Double getKetepatan_waktu4() {
		return ketepatan_waktu4;
	}
	public void setKetepatan_waktu4(Double ketepatanWaktu4) {
		ketepatan_waktu4 = ketepatanWaktu4;
	}
	public Double getKetepatan_waktu5() {
		return ketepatan_waktu5;
	}
	public void setKetepatan_waktu5(Double ketepatanWaktu5) {
		ketepatan_waktu5 = ketepatanWaktu5;
	}
	public Double getManfaat_produk1() {
		return manfaat_produk1;
	}
	public void setManfaat_produk1(Double manfaatProduk1) {
		manfaat_produk1 = manfaatProduk1;
	}
	public Double getManfaat_produk2() {
		return manfaat_produk2;
	}
	public void setManfaat_produk2(Double manfaatProduk2) {
		manfaat_produk2 = manfaatProduk2;
	}
	public Double getManfaat_produk3() {
		return manfaat_produk3;
	}
	public void setManfaat_produk3(Double manfaatProduk3) {
		manfaat_produk3 = manfaatProduk3;
	}
	public Double getManfaat_produk4() {
		return manfaat_produk4;
	}
	public void setManfaat_produk4(Double manfaatProduk4) {
		manfaat_produk4 = manfaatProduk4;
	}
	public Double getManfaat_produk5() {
		return manfaat_produk5;
	}
	public void setManfaat_produk5(Double manfaatProduk5) {
		manfaat_produk5 = manfaatProduk5;
	}
	public Double getPemahaman_resiko1() {
		return pemahaman_resiko1;
	}
	public void setPemahaman_resiko1(Double pemahamanResiko1) {
		pemahaman_resiko1 = pemahamanResiko1;
	}
	public Double getPemahaman_resiko2() {
		return pemahaman_resiko2;
	}
	public void setPemahaman_resiko2(Double pemahamanResiko2) {
		pemahaman_resiko2 = pemahamanResiko2;
	}
	public Double getPemahaman_resiko3() {
		return pemahaman_resiko3;
	}
	public void setPemahaman_resiko3(Double pemahamanResiko3) {
		pemahaman_resiko3 = pemahamanResiko3;
	}
	public Double getPemahaman_resiko4() {
		return pemahaman_resiko4;
	}
	public void setPemahaman_resiko4(Double pemahamanResiko4) {
		pemahaman_resiko4 = pemahamanResiko4;
	}
	public Double getPemahaman_resiko5() {
		return pemahaman_resiko5;
	}
	public void setPemahaman_resiko5(Double pemahamanResiko5) {
		pemahaman_resiko5 = pemahamanResiko5;
	}
	public Double getPerlindungan_asuransi1() {
		return perlindungan_asuransi1;
	}
	public void setPerlindungan_asuransi1(Double perlindunganAsuransi1) {
		perlindungan_asuransi1 = perlindunganAsuransi1;
	}
	public Double getPerlindungan_asuransi2() {
		return perlindungan_asuransi2;
	}
	public void setPerlindungan_asuransi2(Double perlindunganAsuransi2) {
		perlindungan_asuransi2 = perlindunganAsuransi2;
	}
	public Double getPerlindungan_asuransi3() {
		return perlindungan_asuransi3;
	}
	public void setPerlindungan_asuransi3(Double perlindunganAsuransi3) {
		perlindungan_asuransi3 = perlindunganAsuransi3;
	}
	public Double getPerlindungan_asuransi4() {
		return perlindungan_asuransi4;
	}
	public void setPerlindungan_asuransi4(Double perlindunganAsuransi4) {
		perlindungan_asuransi4 = perlindunganAsuransi4;
	}
	public Double getPerlindungan_asuransi5() {
		return perlindungan_asuransi5;
	}
	public void setPerlindungan_asuransi5(Double perlindunganAsuransi5) {
		perlindungan_asuransi5 = perlindunganAsuransi5;
	}
	public Double getTarif_premi1() {
		return tarif_premi1;
	}
	public void setTarif_premi1(Double tarifPremi1) {
		tarif_premi1 = tarifPremi1;
	}
	public Double getTarif_premi2() {
		return tarif_premi2;
	}
	public void setTarif_premi2(Double tarifPremi2) {
		tarif_premi2 = tarifPremi2;
	}
	public Double getTarif_premi3() {
		return tarif_premi3;
	}
	public void setTarif_premi3(Double tarifPremi3) {
		tarif_premi3 = tarifPremi3;
	}
	public Double getTarif_premi4() {
		return tarif_premi4;
	}
	public void setTarif_premi4(Double tarifPremi4) {
		tarif_premi4 = tarifPremi4;
	}
	public Double getTarif_premi5() {
		return tarif_premi5;
	}
	public void setTarif_premi5(Double tarifPremi5) {
		tarif_premi5 = tarifPremi5;
	}
	public Double getKelayakan_beban_biaya1() {
		return kelayakan_beban_biaya1;
	}
	public void setKelayakan_beban_biaya1(Double kelayakanBebanBiaya1) {
		kelayakan_beban_biaya1 = kelayakanBebanBiaya1;
	}
	public Double getKelayakan_beban_biaya2() {
		return kelayakan_beban_biaya2;
	}
	public void setKelayakan_beban_biaya2(Double kelayakanBebanBiaya2) {
		kelayakan_beban_biaya2 = kelayakanBebanBiaya2;
	}
	public Double getKelayakan_beban_biaya3() {
		return kelayakan_beban_biaya3;
	}
	public void setKelayakan_beban_biaya3(Double kelayakanBebanBiaya3) {
		kelayakan_beban_biaya3 = kelayakanBebanBiaya3;
	}
	public Double getKelayakan_beban_biaya4() {
		return kelayakan_beban_biaya4;
	}
	public void setKelayakan_beban_biaya4(Double kelayakanBebanBiaya4) {
		kelayakan_beban_biaya4 = kelayakanBebanBiaya4;
	}
	public Double getKelayakan_beban_biaya5() {
		return kelayakan_beban_biaya5;
	}
	public void setKelayakan_beban_biaya5(Double kelayakanBebanBiaya5) {
		kelayakan_beban_biaya5 = kelayakanBebanBiaya5;
	}
	public Double getPenggunaan_policy1() {
		return penggunaan_policy1;
	}
	public void setPenggunaan_policy1(Double penggunaanPolicy1) {
		penggunaan_policy1 = penggunaanPolicy1;
	}
	public Double getPenggunaan_policy2() {
		return penggunaan_policy2;
	}
	public void setPenggunaan_policy2(Double penggunaanPolicy2) {
		penggunaan_policy2 = penggunaanPolicy2;
	}
	public Double getPenggunaan_policy3() {
		return penggunaan_policy3;
	}
	public void setPenggunaan_policy3(Double penggunaanPolicy3) {
		penggunaan_policy3 = penggunaanPolicy3;
	}
	public Double getPenggunaan_policy4() {
		return penggunaan_policy4;
	}
	public void setPenggunaan_policy4(Double penggunaanPolicy4) {
		penggunaan_policy4 = penggunaanPolicy4;
	}
	public Double getPenggunaan_policy5() {
		return penggunaan_policy5;
	}
	public void setPenggunaan_policy5(Double penggunaanPolicy5) {
		penggunaan_policy5 = penggunaanPolicy5;
	}
	public Double getKetepatan_waktu_mail_sms1() {
		return ketepatan_waktu_mail_sms1;
	}
	public void setKetepatan_waktu_mail_sms1(Double ketepatanWaktuMailSms1) {
		ketepatan_waktu_mail_sms1 = ketepatanWaktuMailSms1;
	}
	public Double getKetepatan_waktu_mail_sms2() {
		return ketepatan_waktu_mail_sms2;
	}
	public void setKetepatan_waktu_mail_sms2(Double ketepatanWaktuMailSms2) {
		ketepatan_waktu_mail_sms2 = ketepatanWaktuMailSms2;
	}
	public Double getKetepatan_waktu_mail_sms3() {
		return ketepatan_waktu_mail_sms3;
	}
	public void setKetepatan_waktu_mail_sms3(Double ketepatanWaktuMailSms3) {
		ketepatan_waktu_mail_sms3 = ketepatanWaktuMailSms3;
	}
	public Double getKetepatan_waktu_mail_sms4() {
		return ketepatan_waktu_mail_sms4;
	}
	public void setKetepatan_waktu_mail_sms4(Double ketepatanWaktuMailSms4) {
		ketepatan_waktu_mail_sms4 = ketepatanWaktuMailSms4;
	}
	public Double getKetepatan_waktu_mail_sms5() {
		return ketepatan_waktu_mail_sms5;
	}
	public void setKetepatan_waktu_mail_sms5(Double ketepatanWaktuMailSms5) {
		ketepatan_waktu_mail_sms5 = ketepatanWaktuMailSms5;
	}
	public Double getKemudahan_call_center1() {
		return kemudahan_call_center1;
	}
	public void setKemudahan_call_center1(Double kemudahanCallCenter1) {
		kemudahan_call_center1 = kemudahanCallCenter1;
	}
	public Double getKemudahan_call_center2() {
		return kemudahan_call_center2;
	}
	public void setKemudahan_call_center2(Double kemudahanCallCenter2) {
		kemudahan_call_center2 = kemudahanCallCenter2;
	}
	public Double getKemudahan_call_center3() {
		return kemudahan_call_center3;
	}
	public void setKemudahan_call_center3(Double kemudahanCallCenter3) {
		kemudahan_call_center3 = kemudahanCallCenter3;
	}
	public Double getKemudahan_call_center4() {
		return kemudahan_call_center4;
	}
	public void setKemudahan_call_center4(Double kemudahanCallCenter4) {
		kemudahan_call_center4 = kemudahanCallCenter4;
	}
	public Double getKemudahan_call_center5() {
		return kemudahan_call_center5;
	}
	public void setKemudahan_call_center5(Double kemudahanCallCenter5) {
		kemudahan_call_center5 = kemudahanCallCenter5;
	}
	
	
	
}
