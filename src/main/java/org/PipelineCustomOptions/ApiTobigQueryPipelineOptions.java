package org.PipelineCustomOptions;

import org.apache.beam.sdk.options.*;

public interface ApiTobigQueryPipelineOptions extends PipelineOptions {
    @Description("Input URL")
    @Default.String("https://hub.dummyapis.com/employee?noofRecords=%d&idStarts=%d")
    @Validation.Required
    String getUrl();
    void setUrl(String url);

    @Description("Project")
    @Default.String("burner-sidsoni1")
    @Validation.Required
    String getProjectId();
    void setProjectId(String proj);

    @Description("Dataset")
    @Default.String("ApiToBqStg")
    @Validation.Required
    String getDataSetId();
    void setDataSetId(String value);

    @Description("Table")
    @Default.String("Employee")
    @Validation.Required
    String gettableName();
    void setTableName(String val);

    @Description("Total Records")
    @Default.Integer(10)
    int getCount();
    void setCount(int value);
}