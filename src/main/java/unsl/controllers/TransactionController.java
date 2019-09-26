package unsl.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
import unsl.entities.Amount;
import unsl.entities.ResponseError;
import unsl.entities.Transaction;
import unsl.services.TransactionServices;
import unsl.utils.RestService;


@RestController
public class TransactionController {
     
    
    @Autowired
    TransactionServices transactionService;
   
    @Autowired
    RestService  restService; 

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
    public Object createTransaction(@RequestBody Transaction transaction) throws Exception {
       Amount amount_for_Origin = new Amount();
       Amount amount_for_destination = new Amount(); 
      
       Account origin_Account;
       Account destination_Account;


       /**importe mayor a cero */
       if(transaction.getAmount().equals(new BigDecimal(0))){
        return new ResponseEntity(new ResponseError(400, "The amount to transfer can't be zero"),
        HttpStatus.NOT_FOUND);
       }
       /** importe positivo */
       if((transaction.getAmount().compareTo(new BigDecimal(-1)) == -1) || (transaction.getAmount().compareTo(new BigDecimal(-1))==0)){
        return new ResponseEntity(new ResponseError(400, "The amount to transfer can't be negative"),
        HttpStatus.NOT_FOUND);
       }
       /* obtengo dos accounts */
       origin_Account= restService.getAccount(String.format("http://localhost:8889/accounts/%d",transaction.getOrigin_account_id()));
       destination_Account= restService.getAccount(String.format("http://localhost:8889/accounts/%d",transaction.getDestination_account_id()));
       /**  una cuenta no pude transferir a la misma cuenta*/
       if(origin_Account.getId()== destination_Account.getId()){
        return new ResponseEntity(new ResponseError(400, "The origin account and destination account must be different accounts"),HttpStatus.NOT_FOUND);
       }
       /* el tipo de moneda tiene que ser el mismo */
       if (!origin_Account.getCurrency().equals(destination_Account.getCurrency()) ) {
        return new ResponseEntity(new ResponseError(400, "The origin account and destination account must have the same currency"),HttpStatus.NOT_FOUND);
       }
     
       /* debe tener suficiente dinero para transferir */
       if(transaction.getAmount().compareTo(origin_Account.getAccount_balance())>0) {
        return new ResponseEntity(new ResponseError(400, "Insufficient money to transfer"),
                HttpStatus.NOT_FOUND);
       }

       /* trasnferencia descuenta de una y suma en la otra*/ 
       
       amount_for_Origin.setAmount(origin_Account.getAccount_balance().subtract(transaction.getAmount()));  
       amount_for_destination.setAmount(destination_Account.getAccount_balance().add(transaction.getAmount()));

       restService.putAccount(String.format("http://localhost:8889/accounts/%d",origin_Account.getId()),amount_for_Origin);
       restService.putAccount(String.format("http://localhost:8889/accounts/%d",destination_Account.getId()),amount_for_destination);
       
       return transactionService.saveTransaction(transaction,"PROCESADA");
    
    }
     /*
    @PutMapping(value = "/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateStatus(@RequestBody String status){
        
         
        if(status.equals("PROCESADA")){
          

         }  

    }
   */


    

}


