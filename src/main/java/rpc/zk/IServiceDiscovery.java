package rpc.zk;

/**
 * Created by Chenjf on 2018/12/4.
 * 发现服务
 */
public interface IServiceDiscovery {

    /**
     * 根据请求的服务名称，获得对应的调用地址
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
