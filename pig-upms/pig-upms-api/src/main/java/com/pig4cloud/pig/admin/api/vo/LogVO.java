package com.pig4cloud.pig.admin.api.vo;

import com.pig4cloud.pig.admin.api.entity.SysLog;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Data
public class LogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private SysLog sysLog;
	private String username;
}
