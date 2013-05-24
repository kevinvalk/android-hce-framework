package org.kevinvalk.hce.framework.apdu;

import struct.StructClass;
import struct.StructField;

@StructClass
public class AidApdu extends HeaderApdu
{
	@StructField(order = 4)
	public byte lc;
	
	@StructField(order = 5)
	public byte[] aid = new byte[7];
}
