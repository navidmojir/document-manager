package ir.mojir.document_manager.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

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
