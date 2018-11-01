package de.ba.bub.studisu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


/**
 * copied from jobsuche
 * ties @Autowired Validator to this Instance
 * otherwise we get 
 */
@Configuration
public class ValidatorConfiguration {

    /**
     * called by spring framework to get validation factory
     * @return
     */
    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}
