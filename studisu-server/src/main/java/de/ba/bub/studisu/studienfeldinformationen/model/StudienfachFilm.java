package de.ba.bub.studisu.studienfeldinformationen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mapper
 * 
 * @author csl
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StudienfachFilm implements java.io.Serializable {

	private static final long serialVersionUID = -2806882262776530856L;

	@JsonProperty("LocalData")
	private LocalData localData;
	
    public LocalData getLocalData() {
		return localData;
	}

	public void setLocalData(LocalData localData) {
		this.localData = localData;
	}
}
