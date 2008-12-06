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
 * MRUCache.java
 * For storing MRU (most recently used list) in RMS 
 *
 * 
 * Callsign data are taken from SKMM (MCMC) website http://www.skmm.gov.my/registers1/aa.asp?aa=AARadio
 * Thanks to 9M2CIO (http://9m2cio.info) for providing the callsign database in SQL/CSV format
 */


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import net.mypapit.java.StringTokenizer;
import net.mypapit.java.MRU;
import de.enough.polish.io.RmsStorage;
import java.util.Vector;


public class MRUCache {
	Vector vector;
	RmsStorage storage;
	MRUSerialize mrulist;
	
	public MRUCache() {
			this.storage = new RmsStorage();
			try {
					vector = (Vector) this.storage.read("cache");
					mrulist = (MRUSerialize) this.vector.elementAt(0);
			} catch (IOException ioex) {
				//ioexeption means that storage doesnt exists yet
					mrulist = new MRUSerialize(3);
			}
	}
	
	public MRUSerialize load() {
		return mrulist;
	}
	
	public void save(MRUSerialize mru) throws IOException {
		vector = new Vector();
		vector.addElement(mru);
		
		this.storage.save(vector,"cache");
		
	
	}

}
