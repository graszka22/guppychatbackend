package annotationprocessor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import static annotationprocessor.Utils.*;

@SupportedAnnotationTypes("annotationprocessor.Schema")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class SchemaProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            try {
                for(Element e : annotatedElements) {
                    generateGatewayInterface(e);
                    generateGatewayImplementation(e);
                    generateGatewayFactory(e);
                    generateGatewayFactoryImplementation(e);
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "IOException!"+e.getMessage());
            }
        }
        return true;
    }

    private static class Field {
        String name;
        String type;
        Field(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    private void generateGatewayInterface(Element e) throws IOException {
        String tableName = e.getAnnotation(Schema.class).tableName();
        String className = snakeCaseToCamelCase(tableName) + "Gateway";
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package com.guppychat.backend.datasource.gateways;");
            out.println("import java.sql.SQLException;");
            out.println(String.format("public interface %s {", className));
            ArrayList<Field> fields = new ArrayList<>();
            for(Element f : e.getEnclosedElements()) {
                if (f.getKind() != ElementKind.FIELD) continue;
                fields.add(new Field(f.getSimpleName().toString(), f.asType().toString()));
            }
            for(Field f : fields) {
                out.println(String.format("\t%s get%s();", f.type, upperFirstLetter(f.name)));
                out.println(String.format("\tvoid set%s(%s %s);", upperFirstLetter(f.name), f.type, f.name));
            }
            out.println("\tint getId();");
            out.println("\tvoid setId(int id);");
            out.println("\tint insert() throws SQLException;");
            out.println("\tvoid update() throws SQLException;");
            out.println("}");
        }
    }

    private void generateGatewayFactory(Element e) throws IOException {
        String tableName = e.getAnnotation(Schema.class).tableName();
        String className = snakeCaseToCamelCase(tableName) + "GatewayFactory";
        String gatewayName = snakeCaseToCamelCase(tableName) + "Gateway";
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package com.guppychat.backend.datasource.gateways;");
            out.println(String.format("import com.guppychat.backend.datasource.gateways.%s;", gatewayName));
            out.println(String.format("public interface %s {", className));
            out.println(String.format("\t%s create();", gatewayName));
            out.println("}");
        }
    }

    private void generateGatewayFactoryImplementation(Element e) throws IOException {
        String tableName = e.getAnnotation(Schema.class).tableName();
        String className = snakeCaseToCamelCase(tableName) + "GatewayPSQLFactory";
        String gatewayName = snakeCaseToCamelCase(tableName) + "GatewayPSQL";
        String factoryName = snakeCaseToCamelCase(tableName) + "GatewayFactory";
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package com.guppychat.backend.datasource.PSQL.gateways;");
            out.println(String.format("import com.guppychat.backend.datasource.gateways.%s;", factoryName));
            out.println(String.format("import com.guppychat.backend.datasource.PSQL.gateways.%s;", gatewayName));
            out.println(String.format("public class %s implements %s {", className, factoryName));
            out.println("\t@Override");
            out.println(String.format("\tpublic %s create() { return new %s(); }", gatewayName, gatewayName));
            out.println("}");
        }
    }

    private void generateGatewayImplementation(Element e) throws IOException {
        String tableName = e.getAnnotation(Schema.class).tableName();
        String interfaceName = snakeCaseToCamelCase(tableName) + "Gateway";
        String className = snakeCaseToCamelCase(tableName) + "GatewayPSQL";

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package com.guppychat.backend.datasource.PSQL.gateways;");
            out.println(String.format("import com.guppychat.backend.datasource.gateways.%s;", interfaceName));
            out.println("import com.guppychat.backend.Registry;");
            out.println("import java.sql.PreparedStatement;");
            out.println("import java.sql.ResultSet;");
            out.println("import java.sql.SQLException;");
            out.println("");
            out.println(String.format("public class %s implements %s {", className, interfaceName));
            ArrayList<Field> fields = new ArrayList<>();
            for(Element f : e.getEnclosedElements()) {
                if (f.getKind() != ElementKind.FIELD) continue;
                fields.add(new Field(f.getSimpleName().toString(), f.asType().toString()));
            }
            out.println("\tprivate int id;");
            for(Field f : fields)
                out.println(String.format("\tprivate %s %s;", f.type, f.name));

            out.println("");

            out.println(String.format("\tpublic %s(ResultSet rs) throws SQLException {", className));
            out.println("\t\tthis.id = rs.getInt(\"id\");");
            for(Field f : fields)
                out.println(String.format("\t\tthis.%s = rs.get%s(\"%s\");", f.name, upperFirstLetter(getClassNameFromQualifiedName(f.type)), camelCaseToSnakeCase(f.name)));
            out.println("\t}");

            out.println("");

            out.println(String.format("\tpublic %s() {}", className));

            out.println("");
/*
            out.print(String.format("\tpublic %s(", className));
            out.print(String.join(", ", fields.stream()
                    .map(field -> String.format("%s %s", field.type, field.name)).toArray(String[]::new)));
            out.println(") {");
            for(Field f : fields)
                out.println(String.format("\t\tthis.%s = %s;", f.name, f.name));
            out.println("\t}");
*/
            out.println("");

            for(Field f : fields) {
                out.println("");
                out.println(String.format("\tpublic %s get%s() {", f.type, upperFirstLetter(f.name)));
                out.println(String.format("\t\treturn %s;", f.name));
                out.println("\t}");
                out.println("");
                out.println(String.format("\tpublic void set%s(%s %s) {", upperFirstLetter(f.name), f.type, f.name));
                out.println(String.format("\t\tthis.%s = %s;", f.name, f.name));
                out.println("\t}");
            }
            out.println("\tpublic int getId() { return id; }");
            out.println("\tpublic void setId(int id) { this.id = id; }");
            out.println("");

            out.print(String.format("\tprivate static final String insertStatement = \"INSERT INTO %s(", tableName));
            out.print(String.join(", ", fields.stream().map(field -> camelCaseToSnakeCase(field.name)).toArray(String[]::new)));
            out.print(") VALUES (");
            out.print(String.join(", ", fields.stream().map(field -> "?").toArray(String[]::new)));
            out.println(");\";");

            out.print(String.format("\tprivate static final String updateStatement = \"UPDATE %s SET ", tableName));
            out.print(String.join(", ", fields.stream().map(field -> String.format("%s = ?", camelCaseToSnakeCase(field.name))).toArray(String[]::new)));
            out.println(" WHERE id = ?;\";");

            out.println("");

            out.println("\tpublic int insert() throws SQLException {");
            out.println("\t\tPreparedStatement pstmt = Registry.getPSQLDatabase().getPreparedStatement(insertStatement);");
            for(int i = 0; i < fields.size(); ++i) {
                out.println(String.format("\t\tpstmt.set%s(%d, %s);", upperFirstLetter(getClassNameFromQualifiedName(fields.get(i).type)), i+1, fields.get(i).name));
            }
            out.println("\t\tpstmt.executeUpdate();");
            out.println("\t\ttry(ResultSet rs = pstmt.getGeneratedKeys()) {");
            out.println("\t\t\tif (rs.next()) {");
            out.println("\t\t\t\tthis.id = rs.getInt(1);");
            out.println("\t\t\t\treturn this.id;");
            out.println("\t\t\t}");
            out.println("\t\t}");
            out.println("\t\tthrow new SQLException(\"Can't insert row into database\");");
            out.println("\t}");

            out.println("");
            out.println("\tpublic void update() throws SQLException {");
            out.println("\t\tPreparedStatement pstmt = Registry.getPSQLDatabase().getPreparedStatement(updateStatement);");
            for(int i = 0; i < fields.size(); ++i) {
                out.println(String.format("\t\tpstmt.set%s(%d, %s);", upperFirstLetter(getClassNameFromQualifiedName(fields.get(i).type)), i+1, fields.get(i).name));
            }
            out.println(String.format("\t\tpstmt.setInt(%d, id);", fields.size()+1));
            out.println("\t\tpstmt.executeUpdate();");
            out.println("\t}");

            out.println("}");
        }
    }
}