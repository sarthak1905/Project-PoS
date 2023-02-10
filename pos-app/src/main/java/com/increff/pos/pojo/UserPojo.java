package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static com.increff.pos.pojo.GeneratorTable.POS_USER_SEQ;
import static com.increff.pos.pojo.GeneratorTable.POS_TABLE_NAME;

@Entity
@Getter
@Setter
@Table(name = "users", indexes = @Index(name="email_index", columnList = "email"))
public class UserPojo extends AbstractVersionPojo {

	@Id
	@TableGenerator(name = POS_USER_SEQ, pkColumnValue = POS_USER_SEQ, table = POS_TABLE_NAME)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = POS_USER_SEQ)
	private int id;
	@Column(nullable = false, unique = true)
	@NotBlank
	private String email;
	@Column(nullable = false)
	@NotBlank
	private String password;
}
