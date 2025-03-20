package br.com.projeto.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import br.com.projeto.dao.VeiculoDAO;
import br.com.projeto.models.Carro;
import br.com.projeto.models.Moto;
import br.com.projeto.models.Veiculo;
import br.com.projeto.services.VeiculoService;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoDAO veiculoDAO;
    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoDAO veiculoDAO, VeiculoService veiculoService) {
        this.veiculoDAO = veiculoDAO;
        this.veiculoService = veiculoService;
    }

    // ✅ Buscar veículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVeiculoPorId(@PathVariable int id) {
        Veiculo veiculo = veiculoService.buscarPorId(id);
        if (veiculo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado!");
        }
        return ResponseEntity.ok(veiculo);
    }

    // ✅ Listar todos os veículos
    @GetMapping("")
    public List<Veiculo> listarVeiculos() {
        return veiculoDAO.buscarTodos();
    }

    // ✅ Corrigido erro de duplicidade na URL de busca
    @GetMapping("/busca")
    public List<Veiculo> buscarVeiculos(
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) String modelo,
        @RequestParam(required = false) String fabricante,
        @RequestParam(required = false) String cor,
        @RequestParam(required = false) Integer ano
    ) {
        return veiculoService.buscarVeiculos(id, modelo, fabricante, cor, ano);
    }

    @PostMapping
public ResponseEntity<?> criarVeiculo(@RequestBody Map<String, Object> veiculoMap) {
    Veiculo veiculo;

    String tipo = (String) veiculoMap.get("tipo");
    if (tipo == null || tipo.isEmpty()) {
        return ResponseEntity.badRequest().body("Erro: O campo 'tipo' é obrigatório!");
    }

    if ("Carro".equalsIgnoreCase(tipo)) {
        Carro carro = new Carro();
        carro.setModelo((String) veiculoMap.get("modelo"));
        carro.setFabricante((String) veiculoMap.get("fabricante"));
        carro.setAno(Integer.parseInt(veiculoMap.get("ano").toString()));
        carro.setPreco(Double.parseDouble(veiculoMap.get("preco").toString()));
        carro.setCor((String) veiculoMap.getOrDefault("cor", "Não Informado"));
        carro.setNumeroPatrimonio((String) veiculoMap.get("numeroPatrimonio"));
        carro.setQuantidadePortas(Integer.parseInt(veiculoMap.get("quantidadePortas").toString()));
        carro.setTipoCombustivel((String) veiculoMap.get("tipoCombustivel"));
        veiculo = carro;

    } else if ("Moto".equalsIgnoreCase(tipo)) {
        Moto moto = new Moto();
        moto.setModelo((String) veiculoMap.get("modelo"));
        moto.setFabricante((String) veiculoMap.get("fabricante"));
        moto.setAno(Integer.parseInt(veiculoMap.get("ano").toString()));
        moto.setPreco(Double.parseDouble(veiculoMap.get("preco").toString()));
        moto.setCor((String) veiculoMap.getOrDefault("cor", "Não Informado"));
        moto.setNumeroPatrimonio((String) veiculoMap.get("numeroPatrimonio"));
        moto.setCilindrada(Integer.parseInt(veiculoMap.get("cilindradas").toString()));
        veiculo = moto;

    } else {
        return ResponseEntity.badRequest().body("Erro: Tipo de veículo não reconhecido! Use 'Carro' ou 'Moto'.");
    }

    Veiculo salvo = veiculoService.criarVeiculo(veiculo);
    return ResponseEntity.ok(salvo);
}

    // ✅ Atualizar veículo existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable int id, @RequestBody Veiculo veiculoAtualizado) {
        Veiculo veiculoExistente = veiculoService.buscarPorId(id);
        if (veiculoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado!");
        }

        // Atualiza os campos do veículo existente
        veiculoExistente.setModelo(veiculoAtualizado.getModelo());
        veiculoExistente.setFabricante(veiculoAtualizado.getFabricante());
        veiculoExistente.setAno(veiculoAtualizado.getAno());
        veiculoExistente.setPreco(veiculoAtualizado.getPreco());
        veiculoExistente.setCor(veiculoAtualizado.getCor());

        if (veiculoExistente instanceof Carro && veiculoAtualizado instanceof Carro) {
            ((Carro) veiculoExistente).setQuantidadePortas(((Carro) veiculoAtualizado).getQuantidadePortas());
            ((Carro) veiculoExistente).setTipoCombustivel(((Carro) veiculoAtualizado).getTipoCombustivel());
        } else if (veiculoExistente instanceof Moto && veiculoAtualizado instanceof Moto) {
            ((Moto) veiculoExistente).setCilindrada(((Moto) veiculoAtualizado).getCilindrada());
        }

        Veiculo atualizado = veiculoService.atualizarVeiculo(id, veiculoExistente);
        return ResponseEntity.ok(atualizado);
    }

    // ✅ Excluir veículo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirVeiculo(@PathVariable int id) {
        veiculoService.excluirVeiculo(id);
        return ResponseEntity.ok("Veículo excluído com sucesso");
    }
}
