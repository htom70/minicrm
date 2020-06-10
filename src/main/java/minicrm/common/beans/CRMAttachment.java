package minicrm.common.beans;

import java.io.Serializable;

//import javax.persistence.Entity;
//import javax.persistence.Id;
import javax.persistence.*;

@Entity
public class CRMAttachment implements Serializable{
	@Id
	private int id;
	private String name;
	private String filePath;
	
	public CRMAttachment() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
