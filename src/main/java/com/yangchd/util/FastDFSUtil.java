package com.yangchd.util;

import org.csource.common.NameValuePair;

import java.io.File;
import java.util.Map;

public class FastDFSUtil {
	
	/**
	 * 上传临时文件，会被定时任务清理
	 */
	public static Map<String,Object> uploadByFile(File upfile) throws Exception{
		//文件后缀
		String ext = FileManager.getFileExt(upfile);
		//文件作者，直接以文件名
		String author = upfile.getName();
		
		FastDFSFile file = new FastDFSFile(FileManager.getBytesByFile(upfile),author, ext);
		NameValuePair[] meta_list = FileManager.getValuePairs
				(upfile.getName(), String.valueOf(upfile.length()), ext, author);
		
		Map<String,Object> fileMap = FileManager.upload(file, meta_list);
		return fileMap;
	}

	public static void main(String[] args) {
		File file = new File("C://11.txt");
		try {
			uploadByFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
