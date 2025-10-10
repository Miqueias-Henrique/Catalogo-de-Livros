import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException; // Importado para tratar erros de entrada
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Versão ATUALIZADA do aplicativo de catálogo de livros.
 * Foco em robustez, clareza do código e melhor experiência de usuário no console.
 */
public class CatalogoLivrosApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InterfaceArvore catalogo = escolherArvore(scanner);
        final String NOME_ARQUIVO = "livros_catalogo.txt";

        int escolha = -1;
        do {
            exibirMenu();
            try {
                escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (escolha) {
                    case 1: inserirLivro(catalogo, scanner); break;
                    case 2: buscarLivro(catalogo, scanner); break;
                    case 3: removerLivro(catalogo, scanner); break;
                    case 4: listarLivros(catalogo, scanner); break;
                    case 5: carregarDeArquivo(catalogo, NOME_ARQUIVO); break;
                    case 6: exibirEstatisticas(catalogo); break;
                    case 7: modificarLivro(catalogo, scanner); break; // NOVA OPÇÃO
                    case 0: salvarEFechar(catalogo, NOME_ARQUIVO); break;
                    default: System.out.println(">>> Opcao invalida. Tente novamente."); break;
                }
            } catch (InputMismatchException e) {
                System.out.println(">>> ERRO: Por favor, digite um numero valido para a opcao do menu.");
                scanner.nextLine(); // Limpa o buffer do scanner para evitar um loop infinito
                escolha = -1; // Garante que o loop continue
            }
        } while (escolha != 0);

        scanner.close();
    }

    private static InterfaceArvore escolherArvore(Scanner scanner) {
        System.out.println("=============================================");
        System.out.println("  BEM-VINDO AO SISTEMA DE CATALOGO DE LIVROS");
        System.out.println("=============================================");
        System.out.println("Escolha a estrutura de arvore para o catalogo:");
        System.out.println("1. BST (Arvore Binaria de Busca Padrao)");
        System.out.println("2. AVL (Arvore Auto-Balanceada)");

        int tipo = 0;
        while(tipo != 1 && tipo != 2) {
            System.out.print("Sua escolha: ");
            try {
                tipo = scanner.nextInt();
                if(tipo != 1 && tipo != 2) System.out.println(">>> Opcao invalida. Digite 1 ou 2.");
            } catch (InputMismatchException e) {
                System.out.println(">>> ERRO: Digite um numero (1 ou 2).");
            } finally {
                scanner.nextLine(); // Limpa o buffer
            }
        }

        if (tipo == 2) {
            System.out.println("\n--- Catalogo iniciado com Arvore AVL ---");
            return new AVL();
        }
        System.out.println("\n--- Catalogo iniciado com Arvore BST ---");
        return new BST();
    }

    private static void exibirMenu() {
        System.out.println("         MENU PRINCIPAL                   ");
        System.out.println(" 1. Inserir novo livro                    ");
        System.out.println(" 2. Buscar livro por ID                   ");
        System.out.println(" 3. Remover livro por ID                  ");
        System.out.println(" 4. Listar livros (diferentes percursos)  ");
        System.out.println(" 5. Carregar catalogo do arquivo          ");
        System.out.println(" 6. Exibir Estatisticas da Arvore         ");
        System.out.println(" 7. Modificar um Livro                    "); // NOVA OPÇÃO
        System.out.println(" 0. Salvar Alteracoes e Sair              ");
        System.out.print("Sua escolha: ");
    }

    private static void inserirLivro(InterfaceArvore catalogo, Scanner scanner) {
        try {
            System.out.println("\n--- Inserir Novo Livro ---");
            System.out.print("Digite o ID (numero): ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha
            System.out.print("Digite o Titulo: ");
            String titulo = scanner.nextLine();
            System.out.print("Digite o Autor: ");
            String autor = scanner.nextLine();
            catalogo.inserir(new Livro(id, titulo, autor));
            System.out.println(">>> Livro '" + titulo + "' inserido com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println(">>> ERRO: O ID deve ser um numero. Operacao cancelada.");
            scanner.nextLine(); // Limpar buffer em caso de erro
        }
    }

    private static void buscarLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Buscar Livro por ID ---");
        System.out.print("Digite o ID para busca: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            Livro livro = catalogo.buscar(id);
            if (livro != null) {
                System.out.println(">>> Encontrado: " + livro);
            } else {
                System.out.println(">>> Livro com ID " + id + " nao foi encontrado.");
            }
            System.out.println(catalogo.getEstatisticas().split("\\|")[2].trim());
        } catch (InputMismatchException e) {
            System.out.println(">>> ERRO: O ID deve ser um numero.");
            scanner.nextLine();
        }
    }

    private static void removerLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Remover Livro por ID ---");
        System.out.print("Digite o ID para remocao: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            catalogo.remover(id);
            System.out.println(">>> Operacao de remocao para o ID " + id + " concluida.");
        } catch (InputMismatchException e) {
            System.out.println(">>> ERRO: O ID deve ser um numero.");
            scanner.nextLine();
        }
    }

    private static void listarLivros(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Escolha o tipo de listagem ---");
        System.out.println("1. In-Ordem (ordenado por ID)");
        System.out.println("2. Pre-Ordem (raiz, esquerda, direita)");
        System.out.println("3. Pos-Ordem (esquerda, direita, raiz)");
        System.out.println("4. Em Largura (por nivel)");
        System.out.print("Sua escolha: ");
        try {
            int tipo = scanner.nextInt();
            scanner.nextLine();
            System.out.println("\n--- LISTA DE LIVROS ---");
            switch (tipo) {
                case 1: catalogo.inOrdem(); break;
                case 2: catalogo.preOrdem(); break;
                case 3: catalogo.posOrdem(); break;
                case 4: catalogo.emLargura(); break;
                default: System.out.println("Opcao invalida."); break;
            }
            System.out.println("--- FIM DA LISTA ---");
        } catch (InputMismatchException e) {
            System.out.println(">>> ERRO: Escolha deve ser um numero.");
            scanner.nextLine();
        }
    }

    private static void carregarDeArquivo(InterfaceArvore catalogo, String nomeArquivo) {
        System.out.println("\n--- Carregando do Arquivo ---");
        try (Scanner fileScanner = new Scanner(new File(nomeArquivo))) {
            int count = 0;
            while (fileScanner.hasNextLine()) {
                String[] dados = fileScanner.nextLine().split(",");
                if (dados.length == 3) {
                    int id = Integer.parseInt(dados[0].trim());
                    String titulo = dados[1].trim();
                    String autor = dados[2].trim();
                    catalogo.inserir(new Livro(id, titulo, autor));
                    count++;
                }
            }
            System.out.println(">>> " + count + " livros carregados do arquivo '" + nomeArquivo + "'!");
        } catch (FileNotFoundException e) {
            System.out.println(">>> ERRO: Arquivo '" + nomeArquivo + "' nao encontrado na raiz do projeto.");
        } catch (Exception e) {
            System.out.println(">>> ERRO ao ler o arquivo: " + e.getMessage());
        }
    }

    private static void exibirEstatisticas(InterfaceArvore catalogo) {
        System.out.println("\n--- Estatisticas da Arvore ---");
        System.out.println(catalogo.getEstatisticas());
        System.out.println("------------------------------");
    }

    private static void salvarEFechar(InterfaceArvore catalogo, String nomeArquivo) {
        try {
            catalogo.salvarParaArquivo(nomeArquivo);
            System.out.println("\n>>> Alteracoes salvas com sucesso no arquivo '" + nomeArquivo + "'!");
            System.out.println("Saindo do sistema...");
        } catch (Exception e) {
            System.out.println("\n>>> ERRO ao salvar o arquivo: " + e.getMessage());
        }
    }

    private static void modificarLivro(InterfaceArvore catalogo, Scanner scanner) {
        System.out.println("\n--- Modificar Livro ---");
        System.out.print("Digite o ID do livro que deseja modificar: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            Livro livro = catalogo.buscar(id);

            // Verifica se o livro foi encontrado
            if (livro == null) {
                System.out.println(">>> ERRO: Livro com ID " + id + " nao encontrado.");
                return; // Encerra o método se não encontrar o livro
            }

            System.out.println("Livro encontrado: " + livro);
            System.out.println("O que voce deseja modificar?");
            System.out.println("1. Titulo");
            System.out.println("2. Autor");
            System.out.println("0. Cancelar");
            System.out.print("Sua escolha: ");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (escolha) {
                case 1:
                    System.out.print("Digite o NOVO Titulo: ");
                    String novoTitulo = scanner.nextLine();
                    if (novoTitulo.trim().isEmpty()) {
                        System.out.println(">>> ERRO: O titulo nao pode estar em branco. Operacao cancelada.");
                    } else {
                        livro.titulo = novoTitulo; // Modifica o título diretamente no objeto
                        System.out.println(">>> Titulo atualizado com sucesso!");
                    }
                    break;
                case 2:
                    System.out.print("Digite o NOVO Autor: ");
                    String novoAutor = scanner.nextLine();
                    if (novoAutor.trim().isEmpty()) {
                        System.out.println(">>> ERRO: O autor nao pode estar em branco. Operacao cancelada.");
                    } else {
                        livro.autor = novoAutor; // Modifica o autor diretamente no objeto
                        System.out.println(">>> Autor atualizado com sucesso!");
                    }
                    break;
                case 0:
                    System.out.println(">>> Operacao cancelada.");
                    break;
                default:
                    System.out.println(">>> Opcao invalida.");
                    break;
            }

            if (escolha == 1 || escolha == 2) {
                System.out.println("Lembre-se de usar a opcao 'Salvar e Sair' para tornar a alteracao permanente no arquivo.");
            }

        } catch (InputMismatchException e) {
            System.out.println(">>> ERRO: O ID e a escolha devem ser numeros.");
            scanner.nextLine(); // Limpa o buffer
        }
    }
}

