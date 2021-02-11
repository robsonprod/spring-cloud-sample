# spring-cloud-sample
 Desafio Parent (nome-modulo-parent 1.0.0)

Technology :
- [Spring Cloud Config Server](https://spring.io/projects/spring-cloud-netflix)
- [Java Platform (JDK) 8](https://www.java.com/pt-BR/download/help/java8_pt-br.html)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Hibernate](https://hibernate.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/)
- [ Redis ](https://redis.io/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [Zuul service](https://github.com/Netflix/zuul/wiki)  
- [Eureka service]() 

Clone the repo:
``` bash
git clone git@github.com:robsonprod/spring-cloud-sample.git
cd spring-cloud-sample
```

Install dependencies:
``` bash
../mvnw spring-boot:run
```

Initialize the submodule:
``` bash
git submodule init
```

Update the submodule:
``` bash
git submodule update
```

Update Persistence Config:
spring-cloud-sample\desafio-persistence\src\main\resources
``` bash
db.hostname=localhost
db.database=desafio
db.username=postgres
db.password=123
```

Docker Up (Redis and RabbitMQ):
spring-cloud-sample\docker
docker-compose.yml file, then run docker-compose up.


spring-cloud-app how to install:
Open a terminal window and go to the project root folder.
You need to have npm installed globally.
``` bash
npm i
```
spring-cloud-app how to run:
Open a terminal window and go to the project root folder.
You need to have Angular installed globally.
``` bash
ng serve
```


Run it in order:
``` bash
1. desafio-configuration
2. desafio-eureka-server (discovery)
3. desafio-users
4. desafio-zuul (proxy) - call endpoints
5. spring-cloud-app
```

Config Enviroment variables:
<p>in application.yml</p>

```bash
git:
  uri: ${GIT_HOSTNAME}
  username: ${GIT_USERNAME}
  password: ${GIT_PASSWORD}
  search-paths: developer*
```

<p>example</p>

``` bash
1. config
GIT_HOSTNAME=https://github.com/robsonprod/spring-cloud-sample-config.git;GIT_USERNAME=robsonprod@gmail.com;GIT_PASSWORD=#
```

Config RabbitMQ
<p>In users.yml update </p>

``` bash
rabbitmq:
    host: localhost
    username: test
    password: "test"
    port: 5672
```
<p>Create Exchanges desafio-exchange same as RabbitConfig topicExchangeName and add a queue </p>
