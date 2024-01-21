package org.pipeline;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.bigqueryCommonUtils.BigQueryUtilities;
import org.PipelineCustomOptions.ApiTobigQueryPipelineOptions;
import org.transformers.ConvertToTableRowFn;
import org.transformers.StringResponseToListOfMapDofn;
import org.transformers.readApiDataDoFn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class api_to_bigquery {
    private static final int BATCH_SIZE=5;

    static class LogOutput<T> extends DoFn<T,T> {

        @ProcessElement
        public void processElement(ProcessContext c) throws Exception {
            System.out.println(c.element());
        }
    }

    public static void main(String[] args) {
        ApiTobigQueryPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(ApiTobigQueryPipelineOptions.class);
        Pipeline pipeline = Pipeline.create(options);
        String sourceQuery = options.getUrl();
        int TOTAL_EMPLOYEES = options.getCount();

        try {
            PCollection<Map<String, String>> api_response_trans_row = pipeline
                    .apply("Total records to fetch: " + TOTAL_EMPLOYEES, Create.of(TOTAL_EMPLOYEES).withType(TypeDescriptors.integers()))
                    .apply("Batches Created", FlatMapElements.into(TypeDescriptors.integers()).via(createSequence()))
                    .apply("Creating Source query", MapElements.into(TypeDescriptors.kvs(TypeDescriptors.integers(), TypeDescriptors.strings()))
                            .via(id -> {
                                return KV.of(id, String.format(sourceQuery, BATCH_SIZE, id));
                            })
                    )
                    .apply("Redistribute the queries", GroupByKey.create())
                    .apply("Read data from API", ParDo.of(new readApiDataDoFn()))
                    .apply("Extract data", ParDo.of(new StringResponseToListOfMapDofn()))
                    .apply("flattening", Flatten.iterables());

            PCollection<TableRow> tableRows = api_response_trans_row.apply("Raw Row to BQ TableRow",
                    ParDo.of(new ConvertToTableRowFn(
                            BigQueryUtilities.getTableSchema(options.getProjectId(), options.getDataSetId() + "." + options.gettableName())
                    )));

            tableRows.apply("Write To BQ Table - " + options.gettableName(),
                    BigQueryUtilities.BigQueryWriter(options.getDataSetId() + "." + options.gettableName()));

//            tableRows.apply(ParDo.of(new LogOutput<>()));
            pipeline.run();
        } catch (Exception e){
            System.out.println("Getting exception at main class: "+e);
        }
    }


    public static SerializableFunction<Integer,Iterable<Integer>> createSequence(){
        return input -> {
            if (input != null) {
                List<Integer> arr = new ArrayList<>();
                for (int i = 1; i <= input; i++) {
                    if (i % BATCH_SIZE == 0) {
                        arr.add(i-BATCH_SIZE+1);
                    }
                }
                return arr;
            }
            else return new ArrayList<>();
        };

    }
}
