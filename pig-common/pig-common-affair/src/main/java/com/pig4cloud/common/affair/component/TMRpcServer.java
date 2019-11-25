/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pig4cloud.common.affair.component;

import com.codingapi.txlcn.common.runner.TxLcnInitializer;
import com.codingapi.txlcn.common.runner.TxLcnRunnerOrders;
import com.codingapi.txlcn.txmsg.RpcConfig;
import com.codingapi.txlcn.txmsg.RpcServerInitializer;
import com.codingapi.txlcn.txmsg.dto.ManagerProperties;
import com.pig4cloud.common.affair.properties.TxManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Component
public class TMRpcServer implements TxLcnInitializer {

    private final TxManagerProperties txManagerConfig;

    private final RpcServerInitializer rpcServerInitializer;

    private final RpcConfig rpcConfig;

    @Autowired
    public TMRpcServer(TxManagerProperties txManagerConfig, RpcServerInitializer rpcServerInitializer, RpcConfig rpcConfig) {
        this.txManagerConfig = txManagerConfig;
        this.rpcServerInitializer = rpcServerInitializer;
        this.rpcConfig = rpcConfig;
    }

    @Override
    public void init() {
        // 1. 配置
        if (rpcConfig.getWaitTime() <= 5) {
            rpcConfig.setWaitTime(1000);
        }
        if (rpcConfig.getAttrDelayTime() < 0) {
            rpcConfig.setAttrDelayTime(txManagerConfig.getDtxTime());
        }

        // 2. 初始化RPC Server
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setCheckTime(txManagerConfig.getHeartTime());
        managerProperties.setRpcPort(txManagerConfig.getPort());
        managerProperties.setRpcHost(txManagerConfig.getHost());
        rpcServerInitializer.init(managerProperties);
    }

    @Override
    public int order() {
        return TxLcnRunnerOrders.MAX;
    }
}
