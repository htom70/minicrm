package minicrm.common.beans;

import minicrm.zk.util.Role;
import minicrm.zk.util.RoleFactory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CRMUser implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String password;
	private String fullName;
//	@OneToOne(cascade = CascadeType.ALL,orphanRemoval=false)
	@OneToOne(cascade = CascadeType.ALL)
	private CRMCustomer crmCustomer;
	@OneToOne(cascade = CascadeType.ALL)
	private Role role;
	
	public CRMUser() {

	}

	public CRMUser(String email, String password, String fullName, CRMCustomer crmCustomer, Role role) {
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.crmCustomer = crmCustomer;
		this.role=role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public CRMCustomer getCrmCustomer() {
		return crmCustomer;
	}

	public void setCrmCustomer(CRMCustomer crmCustomer) {
		this.crmCustomer = crmCustomer;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean isAdmin() {
		return role.getValue() == RoleFactory.getInstance().getRole("Admin").getValue() ? true : false;
	}

}
