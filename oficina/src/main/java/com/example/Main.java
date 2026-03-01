package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static final int MAX_NOVOS_PRODUTOS = 10;
    static String nomeArquivoDados = "dadosProdutos.csv";
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;

    static void pausa() {
        System.out.println("\n---------------------------------------------");
        System.out.println("Digite ENTER para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("\n+===========================================+");
        System.out.println("|           COMÉRCIO DE COISINHAS           |");
        System.out.println("+===========================================+");
    }

    static int menu() {
        cabecalho();
        System.out.println("| 1 - Listar todos os produtos              |");
        System.out.println("| 2 - Procurar e listar um produto          |");
        System.out.println("| 3 - Cadastrar novo produto                |");
        System.out.println("| 0 - Sair                                  |");
        System.out.println("+-------------------------------------------+");
        System.out.print("Digite sua opção: ");
        try {
            return Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos;
        File arquivo = new File(nomeArquivoDados);

        try (Scanner leitorArquivo = new Scanner(arquivo)) {
            if (leitorArquivo.hasNextLine()) {
                String linhaN = leitorArquivo.nextLine();
                int n = Integer.parseInt(linhaN.trim());
                
                vetorProdutos = new Produto[n + MAX_NOVOS_PRODUTOS];
                
                while (leitorArquivo.hasNextLine() && quantosProdutos < n) {
                    String linha = leitorArquivo.nextLine();
                    if (!linha.trim().isEmpty()) {
                        vetorProdutos[quantosProdutos] = Produto.criarDoTexto(linha);
                        quantosProdutos++;
                    }
                }
            } else {
                vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Criando nova base.");
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        }
        
        return vetorProdutos;
    }

    static void listarTodosOsProdutos() {
        System.out.println("\n+-------------------------------------------+");
        System.out.println("|             LISTA DE PRODUTOS             |");
        System.out.println("+-------------------------------------------+");
        if (quantosProdutos == 0) {
            System.out.println("|        Nenhum produto cadastrado.         |");
            System.out.println("+-------------------------------------------+");
        } else {
            for (int i = 0; i < quantosProdutos; i++) {
                System.out.println((i + 1) + ". " + produtosCadastrados[i].toString());
            }
            System.out.println("+-------------------------------------------+");
        }
    }

    static void localizarProdutos() {
        System.out.println("\n+-------------------------------------------+");
        System.out.println("|            LOCALIZAR PRODUTO              |");
        System.out.println("+-------------------------------------------+");
        System.out.print("Digite a descrição do produto para busca: ");
        String desc = teclado.nextLine();
        boolean encontrou = false;
        
        // Cria um produto temporário apenas para usar o método equals implementado
        Produto busca = new ProdutoNaoPerecivel(desc, 1.0); 

        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].equals(busca)) {
                System.out.println("Produto encontrado: " + produtosCadastrados[i].toString());
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Produto não encontrado: " + desc);
        }
    }

    static void cadastrarProduto() {
        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("\n+-------------------------------------------+");
            System.out.println("|   Capacidade máxima de produtos atingida  |");
            System.out.println("+-------------------------------------------+");
            return;
        }

        System.out.println("\n+-------------------------------------------+");
        System.out.println("|           CADASTRO DE PRODUTO             |");
        System.out.println("+-------------------------------------------+");
        System.out.print("Tipo (1 - Não Perecível, 2 - Perecível): ");
        int tipo = Integer.parseInt(teclado.nextLine());

        System.out.print("Descrição: ");
        String desc = teclado.nextLine();
        System.out.print("Preço de Custo: ");
        double preco = Double.parseDouble(teclado.nextLine().replace(",", "."));
        System.out.print("Margem de Lucro (ex: 0.3 para 30%): ");
        double margem = Double.parseDouble(teclado.nextLine().replace(",", "."));

        Produto novo = null;
        if (tipo == 1) {
            novo = new ProdutoNaoPerecivel(desc, preco, margem);
        } else if (tipo == 2) {
            System.out.print("Data de Validade (dd/mm/aaaa): ");
            String dataStr = teclado.nextLine();
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            novo = new ProdutoPerecivel(desc, preco, margem, data);
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        produtosCadastrados[quantosProdutos] = novo;
        quantosProdutos++;
        System.out.println("\n>>> Produto cadastrado com sucesso! <<<");
    }

    public static void salvarProdutos(String nomeArquivo) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nomeArquivo))) {
            escritor.println(quantosProdutos);
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.println(produtosCadastrados[i].gerarDadosTexto());
            }
            System.out.println("Dados salvos com sucesso em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        try {
            teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        } catch (Exception e) {
            teclado = new Scanner(System.in);
        }

        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        int opcao = -1;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
            if (opcao != 0) pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}