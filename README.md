# 🎯 Projeto: API Spring Boot 3 + JDBI + AWS SNS/SQS

Este projeto demonstra como criar uma API REST em **Java 21 + Spring Boot 3**, utilizando **JDBI** para operações com banco de dados, integração com **AWS SNS** para publicação de eventos e (opcionalmente) consumo com **SQS**.

---

## 🚀 Tecnologias

- Java 21
- Spring Boot 3
- JDBI 3
- AWS SNS (publisher)
- AWS SQS (listener opcional)
- MariaDB / PostgreSQL
- Spring Cloud AWS

---

## 💡 Objetivo

Exemplificar para desenvolvedores como:

- Implementar um **CRUD usando JDBI**, amplamente adotado por empresas por sua leveza e simplicidade.
- Publicar eventos para AWS SNS.
- Configurar um listener para AWS SNS (exemplo opcional).

---

## 🛠️ Exemplo de Implementação

### 🔗 Publicação de Eventos SNS

Classe `UserPublisher` responsável por publicar eventos de usuário:

```java
@Service
@Slf4j
public class UserPublisher {

    private final SnsClient snsClient;
    private final String userEventTopicArn;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserPublisher(SnsClient snsClient,
                         @Qualifier("userEventsTopicArn") String userEventTopicArn,
                         ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.userEventTopicArn = userEventTopicArn;
        this.objectMapper = objectMapper;
    }

    public void publishUserEvent(User user, EventType eventType) {
        try {
            String message = objectMapper.writeValueAsString(
                Envelope.builder()
                    .eventType(eventType)
                    .data(objectMapper.writeValueAsString(user))
                    .build()
            );

            snsClient.publish(PublishRequest.builder()
                    .topicArn(userEventTopicArn)
                    .message(message)
                    .subject("User Event: " + eventType.name())
                    .build());

            log.info("User event published: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish event", e);
        }
    }
}
```
---

### 🔧 Configuração do SNS

```java
@Configuration
public class SnsConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.topic.arn.user}")
    private String awsUserEventsTopicArn;

    @Bean
    public SnsClient snsClient(){
        return SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean(name = "userEventsTopicArn")
    public String snsUserEventTopicArn() {
        return awsUserEventsTopicArn;
    }
}
```

---

### 📝 CRUD com JDBI

#### Interface `UserDao`

```java
@RegisterBeanMapper(UserMapper.class)
public interface UserDao {

    @SqlUpdate("INSERT INTO User_TB(identification, nome, email) VALUES(:identification, :nome, :email)")
    @GetGeneratedKeys
    String insert(@BindBean User user);

    @SqlQuery("SELECT * FROM User_TB WHERE identification = :identification")
    User findByIdentification(@Bind("identification") String identification);

    @SqlQuery("SELECT * FROM User_TB")
    Set<User> findAll();

    @SqlUpdate("DELETE FROM User_TB WHERE identification = :identification")
    void delete(@Bind("identification") String identification);

    @SqlUpdate("UPDATE User_TB SET email = :email WHERE identification = :identification")
    String update(@Bind("email") String email, @Bind("identification") String identification);
}
```

#### Serviço `UserServices`

```java
@Service
@Slf4j
public class UserServices {

    @Autowired
    private UserDao userDao;

    @Transactional
    public String createUser(User user) {
        user.setIdentification(UUID.randomUUID().toString());
        return userDao.insert(user);
    }

    @Transactional(readOnly = true)
    public User getUserByIdentification(String identification) {
        return userDao.findByIdentification(identification);
    }

    @Transactional(readOnly = true)
    public Set<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public void deleteUser(String identification) {
        userDao.delete(identification);
    }

    @Transactional
    public User updateUser(UserUpdateDto dto, String identification) {
        userDao.update(dto.getEmail(), identification);
        return getUserByIdentification(identification);
    }
}
```

---

### 🔎 UserMapper

```java
@Component
@Slf4j
public class UserMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return User.builder()
                .identification(rs.getString("identification"))
                .nome(rs.getString("nome"))
                .email(rs.getString("email"))
                .build();
    }
}
```

---

### ⚙️ Configuração do JDBI

```java
@Configuration
@Slf4j
public class JdbiConfiguration {

    @Bean
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> plugins, List<RowMapper<?>> mappers) {
        Jdbi jdbi = Jdbi.create(new TransactionAwareDataSourceProxy(ds));
        plugins.forEach(jdbi::installPlugin);
        mappers.forEach(jdbi::registerRowMapper);
        return jdbi;
    }

    @Bean
    public UserDao userDao(Jdbi jdbi) {
        return jdbi.onDemand(UserDao.class);
    }

    @Bean
    public JdbiPlugin sqlObjectPlugin() {
        return new SqlObjectPlugin();
    }
}
```

---

## 💻 Configuração `application.yml`

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: org.mariadb.jdbc.Driver

aws:
  region: ${AWS_REGION}
  topic:
    arn:
      user: arn:aws:sns:us-east-1:123456789012:user-events
```

---

## 🎯 Endpoints da API

| Método | Endpoint          | Descrição        |
| ------ | ----------------- | ---------------- |
| POST   | `/api/users`      | Cria usuário     |
| GET    | `/api/users/{id}` | Busca usuário    |
| PUT    | `/api/users/{id}` | Atualiza usuário |
| DELETE | `/api/users/{id}` | Remove usuário   |

---

## ✔️ Considerações Finais

Este projeto serve como base para:

* Projetos reais que busquem alta performance usando JDBI ao invés de JPA.
* Integração com AWS SNS para eventos assíncronos.
* Escalabilidade e manutenção simplificada.

---


