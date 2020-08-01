package reflection.api;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class InvestigatorImpl implements Investigator {

    private Object objToInvestigate;
    private Class clsToInvestigate;

    //default public c'tor
    public InvestigatorImpl() {
    }

    @Override
    public void load(Object anInstanceOfSomething) {
        objToInvestigate = anInstanceOfSomething;
        clsToInvestigate = anInstanceOfSomething.getClass();
    }

    @Override
    public int getTotalNumberOfMethods() {
        Method[] allMethods = clsToInvestigate.getDeclaredMethods();
        return allMethods.length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        Constructor[] allConstructors = clsToInvestigate.getDeclaredConstructors();
        return allConstructors.length;
    }

    @Override
    public int getTotalNumberOfFields() {
        Field[] allFields = clsToInvestigate.getDeclaredFields();
        return allFields.length;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        Set<String> names = new HashSet<String>();
        Type[] interfaces = clsToInvestigate.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            names.add(interfaces[i].getTypeName());
        }

        return names;
    }

    @Override
    public int getCountOfConstantFields() {
        int countConstants = 0;
        Field[] allFields = clsToInvestigate.getDeclaredFields();
        for (int i = 0; i < allFields.length; i++) {
            if (Modifier.isFinal(allFields[i].getModifiers())) {
                countConstants++;
            }
        }

        return countConstants;
    }

    @Override
    public int getCountOfStaticMethods() {
        int countStaticMethods = 0;
        Method[] allMethods = clsToInvestigate.getDeclaredMethods();
        for (int i = 0; i < allMethods.length; i++) {
            if (Modifier.isStatic(allMethods[i].getModifiers())) {
                countStaticMethods++;
            }
        }

        return countStaticMethods;
    }

    @Override
    public boolean isExtending() {
        Class<?> superClass = clsToInvestigate.getSuperclass();
        if (superClass == null) {
            return false;
        }

        return true;
    }

    @Override
    public String getParentClassSimpleName() {
        String parentClassName = null;
        Class<?> superClass = clsToInvestigate.getSuperclass();
        if (superClass == null) {
            return null;
        }
        else {
            return superClass.getSimpleName();
        }
    }

    @Override
    public boolean isParentClassAbstract() {
        Class<?> superClass = clsToInvestigate.getSuperclass();
        if (superClass == null) {
            return false;
        }
        else if (Modifier.isAbstract(superClass.getModifiers())) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        Set<String> allFields = new HashSet<String>();
        Class<?> currentClass = clsToInvestigate;
        while (currentClass.getSuperclass() != null) {
            Field[] currentClassFields = clsToInvestigate.getDeclaredFields();
            for (int i = 0; i < currentClassFields.length; i++) {
                allFields.add(currentClassFields[i].getName());
            }
            currentClass = currentClass.getSuperclass();
        }

        return allFields;
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        try {
            Method method = clsToInvestigate.getDeclaredMethod(methodName, parameterTypes);
            int result = (int)method.invoke(objToInvestigate, args);
            return result;

        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        Class[] parameterTypes = new Class[numberOfArgs];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        try {
            Constructor ctor = clsToInvestigate.getDeclaredConstructor(parameterTypes);
            return ctor.newInstance(args);
        }
        catch (NoSuchMethodException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try {
            Method method = clsToInvestigate.getDeclaredMethod(name, parametersTypes);
            method.setAccessible(true);
            return method.invoke(objToInvestigate,args);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            return null;
        }  catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        StringBuilder inheritanceChain = new StringBuilder();
        Class<?> currentClass = clsToInvestigate;
        inheritanceChain.insert(0,currentClass.getName());
        while (currentClass.getSuperclass() != null) {
               currentClass = currentClass.getSuperclass();
               inheritanceChain.insert(0,currentClass.getName() + delimiter);
            }
        inheritanceChain.insert(0, "Object" + delimiter);

        return inheritanceChain.toString();
    }
}
