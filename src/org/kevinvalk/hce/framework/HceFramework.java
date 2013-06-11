package org.kevinvalk.hce.framework;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.kevinvalk.hce.framework.apdu.Apdu;
import org.kevinvalk.hce.framework.apdu.CommandApdu;
import org.kevinvalk.hce.framework.apdu.ResponseApdu;

public class HceFramework
{
	private Map<ByteBuffer, Applet> applets_;
	private Applet activeApplet_;
	private volatile AppletThread appletThread_;
	
	public HceFramework()
	{
		applets_ = new HashMap<ByteBuffer, Applet>();
		activeApplet_ = null;
		appletThread_ = new AppletThread();
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
		if (applets_.containsKey(ByteBuffer.wrap(applet.getAid())))
			return true;
		return (applets_.put(ByteBuffer.wrap(applet.getAid()), applet) == null);
	}
	
	/**
	 * Handles a new terminal
	 * 
	 * @param TagWrapper tag
	 * @return boolean
	 */
	public boolean handleTag(TagWrapper tag)
	{
		try
		{
			// Get the first APDU from the tag
			Apdu apdu = AppletThread.getApdu(tag); 

			// Keep trying
			do
			{
				CommandApdu commandApdu = new CommandApdu(apdu);
				
				// SELECT
				if (commandApdu.cla == Iso7816.CLA_ISO7816 && commandApdu.ins == Iso7816.INS_SELECT)
				{
					// Check P1 and P2
					if (commandApdu.p1 != Iso7816.P1_DF || commandApdu.p2 != Iso7816.P2_SELECT)
					{
						apdu = AppletThread.sendApdu(tag, new ResponseApdu(Iso7816.SW_INCORRECT_P1P2));
						continue;
					}
					
					// We have an applet
					if (applets_.containsKey(ByteBuffer.wrap(commandApdu.getData())))
					{
						Util.d("FW", "Found an applet");
						
						// If we have an active applet deselect it
						if (activeApplet_ != null)
							activeApplet_.deselect();
											
						// Set the applet to active and select it
						activeApplet_ = applets_.get(ByteBuffer.wrap(commandApdu.getData()));
						activeApplet_.select();
						
						// Send an OK and start the applet
						Apdu response = AppletThread.sendApdu(tag, new ResponseApdu(Iso7816.SW_NO_ERROR));
						
						// Stop current applet thread and wait just a bit
						appletThread_.stop();
						Thread.sleep(250);
						
						// Set the applet to the active runnable
						appletThread_.setApplet(activeApplet_, tag);
						appletThread_.setApdu(response);
						
						// Run it
						Thread thread= new Thread(appletThread_);
						thread.setName("JavaCard");
						thread.start();
						
						// Stop trying
						return true;
					}
				}
				
				apdu = AppletThread.sendApdu(tag, new ResponseApdu(Iso7816.SW_INS_NOT_SUPPORTED));
			}
			while(true);
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
