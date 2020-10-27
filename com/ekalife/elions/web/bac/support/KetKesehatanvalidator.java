package com.ekalife.elions.web.bac.support;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.service.ElionsManager;

/**
 * @author HEMILDA
 * validator KetKesehatanController
 */	
public class KetKesehatanvalidator implements Validator {
	private ElionsManager elionsManager;

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public boolean supports(Class data) {
		return Kesehatan.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
		Kesehatan edit = (Kesehatan) cmd;
		Integer sehat = edit.getMsnm_sehat();
		if (sehat == null)
		{
			sehat = new Integer(1);
			edit.setMsnm_sehat(sehat);
		}
		if (sehat.intValue()==0)
		{
			String alasan_sehat = edit.getMsnm_alasan_sehat();
			if (alasan_sehat==null)
			{
				alasan_sehat="";
			}
			if (alasan_sehat.trim().length() == 0)
			{
				err.rejectValue("msnm_alasan_sehat", "", "Silahkan alasan keadaan tidak sehat terlebih dahulu.");
			}
		}
		Integer dirawat = edit.getMsnm_perawatan();
		if (dirawat == null)
		{
			dirawat = new Integer(0);
			edit.setMsnm_perawatan(dirawat);
		}
		
		Integer dioperasi = edit.getMsnm_operasi();
		if (dioperasi == null)
		{
			dioperasi = new Integer(0);
			edit.setMsnm_operasi(dioperasi);
		}
		
		Integer badan = edit.getMsnm_berubah();
		if (badan == null)
		{
			badan = new Integer(0);
			edit.setMsnm_berubah(badan);
		}
		
		String keadaan = edit.getKeadaan();
		if (keadaan ==null)
		{
			keadaan="0";
			edit.setKeadaan(keadaan);
		}

		if (badan.intValue() == 1 && keadaan.equalsIgnoreCase("0"))
		{
			err.rejectValue("msnm_turun_naik","","Silahkan dipilih keadaan berat badan naik atau turun terlebih dahulu.");
		}else{
			if (badan.intValue() == 0)
			{
				keadaan="0";
				edit.setKeadaan(keadaan);
			}
		}
		
		edit.setMsnm_turun_naik(Integer.parseInt(keadaan));
		
		if ( keadaan.equalsIgnoreCase("1") || keadaan.equalsIgnoreCase("2"))
		{
			Integer banyak = edit.getMsnm_banyak();
			if (banyak == null)
			{
				banyak = new Integer(0);
				edit.setMsnm_banyak(banyak);
			}
			if (banyak.intValue() == 0)
			{
				err.rejectValue("msnm_banyak", "","Silahkan diisi terlebih dahulu besarnya perubahan berat badan dalam KG.");
			}
			if (edit.getMsnm_alasan_berat() == null)
			{
				edit.setMsnm_alasan_berat("");
			}
			if (edit.getMsnm_alasan_berat().trim().length() == 0)
			{
				err.rejectValue("msnm_alasan_berat","", "Silahkan isi alasan perubahan berat badan terlebih dahulu.");
			}
		}else{
			edit.setMsnm_banyak(null);
			edit.setMsnm_alasan_berat(null);
		}
		
		Integer berat = edit.getMsnm_berat();
		if (berat==null)
		{
			berat = new Integer(0);
			edit.setMsnm_berat(berat);
		}
		if (berat.intValue() == 0)
		{
			err.rejectValue("msnm_berat", "", "Silahkan isi berat terlebih dahulu.");
		}
		
		Integer tinggi = edit.getMsnm_tinggi();
		if (tinggi == null)
		{
			tinggi = new Integer(0);
			edit.setMsnm_tinggi(tinggi);
		}
		if (tinggi.intValue() == 0)
		{
			err.rejectValue("msnm_tinggi", "", "Silahkan isi tinggi terlebih dahulu.");
		}
		
		String hobi = edit.getMsnm_hobi();
		if (hobi == null)
		{
			hobi ="";
		}
		if (hobi.trim().length() == 0)
		{
			err.rejectValue("msnm_hobi","", "Silahkan isi hobi/jenis olahraga terlebih dahulu.");
		}
		
		Integer cacat = edit.getMsnm_cacat();
		if (cacat == null)
		{
			cacat = new Integer(0);
			edit.setMsnm_cacat(cacat);
		}
		if (cacat.intValue() == 1)
		{
			String alasan_cacat = edit.getMsnm_alasan_cacat();
			if (alasan_cacat == null)
			{
				alasan_cacat ="";
			}
			if (alasan_cacat.trim().length()==0)
			{
				err.rejectValue("msnm_alasan_cacat", "", "Silahkan isi alasan menderita cacat terlebih dahulu.");
			}
		}
		Integer jumlah  = new Integer(0);
		Integer tumor = edit.getMsnm_tumor();
		if (tumor == null)
		{
			edit.setMsnm_tumor(new Integer(0));
		}else{
			if (tumor.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer asma = edit.getMsnm_asma();
		if (asma == null)
		{
			edit.setMsnm_asma(new Integer(0));
		}
		else{
			if (asma.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer diabetes = edit.getMsnm_diabetes();
		if (diabetes == null)
		{
			edit.setMsnm_diabetes(new Integer(0));
		}else{
			if (diabetes.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer lambung = edit.getMsnm_lambung();
		if ( lambung == null)
		{
			edit.setMsnm_lambung(new Integer(0));
		}else{
			if (lambung.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer jantung = edit.getMsnm_jantung();
		if (jantung == null)
		{
			edit.setMsnm_jantung(new Integer(0));
		}else{
			if (jantung.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer pingsan = edit.getMsnm_jantung();
		if (pingsan == null)
		{
			edit.setMsnm_pingsan(new Integer(0));
		}else{
			if (pingsan.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer darahtinggi = edit.getMsnm_darah_tinggi();
		if (darahtinggi == null)
		{
			edit.setMsnm_darah_tinggi(new Integer(0));
		}else{
			if (darahtinggi.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer kelainan = edit.getMsnm_kelainan();
		if (kelainan == null)
		{
			edit.setMsnm_kelainan(new Integer(0));
		}else{
			if (kelainan.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer gila = edit.getMsnm_gila();
		if (gila == null)
		{
			edit.setMsnm_gila(new Integer(0));
		}else{
			if (gila.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		Integer lain = edit.getMsnm_lain();
		if (lain ==null)
		{
			edit.setMsnm_lain(new Integer(0));
		}else{
			if (lain.intValue() == 1)
			{
				jumlah = new Integer(jumlah.intValue() + 1 );
			}
		}
		
		if (jumlah.intValue() > 0)
		{
			String alasan_sakit = edit.getMsnm_desc_sakit();
			if (alasan_sakit==null)
			{
				alasan_sakit ="";
			}
			if (alasan_sakit.equalsIgnoreCase(""))
			{
				err.rejectValue("msnm_desc_sakit","", "Silahkan isi terlebih dahulu penjelasan mengenai penyakit.");
			}
		}else{
			edit.setMsnm_desc_sakit("");
		}
		
	}
}
