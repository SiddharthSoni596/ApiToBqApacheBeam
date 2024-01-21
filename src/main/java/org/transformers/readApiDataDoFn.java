package org.transformers;

import org.HttpCommonUtils.httpUtilities;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class readApiDataDoFn extends DoFn<KV<Integer,Iterable<String>>, String> {
    private transient HttpClient httpClient;
    @Setup
    public void initHttpClient(){
        httpClient = httpUtilities.buildClient();
    }
    @ProcessElement
    public void fetchRecords(ProcessContext context){
        Optional<Iterable<String>> optionalValue = Optional.ofNullable(context.element().getValue());
        optionalValue.ifPresent((value) -> value.forEach((requestBody) -> {
            try{
                HttpRequest httpRequest = httpUtilities.buildHttpGetRequest(requestBody);
                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                if(response.statusCode() == 200){
                    context.output(responseBody);
                } else {
                    System.out.println("Error occured: "+response.statusCode());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }));
    }
}
