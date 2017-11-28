package com.yangchd.util;

import java.io.Serializable;

/**
 * Created by yangchd on 2017/8/23.
 * Serializable 序列化
 * 存放跟FastDFS有关的文件上传和下载的参数
 */
public interface FileManagerConfig extends Serializable {

    String FILE_DEFAULT_AUTHOR = "YangCHD";

    String PROTOCOL = "http://";

    String SEPARATOR = "/";

    String TRACKER_NGNIX_ADDR = "10.168.71.148";

}