package com.delivery.mydelivery.recruit;

public class ParticipantVO {

    private int participantId;
    private int recruitId;
    private int userId;
    private String participantType;
    private int paymentMoney;
    private int paymentStatus;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(int recruitId) {
        this.recruitId = recruitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getParticipantType() {
        return participantType;
    }

    public void setParticipantType(String participantType) {
        this.participantType = participantType;
    }

    public int getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(int paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
