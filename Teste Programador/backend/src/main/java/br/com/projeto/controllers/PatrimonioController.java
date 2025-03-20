package br.com.projeto.controllers;

import br.com.projeto.models.*;
import br.com.projeto.services.PatrimonioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patrimonios")  // ✅ URL base para todos os endpoints
public class PatrimonioController {

    private final PatrimonioService patrimonioService;

    @Autowired
    public PatrimonioController(PatrimonioService patrimonioService) {
        this.patrimonioService = patrimonioService;
    }

    // 🔍 Buscar patrimônio por ID
    @GetMapping("/{id}")
    public ResponseEntity<Patrimonio> buscarPorId(@PathVariable Integer id) {
        return patrimonioService.buscarPorId(id)
                .map(ResponseEntity::ok)  // ✅ Se encontrado, retorna 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build());  // ❌ Se não encontrado, retorna 404
    }

    // 📄 Listar todos os patrimônios
    @GetMapping
    public ResponseEntity<List<Patrimonio>> listarTodos() {
        return ResponseEntity.ok(patrimonioService.buscarTodos());
    }

    // ➕ Criar novo equipamento
    @PostMapping("/equipamento")
    public ResponseEntity<Patrimonio> criarEquipamento(@Valid @RequestBody Equipamento equipamento) {
        return ResponseEntity.ok(patrimonioService.salvarPatrimonio(equipamento));
    }

    // ✏️ Atualizar patrimônio existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPatrimonio(@PathVariable Integer id, @Valid @RequestBody Patrimonio patrimonioAtualizado) {
        return patrimonioService.buscarPorId(id)
                .map(patrimonio -> {
                    // 🔎 Verifica se já existe outro patrimônio com o mesmo número (mas com ID diferente)
                    Optional<Patrimonio> patrimonioComMesmoNumero = patrimonioService.buscarPorNumeroPatrimonio(patrimonioAtualizado.getNumeroPatrimonio());
                    if (patrimonioComMesmoNumero.isPresent() && !patrimonioComMesmoNumero.get().getId().equals(id)) {
                        return ResponseEntity.badRequest().body("⚠️ Já existe um patrimônio com esse número.");
                    }

                    // 🔄 Atualiza os campos básicos
                    patrimonio.setModelo(patrimonioAtualizado.getModelo());
                    patrimonio.setNumeroPatrimonio(patrimonioAtualizado.getNumeroPatrimonio());
                    patrimonio.setSubgrupo(patrimonioAtualizado.getSubgrupo());

                    // 🛠️ Se for um equipamento, atualiza a descrição também
                    if (patrimonio instanceof Equipamento && patrimonioAtualizado instanceof Equipamento) {
                        ((Equipamento) patrimonio).setDescricao(((Equipamento) patrimonioAtualizado).getDescricao());
                    }

                    // 💾 Salva e retorna o patrimônio atualizado
                    Patrimonio atualizado = patrimonioService.salvarPatrimonio(patrimonio);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());  // ❌ Se não encontrado, retorna 404
    }

    // 🗑️ Excluir patrimônio
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirPatrimonio(@PathVariable Integer id) {
        return patrimonioService.buscarPorId(id)
                .map(patrimonio -> {
                    patrimonioService.excluirPatrimonio(id);
                    return ResponseEntity.ok("✅ Patrimônio excluído com sucesso!");  // ✅ Mensagem de sucesso
                })
                .orElseGet(() -> ResponseEntity.notFound().build());  // ❌ Se não encontrado
    }
}
