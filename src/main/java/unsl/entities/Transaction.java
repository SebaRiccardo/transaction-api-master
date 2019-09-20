package unsl.entities;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity



public class Transaction {
    
    public static enum Status{
        PENDIENTE,
        PROCESADA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long origin_account_id;
    private long destination_account_id;
    private String date;
    private long amount;
    

    @Enumerated(EnumType.STRING)
    private Status status;
    
}