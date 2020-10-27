/**
 * 
 */
package com.ekalife.elions.web.bac.support;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.FilePdf;
import com.ekalife.elions.model.TandaTangan;
import com.ekalife.elions.service.AjaxManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.f_validasi;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class detail Bac Validator
 * 
 * @author Canpri
 * @since Oct 10, 2014 (2:33:52 PM)
 * @param args
 */
public class Editbacdetvalidator implements Serializable{
	protected final Log logger = LogFactory.getLog( getClass() );
	private static final long serialVersionUID = 1460500527878729813L;

	private Properties props;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private DateFormat defaultDateFormat;
	public String err="";
	
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	public void setProps(Properties props) {
		this.props = props;
	}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}
	
	public void Tes(){
		Date a = elionsManager.selectSysdate();
		logger.info("Tanggal : "+a);
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Nama
	 * @param nama, jenis(pemegang, tertanggung, atau pembayar premi)
	 * @return 
	 */
	public String Nama(String nama, String jenis){
		if(nama==null)
			nama="";
		else 
			if(nama.trim().length()==0)
				err="Nama "+jenis+" masih kosong, silahkan masukkan nama "+jenis+" terlebih dahulu";
			else
				err="Nama "+jenis+" tidak boleh mengandung angka";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Alamat
	 * @param alamat, jenis(pemegang, tertanggung, atau pembayar premi)
	 */
	public String Alamat(String alamat, String jenis){
		if(alamat==null)
			alamat="";
		else if(alamat.trim().length()==0)
			err="Alamat "+jenis+" masih kosong, silahkan masukkan alamat "+jenis+" terlebih dahulu";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Kota
	 * @param kota, jenis(pemegang, tertanggung, atau pembayar premi)
	 */
	public String Kota(String kota, String jenis){
		if(kota==null)
			kota="";
		else if(kota.trim().length()==0)
			err="Kota rumah "+jenis+" masih kosong, silahkan masukkan kota "+jenis+" terlebih dahulu";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi kodePos
	 * @param kodePos, jenis(pemegang, tertanggung, atau pembayar premi), tempat(rumah, kantor, atau perusahaan)
	 */
	public String kodePos(String kodePos, String jenis, String tempat){
		boolean cek;
		if (kodePos==null)
			kodePos="";
		else{
			if(kodePos.trim().length()!=0) {
				f_validasi data = new f_validasi();
				cek= f_validasi.f_validasi_numerik(kodePos);	
				if (cek==false)
					err="Silahkan masukkan kode pos "+tempat+" "+jenis+" dalam bentuk numerik";
			}
			else
				err="Kode pos "+jenis+" masih kosong, silahkan masukkan kode pos "+jenis+" terlebih dahulu";
		}
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Provinsi
	 * @param provinsi, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String Provinsi(String provinsi, String jenis){
		if(provinsi==null)
			provinsi="";
		else if(provinsi.trim().length()==0)
			err="Provinsi "+jenis+" masih kosong, silahkan masukkan provinsi "+jenis+" terlebih dahulu";	
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi teleponRumah
	 * @param telepon, tingkat(telepon yang ke-)
	 */	
	public String teleponRumah(String telepon, Integer tingkat){
		if(telepon==null)
			telepon="";
		else{
			if (telepon.trim().length()!=0) {
				f_validasi data = new f_validasi();
				err= data.f_validasi_telp_rmh(telepon);	
			}
			else 
				err="Jika tidak ada telpon rumah "+tingkat+" harap diisi dengan tanda -";
		}
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi teleponKantor
	 * @param telepon, tingkat(telepon yang ke-)
	 */	
	public String teleponKantor(String telepon, Integer tingkat){
		if(telepon==null)
			telepon="";
		else{
			if (telepon.trim().length()!=0) {
				f_validasi data = new f_validasi();
				err= data.f_validasi_telp_ktr(telepon);	
			}
			else		
				err="Jika tidak ada telpon kantor "+tingkat+" harap diisi dengan tanda -";
		}
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi teleponPerusahaan
	 * @param telepon, tingkat(telepon yang ke-)
	 */	
	public String teleponPerusahaan(String telepon, Integer tingkat){
		if(telepon==null)
			telepon="";
		else{
			if (telepon.trim().length()!=0) {
				f_validasi data = new f_validasi();
				err= data.f_validasi_telp_ktr(telepon);	
			}
			else		
				err="Jika tidak ada telpon perusahaan "+tingkat+" harap diisi dengan tanda -";
		}
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Kewarganegaraan
	 * @param kewarganegaraan, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String Kewarganegaraan(String kewarganegaraan, String jenis){
		if(kewarganegaraan==null)
			kewarganegaraan="";
		else if(kewarganegaraan.trim().length()==0)
			err="Kewarganegaraan "+jenis+" masih kosong, silahkan masukkan kewarganegaraan "+jenis+" terlebih dahulu";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi faks
	 * @param faks, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String Faks(String faks, String jenis){
		if(faks==null)
			faks="";
		else{
			if (faks.trim().length()!=0) {
				f_validasi data = new f_validasi();
				err= data.f_validasi_fax(faks);	
			}
			else		
				err="Jika tidak ada telpon kantor "+faks+" harap diisi dengan tanda -";			
		}
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi tempatLahir
	 * @param tempatLahir, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String tempatLahir(String tempatLahir, String jenis){
		if(tempatLahir==null)
			tempatLahir="";
		else if(tempatLahir.trim().length()==0)
			err="Tempat lahir "+jenis+" masih kosong, silahkan masukkan tempat lahir "+jenis+" terlebih dahulu";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi noNpwp
	 * @param noNpwp, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String noNpwp(String noNpwp, String jenis){
		if(noNpwp==null)
			noNpwp="";
		else if(noNpwp.trim().length()==0)
			err="No. NPWP "+jenis+" masih kosong, silahkan masukkan No. NPWP "+jenis+" terlebih dahulu";
		return err;
	}
	
	/**
	 * @author Rahmayanti
	 * validasi Tempat
	 * @param tempat, jenis(pemegang, tertanggung, atau pembayar premi)
	 */	
	public String Tempat(String tempat, String jenis){
		if(tempat==null)
			tempat="";
		else if(tempat.trim().length()==0)
			err="Tempat "+jenis+" masih kosong, silahkan masukkan Tempat "+jenis+" terlebih dahulu";
		return err;
	}
	
	
	
	
	
}