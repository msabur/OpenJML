package org.jmlspecs.openjmltest.testcases;

import java.util.Collection;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openjml.runners.ParameterizedWithNames;

@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
@RunWith(ParameterizedWithNames.class)
public class esctypeannotations extends EscBase {

    public esctypeannotations(String options, String solver) {
        super(options,solver);
    }
    
    @Before @Override
    public void setUp() throws Exception {
    	//captureOutput = true;
    	super.setUp();
    }
 
    protected void helpTCX(String classname, String s, Object... expectedResults) {
    	super.helpTCX(classname,  s,  expectedResults);
    }

    @Test
    public void testField1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                /*@ nullable */ Object o1;
                @Nullable Object o2;
            }
            //@ nullable_by_default
            class TestJava1 {
                @NonNull Object o3; // ERROR
            }
            //@ nullable_by_default
            class TestJava2 {
                /*@ non_null */ Object o4; // ERROR
            }
            //@ non_null_by_default
            class TestJava3 {
                Object o5; // ERROR
            }
            //@ nullable_by_default
            class TestJava4 {
                /*@ nullable */ Object o1;
                @Nullable Object o2;
                Object o5; // OK
            }
            //@ nullable_by_default
            class TestJava5 {
                Object o4; // OK
            }
            //@ nullable_by_default
            class TestJava6 {
                @NonNull public Object o3; // ERROR
            }
            //@ nullable_by_default
            class TestJava7 {
                /*@ non_null */ public Object o4; // ERROR
            }
            """
            ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (NullField) in method TestJava1",21
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (NullField) in method TestJava2",28
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (NullField) in method TestJava3",12
            ,"/tt/TestJava.java:32: warning: The prover cannot establish an assertion (NullField) in method TestJava6",28
            ,"/tt/TestJava.java:36: warning: The prover cannot establish an assertion (NullField) in method TestJava7",35
            );
    }

    @Test
    public void testGhost1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                //@ ghost nullable  Object o1;
                //@ ghost @Nullable Object o2;
            }
            //@ nullable_by_default
            class TestJava1 {
                //@ ghost @NonNull Object o3; // ERROR
            }
            //@ nullable_by_default
            class TestJava2 {
                //@ ghost  non_null  Object o4; // ERROR
            }
            //@ non_null_by_default
            class TestJava3 {
                //@ ghost Object o5; // ERROR
            }
            //@ nullable_by_default
            class TestJava4 {
                //@ ghost  nullable Object o1;
                //@ ghost @Nullable Object o2;
                //@ ghost Object o5; // OK
            }
            //@ nullable_by_default
            class TestJava5 {
                //@ ghost Object o4; // OK
            }
            //@ nullable_by_default
            class TestJava6 {
                //@ ghost @NonNull public Object o3; // ERROR
            }
            //@ nullable_by_default
            class TestJava7 {
              //@ ghost non_null public Object o4; // ERROR
            }
            """
            ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (NullField) in method TestJava1",31
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (NullField) in method TestJava2",33
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (NullField) in method TestJava3",22
            ,"/tt/TestJava.java:32: warning: The prover cannot establish an assertion (NullField) in method TestJava6",38
            ,"/tt/TestJava.java:36: warning: The prover cannot establish an assertion (NullField) in method TestJava7",36
            );
    }

    @Test
    public void testField2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                java.lang.@Nullable Object o2;
            }
            //@ nullable_by_default
            class TestJava1 {
                java.lang.@NonNull Object o3; // ERROR
            }
            //@ non_null_by_default
            class TestJava3 {
                java.lang.Object o5; // ERROR
            }
            //@ nullable_by_default
            class TestJava4 {
                java.lang.@Nullable Object o2;
                java.lang.Object o5; // OK
            }
            """
            ,"/tt/TestJava.java:9: warning: The prover cannot establish an assertion (NullField) in method TestJava1",31
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (NullField) in method TestJava3",22
            );
    }

    @Test
    public void testField3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                java.lang. /*@ nullable */ Object o1;
            }
            //@ nullable_by_default
            class TestJava2 {
                java.lang./*@ non_null */ Object o4; // ERROR
            }
            //@ nullable_by_default
            class TestJava4 {
                java.lang./*@ nullable */ Object o1;
            }
            """
            ,"/tt/TestJava.java:9: warning: The prover cannot establish an assertion (NullField) in method TestJava2",38
            );
    }

    @Test
    public void testField4() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                /*@ nullable */ java.lang.Object o1;
                @Nullable java.lang.Object o2;
            }
            //@ nullable_by_default
            class TestJava1 {
                @NonNull java.lang.Object o3; // ERROR
            }
            """
            ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (NullField) in method TestJava1",31
            );
    }

    @Test
    public void testGhostLocal1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    //@ ghost  nullable Object o1 = null;
                    //@ ghost @Nullable Object o2 = null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m() {
                  //@ ghost @NonNull Object o3 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m() {
                  //@ ghost  non_null Object o4 = null; // ERROR
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                public void m() {
                  //@ ghost Object o5 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m() {
                  //@ ghost nullable Object o1;
                  //@ ghost @Nullable Object o2 = null;
                  //@ ghost Object o5 = null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava5 {
                public void m() {
                  //@ ghost Object o4 = null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava6 {
                public void m() {
                  //@ ghost @NonNull final Object o3 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava7 {
                public void m() {
                  //@ ghost non_null final Object o4 = null; // ERROR
                }
            }
            """
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",33
            ,"/tt/TestJava.java:19: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o4",34
            ,"/tt/TestJava.java:25: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o5",24
            ,"/tt/TestJava.java:45: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",39
            ,"/tt/TestJava.java:51: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o4",39
            );
    }

    @Test
    public void testLocal1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    /*@ nullable */ Object o1 = null;
                    @Nullable Object o2 = null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m() {
                    @NonNull Object o3 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m() {
                    /*@ non_null */ Object o4 = null; // ERROR
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                public void m() {
                    Object o5 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m() {
                    /*@ nullable */ Object o1;
                    @Nullable Object o2 = null;
                    Object o5 = null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava5 {
                public void m() {
                    Object o4 = null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava6 {
                public void m() {
                    @NonNull final Object o3 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava7 {
                public void m() {
                    /*@ non_null */ final Object o4 = null; // ERROR
                }
            }
            """
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",25
            ,"/tt/TestJava.java:19: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o4",32
            ,"/tt/TestJava.java:25: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o5",16
            ,"/tt/TestJava.java:45: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",31
            ,"/tt/TestJava.java:51: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o4",38
            );
    }

    @Test
    public void testGhostLocal2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    //@ ghost java.lang.@Nullable Object o2 = null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m() {
                  //@ ghost java.lang.@NonNull Object o3 = null; // ERROR
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                public void m() {
                  //@ ghost java.lang.Object o5 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m() {
                  //@ ghost java.lang.@Nullable Object o2 = null;
                  //@ ghost java.lang.Object o5 = null; // OK
                }
            }
            """
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",43
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o5",34
            );
    }

    @Test
    public void testLocal2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    java.lang.@Nullable Object o2 = null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m() {
                    java.lang.@NonNull Object o3 = null; // ERROR
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                public void m() {
                    java.lang.Object o5 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m() {
                    java.lang.@Nullable Object o2 = null;
                    java.lang.Object o5 = null; // OK
                }
            }
            """
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",35
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o5",26
            );
    }

    @Test
    public void testLocal3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    java.lang. /*@ nullable */ Object o1 = null;
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m() {
                    java.lang./*@ non_null */ Object o4 = null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m() {
                    java.lang./*@ nullable */ Object o1 = null;
                }
            }
            """
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o4",42
            );
    }

    @Test
    public void testLocal4() {
        expectedExit = 1;
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m() {
                    /*@ nullable */ java.lang.Object o1 = null;
                    @Nullable java.lang.Object o2 = null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m() {
                    @NonNull java.lang.Object o3 = null; // ERROR
                }
            }
            """
            ,"/tt/TestJava.java:7: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava",19
            ,"/tt/TestJava.java:13: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava1",18
            //,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m: o3",31
            );
    }

    @Test
    public void testFormal1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m(/*@ nullable */ Object o1, Object o3) {
                    //@ check o1 != null; // ERROR
                    //@ check o3 != null; // OK
                }
            }
            //@ non_null_by_default
            class TestJava0 {
                public void m(@Nullable Object o2) {
                    //@ check o2 != null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m(@NonNull Object o3, /*@ non_null */ Object o4) {
                    //@ check o3 != null; // OK
                    //@ check o4 != null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m(/*@ nullable */ Object o1, @Nullable Object o2, Object o5) {
                    //@ check o1 != null; // ERROR
                    //@ check o2 != null; // ERROR
                    //@ check o5 != null; // ERROR
                }
            }
            class TestJava6 {
                public void m(final /*@ nullable */ Object o1, final @Nullable Object o2, final Object o5) {
                    //@ check o1 != null; // ERROR
                    //@ check o2 != null; // ERROR
                    //@ check o5 != null; // OK
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m",13
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Assert) in method m",13
            ,anyorder(seq("/tt/TestJava.java:26: warning: The prover cannot establish an assertion (Assert) in method m",13)
            ,seq("/tt/TestJava.java:27: warning: The prover cannot establish an assertion (Assert) in method m",13)
            ,seq("/tt/TestJava.java:28: warning: The prover cannot establish an assertion (Assert) in method m",13))
//            ,anyorder(seq("/tt/TestJava.java:34: warning: The prover cannot establish an assertion (Assert) in method m",13)
//            ,seq("/tt/TestJava.java:35: warning: The prover cannot establish an assertion (Assert) in method m",13)
//            ,seq("/tt/TestJava.java:36: warning: The prover cannot establish an assertion (Assert) in method m",13))
            ,anyorder(seq("/tt/TestJava.java:33: warning: The prover cannot establish an assertion (Assert) in method m",13)
            ,seq("/tt/TestJava.java:34: warning: The prover cannot establish an assertion (Assert) in method m",13))
            );
    }

    @Test
    public void testFormal2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m(java.lang.@Nullable Object o2) {
                    //@ check o2 != null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m(java.lang.@NonNull Object o3) {
                    //@ check o3 != null; // OK
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                public void m(java.lang.Object o5) {
                    //@ check o5 != null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m(java.lang.@Nullable Object o2, java.lang.Object o5) {
                    //@ check o2 != null; // ERROR
                    //@ check o5 != null; // ERROR
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m",13
            ,anyorder(seq("/tt/TestJava.java:24: warning: The prover cannot establish an assertion (Assert) in method m",13)
            ,seq("/tt/TestJava.java:25: warning: The prover cannot establish an assertion (Assert) in method m",13))
            );
    }

    @Test
    public void testFormal3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m(java.lang. /*@ nullable */ Object) {
                    //@ check o1 != null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m(java.lang./*@ non_null */ Object o4) {
                    //@ check o4 != null; // OK
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                public void m(java.lang./*@ nullable */ Object o1) {
                    //@ check o1 != null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava5 {
                public void m(/*@ nullable */ final Object o1, @Nullable final Object o2, Object o5) {
                    //@ check o1 != null; // ERROR
                    //@ check o2 != null; // ERROR
                    //@ check o5 != null; // ERROR
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m",13
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (Assert) in method m",13
            );
    }

    @Test
    public void testFormal4() {
        expectedExit = 1;
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m(/*@ nullable */ java.lang.Object o1, @Nullable java.lang.Object o2) {
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m(@NonNull java.lang.Object o3) {
                }
            }
            """
            ,"/tt/TestJava.java:5: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava",35
            ,"/tt/TestJava.java:5: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava",66
            ,"/tt/TestJava.java:10: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava1",28
            );
    }

    @Test
    public void testMethod1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                //@ ensures \\result != null; // ERROR
                public /*@ nullable */ Object m() {
                    return null;
                }
            }
            //@ non_null_by_default
            class TestJava0 {
                //@ ensures \\result != null; // ERROR
                public @Nullable Object m() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                //@ ensures \\result != null; // OK
                public @NonNull Object m1() {
                    return null; // NULL RETURN
                }
                //@ ensures \\result != null; // OK
                public /*@ non_null */ Object m2() {
                    return null; // NULL RETURN
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                //@ ensures \\result != null; // ERROR // Line 30
                public @Nullable Object m1() {
                    return null;
                }
                //@ ensures \\result != null; // ERROR
                public /*@ nullable */ Object m2() {
                    return null;
                }
                //@ ensures \\result != null; // ERROR
                public Object m3() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava3 {
                //@ ensures \\result != null; // ERROR
                @Nullable public Object m1() {
                    return null;
                }
                //@ ensures \\result != null; // ERROR
                /*@ nullable */ public Object m2() {  // Line 50
                    return null;
                }
                //@ ensures \\result != null; // ERROR
                 Object m3() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                @NonNull public Object m1() { // Line 60
                    return null; // ERROR
                }
                /*@ non_null */ public Object m2() {
                    return null; // ERROR
                }
                Object m3() {
                    return null;
                }
            }
            //@ non_null_by_default
            class TestJava5 {
                Object m3() { // ERROR
                    return null;
                }
            }
            """
            ,"/tt/TestJava.java:7: warning: The prover cannot establish an assertion (Postcondition) in method m",9
            ,"/tt/TestJava.java:5: warning: Associated declaration",9
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (Postcondition) in method m",9
            ,"/tt/TestJava.java:12: warning: Associated declaration",9
            ,"/tt/TestJava.java:20: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m1: m1",21
            ,"/tt/TestJava.java:20: warning: Associated declaration",28
            ,"/tt/TestJava.java:21: warning: Associated method exit",9
            ,"/tt/TestJava.java:24: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m2: m2",28
            ,"/tt/TestJava.java:24: warning: Associated declaration",35
            ,"/tt/TestJava.java:25: warning: Associated method exit",9
            ,"/tt/TestJava.java:32: warning: The prover cannot establish an assertion (Postcondition) in method m1",9
            ,"/tt/TestJava.java:30: warning: Associated declaration",9
            ,"/tt/TestJava.java:36: warning: The prover cannot establish an assertion (Postcondition) in method m2",9
            ,"/tt/TestJava.java:34: warning: Associated declaration",9
            ,"/tt/TestJava.java:40: warning: The prover cannot establish an assertion (Postcondition) in method m3",9
            ,"/tt/TestJava.java:38: warning: Associated declaration",9
            ,"/tt/TestJava.java:47: warning: The prover cannot establish an assertion (Postcondition) in method m1",9
            ,"/tt/TestJava.java:45: warning: Associated declaration",9
            ,"/tt/TestJava.java:51: warning: The prover cannot establish an assertion (Postcondition) in method m2",9
            ,"/tt/TestJava.java:49: warning: Associated declaration",9
            ,"/tt/TestJava.java:55: warning: The prover cannot establish an assertion (Postcondition) in method m3",9
            ,"/tt/TestJava.java:53: warning: Associated declaration",9
            ,"/tt/TestJava.java:60: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m1: m1",21
            ,"/tt/TestJava.java:60: warning: Associated declaration",28
            ,"/tt/TestJava.java:61: warning: Associated method exit",9
            ,"/tt/TestJava.java:63: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m2: m2",28
            ,"/tt/TestJava.java:63: warning: Associated declaration",35
            ,"/tt/TestJava.java:64: warning: Associated method exit",9
            ,"/tt/TestJava.java:72: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m3: m3",5
            ,"/tt/TestJava.java:72: warning: Associated declaration",12
            ,"/tt/TestJava.java:73: warning: Associated method exit",9
            );
    }

    @Test
    public void testMethod2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                //@ ensures \\result != null; // ERROR
                public java.lang.@Nullable Object m() { // Line 6
                    return null;
                }
            }
            //@ non_null_by_default
            class TestJava1 {
                //@ ensures \\result != null; // OK
                public java.lang.@NonNull Object m() { // Line 13
                    return null; // ERROR
                }
            }
            //@ non_null_by_default
            class TestJava3 {
                //@ ensures \\result != null; // OK
                public java.lang.Object m() { // Line 20
                    return null; // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                //@ ensures \\result != null; // ERROR
                public java.lang.@Nullable Object m() { // Line 27
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava5 {
                public java.lang.@NonNull Object m() { // Line 33 // ERROR
                    return null;
                }
            }
            """
            ,"/tt/TestJava.java:7: warning: The prover cannot establish an assertion (Postcondition) in method m",9
            ,"/tt/TestJava.java:5: warning: Associated declaration",9
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m: m",22
            ,"/tt/TestJava.java:13: warning: Associated declaration",38
            ,"/tt/TestJava.java:14: warning: Associated method exit",9
            ,"/tt/TestJava.java:20: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m: m",21
            ,"/tt/TestJava.java:20: warning: Associated declaration",29
            ,"/tt/TestJava.java:21: warning: Associated method exit",9
            ,"/tt/TestJava.java:28: warning: The prover cannot establish an assertion (Postcondition) in method m",9
            ,"/tt/TestJava.java:26: warning: Associated declaration",9
            ,"/tt/TestJava.java:33: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m: m",22
            ,"/tt/TestJava.java:33: warning: Associated declaration",38
            ,"/tt/TestJava.java:34: warning: Associated method exit",9
           );
    }

    @Test
    public void testMethod3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                //@ ensures \\result != null; // ERROR
                public java.lang./*@ nullable */ Object m() {
                    return null;
                }
            }
            //@ non_null_by_default
            public class TestJava1 {
                //@ ensures \\result != null; // OK
                public java.lang./*@ non_null */ Object m() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava3 {
                //@ ensures \\result != null; // OK
                public java.lang./*@ non_null*/ Object m() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava4 {
                //@ ensures \\result != null; // ERROR
                public java.lang./*@ nullable*/ Object m() {
                    return null;
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m",13
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (Assert) in method m",13
            );
    }

    @Test
    public void testMethod4() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                //@ ensures \\result != null; // ERROR
                public /*@ nullable */ java.lang.Object m1() {
                    return null;
                }
                //@ ensures \\result != null; // OK
                public @Nullable java.lang.Object m2() {
                    return null;
                }
                //@ ensures \\result != null; // OK
                public java.lang.Object m3() {
                    return null;
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                //@ ensures \\result != null; // ERROR
                public /*@ nullable */ java.lang.Object m1() {
                    return null;
                }
                //@ ensures \\result != null; // OK
                public @Nullable java.lang.Object m2() {
                    return null;
                }
                //@ ensures \\result != null; // ERROR
                public java.lang.Object m3() {
                    return null;
                }
            }
            """
            ,"/tt/TestJava.java:7: warning: The prover cannot establish an assertion (Postcondition) in method m1",9
            ,"/tt/TestJava.java:5: warning: Associated declaration",9
            ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Postcondition) in method m2",9
            ,"/tt/TestJava.java:9: warning: Associated declaration",9
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (PossiblyNullReturn) in method m3: m3",21
            ,"/tt/TestJava.java:14: warning: Associated declaration",29
            ,"/tt/TestJava.java:15: warning: Associated method exit",9
            ,"/tt/TestJava.java:22: warning: The prover cannot establish an assertion (Postcondition) in method m1",9
            ,"/tt/TestJava.java:20: warning: Associated declaration",9
            ,"/tt/TestJava.java:26: warning: The prover cannot establish an assertion (Postcondition) in method m2",9
            ,"/tt/TestJava.java:24: warning: Associated declaration",9
            ,"/tt/TestJava.java:30: warning: The prover cannot establish an assertion (Postcondition) in method m3",9
            ,"/tt/TestJava.java:28: warning: Associated declaration",9
            );
    }


    @Test
    public void testMethodReturn1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class Test1 {
                static public /*@ non_null */ java.lang.Object m1() {
                    return new Object();
                }
                static public java.lang.Object m2() {
                    return new Object();
                }
                /*@ non_null */ static public java.lang.Object m3() {
                    return new Object();
                }
            }
            //@ nullable_by_default
            class Test2 {
                static public /*@ non_null */ java.lang.Object m1() {
                    return new Object();
                }
                static public java.lang.Object m2() {  // Line 20
                    return new Object();
                }
                /*@ non_null */ static public java.lang.Object m3() {
                    return new Object();
                }
            }
            class Test3 {
                static public java.lang.@NonNull Object m1() {
                    return new Object();
                }
                static public @NonNull java.lang.Object m2() {
                    return new Object();
                }
                static @NonNull public java.lang.Object m3() {
                    return new Object();
                }
            }
            public class TestJava {
                public void test1() {
                    Object o1 = Test1.m1();
                    Object o2 = Test1.m2();
                    Object o3 = Test1.m3();
                    Object o4 = Test2.m1();
                    Object o5 = Test2.m2(); // ERROR
                    Object o6 = Test2.m3();
                    Object o7 = Test3.m1();
                    Object o8 = Test3.m2();
                    Object o9 = Test3.m3();
                }
            }
            """
            ,"/tt/TestJava.java:44: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method test1: o5",16
            );
    }

    @Test
    public void testMethodReturn2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
             //@ non_null_by_default
            class Test1a {
                static public java.lang./*@ non_null */ Object m1() {
                    return new Object();
                }
            }
            public class TestJava {
                public void test1() {
                    Object o7 = Test1a.m1();
                }
            }
            """
            );
    }

    @Test
    public void testException1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava {
                public void m1() throws RuntimeException {
                    throw null;
                }
                public void m2() throws /*@ nullable */ RuntimeException {
                    throw null;
                }
                public void m3() throws @NonNull RuntimeException {
                    throw null;
                }
            }
            class TestJava1 {
                public void m1() throws java.lang.RuntimeException {
                    throw null;
                }
                public void m2() throws java.lang. /*@ nullable */ RuntimeException {
                    throw null;
                }
                public void m3() throws java.lang. @Nullable RuntimeException {
                    throw null;
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m1",15
            ,"/tt/TestJava.java:9: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m2",15
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m3",15
            ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m1",15
            ,"/tt/TestJava.java:20: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m2",15
            ,"/tt/TestJava.java:23: warning: The prover cannot establish an assertion (PossiblyNullValue) in method m3",15
            );
    }
    
    @Test
    public void testException2() {
        expectedExit = 1;
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1() throws  /*@ nullable */ java.lang.RuntimeException {
                    throw null;
                }
                public void m2() throws  @Nullable java.lang.RuntimeException {
                    throw null;
                }
            }
            """
            ,"/tt/TestJava.java:5: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava",46
            ,"/tt/TestJava.java:8: error: cannot find symbol\n  symbol:   class java\n  location: class tt.TestJava",40
            );
    }
    
    @Test
    public void testCast1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(@Nullable Object o) {
                    Object oo = (@NonNull Object)o;
                }
                //@ requires o != null;
                public void m2(@Nullable Object o) {
                    Object oo = (@NonNull Object)o;
                }
                public void m3(@Nullable Object o) {
                    Object oo = (@Nullable Object)o;
                }
                public void m4(@Nullable Object o) {
                    Object oo = (Object)o;
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (NullCast) in method m1",21
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m3: oo",16
            ,"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m4: oo",16
            );
    }
    
    @Test
    public void testCast2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ nullable_by_default
            class TestJava {
                public void m1(@Nullable Object o) {
                    Object oo = (@NonNull Object)o;
                }
                //@ requires o != null;
                public void m2(@Nullable Object o) {
                    Object oo = (@NonNull Object)o;
                }
                public void m3(@Nullable Object o) {
                    Object oo = (@Nullable Object)o;
                }
                public void m4(@Nullable Object o) {
                    Object oo = (Object)o;
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (NullCast) in method m1",21
            );
    }
    
    @Test
    public void testCast3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(@Nullable Object o) {
                    Object oo = (/*@ non_null */ Object)o;
                }
                //@ requires o != null;
                public void m2(@Nullable Object o) {
                    Object oo = (/*@ non_null */ Object)o;
                }
                public void m3(@Nullable Object o) {
                    Object oo = (/*@ nullable */ Object)o;
                }
                public void m4(@Nullable Object o) {
                    Object oo = (Object)o;
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (NullCast) in method m1",21
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m3: oo",16
            ,"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m4: oo",16
            );
    }
    
    @Test
    public void testInstanceof1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(@Nullable Object o) {
                    //@ assert o instanceof @NonNull Object; // ERROR
                }
                public void m2(@Nullable Object o) {
                    //@ assert o instanceof @Nullable Object;  // OK
                }
                //@ requires o != null;
                public void m3(@Nullable Object o) {
                    Object oo = (Object)o; // OK
                }
                public void m4(@Nullable Object o) {
                    Object oo = (Object)o; // ERROR
                }
                //@ requires o != null;
                public void m5(@Nullable Object o) {
                    //@ assert o instanceof @NonNull Object; // OK
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m1",13
            ,"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m4: oo",16
            );
    }
    
    @Test
    public void testInstanceof2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(@Nullable Object o) {
                    //@ assert o instanceof non_null Object; // ERROR
                }
                public void m2(@Nullable Object o) {
                    //@ assert o instanceof nullable Object;  // OK
                }
                public void m3(@Nullable Object o) {
                    boolean b = o instanceof /*@ non_null*/ Object;
                    //@ assert b; // ERROR
                }
                public void m4(@Nullable Object o) {
                    boolean b =  o instanceof /*@ nullable */ Object;
                    //@ assert b; // OK
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m1",13
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Assert) in method m3",13
            );
    }
    
    @Test
    public void testInstanceof3() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m2(@Nullable Object o) {
                    //@ assert o instanceof /*@ nullable*/ Object;  // FIXME: Parsing error
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Assert) in method m1",13
            ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Assert) in method m3",13
            );
    }
    
    @Test
    public void testClass1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public abstract class TestJava extends @NonNull Object implements @NonNull Cloneable {
            }
            """
            );
    }

    @Test
    public void testClass2() {
        expectedExit = 1;
        main.addOptions("--show=ast"); // FIXME
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava extends @NonNull java.lang.Object {
            }
            """
            ,"/tt/TestJava.java:4: error: cannot find symbol\n  symbol: class java",40
            );
    }

    @Test
    public void testClass2a() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava extends java.lang.@NonNull Object {
            }
            """
            );
    }

    @Test
    public void testClass3() {
        expectedExit = 1;
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava implements @NonNull java.lang.Cloneable {
            }
            """
            ,"/tt/TestJava.java:4: error: cannot find symbol\n  symbol: class java",43
            );
    }
    @Test
    public void testClass3a() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            public class TestJava implements java.lang.@NonNull Cloneable {
            }
            """
            );
    }
    
    @Test
    public void testGeneric1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class Gen<T> { T f; /*@ requires t != null; */ Gen(T t) { f = t; } T m(T x) { return x; } }
            public class TestJava {
                public void m2(@Nullable Object o, Gen<@NonNull Object> g, Gen<@Nullable Object> h) {
                    //@ assert g.f != null;
                    //@ check h.f != null; // ERROR
                }
            }
            """
            );
    }
    
    // FIXME - use non_null in for decl?
    @Test
    public void testFor1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m0(@NonNull Object @NonNull [] a) {
                    for (Object o: a) {
                        //@ assert o != null;
                    }
                }
                public void m1(@NonNull Object @NonNull [] a) {
                    for (@NonNull Object o: a) {
                        //@ assert o != null;
                    }
                }
                public void m2(@Nullable Object @NonNull [] a) {
                    for (Object o: a) { // type warning, NullElement warning
                        //@ assert o != null; // ERROR
                    }
                }
                //@ requires \\nonnullelements(a);
                public void m3(@Nullable Object @NonNull [] a) {
                    for (Object o: a) { // type warning, NullElement warning
                        //@ assert o != null; // ERROR
                    }
                }
                public void m4(Object [] a) {
                    for (Object o: a) {
                        //@ assert o != null;
                    }
                }
                //@ nullable_by_default
                class TestJava1 {
                    public void m0(Object @NonNull [] a) {
                        for (Object o: a) {
                            //@ assert o != null; // ERROR
                        }
                    }
                    public void m1(Object @NonNull [] a) {
                        for (@NonNull Object o: a) { // FIXME - should have a type error
                            //@ assert o != null;
                        }
                    }
                }
            }
            """
            ,"/tt/TestJava.java:16: warning: o is non_null but the type of a allows null array elements",21
            ,"/tt/TestJava.java:22: warning: o is non_null but the type of a allows null array elements",21
            ,"/tt/TestJava.java:39: warning: o is non_null but the type of a allows null array elements",34
            ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (Assert) in method m2",17
            ,"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (NullElement) in method m2",24
            ,"/tt/TestJava.java:22: warning: The prover cannot establish an assertion (NullElement) in method m3",24
            ,"/tt/TestJava.java:35: warning: The prover cannot establish an assertion (Assert) in method m0",21
            ,"/tt/TestJava.java:40: warning: The prover cannot establish an assertion (Assert) in method m1",21
            ,"/tt/TestJava.java:39: warning: The prover cannot establish an assertion (NullElement) in method m1",37
            );
    }
    
    // FIXME - use non_null in for decl?
    @Test
    public void testFor2() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m0(@Nullable Object oo) {
                    for (Object o = oo; b(o); ) {  // ERROR
                    }
                }
                public void m1(@Nullable Object oo) {
                    for (@Nullable Object o = oo; b(o); ) {
                    }
                }
                public void m2(Object oo) {
                    for (Object o = oo; b(o); o = f()) { // ERROR (the update)
                    }
                }
               @Nullable Object f() { return null; } 
                boolean b(@Nullable Object o) { return true; } 
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m0(@Nullable Object oo) {
                    for (@NonNull Object o = oo; b(o); ) { // ERROR
                    }
                }
                public void m1(@Nullable Object oo) {
                    for (Object o = oo; b(o); ) {
                    }
                }
                boolean b(@Nullable Object o) { return true; } 
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m0: o",21
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (PossiblyNullAssignment) in method m2",37
            ,"/tt/TestJava.java:23: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m0: o",30
            );
    }
    
    @Test
    public void testCatch1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(int i) {
                    try {
                        if (i < 0) throw new RuntimeException();
                    } catch (@Nullable Exception e) {
                            //@ assert e != null;
                    }
                }
                public void m2(int i) {
                    try {
                        if (i < 0) throw new RuntimeException();
                    } catch (/*@ nullable */ Exception e) {
                            //@ assert e != null;
                    }
                }
                public void m3(Throwable t) throws Throwable {
                    try {
                        throw t;
                    } catch (@Nullable Exception | @Nullable AssertionError e ) {
                        //@ assert e != null;
                    }
                }
            }
            """
            );
    }
    
    @Test
    public void testNew1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(int i) {
                    if (i < 0) throw new @Nullable RuntimeException();
                }
                public void m2(int i) {
                    if (i < 0) throw new /*@ nullable */RuntimeException();
                }
            }
            """
            );
    }
    
    @Test
    public void testLet1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1(@Nullable Object oo) {
                    //@ assert (\\let nullable Object o = oo; true);
                }
            }
            //@ non_null_by_default
            class TestJava1 {
                public void m1(@Nullable Object oo) {
                    //@ assert (\\let Object o = oo; true); // ERROR
                }
                public void m2(@Nullable Object oo) {
                    //@ assert oo != null ==> (\\let Object o = oo; true); // OK
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m1(@Nullable Object oo) {
                    //@ assert (\\let non_null Object o = oo; true); // ERROR
                }
            }
            //@ nullable_by_default
            class TestJava3 {
                public void m1(@Nullable Object oo) {
                    //@ assert (\\let Object o = oo; true);
                }
            }
            """
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m1",37
            ,"/tt/TestJava.java:21: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m1",46);
    }
    
    public void testQuant1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public void m1() {
                    //@ assert (\\forall nullable Object o; o == null; true);
                }
            }
            //@ non_null_by_default
            class TestJava1 {
                public void m1() {
                    //@ assert (\\forall Object o; o == null; true); // ERROR  // FIXME
                }
            }
            //@ nullable_by_default
            class TestJava2 {
                public void m1() {
                    //@ assert (\\forall non_null Object; o == null; true); // ERROR // FIXME
                }
            }
            //@ nullable_by_default
            class TestJava3 {
                public void m1() {
                    //@ assert (\\forall Object o = null; true);
                }
            }
            """
            ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m1",37
            ,"/tt/TestJava.java:18: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m1",46);
    }
    
    
    @Test
    public void testOld1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                public boolean f;
                //@ requires t.f; // ERROR
                public void m1(@Nullable TestJava t) {
                }
                //@ old TestJava tt = t; // ERROR
                //@ requires tt.f;
                public void m2(@Nullable TestJava t) {
                }
                //@ old @Nullable TestJava tt = t; // OK
                //@ requires tt.f; // ERROR
                public void m3(@Nullable TestJava t) {
                }
                //@ old @NonNull TestJava tt = t; // ERROR
                //@ requires tt.f; // OK
                public void m4(@Nullable TestJava t) {
                }
                //@ old nullable TestJava tt = t; // OK
                //@ requires tt.f; // ERROR
                public void m5(@Nullable TestJava t) {
                }
                //@ old non_null TestJava tt = t; // ERROR
                //@ requires tt.f; // OK
                public void m6(@Nullable TestJava t) {
                }
            }
            """
            ,"/tt/TestJava.java:6: warning: The prover cannot establish an assertion (UndefinedNullDeReference) in method m1",19
            ,"/tt/TestJava.java:9: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m2",27
            ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (UndefinedNullDeReference) in method m3",20
            ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m4",36
            ,"/tt/TestJava.java:22: warning: The prover cannot establish an assertion (UndefinedNullDeReference) in method m5",20
            ,"/tt/TestJava.java:25: warning: The prover cannot establish an assertion (UndefinedNullInitialization) in method m6",36
            );
    }
    
    @Test
    public void testSignals1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*;
            //@ non_null_by_default
            class TestJava {
                //@ signals_only @Nullable Exception, java.lang.@NonNull RuntimeException;
                public void m() {
                }
                //@ signals (@NonNull Exception e) e != null;
                public void m1() {
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                //@ signals (Exception e) e != null;
                public void m2() {
                }
            }
            """
            ,"/tt/TestJava.java:5: warning: Annotations on signals_only exception types are meaningless and are ignored",22
            ,"/tt/TestJava.java:5: warning: Annotations on signals_only exception types are meaningless and are ignored",53
            );
    }
    
    @Test
    public void testTry1() {
        helpTCX("tt.TestJava",
            """
            package tt;
            import org.jmlspecs.annotation.*; import java.io.*;
            //@ non_null_by_default
            class TestJava {
                public void m() throws Exception {
                    try (@NonNull FileReader f1 = new FileReader("")) {}
                    try (@Nullable FileReader f2 = new FileReader("")) {}
                    try (FileReader f3 = new FileReader("")) {}
                }
                public void m1() throws Exception {
                    try (@NonNull FileReader f1 = null) {} // ERROR
                    try (@Nullable FileReader f2 = null) {}
                    try (FileReader f3 = null) {}
                }
            }
            //@ nullable_by_default
            class TestJava1 {
                public void m2() throws Exception {
                    try (FileReader f = new FileReader("")) {}
                    try (FileReader f3 = null) {}
                }
            }
            """
            ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (PossiblyNullInitialization) in method m1: f1",34
            );
    }
    
    
    
    // type parameters
    // type arguments
    
    // allocate array
    // lambda function
    
    // quantified declaration
    
    // type checking in assignment, in actual to formal, in initialization, in array initialization
}
