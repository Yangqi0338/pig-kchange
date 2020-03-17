package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.pig4cloud.pig.common.core.constant.SecurityConstants.FROM;
import static com.pig4cloud.pig.common.core.constant.ServiceNameConstants.UMPS_SERVICE;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@FeignClient(contextId = "remoteLogService", value = UMPS_SERVICE)
public interface RemoteLogService {
	/**
	 * 保存日志
	 *
	 * @param sysLog 日志实体
	 * @param from   内部调用标志
	 * @return succes、false
	 */
	@PostMapping("/log")
	R<Boolean> saveLog(@RequestBody SysLog sysLog, @RequestHeader(FROM) String from);
}
