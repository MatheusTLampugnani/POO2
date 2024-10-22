package com.example;
import java.util.Scanner;

public class ProdutoApp {
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Consultar Produto");  
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    cadastrarProduto(scanner);
                    break;
                case 2:
                    consultarProduto(scanner);
                    break;               
                case 3:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 3);

        scanner.close();
    }

    private static void inativarProduto(Scanner scanner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inativarProduto'");
    }

    private static void cadastrarProduto(Scanner scanner) {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o preço do produto: ");
        double preco = scanner.nextDouble();
        scanner.nextLine(); // Consumir a quebra de linha

        Produto produto = new Produto(nome, preco);
        produtoDAO.cadastrarProduto(produto);
    }

    private static void consultarProduto(Scanner scanner) {
        System.out.print("Digite o ID do produto que deseja consultar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        Produto produto = produtoDAO.consultarProduto(id);
        if (produto != null) {
            System.out.println("Produto encontrado: " + produto);
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
        }
    }
}
