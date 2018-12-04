package rpc.zk.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * Created by Chenjf on 2018/12/4.
 *  负载均衡器包括：随机负载，权重负载，轮询负载等等（不同的负载均衡器有不同的算法）
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> repos) {
        int len = repos.size();
        Random random = new Random();

        return repos.get(random.nextInt(len));//获取repos长度范围内的一个随机地址
}
}
