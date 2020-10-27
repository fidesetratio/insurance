package com.ekalife.elions.web.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.web.uw.PrintPolisPrintingController;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class GadgetOnlineWebService extends ParentController{	
	
	
	
	
	
	
	public ModelAndView handleRequest(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String spaj = request.getParameter("spaj");
	//	map.put("result", spaj);
		map = this.gadget(request, response);
		Gson gson =new GsonBuilder().setPrettyPrinting().create();
		out.print(gson.toJson(map));
		out.close();
		return null;
	}
	
	public HashMap<String,Object> gadget(HttpServletRequest request, HttpServletResponse response)throws Exception {
		HashMap<String,Object> result = new HashMap<String, Object>();
		ArrayList<String> errors = new ArrayList<String>();
		
		try{
		String reg_spaj = request.getParameter("spaj");
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String lcaId = elionsManager.selectCabangFromSpaj(reg_spaj);
		if(lcaId.equals("09") || lcaId.equals("37") || lcaId.equals("52")){
			if (mspo_flag_spaj.equals("3")){
				if(syariah == 1){
					espajonlinegadgetfullsyariah(request, response);
				}else{
					espajonlinegadgetfullkonven(request, response);
				}
			}else if (mspo_flag_spaj.equals("4")){
				if(syariah == 1){
					espajonlinegadgetsiosyariah(request, response);
				}else{
					espajonlinegadgetsiokonven(request, response);
				}
			}
		}
		
		else{
			espajonlinegadgetexisting(request, response);
		}

		}catch(Exception e){
			errors.add(e.getMessage());
			System.out.println(e.getMessage());
		}
		
		if(errors.size() >0)
		{	StringBuffer buffer = new StringBuffer();
			for(String e:errors){
				buffer.append(e);
				buffer.append("|");
			}
			result.put("warning", buffer.toString());
		}
		return result;
	}
	
	public ModelAndView espajonlinegadgetfullkonven(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		
		Map map= uwManager.selectProdInsured(reg_spaj);
		BigDecimal lsbs = (BigDecimal) map.get("LSBS_ID");
		BigDecimal lsdbs = (BigDecimal) map.get("LSDBS_NUMBER");
		String lsbs_id = lsbs.toString();
		String lsdbs_number = lsdbs.toString();
		String spajTempDirectory = props.getProperty("pdf.dir.spajtemp");
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		if (lsbs_id.equals("134") && lsdbs_number.equals("13")) {
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonvenBTN.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonvenBTN.pdf";
		}else if(cabang.equals("37") || cabang.equals("52")){
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonvenAgency.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonvenAgency.pdf";
		}
		else {
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonven.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonven.pdf";
		}
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			HashMap agenAgency= Common.serializableMap(uwManager.selectInfoAgen2(reg_spaj));
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);
			if (cabang.equals("37") || cabang.equals("52")){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agenAgency.get("MCL_FIRST").toString().toUpperCase(), 160, 409,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agenAgency.get("MSAG_ID").toString(), 160, 399, 0);
			}else{
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("NM_AGEN").toString().toUpperCase(), 160, 409,
					0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("KD_AGEN").toString(), 160, 399, 0);
			}
			over.endText();

			// ---------- Data Halaman Ketiga ----------
			over = stamp.getOverContent(3);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_PP_" + ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_TU_" + ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 655;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,
								pesertaPlus.getNama().toUpperCase(), 290, vertikal,
								0);
						vertikal = vertikal + 2;
					}
				}
			}
			if (cabang.equals("37") || cabang.equals("52")){
				try {
					String ttdAgen = exportDirectory + "\\" + idgadget
							+ "_TTD_AGEN_" + ".jpg";
					Image img5 = Image.getInstance(ttdAgen);
					img5.scaleAbsolute(30, 30);
					over.addImage(img5, img5.getScaledWidth(), 0, 0,
							img5.getScaledHeight(), 120, 280);
					over.stroke();
				} catch (FileNotFoundException e) {
					logger.error("ERROR :", e);
//					ServletOutputStream sos = response.getOutputStream();
//					sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//					sos.close();
				}
				
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("MCL_FIRST")) ? "-" : 
							agenAgency.get("MCL_FIRST").toString().toUpperCase(),100, 260,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("MSAG_ID")) ? "-" : 
							agenAgency.get("MSAG_ID").toString().toUpperCase(),100, 250,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("TEAM")) ? "-" : 
							agenAgency.get("TEAM").toString().toUpperCase(),100, 240,0);
			}else{
			try {
				
				String ttdPenutup = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);
			}
			over.endText();

			// //---------- Data Halaman Keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
//			monyong = 0;
//			String[] alamat = StringUtil.pecahParagraf(dataPP
//					.getAlamat_kantor().toUpperCase(), 70);
//			for (int i = 0; i < alamat.length; i++) {
//				monyong = 7 * i;
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
//						529 - monyong, 0);
//			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman Kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 270, 163 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 440,
													163 - j, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 97, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 87, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 77, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 68, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 58, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					47, 0);
			over.endText();

			// ---------- Data Halaman Keenam ----------

			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}
			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						int pesertatambahan = 0;
						String[] nama = StringUtil.pecahParagraf(pesertaPlus.getNama().toUpperCase(), 20);
						for (int z = 0; z < nama.length; z++) {
							pesertatambahan = 7 * z;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									nama[z], 76, 517 - pesertatambahan, 0);
						}
