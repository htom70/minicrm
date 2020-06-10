package minicrm.server.util;

import minicrm.common.beans.CRMUser;
import minicrm.zk.util.RoleFactory;
import org.apache.log4j.Logger;


public class AuthenticationLoginService {
	//	static Logger logger = Logger.getLogger(AuthenticationLoginService.class);
	private static Logger LOGGER = Logger.getLogger(AuthenticationLoginService.class);

	public static boolean login(String email, String pass) {
		CRMUser user = DAOHelper.getUser(email, pass);
		
		if (user != null && (user.getRole().getValue()!= RoleFactory.getInstance().getRole("T�r�lt").getValue())) {
			//(user.getRole().getValue()==RoleFactory.getInstance().getRole("Admin").getValue() || user.getRole().getValue()==RoleFactory.getInstance().getRole("Norm�l").getValue())
			SessionUtil.setAttribute("userCredential", user);
			return true;
		} else {
			LOGGER.debug("CRMUser is null for: " + email + "," + pass);
			return false;
		}
	}
	
	public static String getActualUserName() {
		return ((CRMUser)SessionUtil.getAttribute("userCredential")).getFullName();
	}
	
	public static CRMUser getUserCredential() {
		return (CRMUser)SessionUtil.getAttribute("userCredential");
	}

    public static void logout() {
    	SessionUtil.closeSession();
    }
    
    public static boolean checkUserCredential(){
    	return SessionUtil.getAttribute("userCredential") == null?false:true;
    }
    
    public static boolean isAdmin() {
    	return getUserCredential().isAdmin();
    }
}
