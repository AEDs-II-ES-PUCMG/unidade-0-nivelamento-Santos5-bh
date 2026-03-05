package com.example;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto {

	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	protected double quantidadeEmEstoque;

	/**
	 * Inicializador privado. Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00, 0.0
	 * 
	 * @param desc        Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto  Preço do produto (mínimo 0.01)
	 * @param margemLucro Margem de lucro (mínimo 0.01)
	 * @param quantidadeEmEstoque Quantos produtos (minimo 1.0)
	 */
	private void init(String desc, double precoCusto, double margemLucro, double quantidadeEmEstoque) {

		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
			this.quantidadeEmEstoque = quantidadeEmEstoque;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}

	/**
	 * Construtor completo. Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00, 0.0
	 * 
	 * @param desc        Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto  Preço do produto (mínimo 0.01)
	 * @param margemLucro Margem de lucro (mínimo 0.01)
	 * @param quantidadeEmEstoque Quantos produtos (minimo 1.0)
	 */
	public Produto(String desc, double precoCusto, double margemLucro, double quantidadeEmEstoque) {
		init(desc, precoCusto, margemLucro, quantidadeEmEstoque);
	}

	/**
	 * Construtor sem margem de lucro - fica considerado o valor padrão de margem de
	 * lucro.
	 * Os valores default, em caso de erro, são:
	 * "Produto sem descrição", R$ 0.00
	 * 
	 * @param desc       Descrição do produto (mínimo de 3 caracteres)
	 * @param precoCusto Preço do produto (mínimo 0.01)
	 */
	public Produto(String desc, double precoCusto, double quantidadeEmEstoque) {
		init(desc, precoCusto, MARGEM_PADRAO, quantidadeEmEstoque);
	}

	/**
	 * Retorna o valor de venda do produto, considerando seu preço de custo e margem
	 * de lucro.
	 * 
	 * @return Valor de venda do produto (double, positivo)
	 */
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}

	/**
	 * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
	 * 
	 * @return String com o formato:
	 *         [NOME]: R$ [VALOR DE VENDA]
	 */
	@Override
	public String toString() {

		NumberFormat moeda = NumberFormat.getCurrencyInstance();

		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}

	/**
	 * Igualdade de produtos: caso possuam o mesmo nome/descrição.
	 * 
	 * @param obj Outro produto a ser comparado
	 * @return booleano true/false conforme o parâmetro possua a descrição igual ou
	 *         não a este produto.
	 */
	@Override
	public boolean equals(Object obj) {
		Produto outro = (Produto) obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	/**
	 * Gera uma linha de texto a partir dos dados do produto
	 * @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	 */
	public abstract String gerarDadosTexto();

	/**
	 * Cria um produto a partir de uma linha de dados em formato texto.
	 * @param linha Linha com os dados do produto a ser criado.
	 * @return Um produto com os dados recebidos
	 */
	static Produto criarDoTexto(String linha) {
		String[] dados = linha.split(";");
		
		// Remove espaços em branco e substitui vírgula por ponto para garantir o parse do double
		int tipo = Integer.parseInt(dados[0].trim());
		String descricao = dados[1].trim();
		double precoCusto = Double.parseDouble(dados[2].trim().replace(",", "."));
		double margemLucro = Double.parseDouble(dados[3].trim().replace(",", "."));
		double quantidadeEmEstoque = Double.parseDouble(dados[4].trim().replace(",", "."));

		Produto novoProduto = null;

		if (tipo == 1) {
			novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
		} else if (tipo == 2) {
			// Formato esperado: dd/MM/yyyy
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dataValidade = LocalDate.parse(dados[5].trim(), formatter);
			novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade, quantidadeEmEstoque);
		} else {
			throw new IllegalArgumentException("Tipo de produto inválido no arquivo de dados.");
		}

		return novoProduto;
	}
}