package br.com.projeto.services;

import br.com.projeto.dao.VeiculoDAO;
import br.com.projeto.models.Carro;
import br.com.projeto.models.Moto;
import br.com.projeto.models.Veiculo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service // 🔥 Permite que o Spring gerencie essa classe
public class VeiculoService {

    private final VeiculoDAO veiculoDAO;

    @Autowired // ✅ Injeta automaticamente o DAO
    public VeiculoService(VeiculoDAO veiculoDAO) {
        this.veiculoDAO = veiculoDAO;
    }

    public List<Veiculo> listarVeiculos() {
        return veiculoDAO.buscarTodos();
    }

    public Veiculo criarVeiculo(Veiculo veiculo) {
        veiculoDAO.inserirVeiculo(veiculo);
        return veiculo;
    }

    public void excluirVeiculo(int id) {
        veiculoDAO.excluirVeiculo(id);
    }

    public List<Veiculo> buscarVeiculos(Integer id, String modelo, String fabricante, String cor, Integer ano) {
        List<Veiculo> veiculos = veiculoDAO.buscarTodos(); // ✅ Agora funciona corretamente

        return veiculos.stream()
            .filter(v -> id == null || id.equals(v.getId())) 
            .filter(v -> modelo == null || modelo.trim().isEmpty() || (v.getModelo() != null && v.getModelo().equalsIgnoreCase(modelo))) 
            .filter(v -> fabricante == null || fabricante.trim().isEmpty() || (v.getFabricante() != null && v.getFabricante().equalsIgnoreCase(fabricante)))
            .filter(v -> cor == null || cor.trim().isEmpty() || (v.getCor() != null && v.getCor().equalsIgnoreCase(cor)))
            .filter(v -> ano == null || ano.equals(v.getAno()))
            .collect(Collectors.toList());
    }
    
    public Veiculo atualizarVeiculo(int id, Veiculo veiculoAtualizado) {
        Veiculo veiculoExistente = veiculoDAO.buscarPorId(id);

        if (veiculoExistente == null) {
            throw new RuntimeException("Erro: Veículo com ID " + id + " não encontrado!");
        }

        // Atualiza os dados principais
        veiculoExistente.setModelo(veiculoAtualizado.getModelo());
        veiculoExistente.setFabricante(veiculoAtualizado.getFabricante());
        veiculoExistente.setAno(veiculoAtualizado.getAno());
        veiculoExistente.setPreco(veiculoAtualizado.getPreco());
        veiculoExistente.setCor(veiculoAtualizado.getCor());

        // Tratamento específico para Carro
        if (veiculoExistente instanceof Carro && veiculoAtualizado instanceof Carro) {
            Carro carroExistente = (Carro) veiculoExistente;
            Carro carroAtualizado = (Carro) veiculoAtualizado;

            if (carroAtualizado.getQuantidadePortas() <=0) {
                throw new RuntimeException("Erro: Carro deve ter uma quantidade válida de portas.");
            }
            if (carroAtualizado.getTipoCombustivel() == null || carroAtualizado.getTipoCombustivel().isEmpty()) {
                throw new RuntimeException("Erro: O tipo de combustível do carro não pode estar vazio.");
            }

            carroExistente.setQuantidadePortas(carroAtualizado.getQuantidadePortas());
            carroExistente.setTipoCombustivel(carroAtualizado.getTipoCombustivel());

        } else if (veiculoExistente instanceof Moto && veiculoAtualizado instanceof Moto) {
            Moto motoExistente = (Moto) veiculoExistente;
            Moto motoAtualizado = (Moto) veiculoAtualizado;

            if (motoAtualizado.getCilindrada() <=0){
                throw new RuntimeException("Erro: Moto deve ter cilindradas definidas.");
            }

            motoExistente.setCilindrada(motoAtualizado.getCilindrada());
        }

        veiculoDAO.atualizarVeiculo(veiculoExistente);
        return veiculoExistente;
    }

    public Veiculo buscarPorId(int id) {
        return veiculoDAO.buscarPorId(id);
    }
}
