import de.enough.polish.io.Serializable;
public class CallsignInfo implements Serializable
{

public String handle,callsign,apparatus,expiry;

public CallsignInfo() {}


public CallsignInfo (String handle,String callsign,String apparatus,String expiry)
{
		this.handle = handle;
		this.callsign= callsign;
		this.apparatus = apparatus;
		this.expiry = expiry;
	
	
}


public void add(String handle,String callsign,String apparatus,String expiry)
{
		this.handle = handle;
		this.callsign= callsign;
		this.apparatus = apparatus;
		this.expiry = expiry;
	
	
}

public String toSB()
{
	StringBuffer sb = new StringBuffer("");
	sb.append(handle+"||"+callsign+"||"+apparatus+"||"+expiry);
	
	return sb.toString();
	
}

}