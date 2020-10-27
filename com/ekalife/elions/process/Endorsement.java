package com.ekalife.elions.process;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRJdtCompiler;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.StringUtils;

import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;
import com.ibatis.common.resources.Resources;

public class Endorsement extends ParentDao{

	private List wf_set_other(List ads_det, int ai_row, String noEndorse) {
		
		Map tmp = (HashMap) ads_det.get(ai_row);
		int li_jenis = ((Integer) ((tmp).get("LSJE_ID")));
		
		if("01 , 02 , 21 , 22 , 20, 41 , 47 , 48".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
			String ls_data = "";
			ls_data += (String)tmp.get("MSDE_OLD1") + " "
			+ (String)tmp.get("MSDE_OLD2") + " "
			+ (String)tmp.get("MSDE_OLD3") + " "
			+ (String)tmp.get("MSDE_OLD4") + " "
			+ (String)tmp.get("MSDE_OLD5") + " ";
			String temp = (String) tmp.get("MSDE_OLD6"); 
			if((temp!=null?temp:"").length()>3) ls_data += " / " + temp;
			if(li_jenis==20) {
				if(ls_data.startsWith("B") || ls_data.startsWith("T")) {
					ls_data += " (Almarhum)";
				}else {
					ls_data += " (Almarhumah)";
				}
			}
			tmp.put("MSDE_OLD1", ls_data);
			tmp.put("MSDE_OLD2", null);
			tmp.put("MSDE_OLD3", null);
			tmp.put("MSDE_OLD4", null);
			tmp.put("MSDE_OLD5", "  ");

			String ls_data2="";
			ls_data2 += (String)tmp.get("MSDE_NEW1") + " "
			+ (String)tmp.get("MSDE_NEW2")+ " "
			+ (String)tmp.get("MSDE_NEW3")+ " "
			+ (String)tmp.get("MSDE_NEW4")+ " "
			+ (String)tmp.get("MSDE_NEW5")+ " ";
			temp = (String) tmp.get("MSDE_NEW6"); 
			if((temp!=null?temp:"").length()>3 && li_jenis!=20) ls_data2 += " / " + temp;
			
			String ls_mcl_id = this.uwDao.selectTertanggungFromEndorse(noEndorse);
			
			Date ldt_birth = this.uwDao.selectBirthday(ls_mcl_id);
			int li_umur = FormatDate.dateDifferenceInYears(ldt_birth, this.commonDao.selectSysdateTruncated(0)); 
			if(li_jenis==20) {
				if(li_umur<=17) {
					ls_data2 += " (Yang Ditunjuk)";
				}else {
					ls_data2 += " (Tertanggung)";
				}
			}
			tmp.put("MSDE_NEW1", ls_data2);
			
			int li_count;
			li_count = this.uwDao.selectCountEndorse(noEndorse);
			
			if(li_count>0 && li_umur<=17) {
				String ls_client = this.uwDao.selectMclFirst(ls_mcl_id);
				tmp.put("MSDE_NEW2", "    ");
				tmp.put("MSDE_NEW3", "Sehubungan dengan usia Tertanggung a/n. " + ls_client + " yang belum mencapai 17 tahun, maka Pemegang Polis digantikan oleh Yang Ditunjuk.");
				tmp.put("MSDE_NEW4", null);
			}else {
				tmp.put("MSDE_NEW2", null);
				tmp.put("MSDE_NEW3", null);
				tmp.put("MSDE_NEW4", null);
			}
			tmp.put("MSDE_NEW5", null);
		}else if("03, 04, 23, 19, 34, 35".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {

			String ls_data = ""; String ls_data2 = ""; String ls_data3 = "";
			ls_data+=(String)tmp.get("MSDE_OLD1") +" ";
			ls_data2+=(String)tmp.get("MSDE_OLD2")+" "+(String)tmp.get("MSDE_OLD3")+" ";
			ls_data3+=(String)tmp.get("MSDE_OLD4")+" "+(String)tmp.get("MSDE_OLD5")+" ";
			if(ls_data3.length()>3) ls_data3 = "Telp. " + ls_data3;
			String temp = (String) tmp.get("MSDE_OLD6"); 
			if((temp!=null?temp:"").length()>2) ls_data3 += " / " + temp;
			tmp.put("MSDE_OLD1", ls_data);
			tmp.put("MSDE_OLD2", ls_data2);
			tmp.put("MSDE_OLD3", ls_data3);
			tmp.put("MSDE_OLD4", null);
			tmp.put("MSDE_OLD5", null);
			String ls_kota = (String)tmp.get("MSDE_NEW2");
			Map m = this.uwDao.selectNamaKota(ls_kota);
			String ls_propinsi = (String) m.get("LSPR_NOTE");
			Integer li_prop_id = (Integer) m.get("LSPR_ID");
			if(ls_propinsi==null) ls_propinsi="";
			if(li_prop_id==null) li_prop_id=new Integer(0);
			
			String ls_data4 = (String)tmp.get("MSDE_NEW1");
			String ls_data5="";
			if(li_prop_id!=9 && li_prop_id!=0) {
				ls_data5 = (String)tmp.get("MSDE_NEW2") + ", " + ls_propinsi + " " + (String)tmp.get("MSDE_NEW3"); 
			}else {
				ls_data5 = (String)tmp.get("MSDE_NEW2") + " " + (String)tmp.get("MSDE_NEW3");
			}
			String ls_data6 = (String)tmp.get("MSDE_NEW4") + " " + (String)tmp.get("MSDE_NEW5");
			if(ls_data6.length()>3) ls_data6 = "Telp. " + ls_data6;
			String tempayan = (String) tmp.get("MSDE_NEW6"); 
			if((tempayan!=null?tempayan:"").length()>2) ls_data6 += "/" + tempayan;
			
			tmp.put("MSDE_NEW1", ls_data4);
			tmp.put("MSDE_NEW2", ls_data5);
			tmp.put("MSDE_NEW3", ls_data6);
			tmp.put("MSDE_NEW4", null);
			tmp.put("MSDE_NEW5", null);

		}else if("05, 06, 24".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
			String ls_data = ""; String ls_data2 = ""; 
			ls_data+=(String)tmp.get("MSDE_OLD1")+" : "+(String)tmp.get("MSDE_OLD2")+" ";
			if(ls_data.length()<5) ls_data = "------------";
			tmp.put("MSDE_OLD1", ls_data);
			tmp.put("MSDE_OLD2", null);
			tmp.put("MSDE_OLD3", null);
			tmp.put("MSDE_OLD4", null);
			tmp.put("MSDE_OLD5", null);
			ls_data2+=(String)tmp.get("MSDE_NEW1")+" : "+(String)tmp.get("MSDE_NEW2")+" ";
			tmp.put("MSDE_NEW1", ls_data2);
			tmp.put("MSDE_NEW2", null);
			tmp.put("MSDE_NEW3", null);
			tmp.put("MSDE_NEW4", null);
			tmp.put("MSDE_NEW5", null);

		}else if("16, 26".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
			String ls_data = ""; String ls_data2 = ""; 
			ls_data+=(String)tmp.get("MSDE_OLD1")+ " s/d "+ (String)tmp.get("MSDE_OLD2")+ " ";
			tmp.put("MSDE_OLD1", ls_data);
			tmp.put("MSDE_OLD2", null);
			tmp.put("MSDE_OLD3", null);
			tmp.put("MSDE_OLD4", null);
			tmp.put("MSDE_OLD5", null);
			ls_data2+=(String)tmp.get("MSDE_NEW1")+ " : "
			+ (String)tmp.get("MSDE_NEW2")+ " ";
			tmp.put("MSDE_NEW1", ls_data2);
			tmp.put("MSDE_NEW2", null);
			tmp.put("MSDE_NEW3", null);
			tmp.put("MSDE_NEW4", null);
			tmp.put("MSDE_NEW5", null);
			
		}else if("10, 11".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
			String ls_data = (String)tmp.get("MSDE_OLD1");
			String ls_data2 = (String)tmp.get("MSDE_OLD2");
			ls_data2 = ls_data2.substring(6) + "/" + ls_data2.substring(3, 5) + "/" + ls_data2.substring(0, 2);
			tmp.put("MSDE_OLD1", ls_data + ", " + new SimpleDateFormat("yyyy/mm/dd").format(ls_data2));

			String ls_data3 = (String)tmp.get("MSDE_NEW1");
			String ls_data4 = (String)tmp.get("MSDE_NEW2");
			ls_data4 = ls_data4.substring(6) + "/" + ls_data4.substring(3, 5) + "/" + ls_data4.substring(0, 2);
			tmp.put("MSDE_NEW1", ls_data3 + ", " + new SimpleDateFormat("yyyy/mm/dd").format(ls_data4));

			tmp.put("MSDE_OLD2", null);
			tmp.put("MSDE_NEW2", null);

		}else if("18, 39, 40".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
			String ls_data = (String)tmp.get("MSDE_OLD1");
			if(ls_data==null) tmp.put("MSDE_OLD1", "------------");
			else if(ls_data.length()<2) tmp.put("MSDE_OLD1", "------------");
			
			tmp.put("MSDE_OLD2", null);
			tmp.put("MSDE_OLD3", null);
			tmp.put("MSDE_OLD4", null);
			tmp.put("MSDE_OLD5", null);

			tmp.put("MSDE_NEW2", null);
			tmp.put("MSDE_NEW3", null);
			tmp.put("MSDE_NEW4", null);
			tmp.put("MSDE_NEW5", null);
			
			//di-comment di PB (w_cetak_edm_new.wf_set_other)			
			//		}else if(li_jenis==30) {
			//		}else if(li_jenis==31) {
			//		}else if(li_jenis==32) {
		}
		ads_det.remove(ai_row);
		ads_det.add(ai_row, tmp);
		return ads_det;
	}
	
	private String wf_cek_sama(int li_jenis, List lds_det) {
		int li_pegang=-1, li_tanggung=-1, li_jenis2 = 0;
		String ls_data="", ls_data2="", ls_head="";
		
		if(li_jenis==1) li_jenis2=2;
		else if(li_jenis==2) li_jenis2 =1;
		else if(li_jenis==39)li_jenis2=40;
		else if(li_jenis==40)li_jenis2=39;
		else if(li_jenis==10)li_jenis2=11;
		else if(li_jenis==11)li_jenis2=10;
		else if(li_jenis==3)li_jenis2=19;
		else if(li_jenis==19)li_jenis2=3;
		else if(li_jenis==34)li_jenis2=35;
		else if(li_jenis==35)li_jenis2=34;
		else if(li_jenis==5)li_jenis2=6;
		else if(li_jenis==6)li_jenis2=5;
		
		for(int i=0; i<lds_det.size(); i++) {
			Map tmp = (HashMap) lds_det.get(i);
			Integer lsje = (Integer) tmp.get("LSJE_ID");
			if(lsje!=null) {
				if(li_pegang>-1 && li_tanggung>-1) {
					break;
				}else {
					if(lsje==li_jenis) {
						li_pegang=i; 
					}
					if(lsje==li_jenis2) {
						li_tanggung=i; 
					}
				}
			}
		}
		
		if(li_pegang>-1 && li_tanggung>-1) {
			Map tmp = (HashMap) lds_det.get(li_pegang);
			ls_data+= (String)tmp.get("MSDE_OLD1")+ " "
				+ (String)tmp.get("MSDE_OLD2")+ " "
				+ (String)tmp.get("MSDE_OLD3")+ " "
				+ (String)tmp.get("MSDE_OLD4")+ " "
				+ (String)tmp.get("MSDE_OLD5")+ " ";
			Map tmp2 = (HashMap) lds_det.get(li_tanggung);
			ls_data2+= (String)tmp2.get("MSDE_OLD1")+ " "
				+ (String)tmp2.get("MSDE_OLD2")+ " "
				+ (String)tmp2.get("MSDE_OLD3")+ " "
				+ (String)tmp2.get("MSDE_OLD4")+ " "
				+ (String)tmp2.get("MSDE_OLD5")+ " ";
			if(ls_data.trim().equals(ls_data2.trim())) {
				if(li_jenis==1 || li_jenis==2) ls_head = "Perubahan Nama Pemegang Polis / Tertanggung dari : ";
				else if(li_jenis==5 || li_jenis==6) ls_head = "Perubahan Identitas Pemegang Polis / Tertanggung dari : ";
				else if(li_jenis==10 || li_jenis==11) ls_head = "Perubahan Tempat & Tanggal Lahir Pemegang Polis / Tertanggung dari : ";
				else if(li_jenis==3 || li_jenis==19) ls_head = "Perubahan Alamat Pemegang Polis / Tertanggung dari : ";
				else if(li_jenis==34 || li_jenis==35) ls_head = "Perubahan Alamat Kantor Pemegang Polis / Tertanggung dari : ";
				else if(li_jenis==39 || li_jenis==40) ls_head = "Perubahan Kewarganegaraan Pemegang Polis / Tertanggung dari : ";
			}
		}
		
		return ls_head;
	}
	
	public Map prosesCetakEndorsementBaru(String noEndorse, String tipe){

		Map result = new HashMap();

		result.put("endorseno", noEndorse);
		result.put("props", props);

		if(Integer.parseInt(tipe)<=1) { //Endorsement Tipe baru (EKA.MST_ENDORS)
			result.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.endorse.new")+".jasper");
		}else { //Endorsement tipe lama (EKA.MST_ENDORS_OLD)
			result.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.endorse.old")+".jasper");
			return result;
		}		

		try {
			JRJdtCompiler subReportCompiler = new JRJdtCompiler();
			
			if(logger.isDebugEnabled())logger.debug("prosesCetakEndorsementBaru");

			List lds_det, lds_temp;
			String ls_head, ls_data, ls_data2, ls_data3, ls_spaj, ls_new_head, ls_new_kurs;
			int li_jenis, li_banyak, li_no = 0, li_re=-1, li_ds=0;
			Date ldt_aksep;
			boolean lb_jadwal = false, lb_sama[] = new boolean[]{false, false, false, false, false, false}, lb_ssh = false, lb_31 = false;

			lds_det = this.uwDao.selectCekDetailEndorse(noEndorse);
			lds_temp = this.uwDao.selectCetakHeaderEndorse(noEndorse);
			//lds_bank?;
			
			/* Setting Header */
			if(lds_temp.isEmpty()) {
				return result;
			}
			
			Map te = (HashMap) lds_temp.get(0);
			ls_spaj = (String) te.get("REG_SPAJ");
			
			if(!lds_det.isEmpty()) {
				Map m = (HashMap) lds_det.get(0);
				if(((Integer) m.get("LSJE_ID"))!=17) {
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.header");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(lds_temp));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.header")+".jasper"))));
				}else {
					lb_ssh = true;
				}
				
			}
			
			for(int i=0; i<lds_det.size(); i++) {
				Map detail = (HashMap) lds_det.get(i);
				li_jenis = ((Integer) detail.get("LSJE_ID"));
				if(logger.isDebugEnabled())logger.debug("NOMOR ENDORSE: "+noEndorse+" ~ LI_JENIS : "+li_jenis);
				if(li_jenis==8 || li_jenis==12 || li_jenis==13) {
					li_banyak = 0;
					ls_data3 = "";
					ls_data = (String) detail.get("MSDE_OLD2");
					ls_data2 = (String) detail.get("MSDE_NEW2");
					if(!ls_data.trim().equals(ls_data2.trim())) {
						li_banyak++;
						ls_data3+="Macam asuransi";
					}
					ls_data = (String) detail.get("MSDE_OLD4");
					ls_data2 = (String) detail.get("MSDE_NEW4");
					if(!ls_data.trim().equals(ls_data2.trim())) {
						li_banyak++;
						ls_data3+=", Uang Pertanggungan";
					}
					ls_data = (String) detail.get("MSDE_OLD1");
					ls_data2 = (String) detail.get("MSDE_NEW1");
					if(!ls_data.trim().equals(ls_data2.trim())) {
						li_banyak++;
						ls_data3+=", Cara Pembayaran";
					}
					if(li_banyak==2)ls_data3.replaceAll(", ", " dan ");
					detail.put("LSJE_JENIS", ls_data3);
				}
				
				ls_head = (String) detail.get("LSJE_JENIS");
				if(li_jenis != 12 && li_jenis != 31){
					if(li_jenis==1 || li_jenis==2 || li_jenis==3 || li_jenis==19) {
						ls_head += " yang tertera di Polis dari : ";
					}else if(li_jenis==47) {
						ls_head = "Perubahan Nama Pemegang Polis yang tertera di Polis dari : ";
					}else if(li_jenis==32) {
						ls_head = "Manfaat Tambahan / Rider yang tertera di Polis";
					}else {
						ls_head += " dari : ";
					}
				}else {
					if(li_jenis==30 || li_jenis==31) {
						ls_head = "Perubahan Jumlah Premi dari : ";
					}else {
						ls_head = "Jenis produk yang ada di dalam Polis adalah : ";
					}
				}
				
				ls_new_head = wf_cek_sama(li_jenis, lds_det);
				
				if(!ls_new_head.trim().equals("")) {
					ls_head = ls_new_head;
					if(li_jenis==1 || li_jenis==2) { if(lb_sama[0]) continue; lb_sama[0]  = true; }
					else if(li_jenis==10 || li_jenis==11) { if(lb_sama[1]) continue; lb_sama[1]  = true; }
					else if(li_jenis==3 || li_jenis==19) { if(lb_sama[2]) continue; lb_sama[2]  = true; }
					else if(li_jenis==34 || li_jenis==35) { if(lb_sama[3]) continue; lb_sama[3]  = true; }
					else if(li_jenis==39 || li_jenis==40) { if(lb_sama[4]) continue; lb_sama[4]  = true; }
					else if(li_jenis==5 || li_jenis==6) { if(lb_sama[5]) continue; lb_sama[5]  = true; }
				}
				
				if(!lb_ssh) {
					li_no++;
					ls_head = li_no + ". " + ls_head;
					List ketHeader = new ArrayList();
					Map map = new HashMap();
					map.put("KETHEADER", ls_head);
					ketHeader.add(map);
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.ketheader");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(ketHeader));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.ketheader")+".jasper"))));
				}
				
				if("08, 12, 13, 30, 31, 37, 42, 43, 28 , 49".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
					List temp = this.uwDao.selectEndorseRubahPlanViewAll(noEndorse, li_jenis);
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.rubahplanviewawal");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.rubahplanviewawal")+".jasper"))));
					
					List temp2 = this.uwDao.selectEndorseRubahPlanViewAll(noEndorse, li_jenis);
					//tambah tgl 24/12/01 Himmia
					if(li_jenis==12) {
						ls_new_kurs = (String) detail.get("MSDE_NEW3");
						Map m = (HashMap) temp2.get(0);
						if(ls_new_kurs.trim().equals(((String) m.get("LKU_SYMBOL")).trim())){
							m.put("LKU_SYMBOL", ls_new_kurs);
						}
					}
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.rubahplanviewakhir");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp2));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.rubahplanviewakhir")+".jasper"))));

					if(li_jenis==12 || li_jenis==13 || li_jenis==28 || li_jenis==31) lb_jadwal=true;
					if(li_jenis==31) lb_31=true;
					if(li_jenis==43) {
						li_re = this.uwDao.selectCountCancel(ls_spaj);
						if(li_re>0) lb_jadwal=true;
					}

				}else if("07, 25, 33 , 46".indexOf(FormatString.rpad("0", String.valueOf(li_jenis), 2))>-1) {
					List temp = this.uwDao.selectCetakWarisLama(noEndorse);
					if(li_jenis==33) {
						for(int j=0; j<temp.size(); j++) {
							Map m = (HashMap) temp.get(j);
							m.put("FLAG_TGL", new Integer(1));
						}
					}
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakwarislama");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakwarislama")+".jasper"))));

					List temp2 = this.uwDao.selectCetakWarisBaru(noEndorse);
					if(li_jenis==33) {
						for(int j=0; j<temp2.size(); j++) {
							Map m = (HashMap) temp2.get(j);
							m.put("FLAG_TGL", new Integer(1));
						}
					}
					for(int j=0; j<temp.size(); j++) {
						Map m = (HashMap) temp2.get(j);
						li_re=0;
						if(m.get("LSRE_ID_NEW")!=null) li_re = ((BigDecimal) m.get("LSRE_ID_NEW")).intValue();
						if(li_re==3) break;
					}
					if(li_re!=3) {
						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakwarisbaru");
						result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp2));
						result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakwarisbaru")+".jasper"))));
					}else { //set bank-clause
						List notEqual3 = new ArrayList(); List equal3 = new ArrayList();
						for(int k=0; k<temp2.size(); k++) {
							Map m = (HashMap) temp2.get(k);
							if(m.get("LSRE_ID_NEW")!=null) {
								if(((BigDecimal) m.get("LSRE_ID_NEW")).intValue()==3) { 
									notEqual3.add(m);
								}else { 
									equal3.add(m);
								}
							}
						}

						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakwarisbaru");
						result.put("ds"+li_ds, JasperReportsUtils.convertReportData(notEqual3));
						result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakwarisbaru")+".jasper"))));
						
						Map m = (HashMap) temp2.get(0);
						List lds_bank = this.uwDao.selectEndorseBankClause(noEndorse, ls_spaj, (String) m.get("MSTE_INSURED_NO"), ((BigDecimal) m.get("MSAW_NUMBER")).intValue());
						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.bankclause");
						result.put("ds"+li_ds, JasperReportsUtils.convertReportData(lds_bank));
						result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.bankclause")+".jasper"))));

						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakwarisbaru");
						result.put("ds"+li_ds, JasperReportsUtils.convertReportData(equal3));
						result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakwarisbaru")+".jasper"))));
					}
				}else if(li_jenis==18) {
					List temp = this.uwDao.selectEndorseBebasPremi(ls_spaj, li_jenis);
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.rubahplanbebaspremi");
					result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp));
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.rubahplanbebaspremi")+".jasper"))));
					/*
			CASE 18
				lds_temp.DataObject = 'd_cetak_rubah_plan_bebas_premi'
				lds_temp.SetTransObject(sqlca)
				ls_data = lds_det.GetItemString(i, 'msde_new1')
				lds_temp.Retrieve(ls_spaj, ls_data, li_jenis)
				lds_temp.SetItem(lds_temp.Rowcount(), 'ket', ls_data)
				li_ds ++
				wf_set_ds(lds_temp, li_ds )					 * 
					 */
				}else {
					List temp = null;
					if(!lb_ssh) {
						temp = this.uwDao.selectCetakHeaderEndorseNew(noEndorse, li_jenis);
						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.edm");
						result.put("sub"+li_ds, subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.edm")+".jasper"))));
					}else {
						temp = this.uwDao.selectCetakHeaderEndorseSsh(noEndorse);
						if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.ssh");
						result.put("sub"+li_ds, subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.ssh")+".jasper"))));
					}
					
					temp = wf_set_other(temp, 0, noEndorse);
	
					result.put("ds"+(li_ds++), JasperReportsUtils.convertReportData(temp));
	
				}

			}
			
			if(lb_jadwal) {
				List temp = this.uwDao.selectEndorseJadwalBayar(noEndorse);
				if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakjadwalbayar");
				li_ds++;
				result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp));
				result.put("sub"+li_ds, subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakjadwalbayar")+".jasper"))));
			}
			
			/* Setting Footer */
			if(!lb_ssh) {
				List temp = this.uwDao.selectEndorseEdmNewFoot(noEndorse);
				result.put("ds"+li_ds, JasperReportsUtils.convertReportData(temp));
				if(!lb_31 && lb_jadwal) {
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakedmnewfootspaj");
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakedmnewfootspaj")+".jasper"))));
				}else {
					if(logger.isDebugEnabled())logger.debug(li_ds+": report.endorse.sub.cetakedmnewfoot");
					result.put("sub"+(li_ds++), subReportCompiler.compileReport(JRXmlLoader.load(Resources.getResourceAsStream(props.getProperty("report.endorse.sub.cetakedmnewfoot")+".jasper"))));
				}
			}
			
			String temp = "";
			for(int a=0; a<li_ds; a++) {
				temp+= (a!=0?",":"") +"ds"+a;
			}
			
			result.put("subReportDatakeys", StringUtils.commaDelimitedListToStringArray(temp));
			
			return result;
		}catch(Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}
	
}
