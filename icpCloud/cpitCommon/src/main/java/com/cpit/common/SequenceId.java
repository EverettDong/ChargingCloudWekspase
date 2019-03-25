package com.cpit.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class SequenceId {
	
	private static SequenceId instance = null;
	
	private final static int SIZE = 10;
	
	private JdbcTemplate jdbcTemplate = null;
	
	private final static String PREFIX_CRM = "crm";
	private final static String PREFIX_COL = "collect";
	private final static String PREFIX_BIL = "billing";
	
	
	private SequenceId(){
		jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
	}
	
	public static synchronized SequenceId getInstance(){
		if(instance == null){
			instance = new SequenceId();
		}
		return instance;
	}
	

	/**
	 * 
	 * 获取sequence Id
	 * @param sequenceName
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int getId(String sequenceName) {
		String tableName = "xt_v_sequence";
		if(sequenceName.startsWith(PREFIX_COL)){
			tableName = "col_v_sequence";
		}else if(sequenceName.startsWith(PREFIX_CRM) || sequenceName.startsWith(PREFIX_BIL)){
			tableName = "crm_v_sequence";
		}
		DataSource ds = jdbcTemplate.getDataSource();
		Connection conn = null;
		int id = -1;
		try{
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			String sql = "select CURRENT_VALUE from "+tableName+" where name= ?";
			PreparedStatement psmt = conn.prepareStatement(sql);
			psmt.setString(1, sequenceName);
			ResultSet result = psmt.executeQuery();
		    if(result.next()){
		    		id = result.getInt(1);
		    }
		    result.close();
		    psmt.close();
		    sql = "update "+tableName
		     	  +"	 set CURRENT_VALUE = CURRENT_VALUE + INC"
		    		  +" where name= ?";
		    psmt = conn.prepareStatement(sql);
		    psmt.setString(1, sequenceName);
		    psmt.execute();
		    psmt.close();
			commit(conn);
		}catch(Exception ex){
			id = -1;
			rollback(conn);
		}finally{
			if(conn != null){
				close(conn);
			}
		}
		return id;
	}
	
	/**
	 * 
	 * 获取带前缀的固定长度的sequnenceId
	 * @param sequenceName 名称
	 * @param prefix 前缀
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getId(String sequenceName, String prefix){
		return getId(sequenceName, prefix, SIZE);
	}	
	

	/**
	 * 获取变长的sequnceId
	 * @param sequenceName
	 * @param prefix
	 * @param size
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getId(String sequenceName, String prefix, int size){
		int id = getId(sequenceName);
		return prefix+StringUtils.leftPad(String.valueOf(id), size, "0");
	}

	/**
	 * 获取number个固定长度的sequence
	 * @param sequenceName
	 * @param prefix
	 * @param number
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<String> getIdList(String sequenceName, String prefix, int number){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<number;i++){
			list.add(getId(sequenceName,prefix));
		}
		return list;
	}	
	
	
	//=======================private method
	
	private void close(Connection conn){
		try{
			conn.close();
		}catch(Exception ex){
		}
	}
	
	private void rollback(Connection conn){
		try{
			conn.rollback();
		}catch(Exception ex){
		}
	}
	
	private void commit(Connection conn){
		try{
			conn.commit();
		}catch(Exception ex){
		}
	}	
}
