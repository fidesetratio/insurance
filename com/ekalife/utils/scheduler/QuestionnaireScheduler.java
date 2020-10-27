package com.ekalife.utils.scheduler;

import java.io.File;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.jasper.JasperUtils;

import id.co.sinarmaslife.std.spring.util.Email;

/**
 * @author Samuel
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 * @since Feb 22, 2010
 */
public class QuestionnaireScheduler
{
	protected final Log logger = LogFactory.getLog( getClass() );
    private static final Comparable String = null;
    private ElionsManager elionsManager;
    private UwManager uwManager;
    private Properties props;
    private NumberFormat numberFormat;
    private Email email;
    private DateFormat dateFormat;
    //protected Connection connection = null;
    private String jdbcName;

    public void setJdbcName( String jdbcName ) {this.jdbcName = jdbcName;}

    public void setDateFormat( DateFormat dateFormat ) {this.dateFormat = dateFormat;}

    public void setEmail( Email email ) {this.email = email;}

    public void setProps( Properties props ) {this.props = props;}

    public void setNumberFormat( NumberFormat numberFormat ) {this.numberFormat = numberFormat;}

    public void setElionsManager( ElionsManager elionsManager ) {this.elionsManager = elionsManager;}

    public void setUwManager( UwManager uwManager ) {this.uwManager = uwManager;}

    public QuestionnaireScheduler()
    {
        this.dateFormat = new SimpleDateFormat( "yyyyMMdd" );
    }

    //Singleton method untuk menyimpan objek koneksi
    /*
    protected Connection getConnection()
    {
        if( this.connection == null )
        {
            try
            {
                this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
            }
            catch( SQLException e )
            {
                logger.error("ERROR :", e);
            }
        }
        return this.connection;
    }
    */

    public void send( boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments )
            throws MailException, MessagingException
    {
        //enable untuk debugging saja
//        to = new String[]{ "samuel@sinarmasmsiglife.co.id" };
//        cc = new String[]{ "samuel@sinarmasmsiglife.co.id" };
//        bcc = new String[]{ "samuel@sinarmasmsiglife.co.id" };
    	
    	to = new String[]{ "andy@sinarmasmsiglife.co.id" };
        cc = new String[]{ "andy@sinarmasmsiglife.co.id" };
        bcc = new String[]{ "andy@sinarmasmsiglife.co.id" };

        this.email.send( isHtml, from, to, cc, bcc, subject, message, attachments );
    }

    //main method
    public void main() throws Exception
    {
        logger.info( "QuestionnaireScheduler.main CALLED" );
        //todo: nanti commentnya diiilangin
//        if( jdbcName.equals( "eka8i" ) )
//        {
            doScheduling();
//        }
    }

    public void doScheduling() throws Exception
    {
        long start = System.currentTimeMillis();
        logger.info( "QUESTIONNAIRE SCHEDULER AT " + new Date() );
        Connection conn = null;
        try
        {
        	conn = this.elionsManager.getUwDao().getDataSource().getConnection();
            String outputDir = props.getProperty( "pdf.dir.report" ) + "\\random_sample_bii\\";

            Map<String, Comparable> params = new HashMap<String, Comparable>();
            params.put( "tanggalAwal", (new SimpleDateFormat("dd/MM/yyyy")).parse("01/01/2009") );
            params.put( "tanggalAkhir", (new SimpleDateFormat("dd/MM/yyyy")).parse("29/03/2010") );
            params.put( "user", "SYSTEM" );

//    report.questionnaire.validasi_spaj_bii_all=com/ekalife/elions/reports/questionnaire/validasi_spaj_bii_all
//	report.questionnaire.validasi_by_sales_bii_detail=com/ekalife/elions/reports/questionnaire/validasi_by_sales_bii_detail
//	report.questionnaire.validasi_by_sales_bii_summary=com/ekalife/elions/reports/questionnaire/validasi_by_sales_bii_summary
//	report.questionnaire.validasi_per_cabang_summary=

//			Bagian ini untuk menghasilkan file dalam bentuk Xcel
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
            String tgl = df.format( new Date() );
            String outputFilename1 = "validasi_spaj_bii_all_" + tgl + ".xls";
            String sourceFileName1 = props.getProperty( "report.questionnaire.validasi_spaj_bii_all" ) + ".jasper";
            String outputFilename2 = "validasi_by_sales_bii_detail_" + tgl + ".xls";
            String sourceFileName2 = props.getProperty( "report.questionnaire.validasi_by_sales_bii_detail" ) + ".jasper";
            String outputFilename3 = "validasi_by_sales_bii_summary_" + tgl + ".xls";
            String sourceFileName3 = props.getProperty( "report.questionnaire.validasi_by_sales_bii_summary" ) + ".jasper";
            String outputFilename4 = "validasi_per_cabang_summary_" + tgl + ".xls";
            String sourceFileName4 = props.getProperty( "report.questionnaire.validasi_per_cabang_summary" ) + ".jasper";

            JasperUtils.exportReportToXls(
                    sourceFileName1, outputDir, outputFilename1,
                    params, conn );

            JasperUtils.exportReportToXls(
                    sourceFileName2, outputDir, outputFilename2,
                    params, conn );

            JasperUtils.exportReportToXls(
                    sourceFileName3, outputDir, outputFilename3,
                    params, conn );
            
            JasperUtils.exportReportToXls(
                    sourceFileName4, outputDir, outputFilename4,
                    params, conn );

            List<File> attachments = new ArrayList<File>();
            attachments.add( new File( outputDir + "\\" + outputFilename1 ) );
            attachments.add( new File( outputDir + "\\" + outputFilename2 ) );
            attachments.add( new File( outputDir + "\\" + outputFilename3 ) );
            attachments.add( new File( outputDir + "\\" + outputFilename4 ) );

            //Looping untuk send email per masing2 distribusi
            String to = "";
            String cc = "";
            String bcc = "";

//            to = "ingrid;rachel;ariani;";
//            cc = "samuel;yusuf;";
//            bcc = "";

            to = "samuel;";
            cc = "samuel;";
            bcc = "";

            //to dan cc nya @sinarmasmsiglife.co.id
            String[] emailTo = to.split( ";" );
            String[] emailCc = cc.split( ";" );
            String[] emailBcc = bcc.split( ";" );

            for( int y = 0; y < emailTo.length; y++ )
            {
                emailTo[ y ] = emailTo[ y ].concat( "@sinarmasmsiglife.co.id" );
            }
            for( int y = 0; y < emailCc.length; y++ )
            {
                emailCc[ y ] = emailCc[ y ].concat( "@sinarmasmsiglife.co.id" );
            }
            for( int y = 0; y < emailBcc.length; y++ )
            {
                emailBcc[ y ] = emailBcc[ y ].concat( "@sinarmasmsiglife.co.id" );
            }

            // Send email
            send( true,
                    "ajsjava@sinarmasmsiglife.co.id",
                    emailTo, emailCc, emailBcc,
                    "Validasi SPAJ BII",
                    "Lihat Attachment Validasi SPAJ BI",
                    attachments );

            long end = System.currentTimeMillis();

            logger.info( "QUESTIONNAIRE SCHEDULER FINISHED AT " + new Date() + " in " + ( ( float ) ( end - start ) / 1000 ) + " SECONDS." );
        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            long end = System.currentTimeMillis();
            logger.info( "QUESTIONNAIRE SCHEDULER ERROR AT " + new Date() + " in " + ( ( float ) ( end - start ) / 1000 ) + " SECONDS." );
        }finally {
        	this.elionsManager.getUwDao().closeConn(conn);
        }
    }

}