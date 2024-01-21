package org.bigqueryCommonUtils;
import com.google.api.services.bigquery.model.TableRow;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;

import java.io.IOException;

public class BigQueryUtilities {
    public static Schema getTableSchema(String projId , String tableName){
        try {
            BigQuery bigQuery = BigQueryOptions.newBuilder()
                    .setProjectId(projId)
                    .setCredentials(GoogleCredentials.getApplicationDefault()).build().getService();
            String[] tableNameTokens = tableName.split("\\.");
            System.out.println(tableNameTokens[0]+" , "+tableNameTokens[1]);
            TableId tableid  = TableId.of(tableNameTokens[0],tableNameTokens[1]);
            Table table = bigQuery.getTable(tableid);
            return table.getDefinition().getSchema();
        } catch (IOException e) {
            throw new RuntimeException("failed to create bigquery connection...",e);
        }
    }

    public static BigQueryIO.Write<TableRow> BigQueryWriter(String tableName){
        return BigQueryIO.writeTableRows()
                .to(tableName)
                .withoutValidation()
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE);
    }

}
