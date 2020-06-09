package user.minicrm.zk.model;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import user.minicrm.common.beans.CRMUser;
import user.minicrm.server.util.DAOHelper;
import user.minicrm.zk.util.Role;

public class UsersViewModel{
	
	private ListModelList<CRMUser> userList;
	private CRMUser selectedUser,newUser;
	private String dialogPage="";
	private static Logger logger;
	private ListModelList<Role> roleList;

	@Init
	public void init() {
		logger = Logger.getLogger(this.getClass());
		roleList=DAOHelper.getAllRolesFromDatabase();
		userList=DAOHelper.getAllUsersFromDatabase();
		newUser=new CRMUser();
		newUser.setRole(roleList.get(0));
		selectedUser=null;
		logger.debug("Adatbázisból az adatok betöltõdtek.");
	}
	
	@NotifyChange({"userList","newUser","dialogPage","selectedUser"})
	@Command
	public void saveUser(@BindingParam("page") String page) {
		DAOHelper.createNewUser(newUser);
		userList.add(newUser);
		selectedUser=newUser;
		newUser=new CRMUser();
		newUser.setRole(roleList.get(0));
		this.dialogPage=page;
		logger.debug("Felhasználó mentése sikeres volt.");
	}
	
	@NotifyChange({"userList","dialogPage","selectedUser"})
	@Command
	public void modifyUser(@BindingParam("page") String page) {
		DAOHelper.updateUser(selectedUser);
		dialogPage=page;
		logger.debug("Felhasználó módosítása sikeres volt.");
	}
	
	@NotifyChange({ "selectedUser", "dialogPage", "userList" })
	@Command
	public void deleteUser(@BindingParam("page") String page) {
		userList.remove(selectedUser);
		DAOHelper.deleteUser(selectedUser);
		selectedUser = null;
		this.dialogPage = page;
		logger.debug("Sikeres felhasználó törlés");
	}
	
	@NotifyChange("dialogPage")
	@Command
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		BindUtils.postNotifyChange(null, null, this, "dialogPage");
		logger.debug("Dialógusablak megváltozott.");
	}
	
	public ListModelList<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(ListModelList<Role> roleList) {
		this.roleList = roleList;
	}
	
	public ListModelList<CRMUser> getUserList() {
		return userList;
	}

	public void setUserList(ListModelList<CRMUser> userList) {
		this.userList = userList;
	}

	public CRMUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(CRMUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	public CRMUser getNewUser() {
		return newUser;
	}

	public void setNewUser(CRMUser newUser) {
		this.newUser = newUser;
	}

	public String getDialogPage() {
		return dialogPage;
	}

	public void setDialogPage(String dialogPage) {
		this.dialogPage = dialogPage;
	}
}
