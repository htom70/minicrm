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
        try (InputStream inputstream = new FileInputStream(System.getProperty("propfile"))){
//            input = session.getServletContext().getResourceAsStream("/minicrm/minicrm.properties");
//            System.getProperties().load(new FileInputStream(System.getProperty("propertyfile")));
//            input = session.getServletContext().getResourceAsStream("/config/minicrm.properties");
//            input = session.getServletContext().getResourceAsStream("\\config\\minicrm.properties");
//            prop.load(input);
            String propName = System.getProperty("propfile");
            LOGGER.debug("propName: " + propName);
            LOGGER.debug("Inputstream: "+inputstream.toString());
            prop.load(inputstream);
            prop.setProperty("javax.persistence.jdbc.driver", prop.getProperty("db.driver"));
            prop.setProperty("javax.persistence.jdbc.url", prop.getProperty("db.url"));
            prop.setProperty("javax.persistence.jdbc.user", prop.getProperty("db.userid"));
            prop.setProperty("javax.persistence.jdbc.password", prop.getProperty("db.password"));
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
        return prop;
    }
}
