package com.jeeframework.webframework.filter.util;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.RequestUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;



public class UrlUtil {
	public static String getUri(HttpServletRequest request) {
		// handle http dispatcher includes.
		String uri = (String) request
				.getAttribute("javax.servlet.include.servlet_path");

		if (uri != null) {
			return uri;
		}

		uri = RequestUtils.getServletPath(request);

		if (uri != null && !"".equals(uri)) {
			return uri;
		}

		uri = request.getRequestURI();

		return uri.substring(request.getContextPath().length());
	}

	public static ActionMapping getRestMapping(String reqUri,
			ConfigurationManager configManager) {
		ActionMapping mapping = new ActionMapping();

		if (reqUri == null) {
			return null;
		}

		parseRestNameAndNamespace(reqUri, mapping, configManager);

		if (mapping.getName() == null) {
			return null;
		}

		return mapping;
	}

	public static ActionMapping getMapping(HttpServletRequest request,
			ConfigurationManager configManager) {
		ActionMapping mapping = new ActionMapping();
		String uri = getUri(request);

		if (uri == null) {
			return null;
		}

		parseNameAndNamespace(uri, mapping, configManager);

		if (mapping.getName() == null) {
			return null;
		}

		return mapping;
	}

	public static void parseRestNameAndNamespace(String uri,
			ActionMapping mapping, ConfigurationManager configManager) {
		String namespace, name;
		int lastSlash = uri.lastIndexOf("/");

		if (lastSlash == -1) {
			namespace = "";
			name = uri;
		} else if (lastSlash == 0) {
			namespace = "/";
			name = uri.substring(lastSlash + 1);
		} else {

			Configuration config = configManager.getConfiguration();
			String prefix = uri.substring(0, lastSlash);
			namespace = "";
			for (Iterator i = config.getPackageConfigs().values().iterator(); i
					.hasNext();) {
				String ns = ((PackageConfig) i.next()).getNamespace();
				if (ns != null
						&& prefix.startsWith(ns)
						&& (prefix.length() == ns.length() || prefix.charAt(ns
								.length()) == '/')) {
					if (ns.length() > namespace.length()) {
						namespace = ns;
					}
				}
			}

			name = uri.substring(namespace.length() + 1);

		}
		lastSlash = name.lastIndexOf("/");
		if (lastSlash != -1 && (name.length() == lastSlash + 1)) {
			name = name.substring(0, name.length() - 1);
		}
		lastSlash = name.lastIndexOf("/");
		// lastSlash = name.lastIndexOf("-");
		String actionName = null;
		String methodName = null;
		if (lastSlash != -1) // url ==> ActionName-ActionMethod/
		{
			methodName = name.substring(lastSlash + 1);
			actionName = name.substring(0, lastSlash);
		} else {
			return;
		}
		mapping.setNamespace(namespace);

		if (name.indexOf(".") == -1) {
			if (actionName == null) {
				mapping.setName(name);
			} else {
				mapping.setMethod(methodName);
				mapping.setName(actionName);
			}

		}

	}

	public static void parseNameAndNamespace(String uri, ActionMapping mapping,
			ConfigurationManager configManager) {
		String namespace, name;
		int lastSlash = uri.lastIndexOf("/");

		if (lastSlash == -1) {
			namespace = "";
			name = uri;
		} else if (lastSlash == 0) {
			namespace = "/";
			name = uri.substring(lastSlash + 1);
		} else {

			Configuration config = configManager.getConfiguration();
			String prefix = uri.substring(0, lastSlash);
			namespace = "";
			for (Iterator i = config.getPackageConfigs().values().iterator(); i
					.hasNext();) {
				String ns = ((PackageConfig) i.next()).getNamespace();
				if (ns != null
						&& prefix.startsWith(ns)
						&& (prefix.length() == ns.length() || prefix.charAt(ns
								.length()) == '/')) {
					if (ns.length() > namespace.length()) {
						namespace = ns;
					}
				}
			}

			name = uri.substring(namespace.length() + 1);

		}
		lastSlash = name.lastIndexOf("/");
		if (lastSlash != -1 && (name.length() == lastSlash + 1)) {
			name = name.substring(0, name.length() - 1);
		}
		lastSlash = name.lastIndexOf("/");
		String actionName = null;
		String methodName = null;
		if (lastSlash != -1) // url ==> ActionName/ActionMethod/
		{
			methodName = name.substring(lastSlash + 1);
			actionName = name.substring(0, lastSlash);
		}
		mapping.setNamespace(namespace);

		if (name.indexOf(".") == -1) {
			if (actionName == null) {
				mapping.setName(name);
			} else {
				mapping.setMethod(methodName);
				mapping.setName(actionName);
			}

		}

	}
}
