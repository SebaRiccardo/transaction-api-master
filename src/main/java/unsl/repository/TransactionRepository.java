package unsl.repository;

import unsl.entities.Transaction;;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Account, Long> {
   
}