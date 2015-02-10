package org.ne81.sp.cmpp;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class CmppCumulativeDecoder extends CumulativeProtocolDecoder {
	// ByteBuffer buf;
	// ByteBuffer headerBuf = ByteBuffer.allocate(8);
	// boolean ready = false;
	byte version = (byte) 32;

	public CmppCumulativeDecoder(byte version) {
		this.version = version;
	}

	public CmppCumulativeDecoder() {
		this.version = (byte) 32;
	}

	// @Override
	// public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput
	// out) throws Exception {
	// if (!ready)
	// if (in.remaining() < headerBuf.remaining()) {
	// byte bytes[] = new byte[in.remaining()];
	// in.get(bytes);
	// headerBuf.put(bytes);
	// } else if (in.remaining() >= headerBuf.remaining()) {
	// byte bytes[] = new byte[headerBuf.remaining()];
	// in.get(bytes);
	// headerBuf.put(bytes);
	// headerBuf.flip();
	// ready = true;
	// }
	// if (ready)
	// if (buf == null) {
	// int totalLength = headerBuf.getInt();
	// int commandId = headerBuf.getInt();
	// if (totalLength <= 3511 && totalLength >= Constants.MESSAGE_HEADER_LEN
	// && isRightCommandId(commandId)) {
	// buf = ByteBuffer.allocate(totalLength);
	// buf.putInt(totalLength);
	// buf.putInt(commandId);
	// } else {
	// String hexdump = String.format("%040x", new
	// BigInteger(headerBuf.array()))
	// + String.format("%040x", new BigInteger(in.array()));
	// headerBuf.clear();
	// ready = false;
	// session.close(true);
	// throw new Exception("包头错误：" + hexdump);
	// }
	// } else if (in.remaining() >= buf.remaining()) {
	// byte bytes[] = new byte[buf.remaining()];
	// in.get(bytes);
	// buf.put(bytes);
	// buf.flip();
	// CmppMessageHeader message = decode(buf);
	// if (message != null) {
	// out.write(message);
	// } else {
	// String hexdump = String.format("%040x", new BigInteger(buf.array()))
	// + String.format("%040x", new BigInteger(in.array()));
	// buf.clear();
	// buf = null;
	// headerBuf.clear();
	// ready = false;
	// session.close(true);
	// throw new Exception("数据包错误：" + hexdump);
	// }
	// buf.clear();
	// buf = null;
	// headerBuf.clear();
	// ready = false;
	// } else {
	// byte bytes[] = new byte[in.remaining()];
	// in.get(bytes);
	// buf.put(bytes);
	// }
	// }

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

	public boolean isRightCommandId(int commandId) {
		switch (commandId) {
		case Constants.CMPP_CONNECT:
		case Constants.CMPP_CONNECT_RESP:
		case Constants.CMPP_DELIVER:
		case Constants.CMPP_DELIVER_RESP:
		case Constants.CMPP_SUBMIT:
		case Constants.CMPP_SUBMIT_RESP:
		case Constants.CMPP_ACTIVE_TEST_RESP:
		case Constants.CMPP_ACTIVE_TEST:
		case Constants.CMPP_TERMINATE:
		case Constants.CMPP_TERMINATE_RESP:
			return true;
		default:
			return false;
		}
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

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		if (in.remaining() > 8) {
			// in.mark();
			int totalLength = in.getInt();
			int commandId = in.getInt();
			if (totalLength > 3511 || totalLength < Constants.MESSAGE_HEADER_LEN
					|| (!isRightCommandId(commandId))) {
				String hexdump = String.format("%040x", new BigInteger(in.array()));
				session.close(true);
				throw new Exception("包头错误：" + hexdump);
			}
			if (in.remaining() + 8 < totalLength) {
				in.rewind();
				return false;
			}
			ByteBuffer buf = ByteBuffer.allocate(totalLength);
			buf.putInt(totalLength);
			buf.putInt(commandId);
			byte[] tempBytes = new byte[totalLength - 8];
			in.get(tempBytes);
			buf.put(tempBytes);
			buf.flip();
			CmppMessageHeader message = decode(buf);
			if (message == null) {
				in.rewind();
				String hexdump = String.format("%040x", new BigInteger(in.array()));
				buf.clear();
				buf = null;
				session.close(true);
				throw new Exception("数据包错误：" + hexdump);
			}
			out.write(message);
			return true;
		}
		return false;
	}
}
