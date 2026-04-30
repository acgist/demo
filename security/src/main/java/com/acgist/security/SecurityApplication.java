package com.acgist.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	
	@Order(1)
	@Configurable
	@EnableWebSecurity
	public static class AdminConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.antMatcher("/admin/**").authorizeRequests().anyRequest().access("@roleService.hasPermission(request, authentication)")
				.and()
				.exceptionHandling().accessDeniedPage("/admin/login")
				.and()
				.logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/login").permitAll()
				.and()
				.formLogin()
				.loginPage("/admin/login")
				.loginProcessingUrl("/admin/login")
				.usernameParameter("username").passwordParameter("password")
				.defaultSuccessUrl("/admin/list")
				// 不能转发：405
//				.successForwardUrl("/user/list")
				.failureUrl("/admin/login?fail")
				.permitAll();
		}
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(new UserDetailsService() {
				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					return new User("admin", new BCryptPasswordEncoder().encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
				}
			}).passwordEncoder(new BCryptPasswordEncoder());
		}
	}
	
	@Order(2)
	@Configurable
	@EnableWebSecurity
	public static class UserConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.antMatcher("/user/**").authorizeRequests().anyRequest().access("@roleService.hasPermission(request, authentication)")
				.and()
				.exceptionHandling().accessDeniedPage("/user/login")
				.and()
				.logout().logoutUrl("/user/logout").logoutSuccessUrl("/user/login").permitAll()
				.and()
				.formLogin().loginPage("/user/login").loginProcessingUrl("/user/login").usernameParameter("username").passwordParameter("password")
				.defaultSuccessUrl("/user/list")
				.failureUrl("/user/login?fail")
				.permitAll();
		}
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(new UserDetailsService() {
				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					return new User("user", new BCryptPasswordEncoder().encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("user"));
				}
			}).passwordEncoder(new BCryptPasswordEncoder());
		}
	}
	
	private static class ApiFilter extends BasicAuthenticationFilter {
		public ApiFilter(AuthenticationManager authenticationManager) {
			super(authenticationManager);
		}
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
			// 获取参数：自行实现
			String name = request.getParameter("name");
			name = name == null ? "fail" : name;
			User user = new User(name, new BCryptPasswordEncoder().encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList(name));
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				user,
				user.getPassword(),
				user.getAuthorities()
			);
			// 设置登陆信息
			SecurityContextHolder.getContext().setAuthentication(token);
			// 一定不要忘记继续执行：否者前台什么信息都没有
			chain.doFilter(request, response);
		}
		
	}
	
	@Order(3)
	@Configurable
	@EnableWebSecurity
	public static class ApiConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.csrf().disable()
			.logout().disable()
			.formLogin().disable()
			.exceptionHandling().accessDeniedPage("/api/fail")
			.and()
			.authorizeRequests().antMatchers("/api/fail").permitAll()
			.and()
			.antMatcher("/api/**").authorizeRequests().anyRequest().access("@roleService.hasPermission(request, authentication)")
			.and()
			.addFilterBefore(new ApiFilter(this.authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}
	
	@Order(4)
	@Configurable
	@EnableWebSecurity
	public static class AllConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable().headers().frameOptions().sameOrigin()
				.and()
				.antMatcher("/**").authorizeRequests().anyRequest().permitAll();
		}
	}
	
}
