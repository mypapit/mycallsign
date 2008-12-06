/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as published by
 *  the Free Software Foundation
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
 * MYCallsign 2.0 <info@mypapit.net>
 * Copyright 2008 Mohammad Hafiz bin Ismail (9w2wtf). All rights reserved.
 *
 * SendSMS.java
 * Send SMS action thread 
 */

//#if polish.api.wmapi
import javax.wireless.messaging.*;
//#endif

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;
import net.mypapit.java.StringTokenizer;

public class SendSMS implements Runnable {
private MYCallsign midlet;
private Display display;
private Gauge g;
private Form formRunning;

public SendSMS(MYCallsign midlet)
{
		this.midlet = midlet;
		display = Display.getDisplay(this.midlet);
		formRunning = new Form("Sending Result");
		g = new Gauge("Processing...",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
		formRunning.append(g);
		display.setCurrent(formRunning);

}

public void start() {
       Thread t = new Thread(this);
       t.start();
}

public void run() {

//StringBuffer sb = new StringBuffer("");

boolean prob=false;
//#if polish.api.wmapi

	try {
		String addr = "sms://" + midlet.sendResult.tfPhoneNo.getString();
		MessageConnection conn = (MessageConnection) Connector.open(addr);
		TextMessage msg =
		(TextMessage)conn.newMessage(MessageConnection.TEXT_MESSAGE);
		//sb.append("Definition for : " + midlet.tf.getString() +" \n\n" );
		
		midlet.stringbuffer.append("\n-----------\n[MYCallsign]\nhttp://m.ashamradio.com/");
		msg.setPayloadText(midlet.stringbuffer.toString());
		conn.send(msg);

		} catch (IllegalArgumentException iae) {
			midlet.showAlert("Alert","Please fill in the form",midlet.sendResult);
			prob=true;

		} catch (SecurityException sex) {
			midlet.showAlert("Error","Not Allowed To Send SMS, check security settings",midlet.sendResult);
			prob=true;
		} catch (InterruptedIOException iioex) {
			midlet.showAlert("Error","Sending Timed-Out - Please retry later",midlet.sendResult);
			prob=true;
		}

		catch (Exception e) {
			midlet.showAlert("Error", "Send Result failed :" + e.toString(),midlet.sendResult);
			prob=true;
		}

		
		if (prob==false){
			midlet.showAlert("Definition sent!");
			display.setCurrent(midlet.form);
		}
		midlet.stringbuffer = null;
		midlet.sendResult = null;

//#endif
	}


}