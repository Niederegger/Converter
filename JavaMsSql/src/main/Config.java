package main;

public class Config {
	/**
	 * MasterValuesTable (mvt) represents MasterTable
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
	public int DatePosition = 0;
	public int IsinPosition = 2;
	public int MicPosition = 6;
	
	public String Seperator = ";";
	
	public String Source_ID = "DBAG";
	public String URLSource = "http://www.deutsche-boerse-cash-market.com/dbcm-de/instrumente-statistiken/alle-handelbaren-instrumente/boersefrankfurt";
	
	public String File = "allTradableInstruments_2017_April_19_02_00";
	public String FileEnding = ".csv";
	public String Path = "D:\\Alexey\\EclipseWorkspace\\JavaMsSql\\files\\";
	
	public String Comment = "Erstellt mit Java";
	
	public String user = "TestUser";
	public String pw = "TestUser";
	public String serverName = "ACER-2016\\SQLEXPRESS";
	public String dbName = "MasterData";
	public int port = 1433;
}
