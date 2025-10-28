LINK PARA VIDEO DEMONSTRANDO APLICAÇÃO: https://drive.google.com/file/d/1ID43jVZuVDwP_R85njV5CMKJZdJn8X-H/view?usp=sharing

# PARTE 4: Entregáveis do Trabalho 2 - Modelagem e Desenvolvimento

Esta seção documenta as decisões de projeto, arquitetura e qualidade tomadas durante o desenvolvimento da primeira versão funcional do "Sistema de Gestão para Escolinhas de Futebol", conforme solicitado no Trabalho 2.

## 1. Plano de Gerenciamento de Qualidade (5%)

### 1.1. Introdução
Este plano define as práticas e padrões que a equipe utilizou para garantir a qualidade do software durante o desenvolvimento do T2. O objetivo é entregar um software funcional, confiável e fácil de manter.

### 1.2. Papéis e Responsabilidades
A qualidade foi uma responsabilidade compartilhada. Definimos:
* **Revisão de Código:** Realizada informalmente entre os membros para funcionalidades críticas.
* **Testes Manuais:** Cada membro testou as funcionalidades que implementou, verificando os critérios das HUs.
* **Documentação:** Atualização do Readme e comentários no código feitos pelo responsável pela alteração.

### 1.3. Padrões Adotados
* **Documentação:** Readme em Markdown; Código Java comentado.
* **Ferramentas:** Git/GitHub; Trello; Eclipse IDE; Java SE 21; Java Swing.
* **Desenvolvimento:** Padrões de nomenclatura Java; Organização em pacotes (domain, repository, service, view).
* **Não Funcionais:** Foco em **Usabilidade** (interface gráfica simples via Swing), **Manutenabilidade** (arquitetura MVC, padrões de projeto) e **Segurança Básica** (validação de entrada nos painéis).

### 1.4. Processos de Qualidade
* **Gestão de Atividades:** Trello utilizado para dividir e acompanhar as tarefas do T2.
* **Revisão de Documentos:** Revisão conjunta do Readme antes da entrega.

### 1.5. Atividades e Métricas de QA
* **Testes Funcionais Manuais:** Execução dos fluxos principais das HUs implementadas via interface Swing.
* **Métrica:** Verificação da conformidade com os critérios de aceitação das HUs implementadas.

---

## 2. Arquitetura do Sistema (15%)

### 2.1. Padrão Arquitetural Base Escolhido: MVC (Modelo-Visão-Controlador)
A equipe escolheu adotar o padrão arquitetural **MVC** como base para a organização do sistema.

### 2.2. Justificativa da Escolha Arquitetural
O padrão MVC foi selecionado por:
* **Separação Clara de Responsabilidades:** Isola a interface gráfica (Visão - Pacote `view` com Swing), a lógica de negócio (Controlador - Pacote `service`) e os dados (Modelo - Pacotes `domain` e `repository`), facilitando o desenvolvimento paralelo e a manutenção.
* **Preparação para Evolução:** Mesmo implementado em Java Swing/Console para o T2, a estrutura MVC facilita uma futura migração para uma aplicação web (T3), reaproveitando as camadas de serviço e modelo.
* **Manutenabilidade e Testabilidade:** A separação permite testar a lógica de negócio (Serviços) independentemente da interface e facilita a modificação da interface sem impactar as regras de negócio.

### 2.3. Diagrama de Arquitetura

O diagrama abaixo ilustra a aplicação do padrão MVC na nossa aplicação Java Swing:

**(INSTRUÇÃO: Insira aqui a IMAGEM do diagrama de arquitetura MVC que você gerou, por exemplo, usando o código Mermaid ou desenhando em uma ferramenta.)**

```mermaid
graph LR
    Usuario -- Interage --> View[Visão (GUI Java Swing)];
    View -- 1. Envia Ação --> Controller[Controlador (Classes Service)];
    Controller -- 2. Chama Métodos --> Repository[Interfaces Repository];
    Repository -- 3. Acessa Dados --> ModelMem[Modelo (POJOs + Impl. Memória)];
    ModelMem -- 4. Retorna Dados --> Repository;
    Repository -- 5. Retorna Dados --> Controller;
    Controller -- 6. Envia Dados --> View;
    View -- 7. Atualiza Exibição --> Usuario;

    style View fill:#f9f,stroke:#333,stroke-width:2px;
    style Controller fill:#ccf,stroke:#333,stroke-width:2px;
    style Repository fill:#e8d57d,stroke:#333,stroke-width:2px;
    style ModelMem fill:#9cf,stroke:#333,stroke-width:2px;
