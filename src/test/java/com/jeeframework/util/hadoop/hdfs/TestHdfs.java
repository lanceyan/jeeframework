/*
 * @project: com.jeeframework 
 * @package: com.jeeframework.util.hadoop.hdfs
 * @title:   TestHdfs.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.jeeframework.util.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.util.Properties;

/**
 * 描述
 *
 * @author lance
 * @version 1.0 2017-12-13 18:40
 */
public class TestHdfs {

    @Test
    public void testHdfs() throws IOException {
        System.setProperty("HADOOP_USER_NAME", "devadmin");
//        System.setProperty("HADOOP_ROOT_LOGGER", "DEBUG,console");

        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://118.190.138.155:9000");
//        conf.set("dfs.replication", "1");
//        conf.set("dfs.datanode.address","118.190.138.155:50010");
        conf.set("dfs.datanode.use.datanode.hostname", "true");
        conf.set("dfs.client.use.datanode.hostname", "true");

//            conf.set("hadoop.tmp.dir", "file:/data/software/hadoop/hadoop-2.7.2/tmp/dfs/data");
        Properties props = new Properties();
        props.load(TestHdfs.class.getClassLoader().getResourceAsStream("config/hadoop/log4j.properties"));
        PropertyConfigurator.configure(props);

        FSDataOutputStream fsDataOutputStream = null;
        FileSystem fileSystem = null;
        FileInputStream fileInputStream = null;

        try {
            File fff = new File("d:\\logs\\111.txt");

            if (!fff.exists()) {
                System.out.println("no  exist");
            }

//                fileInputStream = new FileInputStream(fff);
            fileSystem = FileSystem.get(URI.create("hdfs://118.190.138.155:9000"), conf);

            fileSystem.copyFromLocalFile(new Path("file:///D:/logs/111.txt"), new Path("/333"));


//                fsDataOutputStream = fileSystem.create(new Path("/333"));
//                IOUtils.copyBytes(fileInputStream, fsDataOutputStream, 4096, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileSystem.close();
//                    fileInputStream.close();
//                    fsDataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public static void main(String[] args) throws Exception {

        try {
//            JobConf conf = new JobConf();
//            conf.set("fs.defaultFS", "hdfs://118.190.138.155:9000");
//            String hdfsPath = "/2222";
//
//            Path path = new Path(hdfsPath);
//            FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
//            if (!fs.exists(path)) {
//                fs.mkdirs(path);
//                System.out.println("Create: " + path);
//            }
//            fs.close();


//            uploadToHdfs();
            //deleteFromHdfs();
            //getDirectoryFromHdfs();
//            appendToHdfs();
//            readFromHdfs();
        } catch (
                Exception e)

        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

        {
            System.out.println("SUCCESS");
        }

    }

    /**
     * 上传文件到HDFS上去
     */

    private static void uploadToHdfs() throws FileNotFoundException, IOException {
        System.setProperty("HADOOP_USER_NAME", "devadmin");
        String localSrc = "d:\\logs\\111.txt";
        String dst = "hdfs://118.190.138.155:9000/qq3.txt";
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        OutputStream out = fs.create(new Path(dst), new Progressable() {
            public void progress() {
                System.out.print(".");
            }
        });
        IOUtils.copyBytes(in, out, 4096, true);
    }


    /**
     * 从HDFS上读取文件
     */
    private static void readFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://192.168.0.113:9000/user/zhangzk/qq.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        FSDataInputStream hdfsInStream = fs.open(new Path(dst));

        OutputStream out = new FileOutputStream("d:/qq-hdfs.txt");
        byte[] ioBuffer = new byte[1024];
        int readLen = hdfsInStream.read(ioBuffer);

        while (-1 != readLen) {
            out.write(ioBuffer, 0, readLen);
            readLen = hdfsInStream.read(ioBuffer);
        }
        out.close();
        hdfsInStream.close();
        fs.close();
    }


    /**
     * 以append方式将内容添加到HDFS上文件的末尾;注意：文件更新，需要在hdfs-site.xml中添<property><name>dfs.append
     * .support</name><value>true</value></property>
     */
    private static void appendToHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://192.168.0.113:9000/user/zhangzk/qq.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        FSDataOutputStream out = fs.append(new Path(dst));

        int readLen = "zhangzk add by hdfs java api".getBytes().length;

        while (-1 != readLen) {
            out.write("zhangzk add by hdfs java api".getBytes(), 0, readLen);
        }
        out.close();
        fs.close();
    }


    /**
     * 从HDFS上删除文件
     */
    private static void deleteFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://192.168.0.113:9000/user/zhangzk/qq-bak.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        fs.deleteOnExit(new Path(dst));
        fs.close();
    }


    /**
     * 遍历HDFS上的文件和目录
     */
    private static void getDirectoryFromHdfs() throws FileNotFoundException, IOException {
        String dst = "hdfs://192.168.0.113:9000/user/zhangzk";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dst), conf);
        FileStatus fileList[] = fs.listStatus(new Path(dst));
        int size = fileList.length;
        for (int i = 0; i < size; i++) {
            System.out.println("name:" + fileList[i].getPath().getName() + "/t/tsize:" + fileList[i].getLen());
        }
        fs.close();
    }

}
