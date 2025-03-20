package br.com.projeto.models;

public class Carro extends Veiculo {
    private Integer quantidadePortas;
    private String tipoCombustivel;

    public Integer getQuantidadePortas() { return quantidadePortas; }
    public void setQuantidadePortas(Integer quantidadePortas) { this.quantidadePortas = quantidadePortas; }

    public String getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(String tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }

    @Override
    public String toString() {
        return super.toString() + ", Carro [quantidadePortas=" + quantidadePortas + ", tipoCombustivel=" + tipoCombustivel + "]";
    }
}
