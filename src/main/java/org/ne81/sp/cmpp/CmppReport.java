package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CmppReport implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136584259645665720L;
	String id;
	Date dt;
	public long msgId;

	public String stat;

	public String submitTime;

	public String doneTime;

	public String destTerminalId;

	public int smscSequence;

	public CmppReport() {
		dt = new Date();
	}

	public CmppReport(long msgId, String stat, String submitTime, String doneTime,
			String destTerminalId, int smscSequence) {
		this.msgId = msgId;
		this.stat = stat;
		this.submitTime = submitTime;
		this.doneTime = doneTime;
		this.destTerminalId = destTerminalId;
		this.smscSequence = smscSequence;
		dt = new Date();
	}

	public boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_REPORT_LEN) {
			return false;
		}
		msgId = buf.getLong();

		byte[] rStatBytes = new byte[7];
		buf.get(rStatBytes);
		stat = CmppUtil.esc0(new String(rStatBytes));

		byte[] rSubmitTimeBytes = new byte[10];
		buf.get(rSubmitTimeBytes);
		submitTime = CmppUtil.esc0(new String(rSubmitTimeBytes));

		byte[] rDoneTimeBytes = new byte[10];
		buf.get(rDoneTimeBytes);
		doneTime = CmppUtil.esc0(new String(rDoneTimeBytes));

		byte[] rDestTerminalIdBytes = new byte[21];
		buf.get(rDestTerminalIdBytes);
		destTerminalId = CmppUtil.esc0(new String(rDestTerminalIdBytes));
		smscSequence = buf.getInt();
		return true;
	}

	public boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < Constants.CMPP_REPORT_LEN) {
			return false;
		}
		buf.putLong(msgId);
		buf.put(CmppUtil.getLenBytes(stat, 7));
		buf.put(CmppUtil.getLenBytes(submitTime, 10));
		buf.put(CmppUtil.getLenBytes(doneTime, 10));
		buf.put(CmppUtil.getLenBytes(destTerminalId, 21));
		buf.putInt(smscSequence);
		return true;
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

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String destTerminalId) {
		this.destTerminalId = destTerminalId;
	}

	public int getSmscSequence() {
		return smscSequence;
	}

	public void setSmscSequence(int smscSequence) {
		this.smscSequence = smscSequence;
	}

	public String toString() {
		return id + "\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt) + "\t" + msgId
				+ "\t" + stat + "\t" + submitTime + "\t" + doneTime + "\t" + destTerminalId + "\t"
				+ smscSequence;
	}
}
