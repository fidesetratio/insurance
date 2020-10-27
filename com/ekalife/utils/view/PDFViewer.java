package com.ekalife.utils.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.PrintPolisMultiController;
import com.ekalife.utils.Common;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

/**
 *	PDF Viewer ini di-customize hanya untuk view file2 SSU polis yang ada di server 
 *	di folder D:\Ekaweb\PDF\SSU
 * 
 * @author Yusuf Sutarko
 */
public class PDFViewer extends AbstractPdfView {

	private ElionsManager elionsManager;
	private UwManager uwManager;
	private Properties props;
	private Products products;
	private BacManager bacManager;
    
	//START 1 APRIL 2007, SEMUA FILE SSU BERUBAH (AJ EKALIFE -> AJ SINARMAS)
	public static File productFile(ElionsManager elionsManager, UwManager uwManager, String dir, String spaj, Map m,Properties props) {
		String bisnis = (String) m.get("BISNIS");
		String det = (String) m.get("DETBISNIS");
		String pack = (String) m.get("FLAGPACKET"); // Andhika - eka.mst_insured.flag_packet
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		User user = new User();
		Map data = uwManager.selectDataUsulan(spaj);
		Date begdate = (Date) data.get("MSTE_BEG_DATE");
		int flagnew =(Integer) data.get("MSPO_FLAG_NEW");//flag untuk produk yg menggunakan nama smile

		Date juli2007 = (new GregorianCalendar(2007,6,1)).getTime(); // 1 januari 2007
		Date  a16Sep = (new GregorianCalendar(2007,8,16)).getTime(); //setelah tanggal 16 september 2007
		Date sep2009 = (new GregorianCalendar(2009,9,1)).getTime(); // untuk simas stabil link (164-2) ada tiga macam SSU : sebelum dan sesudah tgl 1 september 2009, after 1 oct
		Date oct2009 = (new GregorianCalendar(2009,10,1)).getTime(); // untuk simas stabil link (164-2) ada tiga macam SSU : sebelum dan sesudah tgl 1 september 2009, after 1 oct
		
		if(flagnew==1){
			dir=props.getProperty("pdf.dir.syaratpolis3");
		}
		
		Integer flag_special = uwManager.selectFlagSpecial(spaj);
		Pemegang pp = elionsManager.selectpp(spaj);
		Tertanggung tt = elionsManager.selectttg(spaj);
		user.setLus_id(pp.getLus_id().toString());
		user = elionsManager.selectLoginAuthenticationByLusId(user);
		String lku_id = (String)m.get("LKU_ID");
		//SPECIAL CASES
		if("052, 085".indexOf(bisnis) > -1) { //EKA PROTEKSI
			if(!pp.getMcl_id().trim().equals(tt.getMcl_id().trim())) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEDA PPTTG.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if(bisnis.equals("096")){ //MULTI INVEST
			if( det.equals("001")|| det.equals("002")|| det.equals("003")){ //MULTI INVEST MANFAAT LAMA
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}else if( det.equals("007")|| det.equals("008")|| det.equals("009")){ //MULTI INVEST MANFAAT LAMA
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}else { //MULTI INVEST MANFAAT BARU
				return new File(dir+"/"+m.get("BISNIS")+"-All.pdf");
			}
		}else if(bisnis.equals("099")){ //MULTI INVEST (AS)
			if( det.equals("001")|| det.equals("002")|| det.equals("003")){ //MULTI INVEST MANFAAT LAMA
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}else { //MULTI INVEST MANFAAT BARU
				return new File(dir+"/"+m.get("BISNIS")+"-All.pdf");
			}
		}else if("138".equals(bisnis)) { //EKALINK 80+ KARYAWAN
			if(elionsManager.selectIsKaryawanEkalife(spaj)>0) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KARYAWAN.pdf"); 
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if("141".equals(bisnis)) { //EDUVEST
			if(elionsManager.selectIsKaryawanEkalife(spaj)>0) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KARYAWAN.pdf"); 
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if("142".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
			if(det.equals("006") || det.equals("011")) { //POWERSAVE UOB (PRIVILEGE) || SMART SAVE
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
//			}else if(det.equals("008")){
//				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-SOFTCOPY.pdf");
			}else if(begdate.before(juli2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-old.pdf");
			}else if(Integer.parseInt(det) == 13) { //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				if(lku_id.equalsIgnoreCase("01"))
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-new.pdf");
				else
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-USD.pdf");
			}
//		}else if("143".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
//			if(begdate.before(juli2007)) {
//				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-old.pdf");
//			}else {
//				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-new.pdf");
//			}
		}else if("144".equals(bisnis)) { //KELUARGA PRODUK POWERSAVE
			if(begdate.before(juli2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-old.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-new.pdf");
			}
		}else if("158".equals(bisnis)) { //KELUARGA POWERSAVE MANFAAT BULANAN + STABLE SAVE MANFAAT BULANAN YG LAMA
			if(begdate.before(juli2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-old.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-new.pdf");
			}
		}else if(bisnis.equals("162")){ //EKALINK 88
			if( det.equals("003")|| det.equals("004")) {
				if(begdate.before(a16Sep)) {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-old.pdf");
				}else {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-new.pdf");
				}
			}else{
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if("164".equals(bisnis)){ //SIMAS STABIL LINK
			if(det.equals("002")){
				if(begdate.before(sep2009)){
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KHUSUS_BSM_BEFORE1SEP.pdf");
				}else if(begdate.before(oct2009)){
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KHUSUS_BSM_AFTER1SEP.pdf");
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
				}
			}else {
				if(flag_special==1){
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-SPECIAL.pdf");
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
				}
			}
		}else if("175".equals(bisnis) && det.equals("002")) { //PRODUK POWERSAVE SYARIAH BSM
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KHUSUS_BSM.pdf");
				
		}else if("178".equals(bisnis)){ //smart medicare dmtm dan mallins
			
			if(cabang.equals("58")){
				return new File(dir+"/"+m.get("BISNIS")+"-All-MALLASSURANCE.pdf");
			}else if(cabang.equals("40")){
				return new File(dir+"/"+m.get("BISNIS")+"-All-DMTM.pdf");
			}else{
				return new File(dir+"/"+m.get("BISNIS")+"-All-WORKSITE.pdf");
			}
			
		}else if("208".equals(bisnis)){ // *SMART KID			
			if(cabang.equals("58")){
				return new File(dir+"/"+m.get("BISNIS")+"-All-MALLASSURANCE.pdf");
			}else if(cabang.equals("42")){
				return new File(dir+"/"+m.get("BISNIS")+"-All-WORKSITE.pdf");
			}else if(cabang.equals("09")){
				if (Integer.parseInt(det) >=13 && Integer.parseInt(det) <=16){
					return new File(dir+"/"+m.get("BISNIS")+"-VIP"+".pdf");
				}else if(Integer.parseInt(det) >=17 && Integer.parseInt(det) <=20){
					return new File(dir+"/"+m.get("BISNIS")+"-All-BJB.pdf");//SMile Kid
				}else  if(Integer.parseInt(det) >=5 && Integer.parseInt(det) <=8){
					return new File(dir+"/"+m.get("BISNIS")+"-All-BANCASS.pdf");//SIMAS Kid
				}
			}else if(cabang.equals("37")){
				if (Integer.parseInt(det) >=9 && Integer.parseInt(det) <=12){					
					return new File(dir+"/"+m.get("BISNIS")+"-All-AGENCY_New.pdf");//SMile Kid
				}else if(Integer.parseInt(det) >=37 && Integer.parseInt(det) <=44){
					return new File(dir+"/"+m.get("BISNIS")+"-All-AGENCY_New.pdf");//SMile Kid
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-All-AGENCY.pdf");//SMile Kid
				}
			}else if(cabang.equals("40")){
				//Mark Valentino 2018-10-03 SmartKid 208 29 s/d 32
				if(((Integer.parseInt(m.get("DETBISNIS").toString()) >= 29) && (Integer.parseInt(m.get("DETBISNIS").toString()) <= 32)) || ((Integer.parseInt(m.get("DETBISNIS").toString()) >= 45) && (Integer.parseInt(m.get("DETBISNIS").toString()) <= 48))){
					return new File(dir+"/"+m.get("BISNIS") + "-" + m.get("DETBISNIS") + ".pdf");					
				}else 
					return new File(dir+"/"+m.get("BISNIS")+"-All-DMTM.pdf");
			}else{
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			};
			
		}else if("181".equals(bisnis)){
			if(det.equals("001")){
				return new File(dir+"/"+m.get("BISNIS")+"-case10.pdf");
			}else if(det.equals("002") || det.equals("005")){
				if(pp.getLsre_id()==1){
					return new File(dir+"/"+m.get("BISNIS")+"-case8.pdf");
				}else if(pp.getLsre_id()!=1 && tt.getMste_age()<17){
					return new File(dir+"/"+m.get("BISNIS")+"-case9.pdf");
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-case10.pdf");
				}
			}else if(det.equals("003") || det.equals("006")){
				if(pp.getLsre_id()==1){
					return new File(dir+"/"+m.get("BISNIS")+"-case8.pdf");
				}else if(pp.getLsre_id()!=1 && tt.getMste_age()<17){
					return new File(dir+"/"+m.get("BISNIS")+"-case9.pdf");
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-case10.pdf");
				}
			}
		}else if("129".equals(bisnis) && (det.equals("005") || det.equals("006") || det.equals("009") || det.equals("010") || det.equals("011") || det.equals("012")) ){
			if(pack.equals("12")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf"); 
			}else if(pack.equals("13")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf");
			}else if(pack.equals("14")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf");
			}else if(pack.equals("15")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf");
			}else if(pack.equals("16")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf");
			}else if(pack.equals("17")){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-"+m.get("FLAGPACKET")+".pdf");
			}
		}else if("201".equals(bisnis)){
			return new File(dir+"/"+m.get("BISNIS")+"-All.pdf");
		}else if("219".equals(bisnis)){
			if("219".equals(bisnis) && (Integer.parseInt(det) >= 5 || Integer.parseInt(det) <= 8)) //helpdesk [138638] produk baru SPP Syariah (219-5~8)
				return new File(dir+"/"+m.get("BISNIS")+"-ALL-SYARIAH.pdf");
			else
				return new File(dir+"/"+m.get("BISNIS")+"-All.pdf");
		}else if("226".equals(bisnis) && (Integer.parseInt(det) >= 1 && Integer.parseInt(det) <= 5)){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
			return new File(dir+"/"+m.get("BISNIS")+"-ALL.pdf");
		}else if("134".equals(bisnis) && Integer.parseInt(det) == 13){ //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
			return new File(dir+"/"+m.get("BISNIS")+"-013.pdf");
		}else if("163".equals(bisnis) && (Integer.parseInt(det) >= 26 && Integer.parseInt(det) <= 30)){ //helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9
			return new File(dir+"/"+bisnis+"-2630.pdf");
		}else if("173".equals(bisnis) && (Integer.parseInt(det) >= 7 && Integer.parseInt(det) <= 9)){ //helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9
			return new File(dir+"/"+bisnis+"-7_9.pdf");
		}
		
		//yg 1 format untuk semua detail bisnis
		if ("143, 155, 168, 178, 179, 183, 189, 193, 195,204".contains(bisnis)){
			//Andhika - req:Ningrum SMiLe Medical PT ARCO MITRA SEJATI 
			if("183,189,204".indexOf(bisnis)>-1 && cabang.equals("40")){
				//SSU BTN
				if ("183".equals(bisnis) && (Integer.parseInt(det)>=76 && Integer.parseInt(det)<=90)){
					return new File(dir+"/"+m.get("BISNIS")+"SMC"+".pdf");
				}else if("183".equals(bisnis) && ((Integer.parseInt(det)>=106 && Integer.parseInt(det)<=120)||(Integer.parseInt(det)>=133 && Integer.parseInt(det)<=134))){
					return new File(dir+"/"+m.get("BISNIS")+"-DKI"+".pdf"); // SMile MEdical Care DKI
				}else if("204".equals(bisnis) && (Integer.parseInt(det)>=37 && Integer.parseInt(det)<=41)){
					return new File(dir+"/"+m.get("BISNIS")+"-DMTM-SIO-NEW"+".pdf"); // MARZ,VALDO,GOS,VASCO,DENA, SYNERGYS, SSI (R-100 s/d R500)
				}else if("204".equals(bisnis) && (Integer.parseInt(det)>=42 && Integer.parseInt(det)<=48)){
					return new File(dir+"/"+m.get("BISNIS")+"-DMTM-SIO"+".pdf"); // MARZ,VALDO,GOS,VASCO,DENA, SYNERGYS, SSI
				}else if("189".equals(bisnis) && (Integer.parseInt(det)>=33 && Integer.parseInt(det)<=47)){
					return new File(dir+"/"+m.get("BISNIS")+"-DMTM-SIO"+".pdf"); // MARZ,VALDO,GOS,VASCO,DENA, SYNERGYS, SSI, PT AUSINDO PRATAMA KARYA(APK)
				}else if("183".equals(bisnis) && (Integer.parseInt(det)>=135 && Integer.parseInt(det)<=149)){
					return new File(dir+"/"+m.get("BISNIS")+"-DMTM-BJB"+".pdf"); // [141424] - NANA DMTM BJB 135 - 149 
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-TM.pdf");
				}
			}else{
				if ("195".equals(bisnis) && (Integer.parseInt(det)>=13 && Integer.parseInt(det)<=24)){
					return new File(dir+"/"+m.get("BISNIS")+"SHC"+".pdf");
				}else if ( ("195".equals(bisnis) && (Integer.parseInt(det)>=49 && Integer.parseInt(det)<=60)) ||
							("183".equals(bisnis) && (Integer.parseInt(det)>=91 && Integer.parseInt(det)<=105)) ){
					return new File(dir+"/"+m.get("BISNIS")+"-VIP"+".pdf");
				}else if ("195".equals(bisnis) && (Integer.parseInt(det)>=61 && Integer.parseInt(det)<=72)){
					return new File(dir+"/"+m.get("BISNIS")+"-HCD"+".pdf"); // Smile Hospital Care Bank DKI
				}else if ("195".equals(bisnis) && (Integer.parseInt(det)>=37 && Integer.parseInt(det)<=48)){
					return new File(dir+"/"+m.get("BISNIS")+"-SHP"+".pdf"); // Smile Hospital Plus
				}else if ("195".equals(bisnis) && (Integer.parseInt(det)>=73 && Integer.parseInt(det)<=84)){
					return new File(dir+"/"+m.get("BISNIS")+"-SHP-CASH-PLAN"+".pdf"); // Hospital Cash Plan
				}else if("189".equals(bisnis) && (Integer.parseInt(det)>=48 && Integer.parseInt(det)<=62)){ 
					return new File(dir+"/"+m.get("BISNIS")+"-BSIM"+".pdf"); //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
				}else if("195".equals(bisnis) && (Integer.parseInt(det)>=85 && Integer.parseInt(det)<=96)){ 
					return new File(dir+"/"+m.get("BISNIS")+"-SHC-DMTM-BJB"+".pdf"); //[141424] - NANA 195 85-96 Smile hospital care DMTM
				}else{
					return new File(dir+"/"+m.get("BISNIS")+"-All.pdf");
				}
			}
		}
		if(user!=null){
			if ("120".equals(bisnis) && user.getLus_bas()==2 && "22,23,24".indexOf(bisnis) >=0){
				return new File(dir+"/"+m.get("BISNIS")+" SIMPOL"+".pdf");
			}
		}	
		
		//selain itu, format default
		return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
	}
	
	
	/*
	private static File productFile(ElionsManager elionsManager, String dir, String spaj, Map m) {
		String bisnis = (String) m.get("BISNIS");
		String det = (String) m.get("DETBISNIS");

		Map data = elionsManager.selectDataUsulan(spaj);
		Date begdate = (Date) data.get("MSTE_BEG_DATE");
		Date januari2007 = (new GregorianCalendar(2007,0,1)).getTime(); // 1 januari 2007
		Date februari2007 = (new GregorianCalendar(2007,1,1)).getTime(); // 1 februari 2007
		Date midFeb2007 = (new GregorianCalendar(2007,1,15)).getTime(); // 15 februari 2007
		
		//SPECIAL CASES
		if("052, 085".indexOf(bisnis) > -1) { //EKA PROTEKSI
			Pemegang pp = elionsManager.selectpp(spaj);
			Tertanggung tt = elionsManager.selectttg(spaj);
			if(!pp.getMcl_id().trim().equals(tt.getMcl_id().trim())) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEDA PPTTG.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if("150, 151".indexOf(bisnis) > -1) { //SIMPONI
			String lsrg = (String) elionsManager.selectRegion(spaj).get("LSRG_NAMA");
			if(lsrg.equals("0921")) {
				return new File(dir+"/"+m.get("BISNIS")+"-BANK SHINTA.pdf");
			}else if(lsrg.equals("0922")) {
				return new File(dir+"/"+m.get("BISNIS")+"-BII.pdf");
			}
		}else if("141".equals(bisnis)) {
			if(elionsManager.selectIsKaryawanEkalife(spaj)>0) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-KARYAWAN.pdf"); 
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
			}
		}else if("142".equals(bisnis)) {
			if(begdate.before(midFeb2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070215.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070215.pdf");
			}			
		}else if("143,144".indexOf(bisnis)>-1) {
			if(begdate.before(februari2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070201.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070201.pdf");
			}			
		}else if("158".equals(bisnis)) { 
			if("001, 002, 003".indexOf(det)>-1) {//POWER SAVE BULANAN INDIVIDU, BII, AS
				if(begdate.before(februari2007)) {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070201.pdf");
				}else {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070201.pdf");
				}
			}else if(det.equals("005")) {//PLATINUM SAVE BULANAN
				if(begdate.before(januari2007)) {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070101.pdf");
				}else if(begdate.before(midFeb2007)){
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070101.pdf");
				}else {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070215.pdf");
				}
			}else if(det.equals("006")) { //POWER SAVE BULANAN SHINTA
				if(begdate.before(januari2007)) {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070101.pdf");
				}else if(begdate.before(midFeb2007)){
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070101.pdf");
				}else {
					return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070215.pdf");
				}
			}
		}else if("155".equals(bisnis)) { //PLATINUM SAVE
			if(begdate.before(januari2007)) {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-BEFORE 20070101.pdf");
			}else if(begdate.before(midFeb2007)){
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070101.pdf");
			}else {
				return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+"-AFTER 20070215.pdf");
			}
		}
		return new File(dir+"/"+m.get("BISNIS")+"-"+m.get("DETBISNIS")+".pdf");
	}
	*/
	
	//START 1 APRIL 2007, SEMUA FILE SSU BERUBAH (AJ EKALIFE -> AJ SINARMAS)
	public static File riderFile(ElionsManager elionsManager,UwManager uwManager, String dir, String produkUtama, String detProdukUtama, Map m,String spaj, Properties props) {
		String bisnis = (String) m.get("BISNIS");
		String detbisnis = (String) m.get("DETBISNIS");
		Date begdate=uwManager.selectBegDateInsured(spaj);
		bisnis = FormatString.rpad("0", bisnis, 3);
		detbisnis = FormatString.rpad("0", detbisnis, 3);
		Map data = uwManager.selectDataUsulan(spaj);
		int flagnew =(Integer) data.get("MSPO_FLAG_NEW");//flag untuk produk yg menggunakan nama smile
		String daftarSyariah = props.getProperty("product.syariah");
		if(flagnew==1){
			dir=props.getProperty("pdf.dir.syaratpolis3");
		}
		
		
		//untuk site DMTM SIO EXTERNAL 189 (33-47) dan 204 (37-48) hanya print SSU saja
		if ((produkUtama.equals("189") && (Integer.parseInt(detProdukUtama)>=33 && Integer.parseInt(detProdukUtama)<=47) && bisnis.equals("823")) 
				|| (produkUtama.equals("189") && (Integer.parseInt(detProdukUtama)>=48 && Integer.parseInt(detProdukUtama)<=62) && bisnis.equals("823")) 
				|| (produkUtama.equals("204") && (Integer.parseInt(detProdukUtama)>=37 && Integer.parseInt(detProdukUtama)<=48) && bisnis.equals("826"))){ 
			return null;
		}
		 
		//SPECIAL CASES
		if(produkUtama.equals("134") && ("005, 006".indexOf(bisnis) < -1)) { //PLATINUM LINK / AMANAH LINK (Yusuf - 03/01/2008)
			if("807, 811, 813".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-PLATINUM LINK.pdf");
			}else if("814, 816".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-PLATINUM LINK.pdf");
			}
		}
		// (Andhika-10/04/13) SSU SMiLe LINK MEDIVEST tidak lagi menggunakan SSU khusus MEDIVEST req : NOVIE
//		else if(produkUtama.equals("140")) { //MEDIVEST
//			return new File(dir+"/RIDER/"+bisnis+"-MEDIVEST.pdf");
//		}
		else if(produkUtama.equals("141")) { //EDUVEST, GAK ADA RIDER LAGI, UDAH GABUNG 1 FILE
			if ("810".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-PA Eduvest.pdf");
			}else if("804".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-804-Eduvest.pdf");
			}else if("813".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-813-CI Eduvest.pdf");
			}
		}else if((produkUtama.equals("164") && detProdukUtama.equals("002") ) || (produkUtama.equals("142") && detProdukUtama.equals("002")) || (produkUtama.equals("175") && detProdukUtama.equals("002"))){
			if("813".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-CI BSM.pdf");
			}else if("818".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-Term BSM.pdf");
			}
		}else if(produkUtama.equals("194") ){
			if("813".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-CI MAXISAVE.pdf");
			}else if("818".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+bisnis+"-Term MAXISAVE.pdf");
			}
		}else if(produkUtama.equals("128") || produkUtama.equals("129")){ //Cerdas udah ga ada rider lagi udah di gabung 1 File--dian
			return null;
		}else if(produkUtama.equals("160")) { //EKALINK SYARIAH, RIDERNYA BEDA SENDIRI
			if("812, 813".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-EKALINK SYARIAH.pdf");
			}else if(bisnis.equals("810")){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			}
			if("814, 816, 817, 828".indexOf(bisnis) > -1 && detbisnis.equals("001")) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("815") && "001, 006".indexOf(detbisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			}
		}else if(daftarSyariah.indexOf(produkUtama) > -1 ) { //APABILA PRODUK SYARIAH, ADA RIDER2 TERTENTU YANG BEDA
			if("814, 816, 817".indexOf(bisnis) > -1 && detbisnis.equals("001")) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("815") && "001, 006".indexOf(detbisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("812")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("822")) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("810")) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("811")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("819")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("823")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("825")) { // Andhika (14-11-2013)
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("826")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			}else if(bisnis.equals("827") || bisnis.equals("828")){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");	
			}else if(bisnis.equals("830")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("831")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("832")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("833")) {
				return new File(dir+"/RIDER/"+bisnis+"-SYARIAH.pdf");
			} else if(bisnis.equals("813")){
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-SYARIAH.pdf");
			}
		}else if (produkUtama.equals("173")){
			if (detbisnis.equals("001")){
				return new File(dir+"/RIDER/SSKSinglePremiumEkaSarjanaMandiri.pdf");		
			}else{
				return new File(dir+"/RIDER/SSKRegulerPremiumEkaSarjanaMandiri.pdf");
			}	
		}else if (produkUtama.equals("168")){
			return new File(dir+"/RIDER/SSKEndCareJanuari2009.pdf");
		}else if (produkUtama.equals("179")){
			return new File(dir+"/RIDER/179-SSK-All.pdf");
		}else if (produkUtama.equals("120")){
			if (bisnis.equals("819")){
			return new File(dir+"/RIDER/819C.pdf");
			}
		}
		// DIAN SPECIAL CASE
		if(bisnis.equals("819")){
			if(Integer.parseInt(detbisnis)>=281 && Integer.parseInt(detbisnis)<=380){
				return new File(dir+"/RIDER/"+bisnis+"-"+"HCPFamily4.pdf");
			}else{
				if("819".indexOf(bisnis) > -1){
					return new File(dir+"/RIDER/"+bisnis+"-"+"HCPFamily.pdf");
				}
			}
		}
		//Ryan - ambil rider 826 secara global
		if(bisnis.equals("826")){
			return new File(dir+"/RIDER/826.pdf");
		}
		// *Andhika
		if(bisnis.equals("830")){
			return new File(dir+"/RIDER/830.pdf");
		}
		
		if("835".indexOf(bisnis)>-1){
			if(props.getProperty("product.syariah").equals(produkUtama)){
				return new File(dir+"/RIDER/"+bisnis+"-ALL-SYARIAH.pdf");
			}else{
				return new File(dir+"/RIDER/"+bisnis+"-ALL.pdf");
			}
		}
		
		if(bisnis.equals("836")){
			return new File(dir+"/RIDER/836.pdf");
		}
		
		if(bisnis.equals("838")){
			return new File(dir+"/RIDER/838.pdf");
		}
		
		if ((!produkUtama.equals("183") && !produkUtama.equals("189") && !produkUtama.equals("193")) && (bisnis.equals("820") || bisnis.equals("823") || bisnis.equals("825") || bisnis.equals("831") || bisnis.equals("832") || bisnis.equals("833"))){ //kalo ssu nya udah eka sehat, ssk nya gak perlu lagi (yusuf)
			if(bisnis.equals("820")){
				return new File(dir+"/RIDER/820.pdf");
			}else if(bisnis.equals("823")){
				if(props.getProperty("product.syariah").equals(produkUtama)){
					return new File(dir+"/RIDER/823-SYARIAH.pdf");
				}else{
					return new File(dir+"/RIDER/823.pdf");
				}
			}else if(bisnis.equals("825")){
				if(props.getProperty("product.syariah").equals(produkUtama)){
					return new File(dir+"/RIDER/825-SYARIAH.pdf");
				}else{
					return new File(dir+"/RIDER/825.pdf");
				}
			}else if(bisnis.equals("831")){ // *Ladies HCP
				return new File(dir+"/RIDER/831.pdf");
			}else if(bisnis.equals("832")){ // *Ladies MEDICAL
				return new File(dir+"/RIDER/832.pdf");
			}else if(bisnis.equals("833")){ // *Ladies MEDICAL IL
				return new File(dir+"/RIDER/833.pdf");
			}
			
		} // BANCASS ADD PRODUK 220, 213, 134,140 UNTUK SME 848 
		if(produkUtama.equals("217") || produkUtama.equals("218") || produkUtama.equals("116") || produkUtama.equals("153") || //kerjain helpdesk 132786, tambah rider smile medical extra pada produk smile link pro 100 dan smile link 88 //chandra
				produkUtama.equals("190") || produkUtama.equals("200") || produkUtama.equals("118") || produkUtama.equals("220") || produkUtama.equals("213") || produkUtama.equals("134") || produkUtama.equals("140") ) { //project Smile Medical Extra (848-1~70) helpdesk [129135] //Chandra
			if(bisnis.equals("848") && (
					(produkUtama.equals("190") && (detProdukUtama.equals("003") || detProdukUtama.equals("004") || detProdukUtama.equals("002") || detProdukUtama.equals("007") || detProdukUtama.equals("008")   )) || 
					(produkUtama.equals("217") && detProdukUtama.equals("001")) ||
					(produkUtama.equals("116") && (detProdukUtama.equals("003") || detProdukUtama.equals("004"))) ||
					(produkUtama.equals("118") && (detProdukUtama.equals("003") || detProdukUtama.equals("004"))) || //helpdesk [139563]
					(produkUtama.equals("220") && (detProdukUtama.equals("001") || detProdukUtama.equals("002") || detProdukUtama.equals("004"))) ||
					(produkUtama.equals("213") && detProdukUtama.equals("002")) ||
					(produkUtama.equals("134") && detProdukUtama.equals("013")) ||
					(produkUtama.equals("140") && (detProdukUtama.equals("001") || detProdukUtama.equals("002"))) 
					)) {
				File filepdf = null;
				if(Integer.parseInt(detbisnis) >= 1 && Integer.parseInt(detbisnis) <= 70)
					filepdf = new File(dir+"/RIDER/848.pdf");
				else if(Integer.parseInt(detbisnis) >= 71 && Integer.parseInt(detbisnis) <= 140)
					filepdf = new File(dir+"/RIDER/848_71-140.pdf");
				
				return filepdf;
			} else if(bisnis.equals("848") && (
					(produkUtama.equals("200") && (detProdukUtama.equals("003") || detProdukUtama.equals("004"))) ||
					(produkUtama.equals("218") && detProdukUtama.equals("001")) ||
					(produkUtama.equals("153") && (detProdukUtama.equals("003") || detProdukUtama.equals("004")))
					)) {
				return new File(dir+"/RIDER/848-SYARIAH.pdf");
			}
		}
//		if(produkUtama.equals("053")) { //Super Sehat Plus
//			return new File(dir+"/RIDER/Dftr_Pembedahan.pdf");
////			}
//		}
		if((produkUtama.equals("143")&& Integer.parseInt(detProdukUtama)>=4 && Integer.parseInt(detProdukUtama)<=6) ||
				(produkUtama.equals("158") && (Integer.parseInt(detProdukUtama)==13 || Integer.parseInt(detProdukUtama)==15 || Integer.parseInt(detProdukUtama)==16)) ||
				(produkUtama.equals("164")) || (produkUtama.equals("184")) || (produkUtama.equals("142") && Integer.parseInt(detProdukUtama)==2) ||
				(produkUtama.equals("185")) || (produkUtama.equals("175") && Integer.parseInt(detProdukUtama)==2)){ //Untuk super sejahtera(185), gunakan SSK swine Flu bank SInarmas req: Inge(tgl 18/11/2009)
			if(bisnis.equals("822")){
				return new File(dir+"/RIDER/822-001-STABLE.pdf");
			}
		}else{
			if(bisnis.equals("822")){
				return new File(dir+"/RIDER/822-001-GLOBAL.pdf");
			}
		}
		
		//Yusuf, special case untuk rider2nya produk cerdas (120)
		if((produkUtama.equals("120") || produkUtama.equals("121"))){
			if("812".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/"+produkUtama+"-"+detProdukUtama+"-"+bisnis+".pdf");
			}
			else if("811".indexOf(bisnis) > -1){
					return new File(dir+"/RIDER/"+produkUtama+"-"+"HCP Family_CERDAS"+"-"+bisnis+".pdf");
			}
			if("818".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/818C.pdf");
			}
			else if("819".indexOf(bisnis) > -1){
					return new File(dir+"/RIDER/819C.pdf");
			}
			else if("813".indexOf(bisnis) > -1){
				return new File(dir+"/RIDER/813C.pdf");
			}else if("807".indexOf(bisnis) > -1){
					return new File(dir+"/RIDER/807C.pdf");
			}else if("810".indexOf(bisnis) > -1){
				if (detbisnis.equals("001")){
					return new File(dir+"/RIDER/810C1.pdf");
				}else if (detbisnis.equals("002")){
					return new File(dir+"/RIDER/810C2.pdf");
				}else if (detbisnis.equals("003")){
					return new File(dir+"/RIDER/810C3.pdf");
				}
			}
			else if("814".indexOf(bisnis) > -1){
				if (detbisnis.equals("003")){
					return new File(dir+"/RIDER/814C3.pdf");
				}else if (detbisnis.equals("004")){
					return new File(dir+"/RIDER/814C4.pdf");
				}else if (detbisnis.equals("005")){
					return new File(dir+"/RIDER/814C5.pdf");
				}
			}
			else if("815".indexOf(bisnis) > -1){
				if (detbisnis.equals("002")){
					return new File(dir+"/RIDER/815C2.pdf");
				}else if (detbisnis.equals("004")){
					return new File(dir+"/RIDER/815C4.pdf");
				}else if (detbisnis.equals("005")){
					return new File(dir+"/RIDER/815C5.pdf");
				}
			}else if("816".indexOf(bisnis) > -1){
				if (detbisnis.equals("002")){
					return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
//					return new File(dir+"/RIDER/816C2.pdf");
				}else if (detbisnis.equals("003")){
//					return new File(dir+"/RIDER/816C3.pdf");
					return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
				}
			}else if("828,817,837".indexOf(bisnis) > -1){
					return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
			}
			else{
				return new File(dir+"/RIDER/"+produkUtama+"-"+detProdukUtama+"-"+bisnis+"-"+detbisnis+".pdf");
			}
		}
		if(produkUtama.equals("166")){
			if((bisnis.equals("819") )||(bisnis.equals("811") )){
				return new File(dir+"/RIDER/"+"166-819.pdf");	
			}else if(bisnis.equals("815") ) {
				return new File(dir+"/RIDER/"+"166-815.pdf");	
			}else if(("810".indexOf(bisnis) > -1)||("800".indexOf(bisnis) > -1)) {
				return new File(dir+"/RIDER/"+"166-810.pdf");	
			}else if(bisnis.equals("813") ) {
				return new File(dir+"/RIDER/"+"166-813.pdf");	
			}else if(bisnis.equals("814") ) {
				return new File(dir+"/RIDER/"+"166-814.pdf");	
			}else if(("812".indexOf(bisnis) > -1)) {
				return new File(dir+"/RIDER/"+"166-812.pdf");	
			}else if(bisnis.equals("817") ) {
				return new File(dir+"/RIDER/"+"166-817.pdf");	
			}
		}
		if(produkUtama.equals("096")){
			if(bisnis.equals("813")) {
				return new File(dir+"/RIDER/813-MI.pdf");
			}else if(bisnis.equals("818")) {
				return new File(dir+"/RIDER/818-MI.pdf");
			}
		}
		
		//lufi--Khusus Term Unit Link kecuali 120 dan 121
		if(props.getProperty("product.unitLink.new").indexOf(produkUtama)>=0){
			if(bisnis.equals("818")) {
				return new File(dir+"/RIDER/818-ULINKALL.pdf");
			}
		}else if(props.getProperty("product.unitLink.syariah").indexOf(produkUtama)>=0){
			if(bisnis.equals("818")) {
				return new File(dir+"/RIDER/818-ULINKSYARIAH.pdf");
			}
		}
		
		return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
	}
	
	/*
	private static File riderFile(ElionsManager elionsManager, String dir, String produkUtama, Map m) {
		String bisnis = (String) m.get("BISNIS");
		String detbisnis = (String) m.get("DETBISNIS");
		//SPECIAL CASES
		if(produkUtama.equals("134")) { //PLATINUM LINK
			if("807, 811, 813".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-PLATINUM LINK.pdf");
			}else if("814, 816".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+"-PLATINUM LINK.pdf");
			}
		}else if(produkUtama.equals("140")) { //MEDIVEST
			return new File(dir+"/RIDER/"+bisnis+"-MEDIVEST.pdf");
		}else if(produkUtama.equals("141")) { //EDUVEST, GAK ADA RIDER LAGI, UDAH GABUNG 1 FILE
			return null;
		}else if(produkUtama.equals("160")) { //EKALINK SYARIAH, RIDERNYA BEDA SENDIRI
			if("810, 812, 813".indexOf(bisnis) > -1) {
				return new File(dir+"/RIDER/"+bisnis+"-EKALINK SYARIAH.pdf");
			}
		}
		return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
	}
	*/
	
	private static File tambahanFile(ElionsManager elionsManager, String dir, String produkUtama, Map m) {
		String bisnis = (String) m.get("BISNIS");
		String detbisnis = (String) m.get("DETBISNIS");
		//SPECIAL CASES
		if((produkUtama.equals("053"))||(produkUtama.equals("054"))||(produkUtama.equals("131"))||(produkUtama.equals("132"))) { //Super Sehat Plus
			return new File(dir+"/RIDER/Dftr_Pembedahan.pdf");
//			}
		}
		return null;
		//return new File(dir+"/RIDER/"+bisnis+"-"+detbisnis+".pdf");
	}
	
	public static boolean checkFileProduct(ElionsManager elionsManager, UwManager uwManager, Properties props, String spaj) {
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String produkUtama = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String detProdukUtama = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		String dir = props.getProperty("pdf.dir.syaratpolis");
		for(int i=0; i<detBisnis.size(); i++) {
			Map m = (HashMap) detBisnis.get(i);
			File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
			if(file!=null) if(file.exists()) return true;
			file = riderFile(elionsManager, uwManager,dir, produkUtama, detProdukUtama, m, spaj ,props);
			if(file!=null) if(file.exists()) return true;
			file = tambahanFile(elionsManager, dir, produkUtama, m);
			if(file!=null) if(file.exists()) return true;
		}
		
		return false;
	}
	
	public List listFileProduct(String spaj) {
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		/*
		//DIAN --- NGETEST 819 RIDER
		Map temp=new HashMap();
		temp.put("BISNIS", "819");
		temp.put("DETBISNIS","001");
		detBisnis.add(temp);
		*/
		String produkUtama = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String detProdukUtama = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		String dir = props.getProperty("pdf.dir.syaratpolis");
		List<File> hasil = new ArrayList<File>();
		Integer flag1 = 0;
		Integer flag2 = 0;
		Integer flag3 = 0;
		for(int i=0; i<detBisnis.size(); i++) {
			Map m = (HashMap) detBisnis.get(i);
			String bisnis = (String) m.get("BISNIS");
			if ( bisnis.equals("819")){
				if (produkUtama.equals("120")){
					File file = riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
					if(file!=null) if(file.exists()) hasil.add(file);
				}else{
						boolean flag=true;
				File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
				for(File namaFile : hasil)
		        	if(namaFile.toString().contains("HCPFamily.pdf")) flag=false;			
				if(file!=null) if(file.exists()) if(flag==true) hasil.add(file);
				file = riderFile(elionsManager, uwManager,dir, produkUtama, detProdukUtama, m,spaj, props);
				if(file!=null) if(file.exists()) if(flag==true) hasil.add(file);
				}
			}else if(bisnis.equals("820") || bisnis.equals("823") || bisnis.equals("825") || bisnis.equals("848")){
				flag1= flag1+1;
				if (flag1.equals(1)){
					File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
					if(file!=null) if(file.exists()) hasil.add(file);
					file = riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
					if(file!=null) if(file.exists()) hasil.add(file);
				}
			}else if(bisnis.equals("832") || bisnis.equals("833")){
				flag2 = flag2+1;
				if (flag2.equals(1)){
					File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
					if(file!=null) if(file.exists()) hasil.add(file);
					file = riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
					if(file!=null) if(file.exists()) hasil.add(file);
				}
			}else if(bisnis.equals("831")){
				flag3 = flag3+1;
				if (flag3.equals(1)){
					File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
					if(file!=null) if(file.exists()) hasil.add(file);
					file = riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
					if(file!=null) if(file.exists()) hasil.add(file);
				}
			}else{ 
				File file = productFile(elionsManager, uwManager, dir, spaj, m,props);
				if(file!=null) if(file.exists()) hasil.add(file);
				file = riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m, spaj,props);
				if(file!=null) if(file.exists()) hasil.add(file);
				file = tambahanFile(elionsManager, dir, produkUtama, m);
				if(file!=null) if(file.exists()) hasil.add(file);
			}
		}
		return hasil;
	}
	
	@Override
	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		ServletContext servletContext = getServletContext();
		this.elionsManager = (ElionsManager) Common.getBean(servletContext, "elionsManager");
		this.uwManager = (UwManager) Common.getBean(servletContext, "uwManager");
		this.bacManager = (BacManager) Common.getBean(servletContext, "bacManager");
		this.props = (Properties) Common.getBean(getServletContext(), "ekaLifeConfigurations");	
		this.products = (Products) Common.getBean(servletContext, "products");
		
		return PdfWriter.getInstance(document, os);
	}
	
	@Override
	protected void prepareWriter(Map model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
		writer.setEncryption(true, null, null, PdfWriter.AllowPrinting);
	}
	
	@Override
	protected void buildPdfDocument(Map map, Document document, PdfWriter writer, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//Jenis2 PDF yang dihasilkan : 
		Integer jenisDokumen = (Integer) map.get("jenisDokumen");
		Boolean isViewer = (Boolean) map.get("isviewer");
		if(isViewer==null){
			isViewer=false;
		}
		
		//Error, masukkan parameter jenisDokumen pada map
		if(jenisDokumen==null) {
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Maaf, telah terjadi kesalahan pada aplikasi. Harap hubungi EDP.');</script>");
			out.flush();
			return;
		//Syarat Umum / Khusus pada Print Polis
		}else if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS || jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS_EAS) {
			String spaj = map.get("spaj").toString();
			List list = listFileProduct(spaj);
			
			if(list.size()==0) {
				if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS){
					ServletOutputStream out = response.getOutputStream();
					out.println("<script>alert('Tidak ada file Syarat-syarat Umum / Khusus.');</script>");
					out.flush();
					return;
				}
			}else {
				if(isViewer==false){
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "inline;filename=file.pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					showPdf(spaj, list, response.getOutputStream(), document, writer, jenisDokumen,isViewer);
				}else{
					String namaFile= "";
					if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS){
			        	namaFile = props.getProperty("pdf.ssu");
			        }else if(jenisDokumen == PrintPolisMultiController.SYARAT_KHUSUS){
			        	namaFile = props.getProperty("pdf.ssk"); 
			        }else if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS_EAS){
			        	namaFile = props.getProperty("pdf.ssu"); 
			        }else if(jenisDokumen == PrintPolisMultiController.PANDUAN_VIRTUAL_ACCOUNT){
			        	namaFile = "panduan_virtual_account"; 
			        }
					String cabang = elionsManager.selectCabangFromSpaj(spaj);
					File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+namaFile+".pdf");
			        if(!userDir.exists()) {
			        	ServletOutputStream out = response.getOutputStream();
						out.println("<script>alert('Tidak ada Syarat-syarat Umum / Khusus.Silakan diprint terlebih dahulu');</script>");
						out.flush();
						return;
			        }else{
			        	String tampung = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\";
						String file_name = props.getProperty("pdf.ssu")+".pdf";
						List<DropDown> daftarFile = FileUtils.listFilesInDirectory(tampung);
						FileUtils.downloadFile("inline;", tampung, file_name, response);
						return;

			        }
				}
			}
		//Syarat Khusus untuk swine flu pada Print Polis
		}else if(jenisDokumen == PrintPolisMultiController.SYARAT_KHUSUS) {
			String spaj = map.get("spaj").toString();
			
			List<File> hasil = new ArrayList<File>();
			String dir = props.getProperty("pdf.dir.syaratpolis");
			
			hasil.add(new File(dir + "\\RIDER\\822-001-STABLE.pdf"));
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline;filename=file.pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			showPdf(spaj, hasil, response.getOutputStream(), document, writer, jenisDokumen,isViewer);
			
			
		//Polis DM/TM
		}else if(jenisDokumen == PrintPolisMultiController.POLIS_DMTM) {
			String spaj = map.get("spaj").toString();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline;filename=polis.pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			
	        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }

			FileOutputStream fileOutput = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.polis_dmtm")+".pdf");
			PdfCopy pdfCopy = null;
			pdfCopy = new PdfCopy(document, fileOutput);
			
			document.open();
			writer.addJavaScript("this.print(true);", false);

			//pdf template produk DM/TM
			String dir = props.getProperty("pdf.dir.syaratpolis");
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			if (!lsbs.equals("142") && !lsdbs.equals("008")){
			
			PdfReader reader = new PdfReader(dir + "/" + lsbs + "-" + lsdbs + "-" + map.get("tipe") + ".pdf");
			
	        PdfContentByte cb = writer.getDirectContent();
	        for (int j=1; j <= reader.getNumberOfPages(); j++){
	        	//1. gabungkan PDF nya
	        	document.newPage();
	        	cb.addTemplate(writer.getImportedPage(reader, j), 0,0);
	        	//2. simpan ke file
                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
                
                //Add Contents hanya pada Halaman 1
                if(j == 1) {
                	cb.beginText();
                	BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	
                	cb.setFontAndSize(bf, 12);
                	cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS ASURANSI JIWA PERORANGAN POWER SAVE", 299, 744, 0);

                	bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	cb.setFontAndSize(bf, 9);

                	//Kode Bea Meterai
                	String meterai = elionsManager.selectIzinMeteraiTerakhir();
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, meterai,	 							426, 767, 0);
                	
                	//Bagian Label
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pemegang Polis", 					20, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Umur", 								20, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 681, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 681, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tertanggung", 						308, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Umur", 								308, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						308, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Besarnya Premi Sekaligus", 			308, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Garansi Investasi (MGI)", 		308, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tingkat Investasi Pada MGI Pertama", 308, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nilai Tunai Akhir MGI Pertama", 		308, 633, 0);
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							458, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 633, 0);
                	
                	//Bagian Kiri
                	Map m = elionsManager.selectPolisPowersaveDMTM(spaj);
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	100, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("PP_NAMA"), 				100, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("PP_UMUR") + " Tahun",	100, 693, 0);
                	
                	//Bagian Alamat Rumah
                	int monyong = 0;
