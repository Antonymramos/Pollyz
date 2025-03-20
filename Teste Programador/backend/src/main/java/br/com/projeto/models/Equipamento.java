package br.com.projeto.models;

public class Equipamento extends Patrimonio {
    private String descricao;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return super.toString() + ", Equipamento [descricao=" + descricao + "]";
    }
}
