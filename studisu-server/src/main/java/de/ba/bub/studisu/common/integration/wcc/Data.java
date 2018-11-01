package de.ba.bub.studisu.common.integration.wcc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Struktur zur Json-Deserialisierung.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

	@JsonProperty(value = "LocalData")
	private LocalData localData;

	public LocalData getLocalData() {
		return localData;
	}

	public void setLocalData(LocalData localData) {
		this.localData = localData;
	}
}
