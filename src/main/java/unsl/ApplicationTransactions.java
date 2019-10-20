package unsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class ApplicationTransactions extends SpringBootServletInitializer{
	

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate  getRestTemplate2() {
 	return new RestTemplate();
	}


	public static void main(String[] args) {
		SpringApplication.run(ApplicationTransactions.class, args);
	}
   
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationTransactions.class);
	}
}
