/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.yarn.boot.actuate.endpoint.mvc;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.yarn.boot.actuate.endpoint.YarnContainerRegisterEndpoint;
import org.springframework.yarn.boot.actuate.endpoint.mvc.domain.ContainerRegisterResource;

/**
 * A custom {@link MvcEndpoint} adding specific rest API used to
 * handle graceful YARN application shutdown.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnContainerRegisterMvcEndpoint extends EndpointMvcAdapter {

	private final YarnContainerRegisterEndpoint delegate;

	/**
	 * Instantiates a new yarn container register mvc endpoint.
	 *
	 * @param delegate the delegate endpoint
	 */
	public YarnContainerRegisterMvcEndpoint(YarnContainerRegisterEndpoint delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@Override
	public Object invoke() {
		if (!getDelegate().isEnabled()) {
			return new ResponseEntity<Map<String, String>>(Collections.singletonMap(
					"message", "This endpoint is disabled"), HttpStatus.NOT_FOUND);
		}
		return super.invoke();
	}

	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<Void> register(@RequestBody ContainerRegisterResource request) {
		delegate.register(request.getContainerId(), request.getTrackUrl());
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<Void>(responseHeaders, HttpStatus.ACCEPTED);
	}

}
