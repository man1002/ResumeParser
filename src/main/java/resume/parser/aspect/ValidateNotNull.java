package resume.parser.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import resume.parser.exception.StringParseException;

import java.lang.reflect.Method;

/**
 * Вызывает исключение StringParseException, если возвращаемое методом значения null.
 */
@Aspect
public class ValidateNotNull {
    @AfterReturning(pointcut="execution(* *(..)) && @annotation(resume.parser.annotation.ValidateNotNull)", returning="returnObject")
    public void logAfter(JoinPoint joinPoint, Object returnObject) throws StringParseException {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        String requiredFieldName = method.getAnnotation(resume.parser.annotation.ValidateNotNull.class).validateFieldName();
        if(returnObject == null) {
            throw new StringParseException("Ошибка в обработке строки содержащей " + requiredFieldName);
        }
    }

}
