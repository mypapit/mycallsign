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
 * SendResult.java
 * Send SMS, Bluetooh Text interface..
 */

import javax.microedition.lcdui.*;


public class SendResult extends Form
{

public Command cmdSMS;
public Command cmdBack;

public TextField tfName, tfPhoneNo;

public SendResult() {
	super("Send Callsign Info");
	//cmdSMS = new Command("Send",Command.SCREEN,1);
	//cmdBack = new Command("Back",Command.BACK,99);

	//this.addCommand(cmdSMS);
	//this.addCommand(cmdBack);

	StringItem si = new StringItem("Instruction","Enter phone number to send the callsign info to");
//#style .directNumber
	tfPhoneNo = new TextField("Phone Number ","",20,TextField.PHONENUMBER);

	this.append(si);
	this.append(tfPhoneNo);


}

}