//						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//								pesertaPlus.getNama().toUpperCase(), 76, 517 - j,
//								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
						String[] kerjaan = StringUtil.pecahParagraf(pesertaPlus.getKerja().toUpperCase(), 20);
						for (int z = 0; z < kerjaan.length; z++) {
							pesertatambahan = 7 * z;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									kerjaan[z], 290, 517 - pesertatambahan, 0);
						}
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}	
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 55, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							270, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 331, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 375, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 475, 380 - j, 0);
					j += 10;
				}
			}
			// -----------data tertanggung-----------
			String jawab = "";
			if (mspo_flag_spaj.equals("3")){
				if (rslt.size() > 0) {
					Integer j = 0;
					for (int i = 0; i < rslt.size(); i++) {
						MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
						if (ans.getQuestion_id() == 1) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 308, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										290, 0);
							}
						}
						if (ans.getQuestion_id() == 2) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 279, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										270, 0);
							}
						}
						if (ans.getQuestion_id() == 3) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										230, 0);
							}
						}
						if (ans.getQuestion_id() == 4) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										190, 0);
							}
						}
						if (ans.getQuestion_id() == 5) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 180, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										163, 0);
							}
						}
						if (ans.getQuestion_id() == 6) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										143, 0);
							}
						}	
						if (ans.getQuestion_id() == 7) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										84, 0);
							}
						}
						if (ans.getQuestion_id() == 8) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 73, 0);
							}
						}
						if (ans.getQuestion_id() == 9) {
							if (ans.getOption_type() == 0
									&& ans.getOption_order() == 1) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 115, 62, 0);
							} else if (ans.getOption_type() == 0
									&& ans.getOption_order() == 2) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 160, 52, 0);
							}
						}
					}	
			}
			}
			over.endText();

			// ------------Halaman tujuh-----------
			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			if (rslt.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
					if (ans.getQuestion_id() == 10) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						}
					}
					if (ans.getQuestion_id() == 11) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 715, 0);
						}
					}
					if (ans.getQuestion_id() == 12) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 695, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									685, 0);
						}
					}
					if (ans.getQuestion_id() == 13) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 667, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 667, 0);
						}
					}
					if (ans.getQuestion_id() == 15) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 645, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 645, 0);
						}
					}
				}
			}

			// -------------data pemegang polis--------------
			if (rslt2.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt2.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt2.get(i);
					if (ans.getQuestion_id() == 16) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 615, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									595, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									595, 0);
						}
					}
					if (ans.getQuestion_id() == 17) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 587, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									577, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									577, 0);
						}
					}
					if (ans.getQuestion_id() == 18) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 558, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 558, 0);
						}
					}
				}
			}
			over.endText();
			
			
			// ------------Halaman kedelapan-----------			
			
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt3.size() > 0 ) {
				Integer j = 0;
				for (int i = 0; i < rslt3.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt3.get(i);
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 700 - j, 0);
							// --pp
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						}else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);	
								// --tt1/pt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 
										&& ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								}
								// --tt2/pt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 
										&& ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								}
								// --tt3/pt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 
										&& ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								}
								// --tt4/pt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 
										&& ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								}
								
							}
						}
					
					j += 1;
				}
			}

			if (rslt4.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt4.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt4.get(i);
					if (ans.getQuestion_id() == 137) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
						}
						// --tu/pu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 356, 0);
						}
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 356, 0);
						}
						 
						 else if (ans.getOption_group() == 0) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									320, 0);
						}
						
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								}
								
							}
						}

					}
							
					if (ans.getQuestion_id() == 139) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 &&ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								}
							}
						}
						
					}
					// j += 3;
					if (ans.getQuestion_id() >= 140
							&& ans.getQuestion_id() <= 143) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								}
							}
							}
						}
						
					j += 2;
					if (ans.getQuestion_id() == 144) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								}
							}
							}
						}
						
							
					if (ans.getQuestion_id() == 145) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 178, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 168, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								}
							}
						}
					}
				}
			}

			over.endText();

			// --------------Halaman sembilan--------------//
			over = stamp.getOverContent(9);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt5.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt5.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt5.get(i);
					if (ans.getQuestion_id() == 146) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						}
						// --tu
						else if ( ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								}
							}
						}
					}
					// j += 3;
					if (ans.getQuestion_id() == 147) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 697,
								0);
					} else if (ans.getQuestion_id() == 148) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 667,
								0);
					} else if (ans.getQuestion_id() == 149) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 637,
								0);
					} else if (ans.getQuestion_id() == 150) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 607,
								0);
					} else if (ans.getQuestion_id() == 151) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 578,
								0);
					}
					if (ans.getQuestion_id() == 152) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);						
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								}
							}
						}
							
					}
					if (ans.getQuestion_id() == 153) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								}
							}
						}
						
					}
					if (ans.getQuestion_id() == 154) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								}
							}
						}
						
					}
					if (ans.getQuestion_id() == 155) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								}
							}
						}	
					}
				}
				j += 2;
			}
			over.endText();
			stamp.close();
			
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	}
	
	public ModelAndView espajonlinegadgetsiokonven(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String spajTempDirectory = props.getProperty("pdf.dir.spajtemp");
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetsiokonven.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetsiokonven.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("NM_AGEN").toString().toUpperCase(), 160, 409,
					0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("KD_AGEN").toString(), 160, 399, 0);

			over.endText();

			// ---------- Data Halaman Ketiga ----------
			over = stamp.getOverContent(3);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
					+ "_TTD_PP_"+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_TU_" + ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 655;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,
							pesertaPlus.getNama().toUpperCase(), 290, vertikal,
							0);
					vertikal = vertikal + 2;
				}
			}

			try {
				String ttdPenutup = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);
			over.endText();

			// //---------- Data Halaman Keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
			
//			monyong = 0;
//			String[] alamat = StringUtil.pecahParagraf(dataPP
//					.getAlamat_kantor().toUpperCase(), 70);
//			for (int i = 0; i < alamat.length; i++) {
//				monyong = 7 * i;
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
//						529 - monyong, 0);
//			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman Kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 250, 185, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 250,
													175, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 95, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 85, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 75, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 65, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 55, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					45, 0);
			over.endText();

			// ---------- Data Halaman Keenam ----------

			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getTinggi() + "/"
									+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//					String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//							getKerja().toUpperCase(), 15);
