package dandelions.ex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExUserEntity {

	@Column(name="name")
	private String name;
	@Column(name="kana")
	private String kana;
	
	@Column(name="birthday") //yyyy-mm-dd
	private String birthday;
	@Column(name="gender") // M | F | U
	private String gender;
	
	@Column(name="bloodtype") // A | O | B | AB
	private String bloodtype;
	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="tel") //999-9999-9999
	private String tel;
	@Column(name="zip") //999-9999
	private String zip;
	
	@Column(name="address")
	private String address;
	@Column(name="password")
	private String password;
}
