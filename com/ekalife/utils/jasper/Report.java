package com.ekalife.utils.jasper;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ibatis.common.resources.Resources;

/**
 * <p>
 * Class ini digunakan untuk mempermudah pembuatan view dan controller untuk report2 jasperreports
 * dimana user tidak perlu membuat halaman jsp lagi, melainkan cukup meng-instantiate class ini
 * serta mengisi setting2 seperti file report, parameter2, dan jenis format ynag dihasilkan
 * </p>
 * <p>
 * Lihat Penggunaannya di com.ekalife.elions.web.report.UnderWritingController / BacController
 *	</p>
 *<ul>
 * <li>Controller yang memanggil, harus extend parent jasper reporting controller</li>
 * <li>apabila ingin diberi pilihan report (jenis report lebih dari satu), gunakan constructor yang kedua</li>
 * </ul>
 * @author Yusuf
 */
public class Report implements Serializable{
	/*
	 *  (Yusuf) PENDING :
	 * - Otorisasi format yang diperbolehkan (HTML/PDF/XLS/JAVA)
	 * - Parameter isRequired pada fungsi2 addParamXXX
	 */ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311384358897395082L;
	//Tipe-tipe Parameter
	public static final String TEXT 					= "text"; 				//Parameter bertipe text biasa
	public static final String SELECT 					= "select"; 			//Parameter bertipe select-list
	public static final String SELECT_WITH_REFRESH 		= "selectWithRefresh"; 			//Parameter bertipe select-list tanpa pilihan All
	public static final String SELECT_WITHOUT_ALL		= "selectWithoutAll"; 			//Parameter bertipe select-list tanpa pilihan All
	public static final String DATE 					= "date"; 			//Parameter bertipe date
	public static final String DATE_RANGE 				= "dateRange"; 	//Parameter bertipe date range
	public static final String MONTH					= "month"; 			//Parameter bertipe month (yyyymm)
	public static final String MONTH_RANGE				= "monthRange";//Parameter bertipe month range (yyyymm)
	public static final String DEFAULT					= "depault"; 		//Parameter bertipe default (nilainya tidak di-input, misalnya lus_id)
	
	public static final String HTML 	= "html";
	public static final String PDF 		= "pdf";
	public static final String XLS 		= "xls";
	public static final String JAVA 	= "java";
	
	//E-mail report
	private Boolean reportCanBeEmailed=false;
	private Boolean excelEmail=false;
	private String[] reportEmailTo;
	private String[] reportEmailCc;
	private String reportSubject;
	private String reportMessage;
	
	//
	private ArrayList<HashMap> parameterList;
	private ArrayList<HashMap> resultList;
	private String title;
	private String reportPath;
	private ArrayList<DropDown> reportPathList;
	private Integer allowedFormat=0;
	private DateFormat df;
	private String reportQueryMethod;
	private String defaultView;
	private String requestUri;
	private Integer pdfPermissions;
	private HashMap<String, Object> customParameters;
	private Boolean flagEmail=false;
	private String to;
	private Boolean flag=false; //flag==1 emailing List pembayaran komisi di TTP
	private Boolean FlagEC=false;
	private String link; //link untuk onClick di jsp
	private Boolean flagExcel=false;
	private Boolean flagEmailCab=false;
	private String backupPdfPath;
	private String lca_id;
	private String emailCab;
	private ArrayList<HashMap> lsRegion;
	private User currentUser;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	//String backupPdfPath apabila tidak null, maka harus berisi path dimana file akan dibackup pdf nya
	
	//Constructor 1, dengan 1 jenis report
	public Report(String title, String reportPath, String defaultView, String backupPdfPath) {
		this.title = title;
		this.reportPath = reportPath;
		this.defaultView = defaultView;
		this.backupPdfPath = backupPdfPath;
		//this.allowedFormat = allowedFormat;
		init();
	}

	//Constructor 2, dengan > 1 jenis report
	public Report(String title, List<DropDown> reportPathList, String defaultView, String backupPdfPath) {
		this.title = title;
		this.reportPathList = Common.serializableList(reportPathList);
		this.defaultView = defaultView;
		this.backupPdfPath = backupPdfPath;
		//this.allowedFormat = allowedFormat;
		init();
	}

