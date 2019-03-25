-- 非分片库表定义文件
DROP TABLE IF EXISTS mon_device_offline_his;
create table mon_device_offline_his
(  
   id                   int not null auto_increment,
   device_no            varchar(36) not null comment '充电装编码',
   device_ip            varchar(36) not null comment '充电装ip',
   pregate_ip           varchar(36) not null comment '前置网关ip',
   device_port          int not null comment '充电装端口',
   month                int not null comment '月',
   version              varchar(20) not null comment '报文版本',
   p_time               timestamp not null comment '桩上报时间',
   upload_time          datetime    comment 'ftp上传时间',
   type                 char(1) not null default '1' COMMENT '1:上行,2:下行',
   message              varchar(500) not null comment '离线充电报文',
   state                int not null comment '离线充电报文上报状态 0:未上报 1:上报',
   primary key (id)
)auto_increment=10001;

alter table mon_device_offline_his comment '离线充电报文缓存表';

DROP TABLE IF EXISTS mon_message_exception;
CREATE TABLE mon_message_exception (
  id                    int not null auto_increment,
  month                 int not null comment '月',
  type                  char(1) not null default '1' COMMENT '1:上行,2:下行',
  device_ip             varchar(36) not null comment '充电装ip',
  device_port           int not null comment '充电装端口',
  message               varchar(1000) not null comment '报文',
  info                  varchar(200) not null comment '错误信息',
  insertdate            DATETIME not null comment '插入时间',
  PRIMARY KEY (id)
) auto_increment=10001;

alter table mon_message_exception comment '异常报文表';

DROP TABLE IF EXISTS mon_message;
CREATE TABLE mon_message (
  id                    int not null auto_increment,
  month                 int not null comment '月',
  version               varchar(20) not null comment '报文版本',
  command               char(6) DEFAULT '0x0000' COMMENT '协议id',
  type                  char(1) not null default '1' COMMENT '1:上行,2:下行',
  device_no             varchar(36) not null comment '充电装编码',
  device_ip             varchar(36) not null comment '充电装ip',
  device_port           int not null comment '充电装端口',
  message               varchar(1000) not null comment '报文',
  insertdate            DATETIME not null comment '插入时间',
  PRIMARY KEY (id)
) auto_increment=10001;

alter table mon_message comment '签到报文表';