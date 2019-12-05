
package com.pig4cloud.nacos.controller;


import cn.hutool.core.lang.Dict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.alibaba.nacos.common.util.VersionUtils.VERSION;
import static com.alibaba.nacos.core.utils.SystemUtils.*;

/**
 * @author xingxuechao
 * on:2019/2/27 11:17 AM
 */
@RestController
@RequestMapping("/v1/console/server")
public class ServerStateController {

    @GetMapping("state")
    public ResponseEntity serverState() {

        Dict serverState = new Dict(3).set("standalone_mode", STANDALONE_MODE ? STANDALONE_MODE_ALONE : STANDALONE_MODE_CLUSTER)
                .set("function_mode", FUNCTION_MODE).set("version", VERSION);

        return ResponseEntity.ok().body(serverState);
    }

}
