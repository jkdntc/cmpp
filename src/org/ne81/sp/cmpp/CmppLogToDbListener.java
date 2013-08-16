package org.ne81.sp.cmpp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ne81.commons.util.Sequence;
import org.ne81.commons.util.concurrent.Executor;
import org.ne81.commons.util.concurrent.ExecutorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmppLogToDbListener implements CmppListener {
	private final Logger logger = LoggerFactory.getLogger(CmppLogToDbListener.class);
	private Cmpp2Dao dao;
	Executor submitExecutor;
	Executor deliverExecutor;
	Executor reportExecutor;

	public CmppLogToDbListener(String dbconfig) throws ClassNotFoundException, SQLException,
			IOException {
		dao = new Cmpp2Dao(dbconfig);

		submitExecutor = new Executor(new MyExecutorListener(Constants.CMPP_SUBMIT),
				"cmpp_log/cmpp_submit.log", 100000000, false, 1000);

		deliverExecutor = new Executor(new MyExecutorListener(Constants.CMPP_DELIVER),
				"cmpp_log/cmpp_deliver.log", 100000000, false, 1000);

		reportExecutor = new Executor(new MyExecutorListener(Constants.CMPP_REPORT_LEN),
				"cmpp_log/cmpp_report.log", 100000000, false, 1000);
	}

	public CmppLogToDbListener(String dbconfig, String logDir) throws ClassNotFoundException,
			SQLException, IOException {
		dao = new Cmpp2Dao(dbconfig);

		submitExecutor = new Executor(new MyExecutorListener(Constants.CMPP_SUBMIT), logDir
				+ "/cmpp_submit.log", 100000000, false, 1000);

		deliverExecutor = new Executor(new MyExecutorListener(Constants.CMPP_DELIVER), logDir
				+ "/cmpp_deliver.log", 100000000, false, 1000);

		reportExecutor = new Executor(new MyExecutorListener(Constants.CMPP_REPORT_LEN), logDir
				+ "/cmpp_report.log", 100000000, false, 1000);
	}

	@Override
	public void submitSent(CmppClient client, CmppSubmit submit) {
		if (submit.getId() == null)
			submit.setId(Sequence.getInstance().getSequence());
		try {
			submitExecutor.submit(submit.toString());
		} catch (IOException e) {
			// TODO 报警
			logger.error("IOException={}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void deliverReceived(CmppClient client, CmppDeliver deliver) {
		deliver.setId(Sequence.getInstance().getSequence());
		try {
			deliverExecutor.submit(deliver.toString());
		} catch (IOException e) {
			// TODO 报警
			logger.error("IOException={}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void reportReceived(CmppClient client, CmppReport report) {
		report.setId(Sequence.getInstance().getSequence());
		try {
			reportExecutor.submit(report.toString());
		} catch (IOException e) {
			// TODO 报警
			logger.error("IOException={}", e.getMessage());
			e.printStackTrace();
		}
	}

	private class MyExecutorListener implements ExecutorListener {
		private int commandId;

		public MyExecutorListener(int commandId) {
			this.commandId = commandId;
		}

		@Override
		public void call(List<String> list) {
			ArrayList<Object> submitList = new ArrayList<Object>();
			for (String str : list) {
				try {
					if (commandId == Constants.CMPP_SUBMIT) {
						CmppSubmit message = MessageDecoder.submitDecode(str);
						submitList.add(message);
					} else if (commandId == Constants.CMPP_DELIVER) {
						CmppDeliver message = MessageDecoder.deliverDecode(str);
						submitList.add(message);
					} else {
						CmppReport report = MessageDecoder.reportDecode(str);
						submitList.add(report);
					}
				} catch (Exception ex) {
					logger.error("Exception={}", ex.getMessage());
					ex.printStackTrace();
				}
			}
			if (list.size() > 0) {
				dao.insert(submitList);
				submitList.clear();
			}
		}
	}
}
