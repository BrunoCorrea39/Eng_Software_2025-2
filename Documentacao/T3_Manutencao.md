# 🛠️ Trabalho 3 – Engenharia de Software  
## Manutenção do Sistema – Escolinha de Futebol

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

## 🔧 Bug 1 — Cadastro duplicado de alunos

**Problema:**  
O sistema permitia cadastrar mais de um aluno com mesmo nome e mesma data de nascimento.

**Correção aplicada:**  
- Criado método `existeAlunoPorNomeEData` na interface `AlunoRepository`.  
- Implementado esse método em `AlunoRepositoryMemoria`.  
- `AlunoService` agora faz a verificação antes de salvar:

```java
if (alunoRepository.existeAlunoPorNomeEData(nome.trim(), dataNasc)) {
    throw new IllegalArgumentException(
        "Já existe um aluno cadastrado com este nome e data de nascimento."
    );
}
```


## 🔧 Bug 2 — Criação de planos inválidos

**Problema:**  
Era possível criar planos financeiros com:

- nome vazio  
- valor nulo  
- valor menor ou igual a zero  
- duração igual ou inferior a zero  

Esses cenários geravam planos inconsistentes e quebravam regras básicas do domínio financeiro.

---

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
```

## 🔧 Bug 3 — Mensagens confusas ao vincular planos

Problema:
Quando era atribuído um plano inexistente a um aluno, o sistema gerava uma exceção genérica, dificultando a identificação do problema.

Correção aplicada no método atribuirPlanoAoAluno(...):
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
```
## 🔧 Bug 4 — Status financeiro não atualizava após pagamento

Problema:
Ao registrar o pagamento de uma fatura, o sistema atualizava o status da fatura, mas nem sempre notificava corretamente os observers responsáveis por atualizar telas e estados dependentes.
O status consolidado do aluno podia ficar desatualizado.

Correção aplicada no método registrarPagamentoFatura(...):
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
```
##  2️⃣ Nova Funcionalidade via TDD — Calcular Total Pago pelo Aluno (10%)

**Objetivo:**  
Somar todas as faturas **PAGAS** de um aluno, ignorando faturas **pendentes** ou **vencidas**.

Essa funcionalidade foi implementada seguindo rigorosamente o ciclo **TDD (Test-Driven Development)**, conforme exigido no Trabalho 3.

---

### ✔ Ciclo TDD aplicado

- **1. Teste JUnit criado primeiro**
  - O teste validava que:
    - Somente faturas com status **PAGA** entram na soma
    - Faturas **PENDENTES** e **VENCIDAS** devem ser ignoradas

- **2. Teste executado → falha**
  - A funcionalidade ainda não existia no `FinanceiroService`

- **3. Implementação mínima foi criada**
  - Código abaixo foi adicionado ao serviço para fazer o teste passar:


- **Implementação no FinanceiroService:**
```java
public BigDecimal calcularTotalPagoAluno(int alunoId) {
    return faturaRepository.buscarPorAlunoId(alunoId).stream()
            .filter(f -> f.getStatus() == StatusFatura.PAGA)
            .map(Fatura::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```
## 3️⃣ Integração das Funcionalidades (10%)

Após as correções e melhorias implementadas no Trabalho 3, o fluxo completo do sistema passou a operar de forma integrada e consistente. As principais etapas do processo são:

- **Cadastro do aluno**
  - Realizado via `AlunoService.cadastrarAluno()`
  - Inclui validações de entrada
  - Impede cadastros duplicados (Bug 1 corrigido)

- **Criação de um plano financeiro válido**
  - Executado por `FinanceiroService.criarPlano()`
  - Valida nome, valor e duração (Bug 2 corrigido)

- **Atribuição do plano ao aluno**
  - Feita através de `FinanceiroService.atribuirPlanoAoAluno()`
  - Valida aluno e plano (Bug 3 corrigido)
  - Gera automaticamente a primeira fatura do plano:
    
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
```

- **Registro de pagamento de fatura**
  - Realizado por `registrarPagamentoFatura(...)`
  - Atualiza o status da fatura
  - Notifica corretamente os observers (correção aplicada no Bug 4)

- **Cálculo do total pago pelo aluno**
  - Executado por `calcularTotalPagoAluno(...)`
  - Soma apenas faturas com status **PAGA**
  - Funcionalidade criada via TDD no Trabalho 3

- **Consulta do status financeiro consolidado**
  - Obtida através de `getStatusFinanceiroAlunos()`
  - Retorna se o aluno está **PAGO**, **PENDENTE** ou **VENCIDA**, considerando todas as suas faturas

---

Esse fluxo demonstra a integração completa entre cadastro de alunos, criação e vinculação de planos, geração automática de faturas, registro de pagamentos, notificação de observers e cálculo financeiro, garantindo consistência em todo o sistema.


## 4️⃣ Refatorações Gerais no Código (10%)

Além dos bugs corrigidos e da nova funcionalidade, foram realizadas refatorações para melhorar a qualidade do código:

- **Validações centralizadas** em `AlunoService` e `FinanceiroService`.
- **Mensagens de erro mais claras**, facilitando depuração.
- **Injeção de dependências via construtor**, facilitando testes com JUnit/Mockito.
- **Repositório em memória aprimorado**, com o método `existeAlunoPorNomeEData` e utilitário para limpeza em testes.
- **Organização da classe `FinanceiroService` por HU**, separando claramente:
  - HU-01: status financeiro  
  - HU-02: planos  
  - HU-09: pagamento  
  - Nova funcionalidade (cálculo do total pago)  
  - Observer de pagamentos  

Com essas alterações, o sistema ficou mais consistente, fácil de testar e preparado para evoluções futuras.




