package org.ne81.sp.cmpp;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

public class CmppDeliver extends CmppMessageHeader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224350635639864084L;

	long msgId;

	String destId;// 21

	String serviceId;// 10

	byte tp_pid = 0;

	byte tp_udhi = 0;

	byte msgFmt = 15;

	String srcTerminalId;// 21

	byte srcTerminalType;

	byte registeredDelivery = 0;

	byte msgLength;

	byte[] msgContent;// msgLength

	String linkId = "";// 8

	private CmppReport report;

	private int terminalIdLen;
	private int linkIdLen;
	private int expMsgLen;

	public CmppDeliver(byte version, long msgId, String destId, String serviceId,
			String srcTerminalId, byte[] msgContent, String linkId) {
		super(Constants.CMPP_DELIVER, version);
		if (version == Constants.CMPP2_VERSION) {
			terminalIdLen = 21;
			linkIdLen = 8;
			expMsgLen = Constants.CMPP_DELIVER_LEN_EXPMSGLEN;
		} else {
			terminalIdLen = 32;
			linkIdLen = 20;
			expMsgLen = Constants.CMPP3_DELIVER_LEN_EXPMSGLEN;
		}
		this.msgId = msgId;
		this.destId = destId;
		this.serviceId = serviceId;
		this.srcTerminalId = srcTerminalId;
		this.setMsgContent(msgContent);
		this.linkId = linkId;
	}

	public CmppDeliver(byte version) {
		super(Constants.CMPP_DELIVER, version);
		if (version == Constants.CMPP2_VERSION) {
			terminalIdLen = 21;
			linkIdLen = 8;
			expMsgLen = Constants.CMPP_DELIVER_LEN_EXPMSGLEN;
		} else {
			terminalIdLen = 32;
			linkIdLen = 20;
			expMsgLen = Constants.CMPP3_DELIVER_LEN_EXPMSGLEN;
		}
	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < expMsgLen) {
			return false;
		}

		msgId = buf.getLong();

		destId = CmppUtil.getStringFromBuffer(buf, 21);
		serviceId = CmppUtil.getStringFromBuffer(buf, 10);

		tp_pid = buf.get();
		tp_udhi = buf.get();
		msgFmt = buf.get();

		srcTerminalId = CmppUtil.getStringFromBuffer(buf, terminalIdLen);
		if (version == Constants.CMPP3_VERSION)
			srcTerminalType = buf.get();
		registeredDelivery = buf.get();
		msgLength = buf.get();

		if (registeredDelivery == 0) {
			msgContent = new byte[msgLength & 0xFF];
			buf.get(msgContent);
		} else {
			report = new CmppReport();
			report.readBody(buf);
		}
		if (msgContent == null) {
			msgContent = new byte[msgLength];
		}
		linkId = CmppUtil.getStringFromBuffer(buf, linkIdLen);
		return true;

	}

	protected boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < expMsgLen
				+ (registeredDelivery == 0 ? msgContent.length : Constants.CMPP_REPORT_LEN)) {
			return false;
		}

		buf.putLong(msgId);
		buf.put(CmppUtil.getLenBytes(destId, 21));
		buf.put(CmppUtil.getLenBytes(serviceId, 10));
		buf.put(tp_pid);
		buf.put(tp_udhi);
		buf.put(msgFmt);
		buf.put(CmppUtil.getLenBytes(srcTerminalId, terminalIdLen));
		if (version == Constants.CMPP3_VERSION)
			buf.put(srcTerminalType);
		buf.put(registeredDelivery);
		if (registeredDelivery == 0) {
			buf.put((byte) msgContent.length);
			buf.put(msgContent);
		} else {
			buf.put((byte) Constants.CMPP_REPORT_LEN);
			report.writeBody(buf);
		}
		buf.put(CmppUtil.getLenBytes(linkId, linkIdLen));
		return true;
	}

	public String toString() {
		if (registeredDelivery == 0) {
			String msgContent = CmppUtil.getMessageContent(this.msgContent, msgFmt);
			return id + "\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt) + "\t"
					+ msgId + "\t" + destId + "\t" + serviceId + "\t" + tp_pid + "\t" + tp_udhi
					+ "\t" + msgFmt + "\t" + srcTerminalId + "\t" + srcTerminalType + "\t"
					+ registeredDelivery + "\t" + (msgLength & 0xFF) + "\t"
					+ msgContent.replaceAll("[\r\n\0\t\'\"]", "") + "\t" + linkId;
		} else
			return report.toString();

	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getTp_pid() {
		return tp_pid;
	}

	public void setTp_pid(byte tp_pid) {
		this.tp_pid = tp_pid;
	}

	public byte getTp_udhi() {
		return tp_udhi;
	}

	public void setTp_udhi(byte tp_udhi) {
		this.tp_udhi = tp_udhi;
	}

	public byte getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(byte msgFmt) {
		this.msgFmt = msgFmt;
	}

	public String getSrcTerminalId() {
		return srcTerminalId;
	}

	public void setSrcTerminalId(String srcTerminalId) {
		this.srcTerminalId = srcTerminalId;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public byte getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(byte msgLength) {
		this.msgLength = msgLength;
	}

	public byte[] getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
		this.totalLength = Constants.MESSAGE_HEADER_LEN + expMsgLen + msgContent.length;
		this.msgLength = (byte) msgContent.length;
	}

	public CmppReport getReport() {
		return report;
	}

	public void setReport(CmppReport report) {
		this.report = report;
		this.totalLength = Constants.MESSAGE_HEADER_LEN + expMsgLen + Constants.CMPP_REPORT_LEN;
	}

	public byte getSrcTerminalType() {
		return srcTerminalType;
	}

	public void setSrcTerminalType(byte srcTerminalType) {
		this.srcTerminalType = srcTerminalType;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

}
