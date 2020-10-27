package com.ekalife.elions.web.uw;

import com.ekalife.elions.model.MedicalEmailForm;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.elions.model.vo.MedicalCheckupVO;
import com.ekalife.elions.model.vo.PertimbanganMedicalVO;
import com.ekalife.elions.web.uw.business.MedicalEmailBusiness;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;
import java.util.*;
import java.io.File;
import java.text.DateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MedicalEmailFormController extends ParentFormController
{
	protected final Log logger = LogFactory.getLog( getClass() );
    private MedicalEmailBusiness business;

    public MedicalEmailFormController()
    {
        this.business = new MedicalEmailBusiness();
    }

    protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder ) throws Exception
    {
        logger.info( "*-*-*-* MedicalEmailFormController.initBinder" );
        binder.registerCustomEditor( Double.class, null, doubleEditor );
        binder.registerCustomEditor( Integer.class, null, integerEditor );
    }

    protected Map referenceData( HttpServletRequest request, Object command, Errors arg2 ) throws Exception
    {
        logger.info( "*-*-*-* MedicalEmailFormController.referenceData" );
        MedicalEmailForm cmd = ( MedicalEmailForm ) command;
        Map<String, String> refMap = new HashMap<String, String>();
        String contentScript = business.genScriptCheckMedicalCheckupAccordingToJenisMedis( cmd, elionsManager );
        refMap.put( "contentScript", contentScript );
        return refMap;
    }

    protected Object formBackingObject( HttpServletRequest request ) throws Exception
    {
        logger.info( "*-*-*-* MedicalEmailFormController.formBackingObject" );
        MedicalEmailForm cmd = new MedicalEmailForm();

        List<JenisMedicalVO> jenisMedicalVOList = elionsManager.selectLstJenisPrefix();
        List<MedicalCheckupVO> medicalCheckupVOList = elionsManager.selectMedicalCheckupList();
        List<PertimbanganMedicalVO> pertimbanganMedicalVOList = business.genPertimbanganMedicalVOList();

        cmd.setJenisMedicalVOList( jenisMedicalVOList );
        cmd.setMedicalCheckupVOList( medicalCheckupVOList );
        cmd.setPertimbanganMedicalVOList( pertimbanganMedicalVOList );

        String spaj = request.getParameter( "spaj" );
        cmd.setSpaj( spaj );
        cmd.setBeautifiedSpaj( FormatString.nomorSPAJ( spaj ) );
        cmd.setPolicyHolderName( StringUtil.camelHumpAndTrim( elionsManager.selectPolicyHolderNameBySpaj( spaj ) ) );
        cmd.setInsuredName( StringUtil.camelHumpAndTrim( elionsManager.selectInsuredNameBySpaj( spaj ) ) );

        return cmd;
    }

    @Override
    protected void onBind( HttpServletRequest request, Object cmd, BindException err ) throws Exception
    {
        logger.info( "*-*-*-* MedicalEmailFormController.onBind" );
    }

    protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {
        logger.info( "*-*-*-* MedicalEmailFormController.onSubmitsss" );
        MedicalEmailForm medicalEmailForm = ( MedicalEmailForm ) cmd;
        String generalMsg = "";

        String theEvent = request.getParameter( "theEvent" );
        logger.info( "*-*-*-* theEvent = " + theEvent );

        if( errors.getErrorCount() == 0 && errors.getGlobalErrorCount() == 0 )
        {
            if( "onPressButtonPreviewMedicalPdf".equals( theEvent ) )
            {
                logger.info( "*-*-*-* MedicalEmailFormController.onSubmit.onPressButtonPreviewMedicalPdf" );
                request.getSession().setAttribute( "downloadUrlSession", "uw/medical_email_download.htm" );
                request.getSession().setAttribute( "command", medicalEmailForm );
            }
            else if( "onPressButtonSendMedicalEmail".equals( theEvent ) )
            {
                logger.info( "*-*-*-* MedicalEmailFormController.onSubmit.onPressButtonSendMedicalEmail" );
                generalMsg = sendMedicalEmail( request, medicalEmailForm );
            }
        }

        return new ModelAndView(
                "uw/medical_email",
                errors.getModel() ).addObject( "generalMsg", generalMsg ).addAllObjects( this.referenceData( request, cmd, errors )
        );
    }

    private String sendMedicalEmail( HttpServletRequest request, MedicalEmailForm medicalEmailForm )
    {
        String result = "";
        User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );

        String emailSumber = currentUser.getEmail();
        String emailTujuan = uwManager.selectEmailCabangFromSpaj( medicalEmailForm.getSpaj() );
        logger.info( "emailSumber = " + emailSumber );
        logger.info( "emailTujuan = " + emailTujuan );
        
        Map<String, Object> params = business.getDownloadParams( medicalEmailForm );

        String dirName = props.getProperty( "report.email.dir" );
        String fileName = props.getProperty( "report.email.namaFileMedis" );

        try
        {
            JasperUtils.exportReportToPdf(
                        "com/ekalife/elions/reports/uw/medical_email.jasper",
                dirName,
                fileName,
				params,
                business.genTableDetail( medicalEmailForm ),
				PdfWriter.AllowPrinting,
                null,
                null
            );

            List<File> attachments = new ArrayList<File>();
            File sourceFile = new File( dirName + "\\" + fileName );
            attachments.add( sourceFile );

            Date now = new Date();
            try
            {
                email.send(
                        false, 
                		emailSumber, 
                		new String[]{ emailTujuan }, // new String[]{ "samuel@sinarmasmsiglife.co.id" },
                        null, null,
                        "Pengantar Medis SPAJ " + medicalEmailForm.getSpaj() + " cetak pkl " + FormatDate.toIndonesian( now ) + " " + DateFormat.getTimeInstance( DateFormat.MEDIUM ).format(now),
                        "",
                        attachments);
            }
            catch( MessagingException e )
            {
                if(logger.isDebugEnabled())logger.debug( "Error pada pengiriman email surat pengantar medical pada class MedicalEmailFormController.sendMedicalEmail" );
                result = result + "\\nGagal kirim email";
            }

        }
        catch( Exception e )
        {
            logger.error("ERROR :", e);
            result = result + "\\nGagal export ke PDF";
        }

        if( result.equals( "" ) )
        {
            result = "Email telah dikirim.";
        }

        return result;
    }

}