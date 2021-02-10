# spring-cloud-sample
1. Desafio Parent (nome-modulo-parent 1.0.0)
- Cloud Config Server (nome-module 0.0.1)
- Java 11

Clone the repo:
``` bash
git clone git@github.com:robsonprod/spring-cloud-sample.git
cd spring-cloud-sample
```

Install dependencies:
``` 
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

With docker:
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
1. config
2. eureka server (discovery)
3. users
4. zuul (proxy) - call endpoints
```

Config Enviroment variables:
``` bash
1. config
GIT_HOSTNAME=https://github.com/robsonprod/spring-cloud-sample-config.git;GIT_USERNAME=#;GIT_PASSWORD=#
```


