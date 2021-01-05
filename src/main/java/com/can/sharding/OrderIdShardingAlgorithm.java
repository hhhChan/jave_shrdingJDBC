package com.can.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;
import java.util.Iterator;

public class OrderIdShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Iterator<String> ite = availableTargetNames.iterator();
        String target = ite.next();
        Long id = shardingValue.getValue();
        if (id % 2 == 1) {
            target = ite.next();
        }
        return target;
    }
}
