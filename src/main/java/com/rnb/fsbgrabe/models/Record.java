package com.rnb.fsbgrabe.models;

/**
 * Работка с записями таблицы результатов
 */

public class Record {
    private String debtor;
    private String enforcementProceedings;
    private String detailsOfTheExecutiveDocument;
    private String dateReasonForTermination;
    private String subjectOfExecutionAmount;
    private String bailiffsDepartment;
    private String bailiffPhone;

    public Record(String debtor,
                  String enforcementProceedings,
                  String detailsOfTheExecutiveDocument,
                  String dateReasonForTermination,
                  String subjectOfExecutionAmount,
                  String bailiffsDepartment,
                  String bailiffPhone) {
        this.debtor = debtor;
        this.enforcementProceedings = enforcementProceedings;
        this.detailsOfTheExecutiveDocument = detailsOfTheExecutiveDocument;
        this.dateReasonForTermination = dateReasonForTermination;
        this.subjectOfExecutionAmount = subjectOfExecutionAmount;
        this.bailiffsDepartment = bailiffsDepartment;
        this.bailiffPhone = bailiffPhone;
    }

    public Record getRecord() {
        return this;
    }

    public String getJson() {
        return "{" +
                "\"debtor\": \"" + debtor.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"enforcementProceedings\": \"" + enforcementProceedings.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"detailsOfTheExecutiveDocumentv\": \"" + detailsOfTheExecutiveDocument.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"dateReasonForTermination\": \"" + dateReasonForTermination.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"subjectOfExecutionAmount\": \"" + subjectOfExecutionAmount.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"bailiffsDepartment\": \"" + bailiffsDepartment.replaceAll("\n", "").replaceAll("\r", "") + "\"," +
                "\"bailiffPhone\": \"" + bailiffPhone.replaceAll("\n", "").replaceAll("\r", "") + "\"" +
                "}";
    }
}