package com.ekalife.utils.parent;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ekalife.elions.dao.AccountingDao;
import com.ekalife.elions.dao.AkseptasiDao;
import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.dao.BasDao;
import com.ekalife.elions.dao.ChecklistDao;
import com.ekalife.elions.dao.CommonDao;
import com.ekalife.elions.dao.CrossSellingDao;
import com.ekalife.elions.dao.EditDataDao;
import com.ekalife.elions.dao.EndorsDao;
import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.elions.dao.MuamalatDao;
import com.ekalife.elions.dao.RefundDao;
import com.ekalife.elions.dao.ReinstateDao;
import com.ekalife.elions.dao.RekruitmentDao;
import com.ekalife.elions.dao.SimasCardDao;
import com.ekalife.elions.dao.SnowsDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.process.Jurnal;
import com.ekalife.elions.process.Komisi;
import com.ekalife.elions.process.NilaiTunai;
import com.ekalife.elions.process.Produksi;
import com.ekalife.elions.process.SavingBac;
import com.ekalife.elions.process.SavingBacSpajNew;
import com.ekalife.elions.process.SavingBeaMeterai;
import com.ekalife.elions.process.SavingRekruitment;
import com.ekalife.elions.process.Sequence;
import com.ekalife.elions.process.Softcopy;
import com.ekalife.elions.process.SuratUnitLink;
import com.ekalife.elions.process.TransferPolis;
import com.ekalife.utils.Products;
import com.ibatis.sqlmap.client.event.RowHandler;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * <p>
 * Abstract Parent Data Access Object (DAO)
 * Dimana HANYA bean-bean yang meng-extend class ini yang diperbolehkan mengakses
 * database dengan bantuan iBatis. Class ini juga dibuatkan fungsi2 tambahan untuk mempermudah
 * akses ke fungsi2 umum seperti SELECT, INSERT, UPDATE, DELETE
 * </p>
 * <p>
 * Bean-bean DAO ini, nantinya akan diatur transaksinya oleh class2 didalam package
 * com.ekalife.elions.service, yaitu class2 yang berfungsi sebagai TransactionManager
 * </p>
 * 
 * @author Yusuf Sutarko
 */
public abstract class ParentDao extends SqlMapClientDaoSupport {

	protected String statementNameSpace;
	//protected Connection connection;

	//Formatters
	protected DateFormat completeDateFormat;
	protected DateFormat completeDateFormatStripes;
	protected DateFormat completeDateFormatStripesWithSecond;
	protected DateFormat defaultDateFormat;
	protected DateFormat defaultDateFormatReversed;
	protected DateFormat defaultDateFormatStripes;
	protected NumberFormat twoDecimalNumberFormat;
	protected NumberFormat fiveDecimalNumberFormat;

	//Global Properties
	protected Email email;
	protected Properties props;
	protected static Properties props_static;
	protected Products products;	
	
	//Proses
	protected Jurnal jurnal;
	protected Komisi komisi;
	protected NilaiTunai nilaiTunai;
	protected Produksi produksi;
	protected SavingBac savingBac;
	protected SavingBeaMeterai savingBeaMeterai;
	protected SavingRekruitment savingRekruitment;
	protected Sequence sequence;
	protected TransferPolis transferPolis;
	protected Softcopy softcopy;
	protected SuratUnitLink suratUnitLink;
	protected SavingBacSpajNew savingBacSpajNew;
	
	

	//Dao
	protected AkseptasiDao akseptasiDao;
	public void setSuratUnitLink(SuratUnitLink suratUnitLink) {
		this.suratUnitLink = suratUnitLink;
	}

	protected BacDao bacDao;
	protected BasDao basDao;
	protected CommonDao commonDao;
	protected FinanceDao financeDao;
	protected UwDao uwDao;
	protected ReinstateDao reinstateDao;
	protected CrossSellingDao crossSellingDao;
	protected AccountingDao accountingDao;
	protected SimasCardDao simasCardDao;
	protected ChecklistDao checklistDao;
	protected MuamalatDao muamalatDao;
	protected EditDataDao editDataDao;
	protected EndorsDao endorsDao;
	protected RefundDao refundDao;
	protected RekruitmentDao rekruitmentDao;
	protected SnowsDao snowsDao;
	
	
	
	public void setSnowsDao(SnowsDao snowsDao) {
		this.snowsDao = snowsDao;
	}

	public void setSoftcopy(Softcopy softcopy) {
		this.softcopy = softcopy;
	}
	
	public void setEndorsDao(EndorsDao endorsDao) {this.endorsDao = endorsDao;}
	
	public void setRefundDao(RefundDao refundDao) {
		this.refundDao = refundDao;
	}

	public void setMuamalatDao(MuamalatDao muamalatDao) {
		this.muamalatDao = muamalatDao;
	}

	/*
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		} else
			try {
				if(this.connection.isClosed()){
					try {
						this.connection = getDataSource().getConnection();
					} catch (SQLException e) {
						logger.error("ERROR :", e);
					}
				}
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		return this.connection;
	}
	*/
	
	protected void closeConnection(Connection conn){
		try {
            conn.close();
        } catch (SQLException e) { /* ignored */}
	}
	
	//Fungsi-fungsi Query
	protected Object insert(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("INSERT: " + queryName);
		return getSqlMapClientTemplate().insert(this.statementNameSpace + queryName, param);
	}

