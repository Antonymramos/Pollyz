package br.com.projeto.models;

public class Moto extends Veiculo {
    private Integer cilindrada;

    public Integer getCilindrada() { return cilindrada; }
    public void setCilindrada(Integer cilindrada) { this.cilindrada = cilindrada; }

    @Override
    public String toString() {
        return super.toString() + ", Moto [cilindrada=" + cilindrada + "]";
    }
}
