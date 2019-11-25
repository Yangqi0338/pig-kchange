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
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WriteTxExceptionDTO implements Serializable {

    private static final long serialVersionUID = 633490068348052197L;
    private String groupId;
    private String unitId;
    private String modId;
    private Integer transactionState;
    private Short registrar;
    private String remark;
}
