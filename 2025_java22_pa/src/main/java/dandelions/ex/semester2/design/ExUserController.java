package dandelions.ex.semester2.design;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("s2designController")
@RequestMapping("s2design")
public class ExUserController {

	String controller = "/s2design/";
	String view = "/s2/design/";

	@GetMapping(path = "menu")
	public String secure_menu(Model model) {
		log.info("メニュー画面（bootstrap）");
		return view + "menu";
	}

	@GetMapping("lesson1")
	public String lesson1(Model model) {
		log.info("第22回：Bootstrap Part 1 画面表示");
		return view + "lesson1";
	}

	@GetMapping("lesson2")
	public String lesson2(Model model) {
		log.info("第23回：Bootstrap Part 2 画面表示");
		return view + "lesson2";
	}

	@GetMapping("lesson3")
	public String lesson3(Model model) {
		log.info("第24回：Bootstrap Part 3 画面表示");
		return view + "lesson3";
	}

	@GetMapping("lesson4")
	public String lesson4(Model model) {
		log.info("第25回：Bootstrap Part 4 画面表示");
		return view + "lesson4";
	}


	
}
