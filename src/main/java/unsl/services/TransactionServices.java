package unsl.services;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unsl.entities.Transaction;
import unsl.repository.TransactionRepository;

@Service
public class TransactionServices{
 
  @Autowired
  TransactionRepository transationRepository;

  public Transaction getTransaction(long id){
      return transationRepository.findById(id).orElse(null);
  }

  public Transaction saveTransaction(Transaction transaction){
      return transationRepository.save(transaction);
  }

}