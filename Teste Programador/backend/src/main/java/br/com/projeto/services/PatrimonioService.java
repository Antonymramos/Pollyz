package br.com.projeto.services;

import br.com.projeto.dao.PatrimonioDAO;
import br.com.projeto.models.Patrimonio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatrimonioService {

    private final PatrimonioDAO patrimonioDAO;

    @Autowired
    public PatrimonioService(PatrimonioDAO patrimonioDAO) {
        this.patrimonioDAO = patrimonioDAO;
    }

    public List<Patrimonio> buscarTodos() {
        return patrimonioDAO.buscarTodos();
    }
    public Optional<Patrimonio> buscarPorNumeroPatrimonio(String numeroPatrimonio) {
        return patrimonioDAO.buscarPorNumeroPatrimonio(numeroPatrimonio);
    }
    public Patrimonio salvarPatrimonio(Patrimonio patrimonio) {
        if (patrimonio.getId() != null) {
            return patrimonioDAO.atualizarPatrimonio(patrimonio);  // ✅ Atualiza se ID existir
        } else {
            return patrimonioDAO.inserirPatrimonio(patrimonio);    // ✅ Insere se for novo
        }
    }
    
    public Optional<Patrimonio> buscarPorId(Integer id) {
        return patrimonioDAO.buscarPorId(id);  // CORRETO: retorna Optional<Patrimonio>
    }

    public void excluirPatrimonio(int id) {
        patrimonioDAO.excluirPatrimonio(id);
    }
}
