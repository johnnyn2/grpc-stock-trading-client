package com.example.grpclientsvc.service;

import com.example.StockRequest;
import com.example.StockResponse;
import com.example.StockTradingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {
    private final StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;
    private final StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    public StockClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stockTradingServiceBlockingStub = StockTradingServiceGrpc.newBlockingStub(channel);
        this.stockTradingServiceStub = StockTradingServiceGrpc.newStub(channel);
    }

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();
        return stockTradingServiceBlockingStub.getStockPrice(request);
    }

    public void subscribeStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();
        System.out.println("request: " + request);
        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("Stock price update: " + stockResponse.getStockSymbol() +
                        ", Price: " + stockResponse.getPrice() +
                        ", Time: " + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }
        });

        // Keep the main thread alive to receive stream updates
        try {
            Thread.sleep(10000); // Simulate client waiting for updates
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
