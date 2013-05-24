package org.kevinvalk.hce.framework;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.kevinvalk.hce.framework.apdu.AidApdu;

public class Handler
{
	Map<byte[], Applet> applets;
	Map<byte[], List<Thread>> threads;
	
	public Handler()
	{
		applets.clear();
	}
	
	/**
	 * Registers an applet to the framework
	 * 
	 * @param Applet applet
	 * @return boolean
	 */
	public boolean register(Applet applet)
	{
		return (applets.put(applet.getAid(), applet) == null);
	}

	/**
	 * Handles new connected tags
	 * 
	 * @param TagWrapper tag
	 * @return boolean
	 */
	public boolean handleTag(TagWrapper tag)
	{
		Apdu apdu = getApdu(tag);
		
		
		// First apdu is always aid selector
		AidApdu aidApdu = new AidApdu();
		apdu.getAs(aidApdu);
		
		// If this is not an applet selector apdu or we do not have this apdu then die!
		Applet applet = applets.get(aidApdu.aid);
		if (aidApdu.cla != Iso7816.CLA_ISO7816 && aidApdu.ins != Iso7816.INS_SELECT && applet != null )
		{
			sendApdu(tag, new Apdu(Iso7816.SW_APPLET_SELECT_FAILED));
			return false;
		}
		else
		{
			// All good so answer with no error and pass the response to the applet
			apdu = sendApdu(tag, new Apdu(Iso7816.SW_NO_ERROR));
		}
		
		// Lets start the and pass the response
		Thread appletThread = new Thread(applet);
		appletThread.setName("Applet #" + appletThread.getId());
		appletThread.start();
		threads.get(aidApdu.aid).add(appletThread);
		return (appletThread != null);
	}
	
	/**
	 * Sends an apdu to the tag and receives the answer
	 * 
	 * @param tag
	 * @param Apdu The apdu to send
	 * @return Apdu response
	 */
	public Apdu sendApdu(TagWrapper tag, Apdu apdu)
	{
		try
		{
			byte[] response = tag.transceive(apdu.getBuffer());
			return new Apdu(response);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * Gets an apdu from the tag by sending zero bytes
	 * 
	 * @param tag
	 * @return
	 */
	public Apdu getApdu(TagWrapper tag)
	{
		return sendApdu(tag, new Apdu(new byte[0]));
	}
}
