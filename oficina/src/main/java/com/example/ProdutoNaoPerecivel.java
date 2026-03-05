package com.example;
public class ProdutoNaoPerecivel extends Produto {
    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro, double quantidadeEmEstoque) {
        super(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto, double quantidadeEmEstoque) {
        super(descricao, precoCusto, quantidadeEmEstoque);
    }

    
    @Override
    public double valorDeVenda() {
        return super.precoCusto * (1 + super.margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        // 1; descrição;preçoDeCusto;margemDeLucro
        // Usamos String.format com Locale.US ou replace para garantir consistência.
        return String.format("1;%s;%.2f;%.2f", 
            this.descricao, this.precoCusto, this.margemLucro, this.quantidadeEmEstoque);
    }
}
