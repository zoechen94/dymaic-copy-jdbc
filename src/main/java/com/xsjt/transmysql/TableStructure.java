package com.xsjt.transmysql;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: 喜🐑
 * @create: 2018-12-10 00:09
 **/
@Setter
@Getter
public class TableStructure {
    private String  Field;
    private  String type;
    private String Null;
    private String key;
    private String Default;
    private String Extra;
}
