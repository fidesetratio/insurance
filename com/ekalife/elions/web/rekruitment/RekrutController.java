package com.ekalife.elions.web.rekruitment;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentController;

import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Oct 16, 2012 8:43:30 AM
 * @Description :
 *	Modul menu root untuk  biodata online 
 *  request by Pak Jos Chandra
 */

public class RekrutController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		User user = (User) request.getSession().getAttribute("currentUser");


		
		
		if(request.getParameter("transfer")!=null){
			String kode_rekrut = request.getParameter("kode_rekrut");
			if (kode_rekrut == null)
			{
				kode_rekrut ="";
			}
			Kuesioner data=new Kuesioner();
			data = this.elionsManager.selectkuesioner(kode_rekrut);
			data.setDaftarTanggungan(this.uwManager.select_tertanggung_rekrut(kode_rekrut));
			
			//klo dari e-agency cek hanya boleh yang punya dia aja di akses
			User currentUser=(User) request.getSession().getAttribute("currentUser");
			if(currentUser.getMsag_id_ao()!=null){
				String pesan=null;
				//klo bukan inputan agen di pentalin			
				if(!data.getMsag_id().equals(currentUser.getMsag_id_ao())){
					pesan="<script>alert('Mohon maaf, Anda tidak memiliki hak akses ke halaman ini.');history.go(-1);</script>";
			    }
				
				//klo sudah ditransfer hanya bisa view aja
				if(data.getMku_tgl_transfer_admin()!=null){
					pesan="<script>alert('Mohon maaf, Data Agen "+kode_rekrut+" sudah pernah di kirim ke Admin.');history.go(-1);</script>";
			    	
				}
				
				if(data.getMku_flag_ktp()==null){
					pesan="<script>alert('Silahkan upload terlebih dahulu KTP Agen');history.go(-1);</script>";
				}else if(data.getMku_flag_foto()==null){
					pesan="<script>alert('Silahkan upload terlebih dahulu Foto Agen');history.go(-1);</script>";
				}else if(data.getMku_flag_buku_rek()==null){
					pesan="<script>alert('Silahkan upload terlebih dahulu Buku Rekening Agen');history.go(-1);</script>";
				}else if(data.getMku_flag_bsb_ujian()==null){
					pesan="<script>alert('Silahkan upload terlebih dahulu BSB Ujian Agen');history.go(-1);</script>";
				}else{
					String subject="[E-Lions] Kuesioner Pendaftaran Agen Baru";
					
					List<File> attachments=new ArrayList<File>();
					
					String agent = data.getMku_no_reg();	
					String res = request.getSession().getServletContext().getResource("/xml/").toString();
					String jenis = Common.searchXml(res+"JENIS_CABANG.xml", "ID", "JENIS", data.getMku_jenis_cabang());
					String dir = props.getProperty("pdf.dir.export.rekruitment");		
					dir = dir +  "\\" +jenis+ "\\"+agent;
					List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir);
					for(DropDown dd:daftarAda){
						attachments.add(new File(dir+"\\"+dd.getKey()));
					}	
					
					String message="Dh," +
							"<br/>Mohon di follow up pendaftaran agen baru di bawah ini :" +
							"<table>" +
							"<tr><th>No Registrasi</th><th>:</th><td>"+data.getMku_no_reg()+"</td></tr>" +
							"<tr><th>Nama Calon Agen</th><th>:</th><td>"+data.getMku_first()+"</td></tr>" +
							"<tr><th>Tanggal Lahir</th><th>:</th><td>"+DateUtil.toIndonesian(data.getMku_date_birth())+"</td></tr>" +
							"<tr><th>Alamat</th><th>:</th><td>"+data.getMku_alamat()+"</td></tr>" +
							"<tr><th>Kota </th><th>:</th><td>"+data.getMku_kota()+"</td></tr>" +
							"<tr><th>Nama Rekruter </th><th>:</th><td>"+data.getMku_rekruiter()+"</td></tr>" +
							"<tr><th>Kode Rekruter </th><th>:</th><td>"+data.getMsrk_id()+"</td></tr>" +
							"<tr><th>Jenis Cabang </th><th>:</th><td>"+jenis+"</td></tr>" +
							"</table>" +
							"<br/>Berikut kami sertakan attachment kelengkapan data." +
							"<br/>Terima kasih" +
							"<br/><br/>NB: Email ini terkirim otomatis dari sistem Kuesioner Pendaftaran Agen. Mohon tidak mereply email ini";
					
//					email.send(true, props.getProperty("admin.ajsjava"), new String[]{"berto@sinarmasmsiglife.co.id"}, null, null, subject, message, attachments);
					////email ke admin 
					email.send(true, props.getProperty("admin.ajsjava"), uwManager.selectEmailAdmin(data.getMsag_id()).split(";"), null, null, subject, message, attachments);
					////email ke Distribution Support
					email.send(true, props.getProperty("admin.ajsjava"), props.getProperty("admin.agency_support").split(";"), null, null, subject, message, attachments);
					////email ke Bisnis Dev
					email.send(true, props.getProperty("admin.ajsjava"),  props.getProperty("admin.agency_dev").split(";"), null, null, subject, message, attachments);
					
					Kuesioner updateKuesioner=new Kuesioner();
					updateKuesioner.setMku_no_reg(data.getMku_no_reg());
					updateKuesioner.setMku_region(data.getMku_region());
					updateKuesioner.setMku_tgl_transfer_admin(elionsManager.selectSysdate1("dd", false, 0));
					uwManager.updateKuesioner(updateKuesioner);
					
					pesan="<script>alert('Data Agen "+kode_rekrut+" berhasil di kirim ke Admin.');history.go(-1);</script>";
				}
				
				
				if(pesan!=null){
					ServletOutputStream out;
					try {
						out = response.getOutputStream();
						out.println(pesan);
			    		out.flush();
			    		return null;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e1);
					}
					return null;
				}
				
			}
		}
		
		List lsDaftarSpaj = this.uwManager.selectdaftarRegKuesioner(user.getMsag_id_ao());
		map.put("daftarSPAJ", lsDaftarSpaj);
		
		map.put("lebar", user.getScreenWidth()*3/4);
		map.put("panjang", user.getScreenHeight()*3/4);
		
		return new ModelAndView("rekruitment/rekrut", map);
	}
	
}