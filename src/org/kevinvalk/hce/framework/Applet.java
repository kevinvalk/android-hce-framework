package org.kevinvalk.hce.framework;

public abstract class Applet implements Runnable 
{
	public abstract boolean handleApdu(Apdu apdu);
	
	public abstract byte[] getAid();
	
}
