package com.bc.archive_users;

import com.bc.archive_users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArchiveUsersApplication {
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ArchiveUsersApplication.class, args);
		System.out.println("Server is finish / Сервер запущен и готов к работе");
	}
}
