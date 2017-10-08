package com.kss.persistence;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * <b>功能：</b>持久层映射扫描器扩展<br>
 * <b>版本：</b>1.0；2011-03-14；创建
 */
public class ExtMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor,InitializingBean,ApplicationContextAware {
	private String basePackage;
	private boolean addToConfig=true;
	private SqlSessionFactory sqlSessionFactory;
	private ExtSqlSessionTemplate extSqlSessionTemplate;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	private ApplicationContext applicationContext;
	public void setBasePackage(String basePackage){
		this.basePackage=basePackage;
	}
	public void setAddToConfig(boolean addToConfig){
		this.addToConfig=addToConfig;
	}
	public void setAnnotationClass(Class<? extends Annotation> annotationClass){
		this.annotationClass=annotationClass;
	}
	public void setMarkerInterface(Class<?> superClass){
		this.markerInterface=superClass;
	}
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory=sqlSessionFactory;
	}
	public void setSqlSessionTemplate(ExtSqlSessionTemplate extSqlSessionTemplate){
		this.extSqlSessionTemplate=extSqlSessionTemplate;
	}
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
	}
	public void afterPropertiesSet()throws Exception{
		Assert.notNull(this.basePackage,"Property 'basePackage' is required");
	}
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){
	}
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry)throws BeansException {
		Scanner scanner=new Scanner(beanDefinitionRegistry);
		scanner.setResourceLoader(this.applicationContext);
		scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
	private final class Scanner extends ClassPathBeanDefinitionScanner {
		public Scanner(BeanDefinitionRegistry registry){
			super(registry);
		}
		@Override
		protected void registerDefaultFilters(){
			boolean acceptAllInterfaces=true;
			if(ExtMapperScannerConfigurer.this.annotationClass!=null){
				addIncludeFilter(new AnnotationTypeFilter(ExtMapperScannerConfigurer.this.annotationClass));
				acceptAllInterfaces=false;
			}
			if(ExtMapperScannerConfigurer.this.markerInterface!=null){
				addIncludeFilter(new AssignableTypeFilter(ExtMapperScannerConfigurer.this.markerInterface){
					@Override
					protected boolean matchClassName(String className){
						return false;
					}
				});
				acceptAllInterfaces=false;
			}
			if(acceptAllInterfaces){
				addIncludeFilter(new TypeFilter(){
					public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)throws IOException{
						return true;
					}
				});
			}
			addExcludeFilter(new TypeFilter(){
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)throws IOException{
					ClassMetadata classMetadata=metadataReader.getClassMetadata();
					Class<?> candidateClass=null;
					try{
						candidateClass=getClass().getClassLoader().loadClass(classMetadata.getClassName());
					}catch(ClassNotFoundException e){
						return false;
					}
					return candidateClass.getMethods().length==0?true:false;
				}
			});
		}
		@Override
		protected Set<BeanDefinitionHolder> doScan(String... basePackages){
			Set<BeanDefinitionHolder> beanDefinitions=super.doScan(basePackages);
			if(beanDefinitions.isEmpty()){
				logger.warn("No MyBatis mapper was found in '"+ExtMapperScannerConfigurer.this.basePackage+"' package. Please check your configuration.");
			}else{
				for(BeanDefinitionHolder holder:beanDefinitions){
					GenericBeanDefinition definition=(GenericBeanDefinition)holder.getBeanDefinition();
					logger.debug("Creating MapperFactoryBean with name '"+holder.getBeanName()+"' and '"+definition.getBeanClassName()+"' mapperInterface");
					definition.getPropertyValues().add("mapperInterface",definition.getBeanClassName());
					definition.setBeanClass(ExtMapperFactoryBean.class);
					definition.getPropertyValues().add("addToConfig",ExtMapperScannerConfigurer.this.addToConfig);
					if(ExtMapperScannerConfigurer.this.sqlSessionFactory!=null)definition.getPropertyValues().add("sqlSessionFactory",ExtMapperScannerConfigurer.this.sqlSessionFactory);
					if(ExtMapperScannerConfigurer.this.extSqlSessionTemplate!=null)definition.getPropertyValues().add("sqlSessionTemplate",ExtMapperScannerConfigurer.this.extSqlSessionTemplate);
				}
			}
			return beanDefinitions;
		}
		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition){
			return(beanDefinition.getMetadata().isInterface()&&beanDefinition.getMetadata().isIndependent());
		}
		@Override
		protected boolean checkCandidate(String beanName,BeanDefinition beanDefinition)throws IllegalStateException{
			if(super.checkCandidate(beanName,beanDefinition)){
				return true;
			}else{
				logger.warn("Skipping MapperFactoryBean with name '"+beanName+"' and '"+beanDefinition.getBeanClassName()+"' mapperInterface"+". Bean already defined with the same name!");
				return false;
			}
		}
	}
}