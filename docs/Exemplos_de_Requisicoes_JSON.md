# Exemplos de requisições JSON — API Clínica Médica

Este arquivo traz **todas as requisições de teste**, com o JSON pronto para copiar e colar no Postman (ou em qualquer cliente HTTP). É o mesmo conteúdo da coleção `Clinica-API.postman_collection.json`, em formato de leitura.

## Antes de começar

1. Inicie a API. Ela roda em `http://localhost:8080`, e o banco H2 começa vazio.
2. Siga os passos **na ordem**. Os IDs abaixo (1, 2, 3...) consideram que a API acabou de ser iniciada.
3. Em cada requisição com corpo, use o cabeçalho `Content-Type: application/json`. No Postman: *Body* → *raw* → *JSON*.
4. **Token:** faça o login (passos 1, 8 e 9) e copie o valor de `"token"` da resposta. Nas requisições seguintes, use a aba *Authorization* → *Bearer Token* (ou o cabeçalho `Authorization: Bearer <token>`).
5. As datas usam a segunda-feira **01/03/2027** e o domingo **28/02/2027**, que estão no futuro. Se for testar depois disso, troque por qualquer segunda-feira futura.
6. Para repetir tudo do zero, reinicie a API.

| Usuário | Login | Senha | Como é criado |
|---|---|---|---|
| ADMIN | `admin` | `admin123` | automaticamente, ao iniciar a API |
| RECEPCIONISTA | `recep` | `recep123` | passo 3 |
| MÉDICO | `drjoao` | `medico123` | passo 4 |


## 1. Autenticação

### 1. Login ADMIN (gera token)

`POST http://localhost:8080/login`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `200`

```json
{
  "login": "admin",
  "senha": "admin123"
}
```

### 2. Login com senha errada (401)

`POST http://localhost:8080/login`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `401`

```json
{
  "login": "admin",
  "senha": "errada"
}
```


## 2. Usuários (ADMIN)

### 3. Cadastrar RECEPCIONISTA

`POST http://localhost:8080/usuario`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `201`

```json
{
  "login": "recep",
  "senha": "recep123",
  "perfil": "RECEPCIONISTA"
}
```

### 4. Cadastrar MEDICO

`POST http://localhost:8080/usuario`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `201`

```json
{
  "login": "drjoao",
  "senha": "medico123",
  "perfil": "MEDICO"
}
```

### 5. Login duplicado (409)

`POST http://localhost:8080/usuario`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `409`

```json
{
  "login": "recep",
  "senha": "recep123",
  "perfil": "RECEPCIONISTA"
}
```

### 6. Listar usuários

`GET http://localhost:8080/usuario`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `200`

### 7. Listar por perfil

`GET http://localhost:8080/usuario/perfil/RECEPCIONISTA`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `200`

### 8. Login RECEPCIONISTA (gera token)

`POST http://localhost:8080/login`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `200`

```json
{
  "login": "recep",
  "senha": "recep123"
}
```

### 9. Login MEDICO (gera token)

`POST http://localhost:8080/login`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `200`

```json
{
  "login": "drjoao",
  "senha": "medico123"
}
```


## 3. Especialidades

### 10. Cadastrar especialidade

`POST http://localhost:8080/especialidade`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `201`

```json
{
  "nome": "Cardiologia"
}
```

### 11. Cadastrar 2ª especialidade

`POST http://localhost:8080/especialidade`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `201`

```json
{
  "nome": "Dermatologia"
}
```

### 12. Especialidade duplicada (409)

`POST http://localhost:8080/especialidade`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `409`

```json
{
  "nome": "cardiologia"
}
```

### 13. Nome vazio (400)

`POST http://localhost:8080/especialidade`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `400`

```json
{
  "nome": ""
}
```

### 14. Listar especialidades

`GET http://localhost:8080/especialidade`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 15. Atualizar especialidade (PUT)

`PUT http://localhost:8080/especialidade/2`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `200`

```json
{
  "nome": "Dermatologia Clínica"
}
```

### 16. Excluir especialidade sem médicos (204)

`DELETE http://localhost:8080/especialidade/2`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `204`


## 4. Médicos

### 17. Cadastrar médico

`POST http://localhost:8080/medico`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `201`

