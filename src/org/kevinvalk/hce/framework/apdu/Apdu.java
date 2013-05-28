package org.kevinvalk.hce.framework.apdu;

public class Apdu
{
	private byte[] apdu;
	
	public Apdu()
	{
		
	}
	
	public Apdu(byte[] apdu)
	{
		this.apdu = apdu;
	}
	
	/**
	 * Get the raw APDU buffer (everything)
	 * 
	 * @return
	 */
	public byte[] getBuffer()
	{
		return apdu;
	}
 
	/**
	 * Convert bytes to a number with a specific byte length
	 * 
	 * Internal only
	 * 
	 * @param offset
	 * @param length
	 * @return
	 */
	protected long getSomething(byte[] buffer, int offset, int length)
	{
		int value = 0;
		for (int i = 0; i < length && buffer.length > i; i++)
		{
			value <<= 8; // Shift left
			value |= buffer[offset+i] & 0xFF; // Grab one byte and OR it
		}
		return value;
	}
}
