package unsl.services;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unsl.entities.Transaction;
import unsl.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionServices{
 
  @Autowired
  TransactionRepository transactionRepository;
  
  public List<Transaction> getAll() {
    return transactionRepository.findAll();
  }

  public Transaction getTransaction(long id){
      return transactionRepository.findById(id).orElse(null);
  }

  public Transaction saveTransaction(Transaction transaction,Transaction.Status status){
    
    transaction.setDate(TransactionServices.getCurrentTime()); 
    
    transaction.setStatus(status);
   
    return transactionRepository.save(transaction);
  }
  
  public static String getCurrentTime() {
    // ajustar el time zone dependiendo de donde se realizo la transaccion
   
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY"); 
     return formatter.format(today).toString(); 
  }

}