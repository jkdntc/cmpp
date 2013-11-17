package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppDeliverResp extends CmppMessageHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5782520565427422141L;

	long msgId;

	int result;

	public CmppDeliverResp(byte version) {
		super(Constants.CMPP_DELIVER_RESP, version,
				version == Constants.CMPP2_VERSION ? Constants.CMPP_DELIVER_RESP_LEN
						: Constants.CMPP3_DELIVER_RESP_LEN);
	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_DELIVER_RESP_LEN) {
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
		if (buf.remaining() < Constants.CMPP_DELIVER_RESP_LEN) {
			return false;
		}
		buf.putLong(msgId);
		if (version == Constants.CMPP2_VERSION)
			buf.put((byte) result);
		else
			buf.putInt(result);
		return true;
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

	public String toString() {
		return super.toString() + msgId + "\t" + result;
	}
}
