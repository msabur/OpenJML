package org.jmlspecs.openjml.ext;

import static com.sun.tools.javac.parser.Tokens.TokenKind.SEMI;
import static com.sun.tools.javac.parser.Tokens.TokenKind.STAR;
import static com.sun.tools.javac.parser.Tokens.TokenKind.SUPER;
import static com.sun.tools.javac.parser.Tokens.TokenKind.THIS;
import static com.sun.tools.javac.parser.Tokens.TokenKind.BANG;
import static com.sun.tools.javac.parser.Tokens.TokenKind.COMMA;
import static com.sun.tools.javac.parser.Tokens.TokenKind.DOT;
import static com.sun.tools.javac.parser.Tokens.TokenKind.FOR;
import static com.sun.tools.javac.parser.Tokens.TokenKind.IDENTIFIER;
import static com.sun.tools.javac.parser.Tokens.TokenKind.LPAREN;
import static com.sun.tools.javac.parser.Tokens.TokenKind.NEW;
import static com.sun.tools.javac.parser.Tokens.TokenKind.RPAREN;
import static org.jmlspecs.openjml.ext.MiscExtensions.everythingID;
import static org.jmlspecs.openjml.ext.MiscExtensions.nothingID;
import static org.jmlspecs.openjml.ext.MiscExtensions.notspecifiedID;
import static org.jmlspecs.openjml.ext.TypeExprClauseExtension.invariantClause;

import javax.tools.JavaFileObject;

