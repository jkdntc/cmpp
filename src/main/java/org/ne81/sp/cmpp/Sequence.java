package org.ne81.sp.cmpp;

public class Sequence {
	private static Sequence sequence;
	private int seq = 0;

	private Sequence() {

	}

	public static Sequence getInstance() {
		if (sequence == null)
			sequence = new Sequence();
		return sequence;
	}

	synchronized public int getSequence() {
		seq++;
		if (seq >= Integer.MAX_VALUE)
			seq = 1;
		return seq;
	}
}
