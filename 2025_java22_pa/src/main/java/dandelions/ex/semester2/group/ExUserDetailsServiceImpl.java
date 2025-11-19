package dandelions.ex.semester2.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dandelions.ex.ExUserEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@Profile("groupConfig")
@Service("groupServiceImpl")
public class ExUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ExUserRepository repository;

	@Override // UserDetailsServiceインターフェイスで実装メソッドと設定している
	public UserDetails loadUserByUsername(String primaryKey) throws UsernameNotFoundException {

		log.info("primaryKey: " + primaryKey);

		// username（email）でユーザーを検索
		ExUserEntity entity = repository.findById(primaryKey)
				.orElseThrow(() -> new UsernameNotFoundException("認証に失敗しました　:　" + primaryKey));

		log.info("password: " + entity.getPassword());

		// UserDetails を返す。username は email、password はエンティティの password を使用。
		//return User.withUsername(entity.getEmail()).password("{noop}" + entity.getPassword()).roles("USER").build();
		return User.withUsername(entity.getEmail()).password(entity.getPassword()).roles("USER").build();
	}

}
