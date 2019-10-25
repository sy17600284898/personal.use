package com.personal.use.sftp;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * SFTPUtil
 *
 * @author: shiyan
 * @version: 2019-10-16 15:39
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
@Configuration
public class SftpClientFactory {

    // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

    @Autowired
    private SftpConfiguration config;

    /**
     * 创建SFTP连接
     *
     * @return
     * @throws Exception
     */
    public ChannelSftp createSftp() throws Exception {
        JSch jsch = new JSch();
        System.out.println("Try to connect sftp[" + config.getUserName() + "@" + config.getHost() + "], use password[" + config.getPassword() + "]");

        Session session = createSession(jsch, config.getHost(), config.getUserName(), config.getPort());
        session.setPassword(config.getPassword());
        session.connect(config.getSessionConnectTimeout());

        System.out.println("Session connected to {}." + config.getHost());

        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());

        System.out.println("Channel created to {}." + config.getHost());
        return (ChannelSftp) channel;
    }


    /**
     * 创建session
     *
     * @param jsch
     * @param host
     * @param username
     * @param port
     * @return
     * @throws Exception
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws Exception {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }

        if (session == null) {
            throw new Exception(host + " session is null");
        }

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, config.getSessionStrictHostKeyChecking());
        return session;
    }

    /**
     * 关闭连接
     *
     * @param sftp
     */
    public void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    System.out.println("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
