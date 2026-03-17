package za.ac.cput.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Helper {
    public static boolean isEmptyOrNull(String str){
        if(str==null || str.isEmpty()){
            return true;
        }
            return false;
    }

    public static boolean isNumNeg(float num){
        if(num <0){
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email){
        EmailValidator vaildator = EmailValidator.getInstance();
        return vaildator.isValid(email);
    }

    public static <T> boolean isValidType(T t){
        if(t==null){
            return true;
        }
        return false;
    }
}
