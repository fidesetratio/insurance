package com.ekalife.gwt.client;

import java.util.Date;
import java.util.List;

import com.ekalife.gwt.client.model.Polis;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpajAsli implements EntryPoint, ClickListener {

	private DbServiceAsync dbService;

	/** Controls */
	private Grid grid = new Grid();
	private Button refresh = new Button("Refresh", this);
	private Button update = new Button("Update", this);
	private CheckBox[] checkBoxes;
	private List daftarPolis;
	private DialogBox dialogLoading = new DialogBox(false, true);
	//check all
	private CheckBox checkAll = new CheckBox("");

	/** Formatter */
	
	private DateTimeFormat dtf = DateTimeFormat.getFullDateTimeFormat(); 
	private NumberFormat nf = NumberFormat.getDecimalFormat();

	/** Fungsi2 tambahan */
	
	private DbServiceAsync initService() {
		DbServiceAsync dbService = (DbServiceAsync) GWT.create(DbService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) dbService;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "/service");
		return dbService;
	}
	
	private String formatDate(Date date) {
		return date==null ? "" : dtf.format(date);
	}
	
	private String formatNumber(Double angka) {
		return nf.format(angka.doubleValue());
	}

	/***/
	private AsyncCallback callbackUpdateDaftarPolis = new AsyncCallback(){
		public void onFailure(Throwable caught) {
			Window.alert("Gagal");
		}
		public void onSuccess(Object result) {
			Window.alert("Sukses");
		}
	};

	private AsyncCallback callbackDaftarPolis = new AsyncCallback(){
		
		public void onFailure(Throwable caught) {
			Window.alert("Nasibmu naaak!");
		}
		
		public void onSuccess(Object result) {
			
			//fetch result & resize grid
			daftarPolis = (List) result;
			int rows = daftarPolis.size();
			int cols = 4;
			grid.resize(0, 0);
			grid.resize(rows+1, cols+1); //+1 row untuk column header, +1 col untuk checkbox

			checkBoxes = new CheckBox[rows];
			
			//init column headers
			String[] columns = {"", "No. Register", "No. Polis", "Posisi", "Produk"};
			
		    for (int i=0; i<cols+1; i++) {
		    	if(i==0) {
					grid.setWidget(0, i, checkAll);
		    	}else {
					grid.setText(0, i, columns[i]);
		    	}
				grid.getCellFormatter().setStyleName(0, i, "tableHeader");
				grid.getCellFormatter().setWordWrap(0, i, false);
			}
						
		    //fill cells
			for(int i=0; i<rows; i++) {
				final Polis polis = (Polis) daftarPolis.get(i);
				checkBoxes[i] = new CheckBox();
				checkBoxes[i].addClickListener(new ClickListener() {
					public void onClick(Widget sender) {
						if(((CheckBox) sender).isChecked()) polis.update = 1;
						else polis.update = 0;
					}
				});
				grid.setWidget(i+1, 0, checkBoxes[i]);
				grid.setText(i+1, 1, polis.reg_spaj);
				grid.setText(i+1, 2, polis.mspo_policy_no_format);
				grid.setText(i+1, 3, polis.lspd_position);
				grid.setText(i+1, 4, polis.lsdbs_name);
				grid.getRowFormatter().setStyleName(i+1, "odd");
			}

			dialogLoading.hide();
			
		}
		
	};
	
	public void onClick(Widget sender) {
		if(sender == update) {
			if(Window.confirm("Yakin tandakan SPAJ dari polis menjadi ASLI/ORIGINAL?")) {
				dbService.updateDaftarPolis(daftarPolis, callbackUpdateDaftarPolis);
			}
		}else if(sender == refresh) {
			dialogLoading.show();
			dialogLoading.center();			
			dbService.getDaftarPolis(callbackDaftarPolis);
		}else if(sender == checkAll) {
			for(int i=0; i<checkBoxes.length; i++) {
				checkBoxes[i].setChecked(checkAll.isChecked());
				Polis polis = (Polis) daftarPolis.get(i);
				if(checkAll.isChecked()) polis.update = 1;
				else polis.update = 0;
			}
		}
	}
	
	public void onModuleLoad() {
		if(dbService == null) dbService = initService();
		dialogLoading.setText("Silahkan Tunggu...");
		dialogLoading.show();
		dialogLoading.center();			
		dbService.getDaftarPolis(callbackDaftarPolis);
		checkAll.addClickListener(this);
		RootPanel.get("slot1").add(grid);
		RootPanel.get("slot1").add(refresh);
		RootPanel.get("slot1").add(update);
	}
	
}