package com.ekalife.elions.process.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Simultan extends ParentDao{	
	/**Fungsi : Untuk Proses simultan polis dimana 
	 * 			untuk Untuk proses simultannya berbeda dengan yg sebelumnya.
	 * 			telah ditambahkan id simultan baru. sehingga untuk semua polis yang simultan maka
	 * 			akan mempunyai id simultan yang sama, dan untuk mcl_id itu berbeda. 
	 * 			tabel eka.mst_simultaneous (id_simultan format MMYYY+counter(5 digit)
	 * @param proses
	 * @param spaj
	 * @param lcaId
	 * @param lsClientPpOld
	 * @param lsClientTtOld
	 * @return
	 * @throws Exception
	 * @author Ferry Harlim 07/11/2007
	 */	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public Integer prosesSimultan(Command command,String lsClientPpOld,String lsClientTtOld,String idSimultanPp,String idSimultanTt)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lsClientPpNew = null,lsClientTtNew=null;String lsClientPmNew=null;
		String lsClientPp = null,lsClientTt=null;//String lsClientPm =null;
		//String lsClientPmOld = bacDao.selectMspoPayerOld(command.getSpaj());
		
		Integer insured=1;
		//hubungan diri sendiri mapu
		if(command.getLsreIdPp()==1){//hubungan diri sendiri
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
					lsClientPpNew = lsClientPpOld;
				}else{
					
					lsClientPpNew=uwDao.wf_get_client_id(command.getLcaIdPp());
					if(lsClientPpNew==null){
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						return null;
					}
					//
					Client client=new Client();
					AddressNew addressNew=new AddressNew();
					Personal personal = new Personal();
					ContactPerson contactPerson = new ContactPerson();
					client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
					addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
					uwDao.insertMstClientNew(client);
					uwDao.insertMstAddressNew(addressNew);
					uwDao.updateMstPolicyMspoPolicyHolder(command.getSpaj(),lsClientPpNew);
					uwDao.updateMstInsuredMsteInsured(command.getSpaj(),lsClientPpNew);
		//			bacDao.updateMstPolicyMspoPayer(command.getSpaj(), lsClientPpNew);
		//			 tambahan untuk badan usaha
								
					if(client.getMcl_jenis() == 1){
						personal = commonDao.selectProfilePerusahaan(lsClientPpOld);
						contactPerson = bacDao.selectpic(lsClientPpOld);
						personal.setMcl_id(lsClientPpNew);
						contactPerson.setMcl_id(lsClientPpNew);
						bacDao.insertMstCompanyId(personal, lsClientPpOld);
						bacDao.insertMstCompanyContactId(contactPerson, lsClientPpOld);
						bacDao.updateMstCompanyContactAddressId(contactPerson, lsClientPpOld);
						delete("delete.mst_company_contact_family", lsClientPpOld);
						String nama_suamiistri = contactPerson.getNama_si();
						if(nama_suamiistri == null)nama_suamiistri = "";
						if (!nama_suamiistri.equalsIgnoreCase(""))
						{
							Date tanggal_lahir_suamiistri = contactPerson.getTgllhr_si();
							Map param1=new HashMap();
							param1.put("mcl_id", lsClientPpNew);
							param1.put("nama", contactPerson.getNama_si());
							param1.put("lsre_id",5);
							param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
							param1.put("insured", 1);
							param1.put("no", 0);
							bacDao.insertMstCompanyContactFamily(param1);
							//logger.info("insert mst keluarga");
						}
						
						String nama_anak1 = contactPerson.getNama_anak1();
						if(nama_anak1 == null)nama_anak1 = "";
						if (!nama_anak1.equalsIgnoreCase(""))
						{
							Date tanggal_lahir_anak1 = contactPerson.getTgllhr_anak1();
							Map param1=new HashMap();
							param1.put("mcl_id", lsClientPpNew);
							param1.put("nama", contactPerson.getNama_anak1());
							param1.put("lsre_id",4);
							param1.put("tanggal_lahir", tanggal_lahir_anak1);
							param1.put("insured", 1);
							param1.put("no", 1);
							bacDao.insertMstCompanyContactFamily(param1);
							//logger.info("insert mst keluarga");
						}
						
						String nama_anak2 = contactPerson.getNama_anak2();
						if(nama_anak2 == null)nama_anak2 = "";
						if (!nama_anak2.equalsIgnoreCase(""))
						{
							Date tanggal_lahir_anak2 = contactPerson.getTgllhr_anak2();
							Map param1=new HashMap();
							param1.put("mcl_id", lsClientPpNew);
							param1.put("nama", contactPerson.getNama_anak2());
							param1.put("lsre_id",4);
							param1.put("tanggal_lahir", tanggal_lahir_anak2);
							param1.put("insured", 1);
							param1.put("no", 2);
							bacDao.insertMstCompanyContactFamily(param1);
							//logger.info("insert mst keluarga");
						}
						
						String nama_anak3 = contactPerson.getNama_anak2();
						if(nama_anak3 == null)nama_anak3 = "";
						if (!nama_anak3.equalsIgnoreCase(""))
						{
							Date tanggal_lahir_anak3 = contactPerson.getTgllhr_anak3();
							Map param1=new HashMap();
							param1.put("mcl_id", lsClientPpNew);
							param1.put("nama", contactPerson.getNama_anak3());
							param1.put("lsre_id",4);
							param1.put("tanggal_lahir", tanggal_lahir_anak3);
							param1.put("insured", 1);
							param1.put("no", 3);
							bacDao.insertMstCompanyContactFamily(param1);
							//logger.info("insert mst keluarga");
						}
						bacDao.deleteMstCompanyContactId(contactPerson, lsClientPpOld);
						bacDao.deleteMstCompanyId(personal, lsClientPpOld);
					}
				}
			//===
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientPpNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
				idSimultanTt=idSimultanPp;
			}
			lsClientTtNew=lsClientPpNew;
		}else{//hubungan orang lain
			//pemegang
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
				lsClientPpNew = lsClientPpOld;
				lsClientTtNew = lsClientTtOld;
			}else{
				lsClientPpNew=uwDao.wf_get_client_id(command.getLcaIdPp());
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				Personal personal = new Personal();
				ContactPerson contactPerson = new ContactPerson();
				client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				uwDao.insertMstClientNew(client);
				uwDao.insertMstAddressNew(addressNew);
				uwDao.updateMstPolicyMspoPolicyHolder(command.getSpaj(),lsClientPpNew);
	//			 tambahan untuk badan usaha
				if(client.getMcl_jenis() == 1){
					personal = commonDao.selectProfilePerusahaan(lsClientPpOld);
					contactPerson = bacDao.selectpic(lsClientPpOld);
					personal.setMcl_id(lsClientPpNew);
					contactPerson.setMcl_id(lsClientPpNew);
					bacDao.insertMstCompanyId(personal, lsClientPpOld);
					bacDao.insertMstCompanyContactId(contactPerson, lsClientPpOld);
					bacDao.updateMstCompanyContactAddressId(contactPerson, lsClientPpOld);
					bacDao.deleteMstCompanyContactFamily(lsClientPpOld);
					String nama_suamiistri = contactPerson.getNama_si();
					if(nama_suamiistri == null)nama_suamiistri = "";
					if (!nama_suamiistri.equalsIgnoreCase(""))
					{
						Date tanggal_lahir_suamiistri = contactPerson.getTgllhr_si();
						Map param1=new HashMap();
						param1.put("mcl_id", lsClientPpNew);
						param1.put("nama", contactPerson.getNama_si());
						param1.put("lsre_id",5);
						param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
						param1.put("insured", 1);
						param1.put("no", 0);
						bacDao.insertMstCompanyContactFamily(param1);
						//logger.info("insert mst keluarga");
					}
					
					String nama_anak1 = contactPerson.getNama_anak1();
					if(nama_anak1 == null)nama_anak1 = "";
					if (!nama_anak1.equalsIgnoreCase(""))
					{
						Date tanggal_lahir_anak1 = contactPerson.getTgllhr_anak1();
						Map param1=new HashMap();
						param1.put("mcl_id", lsClientPpNew);
						param1.put("nama", contactPerson.getNama_anak1());
						param1.put("lsre_id",4);
						param1.put("tanggal_lahir", tanggal_lahir_anak1);
						param1.put("insured", 1);
						param1.put("no", 1);
						bacDao.insertMstCompanyContactFamily(param1);
						//logger.info("insert mst keluarga");
					}
					
					String nama_anak2 = contactPerson.getNama_anak2();
					if(nama_anak2 == null)nama_anak2 = "";
					if (!nama_anak2.equalsIgnoreCase(""))
					{
						Date tanggal_lahir_anak2 = contactPerson.getTgllhr_anak2();
						Map param1=new HashMap();
						param1.put("mcl_id", lsClientPpNew);
						param1.put("nama", contactPerson.getNama_anak2());
						param1.put("lsre_id",4);
						param1.put("tanggal_lahir", tanggal_lahir_anak2);
						param1.put("insured", 1);
						param1.put("no", 2);
						bacDao.insertMstCompanyContactFamily(param1);
						//logger.info("insert mst keluarga");
					}
					
					String nama_anak3 = contactPerson.getNama_anak2();
					if(nama_anak3 == null)nama_anak3 = "";
					if (!nama_anak3.equalsIgnoreCase(""))
					{
						Date tanggal_lahir_anak3 = contactPerson.getTgllhr_anak3();
						Map param1=new HashMap();
						param1.put("mcl_id", lsClientPpNew);
						param1.put("nama", contactPerson.getNama_anak3());
						param1.put("lsre_id",4);
						param1.put("tanggal_lahir", tanggal_lahir_anak3);
						param1.put("insured", 1);
						param1.put("no", 3);
						bacDao.insertMstCompanyContactFamily(param1);
						//logger.info("insert mst keluarga");
					}
					bacDao.deleteMstCompanyContactId(contactPerson, lsClientPpOld);
					bacDao.deleteMstCompanyId(personal, lsClientPpOld);
				}
				//===
				//tertanggung
				lsClientTtNew=uwDao.wf_get_client_id(command.getLcaIdPp());
				if(lsClientTtNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client2=new Client();
				AddressNew addressNew2=new AddressNew();
				client2=uwDao.selectMstClientNew(lsClientTtNew,lsClientTtOld);
				addressNew2=uwDao.selectMstAddressNew(lsClientTtNew,lsClientTtOld);
				uwDao.insertMstClientNew(client2);
				uwDao.insertMstAddressNew(addressNew2);
				uwDao.updateMstInsuredMsteInsured(command.getSpaj(),lsClientTtNew);
				
			}
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientTtNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
			}
			if(idSimultanTt==null){//tidak simultan
				idSimultanTt=uwDao.createSimultanId();
			}
			
