# 🛠️ Trabalho 3 – Engenharia de Software  
## **Manutenção do Sistema – Escolinha de Futebol**

Este documento descreve todas as atividades realizadas na etapa de **Manutenção (Trabalho 3)**, incluindo:

- Correção de bugs identificados no Trabalho 2  
- Implementação de nova funcionalidade seguindo TDD  
- Integração das funcionalidades antigas com as novas  
- Refatorações gerais aplicadas ao código  

As alterações foram realizadas principalmente nas classes:

- `AlunoService`
- `FinanceiroService`
- `AlunoRepository` + `AlunoRepositoryMemoria`
- Domínios e repositórios relacionados

---

# 1️⃣ Correção de Bugs do Trabalho 2 (10%)

Durante a análise do código desenvolvido no Trabalho 2, foram identificados e corrigidos diversos problemas funcionais e estruturais.

---

## 🔧 **Bug 1 — Cadastro duplicado de alunos**

**Problema:**  
O sistema permitia cadastrar mais de um aluno com mesmo nome e mesma data de nascimento.

**Correção aplicada:**  
- Criado método `existeAlunoPorNomeEData` na interface `AlunoRepository`.  
- Implementado esse método em `AlunoRepositoryMemoria`.  
- `AlunoService` agora faz a verificação antes de salvar:

```java
if (alunoRepository.existeAlunoPorNomeEData(nome.trim(), dataNasc)) {
    throw new IllegalArgumentException("Já existe um aluno cadastrado com este nome e data de nascimento.");
}

## 🔧 Bug 2 — Criação de planos inválidos

**Problema:**  
Era possível criar planos financeiros com:
- nome vazio  
- valor nulo  
- valor menor ou igual a zero  
- duração igual ou inferior a zero  

**Correção aplicada no `FinanceiroService.criarPlano(...)`:**

```java
public PlanoPagamento criarPlano(String nome, BigDecimal valor, int duracaoMeses) {

    // validação de nome
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome do plano não pode ser vazio.");
    }

    // validação de valor
    if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Valor do plano deve ser maior que zero.");
    }

    // validação da duração
    if (duracaoMeses <= 0) {
        throw new IllegalArgumentException("Duração do plano deve ser positiva.");
    }

    PlanoPagamento novoPlano = new PlanoPagamento(0, nome.trim(), valor, duracaoMeses);
    return planoPagamentoRepository.salvar(novoPlano);
}
## 🔧 Bug 3 — Mensagens confusas ao vincular planos

**Problema:**  
Quando era atribuído um plano inexistente a um aluno, o sistema gerava uma exceção genérica, dificultando a identificação do problema.

**Correção aplicada no método `atribuirPlanoAoAluno(...)`:**

```java
public synchronized void atribuirPlanoAoAluno(int alunoId, int planoId) {
    
    // valida existência do aluno
    alunoRepository.buscarPorId(alunoId)
            .orElseThrow(() -> 
                new IllegalArgumentException("Aluno não encontrado (ID " + alunoId + ").")
            );

    // valida existência do plano
    PlanoPagamento plano = planoPagamentoRepository.buscarPorId(planoId)
            .orElseThrow(() ->
                new IllegalArgumentException("Plano não encontrado (ID " + planoId + ").")
            );

    alunoParaPlano.put(alunoId, plano.getId());
    gerarPrimeiraFaturaDoPlano(alunoId, plano);
}

---

# ✅ **🔧 Bug 4 — Status financeiro não atualizava corretamente após pagamento**

```md
## 🔧 Bug 4 — Status financeiro não atualizava após pagamento

**Problema:**  
Ao registrar o pagamento de uma fatura, o sistema atualizava o status da fatura, mas não notificava corretamente os observers responsáveis por atualizar telas e estados dependentes.

Além disso, o status consolidado do aluno podia ficar desatualizado.

**Correção aplicada no método `registrarPagamentoFatura(...)`:**

```java
public boolean registrarPagamentoFatura(int faturaId, LocalDate dataPagamento) {

    Fatura fatura = faturaRepository.buscarPorId(faturaId)
            .orElseThrow(() -> 
                new IllegalArgumentException("Fatura com ID " + faturaId + " não encontrada.")
            );

    if (fatura.getStatus() == StatusFatura.PENDENTE
            || fatura.getStatus() == StatusFatura.VENCIDA) {

        fatura.setStatus(StatusFatura.PAGA);
        fatura.setDataPagamento(dataPagamento);
        faturaRepository.salvar(fatura);

        // 🔔 Notifica corretamente os observers
        notificarObservadores(fatura);

        return true;
    }

    return false; // já estava paga
}

---

# ⭐ **Nova Funcionalidade via TDD — “Calcular Total Pago pelo Aluno”**

```md
# ⭐ Nova Funcionalidade (TDD) — Calcular Total Pago por Aluno

**Objetivo:**  
Somar todas as faturas pagas de um aluno, ignorando faturas pendentes ou vencidas.

**Implementação criada após o teste JUnit (TDD):**

```java
public BigDecimal calcularTotalPagoAluno(int alunoId) {
    return faturaRepository.buscarPorAlunoId(alunoId).stream()
            .filter(f -> f.getStatus() == StatusFatura.PAGA)
            .map(Fatura::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}

---

# 🔗 **Integração das Funcionalidades (com código)**

```md
# 🔗 Integração das Funcionalidades

O fluxo integrado envolve:

1. Cadastro de aluno  
2. Criação e validação de plano  
3. Atribuição de plano → gera primeira fatura  
4. Pagamento da fatura  
5. Cálculo do total pago (nova funcionalidade via TDD)  
6. Status financeiro consolidado  

**Pontos principais envolvidos no código:**

### ➤ Geração da primeira fatura automaticamente
```java
private void gerarPrimeiraFaturaDoPlano(int alunoId, PlanoPagamento plano) {

    LocalDate hoje = LocalDate.now();
    LocalDate vencimento = (hoje.getDayOfMonth() <= 10)
            ? hoje.withDayOfMonth(10)
            : hoje.plusMonths(1).withDayOfMonth(10);

    Fatura f = new Fatura(0, alunoId, plano.getValor(), vencimento);
    f.setStatus(StatusFatura.PENDENTE);
    f.setPlanoPagamentoId(plano.getId());

    faturaRepository.salvar(f);
}

