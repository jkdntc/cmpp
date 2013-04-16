package org.ne81.sp.cmpp;

public interface CmppListener {
	public void submitSent(CmppClient client, CmppSubmit submit);

	public void deliverReceived(CmppClient client, CmppDeliver deliver);

	public void reportReceived(CmppClient client, CmppReport report);
}
