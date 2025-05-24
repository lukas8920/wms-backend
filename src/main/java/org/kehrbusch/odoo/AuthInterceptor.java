package org.kehrbusch.odoo;

import jakarta.annotation.PostConstruct;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.kehrbusch.util.exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private final String url;
    private final String database;
    private final String user;
    private final String password;

    private int uid;
    private final XmlRpcClient authClient;
    private final XmlRpcClient objectClient;

    public AuthInterceptor(@Qualifier("odooApi") String odooApi, @Qualifier("odooDbName") String odooDbName,
                           @Qualifier("odooApiUser") String odooApiUser, @Qualifier("odooApiPw") String odooApiPw){
        this.authClient = new XmlRpcClient();
        this.objectClient = new XmlRpcClient();

        this.url = odooApi;
        this.database = odooDbName;
        this.user = odooApiUser;
        this.password = odooApiPw;
    }

    @PostConstruct
    public void init(){
        try {
            XmlRpcClientConfigImpl authConfig = new XmlRpcClientConfigImpl();
            authConfig.setServerURL(new URL(this.url + "common"));
            authClient.setConfig(authConfig);

            XmlRpcClientConfigImpl objectConfig = new XmlRpcClientConfigImpl();
            objectConfig.setServerURL(new URL(url + "object"));
            objectClient.setConfig(objectConfig);
        } catch (Exception e){
            logger.error("Exception during configuration initialization: {}", e.getMessage());
        }
    }

    private void authenticate() throws ConnectionException {
        try {
            uid = (int) authClient.execute("authenticate", Arrays.asList(
                    this.database, this.user, this.password, Collections.emptyMap()));
            if (uid == 0) {
                throw new ConnectionException("Authentication failed!");
            }
        } catch (XmlRpcException exception){
            throw new ConnectionException("Failure connecting to Odoo Api " + exception.getMessage());
        }
    }

    public Object call(String model, String method, List<Object> args, Map<String, Object> kwargs) throws ConnectionException {
        try {
            return objectClient.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password, model, method, args, kwargs));
        } catch (XmlRpcException e){
            try {
                logger.info("Not authenticated");
                this.authenticate();
                logger.info("Authenticated");
                return objectClient.execute("execute_kw", Arrays.asList(
                        this.database, this.uid, this.password, model, method, args, kwargs));
            } catch (XmlRpcException exception){
                throw new ConnectionException("Failure connecting to Odoo Api " + e.getMessage());
            }
        }
    }
}
