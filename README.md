# inventory-service
Inventory management system where you can 
1. Buy products.
2. Upload products.
3. Get products.
4. Upload inventory.

# Software, tools and frameworks
1. Java 8
2. Spring boot 2.4.1
3. Gradle 6.3
4. Junit and Mockito etc.

# Build and package
```
gradle clean build 
```

# Run

```
java -Dspring.profiles.active=dev -jar build/libs/inventory-service-1.0.0.jar (Using bash)
```
```
java -Dspring.profiles.active=prod -jar build/libs/inventory-service-1.0.0.jar (Using bash)
```
# Swagger URL

```
http://localhost:8080/inventory-service/swagger-ui.html
```

# Health check URL

```
http://localhost:8080/inventory-service/actuator/health
```

# H2 console

```
http://localhost:8080/inventory-service/h2-console/
Password: inventory
```