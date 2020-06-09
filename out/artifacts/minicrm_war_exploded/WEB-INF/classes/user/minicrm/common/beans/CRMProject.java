package user.minicrm.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CRMProject implements Serializable{
	
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private CRMCustomer crmCustomer;
	
	@OneToMany(cascade = CascadeType.ALL) 
	private List<CRMProjectIssue> issues=new ArrayList<CRMProjectIssue>();
	
	public CRMProject() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CRMCustomer getCrmCustomer() {
		return crmCustomer;
	}
	public void setCrmCustomer(CRMCustomer crmCustomer) {
		this.crmCustomer = crmCustomer;
	}
	
	public List<CRMProjectIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<CRMProjectIssue> issues) {
		this.issues = issues;
	}
}
