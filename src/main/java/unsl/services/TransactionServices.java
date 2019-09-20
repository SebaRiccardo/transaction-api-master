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

  public Transaction saveTransaction(Transaction transaction){
    
     transaction.setDate(TransactionServices.getCurrentTime()); 
    
    return transactionRepository.save(transaction);
  }
  
  public static String getCurrentTime() {
    LocalDate today = LocalDate.now();
        return today.toString();
  }

}