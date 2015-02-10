------------------------------------------------------
-- Export file for user SMSDB                       --
-- Created by Administrator on 2011-12-21, 22:11:18 --
------------------------------------------------------

spool cmpp.log

prompt
prompt Creating table NESP_SYNCORDERRELATIONLOG
prompt ========================================
prompt
create table NESP_SYNCORDERRELATIONLOG
(
  ID                    NUMBER(32) not null,
  GWCODE                VARCHAR2(21),
  TRANSACTIONID         VARCHAR2(64),
  VERSION               VARCHAR2(64),
  SENDADDRESSDEVICETYPE VARCHAR2(64),
  SENDADDRESSDEVICEID   VARCHAR2(64),
  DESTADDRESSDEVICETYPE VARCHAR2(64),
  DESTADDRESSDEVICEID   VARCHAR2(64),
  FEEUSERIDTYPE         VARCHAR2(64),
  FEEUSERIDMSISDN       VARCHAR2(64),
  FEEUSERIDPSEUDOCODE   VARCHAR2(64),
  DESTUSERIDTYPE        VARCHAR2(64),
  DESTUSERIDMSISDN      VARCHAR2(64),
  DESTUSERIDPSEUDOCODE  VARCHAR2(64),
  LINKID                VARCHAR2(64),
  ACTIONID              VARCHAR2(64),
  ACTIONREASONID        VARCHAR2(64),
  SPID                  VARCHAR2(64),
  SPSERVICEID           VARCHAR2(64),
  ACCESSMODE            VARCHAR2(64),
  FEATURESTR            VARCHAR2(256),
  DT                    DATE
)
;
alter table NESP_SYNCORDERRELATIONLOG
  add constraint PK_NESP_SYNCORDERRELATIONLOG primary key (ID);

prompt
prompt Creating table NS_CMPP_DELIVER_LOG
prompt ==================================
prompt
create table NS_CMPP_DELIVER_LOG
(
  DELIVERID           NUMBER(10) not null,
  MSG_ID              VARCHAR2(32),
  DEST_ID             VARCHAR2(21),
  SERVICE_ID          VARCHAR2(10),
  TP_PID              INTEGER,
  TP_UDHI             INTEGER,
  MSG_FMT             INTEGER,
  SRC_TERMINAL_ID     VARCHAR2(32),
  SRC_TERMINAL_TYPE   INTEGER,
  REGISTERED_DELIVERY INTEGER,
  MSG_LENGTH          INTEGER,
  MSG_CONTENT         VARCHAR2(280),
  LINKID              VARCHAR2(20),
  DELIVERDATE         DATE not null,
  GWCODE              VARCHAR2(20)
)
;
alter table NS_CMPP_DELIVER_LOG
  add constraint PK_NS_CMPP_DELIVER_LOG primary key (DELIVERID);
create index IDX$$_5C5A0001 on NS_CMPP_DELIVER_LOG (SRC_TERMINAL_ID, TO_CHAR(DELIVERDATE,'yyyy-mm-dd'));
create index IDX_CD_SRC_TERMINAL_ID on NS_CMPP_DELIVER_LOG (SRC_TERMINAL_ID);

prompt
prompt Creating table NS_CMPP_REPORT_LOG
prompt =================================
prompt
create table NS_CMPP_REPORT_LOG
(
  REPORTID         NUMBER(10) not null,
  MSG_ID           VARCHAR2(32),
  STAT             VARCHAR2(20),
  SUBMIT_TIME      VARCHAR2(10),
  DONE_TIME        VARCHAR2(10),
  DEST_TERMINAL_ID VARCHAR2(32),
  SMSC_SEQUENCE    INTEGER,
  REPORTDATE       DATE not null,
  GWCODE           VARCHAR2(20)
)
;
alter table NS_CMPP_REPORT_LOG
  add constraint PK_NS_CMPP_REPORT_LOG primary key (REPORTID);
create index IDX_NS_CMPP_REPORT_LOG on NS_CMPP_REPORT_LOG (STAT);
create index IDX_NS_CMPP_REPORT_LOG_GWCODE on NS_CMPP_REPORT_LOG (GWCODE);
create index IDX_NS_CMPP_REPORT_LOG_MSGID on NS_CMPP_REPORT_LOG (MSG_ID);
create index IDX_NS_CMPP_REPORT_LOG_PN on NS_CMPP_REPORT_LOG (DEST_TERMINAL_ID);

prompt
prompt Creating table NS_CMPP_SUBMIT_LOG
prompt =================================
prompt
create table NS_CMPP_SUBMIT_LOG
(
  SUBMITID            NUMBER(10) not null,
  MSG_ID              VARCHAR2(32),
  PK_TOTAL            INTEGER,
  PK_NUMBER           INTEGER,
  REGISTERED_DELIVERY INTEGER,
  MSG_LEVEL           INTEGER,
  SERVICE_ID          VARCHAR2(10),
  FEE_USERTYPE        INTEGER,
  FEE_TERMINAL_ID     VARCHAR2(32),
  FEE_TERMINAL_TYPE   INTEGER,
  TP_PID              INTEGER,
  TP_UDHI             INTEGER,
  MSG_FMT             INTEGER,
  MSG_SRC             VARCHAR2(6),
  FEETYPE             VARCHAR2(2),
  FEECODE             VARCHAR2(6),
  VALID_TIME          VARCHAR2(17),
  AT_TIME             VARCHAR2(17),
  SRC_ID              VARCHAR2(21),
  DESTUSR_TL          INTEGER,
  DEST_TERMINAL_ID    VARCHAR2(1500),
  DEST_TERMINAL_TYPE  INTEGER,
  MSG_LENGTH          INTEGER,
  MSG_CONTENT         VARCHAR2(280),
  LINKID              VARCHAR2(20),
  SUBMITDATE          DATE not null,
  RESULT              INTEGER,
  GWCODE              VARCHAR2(20)
)
;
alter table NS_CMPP_SUBMIT_LOG
  add constraint PK_NS_CMPP_SUBMIT_LOG primary key (SUBMITID);
create index IDX_NS_CMPP_SUBMIT_LOG_DESTPN on NS_CMPP_SUBMIT_LOG (DEST_TERMINAL_ID);
create index IDX_NS_CMPP_SUBMIT_LOG_GWCODE on NS_CMPP_SUBMIT_LOG (GWCODE);
create index IDX_NS_CMPP_SUBMIT_LOG_LINKID on NS_CMPP_SUBMIT_LOG (LINKID);
create index IDX_NS_CMPP_SUBMIT_LOG_MSG on NS_CMPP_SUBMIT_LOG (MSG_CONTENT);
create index IDX_NS_CMPP_SUBMIT_LOG_PN on NS_CMPP_SUBMIT_LOG (FEE_TERMINAL_ID);
create index IDX_NS_CMPP_SUBMIT_LOG_RESULT on NS_CMPP_SUBMIT_LOG (RESULT);
create index IDX_NS_CMPP_SUBMIT_LOG_SID on NS_CMPP_SUBMIT_LOG (SERVICE_ID);
create index I_NS_CMPP_SUBMIT_LOG_MSG_ID on NS_CMPP_SUBMIT_LOG (MSG_ID);


spool off
