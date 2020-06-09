package user.minicrm.zk.util;

import java.util.Map;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import user.minicrm.server.util.AuthenticationLoginService;

public class AuthInitiator implements Initiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {
		if (AuthenticationLoginService.checkUserCredential() == false) {
			Executions.sendRedirect("/pages/login.zul");
			return;
		}
	}
}
