const API_URL = "http://10.10.6.38:8080/veiculos";

document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM completamente carregado!");
    const botaoAbrirCadastro = document.getElementById("abrirCadastro");
    const botaoFecharCadastro = document.getElementById("fecharCadastro");

    if (botaoAbrirCadastro) {
        botaoAbrirCadastro.addEventListener("click", abrirCadastro);
    } else {
        console.error("Erro: Botão de abrir cadastro não encontrado!");
    }

    if (botaoFecharCadastro) {
        botaoFecharCadastro.addEventListener("click", fecharFormulario);
    } else {
        console.error("Erro: Botão de fechar cadastro não encontrado!");
    }

    const fecharEdicao = document.getElementById("fecharEdicao");
    if (fecharEdicao) {
        fecharEdicao.addEventListener("click", fecharModalEdicao);
    }

    listarVeiculos();
});


function abrirCadastro() {
    document.getElementById("modalTipoVeiculo").style.display = "flex";
}

function limparCamposCadastro() {
    console.log("🧹 Limpando campos do formulário de cadastro...");

    // Obtendo os campos básicos
    const modeloInput = document.getElementById("modelo");
    const fabricanteInput = document.getElementById("fabricante");
    const anoInput = document.getElementById("ano");
    const precoInput = document.getElementById("preco");
    const corInput = document.getElementById("cor");

    if (modeloInput) modeloInput.value = "";
    if (fabricanteInput) fabricanteInput.value = "";
    if (anoInput) anoInput.value = "";
    if (precoInput) precoInput.value = "";
    if (corInput) corInput.value = "";

    console.log("✔️ Campos básicos resetados!");

    // Obtendo os campos extras
    const quantidadePortasInput = document.getElementById("quantidadePortas");
    const tipoCombustivelInput = document.getElementById("tipoCombustivel");
    const cilindradasInput = document.getElementById("cilindradas");

    if (quantidadePortasInput) quantidadePortasInput.value = "";
    if (tipoCombustivelInput) tipoCombustivelInput.value = "";
    if (cilindradasInput) cilindradasInput.value = "";

    console.log("✔️ Campos extras resetados!");
}

async function abrirModalEdicao(id) {
    try {
        console.log("📝 Abrindo edição para o ID:", id);

        const modal = document.getElementById("modalEdicao");
        if (!modal) {
            console.error("❌ Erro: Modal de edição não encontrado!");
            return;
        }

        const editId = document.getElementById("editId");
        const editModelo = document.getElementById("editModelo");
        const editFabricante = document.getElementById("editFabricante");
        const editAno = document.getElementById("editAno");
        const editPreco = document.getElementById("editPreco");
        const editCor = document.getElementById("editCor");
        const editTipo = document.getElementById("editTipo");
        const editExtraCampos = document.getElementById("editExtraCampos");

        if (!editId || !editModelo || !editFabricante || !editAno || 
            !editPreco || !editCor || !editTipo || !editExtraCampos) {
            console.error("❌ Erro: Um ou mais elementos do modal de edição não foram encontrados!");
            return;
        }

        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Erro ao buscar veículo para edição");

        const veiculo = await response.json();
        console.log("🔍 Dados recebidos para edição:", veiculo);

        editId.value = veiculo.id;
        editModelo.value = veiculo.modelo;
        editFabricante.value = veiculo.fabricante;
        editAno.value = veiculo.ano;
        editPreco.value = veiculo.preco;
        editCor.value = veiculo.cor || "Não informado";

        editExtraCampos.innerHTML = "";

        if (veiculo.hasOwnProperty("quantidadePortas")) {
            editTipo.value = "carro";
            editExtraCampos.innerHTML = `
                <label>Quantidade de Portas:</label>
                <input type="number" id="editQuantidadePortas" value="${veiculo.quantidadePortas}">
            `;
        } else if (veiculo.hasOwnProperty("cilindradas")) {
            editTipo.value = "moto";
            editExtraCampos.innerHTML = `
                <label>Cilindradas:</label>
                <input type="number" id="editCilindradas" value="${veiculo.cilindradas}">
            `;
        }

        modal.style.display = "flex";

    } catch (error) {
        console.error("❌ Erro ao abrir modal de edição:", error);
    }
}

function abrirFormulario(tipo) {
    console.log(`🆕 Abrindo formulário para cadastrar: ${tipo}`);

    fecharModalTipoVeiculo();

    const modalCadastro = document.getElementById("modalCadastro");
    const extraCampos = document.getElementById("extraCampos");

    if (modalCadastro) {
        modalCadastro.style.display = "flex";
    }

    if (extraCampos) {
        extraCampos.innerHTML = "";
        if (tipo === "carro") {
            extraCampos.innerHTML = `
                <label>Quantidade de Portas:</label>
                <input type="number" id="quantidadePortas">

                <label>Tipo de Combustível:</label>
                <input type="text" id="tipoCombustivel">
            `;
        } else if (tipo === "moto") {
            extraCampos.innerHTML = `
                <label>Cilindradas:</label>
                <input type="number" id="cilindradas">
            `;
        }
        setTimeout(limparCamposCadastro, 50);
    }
}

function fecharModalTipoVeiculo() {
    document.getElementById("modalTipoVeiculo").style.display = "none";
}

function abrirModalTipoVeiculo() {
    document.getElementById("modalTipoVeiculo").classList.remove("hidden");
}

function fecharModalEdicao() {
    document.getElementById("modalEdicao").style.display = "none";
}

function fecharFormulario() {
    document.getElementById("modalCadastro").style.display = "none";
}

function fecharModalDetalhes() {
    document.getElementById("modalDetalhes").style.display = "none";
}

