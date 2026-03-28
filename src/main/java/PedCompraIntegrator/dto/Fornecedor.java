package PedCompraIntegrator.dto;

public record Fornecedor (

    String email,
    String nome,
    Integer codigo

)
{
    public String toString(){
        return nome
        + " - Cód:"
        + codigo;
    }
}


