package com.jeeframework.webframework.action.validation;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;
//import com.opensymphony.xwork2.validator.ActionValidatorManagerFactory;
import com.opensymphony.xwork2.validator.FieldValidator;
import com.opensymphony.xwork2.validator.Validator;
import com.jeeframework.webframework.action.BaseAction;

import freemarker.template.Template;

/** 
 * Ĭ�ϵ�javascriptУ��
 * @author billwu�������޸��ߣ�
 * @version 1.0���°汾�ţ�
 * 
 * @version 1.1 ����Ĭ�ϵ�У�鷽�� lanceyan��У����ǰ��js��
 */

public class GenJavascriptAction extends BaseAction
{
	private String	actionName;

	public String getActionName()
	{
		return actionName;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	
	public int getObjCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	protected Configuration	configuration;

	@Inject
	public void setConfiguration(Configuration configuration)
	{
		// ������struts���ȡ�õ�configuration�����ģ�ֱ�Ӵӻ�����ע�������
		this.configuration = configuration;
	}

	public String execute() throws Exception
	{
		// ����һ��action����html�������á�ֻ��actionName��actionClass��struts�����ļ��ж�ȡ��
		if (null != actionName && !actionName.trim().equals(""))
		{
			freemarker.template.Configuration cfg = new freemarker.template.Configuration();

			cfg.setClassForTemplateLoading(this.getClass(), "");
			Map<String, Object> data = new HashMap<String, Object>();
			Template t = cfg.getTemplate("JavaScript_ActionValidateTemplate.ftl");
			ActionPO actionPO = parseParameter(actionName);

			// ȡ��ActionConfig���������Դ���ȡ���������������
			ActionConfig actionConfig = configuration.getRuntimeConfiguration().getActionConfig(actionPO.getPackageName(), actionPO.getActionNameParam());

			if (actionConfig != null)
			{
				actionPO.setActionClassName(actionConfig.getClassName());
				List<Validator> tag = getValidators(Class.forName(actionPO.getActionClassName()), actionPO.getActionNameWithMethod());
				data.put("tag", tag);
			}

			data.put("actionAlias", actionPO.getActionAlias());
			t.process(data, new OutputStreamWriter(getResponse().getOutputStream()));

		}
		return null;
	}

	private ActionPO parseParameter(String sourceParameter)
	{
		String parameter = null;

		ActionPO actionPO = new ActionPO();

		if (sourceParameter.indexOf(".") > 0)
		{
			parameter = sourceParameter.substring(0, sourceParameter.indexOf("."));
		}
		else
		{
			parameter = sourceParameter;
		}
		if (parameter.indexOf("/") != parameter.lastIndexOf("/"))
		{
			actionPO.setPackageName(parameter.substring(0, parameter.lastIndexOf("/")));
			actionPO.setActionNameWithMethod(parameter.substring(parameter.lastIndexOf("/") + 1));
		}
		else if (parameter.indexOf("/") == 0)
		{
			actionPO.setPackageName("/");
			actionPO.setActionNameWithMethod(parameter.substring(1));
		}
		else
		{
			actionPO.setPackageName("/");
			actionPO.setActionNameWithMethod(parameter);
		}

		int maohaoIndex = actionPO.getActionNameWithMethod().indexOf("!");
		int xiehuaxianIndex = actionPO.getActionNameWithMethod().indexOf("_");
		if (maohaoIndex > 0 || xiehuaxianIndex > 0)
		{
			actionPO.setActionNameParam(actionPO.getActionNameWithMethod().substring(0, (maohaoIndex > 0 ? maohaoIndex : xiehuaxianIndex)));
			if (maohaoIndex > 0)
			{
				actionPO.setActionAlias(actionPO.getActionNameParam());
			}
			else
			{
				actionPO.setActionAlias(actionPO.getActionNameWithMethod());
			}
		}
		else
		{
			actionPO.setActionNameParam(actionPO.getActionNameWithMethod());
			actionPO.setActionAlias(actionPO.getActionNameParam());
		}

		return actionPO;
	}

	private List<Validator> getValidators(Class actionClass, String actionName)
	{
		List<Validator> all = null;
		try
		{
//			all = ActionValidatorManagerFactory.getInstance().getValidators(actionClass, actionName);
		}
		catch (Exception e)
		{
			all = new ArrayList<Validator>();
		}
		
		List<Validator> validators = new ArrayList<Validator>();
		for (Validator validator : all)
		{
			if (validator instanceof FieldValidator)
			{
				FieldValidator fieldValidator = (FieldValidator) validator;

				validators.add(fieldValidator);
			}
		}

		return validators;
	}

	// ���ڲ��������ݲ�����
	private class ActionPO
	{
		String	actionNameParam				= null;

		String	packageName						= null;

		String	actionNameWithMethod	= null;

		String	actionAlias						= null;

		String	actionClassName				= null;

		public String getActionNameParam()
		{
			return actionNameParam;
		}

		public void setActionNameParam(String actionNameParam)
		{
			this.actionNameParam = actionNameParam;
		}

		public String getPackageName()
		{
			return packageName;
		}

		public void setPackageName(String packageName)
		{
			this.packageName = packageName;
		}

		public String getActionNameWithMethod()
		{
			return actionNameWithMethod;
		}

		public void setActionNameWithMethod(String actionNameWithMethod)
		{
			this.actionNameWithMethod = actionNameWithMethod;
		}

		public String getActionAlias()
		{
			return actionAlias;
		}

		public void setActionAlias(String actionAlias)
		{
			this.actionAlias = actionAlias;
		}

		public String getActionClassName()
		{
			return actionClassName;
		}

		public void setActionClassName(String actionClassName)
		{
			this.actionClassName = actionClassName;
		}
	}
}
