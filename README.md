# FastDFSManager
文件服务器连接池实现——TrackerServer连接池

文件服务器配置：resources文件夹下的fastdfs_client.conf

将TrackerServer的连接以连接池的形式实现。

使用的时候，先初始化ConnectionPool类，然后从中取出一个连接，获取StorageClient，然后在通过API提供的方法进行文件的上传和下载

FileManager类中提供了上传、下载的方法。

FastDFSUtil中封装了File形式的文件上传，其他待后续优化。

注：这个工具类打印了详细的日志，具体参见FastDFSLogger,通过修改日志的实现，可以定制需要的日志打印方式。
