package catalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class BST implements InterfaceArvore {
    private No raiz;
    private long comparacoesInsercao = 0;
    private long comparacoesBusca = 0;

    @Override
    public String getEstatisticas() {
        return "BST Stats | Comparacoes Insercao: " + comparacoesInsercao
                + " | Comparacoes Busca: " + comparacoesBusca;
    }

    @Override
    public long getComparacoesBusca() {
        return comparacoesBusca;
    }

    @Override
    public void salvarParaArquivo(String nomeArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(nomeArquivo, StandardCharsets.UTF_8)) {
            salvarRecursivo(raiz, writer);
        }
    }

    private void salvarRecursivo(No no, PrintWriter writer) {
        if (no != null) {
            salvarRecursivo(no.esquerda, writer);
            writer.println(no.livro.getId() + "," + no.livro.getTitulo() + "," + no.livro.getAutor());
            salvarRecursivo(no.direita, writer);
        }
    }

    @Override
    public boolean inserir(Livro livro) {
        boolean[] inserido = {false};
        raiz = inserirRecursivo(raiz, livro, inserido);
        return inserido[0];
    }

    private No inserirRecursivo(No no, Livro livro, boolean[] inserido) {
        comparacoesInsercao++;
        if (no == null) {
            inserido[0] = true;
            return new No(livro);
        }
        if (livro.getId() < no.livro.getId()) {
            no.esquerda = inserirRecursivo(no.esquerda, livro, inserido);
        } else if (livro.getId() > no.livro.getId()) {
            no.direita = inserirRecursivo(no.direita, livro, inserido);
        }
        return no;
    }

    @Override
    public Livro buscar(int id) {
        comparacoesBusca = 0;
        No no = buscarRecursivo(raiz, id);
        return no != null ? no.livro : null;
    }

    private No buscarRecursivo(No no, int id) {
        comparacoesBusca++;
        if (no == null || no.livro.getId() == id) {
            return no;
        }
        return id < no.livro.getId()
                ? buscarRecursivo(no.esquerda, id)
                : buscarRecursivo(no.direita, id);
    }

    @Override
    public boolean remover(int id) {
        boolean[] removido = {false};
        raiz = removerRecursivo(raiz, id, removido);
        return removido[0];
    }

    private No removerRecursivo(No no, int id, boolean[] removido) {
        if (no == null) {
            return null;
        }
        if (id < no.livro.getId()) {
            no.esquerda = removerRecursivo(no.esquerda, id, removido);
        } else if (id > no.livro.getId()) {
            no.direita = removerRecursivo(no.direita, id, removido);
        } else {
            removido[0] = true;
            if (no.esquerda == null) {
                return no.direita;
            }
            if (no.direita == null) {
                return no.esquerda;
            }
            No sucessor = minValor(no.direita);
            no.livro = sucessor.livro;
            no.direita = removerRecursivo(no.direita, sucessor.livro.getId(), new boolean[1]);
        }
        return no;
    }

    private No minValor(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    @Override
    public void preOrdem() {
        preOrdemRecursivo(raiz);
    }

    private void preOrdemRecursivo(No no) {
        if (no != null) {
            System.out.println(no.livro);
            preOrdemRecursivo(no.esquerda);
            preOrdemRecursivo(no.direita);
        }
    }

    @Override
    public void inOrdem() {
        inOrdemRecursivo(raiz);
    }

    private void inOrdemRecursivo(No no) {
        if (no != null) {
            inOrdemRecursivo(no.esquerda);
            System.out.println(no.livro);
            inOrdemRecursivo(no.direita);
        }
    }

    @Override
    public void posOrdem() {
        posOrdemRecursivo(raiz);
    }

    private void posOrdemRecursivo(No no) {
        if (no != null) {
            posOrdemRecursivo(no.esquerda);
            posOrdemRecursivo(no.direita);
            System.out.println(no.livro);
        }
    }

    @Override
    public void emLargura() {
        if (raiz == null) {
            return;
        }
        Queue<No> fila = new LinkedList<>();
        fila.add(raiz);
        while (!fila.isEmpty()) {
            No atual = fila.poll();
            System.out.println(atual.livro);
            if (atual.esquerda != null) {
                fila.add(atual.esquerda);
            }
            if (atual.direita != null) {
                fila.add(atual.direita);
            }
        }
    }
}
