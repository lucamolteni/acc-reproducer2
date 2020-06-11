package com.healthpartners.api.gsusetup.func;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EffectiveDates implements Externalizable {

	private List<EffectiveDateRange> effectiveDateRanges = new ArrayList<>();

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(effectiveDateRanges);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		effectiveDateRanges = (List<EffectiveDateRange>)in.readObject();
	}

	public List<EffectiveDateRange> getEffectiveDateRanges() {
		return effectiveDateRanges;
	}

	public void setEffectiveDateRanges(List<EffectiveDateRange> effectiveDateRanges) {
		this.effectiveDateRanges = effectiveDateRanges;
	}
}
