package za.ac.cput.dao.product;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.models.entity.product.Supplier;

import java.util.HashSet;
import java.util.Set;

public class SupplierDAO {

    private RestTemplate restTemplate = new RestTemplate();
    private String baseURL = "http://localhost:8080/supplier";
    private String securityUsername = "user";
    private String securityPassword = "password";

    public String addSupplierToDB(Supplier supplier) {
        String url = baseURL + "/create";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(securityUsername, securityPassword);
        ResponseEntity<Supplier> response;
        try {
            HttpEntity<Supplier> request = new HttpEntity<>(supplier, header);
            response = restTemplate.exchange(url, HttpMethod.POST, request, Supplier.class);

            if (response.getStatusCode() == HttpStatus.valueOf(200)) {
                return "Supplier Added!";
            } else {
                return "System Error";
            }
        } catch(Error error) {
            throw error;
        }
    }

    public Set<Supplier> getAllSuppliers() {
        Set<Supplier> resultSet = new HashSet<>();
        String url = baseURL + "/getall";
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(securityUsername, securityPassword);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, header);

        ResponseEntity<Supplier[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Supplier[].class);
        Supplier[] suppliers = response.getBody();
        for (Supplier s : suppliers) {
            resultSet.add(s);
        }
        return resultSet;
    }

    public Supplier getSupplierByID(String id) {
        String url = baseURL + "/read/" + id;

        ResponseEntity<Supplier> response = restTemplate.getForEntity(url, Supplier.class);

        return response.getBody();
    }

    public Supplier updateSupplier(Supplier supplier) {
        String url = baseURL + "/update";
        ResponseEntity<Supplier> response = restTemplate.postForEntity(url, supplier, Supplier.class);

        return response.getBody();
    }

    public String deleteSupplier(Supplier s) {
        String id = s.getSupplierID();
        String url = baseURL + "/delete/" + id;
        System.out.println(url);
        restTemplate.delete(url);
        System.out.println("Supplier deleted");

        return "Success";
    }
}
