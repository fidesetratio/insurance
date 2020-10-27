package com.ekalife.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.Common;

public class CustomerSignUploadServlet extends HttpServlet {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	private static final long serialVersionUID = 4007285821339831055L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		
//		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String nama = ServletRequestUtils.getRequiredStringParameter(request, "nama");
		String imageFormat = ServletRequestUtils.getRequiredStringParameter(request, "imageFormat");
		
		ElionsManager elionsManager = (ElionsManager) Common.getBean(request.getSession().getServletContext(), "elionsManager");

//		if((image = request.getParameter("image"))!=null) {
//			TandaTangan ttd = elionsManager.selectMstTandatangan(spaj, no);
//			if(ttd!=null){
//				response.setContentType("image/jpeg");
//				ServletOutputStream out = response.getOutputStream();
//				out.write(ttd.getMstt_image());
//				out.close();
//			}
//		}else {
			ServletOutputStream out = response.getOutputStream();

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Parse the request
			try {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				List items = upload.parseRequest(request);
				for(int i=0; i<items.size(); i++) {
					FileItem item = (FileItem) items.get(i);
					elionsManager.saveTandaTangan(nama,spaj, item, currentUser,imageFormat);
				}
				out.print("sukses");
			} catch (Exception e) {
				out.print("gagal");
				logger.error("ERROR :", e);
			}
			out.close();
//		}
	}

	/**
	 * @deprecated, fungsi yang bawah, mengambil/menyimpan image ke file, bukan ke database
	 * @author Yusuf
	 */
	public void oldDoPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String image;
		
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj"); 
		ElionsManager elionsManager = (ElionsManager) Common.getBean(request.getSession().getServletContext(), "elionsManager");
		
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		String userDir = elionsManager.getProps().getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\";
		File dir = new File(userDir);
		if(!dir.exists()) dir.mkdirs();
		
		ServletOutputStream out = null;
		BufferedInputStream in = null;
		try{
			if((image = request.getParameter("image"))!=null) {
				int data=0;
				response.setContentType("image/jpeg");
				out = response.getOutputStream();
				in = new BufferedInputStream(new FileInputStream(userDir+"\\sign.jpg"));
				while ((data = in.read()) != -1) {
					out.write(data);
				}
//				in.close();
//				out.close();
			}else {
				out = response.getOutputStream();
			
				// Create a factory for disk-based file items
				FileItemFactory factory = new DiskFileItemFactory();
				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// Parse the request
				try {
					List items = upload.parseRequest(request);
					for(int i=0; i<items.size(); i++) {
						FileItem item = (FileItem) items.get(i);
					    File uploadedFile = new File(userDir + "\\" + item.getName());
					    item.write(uploadedFile);
					}
					out.print("sukses");
				} catch (Exception e) {
					out.print("gagal");
					logger.error("ERROR :", e);
				}
//				out.close();
			}
		}finally{
            try{
                if(in != null){
                   in.close();
                }
                if(out != null){
                   out.close();
                }
			}catch(Exception e){
			      logger.error("ERROR :", e);
			}              
		}
	
	}

}