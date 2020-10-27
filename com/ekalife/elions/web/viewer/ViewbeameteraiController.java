package com.ekalife.elions.web.viewer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Stamp;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.lowagie.text.pdf.PdfWriter;

public class ViewbeameteraiController  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("uw/viewer/bea_meterai");
	}
	
	
	public ModelAndView saldo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Map m = (Map) this.uwManager.stamp_sekarang();
		map.put("saldo_awal", m.get("MSTM_SALDO_AWAL"));
		map.put("saldo_akhir", m.get("MSTM_SALDO_AKHIR"));
		map.put("mstm_bulan", m.get("MSTM_BULAN"));
		map.put("kunci","saldo");
		return new ModelAndView("uw/viewer/bea_meterai", map);
	}
	
	public ModelAndView permohonan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List listkode = this.elionsManager.selectDaftarkodestamp();
		String user_id = currentUser.getName();
		String tanggal1 = request.getParameter("tgl1");
		if (tanggal1 ==null)
		{
			tanggal1="";
		}
		String tgl_sementara = request.getParameter("tgl4");
		if(tgl_sementara == null)
		{
			tgl_sementara ="";
		}

		String formats = request.getParameter("bentuk");
		String jenis = request.getParameter("jenis_bea");
		String tgl1 = tanggal1;
		String status="";
		
		if (jenis==null)
		{
			jenis="baru";
		}
		String saldo1 = request.getParameter("saldo");
		if (saldo1 == null)
		{
			saldo1= "0";
		}
		if (jenis.equalsIgnoreCase("baru"))
		{
			
			Double saldo = Double.parseDouble(saldo1);

			if ( !tanggal1.equalsIgnoreCase("") )
			{
				Stamp data= new Stamp();
				Date tgl3 = defaultDateFormat.parse(tgl1);
				String tgl2 = ((String) this.elionsManager.addmonthss(tgl3, -1));
				data.setTgl_sementara(tgl_sementara);
				Date tgl_s = df.parse(tgl_sementara);
				data.setMsth_tgl_bayar(tgl_s);
				data.setTgl2(tgl2);
				data.setTanggal2(tgl2);
				data.setUser_id(user_id);
				data.setTanggal1(tanggal1);
				data.setTgl1(tgl1);
				data.setSaldo(saldo);
				data = this.uwManager.cek_bea_meterai(data,  currentUser);
				String param1 = data.getTanggal1();
				String param2 = data.getTanggal2();
				status = data.getStatus();
				if (data.getStatus().equalsIgnoreCase("insert"))
				{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=permohonan_bea&show="+formats+"&tgl1="+param1+"&tgl2="+param2);
				}/*else if (data.getStatus().equalsIgnoreCase("batal")){
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
					}*/
			}
		}else{
			String kode_bea = request.getParameter("kode");
			if (kode_bea == null)
			{
				kode_bea ="0";
			}
			if (!kode_bea.equalsIgnoreCase("0"))
			{
				String bulan = this.elionsManager.kode_stamp(kode_bea);
				String dd1 = "01";
				String mm1 = bulan.substring(4);
				String yy1 = bulan.substring(0,4);
				String tgl_1 = dd1+"/"+mm1+"/"+yy1;
				Date tgl_3 = defaultDateFormat.parse(tgl_1);
				String tgl_2 = ((String) this.elionsManager.addmonthss(tgl_3, -1));		
				
				String bulan1 = tgl_2.replaceAll("-","");
				String yy2=bulan1.substring(0,4);
				String mm2=bulan1.substring(4,6);
				String dd2=bulan1.substring(6);
				bulan1=yy2+mm2;
				
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=permohonan_bea&show="+formats+"&tgl1="+bulan+"&tgl2="+bulan1);
			}/*else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}*/
		}
		
		map.put("kunci","permohonan");
		map.put("listkode",listkode );
		map.put("tgl1",tgl1);
		map.put("tgl4", tgl_sementara);
		map.put("saldo",saldo1);
		map.put("status", status);
		map.put("jenis", jenis);
		map.put("jenis_bea", jenis);
		return new ModelAndView("uw/viewer/bea_meterai", map);
	}
	
	/**
	 * @author ANDHIKA (04/06/2013)
	 * Upload Bea Materai
	 */	
	public ModelAndView uploadbeajs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Upload uploader = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(uploader);
		binder.bind(request);
		
		String file1 = ServletRequestUtils.getStringParameter(request, "file1", "");
		String namafile1 = ServletRequestUtils.getStringParameter(request, "namafile1", "");
		
		//Proses upload
		if(request.getParameter("upload") != null) {
			
			try {
				String dir = props.getProperty("pdf.dir.report") + "\\materai\\";
				String uploadDir = props.getProperty("pdf.dir.report")+"\\materai\\attachment\\";
				List<File> attachments = new ArrayList<File>();
				
				List<Map> data = new ArrayList<Map>();
				
				//Atachment tambahan
				if(!uploader.getFile1().isEmpty()){
					File sourceFile1 = new File(uploadDir+uploader.getFile1().getOriginalFilename());
					FileCopyUtils.copy(uploader.getFile1().getBytes(), sourceFile1);
					
					if(sourceFile1.exists()){
						attachments.add(sourceFile1);
					}
				}
				
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('File berhasil diupload!');window.close();</script>");
    			sos.close();
    			return null;
    			
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} 
			
		}
		return new ModelAndView("uw/viewer/upload_bea_materai", cmd);
	}
	
	public ModelAndView estimasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List listkode = this.elionsManager.selectDaftarkodestamp();
		String user_id = currentUser.getName();

		String formats = request.getParameter("bentuk");
		String kode_bea = request.getParameter("kode");
		if (kode_bea == null)
		{
			kode_bea ="0";
		}
		if (!kode_bea.equalsIgnoreCase("0"))
		{
			String bulan = this.elionsManager.kode_stamp(kode_bea);
			String dd1 = "01";
			String mm1 = bulan.substring(4);
			String yy1 = bulan.substring(0,4);
			String tgl_1 = dd1+"/"+mm1+"/"+yy1;
			Date tgl_3 = defaultDateFormat.parse(tgl_1);
			String tgl_2 = ((String) this.elionsManager.addmonthss(tgl_3, -1));		
			
			String bulan1 = tgl_2.replaceAll("-","");
			String yy2=bulan1.substring(0,4);
			String mm2=bulan1.substring(4,6);
			String dd2=bulan1.substring(6,8);
			bulan1=yy2+mm2;
			
			response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=estimasi_bea&show="+formats+"&tgl1="+bulan+"&tgl2="+bulan1);
		}/*else{
			response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
		}*/

		map.put("kunci","estimasi");
		map.put("listkode",listkode );
		return new ModelAndView("uw/viewer/bea_meterai", map);
	}	
	
	public ModelAndView bubuh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List listkode = this.elionsManager.selectDaftarkodestamp();
		String user_id = currentUser.getName();
		String tgl_sementara = request.getParameter("tgl4");
		if(tgl_sementara == null)
		{
			tgl_sementara ="";
		}
		String status ="";
		String formats = request.getParameter("bentuk");
		String saldo1 = request.getParameter("saldo");
		if (saldo1 == null)
		{
			saldo1= "0";
		}

		Double saldo = Double.parseDouble(saldo1);

		String kode_bea = request.getParameter("kode");
		if (kode_bea == null)
		{
			kode_bea ="0";
		}
		if (!kode_bea.equalsIgnoreCase("0"))
		{
			Stamp data= (Stamp) this.elionsManager.detil_kode_stamp(kode_bea);
			data.setStatus("");
			data.setKode(kode_bea);
			data.setMstm_kode(kode_bea);
			data.setTgl_sementara(tgl_sementara);
			Date tgl_s = df.parse(tgl_sementara);
			data.setMsth_tgl_bayar(tgl_s);
			data.setSaldo(saldo);
			data = this.uwManager.edit_bea_meterai(data, currentUser);
			status = data.getStatus();
			if (data.getStatus().equalsIgnoreCase("insert"))
			{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=pembubuhan_bea&show="+formats+"&kode="+kode_bea+"&jumlah_setoran="+saldo);
			}
		}	
		map.put("kunci","bubuh");
		map.put("listkode",listkode );
		map.put("tgl4", tgl_sementara);
		map.put("saldo",saldo1);
		map.put("status", status);
		return new ModelAndView("uw/viewer/bea_meterai", map);
	}
	
	public ModelAndView produk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String id = currentUser.getLus_id();

		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
		String tgl1 = tanggal1;
		String tgl2 = tanggal2;
		if (tanggal1!=null)
		{
			tanggal1=tanggal1.replaceAll("/","");
			tanggal2=tanggal2.replaceAll("/","");
			String yy1,mm1,dd1,yy2,mm2,dd2;
			yy1=tanggal1.substring(4);
			mm1=tanggal1.substring(2,4);
			dd1=tanggal1.substring(0,2);
			yy2=tanggal2.substring(4);
			mm2=tanggal2.substring(2,4);
			dd2=tanggal2.substring(0,2);
			tanggal1=yy1+mm1+dd1;
			tanggal2=yy2+mm2+dd2;
		}else{
			tanggal1 = "";
			tgl1 = "";
			tanggal2 ="";
			tgl2="";
		}
		if (!tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
		{
			Integer jumlah = this.elionsManager.countbea_perproduk(tanggal1, tanggal1);
			if (jumlah == null)
			{
				jumlah = new Integer(0);
			}
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=estimasi_perproduk&show=pdf&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		map.put("kunci","produk");
		return new ModelAndView("uw/viewer/bea_meterai", map);
	}

	public ModelAndView penggunaan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		boolean cari=false;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/viewer/report_bea_materai",map);
	}

}
