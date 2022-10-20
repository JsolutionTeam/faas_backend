package zinsoft.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

import org.springframework.web.filter.CorsFilter;
import zinsoft.util.Constants;
import zinsoft.web.security.BasicAccessDeniedHandler;
import zinsoft.web.security.BasicAuthenticationEntryPoint;
import zinsoft.web.security.LoginFailureHandler;
import zinsoft.web.security.LoginSuccessHandler;
import zinsoft.web.security.LogoutSuccessHandler;
import zinsoft.web.security.ReloadableFilterInvocationSecurityMetadataSource;
import zinsoft.web.security.RememberMeTokenRepository;
import zinsoft.web.security.Sha256PasswordEncoder;
import zinsoft.web.security.WebUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.prefix:}")
    private String apiPrefix;

    @Autowired
    private CorsFilter corsFilter;

    //@formatter:off

    /*
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring().antMatchers(
                "/static/**",
                "/content/login",
                "/"
            );
    }
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        //config.addAllowedMethod("HEAD");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Access-Control-Request-Method");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addExposedHeader("Access-Control-Allow-Origin");
        config.addExposedHeader("Access-Control-Allow-Credentials");
        source.registerCorsConfiguration("/**", config);
        CorsFilter corsFilter = new CorsFilter(source);
        */

        http
                .requestCache()
                .requestCache(new NullRequestCache()) // 동일 URL로 POST 요청하고 인증 실패 후 GET 요청하는 경우 인증 실패로 인식하는 문제 방지
                .and()
                .cors()
                .disable() // cors 사용 x
                .addFilter(corsFilter)// cors 허용


                //.addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                //.addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
                //.anonymous().disable()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())//.accessDeniedPage("/page/error.jsp?code=0101")
                .authenticationEntryPoint(authenticationEntryPoint())
                /*
                .and()
                    .authorizeRequests()
                        .antMatchers("/static/**").permitAll()
                        .antMatchers("/content/login").permitAll()
                        .antMatchers("/").permitAll()
                */
                .and()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll() //   /test/** 경로는 로그인 확인 x

                .and()
                .formLogin()
                //.loginPage(Constants.WEB_SECURITY_LOGIN_PAGE) // authenticationEntryPoint에서 처리
                .loginProcessingUrl(apiPrefix + Constants.WEB_SECURITY_LOGIN_PROCESSING_URL)
                .usernameParameter(Constants.WEB_SECURITY_USERNAME_PARAMETER)
                .passwordParameter(Constants.WEB_SECURITY_PASSWORD_PARAMETER)
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                //.permitAll()
                .and()
                .logout()
                .logoutUrl(apiPrefix + Constants.WEB_SECURITY_LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("q2F5yMWEg")
                .tokenValiditySeconds(1209600) // 2 weeks
                .rememberMeParameter("rememberMe")
                .tokenRepository(rememberMeTokenRepository())
                .and()
                .csrf().disable();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return webUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(simplePasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Bean
    public Sha256PasswordEncoder simplePasswordEncoder() {
        return new Sha256PasswordEncoder();
    }

    @Bean
    public WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator(FilterSecurityInterceptor filterSecurityInterceptor) {
        return new DefaultWebInvocationPrivilegeEvaluator(filterSecurityInterceptor);
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");

        List<AccessDecisionVoter<? extends Object>> decisionVoterList = new ArrayList<AccessDecisionVoter<? extends Object>>();
        decisionVoterList.add(roleVoter);
        decisionVoterList.add(new AuthenticatedVoter());

        AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoterList);
        accessDecisionManager.setAllowIfAllAbstainDecisions(false);

        return accessDecisionManager;
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();

        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setSecurityMetadataSource(reloadableFilterInvocationSecurityMetadataSource());

        return filterSecurityInterceptor;
    }

    @Bean
    public ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource() {
        return new ReloadableFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public UserDetailsService webUserDetailsService() {
        return new WebUserDetailsService();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new BasicAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new BasicAuthenticationEntryPoint();
    }

    @Bean
    public PersistentTokenRepository rememberMeTokenRepository() {
        return new RememberMeTokenRepository();
    }

}
