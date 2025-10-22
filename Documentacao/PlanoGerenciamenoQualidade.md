# Plano de Gerenciamento de Qualidade

## 1. Introdução
Este plano define as práticas e padrões que a equipe utilizará para garantir a qualidade do "Sistema de Gestão para Escolinhas de Futebol" durante o desenvolvimento. O objetivo é entregar um software que atenda aos requisitos funcionais e não funcionais, seja confiável e fácil de manter.

## 2. Papéis e Responsabilidades
A qualidade é uma responsabilidade compartilhada por toda a equipe. No entanto, definimos algumas responsabilidades específicas:
* **Revisão de Código:** Cada membro é responsável por revisar o código de outro membro antes da integração (merge) de novas funcionalidades.
* **Testes Manuais:** Ao finalizar uma funcionalidade (HU), o próprio desenvolvedor é responsável por realizar testes manuais básicos para verificar se os critérios de aceitação foram atendidos.
* **Documentação:** A responsabilidade pela atualização da documentação (Readme, comentários no código) é do membro que realizou a alteração correspondente.

## 3. Padrões Adotados
Para garantir consistência e qualidade, a equipe seguirá os seguintes padrões:
* **Padrão de Documentação:**
    * `README.md`: Seguirá a estrutura definida nos trabalhos da disciplina, utilizando Markdown.
    * Comentários no Código: O código será comentado para explicar lógica complexa ou decisões de projeto. Funções e classes principais terão docstrings descrevendo seu propósito.
* **Padrão de Ferramentas:**
    * Versionamento: Git e GitHub.
    * Gestão de Tarefas: Trello.
    * Linguagem/Framework: Python/Django (Back-end), React (Front-end), PostgreSQL (Banco de Dados) - *Ajustar conforme a escolha real da equipe*.
    * Code Linter: Ferramentas como `Flake8` (Python) e `ESLint` (JavaScript) serão usadas para garantir a consistência do estilo de código.
* **Padrão de Desenvolvimento:**
    * Estilo de Código: PEP 8 para Python, Prettier para JavaScript/React - *Ajustar*.
    * Nomenclatura: Variáveis e funções em `snake_case` (Python) ou `camelCase` (JavaScript). Classes em `PascalCase`. Nomes devem ser descritivos.
* **Padrões Não Funcionais Prioritários:**
    * **Usabilidade:** A interface deve ser intuitiva, seguindo o fluxo do protótipo validado.
    * **Segurança (Básica):** Validação de entrada em todos os formulários para prevenir injeção de dados maliciosos. *Considerar hashing de senhas se houver login*.
    * **Manutenabilidade:** O código deve ser organizado (seguindo a arquitetura MVC/Camadas) e comentado para facilitar futuras modificações.

## 4. Processos de Qualidade
* **Gestão de Atividades:** Utilização do Trello para rastrear o progresso das HUs e bugs.
* **Revisão de Código (Code Review):** Antes de um `merge` para a branch principal (`main` ou `develop`), o código deve ser revisado por pelo menos um outro membro da equipe.
* **Revisão de Documentos:** A documentação no `README.md` será revisada pela equipe antes da entrega final.

## 5. Atividades e Métricas de Garantia de Qualidade (QA)
* **Testes Funcionais Manuais:** Cada HU implementada será testada manualmente pelo desenvolvedor e, idealmente, por outro membro, seguindo os fluxos definidos no protótipo para garantir que os critérios de aceitação foram atendidos.
* **Métrica Simples:** Rastreamento do número de *bugs* encontrados durante os testes manuais e após a integração, buscando reduzir a quantidade ao longo do tempo.
