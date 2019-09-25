package unsl.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import unsl.entities.*;

@Service
public class RestService {
    
    /*
     * @param url
     * @return
     * @throws Exception
     * 
    public User getUser(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        User user;  /* crear contrustor vacio en usuario para q no tire error 

        try {
            user = restTemplate.getForObject(url, User.class);
        }  catch (Exception e){
            throw new Exception( buildMessageError(e));
        }

        return user;
    }
    */
    

    /** 
     * @param url
     * @return
     * @throws Exception
     */
    public Account getAccount(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

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
        RestTemplate restTemplate = new RestTemplate();
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