//                	String[] alamat = StringUtil.pecahParagraf((String) m.get("ALAMAT_RUMAH"), 32);
//                	for(int i=0; i<alamat.length; i++) {
//                		monyong = 12 * i;
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							100, 681-monyong, 0);
//                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("KOTA_RUMAH"), 			100, 669-monyong, 0);

                	//Bagian Kanan
                	JasperScriptlet jasper = new JasperScriptlet();
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("TT_NAMA"), 463, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("TT_UMUR") + " Tahun", 463, 705, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MASA_ASURANSI"), 463, 693, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_TSI")), 463, 681, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM")), 463, 669, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_JANGKA_INV") + " Bulan", 463, 657, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_RATE") + "% Per Tahun", 463, 645, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("NILAI_TUNAI")), 463, 633, 0);
                	
                	
                	cb.endText();
                }
	        }
			}

			pdfCopy.close();
		}else if(jenisDokumen == PrintPolisMultiController.POLIS_PAS || jenisDokumen == PrintPolisMultiController.POLIS_TERM) {
			String spaj = map.get("spaj").toString();

			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			List rider = elionsManager.selectRiderPolisPas(spaj);
			Date sysdate = elionsManager.selectSysdate();
	        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }

			List<String> pdfs = new ArrayList<String>();
			boolean suksesMerge = false;
			String PolisSSU = "";
			String SSK = "";
			
			document.open();
			writer.addJavaScript("this.print(true);", false);

			String dir = props.getProperty("pdf.dir.syaratpolis");
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			
			PolisSSU = dir + "\\" + lsbs + "-" + lsdbs +".pdf";
			pdfs.add(PolisSSU);
			if(rider.size()>0){
				for(int i=1;i<detBisnis.size();i++){
					String lsbs_rider = (String) ((Map) detBisnis.get(i)).get("BISNIS");
					String lsdbs_rider = (String) ((Map) detBisnis.get(i)).get("DETBISNIS");
					SSK = dir + "\\" +"RIDER"+"\\"+ lsbs_rider + "-" + lsdbs_rider +".pdf";
					pdfs.add(SSK);
				}
			}
			String hamid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
			OutputStream output = null; 
			if(lsbs.equals("196")){
				output = new FileOutputStream(props.getProperty("pdf.template.term")+"\\MergeSSUSSK-TERM.pdf");
			}else if (lsbs.equals("073") && lsdbs.equals("008")){
				output = new FileOutputStream(props.getProperty("pdf.template.pa")+"\\MergeSSUSSK-PA.pdf");
			}
			else{
				output = new FileOutputStream(props.getProperty("pdf.template.pas")+"\\MergeSSUSSK.pdf");
			}
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			
			if(!suksesMerge) {
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf, telah terjadi kesalahan pada aplikasi. Harap hubungi EDP.');</script>");
				out.flush();
				return;
			}
			
			File file = null;
			PdfStamper stamp = null;
			String outputName ="";
			String fileDir =props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\";
			String fileName =props.getProperty("pdf.polis_pas")+".pdf";
			if(lsbs.equals("196")){
				PdfReader reader = new PdfReader(props.getProperty("pdf.template.suryamas")+"\\MergeSSUSSK-TERM.pdf");
				fileName=props.getProperty("pdf.polis_term")+".pdf";
				outputName = fileDir+fileName;
				file = new File(outputName);
				stamp = new PdfStamper(reader,new FileOutputStream(file));
			}else if(lsbs.equals("073") && lsdbs.equals("008")){
				PdfReader reader = new PdfReader(props.getProperty("pdf.template.suryamas")+"\\MergeSSUSSK-PA.pdf");
				fileName=props.getProperty("pdf.polis_pa")+".pdf";
				outputName = fileDir+fileName;
				file = new File(outputName);
				stamp = new PdfStamper(reader,new FileOutputStream(file));
			}else if(lsbs.equals("205")){
				if(lsdbs.equals("005") || lsdbs.equals("006") ||  lsdbs.equals("007") || lsdbs.equals("008")){
					PdfReader reader = new PdfReader(props.getProperty("pdf.dir.syaratpolis")+"\\"+lsbs+"-"+lsdbs+".pdf");
					fileName = "polis_pas.pdf";
					outputName = fileDir+fileName;
					file = new File(outputName);
					stamp = new PdfStamper(reader,new FileOutputStream(file));
				}else{
					PdfReader reader = new PdfReader(props.getProperty("pdf.template.pas")+"\\MergeSSUSSK-SYH.pdf");
	//				fileName=props.getProperty("pdf.polis_pasyariah")+".pdf";
					fileName = "polis_pas.pdf";
					outputName = fileDir+fileName;
					file = new File(outputName);
					stamp = new PdfStamper(reader,new FileOutputStream(file));
				}
			}
			else{
				PdfReader reader = new PdfReader(props.getProperty("pdf.template.pas")+"\\MergeSSUSSK.pdf");
				outputName = fileDir+fileName;
				file = new File(outputName);
				stamp = new PdfStamper(reader,new FileOutputStream(file));
			}
			
	        PdfContentByte cb = writer.getDirectContent();
	        		cb = stamp.getOverContent(1);
                	cb.beginText();
                	//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	BaseFont arial = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	
                	BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	BaseFont arialbd = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALBD.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//                	BaseFont arial_narrow = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        			//BaseFont arial_narrow_bold = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                		cb.setFontAndSize(arial, 12);
			    	}else{
			    		cb.setFontAndSize(bf, 12);
			    	}
                	cb.setFontAndSize(bf, 12);
                	int penambahYdetail=0;
                	int penambahYdetailextra=0;
                	int pengurangXdetail=0;
                	int penambahXdetail=0;
                	if(lsbs.equals("196")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS TERM INSURANCE", 299, 756, 0);
                		penambahYdetail=12;
                		penambahYdetailextra=24;
                		pengurangXdetail=57;
                	}else if(lsbs.equals("073") && lsdbs.equals("008")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT INSURANCE (RISIKO A)", 299, 756, 0);
                		penambahYdetail=12;
                		penambahYdetailextra=24;
                		pengurangXdetail=57;
                	}else if(lsbs.equals("205")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE SYARIAH (PAS SYARIAH)", 299, 756, 0);
                	}else if(lsbs.equals("187") && (lsdbs.equals("11") || lsdbs.equals("12") || lsdbs.equals("13") || lsdbs.equals("14"))){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE SMART ACCIDENT CARE", 299, 756, 0);
                	}else if(lsbs.equals("073") && lsdbs.equals("015")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS NISSAN PA", 299, 756, 0);
                	}else if(lsbs.equals("203") && lsdbs.equals("004")){
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS NISSAN DBD", 299, 756, 0);
                	}
                	else{
                		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "POLIS PERSONAL ACCIDENT SINARMASLIFE (PAS)", 299, 756, 0);
                	}

                	//bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                	bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALN.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                	if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                		cb.setFontAndSize(arial, 8);
			    	}else{
			    		cb.setFontAndSize(bf, 8);
			    	}
                	

                	//Kode Bea Meterai
