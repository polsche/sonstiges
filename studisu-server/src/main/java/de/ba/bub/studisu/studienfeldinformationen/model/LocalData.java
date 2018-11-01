package de.ba.bub.studisu.studienfeldinformationen.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mapper
 * 
 * @author csl
 */
public class LocalData implements java.io.Serializable {
    
	private static final long serialVersionUID = -1717311366586666352L;

	@JsonProperty("IsJson")
	private int isJson;
	
	@JsonProperty("METAFLDS")
	private String metaflds;
	
	@JsonProperty("changedMonikers")
	private String changedMonikers;
	
	@JsonProperty("changedSubjects")
	private String changedSubjects;
	
	@JsonProperty("dDocName")
	private String dDocName;
	
	@JsonProperty("dUser")
	private String dUser;
	
	@JsonProperty("idcToken")
	private String idcToken;
	
	@JsonProperty("localizedForResponse")
	private int localizedForResponse;
	
	@JsonProperty("refreshMonikers")
	private String refreshMonikers;
	
	@JsonProperty("refreshSubMonikers")
	private String refreshSubMonikers;
	
	@JsonProperty("refreshSubjects")
	private String refreshSubjects;
	
	@JsonProperty("xBenBpBatchBerufeTvID")
	private int xBenBpBatchBerufeTvID = -1;
	
	@JsonProperty("IdcService")
	private String idcService;

    public int getIsJson() {
		return isJson;
	}

	public void setIsJson(int isJson) {
		this.isJson = isJson;
	}

	public String getMetaflds() {
		return metaflds;
	}

	public void setMetaflds(String metaflds) {
		this.metaflds = metaflds;
	}

	public String getChangedMonikers() {
		return changedMonikers;
	}

	public void setChangedMonikers(String changedMonikers) {
		this.changedMonikers = changedMonikers;
	}

	public String getChangedSubjects() {
		return changedSubjects;
	}

	public void setChangedSubjects(String changedSubjects) {
		this.changedSubjects = changedSubjects;
	}

	public String getDDocName() {
		return dDocName;
	}

	public void setDDocName(String dDocName) {
		this.dDocName = dDocName;
	}

	public String getDUser() {
		return dUser;
	}

	public void setDUser(String dUser) {
		this.dUser = dUser;
	}

	public String getIdcToken() {
		return idcToken;
	}

	public void setIdcToken(String idcToken) {
		this.idcToken = idcToken;
	}

	public int getLocalizedForResponse() {
		return localizedForResponse;
	}

	public void setLocalizedForResponse(int localizedForResponse) {
		this.localizedForResponse = localizedForResponse;
	}

	public String getRefreshMonikers() {
		return refreshMonikers;
	}

	public void setRefreshMonikers(String refreshMonikers) {
		this.refreshMonikers = refreshMonikers;
	}

	public String getRefreshSubMonikers() {
		return refreshSubMonikers;
	}

	public void setRefreshSubMonikers(String refreshSubMonikers) {
		this.refreshSubMonikers = refreshSubMonikers;
	}

	public String getRefreshSubjects() {
		return refreshSubjects;
	}

	public void setRefreshSubjects(String refreshSubjects) {
		this.refreshSubjects = refreshSubjects;
	}

	public String getIdcService() {
		return idcService;
	}

	public void setIdcService(String idcService) {
		this.idcService = idcService;
	}
	
	public int getXBenBpBatchBerufeTvID() {
		return xBenBpBatchBerufeTvID;
	}
	public void setXBenBpBatchBerufeTvID(int xBenBpBatchBerufeTvID) {
		this.xBenBpBatchBerufeTvID = xBenBpBatchBerufeTvID;
	}

}
