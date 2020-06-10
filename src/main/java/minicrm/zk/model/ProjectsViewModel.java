package minicrm.zk.model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.ListModelList;

import minicrm.common.beans.CRMAttachment;
import minicrm.common.beans.CRMCustomer;
import minicrm.common.beans.CRMProject;
import minicrm.common.beans.CRMProjectIssue;
import minicrm.common.beans.CRMUser;
import minicrm.server.util.AttachmentHandler;
import minicrm.server.util.AuthenticationLoginService;
import minicrm.server.util.DAOHelper;

public class ProjectsViewModel {

	private CRMCustomer selectedCustomer;
	private ListModelList<CRMProject> projectList;
	private List<CRMAttachment> tempAttachmentList = new ArrayList<CRMAttachment>(), oldAttachmentListOfIssue;
	private CRMProject selectedProject, newProject;
	private CRMProjectIssue newIssue, selectedIssue;
	private CRMAttachment newAttachment, selectedAttachment;
	private CRMUser selectedUser;
	private String dialogPage = "";
	private static Logger logger;
	private String popupPage;
	private int lastAttachmentID = 0, lastIssueID = 0;
	boolean isDeleteFromOldList = false;

	public ProjectsViewModel() {
		logger = Logger.getLogger(this.getClass());
		selectedUser = AuthenticationLoginService.getUserCredential();
		if (!selectedUser.isAdmin()) {
			selectedCustomer = selectedUser.getCrmCustomer();
			init();
		}
	}

	public void init() {
		selectedProject = null;
		selectedIssue = null;
		newProject = new CRMProject();
		newIssue = null;
		projectList = DAOHelper.getAllProjectsOfCustomerFromDatabase(selectedCustomer.getId());
	}

	@NotifyChange({ "selectedProject", "newProject", "dialogPage", "projectList", "selectedIssue", "newIssue" })
	@GlobalCommand
	public void setCustomer(@BindingParam("customer") CRMCustomer customer) {
		selectedCustomer = customer;
		logger.debug("Sikeres �gyf�l be�ll�t�s");
		init();
	}

	@NotifyChange({ "selectedProject", "newProject", "dialogPage", "projectList", "selectedIssue" })
	@Command
	public void saveProject(@BindingParam("page") String page) {
		newProject.setCrmCustomer(selectedCustomer);
		DAOHelper.createNewProject(newProject);
		projectList.add(newProject);
		selectedIssue = null;
		this.selectedProject = newProject;
		this.newProject = new CRMProject();
		this.dialogPage = page;
		logger.debug("Sikeres projekt ment�s");
	}

	@NotifyChange({ "selectedProject", "newProject", "dialogPage", "projectList" })
	@Command
	public void modifyProject(@BindingParam("page") String page) {
		DAOHelper.updateProject(selectedProject);
		this.dialogPage = page;
		logger.debug("Sikeres projekt m�dos�t�s");
	}

	@NotifyChange({ "selectedProject", "newProject", "dialogPage", "projectList" })
	@Command
	public void deleteProject(@BindingParam("page") String page) {
		projectList.remove(selectedProject);
		DAOHelper.deleteProject(selectedProject);
		selectedProject = new CRMProject();
		this.dialogPage = page;
		logger.debug("Sikeres projekt t�rl�s");
	}

	@NotifyChange({ "selectedIssue", "newIssue", "dialogPage", "editMode", "selectedProject" })
	@Command
	public void saveNewIssue(@BindingParam("page") String page) {
		newIssue.setCreatedBy(selectedUser);
		newIssue.setCreatedDateTime(new java.sql.Timestamp(new Date().getTime()));
		DAOHelper.createNewProjectIssue(newIssue);
		selectedProject.getIssues().add(newIssue);
		DAOHelper.updateProject(selectedProject);
		this.newIssue = new CRMProjectIssue();
		dialogPage = page;
		logger.debug("Sikeres �j hozz�sz�l�s ment�s");
	}

	@NotifyChange({ "selectedIssue", "dialogPage", "editMode", "selectedProject" })
	@Command
	public void saveModifiedIssue(@BindingParam("page") String page) {
		boolean isFoundOldAttachment;
		for (CRMAttachment attachment : oldAttachmentListOfIssue) {
			isFoundOldAttachment = false;
			Iterator<CRMAttachment> iter = selectedIssue.getAttachments().iterator();
			while (iter.hasNext()) {
				CRMAttachment att = iter.next();
				if (attachment == att) {
					isFoundOldAttachment = true;
					continue;
				}
			}
			if (isFoundOldAttachment == false)
				AttachmentHandler.deleteFile(attachment.getFilePath());
		}
		logger.debug(selectedIssue.getAttachments());
		DAOHelper.updateProjectIssue(selectedIssue);
		dialogPage = page;
		logger.debug("Sikeres hozz�sz�l�s m�dos�t�s");
	}

