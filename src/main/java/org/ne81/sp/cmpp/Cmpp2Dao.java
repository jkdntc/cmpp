package org.ne81.sp.cmpp;

import org.ne81.commons.db.SqlPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Cmpp2Dao {
	public final static int READY = 1;
	public final static int SENDING = 2;
	public final static int SENT = 3;
	private SqlPool sqlPool;

	public Cmpp2Dao(String dbconfig) throws ClassNotFoundException, SQLException, IOException {
		sqlPool = new SqlPool(dbconfig, "cmpp_log/sqlpool.log");
	}

	public void close() throws IOException {
		sqlPool.stop();
	}

	public int insertSubmit(List<CmppSubmit> messages) {
		if (messages == null || messages.size() == 0)
			return 0;
		String sql_error = "";
		int colums = 26;
		String sql = "INSERT INTO `cmpp_submit`(`id`, `dt`, `msgId`, `pkTotal`, `pkNumber`, `registeredDelivery`, `msgLevel`, `serviceId`, `feeUserType`, `feeTerminalId`,`feeTerminalType`, `tp_pid`, `tp_udhi`, `msgFmt`, `msgSrc`, `feeType`, `feeCode`, `vaildTime`, `atTime`, `srcId`, `destUsrTl`,`destTerminalType`, `msgLength`, `msgContent`, `linkId`, `result`)VALUES";
		for (int i = 0; i < messages.size(); i++) {
			sql += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?),";
		}
		sql = sql.substring(0, sql.length() - 1);
		int count = 0;
		Connection conn = null;
		for (int times = 0; times < 3; times++)
			try {
				conn = sqlPool.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for (int i = 0; i < messages.size(); i++) {
					CmppSubmit message = messages.get(i);
					int j = 1;
					pstmt.setString(i * colums + j++, message.getId());
					pstmt.setTimestamp(i * colums + j++, new Timestamp(message.getDt().getTime()));
					pstmt.setLong(i * colums + j++, message.getMsgId());
					pstmt.setByte(i * colums + j++, message.getPkTotal());
					pstmt.setByte(i * colums + j++, message.getPkNumber());
					pstmt.setByte(i * colums + j++, message.getRegisteredDelivery());
					pstmt.setByte(i * colums + j++, message.getMsgLevel());
					pstmt.setString(i * colums + j++, message.getServiceId());
					pstmt.setByte(i * colums + j++, message.getFeeUserType());
					pstmt.setString(i * colums + j++, message.getFeeTerminalId());
					pstmt.setByte(i * colums + j++, message.getFeeTerminalType());
					pstmt.setByte(i * colums + j++, message.getTp_pid());
					pstmt.setByte(i * colums + j++, message.getTp_udhi());
					pstmt.setByte(i * colums + j++, message.getMsgFmt());
					pstmt.setString(i * colums + j++, message.getMsgSrc());
					pstmt.setString(i * colums + j++, message.getFeeType());
					pstmt.setString(i * colums + j++, message.getFeeCode());
					pstmt.setString(i * colums + j++, message.getVaildTime());
					pstmt.setString(i * colums + j++, message.getAtTime());
					pstmt.setString(i * colums + j++, message.getSrcId());
					pstmt.setByte(i * colums + j++, message.getDestUsrTl());
					pstmt.setByte(i * colums + j++, message.getDestTerminalType());
					pstmt.setInt(i * colums + j++, message.getMsgLength() & 0xFF);
					pstmt.setString(i * colums + j++, CmppUtil.getMessageContent(
							message.getMsgContent(), message.getMsgFmt()));
					pstmt.setString(i * colums + j++, message.getLinkId());
					pstmt.setInt(i * colums + j++, message.getResult());
				}
				sql_error = pstmt.toString();
				count = pstmt.executeUpdate();
				pstmt.close();
				String subSql = "INSERT INTO `cmpp_submit_destid`(`submitid`, `destTerminalId`)VALUES";
				for (int i = 0; i < messages.size(); i++) {
					String destTerminalIds[] = messages.get(i).getDestTerminalId();
					for (int j = 0; j < destTerminalIds.length; j++) {
                        subSql += "('" + messages.get(i).getId() + "','" + destTerminalIds[j] + "'),";
					}
				}
                subSql = subSql.substring(0, subSql.length() - 1);
				pstmt = conn.prepareStatement(subSql);
				sql_error = pstmt.toString();
				count = pstmt.executeUpdate();
				pstmt.close();
				break;
			} catch (SQLException e) {
				sqlPool.saveSql(e.getMessage(), sql_error);
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

			}
		return count;
	}

	public int insertDeliver(List<CmppDeliver> messages) {
		int colums = 14;
		String sql = "INSERT INTO `cmpp_deliver`(`id`,`dt`,`msgId`,`destId`,`serviceId`,`tp_pid`,`tp_udhi`,`msgFmt`,`srcTerminalId`,`srcTerminalType`,`registeredDelivery`,`msgLength`,`msgContent`,`linkId`)VALUES ";
		for (int i = 0; i < messages.size(); i++) {
			sql += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?),";
		}
		sql = sql.substring(0, sql.length() - 1);
		int count = 0;
		Connection conn = null;
        String pSql="";
		for (int times = 0; times < 3; times++)
			try {
				conn = sqlPool.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for (int i = 0; i < messages.size(); i++) {
					CmppDeliver message = messages.get(i);
					int j = 1;
					pstmt.setString(i * colums + j++, message.getId());
					pstmt.setTimestamp(i * colums + j++, new Timestamp(message.getDt().getTime()));
					pstmt.setLong(i * colums + j++, message.getMsgId());
					pstmt.setString(i * colums + j++, message.getDestId());
					pstmt.setString(i * colums + j++, message.getServiceId());
					pstmt.setByte(i * colums + j++, message.getTp_pid());
					pstmt.setByte(i * colums + j++, message.getTp_udhi());
					pstmt.setByte(i * colums + j++, message.getMsgFmt());
					pstmt.setString(i * colums + j++, message.getSrcTerminalId());
					pstmt.setByte(i * colums + j++, message.getSrcTerminalType());
					pstmt.setByte(i * colums + j++, message.getRegisteredDelivery());
					pstmt.setInt(i * colums + j++, message.getMsgLength() & 0xFF);
					pstmt.setString(i * colums + j++, CmppUtil.getMessageContent(
							message.getMsgContent(), message.getMsgFmt()));
					pstmt.setString(i * colums + j++, message.getLinkId());
				}
                pSql = pstmt.toString();
				count = pstmt.executeUpdate();
				pstmt.close();
				break;
			} catch (SQLException e) {
				sqlPool.saveSql(e.getMessage(), pSql);
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

			}
		return count;
	}

	public int insertReport(List<CmppReport> messages) {
		int colums = 8;
		String sql = "INSERT INTO `cmpp_report`(`id`,`dt`,`msgId`,`stat`,`submitTime`,`doneTime`,`destTerminalId`,`smscSequence`)VALUES";
		for (int i = 0; i < messages.size(); i++) {
			sql += "(?,?,?,?,?,?,?,?),";
		}
		sql = sql.substring(0, sql.length() - 1);
		int count = 0;
		Connection conn = null;
        String pSql="";
		for (int times = 0; times < 3; times++)
			try {
				conn = sqlPool.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for (int i = 0; i < messages.size(); i++) {
					CmppReport message = messages.get(i);
					int j = 1;
					pstmt.setString(i * colums + j++, message.getId());
					pstmt.setTimestamp(i * colums + j++, new Timestamp(message.getDt().getTime()));
					pstmt.setLong(i * colums + j++, message.getMsgId());
					pstmt.setString(i * colums + j++, message.getStat());
					pstmt.setString(i * colums + j++, message.getSubmitTime());
					pstmt.setString(i * colums + j++, message.getDoneTime());
					pstmt.setString(i * colums + j++, message.getDestTerminalId());
					pstmt.setInt(i * colums + j++, message.getSmscSequence());
				}
                pSql = pstmt.toString();
				count = pstmt.executeUpdate();
				pstmt.close();
				break;
			} catch (SQLException e) {
				sqlPool.saveSql(e.getMessage(), pSql);
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

			}
		return count;
	}

	public int insert(List list) {
		if (list.get(0) instanceof CmppSubmit) {
			return insertSubmit(list);
		} else if (list.get(0) instanceof CmppDeliver) {
			return insertDeliver(list);
		} else if (list.get(0) instanceof CmppReport) {
			return insertReport(list);
		}
		return 0;
	}
}
