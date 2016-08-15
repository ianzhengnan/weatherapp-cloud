package com.ian.hcp.samples.weatherapp.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@MappedSuperclass
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id")
public abstract class BaseObject {
	
	@Id
	@Column(name="GUID", length = 36)
	private String guid = UUID.randomUUID().toString();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", updatable = false)
	private Date createdAt = null;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFICATION_DATE")
	private Date lastModifiedAt = null;
	
	@Column(name = "CREATED_BY", updatable = false, length = 20)
	private String createdBy = null;
	
	@Column(name = "MODIFIED_BY", length = 20)
	private String lastModifiedBy = null;
	
	@PreUpdate
	protected void updateAuditInformation(){
		lastModifiedAt = new Date();
	}
	
	@PrePersist
	protected void generateAuditInformation(){
		final Date now = new Date();
		
		createdAt = now;
		lastModifiedAt = now;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
