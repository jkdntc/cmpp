package org.ne81.sp.cmpp;

/**
 * Cmpp2.0协议所需常量及程序所需常量
 * 
 * @author jk
 * 
 */
public class Constants {

	/** 每C分钟发送一次网络监测包 */
	public static final int C = 30 * 1000;

	/** 网关之间的消息发送后等待T秒后未收到响应，立即重发 */
	public static final int T = 30 * 1000;

	/** 连续发送N次后仍未得到响应则断开连接 */
	public static final int N = 3;
	/** 滑动窗口数 */
	public static final int WINDOWS = 16;

	/** 消息头长度 */
	public static final int MESSAGE_HEADER_LEN = 12;

	/** 请求连接包长度 */
	public static final int CMPP_CONNECT_LEN = 6 + 16 + 1 + 4;

	/** 请求连接应答包长度 */
	public static final int CMPP_CONNECT_RESP_LEN = 1 + 16 + 1;

	/** 下行长度（不包含短信内容长度） */
	public static final int CMPP_SUBMIT_LEN_EXPMSGLEN = 8 + 1 + 1 + 1 + 1 + 10 + 1 + 21 + 1 + 1 + 1
			+ 6 + 2 + 6 + 17 + 17 + 21 + 1 + 1 + 8;

	/** 下行应答长度 */
	public static final int CMPP_SUBMIT_RESP_LEN = 8 + 1;

	/** 上行长度（不包含短信内容长度） */
	public static final int CMPP_DELIVER_LEN_EXPMSGLEN = 8 + 21 + 10 + 1 + 1 + 1 + 21 + 1 + 1 + 8;

	public static final int CMPP_REPORT_LEN = 8 + 7 + 10 + 10 + 21 + 4;
	/** 上行应答长度 */
	public static final int CMPP_DELIVER_RESP_LEN = 8 + 1;

	public static final int CMPP_ACTIVE_TEST_RESP_LEN = 1;

	/** 请求连接应答包长度 */
	public static final int CMPP3_CONNECT_RESP_LEN = 4 + 16 + 1;

	public static final int CMPP3_SUBMIT_LEN_EXPMSGLEN = 8 + 1 + 1 + 1 + 1 + 10 + 1 + 32 + 1 + 1
			+ 1 + 1 + 6 + 2 + 6 + 17 + 17 + 21 + 1 + 1 + 1 + 20;

	public static final int CMPP3_SUBMIT_RESP_LEN = 8 + 4;

	public static final int CMPP3_DELIVER_LEN_EXPMSGLEN = 8 + 21 + 10 + 1 + 1 + 1 + 32 + 1 + 1 + 1
			+ 20;

	public static final int CMPP3_DELIVER_RESP_LEN = 8 + 4;

	public static final int CMPP_CONNECT = 0x00000001;

	public static final int CMPP_CONNECT_RESP = 0x80000001;

	public static final int CMPP_TERMINATE = 0x00000002;

	public static final int CMPP_TERMINATE_RESP = 0x80000002;

	public static final int CMPP_SUBMIT = 0x00000004;

	public static final int CMPP_SUBMIT_RESP = 0x80000004;

	public static final int CMPP_DELIVER = 0x00000005;

	public static final int CMPP_DELIVER_RESP = 0x80000005;

	public static final int CMPP_QUERY = 0x00000006;

	public static final int CMPP_QUERY_RESP = 0x80000006;

	public static final int CMPP_CANCEL = 0x00000007;

	public static final int CMPP_CANCEL_RESP = 0x80000007;

	public static final int CMPP_ACTIVE_TEST = 0x00000008;

	public static final int CMPP_ACTIVE_TEST_RESP = 0x80000008;

	/** Cmpp协议版本字节常量 */
	public static final byte CMPP2_VERSION = (byte) 32;
	/** Cmpp协议版本字节常量 */
	public static final byte CMPP3_VERSION = (byte) 48;
}