//						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//								pekerjaan[i], 290, 519 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getKerja(), 290, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getSex(), 375, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							FormatDate.toString(pesertaPlus.getTgl_lahir()),
							430, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 517 - j, 0);
					j += 10;
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							275, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 333, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 380, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 420 - j, 0);
					j += 10;
				}
			}
			over.endText();
			
			// ------------Halaman tujuh-----------
			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			String jawab;
				 if (rslt6.size() > 0) {
					// /81, 104
								Integer j = 0;
								Integer k = 0;
								Integer l = 0;
								for (int i = 0; i < rslt6.size(); i++) {
									MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
									// j += 3;
									if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 765 - j, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 765 - j, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 765 - j, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 765 - j, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 765 - j, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 765 - j, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 765 - j, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 765 - j, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 765 - j, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 765 - j, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 745 - j, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 745 - j, 0);
											}
										}

									}
									j += 10;
									if (ans.getQuestion_id() == 84) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 595, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 595, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 595, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 595, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 595, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 595, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 595, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 595, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 595, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 595, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 595, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 595, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 85) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										}

										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 585, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 585, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 585, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 585, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 585, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 585, 0);
												}
												//--tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 585, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 585, 0);
												}
												//--tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 585, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 585, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 585, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 585, 0);
											}
										}

									}
									if (ans.getQuestion_id() == 86) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 575, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 575, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 575, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 575, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 575, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 575, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 575, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 575, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 575, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 575, 0);
												}
											}
										}else{
											if ( ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 575, 0);
											} else if ( ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 575, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 87) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 565, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 565, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 565, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 565, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 565, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 565, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 565, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 565, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 565, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 565, 0);
												}
												
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 565, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 565, 0);
											}
										}

									}
									if (ans.getQuestion_id() == 88) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 555, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 555, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 555, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 555, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 555, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 555, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 555, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 555, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 555, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 555, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 555, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 555, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 89) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 545, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 545, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 545, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 545, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 545, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 545, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 545, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 545, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 545, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 545, 0);
												}
											}
										}else{
											 if (ans.getOption_group() == 1
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 545, 0);
												} else if (ans.getOption_group() == 1
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 545, 0);
												}
										}
									
									}
									if (ans.getQuestion_id() == 90) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 535, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 535, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 535, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 535, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 535, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 535, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 535, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 535, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 535, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 535, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 535, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 535, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 91) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 525, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 525, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 525, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 525, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 525, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 525, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 525, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 525, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 525, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 525, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 525, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 525, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 92) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 515, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 515, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 515, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 515, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 515, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 515, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 515, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 515, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 515, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 515, 0);
												}
											}
										}else{
											 if (ans.getOption_group() == 1
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 515, 0);
												} else if (ans.getOption_group() == 1
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 515, 0);
												}
										}
										

									}
									if (ans.getQuestion_id() == 93) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 425, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 505, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 505, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 505, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 505, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 505, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 505, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 505, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 505, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 505, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 505, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 505, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 505, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 505, 0);
											}
										}
										
									}
									if (ans.getQuestion_id() == 94) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 495, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 495, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 495, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 495, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 495, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 495, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 495, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 495, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 495, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 495, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 495, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 495, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 95) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 485, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 485, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 485, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 485, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 485, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 485, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 485, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 485, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 485, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 485, 0);
												}
											}
										}else{
											 if (ans.getOption_group() == 1
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 485, 0);
												} else if (ans.getOption_group() == 1
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 485, 0);
												}
										}
									}
									
									if (ans.getQuestion_id() == 96) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 475, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 475, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 475, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 475, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 475, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 475, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 475, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 475, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 475, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 475, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 475, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 475, 0);
											}
										}
									}
											
									if (ans.getQuestion_id() == 97) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 465, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 465, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 465, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 465, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 465, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 465, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 465, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 465, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 465, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 465, 0);
												}
											}
										}else{
											if ( ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 465, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 465, 0);
											}
										}
									}
									if (ans.getQuestion_id() == 98) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 455, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 455, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 455, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 455, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 455, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 455, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 455, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 455, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 455, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 455, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 455, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 455, 0);
											}
										}
										
									}
									if (ans.getQuestion_id() == 99) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 445, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 445, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 445, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 445, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 445, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 445, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 445, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 445, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 445, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 445, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 445, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 445, 0);
											}
										}

									}
									if (ans.getQuestion_id() == 100) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 435, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 435, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 435, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 435, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 435, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 435, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 435, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 435, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 435, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 435, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 435, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 435, 0);
											}
										}
										
									}
									if (ans.getQuestion_id() == 101) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										}
										else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													400, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 430, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 430, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 430, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 430, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 430, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 430, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 430, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 430, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 430, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 430, 0);
												}
											}
										}
										 else{
											 if ( ans.getOption_group() == 1
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 430, 0);
												} else if ( ans.getOption_group() == 1
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 430, 0);
												}
										}
									}
									
									if (ans.getQuestion_id() == 102) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										}
										else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													352, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 390, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 390, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 390, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 390, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 390, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 390, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 390, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 390, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 390, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 390, 0);
												}
											}
										}else{
											 if (ans.getOption_group() == 1
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 390, 0);
												} else if (ans.getOption_group() == 1
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 390, 0);
												}
										}
										 
									}
									if (ans.getQuestion_id() == 103) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										}
										 else if (ans.getOption_group() == 0
													&& ans.getOption_order() == 1) {
												jawab = ans.getAnswer2();
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														Common.isEmpty(jawab) ? "-" : jawab, 110,
														282, 0);
											}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												// --tu
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 342, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 400, 342, 0);
												}
												// --tt1
												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 342, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 425, 342, 0);
												}
												// --tt2
												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 342, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 463, 342, 0);
												}
												// --tt3
												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 342, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 493, 342, 0);
												}
												// --tt4
												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 1
														&& ans.getAnswer2().equals("1")) {
													jawab = "Ya";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 342, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
														&& ans.getOption_order() == 2
														&& ans.getAnswer2().equals("1")) {
													jawab = "Tidak";
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															jawab, 540, 342, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1
													&& ans.getAnswer2().equals("1")) {
												jawab = "Ya";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 342, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2
													&& ans.getAnswer2().equals("1")) {
												jawab = "Tidak";
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														jawab, 400, 342, 0);
											}
										}
										
									}
									if (ans.getQuestion_id() == 104) {
										if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													262, 0);
										}
										if (peserta.size() > 0) {
											for (int x = 0; x < peserta.size(); x++) {
												PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
												 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 400,
															272, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 400,
															262, 0);
												}

												else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 1
														&& ans.getOption_order() == 1) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 425,
															272, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 1
														&& ans.getOption_order() == 2) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 425,
															262, 0);
												}

												else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 463,
															272, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 463,
															262, 0);
												}

												else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 1
														&& ans.getOption_order() == 1) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 493,
															272, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 1
														&& ans.getOption_order() == 2) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 493,
															262, 0);
												}

												else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 2
														&& ans.getOption_order() == 1) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 540,
															272, 0);
												} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 2
														&& ans.getOption_order() == 2) {
													jawab = ans.getAnswer2();
													over.setFontAndSize(italic, 6);
													over.showTextAligned(PdfContentByte.ALIGN_LEFT,
															Common.isEmpty(jawab) ? "-" : jawab, 540,
															262, 0);
												}
											}
										}else{
											if (ans.getOption_group() == 1
													&& ans.getOption_order() == 1) {
												jawab = ans.getAnswer2();
												over.setFontAndSize(italic, 6);
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														Common.isEmpty(jawab) ? "-" : jawab, 400,
														272, 0);
											} else if (ans.getOption_group() == 1
													&& ans.getOption_order() == 2) {
												jawab = ans.getAnswer2();
												over.setFontAndSize(italic, 6);
												over.showTextAligned(PdfContentByte.ALIGN_LEFT,
														Common.isEmpty(jawab) ? "-" : jawab, 400,
														262, 0);
											}
										}
									}
								}
							}	
			
			over.endText();
			stamp.close();
			
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
}
	
	public ModelAndView espajonlinegadgetfullsyariah(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String spajTempDirectory = props.getProperty("pdf.dir.spajtemp");
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetfullsyariah.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetfullsyariah.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),160, 409,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),160, 399,0);
			over.endText();

			// ---------- Data Halaman keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 655;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,
							pesertaPlus.getNama().toUpperCase(), 290, vertikal,
							0);
					vertikal = vertikal + 2;
				}
			}

			try {
				String ttdPenutup = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);

			over.endText();

			// //---------- Data Halaman kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman keenam ----------
			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 270, 163 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 440,
													163 - j, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 97, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 87, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 77, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 68, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 58, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					47, 0);
			over.endText();

			// ---------- Data Halaman ketujuh----------

			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getTinggi() + "/"
									+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//					String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//							getKerja().toUpperCase(), 15);
