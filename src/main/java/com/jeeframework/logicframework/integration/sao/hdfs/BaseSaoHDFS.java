/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.logicframework.integration.sao.hdfs
 * @title:   HdfsSao.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.logicframework.integration.sao.hdfs;

import com.jeeframework.logicframework.integration.sao.BaseSAO;
import com.jeeframework.logicframework.integration.sao.SAOException;
import com.jeeframework.util.validate.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * hdfs基础服务访问对象
 *
 * @author lance
 * @version 1.0 2017-12-15 11:35
 */
public class BaseSaoHDFS extends BaseSAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    Configuration configuration = new Configuration();
    private String hdfsAddress; //hdfs 服务器地址
    private String userName; //hadoop执行用户

    /**
     * 上传文件字节数组到远程文件路径
     *
     * @param fileBytes  文件字节内容
     * @param hadoopPath 远程hdfs文件路径
     * @throws SAOException
     */
    public void uploadFile(byte[] fileBytes, String hadoopPath) throws SAOException {


        FileSystem fileSystem = null;
        FSDataOutputStream fsDataOutputStream = null;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);

            Path path = new Path(hadoopPath);
            fsDataOutputStream = fileSystem.create(path);
            fsDataOutputStream.write(fileBytes);
            fsDataOutputStream.close();
        } catch (IOException e) {
            throw new SAOException("上传到远程文件  " + hadoopPath + " 出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
                if (fsDataOutputStream != null) {
                    fsDataOutputStream.close();
                }
            } catch (IOException e) {
                throw new SAOException("上传后关闭文件流出错。 ", e);
            } finally {
            }
        }
    }

    /**
     * 上传本地文件到远程文件
     *
     * @param localPath  本地文件路径
     * @param hadoopPath 远程文件路径
     */
    public void uploadFile(String localPath, String hadoopPath) throws SAOException {

        FSDataOutputStream fsDataOutputStream = null;
        FileSystem fileSystem = null;
        FileInputStream fileInputStream = null;

        try {
            File localPathFile = new File(localPath);
            if (!localPathFile.exists()) {
                throw new FileNotFoundException(localPath + "  本地文件不存在");
            }
            fileInputStream = new FileInputStream(localPath);
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);
            fsDataOutputStream = fileSystem.create(new Path(hadoopPath));
            IOUtils.copyBytes(fileInputStream, fsDataOutputStream, 4096, false);
        } catch (IOException e) {
            throw new SAOException(localPath + "上传到远程文件  " + hadoopPath + " 出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fsDataOutputStream != null) {
                    fsDataOutputStream.close();
                }
            } catch (IOException e) {
                throw new SAOException("上传后关闭文件流出错。 ", e);
            } finally {
            }
        }
    }

    /**
     * 下载远程文件路径到本地文件路径
     *
     * @param hadoopPath 远程文件路径
     * @param localPath  本地文件路径
     */
    public void downloadFile(String hadoopPath, String localPath) throws SAOException {
        FileSystem fs = null;
        FSDataInputStream in = null;
        OutputStream out = null;
        try {
            fs = FileSystem.get(URI.create(hdfsAddress), configuration);
            Path fileName2 = new Path(hadoopPath);
            checkFileExists(fs, fileName2);
            in = fs.open(fileName2);
            out = new BufferedOutputStream(new FileOutputStream(localPath));
            IOUtils.copyBytes(in, out, 1024, true);
        } catch (IOException e) {
            throw new SAOException("下载远程文件  " + hadoopPath + " 出错。 ", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (Exception e) {
                throw new SAOException("下载远程文件后关闭文件流  " + hadoopPath + " 出错。 ", e);
            }
        }
    }

    /**
     * 下载远程文件，返回文件字节内容
     *
     * @param hadoopPath 远程文件路径
     * @return
     */
    public byte[] downloadFile(String hadoopPath) throws SAOException {
        FileSystem fs = null;
        FSDataInputStream in = null;
        ByteArrayOutputStream out = null;
        byte[] retBytes;
        try {
            fs = FileSystem.get(URI.create(hdfsAddress), configuration);
            Path fileName2 = new Path(hadoopPath);
            checkFileExists(fs, fileName2);
            in = fs.open(fileName2);
            out = new ByteArrayOutputStream();
            IOUtils.copyBytes(in, out, 1024, true);
            retBytes = out.toByteArray();
        } catch (IOException e) {
            throw new SAOException("下载远程文件  " + hadoopPath + " 出错。 ", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (Exception e) {
                throw new SAOException("下载远程文件后关闭文件流  " + hadoopPath + " 出错。 ", e);
            }
        }
        return retBytes;
    }

    /**
     * 创建远程文件夹路径
     *
     * @param hadoopPath 远程文件夹路径
     * @return
     */
    public boolean mkDirs(String hadoopPath) {
        FileSystem fileSystem = null;
        boolean result = false;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);
            result = fileSystem.mkdirs(new Path(hadoopPath));
        } catch (IOException e) {
            throw new SAOException("创建目录出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                throw new SAOException("创建目录关闭文件流出错。 ", e);
            } finally {
            }
            return result;
        }
    }

    /**
     * 从远程服务器文件夹下获取文件列表
     *
     * @param hadoopFolderPath 远程文件夹路径
     * @return
     * @throws IOException
     */
    public List<String> listFiles(String hadoopFolderPath) throws SAOException {
        Path path = new Path(hadoopFolderPath);
        FileSystem fileSystem = null;
        List<String> paths = new ArrayList<>();
        try {
            fileSystem = FileSystem.get(URI.create(this.hdfsAddress), configuration);
            checkFileExists(fileSystem, path);

            if (fileSystem.exists(path)) {
                FileStatus fileStatusFolder = fileSystem.getFileStatus(path);
                if (!fileStatusFolder.isDirectory()) {
                    throw new SAOException("listFiles  " + hadoopFolderPath + " 不是目录。 ");
                }
                FileStatus[] fileStatus = fileSystem.listStatus(path);
                for (int i = 0; i < fileStatus.length; i++) {
                    FileStatus fileStatu = fileStatus[i];
                    Path oneFilePath = fileStatu.getPath();
                    if (fileStatu.isFile() || fileStatu.isSymlink()) {//只要文件
                        paths.add(oneFilePath.toUri().getPath());
                    } else {
                        paths.add(oneFilePath.toUri().getPath() + Path.SEPARATOR);
                    }
                }
            }
        } catch (IOException e) {
            throw new SAOException("listFiles  " + hadoopFolderPath + " 出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                throw new SAOException("listFiles关闭文件流出错。 ", e);
            } finally {
            }
        }
        return paths;
    }

    /**
     * 判断远程文件是否存在
     *
     * @param hadoopPath
     * @return
     */
    public boolean existsFile(String hadoopPath) throws SAOException {
        FileSystem fileSystem = null;
        boolean result = false;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);
            result = fileSystem.exists(new Path(hadoopPath));
        } catch (IOException e) {
            throw new SAOException("检查文件存在出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                throw new SAOException("检查文件存在关闭文件流出错。 ", e);
            } finally {
            }
            return result;
        }
    }


    /**
     * 重命名远程文件
     *
     * @param oldHadoopFile 旧的hadoop上文件
     * @param newHadoopFile 新的hadoop文件
     * @return
     */
    public boolean renameFile(String oldHadoopFile, String newHadoopFile) throws SAOException {

        FileSystem fileSystem = null;
        boolean result = false;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);
            Path oldHadoopFilePath = new Path(oldHadoopFile);

            checkFileExists(fileSystem, oldHadoopFilePath);

            Path newHadoopFilePath = new Path(newHadoopFile);
            if (fileSystem.exists(newHadoopFilePath)) {
                throw new SAOException("renameFile 存在出错 ， " + newHadoopFilePath + " 已经存在。 ");
            }

            result = fileSystem.rename(oldHadoopFilePath, newHadoopFilePath);
        } catch (IOException e) {
            throw new SAOException("renameFile 存在出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                throw new SAOException(" renameFile 关闭文件流出错。 ", e);
            } finally {
            }
            return result;
        }
    }


    /**
     * 删除远程文件
     *
     * @param hadoopPath 远程文件路径
     * @param recursive  是否递归删除文件
     * @return
     */
    public boolean deleteFile(String hadoopPath, boolean recursive) throws SAOException {
        FileSystem fileSystem = null;
        boolean result = false;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);
            Path hadoopPathTmp = new Path(hadoopPath);
            checkFileExists(fileSystem, hadoopPathTmp);
            result = fileSystem.delete(new Path(hadoopPath), recursive);
        } catch (IOException e) {
            throw new SAOException("deleteFile 存在出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
            } catch (IOException e) {
                throw new SAOException(" renameFile 关闭文件流出错。 ", e);
            } finally {
            }
            return result;
        }
    }

    /**
     * 追加字节到文件上
     *
     * @param fileBytes  追加的内容
     * @param hadoopPath 远程hadoop文件路径
     * @throws SAOException
     */
    public void appendFile(byte[] fileBytes, String hadoopPath) throws SAOException {
        FileSystem fileSystem = null;
        FSDataOutputStream fsDataOutputStream = null;
        try {
            fileSystem = FileSystem.get(URI.create(hdfsAddress), configuration);

            Path path = new Path(hadoopPath);
            fsDataOutputStream = fileSystem.append(path);
            fsDataOutputStream.write(fileBytes);
            fsDataOutputStream.close();
        } catch (IOException e) {
            throw new SAOException("append 到远程文件  " + hadoopPath + " 出错。 ", e);
        } finally {
            try {
                if (fileSystem != null) {
                    fileSystem.close();
                }
                if (fsDataOutputStream != null) {
                    fsDataOutputStream.close();
                }
            } catch (IOException e) {
                throw new SAOException("append 到远程文件后关闭文件流出错。 ", e);
            } finally {
            }
        }
    }


    /**
     * Check if a file exists, if not will throw a FileNotFoundException
     *
     * @param path The path of the file to check
     * @throws IOException
     */
    private void checkFileExists(FileSystem fileSystem, Path path) throws IOException {

        // Check file exists
        if (!fileSystem.exists(path)) {
            logger.error("Path '" + path.toString() + "' does not exist.");
            fileSystem.close();
            throw new FileNotFoundException("Path '" + path.toString() + "' does not exist.");
        }
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration.set("dfs.datanode.use.datanode.hostname", "true");
        configuration.set("dfs.client.use.datanode.hostname", "true");

        if (!Validate.isEmpty(this.userName)) {
            System.setProperty("HADOOP_USER_NAME", userName);
        }

    }

    public void setHdfsAddress(String hdfsAddress) {
        this.hdfsAddress = hdfsAddress;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }
}
