package tool.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: LogUtil
 * @author: Lying
 * @description: 1.The current implementation is relatively rough and needs to be optimized in the future
 * 2.Implemented base on logback
 * @date: 2022/11/17 下午5:10
 */
@SuppressWarnings("unused")
public class LogUtil implements Serializable {
    private static final long serialVersionUID = 3497418501502482083L;

    private static Logger logger;

    private static final Map<String, Boolean> keyMap = new HashMap<>();

    private static final Map<String, Logger> keyLogger = new HashMap<>();

    public LogUtil() {
        logger = (Logger) LoggerFactory.getLogger(LogUtil.class);
    }

    public LogUtil(Class<?> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
    }

    public Logger setLogger(String key) {
        LoggerContext context = logger.getLoggerContext();

        //创建一个子logger
        Logger nowLogger = context.getLogger(key);
        //向root提交
        nowLogger.setAdditive(true);

        String logPath = context.getProperty("logPath");

        if (logPath == null || logPath.isEmpty()) {
            logger.error("请配置基础日志配置信息");
        }

        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName(key);
        appender.setFile(OptionHelper.substVars(logPath + "/" + key + ".log", context));
        appender.setAppend(true);
        appender.setPrudent(false);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy<>();

        policy.setFileNamePattern(OptionHelper.substVars(logPath + "/" + key + ".%d{yyyy-MM-dd}.%i.log.zip", context));
        //日志活动文件大小，超过就做切割并压缩
        String maxFileSize = context.getProperty("maxFileSize");
        policy.setMaxFileSize(maxFileSize == null || maxFileSize.isEmpty() ? FileSize.valueOf("10MB") : FileSize.valueOf(maxFileSize + "MB"));
        //日志保留天数
        String maxHistory = context.getProperty("maxHistory");
        policy.setMaxHistory(maxHistory == null || maxHistory.isEmpty() ? 30 : Integer.parseInt(maxHistory));
        //压缩文件总大小
        String totalSizeCap = context.getProperty("totalSizeCap");
        policy.setTotalSizeCap(totalSizeCap == null || totalSizeCap.isEmpty() ? FileSize.valueOf("32GB") : FileSize.valueOf(totalSizeCap + "GB"));
        policy.setParent(appender);
        policy.setContext(context);
        policy.start();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        String pattern = context.getProperty("pattern");
        encoder.setPattern(pattern == null || pattern.isEmpty() ? "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5thread %-5level %logger --- %msg%n" : pattern);
        encoder.start();

        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.setAppend(true);
        appender.start();
        nowLogger.addAppender(appender);

        String level = context.getProperty("level");
        if (level == null || level.isEmpty()) {
            nowLogger.setLevel(Level.TRACE);
        } else if (level.equalsIgnoreCase("TRACE")) {
            nowLogger.setLevel(Level.TRACE);
        } else if (level.equalsIgnoreCase("ALL")) {
            nowLogger.setLevel(Level.ALL);
        } else if (level.equalsIgnoreCase("DEBUG")) {
            nowLogger.setLevel(Level.DEBUG);
        } else if (level.equalsIgnoreCase("ERROR")) {
            nowLogger.setLevel(Level.ERROR);
        } else if (level.equalsIgnoreCase("INFO")) {
            nowLogger.setLevel(Level.INFO);
        } else if (level.equalsIgnoreCase("WARN")) {
            nowLogger.setLevel(Level.WARN);
        } else if (level.equalsIgnoreCase("OFF")) {
            nowLogger.setLevel(Level.OFF);
        } else {
            nowLogger.setLevel(Level.TRACE);
        }

        keyLogger.put(key, nowLogger);

        return nowLogger;
    }

    public Logger handleKey(String key) {
        Logger nowLogger = keyLogger.get(key);

        if (nowLogger == null) {
            nowLogger = setLogger(key);
            if (nowLogger == null) {
                nowLogger = logger;
            }
        }

        return nowLogger;
    }

    public <T> void var(String name, T value) {
        logger.trace(String.format("var->%s=%s", name, value));
    }

    public <T> void var(String name, T value, String key) {
        handleKey(key).trace(String.format("var->%s=%s", name, value));
    }

    public <T> void input(String name, T value) {
        logger.trace(String.format("input->%s=%s", name, value));
    }

    public <T> void input(String name, T value, String key) {
        handleKey(key).trace(String.format("input->%s=%s", name, value));
    }

    public <T> void output(String name, T value) {
        logger.trace(String.format("output->%s=%s", name, value));
    }

    public <T> void output(String name, T value, String key) {
        handleKey(key).trace(String.format("output->%s=%s", name, value));
    }

    public void error(String s) {
        logger.error(s);
    }

    public void error(String s, String key) {
        handleKey(key).error(s);
    }

    public void error(Exception e) {
        logger.error(ExceptionUtil.StackTracetoString(e));
    }

    public void error(Exception e, String key) {
        handleKey(key).error(ExceptionUtil.StackTracetoString(e));
    }

    public void error2(String message, Exception e) {
        logger.error(message + ExceptionUtil.StackTracetoString(e));
    }

    public void error2(String message, Exception e, String key) {
        handleKey(key).error(message + ExceptionUtil.StackTracetoString(e));
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void warn(String s, String key) {
        handleKey(key).warn(s);
    }

    public void info(String s) {
        logger.info(s);
    }

    public void info(String s, String key) {
        handleKey(key).info(s);
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void debug(String s, String key) {
        handleKey(key).debug(s);
    }

    public void trace(String s) {
        logger.trace(s);
    }

    public void trace(String s, String key) {
        handleKey(key).trace(s);
    }
}