//                	String meterai = elionsManager.selectIzinMeteraiTerakhir();
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, meterai,	 							426, 767, 0);
                	
                	if(!products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Kartu", 							20, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mulai Asuransi", 					20, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nama Paket", 						308, 705, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445, 705, 0);
                	}
                	
                	//Bagian Label
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "No. Polis", 							20, 717+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Pemegang Polis", 					20, 705+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					20, 693+penambahYdetail, 0);

                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 717+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",				 					95, 705+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 693+penambahYdetail, 0);
                	if(lsbs.equals("205")){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Peserta", 						308, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Kontribusi/Premi", 								308, 681+penambahYdetail, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 658+penambahYdetailextra, 0);
                		int monyong = 0;
                		int monyong2 = 0;
                		Map m = new HashMap();
                		m = uwManager.selectPolisBiasa(spaj);
                		String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT")+"\n"+(String) m.get("KODE_POS"), 32);
                		for(int i=0; i<alamat.length; i++) {
                    		monyong2 = 12 * i;
                    		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			100, 658-monyong2+penambahYdetailextra, 0);
                    	}
                	}else{
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tertanggung", 						308, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Premi", 								308, 681+penambahYdetail, 0);
                	}
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Lahir/Umur", 					308, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Uang Pertanggungan", 				308, 693+penambahYdetail, 0);
                	if(rider.size()>0){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Asuransi Tambahan", 					308, 669+penambahYdetail, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tingkat Investasi Pada MGI Pertama", 308, 645, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Nilai Tunai Akhir MGI Pertama", 		308, 633, 0);
                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445-pengurangXdetail, 669+penambahYdetail, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 645, 0);
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									458, 633, 0);
                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":",		 							445-pengurangXdetail, 729, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445-pengurangXdetail, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445-pengurangXdetail, 693+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445-pengurangXdetail, 681+penambahYdetail, 0);
                	
                	if(lsbs.equals("196")||lsbs.equals("073") && lsdbs.equals("008")){
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Masa Asuransi", 						308, 669+penambahYdetail, 0/*681+penambahYdetail, 0*/);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	    450-pengurangXdetail, 669+penambahYdetail, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tgl Mulai Asuransi", 			    308, 657+penambahYdetail, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Alamat", 							20, 668+penambahYdetailextra, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 								    445-pengurangXdetail, 657+penambahYdetail, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									445-pengurangXdetail, 669+penambahYdetail, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ":", 									95, 668+penambahYdetailextra, 0);
                	}
                	
                	//Bagian Kiri
                	Map m = new HashMap();
                	if(!products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                		m = elionsManager.selectPolisPas(spaj);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, StringUtil.nomorPAS((String) m.get("NO_KARTU")), 	101, 729, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	101, 669, 0);
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("PAKET"), 451, 705, 0);
                	}else{
                		int monyong = 0;
                		int monyong2 = 0;
                		m = uwManager.selectPolisBiasa(spaj);
                		String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
                		for(int i=0; i<alamat.length; i++) {
                    		monyong2 = 12 * i;
                    		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			100, 668-monyong2+penambahYdetailextra, 0);
                    	}
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("MSPR_BEG_DATE")),	451-pengurangXdetail, 657+penambahYdetail, 0);
                	}
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("MSPO_POLICY_NO_FORMAT"), 	101, 717+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_PP"), 				101, 705+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_PP"))+"/ "+ (BigDecimal) m.get("USIA_PP") + " Tahun",	101, 693+penambahYdetail, 0);
                	//cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "1 tahun dan dapat diperpanjang",	101, 681+penambahYdetail, 0);
                	
                	//Bagian Alamat Rumah
                	/*int monyong = 0;
//                	String[] alamat = StringUtil.pecahParagraf((String) m.get("ALAMAT_RUMAH"), 32);
//                	for(int i=0; i<alamat.length; i++) {
//                		monyong = 12 * i;
//                    	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							100, 681-monyong, 0);
//                	}
                	int monyong2 = 0;
                	String[] alamat = StringUtil.pecahParagrafLineBreaksInclude((String) m.get("ALAMAT"), 32);
                	for(int i=0; i<alamat.length; i++) {
                		monyong2 = 12 * i;
                		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 647-monyong2+penambahYdetailextra, 0);
                	}
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i] , 			20, 645, 0);
*/
                	//Bagian Kanan
                	JasperScriptlet jasper = new JasperScriptlet();
                	
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (String) m.get("NAMA_TT"), 451-pengurangXdetail, 729, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) m.get("TGL_LAHIR_TT"))+"/ "+ (BigDecimal) m.get("USIA_TT") + " Tahun", 451-pengurangXdetail, 717, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrencyNoDigit((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_TSI"))+",-", 451-pengurangXdetail, 694+penambahYdetail, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrencyNoDigit((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("MSPR_PREMIUM"))+",-"+" " +m.get("LSCB_PRINT").toString().toLowerCase(), 451-pengurangXdetail, 681+penambahYdetail, 0);
                	if(rider.size()>0){
	                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "ASURANSI DEMAM BERDARAH", 451-pengurangXdetail, 669+penambahYdetail, 0);
	                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "- Premi : Gratis", 456-pengurangXdetail, 657+penambahYdetail, 0);
                	}
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, (BigDecimal) m.get("MPS_RATE") + "% Per Tahun", 463, 645, 0);
//                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency((String) m.get("LKU_SYMBOL"), (BigDecimal) m.get("NILAI_TUNAI")), 463, 633, 0);
                	int penambahYFooter=0;
                	String direksi ="Chief Operating & IT Officer";
                	if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
                		penambahYFooter=30;
                		direksi="Chief Operating & IT Officer";
                	}
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jakarta, " + FormatDate.toIndonesian(sysdate), 451-pengurangXdetail, 607+penambahYFooter, 0);
                	cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "PT Asuransi Jiwa Sinarmas MSIG Tbk.", 451-pengurangXdetail, 595+penambahYFooter, 0);
            			
