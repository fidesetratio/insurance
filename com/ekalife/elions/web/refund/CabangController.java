package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 16, 2009 11:00:36 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.CabangEditForm;
import com.ekalife.elions.model.refund.CabangForm;
import com.ekalife.utils.CurrencyFormatEditor;
import com.ekalife.utils.parent.ParentWizardController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CabangController extends ParentWizardController
{
    public static final int CABANG_JSP = 0;

    protected String pages[];
    private CabangHelper cabangHelper;
    protected CurrencyFormatEditor currencyEditor;
    protected CabangValidator cabangValidator;

    public void setCabangHelper( CabangHelper cabangHelper)
    {
        this.cabangHelper = cabangHelper;
    }

    public void setCurrencyEditor( CurrencyFormatEditor currencyEditor )
    {
        this.currencyEditor = currencyEditor;
    }

    public void setCabangValidator( CabangValidator cabangValidator )
    {
        this.cabangValidator = cabangValidator;
    }

    @Override
    protected Object formBackingObject( HttpServletRequest request ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.formBackingObject" );
        this.pages = getPages();

        setAllowDirtyBack( false );

        return cabangHelper.initializeForm( request );
    }

    @Override
    protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.initBinder" );
        binder.registerCustomEditor( Date.class, null, dateEditor );
        binder.registerCustomEditor( Integer.class, null, integerEditor );
        binder.registerCustomEditor( BigDecimal.class, null, currencyEditor );
    }

    @Override
    protected void onBind( HttpServletRequest request, Object command )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.onBind" );
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        CabangForm cabangForm = ( CabangForm ) command;
        cabangForm.setTheEvent( theEvent );

        int currentPage = getCurrentPage( request );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* currentPage = " + currentPage );
    }

    protected boolean suppressValidation( HttpServletRequest request, Object command, BindException errors )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.suppressValidation" );

        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        boolean result = false;
        int currentPage = getCurrentPage( request );
        int targetPage = getTargetPage( request, currentPage );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* currentPage = " + currentPage );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* targetPage = " + targetPage );

        if(logger.isDebugEnabled())logger.debug( "*-*-*-* result suppressValidation = " + result );
        return result;
    }

    @Override
    protected void onBindAndValidate( HttpServletRequest request, Object command, BindException errors, int currentPage ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundController.onBindAndValidate" );
        int nextPage = getTargetPage( request, currentPage );
        String theEvent = ( ( CabangForm ) command ).getTheEvent();
        cabangValidator = new CabangValidator( elionsManager, command, errors );
        if( nextPage >= currentPage ) //validasi hanya kalo ke halaman yg sama ato berikutnya, bukan ke halaman sebelumnya
        {
            if( currentPage == CABANG_JSP)
            {
                if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                    cabangValidator.validateCommonInput( request );
                }
            }
        }
    }

    @Override
    protected Map referenceData( HttpServletRequest request, Object command, Errors errors, int page ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangController.referenceData" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* page = " + page );

        cabangValidator = new CabangValidator( elionsManager, command, errors );
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );
        CabangEditForm editForm = ( (CabangForm) command ).getEditForm();
        Map<String, String> result = new HashMap<String, String>();
        User user = (User) request.getSession().getAttribute("currentUser");
        editForm.setCurrentUser(user.getName());

        if( page == CABANG_JSP )
        {
            cabangHelper.initializeEditForm( command );
        }

        return result;
    }

    @Override
    protected ModelAndView processFinish( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangController.processFinish" );
        return new ModelAndView( new RedirectView( request.getContextPath() + "/common/menu.htm?frame=main" ) );
    }

    protected void postProcessPage( HttpServletRequest request, Object command, Errors errors, int page ) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* CabangController.postProcessPage" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* page = " + page );
        CabangEditForm editForm = ( ( CabangForm ) command ).getEditForm();

        User user = (User) request.getSession().getAttribute("currentUser");
        editForm.setCurrentUser(user.getName());
        String theEvent = request.getParameter( "theEvent" );
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* theEvent = " + theEvent );

        // hanya lakukan jika form telah melewati validasi
        if( errors.getErrorCount() == 0 && errors.getGlobalErrorCount() == 0 )
        {
            if( page == CABANG_JSP)
            {
                if( "onPressButtonBatalkanSpaj".equals( theEvent ) )
                {
                    cabangHelper.onPressButtonBatalkanSpajTindakanTidakAda( request, command, props );
                }
            }
        }
    }
}
