package br.com.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.projeto") // 🔹 Certifique-se de que está correto
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
