
# Recipe Platform BE

The Recipe Platform project aims to create an online platform where users can share and discover new recipes.The primary
goal of this project is to create a community-driven platform that promotes food culture and encourages culinary
exploration. By providing a space for users to share, discover, and discuss recipes, we aim to foster a vibrant and
engaging community of food lovers. We believe that through this platform, we can contribute to the appreciation and
exploration of diverse cuisines and recipes.

# Key Features
1. User Recipe Management:
   Users can upload their own recipes, complete with ingredients, instructions, and photos. This feature allows for the sharing of unique, personal, or family recipes, contributing to the diversity of the platform.

2. Recipe Browsing and Searching:
   The platform will include a robust search function, allowing users to browse and search for recipes uploaded by other users. This could be based on specific ingredients, cuisine type, dietary restrictions, or other criteria.

3. Recipe Rating and Reviews:
   Users can rate and review recipes they've tried. This feature not only provides feedback to the recipe uploader but also helps other users in deciding which recipes to try.

4. Favorite Recipes:
   Users can create a list of their favorite recipes for easy access in the future. This personalization feature enhances the user experience by making it easier to find and revisit loved recipes.

5. Admin Management:
   Admins will have the ability to manage the platform, including approving new recipes, creating and managing categories or tags, and sending notifications to users. This ensures the quality and organization of the content on the platform.

6. Recommendation System:
   The platform will feature a recommendation system that suggests popular or relevant recipes based on user preferences. This personalized feature enhances user engagement and discovery of new recipes.

# Tools Required
1. JDK(Java Development Kit) :  Ensure you have JDK 11 or above installed.
2. Intellij IDEA             :  IDE for developing spring boot projects.
3. MySQL Workbench/shell     :  : Utilized for database design, management, and querying within the MySQL environment.

# Technologies:
1. Java                      :  Core programming language.
2. Maven                     :  A build tool used for dependency management and build automation.
3. Mysql                     :  Used as the relational database for persistent data storage and management.

# Architecture
The system is organized into distinct layers to separate concerns in accordance with MVC:

1. View/Controller Layer: Manages the GUI and user interactions.
2. Model Layer: Handles the business logic of the application, making use of DTOs (Data Transfer Objects) for efficient data operations.
3. DAO Layer: Interfaces with the database for CRUD operation

# Project Setup:
To run this project locally, you will need the following:

1. Install the required tools.
2. Clone the project from git repository
> git clone https://githyd.epam.com/epm-perp/june-10-team-3/recipe-platform-be
3. Switch to the project directory and configure MYSQL database
> cd recipe-platform-be

>CREATE DATABASE recipe-platform;
4. Compile and run the application with the following command.
> mvn clean install
> mvn spring-boot:run