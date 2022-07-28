package org.jarvis;

import org.jarvis.jdbc.data.Credentials;
import org.jarvis.jdbc.data.User;
import org.jarvis.jdbc.data.UserOneToOneTestRepository;
import org.jarvis.spring.aspect.metrix.Monitor;
import org.jarvis.spring.misc.ApplicationInfoLogRunner;
import org.jarvis.spring.web.filter.CachedBodyRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

import javax.servlet.Filter;
import java.util.Date;


@SpringBootApplication
@RestController
public class JarvisApplication {

    public static void main(String[] args) {
//        SpringApplication.run(JarvisApplication.class, args);

		SpringApplication application = new SpringApplication(JarvisApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
    }

    @RequestMapping
    @Monitor
    public String index(String hehe) {
        System.out.println(hehe);
		return "OK";
    }

    @Bean
    public Filter filter() {
        return new CachedBodyRequestFilter();
    }

    @Bean
    public CommandLineRunner applicationInfoLogRunner() {
        return new ApplicationInfoLogRunner();
    }


  /*  @Autowired
    private UserOneToOneTestRepository userOneToOneTestRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {

        return args -> {
            Credentials credentials = new Credentials();
            credentials.setUserName("peterm");
            credentials.setPassword("password");
            User user = new User();
            user.setCreatedTime(new Date());
            user.setDateofBirth(new Date());
            user.setCredentials(credentials);
            User createdUser = userOneToOneTestRepository.save(user);
            System.err.println(createdUser);

        };
    }*/


}
