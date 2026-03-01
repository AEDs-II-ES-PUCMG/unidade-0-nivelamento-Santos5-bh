package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ProdutoNaoPerecivelTest {

    @Test
    public void calculaPrecoCorretamenteSemMargem() {
        ProdutoNaoPerecivel produto = new ProdutoNaoPerecivel("Produto A", 10.0);
        assertEquals(12.0, produto.valorDeVenda(), 1e-6);
    }

    @Test
    public void calculaPrecoCorretamenteComMargem(){
        ProdutoNaoPerecivel produto = new ProdutoNaoPerecivel("Produto B", 5, 0.5);
        assertEquals(7.5, produto.valorDeVenda(), 1e-6);
    }

    @Test
    public void stringComDescricaoEValor(){
        ProdutoNaoPerecivel produto = new ProdutoNaoPerecivel("Produto C", 5, 0.5);
        assertTrue(produto.toString().contains("Produto C"));
        assertTrue(produto.toString().contains("7,50"));
    }
    
    @Test
    public void naoCriarProdutoComPrecoNegativo(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProdutoNaoPerecivel("Produto D", -5, 0.5);
        });
        assertEquals("Valores inválidos para os dados do produto.", exception.getMessage());
    }

    @Test void naoCriarProdutoComMargemNegativa(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProdutoNaoPerecivel("Produto E", 5, -0.5);
        });
        assertEquals("Valores inválidos para os dados do produto.", exception.getMessage());
    }

}
