package com.example;
import java.sql.*;

public class ProdutoDAO {

    public Connection getConnection() throws SQLException {
        
        String url = "jdbc:mysql://localhost:3306/poo2";
        String user = "lampugman";
        String password = "L@mpugn4n1";
        return DriverManager.getConnection(url, user, password);
    }

    public ProdutoDAO() {
        try (Connection conn = getConnection()) {
            // Verifica se a tabela existe e adiciona a coluna 'status' se necessário
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS produto ("
                                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                                    + "nome VARCHAR(255) NOT NULL,"
                                    + "preco DOUBLE NOT NULL)";
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

    // Verifica se a coluna 'status' já existe
    private boolean temColunaStatus(Connection conn) {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, "produto", "status")) {
            return rs.next(); // Se encontrar a coluna, retorna true
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a coluna 'status': " + e.getMessage());
            return false;
        }
    }

    // Método para cadastrar um novo produto
    public void cadastrarProduto(Produto produto) {
        String sqlInsert = "INSERT INTO produto(nome, preco, status) VALUES(?, ?, 1)";
        try (Connection conn = getConnection();
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
        try (Connection conn = getConnection();
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
    
    // Método para desativar um produto (inativá-lo) pelo ID
    public void desativarProduto(int id) {
        String sqlUpdate = "UPDATE produto SET status = 0 WHERE id = ?";
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto com ID " + id + " foi inativado com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado com o ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inativar o produto: " + e.getMessage());
        }
    }


}
