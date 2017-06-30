package de.vv.stockstore.converter.esma.registers_mifid_sha;

public class Entry {
	 public String _root_;
     public String sha_modificationDateStr;
     public String sha_countryCode;
     public String sha_relevantAuthority;
     public String sha_exchangeRate;
     public String sha_name;
     public String sha_sms;
     public String sha_status;
     public String sha_dailyTransactions;
     public String id;
     public String sha_type;
     public String sha_freeFloatRange;
     public String sha_isin;
     public String sha_adt;
     public String sha_avt;
     public String _version_;
     public String timestamp;
     
     public void trim(){
    	 if(_root_!=null)_root_=_root_.trim();
    	 if(sha_modificationDateStr!=null)sha_modificationDateStr=sha_modificationDateStr.trim();
    	 if(sha_countryCode!=null)sha_countryCode=sha_countryCode.trim();
    	 if(sha_relevantAuthority!=null)sha_relevantAuthority=sha_relevantAuthority.trim();
    	 if(sha_exchangeRate!=null)sha_exchangeRate=sha_exchangeRate.trim();
    	 if(sha_name!=null)sha_name=sha_name.trim();
    	 if(sha_sms!=null)sha_sms=sha_sms.trim();
    	 if(sha_status!=null)sha_status=sha_status.trim();
    	 if(sha_dailyTransactions!=null)sha_dailyTransactions=sha_dailyTransactions.trim();
    	 if(id!=null)id=id.trim();
    	 if(sha_type!=null)sha_type=sha_type.trim();
    	 if(sha_freeFloatRange!=null)sha_freeFloatRange=sha_freeFloatRange.trim();
    	 if(sha_isin!=null)sha_isin=sha_isin.trim();
    	 if(sha_adt!=null)sha_adt=sha_adt.trim();
    	 if(sha_avt!=null)sha_avt=sha_avt.trim();
    	 if(_version_!=null)_version_=_version_.trim();
    	 if(timestamp!=null)timestamp=timestamp.trim();
     }
}
