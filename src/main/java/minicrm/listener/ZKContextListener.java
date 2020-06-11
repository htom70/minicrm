package minicrm.listener;

import minicrm.server.util.PersistenceInitUtil;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

public class ZKContextListener implements WebAppInit, WebAppCleanup {


    @Override
    public void init(WebApp webApp) throws Exception {
        PersistenceInitUtil.init();
    }

    @Override
    public void cleanup(WebApp webApp) throws Exception {
        PersistenceInitUtil.destroy();
    }
}

