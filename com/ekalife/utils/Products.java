/**
 * 
 */
package com.ekalife.utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.FilePdf;
import com.ekalife.elions.model.TandaTangan;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfStamper;

import produk_asuransi.n_prod;
/**
 * Class yang digunakan untuk fungsi2 yang berhubungan dengan produk (mengambil
 * nilai dari <b>WEB-INF/ekalife.properties</b> atau dari <b>n_prod</b>
 * bersangkutan)
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class Products implements Serializable{
	protected static final Log logger = LogFactory.getLog( Products.class );
	private static final long serialVersionUID = 1460500527878729813L;

	private Properties props;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public void setProps(Properties props) {
		this.props = props;
	}
	
	
	/**
	 * Fungsi main, untuk sekedar mengetest value2 dari n_prod saja
	 * 
	 * @author Yusuf
	 * @since Feb 13, 2008 (2:33:52 PM)
	 * @param args
	 */
	public static void main(String[] args) {
		String prod;
		
		for(int i=0; i<=300; i++) {			
			prod ="produk_asuransi.n_prod_"+FormatString.rpad("0", String.valueOf(i), 2);
//			if(i == 126) {
//				logger.info("asdf");
//			}
			try {
				Class aClass = Class.forName(prod);
				n_prod produk = (n_prod) aClass.newInstance();
				for(int j=1; j<10; j++) {
					produk.of_set_bisnis_no(j);
					for(int x=1; x < produk.ii_pmode_list.length; x++) {
						int y = produk.ii_pmode_list[x];
						logger.info(i + ";" + produk.ii_bisnis_no + ";" + y + ";" +  
								(y == 0 ? "sekaligus" : y == 1 ? "triwulanan" : y == 2 ? "semesteran" : y == 3 ? "tahunan" : y == 6 ? "bulanan" : ""));
					}
				}
			} catch(ArrayIndexOutOfBoundsException a) {
				logger.error(a);
			} catch (Exception e) {
			}
		}
		
//		int hit = 0;
//		String prod;
//		int k1, k2;
//		
//		int j = 100;
//		
//		for(int maunya=0; maunya<j; maunya++) {
//			logger.info("KODE_FLAG = " + maunya);
//			//produk utama
//			for(int i=0; i<=300; i++) {			
//				prod ="produk_asuransi.n_prod_"+FormatString.rpad("0", String.valueOf(i), 2);
//				try {
//					Class aClass = Class.forName(prod);
//					n_prod produk = (n_prod) aClass.newInstance();
//					produk.of_set_kurs("01");
//					k1 = produk.kode_flag;
//					produk.of_set_kurs("02");
//					k2 = produk.kode_flag;
//					
//					if(k1 == k2 && k1 == maunya) {
//						logger.info(i + " = " + k1);
//					}else if(k1 != k2) {
//						if(k1 == maunya) logger.info(i + " [01] = " + k1);
//						if(k2 == maunya) logger.info(i + " [02] = " + k2);
//					}
//				} catch (Exception e) {}
//			}
//			
//			logger.info("--------------------------------------------------------------------------------------------");
//			
//			//produk rider
//			for(int i=800; i<=900; i++) {			
//				prod ="produk_asuransi.n_prod_"+FormatString.rpad("0", String.valueOf(i), 2);
//				try {
//					Class aClass = Class.forName(prod);
//					n_prod produk = (n_prod) aClass.newInstance();
//					produk.of_set_kurs("01");
//					k1 = produk.kode_flag;
//					produk.of_set_kurs("02");
//					k2 = produk.kode_flag;
//					
//					if(k1 == k2 && k1 != 0) {
//						logger.info(i + " = " + k1);
//					}else if(k1 != k2) {
//						if(k1 != 0) logger.info(i + " [01] = " + k1);
//						if(k2 != 0) logger.info(i + " [02] = " + k2);
//					}
//				} catch (Exception e) {}
//			}
//		}
		
	}
	
	@SuppressWarnings("unchecked")
	private boolean isProductInCategory(String kategori, String businessID) {
		String[] array = props.getProperty(kategori).split(",");
		for (int i = 0; i < array.length; i++)
			array[i] = array[i].trim();
		Set set = new HashSet(Arrays.asList(array));
		if (set.contains(FormatString.rpad("0", businessID, 3))) {
			return true;
		} else
			return false;		
	}

	public boolean isKaryawan(String lsbs, String lsdbs){
		return isProductInCategory("product.karyawan", 
				FormatString.rpad("0", lsbs, 3)+FormatString.rpad("0", lsdbs, 3));
	}
	
	public boolean isUatPassed(String businessId) {
		return isProductInCategory("product.uat", businessId);
	}

	public boolean cerdas(String businessId) {
		return isProductInCategory("product.cerdas", businessId);
	}
	
	public boolean unitLink(String businessId) {
		return isProductInCategory("product.unitLink", businessId);
	}
	
	public boolean provider(String businessId) {
		return isProductInCategory("product.provider", businessId);
	}

	public boolean unitLinkSyariah(String businessId) {
		return isProductInCategory("product.unitLink.syariah", businessId);
	}

	public boolean ekaSiswa(String businessID) {
		return isProductInCategory("product.ekasiswa", businessID);
	}

	public boolean ekaProteksi(String businessID) {
		return isProductInCategory("product.ekaproteksi", businessID);
	}

	public boolean newNilaiTunai(String businessID) {
		return isProductInCategory("product.nilaiTunai.new", businessID);
	}
	
	public boolean powerSave(String businessID){
		return isProductInCategory("product.powerSave", businessID);
	}
	
	public boolean stableSave2(String businessID){
		return isProductInCategory("product.stableSave", businessID);
	}
	
	public boolean progressiveLink(String businessID){
		return isProductInCategory("product.progressiveLink", businessID);
	}
	
	public boolean healthProductStandAlone(String businessID){
		return isProductInCategory("product.kesehatan.standalone", businessID);
	}

	public boolean stableLink(String businessID){
		return isProductInCategory("product.stableLink", businessID);
	}
	
	public boolean bridge(String businessID){
		return isProductInCategory("product.bridge", businessID);
	}
	
	public boolean SuperSejahtera(String businessID){
		return isProductInCategory("product.superSejahtera", businessID);
	}

	public boolean DanaSejahtera(String businessID){
		return isProductInCategory("product.danaSejahtera", businessID);
	}
	
	public boolean stableSavePremiBulanan(String businessID){
		return isProductInCategory("product.stableSavePremiBulanan", businessID);
	}
	
	public boolean productBsmFlowStandardIndividu(int lsbs_id, int lsdbs_number){// prosedur standar : seperti individu; input -> U/W-> payment -> print polis -> input tt -> filling
		if((lsbs_id ==120 && ((lsdbs_number>=10 && lsdbs_number<=12)||(lsdbs_number>=19 && lsdbs_number<=21) || (lsdbs_number>=22 && lsdbs_number<=24))) ||
			(lsbs_id==164 && lsdbs_number==11) || (lsbs_id==194 && lsdbs_number==1) || (lsbs_id==163 && (lsdbs_number>=6 && lsdbs_number<=10)) ||
			(lsbs_id==182 && lsdbs_number>=13 && lsdbs_number<=15) || (lsbs_id==111 && lsdbs_number==2) || (lsbs_id==175 && lsdbs_number==2)|| (lsbs_id==216) ||
			(lsbs_id==134 && lsdbs_number==9) || (lsbs_id==220 && lsdbs_number==3)){
			return true;
		}else return false;
	}

	//termasuk didalamnya stable save dan stable save manfaat bulanan (beda dgn stable save premi bulanan)
	public boolean stableSave(int lsbs_id, int lsdbs_number){
		if(isProductInCategory("product.stableSave", FormatString.rpad("0", String.valueOf(lsbs_id), 3))) {
			return true;
		}else{
			if(lsbs_id == 143 && (lsdbs_number == 4 || lsdbs_number == 5 || lsdbs_number == 6)){
				return true;
			}else if(lsbs_id == 144 && lsdbs_number == 4){
				return true;
			}else if(lsbs_id == 158 && (lsdbs_number == 13 || lsdbs_number == 15 || lsdbs_number == 16)){
				return true;
			}else if(lsbs_id == 184){
				return true;
			}else{
				return false;
			}
		}
	}

	public boolean unitLinkNew(String businessID) {
		return isProductInCategory("product.unitLink.new", businessID);
	}
	
	public boolean multiInvest(String businessID) {
		return isProductInCategory("product.multiInvest", businessID);
	}
	
	public boolean syariah(String businessID, String businessNo) {
		//yg satu lsbs_id namun bukan produk syariah
		int lsbs = Integer.parseInt(businessID);
		int lsdbs = Integer.parseInt(businessNo);
		
		if(uwManager.selectLineBusLstBisnis(businessID)==3){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean worksite(String bisinessID){
		return isProductInCategory("product.worksite", bisinessID);
	}
	
	public boolean bsm(String businessID, String businessNo) {
		int lsbs = Integer.parseInt(businessID);
		int lsdbs = Integer.parseInt(businessNo);
		String produkBsm = uwManager.selectprodukBSM(lsbs,lsdbs);
		if(!Common.isEmpty(produkBsm)){
			return true;
		}else return false;
		
//		if( (lsbs==164 && lsdbs==2) || (lsbs==142 && lsdbs==2) || (lsbs==158 && lsdbs==6) || (lsbs==194) ){
//			return true;
//		}else return false;
	}

	/**
	 * Yusuf (10/02/2010) - Req Dr. Ingrid via email, 
	 * stable save, progressive save, powersave, stable link, progressive link yg agency dan worksite, kecuali bancassurance 
	 * semua produk yang masuk kategori diatas, saat transfer dari print polis, bila akseptasi normal, maka tidak akan lewat
	 * TTP lagi, melainkan langsung transfer ke komisi finance / filling 
	 */
	public boolean isLangsungTransferKeKomisi(String reg_spaj){
		String lca_id = elionsManager.selectCabangFromSpaj(reg_spaj);
		ArrayList detBisnis = Common.serializableList(elionsManager.selectDetailBisnis(reg_spaj));
		int lsbs_id = Integer.valueOf((String) ((Map) detBisnis.get(0)).get("BISNIS"));
		int lsdbs_number = Integer.valueOf((String) ((Map) detBisnis.get(0)).get("DETBISNIS"));
		if(lca_id.equals("09")) return false; //bila bancass, tidak rubah flow apa2, return false
		
		//stable save, progressive save, powersave, stable link, progressive link yg agency dan worksite, kecuali bancassurance
		//(7 sept 2010) req ASRI : Untuk PAS(187) ditambahkan proses langsung transfer ke komisi finance/ filing
		if(powerSave(String.valueOf(lsbs_id)) || 
				stableLink(String.valueOf(lsbs_id)) || 
				stableSavePremiBulanan(String.valueOf(lsbs_id)) || 
				progressiveLink(String.valueOf(lsbs_id)) || 
				stableSave(lsbs_id, lsdbs_number) ||
				lsbs_id==187 ||
				productKhususSuryamasAgency(lsbs_id, lsdbs_number)
				){
			return true;
		}
		return false;
	}
	
	public boolean muamalat(String reg_spaj) {
		ArrayList detBisnis = Common.serializableList(elionsManager.selectDetailBisnis(reg_spaj));
		int lsbs_id = Integer.valueOf((String) (Common.serializableMap((Map) detBisnis.get(0)).get("BISNIS")));
		int lsdbs_number = Integer.valueOf((String) (Common.serializableMap((Map) detBisnis.get(0)).get("DETBISNIS")));
		if((lsbs_id == 153 && lsdbs_number == 5) || lsbs_id == 170 || (lsbs_id == 171 && lsdbs_number == 1)) {
			return true;
		}else return false;
	}
	
	public boolean productChannel88(int lsbs_id, int lsdbs_number) {
		if(lsbs_id == 196){
			return true;
		}else if (lsbs_id == 73 && lsdbs_number ==8){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean muamalat(int lsbs_id, int lsdbs_number) {
		if((lsbs_id == 153 && lsdbs_number == 5) || lsbs_id == 170 || (lsbs_id == 171 && lsdbs_number == 1)) {
			return true;
		}else return false;
	}
	
	public boolean bethany(int lsbs_id, int lsdbs_number) {
		if(lsbs_id == 96 && (lsdbs_number>=13 && lsdbs_number<=15)) {
			return true;
		}else return false;
	}

	public boolean nilaiTunaiNotFixed(String businessID) {
		return isProductInCategory("nilaitunai.fixed", businessID);
	}
	
	public boolean productMallLikeBSM(int lsbs_id, Integer lsdbs_number){
		if((lsbs_id == 143 && lsdbs_number==7) || (lsbs_id == 142 && lsdbs_number==10) ) {
			return true;
//		}else if(lsbs_id == 164 && "9,10".indexOf(lsdbs_number.toString())>-1 ) {
		}else if((lsbs_id == 164 && lsdbs_number == 9) || (lsbs_id == 164 && lsdbs_number == 10)) {
			return true;
		}else if(lsbs_id == 188 && lsdbs_number==9) {
			return true;
		}
		else return false;
	}
	
	public String kategoriPrintPolis(String spaj) {
		HashMap detBisnis = Common.serializableMap((Map) elionsManager.selectDetailBisnis(spaj).get(0));
		String lsbs_id = (String) detBisnis.get("BISNIS");
		String lsdbs_number = (String) detBisnis.get("DETBISNIS");
		Integer mspo_provider = uwManager.selectGetMspoProvider(spaj);
		
		String kategori = "biasa";
		if(lsbs_id.equals("196") && lsdbs_number.equals("002")) kategori="term";//term khusus sinarmas sekuritas
		
		if(lsbs_id.equals("208") && 
				(lsdbs_number.equals("005")||lsdbs_number.equals("006")||lsdbs_number.equals("007")||lsdbs_number.equals("008")
				||lsdbs_number.equals("009")||lsdbs_number.equals("010")||lsdbs_number.equals("011")||lsdbs_number.equals("012")
				||lsdbs_number.equals("013")||lsdbs_number.equals("014")||lsdbs_number.equals("015")||lsdbs_number.equals("016")
				||lsdbs_number.equals("017")||lsdbs_number.equals("018")||lsdbs_number.equals("019")||lsdbs_number.equals("020")
				||lsdbs_number.equals("021")||lsdbs_number.equals("022")||lsdbs_number.equals("023")||lsdbs_number.equals("024")
				||lsdbs_number.equals("025")||lsdbs_number.equals("026")||lsdbs_number.equals("027")||lsdbs_number.equals("028")
				||lsdbs_number.equals("029")||lsdbs_number.equals("030")||lsdbs_number.equals("031")||lsdbs_number.equals("032")
				||lsdbs_number.equals("037")||lsdbs_number.equals("038")||lsdbs_number.equals("039")||lsdbs_number.equals("040")||lsdbs_number.equals("041")||lsdbs_number.equals("042")||lsdbs_number.equals("043")||lsdbs_number.equals("044")
				||lsdbs_number.equals("045")||lsdbs_number.equals("046")||lsdbs_number.equals("047")||lsdbs_number.equals("048")) ||
		   (lsbs_id.equals("219") && (Integer.parseInt(lsdbs_number) >= 5 && Integer.parseInt(lsdbs_number) <= 8)) //helpdesk [138638] produk baru SPP Syariah (219-5~8)
				)
			kategori="simas_kid";
		
		if(isProductInCategory("printpolis.powersave", lsbs_id)) {
			kategori = "powersave";
			if(Integer.parseInt(lsbs_id)==188){
				kategori = "powersavenew";
			}
			if(stableSave(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs_number))) {
				if(uwManager.selectFlagBulananStableLinkStableSave(spaj)==1){
					kategori = "stablesave";
				}else{
					kategori = "stablesave_non_bulanan";
				}
				
			}
 		    if(syariah(lsbs_id, lsdbs_number)) kategori = "powersave.syariah";
		}else if(isProductInCategory("printpolis.endcare", lsbs_id)) {
			kategori="endcare";
		}else if(isProductInCategory("printpolis.guthrie", lsbs_id)) {
			kategori="guthrie";
		}else if(isProductInCategory("printpolis.powersave.bulanan", lsbs_id)) {
			kategori="powersavebulanan";
			if(syariah(lsbs_id, lsdbs_number)) kategori = "powersavebulanan.syariah";
		}else if(isProductInCategory("printpolis.stablesave.cicilan", lsbs_id)) {
			kategori="stablesave.cicilan";
		}else if(isProductInCategory("printpolis.stablesave.cicilan.syariah", lsbs_id)) {
			kategori="stablesave.cicilan.syariah";
		}else if(isProductInCategory("printpolis.stablesave", lsbs_id)) {
			if(uwManager.selectFlagBulananStableLinkStableSave(spaj)==1){
				kategori = "stablesave";
			}else{
				kategori = "stablesave_non_bulanan";
			}
		}else if(isProductInCategory("printpolis.excellink80", lsbs_id)) {
			kategori="excellink80";
//			kategori="excellink80new";
		}else if(isProductInCategory("printpolis.excellink80.syariah", lsbs_id)) {
			kategori="excellink80syariah";
		}else if(isProductInCategory("printpolis.excellink18", lsbs_id)) {
			kategori="excellink18";
		}else if(isProductInCategory("printpolis.cerdas", lsbs_id)) {
			kategori="cerdas";
		}else if(isProductInCategory("printpolis.platinumlink", lsbs_id)) {
			//if("001".equals(lsdbs_number)) {
				kategori = "platinumlink";
			//}else if("002".equals(lsdbs_number)) {
			//	kategori = "platinumlinksingle";
			//}
		}else if(isProductInCategory("printpolis.amanahlink", lsbs_id)) {
			kategori = "amanahlink";
		}else if(isProductInCategory("printpolis.endowment20", lsbs_id)) {
			kategori="end20";
		}else if(isProductInCategory("printpolis.ekalink", lsbs_id)) {
			kategori="ekalink";
		}else if(isProductInCategory("printpolis.arthalink", lsbs_id)) {
			kategori="arthalink";
		}else if(isProductInCategory("printpolis.ekalink.syariah", lsbs_id)) {
			kategori="ekalink_syariah";
		}else if(isProductInCategory("printpolis.pa", lsbs_id)) {
			kategori="pa";
		}else if(isProductInCategory("printpolis.stablelink", lsbs_id)) {
			kategori="stablelink";
		}else if(isProductInCategory("printpolis.investimax", lsbs_id)) {
			kategori="investimax";
		}else if(isProductInCategory("printpolis.danasejahtera", lsbs_id)) {
			kategori="danasejahtera";
		}else if(isProductInCategory("printpolis.hidupbahagia", lsbs_id)) {
			kategori="hidupbahagia";
		}else if(isProductInCategory("printpolis.ekasarman", lsbs_id)) {
			
			if(Integer.parseInt(lsdbs_number) > 1) kategori="ekasarman_reguler";
			else kategori="ekasarman";
			
		}else if(isProductInCategory("printpolis.ekasiswa", lsbs_id)) {
			kategori="ekasiswa";
		}else if(isProductInCategory("printpolis.hcp", lsbs_id)) {
			kategori="hcp";
		}else if(isProductInCategory("printpolis.biasa.syariah", lsbs_id)) {
			kategori="biasa.syariah";
			//helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
			if(lsbs_id.equalsIgnoreCase("189") && (Integer.parseInt(lsdbs_number.trim()) >= 48 && Integer.parseInt(lsdbs_number.trim()) <= 62)){
				kategori="biasa_sa";
			}
		}else if (isProductInCategory("printpolis.konvensional.provider", lsbs_id) && mspo_provider==2){
			kategori="biasa_provider";
		}else if(isProductInCategory("printpolis.sa", lsbs_id)){
			kategori="biasa_sa";
		}else if(isProductInCategory("printpolis.pas", lsbs_id)){
			kategori="pas";
		}else if(isProductInCategory("printpolis.provestara", lsbs_id)){
			kategori="provestara";
		}
		
		return kategori;
	}
	
	/**
	 * Kategori untuk print SPAJ
	 * @author Yusuf Sutarko
	 * @since Jan 17, 2008 (10:37:55 AM)
	 * @param spaj
	 * @return
	 */
	public String kategoriPrintSpaj(String spaj) {
		ArrayList detBisnisList = Common.serializableList(elionsManager.selectDetailBisnis(spaj));
		String kategori = "";
		if(!detBisnisList.isEmpty()){
			HashMap detBisnis= Common.serializableMap((Map) detBisnisList.get(0));
			String lsbs_id = (String) detBisnis.get("BISNIS");
			String lsdbs_number = (String) detBisnis.get("DETBISNIS");
			
			
			
			//PLATINUM SAVE
			if((lsbs_id.equals("155") && lsdbs_number.equals("001")) || (lsbs_id.equals("158") && lsdbs_number.equals("005"))) {
				kategori = "SPAJ Bancass - Platinum Save.pdf";
			//PLATINUM SAVE - SPECTA SAVE
			}else if((lsbs_id.equals("155") && lsdbs_number.equals("002")) || (lsbs_id.equals("158") && lsdbs_number.equals("008"))) {
				kategori = "SPAJ Bancass - Specta Save.pdf";
			//PLATINUM SAVE - SMART INVEST
			}else if((lsbs_id.equals("155") && lsdbs_number.equals("003")) || (lsbs_id.equals("158") && lsdbs_number.equals("009"))) {
				kategori = "SPAJ Bancass - Platinum Save - Smart Invest.pdf";
			}
			
			// FILE SPAJ UNTUK PRODUK2 YANG LAIN
		}
		
		
		return kategori;
	}
	
	public FilePdf kategoriPosisiPrintSpaj(FilePdf f) {
		
		
		if(f.getKey().equals("SPAJ Bancass - Bank Sinarmas.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Bancass -  Simas Prima.pdf")) {
			f.setX(498);
			f.setY(870);
			f.setFontSize(5);
		}else if(f.getKey().equals("SPAJ Bancass - Cerdas.pdf")) {
			f.setX(418);
			f.setY(872);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Bancass - Link.pdf")) {
			f.setX(1340);
			f.setY(2423);
			f.setFontSize(20);
		}else if(f.getKey().equals("SPAJ Bancass - Maxi.pdf")) {
			f.setX(425);
			f.setY(873);
			f.setFontSize(5);
		}else if(f.getKey().equals("SPAJ Bancass - Platinum Save.pdf")) {
			f.setX(498);
			f.setY(870);
			f.setFontSize(5);
		}else if(f.getKey().equals("SPAJ Bancass - Platinum Save - Smart Invest.pdf")) {
			f.setX(505);
			f.setY(909);
			f.setFontSize(5);
		}else if(f.getKey().equals("SPAJ Bancass - Specta Save.pdf")) {
			f.setX(124);
			f.setY(735);
			f.setFontSize(5);
		}else if(f.getKey().equals("SPAJ EDUVEST.pdf")) {
			// MASIH ADA NO. BLANKO DOBEL
			f.setX(528);
			f.setY(750);
			f.setFontSize(8);
		}else if(f.getKey().equals("SPAJ HCP Family.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ HORISON.pdf")) {
			// MASIH ADA NO. BLANKO DOBEL
			f.setX(529);
			f.setY(733);
			f.setFontSize(8);
		}else if(f.getKey().equals("SPAJ MEDIPLAN.pdf")) {
			// MASIH ADA NO. BLANKO DOBEL
			f.setX(529);
			f.setY(761);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ MEDIVEST.pdf")) {
			// MASIH ADA NO. BLANKO DOBEL
			f.setX(529);
			f.setY(751);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ POWER SAVE.pdf")) {
			f.setX(427);
			f.setY(822);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ SS - SSP.pdf")) {
			f.setX(427);
			f.setY(830);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ SUPER PROTECTION.pdf")) {
			f.setX(427);
			f.setY(834);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ SYARIAH.pdf")) {
			f.setX(467);
			f.setY(695);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ.pdf")) {
			f.setX(467);
			f.setY(713);
			f.setFontSize(10);
		}else if(f.getKey().equals("SPAJ INVESTIMAX.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Bank Mayapada.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Amanah Link Syariah.pdf")) {
			f.setX(505);
			f.setY(909);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Power Save UOB Buana.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ Stable link.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(6);
		}else if(f.getKey().equals("SPAJ MULTIINVEST.pdf")) {
			f.setX(498);
			f.setY(870);
			f.setFontSize(5);
		}
		return f;
	}
	
	public FilePdf kategoriPosisiPrintSpajAsm(FilePdf f) {
		
		
		if(f.getKey().equals("SPAJ Stable link.pdf")) {
			f.setX(490);
			f.setY(907);
			f.setFontSize(10);
		}else if(f.getKey().equals("SPAJ MULTIINVEST.pdf")) {
			f.setX(498);
			f.setY(870);
			f.setFontSize(18);
		}
		return f;
	}
	
	public void kategoriPosisiTandaTanganSpaj(String fileName, String spaj, PdfStamper stamp) throws MalformedURLException, IOException, DocumentException {
		PdfContentByte over;
		int hal = 1;
		TandaTangan pp= new TandaTangan();
		TandaTangan tt= new TandaTangan();
		
		ArrayList cariMCL=Common.serializableList(this.elionsManager.selectListMCL_ID(spaj));
		ArrayList ttd = Common.serializableList(elionsManager.selectMstTandatanganSPAJ(spaj));		
	
		spaj=spaj.trim();
		if(!cariMCL.isEmpty()){
			for(int i=0; i<cariMCL.size();i++){
				HashMap mcl_idMap= Common.serializableMap((Map) cariMCL.get(i));
				String mcl_idPP= (String) mcl_idMap.get("MSPO_POLICY_HOLDER");
				String mcl_idTT= (String) mcl_idMap.get("MSTE_INSURED");				
				for(int j=0;j<ttd.size();j++){
					HashMap ttdMap= Common.serializableMap((Map) ttd.get(j));				
					String mclTTD= (String) ttdMap.get("MCL_ID");
					if(mcl_idTT.equals(mcl_idPP)){
						TandaTangan ttdpp = elionsManager.selectTandatanganMCLID(mcl_idPP);
						if(ttdpp!=null){
							pp.setMstt_image(ttdpp.getMstt_image());
							tt.setMstt_image(ttdpp.getMstt_image());
						}
					}
					else if(mclTTD.equals(mcl_idPP)){
						TandaTangan ttdpp = elionsManager.selectTandatanganMCLID(mcl_idPP);
						if(ttdpp!=null){
							pp.setMstt_image(ttdpp.getMstt_image());
						}
					}else if(mclTTD.equals(mcl_idTT)){
						TandaTangan ttdtt = elionsManager.selectTandatanganMCLID(mcl_idTT);
						if(ttdtt!=null){
							tt.setMstt_image(ttdtt.getMstt_image());
						}
					}
				}
				
			}
		}
		//Yusuf (17/01/08) - Apabila ada tanda tangannya, masukkan ke dalam tanda tangan PEMEGANG POLIS !
		
		int x_pp = -1;//jika posisi tanda tangan ada pada halaman 1
		int x_tt = -1;
		int y_pp = -1;
		int y_tt = -1;
		int s_pp = -1;
		int s_tt = -1;
			
		int x_pp1 = -1;//jika posisi tanda tangan ada pada halaman 21
		int x_tt1 = -1;
		int y_pp1 = -1;
		int y_tt1 = -1;
		int s_pp1 = -1;
		int s_tt1 = -1;
		
		int x_pp2 = -1;//jika posisi tanda tangan ada pada halaman 3
		int x_tt2 = -1;
		int y_pp2 = -1;
		int y_tt2 = -1;
		int s_pp2 = -1;
		int s_tt2= -1;
		
		int x_pp3 = -1;//jika posisi tanda tangan ada pada halaman 6
		int x_tt3 = -1;
		int y_pp3 = -1;
		int y_tt3 = -1;
		int s_pp3 = -1;
		int s_tt3= -1;
		
		if(fileName.equals("SPAJ Bancass - Bank Sinarmas.pdf")) {
			x_pp = 195;
			y_pp = 96;
			s_pp = 8;
			x_tt = 60;
			y_tt = 96;
			s_tt = 7;
			
		}else if(fileName.equals("SPAJ Bancass - Cerdas.pdf")) {
			hal = 3;
			x_pp2 = 410;
			y_pp2 = 270;
			s_pp2 = 15;
			x_tt2 = 210;
			y_tt2 = 270;
			s_tt2 = 15;
		
		}else if(fileName.equals("SPAJ Bancass - Link.pdf")) {
			hal = 2;
			x_pp1 = 540;
			y_pp1 = 159;
			s_pp1 = 30;
			x_tt1 = 150;
			y_tt1 = 159;
			s_tt1 = 30;
		}else if(fileName.equals("SPAJ Bancass - Maxi.pdf")) {
			x_pp = 165;
			y_pp = 50;
			s_pp = 8;
			x_tt = 60;
			y_tt = 50;
			s_tt = 8;
		}else if(fileName.equals("SPAJ Bancass - Platinum Save.pdf")) {
			hal = 2;
			x_pp1 = 170;
			y_pp1 = 115;
			s_pp1 = 10;
			x_tt1 = 40;
			y_tt1 = 115;
			s_tt1 = 10;
		}else if(fileName.equals("SPAJ Bancass -  Simas Prima.pdf")) {
			x_pp = 170;
			y_pp = 48;
			s_pp = 12;
			x_tt = 50;
			y_tt = 48;
			s_tt = 12;
		}else if(fileName.equals("SPAJ Bancass - Platinum Save - Smart Invest.pdf")) {
			// TERTANGGUNG
			hal = 2;
			x_pp1 = 172;
			y_pp1 = 85;
			s_pp1= 17;
			x_tt1 = 40;
			y_tt1 = 85;
			s_tt1 = 13;
		}else if(fileName.equals("SPAJ Bancass - Specta Save.pdf")) {
			hal = 2;
			x_pp1 = 172;
			y_pp1 = 60;
			s_pp1= 15;
			x_tt1 = 40;
			y_tt1 = 60;
			s_tt1 = 13;
		}else if(fileName.equals("SPAJ EDUVEST.pdf")) {
			
			hal = 2;
			x_pp1 = 360;
			y_pp1 = 185;
			s_pp1 = 15;
			x_tt1 = 205;
			y_tt1 = 185;
			s_tt1 = 15;
		}else if(fileName.equals("SPAJ HCP Family.pdf")) {
			
		}else if(fileName.equals("SPAJ MULTIINVEST.pdf")) {
			
		}else if(fileName.equals("SPAJ HORISON.pdf")) {
			hal = 2;
			x_tt1 = 325;
			y_tt1 = 175;
			s_tt1 = 15;
		}else if(fileName.equals("SPAJ MEDIPLAN.pdf")) {
			hal = 2;
			x_tt1 = 300;
			y_tt1 = 408;
			s_tt1 = 15;
			x_pp1 = 465;
			y_pp1 = 408;
			s_pp1 = 15;
		}else if(fileName.equals("SPAJ MEDIVEST.pdf")) {
			hal = 2;
			x_tt1 = 300;
			y_tt1 = 172;
			s_tt1 = 17;
			x_pp1 = 465;
			y_pp1 = 172;
			s_pp1 = 17;
		}else if(fileName.equals("SPAJ POWER SAVE.pdf")) {
		
			hal = 2;
			x_pp1 = 410;
			y_pp1 = 385;
			s_pp1 = 15;
			x_tt1 = 105;
			y_tt1 = 385;
			s_tt1 = 13;
		}else if(fileName.equals("SPAJ SS - SSP.pdf")) {
			hal = 3;
			x_pp2 = 410;
			y_pp2 = 748;
			s_pp2 = 15;
			x_tt2 = 120;
			y_tt2 = 748;
			s_tt2 = 15;
		}else if(fileName.equals("SPAJ SUPER PROTECTION.pdf")) {
			hal = 2;
			x_pp1 = 415;
			y_pp1 = 382;
			s_pp1 = 15;
			x_tt1 = 120;
			y_tt1 = 382;
			s_tt1 = 15;
		}else if(fileName.equals("SPAJ SYARIAH.pdf")) {
			hal = 6;
			x_pp3 = 387;
			y_pp3 = 695;
			s_pp3 = 15;
			x_tt3 = 175;
			y_tt3 = 695;
			s_tt3 = 15;
		}else if(fileName.equals("SPAJ.pdf")) {
			
		}else if(fileName.equals("SPAJ INVESTIMAX.pdf")) {
			hal = 3;
			x_pp2 = 60;
			y_pp2 = 245;
			s_pp2 = 15;
			x_tt2 = 230;
			y_tt2 = 245;
			s_tt2= 15;
		}else if(fileName.equals("SPAJ Bank Mayapada.pdf")) {
			x_pp = 180;
			y_pp = 32;
			s_pp = 12;
			x_tt = 47;
			y_tt = 32;
			s_tt = 12;
		}else if(fileName.equals("SPAJ Amanah Link Syariah.pdf")) {
			hal = 3;
			x_pp2 = 75;
			y_pp2 = 65;
			s_pp2 = 15;
			x_tt2 = 215;
			y_tt2 =65;
			s_tt2= 15;
		}else if(fileName.equals("SPAJ Power Save UOB Buana.pdf")) {
			x_pp = 180;
			y_pp = 32;
			s_pp = 12;
			x_tt = 47;
			y_tt = 32;
			s_tt = 12;
		}else if(fileName.equals("SPAJ MULTIINVEST.pdf")) {
			x_pp = 180;
			y_pp = 32;
			s_pp = 12;
			x_tt = 47;
			y_tt = 32;
			s_tt = 12;
		}
		over = stamp.getOverContent(1);
		
        //Tambahkan tanda tangan Pemegang
        if(pp != null && x_pp != -1 && y_pp != -1 && s_pp != -1) {
        	if(pp.getMstt_image()!=null){
	            Image pemegang = Image.getInstance(pp.getMstt_image());
	            pemegang.setAbsolutePosition(x_pp, y_pp);
	            pemegang.scalePercent(s_pp);
	            over.addImage(pemegang);
        	}
        }
        
        //Tambahkan tanda tangan Tertanggung
        if(tt != null  && x_tt != -1 && y_tt != -1 && s_tt != -1) {
        	if(tt.getMstt_image()!=null){
	            Image tertanggung = Image.getInstance(tt.getMstt_image());
	            tertanggung.setAbsolutePosition(x_tt, y_tt);
	            tertanggung.scalePercent(s_tt);
	            over.addImage(tertanggung);
        	}
        }
        
        if(hal == 2) {
			over = stamp.getOverContent(2);		
	        
			//Tambahkan tanda tangan Pemegang hal 2
	        if(pp != null && x_pp1 != -1 && y_pp1 != -1 && s_pp1 != -1) {
	        	if(pp.getMstt_image()!=null){
		            Image pemegang = Image.getInstance(pp.getMstt_image());
		            pemegang.setAbsolutePosition(x_pp1, y_pp1);
		            pemegang.scalePercent(s_pp1);
		            over.addImage(pemegang);
	        	}
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 2
	        if(tt != null  && x_tt1 != -1 && y_tt1 != -1 && s_tt1 != -1) {
	        	if(tt.getMstt_image()!=null){
		            Image tertanggung = Image.getInstance(tt.getMstt_image());
		            tertanggung.setAbsolutePosition(x_tt1, y_tt1);
		            tertanggung.scalePercent(s_tt1);
		            over.addImage(tertanggung);
	        	}
	        }
        }
        if(hal == 3) {
			over = stamp.getOverContent(3);		
	        
			//Tambahkan tanda tangan Pemegang hal 2
	        if(pp != null && x_pp2 != -1 && y_pp2 != -1 && s_pp2 != -1) {
	        	if(pp.getMstt_image()!=null){
		            Image pemegang = Image.getInstance(pp.getMstt_image());
		            pemegang.setAbsolutePosition(x_pp2, y_pp2);
		            pemegang.scalePercent(s_pp2);
		            over.addImage(pemegang);
	        	}
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 2
	        if(tt != null  && x_tt2 != -1 && y_tt2 != -1 && s_tt2 != -1) {
	        	if(tt.getMstt_image()!=null){
		            Image tertanggung = Image.getInstance(tt.getMstt_image());
		            tertanggung.setAbsolutePosition(x_tt2, y_tt2);
		            tertanggung.scalePercent(s_tt2);
		            over.addImage(tertanggung);
	        	}
	        }
        }
        if(hal == 6) {
			over = stamp.getOverContent(6);		
	        
			//Tambahkan tanda tangan Pemegang hal 6
	        if(pp != null && x_pp3 != -1 && y_pp3 != -1 && s_pp3 != -1) {
	        	if(pp.getMstt_image()!=null){
		            Image pemegang = Image.getInstance(pp.getMstt_image());
		            pemegang.setAbsolutePosition(x_pp3, y_pp3);
		            pemegang.scalePercent(s_pp3);
		            over.addImage(pemegang);
	        	}
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 6
	        if(tt != null  && x_tt3 != -1 && y_tt3 != -1 && s_tt3!= -1) {
	        	if(tt.getMstt_image()!=null){
		            Image tertanggung = Image.getInstance(tt.getMstt_image());
		            tertanggung.setAbsolutePosition(x_tt3, y_tt3);
		            tertanggung.scalePercent(s_tt3);
		            over.addImage(tertanggung);
	        	}
	        }
        }
        
	}
	public void kategoriPosisiASM(String fileName, String spaj, PdfStamper stamp) throws MalformedURLException, IOException, DocumentException {
		PdfContentByte over;
		int hal = 1;
		TandaTangan pp= new TandaTangan();
		TandaTangan tt= new TandaTangan();
		
		ArrayList cariMCL=Common.serializableList(this.elionsManager.selectListMCL_ID(spaj));
		ArrayList ttd = Common.serializableList(elionsManager.selectMstTandatanganSPAJ(spaj));		
	
//		spaj=spaj.trim();
//		if(!cariMCL.isEmpty()){
//			for(int i=0; i<cariMCL.size();i++){
//				Map mcl_idMap= (Map) cariMCL.get(i);
//				String mcl_idPP= (String) mcl_idMap.get("MSPO_POLICY_HOLDER");
//				String mcl_idTT= (String) mcl_idMap.get("MSTE_INSURED");				
//				for(int j=0;j<ttd.size();j++){
//					Map ttdMap= (Map) ttd.get(j);				
//					String mclTTD= (String) ttdMap.get("MCL_ID");
//					if(mcl_idTT.equals(mcl_idPP)){
//						TandaTangan ttdpp = elionsManager.selectTandatanganMCLID(mcl_idPP);
//						if(ttdpp!=null){
//							pp.setMstt_image(ttdpp.getMstt_image());
//							tt.setMstt_image(ttdpp.getMstt_image());
//						}
//					}
//					else if(mclTTD.equals(mcl_idPP)){
//						TandaTangan ttdpp = elionsManager.selectTandatanganMCLID(mcl_idPP);
//						if(ttdpp!=null){
//							pp.setMstt_image(ttdpp.getMstt_image());
//						}
//					}else if(mclTTD.equals(mcl_idTT)){
//						TandaTangan ttdtt = elionsManager.selectTandatanganMCLID(mcl_idTT);
//						if(ttdtt!=null){
//							tt.setMstt_image(ttdtt.getMstt_image());
//						}
//					}
//				}
//				
//			}
//		}
		//Yusuf (17/01/08) - Apabila ada tanda tangannya, masukkan ke dalam tanda tangan PEMEGANG POLIS !
		
		int x_pp = -1;//jika posisi tanda tangan ada pada halaman 1
		int x_tt = -1;
		int y_pp = -1;
		int y_tt = -1;
		int s_pp = -1;
		int s_tt = -1;
			
		int x_pp1 = -1;//jika posisi tanda tangan ada pada halaman 21
		int x_tt1 = -1;
		int y_pp1 = -1;
		int y_tt1 = -1;
		int s_pp1 = -1;
		int s_tt1 = -1;
		
		int x_pp2 = -1;//jika posisi tanda tangan ada pada halaman 3
		int x_tt2 = -1;
		int y_pp2 = -1;
		int y_tt2 = -1;
		int s_pp2 = -1;
		int s_tt2= -1;
		
		int x_pp3 = -1;//jika posisi tanda tangan ada pada halaman 6
		int x_tt3 = -1;
		int y_pp3 = -1;
		int y_tt3 = -1;
		int s_pp3 = -1;
		int s_tt3= -1;
		
		if(fileName.equals("SPAJ Stable link.pdf")) {
			x_pp = 195;
			y_pp = 96;
			s_pp = 8;
			x_tt = 60;
			y_tt = 96;
			s_tt = 7;
			
		}else if(fileName.equals("SPAJ MULTIINVEST")) {
			hal = 3;
			x_pp2 = 410;
			y_pp2 = 270;
			s_pp2 = 15;
			x_tt2 = 210;
			y_tt2 = 270;
			s_tt2 = 15;
		
		}
		over = stamp.getOverContent(1);
		
        //Tambahkan tanda tangan Pemegang
        if(pp != null && x_pp != -1 && y_pp != -1 && s_pp != -1) {
            Image pemegang = Image.getInstance(pp.getMstt_image());
            pemegang.setAbsolutePosition(x_pp, y_pp);
            pemegang.scalePercent(s_pp);
            over.addImage(pemegang);
        }
        
        //Tambahkan tanda tangan Tertanggung
        if(tt != null  && x_tt != -1 && y_tt != -1 && s_tt != -1) {
            Image tertanggung = Image.getInstance(tt.getMstt_image());
            tertanggung.setAbsolutePosition(x_tt, y_tt);
            tertanggung.scalePercent(s_tt);
            over.addImage(tertanggung);
        }
        
        if(hal == 2) {
			over = stamp.getOverContent(2);		
	        
			//Tambahkan tanda tangan Pemegang hal 2
	        if(pp != null && x_pp1 != -1 && y_pp1 != -1 && s_pp1 != -1) {
	            Image pemegang = Image.getInstance(pp.getMstt_image());
	            pemegang.setAbsolutePosition(x_pp1, y_pp1);
	            pemegang.scalePercent(s_pp1);
	            over.addImage(pemegang);
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 2
	        if(tt != null  && x_tt1 != -1 && y_tt1 != -1 && s_tt1 != -1) {
	            Image tertanggung = Image.getInstance(tt.getMstt_image());
	            tertanggung.setAbsolutePosition(x_tt1, y_tt1);
	            tertanggung.scalePercent(s_tt1);
	            over.addImage(tertanggung);
	        }
        }
        if(hal == 3) {
			over = stamp.getOverContent(3);		
	        
			//Tambahkan tanda tangan Pemegang hal 2
	        if(pp != null && x_pp2 != -1 && y_pp2 != -1 && s_pp2 != -1) {
	            Image pemegang = Image.getInstance(pp.getMstt_image());
	            pemegang.setAbsolutePosition(x_pp2, y_pp2);
	            pemegang.scalePercent(s_pp2);
	            over.addImage(pemegang);
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 2
	        if(tt != null  && x_tt2 != -1 && y_tt2 != -1 && s_tt2 != -1) {
	            Image tertanggung = Image.getInstance(tt.getMstt_image());
	            tertanggung.setAbsolutePosition(x_tt2, y_tt2);
	            tertanggung.scalePercent(s_tt2);
	            over.addImage(tertanggung);
	        }
        }
        if(hal == 6) {
			over = stamp.getOverContent(6);		
	        
			//Tambahkan tanda tangan Pemegang hal 6
	        if(pp != null && x_pp3 != -1 && y_pp3 != -1 && s_pp3 != -1) {
	            Image pemegang = Image.getInstance(pp.getMstt_image());
	            pemegang.setAbsolutePosition(x_pp3, y_pp3);
	            pemegang.scalePercent(s_pp3);
	            over.addImage(pemegang);
	        }
	        
	        //Tambahkan tanda tangan Tertanggung hal 6
	        if(tt != null  && x_tt3 != -1 && y_tt3 != -1 && s_tt3!= -1) {
	            Image tertanggung = Image.getInstance(tt.getMstt_image());
	            tertanggung.setAbsolutePosition(x_tt3, y_tt3);
	            tertanggung.scalePercent(s_tt3);
	            over.addImage(tertanggung);
	        }
        }
        
	}
	
	public boolean productKhususSuryamasAgency(int lsbs_id, int lsdbs_number){
		if(lsbs_id == 196 && lsdbs_number == 1){
			return true;//TERM
		}else if(lsbs_id == 73 && lsdbs_number==8){
			return true;//PA RISK A
		}else{
			return false;
		}
	}
	
	public boolean medicalPlusSilver(String businessID) {
		return isProductInCategory("product.medicalplussilver", businessID);
	}
	
	public boolean medicalPlusGold(String businessID) {
		return isProductInCategory("product.medicalplusgold", businessID);
	}
	
	public boolean medicalPlusPlatinum(String businessID) {
		return isProductInCategory("product.medicalplusplatinum", businessID);
	}
	
	public boolean medicalPlusSilverAddon(String businessID) {
		return isProductInCategory("product.medicalplussilver_addon", businessID);
	}
	
	public boolean medicalPlusGoldAddOn(String businessID) {
		return isProductInCategory("product.medicalplusgold_addon", businessID);
	}
	
	public boolean medicalPlusPlatinumAddOn(String businessID) {
		return isProductInCategory("product.medicalplusplatinum_addon", businessID);
	}

}