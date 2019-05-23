package com.example.cas.handler;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.FailedLoginException;

import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import com.example.cas.auth.UsernamePasswordSysCredential;
import com.example.cas.exception.CustomException;
/**
 * @author yellowcong
 * 创建日期:2018/02/02
 *
 */
public class CustomerHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    public CustomerHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory,
            Integer order) {
        super(name, servicesManager, principalFactory, order);
    }
    
    /**
     * 用于判断用户的Credential(换而言之，就是登录信息)，是否是俺能处理的
     * 就是有可能是，子站点的登录信息中不止有用户名密码等信息，还有部门信息的情况
     */
    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof UsernamePasswordSysCredential;
    }

    /**
     * 用于认证处理----自定义登录认证+多属性返回
     */
    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
    	UsernamePasswordSysCredential usernamePasswordCredentia = (UsernamePasswordSysCredential) credential;

        //获取传递过来的用户名和密码
        String username = usernamePasswordCredentia.getUsername();
        String password = usernamePasswordCredentia.getPassword();
        String system = usernamePasswordCredentia.getSystem();
        System.out.println("system="+system+";username="+username+";password="+password);
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            //直接是原生的数据库配置啊
            String url = "jdbc:mysql://localhost:3306/cc?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            String user = "root";
            String pass = "123456";

            conn = DriverManager.getConnection(url,user, pass);

            //查询语句
            String sql = "SELECT * FROM cas_user_base WHERE user_name =? AND user_psd = ?";
            PreparedStatement  ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                //存放数据到里面
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("username", rs.getString("user_name"));
                result.put("userpsd", rs.getString("user_psd"));
                result.put("email", rs.getString("email"));
                result.put("addr", rs.getString("addr"));
                result.put("phone", rs.getString("phone"));
                result.put("age", rs.getString("age"));
                System.out.println("result----------"+result);
                //允许登录，并且通过this.principalFactory.createPrincipal来返回用户属性
                return createHandlerResult(credential, this.principalFactory.createPrincipal(username, result), null);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //当是admin用户的情况，直接就登录了，谁叫他是admin用户呢
        if(username.startsWith("admin")) {
            //直接返回去了
            return createHandlerResult(credential, this.principalFactory.createPrincipal(username, Collections.emptyMap()), null);
        }else if (username.startsWith("cas")) {
            //自定义异常测试
            throw new CustomException("自定义异常测试");
        }else if (username.startsWith("lock")) {
            //用户锁定
            throw new AccountLockedException();
        } else if (username.startsWith("disable")) {
            //用户禁用
            throw new AccountDisabledException();
        } else if (username.startsWith("invali")) {
            //禁止登录该工作站登录
            throw new InvalidLoginLocationException();
        } else if (username.startsWith("passorwd")) {
            //密码错误
            throw new FailedLoginException();
        } else if (username.startsWith("account")) {
            //账号错误
            throw new AccountLockedException();
        }
        return null;
    }

}

