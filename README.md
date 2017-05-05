# Converter
## Synopsis and Motivation

This project is used to parsed data from a .csv file to a Microsoft SQL Database. The first File which contains the right naming and file ending is grabbed (represented by a tile ~ at the end of filename). Then each row within the .csv file is parsed to collect needed values. These values are queried to database, which is finished by calling an update stored procedure. The grabbed file is freed and written into a bakup directory.

## Installation

You can find a precompiled version here: 'Converter/converter/preCompiled/'. Follow the next steps to run this program:
1. Use the precompiled Jar or compile your own one.
2. Choose a directory where your files are placed. Create a directory named bak inside this one.
3. Adjust your config file (more information down below). // take your time to setup the config correctly
4. Execute: 
```
...>java -jar Converter.jar converterAlleHandelbareInstrumente.conf
```

## API Reference

- gson: working with config file (it's written as  a Json-Object)
- mssql-jdbc: establish connection to Microsoft Server SQL

## Tests

todo

## Config

Config is written as a Json-Object, here is an example:
```
{
  "MasterValuesTable": [
    "MVU_SOURCE_ID",
    "MVU_ISIN",
    "MVU_MIC",
    "MVU_AS_OF_DATE",
    "MVU_FIELDNAME",
    "MVU_STRINGVALUE",
    "MVU_DATA_ORIGIN",
    "MVU_URLSOURCE",
    "MVU_COMMENT"
  ],
  "MasterValuesFields": [
    "Instrument",
    "WKN",
    "Mnemonic",
    "CCP eligible",
    "Unit of Quotation",
    "Interest Rate",
    "Closing Price Previous Business Day",
    "Market Segment",
    "Settlement Currency"
  ],
  "Positions": [
    1,
    4,
    5,
    7,
    78,
    79,
    82,
    83,
    98
  ],
  "MV_AS_OF_DATE_NEEDED": [
    false,
    false,
    false,
    false,
    false,
    false,
    true,
    false,
    false
  ],
  "DatePosition": 0,
  "IsinPosition": 2,
  "MicPosition": 6,
  "Seperator": ";",
  "Source_ID": "DBAG",
  "URLSource": "http://www.deutsche-boerse-cash-market.com/dbcm-de/instrumente-statistiken/alle-handelbaren-instrumente/boersefrankfurt",
  "File": "allTradableInstruments",
  "FileEnding": ".csv",
  "Path": "D:\\Alexey\\EclipseWorkspace\\converter\\preCompiled\\Destination\\",
  "Comment": "Erstellt mit Java",
  "user": "TestUser",
  "pw": "TestUser",
  "serverName": "ACER-2016\\SQLEXPRESS",
  "dbName": "MasterData",
  "port": 1433
}

```  
Definition of these Values:

| variable      | explanation |
| ------------- | --------- |
| MasterValuesTable | obsolete (definition of table-columns) |
| MasterValuesFields  | all needed fields |
| MV_AS_OF_DATE_NEEDED  | whether AS_OF_DATE is needed |
| DatePosition  | position of date |
| IsinPosition  | position of ISIN |
| MicPosition  | position of MIC |
| Seperator  | which seperator does this file use |
| Source_ID  | your source ID |
| URLSource  | from which website was this file loaded |
| File  | Filename has to contain this String |
| FileEnding  | file extension |
| Path  | path to where all files are located |
| Comment  | this comment is stored to db |
| user  | SQL: User |
| pw  | SQL: User password |
| serverName  | SQL: Servername |
| dbName  | SQL: Databasename |
| port  | SQL: port |


Attention! : 
- Since this program is written in Java, you have to use doubled backslashes instead of single ones, cause it's an escape character in Java.
