package com.ekalife.elions.business.bac;

import com.ekalife.elions.vo.BacSpajParamVO;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfContentByte;

public class MultiInvestAsm {

	private static MultiInvestAsm instance;
	
	public static MultiInvestAsm getInstance()
	{
		if( instance == null )
		{
			instance = new MultiInvestAsm();
		}
		return instance;
	}
	
	public MultiInvestAsm()
	{
	}
	
	public PdfContentByte createPdf( BacSpajParamVO paramVO )
	{
		PdfContentByte over = paramVO.getOver(); 
		Integer x = paramVO.getX();
		Integer y = paramVO.getY();
		Integer f = paramVO.getF();
		String noBlanko= paramVO.getNoBlanko();
	
		over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+5, y-131, 0);

		return over;
		
		/*
		PdfContentByte over = paramVO.getOver(); 
		Pemegang pemegang = paramVO.getPemegang(); 
		Tertanggung tertanggung = paramVO.getTertanggung(); 
		Integer x = paramVO.getX();
		Integer y = paramVO.getY();
		Integer f = paramVO.getF();
		Datausulan datausulan = paramVO.getDatausulan();
		Powersave data_pwr = paramVO.getData_pwr();
		Rekening_client data_rek = paramVO.getData_rek();
		Agen dataagen =paramVO.getDataagen();
		InvestasiUtama inv = paramVO.getInv();
		PdfStamper stamp =paramVO.getStamp();
		BaseFont bf = paramVO.getBf();
		PdfReader reader =paramVO.getReader();
		String noBlanko= paramVO.getNoBlanko();
	
		int tambah = 60;
//				noBlanko ="DIANNN";
		over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+542, y+843, 0);
		
		Integer nOne = reader.getNumberOfPages();
		
		logger.info("jumlah page:"+nOne);
		
		over.endText();
		//halaman 2
		over = stamp.getOverContent(2);
    	over.beginText();
    	over.setFontAndSize(bf, f); //set ukuran font
    	over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+535, y+843, 0);
    	over.endText();
    	
    	//halaman 3
    	over = stamp.getOverContent(3);
    	over.beginText();
    	over.setFontAndSize(bf, f); //set ukuran font
    	over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+531, y+841, 0);
    	over.endText();
    	
    	//halaman 4
//    	over = stamp.getOverContent(4);
//    	over.beginText();
//    	over.setFontAndSize(bf, f); //set ukuran font
//    	over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+530, y+841, 0);
//    	over.endText();
    	
    	//halaman 5
//    	over = stamp.getOverContent(5);
//    	over.beginText();
//    	over.setFontAndSize(bf, f); //set ukuran font
//    	over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+535, y+832, 0);
//    	over.endText();
    	
    	//halaman 6
//    	over = stamp.getOverContent(6);
//    	over.beginText();
//    	over.setFontAndSize(bf, f); //set ukuran font
//    	over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x+535, y+841, 0);
//    	over.endText();
		
		return over;
		*/
	}
	
}