```json
{
  "nome": "Dr. João Silva",
  "crm": "123456-BA",
  "especialidadeId": 1,
  "diaAtendimento": "2027-03-01",
  "horarioInicio": "08:00",
  "horarioFim": "12:00"
}
```

### 18. CRM inválido (400)

`POST http://localhost:8080/medico`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `400`

```json
{
  "nome": "Dra. Ana",
  "crm": "abc",
  "especialidadeId": 1,
  "diaAtendimento": "2027-03-01",
  "horarioInicio": "08:00",
  "horarioFim": "12:00"
}
```

### 19. Especialidade inexistente (404)

`POST http://localhost:8080/medico`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `404`

```json
{
  "nome": "Dra. Ana",
  "crm": "654321-BA",
  "especialidadeId": 999,
  "diaAtendimento": "2027-03-01",
  "horarioInicio": "08:00",
  "horarioFim": "12:00"
}
```

### 20. Jornada inválida (400)

`POST http://localhost:8080/medico`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `400`

```json
{
  "nome": "Dra. Ana",
  "crm": "654321-BA",
  "especialidadeId": 1,
  "diaAtendimento": "2027-03-01",
  "horarioInicio": "14:00",
  "horarioFim": "10:00"
}
```

### 21. Listar médicos (qualquer perfil)

`GET http://localhost:8080/medico`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 22. Buscar médico por ID

`GET http://localhost:8080/medico/1`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 23. Médicos por especialidade

`GET http://localhost:8080/medico/especialidade/1`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 24. Atualizar médico (PUT)

`PUT http://localhost:8080/medico/1`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `200`

```json
{
  "horarioFim": "13:00"
}
```

### 25. Excluir especialidade com médico (409)

`DELETE http://localhost:8080/especialidade/1`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `409`


## 5. Pacientes

### 26. Cadastrar paciente

`POST http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `201`

```json
{
  "nome": "Maria Souza",
  "dataNascimento": "1990-05-10",
  "telefone": "71999998888",
  "cpf": "12345678901"
}
```

### 27. Cadastrar 2º paciente

`POST http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `201`

```json
{
  "nome": "Carlos Lima",
  "dataNascimento": "1985-02-20",
  "telefone": "71988887777",
  "cpf": "98765432100"
}
```

### 28. Cadastrar 3º paciente (será inativado)

`POST http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `201`

```json
{
  "nome": "Paula Reis",
  "dataNascimento": "2000-08-01",
  "telefone": "71977776666",
  "cpf": "11122233344"
}
```

### 29. Campos vazios/inválidos (400)

`POST http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "nome": "",
  "cpf": "123"
}
```

### 30. CPF duplicado (409)

`POST http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `409`

```json
{
  "nome": "Outra",
  "dataNascimento": "1990-05-10",
  "telefone": "71999998888",
  "cpf": "12345678901"
}
```

### 31. Listar pacientes

`GET http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 32. Buscar paciente por ID

`GET http://localhost:8080/pacientes/1`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 33. Paciente inexistente (404)

`GET http://localhost:8080/pacientes/999`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `404`

### 34. Atualizar telefone (PUT)

`PUT http://localhost:8080/pacientes/2`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

```json
{
  "telefone": "71911112222"
}
```

### 35. Inativar paciente (PATCH)

`PATCH http://localhost:8080/pacientes/3/inativar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`


## 6. Agendamentos - fluxo completo

### 36. Horários livres do médico

`GET http://localhost:8080/agendamento/horarios-livres?medicoId=1&data=2027-03-01`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 37. Agendar consulta (09:00)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `201`

```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "09:00"
}
```

### 38. Agendar 2ª consulta (10:00)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `201`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "10:00"
}
```

### 39. Confirmar consulta (recepção)

`PATCH http://localhost:8080/agendamento/2/confirmar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 40. Reagendar p/ horário ocupado (409 + rollback)

`PATCH http://localhost:8080/agendamento/1/reagendar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `409`
- **Observação:** este par de requisições demonstra o `@Transactional`. O reagendamento falha (409), e a consulta original continua `AGENDADO` (rollback).

```json
{
  "novaData": "2027-03-01",
  "novaHora": "10:00"
}
```

### 41. Consulta original continua AGENDADA (prova do rollback)

`GET http://localhost:8080/agendamento/1`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`
- **Observação:** este par de requisições demonstra o `@Transactional`. O reagendamento falha (409), e a consulta original continua `AGENDADO` (rollback).

