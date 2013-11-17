package org.ne81.sp.cmpp;

import java.util.Date;

public abstract class CmppMessageHeader implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5459802431134753740L;

	/** 消息总长度(含消息头及消息体) */
	protected int totalLength;

	/** 命令标识 */
	protected int commandId;

	/** 消息流水号，顺序累加，步长为1，循环使用（一对请求和应答消息的流水号必须相同） */
	protected int sequenceId;

	/** 读取消息头是否成功 */
	boolean readHeader = true;

	/** 读取消息体是否成功 */
	boolean wroteHeader;
	protected String id;
	protected Date dt = new Date();
	protected byte version;

	protected CmppMessageHeader(int commandId, int bodyLen) {
		this.commandId = commandId;
		this.totalLength = bodyLen + Constants.MESSAGE_HEADER_LEN;
		if (!((commandId & 0x80000000) == 0x80000000))
			sequenceId = Sequence.getInstance().getSequence();
		this.version = Constants.CMPP2_VERSION;
	}

	protected CmppMessageHeader(int commandId, byte version) {
		this.commandId = commandId;
		if (!((commandId & 0x80000000) == 0x80000000))
			sequenceId = Sequence.getInstance().getSequence();
		this.version = version;
	}

	protected CmppMessageHeader(int commandId, byte version, int bodyLen) {
		this.commandId = commandId;
		this.totalLength = bodyLen + Constants.MESSAGE_HEADER_LEN;
		if (!((commandId & 0x80000000) == 0x80000000))
			sequenceId = Sequence.getInstance().getSequence();
		this.version = version;
	}

	private boolean readHeader(java.nio.ByteBuffer buf) {
		// if header is not fully read, don't read it.
		if (buf.remaining() < Constants.MESSAGE_HEADER_LEN)
			return false;
		// read header and validate the message
		totalLength = buf.getInt();
		commandId = buf.getInt();
		sequenceId = buf.getInt();
		return true;
	}

	protected abstract boolean readBody(java.nio.ByteBuffer buf);

	private boolean writeHeader(java.nio.ByteBuffer buf) {
		// check if there is enough space to write header
		if (buf.remaining() < Constants.MESSAGE_HEADER_LEN)
			return false;
		buf.putInt(totalLength);
		buf.putInt(commandId);
		buf.putInt(sequenceId);
		return true;
	}

	protected abstract boolean writeBody(java.nio.ByteBuffer buf);

	public boolean readFromBuffer(java.nio.ByteBuffer buf) {
		// read a header if not read yet.
		// buf.rewind();
		if (!readHeader) {
			readHeader = readHeader(buf);
			if (!readHeader)
				return false;
		}
		// Header is read, now try to read body
		if (readBody(buf)) {
			// finished reading single complete message
			readHeader = false; // reset state
			return true;
		} else
			return false;
	}

	public java.nio.ByteBuffer toBuffer() {
		java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(totalLength);
		// write a header if not written yet.
		if (!wroteHeader) {
			wroteHeader = writeHeader(buf);
			if (!wroteHeader)
				return null; // buffer is almost full perhaps
		}
		// Header is written, now try to write body
		if (writeBody(buf)) {
			// finished writing single complete message
			wroteHeader = false;
			buf.flip();
			return buf;
		} else {
			return null;
		}
	}

	public String toString() {
		return "TotalLength=" + totalLength + " CommandId=" + Integer.toHexString(commandId)
				+ " SequenceId=" + sequenceId + "\t";
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}