//			if (lsClientPmOld != null){
//				
//			//pembayar premi
//			lsClientPmNew= uwDao.wf_get_client_id(command.getLcaIdPp());
//			if(lsClientPmNew==null){
//				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//				return null;
//			}
//				Client client3 = new Client();
//				AddressNew addressNew3 = new AddressNew();
//				client3 = uwDao.selectMstClientNew(lsClientPmNew, lsClientPmOld);
//				addressNew3 = uwDao.selectMstAddressNew(lsClientPmNew, lsClientPmOld);
//				uwDao.insertMstClientNew(client3);
//				uwDao.insertMstAddressNew(addressNew3);
//				bacDao.updateMstPolicyMspoPayer(command.getSpaj(), lsClientPmNew);
//			}
			
		}

		uwDao.wf_sts_client(lsClientPp,new Integer(1));
		uwDao.wf_sts_client(lsClientTt,new Integer(1));
		
		
		uwDao.wfInsSimultanNew(command.getSpaj(), lsClientTtNew, lsClientPpNew,insured,idSimultanPp,idSimultanTt);
		
		return new Integer(0);
	}
	/**Fungsi:	Untuk Memproses Simultan Ulang polis yang salah memilih ID simultannya.
	 * 			padahal polis tsb tidak simultan dgn ID simultan yang dipilih.
	 * 			tahapan: 1. update Client ID yg baru dgn yg lama (WW),
	 * 					 2. hapus di eka.mst_simultanenous dengan filter reg_spaj yang salah.(2 row deleted)
	 * 					 3. jalankan prosesSimultanUlang pada saat proses simultan
	 * @param command
	 * @param lsClientPpOld
	 * @param lsClientTtOld
	 * @param idSimultanPp
	 * @param idSimultanTt
	 * @return Integer
	 * @throws Exception
	 */
	public Integer prosesSimultanUlang(Command command,String lsClientPpOld,String lsClientTtOld,String idSimultanPp,String idSimultanTt)throws Exception{
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lsClientPpNew = null,lsClientTtNew=null;
		String lsClientPp = null,lsClientTt=null;
		Integer insured=1;
		//hubungan diri sendiri mapu
		if(command.getLsreIdPp()==1){//hubungan diri sendiri
			//masukan ID yang telah di generate
			lsClientPpNew="240000004376";
			//
			uwDao.updateMstPolicyMspoPolicyHolder(command.getSpaj(),lsClientPpNew);
			uwDao.updateMstInsuredMsteInsured(command.getSpaj(),lsClientPpNew);
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientPpNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
				idSimultanTt=idSimultanPp;
			}
			lsClientTtNew=lsClientPpNew;
		}else{//hubungan orang lain
			//pemegang
			//ganti id tsb dengan id yang ada pada policy dan insured
			lsClientPpNew="XXXXXXXXXX";
			lsClientTtNew="XXXXXXXXXX";
			//pemegang
			uwDao.updateMstPolicyMspoPolicyHolder(command.getSpaj(),lsClientPpNew);
			//tertanggung
			uwDao.updateMstInsuredMsteInsured(command.getSpaj(),lsClientTtNew);

			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientTtNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
			}
			if(idSimultanTt==null){//tidak simultan
				idSimultanTt=uwDao.createSimultanId();
			}

		}

		
		uwDao.wfInsSimultanNew(command.getSpaj(), lsClientTtNew, lsClientPpNew,insured,idSimultanPp,idSimultanTt);
		
		return new Integer(0);
	}
	
	//ada beberapa modifikasi untuk simple bac dari yang original
	public Integer prosesSimultanSimpleBac(Command command,String lsClientPpOld,String lsClientTtOld,String idSimultanPp,String idSimultanTt)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lsClientPpNew = null,lsClientTtNew=null;
		String lsClientPp = null,lsClientTt=null;
		Integer insured=1;
				
		//hubungan diri sendiri mapu
		if(command.getLsreIdPp()==1){//hubungan diri sendiri
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00")){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
				lsClientPpNew = lsClientPpOld;
			}else{
				lsClientPpNew=uwDao.wf_get_client_id(command.getLcaIdPp());
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				//
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				uwDao.insertMstClientNew(client);
				uwDao.insertMstAddressNew(addressNew);
			}
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientPpNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
				idSimultanTt=idSimultanPp;
			}
			lsClientTtNew=lsClientPpNew;
		}else{//hubungan orang lain
			//pemegang
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
				lsClientPpNew = lsClientPpOld;
				lsClientTtNew = lsClientTtOld;
			}else{
				lsClientPpNew=uwDao.wf_get_client_id(command.getLcaIdPp());
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				Personal personal = new Personal();
				ContactPerson contactPerson = new ContactPerson();
				client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				uwDao.insertMstClientNew(client);
				uwDao.insertMstAddressNew(addressNew);
				//tertanggung
				lsClientTtNew=uwDao.wf_get_client_id(command.getLcaIdPp());
				if(lsClientTtNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client2=new Client();
				AddressNew addressNew2=new AddressNew();
				client2=uwDao.selectMstClientNew(lsClientTtNew,lsClientTtOld);
				addressNew2=uwDao.selectMstAddressNew(lsClientTtNew,lsClientTtOld);
				uwDao.insertMstClientNew(client2);
				uwDao.insertMstAddressNew(addressNew2);
			}
			
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientTtNew;
			if(idSimultanPp==null){//tidak simultan
				idSimultanPp=uwDao.createSimultanId();
			}
			if(idSimultanTt==null){//tidak simultan
				idSimultanTt=uwDao.createSimultanId();
			}

		}

		uwDao.wf_sts_client(lsClientPp,new Integer(1));
		uwDao.wf_sts_client(lsClientTt,new Integer(1));
		
		
		uwDao.wfInsSimultanNew(command.getReg_spaj(), lsClientTtNew, lsClientPpNew,insured,idSimultanPp,idSimultanTt);
		
		//update mst pas sms (bac simple)
		//cek apakah ada di pas
			List<Pas> pasList = uwDao.selectAllPasList(command.getSpaj(), null, null, null, null, null, null, "simple_business",null,null,null);
			//kalau ada datanya
			if(pasList.size() > 0){
				Pas pas = pasList.get(0);
				uwDao.updateSimpleBacSimultan(pas.getMsp_id(), lsClientTtNew, lsClientPpNew);
				uwDao.insertMstPositionSpajPas(command.getCurrentUser().getLus_id(), "SIMULTAN:PP "+lsClientPpOld+"-"+lsClientPpNew+" & TT "+lsClientTtOld+"-"+lsClientTtNew, pas.getMsp_fire_id(), 5);
			}
		//-------
		
		return new Integer(0);
	}
	
	
}
