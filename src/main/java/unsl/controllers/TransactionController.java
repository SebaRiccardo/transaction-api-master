package unsl.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsl.entities.ResponseError;
import unsl.entities.Transaction;
import unsl.services.TransactionServices;

@RestController
public class TransactionController{
    
    @Autowired
    TransactionServices transactionService;
    
    @GetMapping(value ="/transactions")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)  
    public List<Transaction> getAll(){
        return transactionService.getAll();
    }
     
    @GetMapping(value="/transactions/{id}")
    @ResponseBody
    public Object getTransaction(@PathVariable("id")long transactionId){
           Transaction transaction = transactionService.getTransaction(transactionId);
           if(transaction==null){
               return new ResponseEntity(new ResponseError(404,String.format("Transaction %d not found", transaction.getId())),HttpStatus.NOT_FOUND);
           }
           return transaction;
    }
    
    @PostMapping(value = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Object createTransaction(@RequestBody Transaction transaction){
       return transactionService.saveTransaction(transaction);
    
    }

}


