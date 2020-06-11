package com.healthpartners.api.gsusetup.func;

import java.time.LocalDate;

public interface IEffectiveDateRange {

    LocalDate getEffectiveFromDate();

    LocalDate getEffectiveToDate();
}
