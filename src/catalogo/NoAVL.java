package catalogo;

class NoAVL {
    Livro livro;
    int altura;
    NoAVL esquerda;
    NoAVL direita;

    NoAVL(Livro livro) {
        this.livro = livro;
        this.altura = 1;
    }
}
