package minicrm.zk.model;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import minicrm.common.beans.CRMCustomer;
import minicrm.server.util.DAOHelper;

import java.util.logging.LogManager;

public class CustomersViewModel {

	private ListModelList<CRMCustomer> customerList;
	private CRMCustomer selectedCustomer, newCustomer;
	private String dialogPage = "";
	private Logger LOGGER=Logger.getLogger(CustomersViewModel.class);

	@Init
	public void init() {
		LOGGER = Logger.getLogger(this.getClass());
		customerList = DAOHelper.getAllCustomersFromDatabase();
		newCustomer = new CRMCustomer();
		selectedCustomer = null;
		LOGGER.debug("�gyf�l lista bet�ltve.");
	}

	@NotifyChange({ "newCustomer", "dialogPage" })
	@Command
	public void saveCustomer(@BindingParam("page") String page) {
		DAOHelper.createNewCustomer(newCustomer);
		customerList.add(newCustomer);
		selectedCustomer = newCustomer;
		newCustomer = new CRMCustomer();
		dialogPage = page;
		LOGGER.debug("�gyf�l ment�se megt�rt�nt");
	}

	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		LOGGER.debug("Dial�gusablak megv�ltozott.");
	}

	public ListModelList<CRMCustomer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(ListModelList<CRMCustomer> CustomerList) {
		this.customerList = CustomerList;
	}

	public CRMCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CRMCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public CRMCustomer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(CRMCustomer newCustomer) {
		this.newCustomer = newCustomer;
	}

	public String getDialogPage() {
		return dialogPage;
	}

	public void setDialogPage(String dialogPage) {
		this.dialogPage = dialogPage;
	}

}
