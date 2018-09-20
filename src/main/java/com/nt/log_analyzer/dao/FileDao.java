package com.nt.log_analyzer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nt.log_analyzer.model.FileModel;

@Mapper
public interface FileDao {
	
	int insertFileModel(FileModel fileModel);
	FileModel selectByFileAbsolutePath(@Param("fileAbsolutePath") String fileAbsolutePath);
	int updateById(FileModel fileModel);
	List<FileModel> selectAllFile();
	int deleteFileAndLog(int fileId);
}
