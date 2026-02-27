package main.java;
import java.time.LocalDate;

public class ProdutoPerecivel extends Produto{
    private static final double DESCONTO = 0.25; // 25% de desconto
    private static final int PRAZO_DESCONTO = 7; // 7 Dias
    private final LocalDate dataDeValidade;

    public ProdutoPerecivel(String descricao, double precoCusto, double margemLucro, LocalDate dataDeValidade) {
        super(descricao, precoCusto, margemLucro);

        // Verificação se a data de validade é posterior a data atual
        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de validade deve ser posterior a data atual");
        }
        
        this.dataDeValidade = dataDeValidade;
    }

    @Override
    public double valorDeVenda(){
        // Verificação se o produto está vencido e retorna um aviso
        if (dataDeValidade.isBefore(LocalDate.now())){
            throw new IllegalStateException("O produto está vencido");
        }
        
        // Desconto se o produto estiver perto de vencer
        if (dataDeValidade.isBefore(LocalDate.now().plusDays(PRAZO_DESCONTO))) {
            return (super.precoCusto - (super.precoCusto * DESCONTO)) * (1 + super.margemLucro);
        }

        return super.precoCusto * (1 + super.margemLucro);
    }
 
    @Override
    public String toString(){
        return super.toString() + " | Validade" + dataDeValidade;
    }
}

