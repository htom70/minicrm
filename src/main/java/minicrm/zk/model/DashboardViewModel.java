package minicrm.zk.model;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

import minicrm.common.beans.CRMCustomer;
import minicrm.common.beans.CRMUser;
import minicrm.server.util.AuthenticationLoginService;
import minicrm.server.util.DAOHelper;

public class DashboardViewModel {

	private int width;
	private int height;
	private String name;
	private String password;
	private CRMUser loggedUser, selectedUser;
	protected CRMCustomer selectedCustomer;
	private ListModelList<CRMCustomer> customerList;
	private String dialogPage = "";
	private String contentPage = "";
	private static Logger logger;

	@Command
	public void updateClientInfo(@BindingParam("width") int width, @BindingParam("height") int height) {
		showContentPage();
		logger.debug("A megjelen�t� k�perny� felbont�sa: " + width + " x " + height);
	}

	public void showContentPage() {
		if (!loggedUser.isAdmin()) {
			contentPage = "/pages/projects.zul";
			BindUtils.postNotifyChange(null, null, this, "contentPage");
		}
		logger.debug("User szerepk�rrel rendelkez� felhaszn�l� kezdeti n�zete bet�ltve");
	}

	@Init
	public void init() {
		logger = Logger.getLogger(this.getClass());
		loggedUser = AuthenticationLoginService.getUserCredential();
		if (!loggedUser.isAdmin()) {
			selectedCustomer = loggedUser.getCrmCustomer();
		} else
			customerList = DAOHelper.getAllCustomersFromDatabase();
		logger.debug("�gyf�l illetve �gyf�llista bet�ltve.");
	}

	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		logger.debug("Dial�gusablak megv�ltozott.");
	}

	@Command
	@NotifyChange("contentPage")
	public void showContentPage(@BindingParam("page") String page) {
		if (page == null)
			contentPage = "/pages/projects.zul";
		else
			contentPage = page;
		logger.debug("Tartalom oldal megv�ltozott.");
	}

	@Command
	public void doLogout() {
		AuthenticationLoginService.logout();
		Executions.sendRedirect("/pages/login.zul");
		logger.debug("A kijelentkez�s megt�rt�nt.");
	}

	@NotifyChange({ "contentPage", "selectedCustomer" })
	@Command
	public void loadProjectByCustomer(@BindingParam("customer") CRMCustomer customer) {
		selectedCustomer = customer;
		contentPage = "/pages/projects.zul";
		logger.debug("A kiv�lasztott �gyf�l adatai bet�lt�dtek.");
	}

	@NotifyChange({ "customerList" })
	@GlobalCommand
	public void addCustomer(@BindingParam("customer") CRMCustomer customer) {
		customerList.add(customer);
		logger.debug("�gyf�l hozz�ad�sa megt�rt�nt.");
	}

	public CRMUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(CRMUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	public CRMUser getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(CRMUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public String getContentPage() {
		return contentPage;
	}

	public void setContentPage(String contentPage) {
		this.contentPage = contentPage;
	}

	public ListModelList<CRMCustomer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(ListModelList<CRMCustomer> customerList) {
		this.customerList = customerList;
	}

	public CRMCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CRMCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDialogPage() {
		return this.dialogPage;
	}

	public void setDialogPage(String _page) {
		this.dialogPage = _page;
	}
}
