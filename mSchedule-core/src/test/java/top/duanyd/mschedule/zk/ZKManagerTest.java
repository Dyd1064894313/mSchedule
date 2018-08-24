package top.duanyd.mschedule.zk;

import java.util.Properties;

/**
 * @Author: duanyandong
 * @Date: 2018/8/23 19:35
 * @Description:
 */
public class ZKManagerTest {

    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        p.setProperty(ZKManager.keys.zkConnectString.toString(), "127.0.0.1:2181");
        p.setProperty(ZKManager.keys.rootPath.toString(), "lottery");
        p.setProperty(ZKManager.keys.userName.toString(), "");
        p.setProperty(ZKManager.keys.password.toString(), "");
        p.setProperty(ZKManager.keys.zkSessionTimeout.toString(), "1000");
        p.setProperty(ZKManager.keys.isCheckParentPath.toString(), "true");
        ZKManager zkManager = new ZKManager(p);
    }
}
