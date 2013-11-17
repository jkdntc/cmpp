package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class CmppEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		ByteBuffer buf = ((CmppMessageHeader) message).toBuffer();
		if (buf == null)
			throw new Exception("toBuffer false Message=" + message.toString());
		IoBuffer ibuf = IoBuffer.wrap(buf);
		out.write(ibuf);
	}

}
