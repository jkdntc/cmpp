package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppTerminate extends CmppMessageHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3295552348468632981L;

	protected CmppTerminate() {
		super(Constants.CMPP_TERMINATE, 0);
	}

	protected boolean readBody(ByteBuffer buf) {
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		return true;
	}
}
