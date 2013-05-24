package org.kevinvalk.hce.framework;

import java.io.IOException;

import android.util.Log;

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
	
	/*** BEGIN DEBUG FUNCTIONS ***/
	public void d(String msg)
	{
		d(getName(), msg);
	}
	
	public void d(String format, Object... args)
	{
		d(getName(), format, args);
	}
	
	public static void d(String tag, String msg)
	{
		Log.i(tag, msg);
	}
	
	public static void d(String tag, String format, Object... args)
	{
		Log.i(tag, String.format(format, (Object[])args));
	}
	
	public static String toHex(byte[] buffer)
	{
		return toHex(buffer, 0, buffer.length);
	}
	
	public static String toSHex(byte[] buffer)
	{
		return toHex(buffer).replace(" ", "");
	}
		
	public static String toHex(byte[] buffer, int offset, int length)
	{
		String hex = "";
		for(int i = offset; i < offset+length; i++)
			hex = hex.concat(String.format("%02X ", buffer[i]));		
		return hex.trim();
	}
	/*** END DEBUG FUNCTIONS ***/
}
