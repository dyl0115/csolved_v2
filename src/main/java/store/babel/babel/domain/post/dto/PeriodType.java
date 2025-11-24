package store.babel.babel.domain.post.dto;

import lombok.Getter;

@Getter
public enum PeriodType
{
    WEEK(7),
    MONTH(30),
    YEAR(365),
    ALL(0);

    private final int days;

    PeriodType(int days)
    {
        this.days = days;
    }
}