	protected List query(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("QUERY: " + queryName);
		return getSqlMapClientTemplate().queryForList(this.statementNameSpace + queryName, param);
	}

	public void setCompleteDateFormatStripes(DateFormat completeDateFormatStripes) {
		this.completeDateFormatStripes = completeDateFormatStripes;
	}

	protected Object querySingle(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("QUERY SINGLE: " + queryName);
		return getSqlMapClientTemplate().queryForObject(this.statementNameSpace + queryName, param);
	}

	protected void queryHandler(String queryName, Object param, RowHandler rowHandler) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("QUERY ROWHANDLER: " + queryName);
		getSqlMapClientTemplate().queryWithRowHandler(this.statementNameSpace + queryName, param, rowHandler);
	}

	protected Map queryMap(String queryName, Object param, String keyProperty) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("QUERY SINGLE: " + queryName);
		return getSqlMapClientTemplate().queryForMap(this.statementNameSpace + queryName, param, keyProperty);
	}

	protected Map queryMap(String queryName, Object param, String keyProperty, String valueProperty) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("QUERY SINGLE: " + queryName);
		return getSqlMapClientTemplate().queryForMap(this.statementNameSpace + queryName, param, keyProperty, valueProperty);
	}

	protected int update(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("UPDATE: " + queryName);
		return getSqlMapClientTemplate().update(this.statementNameSpace + queryName, param);
	}

	protected int delete(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("DELETE: " + queryName);
		return getSqlMapClientTemplate().delete(this.statementNameSpace + queryName, param);
	}

	public void setCompleteDateFormatStripesWithSecond(
			DateFormat completeDateFormatStripesWithSecond) {
		this.completeDateFormatStripesWithSecond = completeDateFormatStripesWithSecond;
	}

	//Setter
	public void setEditDataDao(EditDataDao editDataDao) {this.editDataDao = editDataDao;}

	public void setAccountingDao(AccountingDao accountingDao) {
		this.accountingDao = accountingDao;
	}
	
	public void setAkseptasiDao(AkseptasiDao akseptasiDao) {
		this.akseptasiDao = akseptasiDao;
	}

	public void setBacDao(BacDao bacDao) {
		this.bacDao = bacDao;
	}

	public void setBasDao(BasDao basDao) {
		this.basDao = basDao;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}

	public void setJurnal(Jurnal jurnal) {
		this.jurnal = jurnal;
	}

	public void setKomisi(Komisi komisi) {
		this.komisi = komisi;
	}

	public void setNilaiTunai(NilaiTunai nilaiTunai) {
		this.nilaiTunai = nilaiTunai;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setProduksi(Produksi produksi) {
		this.produksi = produksi;
	}

	public void setProps(Properties props) {
		this.props = props;
		props_static = props;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public void setStatementNameSpace(String statementNameSpace) {
		this.statementNameSpace = statementNameSpace;
	}

	public void setTransferPolis(TransferPolis transferPolis) {
		this.transferPolis = transferPolis;
	}

	public void setTwoDecimalNumberFormat(NumberFormat twoDecimalNumberFormat) {
		this.twoDecimalNumberFormat = twoDecimalNumberFormat;
	}

	public void setUwDao(UwDao uwDao) {
		this.uwDao = uwDao;
	}

	public void setDefaultDateFormatStripes(DateFormat defaultDateFormatStripes) {
		this.defaultDateFormatStripes = defaultDateFormatStripes;
	}

	public void setDefaultDateFormatReversed(DateFormat defaultDateFormatReversed) {
		this.defaultDateFormatReversed = defaultDateFormatReversed;
	}

	public void setSavingBac(SavingBac savingBac) {
		this.savingBac = savingBac;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setFiveDecimalNumberFormat(NumberFormat fiveDecimalNumberFormat) {
		this.fiveDecimalNumberFormat = fiveDecimalNumberFormat;
	}

	public void setSavingBeaMeterai(SavingBeaMeterai savingBeaMeterai) {
		this.savingBeaMeterai = savingBeaMeterai;
	}

	public void setSavingRekruitment(SavingRekruitment savingRekruitment) {
		this.savingRekruitment = savingRekruitment;
	}

	public void setReinstateDao(ReinstateDao reinstateDao) {
		this.reinstateDao = reinstateDao;
	}

	public void setCrossSellingDao(CrossSellingDao crossSellingDao) {
		this.crossSellingDao = crossSellingDao;
	}

	public void setCompleteDateFormat(DateFormat completeDateFormat) {
		this.completeDateFormat = completeDateFormat;
	}

	public void setSimasCardDao(SimasCardDao simasCardDao) {
		this.simasCardDao = simasCardDao;
	}

	public void setChecklistDao(ChecklistDao checklistDao) {
		this.checklistDao = checklistDao;
	}

	public RekruitmentDao getRekruitmentDao() {
		return rekruitmentDao;
	}

	public void setRekruitmentDao(RekruitmentDao rekruitmentDao) {
		this.rekruitmentDao = rekruitmentDao;
	}

	
	public void setSavingBacSpajNew(SavingBacSpajNew savingBacSpajNew) {
		this.savingBacSpajNew = savingBacSpajNew;
	}
	
	


}