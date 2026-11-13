package catalogo;

import java.io.IOException;

public interface InterfaceArvore {
    boolean inserir(Livro livro);
    Livro buscar(int id);
    boolean remover(int id);
    void preOrdem();
    void inOrdem();
    void posOrdem();
    void emLargura();
    String getEstatisticas();
    long getComparacoesBusca();
    void salvarParaArquivo(String nomeArquivo) throws IOException;
}
