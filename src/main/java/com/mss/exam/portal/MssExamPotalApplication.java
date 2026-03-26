package com.mss.exam.portal;

import com.mss.exam.portal.repository.MssRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.mss.exam.portal.repository", repositoryBaseClass = MssRepositoryImpl.class)
public class MssExamPotalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssExamPotalApplication.class, args);
    }

}
