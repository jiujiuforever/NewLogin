package im.jizhu.com.loginmodule.utils;

import com.lesso.log.LogUtil;

/**
 * 日志
 */
public class Logger {

	/**
	 * 只有debug包的时候才会输出日志
	 */
	private static final boolean DEBUG = true;
//	private static final boolean DEBUG = BuildConfig.DEBUG;

	public static final String FILE_PATH = "NewLogin-IM";

	private static final boolean DISK = true;

	private static final int EFFECTIVE_DAY = 5;

	private static final long LOG_FILE_SIZE = 10 * 1024 * 1024;

	private static LogUtil logUtil;

	static {
		logUtil = LogUtil.getInstance();
		setDisk(DISK);
	}

	private Logger() {
	}

	public static void setDisk(boolean isDisk){
		if(isDisk){
			logUtil.diskLog(FILE_PATH,LOG_FILE_SIZE,EFFECTIVE_DAY);
		}else{
			logUtil.unDiskLog();
		}
	}

	/**
	 * log.i
	 */
	public static void i(String tag, String msg) {
		if (DEBUG) {
			logUtil.d(tag, msg);
		}
	}

	/**
	 * Log.i
	 *
	 * @param TAG
	 * @param format
	 * @param args
	 */
	public static void i(String TAG, String format, Object... args) {
		if (DEBUG) {
			logUtil.d(TAG, String.format(format, args));
		}
	}

	/**
	 * log.d
	 */
	public static void d(String tag, String msg) {
		if (DEBUG) {
			logUtil.d(tag, msg);
		}
	}

	/**
	 * Log.d
	 *
	 * @param TAG
	 * @param format
	 * @param args
	 */
	public static void d(String TAG, String format, Object... args) {
		if (DEBUG) {
			logUtil.d(TAG, String.format(format, args));
		}
	}

	/**
	 * log.v
	 */
	public static void v(String tag, String msg) {
		if (DEBUG) {
			logUtil.v(tag, msg);
		}
	}

	/**
	 * Log.v
	 *
	 * @param TAG
	 * @param format
	 * @param args
	 */
	public static void v(String TAG, String format, Object... args) {
		if (DEBUG) {
			logUtil.v(TAG, String.format(format, args));
		}
	}

	/**
	 * log.w
	 */
	public static void w(String tag, String msg) {
		if (DEBUG) {
			logUtil.w(tag, msg);
		}
	}

	/**
	 * Log.w
	 *
	 * @param TAG
	 * @param format
	 * @param args
	 */
	public static void w(String TAG, String format, Object... args) {
		if (DEBUG) {
			logUtil.w(TAG, String.format(format, args));
		}
	}

	/**
	 * log.e
	 */
	public static void e(String tag, String msg) {
		if (DEBUG) {
			logUtil.e(tag, msg);
		}
	}

	/**
	 * Log.e
	 *
	 * @param TAG
	 * @param format
	 * @param args
	 */
	public static void e(String TAG, String format, Object... args) {
		if (DEBUG) {
			logUtil.e(TAG, String.format(format, args));
		}
	}

}
