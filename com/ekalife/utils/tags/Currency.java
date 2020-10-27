package com.ekalife.utils.tags;

import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.AngkaTerbilang;

/**
 * <p>Merupakan customtags yang bisa digunakan dengan cara:</p>
 * <ol>
 * 	<li>Mendefinisikan class didalam file TLD di folder WEB-INF/tlds</li>
 * 	<li>mendefinisikan tag dalam JSP, seperti <%@ taglib prefix="elions"uri="/WEB-INF/tlds/elions.tld" %></li>
 * 	<li>Menggunakan tag tersebut dalam JSP, misalnya <elions:polis nomor="${n.MSPO_POLICY_NO}"/></li>
 * 	<li></li>
 * </ol>
 * @author Yusuf Sutarko
 * @since Feb 1, 2007 (11:28:10 AM)
 */
public class Currency implements Tag {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	private static final long serialVersionUID = -1377874373987487910L;

	private String kurs;
	private String terbilang;
	private String jumlah;

	PageContext pageContext; 

	public void setParent(Tag t) {
	}

	public void setPageContext(PageContext p) {
		pageContext = p;
	}

	public void release() {
	}

	public Tag getParent() {
		return null;
	}

	public int doStartTag() {
		try {
			if(jumlah.trim().equals("")) return EVAL_BODY_INCLUDE;
			
			boolean pakeKurs=false, pakeTerbilang=false;
			JspWriter out = pageContext.getOut();
			if (kurs != null) if (!kurs.trim().equals("")) pakeKurs=true;
			if(terbilang != null) if(!terbilang.trim().equals("")) pakeTerbilang=true;

			if(!pakeTerbilang) {
				if(pakeKurs) out.print(kurs);
				DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
				out.print(df.format(Double.parseDouble(jumlah)));
			}else {
				if("english".equalsIgnoreCase(terbilang)) out.print(AngkaTerbilang.english(jumlah));
				else out.print(AngkaTerbilang.indonesian(jumlah));
				if(pakeKurs) out.print(kurs);
				out.println();
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setKurs(String kurs) {
		this.kurs = kurs;
	}

	public void setTerbilang(String terbilang) {
		this.terbilang = terbilang;
	}

	public void setJumlah(String jumlah) {
		this.jumlah = jumlah;
	}

}