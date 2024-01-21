package org.transformers;

import com.google.api.services.bigquery.model.TableRow;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.Schema;
import org.apache.beam.sdk.transforms.DoFn;
import java.util.Map;
import java.util.Objects;

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


public class ConvertToTableRowFn extends DoFn<Map<String,String>, TableRow> {
    private final Schema bigquerySchema;

    public ConvertToTableRowFn(Schema schema) {
        this.bigquerySchema = schema;
    }
    @ProcessElement
    public void processRecords(ProcessContext context) {
        TableRow tableRow = new TableRow();
        Map<String,String> rowMap = context.element();
        if(rowMap != null) {
            for (Field field : bigquerySchema.getFields()) {
                String fieldName = field.getName();
                if (Objects.isNull(rowMap.get(fieldName)) || rowMap.get(fieldName).equals("null"))
                    tableRow.put(fieldName, null);
                else
                    tableRow.put(fieldName, rowMap.get(fieldName));
            }
            context.output(tableRow);
        }

    }

}
