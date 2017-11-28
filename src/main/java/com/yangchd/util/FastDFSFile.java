package com.yangchd.util;

/**
 * @author yangchd
 * @date 2017/8/23
 * 文件Bean，存放上传时文件可能用到的参数
 */
public class FastDFSFile implements FileManagerConfig {

    private static final long serialVersionUID = 1L;

    //文件流
    private byte[] content;
    //文件后缀名
    private String ext;
    //文件名
    private String filename;
    //文件大小
    private String length;
    //文件作者
    private String author = FILE_DEFAULT_AUTHOR;

    public FastDFSFile(byte[] content, String ext) {
        this.content = content;
        this.ext = ext;
    }

    public FastDFSFile(byte[] content, String filename, String ext) {
        this.content = content;
        this.filename = filename;
        this.ext = ext;
    }

    public FastDFSFile(byte[] content, String filename, String ext, String length,
                       String author) {
        this.content = content;
        this.ext = ext;
        this.filename = filename;
        this.length = length;
        this.author = author;
    }

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

    

}