//						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//								pekerjaan[i], 290, 519 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getKerja(), 290, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							pesertaPlus.getSex(), 375, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							FormatDate.toString(pesertaPlus.getTgl_lahir()),
							430, 517 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 517 - j, 0);
					j += 10;
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							275, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 333, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 380, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 420 - j, 0);
					j += 10;
				}
			}
			// -----------data tertanggung-----------
			String jawab = "";
			if (mspo_flag_spaj.equals("3")){
				if (rslt.size() > 0) {
					Integer j = 0;
					for (int i = 0; i < rslt.size(); i++) {
						MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
						if (ans.getQuestion_id() == 1) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 348, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										330, 0);
							}
						}
						if (ans.getQuestion_id() == 2) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 319, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										310, 0);
							}
						}
						if (ans.getQuestion_id() == 3) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 300, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										270, 0);
							}
						}
						if (ans.getQuestion_id() == 4) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										230, 0);
							}
						}
						if (ans.getQuestion_id() == 5) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										203, 0);
							}
						}
						if (ans.getQuestion_id() == 6) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 193, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										183, 0);
							}
						}	
						if (ans.getQuestion_id() == 7) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										124, 0);
							}
						}
						if (ans.getQuestion_id() == 8) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							}
						}
						if (ans.getQuestion_id() == 9) {
							if (ans.getOption_type() == 0
									&& ans.getOption_order() == 1) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 115, 102, 0);
							} else if (ans.getOption_type() == 0
									&& ans.getOption_order() == 2) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 160, 92, 0);
							}
						}
						if (ans.getQuestion_id() == 10) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 86, 0);
							}
						}
						if (ans.getQuestion_id() == 11) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 63, 0);
							}
						}

					}	
			}
			}
			over.endText();

			// ------------Halaman delapan-----------
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			if (mspo_flag_spaj.equals("3")){
			if (rslt.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
					if (ans.getQuestion_id() == 12) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									725, 0);
						}
					}
					if (ans.getQuestion_id() == 13) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 705, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 705, 0);
						}
					}
					if (ans.getQuestion_id() == 15) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 685, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 685, 0);
						}
					}
				}
			}

			// -------------data pemegang polis--------------
			if (rslt2.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt2.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt2.get(i);
					if (ans.getQuestion_id() == 16) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 655, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									635, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									635, 0);
						}
					}
					if (ans.getQuestion_id() == 17) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 625, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									615, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									615, 0);
						}
					}
					if (ans.getQuestion_id() == 18) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 595, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 595, 0);
						}
					}
				}
			}
			}
			else{
				 if (rslt6.size() > 0) {
					// /81, 104
								Integer j = 0;
								Integer k = 0;
								Integer l = 0;
								for (int i = 0; i < rslt6.size(); i++) {
									MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
									// j += 3;
									if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										}
									}
									j += 10;
									if (ans.getQuestion_id() == 84) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										}
									}
									if (ans.getQuestion_id() == 85) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										}

									}
									if (ans.getQuestion_id() == 86) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										}

									}
									if (ans.getQuestion_id() == 87) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										}

									}
									if (ans.getQuestion_id() == 88) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										}

									}
									if (ans.getQuestion_id() == 89) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										}

									}
									if (ans.getQuestion_id() == 90) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										}

									}
									if (ans.getQuestion_id() == 91) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										}

									}
									if (ans.getQuestion_id() == 92) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										}

									}
									if (ans.getQuestion_id() == 93) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 425, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 505, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										}

									}
									if (ans.getQuestion_id() == 94) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										}

									}
									if (ans.getQuestion_id() == 95) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										}

									}
									if (ans.getQuestion_id() == 96) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										}

									}
									if (ans.getQuestion_id() == 97) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										}
									}
									if (ans.getQuestion_id() == 98) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										}
									}
									if (ans.getQuestion_id() == 99) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										}

									}
									if (ans.getQuestion_id() == 100) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										}
									}
									if (ans.getQuestion_id() == 101) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													400, 0);
										}
									}
									if (ans.getQuestion_id() == 102) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													352, 0);
										}
									}
									if (ans.getQuestion_id() == 103) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													282, 0);
										}
									}
									if (ans.getQuestion_id() == 104) {
										if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													262, 0);
										}

										else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													262, 0);
										}

										else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													262, 0);
										}
									}
								}
							}	
			}
			over.endText();
			
			
			// ------------Halaman sembilan-----------
			if (mspo_flag_spaj.equals("3")){
				
			
			over = stamp.getOverContent(9);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt3.size() > 0 ) {
				Integer j = 0;
				for (int i = 0; i < rslt3.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt3.get(i);
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 700 - j, 0);
							// --pp
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						}
						
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											395, 683 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											395, 683 - j, 0);
								}
								// --tt1/pt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 
										&& ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								}
								// --tt2/pt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 
										&& ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								}
								// --tt3/pt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 
										&& ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								}
								// --tt4/pt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 
										&& ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								}
								
							}
						}else{
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 683 - j, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 683 - j, 0);
							}
						}
					
					j += 1;
				}
			}

			if (rslt4.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt4.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt4.get(i);
					if (ans.getQuestion_id() == 137) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
						}
						 
						 else if (ans.getOption_group() == 0) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									320, 0);
						}
						
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tu/pu
								if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 356, 0);
								}
								else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 356, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								}
								
							}
						}else{
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
								
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
							}
						}

					}
							
					if (ans.getQuestion_id() == 139) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						}
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tu
								if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 288, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 &&ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								}
							}
						}else{
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							}
						}
						
					}
					// j += 3;
					if (ans.getQuestion_id() >= 140
							&& ans.getQuestion_id() <= 143) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						}
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tu
								 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 327 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 327 - j, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								}
							}
							}else{
								 if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									}
							}
						}
						
					j += 2;
					if (ans.getQuestion_id() == 144) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						}
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tu
								 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 188, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								}
							}
							}else{
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								}
							}
						}
						
							
					if (ans.getQuestion_id() == 145) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 178, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 168, 0);
						}
						if (peserta.size() > 0) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tu
								if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 168, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								}
							}
						}else{
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							}
						}
					}
				}
			}

			over.endText();

			// --------------Halaman sepuluh--------------//
			over = stamp.getOverContent(10);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt5.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt5.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt5.get(i);
					if (ans.getQuestion_id() == 146) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						}
						// --tt1
						else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 676, 0);
						} else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 676, 0);
						}
						// --tt2
						else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 676, 0);
						} else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 676, 0);
						}
						// --tt3
						else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 676, 0);
						} else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 676, 0);
						}
						// --tt4
						else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 676, 0);
						} else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 676, 0);
						}
					}
					// j += 3;
					if (ans.getQuestion_id() == 147) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 697,
								0);
					} else if (ans.getQuestion_id() == 148) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 667,
								0);
					} else if (ans.getQuestion_id() == 149) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 637,
								0);
					} else if (ans.getQuestion_id() == 150) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 607,
								0);
					} else if (ans.getQuestion_id() == 151) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 578,
								0);
					}
					if (ans.getQuestion_id() == 152) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						}
						// --tt1
						else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 588, 0);
						} else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 588, 0);
						}
						// --tt2
						else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 588, 0);
						} else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 588, 0);
						}
						// --tt3
						else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 588, 0);
						} else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 588, 0);
						}
						// --tt4
						else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 588, 0);
						} else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 588, 0);
						}
					}
					if (ans.getQuestion_id() == 153) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						}
						// --tt1
						else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 568, 0);
						} else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 568, 0);
						}
						// --tt2
						else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 568, 0);
						} else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 568, 0);
						}
						// --tt3
						else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 568, 0);
						} else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 568, 0);
						}
						// --tt4
						else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 568, 0);
						} else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 568, 0);
						}
					}
					if (ans.getQuestion_id() == 154) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						}
						// --tt1
						else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 548, 0);
						} else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 548, 0);
						}
						// --tt2
						else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 548, 0);
						} else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 548, 0);
						}
						// --tt3
						else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 548, 0);
						} else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 548, 0);
						}
						// --tt4
						else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 548, 0);
						} else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 548, 0);
						}
					}
					if (ans.getQuestion_id() == 155) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						}
						// --tt1
						else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 528, 0);
						} else if (ans.getOption_group() == 3
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 425, 528, 0);
						}
						// --tt2
						else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 528, 0);
						} else if (ans.getOption_group() == 4
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 463, 528, 0);
						}
						// --tt3
						else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 528, 0);
						} else if (ans.getOption_group() == 5
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 493, 528, 0);
						}
						// --tt4
						else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 528, 0);
						} else if (ans.getOption_group() == 6
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 540, 528, 0);
						}
					}
				}
				j += 2;
			}
			over.endText();
			stamp.close();
			}
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	}
	
	public ModelAndView espajonlinegadgetsiosyariah(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String spajTempDirectory = props.getProperty("pdf.dir.spajtemp");
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetsiosyariah.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetsiosyariah.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),160, 409,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),160, 399,0);
			over.endText();

			// ---------- Data Halaman keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 655;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,
							pesertaPlus.getNama().toUpperCase(), 290, vertikal,
							0);
					vertikal = vertikal + 2;
				}
			}

			try {
				String ttdPenutup = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);

			over.endText();

			// //---------- Data Halaman kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman keenam ----------
			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 250, 183, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 250,
													173, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 105, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 95, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 85, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 75, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 65, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					55, 0);
			over.endText();

			// ---------- Data Halaman ketujuh----------

			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//						String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//								getKerja().toUpperCase(), 15);
