package com.ekalife.elions.process.uw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.dao.DataAccessException;
import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.ReasTempNew;
import com.ekalife.elions.model.Tertanggung2;
import com.ekalife.elions.model.reas.DataRiderLink;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReasUtilities extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );

	public void prosesInsertMReasTempNew(String begDateAwal,String begDateAkhir)throws DataAccessException{
		int count=0;
		
		//HCP (811 dan 819)
		/*
		List lsPolis=commonDao.selectDataRiderHCP(begDateAwal, begDateAkhir);
		for(int i=0;i<lsPolis.size();i++){
			Map map=(HashMap)lsPolis.get(i);
			//
//			if(((String)map.get("REG_SPAJ")).equals("01200600018"))
//				JOptionPane.showMessageDialog(null, "ss");
			List lsReas=commonDao.selectMReasTempNew((String) map.get("REG_SPAJ"),(BigDecimal)map.get("LSBS_ID"),(BigDecimal)map.get("LSDBS_NUMBER"));
			if(lsReas.size()==0){
				commonDao.insertMReasTempNew(map);
				logger.info("Berhasil Insert nospaj "+ map.get("REG_SPAJ"));
			}
			count++;
		}
		logger.info("Total yang di insert = "+count);
		*/
		
		//non hcp 812-818 tanya atik gmana untuk reas yg sebelumnya
		List lsPolisNonHcp=commonDao.selectDataRiderLinkNonHCP(begDateAwal, begDateAkhir);
		count=0;
		for(int i=0;i<lsPolisNonHcp.size();i++){
			Map map=(HashMap)lsPolisNonHcp.get(i);
			String spaj=(String)map.get("REG_SPAJ");
			BigDecimal sar=(BigDecimal)map.get("SAR");
			BigDecimal retensi,reas;
			//cek dulu ke tabel eka.m_reas_temp retensi untuk membandingkan retensi. 
			//reas=sar rider -retensi
			//jika reas>0 maka rider reas selain itu tidak di reas 
			retensi=new BigDecimal(uwDao.selectRetensiMReasTemp(spaj));
			reas=new BigDecimal(sar.doubleValue()-retensi.doubleValue());
			map.put("REAS", reas);
			map.put("RETENSI", retensi);
			if(reas.doubleValue()>0){
				//
				List lsReas=commonDao.selectMReasTempNew((String) map.get("REG_SPAJ"),(BigDecimal)map.get("LSBS_ID"),(BigDecimal)map.get("LSDBS_NUMBER"),1);
				if(lsReas.size()==0){
					commonDao.insertMReasTempNew(map);
					logger.info("Berhasil Insert nospaj "+ map.get("REG_SPAJ"));
				}
				count++;
			}	
		}
		
	}
	
	public List prosesLaporanReasRiderLinkNew(String tglAwal,String tglAkhir,Integer flag) {
		List lsHasil=new ArrayList();
		List lsSpaj=new ArrayList();
		
		if(flag==1){//laporan rider link Proses Untuk data link sebelum 14/01/2008 (sebelum flag ins=1
			lsSpaj=commonDao.selectRegSpajFromMReasTempNew();
			commonDao.updateMReasTempNewFlagIns(0);
		}else//laporan rider link berdasarkan prod date di eka.mst_production
			lsSpaj=commonDao.selectMstProductionRiderLink(tglAwal,tglAkhir);
		
		//update flag ins menjadi 0 (sudah di buat laporan)
		
		
		for(int i=0;i<lsSpaj.size();i++){
			String spaj=(String)lsSpaj.get(i);
			//query product utama;
			DataRiderLink produkUtama =commonDao.selectDataReasProdukUtama(spaj);
			Pemegang2 pemegang=akseptasiDao.selectPemegang(spaj, 4);
			produkUtama.setNama_pemegang(pemegang.getMcl_first());
			produkUtama.setAge_hld(pemegang.getMspo_age());
			produkUtama.setBirth_date_hld(pemegang.getMspe_date_birth());
			produkUtama.setSex_holder(pemegang.getMspe_sex());
			produkUtama.setMspo_policy_no(pemegang.getMspo_policy_no());
			produkUtama.setMspo_pay_period(pemegang.getMspo_pay_period());
			Tertanggung2 tertanggung=akseptasiDao.selectTertanggung(spaj, 4);
			produkUtama.setNama_tertanggung(tertanggung.getMcl_first());
			produkUtama.setAge_ins(tertanggung.getMste_age());
			produkUtama.setSex_insured(tertanggung.getMspe_sex());
			produkUtama.setBirth_date_ins(tertanggung.getMspe_date_birth());
			produkUtama.setLscb_id(pemegang.getLscb_id());
			lsHasil.add(produkUtama);
			//query product rider
			List lsRider=commonDao.selectMReasTempNew(spaj,null,null,flag);
			for(int j=0;j<lsRider.size();j++){
				DataRiderLink reasTemp=(DataRiderLink)lsRider.get(j);
				DataRiderLink produkTambahan =commonDao.selectDataReasProdukRiderLink(reasTemp);
				if(produkTambahan==null){
					JOptionPane.showMessageDialog(null,"Perbaiki data m_reas_temp_new dengan mst_product insured"+ reasTemp.getReg_spaj());
					
				}else{
					produkTambahan.setReg_spaj(null);
					lsHasil.add(produkTambahan);
				}	
			}
		}
		return lsHasil;
	}
