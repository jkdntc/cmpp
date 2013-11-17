package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppSubmitResp extends CmppMessageHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7468539789869800716L;

	long msgId;

	int result;
	private byte version;

	public CmppSubmitResp(byte version) {
		super(Constants.CMPP_SUBMIT_RESP,
				version == Constants.CMPP2_VERSION ? Constants.CMPP_SUBMIT_RESP_LEN
						: Constants.CMPP3_SUBMIT_RESP_LEN);
		this.version = version;
	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_SUBMIT_RESP_LEN) {
			return false;
		}
		msgId = buf.getLong();
		if (version == Constants.CMPP2_VERSION)
			result = buf.get();
		else
			result = buf.getInt();
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_SUBMIT_RESP_LEN)
			return false;
		buf.putLong(msgId);
		if (version == Constants.CMPP2_VERSION)
			buf.put((byte) result);
		else
			buf.putInt(result);
		return true;
	}

	public String toString() {
		return super.toString() + msgId + "\t" + result;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
