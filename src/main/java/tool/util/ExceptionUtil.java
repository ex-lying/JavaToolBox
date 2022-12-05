package tool.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @className: ExceptionUtil
 * @author: Lying
 * @description: TODO
 * @date: 2022/11/17 下午5:03
 */
@SuppressWarnings("unused")
public class ExceptionUtil extends Exception{
    private static final long serialVersionUID = 8296677497340313653L;

    public ExceptionUtil(String s) {
        super(s);

        LogUtil log = new LogUtil();
        log.error("ThclException Message: ".concat(s));
    }

    public static String StackTracetoString(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