//							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//									pekerjaan[i], 290, 519 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getKerja(), 290, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}
					
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							270, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 330, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 370, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 410 - j, 0);
					j += 10;
				}
			}
			over.endText();
			
			// ------------Halaman delapan-----------
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			String jawab;

				 if (rslt6.size() > 0) {
						// /81, 104
						Integer j = 0;
						Integer k = 0;
						Integer l = 0;
						for (int i = 0; i < rslt6.size(); i++) {
							MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
							// j += 3;
							if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									}
								}

							}
							j += 10;
							if (ans.getQuestion_id() == 84) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 85) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								}

								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										}
										//--tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										}
										//--tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 86) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										}
									}
								}else{
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									} else if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 87) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										}
										
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 88) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 89) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										}
									}
								}else{
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
								}
							
							}
							if (ans.getQuestion_id() == 90) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 91) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 92) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										}
									}
								}else{
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
								}
								

							}
							if (ans.getQuestion_id() == 93) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 425, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 505, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 94) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 95) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										}
									}
								}else{
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 96) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									}
								}
							}
									
							if (ans.getQuestion_id() == 97) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										}
									}
								}else{
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 98) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 99) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 100) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 101) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											400, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										}
									}
								}
								 else{
									 if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 102) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											352, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										}
									}
								}else{
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
								}
								 
							}
							if (ans.getQuestion_id() == 103) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								}
								 else if (ans.getOption_group() == 0
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 110,
												282, 0);
									}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										// --tu
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										}
										// --tt1
										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 104) {
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											272, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											262, 0);
								}
								if (peserta.size() > 0) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										 if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													262, 0);
										}
									}
								}else{
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												272, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												262, 0);
									}
								}
							}
						}
					}	
			
			over.endText();
			stamp.close();
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	
	}
	
	public ModelAndView espajonlinegadgetexisting(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String spajTempDirectory = props.getProperty("pdf.dir.spajtemp");
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		
		 // --------------spaj template existing
		reader = new PdfReader(
				props.getProperty("pdf.template.espajonlinegadget")
						+ "\\spajonlinegadget.pdf");
		output = new FileOutputStream(exportDirectory + "\\"
				+ "espajonlinegadget_" + reg_spaj + ".pdf");

		spaj = dir + "\\spajonlinegadget.pdf";
		pdfs.add(spaj);
		suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
		String outputName = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
				+ reg_spaj + ".pdf";
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
				outputName));

		Pemegang dataPP = elionsManager.selectpp(reg_spaj);
		Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
		PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
		if (pembPremi == null)
			pembPremi = new PembayarPremi();
		AddressBilling addrBill = this.elionsManager
				.selectAddressBilling(reg_spaj);
		Datausulan dataUsulan = this.elionsManager
				.selectDataUsulanutama(reg_spaj);
		dataUsulan.setDaftaRider(elionsManager
				.selectDataUsulan_rider(reg_spaj));
		InvestasiUtama inv = this.elionsManager
				.selectinvestasiutama(reg_spaj);
		Rekening_client rekClient = this.elionsManager
				.select_rek_client(reg_spaj);
		Account_recur accRecur = this.elionsManager
				.select_account_recur(reg_spaj);
		List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
		List benef = this.elionsManager.select_benef(reg_spaj);
		List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
		List dist = this.elionsManager.select_tipeproduk();
		List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
		HashMap spajTemp = (HashMap) listSpajTemp.get(0);
		String idgadget = (String) spajTemp.get("NO_TEMP");
		String submitGadget = (String) spajTemp.get("TGL_UPLOAD");
		Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);

		String s_channel = "";
		for (int i = 0; i < dist.size(); i++) {
			HashMap dist2 = (HashMap) dist.get(i);
			Integer i_lstp_id = (Integer) dist2.get("lstp_id");
			if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
					.intValue()) {
				s_channel = (String) dist2.get("lstp_produk");
			}
		}

		Double d_premiRider = 0.;
		if (dataUsulan.getDaftaRider().size() > 0) {
			for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
				Datarider rider = (Datarider) dataUsulan.getDaftaRider()
						.get(i);
				d_premiRider = rider.getMspr_premium();
			}
		}
		Double d_topUpBerkala = new Double(0);
		Double d_topUpTunggal = new Double(0);
		Double d_totalTopup = new Double(0);
		if (inv != null) {
			DetilTopUp daftarTopup = inv.getDaftartopup();
			d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
					0) : daftarTopup.getPremi_berkala();
			d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
					0) : daftarTopup.getPremi_tunggal();
			d_totalTopup = d_topUpBerkala + d_topUpTunggal;
		}
		Double d_premiExtra = (Common.isEmpty(uwManager
				.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
				.selectSumPremiExtra(reg_spaj));
		Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
				+ d_premiRider + d_premiExtra;

		stamp.setMoreInfo(moreInfo);

		// ---------- Data Halaman Pertama ----------
		over = stamp.getOverContent(1);
		over.beginText();
		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatString.nomorSPAJ(reg_spaj), 370, 706, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 80, 693, 0);

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMcl_first().toUpperCase(), 200, 601, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMcl_first().toUpperCase(), 200, 588, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 200, 575, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 200, 562, 0);

		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 280, 551, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_totalTopup.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_totalTopup))), 280, 541, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiRider.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_premiRider))), 280, 531, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 280, 521, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_PENUTUP").toString().toUpperCase(), 200, 492,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_PENUTUP").toString(), 200, 479, 0);

		over.endText();

		// ---------- Data Halaman Kedua ----------
		over = stamp.getOverContent(2);
		over.beginText();

		// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
		// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
		String ttdPp = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_PP_"
				+ ".jpg";
		try {
			Image img = Image.getInstance(ttdPp);
			img.scaleAbsolute(40, 40);
			over.addImage(img, img.getScaledWidth(), 0, 0,
					img.getScaledHeight(), 458, 705);
			over.stroke();

			// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
			// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
			// ".jpg";
			String ttdTu = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget + "_TTD_TU_"
					+ ".jpg";
			if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
				ttdTu = ttdPp;
			Image img2 = Image.getInstance(ttdTu);
			img2.scaleAbsolute(40, 40);
			over.addImage(img2, img2.getScaledWidth(), 0, 0,
					img2.getScaledHeight(), 458, 658);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 280, 790, 0);
		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
				.getMcl_first().toUpperCase(), 300, 723, 0);
		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
				.getMcl_first().toUpperCase(), 300, 676, 0);
		if (peserta.size() > 0) {
			Integer vertikal = 676;
			for (int i = 0; i < peserta.size(); i++) {
				vertikal = vertikal - 47;
				PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,
						pesertaPlus.getNama().toUpperCase(), 300, vertikal,
						0);
				vertikal = vertikal + 2;
			}
		}

		try {
			// String ttdAgen = exportDirectory + "\\" + idgadget +
			// "_TTD_AGEN_" +
			// (agen.get("NM_PENUTUP").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdAgen = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
					+ "_TTD_AGEN_" + ".jpg";
			Image img3 = Image.getInstance(ttdAgen);
			img3.scaleAbsolute(40, 40);
			over.addImage(img3, img3.getScaledWidth(), 0, 0,
					img3.getScaledHeight(), 100, 420);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		try {
			// String ttdReff = exportDirectory + "\\" + idgadget +
			// "_TTD_REF_" +
			// (agen.get("NM_REFFERAL").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdReff = spajTempDirectory+"\\"+idgadget+"\\Spaj\\"+idgadget
					+ "_TTD_REF_" + ".jpg";
			Image img4 = Image.getInstance(ttdReff);
			img4.scaleAbsolute(40, 40);
			over.addImage(img4, img4.getScaledWidth(), 0, 0,
					img4.getScaledHeight(), 280, 420);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_PENUTUP").toString().toUpperCase(), 110, 415,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 61, 405, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_PENUTUP").toString(), 81, 395, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_REFFERAL").toString().toUpperCase(), 290, 415,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_REFFERAL").toString(), 261, 405, 0);
		over.endText();

		// ---------- Data Halaman Ketiga ----------
		over = stamp.getOverContent(3);
		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		String flag_spaj = "";
		if (dataPP.getMspo_flag_spaj() == 0) {
			flag_spaj = "SPAJ OLD";
		} else if (dataPP.getMspo_flag_spaj() == 1) {
			flag_spaj = "SPAJ NEW VA";
		} else if (dataPP.getMspo_flag_spaj() == 2) {
			flag_spaj = "SPAJ NEW VA & PEMBAYAR PREMI";
		} else if (dataPP.getMspo_flag_spaj() == 3) {
			flag_spaj = "SPAJ FULL (MARET 2015)";
		} else if (dataPP.getMspo_flag_spaj() == 4) {
			flag_spaj = "SPAJ SIO (MARET 2015)";
		} else {
			flag_spaj = "SPAJ GIO (MARET 2015)";
		}
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, flag_spaj, 190,
				788, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 778, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 190, 768, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 758, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMcl_first().toUpperCase(), 190, 748, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
						.getMcl_gelar(), 190, 739, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_mother(), 190, 730, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsne_note(), 190, 720, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
						: dataPP.getMcl_green_card(), 190, 710, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLside_name(), 190, 700, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_no_identity(), 190, 690, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataPP
								.getMspe_no_identity_expired()), 190, 680,
				0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataPP.getMspe_date_birth()),
				190, 671, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspo_age() + " Tahun", 190, 661, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMspe_sex2().toUpperCase(), 190, 651, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190,
				642, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsag_name(), 190, 632, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsed_name(), 190, 622, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
						.getMcl_company_name(), 246, 613, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMkl_kerja(), 190, 603, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
				198, 593, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_kerja_ket()) ? "-" : dataPP
						.getMkl_kerja_ket(), 190, 583, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_kantor()) ? "-" : dataPP
						.getAlamat_kantor(), 250, 573, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
						.getKota_kantor(), 153, 563, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
						.getKd_pos_kantor(), 153, 553, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
						.getTelpon_kantor(), 153, 544, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_kantor2()) ? "-" : dataPP
						.getTelpon_kantor2(), 153, 535, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 525, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
						.getAlamat_rumah(), 153, 515, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
						.getKota_rumah(), 153, 505, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
						.getKd_pos_rumah(), 153, 495, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
						.getTelpon_rumah(), 153, 486, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah2()) ? "-" : dataPP
						.getTelpon_rumah2(), 153, 476, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
						: dataPP.getAlamat_tpt_tinggal(), 177, 466, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
						.getKota_tpt_tinggal(), 153, 456, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
						: dataPP.getKd_pos_tpt_tinggal(), 153, 446, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
						.getTelpon_rumah(), 153, 437, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah2()) ? "-" : dataPP
						.getTelpon_rumah2(), 153, 427, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 417, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
						.getMsap_address(), 208, 407, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
				153, 397, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
				153, 387, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
						.getMcl_npwp(), 153, 378, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
						.getMkl_penghasilan(), 226, 368, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
						.getMkl_pendanaan(), 153, 359, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
						.getMkl_tujuan(), 190, 349, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMcl_first().toUpperCase(), 190, 319, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
						.getMcl_gelar(), 190, 309, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_mother(), 190, 300, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsne_note(), 190, 290, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
						: dataTT.getMcl_green_card(), 190, 281, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLside_name(), 190, 271, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_no_identity(), 190, 261, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataTT
								.getMspe_no_identity_expired()), 190, 251,
				0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataTT.getMspe_date_birth()),
				190, 241, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMste_age() + " Tahun", 190, 231, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMspe_sex2().toUpperCase(), 190, 221, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190,
				212, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsag_name(), 190, 202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsed_name(), 190, 192, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
						.getMcl_company_name(), 246, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMkl_kerja(), 190, 173, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
				198, 163, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_kerja_ket()) ? "-" : dataTT
						.getMkl_kerja_ket(), 190, 154, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
						.getAlamat_kantor(), 250, 144, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
						.getKota_kantor(), 153, 134, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
						.getKd_pos_kantor(), 153, 124, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
						.getTelpon_kantor(), 153, 115, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_kantor2()) ? "-" : dataTT
						.getTelpon_kantor2(), 153, 105, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 95, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
						.getAlamat_rumah(), 153, 85, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
						.getKota_rumah(), 153, 75, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
						.getKd_pos_rumah(), 153, 65, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
						.getTelpon_rumah(), 153, 56, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
						.getTelpon_rumah2(), 153, 46, 0);
		over.endText();

		// ---------- Data Halaman Keempat ----------
		over = stamp.getOverContent(4);
		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
						: dataTT.getAlamat_tpt_tinggal(), 177, 798, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
						.getKota_tpt_tinggal(), 153, 788, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
						: dataTT.getKd_pos_tpt_tinggal(), 153, 779, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
						.getTelpon_rumah(), 153, 769, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
						.getTelpon_rumah2(), 153, 759, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 749, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
						.getMsap_address(), 208, 739, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
				153, 729, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
				153, 719, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
						.getMcl_npwp(), 153, 710, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
						.getMkl_penghasilan(), 226, 700, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
						.getMkl_pendanaan(), 153, 690, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
						.getMkl_tujuan(), 190, 681, 0);

		// Data Pembayar Premi
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
				: pembPremi.getNama_pihak_ketiga(), 190, 652, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
						: pembPremi.getKewarganegaraan(), 190, 642, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
						: pembPremi.getAlamat_lengkap(), 190, 632, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
						.getTelp_rumah(), 190, 622, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
						: pembPremi.getTelp_kantor(), 190, 613, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
						.getEmail(), 190, 603, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
						: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
								.getMspe_date_birth_3rd_pendirian())), 190,
				593, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
						.getMkl_kerja(), 190, 584, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 574, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 564, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 554, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
						.getNo_npwp(), 190, 544, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getLsre_relation()) ? "-"
						: pembPremi.getLsre_relation(), 215, 535, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
						: pembPremi.getSumber_dana(), 190, 525, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
						: pembPremi.getTujuan_dana_3rd(), 190, 515, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 505, 0);

		// Data Usulan
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 476, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 466, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 456, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMspr_ins_period() + " Tahun", 190, 446, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLscb_pay_mode(), 190, 437, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 427, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 190, 417, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN"
						: "KARTU KREDIT"), 190, 407, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
				190, 397, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
				190, 387, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 378, 0);

		// Data Investasi
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 349, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpBerkala.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpBerkala))), 190, 339, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpTunggal.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpTunggal))), 190, 329, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiExtra.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_premiExtra))), 190, 319, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 190, 309, 0);

		// Data Rekening
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN"
						: "KARTU KREDIT"), 190, 280, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getLsbp_nama(), 190, 270, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_cabang(), 190, 260, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 252, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 242, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_no_ac(), 190, 232, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_nama(), 190, 222, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, rekClient
				.getKuasa().toUpperCase(), 190, 212, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(rekClient.getTgl_surat()), 190,
				202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 193, 0);

		Double d_jmlinves = new Double(0);
		String s_jnsinves = "";
		for (int i = 0; i < detInv.size(); i++) {
			DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
			d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
			s_jnsinves = s_jnsinves
					+ detInves.getLji_invest1().toUpperCase() + " "
					+ detInves.getMdu_persen1() + "%";
			if (i != (detInv.size() - 1))
				s_jnsinves = s_jnsinves + ", ";
		}
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_jmlinves)), 208, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 190,
				173, 0);

		if (benef.size() > 0) {
			Integer j = 0;
			for (int i = 0; i < benef.size(); i++) {
				Benefeciary benefit = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_first(), 60, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getSmsaw_birth(), 208, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_sex() == 1 ? "PRIA" : "WANITA",
						255, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dataPP.getLsne_note(), 310, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
						.getLsre_relation().toUpperCase(), 400, 123 - j, 0);
				j += 10;
			}
		}
		over.endText();

		stamp.close();

		File l_file = new File(outputName);
		FileInputStream in = null;
		ServletOutputStream ouputStream = null;
		try {

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "Inline");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			in = new FileInputStream(l_file);
			ouputStream = response.getOutputStream();

			IOUtils.copy(in, ouputStream);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (ouputStream != null) {
					ouputStream.flush();
					ouputStream.close();
				}
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		return null;
	}
	

}
