package br.com.projeto.models;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Patrimonio {
    private Integer id;

    @NotBlank(message = "O modelo é obrigatório.")
    @Size(max = 50, message = "O modelo deve ter no máximo 50 caracteres.")
    private String modelo;

    @NotBlank(message = "O número de patrimônio é obrigatório.")
    @Size(max = 50, message = "O número de patrimônio deve ter no máximo 50 caracteres.")
    private String numeroPatrimonio;

    @NotBlank(message = "O subgrupo é obrigatório.")
    @Size(max = 50, message = "O subgrupo deve ter no máximo 50 caracteres.")
    private String subgrupo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getNumeroPatrimonio() { return numeroPatrimonio; }
    public void setNumeroPatrimonio(String numeroPatrimonio) { this.numeroPatrimonio = numeroPatrimonio; }

    public String getSubgrupo() { return subgrupo; }
    public void setSubgrupo(String subgrupo) { this.subgrupo = subgrupo; }

    @Override
    public String toString() {
        return "Patrimonio [id=" + id + ", modelo=" + modelo + ", numeroPatrimonio=" + numeroPatrimonio + ", subgrupo=" + subgrupo + "]";
    }
}
