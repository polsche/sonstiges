package de.ba.bub.studisu.common.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Simple domain class for an external link
 * Created by loutzenhj on 13.09.2017.
 */
public class ExternalLink implements Serializable {

	private static final long serialVersionUID = -7942765270914539216L;

	private String name;
    private URI link;
    private String tooltip;

    /**
     * Constructor
     * @param linkname name of link e.g. "OSA"
     * @param linkdest the link
     * @param tooltip tooltip des Links
     * @throws URISyntaxException if linkdest not a valid URI
     */
    public ExternalLink(String linkname, String linkdest, String tooltip) throws URISyntaxException{
        if (StringUtils.isEmpty(linkname)){
            throw new IllegalArgumentException("linkname empty");
        }
        //will throw URISyntaxException here if appropriate
        URI uri = new URI(linkdest);
        this.name = linkname;
        this.link = uri;
        this.tooltip = tooltip;
    }

    public String getName() {
        return name;
    }

    public URI getLink() {
        return link;
    }

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
}
