package rpc.zk.loadbalance;

import java.util.List;

/**
 * Created by Chenjf on 2018/12/4.
 * 负载均衡接口
 */
public interface LoadBalance {
    /**
     * 这个方法做一些前置操作，比如说校验
     * @param repos
     * @return
     */
    String selectHost(List<String> repos);
}
