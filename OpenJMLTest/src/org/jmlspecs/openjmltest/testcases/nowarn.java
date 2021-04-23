package org.jmlspecs.openjmltest.testcases;

/** This tests parsing of nowarn. */

import org.jmlspecs.openjml.JmlTree.*;
import org.jmlspecs.openjmltest.ParseBase;
import org.junit.Test;

import com.sun.tools.javac.tree.JCTree.*;

@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class nowarn extends ParseBase {


    @Override
    public void setUp() throws Exception {
//        noCollectDiagnostics = true;
//        jmldebug = true;
        super.setUp();
    }
    
    @Test
    public void testNowarn() {
        checkCompilationUnit("/*@ nowarn Z; */package t; //@ nowarn X; \n/*@ nowarn Y; */ class A{}",
                JmlCompilationUnit.class,16,
                JCPackageDecl.class,16,
                JCIdent.class, 24,
                JmlClassDecl.class, 59,
                JmlModifiers.class, -1);
        checkMessages();
    }
    
    @Test
    public void testNowarn2() {
        checkCompilationUnit("package /*@ nowarn Z ZZ ZZZ; */t; //@ nowarn X,A,B; \n/*@ nowarn Y, YY,  YYY; */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 31,
                JmlClassDecl.class, 80,
                JmlModifiers.class, -1);
        checkMessages();
    }
    
    @Test
    public void testNowarn3() {
        checkCompilationUnit("package /*@ nowarn Z ; nowarn Q; */t; //@ nowarn X,A,B; \n/*@ nowarn Y, YY,  YYY; */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 35,
                JmlClassDecl.class, 84,
                JmlModifiers.class, -1);
        checkMessages();
    }
    
    @Test
    public void testNowarn4() {
        checkCompilationUnit("package /*@ nowarn Z  */t; //@ nowarn X\n/*@ nowarn ; */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 24,
                JmlClassDecl.class, 56,
                JmlModifiers.class, -1);
        checkMessages("/TEST.java:1: warning: A line annotation should end with a semicolon",23,
                "/TEST.java:1: warning: A line annotation should end with a semicolon",40);
    }
    
    @Test
    public void testNowarn5() {
        checkCompilationUnit("package /*@ nowarn Z  */t; //@ nowarn X\n/*@ nowarn ; */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 24,
                JmlClassDecl.class, 56,
                JmlModifiers.class, -1);
        checkMessages("/TEST.java:1: warning: A line annotation should end with a semicolon",23,
                "/TEST.java:1: warning: A line annotation should end with a semicolon",40);
    }
    
    @Test
    public void testNowarn6() {
        checkCompilationUnit("package /*@ nowarn nowarn */t; \n class /*@ nowarn ; */A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 28,
                JmlClassDecl.class, 33,
                JmlModifiers.class, -1);
        checkMessages("/TEST.java:1: warning: A line annotation should end with a semicolon",27);
    }
    
    @Test
    public void testNowarnA() {
        checkCompilationUnit("package t; \n/*@ pure nowarn Y; */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 8,
                JmlClassDecl.class, 34,
                JmlModifiers.class, 16,
                JmlAnnotation.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCIdent.class, 16
        );
        checkMessages();
    }
    
    @Test
    public void testNowarnB() {
        checkCompilationUnit("package t; \n/*@ pure nowarn Y */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 8,
                JmlClassDecl.class, 33,
                JmlModifiers.class, 16,
                JmlAnnotation.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCIdent.class, 16
        );
        checkMessages("/TEST.java:2: warning: A line annotation should end with a semicolon",19);
    }
    
    @Test
    public void testNowarnC() {
        checkCompilationUnit("package t; \n/*@ pure nowarn C; nullable_by_default */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 8,
                JmlClassDecl.class, 54,
                JmlModifiers.class, 16,
                JmlAnnotation.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCIdent.class, 16,
                JmlAnnotation.class, 31,
                JCFieldAccess.class, 31,
                JCFieldAccess.class, 31,
                JCFieldAccess.class, 31,
                JCIdent.class, 31
        );
        checkMessages();
    }
    
    @Test
    public void testNowarnD() {
        checkCompilationUnit("package t; \n/*@ pure nowarn C nullable            */ class A{}",
                JmlCompilationUnit.class,0,
                JCPackageDecl.class,0,
                JCIdent.class, 8,
                JmlClassDecl.class, 53,
                JmlModifiers.class, 16,
                JmlAnnotation.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCFieldAccess.class, 16,
                JCIdent.class, 16
                );
        checkMessages("/TEST.java:2: warning: A line annotation should end with a semicolon",39);
    }
  
    @Test
    public void testNowarnE() {
        checkCompilationUnit("package t; \n/*@  nowarn C! ; */ class A{}",
                JmlCompilationUnit.class,0,0,41,
                JCPackageDecl.class, 0,0,10,
                JCIdent.class, 8,8,9,
                JmlClassDecl.class, 32,32,41,
                JmlModifiers.class, -1,-1,-1);
        checkMessages("/TEST.java:2: error: Expected an identifier here in the line annotation",14);
    }
}
