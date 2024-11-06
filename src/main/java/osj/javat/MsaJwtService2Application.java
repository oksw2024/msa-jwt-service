package osj.javat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaJwtService2Application {

	public static void main(String[] args) {
		SpringApplication.run(MsaJwtService2Application.class, args);
	}

}