/**
	public List prosesInsertReasKonvensional()throws Exception{
		List lsErr=new ArrayList();
		Integer li_pemegang=0;
		try{

			String fileName = "D:/Ferry/Daily Jobs/290207/Unit Link_Rider HCP_kirim reas_0207.xls";
			Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
			Connection connection = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + fileName);
			Statement statement = connection.createStatement();
			Statement statement2 = connection.createStatement();
			String query = "Select DISTINCT REG_SPAJ from MST_POLICY";
			ResultSet rs = statement.executeQuery( query );
			
			int count=0;
			Integer lstbId=1;
			Integer printCertificate=0;
			Integer jenis=0;
			String lusId="534";//ferry harlim
			Integer msrprNumber=1;
			Double ldecSimultan=new Double(0);
			
			while(rs.next()){
				String spaj=rs.getString(1);
				Map  pp=(HashMap)uwDao.selectPemegang(spaj);
//				Integer liCountYear=getCountYear(plan,)
				if(pp!=null){
					Map tt=(HashMap)uwDao.selectTertanggung(spaj);
					DataUsulan2 ds=(DataUsulan2)akseptasiDao.selectDataUsulan(spaj, lstbId);
					String queryDetail="Select * from MST_POLICY WHERE REG_SPAJ='"+spaj+"'";
					ResultSet rsDetail= statement2.executeQuery(queryDetail);
//					List lsProduct=akseptasiDao.selectMstProductInsured(spaj);
					Integer insured=(Integer)tt.get("MSTE_INSURED_NO");
					Integer age=(Integer)tt.get("MSTE_AGE");
					while(rsDetail.next()){
						Double tsi,retensi,tsiReas;
						Integer plan,typeReas,medical,lsdbsNumber;
						String lku_id;
						tsi=rsDetail.getDouble("TSI");
						retensi=rsDetail.getDouble("RETENSI");
						tsiReas=rsDetail.getDouble("TSI_REAS");
						plan=rsDetail.getInt("PLAN");
						typeReas=rsDetail.getInt("TYPE_REAS");
						medical=rsDetail.getInt("MEDICAL");
						lku_id=rsDetail.getString("CURR");
						Product produk=akseptasiDao.selectMstProductInsured(spaj,plan);
						lsdbsNumber=commonDao.selectMstProductInsuredLsdbsNumber(spaj, plan);
						//cek dulu ada di mst_reins gak, klo ada insert di eka.mst_reins_product untuk rider, jika tidak ada
						//maka insert eka.mst_reins dan eka.mst_reins_product
						if(commonDao.selectMstReinsSpaj(spaj)==null){
							Integer li_lssp,li_pos_id;
							Map policy=(HashMap)uwDao.selectMstPolicy(spaj,new Integer(1));
							li_lssp=(Integer)policy.get("LSSP_ID");
							li_pos_id=(Integer)policy.get("LSPD_ID");
							//
							if (li_lssp == 13 && li_pos_id == 95)
								li_pos_id = new Integer(100);
							else
								li_pos_id = new Integer(99);	
							
							if(typeReas!= 2)
								typeReas=uwDao.selectLstBisnisLstrId(plan);
						
							//insert reins dan reins product
							//if(rsDetail.first()){
								uwDao.insertMstReins(spaj,insured,typeReas,ds.getLsbs_id(),ds.getLsdbs_number(),0,1,
										ds.getMspr_beg_date(),ds.getMspr_beg_date(),medical,lstbId,new Integer(0),li_pos_id,lusId,jenis);
							//}
						}else{
							Integer hitPremi;
							//
							Integer li_reas_client=uwDao.selectLstDetBisnisLsdbsReasClient(produk.getLsbs_id(),produk.getLsdbs_number());
							//if(li_reas_client==null){
								lsErr.add("Empty Jenis Client Reas (Error)");
						//	}
							
							if( (li_reas_client==1 && li_pemegang==0 ) || ( li_reas_client >= 2 && li_pemegang==1)  ) {
								hitPremi = 1;
							}else if (produk.getLsbs_id().intValue()< 900 ){
								hitPremi = 1;
							}else
								hitPremi = 0;
//							tahun ke lapse begen date
//							trus di yosep juga harus proses 
//							untuk nentuin type reas dan bla2 
//							jikalau polis ini belum di proses sama sekali
//							jika sudah, hitung renewal nya.
							
							Integer liCountYear=produk.getMspr_ins_period();
							Integer liThKe= new Integer(uwDao.selectTahunKe(defaultDateFormat.format(produk.getMspr_beg_date()),defaultDateFormat.format(ds.getMspr_beg_date())).intValue());
							Double ldecSar=uwDao.selectGetSar(lstbId, produk.getLsbs_id(),produk.getLsdbs_number(), produk.getLku_id(), ds.getLscb_id(), ds.getMspo_pay_period(), ds.getMspo_ins_period(),liThKe, age);
							uwDao.insertMstReinsProduct(spaj, insured, plan, lsdbsNumber, msrprNumber, lku_id, ldecSimultan, tsi, ldecSar,
									retensi, tsiReas, null, null, new Double(0), new Double(0),produk.getMspr_beg_date() ,produk.getMspr_end_date() , li_pemegang, hitPremi, liCountYear);
						}	
					}
					//rsDetail.close();

				}	
				count++;
			}	
			rs.close();
				statement.close();
				statement2.close();
				connection.close();
			
            
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
		return lsErr;
	}
		*/
	public Map prosesInsertReasRiderLinkToReins()throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List lsErr=new ArrayList();
		List lsReport=new ArrayList();
		Integer li_pemegang=0;
		int count=0;
		Integer lstbId=1;
		Integer printCertificate=0;
		Integer jenis=0;
		String lusId="534";//ferry harlim
		Integer msrprNumber=1;
		Double ldecSimultan=new Double(0);
		Integer typeReas,age;
		Integer liReas=1,liBackup=2;
		//List lsSpaj=uwDao.selectMReasTempNew("13200600224");
		List lsSpaj=uwDao.selectMReasTempNewNotInsertToReins();
		for(int i=0;i<lsSpaj.size();i++){
			ReasTempNew reasTemp=(ReasTempNew)lsSpaj.get(i);
			Map  pp=(HashMap)uwDao.selectPemegang(reasTemp.getReg_spaj());
			if(pp!=null){
				Map tt=(HashMap)uwDao.selectTertanggung(reasTemp.getReg_spaj());
				age=(Integer)tt.get("MSTE_AGE");
				DataUsulan2 ds=(DataUsulan2)akseptasiDao.selectDataUsulan(reasTemp.getReg_spaj(), lstbId);
				Product produk=akseptasiDao.selectMstProductInsuredDetail(reasTemp.getReg_spaj(),reasTemp.getLsbs_id(),reasTemp.getLsdbs_number());
				
				//cek dulu ada di mst_reins gak, klo ada insert di eka.mst_reins_product untuk rider, jika tidak ada
				//maka insert eka.mst_reins dan eka.mst_reins_product
				if(produk==null){
					lsErr.add(reasTemp.getReg_spaj());
					continue;
				}
				if(commonDao.selectMstReinsSpaj(reasTemp.getReg_spaj())==null){
					Integer li_lssp,li_pos_id;
					Map policy=(HashMap)uwDao.selectMstPolicy(reasTemp.getReg_spaj(),new Integer(1));
					li_lssp=(Integer)policy.get("LSSP_ID");
					li_pos_id=(Integer)policy.get("LSPD_ID");
					//
					if (li_lssp == 13 && li_pos_id == 95)
						li_pos_id = new Integer(100);
					else
						li_pos_id = new Integer(99);	

					typeReas=uwDao.selectLstBisnisLstrId(reasTemp.getLsbs_id());
				
					//insert reins dan reins product
					//if(rsDetail.first()){
						uwDao.insertMstReins(reasTemp.getReg_spaj(),reasTemp.getMste_insured_no(),typeReas,ds.getLsbs_id(),ds.getLsdbs_number(),0,1,
								ds.getMspr_beg_date(),ds.getMspr_beg_date(),ds.getMste_medical(),lstbId,new Integer(0),li_pos_id,lusId,jenis);
					//}
				}
				
					Integer hitPremi;
					//
					
					Integer li_reas_client=uwDao.selectLstDetBisnisLsdbsReasClient(produk.getLsbs_id(),produk.getLsdbs_number());
					
					if( (li_reas_client==1 && li_pemegang==0 ) || ( li_reas_client >= 2 && li_pemegang==1)  ) {
						hitPremi = 1;
					}else if (produk.getLsbs_id().intValue()< 900 ){
						hitPremi = 1;
					}else
						hitPremi = 0;
//					tahun ke lapse begen date
//					jikalau polis ini belum di proses sama sekali
//					trus di yosep juga harus proses 
//					untuk nentuin type reas dan bla2 
//					jika sudah, hitung renewal nya.
					
					Integer liCountYear=produk.getMspr_ins_period();
					Integer liThKe= new Integer(uwDao.selectTahunKe(defaultDateFormat.format(produk.getMspr_beg_date()),defaultDateFormat.format(ds.getMspr_beg_date())).intValue());
					Double ldecSar=uwDao.selectGetSar(lstbId, produk.getLsbs_id(),produk.getLsdbs_number(), produk.getLku_id(), ds.getLscb_id(), ds.getMspo_pay_period(), ds.getMspo_ins_period(),liThKe, age);
					//
					if(uwDao.selectCountMstReinsProduct(reasTemp.getReg_spaj(), reasTemp.getLsbs_id(), reasTemp.getLsdbs_number(), reasTemp.getMste_insured_no())==0){
					uwDao.insertMstReinsProduct(reasTemp.getReg_spaj(), reasTemp.getMste_insured_no(), reasTemp.getLsbs_id(), reasTemp.getLsdbs_number(), msrprNumber, reasTemp.getLku_id(), ldecSimultan, reasTemp.getTsi(), ldecSar,
							reasTemp.getRetensi(), reasTemp.getReas(), null, null, new Double(0), new Double(0),produk.getMspr_beg_date() ,produk.getMspr_end_date() , li_pemegang, hitPremi, liCountYear);
					uwDao.updateMstInsuredReasnBackup(reasTemp.getReg_spaj(), 1, liReas, liBackup, null, null);
					logger.info("TElah di lakukan proses ke="+i);
					lsReport.add(reasTemp.getReg_spaj());
					
					}
			}
				
		}
		Map param=new HashMap();
		param.put("lsReport", lsReport);
		param.put("lsErr", lsErr);
		return param;
	}
}
