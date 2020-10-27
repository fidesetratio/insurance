package com.ekalife.elions.web.common;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentFormController;

public class MenuAuthorizationFormController extends ParentFormController {

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map refData = new HashMap();
		refData.put("allDept", this.uwManager.selectAllDepartment());
        List allApp = this.elionsManager.selectJenisAplikasi();

        List filteredApp = new ArrayList();
        for( Object app : allApp )
        {
            Map map = ( Map ) app;
            if( map.containsValue( "Individu" ) || map.containsValue( "Medical" )  || map.containsValue( "E-Lions" )  )
            {
                filteredApp.add( app );
            }
        }

        refData.put("allApp", filteredApp );
		return refData;
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Map cmd = new HashMap();
		return cmd;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		String[] nilai = ServletRequestUtils.getStringParameter(request, "nilai", "").split(",");
		String lus = ServletRequestUtils.getStringParameter(request, "lus_id", "");
		String s3="", s2=ServletRequestUtils.getStringParameter(request, "username", "");
		String jenisAplikasi= ServletRequestUtils.getStringParameter(request, "jenisAplikasi", "");
		
		if(request.getParameter("simpan")!=null) {
			this.elionsManager.updateMenuAkses(nilai, lus, jenisAplikasi );
			s3 = "Menu Saved for User "+s2;	
		}else if(request.getParameter("nonaktif")!=null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put( "lusId", lus );
            params.put( "jenisAplikasi", jenisAplikasi );
            this.elionsManager.deleteMenuAkses(params);
			s3 = "Menu Deleted for User "+s2;	
		}
		
		this.elionsManager.resetCommonIbatisCache();
		
		return new ModelAndView(getFormView(), "cmd", errors.getModel()).addAllObjects(this.referenceData(request, command, errors))
			.addObject("s1", lus).addObject("s2", s2).addObject("s3", s3).addObject( "jenisAplikasi", jenisAplikasi );
	}

}
