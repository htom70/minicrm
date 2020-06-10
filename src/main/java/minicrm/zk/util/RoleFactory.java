package minicrm.zk.util;

import org.zkoss.zul.ListModelList;

public class RoleFactory {

	private static ListModelList<Role> roleList = null;
	private static RoleFactory instance = null;

	private RoleFactory() {
		createRoleList();
	}

	public static void createRoleList() {
		roleList = new ListModelList<Role>();
		roleList.add(new Role(9, "Admin"));
		roleList.add(new Role(1, "T�r�lt"));
		roleList.add(new Role(0, "Norm�l"));
	}

	public ListModelList<Role> getRoleList() {
		return roleList;
	}

	public  Role getRole(String role) {
		if (role.equals("Admin")) {
			return roleList.get(0);
		} else if (role.equals("T�r�lt")) {
			return roleList.get(1);
		} else if (role.equals("Norm�l")) {
			return roleList.get(2);
		}
		return null;
	}

	public static RoleFactory getInstance() {
		if (instance == null) {
			instance = new RoleFactory();
		}
		return instance;
	}
}
