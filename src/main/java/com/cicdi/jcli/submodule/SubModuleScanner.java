package com.cicdi.jcli.submodule;


import com.beust.jcommander.Parameters;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author haypo
 * @date 2021/1/9
 */
public class SubModuleScanner {
    public static List<Class<?>> scan2List(String packageName) {
        return ReflectionSupport.findAllClassesInPackage(
                packageName,
                aClass -> aClass.getAnnotation(Parameters.class) != null && (
//                        Arrays.asList(aClass.getInterfaces()).contains(ISubModule.class) &&
                        !ModifierSupport.isAbstract(aClass)
                ),
                s1 -> true
        );
    }

    public static Map<String, Class<?>> scan2Map(String packageName) {
        Map<String, Class<?>> map = new TreeMap<>();

        scan2List(packageName).forEach(
                aClass -> {
                    Parameters parameters = aClass.getAnnotation(Parameters.class);
                    if (parameters != null) {
                        map.put(parameters.commandNames()[0], aClass);
                    }
                }
        );
        return map;
    }

    public static Map<String, ISubmodule> scanSubModule() {
        Map<String, Class<?>> map = scan2Map("com.cicdi.jcli.submodule");
        Map<String, ISubmodule> moduleMap = new HashMap<>(20);
        map.forEach(
                (k, v) -> {
                    try {
                        v.getConstructor().setAccessible(true);
                        ISubmodule obj = (ISubmodule) v.newInstance();
                        moduleMap.put(k, obj);
                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }

                }
        );
        return moduleMap;
    }
}
