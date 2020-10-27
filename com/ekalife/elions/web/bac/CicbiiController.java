package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ReffBii;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller untuk update CIC bii
 */
public class CicbiiController extends ParentFormController{

		protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
			String status = ServletRequestUtils.getStringParameter(request, "status", ServletRequestUtils.getStringParameter(request, "sts", ""));
			ReffBii datautama = (ReffBii)cmd;
			Map map = new HashMap();
			datautama.setReg_spaj(spaj);
			datautama.setStatus(status);
			datautama.setHit_err(new Integer(0));

			if (datautama.getLcc_no()!=null)
			{
				Map data = (HashMap)this.elionsManager.select_cic1(FormatString.rpad("0",datautama.getLcc_no(),3));
				if (data!=null)
				{		
					datautama.setLcc_no(FormatString.rpad("0",(String)data.get("LCC_NO"),3));
					datautama.setNama_cabang((String)data.get("NAMA_CABANG"));
					datautama.setLevel_id((String)data.get("LEVEL_ID"));
					datautama.setHead_no((String)data.get("HEAD_NO"));
				}
			}	

			if (datautama.getLrc_id()!=null)
			{
				Map data = (HashMap)this.elionsManager.select_reff_cic1(datautama.getLrc_id());
				if (data!=null)
				{		
					datautama.setLrc_id((String)data.get("LRC_ID"));
					datautama.setLrc_nama((String)data.get("LRC_NAMA"));
					datautama.setLrc_atas_nama((String)data.get("LRC_ATAS_NAMA"));
					datautama.setLrc_bank((String)data.get("LRC_BANK"));
					datautama.setLrc_no_rek((String)data.get("LRC_NO_REK"));
					datautama.setLrc_aktif((String)data.get("LRC_AKTIF"));
					if (((String)data.get("LRC_AKTIF")).equals("1"))
					{
						datautama.setAktif("AKTIF");
					}else{
						datautama.setAktif("TIDAK AKTIF");
					}
				}
			}
			return map;
		}	
				
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			ReffBii datautama = new ReffBii();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
			String status = ServletRequestUtils.getStringParameter(request, "status", ServletRequestUtils.getStringParameter(request, "sts", ""));

			datautama.setReg_spaj(spaj);
			datautama.setStatus(status);
			datautama = (ReffBii)this.elionsManager.selectmst_reff_cic(spaj);
			if (datautama==null)
			{
				datautama = new ReffBii();
			}

			return  datautama;
		}
		
		protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
			ReffBii datautama = (ReffBii)cmd;
			datautama.setHit_err(new Integer(0));
			if (datautama.getStatus().equalsIgnoreCase("insert"))
			{
				if (datautama.getLrc_id()==null)
				{
					datautama.setLrc_id("");
				}
				if (datautama.getLcc_no()==null)
				{
					datautama.setLcc_no("");
				}
				if (datautama.getLrc_id().equalsIgnoreCase(""))
				{
					datautama.setHit_err(new Integer(1));
					datautama.setStatus("awal");
					errors.rejectValue("lrc_id","","Silahkan cari nama refferer terlebih dahulu.");
				}
				if (datautama.getLcc_no().equalsIgnoreCase(""))
				{
					datautama.setStatus("awal");
					datautama.setHit_err(new Integer(1));
					errors.rejectValue("lcc_no","","Silahkan cari nama BANK CIC terlebih dahulu.");
				}
			}
		}
		
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
			ReffBii datautama = (ReffBii)cmd;
			String spaj = datautama.getReg_spaj();
			Integer status_polis = this.elionsManager.selectPositionSpaj(datautama.getReg_spaj());
			if (status_polis.intValue() != 5)
			{
		
				/**
				 * @author HEMILDA
				 * insert cic bii
				 */
				if (datautama.getStatus().equalsIgnoreCase("insert"))
				{
					Integer[] agent_id;
					Integer[] agent_lev;
					agent_id = new Integer[3];
					agent_lev= new Integer[3];
					String ls_cb_no = FormatString.rpad("0",datautama.getLcc_no(),3);
					String ls_leader = ls_cb_no;
					String li_level="";
					Integer li_cnt = new Integer(0);
					
					for (int i = 2 ;i>= 1;--i)
					{	
						ls_cb_no = ls_leader;
						Map data = (HashMap)this.elionsManager.select_cabang_cic(ls_cb_no);
						if (data!=null)
						{
							li_level = (String)data.get("LEVEL_ID");
							ls_leader = (String)data.get("HEAD_NO");
							li_cnt = new Integer(li_cnt.intValue()+1);
							agent_id[li_cnt.intValue()] = new Integer(Integer.parseInt(ls_cb_no));
							agent_lev[li_cnt.intValue()] = new Integer(3 - li_cnt.intValue());
							while ( Integer.parseInt(li_level) < i)
							{
								li_cnt = new Integer(li_cnt.intValue()+1);
								agent_id[li_cnt.intValue()] = agent_id[li_cnt.intValue() - 1];
								agent_lev[li_cnt.intValue()] = new Integer(3 - li_cnt.intValue());
								i = i-1;
							}
						}
					}
	
					/**
					 * @author HEMILDA
					 * delete cic bii
					 */
					this.elionsManager.deletemstreff_cic(datautama.getReg_spaj());
					
					/**
					 * @author HEMILDA
					 * insert cic bii lebih dari 1
					 */
					for ( int i= 1  ;i<= 2 ; ++i)
					{
						this.elionsManager.insertmst_reff_cic(datautama.getReg_spaj(),Integer.toString(agent_lev[i].intValue()),FormatString.rpad("0",Integer.toString(agent_id[i].intValue()),3),datautama.getLrc_id());
						//logger.info("insert reff cic " +i);
						datautama.setStatussubmit("1");
					}
				}
				datautama.setStatus("awal");
			}else{
				/**
				 * @author HEMILDA
				 * kalau sudah diaksep tidak bisa diubah
				 */
				datautama = new ReffBii();
				datautama.setStatussubmit("5");
				datautama.setReg_spaj(spaj);
				err.rejectValue("reg_spaj","","Spaj ini sudah diaksep, tidak bisa diedit lagi.");
			}
			return new ModelAndView("bac/reff_bii_cic","cmd", datautama).addAllObjects(this.referenceData(request,cmd,err));
		}
		
		protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
	            HttpServletResponse response)
				throws Exception{
			return new ModelAndView("common/duplicate");
		}

}