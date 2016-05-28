package com.jeeframework.logicframework.integration.dao.datasource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.jeeframework.util.encrypt.EncryptUtil;


/**
 * 数据源访问层
 */
public class ConfigDataSource extends BasicDataSource {
    private boolean isConfig = false;

    private boolean encrypt = false;// 默认不加密

    private String encryptPass = null;// 默认的加密密码

    public boolean getIsConfig() {
        return isConfig;
    }

    public void setIsConfig(boolean isConfig) {
        this.isConfig = isConfig;
    }


    @Override
    public synchronized void setPassword(String passwd) {
        if (!isConfig) {
            if (encrypt) {
                passwd = EncryptUtil.desDecrypt(encryptPass, passwd);
            }

            super.setPassword(passwd);
        } else {
            System.out.println("in false");
        }

    }

    @Override
    public synchronized void setUrl(String url) {
        if (!isConfig) {
            super.setUrl(url);
        } else {
            System.out.println("in false");
        }
    }


    @Override
    public synchronized void setUsername(String userName) {
        if (!isConfig) {
            super.setUsername(userName);
        } else {
            System.out.println("in false");
        }
    }

    public String getEncryptPass() {
        return encryptPass;
    }

    public void setEncryptPass(String encryptPass) {
        this.encryptPass = encryptPass;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public void setConfig(boolean isConfig) {
        this.isConfig = isConfig;
    }

}
