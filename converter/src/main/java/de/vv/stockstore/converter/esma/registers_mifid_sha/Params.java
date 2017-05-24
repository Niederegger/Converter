package de.vv.stockstore.converter.esma.registers_mifid_sha;

public class Params {
	public String indent;
	public String q;
	public String wt;
	public String fq;
	public String rows;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Params:");
		sb.append("\nindent: " + indent);
		sb.append("\nq: " + q);
		sb.append("\nwt: " + wt);
		sb.append("\nfq: " + fq);
		sb.append("\nrows: " + rows);
		
		return sb.toString();
	}
}