//            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Hamid Hamzah", 451-pengurangXdetail, 535+penambahYFooter, 0);
            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Andrew Bain", 451-pengurangXdetail, 535+penambahYFooter, 0);
            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "__________", 451-pengurangXdetail, 535+penambahYFooter, 0);
            		if(products.productChannel88(Integer.parseInt(lsbs), Integer.parseInt(lsdbs))){
            			cb.setFontAndSize(arialbd, 8);
                    }
            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, direksi, 451-pengurangXdetail, 526+penambahYFooter, 0);
                	
                	cb.endText();
                	
                	Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.direksi").trim()).getAbsolutePath());
    				
    				img.setAbsolutePosition(450-pengurangXdetail, 545+penambahYFooter);		
    				img.scaleAbsolute(120, 34);
    				cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 450-pengurangXdetail, 545+penambahYFooter);
    				cb.stroke();
//                }
//	        }
            stamp.close();
            File l_file = new File(outputName);
            FileUtil.downloadFile(fileDir, fileName, response, "inline");
            
		}else if(jenisDokumen == PrintPolisMultiController.ENDORS_EKASEHAT_ADMEDIKA){
			String spaj = map.get("spaj").toString();
			
			PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
			List<String> pdfs = new ArrayList<String>();
			boolean suksesMerge = false;
			boolean scFile=false;
			String endorsPolis = "";
			String Kartuadmedika = "";
			String Pesertaadmedika = "";
			String provider = "";
			String filename = "Admedika";
			Date sysdate = elionsManager.selectSysdate();
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			int lsbs_rider = bacManager.selectRiderMedicalExtra(spaj);
			Boolean syariah = products.syariah(lsbs, lsdbs);
			
			Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj,0);
			Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
			Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj,1);
			//Integer s=Integer.parseInt(lsdbs.substring(1));
			Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);
			
			if(punyaEndorsAdmedika == 0)bacManager.prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs));
			if(ekaSehatBaru>=1){
				if(lsbs.equals("189")|| products.syariah(lsbs, lsdbs)){
					if(lsbs.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf"; //gantinttd
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf"; //gantinttd
					}
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru //gantinttd
				}
			}else if(ekaSehatPlus>=1){				
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf"; //gantinttd
			}else{
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf"; //gantinttd
			}
			
			if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
				Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
				provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
			}else{
				if(lsbs.equals("189")|| products.syariah(lsbs, lsdbs)){
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedikaSyariah.pdf";
				}else{
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
				}
				provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
			}
			Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
			
			if (ekaSehatBaru>=1 || ekaSehatPlus>=1 ){
			// buka ya kalo di perlukan patar timotius	pdfs.add(endorsPolis); 
				pdfs.add(Kartuadmedika);
			}
			//pdfs.add(Kartuadmedika);
			Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
			/**(Deddy)
			 * Comment ini dibuka apabila ada request special case(admedika blum print kartu peserta)
			 */
