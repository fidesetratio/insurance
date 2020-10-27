package com.ekalife.elions.web.refund;

import com.ekalife.elions.web.refund.vo.BatalParamsVO;

import java.util.HashMap;
import java.util.Map;

/**
 * *******************************************************************
 * Program History
 * <p/>
 * Project Name      	: E-Lions
 * Function Id         	:
 * Program Name   		: RefundProductSpecInterface
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jan 29, 2009 2:48:13 PM
 * <p/>
 * Version      Re-fix date                 Person in charge    Description
 * <p/>
 * <p/>
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 * *********************************************************************
 */
public interface RefundProductSpecInterface
{
    public HashMap validationBeforeCancel( BatalParamsVO paramsVO );
}
