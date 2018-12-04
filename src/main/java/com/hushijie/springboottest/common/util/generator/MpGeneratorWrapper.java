package com.hushijie.springboottest.common.util.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

/**
 * MpGeneratorWrapper
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/7
 */
public class MpGeneratorWrapper {

    /**
     * controller 层标记
     */
    public static final String FLAG_CONTROLLER = "controller";

    /**
     * service 层标记
     */
    public static final String FLAG_SERVICE = "service";

    /**
     * dao 层标记
     */
    public static final String FLAG_DAO = "dao";

    /**
     * 创建人
     */
    private String author;

    /**
     * 所有生成文件的根目录
     * <p>
     * 建议不要和工程目录相同，避免代码覆盖
     * </p>
     */
    private String allFileRootPath;

    /**
     * 要创建代码的模块名
     */
    private String moduleName;

    /**
     * 所有代码的基础包名
     * <p>
     * 要创建模块所在的父包名，会先在此包名下创建模块的包名，再模块下创建各分层代码所在的子包名
     * </p>
     */
    private String basePackage;

    /**
     * 数据库 url
     */
    private String url;

    /**
     * 数据库驱动名
     */
    private String driverName;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 要生成代码对映的表名
     */
    private String[] tables;

    /**
     * table前缀
     * <p>
     * 生成的实体类名将自动忽略指定的前缀信息
     * </p>
     */
    private String[] tablePrefixArray;

    /**
     * column前缀
     * <p>
     * 生成的实体类的属性名将自动忽略指定的前缀信息
     * </p>
     */
    private String[] columnPrefixArray;

    /**
     * java源码在项目下的相对路径
     */
    private final String javaDir = "/src/main/java";

    /**
     * resources资源文件在项目下的相对路径
     */
    private final String resourcesDir = "/src/main/resources";

    /**
     * java源码在项目指定模块下的相对路径
     */
    private String javaModuleDir;

    /**
     * 生成文件的根目录
     */
    private String path;

    /**
     * 数据源配置，通过该配置，指定需要生成代码的具体数据库
     */
    private DataSourceConfig dataSourceConfig;

    /**
     * 数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
     */
    private StrategyConfig strategyConfig;

    /**
     * 自定义的模版引擎
     */
    private MyVelocityTemplateEngine templateEngine;

    /**
     * 是否需要重新加载属性
     */
    private boolean isReload;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static MpGeneratorWrapper of() {
        return new MpGeneratorWrapper();
    }

    public static MpGeneratorWrapper of(String url, String driverName, String username, String password) {
        return new MpGeneratorWrapper(url, driverName, username, password);
    }

