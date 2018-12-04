package rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import rpc.zk.loadbalance.LoadBalance;
import rpc.zk.loadbalance.RandomLoadBalance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenjf on 2018/12/4.
 */
public class ServiceDiscoveryImpl implements IServiceDiscovery {

    List<String>  repos = new ArrayList<String>();

    private String address;

    private CuratorFramework curatorFramework;

    public ServiceDiscoveryImpl(String address) {
        this.address = address;
        /**
         * 创建  CuratorFramework  设置初始化参数
         */
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10))
                .build();
        curatorFramework.start();
    }

    public String discover(String serviceName) {
        String path = Zkconfig.ZK_REGISTER_PATH + "/"+serviceName;//拼接服务节点
        try {
            repos = curatorFramework.getChildren().forPath(path);

        } catch (Exception e) {
            throw new RuntimeException("获取子节点异常"+e );
        }
        //动态发现服务节点的变化，需要监听
        registerWatcher(path);

        //负载均衡机制，做一个简单的负载均衡，这里采用的是随机负载
        LoadBalance loadBalance = new RandomLoadBalance();

        return loadBalance.selectHost(repos);//返回调用的服务地址

    }

    private void registerWatcher(final String path){
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PatchChild  Watcher  异常"+e);
        }
    }
}