import org.jmlspecs.openjml.IJmlClauseKind;
import org.jmlspecs.openjml.JmlExtension;
import org.jmlspecs.openjml.JmlTokenKind;
import org.jmlspecs.openjml.JmlTree;
import org.jmlspecs.openjml.JmlTree.JmlMethodClauseExpr;
import org.jmlspecs.openjml.JmlTree.JmlMethodSig;
import org.jmlspecs.openjml.JmlTree.JmlTypeClause;
import org.jmlspecs.openjml.JmlTree.JmlTypeClauseConstraint;
import org.jmlspecs.openjml.JmlTree.JmlTypeClauseExpr;
import org.jmlspecs.openjml.JmlTree.Maker;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Kinds.KindSelector;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.JmlAttr;
import com.sun.tools.javac.comp.JmlResolve;
import com.sun.tools.javac.parser.JmlParser;
import com.sun.tools.javac.parser.Tokens.TokenKind;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class TypeExprClauseExtension extends JmlExtension {

    public static final String invariantID = "invariant";
    public static final String constraintID = "constraint";
    public static final String axiomID = "axiom";
    public static final String initiallyID = "initially";
    
    public static final IJmlClauseKind invariantClause = new TypeClause(invariantID);
    
    public static final IJmlClauseKind constraintClause = new TypeClause(constraintID);
    
    public static final IJmlClauseKind axiomClause = new TypeClause(axiomID);
    
    public static final IJmlClauseKind initiallyClause = new TypeClause(initiallyID);
    
    public static class TypeClause extends IJmlClauseKind.TypeClause {
        public TypeClause(String keyword) { super(keyword); }

        public boolean oldNoLabelAllowed() { return true; }
        public boolean preOrOldWithLabelAllowed() { return false; }
        
        public 
        JmlTypeClause parse(JCModifiers mods, String keyword, IJmlClauseKind clauseType, JmlParser parser) {
            init(parser);
            
            int pp = parser.pos();
            int pe = parser.endPos();
            
            
            if (clauseType == constraintClause) {
                JmlTree.JmlTypeClauseConstraint tcl = parseConstraint(mods);
                tcl.source = log.currentSourceFile();
                return tcl;
            } else {
                parser.nextToken();
                JCExpression e = parser.parseExpression();
                if (parser.token().kind != SEMI) {
                    utils.error(parser.pos(), parser.endPos(), "jml.bad.construct",
                            keyword + " declaration");
                    parser.skipThroughSemi();
                } else {
                    parser.nextToken();
                }
                Maker M = parser.maker().at(pp);
                if (mods == null) mods = M.Modifiers(0);
                JmlTypeClauseExpr tcl = parser.to(M.JmlTypeClauseExpr(mods, keyword, clauseType, e));
                tcl.source = log.currentSourceFile();
                return tcl;
            }
        }
        
        /** Parses a constraint clause */
        public JmlTypeClauseConstraint parseConstraint(JCModifiers mods) {
            int pos = parser.pos();
            parser.nextToken();
            JCExpression e = parser.parseExpression();
            List<JmlMethodSig> sigs = null;
            boolean notlist = false;
            if (parser.token().kind == FOR) {
                parser.nextToken();
                if (parser.token().kind == BANG) {
                    notlist = true;
                    parser.nextToken();
                }
                if (parser.tokenIsId(everythingID,notspecifiedID)) {
                    parser.nextToken();
                    // This is the default, so we just leave sigs null
                    if (notlist) sigs = new ListBuffer<JmlMethodSig>().toList();
                    notlist = false;
                } else if (parser.tokenIsId(nothingID)) {
                    parser.nextToken();
                    if (!notlist) sigs = new ListBuffer<JmlMethodSig>().toList();
                    notlist = false;
                    // Here we just have an empty list
                } else {
                    sigs = parseMethodNameList();
                }
            }
            if (mods == null) mods = parser.jmlF.at(pos).Modifiers(0);
            JmlTypeClauseConstraint tcl = parser.to(parser.jmlF.at(pos).JmlTypeClauseConstraint(
                    mods, e, sigs));
            tcl.notlist = notlist;
            tcl.source = log.currentSourceFile();
            if (parser.token().kind != SEMI) {
                utils.error(parser.pos(), parser.endPos(), "jml.bad.construct",
                        "constraint declaration");
                parser.skipThroughSemi();
            } else {
                parser.nextToken();
            }
            return tcl;
        }

        
        public Type typecheck(JmlAttr attr, JCTree expr, Env<AttrContext> env) {
        	JmlTypeClauseExpr tree = (JmlTypeClauseExpr)expr;
            boolean isStatic = tree.modifiers != null && attr.isStatic(tree.modifiers);
            JavaFileObject old = log.useSource(tree.source);
            attr.jmlenv = attr.jmlenv.pushCopy();
            VarSymbol previousSecretContext = attr.currentSecretContext;
            boolean prevAllowJML = attr.jmlresolve.setAllowJML(true);
            long prevVisibility = attr.jmlVisibility;
            Env<AttrContext> localEnv = env; // FIXME - here and in constraint, should we make a new local environment?
            try {
                attr.jmlenv.inPureEnvironment = true;
                attr.jmlenv.currentClauseKind = tree.clauseType;
                // invariant, axiom, initially
                //if (tree.token == JmlToken.AXIOM) isStatic = true; // FIXME - but have to sort out use of variables in axioms in general
                if (isStatic) attr.bumpStatic(localEnv);

                if (tree.clauseType == invariantClause) {
                	attr.jmlVisibility = -1;
                	attr.attribAnnotationTypes(tree.modifiers.annotations,env); // Is this needed?
                    JCAnnotation a = attr.findMod(tree.modifiers,Modifiers.SECRET);
                    attr.jmlVisibility = tree.modifiers.flags & Flags.AccessFlags;
                    if (a != null) {
                        if (a.args.size() != 1) {
                        	utils.error(tree.pos(),"jml.secret.invariant.one.arg");
                        } else {
                            Name datagroup = attr.getAnnotationStringArg(a);
                            if (datagroup != null) {
                                //Symbol v = rs.findField(env,env.enclClass.type,datagroup,env.enclClass.sym);
                                Symbol v = JmlResolve.instance(context).resolveIdent(a.args.get(0).pos(),env,datagroup,KindSelector.VAR);
                                if (v instanceof VarSymbol) attr.currentSecretContext = (VarSymbol)v;
                                else if (v instanceof PackageSymbol) {
                                	utils.error(a.args.get(0).pos(),"jml.annotation.arg.not.a.field",v.getQualifiedName());
                                }
                            }
                        }
                    }
                }

                attr.attribExpr(tree.expression, localEnv, syms.booleanType);
                attr.checkTypeClauseMods(tree,tree.modifiers,tree.clauseType.name() + " clause",tree.clauseType);
                return null;
            } catch (Exception e) {
            	utils.note(tree, "jml.message", "Exception occurred in attributing clause: " + tree);
            	utils.note("    Env: " + env.enclClass.name + " " + (env.enclMethod==null?"<null method>": env.enclMethod.name));
            	throw e;
            } finally {
                if (isStatic) attr.decStatic(localEnv);  // FIXME - move this to finally, but does not screw up the checks on the next line?
                attr.jmlVisibility = prevVisibility;
                attr.currentSecretContext = previousSecretContext;
                attr.jmlresolve.setAllowJML(prevAllowJML);
                attr.jmlenv = attr.jmlenv.pop();
                log.useSource(old);
            }
        }
    }
}
