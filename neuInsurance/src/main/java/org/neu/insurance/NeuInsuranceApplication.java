package org.neu.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.neu.insurance.util.DatabaseCheck;

@SpringBootApplication
public class NeuInsuranceApplication implements CommandLineRunner {

    @Autowired
    private DatabaseCheck databaseCheck;

    public static void main(String[] args) {
        SpringApplication.run(NeuInsuranceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 医保报销系统启动检查 ===");
        databaseCheck.performFullCheck();
        System.out.println("=== 启动检查完成 ===\n");
    }
}
