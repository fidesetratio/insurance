package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentController;

public class UploadController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
//		int lar_id = ServletRequestUtils.getIntParameter(request, "lar_id", -1);
		String lsKode=ServletRequestUtils.getStringParameter(request, "lsKode","-1");
		cmd.put("lsKode", lsKode);
		String[] larId= lsKode.split(",");
		List addressRegion = this.elionsManager.selectAddressRegion();
		String to[]=request.getParameterValues("to");
		String cCopy=ServletRequestUtils.getStringParameter(request, "cc",null);
		if(cCopy==null)
			cCopy=props.getProperty("admin.yusuf");
		else
			cCopy=cCopy+";"+props.getProperty("admin.yusuf");
		
		boolean emailCc=true;
		String cc[] = null;
		if(cCopy!=null){
			cc=cCopy.split(";");
			f_validasi valid=new f_validasi();
			for(int i=0;i<cc.length;i++){
				if(valid.f_validasi_email(cc[i])==false){
					emailCc=false;
					break;
				}	
			}
			if(emailCc==false)
				request.setAttribute("pesan", "Email CC salah contoh : abc@sinarmasmsiglife.co.id");

		}
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if((!lsKode.equals("-1")) && emailCc) {
			List lsAddRegion=elionsManager.selectAddressRegionLarId(lsKode);
			cmd.put("selectedAddressRegion", lsAddRegion);
			if(request.getParameter("upload")!=null) {
				
				for(int i=0;i<lsAddRegion.size();i++){
					Map addr = (HashMap) lsAddRegion.get(i);
					addr.put("LAR_EMAIL", to[i]);
					elionsManager.updateLstAddrRegionLarEmail(larId, to);
					String tDest=props.getProperty("upload.dir")+FormatString.rpad("0", addr.get("LAR_ID").toString(), 3);
					File destDir = new File(tDest);
					if(!destDir.exists()) destDir.mkdir();
					if(upload.getFile1().isEmpty()==false){
						String dest=tDest+"/"+upload.getFile1().getOriginalFilename();
						File outputFile = new File(dest);
					    FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
					}
					if(upload.getFile2().isEmpty()==false){
						String dest=tDest+"/"+upload.getFile2().getOriginalFilename();
						File outputFile = new File(dest);
					    FileCopyUtils.copy(upload.getFile2().getBytes(), outputFile);
					}
					if(upload.getFile3().isEmpty()==false){
						String dest=tDest+"/"+upload.getFile3().getOriginalFilename();
					    File outputFile = new File(dest);
					    FileCopyUtils.copy(upload.getFile3().getBytes(), outputFile);
					}
				}
			    
				if(email!=null) {
					if(to.length>0) {
//						email.send(false, props.getProperty("admin.ajsjava"), to,cc, null, "[Ekalife IT] Sebuah File telah di-upload oleh ["+currentUser.getName()+"]", 
//								"Sebuah file telah di-upload oleh ["+currentUser.getName()+"]"+
//								"\nSilahkan masuk ke"+
//								"\n- [http://intranet/E-Lions], apabila anda berada di kantor pusat dan sekitarnya, atau"+
//								"\n- [http://www.sinarmaslife.co.id/E-Lions/], apabila anda berada di kantor cabang."+
//								"\n\nAnda dapat LOGIN dengan menggunakan id LIONS / ekalife.com, dan memilih tab DOWNLOAD pada halaman utama setelah login."+
//								"\n\nTerima kasih.\nEkalife IT Department"
//								, null);
					}
				}
				request.setAttribute("pesan", "Berhasil");
			} else {
				cmd.put("pesan", "Silahkan isi minimal satu file untuk di-upload");
			}
		}
			

			
		List lsLarId=new ArrayList();
		Map mLar;
		for(int i=0;i<larId.length;i++){
			mLar=new HashMap();
			mLar.put("key", new BigDecimal(Integer.parseInt(larId[i])));
			lsLarId.add(mLar);
		}
		cmd.put("comboSize", currentUser.getComboBoxSize()-2);
		cmd.put("addressRegion", addressRegion);
		cmd.put("lsLarId", lsLarId);
		
		return new ModelAndView("common/upload", cmd);
	}
	  public void copyFile(File in, File out) throws Exception {
		  FileInputStream fis  = null;
		  FileOutputStream fos = null;
		  try {
		    fis  = new FileInputStream(in);
		    fos = new FileOutputStream(out);
		    byte[] buf = new byte[1024];
		    int i = 0;
		    while((i=fis.read(buf))!=-1) {
		      fos.write(buf, 0, i);
		      }
		  }finally {
			  try {
				  if(fis != null) {
					  fis.close();
				  }
				  if(fos != null) {
					  fos.close();
				  }
			  }catch (Exception e) {
				logger.error("ERROR :", e);
			}
		  }
	  }
}