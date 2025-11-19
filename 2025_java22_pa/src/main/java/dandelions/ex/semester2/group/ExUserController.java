package dandelions.ex.semester2.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dandelions.ex.ExUserForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("groupController")
@RequestMapping("group")
public class ExUserController {

	@Autowired
	ExUserService service;

	String controller = "/group/";
	String view = "/s2/group/";

	// ログイン画面（SecurityConfigの loginPage と一致させる）
	@GetMapping(path = "secure")
	public String secure_login(Model model) {

		log.info("ログイン画面（セキュア）");

		model.addAttribute("path", controller);
		return view + "secure";
	}

	@GetMapping(path = "menu")
	public String secure_menu(Model model) {

		log.info("メニュー画面（セキュア）");

		model.addAttribute("path", controller);
		return view + "menu";

	}

	@GetMapping(path = "login")
	public String login(Model model) {

		log.info("ログイン画面");

		// Viewへの送信情報　…　key：path　value：controller
		model.addAttribute("path", controller);
		return view + "login";
	}

	@PostMapping(path = "menu")
	public String menu(Model model, ExUserForm form) {

		log.info("メニュー画面");

		String ret = "menu";

		// Viewへの送信情報　…　key：path　value：controller
		model.addAttribute("path", controller);
		return view + ret;
	}

	@GetMapping(path = "menu_back")
	public String menu_back(Model model) {

		log.info("メニュー画面（戻る）");

		model.addAttribute("path", controller);
		return view + "menu";
	}

	@GetMapping(path = "add")
	public String add(Model model, ExUserForm form) {

		log.info("新規登録画面");

		model.addAttribute("path", controller);
		return view + "add";
	}

	@PostMapping(path = "add_action")
	public String add_action(Model model, ExUserForm form) {

		log.info("登録完了メッセージ画面");

		// サービスのsaveメソッドを呼び出してフォームを保存する
		//form = service.save(form);
		form = service.secureSave(form);

		// モデルにパスを追加する
		model.addAttribute("path", controller);
		return view + "add_msg";
	}

	@GetMapping(path = "search")
	public String search(Model model, ExUserForm form) {

		log.info("検索画面");

		// 都道府県の配列を作成する
		String[] prefs = service.getPrefsList();
		// モデルに都道府県の配列を追加する
		model.addAttribute("prefs", prefs);

		model.addAttribute("path", controller);
		return view + "search";
	}

	@GetMapping(path = "list")
	public String list(Model model, ExUserForm form) {

		log.info("一覧画面");

		// ユーザーリストを取得する
		ArrayList<ExUserForm> list = service.getUsersList(form);

		// モデルにユーザーリストを追加する
		model.addAttribute("list", list);

		model.addAttribute("path", controller);
		return view + "list";
	}

	@GetMapping(path = "edit")
	public String edit(Model model, ExUserForm form) {

		log.info("変更画面（" + form.getEmail() + "）");

		// DBから選択したユーザー情報を取得する
		form = service.getUserData(form);

		// 画面にフォーム情報を渡すためモデルに追加する
		model.addAttribute("form", form);
		model.addAttribute("path", controller);
		return view + "edit";
	}

	@PostMapping(path = "edit_action")
	public String edit_action(Model model, ExUserForm form) {

		log.info("変更完了メッセージ画面");

		// サービスのsaveメソッドを呼び出してフォームを保存する
		form = service.save(form);

		// モデルにパスを追加する
		model.addAttribute("path", controller);
		return view + "edit_msg";
	}

	@PostMapping(path = "edit_delete")
	public String edit_delete(Model model, ExUserForm form) {

		log.info("削除完了メッセージ画面");

		service.delete(form);

		// ユーザーフォームのEmailをコンソールに出力する
		model.addAttribute("path", controller);
		return view + "edit_msg";
	}

	/**
	 * 一括削除画面を表示します。
	 * 
	 * @param model モデルオブジェクト
	 * @param form  ユーザーフォーム
	 * @return 一括削除画面のテンプレート名
	 */
	@PostMapping(path = "delete_multiple")
	public String delete_multiple(Model model, ExUserForm form,
			@RequestParam("selectedUsers") List<String> selectedEmails) {

		// 一括削除画面のログを出力する
		log.info("一括削除画面");

		// 選択されたメールアドレスをログに出力する
		log.info("選択されたメールアドレス: " + selectedEmails);

		service.deleteMultiple(selectedEmails);

		// モデルにパスを追加する
		model.addAttribute("path", controller);
		// 一括削除画面のテンプレート名を返す
		return view + "edit_msg";
	}

}
