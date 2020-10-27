package com.ekalife.utils.scheduler.unused;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * memproses 30 hari komisi (khusus softcopy) setiap harinya 
 * 
 * @author Yusuf
 * @since May 30, 2008 (1:31:22 PM)
 */
public class KomisiScheduler extends ParentScheduler{

	//main method
	public void main() throws Exception{
        User currentUser = new User();
        currentUser.setLus_id("0");
        currentUser.setName("E-Lions");
        currentUser.setDept("IT");
        Map map = new HashMap();
        ServletRequestDataBinder a = new ServletRequestDataBinder(map, "cmd");
        String hasil = "";
        try {
    		hasil = this.elionsManager.transferTandaTerimaPolisToKomisiOrFilling30Hari(currentUser, a.getErrors());
        }catch(Exception e) {
        	email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {props.getProperty("admin.yusuf")}, null, null, "ERROR PROSES KOMISI 30 HARI", e.toString(), null);
        }finally {
        }
	}

}