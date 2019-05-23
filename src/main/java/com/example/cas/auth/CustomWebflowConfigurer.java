package com.example.cas.auth;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * 重新定义默认的web流程
 * @author yellowcong
 * 创建日期:2018/02/06
 *
 */
public class CustomWebflowConfigurer extends AbstractCasWebflowConfigurer {

    

    
    /**
	 * @param flowBuilderServices
	 * @param loginFlowDefinitionRegistry
	 * @param applicationContext
	 * @param casProperties
	 */
	public CustomWebflowConfigurer(FlowBuilderServices flowBuilderServices,
			FlowDefinitionRegistry loginFlowDefinitionRegistry, ApplicationContext applicationContext,
			CasConfigurationProperties casProperties) {
		super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
		System.out.println("CustomWebflowConfigurer...........CustomWebflowConfigurer");
		// TODO Auto-generated constructor stub
	}
	
    /**
     * 绑定输入信息
     *
     * @param flow
     */
    protected void bindCredential(Flow flow) {

		System.out.println("CustomWebflowConfigurer...........bindCredential");
        //重写绑定自定义credential
        createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordSysCredential.class);
        //登录页绑定新参数
        final ViewState state = (ViewState) flow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
        //由于用户名以及密码已经绑定，所以只需对新加系统参数绑定即可
        cfg.addBinding(new BinderConfiguration.Binding("system", null, false));
    }

	@Override
	protected void doInitialize() {
		// TODO Auto-generated method stub
		final Flow flow = getLoginFlow();
		System.out.println("CustomWebflowConfigurer...........doInitialize");
	    bindCredential(flow);
	}



	
}