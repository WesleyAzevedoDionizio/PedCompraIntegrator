package PedCompraIntegrator.dto;

public record Empresa (

     String nome,
     String codigo

){
    public String toString(){
        return codigo
        + " -- "
        + nome;
    }
}

