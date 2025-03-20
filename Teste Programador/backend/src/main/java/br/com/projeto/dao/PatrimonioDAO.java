package br.com.projeto.dao;

import br.com.projeto.models.*;
import br.com.projeto.util.ConexaoBanco;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatrimonioDAO {

    // ✅ Buscar todos os patrimônios
    public List<Patrimonio> buscarTodos() {
        List<Patrimonio> lista = new ArrayList<>();
        String sql = "SELECT * FROM patrimonio";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Patrimonio patrimonio = new Patrimonio();
                patrimonio.setId(rs.getInt("id"));
                patrimonio.setModelo(rs.getString("modelo"));
                patrimonio.setNumeroPatrimonio(rs.getString("numero_patrimonio"));
                patrimonio.setSubgrupo(rs.getString("subgrupo"));
                lista.add(patrimonio);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar patrimônios: " + e.getMessage(), e);
        }

        return lista;
    }

    // ✅ Inserir patrimônio
    public Patrimonio inserirPatrimonio(Patrimonio patrimonio) {
        String sql = "INSERT INTO patrimonio (modelo, numero_patrimonio, subgrupo) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patrimonio.getModelo());
            stmt.setString(2, patrimonio.getNumeroPatrimonio());
            stmt.setString(3, patrimonio.getSubgrupo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                patrimonio.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir patrimônio: " + e.getMessage(), e);
        }

        return patrimonio;
    }
    public Patrimonio atualizarPatrimonio(Patrimonio patrimonio) {
        String sql = "UPDATE patrimonio SET modelo = ?, numero_patrimonio = ?, subgrupo = ? WHERE id = ?";
    
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, patrimonio.getModelo());
            stmt.setString(2, patrimonio.getNumeroPatrimonio());
            stmt.setString(3, patrimonio.getSubgrupo());
            stmt.setInt(4, patrimonio.getId());
    
            int linhasAfetadas = stmt.executeUpdate();
    
            if (linhasAfetadas == 0) {
                throw new RuntimeException("❌ Patrimônio com ID não encontrado.");
            }
    
            return patrimonio;
    
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar patrimônio: " + e.getMessage(), e);
        }
    }
    

    public Optional<Patrimonio> buscarPorId(int id) {
        String sql = "SELECT * FROM patrimonio WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                Patrimonio patrimonio = new Patrimonio();
                patrimonio.setId(rs.getInt("id"));
                patrimonio.setModelo(rs.getString("modelo"));
                patrimonio.setNumeroPatrimonio(rs.getString("numero_patrimonio"));
                patrimonio.setSubgrupo(rs.getString("subgrupo"));
                return Optional.of(patrimonio);
            }
    
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar patrimônio: " + e.getMessage(), e);
        }
        return Optional.empty();  // Se não encontrar, retorna vazio
    }

    // ✅ Excluir patrimônio
    public void excluirPatrimonio(int id) {
        String sql = "DELETE FROM patrimonio WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir patrimônio: " + e.getMessage(), e);
        }
    }
    public Optional<Patrimonio> buscarPorNumeroPatrimonio(String numeroPatrimonio) {
        String sql = "SELECT * FROM patrimonio WHERE numero_patrimonio = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, numeroPatrimonio);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                Patrimonio patrimonio = new Patrimonio();
                patrimonio.setId(rs.getInt("id"));
                patrimonio.setModelo(rs.getString("modelo"));
                patrimonio.setNumeroPatrimonio(rs.getString("numero_patrimonio"));
                patrimonio.setSubgrupo(rs.getString("subgrupo"));
                return Optional.of(patrimonio);
            }
    
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por número de patrimônio: " + e.getMessage(), e);
        }
    
        return Optional.empty();
    }
    public void excluir(int id) {
        String sql = "DELETE FROM patrimonio WHERE id = ?";
    
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
    
            if (linhasAfetadas == 0) {
                throw new RuntimeException("❌ Nenhum patrimônio encontrado com o ID fornecido.");
            }
    
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir patrimônio: " + e.getMessage(), e);
        }
    }
    
}
