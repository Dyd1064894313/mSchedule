package top.duanyd.mschedule.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: duanyandong
 * @Date: 2018/8/3 18:06
 * @Description:
 */
public class ZKManager {

    private static transient Logger logger = LoggerFactory.getLogger(ZKManager.class);
    private Properties properties;
    private CuratorFramework client;
    private ACLProvider aclProvider;
    private String version = "mSchedule-version:1.0.0";

    public static String NAME_SPACE = "mSchedule";

    public ZKManager(Properties properties) throws Exception {
        this.properties = properties;
        logger.info("连接zookeeper……");
        this.connect();
    }

    private void connect() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        createZookeeper(connectionLatch);
        connectionLatch.await(30,TimeUnit.SECONDS);
    }

    private void createZookeeper(final CountDownLatch countDownLatch){
        final String authString = this.properties.getProperty(keys.userName.toString())
                + ":"+ this.properties.getProperty(keys.password.toString());
        aclProvider = new ACLProvider() {
            private List<ACL> acl;
            @Override
            public List<ACL> getDefaultAcl() {
                if(acl == null){
                    acl = new ArrayList<ACL>();
                    acl.add(new ACL(ZooDefs.Perms.ALL, new Id("auth", authString)));
                    acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
                }
                return acl;
            }

            @Override
            public List<ACL> getAclForPath(String s) {
                return acl;
            }
        };
        String connectString = this.properties.getProperty(keys.zkConnectString.toString());
        int connectionTimeoutMs = Integer.parseInt(this.properties
                .getProperty(keys.zkSessionTimeout.toString()));

        client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).
                authorization("digest", authString.getBytes()).connectionTimeoutMs(connectionTimeoutMs).
                connectString(connectString).namespace(NAME_SPACE).
                retryPolicy(new RetryNTimes(10, 1000)).build();
        client.start();
        logger.info("连接成功！");
    }

    public synchronized void reConnect() throws Exception {
        logger.info("重新连接zookeeper……");
        if(client != null){
            client.close();
        }
        client = null;
        this.connect();
    }

    public synchronized void close(){
        logger.info("关闭zookeeper连接");
        if(client != null){
            client.close();
        }
    }

    public enum keys {
        zkConnectString, rootPath, userName, password, zkSessionTimeout, isCheckParentPath
    }

    public ACLProvider getAclProvider(){
        return aclProvider;
    }

    public CuratorFramework getZKClient() throws Exception {
        if(client == null){
            this.connect();
        }
        return this.client;
    }

    public String getRootPath(){
        return properties.getProperty(keys.rootPath.toString(), "");
    }

    public void initRoot() throws Exception {
        if(client == null){
            this.connect();
        }
        String rootPath = getRootPath();
        if(client.checkExists().forPath(rootPath) == null){

        }
    }
}
