package com.personal.use.sftp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:
 * @Date: 2019-08-01 17:05
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
@Configuration
@ConfigurationProperties(prefix = "deploment.service.ftp")
public class SftpConfiguration {

    private String host;

    private int port;

    private String userName;

    private String password;

    private String start;

    private String slash = "/";

    private String protocol = "sftp";

    private Integer channelConnectedTimeout = 15000;
    private Integer sessionConnectTimeout = 15000;

    private String sessionStrictHostKeyChecking = "no";

    private int connectTimeOut = 5000;
    private String controlEncoding = "utf-8";
    private int bufferSize = 1024;
    private int fileType = 2;
    private int dataTimeout = 120000;
    private boolean useEpsvWithIpv4 = false;
    private boolean passiveMode = true;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSlash() {
        return slash;
    }

    public void setSlash(String slash) {
        this.slash = slash;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getDataTimeout() {
        return dataTimeout;
    }

    public void setDataTimeout(int dataTimeout) {
        this.dataTimeout = dataTimeout;
    }

    public boolean isUseEpsvWithIpv4() {
        return useEpsvWithIpv4;
    }

    public void setUseEpsvWithIpv4(boolean useEpsvWithIpv4) {
        this.useEpsvWithIpv4 = useEpsvWithIpv4;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getChannelConnectedTimeout() {
        return channelConnectedTimeout;
    }

    public void setChannelConnectedTimeout(Integer channelConnectedTimeout) {
        this.channelConnectedTimeout = channelConnectedTimeout;
    }

    public String getSessionStrictHostKeyChecking() {
        return sessionStrictHostKeyChecking;
    }

    public void setSessionStrictHostKeyChecking(String sessionStrictHostKeyChecking) {
        this.sessionStrictHostKeyChecking = sessionStrictHostKeyChecking;
    }

    public Integer getSessionConnectTimeout() {
        return sessionConnectTimeout;
    }

    public void setSessionConnectTimeout(Integer sessionConnectTimeout) {
        this.sessionConnectTimeout = sessionConnectTimeout;
    }
}
