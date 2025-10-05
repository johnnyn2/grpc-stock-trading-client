package com.example.grpclientsvc.service;

import com.example.*;
import com.example.OrderSummary;
import com.example.StockOrder;
import com.example.StockRequest;
import com.example.StockResponse;
import com.example.StockTradingServiceGrpc;
import com.example.TradeStatus;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    public void getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();
        StockResponse response = stockTradingServiceBlockingStub.getStockPrice(request);
        System.out.println("Stock Symbol: " + response.getStockSymbol()
                + ", Price: " + response.getPrice()
                + ", Timestamp: " + response.getTimestamp());
    }

    public void subscribeStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(stockSymbol).build();
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

    public void bulkStockOrder() throws InterruptedException {
        StreamObserver<StockOrder> streamObserver =  stockTradingServiceStub.bulkStockOrder(new StreamObserver<OrderSummary>() {
            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println("Order Summary - Total Orders: " + orderSummary.getTotalOrders() +
                        ", Success Count: " + orderSummary.getSuccessCount() + ", Total Amount" + orderSummary.getTotalAmount());
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
        try {
            IntStream.range(0, 10).forEach(i ->
                    streamObserver.onNext(StockOrder.newBuilder()
                            .setPrice(i * 10.0)
                            .setOrderId(Integer.toString(i))
                            .setStockSymbol("GOOGLE")
                            .setQuantity(i)
                            .build())
            );
            streamObserver.onCompleted();
        } catch (Exception e) {
            streamObserver.onError(e);
            throw e;
        } finally {
            // Allow time for the server to process and response
            Thread.sleep(2000);
        }
    }

    public void liveTrading() throws InterruptedException {
        StreamObserver<StockOrder> streamObserver = stockTradingServiceStub.liveTrading(new StreamObserver<TradeStatus>() {
            @Override
            public void onNext(TradeStatus tradeStatus) {
                System.out.println("Trade Status: " + tradeStatus);
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
        try {
            IntStream.range(0, 10).forEach(i -> {
                streamObserver.onNext(StockOrder.newBuilder()
                        .setPrice(i * 10.0)
                        .setOrderId(Integer.toString(i))
                        .setStockSymbol("GOOGLE")
                        .setQuantity(i)
                        .build());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            streamObserver.onCompleted();
        } catch (Exception e) {
            streamObserver.onError(e);
            throw e;
        } finally {
            // Allow time for the server to process and response
            Thread.sleep(2000);
        }
    }
}
