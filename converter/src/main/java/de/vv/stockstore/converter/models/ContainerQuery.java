package de.vv.stockstore.converter.models;

import java.util.Vector;

/**
 * ContainerQuery stores all Queries, which are going to be inserted to DB
 */
public class ContainerQuery {
	public String sourceId;
	public String dataOrigin;
	public String urlSource;
	public String comment;
	
	// innerhalb dieses Vectors werden verarbeitete Reihen akkumuliert
	public Vector<ContainerRow> rows = new Vector<ContainerRow>();

	/**
	 * Constructor
	 * 
	 * @param sourceID
	 * @param dataOrigin
	 * @param urlSource
	 * @param comment
	 */
	public ContainerQuery(String sourceID, String dataOrigin, String urlSource, String comment) {
		this.sourceId = sourceID;
		this.dataOrigin = dataOrigin;
		this.urlSource = urlSource;
		this.comment = comment;
	}

	/**
	 * adds a ContainerRow to row Vector of this object
	 * 
	 * @param cr
	 *            - ContainterRow
	 */
	public void add(ContainerRow cr) {
		rows.add(cr);
	}
	
	public void resetRows(){
		rows = new Vector<ContainerRow>();
	}
	
	public int rowAmount(){
		return rows.size();
	}

}
