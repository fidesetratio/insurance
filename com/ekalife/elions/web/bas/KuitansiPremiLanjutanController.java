package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.parent.ParentMultiController;

public class KuitansiPremiLanjutanController extends ParentMultiController{
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		//Submit print
		if(request.getParameter("printKuitansi") != null){
			String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
			int letakSPAJ = 0;
			String spaj = "";
			String bill_no = "";
			String no_polis ="";
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
			String beg_date_billing = "";
			if(kode.contains("Silahkan cari") || kode.equals("")){
				cmd.put("lsError", "Silakan Pilih Polis / SPAJ terlebih dahulu.");
			}else{
				letakSPAJ = kode.indexOf("~");
				spaj = kode.substring(0, letakSPAJ).trim();
				bill_no = kode.substring(letakSPAJ+1, kode.length()).trim();

				//Validate
				//apabila user mau cetak ulang, muncul pesan bahwa sudah pernah dilakukan print, tidak dapat cetak ulang.(Untuk mencegah penyalahgunaan kuitansi di cabang).
				List<Billing> listBilling =uwManager.selectMstBillingFlagKuitansi(spaj,bill_no);
				if(listBilling.size()>=1){
					for(int i=0;i<listBilling.size();i++){
						Integer msbi_flag_kuitansi = listBilling.get(0).getMsbi_flag_kuitansi();
						beg_date_billing = df.format(listBilling.get(0).getMsbi_beg_date()) ;
						if(msbi_flag_kuitansi==2){
							cmd.put("lsError", "Kuitansi sudah pernah dicetak.Tidak dapat dicetak lebih dari 1 kali.");
						}
					}
				}
				
				Datausulan dataUsulan = elionsManager.selectDataUsulanutama(kode);
				no_polis = elionsManager.selectPolicyNumberFromSpaj(spaj);
				if(dataUsulan!=null){
					String lca_id=dataUsulan.getLca_id();
					//retrieve msbi_bill_no dari nama file nya. Cth : KPL_209208097_01092012, msbi_bill_no = 209208097
					String pathFile = props.getProperty("pdf.dir.kuitansi")+"\\"+lca_id+"\\"+spaj;
					List<DropDown> dirFile = FileUtils.listFilesInDirectory(pathFile);
					if(dirFile.size()==0){
						cmd.put("lsError", "File Kuitansi Untuk SPAJ "+spaj+" tidak ada, mohon diinfokan ke pihak Finance mengenai perihal ini.");
					}else{
						for(DropDown d : dirFile) {
							String nama_file_directory = d.getKey();
							if(nama_file_directory.contains("KPL_"+bill_no) || nama_file_directory.contains("KPL_"+no_polis.trim()+"_"+beg_date_billing.trim())){
								uwManager.updateMstBillingBillNo(spaj, bill_no, 2);
								cmd.put("kode_success", kode);
								cmd.put("submitSuccess", "Kuitansi berhasil di Print. Silakan cek ke Printer Anda.");
							}else{
								cmd.put("lsError", "File Kuitansi Untuk SPAJ "+spaj+" tidak ada, silakan dikonfirmasikan ke pihak Finance mengenai perihal ini.");
							}
						}
					}
				}
			}
		}
		
		//List Sorting
		//list SPAJ ambil langsung dari mst_billing, yang msbi_flag_kuitansi = 1
		List<Map> listSpaj = uwManager.selectListSpajPremiLanjutan(currentUser.getLus_id(),currentUser.getLde_id());
		cmd.put("listSpaj", listSpaj);
		
		//path file ada di ebserver\pdfind\billing\lca_id\spaj
		//apabila sudah pernah print, update msbi_flag_kuitansi jadi 2.
		return new ModelAndView("bas/kuitansilanjutan/main", cmd);
	}
	
	public ModelAndView prosesSilentPrintKuitansi(HttpServletRequest request, HttpServletResponse response )throws Exception{
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		int letakSPAJ = 0;
		String spaj = "";
		String bill_no = "";
		String nama_file_directory="";
		letakSPAJ = kode.indexOf("~");
		spaj = kode.substring(0, letakSPAJ).trim();
		bill_no = kode.substring(letakSPAJ+1, kode.length()).trim();
		String lca_id = elionsManager.selectCabangFromSpaj(spaj);
		String pathFile = props.getProperty("pdf.dir.kuitansi")+"\\"+lca_id+"\\"+spaj;
		List<DropDown> dirFile = FileUtils.listFilesInDirectory(pathFile);
		for(DropDown d : dirFile) {
			 nama_file_directory = d.getKey();
		}
		uwManager.prosesKuitansiPremiLanjutan(spaj, pathFile, nama_file_directory, bill_no, response);
		return null;
	}
		
	public ModelAndView downloadKpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file =  ServletRequestUtils.getStringParameter(request, "file",null);
		String spaj =  ServletRequestUtils.getStringParameter(request, "spaj",null);
		String directory = props.getProperty("pdf.dir.upload.kpl.promo")+"\\KPL\\"+spaj+"\\";
		FileUtil.downloadFile(directory, file, response, "inline");
		return null;
	}
	
	public ModelAndView downloadPromo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file =  ServletRequestUtils.getStringParameter(request, "file",null);
		String msf_id =  ServletRequestUtils.getStringParameter(request, "msf_id",null);
		String directory = props.getProperty("pdf.dir.upload.kpl.promo")+"\\Promo\\"+msf_id+"\\";
		FileUtil.downloadFile(directory, file, response, "inline");
		return null;
	}
}