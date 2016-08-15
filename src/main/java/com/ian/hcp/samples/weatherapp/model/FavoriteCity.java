package com.ian.hcp.samples.weatherapp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "WEATHERAPP_CITY")
@NamedQueries({@NamedQuery(name = "FavoriteCities", query = "SELECT c from FavoriteCity c"),
				@NamedQuery(name = "FavoriteCityById", query = "SELECT c from FavoriteCity c where c.id = :id")})
@XmlRootElement(name = "city")
@XmlAccessorType(XmlAccessType.FIELD)
public class FavoriteCity extends BaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", length = 36, nullable = true)
	private String id = null;
	
	@Column(name = "NAME", length = 128, nullable = true)
	private String name = null;
	
	@Column(name = "COUNTRY", length = 2, nullable = true)
	private String countryCode = null;

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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
