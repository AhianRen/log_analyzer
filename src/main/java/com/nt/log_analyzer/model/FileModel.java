package com.nt.log_analyzer.model;

public class FileModel {
	private int fileId;
	private String fileAbsolutePath;
	private long lastModified;
	private int lastLine;
	
	

	@Override
	public String toString() {
		return "FileModel [fileId=" + fileId + ", fileAbsolutePath=" + fileAbsolutePath + ", lastModified="
				+ lastModified + ", lastLine=" + lastLine + "]";
	}


	public FileModel() {
		super();
	}
	
	
	public int getFileId() {
		return fileId;
	}


	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}


	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}


	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public int getLastLine() {
		return lastLine;
	}
	public void setLastLine(int lastLine) {
		this.lastLine = lastLine;
	}
	
	
	
	
}
