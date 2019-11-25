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

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DTXInfo implements Serializable {

    private static final long serialVersionUID = 6311351333740021064L;

    /**
     * 本周处理事务数量
     */
    private long dtxCount;

    /**
     * 本周失败的事务数量
     */
    private int errorDtxCount;

    /**
     * 今天事务数量
     */
    private int todayDtxCount;

    /**
     * 今天失败的事务数量
     */
    private int todayErrorDtxCount;

}
