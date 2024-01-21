package org.transformers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.*;
/*
ApiToBqStg , Employee
Schema{
    fields=[
            Field{name=Id, type=INTEGER, mode=REQUIRED, description=null, policyTags=null, maxLength=null, scale=null, precision=null},
            Field{name=FirstName, type=STRING, mode=NULLABLE, description=null, policyTags=null, maxLength=null, scale=null, precision=null},
            Field{name=LastName, type=STRING, mode=NULLABLE, description=null, policyTags=null, maxLength=null, scale=null, precision=null},
            Field{name=Salary, type=FLOAT, mode=NULLABLE, description=null, policyTags=null, maxLength=null, scale=null, precision=null},
            Field{name=Age, type=STRING, mode=NULLABLE, description=null, policyTags=null, maxLength=null, scale=null, precision=null},
            Field{name=Address, type=STRING, mode=NULLABLE, description=null, policyTags=null, maxLength=null, scale=null, precision=null}
            ]
}
 */
public class StringResponseToListOfMapDofn extends DoFn<String, List<Map<String,String>>> {
    @ProcessElement
    public void transformRecord(ProcessContext context){
        List<Map<String,String>> array = new ArrayList<Map<String,String>>();
        Optional<String> jsonResponse = Optional.ofNullable(context.element());
        if (jsonResponse.isPresent()) {
            JsonArray jsonArray = JsonParser.parseString(jsonResponse.get()).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                HashMap<String, String> row = getRow(element);
                array.add(row);
            }
            context.output(array);
        }
    }

    private static HashMap<String, String> getRow(JsonElement element) {
        HashMap<String, String> row = new HashMap<>();
        JsonObject jsonObject = element.getAsJsonObject();
        row.put("Id", jsonObject.get("id").getAsString());
        row.put("FirstName", jsonObject.get("firstName").getAsString());
        row.put("LastName", jsonObject.get("lastName").getAsString());
        row.put("Age", jsonObject.get("age").getAsString());
        row.put("Salary", jsonObject.get("salary").getAsString());
        row.put("Address", jsonObject.get("address").getAsString());
        return row;
    }
}
