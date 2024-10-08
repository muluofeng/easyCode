package com.muluofeng.easycode.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author xiexingxing
 * @Created by 2020-10-04 09:40.
 */
@Getter
@Setter
public class CodeConfig implements Serializable {
    public String author;
    public String email;
}