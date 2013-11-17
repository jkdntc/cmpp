package org.ne81.sp.cmpp;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class CmppServer implements Runnable {
	private int port;
	private Thread thisThread;
	private IoAcceptor acceptor;

	public CmppServer(int port) {
		this.port = port;
	}

	public void start() {
		thisThread = new Thread(this);
		thisThread.start();
	}

	public void stop() {
		thisThread = null;
		acceptor.unbind();
	}

	@Override
	public void run() {
		try {
			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new CmppCodecFactory()));
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.setHandler(new CmppHandler(false));
			acceptor.getSessionConfig().setReadBufferSize(4000);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3 * 60 * 1000);
			InetSocketAddress isa = new InetSocketAddress(port);
			acceptor.bind(isa);
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
	}
}
