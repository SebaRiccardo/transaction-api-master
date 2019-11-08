package unsl.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import unsl.entities.Account;
import unsl.entities.Amount;
import unsl.entities.ResponseError;
import unsl.entities.Transaction;
import unsl.entities.Transaction.Status;
import unsl.services.TransactionServices;
import unsl.utils.RestService;


@RestController
public class TransactionController {
     private static String ipCuentas= "localhost";
     public static String port = ":8889";
     
    @Autowired
    TransactionServices transactionService;
   
    @Autowired
    RestService  restService; 
    
    @GetMapping(value = "/ping")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
       
        return "pong";
    }
   

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
            return new ResponseEntity( new ResponseError(404, String.format("Transaction with id %d not found", transactionId)),HttpStatus.NOT_FOUND);
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
        HttpStatus.BAD_REQUEST);
       }
       /** importe positivo */
       if((transaction.getAmount().compareTo(new BigDecimal(-1)) == -1) || (transaction.getAmount().compareTo(new BigDecimal(-1))==0)){
        return new ResponseEntity(new ResponseError(400, "The amount to transfer can't be negative"),
        HttpStatus.BAD_REQUEST);
       }
       /* obtengo dos accounts */
       origin_Account= restService.getAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",transaction.getOrigin_account_id()));
       destination_Account= restService.getAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",transaction.getDestination_account_id()));
       
       /**  una cuenta no pude transferir a la misma cuenta*/
       if(origin_Account.getId()== destination_Account.getId()){
        return new ResponseEntity(new ResponseError(400, "The origin account and destination account must be different accounts"),HttpStatus.BAD_REQUEST);
       }
       
       /* el tipo de moneda tiene que ser el mismo */
       if (!origin_Account.getCurrency().equals(destination_Account.getCurrency()) ) {
        return new ResponseEntity(new ResponseError(400, "The origin account and destination account must have the same currency"),HttpStatus.BAD_REQUEST);
       }
     
       /* debe tener suficiente dinero para transferir */
       if(transaction.getAmount().compareTo(origin_Account.getAccount_balance())>0) {
        return new ResponseEntity(new ResponseError(400, "Insufficient money to transfer"),
                HttpStatus.BAD_REQUEST);
       }

       /* trasnferencia descuenta de cuenta de origen*/ 
       
       amount_for_Origin.setAmount(origin_Account.getAccount_balance().subtract(transaction.getAmount()));  
       /**guarda en la base de datos la cuenta con la plata descontada */ 
       restService.putAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",origin_Account.getId()),amount_for_Origin);
      /** guarda la transaccion para despues saber cuanto le tiene que devolver en caso de cancelada o sumar en caso de procesada  */
       return transactionService.saveTransaction(transaction,Transaction.Status.PENDIENTE);
    
    }
     
    @PutMapping(value = "/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public Transaction updateStatus(@PathVariable("id") long id,@RequestBody Transaction transactionSent) throws Exception {
      
       Amount amount_for_Origin = new Amount();
       Amount amount_for_destination = new Amount(); 
      
       Account origin_Account;
       Account destination_Account;
       Transaction transaction_made= transactionService.getTransaction(id);
       
        if(transactionSent.getStatus()==Status.PROCESADA){

            // solo hace request de una cuenta porque tiene que sumarle a la cuenta destino 
            destination_Account= restService.getAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",transaction_made.getDestination_account_id()));
            amount_for_destination.setAmount(destination_Account.getAccount_balance().add(transaction_made.getAmount()));
            restService.putAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",destination_Account.getId()),amount_for_destination); 
            
            return transactionService.saveTransaction(transaction_made,transactionSent.getStatus());

         }else{
           /** le devuelve el dinero */
           if(transactionSent.getStatus()==Status.CANCELADA){

            origin_Account= restService.getAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",transaction_made.getOrigin_account_id()));
            amount_for_Origin.setAmount(origin_Account.getAccount_balance().add(transaction_made.getAmount())); 
            restService.putAccount(String.format("http://"+ipCuentas+port+"/accounts/%d",origin_Account.getId()),amount_for_Origin);
           
            return transactionService.saveTransaction(transaction_made,transactionSent.getStatus());
         
           }else{
            return transactionService.saveTransaction(transaction_made,transactionSent.getStatus());
           }
         }      
    }
}


