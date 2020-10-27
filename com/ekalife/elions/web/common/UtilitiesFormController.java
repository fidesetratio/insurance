package com.ekalife.elions.web.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.DetUlink;
import com.ekalife.elions.model.TransUlink;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**	@author Ferry Harlim
 *	@since 18/12/2006
 *	Class ini berfungsi sebagai utilities tambahan untuk memperbaiki kesalahan-kesalahan
 *	yang sering terjadi seperti : Data Tertanggung Tidak muncul yang diakibatkan oleh
 *	ada kelemahan di triger, Kesalahan User setelah aksep tidak memperhatikan status medis peserta polis
 *	sehingga untuk melakukan pengeditan status medis, harus di reas ulang. Dan Pengeditan Nilai Det ulink
 *	setelah di lakukan proses FUnd Allocation.
 *	Untuk semua proses ini akan dilakukan documentasi setelah proses selesai, langsung di kirim email ke 
 *	User (ferryh@ekalfie.co.id) documentasi data sebelum dan sesudah dan dilampirkan user_id yang meminta.
 *	Penginputtan di eka.lst_ulangan sebagai history polis.
 */
public class UtilitiesFormController extends ParentFormController{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		if(currentUser.getLus_id().indexOf(props.getProperty("utilities.akses"))>0){
			command.setI(1);
		}
		command.setFlagId("");
		return command;
	}
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		String userReq=request.getParameter("userReq");
		Integer aksep=ServletRequestUtils.getIntParameter(request,"aksep",0);
		//cekin global
		if(command.getFlagId().equals("")){
			err.reject("","Silahkan pilih utilities di atas");
		}else if(userReq.equals("")){
			err.reject("","Silahkan Pilih User yang MenginginKan Perubahan");
		}
		Map mTertanggung=elionsManager.selectTertanggung(command.getSpaj());
		if(mTertanggung==null){
			err.reject("","No SPAJ Tidak Terdaftar");
		}else{
			Map mPosisi = elionsManager.selectCheckPosisi(command.getSpaj());
			Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
			if(lspdId!=2){
			//	err.reject("","Posisi Polis ada di "+mPosisi.get("LSPD_POSITION"));
			}
		}
		//flagId 1=Benerin Tertanggung 2=Ubah Medis setelah reas  3=Edit Spaj Setelah FUND ALOCATION
		if(command.getFlagId().equals("2")){
			List lsReas=elionsManager.selectMReasTemp(command.getSpaj());
			if(lsReas.isEmpty()){
				err.reject("","Tidak Bisa di Proses Ubah Medis Setelah Reas, Reas Belum pernah Dilakukan");
			}
		}else if(command.getFlagId().equals("3")){
			command.setError(1);
			List lsTrans=elionsManager.selectAllMstTransUlink(command.getSpaj());
			if(command.getCount()==null){
				command.setLsDetUlink(elionsManager.selectAllMstDetUlink(command.getSpaj(),null));
				command.setLsTransUlink(lsTrans);
				command.setLsNab(elionsManager.selectJnsLinkAndNab(command.getSpaj(), null));
			}else{
				command.setLsTransUlinkNew(null);
				List<TransUlink> lsTransNew=new ArrayList<TransUlink>();
				Double mduJumlah[]=new Double[lsTrans.size()];
				Double mtuNab[]=new Double[lsTrans.size()];
				String ljiId[]=new String[command.getLsNab().size()];
					
				
				for(int i=0;i<command.getLsNab().size();i++){
					TransUlink tNab=(TransUlink)command.getLsNab().get(i);
					ljiId[i]=tNab.getLji_id();
				}
				int count=0;
				NumberFormat four=new DecimalFormat("#.0000;(#.0000)");
				List lsDetUlinkPerJns=elionsManager.selectAllMstDetUlink(command.getSpaj(),null);
				int size=lsDetUlinkPerJns.size();
//				logika untuk ada topup atau penambahan masih salah
//				jalankan program lalu lihat hasilnya
				/* untuk jenis alokasi dana itu gak bisa dirubah tetapi nilai atau persesnnya dapat di rubah
				 * 		Untuk Jenis Penamabhan dana itu dapat dilakukan, klo sebelumua belom ada, maka selanjutnya bisa kok
				 * 		cekin lagi untuk jenis investasi nya sama tidak dengan sebelumnya, cek berdasarkan eka.mst_trans_ulink dengan
				 * 		eka.mst_det_ulink yang terbaru
				 **/

				if (size!=lsTrans.size()){
					err.reject("","jumlah Jenis Alokasi Dana tidak sama,Silahkan Cross Check Data ");
				}else{
					//cekin jenis alokasi beruabah atao tidak
					//for(int lsDetUlink dengan lsTransUlink bedasarkan)
					int check=1;
					List lsDetUlinkCheck=elionsManager.selectDistinctMstDetUlinkLjiId(command.getSpaj());
					List lsTransUlinkCheck=elionsManager.selectDistinctMstTransUlinkLjiId(command.getSpaj());
					for(int i=0;i<lsDetUlinkCheck.size();i++){
						Integer ljiIdDetUlink=(Integer)lsDetUlinkCheck.get(i);
						Integer ljiIdTransUlink=(Integer)lsTransUlinkCheck.get(i);
							if(ljiIdDetUlink.compareTo(ljiIdTransUlink)!=0)
								check=0;
					}
					if(check==0){
						err.reject("","Telah dilakukan perubahan jenis Alokasi Dana Tidak Bisa di proses");
					}else{
//						for(int i=0;i<command.getLsNab().size();i++){
//							}	
						for(int j=0;j<lsDetUlinkPerJns.size();j++){
							TransUlink tNab=(TransUlink)command.getLsNab().get(j);
							size=size+1;
							Double mtu_unit[]=new Double[size];
							Double mtu_saldo_unit[]=new Double[size];
							Double mtu_saldo_unit_pp[]=new Double[size];
							Double mtu_saldo_unit_tu[]=new Double[size];
					
							TransUlink transUlinkNew=(TransUlink)lsTrans.get(j);
							DetUlink dUlink=(DetUlink)lsDetUlinkPerJns.get(j);
							if(dUlink.getMdu_persen()!=0 && dUlink.getLji_id().equals(tNab.getLji_id()) ){
								mduJumlah[j]=dUlink.getMdu_jumlah();
								mtuNab[j]=tNab.getMtu_nab();
								logger.info(mduJumlah[j]);
								logger.info(mtuNab[j]);
								logger.info(mduJumlah[j]/mtuNab[j]);
								logger.info(four.format(mduJumlah[j]/mtuNab[j]));
								mtu_unit[j]=Double.valueOf(four.format(mduJumlah[j]/mtuNab[j]));
								if(j==0){
									mtu_saldo_unit[j]=mtu_unit[j];
									mtu_saldo_unit_pp[j]=mtu_unit[j];
									mtu_saldo_unit_tu[j]=new Double(0);
								}else{
									if(mtu_unit[j-1]==null)
										mtu_unit[j-1]=new Double(0);

									if(mtu_saldo_unit[j-1]==null)
										mtu_saldo_unit[j-1]=new Double(0);
									
									if(mtu_saldo_unit_pp[j-1]==null)
										mtu_saldo_unit_pp[j-1]=new Double(0);
									
									mtu_saldo_unit[j]=mtu_saldo_unit[j-1]+mtu_unit[j];
									if(transUlinkNew.getLt_id()==2 || transUlinkNew.getLt_id()==5){//topup tunggal atau berkala
										mtu_saldo_unit_pp[j]=mtu_saldo_unit_pp[j-1];
										if(j==1)
											mtu_saldo_unit_tu[j]=mtu_unit[j];
										else
											mtu_saldo_unit_tu[j]=mtu_unit[j]+mtu_unit[j-1];
									}else if(transUlinkNew.getLt_id()==1) {//alokasi investasi
										mtu_saldo_unit_pp[j]=mtu_unit[j]+mtu_unit[j-1];
										mtu_saldo_unit_tu[j]=new Double(0);
									}
								}
							
							}
							transUlinkNew.setMtu_jumlah(mduJumlah[j]);
							transUlinkNew.setMtu_nab(mtuNab[j]);
							transUlinkNew.setMtu_unit(mtu_unit[j]);
							transUlinkNew.setMtu_saldo_unit(mtu_saldo_unit[j]);
							transUlinkNew.setMtu_saldo_unit_pp(mtu_saldo_unit_pp[j]);
							transUlinkNew.setMtu_saldo_unit_tu(mtu_saldo_unit_tu[j]);
							lsTransNew.add(transUlinkNew);
							count++;
						}
						
					}	
				}
				
				command.setLsTransUlinkNew(lsTransNew);
				command.setLsTransUlink(elionsManager.selectAllMstTransUlink(command.getSpaj()));
				
				
			}
			err.reject("","Edit SPaj Setelah FUnd");
			
		}else if(command.getFlagId().equals("4")){
			elionsManager.updateMstInsured(command.getSpaj(), null, aksep, 1);
		}
	}
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map=new HashMap();
		map.put("user", elionsManager.selectLstUser2());
		return map;
	}
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		String lusIdReq=request.getParameter("userReq");
		String medis=request.getParameter("medis");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Command command=(Command)cmd;
		//flagId 1=Benerin Tertanggung 2=Ubah Medis setelah reas  3=Edit Spaj Setelah FUND ALOCATION
		if(command.getFlagId().equals("1")){
			elionsManager.prosesBlankTertanggung(command.getSpaj());	
		}else if(command.getFlagId().equals("2")){
			elionsManager.prosesUbahMedisSetelahReas(command.getSpaj(), lusIdReq,currentUser,medis);
			err.reject("","Silahkan Reas Ulang Polis");
			err.reject("","Setelah itu update kembali status aksep polis (1=Belum Aksep 5=sudah aksep");
			
		}else if(command.getFlagId().equals("3")){
			err.reject("","Edit SPaj Setelah FUnd");
		}
		return new ModelAndView("common/utilities", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}
}
