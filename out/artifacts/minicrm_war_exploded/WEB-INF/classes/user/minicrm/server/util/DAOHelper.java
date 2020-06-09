package user.minicrm.server.util;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;

import user.minicrm.common.beans.CRMAttachment;
import user.minicrm.common.beans.CRMCustomer;
import user.minicrm.common.beans.CRMProject;
import user.minicrm.common.beans.CRMProjectIssue;
import user.minicrm.common.beans.CRMUser;
import user.minicrm.zk.util.Role;

public class DAOHelper {

	private static final String SQL_SELECT_ALL_USERS = "SELECT u FROM CRMUser u ";
	private static final String SQL_SELECT_ALL_ROLES = "SELECT r FROM Role r ";
	private static final String SQL_SELECT_ALL_CUSTOMERS = "SELECT c FROM CRMCustomer c ";
	private static final String SQL_SELECT_ALL_PROJECTS = "SELECT p FROM CRMProject p WHERE p.crmCustomer.id=:customerid ";
	private static final String SQL_SELECT_ALL_ISSUES = "SELECT i FROM CRMProjectIssue i WHERE i.createdBy.id=:userid ";
	private static final String SQL_SELECT_USER_PASSWORD = "SELECT u FROM CRMUser u WHERE u.email=:email and u.password=:password ";
	private static final String SQL_MAX_ID_FROM_ATTACHMENTS="SELECT max(a.id) from CRMAttachment a";
	private static final String SQL_MAX_ID_FROM_ISSUES="SELECT max(i.id) from CRMProjectIssue i";
	private static Logger logger = Logger.getLogger(DAOHelper.class);

	public static ListModelList<CRMUser> getAllUsersFromDatabase() {
		EntityManager em = SessionUtil.getEntityManager();
		ListModelList<CRMUser> lml = new ListModelList<CRMUser>();
		Iterator<CRMUser> iterator=em.createQuery(SQL_SELECT_ALL_USERS).getResultList().iterator();
		while(iterator.hasNext()) {
			CRMUser user=iterator.next();
			lml.add(user);
		}
		logger.debug("Összes felhasználó lekérdezése megtörtént");
		return lml;
	}
	
	public static int getLastIdAttachmentFromDatabase() {
		EntityManager em = SessionUtil.getEntityManager();
		Object max = em.createQuery(SQL_MAX_ID_FROM_ATTACHMENTS).getSingleResult(); 
		if(max == null){
			return 0;
		}else{
			return (Integer)max;
		}
	}
	
	public static int getLastIdIssueFromDatabase() {
		EntityManager em = SessionUtil.getEntityManager();
		Object max = em.createQuery(SQL_MAX_ID_FROM_ISSUES).getSingleResult(); 
		if(max == null){
			return 0;
		}else{
			return (Integer)max;
		}
	}

	public static ListModelList<CRMCustomer> getAllCustomersFromDatabase() {
		EntityManager em = SessionUtil.getEntityManager();
		ListModelList<CRMCustomer> lml = new ListModelList<CRMCustomer>();
		lml.addAll(em.createQuery(SQL_SELECT_ALL_CUSTOMERS).getResultList());
		logger.debug("Összes ügyfél lekérdezése megtörtént");
		return lml;
	}

	public static ListModelList<CRMProject> getAllProjectsOfCustomerFromDatabase(int id) {
		EntityManager em = SessionUtil.getEntityManager();
		ListModelList<CRMProject> lml = new ListModelList<CRMProject>();
		lml.addAll(em.createQuery(SQL_SELECT_ALL_PROJECTS).setParameter("customerid", id).getResultList());
		logger.debug("Összes projekt lekérdezése megtörtént");
		return lml;
	}

	public static ListModelList<CRMProjectIssue> getAllProjectIssuesOfUserFromDatabase(int id) {
		EntityManager em = SessionUtil.getEntityManager();
		ListModelList<CRMProjectIssue> lml = new ListModelList<CRMProjectIssue>();
		lml.addAll(em.createQuery(SQL_SELECT_ALL_ISSUES).setParameter("userid", id).getResultList());
		logger.debug("Összes hozzászólás lekérdezése megtörtént");
		return lml;
	}

	public static int createNewAttachment(CRMAttachment attachment) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(attachment);
		em.getTransaction().commit();
		return attachment.getId();
	}

	public static void createNewProjectIssue(CRMProjectIssue projectIssue) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(projectIssue);
		em.getTransaction().commit();
	}

	public static void createNewProject(CRMProject project) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(project);
		em.getTransaction().commit();
	}

	public static void createNewUser(CRMUser user) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
	}
	
	public static void createNewRole(Role role) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(role);
		em.getTransaction().commit();
	}

	public static void createNewCustomer(CRMCustomer customer) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.persist(customer);
		em.getTransaction().commit();
	}

	public static void updateProjectIssue(CRMProjectIssue projectIssue) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(projectIssue);
		em.getTransaction().commit();
	}
	
	public static void updateUser(CRMUser user) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(user);
		em.getTransaction().commit();
	}

	public static void updateProject(CRMProject project) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(project);
		em.getTransaction().commit();
	}

	public static void updateCustomer(CRMCustomer customer) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(customer);
		em.getTransaction().commit();
	}
	public static void updateAttachment(CRMAttachment attachment) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.merge(attachment);
		em.getTransaction().commit();
	}
		

	public static void deleteProjectIssue(CRMProjectIssue projectIssue) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.remove(projectIssue);
		em.getTransaction().commit();
	}

	public static void deleteUser(CRMUser user) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.remove(user);
		em.getTransaction().commit();
	}

	public static void deleteProject(CRMProject project) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.remove(project);
		em.getTransaction().commit();
	}

	public static CRMUser getUser(String email, String password) {
		EntityManager em = SessionUtil.getEntityManager();
		CRMUser user=null;
		if(em != null) {
			List list = em.createQuery(SQL_SELECT_USER_PASSWORD).setParameter("email", email).setParameter("password", password).getResultList();
			if(!list.isEmpty()){
				user= (CRMUser) list.get(0);
			}
		}else{
			logger.fatal("DAOHELPER.getUser() EntityManager is NULL! ");
		}
			
		return user;
	}

	public static void deleteAttachment(CRMAttachment attachment) {
		EntityManager em = SessionUtil.getEntityManager();
		em.getTransaction().begin();
		em.remove(attachment);
		em.getTransaction().commit();
	}

	public static ListModelList<Role> getAllRolesFromDatabase() {
		EntityManager em = SessionUtil.getEntityManager();
		ListModelList<Role> lml = new ListModelList<Role>();
		lml.addAll(em.createQuery(SQL_SELECT_ALL_ROLES).getResultList());
		logger.debug("Összes jogosultság lekérdezése megtörtént");
		return lml;
	}
}