// Interface, Livro, No, BST, NoAVL, AVL (com as modificações necessárias)
// A interface, Livro e No não precisam de mudanças.

interface InterfaceArvore {
    void inserir(Livro livro);
    Livro buscar(int id);
    void remover(int id);
    void preOrdem();
    void inOrdem();
    void posOrdem();
    void emLargura();
    String getEstatisticas();
    void salvarParaArquivo(String nomeArquivo) throws FileNotFoundException;
}

class Livro {
    int id; String titulo; String autor;
    public Livro(int id, String titulo, String autor) { this.id = id; this.titulo = titulo; this.autor = autor; }
    @Override public String toString() { return "ID: " + id + ", Titulo: '" + titulo + "', Autor: '" + autor + "'"; }
}

class No {
    Livro livro; No esquerda; No direita;
    public No(Livro livro) { this.livro = livro; this.esquerda = null; this.direita = null; }
}
// Adicione este método dentro da classe 'public class CatalogoLivrosApp'


class BST implements InterfaceArvore {
    private No raiz;
    private long comparacoesInsercao = 0;
    private long comparacoesBusca = 0;

    @Override public String getEstatisticas() { return "BST Stats | Comparacoes Insercao: " + comparacoesInsercao + " | Comparacoes Busca: " + comparacoesBusca; }
    @Override public void salvarParaArquivo(String nomeArquivo) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(nomeArquivo))) { _salvarRecursivo(raiz, writer); }
    }
    private void _salvarRecursivo(No no, PrintWriter writer) {
        if (no != null) {
            _salvarRecursivo(no.esquerda, writer);
            writer.println(no.livro.id + "," + no.livro.titulo + "," + no.livro.autor);
            _salvarRecursivo(no.direita, writer);
        }
    }
    @Override public void inserir(Livro livro) { raiz = _inserir(raiz, livro); }
    private No _inserir(No no, Livro livro) { comparacoesInsercao++; if (no == null) return new No(livro); if (livro.id < no.livro.id) no.esquerda = _inserir(no.esquerda, livro); else if (livro.id > no.livro.id) no.direita = _inserir(no.direita, livro); return no; }
    @Override public Livro buscar(int id) { comparacoesBusca = 0; No no = _buscar(raiz, id); return (no != null) ? no.livro : null; }
    private No _buscar(No no, int id) { comparacoesBusca++; if (no == null || no.livro.id == id) return no; return (id < no.livro.id) ? _buscar(no.esquerda, id) : _buscar(no.direita, id); }
    @Override public void remover(int id) { raiz = _remover(raiz, id); }
    private No _remover(No no, int id) { if (no == null) return null; if (id < no.livro.id) no.esquerda = _remover(no.esquerda, id); else if (id > no.livro.id) no.direita = _remover(no.direita, id); else { if (no.esquerda == null) return no.direita; if (no.direita == null) return no.esquerda; No s = _minValor(no.direita); no.livro = s.livro; no.direita = _remover(no.direita, s.livro.id); } return no; }
    private No _minValor(No no) { while (no.esquerda != null) no = no.esquerda; return no; }
    @Override public void preOrdem() { _preOrdem(raiz); }
    private void _preOrdem(No no) { if (no != null) { System.out.println(no.livro); _preOrdem(no.esquerda); _preOrdem(no.direita); } }
    @Override public void inOrdem() { _inOrdem(raiz); }
    private void _inOrdem(No no) { if (no != null) { _inOrdem(no.esquerda); System.out.println(no.livro); _inOrdem(no.direita); } }
    @Override public void posOrdem() { _posOrdem(raiz); }
    private void _posOrdem(No no) { if (no != null) { _posOrdem(no.esquerda); _posOrdem(no.direita); System.out.println(no.livro); } }
    @Override public void emLargura() { if (raiz == null) return; Queue<No> q = new LinkedList<>(); q.add(raiz); while (!q.isEmpty()) { No n = q.poll(); System.out.println(n.livro); if (n.esquerda != null) q.add(n.esquerda); if (n.direita != null) q.add(n.direita); } }
}

