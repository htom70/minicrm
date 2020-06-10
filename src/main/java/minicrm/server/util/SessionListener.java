package minicrm.server.util;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import minicrm.common.beans.CRMUser;

public class SessionListener implements HttpSessionListener {
	private static final Logger LOGGER = Logger.getLogger(SessionListener.class);
	private static int activeSessions = 0;

	public SessionListener() {
		LOGGER.info("SessionListener létrehozva");
	}

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		activeSessions++;
		LOGGER.debug("Session -" + activeSessions + " - létrejött: " + session.getId());
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
					LOGGER.debug("A fájlok törlésre kerültek.");
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
			LOGGER.fatal("", ex);
		}
	}
}
