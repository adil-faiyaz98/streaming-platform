-- Create user_interactions table
CREATE TABLE user_interactions (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    item_id VARCHAR(255) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    interaction_type VARCHAR(50) NOT NULL,
    value DOUBLE PRECISION,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    context_data JSONB
);

-- Create indexes for user_interactions
CREATE INDEX idx_user_interactions_user_id ON user_interactions(user_id);
CREATE INDEX idx_user_interactions_item_id ON user_interactions(item_id);
CREATE INDEX idx_user_interactions_user_item ON user_interactions(user_id, item_id);
CREATE INDEX idx_user_interactions_item_type ON user_interactions(item_id, interaction_type);
CREATE INDEX idx_user_interactions_timestamp ON user_interactions(timestamp);

-- Create content_items table
CREATE TABLE content_items (
    item_id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    release_year INTEGER,
    director VARCHAR(255),
    duration INTEGER,
    maturity_rating VARCHAR(50),
    average_rating DOUBLE PRECISION,
    popularity_score DOUBLE PRECISION,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL,
    feature_vector BYTEA
);

-- Create content_item_genres table
CREATE TABLE content_item_genres (
    item_id VARCHAR(255) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    PRIMARY KEY (item_id, genre),
    FOREIGN KEY (item_id) REFERENCES content_items(item_id) ON DELETE CASCADE
);

-- Create content_item_actors table
CREATE TABLE content_item_actors (
    item_id VARCHAR(255) NOT NULL,
    actor VARCHAR(255) NOT NULL,
    PRIMARY KEY (item_id, actor),
    FOREIGN KEY (item_id) REFERENCES content_items(item_id) ON DELETE CASCADE
);

-- Create content_item_keywords table
CREATE TABLE content_item_keywords (
    item_id VARCHAR(255) NOT NULL,
    keyword VARCHAR(255) NOT NULL,
    PRIMARY KEY (item_id, keyword),
    FOREIGN KEY (item_id) REFERENCES content_items(item_id) ON DELETE CASCADE
);

-- Create recommendation_models table
CREATE TABLE recommendation_models (
    id BIGSERIAL PRIMARY KEY,
    model_type VARCHAR(50) NOT NULL,
    version VARCHAR(50) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    training_duration_ms BIGINT,
    data_points_count INTEGER,
    precision_metric DOUBLE PRECISION,
    recall_metric DOUBLE PRECISION,
    f1_score DOUBLE PRECISION,
    mean_average_precision DOUBLE PRECISION,
    is_active BOOLEAN NOT NULL,
    parameters JSONB
);

-- Create indexes for recommendation_models
CREATE INDEX idx_recommendation_models_type_active ON recommendation_models(model_type, is_active);
CREATE INDEX idx_recommendation_models_created_at ON recommendation_models(created_at);
