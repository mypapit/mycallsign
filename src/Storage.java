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
 * Storage.java
 * Handling data storage and Record Enumerator
 *
 * Mobile Malaysian Callsign Search Application 
 *
 * 
 * Callsign data are taken from SKMM (MCMC) website http://www.skmm.gov.my/registers1/aa.asp?aa=AARadio
 * Thanks to 9M2CIO (http://9m2cio.info) for providing the callsign database in SQL/CSV format
 */

import javax.microedition.rms.*;
import javax.microedition.io.*;
import java.io.*;

public class Storage {
	RecordStore store;

public Storage ()  {
	
	try {
		store = RecordStore.openRecordStore("cache",true);
	} catch (Exception ex){
		
	}
	
}


public int getSize() 
{
		try {
				return store.getSize();
		} catch (Exception ex) {
			
		}
	
		return 0;
}

public int getSizeAvailable() {

		try {
				return store.getSizeAvailable();
		} catch (Exception ex) {
			
		}
	
	return 0;

}

public int getRecords() throws Exception {
	return store.getNumRecords();
}

public void save(byte[] data) throws Exception {

	store.addRecord(data,0,data.length);
	
}

public void close() {
	
	try {
		store.closeRecordStore();
	} catch (Exception ex)
	{
		
	}
}

public RecordEnumeration enumerate() throws Exception 
{

	RComparator rcomp = new RComparator();
	
	return store.enumerateRecords(null,rcomp,false);

}

public RecordEnumeration find(String key) throws Exception 
{
		
		RComparator rcomp = new RComparator();
		RFilter rfilter = new RFilter(key.toUpperCase());
		return store.enumerateRecords(rfilter,rcomp,false);
}


class RFilter implements RecordFilter  
{
	String key;
	
	public RFilter (String key)
	{
			this.key=key;
	}
	
public boolean matches (byte[] compare) {
	CallsignInfo csinfo = new CallsignInfo();
			
		
	try {
		csinfo.write(compare);
	} catch (IOException ioex) {
		return false;
	}
	
	int cmp = key.compareTo(csinfo.callsign);
	
	if (cmp==0) {
		return true;
	} else {
		return false;
	}
	
		
			
		
	}
}

class RComparator implements RecordComparator 
{
	
	public int compare(byte[] rec1, byte[] rec2)
	{
		
		CallsignInfo callsigninfo = new CallsignInfo();
		
		try {
			callsigninfo.write(rec2);
		} catch (IOException ioex) {
				return RecordComparator.EQUIVALENT;
		}
		
		String str1 = new String(rec1);
		
		int cmp=str1.compareTo(callsigninfo.callsign);
		
		if (cmp == 0) {
			return RecordComparator.EQUIVALENT;
		} else if (cmp < 0) {
			return RecordComparator.PRECEDES;
		} else {
			return RecordComparator.FOLLOWS;
		}
	
	}

}
}