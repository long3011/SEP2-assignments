# SEP2 Shopping Cart (Localization + Docker + CI)

Simple Java console shopping cart application for SEP2 Week 1 assignment.

## Features

- Console input for item price and quantity
- Shopping cart total calculation (`price * quantity`)
- Localization with UTF-8 message bundles:
  - English (`en`)
  - Finnish (`fi`)
  - Swedish (`sv`)
  - Japanese (`ja`)
- Unit and integration-like tests with JUnit 5
- Docker image build support
- Jenkins pipeline for build/test/image push

## Project Structure

- `src/main/java/org/example/shoppingcart`
  - `Main` - entry point
  - `ShoppingCartApplication` - interactive console flow
  - `LocaleResolver` - language code to locale mapping
  - `CartCalculator` - total calculations using `BigDecimal`
- `src/main/resources/messages_*.properties` - localized messages
- `src/test/java/org/example/shoppingcart` - test suite
- `Dockerfile` - multi-stage build image
- `Jenkinsfile` - CI/CD pipeline example

## Run Locally

```bash
mvn clean package
java -jar target/SEP2-assignments-1.0-SNAPSHOT.jar
```

Windows terminal note (for proper Japanese output):

```powershell
chcp 65001
java -Dfile.encoding=UTF-8 -jar target/SEP2-assignments-1.0-SNAPSHOT.jar
```

## Run Tests

```bash
mvn test
```

## Docker

Build image:

```bash
docker build -t your-dockerhub-username/sep2-shopping-cart:latest .
```

Run container interactively:

```bash
docker run -it --rm your-dockerhub-username/sep2-shopping-cart:latest
```

## Jenkins CI/CD

`Jenkinsfile` stages:

1. Checkout
2. Build and test with Maven
3. Build Docker image (`latest` + build number tag)
4. Push image to Docker Hub

Configure Jenkins credentials:

- ID: `dockerhub-credentials`
- Type: Username with password

Update `IMAGE_NAME` in `Jenkinsfile` with your Docker Hub repository.

## Play with Docker Verification

In [Play with Docker](https://labs.play-with-docker.com/):

1. Log in to Docker Hub (`docker login`)
2. Pull your image:
   - `docker pull your-dockerhub-username/sep2-shopping-cart:latest`
3. Run:
   - `docker run -it --rm your-dockerhub-username/sep2-shopping-cart:latest`
4. Verify language prompt and total calculation flow.

