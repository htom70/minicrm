package user.minicrm.zk.model;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import user.minicrm.server.util.AuthenticationLoginService;

public class LoginModelView {
	String dialogPage="";
	String name;
	String password;
	
	@Command
	@NotifyChange("dialogPage")
	public void doLogin() {
		if (!AuthenticationLoginService.login(name, password)) {
			dialogPage = "/pages/login-error.zul";
		} else {
			Executions.sendRedirect("/pages/dashboard.zul");
		}
	}
	
	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
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
