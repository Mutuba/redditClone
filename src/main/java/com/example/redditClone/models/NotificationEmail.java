package com.example.redditClone.models;

import lombok.*;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
    private String subject;
    private String recepient;
    private String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof NotificationEmail)) {
            return false;
        }

        NotificationEmail that = (NotificationEmail) o;
        return getRecepient().equals(that.getRecepient()) &&
                getBody().equals(that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecepient(), getBody());
    }
}
