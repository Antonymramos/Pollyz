package br.com.projeto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL = "jdbc:postgresql://localhost:5432/gerenciador_patrimonios";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "12345";


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }
}
