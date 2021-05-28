import java.util.ArrayList;

public class ListSeq<E extends Object> implements Seq<E> {

    // Private Mutable State
	//@ spec_public
    private final /*@ non_null @*/ ArrayList<E> list = new ArrayList<E>();

	//@ spec_public non_null
   private Integer pos = 1;
    //@ public invariant 1 <= pos <= length() + 1;
   //@ public invariant length() < 1000;
   //@ public invariant list.size() < 1000;

    // Constructor
    //@ requires elements.length < 1000;
    public ListSeq( E /*@ non_null @*/ [] elements) {
    	//@ loop_invariant list.size() == \count;
    	//@ loop_decreases elements.length - \count;
        for (E element : elements) {
            list.add(element);
        }
        //@ assume list.size() == elements.length;
    }

    // Interface Seq
    @Override
    public void forth() {
        if (pastEnd()) return;
        pos++;
    }

    //@ also public normal_behavior
    //@   requires !pastEnd();
    //@   ensures \result == pos;
    // @ also public exceptional_behavior
    // @   requires pastEnd();
    @Override //@ pure
    public /*@ non_null */ Integer pos() {
        if (pastEnd()) throw new IndexOutOfBoundsException("There is no position past the end of the sequence.");
        return pos;
    }

    @Override //@ pure
    public E current() {
        return list.get(pos-1);
    }

    //@ also ensures \result != null && \result == list.size(); pure helper
    @Override 
    public /* non_null */ Integer length() {
        return list.size();
    }

    //@ also ensures \result != null && \result == (pos == 1+length()); //@ pure
    @Override 
    public /* non_null */ Boolean pastEnd() {
        return pos.equals(length()+1);
    }
}