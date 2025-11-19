package dandelions.ex.semester2.group;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Profile("groupConfig")
public class WebSecurityConfig {

	private static final String PATH = "/group/";
	@Bean
	PasswordEncoder passwordEncoder() {

		//エンコードの無効化
		//return PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// BCryptPasswordEncoderを使用してパスワードをエンコード
		return new BCryptPasswordEncoder();

	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
					// 静的リソースへのアクセスを許可
					.requestMatchers("/assets/**").permitAll() // CSS、JS、画像などの静的リソースにアクセスを許可
					.requestMatchers("/s2design/**").permitAll() // CSS、JS、画像などの静的リソースにアクセスを許可
					.requestMatchers(PATH + "secure").permitAll() // ログインページのアクセスを許可
					.anyRequest().authenticated() // その他のリクエストは認証を要求
			)
			.formLogin((form) -> form
					.loginPage(PATH + "secure") // カスタムログインページ
					.usernameParameter("email") // ユーザー名のパラメータ名
					.passwordParameter("password") // パスワードのパラメータ名
					.loginProcessingUrl(PATH + "perform_login") // 認証処理を行うURL
					.defaultSuccessUrl(PATH + "menu", true) // 認証成功後のリダイレクト先
					.failureUrl(PATH + "secure?error=true") // 認証失敗時のリダイレクト先
					.permitAll())
			.logout((logout) -> logout
					.logoutUrl(PATH + "perform_logout") // ログアウト処理のURL
					.logoutSuccessUrl(PATH + "secure?logout=true") // ログアウト成功後のリダイレクト先
					.invalidateHttpSession(true) // セッションを無効化
					.deleteCookies("JSESSIONID") // クッキーを削除
					.permitAll());
		return http.build();
	}
}
