package tool.util;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @className: LogUtil
 * @author: Lying
 * @description: TODO
 * @date: 2022/11/17 下午5:10
 */
@SuppressWarnings("unused")
public class LogUtil implements Serializable {

    private static final long serialVersionUID = 3497418501502482083L;

    private static Logger logger;

    public LogUtil() {
        logger = (Logger) LoggerFactory.getLogger(LogUtil.class);
    }

    public LogUtil(Class<?> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
    }

    public <T> void var(String name, T value) {
        logger.trace(String.format("var->%s=%s", name, value));
    }

}
