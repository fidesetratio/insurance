package com.ekalife.utils.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * <p>Merupakan customtags yang bisa digunakan dengan cara:</p>
 * <ol>
 * 	<li>Mendefinisikan class didalam file TLD di folder WEB-INF/tlds</li>
 * 	<li>mendefinisikan tag dalam JSP, seperti <%@ taglib prefix="elions"uri="/WEB-INF/tlds/elions.tld" %></li>
 * 	<li>Menggunakan tag tersebut dalam JSP, misalnya <elions:polis nomor="${n.MSPO_POLICY_NO}"/></li>
 * 	<li></li>
 * </ol>
 * @author Yusuf Sutarko
 */
public class Polis implements Tag {

	private static final long serialVersionUID = -1377874373987487910L;

	private String nomor;

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
			JspWriter out = pageContext.getOut();
			if (nomor == null) {
				out.print(nomor);
			} else if (nomor.length() == 9) {
				out.print(nomor.substring(0, 2) + "." + nomor.substring(2));
			} else if (nomor.length() == 11) {
				out.print(nomor.substring(0, 2) + "." + nomor.substring(2, 6) + "."
						+ nomor.substring(6));
			} else if (nomor.length() == 14) {
				out.print(nomor.substring(0, 2) + "." + nomor.substring(2, 5) + "."
						+ nomor.substring(5, 9) + "." + nomor.substring(9));
			} else
				out.print(nomor);
			
		} catch (Exception e) {
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
	}

}