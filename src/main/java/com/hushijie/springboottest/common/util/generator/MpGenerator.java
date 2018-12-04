package com.hushijie.springboottest.common.util.generator;

/**
 * MpGenerator
 * <p>
 * 使用前先将所有属性值改为自己实际需要的参数值然后在执行 main 方法
 * </p>
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/30
 */
public class MpGenerator {
    /**
     * 创建人
     */
    private static final String AUTHOR = "wubin";

    /**
     * 生成文件的根目录
     */
    private static final String ALL_FILE_ROOT_PATH = "/Users/wubin/IdeaProjects/springboottest-demo";

    /**
     * 模块名
     */
    private static final String MODULE_NAME = "hctest12";

    /**
     * 所有代码的基础报名
     */
    private static final String BASE_PACKAGE = "com.hushijie.springboottest";

    /**
     * 数据库 URL
     */
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/hc_test"
            + "?useUnicode=true"
            + "&characterEncoding=UTF-8"
            + "&zeroDateTimeBehavior=convertToNull"
            + "&useSSL=false";

    /**
     * 数据库驱动名
     */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * 数据库用户名
     */
    private static final String USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "12345678";

    /**
     * 要生成代码对映的表名
     */
    private static final String[] TABLES = {"hc_test"};

    /**
     * table前缀（生成的类将去掉前缀）
     */
    private static final String[] TABLE_PREFIX_ARRAY = {"hc_"};

    /**
     * column前缀（生成的类将去掉前缀）
     */
    private static final String[] COLUMN_PREFIX_ARRAY = {"is_"};

    /**
     * 代码生成器的封装对象
     */
    private static MpGeneratorWrapper mpGeneratorWrapper;

    static {
        mpGeneratorWrapper = MpGeneratorWrapper.of(
                AUTHOR, ALL_FILE_ROOT_PATH, MODULE_NAME, BASE_PACKAGE,
                URL, DRIVER_NAME, USERNAME, PASSWORD, TABLES,
                TABLE_PREFIX_ARRAY, COLUMN_PREFIX_ARRAY);
    }

    public static void main(String[] args) {
        mpGeneratorWrapper.generateController();
        mpGeneratorWrapper.generateService();
        mpGeneratorWrapper.generateDao();
    }
}
