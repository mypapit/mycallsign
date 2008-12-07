/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as 
 *  published by the Free Software Foundation
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * MYCallsign 2.0 <info@mypapit.net> (9w2wtf)
 * Copyright 2008 Mohammad Hafiz bin Ismail. All rights reserved.
 *
 * Info url : 
 * http://kirostudio.com
 * http://mycallsign.googlecode.com/
 * http://m.ashamradio.com/
 * 
 * CallsignInfo.java
 * Callsign information class
 *
 * Mobile Malaysian Callsign Search Application 
 *
 * 
 * Callsign data are taken from SKMM (MCMC) website http://www.skmm.gov.my/registers1/aa.asp?aa=AARadio
 * Thanks to 9M2CIO (http://9m2cio.info) for providing the callsign database in SQL/CSV format
 */

import java.io.*;


public class CallsignInfo
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

public byte[] read() throws IOException {
	byte[] returnbyte = new byte[4];
	ByteArrayOutputStream bout;
	DataOutputStream dout;

	bout = new ByteArrayOutputStream();
	dout = new DataOutputStream(bout);
	
	
	dout.writeUTF(callsign);
	dout.writeUTF(handle);
	dout.writeUTF(apparatus);
	dout.writeUTF(expiry);
	dout.flush();
	
	return bout.toByteArray();
	
	
	//return returnbyte;
	
	
}

public void write(byte[] input) throws IOException
{
	ByteArrayInputStream bin = new ByteArrayInputStream(input);
	DataInputStream din = new DataInputStream(bin);
	
	callsign=din.readUTF();
	handle=din.readUTF();
	apparatus=din.readUTF();
	expiry=din.readUTF();
}

}