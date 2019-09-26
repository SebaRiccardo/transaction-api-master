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
    transaction.setStatus(Transaction.Status.PROCESADA);
   
    return transactionRepository.save(transaction);
  }
  /** invertir el orde de la fech porque da primero el año y despues el mes y el dia */
  public static String getCurrentTime() {
    LocalDate today = LocalDate.now();
        return today.toString();
  }

}