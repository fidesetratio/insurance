package com.ekalife.elions.web.report;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import id.co.sinarmaslife.std.model.vo.DropDown;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Report2 yang digunakan di modul SMS (Sinarmas Sekuritas)
 *
 * @author Deddy
 * @since Jul 01, 2010 (08:52:00 AM)
 */
public class SmsController extends ParentJasperReportingController
{
	protected final Log logger = LogFactory.getLog( getClass() );
    /**
     * Report Produksi BSM Berdasarkan Tanggal Efektif
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception : no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_produksi_tgl_efektif( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_produksi_tgl_efektif" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_efektif_prima_all" ),
                    "Produksi Danamas Prima Berdasarkan Tanggal Efektif Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_efektif_stabil_link_all" ),
                    "Produksi Stable Link Berdasarkan Tanggal Efektif Semua Cabang"
            ) );
        }
        else
        {
        	reportPathList.add( new DropDown(
                  props.getProperty( "report.sms.sms_prod_efektif_prima_cbg" ),
                   "Produksi Danamas Prima Berdasarkan Tanggal Efektif Per Cabang"
            ) );

            reportPathList.add( new DropDown(
                  props.getProperty( "report.sms.sms_prod_efektif_stabil_link_cbg" ),
                   "Produksi Stable Link Berdasarkan Tanggal Efektif Per Cabang"
            ) );
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Efektif BSM", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);
		tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);
		report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }
    
    public ModelAndView internal_ajs_sms( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.internal_ajs_sms" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();
        
       

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        reportPathList.add( new DropDown(
              props.getProperty( "report.sms.ajs_sms_cair_all" ),
              "Laporan Pencairan"
	    ) );
	
	    reportPathList.add( new DropDown(
	              props.getProperty( "report.sms.ajs_sms_prod_akseptasi_all" ),
	              "Laporan Produksi"
	    ) );

	    reportPathList.add( new DropDown(
	              props.getProperty( "report.sms.ajs_sms_outstanding_all" ),
	              "Laporan Outstanding"
	    ) );

        report = new Report( "Report Produksi", reportPathList, Report.PDF, null );
        
        List<Map<String, String>> team = new ArrayList<Map<String, String>>();
		Map<String, String> tmp1 = new HashMap<String, String>();
		tmp1.put("KEY", "Team Jan Rosadi");
		tmp1.put("VALUE", "Jakarta");
		team.add(tmp1);
		tmp1 = new HashMap<String, String>();
		tmp1.put("KEY", "Team Dewi");
		tmp1.put("VALUE", "Surabaya");
		team.add(tmp1);
        report.addParamSelectWithoutAll("team", "team", team, "1", true);

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        return prepareReport( request, response, report );
    }

    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_produksi_tgl_akseptasi( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_produksi_tgl_akseptasi" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_akseptasi_prima_all" ),
                    "Produksi Danamas Prima Berdasarkan Tanggal Akseptasi Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_akseptasi_stabil_link_all" ),
                    "Produksi Stable Link Berdasarkan Tanggal Akseptasi Semua Cabang"
            ) );
        }
        else
        {
        	reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_akseptasi_prima_cbg" ),
                    "Produksi Danamas Prima Berdasarkan Tanggal Akseptasi Per Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_akseptasi_stabil_link_cbg" ),
                    "Produksi Stable Link Berdasarkan Tanggal Akseptasi Per Cabang"
            ) );
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Akseptasi Sekuritas", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi, by Referal
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Apr 7, 2010 (17:00:00 AM)
     */
    public ModelAndView sms_produksi_by_referral_all( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_prod_akseptasi_by_referral_all" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_prod_akseptasi_by_referral_all" ),
                    "Produksi Sinarmas Sekuritas Berdasarkan Tanggal Akseptasi By Referral"
            ) );
        }

        report = new Report( "Report Produksi Berdasarkan Tanggal Akseptasi SMS By Referral", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }    
    
    /**
     * Report Produksi BSM Berdasarkan Tanggal Akseptasi
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_delta_harian( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_delta_harian" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_delta_harian_prima_all" ),
                    "Report Summary (Delta Harian) Danamas Prima Untuk Semua Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_delta_harian_stabil_link_all" ),
                    "Report Summary (Delta Harian) Stable Link Untuk Semua Cabang"
            ) );
        }
        else
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_delta_harian_prima_cbg" ),
                    "Report Summary (Delta Harian) Danamas Prima Per Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_delta_harian_stabil_link_cbg" ),
                    "Report Summary (Delta Harian) Stable Link Per Cabang"
            ) );
        }

        report = new Report( "Report Summary (Delta Harian) SMS", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Cair
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_pencairan( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_pencairan" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_cair_prima_all" ),
                    "Pencairan Danamas Prima Berdasarkan Tanggal Aktual Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_cair_stabil_link_all" ),
                    "Pencairan Stable Link Berdasarkan Tanggal Aktual Semua Cabang"
            ) );
        }
        else
        {
        	reportPathList.add( new DropDown(
        			props.getProperty( "report.sms.sms_cair_prima_cbg" ),
                             "Pencairan Danamas Prima Berdasarkan Tanggal Aktual Per Cabang"
                     ) );
        	reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_cair_stabil_link_cbg" ),
                        "Pencairan Stable Link Berdasarkan Tanggal Aktual Per Cabang"
           ) );
        }

        report = new Report( "Report Pencairan SMS Berdasarkan Tanggal Aktual", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35 " ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
        mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Rollover BSM
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 25, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_rollover( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_rollover" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_rollover_all" ),
                    "Laporan Rollover Danamas Prima Per Tgl Produksi Semua Cabang"
            ) );
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_stabil_rollover_all" ),
                    "Laporan Rollover Stable Link Per Tgl Produksi Semua Cabang"
            ) );
        }
        else
        {
        	reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_rollover" ),
                    "Laporan Rollover Danamas Prima Per Tgl Produksi Per Cabang"
        		) );
        		reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_stabil_rollover" ),
                    "Laporan Rollover Stable Link Per Tgl Produksi Per Cabang"
        		) );
        }

        report = new Report( "Report Roll Over SMS", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Jatuh Tempo BSM
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 25, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_jatuh_tempo( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_jatuh_tempo" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_jatuh_tempo_all" ),
                    "Jatuh Tempo Simas Prima Semua Cabang"
            ) );

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_view_stabil_jatuh_tempo_all" ),
                    "Jatuh Tempo Simas Stabil Link Semua Cabang"
            ) );
        }
        else
        {
            reportPathList.add( new DropDown(
               props.getProperty( "report.sms.sms_view_jatuh_tempo" ),
               "Jatuh Tempo Simas Prima Per Cabang"
        	) );

        	reportPathList.add( new DropDown(
               props.getProperty( "report.sms.sms_view_stabil_jatuh_tempo" ),
               "Jatuh Tempo Simas Stabil Link Per Cabang"
        	) );
        }

        report = new Report( "Report Jatuh Tempo SMS", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }

    /**
     * Report Summary Outstanding
     *
     * @param request: no desc
     * @param response: no desc
     * @return ModelAndView
     * @throws Exception: no desc
     * @since Mar 1, 2010 (11:30:00 AM)
     */
    public ModelAndView sms_outstanding( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        logger.info( "SmsController.sms_outstanding" );
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_outstanding_all" ),
                    "Report Summary Outstanding Semua Produk Untuk Semua Cabang"
            ) );
        }
        else
        {
            reportPathList.add( new DropDown(
                     props.getProperty( "report.sms.sms_outstanding_cbg" ),
                    "Report Summary Outstanding Semua Produk Per Cabang"
            ) );
        }

        report = new Report( "Report Summary Outstanding Sekuritas", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        report.addParamDefault( "username", "username", 0, currentUser.getLus_full_name(), false );
        report.addParamDefault( "lus_id", "lus_id", 200, currentUser.getLus_id(), false );

        if( currentUser.getCab_bank().equals( "M35" ) )
        {
        }
        else
        {
            report.addParamDefault( "lcb_no", "lcb_no", 200, currentUser.getCab_bank(), false );
        }

        report.addParamDate( "Tanggal", "tanggal", true, new Date[]{ sysDate, sysDate }, true );

        List<Map<String, String>> mataUangList = new ArrayList<Map<String, String>>();
		Map<String, String> tmp;

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "01");
		tmp.put("VALUE", "Rupiah");
		mataUangList.add(tmp);

        tmp = new HashMap<String, String>();
		tmp.put("KEY", "02");
		tmp.put("VALUE", "US Dollar");
		mataUangList.add(tmp);

        report.addParamSelectWithoutAll("Mata Uang", "lku_id", mataUangList, "01", true);

        return prepareReport( request, response, report );
    }
    /**
     * Ryan -
     * Report Produksi SMiLe Link SATU
     *
     */
    public ModelAndView sms_produksi_satu ( HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        Report report;
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );
        Date sysDate = elionsManager.selectSysdate();

        List<DropDown> reportPathList = new ArrayList<DropDown>();
        // dibawah ini buwat nambahin jenis2 report

            reportPathList.add( new DropDown(
                    props.getProperty( "report.sms.sms_produksi_satu" ),
                    "Report Produksi SMiLe Link SATU SMS"
            ) );
        report = new Report( "Report Produksi SMiLe Link SATU", reportPathList, Report.PDF, null );

        // dibawah ini sebagai input yg ada di JSP dan sekaligus param ke query
        String jn_bank = Integer.toString(currentUser.getJn_bank());
		report.addParamDefault("username", "username", 200, currentUser.getLus_full_name(), false);
		report.addParamDefault("cab_bank", "cab_bank", 200, currentUser.getCab_bank(), false);
		report.addParamDefault("lus_id", "lus_id", 200, currentUser.getLus_id(), false);
		report.addParamDefault("jn_bank", "jn_bank", 200, jn_bank, false);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {sysDate, sysDate}, true);

        return prepareReport( request, response, report );
    }
}