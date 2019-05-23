package com.example.cas.auth;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;
import javax.validation.constraints.Size;

/**
 * 用户名，密码，系统
 * @author yellowcong
 * 创建日期:2018/02/06
 *
 */
public class UsernamePasswordSysCredential extends RememberMeUsernamePasswordCredential {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Size(min = 2, message = "require.system")
    private String system;

    public String getSystem() {
        return system;
    }

    public UsernamePasswordSysCredential setSystem(String system) {
        this.system = system;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.system).toHashCode();
    }
}
