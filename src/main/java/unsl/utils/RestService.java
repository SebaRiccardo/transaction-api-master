package unsl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.annotation.Retryable;
import unsl.entities.Account;
import unsl.entities.Amount;
import org.springframework.retry.annotation.Backoff;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RestService {
    private static Logger LOGGER = LoggerFactory.getLogger(RestService.class);
    @Autowired
    private RestTemplate restTemplate;
    /** 
     * @param url
     * @return
     * @throws Exception
     */
    @Retryable( maxAttempts = 4, backoff = @Backoff(1000))
    public Account getAccount(String url) throws Exception {
        LOGGER.info(String.format("GET ACCOUNT :"+ url+"%d", LocalDateTime.now().getSecond()));
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
    @Retryable( maxAttempts = 4, backoff = @Backoff(1000))
    public void putAccount(String url, Amount amount) throws Exception {
        LOGGER.info("PUT ACCOUNT :"+ url);
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

