/*
package com.kyrylomyronov.exception.error;

import java.util.Map;

import com.kyrylomyronov.exception.ApiException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

*/
/**
 * Will add the CODE field to the exception.
 *//*

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest requestAttributes,
												  boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
		Throwable error = getError(requestAttributes);
		if (error instanceof ApiException) {
			errorAttributes.put("CODE", ((ApiException) error).getCode());
		}
		return errorAttributes;
	}
}
*/
