package com.pig4cloud.pig.common.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.security.exception.PigAuth2Exception;
import lombok.SneakyThrows;

/**
 * @author lengleng
 * @date 2019/2/1
 * <p>
 * OAuth2 异常格式化
 */
public class PigAuth2ExceptionSerializer extends StdSerializer<PigAuth2Exception> {
	public PigAuth2ExceptionSerializer() {
		super(PigAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(PigAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", CommonConstants.FAIL);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}
