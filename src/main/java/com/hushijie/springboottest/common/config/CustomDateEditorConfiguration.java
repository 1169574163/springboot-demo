package com.hushijie.springboottest.common.config;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * CustomDateEditorConfiguration
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/19
 */
@ControllerAdvice
public class CustomDateEditorConfiguration {
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE, false));
        // LocalDateTime
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                return (this.getValue() == null)
                        ? "" : ((LocalDateTime) this.getValue()).format(LOCAL_DATE_TIME);
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(LocalDateTime.parse(text, LOCAL_DATE_TIME));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Could not parse LocalDateTime: " + e.getMessage(), e);
                }
            }
        });
        // LocalDate
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                return (this.getValue() == null)
                        ? "" : ((LocalDate) this.getValue()).format(LOCAL_DATE);
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(LocalDate.parse(text, LOCAL_DATE));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Could not parse LocalDate: " + e.getMessage(), e);
                }
            }
        });
        // LocalTime
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                return (this.getValue() == null)
                        ? "" : ((LocalTime) this.getValue()).format(LOCAL_TIME);
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    this.setValue(LocalTime.parse(text, LOCAL_TIME));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Could not parse LocalTime: " + e.getMessage(), e);
                }
            }
        });
    }
}
