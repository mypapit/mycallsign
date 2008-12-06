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
 * MYCallsign.java
 * Mobile Malaysian Callsign Search Application 
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


public class MYCallsign extends MIDlet implements CommandListener, ItemCommandListener {


TextField tfCS;
Command cmdSearch, cmdEXIT,cmdBack,cmdAbout,cmdSMS, cmdSend;
Form form, frmCallsign;
AboutForm aboutform;
Display display;
StringBuffer stringbuffer;
	
/*
MRUCache mrucache;
MRUSerialize mrulist;
*/

CallsignInfo callsigninfo;

	
//#if polish.api.wmapi	
SendResult sendResult;
//#endif
	
public MYCallsign(){
	
	StringItem siHelp = new StringItem("Information","Start by entering ham callsign that you wish to enquire");
	siHelp.setLayout(siHelp.LAYOUT_EXPAND | siHelp.LAYOUT_NEWLINE_AFTER);
	tfCS = new TextField("Callsign","",7,TextField.ANY);
	tfCS.setLayout(tfCS.LAYOUT_EXPAND);
		
	tfCS.setItemCommandListener(this);
	
	callsigninfo = new CallsignInfo();
	
	cmdSearch = new Command("Search",Command.ITEM,1);
	cmdEXIT = new Command("Exit",Command.EXIT,99);
	cmdBack = new Command("Back",Command.BACK,80);
	cmdAbout = new Command("About",Command.HELP,10);
	//#if polish.api.wmapi
	cmdSMS = new Command("SMS",Command.SCREEN,5);
	cmdSend = new Command("Send",Command.SCREEN,4);
	//#endif
	form = new Form("Malaysian Callsign Search");
	
	form.append(siHelp);
	form.append(tfCS);
	//form.addCommand(cmdSearch);
	form.addCommand(cmdEXIT);
	form.addCommand(cmdAbout);
	form.setCommandListener(this);
	display = Display.getDisplay(this);
	
	tfCS.addCommand(cmdSearch);
	/*
	mrucache = new MRUCache();
	mrulist = mrucache.load();
*/

}


public void startApp(){

	display.setCurrent(form);
}


public void pauseApp(){

}

public void destroyApp(boolean flag) {

	notifyDestroyed();
}



public void showAlert(String title,String text,Displayable d) {

		Alert a = new Alert(title,text,null,AlertType.WARNING);
		a.setTimeout(Alert.FOREVER);
		display.setCurrent(a,d);

}

public void showAlert(String text)
{
	showAlert("Alert",text,form);

}

public void commandAction(Command cmd, Displayable disp) {
	if (cmd == cmdEXIT) {
		destroyApp(false);
	} else if (cmd == cmdSearch) {
		if (tfCS.getString().length() < 5) {
				showAlert("Please enter a valid callsign");
				return;
		}
		
		/*
		 CallsignInfo csinfo;
		csinfo = (CallsignInfo) mrulist.get(tfCS.getString());
		showAlert(csinfo.handle+" wtf\n");
		if (csinfo != null) {
				processCallsignInfo(csinfo.toSB());
				System.out.println("from cache");
				showAlert("fucked!");
				return;
		}
		 */
		
		
			GetData getdata = new GetData(this);
			getdata.start();
			System.out.println("from internet");
	} 
	//#if polish.api.wmapi
	else if ( (disp == sendResult) && (cmd == cmdBack) ) {
			display.setCurrent(frmCallsign);
	}
	//#endif
		
		else if (cmd == cmdBack) {
			display.setCurrent(form);
	} else if (cmd == cmdAbout) {
		aboutform = new AboutForm("About","MYCallsign 2.0","/i.png");
		aboutform.addCommand(cmdBack);
		
		aboutform.setHyperlink("http://m.ashamradio.com",this);
		aboutform.setCommandListener(this);
		aboutform.setCopyright("Mohammad Hafiz (9W2WTF)","2008");
		aboutform.append("Malaysian Callsign Information");
		aboutform.append("\n\nThis program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 2.0");
		display.setCurrent(aboutform);
	} 
	//#if polish.api.wmapi
	else if (cmd == cmdSend) {
			sendResult = new SendResult();
		//sendResult.addCommand(cmdSMS);
		sendResult.tfPhoneNo.addCommand(cmdSMS);
		sendResult.tfPhoneNo.setItemCommandListener(this);
		sendResult.addCommand(cmdBack);
		sendResult.setCommandListener(this);
		display.setCurrent(sendResult);
		
	} 
	//#endif

}

public void commandAction(Command cmd, Item item) {
	if (item == tfCS) {
		if (tfCS.getString().length() < 5) {
				showAlert("Please enter a valid callsign");
				return;
		}
			GetData getdata = new GetData(this);
			getdata.start();

	}
	
	//#if polish.api.wmapi
	else if (cmd == cmdSMS) {
			sendSMS();
	}
	//#endif
	
}




public void processCallsignInfo(String sb) {

	StringItem handle,callsign,apparatus,expiry;
	
	frmCallsign = new Form("Callsign Info");
	
	StringTokenizer tok = new StringTokenizer(sb,"||");
	
	if (tok.countTokens() < 2) {
			showAlert("Error retrieving callsign information.");
	}
	
	handle = new StringItem("Handle",tok.nextToken());
	callsign = new StringItem("Callsign",tok.nextToken());
	apparatus = new StringItem("AA",tok.nextToken());
	expiry = new StringItem("Expiry",tok.nextToken());
	
	handle.setLayout(handle.LAYOUT_EXPAND);
	callsign.setLayout(handle.LAYOUT_EXPAND);
	apparatus.setLayout(handle.LAYOUT_EXPAND);
	expiry.setLayout(handle.LAYOUT_EXPAND);	
	
	callsigninfo.add(handle.getText(),callsign.getText(),apparatus.getText(),expiry.getText());
	
	
		
	stringbuffer = new StringBuffer("");
	
	stringbuffer.append(handle.getText()+"\n");
	stringbuffer.append(callsign.getText()+"\n");
	stringbuffer.append(apparatus.getText()+"\n");
	stringbuffer.append(expiry.getText()+"\n");
	
	frmCallsign.append(handle);
	frmCallsign.append(callsign);
	frmCallsign.append(apparatus);
	frmCallsign.append(expiry);
	
	frmCallsign.addCommand(cmdBack);
	frmCallsign.addCommand(cmdSend);
	frmCallsign.setCommandListener(this);
	
	display.setCurrent(frmCallsign);
	/*try {
		mrulist.put(callsigninfo.callsign,callsigninfo);
		mrucache.save(mrulist);
		//System.out.println("saving cache");
		//showAlert("shitfucked!");
	} catch (Exception ioex) {
			ioex.printStackTrace();
			showAlert(ioex.toString());
	}
	 */
	
}


//#if polish.api.wmapi

