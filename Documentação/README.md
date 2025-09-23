# Eng_Software_2025-2
Repositório para a disciplina de Engenharia de Software da UTFPR, campus Apucarana.

Link para o Docs do Documento de Requisitos (V1): https://docs.google.com/document/d/1Mk7BkMl4_-l1ifmd_HLYopfKxHWUlUyj/edit?usp=sharing&ouid=103704943696249700746&rtpof=true&sd=true

# Trabalho 1: Engenharia de Requisitos
# Projeto: Sistema de Gestão para Escolinhas de Futebol

**Equipe:**
- Bruno Correa Borges Silva
- João Lucas Damato Drummond
- Guilherme Alves Silva

---

# Documento de Requisitos (V1)

Este documento apresenta a primeira versão dos requisitos para o projeto do Sistema de Gestão para Escolinhas de Futebol.

## 1. Objetivo do Projeto

A gestão de escolinhas de futebol de pequeno e médio porte ainda depende majoritariamente de processos manuais e desconectados. É frequente o uso de planilhas para o financeiro, grupos de WhatsApp para comunicação e anotações em papel para monitorar os alunos. Essa falta de integração gera ineficiência administrativa, falhas na comunicação com as famílias e dificuldade para analisar o desempenho dos atletas. A carência de uma solução centralizada consome tempo dos gestores e treinadores, resultando em uma experiência desorganizada para todos.
Este projeto visa criar um sistema de gestão unificado para escolinhas de futebol, destinado a três tipos de usuários: administradores, treinadores e pais/responsáveis. A plataforma buscará simplificar os processos administrativos e financeiros, modernizar o acompanhamento pedagógico-esportivo e estabelecer um canal de comunicação mais eficaz entre a instituição e as famílias. O objetivo final é trazer maior eficiência, profissionalismo e valor aos serviços oferecidos.

### 1.1. Viabilidade do Projeto

#### Viabilidade Técnica
O desenvolvimento do sistema é considerado tecnicamente viável. A equipe tem experiência com as tecnologias essenciais para uma aplicação web moderna, como HTML, JavaScript, React e Python. O banco de dados escolhido será o PostgreSQL, uma opção sólida, de código aberto e com ampla documentação. O versionamento do código será feito com Git, hospedado no GitHub — ferramentas com as quais a equipe já está familiarizada. A arquitetura modular adotada permitirá que as funcionalidades sejam implementadas e entregues de forma incremental ao longo do semestre.

#### Viabilidade Temporal e de Relevância
Viabilidade Temporal - O cronograma do semestre, que tem como data final 26/09/2025, será cumprido com o desenvolvimento de um Produto Mínimo Viável (MVP). Essa versão inicial contemplará as funcionalidades essenciais de maior impacto: (1) cadastro de alunos, turmas e treinadores; (2) registro de frequência das aulas; e (3) um mural de comunicados centralizado. A gestão do projeto será realizada no Trello, com sprints semanais para assegurar um ritmo de desenvolvimento constante e monitorado. A adoção de uma metodologia ágil e o foco nas prioridades garantem a entrega de um sistema funcional dentro do prazo.
Viabilidade de Relevância - O projeto demonstra alta relevância ao abordar problemas concretos do seu público-alvo. Para os administradores, o sistema reduzirá erros e retrabalho na automatização de matrículas e controle de mensalidades. Para os treinadores, servirá como uma ferramenta prática para registrar a frequência e a evolução dos alunos. Já para os pais e atletas, oferecerá um canal de comunicação confiável, centralizando avisos, calendários e relatórios de desempenho. Dessa forma, a proposta transcende o âmbito acadêmico, apresentando potencial real para impactar positivamente e profissionalizar a gestão de pequenas organizações esportivas.

---

## 2. Levantamento de Requisitos

### 2.1. Técnica de Elicitação Utilizada
A técnica de elicitação de requisitos empregada foi a **Pesquisa Documental**. Foi realizada uma análise de artigos acadêmicos, relatórios e reportagens sobre gestão esportiva e o uso de tecnologia em pequenos clubes para identificar problemas comuns, necessidades do mercado e funcionalidades essenciais para um sistema do gênero.

### 2.2. Artefatos da Pesquisa

_**COMO PREENCHER:** Consolide aqui as pesquisas que cada membro fez, mantendo um formato padrão para cada uma._

**Pesquisa de [Nome do Integrante 1]:**
- **Fonte:** [Link ou referência do artigo/reportagem]
- **Critério de Seleção:** [Explicação breve do porquê este artefato foi escolhido]
- **Citação Relevante:** _"[Citação que embasou alguma decisão ou requisito]"_
- **Conclusão para os Requisitos:** [Como a pesquisa influenciou a definição de uma funcionalidade]

---

## 3. Requisitos Funcionais - Histórias de Usuário

**COMO PREENCHER:** Liste aqui todas as Histórias de Usuário (HUs) que foram definidas e revisadas pela equipe. Organize-as por perfil de usuário para facilitar a leitura.

### 3.1. Perfil: Administrador
- **HU-01: Enviar Comunicados**
  - **Como um** administrador, **eu quero** enviar comunicados para turmas específicas ou para todos os pais, **para que** eu possa manter todos informados de forma eficiente.
  - **Critérios de Aceitação:**
    1. O administrador deve poder selecionar os destinatários (turma, todos).
    2. O sistema deve enviar uma notificação via app.
    3. Deve haver um histórico de comunicados enviados.
  - **Embasamento:** Justificado pela pesquisa de [Nome do Membro], que aponta a importância da comunicação.

_[Continue listando as outras HUs do Administrador]_

### 3.2. Perfil: Treinador
- **HU-XX: [Título da HU]**

_[Continue para todos os perfis e HUs]_

---

## 4. Modelagem de Interação - Diagramas de Sequência

**COMO PREENCHER:** Insira aqui as imagens dos diagramas de sequência que vocês criaram. Adicione uma breve legenda para cada um, associando-o à sua respectiva HU._

**Figura 1: Diagrama de Sequência para a HU-01 (Enviar Comunicados)**
![Diagrama de Sequência para HU-01](caminho/no/repositorio/diagrama_hu01.png)

**Figura 2: Diagrama de Sequência para a HU-02 (Registrar Presença)**
![Diagrama de Sequência para HU-02](caminho/no/repositorio/diagrama_hu02.png)

---

## 5. Protótipo de Média Fidelidade

**COMO PREENCHER:** Apresente aqui o resultado da prototipação. Inclua o link para o projeto no Figma e as imagens dos principais fluxos de tela.

O protótipo de média fidelidade foi desenvolvido na ferramenta Figma e pode ser acessado através do seguinte link:
[**Link para o Protótipo no Figma**](https://www.figma.com/...)

### 5.1. Fluxos de Tela

**Fluxo 1: Envio de Comunicado (HU-01)**
_Descrição breve do fluxo._
![Fluxo de Envio de Comunicado](caminho/no/repositorio/fluxo_comunicado.png)

**Fluxo 2: Registro de Presença (HU-02)**
_Descrição breve do fluxo._
![Fluxo de Registro de Presença](caminho/no/repositorio/fluxo_presenca.png)

### 5.2. Tabela de Rastreabilidade

**COMO PREENCHER:** Insira a tabela que conecta as HUs às telas do protótipo.

| ID da HU | Telas do Protótipo Correspondentes |
| :--- | :--- |
| HU-01 | Tela de Comunicados, Pop-up de Confirmação |
| HU-02 | Dashboard do Treinador, Tela de Chamada |
