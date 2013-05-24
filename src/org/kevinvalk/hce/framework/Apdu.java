package org.kevinvalk.hce.framework;

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
	
	public int getLc()
	{
		return getLc(1);
	}
	
	/**
	 * Returns the LC parameter from the apdu 
	 * 
	 * The LC field can be 1, 2 or 3 bytes large so use argument for that
	 * 
	 * @param size
	 * @return
	 */
	public int getLc(int length)
	{
		return (int) getSomething(Iso7816.OFFSET_LC, length);
	}
	
	public short getShort(int offset)
	{
		return (short) getSomething(offset, 2);
	}
	
	public int getInt(int offset)
	{
		return (int) getSomething(offset, 4);
	}
	
	public long getLong(int offset)
	{
		return getSomething(offset, 8);
	}
	
	/**
	 * Convert bytes to a number with a specefic byte length
	 * 
	 * Internal only
	 * 
	 * @param offset
	 * @param length
	 * @return
	 */
	private long getSomething(int offset, int length)
	{
		int value = 0;
		for (int i = 0; i < length && getBuffer().length > Iso7816.OFFSET_LC + i; i++)
		{
			value <<= 8; // Shift left
			value |= getBuffer()[offset+i] & 0xFF; // Grab one byte and OR it
		}
		return value;
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
