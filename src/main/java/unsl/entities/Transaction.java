package unsl.entities;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "transactions",uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Transaction {

    public static enum Status {
        PENDIENTE,
        PROCESADA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("origin_account_id")
    private long origin_account_id;

    @JsonProperty("destination_account_id")
    private long destination_account_id;

    @JsonProperty("amount")
    private float amount;

    private String date;

    @Enumerated(EnumType.STRING)
    private Status status;

    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getDestination_account_id() {
        return destination_account_id;
    }

    public void setDestination_account_id(long destination_account_id) {
        this.destination_account_id = destination_account_id;
    }

    public long getOrigin_account_id() {
        return origin_account_id;
    }

    public void setOrigin_account_id(long origin_account_id) {
        this.origin_account_id = origin_account_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
   
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}