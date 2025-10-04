package com.example.grpclientsvc.service;

import com.example.StockRequest;
import com.example.StockResponse;
import com.example.StockTradingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {
    private final StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;

    public StockClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stockTradingServiceBlockingStub = StockTradingServiceGrpc.newBlockingStub(channel);
    }

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();
        return stockTradingServiceBlockingStub.getStockPrice(request);
    }
}
