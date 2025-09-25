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
Viabilidade Temporal - O cronograma do semestre, que tem como data final 02/12/2025, será cumprido com o desenvolvimento de um Produto Mínimo Viável (MVP). Essa versão inicial contemplará as funcionalidades essenciais de maior impacto: (1) cadastro de alunos, turmas e treinadores; (2) registro de frequência das aulas; e (3) um mural de comunicados centralizado. A gestão do projeto será realizada no Trello, com sprints semanais para assegurar um ritmo de desenvolvimento constante e monitorado. A adoção de uma metodologia ágil e o foco nas prioridades garantem a entrega de um sistema funcional dentro do prazo.
Viabilidade de Relevância - O projeto demonstra alta relevância ao abordar problemas concretos do seu público-alvo. Para os administradores, o sistema reduzirá erros e retrabalho na automatização de matrículas e controle de mensalidades. Para os treinadores, servirá como uma ferramenta prática para registrar a frequência e a evolução dos alunos. Já para os pais e atletas, oferecerá um canal de comunicação confiável, centralizando avisos, calendários e relatórios de desempenho. Dessa forma, a proposta transcende o âmbito acadêmico, apresentando potencial real para impactar positivamente e profissionalizar a gestão de pequenas organizações esportivas.

---

## 2. Levantamento de Requisitos

### 2.1. Técnica de Elicitação Utilizada
A técnica de elicitação de requisitos empregada foi a **Pesquisa Documental**. Foi realizada uma análise de artigos acadêmicos, relatórios e reportagens sobre gestão esportiva e o uso de tecnologia em pequenos clubes para identificar problemas comuns, necessidades do mercado e funcionalidades essenciais para um sistema do gênero.

### 2.2. Artefatos da Pesquisa

**Artigo 1:**

**Pesquisador: Bruno Correa Borges Silva**
- **Fonte:** Ritmo do Esporte. (s.d.). 3 problemas comuns em escolas de esportes e como resolvê-los. Blog Ritmo do Esporte. Recuperado de: https://organizador.ritmodoesporte.com.br/blog/12949/3-problemas-comuns-em-escolas-de-esportes-e-como-resolver
- **Critério de Seleção:** Este artigo de blog foi selecionado por oferecer uma visão prática e de mercado, vinda de uma startup que atua diretamente na área de gestão esportiva. Ele valida os problemas acadêmicos com uma perspectiva do dia a dia do negócio.
- **Citação Relevante:** _"Um dos principais problemas enfrentados pelas escolas de esportes é a falta de organização na gestão das equipes. Muitas vezes, os treinadores têm dificuldade em acompanhar o desempenho dos alunos, gerenciar a presença nas atividades e manter a comunicação com os pais"._
- **Conclusão para os Requisitos:** Esta citação, por si só, justifica a criação de alguns pilares centrais de desempenho, controle de presença e comunicação, mostrando que uma única ferramenta integrada pode resolver múltiplos problemas operacionais.

**Artigo 2:**

**Pesquisador: Bruno Correa Borges Silva**
- **Fonte:** Moraes, Douglas Diniz. (2017). A PERSPECTIVA DOS PAIS EM RELAÇÃO À PARTICIPAÇÃO ESPORTIVA DOS FILHOS EM UMA ESCOLINHA DE FUTSAL. (Trabalho de Conclusão de Curso). Universidade Federal do Rio Grande do Sul, Porto Alegre. Recuperado de: https://lume.ufrgs.br/handle/10183/174737
- **Critério de Seleção:**  Este TCC foi escolhido por investigar diretamente a perspectiva dos pais, um dos principais usuários do sistema, e por destacar as falhas e ruídos na comunicação com a escolinha.
- **Citação Relevante:** _"[...] houve desconhecimento de alguns e falta de argumentos precisos e esclarecidos de outros quanto à metodologia utilizada na escolinha"._
- **Conclusão para os Requisitos:**  Esta descoberta justifica a criação de um Portal dos Pais, onde a metodologia, o calendário e os objetivos da escolinha são comunicados de forma clara e transparente, alinhando as expectativas.

**Artigo 3:**

**Pesquisador: João Lucas Damato Drummond**
- **Fonte:** Palade, T., & Grigore, G. (2021). STRATEGIES FOR IMPROVING COMMUNICATION BETWEEN COACHES AND PARENTS - A CASE STUDY: ROMANIA vs. UK. Discobolul Physical Education, Sport and Kinetotherapy Journal, 60(4), 401-415. Recuperado de: https://discobolulunefs.ro/media/December2021.4.pdf
- **Critério de Seleção:** Escolhido por seu foco direto na comunicação treinador-pai e por identificar informações críticas que não são compartilhadas, justificando um canal de comunicação mais robusto e seguro.
- **Citação Relevante:** A necessidade de comunicar sem restrições informações como _"problemas familiares que influenciam a participação efetiva e emocional da criança no treino; a criança tem problemas de saúde; a situação escolar da criança não é muito boa"._
- **Conclusão para os Requisitos:** A falha em comunicar informações sensíveis justifica um Canal de Mensagens Diretas e Seguras, permitindo que os pais compartilhem dados confidenciais com o treinador e a administração de forma privada.

