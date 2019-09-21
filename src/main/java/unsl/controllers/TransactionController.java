package unsl.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsl.entities.ResponseError;
import unsl.entities.Transaction;
import unsl.services.TransactionServices;

@RestController
//@RequestMapping("/transacciones")
public class TransactionController {

    @Autowired
    TransactionServices transactionService;

    @GetMapping(value = "/transactions")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping(value = "/transactions/{id}")
    @ResponseBody
    public Object getTransaction(@PathVariable("id") long transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        if (transaction == null) {
            return new ResponseEntity(
                    new ResponseError(404, String.format("Transaction %d not found", transaction.getId())),
                    HttpStatus.NOT_FOUND);
        }
        return transaction;
    }

    @PostMapping(value = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Object createTransaction(@RequestBody Transaction transaction) {

        // supposed this is your FirstController url.
        String url = "http://localhost:8889/accounts/"+transaction.getOrigin_account_id();
        // create request.
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // execute your request.
        
        try {
            HttpResponse response = client.execute(request);
            
            BufferedReader jsonText = new BufferedReader(
		    new InputStreamReader(response.getEntity().getContent()));
             
            

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = rd.readLine()) != null) // Read line by line
                sb.append(line);
                System.out.println(line);
 

            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        return transactionService.saveTransaction(transaction);
    
    }


    // funcion actualizar saldo....

}


