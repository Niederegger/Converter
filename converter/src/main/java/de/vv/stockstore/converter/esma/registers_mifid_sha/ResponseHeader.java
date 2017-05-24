package de.vv.stockstore.converter.esma.registers_mifid_sha;

public class ResponseHeader {
	public String status;
	public String QTime;
	public Params params;
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ResponseHeader:");
		sb.append("\nstatus: " + status);
		sb.append("\nQTime: " + QTime);
		sb.append("\n"+params);
		
		return sb.toString();
	}
}
