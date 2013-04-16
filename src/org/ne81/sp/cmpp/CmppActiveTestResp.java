package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppActiveTestResp extends CmppMessageHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1077810810932325672L;
	byte reserved;

	protected CmppActiveTestResp() {
		super(Constants.CMPP_ACTIVE_TEST_RESP, Constants.CMPP_ACTIVE_TEST_RESP_LEN);
	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_ACTIVE_TEST_RESP_LEN) {
			return false;
		}
		reserved = buf.get();
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_ACTIVE_TEST_RESP_LEN) {
			return false;
		}
		buf.put(reserved);
		return true;

	}

	public String toString() {
		return super.toString() + "Reserved=" + reserved;
	}

}
