package catalogo;

class No {
    Livro livro;
    No esquerda;
    No direita;

    No(Livro livro) {
        this.livro = livro;
    }
}
