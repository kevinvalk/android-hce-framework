package org.kevinvalk.hce.framework;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kevinvalk.hce.framework.apdu.CommandApdu;
import org.kevinvalk.hce.framework.apdu.ResponseApdu;

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
		// If it already contains this AID then just return true
		if (applets.containsKey(ByteBuffer.wrap(applet.getAid())))
			return true;
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
		CommandApdu apdu = new CommandApdu(Applet.getApdu(tag));

		// If this is not an applet selector apdu or we do not have this apdu then die!
		Applet applet = applets.get(ByteBuffer.wrap(apdu.getData()));
		if (apdu.cla != Iso7816.CLA_ISO7816 || apdu.ins != Iso7816.INS_SELECT || applet == null )
		{
			Applet.sendApdu(tag, new ResponseApdu(Iso7816.SW_APPLET_SELECT_FAILED));
			return false;
		}
		
		// Set up this applet
		applet.tag = tag;
		applet.apdu = Applet.sendApdu(tag, new ResponseApdu(Iso7816.SW_NO_ERROR)); // Correct applet selected
		
		// Lets start the and pass the response
		Thread appletThread = new Thread(applet);
		appletThread.setName(applet.getName()+" #" + appletThread.getId());
		appletThread.start();
		
		// Add this thread to the running threads
		List<Thread> appletThreads = threads.get(ByteBuffer.wrap(apdu.getData())); 
		if (appletThreads == null)
			appletThreads = new ArrayList<Thread>(); // If we never made a thread list here make the object first
		appletThreads.add(appletThread);
		
		// Check if all was ok
		return (appletThread != null);
	}
	

}