**Artigo 4:**

**Pesquisador: João Lucas Damato Drummond**
- **Fonte:** Scaglia, Alcides José. (1996). ESCOLINHA DE FUTEBOL: UMA QUESTÃO PEDAGÓGICA. MOTRIZ, 2(1). Recuperado em: https://www.periodicos.rc.biblioteca.unesp.br/index.php/motriz/article/view/6513
- **Critério de Seleção:** Este artigo foi escolhido por sua forte defesa de uma abordagem pedagógica, fornecendo a base filosófica para funcionalidades que vão além do simples registro técnico.
- **Citação Relevante:** _"[A escolinha deve] trocar o tecnicismo de movimentos estereotipados, que são subordinados a resultados imediatos de performance, para se preocupar com a função pedagógica da ação motora desenvolvida..."._
- **Conclusão para os Requisitos:** Esta visão pedagógica inspira o Módulo de Avaliações de Desempenho, que deve permitir ao treinador registrar a evolução do aluno em múltiplos aspectos (técnicos, sociais, comportamentais), valorizando o desenvolvimento integral.

**Artigo 5:**

**Pesquisador: Guilherme Alves Silva**
- **Fonte:** Rodrigues, A. L. P., et al. (2016). A FORMAÇÃO DE JOVENS ATLETAS NAS ESCOLINHAS DE FUTEBOL EM FORTALEZA-CE. Revista Brasileira de Futsal e Futebol, 8(31), 340-347. Recuperado em: https://www.rbff.com.br/index.php/rbff/article/view/474
- **Critério de Seleção:** Este estudo foi escolhido por analisar a relação entre treinadores e pais, focando no problema da pressão excessiva exercida pelas famílias sobre os jovens atletas.
- **Citação Relevante:** _"[...] a aproximação dos pais com a escolinha, porém ela não é benéfica para o aluno, pois os mesmos fazem uma pressão para que os filhos sejam jogadores profissionais..."._
- **Conclusão para os Requisitos:** A existência da pressão parental negativa embasa a criação de funcionalidades no Portal dos Pais (HU-08) que eduquem as famílias sobre a filosofia da escola e a importância de um desenvolvimento saudável, ajudando a gerenciar as expectativas.

**Artigo 6:**

**Pesquisador: Guilherme Alves Silva**
- **Fonte:** Dias, C., et al. (2024). O mercado das escolinhas de futebol em Belo Horizonte. Movimento, v. 30, e30002. Recuperado em: https://www.scielo.br/j/mov/a/7BhhKPsP56qfNKhz8NjGqvF/?format=html&lang=pt
- **Critério de Seleção:** Este artigo foi selecionado por sua análise sobre os desafios administrativos e financeiros das escolinhas, oferecendo uma visão de negócio que justifica as funcionalidades de gestão do nosso sistema.
- **Citação Relevante:** _"Alguns proprietários indicam que até 50% do seu público pode evadir em meses de férias escolares, privando-os de parte significativa de suas receitas nesses períodos"._
- **Conclusão para os Requisitos:** A alta evasão justifica a necessidade de um Módulo de Gestão de Planos, permitindo que o administrador crie planos de longo prazo (semestrais/anuais) para fidelizar os alunos e garantir a previsibilidade da receita.

---

## 3. Requisitos Funcionais - Histórias de Usuário

**COMO PREENCHER:** Liste aqui todas as Histórias de Usuário (HUs) que foram definidas e revisadas pela equipe. Organize-as por perfil de usuário para facilitar a leitura.

### 3.1. Perfil: Administrador
- **- HU-01: Visualizar Status Financeiro dos Alunos (GUILHERME)**
  - Como um administrador, eu quero visualizar um painel com o status de pagamento de todos os alunos, para que eu possa identificar rapidamente quem está inadimplente e gerenciar o fluxo de caixa da escolinha.
  - **Critérios de Aceitação:**
    1. O painel deve listar todos os alunos ativos.
    2. Cada aluno deve ter um status claro (ex: "Em dia", "Em atraso", "Pendente").
    3. Devo conseguir filtrar a lista por status de pagamento.
   
