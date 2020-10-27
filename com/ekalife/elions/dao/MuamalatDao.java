package com.ekalife.elions.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Bmi;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentDao;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Proses2 yang berhubungan dengan polis produk bank muamalat
 * 
 * @author Yusuf
 * @since Nov 19, 2008 (8:23:52 PM)
 */
@SuppressWarnings("unchecked")
public class MuamalatDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.muamalat.";
	}	
	
	public void insertMstDataBmi (Bmi bmi) throws DataAccessException{
		insert("insertMstDataBmi", bmi);
	}

	public List selectDataUntukReportBmi(List list, int lssa_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("daftar", list);
		params.put("lssa_id", lssa_id);
		return query("selectDataUntukReportBmi", params);
	}
	
	public int updateMstDataBmi (Bmi bmi) throws DataAccessException{
		//sekaligus insert history nya
		uwDao.insertMstPositionSpaj("0", "LAPORAN BANK MUAMALAT SUDAH TERKIRIM", bmi.reg_spaj, 0);
		return update("updateMstDataBmi", bmi);
	}
	
	public List<Bmi> selectDataUntukInsertBmi(String reg_spaj, String lus_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("user_input", lus_id);
		return query("selectDataUntukInsertBmi", params);
	}
	
	public List<Bmi> selectDataUntukGenerateTextFileBmi() throws DataAccessException{
		return query("selectDataUntukGenerateTextFileBmi", null);
	}
	
	/**
	 * Proses untuk insert ke EKA.MST_BMI, dipanggil saat TRANSFER KE PRINT POLIS, barengan dgn pengakuan produksi, komisi, dkk
	 * 
	 * @author Yusuf
	 * @since Nov 19, 2008 (8:23:49 PM)
	 * @param reg_spaj
	 * @throws DataAccessException
	 */
	public void saveDataBmi(String reg_spaj, User currentUser) throws DataAccessException{
		List<Bmi> daftarBmi = selectDataUntukInsertBmi(reg_spaj, currentUser.getLus_id());
		if(!daftarBmi.isEmpty()) {
			for(Bmi bmi : daftarBmi) {
				bmi.mdb_id = sequence.sequenceDataBmi();
				insertMstDataBmi(bmi);
			}
		}
	}

}