package org.ne81.sp.cmpp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class CmppDecoder implements ProtocolDecoder {
	ByteBuffer buf;
	ByteBuffer headerBuf = ByteBuffer.allocate(8);
	boolean ready = false;
	byte version = (byte) 32;

	public CmppDecoder(byte version) {
		this.version = version;
	}

	public CmppDecoder() {
		this.version = (byte) 32;
	}

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (!ready)
			if (in.remaining() < headerBuf.remaining()) {
				byte bytes[] = new byte[in.remaining()];
				in.get(bytes);
				headerBuf.put(bytes);
			} else if (headerBuf.remaining() > 0) {
				byte bytes[] = new byte[headerBuf.remaining()];
				in.get(bytes);
				headerBuf.put(bytes);
				headerBuf.flip();
				ready = true;
			}
		if (ready)
			if (buf == null) {
				int totalLength = headerBuf.getInt();
				int commandId = headerBuf.getInt();
				if (totalLength <= 3511 && totalLength >= Constants.MESSAGE_HEADER_LEN
						&& getFromCommandId(commandId) != null) {
					buf = ByteBuffer.allocate(totalLength);
					buf.putInt(totalLength);
					buf.putInt(commandId);
				} else {
					String hexdump = String.format("%040x", new BigInteger(headerBuf.array()));
					headerBuf.clear();
					headerBuf.putInt(commandId);
					ready = false;
					throw new Exception("totalLength error!" + " ERROR hexdump:" + hexdump);
				}
			} else if (in.remaining() >= buf.remaining()) {
				byte bytes[] = new byte[buf.remaining()];
				in.get(bytes);
				buf.put(bytes);
				buf.flip();
				CmppMessageHeader message = decode(buf);
				if (message != null) {
					out.write(message);
				} else {
					buf.clear();
					buf = null;
					headerBuf.clear();
					ready = false;
					throw new Exception("message decode error!" + " ERROR hexdump:"
							+ String.format("%040x", new BigInteger(buf.array())));
				}
				buf.clear();
				buf = null;
				headerBuf.clear();
				ready = false;
			} else {
				byte bytes[] = new byte[in.remaining()];
				in.get(bytes);
				buf.put(bytes);
			}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

	}

	private CmppMessageHeader decode(ByteBuffer buf) {
		int totalLength = buf.getInt();
		int commandId = buf.getInt();
		int sequenceId = buf.getInt();

		CmppMessageHeader message = getFromCommandId(commandId);
		if (message == null) {
		} else {
			message.setSequenceId(sequenceId);
			message.setTotalLength(totalLength);
			if (message.readFromBuffer(buf)) {
				if (commandId == Constants.CMPP_CONNECT)
					version = ((CmppConnect) message).getVersion();
				return message;
			}
		}
		return null;
	}

	public CmppMessageHeader getFromCommandId(int commandId) {
		switch (commandId) {
		case Constants.CMPP_CONNECT:
			return new CmppConnect();
		case Constants.CMPP_CONNECT_RESP:
			return new CmppConnectResp(version);
		case Constants.CMPP_DELIVER:
			return new CmppDeliver(version);
		case Constants.CMPP_DELIVER_RESP:
			return new CmppDeliverResp(version);
		case Constants.CMPP_SUBMIT:
			return new CmppSubmit(version);
		case Constants.CMPP_SUBMIT_RESP:
			return new CmppSubmitResp(version);
		case Constants.CMPP_ACTIVE_TEST_RESP:
			return new CmppActiveTestResp();
		case Constants.CMPP_ACTIVE_TEST:
			return new CmppActiveTest();
		case Constants.CMPP_TERMINATE:
			return new CmppTerminate();
		case Constants.CMPP_TERMINATE_RESP:
			return new CmppTerminateResp();
		default:
			return null;
		}
	}
}
