package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class ProdutoPerecivelTest {
    
    @Test
    public void calcularPrecoSemDescontoCorretamente(){
        ProdutoPerecivel produto = new ProdutoPerecivel("Produto A", 10.0, 0.5, LocalDate.now().plusDays(10));
        assertEquals(15.0, produto.valorDeVenda());
    }

    @Test
    public void calcularPrecoComDescontoCorretamente(){
        ProdutoPerecivel produto = new ProdutoPerecivel("Produto B", 10.0, 0.5, LocalDate.now().plusDays(2));
        assertEquals(11.25, produto.valorDeVenda());
    }

    @Test
    public void naoCriarProdutoForaDaValidade(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProdutoPerecivel("Produto C", 10.0, 0.5, LocalDate.now().minusDays(2));
        });
        assertEquals("A data de validade deve ser posterior a data atual", exception.getMessage());
    }

}