	@NotifyChange({ "selectedIssue", "newIssue", "dialogPage", "selectedProject" })
	@Command
	public void deleteIssue(@BindingParam("page") String page) {
		for (CRMAttachment attachment : selectedIssue.getAttachments()) {
			AttachmentHandler.deleteFile(attachment.getFilePath());
		}
		selectedProject.getIssues().remove(selectedIssue);
		DAOHelper.deleteProjectIssue(selectedIssue);
		selectedIssue = null;
		this.dialogPage = page;
		logger.debug("Sikeres hozz�szol�s t�rl�s");
	}
	
	
	private boolean checkAttachmentIsUnique(String attachmentName,List<CRMAttachment> attachmentList) {
		boolean isExist=false;
		Iterator<CRMAttachment> iterator;
		if(attachmentList!=null) {
		iterator=attachmentList.iterator();
		while(iterator.hasNext())
			if(iterator.next().getName().equals(attachmentName)){
				isExist=true;
			}
		}
		return isExist;
	}

	@NotifyChange({ "selectedAttachment", "newAttachment", "dialogPage", "newIssue", "selectedIssue","popupPage" })
	@Command
	public void saveNewAttachmentToNewIssue(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event,
			@BindingParam("page") String page) {
		if(!checkAttachmentIsUnique(event.getMedia().getName(),newIssue.getAttachments())) {
		newAttachment = new CRMAttachment();
		newAttachment.setName(event.getMedia().getName());
		newAttachment.setFilePath(event.getMedia().getName());
		newAttachment.setId(++lastAttachmentID);
		if (event.getMedia().isBinary()) {
			newAttachment = AttachmentHandler.saveAttachment(event.getMedia().getByteData(), newAttachment,
					selectedCustomer, selectedProject, newIssue);
		} else {
			newAttachment = AttachmentHandler.saveAttachment(
					event.getMedia().getStringData().getBytes(StandardCharsets.UTF_8), newAttachment, selectedCustomer,
					selectedProject, newIssue);
		}
		newIssue.getAttachments().add(newAttachment);
		selectedIssue = newIssue;
		logger.debug("Sikeres csatolm�ny ment�s �j hozz�sz�l�shoz");
		}
		else {
			this.popupPage = "/pages/error_attachment.zul";
			logger.debug("Nem siker�lt a csatolm�ny ment�se, volt m�r ilyen nev� attachment.");
		}
	}

	@Command
	public void downloadAttachments() {
		AttachmentHandler.downloadZip(selectedIssue);
		logger.debug("A csatolm�nyok sikeresen let�lt�dtek.");
	}

	@Command
	public void downloadAttachment(@BindingParam("attachment") CRMAttachment attachment) {
		AttachmentHandler.downloadAttachment(attachment);
		logger.debug("A csatolm�ny sikeresen let�lt�d�tt.");
	}

	@NotifyChange({ "selectedAttachment", "newAttachment", "dialogPage", "selectedIssue","popupPage" })
	@Command
	public void saveNewAttachmentToModifiedIssue(BindContext ctx, @BindingParam("page") String page) {
		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		if(!checkAttachmentIsUnique(event.getMedia().getName(),selectedIssue.getAttachments())) {
		newAttachment = new CRMAttachment();
		newAttachment.setName(event.getMedia().getName());
		newAttachment.setId(++lastAttachmentID);
		newAttachment.setFilePath(event.getMedia().getName());
		if (event.getMedia().isBinary()) {
			newAttachment = AttachmentHandler.saveAttachment(event.getMedia().getByteData(), newAttachment,
					selectedCustomer, selectedProject, selectedIssue);
		} else {
			newAttachment = AttachmentHandler.saveAttachment(
					event.getMedia().getStringData().getBytes(StandardCharsets.UTF_8), newAttachment, selectedCustomer,
					selectedProject, selectedIssue);
		}
		tempAttachmentList.add(newAttachment);
		selectedIssue.getAttachments().add(newAttachment);
		logger.debug("Sikeres csatolm�ny ment�s m�dos�tott hozz�sz�l�shoz.");
		}
		else {
			this.popupPage = "/pages/error_attachment.zul";
			logger.debug("Nem siker�lt a csatolm�ny ment�se, volt m�r ilyen nev� attachment.");
		}
	}

	@NotifyChange({ "selectedAttachment", "newIssue", "popupPage" })
	@Command
	public void deleteAttachmentFromNewIssue(@BindingParam("page") String page) {
		newIssue.getAttachments().remove(selectedAttachment);
		AttachmentHandler.deleteFile(selectedAttachment.getFilePath());
		selectedAttachment = null;
		this.popupPage = page;
	}

