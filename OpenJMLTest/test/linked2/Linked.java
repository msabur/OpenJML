class W {}

// A singly-linked list with the first element being a 'head' and not part of the list
// Specified with model methods
public class Linked {
    //@ public normal_behavior
    //@   ensures \result == (next == null ? seq.<W>empty() : next.values().prepend(next.value));
    //@ pure helper
    //@ model public seq<W> values(); // sequence of values after the current Link, not including the current Link
    
    //@ public normal_behavior
    //@   ensures \result == ((next == null) ? (\bigint)0 : 1 + next.size());
    //@   ensures \result >= 0;
    //@ pure helper
    //@ model public \bigint size();

    // @ public invariant values().size == size();
    
    // @ public model \locset nextFields;
    // @ public model \locset valueFields;
    
    //@ nullable
    public Linked next;// @ in nextFields; maps next.nextFields \into nextFields;
    //@ nullable
    public W value; // @ in valueFields; maps next.valueFields \into valueFields
    
    //@ public normal_behavior
    //@ ensures \result.value == null;
    //@ ensures \result.next == null;
    //@ ensures \result.values() == seq.<W>empty();
    //@ ensures \result.size() == 0;
    //@ pure
    public static Linked empty() {
        return new Linked(null, null);
    }
    
    //@ private normal_behavior
    //@ ensures this.value == t;
    //@ ensures this.next == n;
    //@ pure helper
    private Linked(/*@ nullable */ W t, /*@ nullable */ Linked n) {
        this.next = n;
        this.value = t;
    }
    
    //@ public normal_behavior
    //@   old \bigint oldsize = (\lbl C this.size());
    //@   old seq<W> oldvalues = this.values();
    //@ assignable next;
    //@ ensures \fresh(this.next);
    //@ ensures this.next.value == t;
    //@ ensures this.next.next == \old(this.next);
    //@ ensures (\lbl A this.size()) == (\lbl B oldsize) + 1;
    // @ ensures this.values().equals(oldvalues.prepend(t));
    public void push(W t) {
        //@ assume next != null;
        //@ ghost \bigint n = this.size();
        //@ ghost \bigint nn = this.next.size();
        //@ show this.size(), n;
        // @ assert n == nn + 1;
        Linked v = new Linked(t, next);
        //@ assert v.next == this.next;
        // @ assert nn == this.next.size();
        // @ assert nn == v.next.size();
        //@ assert v.size() == n;
        // @ assert n == this.size();
        // @ assert this != v;
        // @ assert this.next != v;
        this.next = v;
        //@ assume v.size() == n;
        //@ show this.size();
        //@ assert this.size() == 1 + n;
        // @ assert this.size() == 1 + v.size();
    }
    
    //@ public normal_behavior
    //@   requires this != this.next;
    //@   requires next != null;
    //@   old \bigint oldsize = this.size();
    //@   old seq<W> oldvalues = this.values();
    //@   assignable next; // size, values;
    //@   ensures next == \old(next.next);
    //@   ensures this.size() == oldsize - 1;
    // @   ensures this.values().equals(oldvalues.tail(1));
    public void pop() {
        //@ ghost var osize = this.size();
        //@ ghost var nsize = this.next.size();
        //@ ghost var nnsize = this.next.next == null ? (\bigint)-1 : this.next.next.size();
        //@ assert osize > 0;
        //@ assert osize == nsize + 1;
        if (this.next.next == null) {
            //@ assert osize == 1;
        } else {
            //@ assert nsize == this.next.next.size() + 1;
            //@ assert nsize == nnsize + 1;
        }
        this.next = this.next.next;
        if (this.next == null) {
            //@ assert osize == 1;
            //@ assert this.next == null;
            //@ assert this.size() == 0;
        } else {
            //@ assert nnsize == this.next.size();
            //@ assert nsize == this.next.size() + 1;
            //@ assert this.size() == this.next.size() + 1;
        }
        //@ assert this.size() == osize - 1;
    }
    
    //@ public normal_behavior
    //@   requires 0 <= n < this.size();
    //@   old \bigint oldsize = this.size();
    //@   old seq<W> oldvalues = this.values();
    // @   assignable size, values;
    // @   ensures next == \old(next.next);
    //@   ensures this.size() == oldsize - 1;
    // @   ensures this.values().equals(oldvalues.tail(1));
    public void remove(int n) {
        //@ assert this.next != null;
        if (n == 0) {
            this.next = this.next.next;
        } else {
            this.next.remove(n-1);
        }
    }
}