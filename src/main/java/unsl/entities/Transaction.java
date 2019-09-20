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
    @JsonProperty("")
    private long origin_account_id;

    @JsonProperty("")
    private long destination_account_id;

    @JsonProperty("")
    private float amount;
    
    private String date;

    @Enumerated(EnumType.STRING)
    private Status status;

}