//			for(int i=0; i<total_ekasehat;i++){
//				pdfs.add(Pesertaadmedika);
//			}
			
			List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
			if(ekaSehatBaru>=1){
				
			}else{
				if(lsbs_rider > 0) //rider smile medical extra cuma perlu kartu admedika aja //chandra
					pdfs.add(Kartuadmedika);
				else
					pdfs.add(provider);
			}
			String newFix = "";
			if(ekaSehatHCP>=1){
				newFix = props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf";
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(lsbs.equals("189") || products.syariah(lsbs, lsdbs)){
				newFix =props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf";
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(ekaSehatPlus>=1){
				newFix =props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf";
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else{
				// Andhika
				newFix =props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf";
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}
			
			
			if(!suksesMerge) {
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf, telah terjadi kesalahan pada aplikasi. Harap hubungi EDP.');</script>");
				out.flush();
				return;
			}
			
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }

	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+filename+spaj+".pdf";
			
			if(!scFile) {
				Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
				String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
				/**
				 * buka ya jika diperlukan
				 * patar timotius
				 * printPolis.generateEndorseAdmedikaEkaSehatFix(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus,props);
				 */
				outputName = newFix;
			}
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;	
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline;filename="+filename+spaj+".pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}finally{
	            try {
	            	if(in != null) {
	            		in.close();
	            	}
	            	if(ouputStream != null) {
	            		ouputStream.flush();
	            		ouputStream.close();
	            	} 
	            } catch (Exception e) {
	            	logger.error("ERROR", e);
	            }
	            
		        Table table = new Table(1);
		        document.add(table);
			}
			
			
			
			
		//Panduan Virtual Account
		}else if(jenisDokumen == PrintPolisMultiController.PANDUAN_VIRTUAL_ACCOUNT) {
			String spaj = map.get("spaj").toString();
			List list = new ArrayList();
			String dir = props.getProperty("pdf.dir.other");
			File f = new File(dir+"/PANDUAN_VIRTUAL_ACCOUNT.pdf");
			list.add(f);
			
			if(list.size()==0) {
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Tidak ada file Panduan Rekening Billing.');</script>");
				out.flush();
				return;
			}else {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline;filename=file.pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				showPdf(spaj, list, response.getOutputStream(), document, writer, jenisDokumen,isViewer);
				
			}
		}else if(jenisDokumen == PrintPolisMultiController.SURAT_PENJAMINAN_PROVIDER) {
			
			try{
				
				String reg_spaj = map.get("spaj").toString();
				String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
				String fileDir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\";
				String fileName ="SURAT_PENJAMINAN_PROVIDER.pdf";
				
				File userDir = new File(fileDir+fileName);
		        if(!userDir.exists()) {
		        	 File file = bacManager.generateSuratPengajuanProvider(elionsManager,uwManager, reg_spaj, props);	
			    }
				
	           FileUtil.downloadFile(fileDir, fileName, response, "inline");
			}catch(Exception e){
				logger.error("ERROR :", e);
			}
		}else if(jenisDokumen == PrintPolisMultiController.SERTIFIKAT_TERM_ROP){
			String reg_spaj = map.get("spaj").toString();
			String lsbs = map.get("lsbs").toString();
			String detbisnis = map.get("detbisnis").toString();
			String lus_id = map.get("lus_id").toString();
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
	        String path_sertifikat = "path_sertifikat.pdf";
	        String outputName = "SERTIFIKAT.pdf";
	        
	        if( (lsbs.equals("197") && detbisnis.equals("002")) ) {
	        	Map m = PDFViewer.genItextTemplate1(elionsManager, bacManager, props, path_sertifikat, false, 0, reg_spaj, lsbs, detbisnis);
			}else if( (lsbs.equals("212") && detbisnis.equals("008")) ) {
				Map m = PDFViewer.genItextTemplate1(elionsManager, bacManager, props, path_sertifikat, false, 1, reg_spaj, lsbs, detbisnis);
			}else {
				Map m = genItextTemplate1(elionsManager, bacManager, props, path_sertifikat, true, 1, reg_spaj, lsbs, detbisnis);
			}
	        
	        //untuk generate nilai tunai
	        Map params = this.elionsManager.prosesCetakManfaat(reg_spaj, lus_id, request);
	        
			String path_manfaat = "path_manfaat.pdf";
			Report report = new Report("Manfaat Sertifikat", props.getProperty("report.manfaat.manfaat_sertifikat"), Report.PDF, null);	
			if(("212".equals(lsbs) && ("008".equals(detbisnis) || "006".equals(detbisnis))) || ("223".equals(lsbs) && Integer.parseInt(detbisnis) == 1)){
				 report = new Report("Manfaat Sertifikat", props.getProperty("report.manfaat.manfaat_sertifikat_2"), Report.PDF, null);	
			}
			Connection conn = null;
			try {
				conn = uwManager.getUwDao().getDataSource().getConnection();
//				params.put("footer_kiri", 6);
//				params.put("footer_kanan", 6);
				
				JasperUtils.exportReportToPdfNoLock(report.getReportPath()+".jasper", exportDirectory, path_manfaat, params, conn);
			}catch(Exception e){
	            throw e;
			}finally{
				closeConnection(conn);
			}
	        
			PDFMergerUtility before = new PDFMergerUtility();
	        before.addSource(exportDirectory+"\\"+path_sertifikat);
	        before.addSource(exportDirectory+"\\"+path_manfaat);
	        before.setDestinationFileName(exportDirectory+"\\"+outputName);
	        before.mergeDocuments();
	        deleteAllFile(new File(exportDirectory));
	        
//			File l_file = new File(outputName);
//			try{
//				FileInputStream in = new FileInputStream(l_file);
//			    int length = in.available();
//			    byte[] pdfbytes = new byte[length];
//			    in.read(pdfbytes);
//			    in.close();
//
//			}catch (Exception e) {
//				logger.error("ERROR :", e);
//			}
	        FileUtil.downloadFile(exportDirectory, outputName, response, "inline");
	        
	        //andy: tambahan wajib biar view pdf ga error
	        Table table = new Table(1);
	        document.add(table);
		}
	}
	
	protected void closeConnection(Connection conn){
		try {
            conn.close();
        } catch (SQLException e) { /* ignored */}
	}
	
    private Boolean deleteAllFile(File pFile){
        if(pFile.exists()){
            if(pFile.isDirectory()){
                if(pFile.list().length > 0){
                    String[] strFiles = pFile.list();
                    for(String strFileName : strFiles){
                        if(strFileName.contains("path") || strFileName.contains("TP.pdf")){
                             File fileToDelete = new File(pFile, strFileName);
                             deleteAllFile(fileToDelete);
                        }
                    }
                }
            }
        }
        return pFile.delete();
    }
	
	public static Map genItextTemplate1(ElionsManager elionsManager, BacManager bacManager, Properties props, String fileName, boolean sertificateFlag, int benefitType, String spaj, String lsbs_id, String lsdb_number) throws DocumentException, IOException {
		HashMap m = new HashMap();
		String reg_spaj = spaj;
		String lsbs = lsbs_id;
		String detbisnis = lsdb_number;
		
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String wakil = bacManager.selectWakilFromSpaj(reg_spaj);
		//String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
		
		File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
        if(!userDir.exists()) {
            userDir.mkdirs();
        }
        
		
		PdfContentByte over;
		BaseFont fonts = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//		Font font = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
		
		if(StringUtil.isEmpty(fileName)) {
			fileName = "SERTIFIKAT.pdf";
		}
		
		String pathFile = props.getProperty("pdf.dir.syaratpolis")+"\\"+lsbs+"-"+FormatString.rpad("0", String.valueOf(detbisnis), 3)+"-NT.pdf";
//        PdfReader reader = new PdfReader(props.getProperty("pdf.dir")+"\\ss_smile\\"+lsbs+"-"+FormatString.rpad("0", detbisnis, 3)+".pdf");
        PdfReader reader = new PdfReader(pathFile);
        int pages = reader.getNumberOfPages();
        String hal = "1-" + (pages-1);
		
        reader.selectPages(hal);
        
        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+fileName;
        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
        
        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
        Datausulan dataUsulan = elionsManager.selectDataUsulanutama(reg_spaj);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        String alamat_1 = "";
        String alamat_2 = "";
        String no_sertifikat = "";
        try {
        	if(sertificateFlag) {
		        no_sertifikat = bacManager.selectNoSertifikat(reg_spaj);
		        
		        if("".equals(no_sertifikat) || null == (no_sertifikat)){
		        	String lsbsId_3dg = FormatString.rpad("0", lsbs.toString(), 3);
		        	no_sertifikat = bacManager.selectSertifikatTemp(cabang, wakil, lsbsId_3dg);
		        	
		        	HashMap param = new HashMap();
		        	param.put("name_pp", dataPP.getMcl_first());
		        	param.put("bod_pp", sdf.format(dataPP.getMspe_date_birth()));
		        	param.put("age_pp", dataPP.getMspo_age());
		        	param.put("begdate", sdf.format(dataUsulan.getMste_beg_date()));
		        	param.put("insperiod", dataUsulan.getMspo_ins_period());
		        	param.put("address", dataPP.getKota_rumah());
		        	param.put("name_tt", dataTT.getMcl_first());
		        	param.put("bod_tt", sdf.format(dataTT.getMspe_date_birth()));
		        	param.put("age_tt", dataTT.getMste_age());
		        	param.put("lsbs_id", lsbs);
		        	param.put("lsdbs_number", detbisnis);
		        	param.put("premi", dataUsulan.getMspr_premium());
		        	param.put("up", dataUsulan.getMspr_tsi());
		        	param.put("msag_id", dataPP.getMsag_id());
		        	param.put("no_policy", no_sertifikat);
		        	param.put("reg_spaj", reg_spaj);
		        	
		        	bacManager.insertMstSpajCrt(param);
		        }
	        }else {
	        	no_sertifikat = elionsManager.selectPolicyNumberFromSpaj(reg_spaj);
	        }
        	
        	if(sertificateFlag && no_sertifikat == null) {
        		no_sertifikat = elionsManager.selectPolicyNumberFromSpaj(reg_spaj);
        	}
        	
        	JasperScriptlet jasper = new JasperScriptlet();
        	
            if (dataPP.getAlamat_rumah().length() > 46) {
                alamat_1 = dataPP.getAlamat_rumah().substring(0, 45);
                alamat_2 = dataPP.getAlamat_rumah().substring(46, dataPP.getAlamat_rumah().length());
            } else {
                alamat_1 = dataPP.getAlamat_rumah();
            }
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(fonts,8f);
			
			String tglbdate = sdf.format(dataUsulan.getMste_beg_date());
			String tgledate = sdf.format(dataUsulan.getMste_end_date());
			
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 115, 744, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toString(), 115, 730, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, sdf.format(dataPP.getMspe_date_birth())  + " / " + dataPP.getMspo_age() + " Tahun", 115, 715, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 115, 700, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate + " s/d " + tgledate, 115, 683, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_1, 115, 670, 0);
            if (alamat_2.equals("")) {
            	if(dataPP.getKd_pos_rumah()==null || "".equals(dataPP.getKd_pos_rumah()) ){
            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah(), 115, 660, 0);
            	}else{
            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah() + ", " + dataPP.getKd_pos_rumah(), 115, 660, 0);
            	}
            } else {
            	if(dataPP.getKd_pos_rumah()==null || "".equals(dataPP.getKd_pos_rumah()) ){
            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_2, 115, 660, 0);
	            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah(), 115, 650, 0);
            	}else{
	            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_2, 115, 660, 0);
	            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah() + ", " + dataPP.getKd_pos_rumah(), 115, 650, 0);
            	}

            }

            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first(), 398, 744, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, sdf.format(dataTT.getMspe_date_birth())  + " / " + dataTT.getMste_age() + " Tahun", 398, 730, 0);
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_depkeu(), 398, 715, 0);
            
            String cbyr = "";
            int faktor = 1;
            if(dataUsulan.getLscb_id()==1){
            	cbyr = "triwulan";
            	faktor = 3;
            }else if(dataUsulan.getLscb_id()==2){
            	cbyr = "semester";
            	faktor = 6;
            }else if(dataUsulan.getLscb_id()==3){
            	cbyr = "tahun";
            	faktor = 12;
            }else if(dataUsulan.getLscb_id()==6){
            	cbyr = "bulan";
            }
            
            over.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_premium())) + ",- per "+cbyr, 398, 700, 0);
            if(benefitType == 0) {
            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_tsi())), 398, 685, 0);
            }else {
            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dibayarkan sekaligus " + jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_tsi())) + "; Atau", 398, 685, 0);
                over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dibayarkan bulanan " + jasper.formatCurrency("Rp ",BigDecimal.valueOf((dataUsulan.getMspr_premium()*10)/faktor)) + " selama 5 tahun", 398, 673, 0);
            }
            
            if("223".equals(lsbs) && "002".equals(detbisnis)){ //helpdesk [138638] produk baru SLP Syariah (223-2)
                over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 435, 632, 0);
            }else if ("212".equals(lsbs) && "006".equals(detbisnis)){ //NCR/2020/10/038
            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 435, 632, 0);
            }else{
            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 433, 659, 0);            	
            }
            
		    String ttd = Resources.getResourceURL(props.getProperty("images.ttd.direksi")).getPath();
		    Image img = Image.getInstance(ttd);					
			img.setAbsolutePosition(380, 300);
