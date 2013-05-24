package org.kevinvalk.hce.framework;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kevinvalk.hce.framework.apdu.AidApdu;

public class HceFramework
{
	Map<ByteBuffer, Applet> applets;
	Map<ByteBuffer, List<Thread>> threads;
	
	public HceFramework()
	{
		applets = new HashMap<ByteBuffer, Applet>();
		threads = new HashMap<ByteBuffer, List<Thread>>();

	}
	
	/**
	 * Registers an applet to the framework
	 * 
	 * @param Applet applet
	 * @return boolean
	 */
	public boolean register(Applet applet)
	{
		return (applets.put(ByteBuffer.wrap(applet.getAid()), applet) == null);
	}

	/**
	 * Handles new connected tags
	 * 
	 * @param TagWrapper tag
	 * @return boolean
	 */
	public boolean handleTag(TagWrapper tag)
	{
		AidApdu apdu = AidApdu.fromApdu(Applet.getApdu(tag));

		// If this is not an applet selector apdu or we do not have this apdu then die!
		Applet applet = applets.get(ByteBuffer.wrap(apdu.aid));
		if (apdu.cla != Iso7816.CLA_ISO7816 || apdu.ins != Iso7816.INS_SELECT || applet == null )
		{
			Applet.sendApdu(tag, new Apdu(Iso7816.SW_APPLET_SELECT_FAILED));
			return false;
		}
		
		// Set up this applet
		applet.tag = tag;
		
		// Lets start the and pass the response
		Thread appletThread = new Thread(applet);
		appletThread.setName("Applet #" + appletThread.getId());
		appletThread.start();
		
		// Add this thread to the running threads
		List<Thread> appletThreads = threads.get(ByteBuffer.wrap(apdu.aid)); 
		if (appletThreads == null)
			appletThreads = new ArrayList<Thread>(); // If we never made a thread list here make the object first
		appletThreads.add(appletThread);
		
		// Check if all was ok
		return (appletThread != null);
	}
	

}
