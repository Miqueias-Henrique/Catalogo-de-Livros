package catalogo;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CatalogoLivrosApp {
    private static final String ARQUIVO_CATALOGO = "livros_catalogo.txt";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            InterfaceArvore catalogo = escolherArvore(scanner);
            int escolha;
            do {
                exibirMenu();
                Integer entrada = lerInteiro(scanner);
                if (entrada == null) {
                    break;
                }
                escolha = entrada;
                switch (escolha) {
                    case 1 -> inserirLivro(catalogo, scanner);
                    case 2 -> buscarLivro(catalogo, scanner);
                    case 3 -> removerLivro(catalogo, scanner);
                    case 4 -> listarLivros(catalogo, scanner);
                    case 5 -> carregarDeArquivo(catalogo, ARQUIVO_CATALOGO);
                    case 6 -> exibirEstatisticas(catalogo);
                    case 7 -> modificarLivro(catalogo, scanner);
                    case 0 -> salvarEFechar(catalogo, ARQUIVO_CATALOGO);
                    default -> System.out.println("Opcao invalida. Tente novamente.");
                }
            } while (escolha != 0);
        }
    }

    private static Integer lerInteiro(Scanner scanner) {
        while (true) {
            if (!scanner.hasNextLine()) {
                return null;
            }
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.print("Digite um numero valido: ");
            }
        }
    }

    private static InterfaceArvore escolherArvore(Scanner scanner) {
        System.out.println("=============================================");
        System.out.println("  SISTEMA DE CATALOGO DE LIVROS");
        System.out.println("=============================================");
        System.out.println("Escolha a estrutura de arvore para o catalogo:");
        System.out.println("1. BST (Arvore Binaria de Busca)");
        System.out.println("2. AVL (Arvore Auto-Balanceada)");

        int tipo = 0;
        while (tipo != 1 && tipo != 2) {
            System.out.print("Sua escolha: ");
            Integer entrada = lerInteiro(scanner);
            if (entrada == null) {
                return new AVL();
            }
            tipo = entrada;
            if (tipo != 1 && tipo != 2) {
                System.out.println("Opcao invalida. Digite 1 ou 2.");
            }
        }

        if (tipo == 2) {
            System.out.println("\nCatalogo iniciado com Arvore AVL.");
            return new AVL();
        }
        System.out.println("\nCatalogo iniciado com Arvore BST.");
        return new BST();
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Inserir novo livro");
        System.out.println("2. Buscar livro por ID");
        System.out.println("3. Remover livro por ID");
        System.out.println("4. Listar livros (percursos)");
        System.out.println("5. Carregar catalogo do arquivo");
        System.out.println("6. Exibir estatisticas da arvore");
        System.out.println("7. Modificar um livro");
        System.out.println("0. Salvar alteracoes e sair");
        System.out.print("Sua escolha: ");
    }

    private static void inserirLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Inserir Novo Livro ---");
        System.out.print("ID (numero): ");
        Integer id = lerInteiro(scanner);
        if (id == null) {
            return;
        }
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Autor: ");
        String autor = scanner.nextLine().trim();
        if (titulo.isEmpty() || autor.isEmpty()) {
            System.out.println("Titulo e autor nao podem ficar vazios.");
            return;
        }
        if (catalogo.inserir(new Livro(id, titulo, autor))) {
            System.out.println("Livro '" + titulo + "' inserido com sucesso.");
        } else {
            System.out.println("Ja existe um livro cadastrado com o ID " + id + ".");
        }
    }

    private static void buscarLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Buscar Livro por ID ---");
        System.out.print("ID para busca: ");
        Integer id = lerInteiro(scanner);
        if (id == null) {
            return;
        }
        Livro livro = catalogo.buscar(id);
        if (livro != null) {
            System.out.println("Encontrado: " + livro);
        } else {
            System.out.println("Livro com ID " + id + " nao encontrado.");
        }
        System.out.println("Comparacoes Busca: " + catalogo.getComparacoesBusca());
    }

    private static void removerLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Remover Livro por ID ---");
        System.out.print("ID para remocao: ");
        Integer id = lerInteiro(scanner);
        if (id == null) {
            return;
        }
        if (catalogo.remover(id)) {
            System.out.println("Livro com ID " + id + " removido.");
        } else {
            System.out.println("Livro com ID " + id + " nao encontrado.");
        }
    }

    private static void listarLivros(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Tipo de Listagem ---");
        System.out.println("1. In-Ordem (ordenado por ID)");
        System.out.println("2. Pre-Ordem (raiz, esquerda, direita)");
        System.out.println("3. Pos-Ordem (esquerda, direita, raiz)");
        System.out.println("4. Em Largura (por nivel)");
        System.out.print("Sua escolha: ");
        Integer tipo = lerInteiro(scanner);
        if (tipo == null) {
            return;
        }
        System.out.println("\n--- LISTA DE LIVROS ---");
        switch (tipo) {
            case 1 -> catalogo.inOrdem();
            case 2 -> catalogo.preOrdem();
            case 3 -> catalogo.posOrdem();
            case 4 -> catalogo.emLargura();
            default -> System.out.println("Opcao invalida.");
        }
        System.out.println("--- FIM DA LISTA ---");
    }

    private static void carregarDeArquivo(InterfaceArvore catalogo, String nomeArquivo) {
        System.out.println("\n--- Carregando do Arquivo ---");
        try (Scanner fileScanner = new Scanner(new File(nomeArquivo), StandardCharsets.UTF_8)) {
            int count = 0;
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine().trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] dados = linha.split(",", 3);
                if (dados.length == 3) {
                    int id = Integer.parseInt(dados[0].trim());
                    String titulo = dados[1].trim();
                    String autor = dados[2].trim();
                    if (catalogo.inserir(new Livro(id, titulo, autor))) {
                        count++;
                    }
                }
            }
            System.out.println(count + " livros carregados de '" + nomeArquivo + "'.");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo '" + nomeArquivo + "' nao encontrado.");
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private static void exibirEstatisticas(InterfaceArvore catalogo) {
        System.out.println("\n--- Estatisticas da Arvore ---");
        System.out.println(catalogo.getEstatisticas());
    }

    private static void salvarEFechar(InterfaceArvore catalogo, String nomeArquivo) {
        try {
            catalogo.salvarParaArquivo(nomeArquivo);
            System.out.println("\nAlteracoes salvas em '" + nomeArquivo + "'.");
        } catch (Exception e) {
            System.out.println("\nErro ao salvar o arquivo: " + e.getMessage());
        }
    }

    private static void modificarLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Modificar Livro ---");
        System.out.print("ID do livro: ");
        Integer id = lerInteiro(scanner);
        if (id == null) {
            return;
        }
        Livro livro = catalogo.buscar(id);
        if (livro == null) {
            System.out.println("Livro com ID " + id + " nao encontrado.");
            return;
        }

        System.out.println("Livro encontrado: " + livro);
        System.out.println("1. Alterar titulo");
        System.out.println("2. Alterar autor");
        System.out.println("0. Cancelar");
        System.out.print("Sua escolha: ");
        Integer escolha = lerInteiro(scanner);
        if (escolha == null) {
            return;
        }

        switch (escolha) {
            case 1 -> {
                System.out.print("Novo titulo: ");
                String novoTitulo = scanner.nextLine().trim();
                if (novoTitulo.isEmpty()) {
                    System.out.println("O titulo nao pode ficar em branco.");
                } else {
                    livro.setTitulo(novoTitulo);
                    System.out.println("Titulo atualizado.");
                }
            }
            case 2 -> {
                System.out.print("Novo autor: ");
                String novoAutor = scanner.nextLine().trim();
                if (novoAutor.isEmpty()) {
                    System.out.println("O autor nao pode ficar em branco.");
                } else {
                    livro.setAutor(novoAutor);
                    System.out.println("Autor atualizado.");
                }
            }
            case 0 -> System.out.println("Operacao cancelada.");
            default -> System.out.println("Opcao invalida.");
        }
    }
}
