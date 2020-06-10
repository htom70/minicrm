package minicrm.server.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class PropertyLoader {
    private static Properties prop = null;
    private static final Logger LOGGER = Logger.getLogger(PropertyLoader.class);

    public static Properties getProperties(HttpSession session) {
        if (prop == null) {
            prop = new Properties();
        }

        InputStream input = null;
        try {
//            input = session.getServletContext().getResourceAsStream("/minicrm/minicrm.properties");
//            System.getProperties().load(new FileInputStream(System.getProperty("propertyfile")));
            prop.load(new FileInputStream(System.getProperty("property_file_path")));
            prop.setProperty("javax.persistence.jdbc.driver", prop.getProperty("db.driver"));
            prop.setProperty("javax.persistence.jdbc.url", prop.getProperty("db.url"));
            prop.setProperty("javax.persistence.jdbc.user", prop.getProperty("db.userid"));
            prop.setProperty("javax.persistence.jdbc.password", prop.getProperty("db.password"));
        } catch (Exception ex) {
            LOGGER.error(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
        return prop;
    }
}
