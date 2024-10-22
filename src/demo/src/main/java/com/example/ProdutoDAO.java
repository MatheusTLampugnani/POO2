package com.example;
import java.sql.*;

public class ProdutoDAO {

    public static  String URL = "jdbc:sqlite:produtos.db";

    public ProdutoDAO() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            // Verifica se a tabela existe e adiciona a coluna 'status' se necessário
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS produto ("
                                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                    + "nome TEXT NOT NULL,"
                                    + "preco REAL NOT NULL)";
            Statement stmt = conn.createStatement();
            stmt.execute(sqlCreateTable);

            // Verificar se a coluna 'status' já existe
            if (!temColunaStatus(conn)) {
                // Adiciona a coluna 'status' se não existir
                String sqlAddColumn = "ALTER TABLE produto ADD COLUMN status INTEGER DEFAULT 1";
                stmt.execute(sqlAddColumn);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela ou adicionar coluna: " + e.getMessage());
        }
    }

    // Método para verificar se a coluna 'status' existe na tabela
    private boolean temColunaStatus(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(produto)")) {

            while (rs.next()) {
                if ("status".equalsIgnoreCase(rs.getString("name"))) {
                    return true; // Coluna já existe
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a coluna status: " + e.getMessage());
        }
        return false; // Coluna não existe
    }

    // Método para cadastrar um novo produto
    public void cadastrarProduto(Produto produto) {
        String sqlInsert = "INSERT INTO produto(nome, preco, status) VALUES(?, ?, 1)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.executeUpdate();
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    // Método para consultar um produto
    public Produto consultarProduto(int id) {
        String sqlSelect = "SELECT * FROM produto WHERE id = ? AND status = 1"; // Apenas produtos ativos
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Produto(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar produto: " + e.getMessage());
        }
        return null;
    }

    // Método para desativar um produto
    public void desativarProduto(int id) {
        String sqlUpdate = "UPDATE produto SET status = 0 WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produto desativado com sucesso!");
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao desativar produto: " + e.getMessage());
        }
    }
}
