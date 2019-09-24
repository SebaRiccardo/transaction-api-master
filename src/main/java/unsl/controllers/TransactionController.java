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

import unsl.entities.Account;
import unsl.entities.ResponseError;
import unsl.entities.Transaction;
import unsl.services.TransactionServices;
import unsl.utils.RestService;

@RestController
//@RequestMapping("/transacciones")
public class TransactionController {
     
    
    @Autowired
    TransactionServices transactionService;
    @Autowired
    RestService restService; 

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
       long amount;
       Account origin_Account;
       Account destination_Account;
       
       /* obtengo dos accounts */
       origin_Account= restService.getAccount("localhost:8889/accounts/%d",transaction.getOrigin_account_id());
       destination_Account= restService.getAccount("localhost:8889/accounts/%d",transaction.getDestination_account_id());

       /* trasnferencia*/ 
       restService.putAmount(String.format("http://localhost:8889/%d", "id de user" )),amount );


        return transactionService.saveTransaction(transaction);
    
    }


    

}


