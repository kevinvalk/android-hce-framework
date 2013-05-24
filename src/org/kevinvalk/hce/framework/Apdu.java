package org.kevinvalk.hce.framework;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
			if(this.buffer != null && this.buffer.length != 0)
			{
				header = new HeaderApdu();
				JavaStruct.unpack(header, this.buffer);
			}
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
	
	public int getLength()
	{
		return getLength(1);
	}
	
	/**
	 * Returns the LC parameter from the apdu 
	 * 
	 * The LC field can be 1, 2 or 3 bytes large so use argument for that
	 * 
	 * @param size
	 * @return
	 */
	public int getLength(int size)
	{
		int length = 0;
		int l = (getBuffer().length < Iso7816.OFFSET_LC + size) ? (Iso7816.OFFSET_LC + size) - getBuffer().length : size;
		for (int i = 0; i < l; i++)
		{
			length <<= 8; // Shift left
			length |= getBuffer()[Iso7816.OFFSET_LC+i] & 0xFF; // Grab one byte and OR it
		}
		return length;
	}
	
	public byte[] getData()
	{
		return Arrays.copyOfRange(getBuffer(), Iso7816.OFFSET_CDATA, getBuffer().length);
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
