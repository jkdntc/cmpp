package org.ne81.sp.cmpp.test;

import org.ne81.sp.cmpp.CmppServer;

public class CmppTestServer {
	public static void main(String args[]) {
		new CmppServer(17090).start();
	}
}
