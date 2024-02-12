package org.example;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import raykernel.apps.readability.eval.Main;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
public class JavaParser {
    static String file = "";
    public static CodeMetric parseFile(String code){
        ParserConfiguration parserConfiguration = new ParserConfiguration().setAttributeComments(false).setLanguageLevel(ParserConfiguration.LanguageLevel.RAW);

        StaticJavaParser.setConfiguration(parserConfiguration);
        CompilationUnit cu = StaticJavaParser.parse(
                "public class Method{" + """
                        %s
                        """.formatted(code)
                +'}');

        MethodLister methodVisitor = new MethodLister();
        methodVisitor.visit(cu, file);

        return methodVisitor.codeMetric;
    }
    public static class MethodLister extends VoidVisitorAdapter<String>{
        CodeMetric codeMetric=new CodeMetric();
        @Override
        public void visit(MethodDeclaration mt, String file){
            super.visit(mt, file);
            codeMetric = processMethod(mt);
        }
    }
    public static CodeMetric processMethod(MethodDeclaration mt){

        int number_of_predicates=mt.findAll(IfStmt.class).size()+mt.findAll(ForStmt.class).size()+mt.findAll(SwitchEntry.class).size()
                +mt.findAll(WhileStmt.class).size();

        double readability= Main.getReadability(String.valueOf(mt.getBody()));
        int mcCabe=1+number_of_predicates;//cyclomatic complexity
        int sloc=countLines(String.valueOf(mt.getBody()));//source lines of code

        CodeMetric codeMetric=new CodeMetric();
        codeMetric.mcCabe=mcCabe;
        codeMetric.sloc=sloc;
        codeMetric.readability=readability;
        return codeMetric;
    }
    public static class CodeMetric {
        public int sloc;
        public int mcCabe;
        double readability;
    }
    public static int countLines(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        String[] lines = str.split("\\r?\\n");
        return lines.length;
    }
}
