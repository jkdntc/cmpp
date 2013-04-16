package org.ne81.sp.cmpp;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;

public class CmppUtil {
	public static byte[] getLenBytes(String s, int len) {
		if (s == null) {
			s = "";
		}
		byte[] rb = new byte[len];
		byte[] sb = s.getBytes();
		for (int i = sb.length; i < rb.length; i++) {
			rb[i] = 0;
		}
		if (sb.length == len) {
			return sb;
		} else {
			for (int i = 0; i < sb.length && i < len; i++) {
				rb[i] = sb[i];
			}
			return rb;
		}
	}

	public static byte[] getBytes(byte[] inBytes, int len) {
		byte[] outBytes = new byte[len];
		if (inBytes != null) {
			for (int i = 0; i < inBytes.length && i < len; i++) {
				outBytes[i] = inBytes[i];
			}
		}
		return outBytes;
	}

	public static String getStringFromBuffer(java.nio.ByteBuffer buf, int len) {
		byte[] bytes = new byte[len];
		buf.get(bytes);
		return esc0(new String(bytes));
	}

	public static String esc0(String s) {
		if (s == null || s.length() == 0) {
			s = "";
			return s;
		} else {
			int i = s.indexOf('\0');
			if (i > 0)
				s = s.substring(0, i);
			else
				s = s.replaceAll("\0", "");
		}
		return s;
	}

	public static byte[] getMessageContentBytes(String msgContent, byte msgFmt) {
		if (msgFmt == (byte) 8) {
			try {
				return msgContent.getBytes("utf-16");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if (msgFmt == (byte) 15) {
			try {
				return msgContent.getBytes("gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return msgContent.getBytes();

	}

	public static String getMessageContent(byte[] msgContentBytes, byte msgFmt) {
		if (msgContentBytes == null)
			return "";
		String msgContent = "";
		int offset = 0;
		if (msgContentBytes.length > 1 && msgContentBytes[0] == 0x06) {
			offset = 7;
		}
		if (msgFmt == (byte) 8) {
			try {
				msgContent = new String(msgContentBytes, offset, msgContentBytes.length - offset,
						"utf-16");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if (msgFmt == (byte) 15) {
			try {
				msgContent = new String(msgContentBytes, offset, msgContentBytes.length - offset,
						"gbk");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			msgContent = new String(msgContentBytes);
		}
		return msgContent;
	}

	public static String[] splitString(String in, int len) {
		int totalSegments = (int) Math.ceil(in.length() / (double) len);
		String splittedMsg[] = new String[totalSegments];
		for (int j = 0; j < totalSegments; j++) {
			int endIndex = (j + 1) * len;
			if (endIndex > in.length()) {
				endIndex = in.length();
			}
			splittedMsg[j] = in.substring(j * len, endIndex);
		}
		return splittedMsg;
	}

	public static CmppSubmit[] getConcatenatedSms(CmppSubmit submit, String shortMessage) {
		if (shortMessage.length() <= 70) {
			submit.setMsgContent(shortMessage.getBytes());
			return new CmppSubmit[] { submit };
		}

		String splittedMsg[] = splitString(shortMessage, 66);
		int totalSegments = splittedMsg.length;

		submit.setTp_udhi((byte) 0x01);
		// iteerating on splittedMsg array. Only Sequence Number and short
		// message text will change each time
		CmppSubmit submits[] = new CmppSubmit[totalSegments];
		for (int i = 0; i < totalSegments; i++) {
			ByteBuffer ed = ByteBuffer.allocate(7 + splittedMsg[i].getBytes().length);

			ed.put((byte) 6); // UDH Length

			ed.put((byte) 0x08); // IE Identifier

			ed.put((byte) 4); // IE Data Length

			ed.put((byte) 0); // Reference Number
			ed.put((byte) 1); // Reference Number
			ed.put((byte) totalSegments); // Number of pieces

			ed.put((byte) (i + 1)); // Sequence number

			// This encoding comes in Logica Open SMPP. Refer to its docs for
			// more detail
			try {
				if (submit.getMsgFmt() == (byte) 8)
					ed.put(splittedMsg[i].getBytes("utf-16"));

				else
					ed.put(splittedMsg[i].getBytes("gbk"));
			} catch (UnsupportedEncodingException e1) {
				// 不会出问题
				e1.printStackTrace();
			}

			try {
				submits[i] = submit.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i > 0)
				submits[i].setSequenceId(Sequence.getInstance().getSequence());
			submits[i].setMsgContent(ed.array());
		}
		return submits;
	}

	/**
	 * Load a given resource.
	 * <p/>
	 * This method will try to load the resource using the following methods (in
	 * order):
	 * <ul>
	 * <li>From {@link Thread#getContextClassLoader()
	 * Thread.currentThread().getContextClassLoader()}
	 * <li>From {@link Class#getClassLoader()
	 * ClassLoaderUtil.class.getClassLoader()}
	 * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader()
	 * * }
	 * </ul>
	 * 
	 * @param resourceName
	 *            The name of the resource to load
	 * @param callingClass
	 *            The Class object of the calling object
	 */
	public static URL getResource(String resourceName, Class<?> callingClass) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

		if (url == null) {
			url = CmppUtil.class.getClassLoader().getResource(resourceName);
		}

		if (url == null) {
			ClassLoader cl = callingClass.getClassLoader();

			if (cl != null) {
				url = cl.getResource(resourceName);
			}
		}

		if ((url == null) && (resourceName != null)
				&& ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
			return getResource('/' + resourceName, callingClass);
		}

		return url;
	}
}
