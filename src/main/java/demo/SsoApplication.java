package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.security.sso.EnableOAuth2Sso;
import org.springframework.cloud.security.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@EnableOAuth2Sso
public class SsoApplication {

	@RequestMapping("/")
	public String home() {
		return "<html><body><a href='dashboard'>dashboard</a><br/><a href='dashboard/login'>login</a></body></html>";
	}

	@RequestMapping("/dashboard")
	public String dashboard() {
		return "<html><body>Dashboard Yay!</body></html>";
	}

	public static void main(String[] args) {
		SpringApplication.run(SsoApplication.class, args);
	}

	@Component
	public static class LoginConfigurer extends OAuth2SsoConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/dashboard/**").authorizeRequests().anyRequest()
					.authenticated();
		}
	}
}
