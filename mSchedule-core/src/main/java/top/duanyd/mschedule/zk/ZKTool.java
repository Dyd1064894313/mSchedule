package top.duanyd.mschedule.zk;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.zookeeper.CreateMode;

/**
 * @Author: duanyandong
 * @Date: 2018/8/23 20:24
 * @Description:
 */
public class ZKTool {

    public static void createPath(CuratorFramework zkClinet, String path, CreateMode createMode, ACLProvider aclProvider) throws Exception {
        if(StringUtils.isBlank(path)){
            return;
        }
        String[] paths = path.split("/");
        if(createMode.equals(CreateMode.EPHEMERAL)){
            zkClinet.create().creatingParentsIfNeeded().withMode(createMode).withACL(aclProvider.getDefaultAcl()).forPath(path);
        }
    }
}
