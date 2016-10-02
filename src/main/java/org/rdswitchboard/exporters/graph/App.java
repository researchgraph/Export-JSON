package org.rdswitchboard.exporters.graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.neo4j.graphdb.Label;
import org.parboiled.common.StringUtils;
import org.rdswitchboard.exporters.graph.Exporter;
import org.rdswitchboard.exporters.graph.Labels.NodeSource;
import org.rdswitchboard.exporters.graph.Labels.NodeType;


/**
 * Main class for Nexus RDA records exported
 * 
 * Parameters: 
 * 	 $1 : Neor4j main folder (neo4j)
 *   $2 : Output folder (rda/json)
 *   $3 : Export level (3)
 *   $4 : Maximum nodes per file (100)
 *   $5 : Maximum new siblings per node (10)
 * 
 * Installation:
 * 
 * To compile project, please execute: 
 * $ mvn package
 * 
 * To run, please copy program jar file and whole jars directory from target into main project folder and run
 * $ java -jar <program name>.jar
 * 
 * @version 2.0.0
 * @author Dima Kudriavcev (dmitrij@kudriavcev.info)
 * @date 24 May 2015 	
 *
 */

public class App {
	//private static final String SOURCES_NAME = "conf/rds_sources.csv";
	//private static final String PROPERTY_ORIGINAL_KEY = "original_key";
	private static final String PROPERTY_ORCID_ID = "orcid";
	private static final String PROPERTY_DOI = "doi";
	private static final String PROPERTY_LOCAL_ID = "local_id";

	
	
	private static void loadProperties(Properties properties, String propertiesFile) throws IOException {
		try (InputStream in = new FileInputStream(propertiesFile)) {
			properties.load(in);
		}
	}
	
	public static void main(String[] args) {
		try {
			Properties properties = new Properties();
			loadProperties(properties, "properties/export_graph_json.properties");
			
			String sourceNeo4jFolder = properties.getProperty("neo4j", "neo4j");
			
			String s3Bucket = properties.getProperty("s3.bucket");
			String s3Key = properties.getProperty("s3.key");
			boolean s3Public = Boolean.parseBoolean(properties.getProperty("s3.public", "false"));
		
			int maxLevel = Integer.parseInt(properties.getProperty("max.level", "2"));
			int maxNodes = Integer.parseInt(properties.getProperty("max.nodes", "100"));
			int maxSiblings = Integer.parseInt(properties.getProperty("max.siblings", "10"));

			int testNodeId = Integer.parseInt(properties.getProperty("test.node.id", "0"));
			
			if (StringUtils.isEmpty(s3Bucket)) {
				System.out.println("S3 Bucket name can not be empty");
				
				System.exit(1);
			}

			if (StringUtils.isEmpty(s3Key)) {
				System.out.println("S3 Key prefix can not be empty");
				
				System.exit(1);
			}
			
	        Map<Label, Label[]> sources = new HashMap<Label, Label[]>();
	        sources.put(NodeSource.ands, null);
	        sources.put(NodeSource.dryad, new Label[] { NodeSource.crossref });
	        sources.put(NodeSource.cern, new Label[] { NodeSource.ands, NodeSource.dryad, NodeSource.crossref, NodeSource.orcid, 
	        		NodeSource.web, NodeSource.dli, NodeSource.dara});
	        			
	        Map<Label, String> keys = new HashMap<Label, String>();
	        keys.put(NodeSource.ands, PROPERTY_LOCAL_ID);
	        keys.put(NodeSource.orcid, PROPERTY_ORCID_ID);
	        keys.put(NodeSource.dara, PROPERTY_DOI);
	        
	       	Exporter expoter = new Exporter();
	       	expoter.setNeo4jFolder(sourceNeo4jFolder);
	       	expoter.setAwsInstanceProfileCredentials();
	       	
	       	expoter.setS3Bucket(s3Bucket);
	       	expoter.setS3Key(s3Key);
	       	expoter.enablePublicReadRights(s3Public);
	       	expoter.setMaxLevel(maxLevel);
	       	expoter.setMaxNodes(maxNodes);
	       	expoter.setMaxSiblings(maxSiblings);
	       	expoter.setTestNodeId(testNodeId);
	        expoter.process(NodeType.dataset, sources, keys);
	        
		} catch (Exception e) {
			e.printStackTrace();
			
			System.exit(1);
		} 	
	}
}

