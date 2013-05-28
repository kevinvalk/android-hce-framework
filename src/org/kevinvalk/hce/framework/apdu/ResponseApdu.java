package org.kevinvalk.hce.framework.apdu;

import org.kevinvalk.hce.framework.Iso7816;
import org.kevinvalk.hce.framework.IsoException;

import struct.JavaStruct;
import struct.StructClass;
import struct.StructException;
import struct.StructField;

@StructClass
public class ResponseApdu extends Apdu
{
	@StructField(order = 0)
	public byte[] data;
		
	@StructField(order = 1)
	public short sw;

	public ResponseApdu(short status)
	{
		this.data = new byte[0];
		this.sw = status;
	}
	
	public ResponseApdu(byte[] data, short status)
	{
		this.data = data;
		this.sw = status;
	}
	
	public ResponseApdu(int size)
	{
		this.data = new byte[size];
	}
	
	@Override
	public byte[] getBuffer()
	{
		byte[] bytes = null;
		try
		{
			bytes = JavaStruct.pack(this);
		}
		catch (StructException e)
		{
			IsoException.throwIt(Iso7816.SW_INTERNAL_ERROR);
		}
		return bytes;
	}
}
