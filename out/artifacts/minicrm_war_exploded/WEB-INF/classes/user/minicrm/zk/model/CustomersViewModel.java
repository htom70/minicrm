package user.minicrm.zk.model;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import user.minicrm.common.beans.CRMCustomer;
import user.minicrm.common.beans.CRMUser;
import user.minicrm.server.util.DAOHelper;

public class CustomersViewModel {

	private ListModelList<CRMCustomer> customerList;
	private CRMCustomer selectedCustomer, newCustomer;
	private String dialogPage = "";
	private Logger logger;

	@Init
	public void init() {
		logger = Logger.getLogger(this.getClass());
		customerList = DAOHelper.getAllCustomersFromDatabase();
		newCustomer = new CRMCustomer();
		selectedCustomer = null;
		logger.debug("Ügyfél lista betöltve.");
	}

	@NotifyChange({ "newCustomer", "dialogPage" })
	@Command
	public void saveCustomer(@BindingParam("page") String page) {
		DAOHelper.createNewCustomer(newCustomer);
		customerList.add(newCustomer);
		selectedCustomer = newCustomer;
		newCustomer = new CRMCustomer();
		dialogPage = page;
		logger.debug("Ügyfél mentése megtörtént");
	}

	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		logger.debug("Dialógusablak megváltozott.");
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
