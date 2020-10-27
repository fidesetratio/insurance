package com.ekalife.elions.web.cross_selling;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandFat;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.cross_selling.Fat;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Layar input untuk Pengajuan Financial Advisor Trainee (FAT) / Bancass Coordinator (BAC)
 * Untuk input pengajuan/termination FAT/BAC di BSM
 * Juga bisa dipakai oleh Hisar (bancass support) untuk konfirmasinya 
 * 
 * @author Yusuf
 * @since Apr 27, 2011 (10:42:00 AM)
 *
 *
 *(11/02/2013) Pengajuan untuk jenis FAT ditutup
 * dan untuk FATS diganti menjadi Bancassurance Support Trainee (BST)
 *
 */
public class FatBacMultiController extends ParentMultiController {

	/**
	 * Register custom editor, bisa di override oleh children
		 * Binder ini maksudnya untuk formatter data yang di define di appContext-formatter.xml
		 * Contoh: 
		 * doubleEditor ini menggunakan defaultNumberFormat, maka semua data dengan tipe data DOUBLE akan tampil dengan format 1,234,567.89 
	 * 
	 * @author Yusuf
	 * @since 11 Feb 2011
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor);
		binder.registerCustomEditor(Date.class, null, dateEditor);
		binder.registerCustomEditor(Date.class, "fat.create_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "fat.tgl_update", completeDateEditor);
		
	}
	
	/**
	 * Master Posisi, di hardcode di program tapi hanya di 1 tempat ini
	 * 
	 * @return
	 */
	public static ArrayList<DropDown> getListPosisi(){
		ArrayList<DropDown> list = new ArrayList<DropDown>();
		list.add(new DropDown("0", "ALL", ""));
		list.add(new DropDown("1", "1. Input", 		"#ffffff"));
		list.add(new DropDown("2", "2. Otorisasi", 	"#ffffcc"));
		list.add(new DropDown("3", "3. Konfirmasi", "#ccffcc"));
		list.add(new DropDown("4", "4. Print", 		"#ffccff"));
		list.add(new DropDown("5", "5. Filling", 	"#C0E0FF"));
		list.add(new DropDown("6", "6. Terminated", "#FFE080"));
		list.add(new DropDown("7", "7. Konfirmasi Terminasi", "#D8AEFE"));
		return list;
	}
	
	/**
	 * Master Jabatan, di hardcode di program tapi hanya di 1 tempat ini
	 * 
	 * @return
	 */
	public static ArrayList<DropDown> getListJabatan(){
		ArrayList<DropDown> listJabatan = new ArrayList<DropDown>();
		listJabatan.add(new DropDown("BST", "Bancassurance Support Trainee (BST)"));
		listJabatan.add(new DropDown("BAC", "Bancassurance Coordinator (BAC)"));
		return listJabatan;
	}
	
	/**
	 * Bind request parameter ke command, sekaligus memvalidasi datanya
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 16, 2007 (10:02:32 AM)
	 * @param request Parameter yang ada dalam request ini akan di bind ke command object
	 * @param command Object command yang akan dibind datanya
	 * @param isValidated Flag ini menentukan apakah datanya perlu divalidasi atau tidak
	 * @return 
	 * @throws Exception
	 */
	protected BindingResult bindAndValidate(HttpServletRequest request, Object command, boolean isValidated) throws Exception {
		logger.debug("Binding request parameters onto MultiActionController command and Validating the results");
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		initBinder(request, binder);
		
		if(isValidated) {
			Validator[] validators = getValidators();
			if (validators != null) {
				for (int i = 0; i < validators.length; i++) {
					if (validators[i].supports(command.getClass())) {
						ValidationUtils.invokeValidator(validators[i],command, binder.getBindingResult());
					}
				}
			}
		}
		//binder.closeNoCatch();
		return binder.getBindingResult();
	}
	