- **HU-02: Gerenciar Planos de Matrícula (GUILHERME)**
  - Como um administrador, eu quero criar e gerenciar diferentes planos de pagamento (mensal, semestral, anual), para que eu possa oferecer descontos para pagamentos antecipados e reduzir a evasão de alunos nas férias.
  - **Critérios de Aceitação::**
    1. Devo poder criar um novo plano com nome, valor e período (ex: "Plano Semestral", R$ 600,00, 6 meses).
    2. Devo poder associar um aluno a um plano específico no momento da matrícula.
    3. O sistema deve gerar as cobranças automaticamente com base no plano escolhido.

- **HU-03: Cadastrar Novos Alunos (JOÃO)**
  - Como um administrador, eu quero cadastrar um novo aluno com suas informações pessoais, dados dos responsáveis e informações médicas relevantes, para que todas as informações fiquem centralizadas e seguras em um único local.
  - **Critérios de Aceitação:**
    1. O formulário de cadastro deve incluir campos para nome do aluno, data de nascimento, nome dos responsáveis, telefones de contato e um campo para observações médicas (alergias, etc.).
    2. Após o cadastro, o perfil do aluno deve ser criado e ficar acessível no sistema.

### 3.2. Perfil: Treinador
- **HU-04: Realizar Chamada Digital (BRUNO)**
  - Como um treinador, eu quero registrar a presença dos meus alunos de forma rápida através de uma lista digital no meu celular, para que eu possa otimizar o tempo no início do treino e manter um histórico de frequência preciso.
  - **Critérios de Aceitação:**
    1. Ao acessar minha turma, devo ver a lista de todos os alunos matriculados.
    2. Devo conseguir marcar cada aluno como "Presente" ou "Ausente" com um clique.
    3. Após salvar, a frequência do dia deve ser registrada no histórico do aluno e no sistema.

- **HU-05: Registrar Avaliações de Desempenho (JOÃO)**
  - Como um treinador, eu quero registrar observações e avaliações sobre o desenvolvimento de cada aluno, para que eu possa acompanhar seu progresso ao longo do tempo e ter dados concretos para conversar com os pais.
  - **Critérios de Aceitação:**
    1. No perfil de um aluno, devo encontrar uma área para adicionar uma nova avaliação
    2. A avaliação deve permitir notas ou comentários em áreas predefinidas (ex: "Passe", "Drible", "Comportamento em grupo").
    3. Todas as avaliações devem ficar salvas em um histórico no perfil do aluno.
   
- **HU-06: Publicar Comunicados para a Turma (BRUNO)**
  - Como um treinador, eu quero enviar comunicados para todos os pais da minha turma de uma só vez, para que eu possa informar sobre alterações de horário, locais de jogos ou outros avisos importantes de forma eficiente.
Critérios de Aceitação:
  - **Critérios de Aceitação:**
    1. Devo ter uma opção de "Novo Comunicado" na área da minha turma.
    2. Ao publicar, todos os responsáveis dos alunos daquela turma devem receber uma notificação no sistema.

### 3.3. Perfil: Pais/Responséveis
- **HU-07: Visualizar o Desempenho do Meu Filho (JOÃO)**
  - Como um pai/responsável, eu quero acessar o perfil do meu filho para ver seu histórico de frequência e as avaliações de desempenho feitas pelo treinador, para que eu possa acompanhar sua evolução e me sentir mais engajado com as atividades.
  - **Critérios de Aceitação:**
    1. No perfil do meu filho, deve haver uma seção de "Frequência" e outra de "Avaliações".
    2. Devo conseguir ver todas as avaliações passadas com as notas e comentários do treinador.

- **HU-08: Acessar o Mural de Avisos e o Calendário (BRUNO)**
  - Como um pai/responsável, eu quero ter acesso a um mural de notícias e a um calendário de eventos da escolinha, para que eu possa me manter informado sobre os próximos jogos, eventos e comunicados gerais sem depender de grupos de WhatsApp.
  - **Critérios de Aceitação:**
    1. A tela inicial do meu perfil deve exibir os últimos comunicados.
    2. Deve haver uma seção "Calendário" que mostre as datas dos próximos treinos, jogos e eventos.

- **HU-09: Realizar Pagamentos Online (GUILHERME)**
  - Como um pai/responsável, eu quero visualizar minhas faturas e realizar o pagamento da mensalidade diretamente pelo sistema, para que o processo seja mais prático, seguro e eu possa manter meu filho adimplente.
  - **Critérios de Aceitação:**
    1. Devo ter uma seção "Financeiro" onde minhas cobranças em aberto são listadas.
    2. Deve haver um botão "Pagar" que me redirecione para opções de pagamento (ex: Pix, boleto).
    3. Após o pagamento, o status da fatura deve ser atualizado para "Pago".

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
