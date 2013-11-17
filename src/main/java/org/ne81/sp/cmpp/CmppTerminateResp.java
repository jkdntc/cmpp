package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppTerminateResp extends CmppMessageHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7988764756379468015L;

	protected CmppTerminateResp() {
		super(Constants.CMPP_TERMINATE_RESP, 0);
	}

	protected boolean readBody(ByteBuffer buf) {
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		return true;
	}

}
