package org.kevinvalk.hce.framework;

import android.util.Log;

/**
 * An utility class
 *
 */
public final class Util
{
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
	
	public static String toUnspacedHex(byte[] buffer)
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
}
