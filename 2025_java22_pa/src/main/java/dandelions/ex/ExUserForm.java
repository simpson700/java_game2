package dandelions.ex;

import lombok.Data;

@Data
public class ExUserForm {

	private String name;
	private String kana;
	//private String birthday; //yyyy-mm-dd
	//private String gender; // M | F | U
	private String bloodtype; // A | O | B | AB
	private String email;
	//private String tel; //999-9999-9999
	//private String zip; //999-9999
	private String address;

	private String password;
}