### 42. Reagendar p/ 11:00 (ok)

`PATCH http://localhost:8080/agendamento/1/reagendar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

```json
{
  "novaData": "2027-03-01",
  "novaHora": "11:00"
}
```

### 43. Cancelar consulta

`PATCH http://localhost:8080/agendamento/2/cancelar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 44. Horários livres após cancelar (10:00 liberado)

`GET http://localhost:8080/agendamento/horarios-livres?medicoId=1&data=2027-03-01`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 45. Agenda do médico no dia

`GET http://localhost:8080/agendamento/medico/1?data=2027-03-01`

- **Autenticação:** token do **MÉDICO**
- **Resposta esperada:** `200`

### 46. Listar por status

`GET http://localhost:8080/agendamento/status/CANCELADO`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`

### 47. Listar todos

`GET http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `200`


## 7. Regras de negócio e validações

### 48. Mesmo médico, mesmo horário (409)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `409`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "11:00"
}
```

### 49. Agendar no domingo (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-02-28",
  "horaConsulta": "09:00"
}
```

### 50. Fora do horário da clínica - 19:00 (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "19:00"
}
```

### 51. Horário quebrado - 08:15 (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "08:15"
}
```

### 52. Fora da jornada do médico (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "15:00"
}
```

### 53. Data passada (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 2,
  "medicoId": 1,
  "dataConsulta": "2020-01-06",
  "horaConsulta": "09:00"
}
```

### 54. Paciente inativo (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{
  "pacienteId": 3,
  "medicoId": 1,
  "dataConsulta": "2027-03-01",
  "horaConsulta": "08:00"
}
```

### 55. Campos obrigatórios vazios (400)

`POST http://localhost:8080/agendamento`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `400`

```json
{}
```

### 56. Cancelar consulta já cancelada (409)

`PATCH http://localhost:8080/agendamento/2/cancelar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `409`

### 57. Realizar consulta de data futura (400)

`PATCH http://localhost:8080/agendamento/3/realizar`

- **Autenticação:** token do **MÉDICO**
- **Resposta esperada:** `400`

### 58. Inativar usuário MEDICO (regra: 400)

`PATCH http://localhost:8080/usuario/3/inativar`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `400`


## 8. Segurança (401 e 403)

### 59. Sem token (401)

`GET http://localhost:8080/pacientes`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `401`

### 60. Token inválido (401)

`GET http://localhost:8080/medico`

- **Autenticação:** `Bearer token-falso` (de propósito)
- **Resposta esperada:** `401`

### 61. RECEPCIONISTA cadastrando usuário (403)

`POST http://localhost:8080/usuario`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `403`

```json
{
  "login": "x",
  "senha": "xxxxxx",
  "perfil": "ADMIN"
}
```

### 62. RECEPCIONISTA cadastrando médico (403)

`POST http://localhost:8080/medico`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `403`

```json
{}
```

### 63. RECEPCIONISTA marcando consulta realizada (403)

`PATCH http://localhost:8080/agendamento/3/realizar`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `403`

### 64. MEDICO acessando pacientes (403)

`GET http://localhost:8080/pacientes`

- **Autenticação:** token do **MÉDICO**
- **Resposta esperada:** `403`

### 65. MEDICO cancelando consulta (403)

`PATCH http://localhost:8080/agendamento/3/cancelar`

- **Autenticação:** token do **MÉDICO**
- **Resposta esperada:** `403`

### 66. ADMIN inativa RECEPCIONISTA

`PATCH http://localhost:8080/usuario/2/inativar`

- **Autenticação:** token do **ADMIN**
- **Resposta esperada:** `200`

### 67. Login de usuário inativo (401)

`POST http://localhost:8080/login`

- **Autenticação:** nenhuma (sem token)
- **Resposta esperada:** `401`

```json
{
  "login": "recep",
  "senha": "recep123"
}
```

### 68. Token antigo de usuário inativo (401)

`GET http://localhost:8080/pacientes`

- **Autenticação:** token da **RECEPCIONISTA**
- **Resposta esperada:** `401`
