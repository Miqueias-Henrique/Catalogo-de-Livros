package catalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class AVL implements InterfaceArvore {
    private NoAVL raiz;
    private long rotacoes = 0;
    private long comparacoesInsercao = 0;
    private long comparacoesBusca = 0;

    @Override
    public String getEstatisticas() {
        return "AVL Stats  | Rotacoes: " + rotacoes
                + " | Comparacoes Insercao: " + comparacoesInsercao
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

    private void salvarRecursivo(NoAVL no, PrintWriter writer) {
        if (no != null) {
            salvarRecursivo(no.esquerda, writer);
            writer.println(no.livro.getId() + "," + no.livro.getTitulo() + "," + no.livro.getAutor());
            salvarRecursivo(no.direita, writer);
        }
    }

    private int altura(NoAVL no) {
        return no == null ? 0 : no.altura;
    }

    private void atualizarAltura(NoAVL no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private int fatorBalanco(NoAVL no) {
        return no == null ? 0 : altura(no.direita) - altura(no.esquerda);
    }

    private NoAVL rotacaoDireita(NoAVL z) {
        rotacoes++;
        NoAVL y = z.esquerda;
        z.esquerda = y.direita;
        y.direita = z;
        atualizarAltura(z);
        atualizarAltura(y);
        return y;
    }

    private NoAVL rotacaoEsquerda(NoAVL z) {
        rotacoes++;
        NoAVL y = z.direita;
        z.direita = y.esquerda;
        y.esquerda = z;
        atualizarAltura(z);
        atualizarAltura(y);
        return y;
    }

    private NoAVL balancear(NoAVL no) {
        atualizarAltura(no);
        int balanco = fatorBalanco(no);
        if (balanco < -1) {
            if (fatorBalanco(no.esquerda) > 0) {
                no.esquerda = rotacaoEsquerda(no.esquerda);
            }
            return rotacaoDireita(no);
        }
        if (balanco > 1) {
            if (fatorBalanco(no.direita) < 0) {
                no.direita = rotacaoDireita(no.direita);
            }
            return rotacaoEsquerda(no);
        }
        return no;
    }

    @Override
    public boolean inserir(Livro livro) {
        boolean[] inserido = {false};
        raiz = inserirRecursivo(raiz, livro, inserido);
        return inserido[0];
    }

    private NoAVL inserirRecursivo(NoAVL no, Livro livro, boolean[] inserido) {
        comparacoesInsercao++;
        if (no == null) {
            inserido[0] = true;
            return new NoAVL(livro);
        }
        if (livro.getId() < no.livro.getId()) {
            no.esquerda = inserirRecursivo(no.esquerda, livro, inserido);
        } else if (livro.getId() > no.livro.getId()) {
            no.direita = inserirRecursivo(no.direita, livro, inserido);
        } else {
            return no;
        }
        return balancear(no);
    }

    @Override
    public Livro buscar(int id) {
        comparacoesBusca = 0;
        NoAVL no = buscarRecursivo(raiz, id);
        return no != null ? no.livro : null;
    }

    private NoAVL buscarRecursivo(NoAVL no, int id) {
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

    private NoAVL removerRecursivo(NoAVL no, int id, boolean[] removido) {
        if (no == null) {
            return null;
        }
        if (id < no.livro.getId()) {
            no.esquerda = removerRecursivo(no.esquerda, id, removido);
        } else if (id > no.livro.getId()) {
            no.direita = removerRecursivo(no.direita, id, removido);
        } else {
            removido[0] = true;
            if (no.esquerda == null || no.direita == null) {
                no = no.esquerda != null ? no.esquerda : no.direita;
            } else {
                NoAVL sucessor = minValor(no.direita);
                no.livro = sucessor.livro;
                no.direita = removerRecursivo(no.direita, sucessor.livro.getId(), new boolean[1]);
            }
        }
        return no == null ? null : balancear(no);
    }

    private NoAVL minValor(NoAVL no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    @Override
    public void preOrdem() {
        preOrdemRecursivo(raiz);
    }

    private void preOrdemRecursivo(NoAVL no) {
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

    private void inOrdemRecursivo(NoAVL no) {
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

    private void posOrdemRecursivo(NoAVL no) {
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
        Queue<NoAVL> fila = new LinkedList<>();
        fila.add(raiz);
        while (!fila.isEmpty()) {
            NoAVL no = fila.poll();
            System.out.println(no.livro);
            if (no.esquerda != null) {
                fila.add(no.esquerda);
            }
            if (no.direita != null) {
                fila.add(no.direita);
            }
        }
    }
}
