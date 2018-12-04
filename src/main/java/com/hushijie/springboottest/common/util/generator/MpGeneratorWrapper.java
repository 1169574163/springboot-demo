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
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * MpGeneratorWrapper
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/7
 */
public class MpGeneratorWrapper {

    public static enum MpCodeTypeEnum {
        /**
         * controller 层代码生成器
         */
        CONTROLLER,

        /**
         * service 层代码生成器
         */
        SERVICE,

        /**
         * repository 层代码生成器
         */
        REPOSITORY,

        /**
         * dao 层代码生成器
         */
        DAO
    }

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
        execute(MpGeneratorWrapper.MpCodeTypeEnum.CONTROLLER);
    }

    /**
     * 生成Controller层代码
     */
    public void generateService() {
        execute(MpGeneratorWrapper.MpCodeTypeEnum.SERVICE);
    }

    /**
     * 生成Controller层代码
     */
    public void generateDao() {
        execute(MpGeneratorWrapper.MpCodeTypeEnum.DAO);
    }

    /**
     * 执行代码生成器
     *
     * @param codeTypeEnums 代码类型数组
     */
    public void execute(MpCodeTypeEnum... codeTypeEnums) {
        if (null != codeTypeEnums) {
            // 校验参数
            validate();
            List<AutoGenerator> generators = new ArrayList<>(codeTypeEnums.length);
            new AutoGenerator();
            for (MpCodeTypeEnum codeTypeEnum : codeTypeEnums) {
                if (null != codeTypeEnum) {
                    // 设置代码生成器
                    AutoGenerator mpg = new AutoGenerator();
                    mpg.setDataSource(dataSourceConfig);
                    mpg.setTemplateEngine(templateEngine);
                    setGlobalConfig(mpg, codeTypeEnum);
                    setPackageConfig(mpg, codeTypeEnum);
                    setInjectionConfig(mpg, codeTypeEnum);
                    setTemplateConfig(mpg, codeTypeEnum);
                    setStrategyConfig(mpg, codeTypeEnum);
                    generators.add(mpg);
                }
            }
            for (AutoGenerator generator : generators) {
                generator.execute();
            }
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
        this.isReload = false;
    }

    /**
     * 设置全局策略配置
     *
     * @param mpg          代码生成器
     * @param codeTypeEnum 代码类型
     */
    private void setGlobalConfig(AutoGenerator mpg, MpCodeTypeEnum codeTypeEnum) {
        GlobalConfig gc = new GlobalConfig()
                .setFileOverride(true)
                .setOpen(true)
                .setEnableCache(false)
                .setKotlin(false)
                .setActiveRecord(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setIdType(IdType.AUTO);
        //生成文件的输出目录-[默认值：D 盘根目录]
        gc.setOutputDir(path + javaDir);
        gc.setAuthor(author);
        //命名方式
        gc.setEntityName("%sDO");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setControllerName("%sController");
        if (Objects.equals(MpCodeTypeEnum.REPOSITORY, codeTypeEnum)) {
            gc.setServiceName("%sRepository");
            gc.setServiceImplName("%sRepositoryImpl");
        }
        if (Objects.equals(MpCodeTypeEnum.SERVICE, codeTypeEnum)) {
            gc.setServiceName("%sService");
            gc.setServiceImplName("%sServiceImpl");
        }
        mpg.setGlobalConfig(gc);
    }

    /**
     * 包名配置
     * <p>
     * 通过该配置，指定生成代码的包路径
     * </p>
     *
     * @param mpg          代码生成器
     * @param codeTypeEnum 代码类型
     */
    private void setPackageConfig(AutoGenerator mpg, MpCodeTypeEnum codeTypeEnum) {
        PackageConfig pc = new PackageConfig()
                .setParent(basePackage)
                .setModuleName(moduleName)
                .setEntity("model")
                .setMapper("mapper")
                .setXml(null)
                .setService(null)
                .setServiceImpl(null)
                .setController("controller");
        if (Objects.equals(MpCodeTypeEnum.REPOSITORY, codeTypeEnum)) {
            pc.setService("repository");
            pc.setServiceImpl("repository.impl");
        }
        if (Objects.equals(MpCodeTypeEnum.SERVICE, codeTypeEnum)) {
            pc.setService("service");
            pc.setServiceImpl("service.impl");
        }
        mpg.setPackageInfo(pc);
    }

    /**
     * 注入配置
     * <p>
     * 通过该配置，可注入自定义参数等操作以实现个性化操作
     * </p>
     *
     * @param mpg          代码生成器
     * @param codeTypeEnum 代码类型
     */
    private void setInjectionConfig(AutoGenerator mpg, MpCodeTypeEnum codeTypeEnum) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("author", this.getConfig().getGlobalConfig().getAuthor());
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();

        if (Objects.equals(MpCodeTypeEnum.DAO, codeTypeEnum)) {
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
        }
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }

    /**
     * 模版文件配置
     *
     * @param mpg          代码生成器
     * @param codeTypeEnum 代码类型
     */
    private void setTemplateConfig(AutoGenerator mpg, MpCodeTypeEnum codeTypeEnum) {
        TemplateConfig tc = new TemplateConfig()
                .setEntity(null)
                .setMapper(null)
                .setXml(null)
                .setService(null)
                .setServiceImpl(null)
                .setController(null);
        if (Objects.equals(MpCodeTypeEnum.DAO, codeTypeEnum)) {
            tc.setEntity("/mybatis-plus/entity.java.vm");
            tc.setMapper("/mybatis-plus/mapper.java.vm");
        }
        if (Objects.equals(MpCodeTypeEnum.REPOSITORY, codeTypeEnum)) {
            tc.setService("/mybatis-plus/repository.java.vm");
            tc.setServiceImpl("/mybatis-plus/repositoryImpl.java.vm");
        }
        if (Objects.equals(MpCodeTypeEnum.SERVICE, codeTypeEnum)) {
            tc.setService("/mybatis-plus/service.java.vm");
            tc.setServiceImpl("/mybatis-plus/serviceImpl.java.vm");
        }
        if (Objects.equals(MpCodeTypeEnum.CONTROLLER, codeTypeEnum)) {
            tc.setController("/mybatis-plus/controller.java.vm");
        }
        mpg.setTemplate(tc);
    }

    /**
     * 数据库表配置
     *
     * @param mpg          代码生成器
     * @param codeTypeEnum 代码类型
     */
    private void setStrategyConfig(AutoGenerator mpg, MpCodeTypeEnum codeTypeEnum) {
        StrategyConfig sc = getStrategyConfig();
        if (Objects.equals(MpCodeTypeEnum.DAO, codeTypeEnum)) {
            //自定义继承的Entity类全称，带包名
            sc.setSuperEntityClass(Optional.ofNullable(this.basePackage)
                    .map(n -> n + ".common.base.entity.BaseEntity")
                    .orElse(null));
            //自定义基础的Entity类，公共字段
            sc.setSuperEntityColumns("id", "gmt_create", "gmt_modified");
            //自定义继承的Mapper类全称，带包名
            sc.setSuperMapperClass(null);
        }
        if (Objects.equals(MpCodeTypeEnum.REPOSITORY, codeTypeEnum)) {
            //自定义继承的Repository类全称，带包名
            sc.setSuperServiceClass(Optional.ofNullable(this.basePackage)
                    .map(n -> n + ".common.base.repository.BaseRepository")
                    .orElse(null));
            //自定义继承的RepositoryImpl类全称，带包名
            sc.setSuperServiceImplClass(Optional.ofNullable(this.basePackage)
                    .map(n -> n + ".common.base.repository.BaseRepositoryImpl")
                    .orElse(null));
        }
        if (Objects.equals(MpCodeTypeEnum.SERVICE, codeTypeEnum)) {
            //自定义继承的Service类全称，带包名
            sc.setSuperServiceClass(null);
            //自定义继承的ServiceImpl类全称，带包名
            sc.setSuperServiceImplClass(null);
        }
        if (Objects.equals(MpCodeTypeEnum.CONTROLLER, codeTypeEnum)) {
            //自定义继承的Controller类全称，带包名
            sc.setSuperControllerClass(null);
        }
        mpg.setStrategy(sc);
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
                .setSuperEntityClass(null)
                //自定义基础的Entity类，公共字段
                .setSuperEntityColumns((String[]) null)
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
