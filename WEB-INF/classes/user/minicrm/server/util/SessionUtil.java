package user.minicrm.server.util;

import javax.persistence.EntityManager;
import org.zkoss.zk.ui.Sessions;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

public class SessionUtil {

	private static Logger logger = Logger.getLogger(SessionUtil.class);

	static public void setAttribute(String name, Object obj) {
		HttpSession session = (HttpSession) Sessions.getCurrent().getNativeSession();
		if (session != null) {
			session.setAttribute(name, obj);
		}
	}

	static public Object getAttribute(String name) {
		Object ret = null;
		HttpSession session = (HttpSession) Sessions.getCurrent().getNativeSession();
		if (session != null) {
			ret = session.getAttribute(name);
		} else {
			logger.error("Error in SessionUtil.getAttribute() Session is null!");
		}
		return ret;
	}

	public static void removeAttribute(String name) {
		setAttribute(name, null);
	}

	public static void closeSession() {
		Sessions.getCurrent().invalidate();
	}

	public static EntityManager getEntityManager() {
		return (EntityManager) getAttribute("entitymanager");
	}
}
