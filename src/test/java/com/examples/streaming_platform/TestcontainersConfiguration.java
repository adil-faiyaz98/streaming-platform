package com.examples.streaming_platform;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.milvus.MilvusContainer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.qdrant.QdrantContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection(name = "azure-storage/azurite")
	GenericContainer<?> azuriteContainer() {
		return new GenericContainer<>(DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:latest")).withExposedPorts(10000, 10001, 10002);
	}

	@Bean
	@ServiceConnection
	ElasticsearchContainer elasticsearchContainer() {
		return new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10"));
	}

	@Bean
	@ServiceConnection
	KafkaContainer kafkaContainer() {
		return new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));
	}

	@Bean
	@ServiceConnection
	MilvusContainer milvusContainer() {
		return new MilvusContainer(DockerImageName.parse("milvusdb/milvus:latest"));
	}

	@Bean
	@ServiceConnection
	OllamaContainer ollamaContainer() {
		return new OllamaContainer(DockerImageName.parse("ollama/ollama:latest"));
	}

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> pgvectorContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
	}

	@Bean
	@ServiceConnection
	QdrantContainer qdrantContainer() {
		return new QdrantContainer(DockerImageName.parse("qdrant/qdrant:latest"));
	}

}
