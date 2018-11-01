package de.ba.bub.studisu.common.integration;

/**
 * Enumeration der WebService Authentication Types
 * @author OLE
 *
 */
public enum WsAuthType {
    BASIC("basic"),
    SAML("saml"),
	CONSAML("con-saml"),
	NONE("none");
	
	private String value;
	private WsAuthType(String value) {
		this.value = value;
	}
	
	/**
	 * getter fuer Value
	 * @return value string des value
	 */
	public String getValue() {
		return this.value;
	}
}
