package com.healthpartners.api.gsusetup.func;

import org.drools.core.base.accumulators.AbstractAccumulateFunction;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectiveDateAccumulateFunction extends AbstractAccumulateFunction<EffectiveDates> {

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// nothing to write
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// nothing to read
	}

	@Override
	public EffectiveDates createContext() {
		return new EffectiveDates();
	}

	@Override
	public void init(EffectiveDates data) throws Exception {
	}

	@Override
	public void accumulate(EffectiveDates data, Object value) {
		IEffectiveDateRange effectiveDates = (IEffectiveDateRange) value;

		EffectiveDateRange dateRange =
				new EffectiveDateRange(effectiveDates.getEffectiveFromDate(), effectiveDates.getEffectiveToDate());
		data.getEffectiveDateRanges().add(dateRange);
	}

	@Override
	public void reverse(EffectiveDates context, Object value) throws Exception {
		// not supported
	}

	@Override
	public EffectiveDates getResult(EffectiveDates data) throws Exception {
		List<EffectiveDateRange> dateRanges = data.getEffectiveDateRanges();

		if (dateRanges.size() > 1) {
			List<EffectiveDateRange> finalRange = new ArrayList<>();
			// sorting on from date (ascending)....
			dateRanges.sort((dr1, dr2) -> dr1.getEffectiveFromDate().compareTo(dr2.getEffectiveFromDate()));

			Iterator<EffectiveDateRange> dateRangeIterator = dateRanges.iterator();
			EffectiveDateRange mergeRec = dateRangeIterator.next();
			finalRange.add(mergeRec);

			for (; dateRangeIterator.hasNext(); ) {
				EffectiveDateRange toBeMerged = dateRangeIterator.next();

				// check if merging is possible
				if (mergeRec.getEffectiveToDate() == null) {
					break;
				} else if (canMerge(mergeRec, toBeMerged)) {
						// merge possible, updating to date.
						mergeRec.setEffectiveToDate(toBeMerged.getEffectiveToDate());
				} else if (isNewRange(mergeRec, toBeMerged)) {
					// break in range, add new range to list
					mergeRec = toBeMerged;
					finalRange.add(mergeRec);
				}
			}
			data.setEffectiveDateRanges(finalRange);
		}

		return data;
	}

	private boolean canMerge(EffectiveDateRange mergeRec, EffectiveDateRange toBeMerged) {
		return mergeRec.getEffectiveToDate() != null &&
			(toBeMerged.getEffectiveToDate() == null ||
					toBeMerged.getEffectiveToDate().isAfter(mergeRec.getEffectiveToDate())) &&
				toBeMerged.getEffectiveFromDate().compareTo(mergeRec.getEffectiveToDate().plusDays(1)) <= 0;
	}

	private boolean isNewRange(EffectiveDateRange mergeRec, EffectiveDateRange toBeMerged) {
		return mergeRec.getEffectiveToDate() != null &&
				(toBeMerged.getEffectiveToDate() == null ||
						toBeMerged.getEffectiveToDate().isAfter(mergeRec.getEffectiveToDate())) &&
				toBeMerged.getEffectiveFromDate().isAfter(mergeRec.getEffectiveToDate());
	}

	@Override
	public boolean supportsReverse() {
		return false;
	}

	@Override
	public Class<?> getResultType() {
		return EffectiveDates.class;
	}
}
