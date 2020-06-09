package user.minicrm.zk.model;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import user.minicrm.server.util.AuthenticationLoginService;

public class LoginViewModel {
	String dialogPage="";
	String name;
	String password;
	Logger logger;
	
	public LoginViewModel() {
		logger=logger.getLogger(this.getClass());
	}
	
	@Command
	@NotifyChange("dialogPage")
	public void doLogin() {
		if (!AuthenticationLoginService.login(name, password)) {
			dialogPage = "/pages/login-error.zul";
			logger.debug("Rossz felhasználónév és jelszó.");
		} else {
			Executions.sendRedirect("/pages/dashboard.zul");
			logger.debug("Jó felhasználónév és jelszó.");
		}
	}
	
	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		logger.debug("Dialógusablak megváltozott.");
	}
	
	public String getDialogPage() {
		return dialogPage;
	}


	public void setDialogPage(String dialogPage) {
		this.dialogPage = dialogPage;
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
	
}
