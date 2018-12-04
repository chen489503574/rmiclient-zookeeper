package rpc.zk.loadbalance;

import java.util.List;

/**
 * Created by Chenjf on 2018/12/4.
 * 对负载均衡  进行扩展
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    /**
     * 前置校验操作
     * @param repos
     * @return
     */
    @Override
    public String selectHost(List<String> repos) {
        if(repos==null || repos.size()==0){
            return null;
        }
        if (repos.size()==1){
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(List<String>  repos);
}
