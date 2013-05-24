package org.kevinvalk.hce.framework.apdu;

import org.kevinvalk.hce.framework.Apdu;

import struct.ArrayLengthMarker;
import struct.JavaStruct;
import struct.StructClass;
import struct.StructException;
import struct.StructField;

@StructClass
public class AidApdu
{
	@StructField(order = 0)
	public byte cla;
	
	@StructField(order = 1)
	public byte ins;
	
	@StructField(order = 2)
	public byte p1;
	
	@StructField(order = 3)
	public byte p2;
	
	@StructField(order = 4)
	@ArrayLengthMarker(fieldName = "aid")
	public byte lc;
	
	@StructField(order = 5)
	public byte[] aid;
	
	static public AidApdu fromApdu(Apdu apdu)
	{
		AidApdu aidApdu = new AidApdu();
		try
		{
			JavaStruct.unpack(aidApdu, apdu.getBuffer());
			return aidApdu;
		}
		catch (StructException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
}
