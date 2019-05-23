package com.example.cas.config;


import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.config.CasWebflowContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import com.example.cas.auth.CustomWebflowConfigurer;


/**
 * 
 * @author yellowcong
 * 创建日期:2018/02/06
 *
 */
@Configuration("customerAuthWebflowConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@AutoConfigureBefore(value = CasWebflowContextConfiguration.class)
public class CustomerAuthWebflowConfiguration {
    @Autowired
    @Qualifier("logoutFlowRegistry")
    private FlowDefinitionRegistry logoutFlowRegistry;
    @Autowired
    @Qualifier("loginFlowRegistry")
    private FlowDefinitionRegistry loginFlowRegistry;

    @Autowired
    @Qualifier("builder")
    private FlowBuilderServices builder;
    @Autowired
    private CasConfigurationProperties casProperties;
    @Autowired
    private ApplicationContext applicationContext;

    //注册到springboot中
    @Bean
    public CasWebflowConfigurer customWebflowConfigurer() {
        final CustomWebflowConfigurer c = new CustomWebflowConfigurer(builder, loginFlowRegistry, applicationContext, casProperties);
        //c.setLogoutFlowDefinitionRegistry(logoutFlowRegistry);//5.1
        //初期化
        c.initialize();//5.2 5.1与5.2的区别，好坑呐
        System.out.println("CustomerAuthWebflowConfiguration........................customWebflowConfigurer");
        return c;
    }
}
