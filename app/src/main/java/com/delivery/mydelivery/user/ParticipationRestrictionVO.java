package com.delivery.mydelivery.user;

import java.sql.Timestamp;

public class ParticipationRestrictionVO {

    private int participationRestrictionId;
    private int userId;
    private Timestamp restrictionPeriod;

    public int getParticipationRestrictionId() {
        return participationRestrictionId;
    }

    public void setParticipationRestrictionId(int participationRestrictionId) {
        this.participationRestrictionId = participationRestrictionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getRestrictionPeriod() {
        return restrictionPeriod;
    }

    public void setRestrictionPeriod(Timestamp restrictionPeriod) {
        this.restrictionPeriod = restrictionPeriod;
    }
}
