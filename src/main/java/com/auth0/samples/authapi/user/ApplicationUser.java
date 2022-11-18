package com.auth0.samples.authapi.user;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ApplicationUsers")
public class ApplicationUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "userName")
	private String username;

	@Column(name = "password")
	private String password;

	/**
	 * add additional fields
	 * userStatus, activeStatus,
	 * @return
	 */

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
