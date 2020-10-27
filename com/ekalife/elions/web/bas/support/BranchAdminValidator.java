package com.ekalife.elions.web.bas.support;

import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.SpajDet;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.f_validasi;

/**
 * Validator untuk modul2 di sistem kontrol SPAJ
 * (package com.ekalife.elions.web.bas)
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:23:05 AM)
 */
public class BranchAdminValidator implements Validator{

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
			if(cmd.getDaftarFormSpaj()!=null){//validate save permintaan spaj
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
								"Harap isi kolom jumlah pada spaj {0} dengan angka, atau NOL (0).");
					}
					if(s.getMsf_amount_req()!=null) jumlah += s.getMsf_amount_req();					
				}
				errors.setNestedPath("");
				if(!errors.hasErrors()) {
					if(jumlah==0) errors.reject("", "Jumlah dari seluruh permintaan harus lebih dari 0 (NOL).");
				}
			}else{//validate save permintaan brosur
				for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
					errors.setNestedPath("daftarFormBrosur["+i+"]");
					FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
								"Harap isi kolom jumlah pada brosur {0} dengan angka, atau NOL (0).");
					}
					if(s.getMsf_amount_req()!=null) jumlah += s.getMsf_amount_req();					
				}
				errors.setNestedPath("");
				if(!errors.hasErrors()) {
					if(jumlah==0) errors.reject("", "Jumlah dari seluruh permintaan harus lebih dari 0 (NOL).");
				}
			}
		//VALIDASI UNTUK PEMBATALAN PERMOHONAN OLEH CABANG
		}else if(cmd.getSubmitMode().equals("cancel")) {
			if(cmd.getDaftarFormSpaj()!=null){//untuk permintaan spaj
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
			}else{//untuk permintaan brosur
				for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
					errors.setNestedPath("daftarFormBrosur["+i+"]");
					FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
			}
		//VALIDASI UNTUK APPROVE PERMINTAAN SPAJ DARI CABANG OLEH AGENCY / BAS
		}else if(cmd.getSubmitMode().equals("approve")) {
			if(cmd.getDaftarFormSpaj()!=null){//untuk permintaan spaj
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
			}else{//untuk permintaan brosur
				for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
					errors.setNestedPath("daftarFormBrosur["+i+"]");
					FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
			}
		//VALIDASI UNTUK MENOLAK PERMINTAAN SPAJ DARI CABANG
		}else if(cmd.getSubmitMode().equals("reject")) {
			if(cmd.getDaftarFormSpaj()!=null){//untuk permintaan spaj
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
			}else{//untuk permintaan brosur
				for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
					errors.setNestedPath("daftarFormBrosur["+i+"]");
					FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
			}
		//VALIDASI UNTUK MENANDAKAN BAHWA PERMINTAAN SPAJ SUDAH DIKIRIM KE CABANG OLEH GA
		}else if(cmd.getSubmitMode().equals("send")) {
			if(cmd.getDaftarFormSpaj()!=null){//untuk permintaan spaj
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
						if(s.getLsjs_prefix().equals("SC")) {
							ValidationUtils.rejectIfEmptyOrWhitespace(
									errors, "msf_amount", "", param, 
									"Harap isi kolom jumlah yang disetujui pada kartu {0} yang dikirimkan.");						
						}
						else {
							ValidationUtils.rejectIfEmptyOrWhitespace(
									errors, "msf_amount", "", param, 
									"Harap isi kolom jumlah yang disetujui pada spaj {0} yang dikirimkan.");						
						}
						
						if(s.getMsf_amount() != null && s.getMsf_amount_req() != null) {
							if(s.getMsf_amount() > s.getMsf_amount_req()) {
								if(s.getLsjs_prefix().equals("SC")) errors.reject("", param, "Jumlah yang disetujui pada kartu {0} lebih besar daripada yang diminta");
								else errors.reject("", param, "Jumlah yang disetujui pada spaj {0} lebih besar daripada yang diminta");
								
							}
							if(s.getMsf_amount()>0) {
	//							ValidationUtils.rejectIfEmptyOrWhitespace(
	//									errors, "start_no_blanko", "", param, 
	//									"Harap isi range awal nomor blanko pada spaj {0} yang dikirimkan.");
	//							ValidationUtils.rejectIfEmptyOrWhitespace(
	//									errors, "end_no_blanko", "", param, 
	//									"Harap isi range akhir nomor blanko pada spaj {0} yang dikirimkan.");
								if(s.getLsjs_prefix().equals("SC")) {
									ValidationUtils.rejectIfEmptyOrWhitespace(
											errors, "no_blanko_req", "", param, 
											"Harap isi range nomor blanko pada kartu {0} yang dikirimkan.");											
								}
								/**
								 * @author Rahmayanti
								 * Tidak ada validasi untuk lsjs_prefix TB(TAMBAHAN (NEW)) dan U(UMUM (NEW))
								 */	
								else if(s.getLsjs_prefix().equals("TB")||s.getLsjs_prefix().equals("U")
										||s.getLsjs_prefix().equals("UFBK")||s.getLsjs_prefix().equals("UFS")
										||s.getLsjs_prefix().equals("UGS")||s.getLsjs_prefix().equals("UGK")
										||s.getLsjs_prefix().equals("USK")||s.getLsjs_prefix().equals("USS")){
//									none
								}
								else {
									ValidationUtils.rejectIfEmptyOrWhitespace(
											errors, "no_blanko_req", "", param, 
											"Harap isi range nomor blanko pada spaj {0} yang dikirimkan.");								
								}
								if(!errors.hasErrors()) {
									//int range = (Integer.parseInt(s.getEnd_no_blanko()) - Integer.parseInt(s.getStart_no_blanko())) + 1;
								//21-25;23-28
									Long range=(long) 0;
									String noBlankoReq1[]=s.getNo_blanko_req().split(";");
									String noBlankoReq2[]=new String[noBlankoReq1.length];
									//logger.info(noBlankoReq1.length);
									for(int k=0;k<noBlankoReq1.length;k++){
										String cek=f_validasi.f_validasi_nomor_blanko(noBlankoReq1[k]);
										if(!cek.equals(""))
											errors.reject("", param,"Nomor Blanko {0} salah"+cek);
										else{
											if(noBlankoReq1[k].contains("-")) {
												noBlankoReq2=noBlankoReq1[k].split("-");
												if(noBlankoReq2[0].equals("")|| noBlankoReq2[1].equals("")){
													errors.reject("", param,"Nomor Blanko {0} salah"+cek);
												}else{
													Long awal=Long.parseLong(noBlankoReq2[0]);
													Long akhir=Long.parseLong(noBlankoReq2[1]);
													range=range+(akhir-awal)+1;
												}											
											}
											else range++;
										}	
									}
									
									if(s.getMsf_amount() != range.intValue()) {
										if(s.getLsjs_prefix().equals("SC")) errors.reject("", param, "Jumlah kartu {0} yang diminta tidak sesuai dengan range nomor blanko");
										/**
										 * @author Rahmayanti
										 * Tidak ada validasi untuk lsjs_prefix TB(TAMBAHAN (NEW)) dan U(UMUM (NEW))
										 */	
										else if(s.getLsjs_prefix().equals("TB")||s.getLsjs_prefix().equals("U")
												||s.getLsjs_prefix().equals("UFBK")||s.getLsjs_prefix().equals("UFS")
												||s.getLsjs_prefix().equals("UGS")||s.getLsjs_prefix().equals("UGK")
												||s.getLsjs_prefix().equals("USK")||s.getLsjs_prefix().equals("USS"));
										else errors.reject("", param, "Jumlah SPAJ {0} yang diminta tidak sesuai dengan range nomor blanko");
									}
								}
							}
						}
					}
				}
				errors.setNestedPath("");
				if(!errors.hasErrors()) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formHist.msfh_desc", "", "Harap isi kolom keterangan.");
				}
		}else{//untuk permintaan brosur
			for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
				errors.setNestedPath("daftarFormBrosur["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
					if(s.getLsjs_prefix().equals("SC")) {
						ValidationUtils.rejectIfEmptyOrWhitespace(
								errors, "msf_amount", "", param, 
								"Harap isi kolom jumlah yang disetujui pada kartu {0} yang dikirimkan.");						
					}
					else {
						ValidationUtils.rejectIfEmptyOrWhitespace(
								errors, "msf_amount", "", param, 
								"Harap isi kolom jumlah yang disetujui pada brosur {0} yang dikirimkan.");						
					}
					
					if(s.getMsf_amount() != null && s.getMsf_amount_req() != null) {
						if(s.getMsf_amount() > s.getMsf_amount_req()) {
							if(s.getLsjs_prefix().equals("SC")) errors.reject("", param, "Jumlah yang disetujui pada kartu {0} lebih besar daripada yang diminta");
							else errors.reject("", param, "Jumlah yang disetujui pada brosur {0} lebih besar daripada yang diminta");
							
						}
						
						//brosur tidak ada no_blanko
						/*if(s.getMsf_amount()>0) {
//							ValidationUtils.rejectIfEmptyOrWhitespace(
//									errors, "start_no_blanko", "", param, 
//									"Harap isi range awal nomor blanko pada spaj {0} yang dikirimkan.");
//							ValidationUtils.rejectIfEmptyOrWhitespace(
//									errors, "end_no_blanko", "", param, 
//									"Harap isi range akhir nomor blanko pada spaj {0} yang dikirimkan.");
							if(s.getLsjs_prefix().equals("SC")) {
								ValidationUtils.rejectIfEmptyOrWhitespace(
										errors, "no_blanko_req", "", param, 
										"Harap isi range nomor blanko pada kartu {0} yang dikirimkan.");											
							}
							else {
								ValidationUtils.rejectIfEmptyOrWhitespace(
										errors, "no_blanko_req", "", param, 
										"Harap isi range nomor blanko pada spaj {0} yang dikirimkan.");								
							}
							if(!errors.hasErrors()) {
								//int range = (Integer.parseInt(s.getEnd_no_blanko()) - Integer.parseInt(s.getStart_no_blanko())) + 1;
							//21-25;23-28
								int range=0;
								String noBlankoReq1[]=s.getNo_blanko_req().split(";");
								String noBlankoReq2[]=new String[noBlankoReq1.length];
								//logger.info(noBlankoReq1.length);
								for(int k=0;k<noBlankoReq1.length;k++){
									String cek=f_validasi.f_validasi_nomor_blanko(noBlankoReq1[k]);
									if(!cek.equals(""))
										errors.reject("", param,"Nomor Blanko {0} salah"+cek);
									else{
										if(noBlankoReq1[k].contains("-")) {
											noBlankoReq2=noBlankoReq1[k].split("-");
											if(noBlankoReq2[0].equals("")|| noBlankoReq2[1].equals("")){
												errors.reject("", param,"Nomor Blanko {0} salah"+cek);
											}else{
												int awal=Integer.parseInt(noBlankoReq2[0]);
												int akhir=Integer.parseInt(noBlankoReq2[1]);
												range=range+(akhir-awal)+1;
											}											
										}
										else range++;
									}	
								}
								
								if(s.getMsf_amount() != range) {
									if(s.getLsjs_prefix().equals("SC")) errors.reject("", param, "Jumlah kartu {0} yang diminta tidak sesuai dengan range nomor blanko");
									else errors.reject("", param, "Jumlah SPAJ {0} yang diminta tidak sesuai dengan range nomor blanko");
								}
							}
						}*/
					}
				}
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formHist.msfh_desc", "", "Harap isi kolom keterangan.");
			}
		}
		
		//VALIDASI UNTUK MENANDAKAN BAHWA SPAJ HASIL PERMINTAAN SUDAH DITERIMA OLEH CABANG
		}else if(cmd.getSubmitMode().equals("received")) {
			if(cmd.getDaftarFormSpaj()!=null){//untuk permintaan spaj
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
			}else{//untuk permintaan brosur
				for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
					errors.setNestedPath("daftarFormBrosur["+i+"]");
					FormSpaj s = (FormSpaj) cmd.getDaftarFormBrosur().get(i);
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
		//VALIDASI UNTUK MENANDAKAN BAHWA SPAJ SUDAH DIBERIKAN KE AGEN DARI CABANG TERTENTU
		}else if(cmd.getSubmitMode().equals("save_agen")) {
			//
			
			Integer jumTgJwb=elionsManager.selectJumlahSpajPerTgJbwn(cmd.getAgen().getMsab_id(),cmd.getLca_id());
			if(jumTgJwb>0)
				errors.reject("","Silahkan PertanggungJawabkan form sebelumnya!");
			
			if("000000".equals(cmd.getAgen().getMsag_id())) {
				if("".equals(cmd.getAgen().getMsab_nama())) errors.reject("", "Harap isi nama agen");
			}
			
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
				errors.setNestedPath("daftarFormSpaj["+i+"]");
				FormSpaj s = (FormSpaj) cmd.getDaftarFormSpaj().get(i);

				s.setMss_jenis(0);
				s.setLca_id(cmd.getLca_id());
				s.setMsab_id(0);
				s.setLus_id(Integer.valueOf(cmd.getCurrentUser().getLus_id())); 
				int jumlahSpaj = elionsManager.selectCekSpaj(s); 
				Integer sisaSpaj = elionsManager.selectSisaSpaj(s);
				if(sisaSpaj==null)
					sisaSpaj=0;
				if(jumlahSpaj==0 && s.getMsf_amount_req()>0) {
					errors.reject("", new Object[] {s.getLsjs_desc()}, "Saldo SPAJ {0} di cabang tidak ada! Silahkan buat permintaan ke pusat.");
				}
				
				if(!errors.hasErrors()) {
					Object[] param = new Object[] {s.getLsjs_desc()};
					ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "msf_amount_req", "", param, 
							"Harap isi kolom jumlah yang diberikan pada spaj {0}.");
					
					if(s.getMsf_amount_req()>0) {
						/*ValidationUtils.rejectIfEmptyOrWhitespace(
								errors, "start_no_blanko", "", param, 
								"Harap isi range awal nomor blanko pada spaj {0} yang dikirimkan.");
						ValidationUtils.rejectIfEmptyOrWhitespace(
								errors, "end_no_blanko", "", param, 
								"Harap isi range akhir nomor blanko pada spaj {0} yang dikirimkan.");
						if(!errors.hasErrors()) {
							int range = (Integer.parseInt(s.getEnd_no_blanko()) - Integer.parseInt(s.getStart_no_blanko())) + 1;
							if(s.getMsf_amount_req() > sisaSpaj) {
								errors.reject("", param, "Jumlah SPAJ {0} yang diberikan lebih besar dari jumlah yang ada di cabang!");
							} else if(s.getMsf_amount_req() != range) {
								errors.reject("", param, "Jumlah SPAJ {0} yang diberikan tidak sesuai dengan range nomor blanko");
							}
						}*/
					}
					
				}

				/*s.setStart_no_blanko(FormatString.rpad("0", s.getStart_no_blanko(), 6));
				s.setEnd_no_blanko(FormatString.rpad("0", s.getEnd_no_blanko(), 6));
				List<Map> cekBlanko = elionsManager.selectCekBlankoDiAgen(s.getLsjs_id(), s.getStart_no_blanko(), s.getEnd_no_blanko());
				if(!cekBlanko.isEmpty()) {
					Map info = cekBlanko.get(0);
					errors.reject("", new Object[] {info.get("LSJS_DESC"), info.get("MSAG_ID"), info.get("MSAB_NAMA")}, 
							"Range nomor blanko {0} yang anda masukkan sudah berada di agen [{1}]{2}");
				}*/

				if(s.getMsf_amount_req()!=null) jumlah += s.getMsf_amount_req();	
			}
			errors.setNestedPath("");
			if(!errors.hasErrors()) {
				if(jumlah==0) errors.reject("", "Jumlah dari seluruh permintaan harus lebih dari 0 (NOL).");
			}

		//VALIDASI UNTUK MENANDAKAN BAHWA SPAJ SUDAH DIPERTANGGUNGJAWABKAN AGEN
		}else if(cmd.getSubmitMode().equals("save_tgjwb")) {
			for(int i=0; i<cmd.getDaftarPertanggungjawaban().size(); i++) {
				errors.setNestedPath("daftarPertanggungjawaban["+i+"]");
				SpajDet s = cmd.getDaftarPertanggungjawaban().get(i);
				
				if(2 == s.getLsp_id()) { //di agen
					//...
				} else if(3 == s.getLsp_id()) { //jadi spaj
					Map infoBlanko = elionsManager.selectSpajFromBlanko(s.getLsjs_prefix()+s.getNo_blanko());
					if(infoBlanko == null) {
						errors.rejectValue("no_blanko", "", new Object[] {s.getLsjs_prefix()+s.getNo_blanko()}, 
								"Nomor blanko {0} belum diinput ke sistem sebagai SPAJ.");
					}
				} else if(4 == s.getLsp_id() || 5 == s.getLsp_id() || 6 == s.getLsp_id()) { //rusak / hilang / salah
					ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "mssd_desc", "", new Object[] {s.getLsjs_prefix()+s.getNo_blanko()}, 
							"Harap isi keterangan pada nomor blanko {0}.");					
				}
			}
			errors.setNestedPath("");
		}
	}
	
}