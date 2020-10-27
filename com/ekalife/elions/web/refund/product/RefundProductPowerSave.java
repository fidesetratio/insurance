package com.ekalife.elions.web.refund.product;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundProductPowerSave
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jan 29, 2009 3:19:16 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ekalife.elions.web.refund.RefundProductSpecInterface;
import com.ekalife.elions.web.refund.vo.BatalParamsVO;
import com.ekalife.elions.service.ElionsManager;

import java.util.Map;
import java.util.HashMap;

public class RefundProductPowerSave implements RefundProductSpecInterface
{
    protected final Log logger = LogFactory.getLog( getClass() );

    private ElionsManager elionsManager;

    public RefundProductPowerSave( ElionsManager elionsManager )
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundProductPowerSave constructor is called ..." );
        this.elionsManager = elionsManager;
    }

    public HashMap validationBeforeCancel( BatalParamsVO paramsVO )
    {
        HashMap<String, Object> result = new HashMap<String, Object>();

        String validationMessage = "";
        boolean validationPassed = true;
        
        if( elionsManager.selectPowerSaveInterestPaid( paramsVO.getSpajNo() ) > 0 )
        {
            validationMessage = validationMessage.concat( "Polis sudah pernah ada proses pembayaran bunga, hub LB.\\n" );
            validationPassed = false;
        }

        if( elionsManager.selectPowerSaveRollOver( paramsVO.getSpajNo() ) > 0 )
        {
            validationMessage = validationMessage.concat( "Polis sudah pernah roll over, hub LB.\\n" );
            validationPassed = false;
        }

        result.put( "validationMessage", validationMessage );
        result.put( "validationPassed", validationPassed );
        
        return result;
    }

}
