package com.talentbuilder.talentbuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talentbuilder.talentbuilder.enumType.UserPrivilageType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@SuppressWarnings("serial")
@Entity
@Table(name = "privilege")
public class Privilege implements Serializable{

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	 @Column(name = "privilege_id", nullable = false, unique = true)
	private long id;

	@Column(name = "name")
    private UserPrivilageType name;

	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "privileges") 
	private Collection<User> users;
    
    public Privilege() {
    }

    public Privilege(long code) {
    	setId(code);
    }

    public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public UserPrivilageType getName() {
        return name;
    }

    public void setName(UserPrivilageType name) {
        this.name = name;
    }

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}


}
