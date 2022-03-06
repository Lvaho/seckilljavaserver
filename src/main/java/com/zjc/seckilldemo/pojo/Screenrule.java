package com.zjc.seckilldemo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_screenrule")
public class Screenrule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String screenRule;


}
