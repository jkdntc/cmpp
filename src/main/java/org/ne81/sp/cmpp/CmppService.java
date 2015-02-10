package org.ne81.sp.cmpp;

import org.ne81.commons.thread.SenderThreadGroup;
import org.ne81.commons.thread.SenderThreadProcessListener;

import java.io.IOException;

public class CmppService {
    private CmppClient moClient;
    private CmppClient mtClient;
    private CmppListener service;
    private SenderThreadGroup<Object> stg;

    public CmppService(String moConf, String mtConf, String serviceClass)
            throws Exception {
        System.out.println("1:" + moConf + "2:" + mtConf + "3:" + serviceClass);
        moClient = new CmppClient(moConf);
        CmppLogToDbListener toDb = new CmppLogToDbListener(moConf); //已保存文本日志
        moClient.addListener(toDb);
        ServiceListener proxy = new ServiceListener();
        moClient.addListener(proxy);
        if (!mtConf.equals("null")) {
            mtClient = new CmppClient(mtConf);
            mtClient.addListener(toDb);
            mtClient.addListener(proxy);
        }
        service = (CmppListener) Class.forName(serviceClass).newInstance();
        stg = new SenderThreadGroup<Object>(30, new CmppSenderThread(mtClient == null ? moClient : mtClient, service));
    }

    public static void main(String[] args) throws Throwable {
        new CmppService(args[0], args[1], args[2]).run();
    }

    public void run() throws Exception {
        moClient.start();
        if (mtClient != null)
            mtClient.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                moClient.stop();
                if (mtClient != null)
                    mtClient.stop();
            }
        });
    }

    public class ServiceListener implements CmppListener {

        public void deliverReceived(CmppClient client, CmppDeliver deliver) {
            stg.put(deliver);
        }

        public void reportReceived(CmppClient client, CmppReport report) {
            // TODO Auto-generated method stub

        }

        public void submitSent(CmppClient client, CmppSubmit submit) {
            // TODO Auto-generated method stub

        }

    }

    public class CmppSenderThread implements SenderThreadProcessListener<Object> {
        CmppClient client;
        CmppListener service;

        public CmppSenderThread(CmppClient client, CmppListener service)
                throws IOException {
            this.client = client;
            this.service = service;
        }

        @Override
        public void process(Object obj) {
            if (obj instanceof CmppDeliver) {
                service.deliverReceived(client, (CmppDeliver) obj);
            } else if (obj instanceof CmppReport) {
                service.reportReceived(client, (CmppReport) obj);
            } else if (obj instanceof CmppSubmit) {
                service.submitSent(client, (CmppSubmit) obj);
            }
        }

    }
}