	public void sendSMS() {

		if (sendResult.tfPhoneNo.getString().length() < 5) {
			showAlert("Alert","Please enter a valid phone number",sendResult);
			return;
		};

		SendSMS s = new SendSMS(this);
		s.start();


	}

//#endif



}


class GetData implements Runnable,CommandListener {



MYCallsign midlet;
Gauge gauge;
Form formRunning;
Command cmdCancel;

public GetData (MYCallsign midlet) {

	this.midlet = midlet;
	formRunning = new Form("Retrieving data...");
	gauge = new Gauge("Processing",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
	cmdCancel = new Command("Cancel",Command.CANCEL,1);
	formRunning.append(gauge);
	formRunning.addCommand(cmdCancel);
	formRunning.setCommandListener(this);
	midlet.display.setCurrent(formRunning);


}

public void commandAction (Command cmd,Displayable disp)
{
	if (cmd == cmdCancel) {
		midlet.display.setCurrent(midlet.form);
		return;
	}

}

public void start() {
       Thread t = new Thread(this);
       t.start();
}


public void run() {
	HttpConnection conn=null;
	InputStream is=null;
	String sb;

	try {
			String sUrl = "http://callsign.ashamradio.com/callsign/" + URLEncode(midlet.tfCS.getString());
			conn = (HttpConnection) Connector.open(sUrl,Connector.READ);
			if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
					is = conn.openInputStream();
					byte buf[] = new byte[128];
					int total =0;
					while (total < 128) {
						int count = is.read(buf,total,128-total);
						if (count<0) {
							break;
						}
						total += count;
					}

					sb = new String(buf,0,total);
				
				    if (sb.length() < 10) {
							midlet.showAlert("Callsign info not found");
							is.close();
							conn.close();
						
							return;
					}
				
					midlet.processCallsignInfo(sb);
					
			} else if (conn.getResponseCode() == HttpConnection.HTTP_NOT_FOUND) {
					midlet.showAlert("This application has expired. Please get a new version from http://m.ashamradio.com/");

			} else {
				midlet.showAlert("Server busy or unavailable. Please try again later");
			}

	} catch (SecurityException sex) {
		midlet.showAlert("Connection failed. You need to authorize this application to access network");
	} catch (IOException ioex) {
			midlet.showAlert("Connection failed. Please try again later.");
	} catch (Exception e){
		midlet.showAlert(e.toString());
		e.printStackTrace();
		//midlet.display.setCurrent(midlet.form);
	} finally {
		try {

			if (is != null) {
				is.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (IOException ioexception) {}
			is =null;
			conn =null;


	}





}


public String URLEncode(String s)
{
		if (s!=null) {
			StringBuffer tmp = new StringBuffer();
			int i=0;
			try {
				while (true) {
					int b = (int)s.charAt(i++);
					if ((b>=0x30 && b<=0x39) || (b>=0x41 && b<=0x5A) || (b>=0x61 && b<=0x7A)) {
						tmp.append((char)b);
					}
					else {
						tmp.append("%");
						if (b <= 0xf) tmp.append("0");
						tmp.append(Integer.toHexString(b));
					}
				}
			}
			catch (Exception e) {}
			return tmp.toString();
		}
		return null;
}


}

