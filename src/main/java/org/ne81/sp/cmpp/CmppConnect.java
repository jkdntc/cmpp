package org.ne81.sp.cmpp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class CmppConnect extends CmppMessageHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1284195798671119022L;

	String sourceAddr;

	byte[] authenticatorSource;

	byte version;

	int timestamp;

	public String getSourceAddr() {
		return sourceAddr;
	}

	public void setSourceAddr(String sourceAddr) {
		this.sourceAddr = sourceAddr;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public CmppConnect() {
		super(Constants.CMPP_CONNECT, Constants.CMPP_CONNECT_LEN);
	}

	public boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_CONNECT_LEN) {
			return false;
		}
		byte sourceAddrBytes[] = new byte[6];
		buf.get(sourceAddrBytes);
		sourceAddr = new String(sourceAddrBytes);
		authenticatorSource = new byte[16];
		buf.get(authenticatorSource);
		version = buf.get();
		timestamp = buf.getInt();
		return true;
	}

	public boolean writeBody(ByteBuffer buf) {

		if (buf.remaining() < Constants.CMPP_CONNECT_LEN) {
			return false;
		}
		buf.put(CmppUtil.getLenBytes(sourceAddr, 6));
		buf.put(authenticatorSource);
		buf.put(version);
		buf.putInt(timestamp);
		return true;

	}

	public byte[] getAuthenticatorSource() {
		return authenticatorSource;
	}

	public void setAuthenticatorSource(byte[] authenticatorSource) {
		this.authenticatorSource = authenticatorSource;
	}

	public String toString() {
		return super.toString() + "sourceAddr=" + sourceAddr + "\t" + "authenticatorSource="
				+ String.format("%040x", new BigInteger(authenticatorSource)) + "\t" + "version="
				+ version + "\ttimestamp=" + timestamp;
	}
}
