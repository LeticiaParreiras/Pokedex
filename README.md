
# 📱 Pokédex Android - Kotlin

Este projeto é uma aplicação mobile inspirada na franquia Pokémon, desenvolvida como parte da disciplina de Desenvolvimento Mobile na **UCB (Universidade Católica de Brasília)**. O aplicativo permite explorar uma vasta lista de Pokémons consumindo dados da [PokéAPI](https://pokeapi.co/), permitindo a consulta de detalhes e a montagem de uma equipe personalizada.

## 🚀 Funcionalidades

### **Etapa 1 (Concluída)**
* **Listagem de Pokémons:** Exibição dinâmica utilizando `RecyclerView`.
* **Paginação:** Navegação fluida entre as páginas de dados da API.
* **Detalhes Básicos:** Visualização da imagem do Pokémon selecionado.
* **Navegação:** Transição entre telas utilizando `Intents`.

### **Etapa 2 (Em Planejamento/Desenvolvimento)**
* **Busca:** Sistema de busca por nome (case-insensitive).
* **Gerenciamento de Time:** Adicionar e remover até 6 Pokémons da equipe.
* **Validações:** Regras para evitar duplicatas e limite de espaço no time.
* **Detalhamento Técnico:** Exibição de informações completas.

---

## 🛠️ Tecnologias e Arquitetura

O projeto foi construído seguindo as melhores práticas de desenvolvimento Android moderno:

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **Arquitetura:** **MVVM** (Model-View-ViewModel) para separação de responsabilidades.
* **UI/UX:**
    * **Material Design 3:** Componentes e temas consistentes.
    * **RecyclerView + GridLayoutManager:** Listagem performática em grade.
    * **ViewBinding:** Substituição segura e moderna ao `findViewById`.
* **Networking:**
    * Consumo da **PokéAPI**.
    * Comunicação assíncrona para garantir uma interface sem travamentos (Non-blocking UI).

---

## 📂 Estrutura do Projeto

* `Model`: Representação dos dados vindos da API.
* `View`: Activities  que interagem com o usuário.
* `ViewModel`: Gerenciamento do estado da UI e integração com a lógica de dados via `LiveData`.

---

## 🏗️ Como executar o projeto

1.  Clone este repositório:
    ```bash
    git clone https://github.com/LeticiaParreiras/Pokedex.git
    ```
2.  Abra o projeto no **Android Studio**.
3.  Sincronize o Gradle e execute o app em um emulador ou dispositivo físico com Android 8.0+.

---

## Equipe

IGOR PRADO SUDO
LETÍCIA XIMENES PARREIRAS
MARIANA URANI BARROS
---

### **Dica de Personalização:**
> Se você já tiver prints do app rodando, você pode criar uma pasta chamada `screenshots` no seu repositório e adicionar uma linha como esta no README:
> `![Home do App](./screenshots/home.png)`
