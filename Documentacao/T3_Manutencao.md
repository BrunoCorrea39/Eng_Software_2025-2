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
