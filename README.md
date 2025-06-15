# 🟢 Coleta Verde Inteligente - Backend

Sistema de agendamento de coleta de resíduos urbanos desenvolvido para modernizar a gestão pública de resíduos e facilitar o descarte consciente por parte da população. Este repositório corresponde à **API Backend** do projeto, implementada com **Java** e **Spring Boot**.

> 🔗 Acesse o repositório do [Frontend aqui](https://github.com/Andessonreis/coleta-verde-inteligente)

---

## 📑 Tabela de Conteúdos

- [📝 Introdução](#-introdução)
- [🚀 Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [⚙️ Funcionalidades](#️-funcionalidades)
- [📦 Instalação e Execução](#-instalação-e-execução)
- [🔧 Configuração](#-configuração)
- [📚 Documentação da API](#-documentação-da-api)
- [🔐 Regras de Negócio](#-regras-de-negócio)
- [🐞 Possíveis Problemas](#-possíveis-problemas)
- [👥 Contribuidores](#-contribuidores)
- [📄 Licença](#-licença)

---

## 📝 Introdução

O **Coleta Verde Inteligente** é uma solução pensada para atender a necessidade das prefeituras no controle e logística da coleta seletiva e especial. Por meio de uma aplicação web e um sistema backend robusto, os cidadãos conseguem agendar coletas de resíduos como móveis, eletrodomésticos e entulhos de forma rápida e digital.

---

## 🚀 Tecnologias Utilizadas

- **Java 21+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate**
- **MySQL**
- **Maven**
- **Swagger/OpenAPI**
---

## ⚙️ Funcionalidades

- ✅ Cadastro de agendamento com:
  - Endereço
  - Tipo de resíduo
  - Upload de foto (opcional)
- 📅 Consulta de datas disponíveis (com limite de agendamentos por dia)
- ❌ Cancelamento de agendamentos
- 📬 Confirmação de agendamento por e-mail
- 📖 Histórico de agendamentos do usuário
- 🔒 Restrição de agendamento:
  - 1 por residência a cada 30 dias
  - Apenas tipos de resíduos autorizados
  - Notificação e bloqueio de usuários por 60 dias em caso de descumprimento

---

## 🔧 Configuração

Edite o arquivo `src/main/resources/application.properties` com as seguintes informações:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/coleta_verde
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 📚 Documentação da API

A documentação da API pode estar disponível via Swagger:

```
http://localhost:8080/swagger-ui.html
```
---

## 🔐 Regras de Negócio

* Um agendamento permitido a cada 30 dias por residência
* Bloqueio automático de usuários por 60 dias em caso de descumprimento
* Validação dos tipos de resíduos (lista controlada)
* Limite diário de agendamentos por bairro/localidade

---

## 🐞 Possíveis Problemas

* ❗ Conexão com banco de dados falhando? Verifique as credenciais e URL no `application.properties`
* ❗ Datas de coleta indisponíveis? Confirme se os limites diários estão configurados corretamente

---

## 👥 Contribuidores

* [Andesson Reis](https://github.com/Andessonreis)
* [Jorge Roberto Argolo](https://github.com/JorgeRobertoArgolo)

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).