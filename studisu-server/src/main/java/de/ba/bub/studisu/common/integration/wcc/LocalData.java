package de.ba.bub.studisu.common.integration.wcc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Struktur zur Json-Deserialisierung.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalData {

	@JsonProperty(value = "lastReleaseDate")
	private String lastReleaseDate;

	public String getLastReleaseDate() {
		return lastReleaseDate;
	}

	public void setLastReleaseDate(String lastReleaseDate) {
		this.lastReleaseDate = lastReleaseDate;
	}
}
