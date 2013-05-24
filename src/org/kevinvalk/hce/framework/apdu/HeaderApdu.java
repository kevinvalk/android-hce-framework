package org.kevinvalk.hce.framework.apdu;

import struct.StructClass;
import struct.StructField;

@StructClass
public class HeaderApdu
{
	@StructField(order = 0)
	public byte cla;
	
	@StructField(order = 1)
	public byte ins;
	
	@StructField(order = 2)
	public byte p1;
	
	@StructField(order = 3)
	public byte p2;
}
