package user.minicrm.zk.util;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Integer value;
	private String text;

	public Role() {
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Role(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public Integer getValue() {
		return this.value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getText();
	}
}