//			img.scaleAbsolute(90, 34);
			img.scalePercent(20);//NCR/2020/10/038
			if("223".equals(lsbs) && "001".equals(detbisnis)){
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 616);
			}else if("223".equals(lsbs) && "002".equals(detbisnis) || "212".equals(lsbs) && "006".equals(detbisnis)){ //helpdesk [138638] produk baru SLP Syariah (223-2) //NCR/2020/10/038
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 589);
			}else{
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 595);
			}
            
            over.endText();
            stamp.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
			FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj,fileName);
        } 
        
        return m;
	}

	private void showPdf(String spaj, List pdfFiles, ServletOutputStream os, Document document, PdfWriter writer, int jenisDokumen, boolean isViewer) throws Exception{

		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		
        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
        if(!userDir.exists()) {
            userDir.mkdirs();
        }

        String namaFile = null;
        
        if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS){
        	namaFile = props.getProperty("pdf.ssu");
        }else if(jenisDokumen == PrintPolisMultiController.SYARAT_KHUSUS){
        	namaFile = props.getProperty("pdf.ssk"); 
        }else if(jenisDokumen == PrintPolisMultiController.SYARAT_UMUM_KHUSUS_EAS){
        	namaFile = props.getProperty("pdf.ssu"); 
        }else if(jenisDokumen == PrintPolisMultiController.PANDUAN_VIRTUAL_ACCOUNT){
        	namaFile = "panduan_virtual_account"; 
        }
        
		FileOutputStream fileOutput = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\" + namaFile + ".pdf");
		PdfCopy pdfCopy 	= null;
		PdfCopy pdfCopy2 	= null;
		pdfCopy 			= new PdfCopy(document, fileOutput);
		pdfCopy2 			= new PdfCopy(document, os);
		
		document.open();
		if(jenisDokumen != PrintPolisMultiController.SYARAT_UMUM_KHUSUS_EAS){
			writer.addJavaScript("this.print(true);", false);
		}
		
		//pdfFiles berisi semua SSU/SSK dari suatu polis (produk utama maupun rider)
		for(int i=0; i<pdfFiles.size(); i++) {
			String pdfFile = pdfFiles.get(i).toString();
			PdfReader reader = new PdfReader(pdfFile);
	        for (int j=1; j <= reader.getNumberOfPages(); j++){
	        	//1. gabungkan PDF nya
	        	pdfCopy2.addPage(pdfCopy2.getImportedPage(reader, j));
	        	//2. simpan ke file
                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
	        }
		}

		pdfCopy.close();
		pdfCopy2.close();
		

		/*
		//pdfFiles berisi semua SSU/SSK dari suatu polis (produk utama maupun rider)
		for(int i=0; i<pdfFiles.size(); i++) {
			String pdfFile = pdfFiles.get(i).toString();
			PdfReader reader = new PdfReader(pdfFile);
	        //PdfContentByte cb = writer.getDirectContent();
			PdfStamper s = new PdfStamper(reader, os); //(Yusuf - biar kalo SSU nya landscape gak masalah)	        
	        for (int j=1; j <= reader.getNumberOfPages(); j++){
	        	//1. gabungkan PDF nya
	        	//document.newPage();
	        	//cb.addTemplate(writer.getImportedPage(reader, j), 0,0);
	        	s.getImportedPage(reader, j);
	        	//2. simpan ke file
                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
	        }
	        s.close(); //
		}

		pdfCopy.close();
		*/
	}
	
	/**
	 * @author Yusuf
	 * @deprecated
	 * Contoh untuk menggabung semua PDF, tambahkan password, 
	 * bahkan bisa sekaligus tambahkan permission untuk save/print pada PDF yg di hasilkan
	 */
/*	private void combineAllPdfInDirectory(String pdfDir, ServletOutputStream os, Document document, PdfWriter writer) throws Exception{
		document = new Document();
        writer = PdfWriter.getInstance(document, os);
		writer.setCloseStream(false);
//		writer.setEncryption(true, "yusuf", "sutarko", PdfWriter.AllowScreenReaders);
		document.open();
//		writer.addJavaScript("this.print(true);", false);
        PdfContentByte cb = writer.getDirectContent();

		File inputDir = new File(pdfDir);
		//inputDir.listFiles().length
		for(int k=0; k<5; k++) { 
			File file = inputDir.listFiles()[k];
			String namaFile = file.getAbsolutePath();
			if(file.getName().toLowerCase().endsWith(".pdf")) {
				//logger.info(file.getName());
				PdfReader reader = new PdfReader(namaFile);
		        for (int i=1; i <= reader.getNumberOfPages(); i++){
		        	document.newPage();
		        	cb.addTemplate(writer.getImportedPage(reader, i), 0,0);
		        }
	        }
		}
        document.close();
	}*/
	//	combineAllPdfInDirectory(
	//	props.getProperty("pdf.dir.syaratpolis"), 
	//	response.getOutputStream(), 
	//	document, 
	//	writer);
	
}