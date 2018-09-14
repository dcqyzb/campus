package com.mit.campus.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.mit.campus.core.base.controller.CampusErrorView;
import com.mit.campus.core.exception.CampusException;
import com.mit.campus.core.exception.CampusExceptionEnum;
import com.mit.campus.core.util.DateUtil;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.regex.Pattern;

@Configuration
public class DefaultWebConfig extends WebMvcConfigurationSupport {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Bean("error")
    public CampusErrorView error() {
        return new CampusErrorView();
    }

    @PostConstruct
    public void addConversionConfig() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
        genericConversionService.addConverter(new StringToDateConverter());
    }

    public class StringToDateConverter implements Converter<String, Date> {

        @Override
        public Date convert(String dateString) {

            String patternDate = "\\d{4}-\\d{1,2}-\\d{1,2}";
            String patternTimeMinutes = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}";
            String patternTimeSeconds = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";

            boolean dateFlag = Pattern.matches(patternDate, dateString);
            boolean timeMinutesFlag = Pattern.matches(patternTimeMinutes, dateString);
            boolean timeSecondsFlag = Pattern.matches(patternTimeSeconds, dateString);

            if (dateFlag) {
                return DateUtil.parseDate(dateString);
            } else if (timeMinutesFlag) {
                return DateUtil.parseTimeMinutes(dateString);
            } else if (timeSecondsFlag) {
                return DateUtil.parseTime(dateString);
            } else {
                throw new CampusException(CampusExceptionEnum.INVLIDE_DATE_STRING);
            }

        }
    }
}


