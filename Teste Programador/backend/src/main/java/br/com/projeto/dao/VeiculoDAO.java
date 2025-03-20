package br.com.projeto.dao;

import br.com.projeto.models.Veiculo;
import br.com.projeto.models.Carro;
import br.com.projeto.models.Moto;
import br.com.projeto.util.ConexaoBanco;
import br.com.projeto.exception.VeiculoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class VeiculoDAO {

    public void validarVeiculo(Veiculo veiculo) {
        if (veiculo.getModelo() == null || veiculo.getModelo().isEmpty()) {
            throw new VeiculoException("O modelo do veículo não pode estar vazio.");
        }
        if (veiculo.getFabricante() == null || veiculo.getFabricante().isEmpty()) {
            throw new VeiculoException("O fabricante do veículo não pode estar vazio.");
        }
        if (veiculo.getPreco() <= 0) {
            throw new VeiculoException("O preço do veículo deve ser maior que zero.");
        }
        if (veiculo.getAno() < 1886 || veiculo.getAno() > java.time.Year.now().getValue()) {
            throw new VeiculoException("O ano do veículo é inválido.");
        }
        if (veiculo.getModelo().length() > 50) {
            throw new VeiculoException("O modelo do veículo excede o limite de 50 caracteres.");
        }
        if (veiculo.getFabricante().length() > 50) {
            throw new VeiculoException("O fabricante do veículo excede o limite de 50 caracteres.");
        }
    }
    
    public void inserirVeiculo(Veiculo veiculo) {
        String getPatrimonioIdSQL = "SELECT id FROM patrimonio WHERE numero_patrimonio = ?";
    
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmtPatrimonio = conn.prepareStatement(getPatrimonioIdSQL)) {
    
            // 1️⃣ Buscar o ID do patrimônio com base no número do patrimônio
            stmtPatrimonio.setString(1, veiculo.getNumeroPatrimonio());
            ResultSet rs = stmtPatrimonio.executeQuery();
    
            if (!rs.next()) {
                throw new RuntimeException("Erro: Nenhum patrimônio encontrado com o número " + veiculo.getNumeroPatrimonio());
            }
    
            int patrimonioId = rs.getInt("id"); // Obtém o ID correto do patrimônio
    
            // 2️⃣ Inserção do veículo na tabela veiculo
            String sql = "INSERT INTO veiculo (modelo, fabricante, ano, preco, cor, numero_patrimonio, subgrupo, quantidade_portas, tipo_combustivel, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    
            try (PreparedStatement stmtVeiculo = conn.prepareStatement(sql)) {
                stmtVeiculo.setInt(1, patrimonioId); // Usa o ID correto do patrimônio
                stmtVeiculo.setString(2, veiculo.getModelo());
                stmtVeiculo.setString(3, veiculo.getFabricante());
                stmtVeiculo.setInt(4, veiculo.getAno());
                stmtVeiculo.setDouble(5, veiculo.getPreco());
                stmtVeiculo.setString(6, veiculo.getCor());
                stmtVeiculo.setString(7, veiculo.getNumeroPatrimonio());
                stmtVeiculo.setString(8, veiculo.getSubgrupo() != null ? veiculo.getSubgrupo() : "Padrão");
    
                // Define os atributos do veículo corretamente
                if (veiculo instanceof Carro) {
                    stmtVeiculo.setInt(9, ((Carro) veiculo).getQuantidadePortas());
                    stmtVeiculo.setString(10, ((Carro) veiculo).getTipoCombustivel());
                    stmtVeiculo.setString(11, "Carro");
                } else if (veiculo instanceof Moto) {
                    stmtVeiculo.setNull(9, Types.INTEGER);
                    stmtVeiculo.setNull(10, Types.VARCHAR);
                    stmtVeiculo.setString(11, "Moto");
                } else {
                    throw new RuntimeException("Erro: Tentativa de inserir um tipo de veículo desconhecido!");
                }
    
                stmtVeiculo.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir veículo: " + e.getMessage(), e);
        }
    }
    
    public Veiculo buscarPorId(int id) {
        String sql = "SELECT * FROM veiculo WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return construirVeiculo(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar veículo por ID: " + e.getMessage(), e);
        }
        return null;
    }

    private Veiculo construirVeiculo(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");

        if ("Carro".equalsIgnoreCase(tipo)) {
            Carro carro = new Carro();
            carro.setQuantidadePortas(rs.getInt("quantidade_portas"));
            carro.setTipoCombustivel(rs.getString("tipo_combustivel"));
            preencherDadosVeiculo(rs, carro);
            return carro;
        } else if ("Moto".equalsIgnoreCase(tipo)) {
            Moto moto = new Moto();
            moto.setCilindrada(rs.getInt("cilindradas"));
            preencherDadosVeiculo(rs, moto);
            return moto;
        } else {
            throw new RuntimeException("Erro: Tipo de veículo não reconhecido!");
        }
    }

    private void preencherDadosVeiculo(ResultSet rs, Veiculo veiculo) throws SQLException {
        veiculo.setId(rs.getInt("id"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setFabricante(rs.getString("fabricante"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setPreco(rs.getDouble("preco"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setNumeroPatrimonio(rs.getString("numero_patrimonio"));
    }

    public void atualizarVeiculo(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET modelo = ?, fabricante = ?, ano = ?, preco = ?, cor = ?, numero_patrimonio = ?, quantidade_portas = ?, tipo_combustivel = ?, cilindradas = ? WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, veiculo.getModelo());
            stmt.setString(2, veiculo.getFabricante());
            stmt.setInt(3, veiculo.getAno());
            stmt.setDouble(4, veiculo.getPreco());
            stmt.setString(5, veiculo.getCor());
            stmt.setString(6, veiculo.getNumeroPatrimonio());

            if (veiculo instanceof Carro) {
                Carro carro = (Carro) veiculo;
                stmt.setInt(7, carro.getQuantidadePortas());
                stmt.setString(8, carro.getTipoCombustivel());
                stmt.setNull(9, Types.INTEGER); // Sem cilindradas para Carro
            } else if (veiculo instanceof Moto) {
                Moto moto = (Moto) veiculo;
                stmt.setNull(7, Types.INTEGER); // Sem quantidade_portas para Moto
                stmt.setNull(8, Types.VARCHAR); // Sem tipo_combustivel para Moto
                stmt.setInt(9, moto.getCilindrada());
            } else {
                stmt.setNull(7, Types.INTEGER);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setInt(10, veiculo.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar veículo: " + e.getMessage(), e);
        }
    }

    public void excluirVeiculo(int id) {
        String sql = "DELETE FROM veiculo WHERE id = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir veículo: " + e.getMessage(), e);
        }
    }

    public List<Veiculo> buscarTodos() {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculo";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                veiculos.add(construirVeiculo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os veículos: " + e.getMessage(), e);
        }

        return veiculos;
    }
}
