package com.nt.log_analyzer.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nt.log_analyzer.model.LogModel;
@Mapper
public interface LogModelDao {
	//int insertLogModel(LogModel logModel);
	int insertBatch(List<LogModel> logModels);
	
	//int updateLastMessage(String message);
	
	//LogModel selectLogModelById(int id);
	//List<LogModel> selectLogModelsByDateRange(@Param("fromTimeStamp") Date fromTimeStamp,@Param("toTimeStamp") Date toTimeStamp);
	//List<LogModel> selectAllByIdDesc(@Param("startRow")int startRow,@Param("size")int size);
	//long selectAllCount();
	//int selectResultCountByCondition(@Param("ids") List<Integer> ids,@Param("fileName")String fileName,@Param("timeStamp_from") Date timeStamp_from,@Param("timeStamp_to") Date timeStamp_to,@Param("priority") String priority,@Param("relatedType") String relatedType,@Param("threadName")String threadName,@Param("className")String className,@Param("message")String message);
	//List<LogModel> selectLogModelsByCondition(@Param("ids") List<Integer> ids,@Param("fileName")String fileName,@Param("timeStamp_from") Date timeStamp_from,@Param("timeStamp_to") Date timeStamp_to,@Param("priority") String priority,@Param("startRow")int startRow,@Param("size")int size,@Param("relatedType") String relatedType,@Param("threadName")String threadName,@Param("className")String className,@Param("message")String message);
	List<String> selectAllPriority();
	int selectCountByPriority(String priority);
	List<String> selectAllThreadName();
	int selectCountByThreadName(String threadName);
	List<LogModel> selectByIds(@Param("ids")List<Integer> ids);
}