	//Default Constructor
	public void init() {
		this.parameterList = new ArrayList<HashMap>();
        this.customParameters = new HashMap< String, Object >(); 
        this.df = new SimpleDateFormat("dd/MM/yyyy");
        this.reportCanBeEmailed = false;
	}
	
	//Fungsi-fungsi untuk penambahan Parameter
	public void addParamDefault(String name, String id, Integer maxLength, String value, Boolean isVisible) {
		HashMap<String, Comparable> parameter = new HashMap<String, Comparable>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("type", DEFAULT);
		parameter.put("max", new Integer(maxLength));
		parameter.put("value", value);
		parameter.put("visible", isVisible);
		
		this.parameterList.add(parameter);
	}

	public void addParamText(String name, String id, Integer maxLength, String defaultValue, Boolean isRequired) {
		HashMap<String, Comparable> parameter = new HashMap<String, Comparable>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("type", TEXT);
		parameter.put("max", maxLength);
		parameter.put("depault", defaultValue);
		parameter.put("required", isRequired);
		
		this.parameterList.add(parameter);
	}
	
	public void addParamSelect(String name, String id, List list, String defaultValue, Boolean isRequired) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("type", SELECT);
		parameter.put("list", Common.serializableList(list)); //LIST DISINI, HARUS BERUPA LIST DARI MAP BERISI "KEY" DAN "VALUE"
		parameter.put("depault", defaultValue);
		parameter.put("required", new Boolean(isRequired));
		
