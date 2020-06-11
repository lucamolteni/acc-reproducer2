package com.example.api.setup.func;

import java.time.LocalDate;

public interface IEffectiveDateRange {

    LocalDate getEffectiveFromDate();

    LocalDate getEffectiveToDate();
}
