package org.kevinvalk.hce.framework;

import org.kevinvalk.hce.framework.apdu.HeaderApdu;

import struct.JavaStruct;
import struct.StructException;


public class Apdu
{
	public HeaderApdu header;
	public byte[] buffer;
	
	public Apdu(byte[] buffer)
	{
		this.buffer = buffer;
		try
		{
			JavaStruct.unpack(header, this.buffer);
		}
		catch (StructException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	public Apdu(short state)
	{
		this.buffer = toBytes(state);	
	}
	
	public Apdu(byte[] buffer, short state)
	{
		
		
	}

	public byte[] getBuffer()
	{
		return buffer;
	}
	
	public void getAs(HeaderApdu typeApdu)
	{
		try
		{
			JavaStruct.unpack(typeApdu, getBuffer());
		}
		catch (StructException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
    public static byte[] toBytes(short s)
    {
        return new byte[] { (byte) ((s & 0xff00) >> 8), (byte) (s & 0xff) };
    }
}
