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
}
