package unsl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import unsl.entities.Account;
import unsl.entities.Amount;

@Service
public class RestService {
    
    @Autowired
    private RestTemplate restTemplate;
    /** 
     * @param url
     * @return
     * @throws Exception
     */
    public Account getAccount(String url) throws Exception {
        
        Account account;

        try {
            account = restTemplate.getForObject(url, Account.class);
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

        return account;
    }
   

    /**
     * @param url
     * @throws Exception
     */
    public void putAccount(String url, Amount amount) throws Exception {
        try {
            restTemplate.put(url,amount);
            
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

    }

    private String buildMessageError(Exception e) {
        String msg = e.getMessage();
        if (e instanceof HttpClientErrorException) {
            msg = ((HttpClientErrorException) e).getResponseBodyAsString();
        } else if (e instanceof HttpServerErrorException) {
            msg =  ((HttpServerErrorException) e).getResponseBodyAsString();
        }
        return msg;
    }

}