	/**
	 * Layar Utama 
	 * @throws Exception 
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		CommandFat cmd = new CommandFat();
		BindException errors;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//tentukan dahulu jenis usernya
		//0 = user bancass support, user kantor pusat, 1 = spv, 2 = cs
		int jenis = 0; 
		if(currentUser.getCab_bank() != null){
			if(currentUser.getCab_bank().equals("")){
				jenis = 0;
			}else if(currentUser.getValid_bank_1() == null){
				jenis = 1;
			}else if(!currentUser.getCab_bank().trim().toUpperCase().equals("SSS")){
				jenis = 2;
			}
		}
		request.setAttribute("jenis", jenis);
		
		//parameter2 searching
		cmd.begdate = ServletRequestUtils.getStringParameter(request, "begdate", defaultDateFormat.format(elionsManager.selectSysdate(-7)));
		cmd.enddate = ServletRequestUtils.getStringParameter(request, "enddate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		cmd.nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		cmd.posisi = ServletRequestUtils.getIntParameter(request, "posisi", 0);
		
		//keterangan terminasi
		String[] ket_term = request.getParameterValues("ket_term");
		
		//dropdown untuk reference data
		cmd.listPosisi = getListPosisi();
		
		//list data FAT pada cabang bersangkutan
		cmd.listFat = uwManager.selectListFat(defaultDateFormat.parse(cmd.begdate), defaultDateFormat.parse(cmd.enddate), cmd.nama, cmd.posisi, currentUser.getCab_bank());

		//binding data
		errors = new BindException(bindAndValidate(request, cmd, false));
		
		//bila user menekan tombol OTORISASI
		if(request.getParameter("otorisasi") != null){
			//binding data
			errors = new BindException(bindAndValidate(request, cmd, true));
	        
			//Validasi
			if(jenis == 2) errors.reject(null, "Anda tidak mempunyai hak untuk melakukan OTORISASI");
			
			int count = 0;
			for(int i=0; i<cmd.listFat.size(); i++){
				Fat f = cmd.listFat.get(i);
				if(f.isChecked()){
					count++;
					if(f.posisi.intValue() != 2){
						errors.rejectValue("listFat["+i+"].posisi", null, null, "OTORISASI hanya bisa dilakukan untuk data pengajuan yang berada di posisi [2. OTORISASI]");
						break;
					}
				}
			}
			if(count == 0) errors.reject(null, "Silahkan pilih minimal satu pengajuan untuk di OTORISASI.");

			//bila tidak ada error, lanjutkan dengan proses OTORISASI
			if(!errors.hasErrors()){
				cmd.setPesan(uwManager.otorisasiFat(cmd.listFat, currentUser));
				return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=main&pesan=" + cmd.pesan));
			}

		//bila user menekan tombol KONFIRMASI
		} else if(request.getParameter("konfirmasi") != null){
			//binding data
			errors = new BindException(bindAndValidate(request, cmd, true));
	        
			//Validasi
			if(jenis == 1 || jenis == 2) errors.reject(null, "Anda tidak mempunyai hak untuk melakukan KONFIRMASI");

			int count = 0;
			for(int i=0; i<cmd.listFat.size(); i++){
				Fat f = cmd.listFat.get(i);
				if(f.isChecked()){
					count++;
					if(f.posisi.intValue() != 3){
						errors.rejectValue("listFat["+i+"].posisi", null, null, "KONFIRMASI hanya bisa dilakukan untuk data pengajuan yang berada di posisi [3. KONFIRMASI]");
						break;
					}
				}
			}
			if(count == 0) errors.reject(null, "Silahkan pilih minimal satu pengajuan untuk di KONFIRMASI.");

			//bila tidak ada error, lanjutkan dengan proses KONFIRMASI
			if(!errors.hasErrors()){
				cmd.setPesan(uwManager.konfirmasiFat(cmd.listFat, currentUser));
				return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=main&pesan=" + cmd.pesan));
			}
			
		//bila user menekan tombol FILLING
		} else if(request.getParameter("filling") != null){
			//binding data
			errors = new BindException(bindAndValidate(request, cmd, true));
	        
			//Validasi
			int count = 0;
			for(int i=0; i<cmd.listFat.size(); i++){
				Fat f = cmd.listFat.get(i);
				if(f.isChecked()){
					count++;
					if(f.posisi.intValue() != 4){
						errors.rejectValue("listFat["+i+"].posisi", null, null, "FILLING hanya bisa dilakukan untuk data pengajuan yang berada di posisi [4. PRINT]");
						break;
					}
					Integer countCetakSurat = uwManager.countFatCetakHist(f.fatid);
					Integer countCetakNameTag = uwManager.countFatNameTagHist(f.fatid);
					if(countCetakSurat<1){
						errors.rejectValue("listFat["+i+"].fatid", null, null, "Harap lakukan Print Surat BST sebelum melanjutkan ke Proses FILLING");
						break;
					}else if(countCetakNameTag<1){
						errors.rejectValue("listFat["+i+"].fatid", null, null, "Harap lakukan Print Name Tag BST sebelum melanjutkan ke Proses FILLING");
						break;
					}
				}
			}
			if(count == 0) errors.reject(null, "Silahkan pilih minimal satu pengajuan untuk di FILLING.");
			
			//bila tidak ada error, lanjutkan dengan proses FILLING
			if(!errors.hasErrors()){
				cmd.setPesan(uwManager.fillingFat(cmd.listFat, currentUser));
				return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=main&pesan=" + cmd.pesan));
			}
						
		//bila user menekan tombol TERMINATE
		} else if(request.getParameter("terminate") != null){
				//binding data
				errors = new BindException(bindAndValidate(request, cmd, true));
		        
				//Validasi
				int count = 0;
				for(int i=0; i<cmd.listFat.size(); i++){
					Fat f = cmd.listFat.get(i);
					if(f.isChecked()){
						count++;
						if(f.posisi.intValue() != 5){
							errors.rejectValue("listFat["+i+"].posisi", null, null, "TERMINASI hanya bisa dilakukan untuk data pengajuan yang berada di posisi [5. FILLING]");
							break;
						}
						if(ket_term[i] == null || ket_term[i] == ""){
							errors.rejectValue("listFat["+i+"].ket_term", null, null, "Kolom KETERANGAN TERMINASI ["+f.getFatid()+"] harus diisi.");
						}
					}
				}
				if(count == 0) errors.reject(null, "Silahkan pilih minimal satu pengajuan untuk di TERMINASI.");		
				
				//bila tidak ada error, lanjutkan dengan proses TERMINASI
				if(!errors.hasErrors()){
					cmd.setPesan(uwManager.terminasiFat(cmd.listFat, currentUser, ket_term));
					return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=main&pesan=" + cmd.pesan));
				}
			
		//bila user menekan tombol KONFIRMASI TERMINASI
		} else if(request.getParameter("konf_term") != null){
				//binding data
				errors = new BindException(bindAndValidate(request, cmd, true));
					        
				//Validasi
				if(jenis == 1 || jenis == 2) errors.reject(null, "Anda tidak mempunyai hak untuk melakukan KONFIRMASI TERMINASI");

				int count = 0;
				for(int i=0; i<cmd.listFat.size(); i++){
					Fat f = cmd.listFat.get(i);
					if(f.isChecked()){
						count++;
						if(f.posisi.intValue() != 6){
							errors.rejectValue("listFat["+i+"].posisi", null, null, "KONFIRMASI TERMINASI hanya bisa dilakukan untuk data pengajuan yang berada di posisi [6. TERMINATED]");
							break;
						}
					}
				}
				if(count == 0) errors.reject(null, "Silahkan pilih minimal satu pengajuan untuk di KONFIRMASI TERMINASI.");

				//bila tidak ada error, lanjutkan dengan proses KONFIRMASI TERMINASI
				if(!errors.hasErrors()){
					cmd.setPesan(uwManager.konftermFat(cmd.listFat, currentUser));
					return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=main&pesan=" + cmd.pesan));
				}
		}
	
		return new ModelAndView("cross_selling/fat_main", errors.getModel());
	}

	/**
	 * Layar Input Pengajuan FAT / BAC
	 * @throws Exception 
	 */
	public ModelAndView input(HttpServletRequest request, HttpServletResponse response) throws Exception{
		CommandFat cmd = new CommandFat();
		BindException errors;
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		//tentukan dahulu jenis usernya
		//0 = user bancass support, user kantor pusat, 1 = spv, 2 = cs
		int jenis = 0; 
		if(currentUser.getCab_bank() != null){
			if(currentUser.getCab_bank().equals("")){
				jenis = 0;
			}else if(currentUser.getValid_bank_1() == null){
				jenis = 1;
			}else if(!currentUser.getCab_bank().trim().toUpperCase().equals("SSS")){
				jenis = 2;
			}
		}
		request.setAttribute("jenis", jenis);
		
		//form backing object
		String fatid = ServletRequestUtils.getStringParameter(request, "fatid", null);
		if(fatid == null){
			Date sysdate = elionsManager.selectSysdate();
			
			cmd.fat = new Fat();
			cmd.fat.is_active = 1;
			cmd.fat.act_date = sysdate;
			cmd.fat.end_date = sysdate;
			
			if(currentUser.getCab_bank() != null){
				if(!currentUser.getCab_bank().toUpperCase().equals("SSS")){
					cmd.fat.lcb_no = currentUser.getCab_bank();
				}
			}
			
		}else{
			cmd.fat = uwManager.selectFat(fatid);
			cmd.listFatHistory = uwManager.selectFatHistory(fatid);
		}
		
		//referenced data
		request.setAttribute("listJabatan", getListJabatan());
		request.setAttribute("listPosisi", getListPosisi());
		
		List<DropDown> listBSM = elionsManager.selectDropDown("EKA.LST_CABANG_BII", "trim(LCB_NO)", "NAMA_CABANG", "", "NAMA_CABANG", "JENIS = 2 AND FLAG_AKTIF=1");
		request.setAttribute("listBSM", listBSM);
		
		//binding data
		errors = new BindException(bindAndValidate(request, cmd, false));
		
		//bila user menekan tombol simpan
		if(request.getParameter("simpan") != null){
			errors = new BindException(bindAndValidate(request, cmd, true));
			
	        //Validasi Kolom yang harus diisi
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.nama", null, "NAMA harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.tgl_lahir", null, "TANGGAL LAHIR harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.gender", null, "JENIS KELAMIN harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.lcb_no", null, "CABANG BSM harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.act_date", null, "MASA KERJA harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.norek", null, "NOMOR REKENING BSM harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.honor", null, "HONOR PER HARI harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.target", null, "TARGET harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.targetnoa", null, "TARGET NOA harus diisi");
			
			//bila set menjadi non aktif (terminate), maka harus masukkan alasan resign
			if(cmd.fat.is_active != null){
				if(cmd.fat.is_active.intValue() == 0){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fat.resign_why", null, "ALASAN RESIGN/TERMINASI harus diisi");
				}
			}
			
			//edit data hanya bisa dilakukan bila posisi 1 dan 2 saja, bila sudah 3 keatas tidak bisa
			if(cmd.fat.posisi != null){
				if(cmd.fat.posisi.intValue() > 2){
					errors.rejectValue("fat.posisi", null, "Perubahan data hanya bisa dilakukan apabila pengajuan berada pada posisi [1. INPUT] atau [2. OTORISASI]");
				}
			}
			
			//Yusuf (23 Jun 2011) - Req Hisar, tidak bisa input bila nama dan tanggal lahir sama.
			if(!errors.hasErrors() && cmd.fat.fatid.equals("")){
				String dobel = uwManager.selectValidasiInputFatDouble(cmd.fat.nama.toUpperCase().trim(), cmd.fat.tgl_lahir);  
				if(dobel != null){
					errors.rejectValue("fat.nama", null, "Data tidak bisa disimpan. NAMA dan TANGGAL LAHIR yang dimasukkan sudah pernah diinput sebelumnya dengan nomor " + dobel);
				}
			}
			
			//bila tidak ada error, lakukan proses penyimpanan data
			if(!errors.hasErrors()){
				cmd.setPesan(uwManager.saveFat(cmd.fat, currentUser));
				return new ModelAndView(new RedirectView(request.getContextPath()+ "/cross_selling/fat.htm?window=input&fatid=" + cmd.fat.fatid + "&pesan=" + cmd.pesan));
			}
			
		}
		
		return new ModelAndView("cross_selling/fat_input", errors.getModel());
	}
	
}