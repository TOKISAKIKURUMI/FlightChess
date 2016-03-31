package flightchess.stsm.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("flightchess.stsm")
@PropertySource("classpath:flightchess.properties")
public class ServletConfig extends WebMvcConfigurerAdapter {
	@Autowired
    private ApplicationContext appContext;
	
	private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
 
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_HIBERNATE_HBMDDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
 
    @Resource
    private Environment env;
    
	 @Bean
	 public ResourceBundleMessageSource messageSource() {
	        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
	        resourceBundleMessageSource.setBasename("flightchess");
	        return resourceBundleMessageSource;
	 }
	 
	 @Bean
	 public SpringResourceTemplateResolver templateResolver(){
	        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	        templateResolver.setApplicationContext(this.appContext);
	        templateResolver.setPrefix("/WEB-INF/templates/");
	        templateResolver.setSuffix(".html");
	        templateResolver.setTemplateMode("HTML5");
	        // Template cache is true by default. Set to false if you want
	        // templates to be automatically updated when modified.
	        templateResolver.setCacheable(true);
	        return templateResolver;
	 }
	 
	 @Bean
	 public SpringTemplateEngine templateEngine(){
	        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        templateEngine.setTemplateResolver(templateResolver());
	        return templateEngine;
	 }

	 @Bean
	 public ThymeleafViewResolver viewResolver(){
	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	        viewResolver.setTemplateEngine(templateEngine());
	        return viewResolver;
	 }
	 
	 @Bean
	 public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	 
	        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
	        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
	        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
	        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
	 
	        return dataSource;
	 }
	 
	 @Bean
	 public LocalSessionFactoryBean sessionFactory() {
	        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
	        sessionFactoryBean.setDataSource(dataSource());
	        sessionFactoryBean.setPackagesToScan(env.getRequiredProperty(
	PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
	        sessionFactoryBean.setHibernateProperties(hibProperties());
	        return sessionFactoryBean;
	 }
	 
	 private Properties hibProperties() {
	        Properties properties = new Properties();
	        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
	        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
	        //��һ�����״���mysql�������ݱ��ǲ�ʹ�ã�֮��ע�͵�����Ȼÿ������hibernate�Ὣ���ݱ�ɾ��
	        //properties.put(PROPERTY_NAME_HIBERNATE_HBMDDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBMDDL_AUTO));
	        return properties;  
	 }
	 
	 @Bean
	 public HibernateTransactionManager transactionManager() {
	        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
	        transactionManager.setSessionFactory(sessionFactory().getObject());
	        return transactionManager;
	 }
	 
	 //jsonת��
	 @Bean
	 public MappingJackson2HttpMessageConverter mjsoncon() {
		    MappingJackson2HttpMessageConverter mjmc = new MappingJackson2HttpMessageConverter();
		    List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
	        supportedMediaTypes.add(new MediaType("application", "json"));
		    mjmc.setSupportedMediaTypes(supportedMediaTypes);
		    return mjmc;
	 }
	 
	 @Override
	 public void configureMessageConverters(List converters) {
	        converters.add(mjsoncon());
	 }
	 
	 //�첽����
	 @Bean
	 public ThreadPoolTaskExecutor mvcTaskExecutor(){
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(10);
	        executor.setQueueCapacity(100);
	        executor.setMaxPoolSize(25);
	        executor.setKeepAliveSeconds(30);
	        return executor;

	 }
	 
	 @Override
	 public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
	       configurer.setDefaultTimeout(30*1000L); //tomcatĬ��10��
	       configurer.setTaskExecutor(mvcTaskExecutor());//��������TaskExecutor
	 }
	 
	 @Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
	        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
	 }

}
