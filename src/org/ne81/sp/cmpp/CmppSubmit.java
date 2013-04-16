package org.ne81.sp.cmpp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

public class CmppSubmit extends CmppMessageHeader implements java.lang.Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5848975210940175154L;

	long msgId = 0;

	byte pkTotal = 0;

	byte pkNumber = 0;

	byte registeredDelivery = 1;

	byte msgLevel = 0;

	String serviceId;// 10

	byte feeUserType = 3;

	String feeTerminalId;// 32

	byte feeTerminalType = 0;

	byte tp_pid = 0;

	byte tp_udhi = 0;

	byte msgFmt = 15;

	String msgSrc;// 6

	String feeType;// 2

	String feeCode;// 6

	String vaildTime = "";// 17

	String atTime = "";// 17

	String srcId;// 21

	byte destUsrTl = 1;

	String[] destTerminalId = new String[1];// 21

	byte destTerminalType = 0;

	byte msgLength; // 1

	byte[] msgContent = new byte[1];

	String linkId = "";// 20

	int result;
	private int terminalIdLen;
	private int linkIdLen;
	private int submitExpMsgLen;

	public String toString() {
		String msgContent = CmppUtil.getMessageContent(this.msgContent, msgFmt);
		String destTerminalId = "";
		for (int i = 0; i < this.destTerminalId.length; i++) {
			destTerminalId += this.destTerminalId[i] + ",";
		}

		return id + "\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt) + "\t" + msgId
				+ "\t" + pkTotal + "\t" + pkNumber + "\t" + registeredDelivery + "\t" + msgLevel
				+ "\t" + serviceId + "\t" + feeUserType + "\t" + feeTerminalId + "\t"
				+ feeTerminalType + "\t" + tp_pid + "\t" + tp_udhi + "\t" + msgFmt + "\t" + msgSrc
				+ "\t" + feeType + "\t" + feeCode + "\t" + vaildTime + "\t" + atTime + "\t" + srcId
				+ "\t" + destUsrTl + "\t" + destTerminalId + "\t" + destTerminalType + "\t"
				+ this.msgContent.length + "\t" + msgContent.replaceAll("[\r\n\0\t\'\"]", "")
				+ "\t" + linkId + "\t" + result;
	}

	public CmppSubmit(byte version) {
		super(Constants.CMPP_SUBMIT, version);
		if (version == Constants.CMPP2_VERSION) {
			terminalIdLen = 21;
			linkIdLen = 8;
			submitExpMsgLen = Constants.CMPP_SUBMIT_LEN_EXPMSGLEN;
		} else {
			terminalIdLen = 32;
			linkIdLen = 20;
			submitExpMsgLen = Constants.CMPP3_SUBMIT_LEN_EXPMSGLEN;
		}
	}

	public CmppSubmit(byte version, String serviceId, String feeTerminalId, String msgSrc,
			String feeType, String feeCode, String srcId, String destTerminalId, String msgContent,
			String linkId) {
		super(Constants.CMPP_SUBMIT, version);
		if (version == Constants.CMPP2_VERSION) {
			terminalIdLen = 21;
			linkIdLen = 8;
			submitExpMsgLen = Constants.CMPP_SUBMIT_LEN_EXPMSGLEN;
		} else {
			terminalIdLen = 32;
			linkIdLen = 20;
			submitExpMsgLen = Constants.CMPP3_SUBMIT_LEN_EXPMSGLEN;
		}
		// msgLength = (byte) msgContent.getBytes().length;
		this.serviceId = serviceId;
		this.feeTerminalId = feeTerminalId;
		this.msgSrc = msgSrc;
		this.feeType = feeType;
		this.feeCode = feeCode;
		this.srcId = srcId;
		this.setDestTerminalId(new String[] { destTerminalId });
		try {
			this.setMsgContent(msgContent.getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			// 不回出错
			e.printStackTrace();
		}
		this.linkId = linkId;

	}

	protected boolean readBody(ByteBuffer buf) {
		if (buf.remaining() < this.totalLength - Constants.MESSAGE_HEADER_LEN)
			return false;
		msgId = buf.getLong();
		pkTotal = buf.get();

		pkNumber = buf.get();

		registeredDelivery = buf.get();

		msgLevel = buf.get();

		serviceId = CmppUtil.getStringFromBuffer(buf, 10);

		feeUserType = buf.get();

		feeTerminalId = CmppUtil.getStringFromBuffer(buf, terminalIdLen);
		if (version == Constants.CMPP3_VERSION)
			feeTerminalType = buf.get();

		tp_pid = buf.get();

		tp_udhi = buf.get();

		msgFmt = buf.get();

		msgSrc = CmppUtil.getStringFromBuffer(buf, 6);

		feeType = CmppUtil.getStringFromBuffer(buf, 2);

		feeCode = CmppUtil.getStringFromBuffer(buf, 6);

		vaildTime = CmppUtil.getStringFromBuffer(buf, 17);

		atTime = CmppUtil.getStringFromBuffer(buf, 17);

		srcId = CmppUtil.getStringFromBuffer(buf, 21);

		destUsrTl = buf.get();
		destTerminalId = new String[destUsrTl];
		for (int i = 0; i < destUsrTl; i++) {
			destTerminalId[i] = CmppUtil.getStringFromBuffer(buf, terminalIdLen);
		}
		if (version == Constants.CMPP3_VERSION)
			destTerminalType = buf.get();

		msgLength = buf.get();
		msgContent = new byte[msgLength & 0xFF];
		buf.get(msgContent);
		linkId = CmppUtil.getStringFromBuffer(buf, linkIdLen);
		return true;
	}

	protected boolean writeBody(ByteBuffer buf) {
		if (buf.remaining() < submitExpMsgLen + msgContent.length + destTerminalId.length
				* terminalIdLen) {
			return false;
		}
		buf.putLong(0);// 协议要求填空
		buf.put(pkTotal);
		buf.put(pkNumber);
		buf.put(registeredDelivery);
		buf.put(msgLevel);
		buf.put(CmppUtil.getLenBytes(serviceId, 10));
		buf.put(feeUserType);
		buf.put(CmppUtil.getLenBytes(feeTerminalId, terminalIdLen));
		if (version == Constants.CMPP3_VERSION)
			buf.put(feeTerminalType);
		buf.put(tp_pid);
		buf.put(tp_udhi);
		buf.put(msgFmt);

		buf.put(CmppUtil.getLenBytes(msgSrc, 6));
		buf.put(CmppUtil.getLenBytes(feeType, 2));
		buf.put(CmppUtil.getLenBytes(feeCode, 6));
		buf.put(CmppUtil.getLenBytes(vaildTime, 17));
		buf.put(CmppUtil.getLenBytes(atTime, 17));
		buf.put(CmppUtil.getLenBytes(srcId, 21));

		buf.put((byte) destTerminalId.length);

		for (int i = 0; i < destTerminalId.length; i++) {
			buf.put(CmppUtil.getLenBytes(destTerminalId[i], terminalIdLen));
		}
		if (version == Constants.CMPP3_VERSION)
			buf.put(destTerminalType);
		buf.put((byte) msgContent.length);

		buf.put(msgContent);

		buf.put(CmppUtil.getLenBytes(linkId, linkIdLen));

		return true;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public byte getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}

	public byte getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public byte getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(byte msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getFeeUserType() {
		return feeUserType;
	}

	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}

	public String getFeeTerminalId() {
		return feeTerminalId;
	}

	public void setFeeTerminalId(String feeTerminalId) {
		this.feeTerminalId = feeTerminalId;
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

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getVaildTime() {
		return vaildTime;
	}

	public void setVaildTime(String vaildTime) {
		this.vaildTime = vaildTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public byte getDestUsrTl() {
		return destUsrTl;
	}

	public void setDestUsrTl(byte destUsrTl) {
		this.destUsrTl = destUsrTl;
	}

	public String[] getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String[] destTerminalId) {
		this.destTerminalId = destTerminalId;
		this.totalLength = Constants.MESSAGE_HEADER_LEN + submitExpMsgLen + msgContent.length
				+ destTerminalId.length * terminalIdLen;
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
		this.totalLength = Constants.MESSAGE_HEADER_LEN + submitExpMsgLen + msgContent.length
				+ destTerminalId.length * terminalIdLen;
		this.msgLength = (byte) msgContent.length;
	}

	public byte getFeeTerminalType() {
		return feeTerminalType;
	}

	public void setFeeTerminalType(byte feeTerminalType) {
		this.feeTerminalType = feeTerminalType;
	}

	public byte getDestTerminalType() {
		return destTerminalType;
	}

	public void setDestTerminalType(byte destTerminalType) {
		this.destTerminalType = destTerminalType;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public CmppSubmit clone() throws CloneNotSupportedException {
		CmppSubmit o = null;
		o = (CmppSubmit) super.clone();
		return o;
	}
}
