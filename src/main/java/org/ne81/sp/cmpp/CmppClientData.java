package org.ne81.sp.cmpp;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;

public class CmppClientData {
	String spId;
	String sharedSecret;
	String spNumber;
	List<IoSession> session;
	HashMap<Long, Long> submitMsgId;

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getSharedSecret() {
		return sharedSecret;
	}

	public void setSharedSecret(String sharedSecret) {
		this.sharedSecret = sharedSecret;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public List<IoSession> getSession() {
		return session;
	}

	public void setSession(List<IoSession> session) {
		this.session = session;
	}

	public HashMap<Long, Long> getSubmitMsgId() {
		return submitMsgId;
	}

	public void setSubmitMsgId(HashMap<Long, Long> submitMsgId) {
		this.submitMsgId = submitMsgId;
	}

}
