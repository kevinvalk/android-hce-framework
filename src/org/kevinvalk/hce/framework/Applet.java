package org.kevinvalk.hce.framework;

import org.kevinvalk.hce.framework.apdu.CommandApdu;
import org.kevinvalk.hce.framework.apdu.ResponseApdu;


public abstract class Applet
{
	/**
	 * Processes an incoming command APDU
	 * 
	 * @param CommandApdu incoming APDU
	 * @return ResponseApdu outgoing APDU
	 */
	public abstract ResponseApdu process(CommandApdu apdu);
	
	
	/**
	 * To let you know you have been selected by a terminal
	 */
	public abstract void select();
	
	/**
	 * Get the applets name
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Get the application identifier for this applet
	 * @return
	 */
	public abstract byte[] getAid();
	
	/*** BEGIN DEBUG FUNCTIONS ***/
	public void d(String msg)
	{
		Util.d(getName(), msg);
	}
	
	public void d(String format, Object... args)
	{
		Util.d(getName(), format, args);
	}
	/*** END DEBUG FUNCTIONS ***/
}
