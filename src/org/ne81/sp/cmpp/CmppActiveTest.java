package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

public class CmppActiveTest extends CmppMessageHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = -730272219364414335L;

	protected CmppActiveTest() {
		super(Constants.CMPP_ACTIVE_TEST, 0);
	}

	protected boolean readBody(ByteBuffer buf) {
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		return true;
	}

}
