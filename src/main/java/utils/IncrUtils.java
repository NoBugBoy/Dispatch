package utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class IncrUtils {
    protected static  AtomicInteger ATOMIC_INTEGER ;
    private IncrUtils(){};
    public static String incr(){
        if(Objects.isNull(ATOMIC_INTEGER)){
            synchronized (AtomicInteger.class){
                if(Objects.isNull(ATOMIC_INTEGER)){
                    ATOMIC_INTEGER = new AtomicInteger(0);;
                }
            }
        }
        int i = ATOMIC_INTEGER.incrementAndGet();
        return "#" + i;


    }

    public static void main(String[] args) {
        System.out.println(incr());
    }

}
