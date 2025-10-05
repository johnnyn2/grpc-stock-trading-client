package com.example.grpclientsvc;

import com.example.grpclientsvc.service.StockClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpclientsvcApplication implements CommandLineRunner {

    @Autowired
    private StockClientService stockClientService;

	public static void main(String[] args) {
		SpringApplication.run(GrpclientsvcApplication.class, args);
	}

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // unary
        System.out.println("=====Unary=====");
        stockClientService.getStockPrice("GOOGLE");
        // server streaming
        System.out.println("=====Server Streaming=====");
        stockClientService.subscribeStockPrice("GOOGLE");
        // client streaming
        System.out.println("=====Client Streaming=====");
        stockClientService.bulkStockOrder();
        // bidirectional streaming
        System.out.println("=====Bidirectional Streaming=====");
        stockClientService.liveTrading();
    }
}
