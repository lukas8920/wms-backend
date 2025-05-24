package org.kehrbusch.odoo;

import org.kehrbusch.util.exceptions.ConnectionException;
import org.kehrbusch.util.exceptions.InvalidResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PartnerApi {
    private final AuthInterceptor authInterceptor;

    @Autowired
    public PartnerApi(AuthInterceptor authInterceptor){
        this.authInterceptor = authInterceptor;
    }

    public Object readPartnerId(String name) throws InvalidResultException, ConnectionException {
        Map<String, Object> searchReadParams = new HashMap<>();
        searchReadParams.put("fields", List.of("id", "name"));
        searchReadParams.put("limit", 1);

        List<Object> domain = List.of(
                List.of("name", "=", name)
        );

        Object result = this.authInterceptor.call("res.partner", "search_read", List.of(domain), searchReadParams);

        Object[] partners = (Object[]) result;
        for (Object partnerObj : partners) {
            Map<?, ?> partner = (Map<?, ?>) partnerObj;
            return partner.get("id");
        }
        throw new InvalidResultException("No partner Id found");
    }
}
