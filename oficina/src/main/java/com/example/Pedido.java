package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {

    private static final int MAX_PRODUTOS = 10;
    private static final double DESCONTO_PG_A_VISTA = 0.15;

    private Produto[] produtos;
    private LocalDate dataPedido;
    private int quantProdutos;
    private int formaDePagamento;

    /**
     * Construtor da classe Pedido.
     * @param dataPedido Data do pedido.
     * @param formaDePagamento Forma de pagamento (1 para à vista).
     */
    public Pedido(LocalDate dataPedido, int formaDePagamento) {
        this.dataPedido = dataPedido;
        this.formaDePagamento = formaDePagamento;
        this.produtos = new Produto[MAX_PRODUTOS];
        this.quantProdutos = 0;
    }

    /**
     * Inclui um produto no pedido.
     * @param novo Produto a ser incluído.
     * @return true se foi possível incluir, false caso contrário.
     */
    public boolean incluirProduto(Produto novo) {
        if (quantProdutos < MAX_PRODUTOS) {
            produtos[quantProdutos] = novo;
            quantProdutos++;
            return true;
        }
        return false;
    }

    /**
     * Calcula o valor final do pedido, aplicando desconto se for à vista.
     * @return Valor final do pedido.
     */
    public double valorFinal() {
        double total = 0.0;
        for (int i = 0; i < quantProdutos; i++) {
            total += produtos[i].valorDeVenda();
        }
        
        if (formaDePagamento == 1) {
            total = total * (1.0 - DESCONTO_PG_A_VISTA);
        }
        return total;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido realizado em: ").append(dataPedido.format(formatter));
        sb.append(" | Pagamento: ").append(formaDePagamento == 1 ? "À Vista" : "Outro");
        sb.append(" | Qtd Itens: ").append(quantProdutos);
        sb.append(String.format(" | Valor Final: R$ %.2f", valorFinal()));
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pedido outro = (Pedido) obj;
        return this.dataPedido.equals(outro.dataPedido) && 
                this.formaDePagamento == outro.formaDePagamento &&
                this.quantProdutos == outro.quantProdutos;
    }
}