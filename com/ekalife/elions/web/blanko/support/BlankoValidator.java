package com.ekalife.elions.web.blanko.support;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.service.ElionsManager;

/**
 * Validator untuk modul2 di sistem kontrol KERTAS POLIS
 * (package com.ekalife.elions.web.blanko)
 * @author Hemilda Sari Dewi
 * @since Feb 23, 2007 (9:23:05 AM)
 */
public class BlankoValidator implements Validator{

	private ElionsManager elionsManager;
	
	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public boolean supports(Class class1) {
		if(class1.isAssignableFrom(CommandControlSpaj.class)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void validate(Object obj, Errors errors) {
		int jumlah = 0;
		CommandControlSpaj cmd = (CommandControlSpaj) obj;
		//VALIDASI UNTUK SIMPAN PERMINTAAN (INSERT/UPDATE) OLEH CABANG
		if(cmd.getSubmitMode().equals("save")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 1 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : APPROVED / DISETUJUI. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 4 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : SENT / SUDAH DIKIRIM KE CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
				if(!errors.hasErrors()) {
					ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "msf_amount_req", "", new Object[] {s.getLsjs_desc()}, 
							"Harap isi kolom jumlah pada KERTAS POLIS {0} dengan angka, atau NOL (0).");
				}
				if(s.getMsf_amount_req()!=null) jumlah += s.getMsf_amount_req();					
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				if(jumlah==0) errors.reject("", "Jumlah dari seluruh permintaan harus lebih dari 0 (NOL).");
			}
		}else if(cmd.getSubmitMode().equals("simpan")) {
			for(int i=0; i<cmd.getDaftarStokSpaj().size(); i++) {
				errors.setNestedPath("daftarStokSpaj["+i+"]");
				Spaj s = (Spaj) cmd.getDaftarStokSpaj().get(i);
				
				if(!errors.hasErrors()) {
					ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "damage", "", new Object[] {s.getLsjs_desc()}, 
							"Harap isi kolom rusak pada KERTAS POLIS {0} dengan angka, atau NOL (0).");
				}
				if(s.getDamage()!=null) jumlah += s.getDamage();					
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				if(jumlah==0) errors.reject("", "Jumlah dari seluruh permintaan harus lebih dari 0 (NOL).");
			}	
			
		
		//VALIDASI UNTUK PEMBATALAN PERMOHONAN OLEH CABANG
		}else if(cmd.getSubmitMode().equals("cancel")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 4 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : SENT / SUDAH DIKIRIM KE CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
			}
			errors.setNestedPath("");

		//VALIDASI UNTUK APPROVE PERMINTAAN KERTAS POLIS DARI CABANG OLEH AGENCY / BAS
		}else if(cmd.getSubmitMode().equals("approve")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 1 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : APPROVED / DISETUJUI. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 4 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : SENT / SUDAH DIKIRIM KE CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formHist.msfh_desc", "", "Harap isi kolom keterangan.");
			}
			
		//VALIDASI UNTUK MENOLAK PERMINTAAN KERTAS POLIS DARI CABANG
		}else if(cmd.getSubmitMode().equals("reject")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 1 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : APPROVED / DISETUJUI. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 4 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : SENT / SUDAH DIKIRIM KE CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formHist.msfh_desc", "", "Harap isi kolom keterangan.");
			}
			
		//VALIDASI UNTUK MENANDAKAN BAHWA PERMINTAAN KERTAS POLIS SUDAH DIKIRIM KE CABANG OLEH GA
		}else if(cmd.getSubmitMode().equals("send")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 4 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : SENT / SUDAH DIKIRIM KE CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
				if(!errors.hasErrors()) {
					Object[] param = new Object[] {s.getLsjs_desc()};
					ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "msf_amount", "", param, 
							"Harap isi kolom jumlah yang disetujui pada KERTAS POLIS {0} yang dikirimkan.");
					
					if(s.getMsf_amount() != null && s.getMsf_amount_req() != null) {
						if(s.getMsf_amount() > s.getMsf_amount_req()) {
							errors.reject("", param, "Jumlah yang disetujui pada KERTAS POLIS {0} lebih besar daripada yang diminta");
						}
					}
				}
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formHist.msfh_desc", "", "Harap isi kolom keterangan.");
			}
		
		//VALIDASI UNTUK MENANDAKAN BAHWA KERTAS POLIS HASIL PERMINTAAN SUDAH DITERIMA OLEH CABANG
		}else if(cmd.getSubmitMode().equals("received")) {
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);
				if( 0 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REQUESTED / PERMOHONAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 1 == s.getPosisi()) {
						errors.setNestedPath("");
						errors.reject("", "STATUS PERMINTAAN : APPROVED / DISETUJUI. Anda tidak bisa merubah permintaan ini.");
						break;
				} else if( 2 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : CANCELLED / DIBATALKAN. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 3 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : REJECTED / DITOLAK. Anda tidak bisa merubah permintaan ini.");
					break;
				} else if( 5 == s.getPosisi()) {
					errors.setNestedPath("");
					errors.reject("", "STATUS PERMINTAAN : RECEIVED / SUDAH DITERIMA CABANG. Anda tidak bisa merubah permintaan ini.");
					break;
				}
			}
			errors.setNestedPath("");
			
		}
	}
	
}