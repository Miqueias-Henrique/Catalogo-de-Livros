# Catálogo de Livros (BST e AVL)

Aplicação de linha de comando em Java para gerenciamento de um catálogo de livros utilizando árvores binárias de busca (**BST**) e árvores auto-balanceadas (**AVL**), permitindo comparar rotações e número de comparações entre as duas estruturas.

## Estrutura

```text
├── livros_catalogo.txt
└── src/catalogo/
    ├── AVL.java
    ├── BST.java
    ├── CatalogoLivrosApp.java
    ├── InterfaceArvore.java
    ├── Livro.java
    ├── No.java
    └── NoAVL.java
```

## Funcionalidades

- Escolha da estrutura em tempo de execução (`BST` ou `AVL`) através da interface comum `InterfaceArvore`
- Inserção, busca, edição e remoção de livros por ID
- Percursos `In-Ordem`, `Pré-Ordem`, `Pós-Ordem` e `Em Largura` (BFS)
- Leitura e persistência em arquivo CSV (`livros_catalogo.txt`)
- Contagem de rotações (AVL) e comparações em inserções e buscas

## Como executar

Requer JDK 17 ou superior.

```bash
javac -d out src/catalogo/*.java
java -cp out catalogo.CatalogoLivrosApp
```
