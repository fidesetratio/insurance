package produk_asuransi.produk_simpel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.ekalife.elions.model.TmSales;
import com.ekalife.utils.Common;
import com.ekalife.utils.f_hit_umur;


public class n_prod_u_11_061 extends n_prod_u_11_056 {

    private static final long serialVersionUID = -3098352306327060542L;
    
    public n_prod_u_11_061() {
        super();
    }
    
    public TmSales set_prod_u_11_061(List rowAgencyExcelList ) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                
        tmSales.setProduct_code("061");
        tmSales.setSum_insured(new BigDecimal(1000000));
        
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
        
        if(rowAgencyExcelList.size() > 8 && !Common.isEmpty(rowAgencyExcelList.get(8)))
            tmSales.setNo_akun(rowAgencyExcelList.get(8).toString());
        if(rowAgencyExcelList.size() > 9 && !Common.isEmpty(rowAgencyExcelList.get(9)))
            tmSales.setTipe_akun(rowAgencyExcelList.get(9).toString());
        if(rowAgencyExcelList.size() > 10 && !Common.isEmpty(rowAgencyExcelList.get(10)))
            tmSales.setId_customer(rowAgencyExcelList.get(10).toString());
        if(rowAgencyExcelList.size() > 11 && !Common.isEmpty(rowAgencyExcelList.get(11)))
            tmSales.setTgl_buka_akun(new SimpleDateFormat("dd/MM/yyyy").parse(rowAgencyExcelList.get(11).toString()));
        if(rowAgencyExcelList.size() > 12 && !Common.isEmpty(rowAgencyExcelList.get(12))) {
            String stsNikahExcel = "";
            Integer sts_nikah = 1;
            
            if("2".equals(stsNikahExcel) || "M".equalsIgnoreCase(stsNikahExcel) || "Menikah".equalsIgnoreCase(stsNikahExcel)
                    || "Married".equalsIgnoreCase(stsNikahExcel)) {
                sts_nikah = 2;
            } else if(("3".equals(stsNikahExcel) || "J".equalsIgnoreCase(stsNikahExcel) || "Janda".equalsIgnoreCase(stsNikahExcel)
                    || "Cerai".equalsIgnoreCase(stsNikahExcel)) && tmSales.getSex() == 0) {
                sts_nikah = 3;
            } else if(("4".equals(stsNikahExcel) || "D".equalsIgnoreCase(stsNikahExcel) || "Duda".equalsIgnoreCase(stsNikahExcel)
                    || "Cerai".equalsIgnoreCase(stsNikahExcel)) && tmSales.getSex() == 1) {
                sts_nikah = 4;
            }
            
            tmSales.setSts_nikah(sts_nikah);
        }
        if(rowAgencyExcelList.size() > 13 && !Common.isEmpty(rowAgencyExcelList.get(13)))
            tmSales.setKd_cabang(rowAgencyExcelList.get(13).toString());
        if(rowAgencyExcelList.size() > 14 && !Common.isEmpty(rowAgencyExcelList.get(14)))
            tmSales.setPekerjaan(rowAgencyExcelList.get(14).toString());
        if(rowAgencyExcelList.size() > 15 && !Common.isEmpty(rowAgencyExcelList.get(15)))
            tmSales.setTempat_lahir(rowAgencyExcelList.get(15).toString());
        if(rowAgencyExcelList.size() > 16 && !Common.isEmpty(rowAgencyExcelList.get(16)))
            tmSales.setAddress1(rowAgencyExcelList.get(16).toString());
        if(rowAgencyExcelList.size() > 17 && !Common.isEmpty(rowAgencyExcelList.get(17)))
            tmSales.setAddress2(rowAgencyExcelList.get(17).toString());
        if(rowAgencyExcelList.size() > 18 && !Common.isEmpty(rowAgencyExcelList.get(18)))
            tmSales.setHome_phone(rowAgencyExcelList.get(18).toString());
        if(rowAgencyExcelList.size() > 19 && !Common.isEmpty(rowAgencyExcelList.get(19)))
            tmSales.setWork_phone(rowAgencyExcelList.get(19).toString());
        
        // Jika email blank, set ke default email berikut
        if(Common.isEmpty(rowAgencyExcelList.get(5))) {
            tmSales.setEmail("dwidharmaprasetyo@gmail.com");
        }
        
        return tmSales;
    }
    
    public  String validate_prod_u_11_061(){
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
        
        if(tmSales.getBod_holder() != null) {
            Calendar dob = Calendar.getInstance();
            dob.setTime(tmSales.getBod_holder());
            Calendar sysdate = Calendar.getInstance();
            f_hit_umur hitUmur = new f_hit_umur();
            int umur = hitUmur.umur(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DATE), sysdate.get(Calendar.YEAR), sysdate.get(Calendar.MONTH), sysdate.get(Calendar.DATE));
            
            if(!(umur >= 17 && umur <= 59)) {
                err = err+ "  Umur Tertanggung minimal 17 tahun & maksimal 59 tahun,";
            }
        }
        
        return err; 
    }

}