class NoAVL {
    Livro livro; int altura; NoAVL esquerda, direita;
    public NoAVL(Livro livro) { this.livro = livro; this.altura = 1; }
}

class AVL implements InterfaceArvore {
    private NoAVL raiz;
    private long rotacoes = 0, comparacoesInsercao = 0, comparacoesBusca = 0;

    @Override public String getEstatisticas() { return "AVL Stats  | Rotacoes: " + rotacoes + " | Comparacoes Insercao: " + comparacoesInsercao + " | Comparacoes Busca: " + comparacoesBusca; }
    @Override public void salvarParaArquivo(String nomeArquivo) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(nomeArquivo))) { _salvarRecursivo(raiz, writer); }
    }
    private void _salvarRecursivo(NoAVL no, PrintWriter writer) { if (no != null) { _salvarRecursivo(no.esquerda, writer); writer.println(no.livro.id + "," + no.livro.titulo + "," + no.livro.autor); _salvarRecursivo(no.direita, writer); } }
    private int altura(NoAVL no) { return (no == null) ? 0 : no.altura; }
    private void atualizarAltura(NoAVL no) { no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita)); }
    private int fatorBalanco(NoAVL no) { return (no == null) ? 0 : altura(no.direita) - altura(no.esquerda); }
    private NoAVL rotacaoDireita(NoAVL z) { rotacoes++; NoAVL y = z.esquerda; z.esquerda = y.direita; y.direita = z; atualizarAltura(z); atualizarAltura(y); return y; }
    private NoAVL rotacaoEsquerda(NoAVL z) { rotacoes++; NoAVL y = z.direita; z.direita = y.esquerda; y.esquerda = z; atualizarAltura(z); atualizarAltura(y); return y; }
    private NoAVL balancear(NoAVL no) { atualizarAltura(no); int balanco = fatorBalanco(no); if (balanco < -1) { if (fatorBalanco(no.esquerda) > 0) no.esquerda = rotacaoEsquerda(no.esquerda); return rotacaoDireita(no); } if (balanco > 1) { if (fatorBalanco(no.direita) < 0) no.direita = rotacaoDireita(no.direita); return rotacaoEsquerda(no); } return no; }
    @Override public void inserir(Livro livro) { raiz = _inserir(raiz, livro); }
    private NoAVL _inserir(NoAVL no, Livro livro) { comparacoesInsercao++; if (no == null) return new NoAVL(livro); if (livro.id < no.livro.id) no.esquerda = _inserir(no.esquerda, livro); else if (livro.id > no.livro.id) no.direita = _inserir(no.direita, livro); else return no; return balancear(no); }
    @Override public Livro buscar(int id) { comparacoesBusca = 0; NoAVL no = _buscar(raiz, id); return (no != null) ? no.livro : null; }
    private NoAVL _buscar(NoAVL no, int id) { comparacoesBusca++; if (no == null || no.livro.id == id) return no; return (id < no.livro.id) ? _buscar(no.esquerda, id) : _buscar(no.direita, id); }
    @Override public void remover(int id) { raiz = _remover(raiz, id); }
    private NoAVL _remover(NoAVL no, int id) { if (no == null) return null; if (id < no.livro.id) no.esquerda = _remover(no.esquerda, id); else if (id > no.livro.id) no.direita = _remover(no.direita, id); else { if (no.esquerda == null || no.direita == null) no = (no.esquerda != null) ? no.esquerda : no.direita; else { NoAVL s = _minValor(no.direita); no.livro = s.livro; no.direita = _remover(no.direita, s.livro.id); } } return (no == null) ? null : balancear(no); }
    private NoAVL _minValor(NoAVL no) { while (no.esquerda != null) no = no.esquerda; return no; }
    @Override public void preOrdem() { _preOrdem(raiz); }
    private void _preOrdem(NoAVL no) { if (no != null) { System.out.println(no.livro); _preOrdem(no.esquerda); _preOrdem(no.direita); } }
    @Override public void inOrdem() { _inOrdem(raiz); }
    private void _inOrdem(NoAVL no) { if (no != null) { _inOrdem(no.esquerda); System.out.println(no.livro); _inOrdem(no.direita); } }
    @Override public void posOrdem() { _posOrdem(raiz); }
    private void _posOrdem(NoAVL no) { if (no != null) { _posOrdem(no.esquerda); _posOrdem(no.direita); System.out.println(no.livro); } }

    // ATUALIZADO: Implementação completa do percurso em largura para AVL
    @Override
    public void emLargura() {
        if (raiz == null) return;
        Queue<NoAVL> fila = new LinkedList<>();
        fila.add(raiz);
        while (!fila.isEmpty()) {
            NoAVL no = fila.poll();
            System.out.println(no.livro);
            if (no.esquerda != null) fila.add(no.esquerda);
            if (no.direita != null) fila.add(no.direita);
        }
    }
}