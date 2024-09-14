# ScreenMatch API

API REST para gerenciar séries e seus episódios, desenvolvida com **Spring Boot** e utilizando uma base de dados relacional.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento web em Java.
- **Spring Data JPA**: Framework para acesso a dados.
- **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional.
- **Jackson**: Biblioteca para serialização e desserialização JSON.
- **MyMemory API**: API para tradução de textos.
- **JUnit**: Framework para testes unitários.

## Funcionalidades da API

A API fornece as seguintes funcionalidades para gerenciamento de séries e episódios:

### Séries
- **Buscar séries**:
    - Por nome.
    - Por ator.
    - Por categoria.
    - Top 5 séries com melhor avaliação.
    - Listar todos os lançamentos mais recentes.

### Episódios
- **Buscar episódios**:
    - Por série e temporada.
    - Por série e número da temporada.
    - Por série e trecho do título.
    - Top 5 episódios por série com melhor avaliação.
    - Por série e ano limite de lançamento.

### Gerenciamento
- **Gerenciar séries e episódios**:
    - Salvar novas séries e seus episódios no banco de dados.

## Pré-requisitos

- **Java JDK 11** ou superior
- **Maven**
- **PostgreSQL** instalado e configurado

