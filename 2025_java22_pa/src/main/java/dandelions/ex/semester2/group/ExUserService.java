package dandelions.ex.semester2.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dandelions.ex.ExUserEntity;
import dandelions.ex.ExUserForm;
import io.micrometer.common.util.StringUtils;

/**
 * ユーザーサービスクラス。
 * ユーザーの認証、保存、データ取得に関するメソッドを提供します。
 */
@Service("groupService")
public class ExUserService {

	@Autowired
	ExUserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * ユーザーの認証を行います。
	 * 
	 * @param form ユーザーフォーム
	 * @return 認証が成功した場合はtrue、失敗した場合はfalse
	 */
	public boolean isAuth(ExUserForm form) {

		// ユーザーフォームのEmailをコンソールに出力する
		System.out.println("Email　：　" + form.getEmail());
		// ユーザーフォームのPasswordをコンソールに出力する
		System.out.println("Password　：　" + form.getPassword());

		// リポジトリからメールアドレスでユーザーを検索する
		Optional<ExUserEntity> opt = repository.findById(form.getEmail());

		ExUserEntity entity = null;
		boolean result = false;
		// エンティティが存在するか（DBから情報が取得できた）
		if (opt.isPresent()) {
			// オプショナルからエンティティを取得する
			entity = opt.get();

			// エンティティのパスワードとフォームのパスワードを比較する
			if (entity.getPassword().equals(form.getPassword())) {
				result = true;
			}
		}

		// 認証結果を返す
		return result;

	}

	/**
	 * ユーザーフォームを保存します。（セキュア）
	 * 
	 * @param form ユーザーフォーム
	 * @return 保存されたユーザーフォーム
	 */
	public ExUserForm secureSave(ExUserForm form) {

		// 登録更新用のエンティティのインスタンスを生成する
		ExUserEntity entity = new ExUserEntity();

		// フォームで入力した情報をエンティティにコピーする
		BeanUtils.copyProperties(form, entity);

		// 平文 → ハッシュ（登録時は必ず入力されている前提）
		String encoded = passwordEncoder.encode(form.getPassword());
		entity.setPassword(encoded);

		// リポジトリにエンティティを保存する
		entity = repository.save(entity);
		// 更新したエンティティ情報をフォームにコピーする
		BeanUtils.copyProperties(entity, form);

		// 保存されたユーザーフォームを返す
		return form;
	}

	/**
	 * ユーザーフォームを保存します。
	 * 
	 * @param form ユーザーフォーム
	 * @return 保存されたユーザーフォーム
	 */
	public ExUserForm save(ExUserForm form) {

		// 登録更新用のエンティティのインスタンスを生成する
		ExUserEntity entity = new ExUserEntity();
		// フォームで入力した情報をエンティティにコピーする
		BeanUtils.copyProperties(form, entity);
		// リポジトリにエンティティを保存する
		entity = repository.save(entity);
		// 更新したエンティティ情報をフォームにコピーする
		BeanUtils.copyProperties(entity, form);

		// 保存されたユーザーフォームを返す
		return form;
	}

	/**
	 * ユーザーリストを取得します。
	 * 
	 * @param form 検索条件を含むユーザーフォーム
	 * @return 条件に一致するユーザーリスト
	 */
	public ArrayList<ExUserForm> getUsersList(ExUserForm form) {

		// リポジトリから条件に合うユーザーリストを取得する
		/*
		if (Objects.isNull(form.getBloodtype())) {
			form.setBloodtype("%%");
		}
		List<ExUserEntity> entityList = repository.findByKanaContainingAndBloodtypeLikeAndAddressStartingWith(form.getKana(), form.getBloodtype(), form.getAddress());
		*/
		form.setKana("%" + form.getKana() + "%");
		form.setBloodtype(StringUtils.isEmpty(form.getBloodtype()) ? "%" : form.getBloodtype());
		form.setAddress(form.getAddress() + "%");
		List<ExUserEntity> entityList = repository.findByExUserList(form.getKana(), form.getBloodtype(),
				form.getAddress());

		// ユーザーフォームリストのインスタンスを生成する
		ArrayList<ExUserForm> formList = new ArrayList<ExUserForm>();

		// DBから受け取ったユーザー情報をフォームに移す
		for (ExUserEntity entity : entityList) { //←拡張for文　for( データ1件格納変数 : データリスト)
			// フォームのインスタンスを生成する
			form = new ExUserForm();
			// DBの情報をフォームにコピーする
			BeanUtils.copyProperties(entity, form);
			// パスワードは通常は戻さない/空にするのが安全
			form.setPassword(""); // 画面戻し用に空へ
			// フォームリストにフォームを追加する
			formList.add(form);
		}

		// ユーザーリストを返す
		return formList;
	}

	/**
	 * ユーザーデータを取得します。
	 * 
	 * @param form 検索するユーザーフォーム
	 * @return 検索結果のユーザーフォーム、存在しない場合はnull
	 */
	public ExUserForm getUserData(ExUserForm form) {

		// リポジトリからメールアドレスでユーザーを検索する
		Optional<ExUserEntity> opt = repository.findById(form.getEmail());

		ExUserEntity entity = null;
		// エンティティが存在するか（DBから情報が取得できた）
		if (opt.isPresent()) {
			// オプショナルからエンティティを取得する
			entity = opt.get();
			// DBから受け取ったユーザー情報をフォームに移す
			BeanUtils.copyProperties(entity, form);
		} else {
			// エンティティが存在しない場合、フォームをnullに設定する
			form = null;
		}

		// ユーザーフォームを返す
		return form;
	}

	/**
	 * ユーザーフォームを削除します。
	 * 
	 * @param form 削除するユーザーフォーム
	 */
	public void delete(ExUserForm form) {

		// リポジトリからメールアドレスでユーザーを削除する
		repository.deleteById(form.getEmail());

	}

	/**
	 * ユーザーフォームを一括削除します。
	 * 
	 */
	public void deleteMultiple(List<String> selectedEmails) {

		// リポジトリからメールアドレスでユーザーを削除する
		repository.deleteAllById(selectedEmails);

	}

	/**
	 * 都道府県リストのデータを取得します。
	 * 
	 * @return 都道府県リスト
	 */
	public String[] getPrefsList() {

		// 都道府県の配列を作成する
		String[] prefs = {
				"北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県",
				"茨城県", "栃木県", "群馬県", "埼玉県", "千葉県", "東京都", "神奈川県",
				"新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県",
				"岐阜県", "静岡県", "愛知県", "三重県",
				"滋賀県", "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県",
				"鳥取県", "島根県", "岡山県", "広島県", "山口県",
				"徳島県", "香川県", "愛媛県", "高知県",
				"福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県"
		};

		// 都道府県リストを返す
		return prefs;
	}
}
