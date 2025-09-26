# Plano de Gerenciamento de Requisitos

## 1. Introdução

Este documento descreve os processos e procedimentos que a equipe do projeto "Sistema de Gestão para Escolinhas de Futebol" utilizará para gerenciar seus requisitos. O objetivo é garantir que os requisitos sejam completamente compreendidos, documentados, validados e controlados, servindo como uma base estável para o desenvolvimento do software. Este plano cobre a elicitação, análise, validação, priorização, controle de mudanças e as responsabilidades da equipe.

---

## 2. Processos de Gerenciamento de Requisitos

Esta seção detalha as etapas do ciclo de vida dos requisitos do projeto.

### 2.1. Elicitação (Levantamento)
O processo de levantamento dos requisitos foi realizado através da técnica de **Pesquisa Documental**. A equipe analisou artigos acadêmicos, teses e publicações de mercado para identificar os principais desafios e necessidades de escolinhas de futebol. Cada membro da equipe ficou responsável por pesquisar um subtema (gestão, comunicação, pedagogia) para garantir uma cobertura abrangente dos problemas. Os artefatos gerados (resumos, citações e conclusões) estão documentados na seção "Levantamento de Requisitos" do projeto.

### 2.2. Análise e Validação
Os requisitos, documentados como Histórias de Usuário (HUs), foram analisados e validados através de uma sessão de **Inspeção em equipe**. Neste processo:
1.  **Preparação:** As HUs foram distribuídas para todos os membros antes da reunião para estudo individual.
2.  **Sessão de Inspeção:** Durante a reunião, cada HU foi lida e avaliada com base em um checklist com critérios de qualidade, como: **Clareza**, **Atomicidade**, **Testabilidade**, **Viabilidade** e **Valor de Negócio**.
3.  **Documentação:** Os defeitos e ambiguidades encontrados foram registrados. As HUs foram então corrigidas e refinadas até que toda a equipe chegasse a um consenso sobre sua validade e completude.

### 2.3. Priorização
A priorização das Histórias de Usuário para o desenvolvimento seguirá o método **MoSCoW**. Após a validação, cada HU será classificada pela equipe em uma das quatro categorias a seguir, definindo sua ordem de implementação:
- **M - Must-have (Deve ter):** Requisitos essenciais para o Mínimo Produto Viável (MVP). O sistema não funciona sem eles. Ex: Cadastro de alunos.
- **S - Should-have (Deveria ter):** Requisitos importantes e de alto valor, mas não vitais para o funcionamento básico. Serão desenvolvidos logo após os Must-have. Ex: Emissão de relatórios de desempenho.
- **C - Could-have (Poderia ter):** Funcionalidades desejáveis, de menor impacto, que serão implementadas se o tempo e os recursos permitirem. Ex: Customização do perfil do usuário.
- **W - Won't-have (Não terá):** Requisitos que estão explicitamente fora do escopo desta versão do projeto, mas que podem ser considerados para o futuro.

---

## 3. Controle de Mudanças e Versionamento

Este processo define como a equipe lidará com solicitações de mudança em requisitos já aprovados.

Qualquer solicitação para adicionar, remover ou modificar um requisito deve seguir o fluxo abaixo:
1.  **Abertura de uma Issue:** O proponente (qualquer membro da equipe) deve abrir uma **"Issue"** no repositório do GitHub do projeto. A issue deve ter um título claro (ex: "Mudança na HU-09: Adicionar pagamento via Pix") e uma descrição detalhada da mudança e sua justificativa.
2.  **Discussão da Equipe:** A equipe discutirá a issue, avaliando o impacto da mudança no escopo, no prazo e na complexidade técnica do projeto.
3.  **Aprovação:** A mudança só será aprovada após um consenso de toda a equipe.
4.  **Implementação da Mudança:** Se aprovada, a HU correspondente será atualizada no `README.md`. A alteração será registrada em um commit do Git, que obrigatoriamente referenciará o número da Issue (ex: `git commit -m "Altera HU-09 para incluir Pix, conforme #21"`).

O **versionamento** do documento de requisitos é implicitamente controlado pelo histórico de commits do Git, que fornece um registro completo de todas as alterações, quem as fez e quando.

---

## 4. Matriz de Responsabilidades (RACI)

Esta matriz define as responsabilidades de cada membro da equipe nas atividades de gestão de requisitos.

**Legenda:**
- **X:** Executou a tarefa.
- **O:** Auxiliou na tarefa.
- **[vazio]:** Não atuou nesta tarefa.

| Atividade | Bruno | João | Guilherme |
| :--- | :---: | :---: | :---: |
| **Criar o Git e Docs** | X |  |  |
| **Definir Objetivo** | X |  | O |
| **Definir viabilidades** |  | O | X |
| **Elicitar Novos Requisitos** | X | X | X |
| **Documentar as HUs** | X | X | X |
| **Criar Diagramas** | X | X | X |
| **Validar as HUs (Inspeção)** | X | O | O |
| **Priorizar as HUs (MoSCoW)** | O | O | X |
| **Aprovar Mudanças nos Requisitos** | X | X | X |
| **Criar o Protótipo** | O | X | O |
| **Criar Doc. de Req.** | X |  |  |
| **Criar Plano de Ger. de Req** |  |  | X |

*(Nota: a distribuição de Aprovador (A) pode ser rotativa ou centralizada em um membro que assuma o papel de "Gerente de Projeto" ou "Product Owner" da equipe).*
