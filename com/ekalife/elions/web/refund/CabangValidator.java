package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangValidator
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 21, 2009 11:39:51 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.refund.CabangEditForm;
import com.ekalife.elions.model.refund.CabangForm;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.InfoBatalVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;

public class CabangValidator
{
    protected final Log logger = LogFactory.getLog( getClass() );

    ElionsManager elionsManager;
    Object command;
    Errors errors;
    CabangBusiness cabangBusiness;

    public CabangValidator( ElionsManager elionsManager, Object command, Errors errors )
    {
        this.elionsManager = elionsManager;
        this.command = command;
        this.errors = errors;
        this.cabangBusiness = new CabangBusiness( elionsManager );
    }

    public void validateCommonInput( HttpServletRequest request )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundValidator.validateCommonInput" );
        CabangEditForm editForm = ( (CabangForm) command ).getEditForm();

        if( isEmpty( editForm.getSpaj() ) )
        {
            errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.emptyForm", null, "error.refund.emptyForm" );
        }
        else
        {
            PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( editForm.getSpaj() );
            if( policyInfoVO == null )
            {
                errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajNotExist", null, "error.refund.spajNotExist" );
            }
            else
            {
                // refresh jika nomor spaj mengandung huruf (supaya hurufnya dihilangkan )
                editForm.setSpaj( policyInfoVO.getSpajNo() );
                RefundDbVO refundByCd = elionsManager.selectRefundByCd( policyInfoVO.getSpajNo() );
                if( refundByCd != null )
                {
                	if( refundByCd.getFlagUserCabang() != null && refundByCd.getFlagUserCabang() == 1 )
                	{
                        errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajDraftExistDiCabang", null, "error.refund.spajDraftExistDiCabang" );
                	}
                	else
                	{
                        errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajDraftExistDiPusat", null, "error.refund.spajDraftExistDiPusat" );                		
                	}
                }
                else
                {
                    InfoBatalVO infoBatalVO = elionsManager.selectInfoBatalBySpaj( policyInfoVO.getSpajNo() );
                    if( infoBatalVO != null )
                    {
                        errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.spajHasBeenCanceled", null, "error.refund.spajHasBeenCanceled" );
                    }
                    if( editForm.getAlasanCd() == null )
                    {
                        errors.rejectValue( RefundEditFormConst.ALASAN_CD, "error.refund.emptyForm", null, "error.refund.emptyForm" );
                    }
                    else if( editForm.getAlasanCd().equals( RefundConst.ALASAN_LAIN ) )
                    {
                        if( isEmpty( editForm.getAlasan() ) )
                        {
                            errors.rejectValue( RefundEditFormConst.ALASAN, "error.refund.emptyForm", null, "error.refund.emptyForm" );
                        }
                    }
                }

                // cek jika pernah titipan premi / setor duit
                List<SetoranPremiDbVO> setoranPremiBySpaj = elionsManager.selectSetoranPremiBySpaj( policyInfoVO.getSpajNo() );
                if( setoranPremiBySpaj != null && setoranPremiBySpaj.size() > 0 )
                {
                    errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.customerAlreadyPaid", null, "error.refund.customerAlreadyPaid" );
                }

                // cek apakah posisinya baru BAC (baru di input)
                if( !( new Integer( 1 ).equals( policyInfoVO.getLspdId() ) ) )
                {
                    errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.posisiTidakDiBAC", null, "error.refund.posisiTidakDiBAC" );
                }

                // cek otoritas
                User user = (User) request.getSession().getAttribute("currentUser");
                if( !user.getLde_id().equals("29") && elionsManager.selectAksesPembatalanCabang( policyInfoVO.getSpajNo(), new BigDecimal( user.getLus_id() ) ) == null )
                {
                    errors.rejectValue( RefundEditFormConst.SPAJ, "error.refund.tidakPunyaAkses", null, "error.refund.tidakPunyaAkses" );
                }
            }
        }


    }

    private boolean isEmpty( String str )
    {
        return str == null || str.trim().equals( "" );
    }

}
