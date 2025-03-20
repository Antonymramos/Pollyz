package br.com.projeto.models;

public class Veiculo {
    private Integer id;
    private String modelo;
    private String fabricante;
    private int ano;
    private double preco;
    private String cor;
    private String numeroPatrimonio; // Campo obrigatório
    private String tipo; // Adicionado para mapear o tipo
    private String subgrupo;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSubgrupo(){ return subgrupo;}
    public void setSubgrupo(String subgrupo) { this.subgrupo = subgrupo;}
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getNumeroPatrimonio() { return numeroPatrimonio; }
    public void setNumeroPatrimonio(String numeroPatrimonio) { this.numeroPatrimonio = numeroPatrimonio; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", ano=" + ano +
                ", preco=" + preco +
                ", cor='" + cor + '\'' +
                ", numeroPatrimonio='" + numeroPatrimonio + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
