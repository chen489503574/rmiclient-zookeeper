package rpc;

import rpc.zk.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Chenjf on 2018/11/30.
 *  发起客户端和服务端的远程调用，处理对应请求的Handler
 */
public class RemoteInvocationHandler implements InvocationHandler {
    private IServiceDiscovery serviceDiscovery;

    private String vsersion;

    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery ,String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.vsersion = version;
    }

    //直接发起跟 远程服务进行调用
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(vsersion);

        String serviceAddress  = serviceDiscovery.discover(rpcRequest.getClassName());//根据接口名称得到对应的服务地址
        TCPTransport tcpTransport = new TCPTransport(serviceAddress);
        Object result = tcpTransport.send(rpcRequest);
        return result;
    }
}
