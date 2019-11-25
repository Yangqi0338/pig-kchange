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
package com.pig4cloud.common.affair.vo;


import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lorne
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TransactionCmd implements Serializable {

    private static final long serialVersionUID = -5896108547143153343L;

    /**
     * 业务状态
     */
    private LCNCmdType type;

    /**
     * 请求唯一标识
     */
    private String requestKey;

    /**
     * 事务组id
     */
    private String groupId;

    /**
     * TxClient标识键
     */
    private String remoteKey;

    /**
     * 通讯数据
     */
    private MessageDto msg;

}
