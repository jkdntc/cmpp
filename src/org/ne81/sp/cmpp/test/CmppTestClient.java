package org.ne81.sp.cmpp.test;

import java.io.IOException;
import java.sql.SQLException;

import org.ne81.sp.cmpp.CmppClient;
import org.ne81.sp.cmpp.CmppLogToDbListener;
import org.ne81.sp.cmpp.CmppSubmit;
import org.ne81.sp.cmpp.Constants;

public class CmppTestClient {
	public static void main(String args[]) throws IOException, ClassNotFoundException,
			SQLException, InterruptedException {
		CmppClient client = new CmppClient("cmpp.properties");// 117.79.235.165

		client.addListener(new CmppLogToDbListener("jdbc.properties"));

		client.start();
		for (int i = 0; i < 10000; i++) {
			while (!client.ready());
				
			CmppSubmit submit = new CmppSubmit(Constants.CMPP2_VERSION, "serviceId", "1311111111",
					"C00014", "01", "0", "50", "1311111111", "第一次", "linkid");

			client.send(submit);
		}
		client.stop();
		
		
		
		client.start();
		for (int i = 0; i < 10000; i++) {
			while (!client.ready());
				
			CmppSubmit submit = new CmppSubmit(Constants.CMPP2_VERSION, "serviceId", "1311111111",
					"C00014", "01", "0", "50", "1311111111", "二次发送", "linkid");

			client.send(submit);
		}
		client.stop();

	}
}
