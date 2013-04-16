package org.ne81.sp.cmpp;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CmppCodecFactory implements ProtocolCodecFactory {
	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;

	public CmppCodecFactory(byte version) {
		encoder = new CmppEncoder();
		decoder = new CmppDecoder(version);
	}

	public CmppCodecFactory() {
		encoder = new CmppEncoder();
		decoder = new CmppDecoder();
	}

	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
		return decoder;
	}
}
