package br.com.fiap.model.dao;

import br.com.fiap.exception.ErroDeAcessoADadosException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    public static void closeConnection() {
        Connection conn = connection.get();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            connection.remove();
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = connection.get();
            if (conn != null && !conn.isClosed()) {
                return conn;
            }
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new ErroDeAcessoADadosException("Variáveis de ambiente do banco não configuradas", null);
            }

            conn = DriverManager.getConnection(url, user, password);
            connection.set(conn);
            return conn;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao conectar ao banco de dados", e);
        } catch (ClassNotFoundException e) {
            throw new ErroDeAcessoADadosException("Driver do Oracle não encontrado", e);
        }
    }
}
