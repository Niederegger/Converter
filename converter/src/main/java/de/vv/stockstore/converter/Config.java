package de.vv.stockstore.converter;

public class Config {
	/**
	 * MasterValuesTable (mvt) represents MasterTable
	 * currently not in use?!
	 */
	public String[] MasterValuesTable = { 
			"MVU_SOURCE_ID", 
			"MVU_ISIN", 
			"MVU_MIC", 
			"MVU_AS_OF_DATE",
			"MVU_FIELDNAME",
			"MVU_STRINGVALUE", 
			"MVU_DATA_ORIGIN", 
			"MVU_URLSOURCE", 
			"MVU_COMMENT" 
			};

	/**
	 * MasterValuesFields (mvf) represents positions of interest
	 * these values are going to be inserted into fieldname
	 * Positions stores the Column position of each field
	 * we start counting at position 0 instead of 1
	 */
	public String[] MasterValuesFields = {
			"Instrument", // 1
			"WKN", // 5
			"Mnemonic", // 5
			"CCP eligible", // 7
			"Unit of Quotation", // 78
			"Interest Rate", // 79
			"Closing Price Previous Business Day", // 82
			"Market Segment", // 83
			"Settlement Currency", // 98
	};
	
	/**
	 * Positions stores the Array index for mvf
	 */
	public int[] Positions = {
		1, //Instrument
		4, //WKN
		5, //Mnemonic
		7, //CCP eligible
		78, //Unit of Quotation
		79, //Interest Rate
		82, //Closing Price Previous Business Day
		83, //Market Segment
		98, //Settlement Currency
	};
	
	/**
	 * MV_AS_OF_DATE_NEEDED (maodn) represents 
	 * whether or not a query needs the as of date
	 */
	public boolean[] MV_AS_OF_DATE_NEEDED = {
			false, //Instrument
			false, //WKN
			false, //Mnemonic
			false, //CCP eligible
			false, //Unit of Quotation
			false, //Interest Rate
			true,  //Closing Price Previous Business Day
			false, //Market Segment
			false, //Settlement Currency
	};
	
	/**
	 * stores MV_AS_OF_DATE
	 */
	public int DatePosition = 0; // Position des Datums innerhalb der Tabelle
	public int IsinPosition = 2; // Position der ISIN innerhalb der Tabell
	public int MicPosition = 6; // Position der MIC innerhalb der Tabelle
	
	public String Seperator = ";"; // Der Seperator der genutzt wird um in der csv File die Trennung  der einzelnen Einträge zu erstellen
	
	public String Source_ID = "DBAG"; // Die ID die diesem Dokument zugewießen ist
	// das ist die Website, von der das Dokument runtergeladen wurde
	public String URLSource = "http://www.deutsche-boerse-cash-market.com/dbcm-de/instrumente-statistiken/alle-handelbaren-instrumente/boersefrankfurt"; 
	// die File die eingelesen und in die Datenbank geschrieben werden soll <- hier wird sich eventuell noch was aendern
	public String File = "allTradableInstruments_2017_April_19_02_00";
	public String FileEnding = ".csv"; // das fileEnding beschriebt die Endung der File, und somit auch den DateiTyp
	public String Path = "D:\\Alexey\\EclipseWorkspace\\JavaMsSql\\files\\"; // Hier wird der Pfad angegeben in welchem sich die Datei befindet
	// dieser Komment wird mit in die Query geschleust
	public String Comment = "Erstellt mit Java";
	// anschließend folgen !WICHTGE! SQL daten
	public String user = "TestUser"; // User zum arbeiten mit der SQL Datenbnak
	public String pw = "TestUser"; // das Password des Users
	public String serverName = "ACER-2016\\SQLEXPRESS"; // Der Server-Name des SQL-Servers
	public String dbName = "MasterData"; // Der DatenBank Name
	public int port = 1433; // der Port ueber welchen der Zugriff gestattet ist
}
