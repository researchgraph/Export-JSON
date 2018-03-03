package org.rdswitchboard.exporters.graph;

import org.neo4j.graphdb.Label;

public class Labels {
	public static enum NodeType implements Label {
		dataset, grant, researcher, institution, service, publication, pattern, version
	}
	
	public static enum NodeSource implements Label {
		system, ands, arc, nhmrc, web, orcid, dryad, crossref, figshare, cern, dli, dara, nci, gesis
	}

}
