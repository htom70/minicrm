package user.minicrm.server.util;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import user.minicrm.common.beans.CRMUser;

public class SessionListener implements HttpSessionListener {
	private Logger logger;
	private static int activeSessions = 0;

	public SessionListener() {
		logger = Logger.getLogger(this.getClass());
		logger.info("SessionListener létrehozva");
	}

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		activeSessions++;
		logger.debug("Session -" + activeSessions + " - létrejött: " + session.getId());
		Properties prop = PropertyLoader.getProperties(session);
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("openjpa", prop);
		EntityManager entitymanager = emfactory.createEntityManager();
		session.setAttribute("entitymanager", entitymanager);
		session.setAttribute("emfactory", emfactory);
		session.setAttribute("properties", prop);
		InitialAdminCreator adminCreator = new InitialAdminCreator(session);
		adminCreator.init();

	}

	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			HttpSession session = se.getSession();
			if (session.getAttribute("userCredential") != null) {
				String loggedUser = ((CRMUser) session.getAttribute("userCredential")).getFullName();
				if (AttachmentHandler.deleteTempFiles(loggedUser))
					logger.debug("A fájlok törlésre kerültek.");
			}
			activeSessions--;
			if (activeSessions == 0) {
				EntityManagerFactory emf = (EntityManagerFactory) session.getAttribute("emfactory");
				EntityManager em = (EntityManager) session.getAttribute("entitmanager");
				if (em != null) {
					em.close();
				}
				if (emf != null) {
					emf.close();
				}
			}
		} catch (Exception ex) {
			logger.fatal("", ex);
		}
	}
}
