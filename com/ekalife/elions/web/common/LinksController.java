package com.ekalife.elions.web.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.EncryptUtils;
import com.ekalife.utils.parent.ParentMultiController;

public class LinksController extends ParentMultiController {
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		boolean isSubDomain = false;
		Map m = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		m.put("lus_id", currentUser.getLus_id());
		String servername=request.getServerName();
//		String[] servername_split= servername.split(".");
//		String servername="128.21.32.10";
		String servername2=servername.replace(".co.id", "");
//		String[] servername_split= servername2.split("\\.",-1);
		String[] servername_split= servername2.split("\\.");
		if("elions.sinarmasmsiglife".equals(servername2))isSubDomain=true;
		if(servername_split.length == 2)isSubDomain=true;
		Integer port = request.getServerPort();
		String link = "";
		Integer running = ServletRequestUtils.getIntParameter(request, "running", 0);
		
		if(running !=0){
			if(running==1){
				if("localhost,127.0.0.1".indexOf(servername) <0){
					link="http://"+ servername +"/E-Policy";
				}else{
					if(isSubDomain) {
						link = "http://epolicy.sinarmasmsiglife.co.id";
					} else {
						link="http://"+ servername +":"+ port +"/E-Policy";
					}
				}
				
//				link="http://www.sinarmasmsiglife.co.id/E-Policy";
			}else if(running==2){
				if("localhost,127.0.0.1".indexOf(servername) <0){
					link="http://"+ servername +"/E-Agency";
				}else{
					if(isSubDomain) {
						link = "http://eagency.sinarmasmsiglife.co.id";
					} else {
						link="http://"+ servername +":"+ port +"/E-Agency";
					}
				}
				
//				link="http://www.sinarmasmsiglife.co.id/E-Agency";
			}else if(running==3){
				//link="http://128.21.32.14/bas?auth="+EncryptUtils.encode("14041985".getBytes());
				link="http://basbook.sinarmasmsiglife.co.id/?auth="+EncryptUtils.encode("14041985".getBytes());
			}else if(running==4){
//				link="http://202.43.181.35/bas?auth="+EncryptUtils.encode("14041985".getBytes());
				//link="http://www.e-sehat.co.id/bas?auth="+EncryptUtils.encode("14041985".getBytes());
				link="http://basbook.sinarmasmsiglife.co.id/?auth="+EncryptUtils.encode("14041985".getBytes());
			}else if(running==5){
				String link_prop = logProp("elions",currentUser.getLus_id(),"bsm");
				if(isSubDomain){
					//link="http://eproposal.sinarmasmsiglife.co.id/?web=elions&lusId="+currentUser.getLus_id()+"&type=bsm";
					link="http://eproposal.sinarmasmsiglife.co.id/?"+link_prop;
				}else{
					//link="http://eproposal.sinarmasmsiglife.co.id/?web=elions&lusId="+currentUser.getLus_id()+"&type=bsm";
					//link="http://"+ servername +"/E-Proposal/?web=elions&lusId="+currentUser.getLus_id()+"&type=bsm";
					link="http://"+ servername +"/E-Proposal/?"+link_prop;
				}
			}else if(running==6){
				String link_prop = logProp("elions",currentUser.getLus_id(),"sms");
				if(isSubDomain){
					//link="http://eproposal.sinarmasmsiglife.co.id/?web=elions&lusId="+currentUser.getLus_id()+"&type=sms";
					link="http://eproposal.sinarmasmsiglife.co.id/?"+link_prop;
				}else{
					//link="http://eproposal.sinarmasmsiglife.co.id/?web=elions&lusId="+currentUser.getLus_id()+"&type=sms";
					//link="http://"+ servername +"/E-Proposal/?web=elions&lusId="+currentUser.getLus_id()+"&type=bsm";
					link="http://"+ servername +"/E-Proposal/?"+link_prop;
				}
			}
		}
		
		
//		<c:if test="${sessionScope.currentUser.jn_bank ne 2 and sessionScope.currentUser.jn_bank ne 3}">
//		<td align=center><a href="/E-Policy" target="_blank"><img
//				src="include/image_links/epol_s.gif" title="E-Policy" /><br />E-Policy</a>
//		</td>
//		<td align=center><a href="/E-Agency" target="_blank"><img
//				src="include/image_links/eagency_s.gif" title="E-Agency" /><br />E-Agency</a>
//		</td>
//		
//		<c:if test="${sessionScope.currentUser.lus_id ne 2527}">
//			<td align=center><a
//				href="http://128.21.32.14/bas?auth=<%=EncryptUtils.encode("14041985".getBytes())%>"
//				target="_blank"><img src="include/image_links/bas_s.gif"
//					title="BAS" /><br />BAS (internal)</a>
//			</td>
//			<td align=center><a
//				href="http://202.43.181.35/bas?auth=<%=EncryptUtils.encode("14041985".getBytes())%>"
//				target="_blank"><img src="include/image_links/bas_s.gif"
//					title="BAS" /><br />BAS (eksternal)</a>
//			</td>
//		</c:if>
//	</c:if>
//	
//	<c:if test="${sessionScope.currentUser.jn_bank eq 2}">
//	<td align=center><a href="http://www.sinarmasmsiglife.co.id/E-Proposal/?web=elions&lusId=${sessionScope.currentUser.lus_id}&type=bsm" target="_blank"><img
//			src="include/image_links/proposal.gif" title="E-Proposal" /><br />E-Proposal</a></c:if></td>
//	<c:if test="${sessionScope.currentUser.jn_bank eq 3}">
//	<td align=center><a href="http://www.sinarmasmsiglife.co.id/E-Proposal/?web=elions&lusId=${sessionScope.currentUser.lus_id}&type=sms" target="_blank"><img
//			src="include/image_links/proposal.gif" title="E-Proposal" /><br />E-Proposal</a></c:if></td>
//		
		if(running!=0){
//			return new ModelAndView(new RedirectView(link,true,true));
			try {
				response.sendRedirect(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
		}else{
			m.put("link", link);
			return new ModelAndView("common/links", m);
		}
		return null;
	}
	
	//untuk dapat id buat login proposal
	public String logProp(String web, String lus_id, String type){
		String result = "";
		
		String kode = lus_id;
		String id = "-";
		
		String link = "web="+web+"~kode="+kode+"~id="+id+"~type="+type;
		
		try{
			String sec_1 = bacManager.selectSeqUrlSecureId();
			String sec_2 = "login_e-proposal";
			String encrypt_1 = "";
			String encrypt_2 = "";
			
			//decrypt sampai tidak ada char #
			Boolean ok = false;
			do{
				Integer kres = 0;
				
				encrypt_1 = elionsManager.selectEncryptDecrypt(sec_1, "e");
				encrypt_2 = elionsManager.selectEncryptDecrypt(sec_2, "e");
				
				if(encrypt_1.contains("#"))kres = 1;
				else if(encrypt_2.contains("#"))kres = 1;
				
				if(kres==0)ok = true;
				
			}while(ok==false);
			
			bacManager.insertMstUrlSecure(sec_1, sec_2, kode, link, encrypt_1, encrypt_2);
			
			result = "id1="+encrypt_1+"&id2="+encrypt_2;
		}catch(Exception e){
			result = "error";
		}
		
		return result;
	}
	
}