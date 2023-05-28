package ultra.com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	// @Bean
	// public BCryptPasswordEncoder bCryptPasswordEncoder() {
	// 	return new BCryptPasswordEncoder();
	// }	

}