    public static MpGeneratorWrapper of(
            String author, String allFileRootPath, String moduleName, String basePackage,
            String url, String driverName, String username, String password, String[] tables,
            String[] tablePrefixArray, String[] columnPrefixArray) {
        return new MpGeneratorWrapper(
                author, allFileRootPath, moduleName, basePackage,
                url, driverName, username, password, tables,
                tablePrefixArray, columnPrefixArray);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 生成Controller层代码
     */
    public void generateController() {
        execute(new AutoGenerator(), FLAG_CONTROLLER);
    }

    /**
     * 生成Controller层代码
     */
    public void generateService() {
        execute(new AutoGenerator(), FLAG_SERVICE);
    }

    /**
     * 生成Controller层代码
     */
    public void generateDao() {
        execute(new AutoGenerator(), FLAG_DAO);
    }

    /**
     * 执行代码生成器
     *
     * @param mpg           代码生成器
     * @param generatorFlag 生成器标记
     */
    public void execute(AutoGenerator mpg, String generatorFlag) {
        if (null != mpg) {
            // 校验参数
            validate();
            // 设置代码生成器
            setGlobalConfig(mpg, generatorFlag);
            setPackageConfig(mpg, generatorFlag);
            setInjectionConfig(mpg, generatorFlag);
            setTemplateConfig(mpg, generatorFlag);
            setDBConfig(mpg);
            mpg.setTemplateEngine(templateEngine);
            // 生成代码
            mpg.execute();
        }
    }

    /**
     * 校验参数
     */
    private void validate() {
        if (null == this.author || "".equals(this.author)) {
            throw new NoSuchElementException("no such author");
        }
        if (null == this.allFileRootPath || "".equals(this.allFileRootPath)) {
            throw new NoSuchElementException("no such allFileRootPath");
        }
        if (null == this.url || "".equals(this.url)) {
            throw new NoSuchElementException("no such url");
        }
        if (null == this.driverName || "".equals(this.driverName)) {
            throw new NoSuchElementException("no such driverName");
        }
        this.username = Optional.ofNullable(this.username).orElse("");
        this.password = Optional.ofNullable(this.password).orElse("");
        if (null == this.tables || this.tables.length <= 0) {
            throw new NoSuchElementException("no such tables");
        }
        if (this.isReload) {
            reload();
        }
    }

    /**
     * 重新加载属性
     */
    private void reload() {
        this.path = new File(this.allFileRootPath).getAbsolutePath();
        this.javaModuleDir = this.javaDir
                + File.separator
                + Optional.ofNullable(this.basePackage)
                .map(n -> n.replaceAll("\\.", File.separator)).orElse("")
                + File.separator
                + Optional.ofNullable(this.moduleName).orElse("");
        this.dataSourceConfig = getDataSourceConfig();
        this.strategyConfig = getStrategyConfig();
        this.isReload = false;
    }

    /**
     * 设置全局策略配置
     *
     * @param mpg           代码生成器
     * @param generatorFlag 生成器标记
     */
    private void setGlobalConfig(AutoGenerator mpg, String generatorFlag) {
        GlobalConfig gc = new GlobalConfig();
        //生成文件的输出目录-[默认值：D 盘根目录]
        gc.setOutputDir(path + javaDir);
        //是否覆盖已有文件-[默认值：false]
        gc.setFileOverride(true);
        //是否打开输出目录-[默认值：true]
        gc.setOpen(true);
        //是否在xml中添加二级缓存配置-[默认值：false]
        gc.setEnableCache(false);
        //开发人员-[默认值：null]
        gc.setAuthor(author);
        //开启 Kotlin 模式-[默认值：false]
        gc.setKotlin(false);
        //开启 ActiveRecord 模式-[默认值：false]
        gc.setActiveRecord(false);
        //开启 BaseResultMap-[默认值：false]
        gc.setBaseResultMap(true);
        //开启 baseColumnList-[默认值：false]
        gc.setBaseColumnList(true);
        //指定生成的主键的ID类型-[默认值：null]
        gc.setIdType(IdType.AUTO);
        //命名方式
        if (FLAG_DAO.equals(generatorFlag)) {
            //实体命名方式-[默认值：null 例如：%sDO 生成 UserDO]
            gc.setEntityName("%sDO");
            //mapper 命名方式-[默认值：null 例如：%sMapper 生成 UserMapper]
            gc.setMapperName("%sMapper");
            //mapper xml 命名方式-[默认值：null 例如：%sMapper 生成 UserMapper.xml]
            gc.setXmlName("%sMapper");
            //service 命名方式-[默认值：null 例如：%sService 生成 UserService]
            gc.setServiceName("%sInnerService");
            //service impl 命名方式-[默认值：null 例如：%sServiceImpl 生成 UserServiceImpl]
            gc.setServiceImplName("%sInnerServiceImpl");
        } else if (FLAG_SERVICE.equals(generatorFlag)) {
            //service 命名方式-[默认值：null 例如：%sService 生成 UserService]
            gc.setServiceName("%sService");
            //service impl 命名方式-[默认值：null 例如：%sServiceImpl 生成 UserServiceImpl]
            gc.setServiceImplName("%sServiceImpl");
        } else if (FLAG_CONTROLLER.equals(generatorFlag)) {
            //controller 命名方式-[默认值：null 例如：%sController 生成 UserController]
            gc.setControllerName("%sController");
        }
        mpg.setGlobalConfig(gc);
    }

    /**
     * 包名配置
     * <p>
     * 通过该配置，指定生成代码的包路径
     * </p>
     *
     * @param mpg           代码生成器
     * @param generatorFlag 生成器标记
     */
    private void setPackageConfig(AutoGenerator mpg, String generatorFlag) {
        PackageConfig pc = new PackageConfig()
                //父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
                .setParent(basePackage)
                //父包模块名
                .setModuleName(moduleName);
        if (FLAG_DAO.equals(generatorFlag)) {
            //Entity包名
            pc.setEntity("model");
            //Mapper包名
            pc.setMapper("mapper");
            //Mapper XML包名
            pc.setXml(null);
            //Service包名
            pc.setService("innerservice");
            //Service impl包名
            pc.setServiceImpl("innerservice.impl");
            //Controller包名
            pc.setController(null);
        } else if (FLAG_SERVICE.equals(generatorFlag)) {
            pc.setEntity(null);
            pc.setMapper(null);
            pc.setXml(null);
            //Service包名
            pc.setService("service");
            //Service impl包名
            pc.setServiceImpl("service.impl");
            pc.setController(null);
        } else if (FLAG_CONTROLLER.equals(generatorFlag)) {
            pc.setEntity(null);
            pc.setMapper(null);
            pc.setXml(null);
            pc.setService(null);
            pc.setServiceImpl(null);
            //Controller包名
            pc.setController("controller");
        }
        mpg.setPackageInfo(pc);
    }

    /**
     * 注入配置
     * <p>
     * 通过该配置，可注入自定义参数等操作以实现个性化操作
     * </p>
     *
     * @param mpg           代码生成器
     * @param generatorFlag 生成器标记
     */
    private void setInjectionConfig(AutoGenerator mpg, String generatorFlag) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("author", this.getConfig().getGlobalConfig().getAuthor());
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();

        if (FLAG_DAO.equals(generatorFlag)) {
            focList.add(new FileOutConfig("/mybatis-plus/mapper.xml.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return path + resourcesDir + File.separator
                            + "mybatis" + File.separator + "mapper" + File.separator
                            + Optional.ofNullable(moduleName).orElse("") + File.separator
                            + tableInfo.getXmlName() + ".xml";
                }
            });
        } else if (FLAG_SERVICE.equals(generatorFlag)) {
//            focList.add(new FileOutConfig("/mybatis-plus/entity.java.vm") {
//                @Override
//                public String outputFile(TableInfo tableInfo) {
//                    // 自定义输入文件名称
//                    return path + javaModuleDir + "/model/"
//                            + tableInfo.getEntityName() + "DO.java";
//                }
//            });
        } else if (FLAG_CONTROLLER.equals(generatorFlag)) {
            //
        }

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }

    /**
     * 模版文件配置
     *
     * @param mpg           代码生成器
     * @param generatorFlag 生成器标记
     */
    private void setTemplateConfig(AutoGenerator mpg, String generatorFlag) {
        TemplateConfig tc = new TemplateConfig();
        if (FLAG_DAO.equals(generatorFlag)) {
            //Java 实体类模板
            tc.setEntity("/mybatis-plus/entity.java");
            //mapper 模板
            tc.setMapper("/mybatis-plus/mapper.java");
            //mapper xml 模板
            tc.setXml(null);
            //Service 类模板
            tc.setService("/mybatis-plus/innerService.java");
            //Service impl类模板
            tc.setServiceImpl("/mybatis-plus/innerServiceImpl.java");
            //controller 控制器模板
            tc.setController(null);
        } else if (FLAG_SERVICE.equals(generatorFlag)) {
            tc.setEntity(null);
            tc.setMapper(null);
            tc.setXml(null);
            //Service 类模板
            tc.setService("/mybatis-plus/service.java");
            //Service impl类模板
            tc.setServiceImpl("/mybatis-plus/serviceImpl.java");
            tc.setController(null);
        } else if (FLAG_CONTROLLER.equals(generatorFlag)) {
            tc.setEntity(null);
            tc.setMapper(null);
            tc.setXml(null);
            tc.setService(null);
            tc.setServiceImpl(null);
            //controller 控制器模板
            tc.setController("/mybatis-plus/controller.java");
        }
        mpg.setTemplate(tc);
    }

    /**
     * 设置数据库
     *
     * @param mpg 代码生成器
     */
    private void setDBConfig(AutoGenerator mpg) {
        mpg.setDataSource(this.dataSourceConfig);
        mpg.setStrategy(this.strategyConfig);
    }

    /**
     * 数据源配置
     * <p>
     * 通过该配置，指定需要生成代码的具体数据库
     * </p>
     *
     * @return DataSourceConfig
     */
    private DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig()
                //数据库类型-[该类内置了常用的数据库类型【必须】]
                .setDbType(DbType.MYSQL)
                //类型转换-[默认由 dbType 类型决定选择对应数据库内置实现]
                .setTypeConvert(new MySqlTypeConvert())
                //驱动连接的URL
                .setUrl(this.url)
                //驱动名称
                .setDriverName(this.driverName)
                //数据库连接用户名
                .setUsername(this.username)
                //数据库连接密码
                .setPassword(this.password);
    }

    /**
     * 数据库表配置
     * <p>
     * 通过该配置，可指定需要生成哪些表或者排除哪些表
     * </p>
     *
     * @return StrategyConfig
     */
    private StrategyConfig getStrategyConfig() {
        //自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));

        // 数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
        return new StrategyConfig()
                //是否大写命名
                .setCapitalMode(false)
                //是否跳过视图
                .setSkipView(true)
                //数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
                .setColumnNaming(NamingStrategy.underline_to_camel)
                //表前缀
                .setTablePrefix(this.tablePrefixArray)
                //字段前缀
                .setFieldPrefix(this.columnPrefixArray)
                //自定义继承的Entity类全称，带包名
                .setSuperEntityClass(Optional.ofNullable(this.basePackage)
                        .map(n -> n + ".common.base.entity.BaseEntity")
                        .orElse(null))
                //自定义基础的Entity类，公共字段
                .setSuperEntityColumns("id", "gmt_create", "gmt_modified")
                //自定义继承的Mapper类全称，带包名
                .setSuperMapperClass(null)
                //自定义继承的Service类全称，带包名
                .setSuperServiceClass(null)
                //自定义继承的ServiceImpl类全称，带包名
                .setSuperServiceImplClass(null)
                //自定义继承的Controller类全称，带包名
                .setSuperControllerClass(null)
                //需要包含的表名，允许正则表达式（与exclude二选一配置）
                .setInclude(tables)
                //【实体】是否生成字段常量（默认 false）
                .setEntityColumnConstant(false)
                //【实体】是否为构建者模型（默认 false）
                .setEntityBuilderModel(true)
                //【实体】是否为lombok模型（默认 false）
                .setEntityLombokModel(false)
                //Boolean类型字段是否移除is前缀（默认 false）
                .setEntityBooleanColumnRemoveIsPrefix(true)
                //生成 @RestController 控制器
                .setRestControllerStyle(true)
                //驼峰转连字符
                .setControllerMappingHyphenStyle(false)
                //是否生成实体时，生成字段注解
                .entityTableFieldAnnotationEnable(true)
                //乐观锁属性名称
                .setVersionFieldName(null)
                //逻辑删除属性名称
                .setLogicDeleteFieldName("is_deleted")
                //表填充字段
                .setTableFillList(tableFillList);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * MyVelocityTemplateEngine
     * <p>
     * 自定义 Velocity 模版引擎，如果 resource 目录下有模版文件会加载此模版引擎
     * </p>
     *
     * @author wubin email: wubin@hushijie.com.cn
     * @date 2018/11/31
     */
    private class MyVelocityTemplateEngine extends AbstractTemplateEngine {
        private static final String DOT_VM = ".vm";
        private String templateFileResourcePath = "";
        private VelocityEngine velocityEngine;

        public MyVelocityTemplateEngine() {
        }

        public void setTemplateFileResourcePath(String templateFileResourcePath) {
            this.templateFileResourcePath = templateFileResourcePath;
            if (null != this.velocityEngine) {
                this.velocityEngine.setProperty("file.resource.loader.path", templateFileResourcePath);
            }
        }

        @Override
        public MyVelocityTemplateEngine init(ConfigBuilder configBuilder) {
            super.init(configBuilder);
            if (null == this.velocityEngine) {
                Properties p = new Properties();
                p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
                p.setProperty("file.resource.loader.path", templateFileResourcePath);
                p.setProperty("UTF-8", ConstVal.UTF8);
                p.setProperty("input.encoding", ConstVal.UTF8);
                p.setProperty("file.resource.loader.unicode", "true");
                this.velocityEngine = new VelocityEngine(p);
            }
            return this;
        }

        @Override
        public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
            if (!StringUtils.isEmpty(templatePath)) {
                Template template = this.velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
                template.merge(new VelocityContext(objectMap), writer);
                writer.close();
                logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
            }
        }

        @Override
        public String templateFilePath(String filePath) {
            if (null != filePath && !filePath.contains(DOT_VM)) {
                return filePath + DOT_VM;
            } else {
                return filePath;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private MpGeneratorWrapper() {
        templateEngine = new MyVelocityTemplateEngine();
        templateEngine.setTemplateFileResourcePath(
                Object.class.getResource("/templates").getPath()
        );
        isReload = true;
    }

    private MpGeneratorWrapper(String url, String driverName, String username, String password) {
        this();
        this.url = url;
        this.driverName = driverName;
        this.username = username;
        this.password = password;
    }

    private MpGeneratorWrapper(
            String author, String allFileRootPath, String moduleName, String basePackage,
            String url, String driverName, String username, String password, String[] tables,
            String[] tablePrefixArray, String[] columnPrefixArray) {
        this(url, driverName, username, password);
        this.author = author;
        this.allFileRootPath = allFileRootPath;
        this.moduleName = moduleName;
        this.basePackage = basePackage;
        this.tables = tables;
        this.tablePrefixArray = tablePrefixArray;
        this.columnPrefixArray = columnPrefixArray;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getAuthor() {
        return author;
    }

    public MpGeneratorWrapper setAuthor(String author) {
        this.author = author;
        this.isReload = true;
        return this;
    }

    public String getAllFileRootPath() {
        return allFileRootPath;
    }

    public MpGeneratorWrapper setAllFileRootPath(String allFileRootPath) {
        this.allFileRootPath = allFileRootPath;
        this.isReload = true;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public MpGeneratorWrapper setModuleName(String moduleName) {
        this.moduleName = moduleName;
        this.isReload = true;
        return this;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public MpGeneratorWrapper setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        this.isReload = true;
        return this;
    }

    public String[] getTables() {
        return tables;
    }

    public MpGeneratorWrapper setTables(String[] tables) {
        this.tables = tables;
        this.isReload = true;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MpGeneratorWrapper setUrl(String url) {
        this.url = url;
        this.isReload = true;
        return this;
    }

    public String getDriverName() {
        return driverName;
    }

    public MpGeneratorWrapper setDriverName(String driverName) {
        this.driverName = driverName;
        this.isReload = true;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MpGeneratorWrapper setUsername(String username) {
        this.username = username;
        this.isReload = true;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MpGeneratorWrapper setPassword(String password) {
        this.password = password;
        this.isReload = true;
        return this;
    }

    public String[] getTablePrefixArray() {
        return tablePrefixArray;
    }

    public MpGeneratorWrapper setTablePrefixArray(String[] tablePrefixArray) {
        this.tablePrefixArray = tablePrefixArray;
        this.isReload = true;
        return this;
    }

    public String[] getColumnPrefixArray() {
        return columnPrefixArray;
    }

    public MpGeneratorWrapper setColumnPrefixArray(String[] columnPrefixArray) {
        this.columnPrefixArray = columnPrefixArray;
        this.isReload = true;
        return this;
    }
}
