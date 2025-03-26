CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- Ensures that PostgreSQL supports UUIDs

CREATE TABLE TB_ROLE (
    id UUID PRIMARY KEY,
    role VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE TB_USER (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE TB_USER_ROLE (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES TB_USER(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES TB_ROLE(id) ON DELETE CASCADE
);

CREATE TABLE TB_RECIPE (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    instructions TEXT NOT NULL,
    servings VARCHAR(255),
    user_id UUID,
    CONSTRAINT fk_recipe_user FOREIGN KEY (user_id) REFERENCES TB_USER(id) ON DELETE CASCADE
);

CREATE TABLE TB_INGREDIENT (
    id UUID PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    ingredient_unit VARCHAR(255) NOT NULL,
    ingredient_type VARCHAR(255) NOT NULL,
    recipe_id UUID,
    CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES TB_RECIPE(id) ON DELETE CASCADE
);

