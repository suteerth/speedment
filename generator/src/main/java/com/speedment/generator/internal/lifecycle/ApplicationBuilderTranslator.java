package com.speedment.generator.internal.lifecycle;

import com.speedment.fika.codegen.Generator;
import com.speedment.fika.codegen.model.File;
import com.speedment.fika.codegen.model.Class;
import com.speedment.fika.codegen.model.Type;
import com.speedment.generator.internal.DefaultJavaClassTranslator;
import com.speedment.runtime.Speedment;
import com.speedment.runtime.config.Project;
import com.speedment.runtime.internal.runtime.AbstractApplicationBuilder;

/**
 *
 * @author Emil Forslund
 * @since  2.4.0
 */
public final class ApplicationBuilderTranslator extends DefaultJavaClassTranslator<Project, Class> {

    private final String className = getSupport().typeName(getSupport().projectOrThrow()) + "ApplicationBuilder";
    
    public ApplicationBuilderTranslator(
            Speedment speedment, 
            Generator generator, 
            Project project) {
        
        super(speedment, generator, project, Class::of);
    }

    @Override
    protected String getClassOrInterfaceName() {
        return className;
    }

    @Override
    protected Class makeCodeGenModel(File file) {
        return newBuilder(file, className)
            .forEveryProject((clazz, project) -> {
                clazz.public_().final_()
                    .setSupertype(generatedBuilderType());
            }).build();
    }

    @Override
    protected String getJavadocRepresentText() {
        return "The default {@link " + AbstractApplicationBuilder.class.getName() + 
            "} implementation class for the {@link " + Project.class.getName() + 
            "} named " + getSupport().projectOrThrow().getName() + ".";
    }
    
    private Type generatedBuilderType() {
        return Type.of(
            getSupport().basePackageName() + ".generated.Generated" + 
            getSupport().typeName(getSupport().projectOrThrow()) + "ApplicationBuilder"
        );
    }
}