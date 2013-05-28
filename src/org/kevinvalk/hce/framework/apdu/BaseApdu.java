package org.kevinvalk.hce.framework.apdu;

import javax.crypto.SecretKey;

import org.kevinvalk.hce.framework.Apdu;

public abstract class BaseApdu
{
	protected boolean isVerified;
	protected SecretKey encKey;
	protected SecretKey macKey;
	
	public abstract Apdu toApdu();
	public abstract int expectedLc();
	public abstract boolean isVerified();

}
