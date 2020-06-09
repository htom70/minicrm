package user.minicrm.server.util;

import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class PropertyLoader {
	private static Properties prop = null;
	private static Logger logger = Logger.getLogger(PropertyLoader.class);

	public static Properties getProperties(HttpSession session) {
		if (prop == null) {
			prop = new Properties();
		}

		InputStream input = null;
		try {
			input = session.getServletContext().getResourceAsStream("/config/minicrm.properties");
			prop.load(input);
			prop.setProperty("javax.persistence.jdbc.driver", prop.getProperty("db.driver"));
			prop.setProperty("javax.persistence.jdbc.url", prop.getProperty("db.url"));
			prop.setProperty("javax.persistence.jdbc.user", prop.getProperty("db.userid"));
			prop.setProperty("javax.persistence.jdbc.password", prop.getProperty("db.password"));
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		return prop;
	}
}
