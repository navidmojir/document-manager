package ir.mojir.document_manager.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String fileName;
	
	private String fileExtension;
	
	@Transient
	@JsonIgnore
	private byte[] bytes;
	
	private String creatorUser;
	
	@ElementCollection
	private Set<String> grantedUsers;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}

	public Set<String> getGrantedUsers() {
		return grantedUsers;
	}

	public void setGrantedUsers(Set<String> grantedUsers) {
		this.grantedUsers = grantedUsers;
	}
	
	public void addGrantedUser(String username) {
		if(this.grantedUsers == null)
			this.grantedUsers = new HashSet<>();
		this.grantedUsers.add(username);
	}
	
	public void removeGrantedUser(String username) {
		if(this.grantedUsers == null)
			return;
		this.grantedUsers.remove(username);
	}


	
	
}
