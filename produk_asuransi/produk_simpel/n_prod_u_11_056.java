package produk_asuransi.produk_simpel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.TmSales;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.StringUtil;
/**
 * Produk Upload PAS Business Partner
 * @author : Adrian
 * @since : Jan 21, 2015
 */
public class n_prod_u_11_056 extends n_prod_u {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	//ex:	
	void main(){
		int x = getumur();
		//2
	}		
	//override
	int getumur(){
		return 2;
	}
	
	public n_prod_u_11_056(){
		super();
	}
	
    public TmSales set_prod_u_11_056(List rowAgencyExcelList ){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				
		tmSales.setHolder_name(rowAgencyExcelList.get(1).toString());
		
		String JK = rowAgencyExcelList.get(2).toString().trim();				
		if(rowAgencyExcelList.get(2).toString().trim().equalsIgnoreCase("Pria") || JK.equalsIgnoreCase("P"))
		{   tmSales.setSex(1);
				} 
		if(rowAgencyExcelList.get(2).toString().trim().equalsIgnoreCase("Wanita") || JK.equalsIgnoreCase("W"))
		{   tmSales.setSex(0);	
				} 							
		
		tmSales.setCard_no(rowAgencyExcelList.get(3).toString());
		try{							
			Date date = df.parse(rowAgencyExcelList.get(4).toString());					
			tmSales.setBod_holder(date);
		}
		catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		tmSales.setEmail(rowAgencyExcelList.get(5).toString());
		tmSales.setMobile_no(rowAgencyExcelList.get(6).toString());		
		
		return tmSales;
	}
    
    
    public  String validate_prod_u_11_056(){
    	String err="";    	
    	 
		if(Common.isEmpty(tmSales.getHolder_name()) || tmSales.getHolder_name() == null || tmSales.getHolder_name() == ""){
		   err = err+ " PAS-Free: Nama Tertanggung tidak boleh kosong!,";
		}
		if(tmSales.getSex()==null) err = err+ " P/W  harus diisi Format: Pria/Wanita,";		    
		if(Common.isEmpty(tmSales.getCard_no()) || tmSales.getCard_no() == null || tmSales.getCard_no() == ""){
	    		err = err+ ": No.Identitas tidak boleh kosong!,";
		}	
	    if(tmSales.getBod_holder()==null) err = err+ " Tanggal lahir harus diisi dgn Format: DD/mm/yyyy,";
	   	    
	    if(Common.isEmpty(tmSales.getMobile_no()) || tmSales.getMobile_no() == null || tmSales.getMobile_no() == ""){
    		err = err+ ": No.HP tidak boleh kosong!,";
	    }	
    	if(Common.isEmpty(tmSales.getEmail())){
			err = err+ "  email  harus diisi,";
		}						
		if(!Common.isEmpty(tmSales.getEmail())){
			try {
				InternetAddress.parse(tmSales.getEmail().trim());
			} catch (AddressException e) {
				err = err+ "  email tidak valid,";
			} finally {
				if(!tmSales.getEmail().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					err = err+ " email tidak valid,";
				}
			}
		}
	    
    return err;	
    }
      
}
