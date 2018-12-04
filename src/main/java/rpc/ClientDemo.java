package rpc;

import rpc.zk.IServiceDiscovery;
import rpc.zk.ServiceDiscoveryImpl;
import rpc.zk.Zkconfig;

/**
 * Created by Chenjf on 2018/11/30.
 */
public class ClientDemo {
    public static void main(String[] args) throws InterruptedException {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(Zkconfig.CONNNECTION_STR);//指定连接哪个注册中心
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);
        for (int i=0;i<10;i++){
            IGpHello iGpHello = rpcClientProxy.clientProxy(IGpHello.class,null);
            System.out.println(iGpHello.sayHi("佳阳"));
            Thread.sleep(1000);
        }
        //下面的是单节点服务的演示示例
//        IGpHello iGpHello = rpcClientProxy.clientProxy(IGpHello.class,null);
//
//        System.out.println(iGpHello.sayHi("佳阳"));
    }
}
