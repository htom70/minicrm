package user.minicrm.common.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class CRMProjectIssue implements Serializable{
	
	@Id
	private int id;
	
	private String shortName;
	@Column(length=1000)
	private String description;
	private Timestamp createdDateTime;
	
	@ManyToOne(cascade = CascadeType.ALL) 
	private CRMUser createdBy;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	List<CRMAttachment> attachments;
	
	public CRMProjectIssue() {
		attachments=new ArrayList<CRMAttachment>();
	}
	
	public CRMUser getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(CRMUser createdBy) {
		this.createdBy = createdBy;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public List<CRMAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<CRMAttachment> attachments) {
		this.attachments = attachments;
	}
	
}
