package com.ekalife.elions.web.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import com.ekalife.elions.model.MaintenanceMenu;
import com.ekalife.elions.model.Menu;
import com.ekalife.utils.parent.ParentFormController;

public class MenuMaintenanceFormController extends ParentFormController {

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map refData = new HashMap();
		//refData.put("allApp", this.elionsManager.selectJenisAplikasi());
		//refData.put("allMenu", this.elionsManager.selectAllMenu(ServletRequestUtils.getIntParameter(request, "jenis", 1)));
		refData.put("allMenu", this.elionsManager.selectAllMenuMaintenance(ServletRequestUtils.getIntParameter(request, "jenis",13)));
		return refData;
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		MaintenanceMenu mainMenu=new MaintenanceMenu();
		elionsManager.resetCommonIbatisCache();
		mainMenu.setProses("0");
		return mainMenu;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		MaintenanceMenu showMenu=(MaintenanceMenu)cmd;
		Integer menuId=ServletRequestUtils.getIntParameter(request, "menu_id",0);
		Integer save=ServletRequestUtils.getIntParameter(request, "save",0);
//		String namaMenu=request.getParameter("nama_menu");
//		String linkMenu=request.getParameter("link_menu");
//		String flagAktif=ServletRequestUtils.getStringParameter(request, "flag_aktif","Y");
//		String flagPublic=ServletRequestUtils.getStringParameter(request, "flag_public","N");
		if(save==0){
			if(! showMenu.getProses().equals("0")){
				if(showMenu.getProses().equals("1")){//add
					Menu menu=new Menu();
					menu.setParent_menu_id(menuId);
					menu.setJenis(13);
					menu.setFlag_aktif("Y");
					menu.setFlag_public("N");
					showMenu.setMenu(menu);
					showMenu.setMenu_id(menuId);
					err.reject("","add menu");
					showMenu.setTag2(1);
				}else if(showMenu.getProses().equals("2")){//delete
					//cek dulu dia itu punya apa bukan, kalo ya, tidak bisa di delete
					Menu cekMenu=elionsManager.selectLstMenuNew(menuId);
					List lsCheckChild=elionsManager.selectCheckChild(menuId);
					if(lsCheckChild.isEmpty()==false){
						showMenu.setMenu(new Menu());
						err.reject("","Menu ini mempunyai child, tidak dapat di hapus");
					}
					//err.reject("","Delete langsung aja");
				}else if(showMenu.getProses().equals("3")){//edit
					showMenu.setTag2(1);
					Menu menu=elionsManager.selectLstMenuNew(menuId);
					menu.setJenis(13);
					showMenu.setMenu(menu);
					showMenu.setMenu_id(menuId);
					err.reject("","Edit");
				}
			}
		}else{
			if(showMenu.getProses().equals("1") || showMenu.getProses().equals("3")){//
				if(showMenu.getMenu().getNama_menu().equals(""))
					err.reject("","Nama Menu harus Di isi");
					
			}
		}

			
		

	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
		MaintenanceMenu showMenu=(MaintenanceMenu)command;
		Integer suc=0;
		if(showMenu.getProses().equals("1")){//add
			Menu insMenu=showMenu.getMenu();
			insMenu.setMenu_id(elionsManager.selectMaxMenuId());
			Menu menu=elionsManager.selectLstMenuNew(showMenu.getMenu_id());
			Integer urutan=elionsManager.selectMaxUrutanMenu(menu.getMenu_id());
			if(urutan==null){
				urutan=1;
				Menu upMenu=new Menu();
				upMenu=menu;
				upMenu.setLink_menu(null);
				elionsManager.updateLstMenuNew(menu);
			}	
			insMenu.setUrutan(urutan);
			insMenu.setTingkat(elionsManager.selectTingkatMenu(showMenu.getMenu_id()));
			elionsManager.insertLstMenuNew(insMenu);
			suc=1;
		}else if(showMenu.getProses().equals("2")){//delete
			elionsManager.deleteLstMenuNew(showMenu.getMenu_id());
			suc=2;
			//return new ModelAndView(new RedirectView(request.getContextPath()+ "/common/menuakses.htm"));
		}else if(showMenu.getProses().equals("3")){//edit
			elionsManager.updateLstMenuNew(showMenu.getMenu());
			suc=3;
		}
		
		return new ModelAndView("common/menumaintenance", errors.getModel()).addObject("submitSuccess", suc).addAllObjects(this.referenceData(request,command,errors));
	}

}
