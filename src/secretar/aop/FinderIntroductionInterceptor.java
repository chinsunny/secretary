package secretar.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

public class FinderIntroductionInterceptor implements IntroductionInterceptor {

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object target = methodInvocation.getThis();

        if (target instanceof FinderExecutor) {
            String methodName = methodInvocation.getMethod().getName();
            if (methodName.startsWith("find")) {
                Object[] arguments = methodInvocation.getArguments();
                Class<?> returnType = methodInvocation.getMethod().getReturnType();
                if (java.util.List.class.equals(returnType)) {
                    return ((FinderExecutor)target).executeListFinder(methodInvocation.getMethod(), arguments);
                } else {
                    return ((FinderExecutor)target).executeUniqueFinder(methodInvocation.getMethod(), arguments);
                }
            }
        }
        
        return methodInvocation.proceed();
    }

    public boolean implementsInterface(Class intf) {
        return intf.isInterface() && FinderExecutor.class.isAssignableFrom(intf);
    }
}

