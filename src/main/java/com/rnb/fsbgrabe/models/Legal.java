package com.rnb.fsbgrabe.models;

public class Legal {
    private String debtor;
    private String enforcementProceedings;
    private String executiveDocRequisites;
    private String dateAndReasonOfTermination;
    private String serviceInfo;
    private String subjectAndSum;
    private String bailiffDepartmentNameAndAddress;
    private String bailiffNameAndPhone;

    public Legal(String debtor, String enforcementProceedings, String executiveDocRequisites, String dateAndReasonOfTermination, String serviceInfo, String subjectAndSum, String bailiffDepartmentNameAndAddress, String bailiffNameAndPhone) {
        this.debtor = debtor;
        this.enforcementProceedings = enforcementProceedings;
        this.executiveDocRequisites = executiveDocRequisites;
        this.dateAndReasonOfTermination = dateAndReasonOfTermination;
        this.serviceInfo = serviceInfo;
        this.subjectAndSum = subjectAndSum;
        this.bailiffDepartmentNameAndAddress = bailiffDepartmentNameAndAddress;
        this.bailiffNameAndPhone = bailiffNameAndPhone;
    }

    public String getDebtor() {
        return debtor;
    }

    public String getEnforcementProceedings() {
        return enforcementProceedings;
    }

    public String getExecutiveDocRequisites() {
        return executiveDocRequisites;
    }

    public String getDateAndReasonOfTermination() {
        return dateAndReasonOfTermination;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public String getSubjectAndSum() {
        return subjectAndSum;
    }

    public String getBailiffDepartmentNameAndAddress() {
        return bailiffDepartmentNameAndAddress;
    }

    public String getBailiffNameAndPhone() {
        return bailiffNameAndPhone;
    }
}
