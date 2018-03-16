package com.appleyk.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.appleyk.config.MasterConfig;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;


/**
 * SqlSessionFactoryBuilder：build方法创建SqlSessionFactory实例。
 * SqlSessionFactory：创建SqlSession实例的工厂。
 * SqlSession：用于执行持久化操作的对象，类似于jdbc中的Connection。
 * SqlSessionTemplate：MyBatis提供的持久层访问模板化的工具，线程安全，可通过构造参数或依赖注入SqlSessionFactory实例
 * 
 * 主库的数据源模板，应用在主库所对应的Dao层上（扫描对应的mapper），实现主数据源的指定+增删改查
 * @author yukun24@126.com
 * @blob   http://blog.csdn.net/appleyk
 * @date   2018年3月16日-下午1:08:53
 */
@Configuration   // ---> 标注此注解，Spring—Boot启动时，会自动进行相应的主数据源配置 -->注入Bean
@MapperScan(basePackages = "com.appleyk.mapper.master", sqlSessionTemplateRef = "masterSqlSessionTemplate")  
public class MasterDBSource {
	
	// 配置主数据源
	@Primary
	@Bean(name = "MasterDB")
	public DataSource testDataSource(MasterConfig masterConfig) throws SQLException {
		
		/**
		 * MySql数据库驱动 实现 XADataSource接口
		 */
		MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		mysqlXaDataSource.setUrl(masterConfig.getUrl());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXaDataSource.setPassword(masterConfig.getPassword());
		mysqlXaDataSource.setUser(masterConfig.getUsername());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

//		/**
//		 * Postgresql数据库驱动 实现 XADataSource
//		 * 包 --> org.postgresql.xa.PGXADataSource;
//		 */
//		PGXADataSource pgxaDataSource = new PGXADataSource();
//		pgxaDataSource.setUrl(masterConfig.getUrl());
//		
		/**
		 * 设置分布式-- 主数据源
		 */
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlXaDataSource);
		xaDataSource.setUniqueResourceName("MasterDB");

		xaDataSource.setMinPoolSize(masterConfig.getMinPoolSize());
		xaDataSource.setMaxPoolSize(masterConfig.getMaxPoolSize());
		xaDataSource.setMaxLifetime(masterConfig.getMaxLifetime());
		xaDataSource.setBorrowConnectionTimeout(masterConfig.getBorrowConnectionTimeout());
		xaDataSource.setLoginTimeout(masterConfig.getLoginTimeout());
		xaDataSource.setMaintenanceInterval(masterConfig.getMaintenanceInterval());
		xaDataSource.setMaxIdleTime(masterConfig.getMaxIdleTime());
		xaDataSource.setTestQuery(masterConfig.getTestQuery());
		
		System.err.println("主数据源注入成功.....");
		return xaDataSource;
	}

	@Bean(name = "masterSqlSessionFactory")
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("MasterDB") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Bean(name = "masterSqlSessionTemplate")
	public SqlSessionTemplate masterSqlSessionTemplate(
			@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