	@NotifyChange({ "selectedAttachment", "selectedIssue", "popupPage" })
	@Command
	public void deleteAttachmentFromModifiedIssue(@BindingParam("page") String page) {
		tempAttachmentList.remove(selectedAttachment);
		if (oldAttachmentListOfIssue.contains(selectedAttachment) == false)
			AttachmentHandler.deleteFile(selectedAttachment.getFilePath());
		else
			isDeleteFromOldList = true;
		selectedIssue.getAttachments().remove(selectedAttachment);
		selectedAttachment = null;
		this.popupPage = page;
		logger.debug("Csatolm�ny: "+selectedAttachment.getName()+ " t�rl�se megt�rt�nt.");
	}

	@NotifyChange({ "dialogPage", "newIssue" })
	@Command
	public void cancelNewIssue(@BindingParam("page") String page) {
		this.dialogPage = page;
		for (CRMAttachment attachment : newIssue.getAttachments()) {
			AttachmentHandler.deleteFile(attachment.getFilePath());
		}
		newIssue = null;
		logger.debug("�j hozz�szol�s megszak�t�sa.");
	}

	@NotifyChange({ "dialogPage", "selectedIssue" })
	@Command
	public void cancelModifiedIssue(@BindingParam("page") String page) {
		this.dialogPage = page;
		if (tempAttachmentList != null && tempAttachmentList.size() > 0) {
			for (CRMAttachment attachment : tempAttachmentList) {
				Iterator<CRMAttachment> iter = selectedIssue.getAttachments().iterator();
				while (iter.hasNext()) {
					CRMAttachment att = iter.next();
					if (att == attachment) {
						iter.remove();
					}
				}
				AttachmentHandler.deleteFile(attachment.getFilePath());
			}
		}
		if (isDeleteFromOldList) {
			selectedIssue.setAttachments(oldAttachmentListOfIssue);
		}
		logger.debug("Hozz�sz�l�s m�dos�t�s�nak megszak�t�sa.");
	}

	@Command
	@NotifyChange("dialogPage")
	public void showDialog(@BindingParam("page") String page) {
		this.dialogPage = page;
		logger.debug("Dial�gus ablak megv�ltozott.");
	}

	@Command
	@NotifyChange("popupPage")
	public void showPopup(@BindingParam("page") String page) {
		this.setPopupPage(page);
		logger.debug("Popup ablak megv�ltozott.");
	}

	@Command
	@NotifyChange({ "dialogPage", "tempAttachmentList" })
	public void modifySelectedIssue(@BindingParam("page") String page) {
		oldAttachmentListOfIssue = new ArrayList<CRMAttachment>(selectedIssue.getAttachments());
		lastAttachmentID = DAOHelper.getLastIdAttachmentFromDatabase();
		tempAttachmentList.clear();
		this.dialogPage = page;
		logger.debug("Hozz�sz�l�s m�dos�t�sa ablak akt�v.");
	}

	@Command
	@NotifyChange({ "dialogPage", "tempAttachmentList", "newIssue" })
	public void createNewIssue(@BindingParam("page") String page) {
		newIssue = new CRMProjectIssue();
		lastIssueID = DAOHelper.getLastIdIssueFromDatabase();
		newIssue.setId(++lastIssueID);
		lastAttachmentID = DAOHelper.getLastIdAttachmentFromDatabase();
		this.dialogPage = page;
		logger.debug("�j hozz�sz�l�s hozz�ad�sa ablak akt�v.");
	}

	public CRMCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CRMCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ListModelList<CRMProject> getProjectList() {
		return projectList;
	}

	public void setProjectList(ListModelList<CRMProject> projectList) {
		this.projectList = projectList;
	}

	public CRMProject getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(CRMProject selectedProject) {
		this.selectedProject = selectedProject;
	}

	public CRMProject getNewProject() {
		return newProject;
	}

	public void setNewProject(CRMProject newProject) {
		this.newProject = newProject;
	}

	public CRMProjectIssue getSelectedIssue() {
		return selectedIssue;
	}

	public void setSelectedIssue(CRMProjectIssue selectedIssue) {
		this.selectedIssue = selectedIssue;
	}

	public String getDialogPage() {
		return dialogPage;
	}

	public void setDialogPage(String dialogPage) {
		this.dialogPage = dialogPage;
	}

	public CRMProjectIssue getNewIssue() {
		return newIssue;
	}

	public void setNewIssue(CRMProjectIssue newIssue) {
		this.newIssue = newIssue;
	}

	public CRMUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(CRMUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	public CRMAttachment getSelectedAttachment() {
		return selectedAttachment;
	}

	public void setSelectedAttachment(CRMAttachment selectedAttachment) {
		this.selectedAttachment = selectedAttachment;
	}

	public List<CRMAttachment> getTempAttachmentList() {
		return tempAttachmentList;
	}

	public void setTempAttachmentList(List<CRMAttachment> tempAttachmentList) {
		this.tempAttachmentList = tempAttachmentList;
	}

	public String getPopupPage() {
		return popupPage;
	}

	public void setPopupPage(String popupPage) {
		this.popupPage = popupPage;
	}
}
