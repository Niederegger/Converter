package main;

import java.util.Vector;

public class ContainerQuery {
	public String  sourceId;
	public String dataOrigin;
	public String urlSource;
	public String comment;
	public Vector<ContainerRow> rows = new Vector<ContainerRow>();
	
	public ContainerQuery(String sourceID, String dataOrigin, String urlSource, String comment){
		this.sourceId = sourceID;
		this.dataOrigin = dataOrigin;
		this.urlSource = urlSource;
		this.comment = comment;
	}
	
	public void add(ContainerRow cr){
		rows.add(cr);
	}
	
}
