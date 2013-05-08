package org.ne81.sp.cmpp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmppClient implements Runnable, CmppListener {
	private final Logger logger = LoggerFactory.getLogger(CmppClient.class);
	private String sharedSecret;
	private String spClientBindIp;
	private String spId;
	private String spNumber;
	private String ismgServerIp;
	private int ismgServerPort;
	private byte version = (byte) 32;
	private Thread thisThread;
	private IoSession session = null;
	private NioSocketConnector connector;
	private CmppHandler handler;
	private static final int WINDOWS = 16;
	private Vector<CmppListener> listener = new Vector<CmppListener>();

	public CmppClient(String conf) throws IOException {
		Properties properties = new Properties();
		properties.load(CmppUtil.getResource(conf, this.getClass()).openStream());
		this.spId = properties.getProperty("spId");
		this.sharedSecret = properties.getProperty("sharedSecret");
		this.spClientBindIp = properties.getProperty("spClientBindIp");
		this.spNumber = properties.getProperty("spNumber");
		this.ismgServerIp = properties.getProperty("ismgServerIp");
		this.ismgServerPort = Integer.parseInt(properties.getProperty("ismgServerPort"));
		this.version = (byte) Integer.parseInt(properties.getProperty("version"));
		init();
	}

	public CmppClient(String sharedSecret, String spClientBindIp, String spId, String spNumber,
			String ismgServerIp, int ismgServerPort, byte version) {
		this.spId = spId;
		this.sharedSecret = sharedSecret;
		this.spClientBindIp = spClientBindIp;
		this.spNumber = spNumber;
		this.ismgServerIp = ismgServerIp;
		this.ismgServerPort = ismgServerPort;
		this.version = version;
		init();
	}

	private void init() {
		handler = new CmppHandler(true);
		handler.setListener(this);
	}

	public void start() {
		thisThread = new Thread(this);
		thisThread.start();
	}

	public void stop() {
		thisThread = null;
		// wait until the summation is done
		session.getCloseFuture().awaitUninterruptibly(10 * 1000);
		session.close(false);
		connector.dispose();
	}

	public void run() {
		logger.info("Start ISMGServer=" + ismgServerIp + ":" + ismgServerPort + "\t SPNumber="
				+ spNumber);

		while (thisThread == Thread.currentThread()) {
			if (session == null || !session.isConnected()) {
				try {
					if (connector != null)
						connector.dispose();
					connector = new NioSocketConnector();
					connector.setConnectTimeoutMillis(10 * 1000);

					connector.getFilterChain().addLast("codec",
							new ProtocolCodecFilter(new CmppCodecFactory(version)));
					connector.getFilterChain().addLast("logger", new LoggingFilter());
					connector.setHandler(handler);
					connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
							Constants.C / 1000);
					ConnectFuture future = connector.connect(new InetSocketAddress(ismgServerIp,
							ismgServerPort), new InetSocketAddress(spClientBindIp, 0));
					future.awaitUninterruptibly();
					session = future.getSession();
					session.getConfig().setReadBufferSize(4000);
					CmppConnect cc = new CmppConnect();
					SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmss");
					String time = formatter.format(new Date());
					cc.setTimestamp(Integer.parseInt(time));
					try {
						cc.setAuthenticatorSource(MessageDigest.getInstance("MD5").digest(
								(spId + "\0\0\0\0\0\0\0\0\0" + sharedSecret + time).getBytes()));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					// cc.setSequenceId(1);
					cc.setSourceAddr(spId);
					// cc.setTotalLength(totalLength);
					cc.setVersion((byte) version);
					session.write(cc);
				} catch (RuntimeIoException e) {
					logger.error("Failed to connect.");
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	synchronized public boolean ready() {
		return (handler.isLogin() && handler.getW() < WINDOWS && session != null && session
				.isConnected());
	}

	synchronized public void send(CmppSubmit submit) {
		submit.setMsgSrc(spId);
		handler.setW(handler.getW() + 1);
		handler.getSubmitTable().put(submit.getSequenceId(), submit);
		session.write(submit);
	}

	public void addListener(CmppListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void submitSent(CmppClient client, CmppSubmit submit) {
		for (CmppListener listener : this.listener)
			listener.submitSent(this, submit);
	}

	@Override
	public void deliverReceived(CmppClient client, CmppDeliver deliver) {
		for (CmppListener listener : this.listener)
			listener.deliverReceived(this, deliver);
	}

	@Override
	public void reportReceived(CmppClient client, CmppReport report) {
		for (CmppListener listener : this.listener)
			listener.reportReceived(this, report);
	}
}
