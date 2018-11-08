package com.ivyzh.mybutterknife_compiler;

import com.google.auto.service.AutoService;
import com.ivyzh.mybutterknife_annotations.MyBindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by Ivy on 2018/11/8.
 */

@AutoService(Processor.class)
public class MyButterKnifeProcessor extends AbstractProcessor {

    private Filer mFiler;//用来生成java文件
    private Elements mElementsUtils;//用来获取生成java文件的路径


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementsUtils = processingEnvironment.getElementUtils();
    }

    // 1. 指定处理的版本，这里返回最新版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    //2. 给到需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(MyBindView.class);
        // ... 其他注解，如onClick

        return annotations;
    }

    //3. 生成代码
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("----------->代码生成部分");
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(MyBindView.class);
        for (Element e : elements) {
            Element enclosingElement = e.getEnclosingElement();
            System.out.println("--Element--" + e.getSimpleName().toString() + " in " + enclosingElement.getSimpleName().toString());
        }


        // 1. 解析 map: activity-->list<element>
        Map<Element, List<Element>> map = new LinkedHashMap<>();
        for (Element e : elements) {
            Element enclosingElement = e.getEnclosingElement();

            List<Element> viewElements = map.get(enclosingElement);
            if (viewElements == null) {
                viewElements = new ArrayList<>();
                map.put(enclosingElement, viewElements);
            }
            viewElements.add(e);
        }

        // 2.生成 java类

        for (Map.Entry<Element, List<Element>> entry : map.entrySet()) {
            Element enclosingElement = entry.getKey();
            List<Element> elementsValue = entry.getValue();

            System.out.println(enclosingElement + "----------->" + elementsValue.size());//com.ivyzh.butterknifedemo.MainActivity----------->2


            String activitySimpleName = enclosingElement.getSimpleName().toString();
            ClassName unbinderClassSimpleName = ClassName.get("com.ivyzh.mybutterknife", "MyUnbinder");
            System.out.println("unbinderClassSimpleName----------->" + unbinderClassSimpleName);//com.ivyzh.mybutterknife.MyUnbinder


            // 2.1组装类:  xxx_MyViewBinding implements Unbinder
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activitySimpleName + "_MyViewBinder")
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC).addSuperinterface(unbinderClassSimpleName)
                    .addField(ClassName.bestGuess(enclosingElement.toString()), "target");// 添加成员变量 MainActivity target;
            ;


            // 2.2组装unbind 方法
            MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);

            // 2.3组装构造函数: public xxx_ViewBinding(xxx target)
//             ClassName activityName = ClassName.bestGuess(activitySimpleName);// 这里不能用activitySimpleName
            ClassName activityName = ClassName.bestGuess(enclosingElement.toString());// 用enclosingElement.toString()
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(activityName, "target");


            // 2.3.1添加 target.textView1 = Utils.findViewById(target,R.id.tv1);
            //  target.username = finder.findRequiredViewAsType(source, 2131230905, "field 'username'", TextView.class);

            // 2.3.2 unber里面的方法也在这里面实现
            unbindMethodBuilder.addStatement("$L target= this.target", enclosingElement.getSimpleName().toString());

            for (Element view : elementsValue) {
                String viewName = view.getSimpleName().toString();
                ClassName utilsName = ClassName.get("com.ivyzh.mybutterknife", "FindViewUtils");
                int resId = view.getAnnotation(MyBindView.class).value();
                constructorBuilder.addStatement("this.target = target");
                constructorBuilder.addStatement("target.$L = $L.findViewById(target,$L)", viewName, utilsName, resId);
                unbindMethodBuilder.addStatement("target.$L = null", viewName);

            }


            // 2.4 将方法统一添加到classBuilder中
            classBuilder.addMethod(constructorBuilder.build());// 添加构造函数
            classBuilder.addMethod(unbindMethodBuilder.build());// 添加unbinder方法


            // 2.5生成类
            try {
                String packageName = "";//如果是空的话，会生成在根目录
                packageName = mElementsUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                JavaFile.builder(packageName, classBuilder.build())
                        .build()
                        .writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("生成类失败：" + e.toString());
            }
        }


        return false;
    }
}