		this.parameterList.add(parameter);
	}

    public void addParamSelectWithRefresh(String name, String id, List list, String defaultValue, Boolean isRequired) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("type", SELECT_WITH_REFRESH );
		parameter.put("list", Common.serializableList(list)); //LIST DISINI, HARUS BERUPA LIST DARI MAP BERISI "KEY" DAN "VALUE"
		parameter.put("depault", defaultValue);
		parameter.put("required", isRequired);

		this.parameterList.add(parameter);
	}

    public void addParamSelectWithoutAll(String name, String id, List list, String defaultValue, Boolean isRequired) {
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("type", SELECT_WITHOUT_ALL);
		parameter.put("list", Common.serializableList(list)); //LIST DISINI, HARUS BERUPA LIST DARI MAP BERISI "KEY" DAN "VALUE"
		parameter.put("depault", defaultValue);
		parameter.put("required", isRequired);

		this.parameterList.add(parameter);
	}

    public void addParamDate(String name, String id, Boolean isRange, Date[] defaultValues, Boolean isRequired) {
		HashMap<String, Comparable> parameter = new HashMap<String, Comparable>();
		parameter.put("name", name);
		parameter.put("id", id);
		
		if(isRange) {
			if(defaultValues != null) {
				parameter.put("defaultAwal", defaultValues[0]);
				parameter.put("defaultAkhir", defaultValues[1]);
			}
			parameter.put("type", DATE_RANGE);
		} else {
			parameter.put("depault", defaultValues[0]);
			parameter.put("type", DATE);
		}
		
		parameter.put("required", new Boolean(isRequired));
		
		this.parameterList.add(parameter);
	}

	public void addParamMonth(String name, String id, Boolean isRange, Integer thnAwal, Integer thnAkhir, Boolean isRequired) {
		ArrayList<Integer> tahun = new ArrayList<Integer>();
		for(; thnAwal<=thnAkhir; thnAwal++) {
			tahun.add(thnAwal);
		}
		SortedMap<String, String> bln = new TreeMap<String, String>();
		bln.put("01","Januari");
		bln.put("02","Februari");
		bln.put("03","Maret");
		bln.put("04","April");
		bln.put("05","Mei");
		bln.put("06","Juni");
		bln.put("07","Juli");
		bln.put("08","Agustus");
		bln.put("09","September");
		bln.put("10","Oktober");
		bln.put("11","November");
		bln.put("12","Desember");
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("id", id);
		parameter.put("tahun", tahun);
		parameter.put("bulan", bln);
		
		if(isRange) {
			parameter.put("type", MONTH_RANGE);
		} else {
			parameter.put("type", MONTH);
		}
		
		parameter.put("required", new Boolean(isRequired));
		
		this.parameterList.add(parameter);
	}
	
	public Object getParameterValue(String id) {
		if(this.parameterList!=null) {
			for(int i=0; i<this.parameterList.size(); i++) {
				String tmp = (String) this.parameterList.get(i).get("id");
				if(tmp.equals(id)) return this.parameterList.get(i).get("value");
			}
		}
		return null;
	}
	
	public Map<String, Object> bindParameters(HttpServletRequest request) throws Exception {
		Map<String, Object> paramValues = new HashMap<String, Object>();
		
		if(this.reportPath != null)
			paramValues.put("reportPath", "/WEB-INF/classes/" + reportPath);
		else
			paramValues.put("reportPath", "/WEB-INF/classes/" + ServletRequestUtils.getRequiredStringParameter(request, "reportPath"));
			
		for(int i=0; i<parameterList.size(); i++) {
			Map parameter = (Map) parameterList.get(i);
			String type = (String) parameter.get("type");
			String id = (String) parameter.get("id");
            //logger.info( "*-*-*-* type = " + type );
            if(type.equals(DEFAULT))
            {
				paramValues.put(id, ServletRequestUtils.getStringParameter(request, id, (String) getParameterValue(id)));
			}
            else if(
                    type.equals(TEXT) ||
                    type.equals(SELECT) ||
                    type.equals(SELECT_WITH_REFRESH) ||
                    type.equals(SELECT_WITHOUT_ALL)
                    )
            {
				String lus_id = ServletRequestUtils.getStringParameter(request, id, "");
				String report_path = (String) paramValues.get("reportPath");
				int start_name = report_path.lastIndexOf("/")+1;
				String report_name = report_path.substring(start_name, report_path.length());
				//All
				paramValues.put(id, ServletRequestUtils.getStringParameter(request, id, ""));
				if(lus_id.equals("All") && report_name.equals("register_spaj")){
					String path = report_path.substring(0, start_name-1);
					Properties props = new Properties();
					FileInputStream in = new FileInputStream(Resources.getResourceAsFile("ekalife.properties"));
					props.load(in);
					paramValues.put("reportPath",  "/WEB-INF/classes/" + props.getProperty("report.summary.register_spaj_all"));
					//paramValues.put("reportPath", path + "/" +"register_spaj_all" );
				}
			} else if(type.equals(DATE)) {
				paramValues.put(id, df.parse(ServletRequestUtils.getRequiredStringParameter(request, id)));
			} else if(type.equals(DATE_RANGE)) {
				
				String awal=request.getParameter(id+"Awal");
				String akhir=request.getParameter(id+"Akhir");
				if(awal!=null && akhir!=null){
					paramValues.put(id+"Awal", df.parse(awal));
					paramValues.put(id+"Akhir", df.parse(akhir));
				}
			}else if(type.equals(MONTH)) {
				paramValues.put(id, 
						ServletRequestUtils.getRequiredStringParameter(request, id+"_yyyy")+
						ServletRequestUtils.getRequiredStringParameter(request, id+"_mm"));
			}else if(type.equals(MONTH_RANGE)) {
				paramValues.put(id+"Awal", 
						ServletRequestUtils.getRequiredStringParameter(request, id+"Awal_yyyy")+
						ServletRequestUtils.getRequiredStringParameter(request, id+"Awal_mm"));
				paramValues.put(id+"Akhir", 
						ServletRequestUtils.getRequiredStringParameter(request, id+"Akhir_yyyy")+
						ServletRequestUtils.getRequiredStringParameter(request, id+"Akhir_mm"));
			}
		}
		
		if(this.customParameters!=null) {
			if(!this.customParameters.isEmpty()) {
				paramValues.putAll(this.customParameters);
			}
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("BINDING JASPER REPORT PARAMETERS : ");
			for(Iterator it = paramValues.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				logger.debug(key + " = " + paramValues.get(key));
			}
		}

        logger.info( "*-*-*-* paramValues = " + paramValues );

        return paramValues;
	}
	
	//Untuk merubah requestUri dari .htm menjadi sesuai dgn format (.xls .pdf .html)
	public void changeUri(HttpServletRequest request) {
		String tempUri = request.getRequestURI();
		this.requestUri = tempUri.substring(0, tempUri.lastIndexOf("."));
	}
	
	/** Getter Setter **/
	public ArrayList getParameterList() {return parameterList;}
	public void setParameterList(List<Map> parameterList) {this.parameterList = Common.serializableList(parameterList);}


    public String[] getReportEmailTo() {
		return reportEmailTo;
	}

	public void setReportEmailTo(String[] reportEmailTo) {
		this.reportEmailTo = reportEmailTo;
	}

	public String[] getReportEmailCc() {
		return reportEmailCc;
	}

	public void setReportEmailCc(String[] reportEmailCc) {
		this.reportEmailCc = reportEmailCc;
	}
	public String getReportMessage() {
		return reportMessage;
	}

	public void setReportMessage(String reportMessage) {
		this.reportMessage = reportMessage;
	}

	public String getReportSubject() {
		return reportSubject;
	}

	public void setReportSubject(String reportSubject) {
		this.reportSubject = reportSubject;
	}

	public ArrayList getResultList()
    {
        return resultList;
    }

    public void setResultList( List resultList )
    {
        this.resultList = Common.serializableList(resultList);
    }

    public DateFormat getDf()
    {
        return df;
    }

    public void setDf( DateFormat df )
    {
        this.df = df;
    }

    public Boolean getReportCanBeEmailed() {
		return reportCanBeEmailed;
	}

	public void setReportCanBeEmailed(Boolean reportCanBeEmailed) {
		this.reportCanBeEmailed = reportCanBeEmailed;
	}

	public Boolean getExcelEmail() {
		return excelEmail;
	}

	public void setExcelEmail(Boolean excelEmail) {
		this.excelEmail = excelEmail;
	}

	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public Integer getAllowedFormat() {return allowedFormat;}
	public void setAllowedFormat(int allowedFormat) {this.allowedFormat = allowedFormat;}
	public String getReportPath() {return reportPath;}
	public void setReportPath(String reportPath) {this.reportPath = reportPath;}
	public String getReportQueryMethod() {return reportQueryMethod;}
	public void setReportQueryMethod(String reportQueryMethod) {this.reportQueryMethod = reportQueryMethod;}
	public ArrayList getReportPathList() {return reportPathList;}
	public void setReportPathList(List<DropDown> reportPathList) {this.reportPathList = Common.serializableList(reportPathList);}
	public String getDefaultView() {return defaultView;}
	public void setDefaultView(String defaultView) {this.defaultView = defaultView;}
	public String getRequestUri() {return requestUri;}
	public void setRequestUri(String requestUri) {this.requestUri = requestUri;}
	public Integer getPdfPermissions() {return pdfPermissions;}
	public void setPdfPermissions(int pdfPermissions) {this.pdfPermissions = pdfPermissions;}
	public HashMap getCustomParameters() {return customParameters;}
	public void setCustomParameters(Map<String, Object> customParameters) {this.customParameters = Common.serializableMap(customParameters);}

	public Boolean getFlagEmail() {
		return flagEmail;
	}

	public void setFlagEmail(Boolean flagEmail) {
		this.flagEmail = flagEmail;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getFlagExcel() {
		return flagExcel;
	}

	public void setFlagExcel(Boolean flagExcel) {
		this.flagExcel = flagExcel;
	}

	public Boolean getFlagEmailCab() {
		return flagEmailCab;
	}

	public void setFlagEmailCab(Boolean flagEmailCab) {
		this.flagEmailCab = flagEmailCab;
	}

	public Boolean getFlagEC() {
		return FlagEC;
	}

	public void setFlagEC(Boolean flagEC) {
		FlagEC = flagEC;
	}

	public String getBackupPdfPath() {
		return backupPdfPath;
	}

	public void setBackupPdfPath(String backupPdfPath) {
		this.backupPdfPath = backupPdfPath;
	}

	public String getEmailCab() {
		return emailCab;
	}

	public void setEmailCab(String emailCab) {
		this.emailCab = emailCab;
	}

	public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public ArrayList getLsRegion() {
		return lsRegion;
	}

	public void setLsRegion(List lsRegion) {
//		List lsRegion=new ArrayList();
//		lsRegion=elionsManager.selectAllLstCab();
		this.lsRegion = Common.serializableList(lsRegion);
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}



	
	
}