async function listarVeiculos(veiculos = null) {
    try {
        // Se "veiculos" for null, buscamos na API
        if (!veiculos) {
            const response = await fetch(API_URL);
            veiculos = await response.json();
        }

        console.log("📢 Dados recebidos para exibição:", veiculos);

        const tabela = document.getElementById("tabelaVeiculos");
        if (!tabela) {
            console.error("❌ ERRO: Tabela não encontrada no HTML!");
            return;
        }

        tabela.innerHTML = ""; // ✅ Limpa a tabela antes de adicionar os novos dados

        veiculos.forEach(veiculo => {
            let row = tabela.insertRow();
            row.innerHTML = `
                <td>${veiculo.id}</td>
                <td>${veiculo.modelo}</td>
                <td>${veiculo.fabricante}</td>
                <td>${veiculo.ano}</td>
                <td>${veiculo.cor || "Não informado"}</td>
                <td>R$ ${veiculo.preco.toLocaleString()}</td>
                <td>
                    <button class="btn btn-detalhes" onclick="abrirDetalhes(${veiculo.id})">🔍 Detalhes</button>
                    <button class="btn btn-editar" onclick="abrirModalEdicao(${veiculo.id})">✏️ Editar</button>
                    <button class="btn btn-danger" onclick="excluirVeiculo(${veiculo.id})">🗑️ Excluir</button>
                </td>
            `;
        });

    } catch (error) {
        console.error("❌ Erro ao listar veículos:", error);
    }
}

async function abrirDetalhes(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Erro ao buscar detalhes do veículo");

        const veiculo = await response.json();
        document.getElementById("detalhesId").innerText = veiculo.id;
        document.getElementById("detalhesModelo").innerText = veiculo.modelo;
        document.getElementById("detalhesFabricante").innerText = veiculo.fabricante;
        document.getElementById("detalhesAno").innerText = veiculo.ano;
        document.getElementById("detalhesPreco").innerText = veiculo.preco ? `R$ ${veiculo.preco.toFixed(2)}` : "Não informado";
        document.getElementById("detalhesCor").innerText = veiculo.cor || "Não informado";

        let extraDetalhes = document.getElementById("extraDetalhes");
        extraDetalhes.innerHTML = "";

        if (veiculo.quantidadePortas) {
            extraDetalhes.innerHTML = `<p><strong>Quantidade de Portas:</strong> ${veiculo.quantidadePortas}</p>
                                       <p><strong>Tipo de Combustível:</strong> ${veiculo.tipoCombustivel}</p>`;
        } else if (veiculo.cilindradas) {
            extraDetalhes.innerHTML = `<p><strong>Cilindradas:</strong> ${veiculo.cilindradas}</p>`;
        }

        document.getElementById("modalDetalhes").style.display = "flex";
    } catch (error) {
        console.error("Erro ao abrir modal de detalhes:", error);
    }
}

async function buscarVeiculos() {
    const modelo = document.getElementById("buscaModelo")?.value.trim() || "";
    const fabricante = document.getElementById("buscaFabricante")?.value.trim() || "";
    const cor = document.getElementById("buscaCor")?.value.trim() || "";
    const ano = document.getElementById("buscaAno")?.value.trim() || "";
    const id = document.getElementById("buscaId")?.value.trim() || "";

    const params = new URLSearchParams();
    if (id) params.append("id", id);
    if (modelo) params.append("modelo", modelo);
    if (fabricante) params.append("fabricante", fabricante);
    if (cor) params.append("cor", cor);
    if (ano) params.append("ano", ano);

    try {
        console.log("📢 Buscando veículos com os parâmetros:", params.toString());

        const response = await fetch(`${API_URL}/busca?${params.toString()}`);
        if (!response.ok) throw new Error(`Erro ${response.status}: ${response.statusText}`);

        const data = await response.json();
        console.log("🔍 Resposta da API:", data);

        if (!Array.isArray(data)) {
            throw new Error("❌ A resposta da API não é um array válido!");
        }

        listarVeiculos(data);

    } catch (error) {
        console.error("❌ Erro ao buscar veículos:", error);
        alert("Erro ao buscar veículos. Verifique os logs para mais detalhes.");
    }
}


async function cadastrarVeiculo() {
    const modelo = document.getElementById("modelo").value;
    const fabricante = document.getElementById("fabricante").value;
    const ano = parseInt(document.getElementById("ano").value);
    const preco = parseFloat(document.getElementById("preco").value);
    const cor = document.getElementById("cor").value;

    const tipo = document.getElementById("extraCampos").innerHTML.includes("Portas") ? "carro" : "moto";
    let extraCampo = tipo === "carro"
        ? parseInt(document.getElementById("quantidadePortas").value)
        : parseInt(document.getElementById("cilindradas").value);

    const veiculo = {
        modelo,
        fabricante,
        ano,
        preco,
        cor,
        ...(tipo === "carro" ? { quantidadePortas: extraCampo, tipoCombustivel: document.getElementById("tipoCombustivel").value } : { cilindradas: extraCampo })
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(veiculo),
        });

        if (!response.ok) throw new Error("Erro ao cadastrar veículo");

        alert("Veículo cadastrado com sucesso!");
        fecharFormulario();
        listarVeiculos();
    } catch (error) {
        console.error("Erro ao cadastrar veículo:", error);
    }
}

async function excluirVeiculo(id) {
    if (!confirm("Tem certeza que deseja excluir este veículo?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });

        if (!response.ok) throw new Error("Erro ao excluir veículo");

        alert("Veículo excluído com sucesso!");
        listarVeiculos();
    } catch (error) {
        console.error("Erro ao excluir veículo:", error);
    }
}
