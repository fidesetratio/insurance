package com.ekalife.elions.model.refund;

import java.io.Serializable;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundSignInForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Dec 2, 2008 4:03:37 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class RefundSignInForm implements Serializable {

	private static final long serialVersionUID = 7470334183563446758L;
	private String password;
    private String signIn;
    private String superuser;

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignIn() {
        return signIn;
    }

    public void setSignIn(String signIn) {
        this.signIn = signIn;
    }

    public String getSuperuser() {
		return superuser;
	}

	public void setSuperuser(String superuser) {
		this.superuser = superuser;
	}
}
