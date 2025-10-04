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
        System.out.println(stockClientService.getStockPrice("GOOGLE"));
    }
}
