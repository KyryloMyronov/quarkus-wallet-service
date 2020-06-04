package com.kyrylomyronov.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class TrackableEntity {

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified", nullable = false)
	private Date modified;

	public String getId() {
		return id;
	}

	public Date getModified() {
		return modified;
	}

	@PrePersist
	public void onCreate() {
		if (this.id == null) {
			this.id = UUID.randomUUID().toString();
		}
		modified = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		modified = new Date();
	}
}
