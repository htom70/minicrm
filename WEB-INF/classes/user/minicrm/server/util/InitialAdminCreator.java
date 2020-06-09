package user.minicrm.server.util;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import user.minicrm.common.beans.CRMCustomer;
import user.minicrm.common.beans.CRMUser;
import user.minicrm.zk.util.Role;
import user.minicrm.zk.util.RoleFactory;

public class InitialAdminCreator {
	EntityManager entityManager;
	Logger logger;
	private final int ADMIN_ROLE=9;
	private final String GET_ALL_ADMINS="SELECT u.id FROM CRMUser u WHERE u.role.value = "+ADMIN_ROLE;

	public InitialAdminCreator(HttpSession sess) {
		entityManager = (EntityManager)sess.getAttribute("entitymanager");
		logger = Logger.getLogger(this.getClass());
	}

	private boolean hasAdmin() {
		boolean hasAdminInDatabase=entityManager.createQuery(GET_ALL_ADMINS).getResultList().isEmpty()?false:true;
		return hasAdminInDatabase;
	}
	
	public void init() {
		if(hasAdmin()==false) {
			entityManager.getTransaction().begin();
			CRMCustomer crmCustomer=new CRMCustomer("User Rendszerház Kft.");
			CRMUser crmUser=new CRMUser("admin@admin.hu","Password01","Teszt Admin",crmCustomer,RoleFactory.getInstance().getRole("Admin"));
			logger.debug("Jogosultságok létrehozva az adatbázisban.");
			for(Role role:RoleFactory.getInstance().getRoleList()) {
				entityManager.persist(role);
			}
			entityManager.persist(crmCustomer);
			entityManager.persist(crmUser);
			entityManager.getTransaction().commit();
			logger.debug("Admin user még nem volt az adatbázisban, létrehozva");	
		}
		else {
			logger.debug("Admin user létezik az adatbázisban.");
		}
	}
}
