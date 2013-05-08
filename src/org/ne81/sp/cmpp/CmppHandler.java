package org.ne81.sp.cmpp;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmppHandler implements IoHandler {
	private final Logger logger = LoggerFactory.getLogger(CmppHandler.class);
	private boolean client;
	private boolean login;
	private int w;
	private boolean noResp = false;
	private Hashtable<Integer, CmppMessageHeader> submitTable;
	private CmppListener listener;
	private long reportMsgId;
	private byte version = 32;

	public CmppHandler(boolean client) {
		this.client = client;
		submitTable = new Hashtable<Integer, CmppMessageHeader>();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		logger.error("EXCEPTION: " + cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (client) {
			if (message instanceof CmppConnectResp) {
				if (((CmppConnectResp) message).getStatus() == 0) {
					for (Entry<Integer, CmppMessageHeader> entry : submitTable.entrySet()) {
						CmppMessageHeader submit = entry.getValue();
						session.write(submit);
						w++;
					}
					login = true;
					version = ((CmppConnectResp) message).getVersion();
				}
			} else if (message instanceof CmppDeliver) {
				CmppDeliver deliver = (CmppDeliver) message;
				CmppDeliverResp cdr = new CmppDeliverResp(version);
				cdr.setMsgId(((CmppDeliver) message).getMsgId());
				cdr.setResult((byte) 0);
				cdr.setSequenceId(((CmppDeliver) message).getSequenceId());
				session.write(cdr);
				if (listener != null) {
					if (deliver.getRegisteredDelivery() == 0)
						listener.deliverReceived(null, deliver);
					else
						listener.reportReceived(null, deliver.getReport());
				}
			} else if (message instanceof CmppSubmitResp) {
				CmppSubmitResp scr = (CmppSubmitResp) message;
				CmppSubmit submit = (CmppSubmit) submitTable.remove(scr.getSequenceId());
				if (submit != null) {
					submit.setMsgId(scr.getMsgId());
					submit.setResult(scr.getResult());
					if (listener != null) {
						listener.submitSent(null, submit);
					}
				} else {
					logger.error("无法对应的下行应答：" + scr.toString());
				}
				w--;
			}
		} else {
			if (message instanceof CmppConnect) {
				version = ((CmppConnect) message).getVersion();
				CmppConnectResp ccr = new CmppConnectResp(version);
				ccr.setAuthenticatorISMG(((CmppConnect) message).getAuthenticatorSource());
				ccr.setSequenceId(((CmppConnect) message).getSequenceId());
				ccr.setStatus(0);
				ccr.setVersion(version);
				session.write(ccr);

				// for test
				// for (int i = 0; i < 100; i++) {
				// CmppDeliver deliver = new CmppDeliver(version, ++reportMsgId,
				// "10658167303",
				// "serviceid", "13651398480", new String("4").getBytes(),
				// "linkId");
				// session.write(deliver);
				// }
			} else if (message instanceof CmppDeliverResp) {

			} else if (message instanceof CmppSubmit) {
				CmppSubmitResp csr = new CmppSubmitResp(version);
				csr.setSequenceId(((CmppSubmit) message).getSequenceId());
				csr.setMsgId(++reportMsgId);
				csr.setResult(0);
				session.write(csr);
				// for test
				String destTerminalId[] = ((CmppSubmit) message).getDestTerminalId();
				for (int i = 0; i < destTerminalId.length; i++) {
					CmppDeliver deliver = new CmppDeliver(version, reportMsgId, "", "", "",
							new String("状态报告").getBytes(), "linkId");
					deliver.setRegisteredDelivery((byte) 1);
					deliver.setReport(new CmppReport(reportMsgId, "DELIVRD", "", "",
							destTerminalId[i], i));
					session.write(deliver);
				}
			}

		}
		if (message instanceof CmppActiveTest) {
			CmppActiveTestResp catr = new CmppActiveTestResp();
			catr.setSequenceId(((CmppActiveTest) message).getSequenceId());
			session.write(catr);
		} else if (message instanceof CmppActiveTestResp) {
			noResp = false;
		} else if (message instanceof CmppTerminate) {
			CmppTerminateResp ctr = new CmppTerminateResp();
			ctr.setSequenceId(((CmppTerminate) message).getSequenceId());
			session.write(ctr);
			session.close(true);
		} else if (message instanceof CmppTerminateResp) {
			session.close(true);
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		if (client) {
			if (noResp) {
				session.close(true);
			} else {
				if (submitTable.size() > 0) { // Idel后，有下行没返回应答
					session.close(true);
				} else {
					noResp = true;
					CmppActiveTest cat = new CmppActiveTest();
					session.write(cat);
				}
			}
		} else {
			session.close(true);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if (message instanceof CmppConnect) {

		} else if (message instanceof CmppConnectResp) {

		} else if (message instanceof CmppDeliver) {

		} else if (message instanceof CmppDeliverResp) {

		} else if (message instanceof CmppSubmit) {
		} else if (message instanceof CmppSubmitResp) {

		} else if (message instanceof CmppActiveTest) {

		} else if (message instanceof CmppActiveTestResp) {

		} else if (message instanceof CmppTerminate) {

		} else if (message instanceof CmppTerminateResp) {

		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		setLogin(false);
		setW(0);
		noResp = false;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {

	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public Hashtable<Integer, CmppMessageHeader> getSubmitTable() {
		return submitTable;
	}

	public CmppListener getListener() {
		return listener;
	}

	public void setListener(CmppListener listener) {
		this.listener = listener;
	}

}
