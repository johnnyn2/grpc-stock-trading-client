### React (Vite) + Envoy (Proxy) + Spring Boot (gRPC)
This repository demostrate how a Spring Boot microservice can fire gRPC HTTP/2 request to gRPC server.

Four modes:
1. Unary (Like REST API, 1 request, 1 response)
2. Server Streaming (1 request, Many response)
3. Client Streaming (Many request, 1 response)
4. Bidirectional (Like WebSocket)

### Project Structure
- ```src/main/proto``` Protobuf Schema like Swagger file
- ```pom.xml``` Configuration for generate HTTP client
- Right-Click ```target/generated-sources``` folder. Select <i>Mark Directory as</i>. Select <i>Sources Root</i>

### Repository
- [grpc-stock-trading-vite-client](https://github.com/johnnyn2/grpc-stock-trading-vite-client) : Browser frontend
- [grpc-stock-trading-envoy](https://github.com/johnnyn2/grpc-stock-trading-envoy) : Envoy Proxy
- [grpc-stock-trading-server](https://github.com/johnnyn2/grpc-stock-trading-server) : gRPC backend server
- [grpc-stock-trading-client](https://github.com/johnnyn2/grpc-stock-trading-client) : gRPC service client