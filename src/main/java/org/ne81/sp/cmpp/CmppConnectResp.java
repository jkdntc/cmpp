package org.ne81.sp.cmpp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class CmppConnectResp extends CmppMessageHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 247528552740058684L;

	int status;

	byte[] authenticatorISMG;

	byte version;

	int connectRespLen;

	protected CmppConnectResp(byte version) {
		super(Constants.CMPP_CONNECT_RESP, version);

		if (super.version == Constants.CMPP2_VERSION) {
			connectRespLen = Constants.CMPP_CONNECT_RESP_LEN;
		} else {
			connectRespLen = Constants.CMPP3_CONNECT_RESP_LEN;
		}
		totalLength = Constants.MESSAGE_HEADER_LEN + connectRespLen;
	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < connectRespLen) {
			return false;
		}
		if (super.version == Constants.CMPP2_VERSION)
			status = buf.get();
		else
			status = buf.getInt();
		authenticatorISMG = new byte[16];
		buf.get(authenticatorISMG);
		version = buf.get();
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < connectRespLen) {
			return false;
		}
		if (super.version == Constants.CMPP2_VERSION)
			buf.put((byte) status);
		else
			buf.putInt(status);
		buf.put(authenticatorISMG);
		buf.put(version);
		return true;
	}

	public String toString() {
		return super.toString() + "Status=" + status + "\tauthenticatorISMG="
				+ String.format("%040x", new BigInteger(authenticatorISMG)) + "\tVersion="
				+ version;
	}

	public byte[] getAuthenticatorISMG() {
		return authenticatorISMG;
	}

	public void setAuthenticatorISMG(byte[] authenticatorISMG) {
		this.authenticatorISMG = authenticatorISMG;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}
}
