package de.patrickolschewsky.stback.stback.model;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "stunden_eintrag")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
        allowGetters = true)	
public class StundenEintrag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String datum;
	private String kommenZeit;
	private String gehenZeit;
	private String leistung;
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
	@ManyToOne
	User benutzer;
	
	
	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDatum() {
		return datum;
	}


	public void setDatum(String datum) {
		this.datum = datum;
	}


	public String getKommenZeit() {
		return kommenZeit;
	}


	public void setKommenZeit(String kommenZeit) {
		this.kommenZeit = kommenZeit;
	}


	public String getGehenZeit() {
		return gehenZeit;
	}


	public void setGehenZeit(String gehenZeit) {
		this.gehenZeit = gehenZeit;
	}


	public String getLeistung() {
		return leistung;
	}


	public void setLeistung(String leistung) {
		this.leistung = leistung;
	}


	public User getBenutzer() {
		return benutzer;
	}


	public void setBenutzer(User benutzer) {
		this.benutzer = benutzer;
	}


	
	
	
}
