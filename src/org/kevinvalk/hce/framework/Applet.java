package org.kevinvalk.hce.framework;

import java.io.IOException;

public abstract class Applet implements Runnable 
{
	protected TagWrapper tag;
	protected boolean isRunning;
	protected Apdu apdu;
	public abstract Apdu handleApdu(Apdu apdu);
	
	public abstract String getName();
	public abstract byte[] getAid();
	
	public Apdu sendApdu(Apdu apdu)
	{
		return Applet.sendApdu(tag, apdu);
	}
	
	public Apdu getApdu()
	{
		return Applet.getApdu(tag);
	}
	
	/**
	 * Sends an apdu to the tag and receives the answer
	 * 
	 * @param tag
	 * @param Apdu The apdu to send
	 * @return Apdu response
	 */
	public static Apdu sendApdu(TagWrapper tag, Apdu apdu)
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
	public static Apdu getApdu(TagWrapper tag)
	{
		return sendApdu(tag, new Apdu(new byte[0]));
	}
}
