# Savasana

## Description
Ce projet est une application utilisant Angular pour son front-end et Spring Boot pour son back-end. Les utilisateurs peuvent consulter, créer et mettre à jour des sessions de yoga.

L'application utilise pour sa réalisation de tests Jest côté front-end, Cypress côté end-2-end, ainsi que JUnit accompagné de Mockito pour le back-end.

## Prérequis
- **Java 11**
- **NodeJS 16**
- **MySQL**
- **Angular CLI 14**

## Installation
### Cloner le dépôt
```
git clone https://github.com/msjemenblc/savasana.git
cd savasana
```

### Configuration de la base de données
Générez une nouvelle base de données, suivi de ce script pour la création des tables :

```
CREATE TABLE `TEACHERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50),
  `description` VARCHAR(2000),
  `date` TIMESTAMP,
  `teacher_id` int,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
  `user_id` INT, 
  `session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'); 
```

### Installer les dépendances
- Installez celles du front-end grâce à la commande npm install
- Pour le back-end utilisez la commande mvn clean install

### Lancer le projet
- Pour lancer le back-end, il faut utiliser la commande mvn spring-boot:run
- Pour le front-end, c'est la commande ng serve

### Naviguer sur l'application
Vous pouvez vous connectez avec des identifiants admin générés par défaut :
- login: yoga@studio.com
- password: test!1234

### Générer les rapports de tests
- Front-end : npm test
- Front-end (visualiser le coverage) : npm test -- --coverage

- end-2-end : npx cypress open

- Back-end : mvn test ou mvn clean test
- Back-end (visualiser le coverage) : mvn jacoco:report
-> Allez ensuite dans /back/target/site/jacoco/index.html pour obtenir le tableau