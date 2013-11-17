package org.ne81.sp.cmpp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageDecoder {
	public static CmppSubmit submitDecode(String str) throws Exception {
		String field[] = loadConvert(str);
		if (field.length < 27)
			throw new Exception("submit 列数不够 <27 message=" + str);
		int i = 0;
		CmppSubmit cmppSubmit = new CmppSubmit(Constants.CMPP3_VERSION);
		cmppSubmit.setId(field[i++]);
		cmppSubmit.setDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(field[i++]));
		cmppSubmit.setMsgId(Long.parseLong(field[i++]));
		cmppSubmit.setPkTotal(Byte.parseByte(field[i++]));
		cmppSubmit.setPkNumber(Byte.parseByte(field[i++]));
		cmppSubmit.setRegisteredDelivery(Byte.parseByte(field[i++]));
		cmppSubmit.setMsgLevel(Byte.parseByte(field[i++]));
		cmppSubmit.setServiceId(field[i++]);
		cmppSubmit.setFeeUserType(Byte.parseByte(field[i++]));
		cmppSubmit.setFeeTerminalId(field[i++]);
		cmppSubmit.setFeeTerminalType(Byte.parseByte(field[i++]));
		cmppSubmit.setTp_pid(Byte.parseByte(field[i++]));
		cmppSubmit.setTp_udhi(Byte.parseByte(field[i++]));
		cmppSubmit.setMsgFmt(Byte.parseByte(field[i++]));
		cmppSubmit.setMsgSrc(field[i++]);
		cmppSubmit.setFeeType(field[i++]);
		cmppSubmit.setFeeCode(field[i++]);
		cmppSubmit.setVaildTime(field[i++]);
		cmppSubmit.setAtTime(field[i++]);
		cmppSubmit.setSrcId(field[i++]);
		cmppSubmit.setDestUsrTl(Byte.parseByte(field[i++]));
		cmppSubmit.setDestTerminalId(field[i++].split(","));
		cmppSubmit.setDestTerminalType(Byte.parseByte(field[i++]));
		cmppSubmit.setMsgLength((byte) Integer.parseInt(field[i++]));

		cmppSubmit
				.setMsgContent(CmppUtil.getMessageContentBytes(field[i++], cmppSubmit.getMsgFmt()));
		cmppSubmit.setLinkId(field[i++]);
		cmppSubmit.setResult(Integer.parseInt(field[i++]));
		return cmppSubmit;
	}

	/**
	 * id + "\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt) + "\t"
	 * + msgId + "\t" + destId + "\t" + serviceId + "\t" + tp_pid + "\t" +
	 * tp_udhi + "\t" + msgFmt + "\t" + srcTerminalId + "\t" + srcTerminalType +
	 * "\t" + registeredDelivery + "\t" + (msgLength & 0xFF) + "\t" +
	 * msgContent.replaceAll("[\r\n\0\t\'\"]", "") + "\t" + linkId;
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static CmppDeliver deliverDecode(String str) throws Exception {
		String field[] = loadConvert(str);
		if (field.length < 14)
			throw new Exception("deliver 列数不够 <13");
		int i = 0;
		CmppDeliver deliver = new CmppDeliver(Constants.CMPP3_VERSION);
		deliver.setId(field[i++]);
		deliver.setDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(field[i++]));
		deliver.setMsgId(Long.parseLong(field[i++]));
		deliver.setDestId(field[i++]);
		deliver.setServiceId(field[i++]);
		deliver.setTp_pid(Byte.parseByte(field[i++]));
		deliver.setTp_udhi(Byte.parseByte(field[i++]));
		deliver.setMsgFmt(Byte.parseByte(field[i++]));
		deliver.setSrcTerminalId(field[i++]);
		deliver.setSrcTerminalType(Byte.parseByte(field[i++]));
		deliver.setRegisteredDelivery(Byte.parseByte(field[i++]));
		deliver.setMsgLength(Byte.parseByte(field[i++]));
		deliver.setMsgContent(CmppUtil.getMessageContentBytes(field[i++], deliver.getMsgFmt()));
		deliver.setLinkId(field[i++]);
		return deliver;
	}

	/**
	 * id + "\t" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt) + "\t"
	 * + msgId + "\t" + stat + "\t" + submitTime + "\t" + doneTime + "\t" +
	 * destTerminalId + "\t" + smscSequence;
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static CmppReport reportDecode(String str) throws Exception {
		String field[] = loadConvert(str);
		if (field.length < 8)
			throw new Exception("report 列数不够 <8");
		int i = 0;
		CmppReport report = new CmppReport();
		report.setId(field[i++]);
		report.setDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(field[i++]));
		report.setMsgId(Long.parseLong(field[i++]));
		report.setStat(field[i++]);
		report.setSubmitTime(field[i++]);
		report.setDoneTime(field[i++]);
		report.setDestTerminalId(field[i++]);
		report.setSmscSequence(Integer.parseInt(field[i++]));
		return report;
	}

	public static String[] loadConvert(String inStr) {
		List<String> list = new ArrayList<String>();
		int end = inStr.length();
		int off = 0;
		char[] in = inStr.toCharArray();
		char aChar;
		int start = 0;
		while (off < end) {
			aChar = in[off++];
			if (aChar == '\t') {
				list.add(new String(in, start, off - start - 1));
				start = off;
			}
		}
		list.add(new String(in, start, end - start));
		String[] out = new String[list.size()];
		out = list.toArray(out);
		return out;
	}
}
