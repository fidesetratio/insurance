package com.ekalife.utils.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Class ini fungsinya cuman satu, ngirim e-mail
 * Saat ini fungsi e-mailnya cuman bisa ngirim yang berbasis text
 * (untuk email yang bisa html dan attachment, coba lihat class Email di project E-Lions)
 * 
 * @author Yusuf Sutarko
 * @since Mar 16, 2007 (9:48:21 AM)
 */
public class Email {
	
	private JavaMailSender mailSender;
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Fungsi utama untuk mengirim email dalam bentuk text biasa 
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 16, 2007 (10:05:31 AM)
	 * @param to Array String berisi daftar email yang dituju
	 * @param subject Subject dari email
	 * @param message Pesan dari email
	 * @throws MailException
	 */
	public void send(String from, String[] to, String subject, String message) throws MailException{
		if (logger.isDebugEnabled()) logger.debug("SENDING EMAIL...");
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setReplyTo(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(message);		
		mailSender.send(msg);
	}
	
	/**
	 * Fungsi untuk mengirim email dalam bentuk text biasa, paling simple, cocok untuk error reporting 
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 16, 2007 (10:05:31 AM)
	 * @param to Array String berisi daftar email yang dituju
	 * @param subject Subject dari email
	 * @param message Pesan dari email
	 * @throws MailException
	 */
	public void sendCC(String from, String[] to, String[] cc, String[] bcc, String subject, String message) throws MailException{
		if (logger.isDebugEnabled()) logger.debug("SENDING EMAIL...");
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		if(to!=null) msg.setTo(to);
		if(cc!=null) msg.setCc(cc);
		if(bcc!=null) msg.setBcc(bcc);
		msg.setSubject(subject);
		msg.setText(message);		
		mailSender.send(msg);
	}
	
	public void sendHtmlEmail(String from, String[] to,String[] cc, String[] bcc, String subject, String text, List<File> attachments) throws MailException, MessagingException, IOException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(from);
		helper.setSubject(subject);
		if(to!=null) helper.setTo(to);
		if(cc!=null) helper.setCc(cc);
		if(bcc!=null) helper.setBcc(bcc);
		
		helper.setText(text, true);

		if(attachments!=null) {
			for(int i=0; i<attachments.size(); i++) {
				File attachment = attachments.get(i);
				if(attachment.exists())	helper.addAttachment(attachment.getName(), attachment);
			}
		}	

		mailSender.send(helper.getMimeMessage());
	}
	
	public void sendHtmlEmail(String from, String[] to,String[] cc, String[] bcc, String subject, InputStream in, List<File> attachments) throws MailException, MessagingException, IOException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(from);
		helper.setSubject(subject);
		if(to!=null) helper.setTo(to);
		if(cc!=null) helper.setCc(cc);
		if(bcc!=null) helper.setBcc(bcc);
		
		InputStreamReader is = null;
		BufferedReader br = null;

		try {
			is = new InputStreamReader(in);
			br = new BufferedReader(is);
			String str;
			StringBuffer text = new StringBuffer();
			while ((str = br.readLine()) != null) {
				text.append(str);
			}
			helper.setText(text.toString(), true);

			if(attachments!=null) {
				for(int i=0; i<attachments.size(); i++) {
					File attachment = attachments.get(i);
					if(attachment.exists())	helper.addAttachment(attachment.getName(), attachment);
				}
			}	

			mailSender.send(helper.getMimeMessage());
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} finally {
			if(br!=null) br.close();
			if(is!=null) is.close();
		}
	}
	
	/**
	 * Fungsi full-blown untuk mengirim e-mail. Support tipe HTML, multiple attachments, inline / attached
	 * 
	 * @author Yusuf Sutarko
	 * @since May 2, 2007 (11:20:43 AM)
	 * @param from
	 * @param to
	 * @param cc
	 * @param subject
	 * @param text
	 * @param attachments
	 * @throws MailException
	 * @throws MessagingException
	 */
	public void sendWithAttachments(String from, String[] to,String[] cc, String[] bcc, String subject, String text, List<File> attachments) throws MailException, MessagingException{
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(from);
		helper.setSubject(subject);
		if(to!=null) helper.setTo(to);
		if(cc!=null) helper.setCc(cc);
		if(bcc!=null) helper.setBcc(bcc);

		//String css = props.getProperty("email.uw.css.satu") + props.getProperty("email.uw.css.dua");
		//String footer = props.getProperty("email.uw.footer");
	     
		helper.setText(text);
		//helper.addInline("myLogo", new ClassPathResource(props.getProperty("images.ttd.ekalife")));
		
		if(attachments!=null) {
			for(int i=0; i<attachments.size(); i++) {
				File attachment = attachments.get(i);
				if(attachment.exists())	helper.addAttachment(attachment.getName(), attachment);
			}
		}	
		
		mailSender.send(helper.getMimeMessage());
	}

}