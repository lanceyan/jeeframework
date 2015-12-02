package com.jeeframework.webframework.remoting.hessian;

import org.springframework.remoting.caucho.HessianServiceExporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 封装 hessian 远程调用接口
 * @author lance
 */
public class HessianServiceExporterWrapper extends HessianServiceExporter {

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			super.handleRequest(request, response);
			
			
		} finally {
		}
	}
}
