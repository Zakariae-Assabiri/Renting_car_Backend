# Backend Location Voiture

Ce projet est l'API backend d'une application de gestion de location de voitures. Il permet aux gestionnaires d'agence de location de voitures de suivre les véhicules, gérer les réservations, les clients, et effectuer diverses tâches administratives liées à la location de voitures.

## Fonctionnalités

- **Gestion des véhicules** : Ajouter, mettre à jour et supprimer des informations sur les voitures disponibles à la location.
- **Gestion des réservations** : Créer, modifier et annuler des réservations de véhicules.
- **Gestion des clients** : Ajouter, mettre à jour et supprimer des clients.
- **Gestion des utilisateurs** : Authentification et autorisation des utilisateurs (gestionnaires et clients) avec Spring Security.
- **Gestion des paiements** : Estimation et gestion des paiements des réservations.
- **Estimation financière** : Fourniture de rapports et estimations sur les revenus et dépenses de l'agence.

## Technologies

- **Java 17**
- **Spring Boot** pour la création de l'API REST.
- **Spring Security** pour l'authentification et la gestion des rôles.
- **PostgreSQL** comme base de données.
- **JPA/Hibernate** pour la gestion des entités et de la persistance des données.
- **JWT (JSON Web Token)** pour l'authentification basée sur les tokens.
- **Lombok** pour la réduction du code boilerplate.
- **Thymeleaf** pour le rendu des vues HTML (si nécessaire).

## Prérequis

- Java 17 ou version supérieure.
- PostgreSQL installé et configuré.
- Maven pour la gestion des dépendances.
- Une clé secrète pour la gestion des JWT.
