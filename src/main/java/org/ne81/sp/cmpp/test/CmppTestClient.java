package org.ne81.sp.cmpp.test;

import java.io.IOException;
import java.sql.SQLException;

import org.ne81.sp.cmpp.CmppClient;
import org.ne81.sp.cmpp.CmppLogToDbListener;
import org.ne81.sp.cmpp.CmppSubmit;
import org.ne81.sp.cmpp.CmppUtil;
import org.ne81.sp.cmpp.Constants;

public class CmppTestClient {
	public static void main(String args[]) throws IOException, ClassNotFoundException,
			SQLException, InterruptedException {
		final CmppClient client = new CmppClient("cmpp.properties");

		//client.addListener(new CmppLogToDbListener("cmpp.properties"));

		client.start();
		for (int i = 0; i < 1; i++) {
			while (!client.ready())
				;
			CmppSubmit submit = new CmppSubmit(Constants.CMPP2_VERSION, "MJS0019907", "13651398480",
					"01", "0", "10690179", "13651398480", "中文안녕하세요", "linkid");
			submit.setMsgId(submit.getSequenceId());
			// submit.setMsgFmt((byte) 8);
			CmppSubmit submits[] = CmppUtil
					.getConcatenatedSms(
							submit,
							"你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好");
			for (int j = 0; j < submits.length; j++)
				client.send(submits[j]);
		}
		java.lang.Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				client.stop();
			}
		});

	}
}
