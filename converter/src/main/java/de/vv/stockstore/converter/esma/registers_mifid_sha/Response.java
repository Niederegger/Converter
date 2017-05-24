package de.vv.stockstore.converter.esma.registers_mifid_sha;

public class Response {
	public String numFound;
	public String start;
	public Entry[] docs;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Response:");
		sb.append("\nnumFound: " + numFound);
		sb.append("\nstart: " + start);
		if (docs == null)
			sb.append("\nentries: none");
		else
			sb.append("\nentries: " + docs.length);

		return sb.toString();
	}
}
