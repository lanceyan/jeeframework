package com.jeeframework.logicframework.integration.sao.hdfs;

import com.jeeframework.testframework.AbstractSpringBaseTestNoTransaction;
import com.jeeframework.util.io.FileUtils;
import com.jeeframework.util.validate.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * Hdfs测试
 *
 * @author lance
 * @version 1.0 2017-12-15 14:56
 */
public class BaseSaoHDFSTest extends AbstractSpringBaseTestNoTransaction {


    @Autowired
    BaseSaoHDFS baseSaoHDFS;

    @Test
    public void upload() throws Exception {
        baseSaoHDFS.uploadFile("d:\\logs\\111.txt", "/1111");

    }

    @Test
    public void upload1() throws Exception {
        File file = new File("d:\\logs\\111.txt");
        byte[] bytes = FileUtils.readFileToByteArray(file);

        baseSaoHDFS.uploadFile(bytes, "/555");
    }

    @Test
    public void downLoad() throws Exception {
        byte[] fileBytes = baseSaoHDFS.downloadFile("/555");
        System.out.println(new String(fileBytes, "GBK"));
    }

    @Test
    public void downLoad1() throws Exception {
        baseSaoHDFS.downloadFile("/444", "d:\\logs\\444.txt");
    }

    @Test
    public void mkdirs() throws Exception {
        baseSaoHDFS.mkDirs("/111222333");
    }

    @Test
    public void getFiles() throws Exception {
        List<String> fileList = baseSaoHDFS.listFiles("/");
        for (String file : fileList) {
            System.out.println(file);
        }
    }

    @Test
    public void existsFile() throws Exception {
        boolean result = baseSaoHDFS.existsFile("/555");
        Assert.isTrue(result);
    }

    @Test
    public void renameFile() throws Exception {
        baseSaoHDFS.renameFile("/111", "/5556");
    }

    @Test
    public void deleteFile() throws Exception {

        baseSaoHDFS.deleteFile("/111", true);
    }

    @Test
    public void appendFile() throws Exception {
        byte[] bytes = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes();
        baseSaoHDFS.appendFile(bytes, "/111");
    }

}