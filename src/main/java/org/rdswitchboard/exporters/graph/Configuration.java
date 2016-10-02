package org.rdswitchboard.exporters.graph;

import org.neo4j.graphdb.Label;

public class Configuration {
	private final Label[] type;
	private final Label[] linkedSource;
	private final String key;
	
	public Configuration(final Label[] type, final Label[] linkedSource, final String key) {
		this.type = type;
		this.linkedSource = linkedSource;
		this.key = key;
	}
	
	public Label[] getType() {
		return type;
	}

	public Label[] getLinkedSource() {
		return linkedSource;
	}
	
	public String getKey() {
		return key;
	}
}
