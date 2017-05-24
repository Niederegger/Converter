package de.vv.stockstore.converter.esma.registers_mifid_sha;

public class EntryList {
	public ResponseHeader responseHeader;
	public Response response;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("EntryList:\n");
		sb.append(responseHeader);
		sb.append("\n");
		sb.append(response);
		
		return sb.toString();
	}
}
