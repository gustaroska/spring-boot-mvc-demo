package com.demo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;
import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "students")
@RedisHash("students")
public class Student implements Serializable, Persistable<String>{ 
	
	@Id
    private String id;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "male")
    private boolean male;
	
	@Column(name = "grade")
    private int grade;
	
	@Column(name = "status")
    private String status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name = "deleted_date")
	private Date deletedDate;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Student(String id) {
        this.id = id;
    }

    public Student(String id, String name, boolean male, int grade, String status) {
        this.id = id;
        this.name = name;
        this.male = male;
        this.grade = grade;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", male=" + male + ", grade=" + grade + ", status=" + status
				+ ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate + ", deletedDate="
				+ deletedDate + "]";
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		// TODO Auto-generated method stub
		if(this.createdDate == null) {
			this.createdDate = new Date(); // because this executed only when deal with database
		}
		
		if(this.deletedDate != null) {
			this.deletedDate = null;
			return true; // when undo delete from redis, use insert statement
		}
		
		return this.lastModifiedDate == null;
	}
}