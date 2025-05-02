# urls-tds-company

# URL Shortener API

## 📋 Sobre o Projeto
API REST para encurtamento de URLs desenvolvida com Spring Boot. O sistema permite criar URLs curtas, redirecionamento para URLs originais e acompanhamento de estatísticas de acesso.

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Swagger/OpenAPI

## ⚙️ Funcionalidades

- Criação de URLs curtas
- Redirecionamento para URLs originais
- Busca de URL por ID
- Estatísticas de acesso (diário e total)
- Validação de URLs duplicadas
- Documentação da API com Swagger

## 📦 Estrutura do Projeto

src/
├── main/
│ ├── java/
│ │ └── com/tds/urls_tds_company/
│ │ ├── config/
│ │ ├── exception/
│ │ ├── model/
│ │ ├── repository/
│ │ ├── service/
│ │ └── web/
│ └── resources/
│ └── application.properties
└── test/
└── java/
└── com/tds/urls_tds_company/
├── service/
└── web/


## 🚀 Como Executar

### Pré-requisitos

- Java 17
- Maven
- PostgreSQL

### Configuração do Banco de Dados

1. Execute o arquivo docker-compose
    docker-compose up -d



### Executando a Aplicação

```bash
# Clone o repositório
git clone git@github.com:mauriciofcjr/urls-tds-company.git

# Entre no diretório
cd urls-tds-company

# Compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

## 📌 Endpoints da API

### Criar URL Curta
```http
POST /api/v1/url
```
```json
{
    "url": "https://www.exemplo.com.br"
}

http://localhost:8080/swagger-ui.html
```

### Buscar URL por ID
```http
GET /api/v1/url/{id}
```

### Redirecionar para URL Original
```http
GET /api/v1/url/original/{shortUrl}
```

### Obter Estatísticas
```http
GET /api/v1/url/estatisticas/{shortUrl}
```

## 📊 Respostas da API

### Sucesso na Criação
```json
{
    "id": 1,
    "url": "https://www.exemplo.com.br",
    "shortUrl": "abc123"
}
```

### Estatísticas
```json
{
    "acessosDiarios": 5,
    "acessoTotal": 10
}
```

## ⚠️ Tratamento de Erros

A API inclui tratamento para os seguintes casos:
- URL não encontrada (404)
- URL duplicada (409)
- Dados inválidos (422)
- Erro interno do servidor (500)

## 🧪 Testes

O projeto inclui testes unitários para as camadas de serviço e controller. Para executar os testes:

```bash
mvn test
```

### Cobertura de Testes

- Testes unitários para `UrlController`
- Testes unitários para `UrlService`
- Validação de casos de sucesso e erro
- Testes de integração com banco de dados

## 📚 Documentação

A documentação completa da API está disponível através do Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## 🔐 Segurança

- Validação de URLs
- Proteção contra duplicação de URLs
- Sanitização de entradas
- Logs de acesso e erros

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ✒️ Autores

* **Seu Nome** - *Desenvolvimento* - [seu-usuario](https://github.com/seu-usuario)

## 📄 Versionamento

Usamos [SemVer](http://semver.org/) para controle de versão. Para as versões disponíveis, veja as [tags neste repositório](https://github.com/seu-usuario/urls-tds-company/tags).

---
⌨️ com ❤️ por [seu-usuario](https://github.com/seu-usuario) 😊

Este README.md fornece:
Visão geral do projeto
Tecnologias utilizadas
Instruções detalhadas de instalação e execução
Documentação dos endpoints
Exemplos de requisições e respostas
Informações sobre testes
Estrutura do projeto
Instruções para contribuição
Informações sobre licença e autoria
O README está formatado em Markdown, com:
Emojis para melhor visualização
Seções bem organizadas
Exemplos de código
Instruções claras
Estrutura de diretórios
Exemplos de requisições e respostas
Você pode personalizar:
URLs do repositório
Informações de autor
Configurações específicas do seu ambiente
Adicionar ou remover seções conforme necessário