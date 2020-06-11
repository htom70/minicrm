package minicrm.server.util;

import org.apache.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PersistenceInitUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PROPERTY_FILE_PATH = "propfile";
    private static Logger LOGGER = Logger.getLogger(PersistenceInitUtil.class);

    public static void init() {
        Properties prop = new Properties();
        String properties1 = System.getProperty("propfile");
        try (InputStream inputStream = new FileInputStream(System.getProperty(PROPERTY_FILE_PATH))) {
            prop.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("Config file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.setProperty("javax.persistence.jdbc.driver", prop.getProperty("db.driver"));
        prop.setProperty("javax.persistence.jdbc.url", prop.getProperty("db.url"));
        prop.setProperty("javax.persistence.jdbc.user", prop.getProperty("db.userid"));
        prop.setProperty("javax.persistence.jdbc.password", prop.getProperty("db.password"));
        entityManagerFactory = Persistence.createEntityManagerFactory("openjpa", prop);
        LOGGER.debug("EntityManagerFactory: " + entityManagerFactory.toString());
    }

    public static void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
